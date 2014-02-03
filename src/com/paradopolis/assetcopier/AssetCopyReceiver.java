package com.paradopolis.assetcopier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;

public class AssetCopyReceiver extends BroadcastReceiver{


	private static final String TAG = AssetCopyReceiver.class.getSimpleName();
	private static final boolean DEBUG = true;
	private static final String PATH_SDCARD = "sdcard";
	private static final String PREF_FIRST_RUN = "first_run";
	private File mSdCard;

	@Override
	public void onReceive(Context context, Intent intent) {
		if (DEBUG) Log.d(TAG, "Received action: " + intent.getAction());
		// TODO Would be nice to find a way to clear shared preferences on reinstall, 
		// instead of just uninstall
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		if (prefs.getBoolean(PREF_FIRST_RUN, false)) {
			Log.v(TAG, "Not copying assets - already ran.");
		}
		mSdCard = Environment.getExternalStorageDirectory();
		if (mSdCard == null) {
			Log.e(TAG, "Could not get access to sdcard.");
		}
		copyAssets(context);
		prefs.edit().putBoolean(PREF_FIRST_RUN, true);
		prefs.edit().commit();
	}



	private void copyAssets(Context context) {
		// We must use a subdirectory because Android implicitly adds
		// extra directories when using an empty string
		copyDirectory(PATH_SDCARD, context);
	}

	private void copyDirectory(String root, Context context) {
		// Get a handle to all the assets included.
		AssetManager assetManager = context.getAssets();
		String[] files = null;
		try {
			files = assetManager.list(root);
		} catch (IOException e) {
			Log.e(TAG, "Failed to get asset file list.", e);
		}
		if (DEBUG) Log.d(TAG, "************");
		if (DEBUG) Log.d(TAG, "Files to copy in " + root + ": " + Arrays.toString(files));
		for(String fileName : files) {
			String filePath = root + "/" + fileName;
			InputStream inputStream = null;
			OutputStream outputStream = null;
			try { 
				inputStream = assetManager.open(filePath);
			} catch(IOException e) {
				// Asset could not be opened, is likely a directory.
				if (DEBUG) Log.w(TAG, "Could not open: " + filePath);
				// Treat the file as a directory and attempt to copy it's contents
				copyDirectory(filePath, context);
			} 
			// If input was not set, then continue to the next found file.
			if (inputStream == null) {
				continue;
			}
			try {
				// If we are here then we have an actual file, lets copy it.
				filePath = trimSdcard(filePath);
				File file = new File(mSdCard+filePath);
				if (DEBUG) Log.d(TAG, "Copying file: " + file.getAbsolutePath());
				outputStream = new FileOutputStream(file);
				copyFile(inputStream, outputStream);
				inputStream.close();
				inputStream = null;
				outputStream.flush();
				outputStream.close();
				outputStream = null;
			} catch (Exception e) {
				// Huh... guess something bad happened...
				e.printStackTrace();
			}
		}
	}

	/**
	 * This just gets rid of our PATH_SDCARD constant so that we can
	 * put match the files in sdcard to where they should go on the device.
	 * @param string - File to trim
	 * @return - The string minus the PATH_SDCARD or else the string if no
	 * 			 path is found.
	 */
	private String trimSdcard(String string) {
		if (string.startsWith(PATH_SDCARD)) {
			return string.substring(PATH_SDCARD.length());
		}
		return string;
	}

	/**
	 * This copies the actual file from our input to our output stream.
	 * @param in
	 * @param out
	 * @throws IOException
	 */
	private void copyFile(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		int read;
		while((read = in.read(buffer)) != -1){
			out.write(buffer, 0, read);
		}
	}

}
