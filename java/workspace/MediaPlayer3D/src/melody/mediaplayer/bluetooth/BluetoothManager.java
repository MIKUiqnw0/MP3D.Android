package melody.mediaplayer.bluetooth;

import java.io.IOException;
import java.util.UUID;

import melody.mediaplayer.gui.MediaPlayer3D;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

/**tag
 * BluetoothManager.java<br>
 * Bluetooth management activity for transfering playlist information
 * to other devices. The current version of the module is tied to the
 * media player application through UUID and service name declarations,
 * along with media player specific methods.<br>
 * Potential for modular bluetooth management can be seen and will be
 * adjusted in future versions.
 * 
 * @author MIKUiqnw0
 */
public class BluetoothManager extends Activity implements OnClickListener {
	
	private static final int REQUESTENABLE = 1;
	private static final String SERVICE_NAME = "mediaplayer3d";
	private static final String UUID_STR = "d587304b-01b4-48f1-a0f2-0cc109df5486";
	private static BluetoothAdapter adapter;
	private BluetoothSocket client,
							host;
	private BroadcastReceiver receiver;

	@Override
	/**
	 * Calls a check to identify bluetooth capability before proceeding to
	 * enable bluetooth if disabled. 
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		checkAdapter();
		enableBluetooth();
	}
	
	/**
	 * Retrieves the class member holding a client socket.
	 * @return A reference to the client socket if assigned.
	 */
	public BluetoothSocket getClientSocket() {
		return client;
	}
	
	/**
	 * Retrieves the class member holding a host socket.
	 * @return A reference to the host socket if assigned.
	 */
	public BluetoothSocket getHostSocket() {
		return host;
	}
	
	/**
	 * Checks the device for bluetooth capability.<br>
	 * If the device does not have bluetooth capability, a dialog 
	 * alerting the user of this fact is shown before ending the activity.
	 */
	private void checkAdapter() {
		adapter = BluetoothAdapter.getDefaultAdapter();
		if(adapter == null) {
			Log.w(MediaPlayer3D.logTag, "checkAdapter() could not find bluetooth capability");
			AlertDialog dialog = new AlertDialog.Builder(this).create();
			dialog.setTitle("Error");
			dialog.setMessage("Your device does not support bluetooth.");
			dialog.show();
			finish();
		}
	}
	
	/**
	 * Verifies that the device has bluetooth enabled. Requests for the device
	 * to be enabled if not the case or moves the process along if it is.
	 */
	private void enableBluetooth() {
		if(!adapter.isEnabled()) {
			Log.i(MediaPlayer3D.logTag, "Bluetooth not enabled, requesting user enable");
			Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(intent, REQUESTENABLE);
		} else {
			queryUser();
		}
	}
	
	/**
	 * Displays a dialog requesting the users interaction with the activity.
	 */
	private void queryUser() {
		AlertDialog dialog = new AlertDialog.Builder(this).create();
		dialog.setTitle("Playlist Transfer");
		dialog.setMessage("If you are transfering to this device, please select Host.\nOtherwise, choose Client to discover other devices");
		dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Host", this);
		dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Client", this);
		dialog.show();
		Log.i(MediaPlayer3D.logTag, "Host/Client query dialog displayed");
	}
	
	/**
	 * Client method which initiates a discovery broadcast to other bluetooth devices.
	 * <p>
	 * TODO: Issue found on line 135, adapter.cancelDiscovery() ends discovery far to 
	 * 		  prematurely (only 1 device will be known). 
	 */
	private void discoverDevices() {
		receiver = new BroadcastReceiver() {
			private BluetoothSocket establishedSocket;
			
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
				
				if(BluetoothDevice.ACTION_FOUND.equals(action)) {
					Log.i(MediaPlayer3D.logTag, "discoverDevices() detected a bluetooth device");
					BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
					try {
						establishedSocket = device.createRfcommSocketToServiceRecord(UUID.fromString(UUID_STR));
					} catch (IOException e) {
						e.printStackTrace();
					}
					Log.i(MediaPlayer3D.logTag, "Secure BluetoothSocket created using internal UUID");
					adapter.cancelDiscovery();
					
					new Thread(new Runnable() {
						@Override
						public void run() {
							try {
								Log.i(MediaPlayer3D.logTag, "Establishing connection with hosting device...");
								establishedSocket.connect();
								client = establishedSocket;
								Log.i(MediaPlayer3D.logTag, "Connection with host successfully established");
							} catch (IOException e) {
								e.printStackTrace();
								Log.e(MediaPlayer3D.logTag, "Connection with host could not be established");
							}
						}
					}).start();
				}
			}
		};
		
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);		
		registerReceiver(receiver, filter);
		adapter.startDiscovery();
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(30000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				adapter.cancelDiscovery();
				unregisterReceiver(receiver);
			}
		}).start();
		
	}

	/**
	 * Host method which creates a secure socket and waits for a client
	 * connection.
	 */
	private void hostServer() {
		BluetoothServerSocket serverSocket = null;
		try {
			serverSocket = adapter.listenUsingRfcommWithServiceRecord(SERVICE_NAME, UUID.fromString(UUID_STR));	
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		new Thread(new AcceptRunnable(serverSocket)).start();
		client = AcceptRunnable.getClientSocket();
	}
	
	@Override
	/**
	 * Branches class interaction to host or client functionality.
	 */
	public void onClick(DialogInterface dialog, int which) {
		switch(which) {
		case DialogInterface.BUTTON_NEGATIVE:
			discoverDevices();
			break;
		case DialogInterface.BUTTON_POSITIVE:
			hostServer();
			break;
		}
	}
	
	@Override
	/**
	 * Tied with the enableBluetooth() function. Assess' the users interaction with the
	 * enable bluetooth activity and moves the process along to queryUser() if successful.<br>
	 * Ends the activity with an alert dialog if bluetooth could not be enabled.
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == REQUESTENABLE) {
			if(resultCode == RESULT_OK) {
				queryUser();
			} else if(resultCode == RESULT_CANCELED) {
				AlertDialog dialog = new AlertDialog.Builder(this).create();
				dialog.setTitle("Error");
				dialog.setMessage("Bluetooth is not enabled.");
				dialog.show();
				finish();
			}
		}
	}
}
