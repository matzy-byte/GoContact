package com.matzy.gocontact.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.matzy.gocontact.R;

import java.util.Objects;

public class PreferencesDialogFragment extends DialogFragment {
    private SharedPreferences prefs;
    private EditText interval, time, theme, algo;
    private Button save, close;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.preferences, container, false);

        prefs = requireContext().getSharedPreferences("app_settings", Context.MODE_PRIVATE);

        interval = view.findViewById(R.id.edit_notification_interval);
        time = view.findViewById(R.id.edit_notification_time);
        theme = view.findViewById(R.id.edit_theme);
        algo = view.findViewById(R.id.edit_weighted_algo);
        save = view.findViewById(R.id.btn_save_contact);
        close = view.findViewById(R.id.btn_close_contact);

        interval.setText(String.valueOf(prefs.getInt("interval", 15)));
        time.setText(prefs.getString("time", "00:00"));
        theme.setText(prefs.getString("theme", "Light"));
        algo.setText(prefs.getString("algo", "priority"));

        save.setOnClickListener(v -> {
            prefs.edit().putInt("interval", Integer.parseInt(interval.getText().toString())).apply();
            prefs.edit().putString("time", time.getText().toString()).apply();
            prefs.edit().putString("theme", theme.getText().toString()).apply();
            prefs.edit().putString("algo", algo.getText().toString()).apply();
            Objects.requireNonNull(getDialog()).dismiss();
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
