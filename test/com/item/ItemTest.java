package com.item;

import org.junit.Test;

import static org.junit.Assert.*;

public class ItemTest {

    Item item1;
    Item item2;

    @Test
    public void testGetLocationPositiveNameLocation() {
        item1 = new Item("spork", "dock");
        assertEquals("spork", item1.getName());
        assertEquals("dock", item1.getLocation());
    }

    public void testGetNameLocationWithNulls() {
        item1 = new Item(null, null);
        assertEquals(null, item1.getLocation());
        assertEquals(null, item1.getName());
    }
}