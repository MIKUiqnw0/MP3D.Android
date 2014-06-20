package melody.mediaplayer.database;

import java.io.File;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * MediaDatabaseManager.java<br>
 * Handles internal database interactions using SQLite by creating, updating 
 * and querying the database and its tables. 
 * Also caches file locations for tracks and stores playlists.<br>
 * Project tailoring would require the DB_NAME and DB_VERSION be changed.
 * <p>
 * TODO: Playlist functionality, proper metadata extraction<br>
 * Targets various UI and background elements and may raise crosscutting concerns.
 * @author:  MIKUiqnw0
 */
public class MediaDatabaseManager extends SQLiteOpenHelper {

	// Database identifiers
	private static final int DB_VERSION = 1;
	private static final String DB_NAME = "MediaPlayer3D";
	
	// Table constants
	private static final String TABLE_TRACKS = "tracks";
	private static final String TRACKS_ID = "track_id";
	private static final String TRACKS_TITLE = "title";
	private static final String TRACKS_FILE = "file_path";
	
	private static final String TABLE_PLAYLISTS = "playlists";
	private static final String PLAYLISTS_ID = "pl_id";
	private static final String PLAYLISTS_NAME = "name";
	private static final String PLAYLISTS_TRACKS = "tracks_list";
	
	/**
	 * Default SQLiteOpenHelper constructer
	 * @param context The application context
	 */
	public MediaDatabaseManager(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}
	
	/**
	 * Creates the initial tables required for this application
	 * It is executed during initialization of the object
	 * @param db The datatbase created on the intialization of this object
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		// Create Tracks Table
		db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY, %s TEXT, %s TEXT)", TABLE_TRACKS, TRACKS_ID, TRACKS_TITLE, TRACKS_FILE));
		// Create Playlist Table
		db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY, %s TEXT)", TABLE_PLAYLISTS, PLAYLISTS_ID, PLAYLISTS_NAME));
	}
	
	/**
	 * adds a file location reference to the database
	 * @param handle The handle on the file which will be used to retrieve the path
	 */
	public void addTrack(File handle) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(TRACKS_TITLE, "temp");
		values.put(TRACKS_FILE, handle.getAbsolutePath());
		db.insert(TABLE_TRACKS, null, values);
		db.close();
	}
	
	/**
	 * Removes a file reference from the database
	 */
	public void removeTrack() {
		
	}
	
	public void findTrack() {
		
	}
	/**
	 * Creates an empty playlist entry in the playlist table
	 */
	public void createPlaylist(String playlistName) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(TRACKS_TITLE, playlistName);
		db.insert(TABLE_PLAYLISTS, null, values);
		db.close();
	}
	
	/**
	 * Deletes a playlist entry from the playlist table
	 * @param playlistName The target playlist to delete from the database
	 */
	public void deletePlaylist(String playlistName) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_PLAYLISTS, String.format("%s == %s", PLAYLISTS_NAME, playlistName), null);
		db.close();
	}
	
	/**
	 * Adds a track reference to the tracks_list field of a specified playlist
	 * @param playlistName The target playlist to add a track to
	 */
	public void playlistAdd(String playlistName) {}
	
	/**
	 * Removes a track reference from the tracks_list field of a specified playlist
	 * @param playlistName The target playlist to remove a track from 
	 */
	public void playlistRemove(String playlistName) {}
	
	/**
	 * Retrieve all playlists from the database
	 * @return An ArrayList of playlist names
	 */
	public ArrayList<?> getAllPlaylists() {
		return null;
	}
	
	/**
	 * Retrieve a specific playlist from the database
	 * @param playlistName The target playlist to retrieve from the database
	 */
	public void getPlaylist(String playlistName) {}
	
	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {}
	
}
