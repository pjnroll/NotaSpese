package org.altervista.pierluigilaviano.notaspese.helper;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class Utils {
    /**
      * Util method that allows to hide the keyboard after the user presses a Button
      * @param v the current View
      */
    public static void hideKeyboard(Context ctx, View v) {
        if (v != null) {
            InputMethodManager imm;
            imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }
}