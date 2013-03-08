package com.vasily.powermenu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import org.rootcommands.RootCommands;
import org.rootcommands.Shell;
import org.rootcommands.Toolbox;

public class Utils {

	private static boolean result;
	 
	 
	 public static void ExecuteCommand(int command){
		 try {
			 if(command == -1){
				 Process proc = Runtime.getRuntime().exec("su -c reboot download");
				 proc.waitFor();
			 }else{
				 Shell rootShell = Shell.startRootShell();
	             Toolbox tb = new Toolbox(rootShell);
	             tb.reboot(command);
			 }
		 } catch (Exception ex) {
	            Log.i("PowerMenu", "Could not perform action", ex);
	        }
     }
	 
	 /**
     * Displays a Confirmation Dialog before doing an action. 
     */
     public static boolean confirmActionDialog(final Activity activity, String action ){
    	 AlertDialog.Builder builder = new AlertDialog.Builder(activity);
	   	 builder.setTitle("Confirm " + action);
	   	 builder.setPositiveButton(R.string.dialog_true, new DialogInterface.OnClickListener() {
   		 public void onClick(DialogInterface dialog, int id) {
   			 	result = true;
   	            }
   	        });
   	 builder.setNegativeButton(R.string.dialog_false, new DialogInterface.OnClickListener() {
   	            public void onClick(DialogInterface dialog, int id) {
   	            	result = false;
   	            	dialog.dismiss();
   	            }
   	        });
   	 AlertDialog dialog = builder.create();
   	 dialog.show();
   	 return result;
    }
     
     public static boolean isAndroidRooted(final Activity activity) {
         boolean rootAccess = false;
         if (RootCommands.rootAccessGiven()) {
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
                            	 activity.finish(); // finish current activity, means exiting app
                             }
                         });

                 AlertDialog alert = builder.create();
                 alert.show();
             }
 	     return rootAccess;
     }
 }
