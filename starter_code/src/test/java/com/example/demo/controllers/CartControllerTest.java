package com.example.demo.controllers;

import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CartControllerTest {

    private CartController cartController;
    // TODO
    private UserRepository userRepository;
    private CartRepository cartRepository;
    private ItemRepository itemRepository;

    @Before
    public void setUp() {
        //
    }

    @Test
    public void addToCartHappyFlow() {
        // ModifyCartRequest
    }

    @Test
    public void addToCartNagUserNotFound() {
        //
    }

    @Test
    public void addToCartNagItemNotFound() {
        //
    }

    @Test
    public void removeFromCartHappyFlow() {
        //
    }

    @Test
    public void removeFromCartNagUserNotFound() {
        //
    }

    @Test
    public void removeFromCartNagItemNotFound() {
        //
    }
}
