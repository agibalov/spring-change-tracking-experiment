package me.loki2302.client;

public class NoteDataContext {
    public Long revision = null;
    public LocalRepository<LocalNote> noteRepository = new LocalRepository<LocalNote>();
}
