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

import java.util.Objects;


public class ContactDialogFragment extends DialogFragment {
    private Contact contact;
    private static final String ARG_CONTACT = "contact_arg";
    private ContactListener listener;
    private TextView fullName, latestInteraction, priority, birthdate;
    private Button edit, close;


    public interface ContactListener {
        void onEditClicked(Contact contact);
    }

    public static ContactDialogFragment newInstance(Contact contact) {
        ContactDialogFragment fragment = new ContactDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CONTACT, contact);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ContactListener) {
            listener =(ContactListener) context;
        } else {
            throw new RuntimeException(context + "must implement ContactFormListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contact, container, false);

        assert getArguments() != null;
        contact = (Contact) getArguments().getSerializable(ARG_CONTACT);

        fullName = view.findViewById(R.id.txt_full_name);
        latestInteraction = view.findViewById(R.id.txt_latest_interaction);
        priority = view.findViewById(R.id.txt_priority);
        birthdate = view.findViewById(R.id.txt_birthdate);

        fullName.setText(contact.getFullName());
        latestInteraction.setText(contact.getLatestInteraction());
        priority.setText(String.valueOf(contact.priority));
        birthdate.setText(contact.birthdate);

        edit = view.findViewById(R.id.btn_save_contact);
        close = view.findViewById(R.id.btn_close_contact);

        edit.setOnClickListener(v -> {
            Objects.requireNonNull(getDialog()).dismiss();
            listener.onEditClicked(contact);
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
