package com.glow.menu.application.api.model;

import java.time.Instant;

public record ApiError(
    String code,
    String message,
    int status,
    Instant timestamp) {
}
