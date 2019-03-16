package com.raymondctc.udacity.popularmovies.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.raymondctc.udacity.popularmovies.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class ChangeSortingDialogFragment extends DialogFragment {
    public static ChangeSortingDialogFragment newInstance() {

        Bundle args = new Bundle();

        ChangeSortingDialogFragment fragment = new ChangeSortingDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final AlertDialog.Builder builder
                = new AlertDialog.Builder(getContext())
                .setItems(R.array.dialog_sort, (dialog, which) -> {

                })
                ;

        return builder.create();
    }
}
