package com.glow.menu.infrastructure.repository;

import com.glow.menu.domain.shared.PageRequest;
import com.glow.menu.domain.shared.PageResult;
import com.glow.menu.infrastructure.repository.entities.MenuItemJpaEntity;
import com.glow.menu.infrastructure.repository.projections.MenuItemIdProjection;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class MenuItemJpaRepository implements PanacheRepositoryBase<MenuItemJpaEntity, String> {

    public PageResult<String> findIds(PageRequest pageRequest, String restaurantId) {
        var query = (restaurantId == null || restaurantId.isBlank())
            ? findAll().page(pageRequest.page(), pageRequest.size()).project(MenuItemIdProjection.class)
            : find("restaurantId", restaurantId).page(pageRequest.page(), pageRequest.size())
                .project(MenuItemIdProjection.class);

        var hasNext = query.hasNextPage();

        return new PageResult<>(
            query.list().stream().map(MenuItemIdProjection::id).toList(),
            hasNext ? pageRequest.page() + 1 : pageRequest.page(),
            !hasNext);
    }

    public List<MenuItemJpaEntity> findMenuItemsByIds(List<String> ids) {
        return list("id in ?1", ids);
    }

    public List<MenuItemJpaEntity> findByRestaurantId(String restaurantId) {
        return list("restaurantId", restaurantId);
    }

    public Optional<MenuItemJpaEntity> findMenuItemById(String id) {
        return findByIdOptional(id);
    }
}