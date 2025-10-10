package com.example.chatserver.user;

import com.example.chatserver.common.ApiResponse;
import com.example.chatserver.security.UserPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Log log = LogFactory.getLog(UserController.class);
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository Repository;

    @Autowired
    private UserMapper userMapper;

//    @GetMapping
//    public List<UserDTO> getUsers() {
//        return Repository.findAll().stream()
//                .map(userMapper::toDto)
//                .toList();
//    }

    @GetMapping("/myinfo")
    public ApiResponse getUsers(@AuthenticationPrincipal UserPrincipal userPrincipal) {

        User user = Repository.findByUsername(userPrincipal.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Dùng builder để tạo UserDTO
        UserDTO userDTO = UserDTO.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .name(user.getName())
                .password("******")   // ⚠️ thường không nên trả về password trong API
                .email(user.getEmail())
                .verify(user.getVerify())
                .build();

        return new ApiResponse("success", "Fetched users successfully", userDTO);
    }

    @PostMapping("/update")
    public ApiResponse updateUser(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody UserDTO userDTO) {
        userDTO.setUsername(userPrincipal.getUsername());
        UserDTO updatedUser = userService.updateUser(userDTO);
//        System.out.println(updatedUser);
        return new ApiResponse("success", "User updated successfully", updatedUser);
    }

    @PostMapping("change-password")
    public ApiResponse changePassword(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody ChangePasswordRequest changePasswordRequest) {

        String sub = userPrincipal.getUsername();
        boolean result = userService.changePassword(sub, changePasswordRequest.getOldPassword(), changePasswordRequest.getNewPassword());
        if (result) {
            return new ApiResponse("success", "Password changed successfully", null);
        } else {
            return new ApiResponse("error", "Failed to change password", null);
        }
    }

}
