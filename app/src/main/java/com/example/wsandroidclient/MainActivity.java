package com.example.wsandroidclient;

import java.net.URI;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Librería WebSockets:
 * https://github.com/TooTallNate/Java-WebSocket
 */
public class MainActivity extends Activity {
	WebSocketClient wsc;
	TextView tv;
	EditText et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.conversacion);
        et = (EditText) findViewById(R.id.msg);
    }

    public void conectarWs(View v) {
    	if(wsc != null && wsc.isOpen()) {
    		wsc.close();
    		wsc = null;
		}
    	wsc = newWebSocketClient();
		wsc.connect();
    }

	public WebSocketClient newWebSocketClient() {
		try {
			wsc = new WebSocketClient(new URI("ws://10.0.2.2:8080/saludos")) {
				@Override
				public void onMessage(final String message) {
					System.out.println("=> " + message);
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							tv.append("=> " + message + "\n");
						}
					});
				}

				@Override
				public void onOpen(ServerHandshake handshake) {
					System.out.println("Conexión abierta");
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							tv.append("*** Conexión abierta ***\n");
						}
					});
				}

				@Override
				public void onClose(int code, String reason, boolean remote) {
					System.out.println("Conexión cerrada: " + reason + code);
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							tv.append("*** Conexión cerrada: " + reason + " - " + code + " ***\n");
						}
					});
				}

				@Override
				public void onError(Exception ex) {
					ex.printStackTrace();
				}

			};
			return wsc;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

    public void enviarWs(View v) {
    	String msg = et.getText().toString();
		wsc.send(msg);
    	tv.append("<= " + msg + "\n");
    }    

    @Override
    protected void onDestroy() {
    	if(wsc != null && wsc.isOpen()) {
			wsc.close();
			wsc = null;
    	}
    	super.onDestroy();
    }
    
}
