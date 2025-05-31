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

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Objects;

public class ContactDeleteDialogFragment extends DialogFragment {
    private Contact contact;
    private static final String ARG_CONTACT = "contact_arg";
    private ContactDeleteDialogFragment.ContactListener listener;
    private TextView fullName;
    private Button yes, no;


    public interface ContactListener {
        void onRemoveClicked(Contact contact);
    }

    public static ContactDeleteDialogFragment newInstance(Contact contact) {
        ContactDeleteDialogFragment fragment = new ContactDeleteDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CONTACT, contact);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ContactDialogFragment.ContactListener) {
            listener =(ContactDeleteDialogFragment.ContactListener) context;
        } else {
            throw new RuntimeException(context + "must implement ContactFormListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contact_remove, container, false);

        assert getArguments() != null;
        contact = (Contact) getArguments().getSerializable(ARG_CONTACT);

        fullName = view.findViewById(R.id.txt_full_name);
        fullName.setText(contact.getFullName());

        yes = view.findViewById(R.id.btn_save_contact);
        no = view.findViewById(R.id.btn_close_contact);
        yes.setOnClickListener(v -> {
            Objects.requireNonNull(getDialog()).dismiss();
            listener.onRemoveClicked(contact);
        });
        no.setOnClickListener(v -> Objects.requireNonNull(getDialog()).dismiss());

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
