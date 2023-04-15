package com.slinkdigital.user.controller;

import com.slinkdigital.user.dto.RefreshTokenRequest;
import com.slinkdigital.user.dto.UpdatePasswordRequest;
import com.slinkdigital.user.dto.UserDto;
import com.slinkdigital.user.dto.UserRequest;
import com.slinkdigital.user.service.AdminService;
import com.slinkdigital.user.service.AuthService;
import com.slinkdigital.user.service.PasswordService;
import com.slinkdigital.user.service.RoleService;
import com.slinkdigital.user.service.UserService;
import java.util.List;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
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
    private final PasswordService passwordService;
    private final RoleService roleService;
    private final AuthService authService;
    private final AdminService adminService;

    @GetMapping //admin
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @PutMapping("psw")
    @ResponseStatus(HttpStatus.OK)
    public void updatePassword(@RequestBody UpdatePasswordRequest updatePasswordRequest) {
        passwordService.updatePassword(updatePasswordRequest);
    }

    @PostMapping("message-admin")
    @ResponseStatus(HttpStatus.OK)
    public void sendMessageToAdmin(@RequestBody UserRequest userRequest) {
        adminService.sendMessageToAdmin(userRequest);
    }

    @PostMapping("roles/couple")
    @ResponseStatus(HttpStatus.OK)
    public void addRoleCouple(@RequestParam("couple") List<Long> couple) {
        roleService.addRoleCouple(couple);
    }

    @PostMapping("logout")
    @ResponseStatus(HttpStatus.OK)
    public void logout(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        authService.logout(refreshTokenRequest);
    }
}
