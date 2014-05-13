package me.loki2302.client.commands;

import me.loki2302.NoteOperations;
import me.loki2302.client.LocalNote;
import me.loki2302.client.LocalRepository;

public interface ApiCommand<TResult> {
    TResult applyLocally(LocalRepository<LocalNote> noteRepository);
    void applyRemotely(NoteOperations noteOperations);
}
