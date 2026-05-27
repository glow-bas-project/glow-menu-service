package com.glow.menu.domain.model;

import com.glow.menu.domain.shared.DomainException;
import com.glow.menu.domain.shared.DomainPrecondition;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class MenuItem {

    private final UUID id;
    private final Instant createdAt;
    private final Instant updatedAt;
    private final String restaurantId;
    private final String name;
    private final String description;
    private final Integer price;
    private final FoodCategory category;
    private final List<String> ingredients;

    private MenuItem(Builder builder) {
        this.id = Objects.isNull(builder.id) ? UUID.randomUUID() : builder.id;
        this.createdAt = Objects.isNull(builder.createdAt) ? Instant.now() : builder.createdAt;
        this.updatedAt = Objects.isNull(builder.updatedAt) ? Instant.now() : builder.updatedAt;
        this.restaurantId = DomainPrecondition.requireNonBlank(builder.restaurantId,
            "Menu item restaurant ID cannot be null or empty");
        this.name = DomainPrecondition.requireNonBlank(builder.name,
            "Menu item name cannot be null or empty");
        this.description = builder.description;
        this.price = DomainPrecondition.requireNonNull(builder.price,
            "Menu item price cannot be null");
        if (price < 0) {
            throw new DomainException("Menu item price cannot be negative");
        }
        this.category = DomainPrecondition.requireNonNull(builder.category,
            "Menu item category cannot be null");
        this.ingredients = builder.ingredients != null ? List.copyOf(builder.ingredients) : List.of();
    }

    public static Builder builder() {
        return new Builder();
    }

    public UUID getId() {
        return id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Integer getPrice() {
        return price;
    }

    public FoodCategory getCategory() {
        return category;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        MenuItem menuItem = (MenuItem) o;
        return Objects.equals(id, menuItem.id) && Objects.equals(createdAt, menuItem.createdAt) &&
            Objects.equals(updatedAt, menuItem.updatedAt) && Objects.equals(restaurantId, menuItem.restaurantId) &&
            Objects.equals(name, menuItem.name) && Objects.equals(description, menuItem.description) &&
            Objects.equals(price, menuItem.price) && category == menuItem.category &&
            Objects.equals(ingredients, menuItem.ingredients);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdAt, updatedAt, restaurantId, name, description, price, category, ingredients);
    }

    public static class Builder {
        private UUID id;
        private Instant createdAt;
        private Instant updatedAt;
        private String restaurantId;
        private String name;
        private String description;
        private Integer price;
        private FoodCategory category;
        private List<String> ingredients;

        public MenuItem build() {
            return new MenuItem(this);
        }

        public UUID getId() {
            return id;
        }

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(Instant updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Builder restaurantId(String restaurantId) {
            this.restaurantId = restaurantId;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder price(Integer price) {
            this.price = price;
            return this;
        }

        public Builder category(FoodCategory category) {
            this.category = category;
            return this;
        }

        public Builder ingredients(List<String> ingredients) {
            this.ingredients = ingredients;
            return this;
        }
    }
}