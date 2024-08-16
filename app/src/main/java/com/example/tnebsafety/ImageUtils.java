package com.example.tnebsafety;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;


public class ImageUtils {

    public static void listImagesFromPicturesFolder(Context context, String subfolderName) {
        // Set the time range for the past one week
        long currentTime = System.currentTimeMillis();
        long oneWeekAgo = currentTime - (7 * 24 * 60 * 60 * 1000);

        // Construct the base path to the "Pictures" directory
        String picturesDirectory = Environment.getExternalStorageDirectory() + "/Pictures/";

        // Create a query for images in the specified folder
        String[] projection = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA, MediaStore.Images.Media.DATE_TAKEN};
        String selection = MediaStore.Images.Media.DATE_TAKEN + ">?" + " AND " + MediaStore.Images.Media.DATA + " LIKE ?";
        String[] selectionArgs = {String.valueOf(oneWeekAgo), "%" + subfolderName + "%"};
        String sortOrder = MediaStore.Images.Media.DATE_TAKEN + " DESC";

        // Perform the query
        ContentResolver contentResolver = context.getContentResolver();
        Uri queryUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = contentResolver.query(queryUri, projection, selection, selectionArgs, sortOrder);

        if (cursor != null) {
            int idColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
            int pathColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
            int dateTakenColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN);

            while (cursor.moveToNext()) {
                // Get image details
                long imageId = cursor.getLong(idColumnIndex);
                String imagePath = cursor.getString(pathColumnIndex);
                long dateTaken = cursor.getLong(dateTakenColumnIndex);

                // Do something with the image details (e.g., add to a list, display, etc.)
                // ...

                // Example: Log the details
                System.out.println("Image ID: " + imageId);
                System.out.println("Image Path: " + imagePath);
                System.out.println("Date Taken: " + dateTaken);
            }

            cursor.close();
        }
    }
}
