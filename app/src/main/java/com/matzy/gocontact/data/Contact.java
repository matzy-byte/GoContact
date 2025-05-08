package com.matzy.gocontact.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;

@Entity(tableName = "contact_table")
public class Contact implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String firstName;
    public String lastName;
    public int priority;
    public String birthdate;

    public Contact(String firstName, String lastName, int priority, String birthdate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.priority = priority;
        this.birthdate = birthdate;
    }

    public String getFullName() {
        return String.format("%s %s", firstName, lastName);
    }

    public String getLatestInteraction() {
        return (new Date()).toString();
    }
}
