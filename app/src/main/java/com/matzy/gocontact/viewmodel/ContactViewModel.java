package com.matzy.gocontact.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.matzy.gocontact.data.Contact;

import java.util.List;

public class ContactViewModel extends AndroidViewModel {
    private ContactRepository repository;
    private LiveData<List<Contact>> allContacts;

    public ContactViewModel(@NonNull Application application) {
        super(application);
        repository = new ContactRepository(application);
        allContacts = repository.getAllContacts();
    }

    public void upsert(Contact contact) {
        repository.upsert(contact);
    }

    public void delete(Contact contact) {
        repository.delete(contact);
    }

    public LiveData<List<Contact>> getAllContacts() {
        return allContacts;
    }
}
