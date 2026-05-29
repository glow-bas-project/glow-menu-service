package com.glow.menu.application.services;

import com.glow.menu.application.api.model.CreateMenuItemRequest;
import com.glow.menu.application.api.model.UpdateMenuItemRequest;
import com.glow.menu.application.mappers.MenuItemDtoMapper;
import com.glow.menu.application.model.MenuItemDto;
import com.glow.menu.domain.model.FoodCategory;
import com.glow.menu.domain.model.MenuItem;
import com.glow.menu.domain.repository.MenuItemRepository;
import com.glow.menu.domain.shared.PageRequest;
import com.glow.menu.domain.shared.PageResult;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MenuItemServiceTest {

    @Mock
    MenuItemRepository menuItemRepository;

    @Mock
    MenuItemDtoMapper mapper;

    @InjectMocks
    MenuItemService service;

    @Test
    public void createMenuItem_savesAndMapsToDto() {
        // given
        var request = createRequest();
        when(mapper.toDto(any(MenuItem.class))).thenAnswer(invocation -> toDto(invocation.getArgument(0)));

        // when
        var dto = service.createMenuItem(request);

        // then
        assertEquals(request.restaurantId(), dto.restaurantId());
        assertEquals(request.name(), dto.name());
        assertEquals(request.price(), dto.price());
        verify(menuItemRepository).save(any(MenuItem.class));
        verify(mapper).toDto(any(MenuItem.class));
    }

    @Test
    public void findById_throwsWhenMissing() {
        // given
        when(menuItemRepository.findById("missing")).thenReturn(Optional.empty());

        // when / then
        assertThrows(NotFoundException.class, () -> service.findById("missing"));
    }

    @Test
    public void findById_returnsDtoWhenPresent() {
        // given
        var menuItem = createMenuItem();
        var expected = toDto(menuItem);
        when(menuItemRepository.findById(expected.id())).thenReturn(Optional.of(menuItem));
        when(mapper.toDto(menuItem)).thenReturn(expected);

        // when
        var actual = service.findById(expected.id());

        // then
        assertEquals(expected, actual);
    }

    @Test
    public void updateMenuItem_updatesExistingItem() {
        // given
        var existing = createMenuItem();
        var request = new UpdateMenuItemRequest(
            "restaurant-456",
            "Pizza Pepperoni",
            "Updated description",
            1599,
            FoodCategory.PIZZA,
            List.of("tomato", "mozzarella", "pepperoni")
        );
        when(menuItemRepository.findById(existing.getId().toString())).thenReturn(Optional.of(existing));
        when(mapper.toDto(any(MenuItem.class))).thenAnswer(invocation -> toDto(invocation.getArgument(0)));

        // when
        var actual = service.updateMenuItem(existing.getId().toString(), request);

        // then
        assertEquals(request.restaurantId(), actual.restaurantId());
        assertEquals(request.name(), actual.name());
        assertEquals(request.price(), actual.price());
        var captor = ArgumentCaptor.forClass(MenuItem.class);
        verify(menuItemRepository).update(captor.capture());
        assertEquals(existing.getId(), captor.getValue().getId());
        assertEquals(request.restaurantId(), captor.getValue().getRestaurantId());
        assertEquals(request.name(), captor.getValue().getName());
        assertEquals(request.price(), captor.getValue().getPrice());
    }

    @Test
    public void findByRestaurantId_returnsItemsForRestaurant() {
        // given
        var item = createMenuItem();
        var dto = toDto(item);
        when(menuItemRepository.findByRestaurantId(item.getRestaurantId())).thenReturn(List.of(item));
        when(mapper.toDto(item)).thenReturn(dto);

        // when
        var actual = service.findByRestaurantId(item.getRestaurantId());

        // then
        assertEquals(List.of(dto), actual);
    }

    @Test
    public void findMenuItemIds_delegatesToRepository() {
        // given
        var pageResult = new PageResult<>(List.of(UUID.randomUUID().toString()), 1, true);
        when(menuItemRepository.findIds(any(PageRequest.class), any())).thenReturn(pageResult);

        // when
        var actual = service.findMenuItemIds(10, 0, "restaurant-123");

        // then
        assertEquals(pageResult, actual);
        var captor = ArgumentCaptor.forClass(PageRequest.class);
        verify(menuItemRepository).findIds(captor.capture(), any());
        assertEquals(10, captor.getValue().size());
        assertEquals(0, captor.getValue().page());
    }

    @Test
    public void materialise_mapsEachMenuItem() {
        // given
        var menuItem = createMenuItem();
        var id = menuItem.getId().toString();
        var dto = toDto(menuItem);
        when(menuItemRepository.findByIds(List.of(id))).thenReturn(List.of(menuItem));
        when(mapper.toDto(menuItem)).thenReturn(dto);

        // when
        var actual = service.materialise(List.of(id));

        // then
        assertEquals(List.of(dto), actual);
    }

    @Test
    public void deleteMenuItemById_returnsTrueWhenRepositoryDeletes() {
        // given
        when(menuItemRepository.deleteById("id123")).thenReturn(true);

        // when
        var deleted = service.deleteMenuItemById("id123");

        // then
        assertTrue(deleted);
        verify(menuItemRepository).deleteById("id123");
    }

    private static CreateMenuItemRequest createRequest() {
        return new CreateMenuItemRequest(
            "restaurant-123",
            "Pizza Margherita",
            "Classic tomato and mozzarella pizza",
            1299,
            FoodCategory.PIZZA,
            List.of("tomato", "mozzarella")
        );
    }

    private static MenuItem createMenuItem() {
        return MenuItem.builder()
            .restaurantId("restaurant-123")
            .name("Pizza Margherita")
            .description("Classic tomato and mozzarella pizza")
            .price(1299)
            .category(FoodCategory.PIZZA)
            .ingredients(List.of("tomato", "mozzarella"))
            .build();
    }

    private static MenuItemDto toDto(MenuItem menuItem) {
        return new MenuItemDto(
            menuItem.getId().toString(),
            menuItem.getRestaurantId(),
            menuItem.getName(),
            menuItem.getDescription(),
            menuItem.getPrice(),
            menuItem.getCategory(),
            menuItem.getIngredients(),
            menuItem.getCreatedAt(),
            menuItem.getUpdatedAt()
        );
    }
}
