/*
 * Copyright (C) 2017 IOTA Foundation
 *
 * Authors: pinpong, adrianziser, saschan
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.iota.wallet.ui.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import org.iota.wallet.R;
import org.iota.wallet.helper.Utils;

import java.io.File;
import java.io.FileOutputStream;

public class GeneratedQRCodeDialog extends DialogFragment implements DialogInterface.OnClickListener {

    private Bitmap bitmap;

    public GeneratedQRCodeDialog() {
    }

    private static void shareQRCode(Context context, Bitmap bitmap) {
        try {
            File file = new File(Utils.getExternalIotaDirectory(context), "qr_code.png");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            if (file.mkdirs()) {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                shareIntent.setType("image/png");
                context.startActivity(Intent.createChooser(shareIntent, context.getResources().getText(R.string.buttons_share)));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.dialog_generated_qr_code, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.generated_qr_code_image);

        Bundle bundle = getArguments();
        bitmap = bundle.getParcelable("bitmap");
        imageView.setImageBitmap(bitmap);

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setCancelable(false)
                .setPositiveButton(R.string.buttons_share, this)
                .setNegativeButton(R.string.buttons_cancel, null)
                .create();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int which) {
        switch (which) {
            case AlertDialog.BUTTON_POSITIVE:
                shareQRCode(getActivity(), bitmap);
                getDialog().dismiss();
        }
    }
}
