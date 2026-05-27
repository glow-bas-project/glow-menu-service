package com.glow.menu.domain.repository;

import com.glow.menu.domain.model.MenuItem;
import com.glow.menu.domain.shared.PageRequest;
import com.glow.menu.domain.shared.PageResult;

import java.util.List;
import java.util.Optional;

public interface MenuItemRepository {

    void save(MenuItem menuItem);

    void update(MenuItem menuItem);

    Optional<MenuItem> findById(String id);

    PageResult<String> findIds(PageRequest pageRequest, String restaurantId);

    List<MenuItem> findByIds(List<String> ids);

    List<MenuItem> findByRestaurantId(String restaurantId);

    boolean deleteById(String id);
}