package melody.mediaplayer.bluetooth;

import java.io.IOException;

import melody.mediaplayer.gui.MediaPlayer3D;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

/**
 * AcceptRunnable.java<br>
 * Threaded implementation for obtaining a connectable client used in conjuction 
 * with the server hosting method invoked in the BluetoothManager class.
 * @author MIKUiqnw0
 */
class AcceptRunnable implements Runnable {
	private BluetoothServerSocket threadServerSocket;
	private static BluetoothSocket clientSocket;
	
	/**
	 * Constructor requesting a BluetoothServerSocket
	 * @param serverSock The server socket to begin accepting connections with.
	 */
	AcceptRunnable(BluetoothServerSocket serverSock) {
		threadServerSocket = serverSock;
	}
	
	/**
	 * Static implementation to retrieve the client socket after the host acquires
	 * a connection.
	 * @return A connected reference to the client socket.
	 */
	public static synchronized BluetoothSocket getClientSocket() {
		Log.i(MediaPlayer3D.logTag, "Blocking method 'wait()' called in getClientSocket()");
		while(clientSocket == null) {
			try {
				clientSocket.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		Log.i(MediaPlayer3D.logTag, "getClientSocket continued");
		return clientSocket;
	}
	
	@Override
	/**
	 * Executes the accept() blocking method from the socket. Assigns to the
	 * clientSocket member before allowing getClientSocket() to continue operation
	 * and closing the server socket thread.
	 */
	public void run() {
		Log.i(MediaPlayer3D.logTag, "Blocking method 'accept()' called by run() in AcceptRunnable.class");
		try {
			synchronized(clientSocket) {
				clientSocket = threadServerSocket.accept();
				Log.i(MediaPlayer3D.logTag, "accept() found a client");
				clientSocket.notify();
				threadServerSocket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
