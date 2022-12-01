package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class ItemControllerTest {

    private ItemController itemController;
    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        this.itemController = new ItemController();
        TestUtils.injectObject(this.itemController, "itemRepository", this.itemRepository);
    }

    @Test
    public void getItemsHappyFlow() {
        List<Item> mockedList = new ArrayList<>();

        when(this.itemRepository.findAll()).thenReturn(mockedList);

        final ResponseEntity<List<Item>> itemListResponse = this.itemController.getItems();

        verify(this.itemRepository, times(1)).findAll();
        assertNotNull(itemListResponse);
        assertEquals(200, itemListResponse.getStatusCodeValue());

        List<Item> returnedItems = itemListResponse.getBody();
        assertNotNull(returnedItems);
        assertEquals(returnedItems.size(), 0);
    }

    @Test
    public void getItemByIdHappyFlow() {
        final long itemId = 33L;
        Item mockedItem = new Item();
        mockedItem.setId(itemId);

        when(this.itemRepository.findById(itemId)).thenReturn(Optional.of(mockedItem));

        final ResponseEntity<Item> itemResponse = this.itemController.getItemById(itemId);

        verify(this.itemRepository, times(1)).findById(itemId);
        assertNotNull(itemResponse);
        assertEquals(200, itemResponse.getStatusCodeValue());

        Item returnedItem = itemResponse.getBody();
        assertNotNull(returnedItem);
        assertEquals(returnedItem, mockedItem);
    }

    @Test
    public void getItemByNameHappyFlow() {
        final String itemName = "WrappedCircle";
        Item mockedItem = new Item();
        mockedItem.setName(itemName);
        List<Item> mockedItems = new ArrayList<>();
        mockedItems.add(mockedItem);

        when(this.itemRepository.findByName(itemName)).thenReturn(mockedItems);

        final ResponseEntity<List<Item>> itemListResponse = this.itemController.getItemsByName(itemName);

        verify(this.itemRepository, times(1)).findByName(itemName);
        assertNotNull(itemListResponse);
        assertEquals(200, itemListResponse.getStatusCodeValue());

        List<Item> returnedItems = itemListResponse.getBody();
        assertNotNull(returnedItems);
        assertEquals(returnedItems, mockedItems);
    }

    @Test
    public void getItemByNameHappyFlowNotFound() {
        final String itemName = "WrappedCircle";

        when(this.itemRepository.findByName(itemName)).thenReturn(null);

        final ResponseEntity<List<Item>> itemListResponse = this.itemController.getItemsByName(itemName);

        verify(this.itemRepository, times(1)).findByName(itemName);
        assertNotNull(itemListResponse);
        assertEquals(404, itemListResponse.getStatusCodeValue());

        List<Item> returnedItems = itemListResponse.getBody();
        assertNull(returnedItems);
    }

}
