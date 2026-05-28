package com.glow.menu.application.services;

import com.glow.menu.application.api.model.CreateMenuItemRequest;
import com.glow.menu.application.mappers.MenuItemDtoMapper;
import com.glow.menu.application.model.MenuItemDto;
import com.glow.menu.domain.model.MenuItem;
import com.glow.menu.domain.repository.MenuItemRepository;
import com.glow.menu.domain.shared.PageRequest;
import com.glow.menu.domain.shared.PageResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;

import java.time.Instant;
import java.util.List;

@ApplicationScoped
public class MenuItemService {

    private final MenuItemRepository menuItemRepository;
    private final MenuItemDtoMapper mapper;

    public MenuItemService(MenuItemRepository menuItemRepository, MenuItemDtoMapper mapper) {
        this.menuItemRepository = menuItemRepository;
        this.mapper = mapper;
    }

    public MenuItemDto createMenuItem(CreateMenuItemRequest request) {
        var menuItem = MenuItem.builder()
            .restaurantId(request.restaurantId())
            .name(request.name())
            .description(request.description())
            .price(request.price())
            .category(request.category())
            .ingredients(request.ingredients())
            .build();

        menuItemRepository.save(menuItem);
        return mapper.toDto(menuItem);
    }

    public MenuItemDto findById(String id) {
        return mapper.toDto(
            menuItemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Menu item with id " + id + " not found")));
    }

    public MenuItemDto updateMenuItem(String id, com.glow.menu.application.api.model.UpdateMenuItemRequest request) {
        var existing = menuItemRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Menu item with id " + id + " not found"));

        var updated = MenuItem.builder()
            .id(existing.getId())
            .createdAt(existing.getCreatedAt())
            .updatedAt(Instant.now())
            .restaurantId(request.restaurantId())
            .name(request.name())
            .description(request.description())
            .price(request.price())
            .category(request.category())
            .ingredients(request.ingredients())
            .build();

        menuItemRepository.update(updated);
        return mapper.toDto(updated);
    }

    public List<MenuItemDto> findByRestaurantId(String restaurantId) {
        return menuItemRepository.findByRestaurantId(restaurantId).stream()
            .map(mapper::toDto)
            .toList();
    }

    public PageResult<String> findMenuItemIds(int size, int page, String restaurantId) {
        return menuItemRepository.findIds(new PageRequest(size, page), restaurantId);
    }

    public List<MenuItemDto> materialise(List<String> ids) {
        return menuItemRepository.findByIds(ids).stream()
            .map(mapper::toDto)
            .toList();
    }

    public boolean deleteMenuItemById(String id) {
        return menuItemRepository.deleteById(id);
    }
}