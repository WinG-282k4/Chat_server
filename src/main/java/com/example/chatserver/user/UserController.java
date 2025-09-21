package com.example.chatserver.user;

import com.example.chatserver.common.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping("/update")
    public ApiResponse updateUser(HttpServletRequest request, @RequestBody UserDTO userDTO) {
        String payload = request.getAttribute("jwtPayload") != null ? (String) request.getAttribute("jwtPayload") : null;
        String sub = null;
        if (payload != null) {
            try {
                // Extract 'sub' from payload
                String[] parts = payload.split(",");
                for (String part : parts) {
                    if (part.contains("\"sub\"")) {
                        sub = part.split(":")[1].replaceAll("[\"}]", "").trim();
                        break;
                    }
                }
                System.out.println("Extracted sub: " + sub);
            } catch (Exception e) {
                return new ApiResponse("error", "Invalid payload format", null);
            }
        }
        userDTO.setUsername(sub);
        UserDTO updatedUser = userService.updateUser(userDTO);
//        System.out.println(updatedUser);
        return new ApiResponse("success", "User updated successfully", updatedUser);
    }

    @PostMapping("change-password")
    public ApiResponse changePassword(HttpServletRequest request, @RequestBody ChangePasswordRequest changePasswordRequest) {
        String payload = request.getAttribute("jwtPayload") != null ? (String) request.getAttribute("jwtPayload") : null;
        String sub = null;
        if (payload != null) {
            try {
                // Extract 'sub' from payload
                String[] parts = payload.split(",");
                for (String part : parts) {
                    if (part.contains("\"sub\"")) {
                        sub = part.split(":")[1].replaceAll("[\"}]", "").trim();
                        break;
                    }
                }
                System.out.println("Extracted sub: " + sub);
            } catch (Exception e) {
                return new ApiResponse("error", "Invalid payload format", null);
            }
        }
        boolean result = userService.changePassword(sub, changePasswordRequest.getOldPassword(), changePasswordRequest.getNewPassword());
        if (result) {
            return new ApiResponse("success", "Password changed successfully", null);
        } else {
            return new ApiResponse("error", "Failed to change password", null);
        }
    }
}
