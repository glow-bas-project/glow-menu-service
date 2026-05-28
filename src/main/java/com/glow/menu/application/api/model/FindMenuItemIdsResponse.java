package com.glow.menu.application.api.model;

import java.util.List;

public record FindMenuItemIdsResponse(
    List<String> menuItemIds,
    int nextPage,
    boolean hasMore) {
}