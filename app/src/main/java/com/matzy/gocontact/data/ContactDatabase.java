package com.matzy.gocontact.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Contact.class}, version = 1, exportSchema = false)
public abstract class ContactDatabase extends RoomDatabase {
    private static volatile ContactDatabase INSTANCE;

    public abstract ContactDAO contactDAO();

    public static ContactDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    ContactDatabase.class, "contact_table")
                    .build();
        }
        return INSTANCE;
    }
}
