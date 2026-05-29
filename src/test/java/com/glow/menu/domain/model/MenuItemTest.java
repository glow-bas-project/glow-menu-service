package com.glow.menu.domain.model;

import com.glow.menu.domain.shared.DomainException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MenuItemTest {

    @ParameterizedTest
    @NullAndEmptySource
    public void build_rejectsBlankRestaurantId(String restaurantId) {
        // given
        var builder = validBuilder().restaurantId(restaurantId);

        // when
        var ex = assertThrows(DomainException.class, builder::build);

        // then
        assertEquals("Menu item restaurant ID cannot be null or empty", ex.getMessage());
    }

    @ParameterizedTest
    @NullAndEmptySource
    public void build_rejectsBlankName(String name) {
        // given
        var builder = validBuilder().name(name);

        // when
        var ex = assertThrows(DomainException.class, builder::build);

        // then
        assertEquals("Menu item name cannot be null or empty", ex.getMessage());
    }

    @Test
    public void build_rejectsNullPrice() {
        // given
        var builder = validBuilder().price(null);

        // when
        var ex = assertThrows(DomainException.class, builder::build);

        // then
        assertEquals("Menu item price cannot be null", ex.getMessage());
    }

    @Test
    public void build_rejectsNegativePrice() {
        // given
        var builder = validBuilder().price(-1);

        // when
        var ex = assertThrows(DomainException.class, builder::build);

        // then
        assertEquals("Menu item price cannot be negative", ex.getMessage());
    }

    @Test
    public void build_rejectsNullCategory() {
        // given
        var builder = validBuilder().category(null);

        // when
        var ex = assertThrows(DomainException.class, builder::build);

        // then
        assertEquals("Menu item category cannot be null", ex.getMessage());
    }

    @Test
    public void build_createsMenuItemWithValidValues() {
        // given
        var builder = validBuilder();

        // when
        var menuItem = builder.build();

        // then
        assertNotNull(menuItem.getId());
        assertNotNull(menuItem.getCreatedAt());
        assertNotNull(menuItem.getUpdatedAt());
        assertEquals("restaurant-123", menuItem.getRestaurantId());
        assertEquals("Pizza Margherita", menuItem.getName());
        assertEquals("Classic tomato and mozzarella pizza", menuItem.getDescription());
        assertEquals(1299, menuItem.getPrice());
        assertEquals(FoodCategory.PIZZA, menuItem.getCategory());
        assertEquals(2, menuItem.getIngredients().size());
    }

    @Test
    public void build_initializesEmptyIngredientsWhenMissing() {
        // given
        var builder = validBuilder().ingredients(null);

        // when
        var menuItem = builder.build();

        // then
        assertNotNull(menuItem.getIngredients());
        assertEquals(0, menuItem.getIngredients().size());
    }

    private static MenuItem.Builder validBuilder() {
        return MenuItem.builder()
            .restaurantId("restaurant-123")
            .name("Pizza Margherita")
            .description("Classic tomato and mozzarella pizza")
            .price(1299)
            .category(FoodCategory.PIZZA)
            .ingredients(java.util.List.of("tomato", "mozzarella"));
    }
}
