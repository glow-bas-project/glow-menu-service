package com.glow.menu.infrastructure.services;

import com.glow.menu.domain.model.FoodCategory;
import com.glow.menu.domain.model.MenuItem;
import com.glow.menu.domain.shared.PageRequest;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
public class MenuItemRepositoryServiceIntegrationTest {

    @Inject
    MenuItemRepositoryService repository;

    @Test
    @Transactional
    public void saveAndFindById_roundTrips() {
        // given
        var menuItem = newMenuItem("restaurant-1", "Pizza Margherita", 1299);

        // when
        repository.save(menuItem);
        var id = menuItem.getId().toString();
        var found = repository.findById(id);

        // then
        assertTrue(found.isPresent());
        assertEquals("restaurant-1", found.get().getRestaurantId());
        assertEquals("Pizza Margherita", found.get().getName());
        assertEquals(1299, found.get().getPrice());
    }

    @Test
    @Transactional
    public void findIds_returnsPersistedId() {
        // given
        var menuItem = newMenuItem("restaurant-2", "Pizza Funghi", 1399);
        repository.save(menuItem);
        var id = menuItem.getId().toString();

        // when
        var page = repository.findIds(new PageRequest(10, 0), "restaurant-2");

        // then
        assertTrue(page.result().contains(id));
    }

    @Test
    @Transactional
    public void findByRestaurantId_returnsMatchingItems() {
        // given
        var first = newMenuItem("restaurant-3", "Burger", 1199);
        var second = newMenuItem("restaurant-3", "Fries", 499);
        repository.save(first);
        repository.save(second);

        // when
        var items = repository.findByRestaurantId("restaurant-3");

        // then
        assertEquals(2, items.size());
    }

    @Test
    @Transactional
    public void update_modifiesPersistedMenuItem() {
        // given
        var menuItem = newMenuItem("restaurant-4", "Pasta Carbonara", 1499);
        repository.save(menuItem);
        var updated = MenuItem.builder()
            .id(menuItem.getId())
            .createdAt(menuItem.getCreatedAt())
            .restaurantId(menuItem.getRestaurantId())
            .name("Pasta Bolognese")
            .description("Updated pasta dish")
            .price(1599)
            .category(FoodCategory.PASTA)
            .ingredients(List.of("pasta", "tomato", "meat"))
            .build();

        // when
        repository.update(updated);
        var found = repository.findById(menuItem.getId().toString());

        // then
        assertTrue(found.isPresent());
        assertEquals("Pasta Bolognese", found.get().getName());
        assertEquals(1599, found.get().getPrice());
    }

    @Test
    @Transactional
    public void deleteById_removesMenuItem() {
        // given
        var menuItem = newMenuItem("restaurant-5", "Salad", 899);
        repository.save(menuItem);
        var id = menuItem.getId().toString();

        // when
        var deleted = repository.deleteById(id);
        var after = repository.findById(id);

        // then
        assertTrue(deleted);
        assertTrue(after.isEmpty());
    }

    private static MenuItem newMenuItem(String restaurantId, String name, Integer price) {
        return MenuItem.builder()
            .restaurantId(restaurantId)
            .name(name)
            .description(name + " description")
            .price(price)
            .category(FoodCategory.OTHER)
            .ingredients(List.of("ingredient-1", "ingredient-2"))
            .build();
    }
}