package com.matzy.gocontact.activities;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.matzy.gocontact.R;
import com.matzy.gocontact.data.Contact;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;


public class ContactFormDialogFragment extends DialogFragment {
    private static final String ARG_CONTACT = "contact_arg";
    private ContactFormListener listener;
    private TextView firstName, lastName, priority, birthdate;
    private Button save, close;
    private Contact contact;
    SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());

    public interface ContactFormListener {
        void onSaveClicked(Contact contact);
    }

    public static ContactFormDialogFragment newInstance(Contact contact) {
        ContactFormDialogFragment fragment = new ContactFormDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CONTACT, contact);
        fragment.setArguments(args);
        return fragment;
    }

    public static ContactFormDialogFragment newInstance() {
        return new ContactFormDialogFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ContactFormListener) {
            listener =(ContactFormListener) context;
        } else {
            throw new RuntimeException(context + "must implement ContactListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contact_form, container, false);

        if (getArguments() != null) {
            contact = (Contact) getArguments().getSerializable(ARG_CONTACT);
        }

        firstName = view.findViewById(R.id.edit_first_name);
        lastName = view.findViewById(R.id.edit_last_name);
        priority = view.findViewById(R.id.edit_priority);
        birthdate = view.findViewById(R.id.edit_birthdate);
        save = view.findViewById(R.id.btn_save_contact);
        close = view.findViewById(R.id.btn_close_contact);

        if (contact != null) {
            firstName.setText(contact.firstName);
            lastName.setText(contact.lastName);
            priority.setText(String.valueOf(contact.priority));
            birthdate.setText(format.format(contact.birthdate));
        }

        save.setOnClickListener(v -> {
            Objects.requireNonNull(getDialog()).dismiss();

            Date bday = new Date();
            Date lday = new Date();
            try {
                bday = format.parse(birthdate.getText().toString());
                lday = new Date();
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            if (contact == null) {
                contact = new Contact(firstName.getText().toString(), lastName.getText().toString(), Integer.parseInt(priority.getText().toString()), bday, lday);
            } else {
                contact.firstName = firstName.getText().toString();
                contact.lastName = lastName.getText().toString();
                contact.priority = Integer.parseInt(priority.getText().toString());
                contact.birthdate = bday;
                contact.latestInteraction = lday;
            }
            listener.onSaveClicked(contact);
        });
        close.setOnClickListener(v -> Objects.requireNonNull(getDialog()).dismiss());

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null && dialog.getWindow() != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstance) {
        Dialog dialog = super.onCreateDialog(savedInstance);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        return dialog;
    }
}
