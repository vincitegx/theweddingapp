package com.slinkdigital.user.controller;

import com.slinkdigital.user.dto.ApiResponse;
import com.slinkdigital.user.dto.RefreshTokenRequest;
import com.slinkdigital.user.dto.UpdatePasswordRequest;
import com.slinkdigital.user.dto.UserDto;
import com.slinkdigital.user.dto.UserRequest;
import com.slinkdigital.user.service.UserService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import static org.springframework.http.HttpStatus.OK;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author TEGA
 */
@RestController
@RequestMapping("api/us/v1/users")
@RequiredArgsConstructor
public class UserController1 {

    private final UserService userService;

    @GetMapping //admin
    public ResponseEntity<ApiResponse> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("users", users))
                        .message("List Of Users")
                        .status(OK)
                        .build()
        );
    }

    @GetMapping("{email}/search") //admin
    public ResponseEntity<ApiResponse> searchUser(@PathVariable String email) {
        UserDto user = userService.searchUser(email);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("user", user))
                        .message("Success")
                        .status(OK)
                        .build()
        );
    }

    @PutMapping("psw")
    public ResponseEntity<ApiResponse> updatePassword(@RequestBody UpdatePasswordRequest updatePasswordRequest) {
        Map<String, String> msgStatus = userService.updatePassword(updatePasswordRequest);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("isPasswordUpdated", true))
                        .message(msgStatus.get("success"))
                        .status(OK)
                        .build()
        );
    }

    @PostMapping("message-admin")
    public ResponseEntity<ApiResponse> sendMessageToAdmin(@RequestBody UserRequest userRequest) {
        Map<String, String> msgStatus = userService.sendMessageToAdmin(userRequest);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("isMessageSent", true))
                        .message(msgStatus.get("success"))
                        .status(OK)
                        .build()
        );
    }

    @PostMapping("roles/couple")
    public ResponseEntity<Boolean> addRoleCouple(@RequestParam("couple") List<Long> couple) {
        userService.addRoleCouple(couple);
        return ResponseEntity.ok(true);
    }

    @PostMapping("logout")
    public ResponseEntity<ApiResponse> logout(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        boolean isLoggedOut = userService.logout(refreshTokenRequest);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("isLoggedOut", isLoggedOut))
                        .message("Logout Successful !!!")
                        .status(OK)
                        .build()
        );
    }
}
