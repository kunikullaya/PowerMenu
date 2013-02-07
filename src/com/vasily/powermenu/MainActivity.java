package com.vasily.powermenu;
 
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

public class MainActivity extends Activity {
	
	/**
	 * Global Decelerations?  
	 */
	private String[] listViewItems;
	private int actionID;
	private boolean confirmBeforeAction = true;
	
	/**
     * Need to redo this piece of code. 
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView = (ListView) findViewById(R.id.mylist);
        listViewItems = getResources().getStringArray(R.array.optionArray); 
 		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1, listViewItems);
		listView.setAdapter(adapter);
		CheckPref();
		listView.setOnItemClickListener(new OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) { 
			actionID = position;
			if(confirmBeforeAction) 
				confirmActionDialog();
			else
				doTheAction(); 
 		}
		});
	 }
    
    /**
     * Loads the preferences. 
     */
    private void CheckPref(){
    	SharedPreferences myPref   = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
    	confirmBeforeAction = myPref.getBoolean("askBeforePerformAction",true);
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
     * Displays a Confirmation Dialog before doing an action. 
     */
     private boolean confirmActionDialog(){
    	AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
	   	 builder.setTitle("Confirm " + listViewItems[actionID].toString());
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
   	 return false;
    }
    
 /**
  * Performs one of the 5 actions.  
  */
    private boolean doTheAction(){
    	String command = "";
    	switch (actionID)
        {
        	case 0: 
        		command = "reboot";
        		break;
        	case 1:
        		command = "reboot recovery";
        		break;
        	case 2:
        		command = "reboot download";
        		break;
        	case 3:
        		command = "busybox killall system_server ";
        		break;	
        	case 4:
        		command = "reboot -p";
        		break;
        }
    	try {
            Process proc = Runtime.getRuntime().exec(
            									new String[] { "su", "-c", command }
            										);
            wait(10);
            proc.waitFor();
        } catch (Exception ex) {
            Log.i("PowerMenu", "Could not perform action", ex);
        }
    	return false;
      }
  }
