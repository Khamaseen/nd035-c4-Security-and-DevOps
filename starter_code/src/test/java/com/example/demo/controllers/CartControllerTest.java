package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class CartControllerTest {

    private CartController cartController;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        this.cartController = new CartController();
        TestUtils.injectObject(this.cartController, "userRepository", this.userRepository);
        TestUtils.injectObject(this.cartController, "cartRepository", this.cartRepository);
        TestUtils.injectObject(this.cartController, "itemRepository", this.itemRepository);
    }

    @Test
    public void addToCartHappyFlow() {
        final String userName = "UserName";
        final long userId = 53L;
        final long itemId = 4L;
        final int itemQuantity = 8;
        final BigDecimal itemPrice = BigDecimal.valueOf(4.5);

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername(userName);
        modifyCartRequest.setItemId(itemId);
        modifyCartRequest.setQuantity(itemQuantity);

        Cart cart = new Cart();

        User user = new User();
        user.setUsername(userName);
        user.setId(userId);
        user.setCart(cart);

        Item item = new Item();
        item.setPrice(itemPrice);

        when(this.userRepository.findByUsername(userName)).thenReturn(user);
        when(this.itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        final ResponseEntity<Cart> cartResponseEntity = this.cartController.addTocart(modifyCartRequest);

        assertNotNull(cartResponseEntity);
        assertEquals(200, cartResponseEntity.getStatusCodeValue());

        final Cart cartByResponse = cartResponseEntity.getBody();

        assertNotNull(cartByResponse);
        assertEquals(cartByResponse.getItems().size(), itemQuantity);
    }

    @Test
    public void addToCartNagUserNotFound() {
        final String userName = "UserName";
        final long itemId = 4L;
        final int itemQuantity = 8;

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername(userName);
        modifyCartRequest.setItemId(itemId);
        modifyCartRequest.setQuantity(itemQuantity);

        when(this.userRepository.findByUsername(userName)).thenReturn(null);

        final ResponseEntity<Cart> cartResponseEntity = this.cartController.addTocart(modifyCartRequest);

        assertNotNull(cartResponseEntity);
        assertEquals(404, cartResponseEntity.getStatusCodeValue());

        final Cart cartByResponse = cartResponseEntity.getBody();

        assertNull(cartByResponse);
    }

    @Test
    public void addToCartNagItemNotFound() {
        final String userName = "UserName";
        final long userId = 53L;
        final long itemId = 4L;

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername(userName);

        User user = new User();
        user.setUsername(userName);
        user.setId(userId);


        when(this.userRepository.findByUsername(userName)).thenReturn(user);
        when(this.itemRepository.findById(itemId)).thenReturn(Optional.ofNullable(null));

        final ResponseEntity<Cart> cartResponseEntity = this.cartController.addTocart(modifyCartRequest);

        assertNotNull(cartResponseEntity);
        assertEquals(404, cartResponseEntity.getStatusCodeValue());

        final Cart cartByResponse = cartResponseEntity.getBody();

        assertNull(cartByResponse);
    }

    @Test
    public void removeFromCartHappyFlow() {
        // 60 % reached
    }

    @Test
    public void removeFromCartNagUserNotFound() {
        // 60 % reached
    }

    @Test
    public void removeFromCartNagItemNotFound() {
        // 60 % reached
    }
}
