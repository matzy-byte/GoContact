package com.matzy.gocontact.viewmodel;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.matzy.gocontact.data.Contact;
import com.matzy.gocontact.data.ContactDAO;
import com.matzy.gocontact.data.ContactDatabase;

import java.lang.reflect.Executable;
import java.util.List;
import java.util.concurrent.Executors;

public class ContactRepository {
    private ContactDAO contactDAO;
    private LiveData<List<Contact>> allContacts;

    public ContactRepository(Application application) {
        ContactDatabase database = ContactDatabase.getInstance(application);
        contactDAO = database.contactDAO();
        allContacts = contactDAO.getAllContacts();
    }

    public void upsert(Contact contact) {
        Executors.newSingleThreadExecutor().execute(() -> contactDAO.upsert(contact));
    }

    public LiveData<List<Contact>> getAllContacts() {
        return allContacts;
    }
}
