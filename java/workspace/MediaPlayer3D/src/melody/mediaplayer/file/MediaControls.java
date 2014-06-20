package melody.mediaplayer.file;

import java.io.File;

import melody.mediaplayer.gui.MediaPlayer3D;

import android.os.Environment;
import android.util.Log;

/**
 * MediaControls.class
 * Utility class for external media verification.
 * @author MIKUiqnw0
 */
public class MediaControls {

	static class NoExternalMediaException extends Exception {
		@Override
		public String getMessage() {
			final String message = "External media is not mounted.";
			return message;
		}
	}
	/**
	 * Checks the device for inserted external storage.
	 * @return true if external media has been detected on the device<br>
	 * 		   false if no external media is found.
	 */
	static public void externalMediaCheck() throws NoExternalMediaException {
		Log.i(MediaPlayer3D.logTag, "externalMediaCheck checking for external storage...");
		final String state = Environment.getExternalStorageState();
		if(!state.equals(Environment.MEDIA_MOUNTED) ||
		   !state.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
			Log.i(MediaPlayer3D.logTag, "externalMediaCheck found external storage.");
			return;
		} else {
			Log.w(MediaPlayer3D.logTag, "externalMediaCheck failed to locate external storage");
			throw new NoExternalMediaException();
		}
	}
	
	/**
	 * Retrieves the root path of the the external storage.
	 * @return File reference to external storage
	 */
	static public File getExternalPath() {
		try {
			externalMediaCheck();
			return Environment.getExternalStorageDirectory();
		} catch(NoExternalMediaException e) {
			e.printStackTrace();
		}
		Log.w(MediaPlayer3D.logTag, "Failed to getExternalPath, null File reference returned");
		return null;
	}
}
