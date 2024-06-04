package com.example.findyourband.services;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.findyourband.R;

import java.util.List;

public class ContactsDialogController {
    ContactsDialogController() {

    }

    public static void showContactsDialog(Context context, List<String> contacts) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context, R.style.CustomDialog);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_contacts, null);
        dialogBuilder.setView(dialogView);

        LinearLayout contactLayout = dialogView.findViewById(R.id.contactsLayout);

        StringBuilder contactsDisplay = new StringBuilder();
        for (String contact : contacts) {
            contactsDisplay.append(contact).append("\n\n");
        }

        if (contacts.get(0).equals("")) {
            contactLayout.removeView(dialogView.findViewById(R.id.contactPhoneLayout));
        } else {
            TextView phoneNumberText = dialogView.findViewById(R.id.phoneNumberText);
            phoneNumberText.setText(contacts.get(0));
            phoneNumberText.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + contacts.get(0)));
                context.startActivity(intent);
            });
        }
        if (contacts.get(1).equals("")) {
            contactLayout.removeView(dialogView.findViewById(R.id.contactEmailLayout));
        } else {
            TextView emailText = dialogView.findViewById(R.id.emailText);
            emailText.setText(contacts.get(1));
            emailText.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:" + contacts.get(1)));
                context.startActivity(intent);
            });
        }
        if (contacts.get(2).equals("")) {
            contactLayout.removeView(dialogView.findViewById(R.id.contactSocialMediaLayout));
        } else {
            TextView socialMediaText = dialogView.findViewById(R.id.socialMediaText);
            socialMediaText.setText(contacts.get(2));
            socialMediaText.setOnClickListener(v -> {
                String socialMediaUrl = contacts.get(2);
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.t.me/" + socialMediaUrl));
                    context.startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(context, "Невозможно открыть ссылку.", Toast.LENGTH_SHORT).show();
                }

            });
        }

        dialogBuilder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }
}
