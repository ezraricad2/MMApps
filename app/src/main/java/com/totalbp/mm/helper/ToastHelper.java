package com.totalbp.mm.helper;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by 140030 on 06/09/2017.
 */

public class ToastHelper {
    public static void showAToast (String st, Toast toast, Context context){ //"Toast toast" is declared in the class
        try{ toast.getView().isShown();     // true if visible
            toast.setText(st);
        } catch (Exception e) {         // invisible if exception
            toast = Toast.makeText(context, st, Toast.LENGTH_SHORT);
        }
        toast.show();  //finally display it
    }
}
