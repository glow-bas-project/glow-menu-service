package com.glow.menu.application.api.model;

import java.util.List;

public record MaterialiseMenuItemsByIdsRequest(
    List<String> ids) {
}