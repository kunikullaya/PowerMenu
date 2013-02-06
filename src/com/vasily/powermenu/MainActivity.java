package com.vasily.powermenu;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log; 
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter; 
import android.widget.ListView; 
public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView = (ListView) findViewById(R.id.mylist);
		String[] values = new String[] { "Reboot", "Recovery", "Download",
				"Shutdown" }; 
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1, values);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
 
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) { 
				doThisAction(position);
			}
		});
    }
 
    private void doThisAction(int actionID){
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
        		command = "reboot -p";
        		break;
        }
    	
    	try {
            Process proc = Runtime.getRuntime().exec(new String[] { "su", "-c", command });
            proc.waitFor();
        } catch (Exception ex) {
            Log.i("PowerMenu", "Could not perform action", ex);
        }
      }
  }
