package com.matzy.gocontact.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ContactDAO {
    @Insert
    void Insert(Contact contact);

    @Query("SELECT * FROM contact_table")
    LiveData<List<Contact>> getAllContacts();
}
