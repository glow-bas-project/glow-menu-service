package com.glow.menu.application.api.model;

public record FindMenuItemIdsRequest(
    String restaurantId,
    int size,
    int page) {
}