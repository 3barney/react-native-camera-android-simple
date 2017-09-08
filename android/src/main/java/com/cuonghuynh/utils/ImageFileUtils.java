package com.cuonghuynh.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by cuong.huynh on 6/2/17.
 */

public class ImageFileUtils {
    private static final String TAG = ImageFileUtils.class.getSimpleName();
		private static final String NOMEDIA=".nomedia";

    public static void saveBitmapToFile(Bitmap bitmap, File file) {
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if(fos != null) {
                    fos.close();
                }
            } catch (IOException e) {}
        }

    }

    public static File getImageOutputFile() {
				File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "Smala");

        // Create the storage directory if it does not exist
        if (!storageDir.exists()) {
            if (!storageDir.mkdirs()) {
                Log.e(TAG, "failed to create directory:" + storageDir.getAbsolutePath());
                return null;
            } else {
								File noMediaFile = new File(storageDir + "/" + NOMEDIA);
		              if (!noMediaFile.exists()) {
		                  try {
		                      boolean newFile = noMediaFile.createNewFile();
		                      Log.i(TAG, ".no media File creation: " + newFile);
		                  } catch (IOException e) {
		                      Log.e(TAG, "failed to create File:" + e.toString());
		                  }
		              }
						}
        } else {
						// storage Dir exists
            File noMediaFile = new File(storageDir + "/" + NOMEDIA);
            if (!noMediaFile.exists()) {
                try {
                    boolean newFile = noMediaFile.createNewFile();
                    Log.i(TAG, ".no media File creation: " + newFile);
                } catch (IOException e) {
                    Log.e(TAG, "failed to create File:" + e.toString());
                }
            }
				}

        // Create a media file name
        String fileName = String.format("%s", new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()));
        fileName = String.format("IMG_%s.jpg", fileName);

        return new File(String.format("%s%s%s", storageDir.getPath(), File.separator, fileName));
    }

    public static void addToGalleryAndNotify(Context context, File imageFile, final Promise promise) {
        final WritableMap response = new WritableNativeMap();
        response.putString("path", Uri.fromFile(imageFile).toString());

        promise.resolve(response);
    }

    public static String validatePath(String filePath) {
        // only accept file path.
        if(filePath.startsWith("file:/")) {
            return filePath.substring("file:/".length());
        }

        return filePath;
    }
}
