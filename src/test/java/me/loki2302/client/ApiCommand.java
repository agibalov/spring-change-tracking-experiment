package me.loki2302.client;

import me.loki2302.NoteOperations;

public interface ApiCommand<TResult> {
    TResult applyLocally(LocalRepository<LocalNote> noteRepository);
    void applyRemotely(NoteOperations noteOperations);
}
