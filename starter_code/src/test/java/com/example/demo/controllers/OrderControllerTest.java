package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class OrderControllerTest {

    private OrderController orderController;
    private OrderRepository orderRepository = mock(OrderRepository.class);
    private UserRepository userRepository = mock(UserRepository.class);

    @Before
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        this.orderController = new OrderController();
        TestUtils.injectObject(this.orderController, "orderRepository", this.orderRepository);
        TestUtils.injectObject(this.orderController, "userRepository", this.userRepository);
    }

    @Test
    public void submitHappyFlow() {
        final String userName = "userName1234";
        final long userId = 0L;

        Cart mockedCart = new Cart();
        mockedCart.setItems(new ArrayList<>());

        User mockedUser = new User();
        mockedUser.setId(userId);
        mockedUser.setCart(mockedCart);

        UserOrder mockedOrder = UserOrder.createFromCart(mockedUser.getCart());

        when(this.userRepository.findByUsername(userName)).thenReturn(mockedUser);

        final ResponseEntity<UserOrder> userOrderResponse = this.orderController.submit(userName);

        verify(this.orderRepository, times(0)).save(mockedOrder);
        assertNotNull(userOrderResponse);
        assertEquals(200, userOrderResponse.getStatusCodeValue());

        final UserOrder userOrder = userOrderResponse.getBody();

        assertNotNull(userOrder);
        assertEquals(userOrder.getItems().size(), 0);
    }

    @Test
    public void submitNagUserNotFound() {
        final String userName = "userName1234";

        final ResponseEntity<UserOrder> userOrderResponse = this.orderController.submit(userName);

        assertNotNull(userOrderResponse);
        assertEquals(404, userOrderResponse.getStatusCodeValue());

        final UserOrder userOrder = userOrderResponse.getBody();

        assertNull(userOrder);
    }

    @Test
    public void getOrdersForUserHappyFlow() {
        final String userName = "userName1234";
        final long userId = 0L;

        User mockedUser = new User();
        mockedUser.setId(userId);

        List<UserOrder> mockedOrder = new ArrayList<>();

        when(this.userRepository.findByUsername(userName)).thenReturn(mockedUser);
        when(this.orderRepository.findByUser(mockedUser)).thenReturn(mockedOrder);

        final ResponseEntity<List<UserOrder>> userOrderListResponse = this.orderController.getOrdersForUser(userName);

        verify(this.orderRepository, times(1)).findByUser(mockedUser);
        assertNotNull(userOrderListResponse);
        assertEquals(200, userOrderListResponse.getStatusCodeValue());

        final List<UserOrder> userOrder = userOrderListResponse.getBody();

        assertNotNull(userOrder);
        assertEquals(userOrder.size(), 0);
    }

    @Test
    public void getOrdersForUserNagUserNotFound() {
        final String userName = "userName1234";
        final long userId = 0L;

        User mockedUser = new User();
        mockedUser.setId(userId);
        mockedUser.setUsername(userName);

        final ResponseEntity<List<UserOrder>> userOrderListResponse = this.orderController.getOrdersForUser(userName);

        verify(this.orderRepository, times(0)).findByUser(mockedUser);
        assertNotNull(userOrderListResponse);
        assertEquals(404, userOrderListResponse.getStatusCodeValue());

        final List<UserOrder> userOrder = userOrderListResponse.getBody();

        assertNull(userOrder);
    }
}
