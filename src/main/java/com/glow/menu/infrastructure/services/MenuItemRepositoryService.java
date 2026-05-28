package com.glow.menu.infrastructure.services;

import com.glow.menu.domain.model.MenuItem;
import com.glow.menu.domain.repository.MenuItemRepository;
import com.glow.menu.domain.shared.PageRequest;
import com.glow.menu.domain.shared.PageResult;
import com.glow.menu.infrastructure.mappers.MenuItemEntityMapper;
import com.glow.menu.infrastructure.repository.MenuItemJpaRepository;
import com.glow.menu.infrastructure.repository.entities.MenuItemJpaEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class MenuItemRepositoryService implements MenuItemRepository {

    private final MenuItemJpaRepository repository;
    private final MenuItemEntityMapper mapper;

    public MenuItemRepositoryService(MenuItemJpaRepository repository, MenuItemEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public void save(MenuItem menuItem) {
        MenuItemJpaEntity entity = mapper.toEntity(menuItem);
        repository.persist(entity);
    }

    @Override
    @Transactional
    public void update(MenuItem menuItem) {
        MenuItemJpaEntity entity = mapper.toEntity(menuItem);
        repository.getEntityManager().merge(entity);
    }

    @Override
    public Optional<MenuItem> findById(String id) {
        return repository.findMenuItemById(id)
            .map(mapper::toDomain);
    }

    @Override
    public PageResult<String> findIds(PageRequest pageRequest, String restaurantId) {
        return repository.findIds(pageRequest, restaurantId);
    }

    @Override
    public List<MenuItem> findByIds(List<String> ids) {
        return repository.findMenuItemsByIds(ids).stream()
            .map(mapper::toDomain)
            .toList();
    }

    @Override
    public List<MenuItem> findByRestaurantId(String restaurantId) {
        return repository.findByRestaurantId(restaurantId).stream()
            .map(mapper::toDomain)
            .toList();
    }

    @Override
    @Transactional
    public boolean deleteById(String id) {
        return repository.deleteById(id);
    }
}