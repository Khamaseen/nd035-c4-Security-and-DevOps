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

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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

        verify(this.bCryptPasswordEncoder, times(1)).encode(password);

    }

    @Test
    public void createUserNagFlowPasswordLessCharacters() {
        final String password = "pas123";
        final String userName = "userName1234";

        CreateUserRequest r = new CreateUserRequest();
        r.setConfirmPassword(password);
        r.setPassword(password);
        r.setUsername(userName);

        final ResponseEntity<User> createdUserResponse = this.userController.createUser(r);

        assertNotNull(createdUserResponse);
        assertEquals(400, createdUserResponse.getStatusCodeValue());

        final User createdUser = createdUserResponse.getBody();

        assertNull(createdUser);
        verify(this.bCryptPasswordEncoder, times(0)).encode(password);
    }

    @Test
    public void findByIdHappyFlow() {
        final String password = "password1234";
        final String userName = "userName1234";
        final String hashedByBCrypt = "hashedYeey";

        final Long firstIdToBeCreated = 0L;

        when(this.bCryptPasswordEncoder.encode(password)).thenReturn(hashedByBCrypt);

        User mockUser = new User();
        mockUser.setUsername(userName);
        mockUser.setPassword(password);
        mockUser.setId(firstIdToBeCreated);
        when(this.userRepository.findById(firstIdToBeCreated)).thenReturn(Optional.of(mockUser));

        CreateUserRequest r = new CreateUserRequest();
        r.setConfirmPassword(password);
        r.setPassword(password);
        r.setUsername(userName);

        final ResponseEntity<User> createdUserResponse = this.userController.createUser(r);

        assertNotNull(createdUserResponse);
        assertEquals(200, createdUserResponse.getStatusCodeValue());

        final User createdUser = createdUserResponse.getBody();

        assertNotNull(createdUser);

        final ResponseEntity<User> findByIdUserResponse = this.userController.findById(createdUser.getId());

        assertNotNull(findByIdUserResponse);
        assertEquals(200, findByIdUserResponse.getStatusCodeValue());

        final User findByIdUser = findByIdUserResponse.getBody();

        assertNotNull(findByIdUser);
        assertEquals(findByIdUser.getUsername(), mockUser.getUsername());
        assertEquals(findByIdUser.getId(), mockUser.getId());
    }

    @Test
    public void findByUserNameHappyFlow() {
        final String password = "password1234";
        final String userName = "userName1234";
        final String hashedByBCrypt = "hashedYeey";

        final Long firstIdToBeCreated = 0L;

        when(this.bCryptPasswordEncoder.encode(password)).thenReturn(hashedByBCrypt);

        User mockUser = new User();
        mockUser.setUsername(userName);
        mockUser.setPassword(password);
        mockUser.setId(firstIdToBeCreated);
        when(this.userRepository.findByUsername(userName)).thenReturn(mockUser);

        CreateUserRequest r = new CreateUserRequest();
        r.setConfirmPassword(password);
        r.setPassword(password);
        r.setUsername(userName);

        final ResponseEntity<User> createdUserResponse = this.userController.createUser(r);

        assertNotNull(createdUserResponse);
        assertEquals(200, createdUserResponse.getStatusCodeValue());

        final User createdUser = createdUserResponse.getBody();

        assertNotNull(createdUser);

        final ResponseEntity<User> findByIdUserResponse = this.userController.findByUserName(createdUser.getUsername());

        assertNotNull(findByIdUserResponse);
        assertEquals(200, findByIdUserResponse.getStatusCodeValue());

        final User findByIdUser = findByIdUserResponse.getBody();

        assertNotNull(findByIdUser);
        assertEquals(findByIdUser.getUsername(), mockUser.getUsername());
        assertEquals(findByIdUser.getId(), mockUser.getId());
    }

}
