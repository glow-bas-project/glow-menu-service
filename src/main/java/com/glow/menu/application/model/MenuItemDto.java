package com.glow.menu.application.model;

import com.glow.menu.domain.model.FoodCategory;

import java.time.Instant;
import java.util.List;

public record MenuItemDto(
    String id,
    String restaurantId,
    String name,
    String description,
    Integer price,
    FoodCategory category,
    List<String> ingredients,
    Instant createdAt,
    Instant updatedAt) {
}