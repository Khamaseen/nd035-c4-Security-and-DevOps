package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private UserController userController;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        this.userController = new UserController();
        TestUtils.injectObject(this.userController, "userRepository" , this.userRepository);
        TestUtils.injectObject(this.userController, "cartRepository", this.cartRepository);
        TestUtils.injectObject(this.userController, "bCryptPasswordEncoder", this.bCryptPasswordEncoder);
    }

    @Test
    public void createUserHappyFlow() {
        final String password = "password1234";
        final String userName = "userName1234";
        final String hashedByBCrypt = "hashedYeey";

        when(this.bCryptPasswordEncoder.encode(password)).thenReturn(hashedByBCrypt);

        CreateUserRequest r = new CreateUserRequest();
        r.setConfirmPassword(password);
        r.setPassword(password);
        r.setUsername(userName);

        final ResponseEntity<User> createdUserResponse = this.userController.createUser(r);

        assertNotNull(createdUserResponse);
        assertEquals(200, createdUserResponse.getStatusCodeValue());

        final User createdUser = createdUserResponse.getBody();

        assertNotNull(createdUser);
        assertNotNull(createdUser.getId());
        assertEquals(createdUser.getUsername(), userName);
        assertEquals(createdUser.getPassword(), hashedByBCrypt);
    }
}
