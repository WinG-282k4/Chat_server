package com.example.chatserver.user;

import com.example.chatserver.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Log log = LogFactory.getLog(UserController.class);
    @Autowired
    private final UserService userService;

    @Autowired
    private final UserRepository Repository;

    @Autowired
    private final UserMapper userMapper;

//    @GetMapping
//    public List<UserDTO> getUsers() {
//        return Repository.findAll().stream()
//                .map(userMapper::toDto)
//                .toList();
//    }

    // Thêm user mới qua WebSocket khi user kết nối
    @MessageMapping("/user.connect")
    @SendTo("/users/topic")
    public UserDTO connect(
//            @AuthenticationPrincipal UserPrincipal user
            Principal principal
    ) {
        //Get userPrincipal from principal
        Authentication auth = (Authentication) principal;
        UserPrincipal user = (UserPrincipal) auth.getPrincipal();
        UserDTO ConnectUser = userService.connect(user.getUsername());
        return ConnectUser;
    }

    //Disconnect user khi user ngắt kết nối
    @MessageMapping("/user.disconnectUser")
    @SendTo("/users/topic")
    public UserDTO disconnectUser(
//            @AuthenticationPrincipal UserPrincipal user
            Principal principal
    ) {
        //Get userPrincipal from principal
        Authentication auth = (Authentication) principal;
        UserPrincipal user = (UserPrincipal) auth.getPrincipal();
        UserDTO Duser = userService.disconnect(user.getUsername());
        return UserDTO.builder()
                .userId(Duser.getUserId())
                .name(Duser.getName())
                .build();
    }

    @GetMapping("/user")
    public ResponseEntity<List<UserDTO>> findConnectedUsers() {
        List<User> connectedUsers = userService.findConnectedUsers();
        List<UserDTO> userDTOs = connectedUsers.stream()
                .map(userMapper::toDto)
                .toList();
        return ResponseEntity.ok(userDTOs);
    }

    @MessageExceptionHandler
    @SendToUser("/queue/errors") // Gửi tin nhắn lỗi CHỈ cho người dùng đã gửi
    public String handleException(MethodArgumentNotValidException exception) {

        // Trả về một tin nhắn lỗi thân thiện cho client
        return "Lỗi: Nội dung tin nhắn không hợp lệ hoặc bị rỗng.";
    }

    @GetMapping("/myinfo")
    public ResponseEntity<Map<String, Object>> getUsers(@AuthenticationPrincipal UserPrincipal userPrincipal) {

        User user = Repository.findByUsername(userPrincipal.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Dùng builder để tạo UserDTO
        UserDTO userDTO = UserDTO.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .name(user.getName())
                .email(user.getEmail())
                .verify(user.getVerify())
                .build();

        return ResponseEntity.ok(Map.of(
                "status", "successful",
                "data", userDTO
        ));
    }

    @PostMapping("/update")
    public ResponseEntity<UserDTO> updateUser(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody UserDTO userDTO) {
        userDTO.setUsername(userPrincipal.getUsername());
        UserDTO updatedUser = userService.updateUser(userDTO);
//        System.out.println(updatedUser);
        return ResponseEntity.ok(updatedUser);
    }

    @PostMapping("change-password")
    public ResponseEntity changePassword(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody ChangePasswordRequest changePasswordRequest) {

        String sub = userPrincipal.getUsername();
        boolean result = userService.changePassword(sub, changePasswordRequest.getOldPassword(), changePasswordRequest.getNewPassword());
        if (result) {
            return ResponseEntity.ok(
                    Map.of(
                            "Status", "Change pass successful"
                    )
            );
        } else {
            return ResponseEntity.ok(
                    Map.of(
                            "Status","Change pass failed"
                    )
            );
        }
    }

}
