package xyz.sethy.api.framework.note;

import java.util.UUID;

/**
 * Created by Seth on 12/03/2017.
 */
public interface NoteManager
{
    void addNote(Note note);

    void removeNote(Note note);

    void getNote(UUID uuid);
}
