package com.glow.menu.application.mappers;

import com.glow.menu.application.model.MenuItemDto;
import com.glow.menu.domain.model.MenuItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface MenuItemDtoMapper {

    MenuItemDto toDto(MenuItem menuItem);

    MenuItem toDomain(MenuItemDto dto);
}