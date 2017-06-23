package com.nidandc.nidansales;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by NIDAN on 23/06/2017.
 */

public class DisplayToast
{
    static void display(Context context,String message)
    {
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }
}
