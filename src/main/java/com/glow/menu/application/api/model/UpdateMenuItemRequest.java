package com.glow.menu.application.api.model;

import com.glow.menu.domain.model.FoodCategory;

import java.util.List;

public record UpdateMenuItemRequest(
    String restaurantId,
    String name,
    String description,
    Integer price,
    FoodCategory category,
    List<String> ingredients) {
}