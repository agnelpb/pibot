// " Did I not tell you that if you believe you will see the glory of God ? " - John 11:40




package com.project.pibot;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.MotionEvent;

public class Splash extends Activity {

		boolean _active = true;
	    final int _splashTime = 2000; // time to display the splash screen in ms
		
		@Override
		public void onCreate(Bundle savedInstanceState) 
		{
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.splash_screen);
	   
	        
	        
	        // thread for displaying the SplashScreen
	        Thread splashTread = new Thread()
	        {
	            @Override
	            public void run()
	            {
	                try {
	                    int waited = 0;
	                    while(_active && (waited < _splashTime)) {
	                        sleep(100);
	                        
	                        // waited is incremented by 100 after every sleep for 100 ms
	                        if(_active) {
	                            waited += 100;
	                        }
	                    }
	                } 
	                catch(InterruptedException e) {
	      
	                }
	                finally 
	                {
	                	    finish();             	
	                       	Intent inte = new Intent(Splash.this, Controller.class);
	                       	startActivity(inte);	                	                              
	                }
	            }
	        };
	        Log.d("Status"," Splash Screen Passed ");
	        splashTread.start();
	        
	    
	    }
		// this is to skip splash screen by touch event
	    @Override
	    public boolean onTouchEvent(MotionEvent event) {
	        if (event.getAction() == MotionEvent.ACTION_DOWN) {
	            _active = false;
	        }
	        return true;
	    }
	}    