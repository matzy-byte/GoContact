package com.matzy.gocontact.data;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class ContactSelectionAlgorithm {
    private static final int WEIGHT_PRIORITY = 1;
    private static final int WEIGHT_BIRTHDATE = 1;
    private static final int WEIGHT_LATEST_INTERACTION = 1;
    public static Contact selectContact(List<Contact> contacts) {
        Date now = new Date();
        return Collections.max(contacts, Comparator.comparingDouble(contact -> WEIGHT_PRIORITY * contact.priority * ((double) (WEIGHT_BIRTHDATE * now.getTime()) / Math.abs(contact.birthdate.getTime()) + WEIGHT_LATEST_INTERACTION * (now.getTime() - contact.latestInteraction.getTime()))));
    }
}
