package com.vasily.powermenu;

import com.stericson.RootTools.RootTools; 

import android.os.Bundle; 
import android.preference.PreferenceManager;
import android.app.Activity;  
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences; 
import android.util.Log;
import android.view.Menu; 
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter; 
import android.widget.ListView;   


public class PowerMenuActivity extends Activity {
	
	/**
	 * Global Decelerations?  
	 */
	private String[] listViewItems;
	private int actionID;
	private boolean showPopup = true;
	 private static boolean result;
	/**
     * Need to redo this piece of code. 
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Utils.checkIfRooted(PowerMenuActivity.this))
		{
	        setContentView(R.layout.activity_main);
	        ListView listView = (ListView) findViewById(R.id.mylist);
	        listViewItems = getResources().getStringArray(R.array.optionArray); 
	 		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, android.R.id.text1, listViewItems);
			listView.setAdapter(adapter);
			if(!RootTools.isBusyboxAvailable()){
				listView.getChildAt(3).setEnabled(false); 
			}
			CheckPref();
			listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) { 
				actionID = position;
				if(showPopup) 
				{
					Log.d("Confirm Before Action:",String.valueOf(showPopup));
					confirmAndDoDialog(listViewItems[actionID].toString());
 				}
				else
					doTheAction();
			 	 
	 		}
			});
		}
	 }
    
   
    /**
     * Displays a Confirmation Dialog before doing an action. 
     */
     public boolean confirmAndDoDialog(String action ){
    	 AlertDialog.Builder builder = new AlertDialog.Builder(PowerMenuActivity.this);
	   	 builder.setTitle("Confirm " + action);
	   	 builder.setPositiveButton(R.string.dialog_true, new DialogInterface.OnClickListener() {
   		 public void onClick(DialogInterface dialog, int id) {
   			 	doTheAction();
   	            }
   	        });
	   	 builder.setNegativeButton(R.string.dialog_false, new DialogInterface.OnClickListener() {
   	            public void onClick(DialogInterface dialog, int id) { 
   	            	dialog.dismiss();
   	            }
   	        });
   	 AlertDialog dialog = builder.create();
   	 dialog.show();
   	 return result;
    }
    
    /**
     * Loads the preferences. 
     */
    private void CheckPref(){
    	SharedPreferences myPref   = PreferenceManager.getDefaultSharedPreferences(PowerMenuActivity.this);
    	showPopup = myPref.getBoolean("askBeforePerformAction",true);
    }			 
    
    /**
     * Inflates the option menu
     */
    @Override
	public boolean onCreateOptionsMenu(Menu menu) { 
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
    
    /**
     * based on the option selected some action is performed.
     * Since I have just 1 action(Starting Settings Activity) This method starts an Activity
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
            	startActivityForResult(new Intent(this, SetPreferenceActivity.class),0);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    /**
     * Gets the result from the SetPreferenceActivity and checks the preferences.
     * I'm not really sure if this is needed. But I'm going to keep it for now.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        CheckPref();
    }
  
   
  
 /**
  * Performs one of the 5 actions.  
  */
    private boolean doTheAction(){
    	String command = null;
    	switch (actionID)
        {
        	case 0: 
        		command = Commands.REBOOT;
        		break;
        	case 1:
        		command = Commands.RECOVERY;
        		break;
        	case 2:
        		command = Commands.DOWNLOAD;
        		break;
        	case 3:
        		command = Commands.HOTBOOT;
        		break;	
        	case 4:
        		command = Commands.SHUTDOWN;
        		break;
        }
    	 Utils.ExecuteCommand(command);
    	return false;
      }
  }
