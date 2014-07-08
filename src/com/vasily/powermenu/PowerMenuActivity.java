package com.vasily.powermenu;

import com.stericson.RootTools.RootTools; 
 
import android.os.Bundle;  
import android.app.Activity;  
import android.app.AlertDialog;
import android.content.DialogInterface; 
import android.content.SharedPreferences;  
import android.view.Menu;  
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter; 
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;   
import android.widget.Switch; 


public class PowerMenuActivity extends Activity {
	
	/**
	 * Global Decelerations?  
	 */
	private String[] listViewItems;
	private int actionID;
	private boolean showPopup = true;
	 private static boolean result;
	 private Switch preferenceSwitch;
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
	        	listViewItems[4] = getResources().getString(R.string.missingBusyBoxTitle);
	        }
			listView.setOnItemClickListener(listViewItemClickHandler);
			
		}
	 }
    
   
    OnItemClickListener listViewItemClickHandler = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) { 
				actionID = position;
				if(showPopup){
					confirmAndDoDialog(listViewItems[actionID].toString());
				}
				else{
					doTheAction();
	 			}
		}
    };
		
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
   
    
    OnCheckedChangeListener preferenceSwitchHandler = new OnCheckedChangeListener() {
    		@Override
		public void onCheckedChanged(CompoundButton btnName, boolean value) {
			SharedPreferences.Editor editor = getSharedPreferences("com.vasily.powermenu", MODE_PRIVATE).edit();
             if(value)
             {
                 editor.putBoolean("askBeforePerformAction", true);
                 editor.commit();
             }else  
             {
                 editor.putBoolean("askBeforePerformAction", false);
                 editor.commit();
                
             }
             SharedPreferences sharedPref = getSharedPreferences("com.vasily.powermenu", MODE_PRIVATE);
             showPopup = sharedPref.getBoolean("askBeforePerformAction", true);
			
		}
      };
   
    
    /**
      * Inflates the option menu
       */
    @Override
 	public boolean onCreateOptionsMenu(Menu menu) { 
    	getMenuInflater().inflate(R.menu.menu_settings, menu);
    	 
        // Get widget's instance
     	preferenceSwitch = (Switch)menu.findItem(R.id.menu_item_switch).getActionView().findViewById(R.id.preference_switch);;
		preferenceSwitch.setOnCheckedChangeListener(this.preferenceSwitchHandler);
	    SharedPreferences sharedPref = getSharedPreferences("com.vasily.powermenu", MODE_PRIVATE);
 		showPopup = sharedPref.getBoolean("askBeforePerformAction", true);
 		preferenceSwitch.setChecked(sharedPref.getBoolean("askBeforePerformAction", true)); 
        return super.onCreateOptionsMenu(menu); 
 		 
  }
    
    
     
    /**
      * based on the option selected some action is performed.
       * Since I have just 1 action(Starting Settings Activity) This method starts an Activity
       */
      @Override
      public boolean onOptionsItemSelected(MenuItem item) {
    	  switch (item.getItemId()) {
	          case R.id.menu_item_switch:
	              break;
	          default:
	              return super.onOptionsItemSelected(item);
	      }
		return true;
     }
      
	 
 /**
  * Performs one of the 5 actions.  
  */
    private boolean doTheAction(){
    	String command = "";
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
        		command = Commands.BOOTLOADER;
        		break;
        	case 4:
                command = Commands.HOTBOOT;

        		break;	
        	case 5:
        		command = Commands.SHUTDOWN;
        		break;
        }
        if(command !=""){
            Utils.ExecuteCommand(command);
        }

    	return false;
      }
  
}
