package melody.mediaplayer.bluetooth;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import android.bluetooth.BluetoothSocket;

/**
 * PlaylistTrasfer.java
 * Utility host / client class which transfers and read in playlist
 * flatfiles.
 * @author MIKUiqnw0
 */
public class PlaylistTransfer {

	/**
	 * Client transfer method which sends their playlist flatfile to the host.
	 * @param client The client BluetoothSocket to transfer from.
	 */
	public static void transferPlaylist(BluetoothSocket client) {		
		try {
			OutputStream out = client.getOutputStream();
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
			writer.write("Sample BT transfer");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Hosting reciever method which reads in data from the client.
	 * @param host The host BluetoothSocket to read data into.
	 */
	public static void readStream(final BluetoothSocket host) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				char[] temp = null;
				InputStream in;
				try {
					in = host.getInputStream();
					BufferedReader reader = new BufferedReader(new InputStreamReader(in));
					reader.read(temp);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
}
