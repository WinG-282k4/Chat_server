package com.example.chatserver.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Component;

@Component
public class UserDTO {

    private Long userId;

    @NotBlank
    private String username;

    @NotBlank
    private String name;

    @NotBlank
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Email(message = "Email should be valid")
    private String email;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean verify;

    // Constructors
    public UserDTO() {
    }

    public UserDTO(Long userId, String username, String name, String password, String email, Boolean verify) {
        this.userId = userId;
        this.username = username;
        this.name = name;
        this.password = password;
        this.email = email;
        this.verify = verify;
    }

    // Getters and Setters


    public @NotBlank String getName() {
        return name;
    }

    public void setName(@NotBlank String name) {
        this.name = name;
    }

    public Boolean getVerify() {
        return verify;
    }

    public void setVerify(Boolean verify) {
        this.verify = verify;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
