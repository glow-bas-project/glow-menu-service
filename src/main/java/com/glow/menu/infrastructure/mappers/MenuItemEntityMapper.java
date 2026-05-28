package com.glow.menu.infrastructure.mappers;

import com.glow.menu.domain.model.MenuItem;
import com.glow.menu.infrastructure.repository.entities.MenuItemJpaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface MenuItemEntityMapper {

    MenuItemJpaEntity toEntity(MenuItem menuItem);

    MenuItem toDomain(MenuItemJpaEntity entity);
}