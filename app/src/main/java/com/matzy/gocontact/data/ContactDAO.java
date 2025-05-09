package com.matzy.gocontact.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Upsert;

import java.util.List;

@Dao
public interface ContactDAO {
    @Upsert
    void upsert(Contact contact);

    @Update
    void update(Contact contact);

    @Query("SELECT * FROM contact_table")
    LiveData<List<Contact>> getAllContactsLive();

    @Query("SELECT * FROM contact_table ORDER BY id ASC")
    List<Contact> getAllContacts();
}
