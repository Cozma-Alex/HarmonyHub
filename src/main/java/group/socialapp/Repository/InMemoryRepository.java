package group.socialapp.Repository;

import group.socialapp.Domain.Entity;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryRepository<ID, E extends Entity<ID>> implements Repository<ID, E> {


    private final Map<ID,E> entities;


    @Override
    public int getNrOfEntities() {
        return entities.size();
    }

    @Override
    public Iterable<E> filterByEmail(ID searchText) {
        return null;
    }

    public InMemoryRepository() {
        this.entities = new HashMap<>();
    }

    @Override
    public Optional<E> findOne(ID id) {
        if (id == null) {
            throw new IllegalArgumentException("Id must not be null");
        }

        return Optional.ofNullable(entities.get(id));
    }

    @Override
    public Iterable<E> findAll() {
        return entities.values();
    }

    @Override
    public Optional<E> addOne(E entity) {
        if (entity == null){
            throw new IllegalArgumentException("Entity must not be null");
        }

        if (entities.get(entity.getId()) != null) {
            return Optional.empty();
        }
        else {
            entities.put(entity.getId(), entity);
        }

        return Optional.of(entity);
    }

    @Override
    public Optional<E> delete(ID id) {
        if (id == null){
            throw new IllegalArgumentException("Id must not be null");
        }

        Optional<E> entity = findOne(id);

        if (entity.isPresent()) {
            entities.remove(id);
            return entity;
        }

        return entity;

    }

    @Override
    public Optional<E> update(E entity) {
        if (entity == null){
            throw new IllegalArgumentException("Entity must not be null");
        }

        if (entities.get(entity.getId()) != null){
            entities.put(entity.getId(), entity);
            return Optional.of(entity);
        }

        return Optional.empty();

    }
}
