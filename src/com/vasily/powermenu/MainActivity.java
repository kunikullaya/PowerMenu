package com.vasily.powermenu;

import android.os.Bundle; 
import android.preference.PreferenceManager;
import android.app.Activity;  
import android.content.Intent;
import android.content.SharedPreferences; 
import android.view.Menu; 
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter; 
import android.widget.ListView;  
import org.rootcommands.Toolbox;


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
		if(Utils.isAndroidRooted(MainActivity.this))
		{
			CheckPref();
			listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) { 
				actionID = position;
				if(confirmBeforeAction) 
				{
					if(Utils.confirmActionDialog(MainActivity.this,listViewItems[actionID].toString()))
						doTheAction();
				}
				else
						doTheAction();
	 		}
			});
		}
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
  * Performs one of the 5 actions.  
  */
    private boolean doTheAction(){
    	int command = 0;
    	switch (actionID)
        {
        	case 0: 
        		command = Toolbox.REBOOT_REBOOT;
        		break;
        	case 1:
        		command = Toolbox.REBOOT_RECOVERY;
        		break;
        	case 2:
        		command = -1;
        		break;
        	case 3:
        		command = Toolbox.REBOOT_HOTREBOOT;
        		break;	
        	case 4:
        		command = Toolbox.REBOOT_SHUTDOWN;
        		break;
        }
    	 Utils.ExecuteCommand(command);
    	return false;
      }
  }
