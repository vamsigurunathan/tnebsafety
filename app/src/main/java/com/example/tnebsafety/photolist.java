package com.example.tnebsafety;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

public class photolist extends AppCompatActivity {
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photolist);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get the directory path where your images are stored
        String directoryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/TNEB SAFETY";


        // Get the list of image files modified in the last week
        ArrayList<File> imageFiles = getImagesInLastWeek(directoryPath);

        // Create and set the adapter for the RecyclerView
        ImageAdapter imageAdapter = new ImageAdapter(imageFiles);
        recyclerView.setAdapter(imageAdapter);
    }

    private ArrayList<File> getImagesInLastWeek(String directoryPath) {
        ArrayList<File> imageFiles = new ArrayList<>();
        File directory = new File(directoryPath);

        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();

            if (files != null) {
                for (File file : files) {
                    // Check if the file is an image and modified in the last week
                    if (file.isFile() && file.getName().endsWith(".jpg") && isFileModifiedInLastWeek(file)) {
                        imageFiles.add(file);
                    }
                }
            }
        }

        // Sort the files by modification time (descending order)
        Collections.sort(imageFiles, (file1, file2) -> Long.compare(file2.lastModified(), file1.lastModified()));

        return imageFiles;
    }

    private boolean isFileModifiedInLastWeek(File file) {
        long oneWeekInMillis = 7 * 24 * 60 * 60 * 1000L; // One week in milliseconds
        long currentTime = System.currentTimeMillis();

        return currentTime - file.lastModified() <= oneWeekInMillis;
    }

    private static class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
        private final ArrayList<File> imageFiles;

        ImageAdapter(ArrayList<File> imageFiles) {
            this.imageFiles = imageFiles;
        }

        @NonNull
        @Override
        public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
            return new ImageViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
            File imageFile = imageFiles.get(position);
            String filetext=imageFile.getName();
            char targetChar = '_';
            String substringToRemove = ".jpg";


            String filetextjpg=removeSubstringByRegex(filetext,substringToRemove);
            String resultSubstring=null;
            String resultthirdsubstring=null;
            int firstIndex = filetextjpg.indexOf(targetChar);
            int secondIndex = filetextjpg.indexOf(targetChar, firstIndex + 1);
            int thirdIndex = filetextjpg.indexOf(targetChar, secondIndex + 1);
            if (firstIndex != -1) {
                // Find the second occurrence


                if (secondIndex != -1) {
                    // Extract substring after the second occurrence
                    resultSubstring = filetextjpg.substring(0, secondIndex);

                    resultthirdsubstring= filetextjpg.substring(thirdIndex);

                } else {
                    System.out.println("There is no second occurrence of underscore.");
                }
            } else {
                System.out.println("There is no first occurrence of underscore.");
            }
            holder.fileNameTextView.setText(resultSubstring+"_"+resultthirdsubstring);

            // Use Glide to load images with a placeholder
            Glide.with(holder.itemView)
                    .load(imageFile)
                    .into(holder.imageView);
        }

        @Override
        public int getItemCount() {
            return imageFiles.size();
        }

        static class ImageViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            TextView fileNameTextView;

            ImageViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.imageView);
                fileNameTextView = itemView.findViewById(R.id.fileNameTextView);
            }
        }
    }
    public static String removeSubstringByRegex(String input, String pattern) {
        return input.replaceAll(pattern, "");
    }
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
