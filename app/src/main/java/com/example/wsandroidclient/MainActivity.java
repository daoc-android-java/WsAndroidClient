package com.example.wsandroidclient;

import java.net.URI;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends Activity {
	WebSocketClient mWs;
	TextView tv;
	EditText et;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.conversacion);
        et = (EditText) findViewById(R.id.msg);
        
		try {
			mWs = new WebSocketClient(new URI("ws://10.0.2.2:8080/saludos"), new Draft_17()) {
				@Override
				public void onMessage(final String message) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							tv.append("=> " + message + "\n");
						}
					});
				}

				@Override
				public void onOpen(ServerHandshake handshake) {
					System.out.println("opened connection");
				}

				@Override
				public void onClose(int code, String reason, boolean remote) {
					System.out.println("closed connection: " + reason + code);
				}

				@Override
				public void onError(Exception ex) {
					ex.printStackTrace();
				}

			};
		} catch (Exception e) {
			e.printStackTrace();
		} 
		        
    }

    public void conectarWs(View v) {
    	mWs.connect();
    }
    
    public void enviarWs(View v) {
    	String msg = et.getText().toString();
    	mWs.send(msg);
    	tv.append("<= " + msg + "\n");
    }    
    
    @Override
    protected void onDestroy() {
    	if(mWs != null) {
    		mWs.close();
    	}
    	super.onDestroy();
    }
    
}
