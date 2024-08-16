package com.example.tnebsafety;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 1;
    private TextView imeiTextView;
   private  String Userid="0" ;
   private String Username="0";
   private String Userrole="0";
    private String Circlename="0";
    private String Sectionname="0";
    private Retrofit retrofit;
    private int photoCount = 0;
    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton clicknavigate=findViewById(R.id.loginButton);
        ImageButton listimages=findViewById(R.id.loginButton3);
        String folderPath = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath();
        String fileName = "data.txt";
        String deletedirectoryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/TNEB SAFETY";

        deleteOldPhotos(deletedirectoryPath);

        String uploadirectoryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/TNEB SAFETY PENDING";
  uploadpendingphotos(uploadirectoryPath);

        File file = new File(folderPath, fileName);

        if (file.exists()) {

            try {
                // Specify the file path
                String filePath = folderPath+"/data.txt";

                // Open the file using FileInputStream
                FileInputStream fileInputStream = new FileInputStream(new File(filePath));

                // Read the file content into a byte array
                byte[] buffer = new byte[fileInputStream.available()];
                fileInputStream.read(buffer);
                // Convert the byte array to a string
                String json = new String(buffer).trim();
                json=json.replace("\"", "")   // Replace double quotes with an empty string
                        .replace("\\", "")   // Replace backslashes with an empty string
                        .replace("{", "")
                        .replace("}", "");

                String[] values = json.split(",");
                 Userid = values[0];
                 Username=values[1];
                 Userrole=values[2];
                 Circlename=values[3];
                 Sectionname=values[4];
                int Useridindex = Userid.indexOf(':');
                int Usernameindex = Username.indexOf(':');
                int Userroleindex = Userrole.indexOf(':');
                int Circlenameindex = Circlename.indexOf(':');
                int Sectionnameindex = Sectionname.indexOf(':');
                Userid=Userid.substring(Useridindex+1);
                Username=Username.substring(Usernameindex+1);
                Userrole=Userrole.substring(Userroleindex+1);
                Circlename=Circlename.substring(Circlenameindex+1);
                Sectionname=Sectionname.substring(Sectionnameindex+1);
                      // Close the file input stream
                fileInputStream.close();
            } catch (IOException  e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Error parsing JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

        } else {
            showToast("NEW USER LOGGED IN");// The file does not exist in the specified folder.
        }
        clicknavigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(MainActivity.this, homescreen.class);
                intent2.putExtra("Useridparam", Userid);
                intent2.putExtra("Usernameparam", Username);
                intent2.putExtra("Userroleparam",  Userrole);
                intent2.putExtra("Circlenameparam", Circlename);
                intent2.putExtra("Sectionnameparam", Sectionname);

                startActivity(intent2);
            }
        });
        listimages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(MainActivity.this, photolist.class);


                startActivity(intent2);
            }
        });


    }
    private void uploadpendingphotos(String directoryPath) {
        File directory = new File(directoryPath);
       if( isNetworkAvailable(this)) {
           if (directory.exists() && directory.isDirectory()) {
               File[] files = directory.listFiles();
               if (files != null) {


                   for (File file : files) {
                       RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
                       MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", file.getName(), requestBody);
                       String imagefilename=file.getName();



                       retrofit = RetrofitSingleton.getRetrofitInstance();
                       pendingphotoupload apiService = retrofit.create(pendingphotoupload.class);
                       Call<ResponseBody> call = apiService.uploadpendingImage(imagePart, requestBody,imagefilename);

                       call.enqueue(new Callback<ResponseBody>() {
                           @Override
                           public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                               // this method is called when we get response from our api.
                               if (response.isSuccessful()) {

                                   String messagevalue= null;
                                   try {
                                       messagevalue = response.body().string();
                                       Gson gson = new Gson();

                                       messagevalue=messagevalue
                                               .replace("\"","")
                                               .replace("appno:","")
                                               .replace("{","")
                                               .replace("}","");


                                       //                  showToast("PHOTOS UPLOADED TO SERVER");
                                       if(messagevalue.equals("success")) {
                                           if (file.delete()) {
                                               showToast("PHOTOS UPLOADED TO SERVER");

                                           }
                                           else {
                                               showToast("PHOTOS NOT MOVED FROM PENDING");
                                           }
                                       }

                                   } catch (IOException e) {
                                       throw new RuntimeException(e);
                                   }
                               } else {
                                   Toast.makeText(MainActivity.this, "FAILED TO SAVE", Toast.LENGTH_SHORT).show();


                               }
                           }

                           @Override
                           public void onFailure(Call<ResponseBody> call, Throwable t) {

                               String errorMessage = "An error occurred: " + t.getMessage();


                           }
                       });


                   }
               }
           }

       }
       else{
           if (directory.exists() && directory.isDirectory()) {
               File[] files = directory.listFiles();
               if (files != null) {


                   for (File file : files) {
                       if (isPhoto(file)) {
                           photoCount++;
                       }
                   }
                   if(photoCount>0){
                       showToast("LOCATION PHOTOS PENDING TO BE UPLOADED.TURN ON INTERNET TO UPLOAD");
                   }

               }
           }
       }

    }

    private void deleteOldPhotos(String directoryPath) {
        File directory = new File(directoryPath);

        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();

            if (files != null) {
                for (File file : files) {
                    // Check if the file is an image and modified more than one week ago
                    if (file.isFile() && file.getName().endsWith(".png") && isFileOlderThanOneWeek(file)) {
                        if (file.delete()) {
                            Log.d("Deletion", "Deleted: " + file.getAbsolutePath());
                        } else {
                            Log.e("Deletion", "Failed to delete: " + file.getAbsolutePath());
                        }
                    }
                }
            }
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

        private boolean isFileOlderThanOneWeek(File file) {
            long oneWeekInMillis = 7 * 24 * 60 * 60 * 1000L; // One week in milliseconds
            long currentTime = System.currentTimeMillis();

            return currentTime - file.lastModified() > oneWeekInMillis;
        }
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            Network network = connectivityManager.getActiveNetwork();
            if (network != null) {
                NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);
                return networkCapabilities != null &&
                        (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET));
            }
        }
        return false;
    }
    private static boolean isPhoto(File file) {
        String name = file.getName().toLowerCase();
        return name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png") || name.endsWith(".gif");
        // Add more extensions as needed
    }
    private void uploadPhoto(File photoFile){


    }
}