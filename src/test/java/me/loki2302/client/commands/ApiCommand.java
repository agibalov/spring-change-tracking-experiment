package me.loki2302.client.commands;

import me.loki2302.client.NoteDataContext;
import me.loki2302.client.api.NoteOperations;

public interface ApiCommand<TResult> {
    TResult applyLocally(NoteDataContext noteDataContext);
    void applyRemotely(NoteOperations noteOperations);
}
