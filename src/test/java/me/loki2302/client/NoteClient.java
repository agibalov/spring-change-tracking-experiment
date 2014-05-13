package me.loki2302.client;

import me.loki2302.client.api.NoteOperations;
import me.loki2302.client.api.TransactionOperations;
import me.loki2302.changelog.ChangeLogEvent;
import me.loki2302.changelog.CreateEntityChangeLogEvent;
import me.loki2302.changelog.DeleteEntityChangeLogEvent;
import me.loki2302.changelog.UpdateEntityChangeLogEvent;
import me.loki2302.client.commands.ApiCommand;
import me.loki2302.client.commands.DeleteNoteCommand;
import me.loki2302.client.commands.SaveNoteCommand;
import me.loki2302.dto.ChangeLogTransactionDto;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class NoteClient {
    private final Queue<ApiCommand> commandQueue = new LinkedList<ApiCommand>();
    private final LocalRepository<LocalNote> noteRepository = new LocalRepository<LocalNote>();
    private final EntityHandler noteEntityHandler = new NoteEntityHandler();
    private final NoteOperations noteOperations;
    private final TransactionOperations transactionOperations;

    public NoteClient(NoteOperations noteOperations, TransactionOperations transactionOperations) {
        this.noteOperations = noteOperations;
        this.transactionOperations = transactionOperations;
    }

    public void sendChanges() {
        while(!commandQueue.isEmpty()) {
            ApiCommand<?> command = commandQueue.remove();
            command.applyRemotely(noteOperations);
        }
    }

    public void retrieveChanges() {
        List<ChangeLogTransactionDto> transactions = transactionOperations.getAllTransactions();
        for(ChangeLogTransactionDto transaction : transactions) {
            for(ChangeLogEvent event : transaction.events) {
                if(event instanceof CreateEntityChangeLogEvent) {
                    CreateEntityChangeLogEvent e = (CreateEntityChangeLogEvent)event;
                    String entityName = e.entityName;
                    if(entityName.equals("me.loki2302.entities.Note")) {
                        noteEntityHandler.handleCreateEntityChangeLogEvent(noteRepository, e);
                    } else {
                        throw new RuntimeException("Unknown entity name " + entityName);
                    }
                } else if(event instanceof UpdateEntityChangeLogEvent) {
                    UpdateEntityChangeLogEvent e = (UpdateEntityChangeLogEvent)event;
                    String entityName = e.entityName;
                    if(entityName.equals("me.loki2302.entities.Note")) {
                        noteEntityHandler.handleUpdateEntityChangeLogEvent(noteRepository, e);
                    } else {
                        throw new RuntimeException("Unknown entity name " + entityName);
                    }
                } else if(event instanceof DeleteEntityChangeLogEvent) {
                    DeleteEntityChangeLogEvent e = (DeleteEntityChangeLogEvent)event;
                    String entityName = e.entityName;
                    if(entityName.equals("me.loki2302.entities.Note")) {
                        noteEntityHandler.handleDeleteEntityChangeLogEvent(noteRepository, e);
                    } else {
                        throw new RuntimeException("Unknown entity name " + entityName);
                    }
                } else {
                    throw new RuntimeException("Unknown event type " + event.getClass());
                }
            }
        }
    }

    public LocalNote saveNote(String id, String text, String text2) {
        SaveNoteCommand command = new SaveNoteCommand();
        command.id = id;
        command.text = text;
        command.text2 = text2;
        commandQueue.add(command);
        return command.applyLocally(noteRepository);
    }

    public void deleteNote(String id) {
        DeleteNoteCommand command = new DeleteNoteCommand();
        command.id = id;
        commandQueue.add(command);
        command.applyLocally(noteRepository);
    }

    public List<LocalNote> getAllNotes() {
        return noteRepository.findAll();
    }

    public LocalNote getNote(String id) {
        LocalNote note = noteRepository.findOne(id);
        if(note == null) {
            throw new RuntimeException("LocalNote doesn't exist");
        }

        return note;
    }
}
