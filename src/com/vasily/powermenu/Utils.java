package com.vasily.powermenu;

import com.stericson.RootTools.RootTools; 
import com.stericson.RootTools.execution.CommandCapture;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log; 

public class Utils {
     public static boolean checkIfRooted(final Activity activity) {
         boolean rootAccess = false;
         if (RootTools.isAccessGiven()) { 
 			 rootAccess = true;
             } else {
                 AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                 builder.setCancelable(false);
                 builder.setIcon(android.R.drawable.ic_dialog_alert);
                 builder.setTitle(R.string.no_root_title);
                 builder.setNeutralButton(R.string.dialog_exit,
                         new DialogInterface.OnClickListener() {
                             @Override
                             public void onClick(DialogInterface dialog, int which) {
                            	 activity.finish();  
                             }
                         });

                 AlertDialog alert = builder.create();
                 alert.show();
             }
 	     return rootAccess;
     } 
}