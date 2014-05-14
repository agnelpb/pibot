// " Did I not tell you that if you believe you will see the glory of God ? " - John 11:40

/*

Copyright 2014 Agnel P B

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

*/


package com.project.pibot;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class Controller extends Activity implements OnClickListener
{
	
	String action;
	String response="error";
	String strength;
	final String ServerIP="192.168.43.192"; // Replace by your Raspberry Pi static IP address
	TextView current_status,signal;
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.controller);
        
        ImageButton exit_button=(ImageButton)findViewById(R.id.exit_button);
        exit_button.setOnClickListener(this);
        
        ImageButton credits=(ImageButton)findViewById(R.id.credits);
        credits.setOnClickListener(this);
        
        ImageButton up=(ImageButton)findViewById(R.id.button_up);
        up.setOnClickListener(this);
        
        ImageButton down=(ImageButton)findViewById(R.id.button_down);
        down.setOnClickListener(this);
        
        ImageButton left=(ImageButton)findViewById(R.id.button_left);
        left.setOnClickListener(this);
        
        ImageButton right=(ImageButton)findViewById(R.id.button_right);
        right.setOnClickListener(this);
       
        current_status=(TextView)findViewById(R.id.current_status);
        current_status.setText("Disconnected");
        
        TextView ip_address=(TextView)findViewById(R.id.ip_address);
        ip_address.setText(ServerIP);
   
        signal=(TextView)findViewById(R.id.signal_strength);
       	signal.setText("No Signal");
       
	}
	
	// Function to find Wifi Strength. Not sure if working.
	public void getWifiStrength()
	{
       	int numberOfLevels=5;
       	WifiManager WifiMan = (WifiManager) getSystemService(getApplicationContext().WIFI_SERVICE);
        WifiInfo wifi = WifiMan.getConnectionInfo();
        int level=WifiManager.calculateSignalLevel(wifi.getRssi(), numberOfLevels);

        switch(level)
        {
        case 0 : strength="No Signal";
        		 break;
        case 1 : strength="Very Poor";
		 		 break;
		case 2 : strength="Poor";
		 		 break;
		case 3 : strength="Moderate";
		 		 break;
		case 4 : strength="Good";
		 		 break;
		case 5 : strength="Excellent";
		 		 break; 		 
        }		 
        
     }
	 	
	

	public void onClick(View v)
	{
		// If exit Button is clicked
		if(v.getId()== R.id.exit_button)
		{	 
			finish();
		}
		
		if(v.getId()== R.id.credits)
		{	 
			Intent inte=new Intent(Controller.this, Credits.class);
	       	startActivity(inte);
		}
		
		if(v.getId()== R.id.button_up)
		{	
			action="up";
	 	}
		
		if(v.getId()== R.id.button_down)
		{	
			action="down";
		}
		
		if(v.getId()== R.id.button_left)
		{	
			action="left";
		}
		
		if(v.getId()== R.id.button_right)
		{	
			action="right";
	 	}
		
		response="error";
	    new Async().execute(action); // The button pressed is sent to Raspberry Pi
	   	
	}
	
	public void onRelease(View v)
	{
		
	}
	
	
	// Contact the RPi server as independent thread 
	private class Async extends AsyncTask<String, Void, Void>
	{
	
		@Override
	    protected Void doInBackground(String... data) 
	    {
			byte[] buf=new byte[20];
			
	    	try
	    	{
	    		getWifiStrength();
	    			    		
	    		Socket soc = new Socket(ServerIP,8888);
	    		DataOutputStream out = new DataOutputStream(soc.getOutputStream());
	    		out.writeBytes(action +"\n");
	    		Log.d("Network", "Data Sent : "+ action);
	    		
	    		DataInputStream in = new DataInputStream(soc.getInputStream());
	    		response=in.readUTF().toString();
	    		
	    		Log.d("Network", "Data Recieved : "+ response);
	    		
	    		
	    		soc.close();
	    	}
	    	catch(Exception e)	
	    	{
	    		Log.e("Network", e.toString());
	    	}
	    	
	        return null;
	    }

	    @Override
	    protected void onPostExecute(Void result) 
	    {
	        super.onPostExecute(result);
	        
	        if(response.equals("success")) 
	        {
	        	current_status.setText("CONNECTED");
	        	Log.d("Network", "connected");
	        }
	        
	        if(response.equals("fail")) // This almost never happens
	        {	
	        	Toast toast = Toast.makeText(getApplicationContext()," Unknown Action ", Toast.LENGTH_SHORT);
	        	toast.show();
	        }
	        
	        if(response.equals("error")) // If network error or unable to contact RPi
	        {	
	        	current_status.setText("DISCONNECTED");
	        	Log.d("network", "disconnected");
	        }
	        signal.setText(strength);
 
	    }

	 }
}
	

