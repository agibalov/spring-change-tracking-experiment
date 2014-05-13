package me.loki2302.client;

public abstract class LocalEntity<TEntity> {
    public String id;

    public abstract void updateFrom(TEntity entity);
}
