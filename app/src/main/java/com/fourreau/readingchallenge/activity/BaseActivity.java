package com.fourreau.readingchallenge.activity;

/**
 * Created by Pierre on 06/07/2015.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.widget.Toast;

import com.fourreau.readingchallenge.R;
import com.gc.materialdesign.widgets.SnackBar;

public abstract class BaseActivity extends AppCompatActivity {

    protected void displayErrorSnackBar(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
//        new com.github.mrengineer13.snackbar.SnackBar.Builder(this)
//                .withMessage(message)
//                .withActionMessage("OK")
//                .withDuration(new Short("2000"))
//                .show();
    }



    protected void displayAlertDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setTitle(title)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    protected int getActionBarSize() {
        TypedValue typedValue = new TypedValue();
        int[] textSizeAttr = new int[]{R.attr.actionBarSize};
        int indexOfAttrTextSize = 0;
        TypedArray a = obtainStyledAttributes(typedValue.data, textSizeAttr);
        int actionBarSize = a.getDimensionPixelSize(indexOfAttrTextSize, -1);
        a.recycle();
        return actionBarSize;
    }

    protected void writeSharedPreferences(String key, int value) {
        SharedPreferences sharedPref = BaseActivity.this.getSharedPreferences("readingchallenge", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    protected int readSharedPreferences(String key) {
        SharedPreferences sharedPref = BaseActivity.this.getSharedPreferences("readingchallenge", Context.MODE_PRIVATE);
        return sharedPref.getInt(key, 0);
    }

    protected void writeSharedPreferencesString(String key, String value) {
        SharedPreferences sharedPref = BaseActivity.this.getSharedPreferences("readingchallenge", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    protected String readSharedPreferencesString(String key) {
        SharedPreferences sharedPref = BaseActivity.this.getSharedPreferences("readingchallenge", Context.MODE_PRIVATE);
        return sharedPref.getString(key, "");
    }
}