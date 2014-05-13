package me.loki2302.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocalRepository<TEntity extends LocalEntity> {
    private final Map<String, TEntity> entities = new HashMap<String, TEntity>();

    public TEntity save(TEntity entity) {
        String entityId = entity.id;
        TEntity repositoryEntity = entities.get(entityId);
        if(repositoryEntity == null) {
            entities.put(entityId, entity);
            repositoryEntity = entity;
        } else {
            repositoryEntity.updateFrom(entity);
        }

        return repositoryEntity;
    }

    public void delete(String id) {
        entities.remove(id);
    }

    public List<TEntity> findAll() {
        return new ArrayList<TEntity>(entities.values());
    }

    public TEntity findOne(String id) {
        return entities.get(id);
    }
}
