package com.matzy.gocontact.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Upsert;

import java.util.List;

@Dao
public interface ContactDAO {
    @Upsert
    void upsert(Contact contact);

    @Query("SELECT * FROM contact_table")
    LiveData<List<Contact>> getAllContacts();
}
