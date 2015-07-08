package com.fourreau.readingchallenge.activity;

/**
 * Created by Pierre on 06/07/2015.
 */

import android.content.res.TypedArray;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;

import com.fourreau.readingchallenge.R;
import com.gc.materialdesign.widgets.SnackBar;

public abstract class BaseActivity extends AppCompatActivity {

    protected void displayErrorSnackBar(String message) {
        SnackBar snackbar = new SnackBar(BaseActivity.this, message, null, null);
        snackbar.show();
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
}