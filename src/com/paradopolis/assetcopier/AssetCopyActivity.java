package com.paradopolis.assetcopier;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

/**
 * This activity has been detatched from the application via the manifest.
 * It currently has no launcher and will never run.
 */
public class AssetCopyActivity extends Activity{
	private static final String TAG = AssetCopyActivity.class.getSimpleName();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "Launched activity!");
		finish();
	}

}
