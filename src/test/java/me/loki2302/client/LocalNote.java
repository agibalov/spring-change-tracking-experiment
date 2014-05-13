package me.loki2302.client;

public class LocalNote extends LocalEntity<LocalNote> {
    public String text;
    public String text2;

    @Override
    public void updateFrom(LocalNote localNote) {
        text = localNote.text;
        text2 = localNote.text2;
    }
}
