package com.slinkdigital.user.controller;

import com.slinkdigital.user.dto.ApiResponse;
import com.slinkdigital.user.dto.RefreshTokenRequest;
import com.slinkdigital.user.dto.UpdatePasswordRequest;
import com.slinkdigital.user.dto.UserDto;
import com.slinkdigital.user.dto.UserRequest;
import com.slinkdigital.user.security.JwtProvider;
import com.slinkdigital.user.service.UserService;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.jupiter.api.Disabled;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 *
 * @author TEGA
 */
@WebMvcTest(controllers = UserController1.class)
public class UserController1IT {
    
    @MockBean
    private UserService userServiceImpl;
    
    @MockBean
    private UserDetailsService userDetailsService;
    
    @MockBean
    private JwtProvider jwtProvider;
    
    @Autowired
    private MockMvc mockMvc;

    /**
     * Test of getAllUsers method, of class UserController1.
     * @throws java.lang.Exception
     */
    @Test
    @Disabled
    public void testGetAllUsers() throws Exception {
        UserDto user1 = new UserDto(1L,"d@gmail.com",new HashSet<>());
        UserDto user2 = new UserDto(2L,"da@gmail.com",new HashSet<>());
        Mockito.when(userServiceImpl.getAllUsers()).thenReturn(Arrays.asList(user1, user2));
//        List<UserDto> users = userServiceImpl.getAllUsers();
        mockMvc.perform(MockMvcRequestBuilders.get("api/us/v1/users"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    /**
     * Test of searchUser method, of class UserController1.
     */
    @Test
    @Disabled
    public void testSearchUser() {
        System.out.println("searchUser");
        String email = "";
        UserController1 instance = null;
        ResponseEntity<ApiResponse> expResult = null;
        ResponseEntity<ApiResponse> result = instance.searchUser(email);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updatePassword method, of class UserController1.
     */
    @Test
    @Disabled
    public void testUpdatePassword() {
        System.out.println("updatePassword");
        UpdatePasswordRequest updatePasswordRequest = null;
        UserController1 instance = null;
        ResponseEntity<ApiResponse> expResult = null;
        ResponseEntity<ApiResponse> result = instance.updatePassword(updatePasswordRequest);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of sendMessageToAdmin method, of class UserController1.
     */
    @Test
    @Disabled
    public void testSendMessageToAdmin() {
        System.out.println("sendMessageToAdmin");
        UserRequest userRequest = null;
        UserController1 instance = null;
        ResponseEntity<ApiResponse> expResult = null;
        ResponseEntity<ApiResponse> result = instance.sendMessageToAdmin(userRequest);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addRoleCouple method, of class UserController1.
     */
    @Test
    @Disabled
    public void testAddRoleCouple() {
        System.out.println("addRoleCouple");
        List<Long> couple = null;
        UserController1 instance = null;
        ResponseEntity<Boolean> expResult = null;
        ResponseEntity<Boolean> result = instance.addRoleCouple(couple);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of logout method, of class UserController1.
     */
    @Test
    @Disabled
    public void testLogout() {
        System.out.println("logout");
        RefreshTokenRequest refreshTokenRequest = null;
        UserController1 instance = null;
        ResponseEntity<ApiResponse> expResult = null;
        ResponseEntity<ApiResponse> result = instance.logout(refreshTokenRequest);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of handleError method, of class UserController1.
     */
    @Test
    @Disabled
    public void testHandleError() {
        System.out.println("handleError");
        HttpServletRequest req = null;
        Exception ex = null;
        UserController1 instance = null;
        ApiResponse expResult = null;
        ApiResponse result = instance.handleError(req, ex);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
