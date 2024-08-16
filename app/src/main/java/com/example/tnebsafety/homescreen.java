package com.example.tnebsafety;
import android.Manifest;
import android.content.Context;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;



import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import android.content.ContentValues;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.location.LocationRequest;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;



import java.io.ByteArrayOutputStream;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class homescreen extends AppCompatActivity {
String selectedcircle;
int selectedcircleint;
private String usernamevalue;
private String userrolevalue;
private String circlenamevalue;
private String sectionnamevalue;
private String staffnamevalue;
private String workdescriptionvalue;
    private String Usermaintvalue;
    private Retrofit retrofit;
    private String latitudevalue;
    private String longitudevalue;
    private String accuracyvalue;
    private float accuracy;
    private double latitude;
    private double longitude;
    private String  selectedValue;
    private Spinner spinner1;
    private Spinner spinner2;
    private List<String> csv1Values;
    private List<String> csv2Values;
    private List<Bitmap> capturedPhotos = new ArrayList<>();
    private Bitmap photo;
    private Bitmap photo2;
    private String timestampstring;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 1;
private String Useridvalue;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    private static final int REQUEST_CODE = 123;
    private static final String TAG = "SaveJsonToFileActivity";
    private WebView webView;
    private ImageView photoImageView;
    private ImageView photoImageView2;
    private ActivityResultLauncher<Intent> cameraLauncher;
    private String Useridfortext;
    private File photoFile;
    private String Usernamefortext;
    private String Userrolefortext;
    private String Cirnamefortext;
    private String Secnamefortext;
    private Spinner spinnerselect;
    private String latvalueselect;
    private String lngvalueselect;
    private String accvalueselect;
    private ProgressBar progressBar;
    private int incrementalno=0;
    private Button btncapture3;
    private  String imageFileName;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private Uri photoUri;
    private static final int CREATE_FILE_REQUEST_CODE = 1;
    private ActivityResultLauncher<String> createDocumentLauncher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);

        Intent intent = getIntent();
        usernamevalue=intent.getStringExtra("Usernameparam");
      userrolevalue=intent.getStringExtra("Userroleparam");
     circlenamevalue=intent.getStringExtra("Circlenameparam");
     sectionnamevalue=intent.getStringExtra("Sectionnameparam");
    Useridvalue=intent.getStringExtra("Useridparam");


        spinnerselect = findViewById(R.id.spinner2);
        spinner1 = findViewById(R.id.spinner6);
        spinner2 = findViewById(R.id.spinner7);
        EditText username = findViewById(R.id.editTextText2);
        EditText staffname = findViewById(R.id.editTextText3);
        EditText workdesc = findViewById(R.id.editTextText4);
        Button btncapture1 = findViewById(R.id.button4mapscreen);
        Button btncapture2 = findViewById(R.id.button4mapscreen2);
         btncapture3 = findViewById(R.id.button4mapscreensubmit);
        photoImageView = findViewById(R.id.photoImageView);
        photoImageView2 = findViewById(R.id.photoImageView2);
         progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        if(Useridvalue.equals("0")) {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                    this,
                    R.array.userrole,
                    android.R.layout.simple_spinner_item
            );
            String[] items = getResources().getStringArray(R.array.userrole);
           ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerselect.setAdapter(adapter2);

            // Read data from CSV file and populate the Spinner
            csv1Values = readCSVFromRawResource(this, R.raw.circle, 0);
            csv2Values = readCSVFromRawResourcefilter(this, R.raw.section);

            ArrayAdapter<String> adaptercircle = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, csv1Values);
            adaptercircle.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner1.setAdapter(adaptercircle);
            String[] defaultItem = {"SELECT SECTION NAME"};
            ArrayAdapter<String> adaptersec = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, defaultItem);

// Set the dropdown view resource to use (optional)
            adaptersec.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

// Set the adapter to the Spinner
            spinner2.setAdapter(adaptersec);

            spinner1.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
                public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                    selectedValue = csv1Values.get(position);
                    selectedValue = selectedValue.substring(1, 4);

                    List<String> filteredValues = filterCSV2(selectedValue);
                    updateSpinner2(filteredValues);
                }

                public void onNothingSelected(android.widget.AdapterView<?> parent) {
                }
            });
        }
        else {
            username.setText(usernamevalue);
            String[] items = {userrolevalue};
            ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerselect.setAdapter(adapter2);
            String[] circleitems = {circlenamevalue};
            ArrayAdapter<String> adaptercircle = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, circleitems);
            adaptercircle.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner1.setAdapter(adaptercircle);
            String[] defaultItem = {sectionnamevalue};
            ArrayAdapter<String> adaptersec = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, defaultItem);
            adaptersec.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adaptersec);
        }

        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            handleCameraResult();
                        } else {
                            Toast.makeText(homescreen.this, "Failed to capture photo", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Location location = locationResult.getLastLocation();
                if (location != null) {
                    latitude = location.getLatitude();
                    latitudevalue = Double.toString(latitude);
                    longitude = location.getLongitude();
                    longitudevalue = Double.toString(longitude);
                    accuracy = location.getAccuracy();
                    accuracyvalue = Float.toString(accuracy);
                    // coordinatesTextView.setText("Latitude: " + latitude + "\nLongitude: " + longitude);

                }

            }

        };
        btncapture1.setOnClickListener(v -> {
            // Perform logout actions here
            // For example, navigating back to MainActivity
            requestCameraPermission();
            //openCamera();
            // sendDataToServer(receivedValue,latitudevalue,longitudevalue,accuracyvalue);
        });
        btncapture2.setOnClickListener(v -> {
            // Perform logout actions here
            // For example, navigating back to MainActivity
            requestCameraPermission();
            //openCamera();
            // sendDataToServer(receivedValue,latitudevalue,longitudevalue,accuracyvalue);
        });

        btncapture3.setOnClickListener(v -> {
            if (isNetworkAvailable(homescreen.this)) {
                usernamevalue = username.getText().toString();
                userrolevalue = (String) spinnerselect.getSelectedItem();
                circlenamevalue = (String) spinner1.getSelectedItem();
                sectionnamevalue = (String) spinner2.getSelectedItem();

                staffnamevalue = staffname.getText().toString();
                workdescriptionvalue = workdesc.getText().toString();
                if (usernamevalue.isEmpty()) {
                    String message = "User Name must be entered";
                    showToast(message);
                } else if (userrolevalue.isEmpty() || userrolevalue.equals("SELECT ROLE")) {
                    String message = "User Role must be selected";
                    showToast(message);
                } else if (circlenamevalue.isEmpty() || circlenamevalue.equals("SELECT CIRCLE NAME")) {
                    String message = "Circle Name must be selected";
                    showToast(message);
                } else if (staffnamevalue.isEmpty()) {
                    String message = "Staff Name who is on structure must be entered";
                    showToast(message);
                } else if (workdescriptionvalue.isEmpty()) {
                    String message = "Work description must be entered";
                    showToast(message);
                } else if (capturedPhotos.size() <= 1) {
                    String message = "Both pictures need to be captured";
                    showToast(message);
                }
                else if(latvalueselect.isEmpty() || lngvalueselect.isEmpty() ) {
                    String message = "LOCATION FEATURE NOT TURNED ON";
                    showToast(message);
                }
                else {

                    progressBar.setVisibility(View.VISIBLE);
                    movepictures(usernamevalue, userrolevalue, circlenamevalue, sectionnamevalue, staffnamevalue, workdescriptionvalue, capturedPhotos.get(0), capturedPhotos.get(1), timestampstring, latvalueselect, lngvalueselect, accvalueselect, Useridvalue);
                }
            }
            else {
                staffnamevalue = staffname.getText().toString();
                workdescriptionvalue = workdesc.getText().toString();
                savepicturestolocal2(capturedPhotos.get(0),capturedPhotos.get(1));
            }
        });

    }
    private String getFileNameFromPath(String path) {
        File file = new File(path);
        return file.getName();
    }
    private void requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            requestStoragePermission();
        }
    }
    private void requestStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && !Environment.isExternalStorageManager()) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        } else {
            capturePhoto();
        }
    }
    private void capturePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
       // if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
          setupPhotoFile();
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            cameraLauncher.launch(takePictureIntent);
        //} else {
          //  Toast.makeText(this, "Camera app not found", Toast.LENGTH_SHORT).show();
       // }
    }
    private void setupPhotoFile() {
        incrementalno=incrementalno+1;
        imageFileName = "test_" + incrementalno;
        File storageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "tempsafety");

        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DISPLAY_NAME, imageFileName + ".jpg");
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/tempsafety");
        showToast(imageFileName);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Uri externalContentUri = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
            photoUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        } else {
            File photoFile = new File(storageDir, imageFileName + ".jpg");
            photoUri = FileProvider.getUriForFile(this, "com.example.android.fileprovider", photoFile);
        }
    }
    private void handleCameraResult() {
        File storageDir2 = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "tempsafety/" + imageFileName + ".jpg");

        if (storageDir2.exists()) {
            try {
                // Use the photoUri directly to create a Bitmap
                InputStream inputStream = getContentResolver().openInputStream(photoUri);
                Bitmap photo = BitmapFactory.decodeStream(inputStream);

                if (photo != null) {
                    photo = rotateBitmap(photo, 90);
                    capturedPhotos.add(photo);

                    photoImageView.setImageBitmap(capturedPhotos.get(0));

                    if (capturedPhotos.size() >= 2) {
                        photoImageView2.setImageBitmap(capturedPhotos.get(1));
                        latvalueselect = String.valueOf(latitudevalue);
                        lngvalueselect = String.valueOf(longitudevalue);
                        accvalueselect = String.valueOf(accuracyvalue);
                        long timestamp = System.currentTimeMillis();
                        timestampstring = String.valueOf(timestamp);
                    }
                }
            } catch (FileNotFoundException e) {
                Toast.makeText(this, "File not found: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "File does not exist", Toast.LENGTH_SHORT).show();
        }
    }

    private List<String> readCSVFromRawResource(Context context, int resourceId, int columnIndex) {
        List<String> values = new ArrayList<>();

        try {
            Resources resources = context.getResources();
            InputStream inputStream = resources.openRawResource(resourceId);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Assuming CSV values are separated by commas
                String[] parts = line.split(",");
                if (parts.length > columnIndex) {
                    // Extract the value from the specified column index
                    String columnValue = parts[columnIndex].trim();
                    values.add(columnValue);
                }
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return values;
    }
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private List<String> filterCSV2(String filterValue) {

        List<String> filteredValues = new ArrayList<>();
        for (String line : csv2Values) {
            String[] parts = line.split(",");
            if (parts.length > 1 && parts[1].trim().equals(filterValue)) {
                if (parts.length > 0) {
                    filteredValues.add(parts[0].trim());
                }
                else {
                    filteredValues.add("SELECT SECTION NAME");
                }
            }
        }
        return filteredValues;
    }

    private void updateSpinner2(List<String> values) {
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, values);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
    }

    private List<String> readCSVFromRawResourcefilter(Context context, int resourceId) {
        List<String> values = new ArrayList<>();

        try {
            Resources resources = context.getResources();
            InputStream inputStream = resources.openRawResource(resourceId);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                values.add(line);
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return values;
    }

    private void openCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            incrementalno=incrementalno+1;

            // Permission already granted, proceed with camera intent
            imageFileName = "test_" + incrementalno ;
            File storageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "tempsafety");

            if (!storageDir.exists()) {
                storageDir.mkdirs();
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && !Environment.isExternalStorageManager()) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivity(intent);
            }
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DISPLAY_NAME, imageFileName + ".jpg");
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/tempsafety");

                // For Android 10 and above, use MediaStore API to create the content URI
                Uri photoFile;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    Uri externalContentUri = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
                    photoFile = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                } else {
                    // For versions below Android 10, use the file URI
                    File photoFile2 = new File(storageDir, imageFileName + ".jpg");
                    photoFile = Uri.fromFile(photoFile2);
                }


                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoFile);
                    cameraLauncher.launch(takePictureIntent);


            }
        }
    }

    private File createImageFile() {
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "test_" + incrementalno ;
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "tempsafety");

        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }

/*
        try {

            showToast("TEST"+String.valueOf(incrementalno));
          /*File tempfile= File.createTempFile(
                    imageFileName,
                    ".jpg",
                    storageDir
            );
          if ()
          showToast("TESTING"+String.valueOf(tempfile));


//return imageUri;
        } catch (IOException e) {
            e.printStackTrace();
            showToast("Error creating temporary file: " + e.getMessage());
            return null;
        }
*/
        return null;
    }
    private void movepictures(String usname,String urvalue,String circlevalue,String sectionvalue,String staffvalue,String staffwork,Bitmap bitmapphoto,Bitmap bitmapphoto2,String timevalue,String latval,String lngval,String accval,String Userid){
        try {
            btncapture3.setEnabled(false);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
            bitmapphoto.compress(Bitmap.CompressFormat.JPEG, 20, stream);
            bitmapphoto2.compress(Bitmap.CompressFormat.JPEG, 20, stream1);
            byte[] byteArray = stream.toByteArray();
            byte[] byteArray2 = stream1.toByteArray();

            String imagefilename = usname + "_" + urvalue + "_" + circlevalue + "_" + sectionvalue + "_1" + ".jpg";
            String imagefilename2 = usname + "_" + urvalue + "_" + circlevalue + "_" + sectionvalue + "_2" + ".jpg";

            RequestBody requestBody = RequestBody.create(byteArray, MediaType.parse("image/jpg"));
            RequestBody requestBody1 = RequestBody.create(byteArray2, MediaType.parse("image/jpg"));

            MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image1", imagefilename, requestBody);
            MultipartBody.Part imagePart1 = MultipartBody.Part.createFormData("image2", imagefilename2, requestBody1);
            retrofit = RetrofitSingleton.getRetrofitInstance();
            Passvaluesstore apiService = retrofit.create(Passvaluesstore.class);
            //    Nscvalidation apiResponse = new Nscvalidation(param1, param2, param3, param4, param5, param6, param7, param8, param9,param10,param11,param12,param13);
            // Call<Nscvalidation> call = apiService.sendFormData(param1, param2, param3, param4, param5, param6, param7, param8, param9,param10,param11,param12,param13);
            Call<ResponseBody> call = apiService.uploadImage(imagePart,imagePart1,requestBody,usname,urvalue, circlevalue, sectionvalue, staffvalue, staffwork, timevalue, latval, lngval,accval,Userid);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    // this method is called when we get response from our api.
                    if (response.isSuccessful()) {

                        String messagevalue= null;
                        try {
                            messagevalue = response.body().string();
                            Gson gson = new Gson();
                           responseoutput Responseresults = gson.fromJson(messagevalue, responseoutput.class);
                             Useridfortext=Responseresults.getUserid();
                             Usernamefortext=Responseresults.getUsername();
                             Userrolefortext=Responseresults.getUserrole();
                             Cirnamefortext=Responseresults.getCirclename();
                             Secnamefortext=Responseresults.getSectionname();
                             Usermaintvalue=Responseresults.getUsermaintvalue();
    //                         showToast(messagevalue);

                                if (!Useridfortext.equals("0")) {
                                    savefiletolocal(messagevalue, capturedPhotos.get(0), capturedPhotos.get(1));
                                } else if (Useridfortext.equals("0") && Usermaintvalue.equals("1")) {

                                    showToast("PHOTOGRAPH uploaded SUCCESSFULLY");
                                    savepicturestolocal(capturedPhotos.get(0), capturedPhotos.get(1));
                                    File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/tempsafety/");

                                    // Check if the directory exists
                                    if (directory.exists() && directory.isDirectory()) {
                                        // Get all files in the directory
                                        File[] files = directory.listFiles();

                                        // Delete each file in the directory
                                        if (files != null) {
                                            for (File file : files) {
                                                if (file.isFile()) {
                                                    file.delete();
                                                }
                                            }
                                        }
                                    }

                                    progressBar.setVisibility(View.GONE);
                                }


                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        Toast.makeText(homescreen.this, "FAILED TO SAVE", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        btncapture3.setEnabled(true);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                    String errorMessage = "An error occurred: " + t.getMessage();
                 showToast(errorMessage);
                    btncapture3.setEnabled(true);

                }
            });





        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        startLocationUpdates();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
            return;
        }

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000); // Update interval in milliseconds

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    private void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates();
            }
        }

        }
        private void savefiletolocal(String myData,Bitmap photo1,Bitmap photo2){
            Gson gson = new Gson();
            String jsonString = gson.toJson(myData);
            String state = Environment.getExternalStorageState();

            //String jsonString = "your_json_data"; // Replace this with your actual JSON data

            // Folder name and file name
            String folderName = "MyFolder";
            String fileName = "data.txt";

            // Check if external storage is available for writing
            if (isExternalStorageWritable()) {
                // Get the directory for the specified folder
     //           File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/TNEBSAFETY");
File folder=createFolderInDocuments(this,folderName);
                String successmsg2="Folder created: " + folder.getAbsolutePath();

                // Create the folder if it doesn't exist
                if (!folder.exists()) {
                    if (folder.mkdirs()) {
                        String successmsg="Folder created: " + folder.getAbsolutePath();
                        Log.i(TAG, successmsg);


                    } else {
                        String errmessage="Failed to create folder: " + folder.getAbsolutePath();
                        Log.e(TAG, errmessage);

                    }
                }

                // Create the JSON file
                File jsonFile = new File(folder, fileName);

                // Write JSON data to the file
                try (FileWriter writer = new FileWriter(jsonFile)) {
                    writer.write(jsonString);
                    Toast.makeText(this, "User created successfully", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "User not created", Toast.LENGTH_SHORT).show();
//                    String errorMessage = "Error saving JSON data: " + e.getMessage();
  //                  Log.e(TAG, errorMessage);
    //                showToast(errorMessage);
                }
            } else {
                Toast.makeText(this, "External storage permission denied", Toast.LENGTH_SHORT).show();
            }
            Bitmap bitmap = photo1;

            Bitmap bitmap2 = photo2;
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH)+1; // Note: Months are zero-based, January is 0
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int hour = calendar.get(Calendar.HOUR_OF_DAY); // 24-hour format
            int minute = calendar.get(Calendar.MINUTE);
            int second = calendar.get(Calendar.SECOND);
            String dayvalue=null;
            String monthvalue=null;
            if(day<10)
            {
                dayvalue="0"+String.valueOf(day);
            }
            else
            {
                dayvalue=String.valueOf(day);
            }
            if(month<10)
            {
                monthvalue="0"+String.valueOf(month);
            }
            else
            {
                monthvalue=String.valueOf(month);
            }
            Date currentDate = new Date();
            // Create a directory in external storage
            File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/TNEB SAFETY");
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Generate a unique filename for the image
            String fileName1 = staffnamevalue+"_"+workdescriptionvalue+"_"+timestampstring+"_"+dayvalue+"_"+monthvalue+"_"+year+"_"+hour+minute+"_01"+".jpg";
            File file = new File(directory, fileName1);
            String fileName2 = staffnamevalue+"_"+workdescriptionvalue+"_"+timestampstring+"_"+dayvalue+"_"+monthvalue+"_"+year+"_"+hour+minute+"_02"+".jpg";
            File file2 = new File(directory, fileName2);

            try {
                // Save the bitmap to the file
                FileOutputStream outputStream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);
                outputStream.close();

                // Show a toast message indicating the image is saved
                Toast.makeText(homescreen.this, "PHOTOGRAPH 1 saved ", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
                // Show a toast message if there's an error saving the image
                Toast.makeText(homescreen.this, "Error saving image", Toast.LENGTH_LONG).show();
            }

            try {
                // Save the bitmap to the file
                FileOutputStream outputStream2 = new FileOutputStream(file2);
                bitmap2.compress(Bitmap.CompressFormat.JPEG, 80, outputStream2);
                outputStream2.close();

                // Show a toast message indicating the image is saved
                Toast.makeText(homescreen.this, "PHOTOGRAPH 2 saved ", Toast.LENGTH_LONG).show();
                Intent intent2 = new Intent(homescreen.this, MainActivity.class);
                startActivity(intent2);
            } catch (IOException e) {
                e.printStackTrace();
                // Show a toast message if there's an error saving the image
                Toast.makeText(homescreen.this, "Error saving image", Toast.LENGTH_LONG).show();
            }

        }
    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    private File createFolderInDocuments(Context context, String folderName) {
        // Get the Documents directory for the app
        File documentsDirectory = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);

        if (documentsDirectory != null) {
            // Create a new folder inside the Documents directory
            File newFolder = new File(documentsDirectory, folderName);

            if (!newFolder.exists()) {
                if (newFolder.mkdirs()) {
                    // Folder created successfully
                    // TODO: Perform actions after creating the folder
                } else {
                    // Failed to create folder
                    // TODO: Handle the failure case
                }
            } else {
                // Folder already exists
                // TODO: Handle the case when the folder already exists
            }
        } else {
            // documentsDirectory is null, indicating failure to access external storage
            // TODO: Handle the case when external storage is not available
        }
        return documentsDirectory;
    }

    private Bitmap rotateBitmap(Bitmap bitmap, float degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }
private void savepicturestolocal(Bitmap photo1,Bitmap photo2) {
    Bitmap bitmap = photo1;

    Bitmap bitmap2 = photo2;
    Calendar calendar = Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH)+1; // Note: Months are zero-based, January is 0
    int day = calendar.get(Calendar.DAY_OF_MONTH);
    int hour = calendar.get(Calendar.HOUR_OF_DAY); // 24-hour format
    int minute = calendar.get(Calendar.MINUTE);
    int second = calendar.get(Calendar.SECOND);
    String dayvalue=null;
    String monthvalue=null;
    if(day<10)
    {
        dayvalue="0"+String.valueOf(day);
    }
    else
    {
        dayvalue=String.valueOf(day);
    }
    if(month<10)
    {
        monthvalue="0"+String.valueOf(month);
    }
    else
    {
        monthvalue=String.valueOf(month);
    }
    Date currentDate = new Date();
    // Create a directory in external storage
    File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/TNEB SAFETY");
    if (!directory.exists()) {
        directory.mkdirs();
    }

    // Generate a unique filename for the image
    String fileName1 = staffnamevalue+"_"+workdescriptionvalue+"_"+timestampstring+"_"+dayvalue+"_"+monthvalue+"_"+year+"_"+hour+minute+"_01"+".jpg";
    File file5 = new File(directory, fileName1);
    String fileName2 = staffnamevalue+"_"+workdescriptionvalue+"_"+timestampstring+"_"+dayvalue+"_"+monthvalue+"_"+year+"_"+hour+minute+"_02"+".jpg";
    File file2 = new File(directory, fileName2);
    File directory2 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/tempsafety/");

    // Check if the directory exists
    if (directory2.exists() && directory2.isDirectory()) {
        // Get all files in the directory
        File[] files2 = directory2.listFiles();

        // Delete each file in the directory
        if (files2 != null) {
            for (File file: files2) {
                if (file.isFile()) {
                    file.delete();
                }
            }
        }
    }
    try {
        // Save the bitmap to the file
        FileOutputStream outputStream = new FileOutputStream(file5);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, outputStream);
        outputStream.close();

        // Show a toast message indicating the image is saved
        Toast.makeText(homescreen.this, "PHOTOGRAPH 1 saved ", Toast.LENGTH_LONG).show();
    } catch (IOException e) {
        e.printStackTrace();
        // Show a toast message if there's an error saving the image
        Toast.makeText(homescreen.this, "Error saving image", Toast.LENGTH_LONG).show();
    }

    try {
        // Save the bitmap to the file
        FileOutputStream outputStream2 = new FileOutputStream(file2);
        bitmap2.compress(Bitmap.CompressFormat.JPEG, 20, outputStream2);
        outputStream2.close();

        // Show a toast message indicating the image is saved
        Toast.makeText(homescreen.this, "PHOTOGRAPH 2 saved ", Toast.LENGTH_LONG).show();
        Intent intent2 = new Intent(homescreen.this, MainActivity.class);
        startActivity(intent2);
    } catch (IOException e) {
        e.printStackTrace();
        // Show a toast message if there's an error saving the image
        Toast.makeText(homescreen.this, "Error saving image", Toast.LENGTH_LONG).show();
    }

}
    private void savepicturestolocal2(Bitmap photo1,Bitmap photo2) {
        Bitmap bitmap = photo1;

        Bitmap bitmap2 = photo2;
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1; // Note: Months are zero-based, January is 0
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY); // 24-hour format
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        String dayvalue=null;
        String monthvalue=null;
        if(day<10)
        {
            dayvalue="0"+String.valueOf(day);
        }
        else
        {
            dayvalue=String.valueOf(day);
        }
        if(month<10)
        {
            monthvalue="0"+String.valueOf(month);
        }
        else
        {
            monthvalue=String.valueOf(month);
        }
        Date currentDate = new Date();
        // Create a directory in external storage
        File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/TNEB SAFETY");
        File directory1 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/TNEB SAFETY PENDING");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        if (!directory1.exists()) {
            directory1.mkdirs();
        }

        // Generate a unique filename for the image
        String fileName1 = staffnamevalue+"_"+workdescriptionvalue+"_"+timestampstring+"_"+dayvalue+"_"+monthvalue+"_"+year+"_"+hour+minute+"_01"+".jpg";
        File file5 = new File(directory, fileName1);
        String fileName2 = staffnamevalue+"_"+workdescriptionvalue+"_"+timestampstring+"_"+dayvalue+"_"+monthvalue+"_"+year+"_"+hour+minute+"_02"+".jpg";
        File file2 = new File(directory, fileName2);

        String fileName3 = usernamevalue+"_"+userrolevalue+"_"+circlenamevalue+"_"+sectionnamevalue+"_"+staffnamevalue+"_"+workdescriptionvalue+"_"+timestampstring+"_"+latvalueselect+"_"+ lngvalueselect+"_"+ accvalueselect+"_"+ Useridvalue+"_01"+".jpg";
        File file3 = new File(directory1, fileName3);
        String fileName4 = usernamevalue+"_"+userrolevalue+"_"+circlenamevalue+"_"+sectionnamevalue+"_"+staffnamevalue+"_"+workdescriptionvalue+"_"+timestampstring+"_"+latvalueselect+"_"+ lngvalueselect+"_"+ accvalueselect+"_"+ Useridvalue+"_02"+".jpg";
        File file4 = new File(directory1, fileName4);

        try {
            // Save the bitmap to the file
            FileOutputStream outputStream = new FileOutputStream(file5);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);
            outputStream.close();

            // Show a toast message indicating the image is saved
            Toast.makeText(homescreen.this, "PHOTOGRAPH 1 saved ", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            // Show a toast message if there's an error saving the image
            Toast.makeText(homescreen.this, "Error saving image", Toast.LENGTH_LONG).show();
        }

        try {
            // Save the bitmap to the file
            FileOutputStream outputStream3 = new FileOutputStream(file3);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, outputStream3);
            outputStream3.close();

            // Show a toast message indicating the image is saved
            Toast.makeText(homescreen.this, "PHOTOGRAPH 1 pending image saved ", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            // Show a toast message if there's an error saving the image
            Toast.makeText(homescreen.this, "Error saving pending image", Toast.LENGTH_LONG).show();
        }
        try {
            // Save the bitmap to the file
            FileOutputStream outputStream4 = new FileOutputStream(file4);
            bitmap2.compress(Bitmap.CompressFormat.JPEG, 20, outputStream4);
            outputStream4.close();

            // Show a toast message indicating the image is saved
            Toast.makeText(homescreen.this, "PHOTOGRAPH 2 pending image saved ", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            // Show a toast message if there's an error saving the image
            Toast.makeText(homescreen.this, "Error saving pending image", Toast.LENGTH_LONG).show();
        }

        try {
            // Save the bitmap to the file
            FileOutputStream outputStream2 = new FileOutputStream(file2);
            bitmap2.compress(Bitmap.CompressFormat.JPEG, 80, outputStream2);
            outputStream2.close();

            // Show a toast message indicating the image is saved
            Toast.makeText(homescreen.this, "PHOTOGRAPH 2 saved ", Toast.LENGTH_LONG).show();
            Intent intent2 = new Intent(homescreen.this, MainActivity.class);
            startActivity(intent2);
        } catch (IOException e) {
            e.printStackTrace();
            // Show a toast message if there's an error saving the image
            Toast.makeText(homescreen.this, "Error saving image", Toast.LENGTH_LONG).show();
        }
        File directory2 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/tempsafety/");

        // Check if the directory exists
        if (directory2.exists() && directory2.isDirectory()) {
            // Get all files in the directory
            File[] files2 = directory2.listFiles();

            // Delete each file in the directory
            if (files2 != null) {
                for (File file: files2) {
                    if (file.isFile()) {
                        file.delete();
                    }
                }
            }
        }
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

    public static int getNetworkStrength(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            Network network = connectivityManager.getActiveNetwork();
            if (network != null) {
                NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);
                if (networkCapabilities != null) {
                    if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        return 2; // High network strength for Wi-Fi
                    } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        return 1; // Medium network strength for cellular data
                    }
                }
            }
        }
        return 0; // No network or low network strength
    }
}




