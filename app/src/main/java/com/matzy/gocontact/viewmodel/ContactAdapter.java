package com.matzy.gocontact.viewmodel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.matzy.gocontact.R;
import com.matzy.gocontact.data.Contact;

import java.util.ArrayList;
import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactHolder> {
    private List<Contact> contacts = new ArrayList<>();
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Contact contact);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_reduced, parent, false);
        return new ContactHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactHolder holder, int position) {
        Contact contact = contacts.get(position);
        holder.bind(contact);
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    class ContactHolder extends RecyclerView.ViewHolder {
        TextView name, latestInteraction;

        public ContactHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.txt_full_name);
            latestInteraction = itemView.findViewById(R.id.txt_latest_interaction);
        }

        public void bind(Contact contact) {
            name.setText(contact.getFullName());
            latestInteraction.setText(contact.getLatestInteraction());

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(contact); // pass the bound contact
                }
            });
        }
    }
}
