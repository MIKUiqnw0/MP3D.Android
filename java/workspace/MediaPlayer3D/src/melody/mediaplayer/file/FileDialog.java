package melody.mediaplayer.file;

import java.io.File;

import melody.mediaplayer.file.MediaControls.NoExternalMediaException;
import melody.mediaplayer.gui.MediaPlayer3D;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.util.Log;

/**
 * FileDialog.java<br>
 * Handles user navigation and selection of files from external media 
 * using the AlertDialog interface. Adaption for internal storage would
 * require modifying the media checks made in the constructor and the
 * browseDialog() function.
 * <p>
 * This class depends on the MediaControls class for verification.<br>
 * TODO: Verify selection as appropriate media
 * 
 * @author: MIKUiqnw0
 */

public class FileDialog implements OnClickListener {
	
	private boolean exitCase;
	private Object lock;
	private File selectedFile;
	private static File SD_PATH;
	private String[] fileList;
	private String dirLevel;
	private AlertDialog dialogHandle;
	
	private final Context appContext;
	private final Activity appActivity;

	/** 
 	 * Requires the application context and activity as parameters
 	 * in order to utilise the UI thread and the application context
 	 * for dialog display.
	 * 
	 * @param appContext The application context
	 * @param appActivity The application activity
	 */
	public FileDialog(Context appContext, Activity appActivity) {
		this.appContext =  appContext;
		this.appActivity = appActivity;
		dirLevel = "/";
		selectedFile = null;
		SD_PATH = MediaControls.getExternalPath();
		fileList = SD_PATH.list();
	}
	
	/**
	 * Entry method for displaying the dialog interface to the
	 * user. 
	 */
	public void browseDialog() {

		/*appActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {*/
		Log.i(MediaPlayer3D.logTag, "Rechecking external storage...");
		try {
			MediaControls.externalMediaCheck();
			Log.i(MediaPlayer3D.logTag, "External storage still valid");
			identifyDirectories(fileList);
			dialogHandle = createDialog();
			dialogHandle.show();
			Log.i(MediaPlayer3D.logTag, "File browser displayed");
		} catch(NoExternalMediaException e) {
			e.printStackTrace();
		}
			/*}
		});*/
	}
	
	/**
	 * A blocking method which waits on the dialog to finish it's
	 * operation. Should be called after creating the dialog.
	 * 
	 * @return A reference to the user selected file in the user's
	 * 		   external media OR null if the dialog is canceled.
	 */
	public File waitOnFile() {
		exitCase = false;
		lock = new Object();
		
		synchronized(lock) {
			while(!exitCase) {		
				try {
					Log.w(MediaPlayer3D.logTag, "Blocking method 'wait()' called in FileDialog by waitOnFile");
					lock.wait();
					Log.i(MediaPlayer3D.logTag, "waitOnFile continued");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			Log.i(MediaPlayer3D.logTag, "waitOnFile returned " + ((selectedFile != null) ? selectedFile.getAbsolutePath(): "null"));
			return selectedFile;
		}
	}
	
	/**
	 * Creates the file browsing dialog with appropriate button format
	 * depending on directory level.
	 * 
	 * @return An alert dialog with list of directories and valid button
	 * 			dialog control buttons.
	 */
	private AlertDialog createDialog() {
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(appContext);
		
		dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int choice) {
				Log.i(MediaPlayer3D.logTag, "Cancel button on FileDialog pressed");
				fileList = SD_PATH.list();
				dirLevel = "/";
				exitCase = true;
				synchronized(lock) {
					lock.notify();
				}
			}
		});
		
		if(!dirLevel.equals("/")) {
			dialogBuilder.setNeutralButton("Back", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int choice) {
					Log.i(MediaPlayer3D.logTag, "Back button on FileDialog pressed");
					String[] temp = dirLevel.split("/");
					String reconstruct = new String();
					for(int i = 0; i < temp.length - 1; ++i) {
						reconstruct = reconstruct.concat(temp[i] + "/");
					}
					dirLevel = reconstruct;
					identifySelection(-1);
				}
			});
		}
		
		dialogBuilder.setTitle("Select a File:");		    
		dialogBuilder.setItems(fileList, this);
		return dialogBuilder.create();
	}
	
	/** 
	 * Identifies directories within a list of files and applies a forward
	 * slash suffix to the end of the directory name.
	 * 
	 * @param fileList A string array containing the file / directory names
	 * 		   			within a directory.
	 */
	private void identifyDirectories(String[] fileList) {
		for(int i = 0; i < fileList.length; ++i) {
			File absFile = new File(SD_PATH.getAbsolutePath() + dirLevel + fileList[i]);
			if(absFile.isDirectory()) {
				fileList[i] = fileList[i].concat("/");
			}
		}
	}
	
	/**
	 * Identifies whether the file selected by the user is actually a file
	 * or a directory. If the file is a directory, it will traverse and repeat
	 * the dialog creation process.
	 * 
	 * @param index A flag used to identify if the function was called with an
	 * 				 index in mind to appropriately assign the temporary file.
	 * 				 <p>
	 * 			     An index < 0 assumes the call was made from the back
	 * 				 button event
	 */
	private void identifySelection(int index) {
		File tempSelect = null;
		
		if(index < 0) {
			tempSelect = new File(SD_PATH.getAbsolutePath() + dirLevel);
		} else { 
			tempSelect = new File(SD_PATH.getAbsolutePath() + dirLevel + fileList[index]);
		}
		
		if(tempSelect.isDirectory()) {
			if(index >= 0) {
				dirLevel = dirLevel.concat(tempSelect.getName() + "/");
			}
			fileList = tempSelect.list();
			browseDialog();
		} else if(tempSelect.isFile()) {
			selectedFile = tempSelect;
			fileList = SD_PATH.list();
			dirLevel = "/";
			exitCase = true;
			synchronized(lock) {
				lock.notify();
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 * Listener function which passes the index of the user's selection to
	 * a checking method.
	 */
	@Override
	public void onClick(DialogInterface dialog, int index) {
		identifySelection(index);
	}
}
