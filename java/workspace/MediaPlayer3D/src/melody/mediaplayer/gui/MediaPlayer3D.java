package melody.mediaplayer.gui;

import java.io.File;

import melody.mediaplayer.bluetooth.BluetoothManager;
import melody.mediaplayer.database.MediaDatabaseManager;
import melody.mediaplayer.file.FileDialog;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.FeatureInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.mediaplayer3d.R;


/**
 * MediaPlayer3D.java<br>
 * Main, entry class of the media player activity.<br>
 * Manages most UI selection events and elements as well as instructions.
 * 
 * @author MIKUiqnw0
 */
public class MediaPlayer3D extends Activity { 
	
	// Development mode flag
	private static final boolean devflag = false;
	public static final String logTag = "melody.mediaplayer";
	private final MediaDatabaseManager database = new MediaDatabaseManager(this);
	private final FileDialog fileDialog = new FileDialog(this, this);

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_media_player3_d);
	}
	

	@Override
	/**
	 * Starts a feature check only if development mode is flagged true.
	 */
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		if(devflag) {
			featureCheck();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.media_player3_d, menu);
		return true;
	}


	@Override
	/**
	 * Directs the flow of the users selection to the relevant
	 * methods.
	 */
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.i(MediaPlayer3D.logTag, "User selected item: " + item.getItemId());
		switch(item.getItemId()) {
		case R.id.about:
			return true;
			
		case R.id.browse:
			new Thread(new Runnable() {
				@Override
				public void run() {
					File temp = fileDialog.waitOnFile();
					if(temp != null) {
						database.addTrack(temp);
					}
				}
			}).start();
			fileDialog.browseDialog();
			// Track play
			return true;
			
		case R.id.tracks:
			// Track db browse
			return true;
			
		case R.id.transfer:
			Intent intent = new Intent(this, BluetoothManager.class);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	/**
	 * Invoked by view button "Play" which starts the music track from the current position
	 * @param view The button view from which this function was called.
	 */
	public void playClick(View view) {
		Log.i(MediaPlayer3D.logTag, "Play button pressed");
	}
	
	/**
	 * Invoked by view button "Stop" which stops and rewinds the music track to the beginning.
	 * @param view The button view from which this function was called.
	 */
	public void stopClick(View view) {
		Log.i(MediaPlayer3D.logTag, "Stop button pressed");
	}
	
	/**
	 * Invoked by view button "Pause" which stops the music track at its current position.
	 * @param view The button view from which this function was called.
	 */
	public void pauseClick(View view) {
		Log.i(MediaPlayer3D.logTag, "Pause button pressed");
	}
	
	/**
	 * Impromtu device feature listing in order to view it's capabilities.
	 */
	private void featureCheck() {
		Log.i(MediaPlayer3D.logTag, "Development mode is active, listing device features...");
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		String featureStr = new String();
		
		FeatureInfo[] list = getPackageManager().getSystemAvailableFeatures();
		for(FeatureInfo feature : list) {
			featureStr = featureStr.concat(feature.name + "\n");
		}
		
		dialog.setTitle("Feature Check");
		dialog.setMessage(featureStr);
		dialog.show();
	}
	
	/**
	 * Static accessible called to determine development mode.
	 * @return A boolean determining the Activity's development mode status.
	 */
	public static boolean isDevMode() {
		return devflag;
	}
	
}
