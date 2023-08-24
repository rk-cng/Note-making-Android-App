package com.singhinfo.enotes.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.text.format.DateFormat;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.singhinfo.enotes.Notes;
import com.singhinfo.enotes.R;
import com.singhinfo.enotes.ViewModel.NotesViewModel;
import com.singhinfo.enotes.databinding.ActivityUpdateNotesBinding;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class UpdateNotes extends AppCompatActivity {

    ActivityUpdateNotesBinding binding;
    String priority = "1",sNote,sTitle,sPriority;
    int iId;
    NotesViewModel notesViewModel;
    static final int PERMISSION_CODE = 1000,SPEECH_CODE=100;
    Bitmap imageBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateNotesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        notesViewModel = new ViewModelProvider(this).get(NotesViewModel.class);

        sNote= getIntent().getStringExtra("note");
        sTitle= getIntent().getStringExtra("title");
        iId= getIntent().getIntExtra("id",0);
        sPriority= getIntent().getStringExtra("priority");

        binding.updateTitle.setText(sTitle);
        binding.updateNote.setText(sNote);
        priority = sPriority;

        switch (sPriority) {
            case "1":
                binding.redPriority.setImageResource(R.drawable.ic_done);
                break;
            case "2":
                binding.yellowPriority.setImageResource(R.drawable.ic_done);
                break;
            case "3":
                binding.greenPriority.setImageResource(R.drawable.ic_done);
                break;
        }

        binding.redPriority.setOnClickListener(v -> {
            binding.redPriority.setImageResource(R.drawable.ic_done);
            binding.yellowPriority.setImageResource(0);
            binding.greenPriority.setImageResource(0);
            priority = "1";
        });
        binding.yellowPriority.setOnClickListener(v -> {
            binding.yellowPriority.setImageResource(R.drawable.ic_done);
            binding.redPriority.setImageResource(0);
            binding.greenPriority.setImageResource(0);
            priority = "2";
        });
        binding.greenPriority.setOnClickListener(v -> {
            binding.greenPriority.setImageResource(R.drawable.ic_done);
            binding.redPriority.setImageResource(0);
            binding.yellowPriority.setImageResource(0);
            priority = "3";
        });

        binding.saveBTN.setOnClickListener(v -> {
            String noteTitle = binding.updateTitle.getText().toString();
            String note = binding.updateNote.getText().toString();
            updateNote(noteTitle, note);
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.delete_menu, menu);
        return true;
    }
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete:
                new AlertDialog.Builder(this)
                        .setTitle("Delete Note ?")
                        .setMessage("Are you sure you want to delete this note?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            try {
                                notesViewModel.deleteNote(iId);
                            } catch (NullPointerException e){
                                e.printStackTrace();
                            }
                            finish();
                        })
                        .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss()).setCancelable(false).show();
                break;
            case R.id.speech:
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                if (intent.resolveActivity(getPackageManager())!=null){
                    startActivityForResult(intent,SPEECH_CODE);
                } else {
                    Toast.makeText(this, "Your device doesn't support this", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.scanner:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED || checkSelfPermission(Manifest.permission.CAMERA)== PackageManager.PERMISSION_DENIED){
                        String[] permission = { Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA};
                        requestPermissions(permission,PERMISSION_CODE);
                    } else {
                        CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(UpdateNotes.this);
                    }
                }
                break;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CODE && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(UpdateNotes.this);
            } else {
                Toast.makeText(this, "Allow Permission", Toast.LENGTH_SHORT).show();
            }
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK){
            try {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                Uri resultUri = Objects.requireNonNull(result).getUri();
                imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                detectText(imageBitmap);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }
        if(requestCode==SPEECH_CODE && resultCode==RESULT_OK){
            try {
                ArrayList<String> voiceText = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                binding.updateNote.setText(binding.updateNote.getText().toString().concat(voiceText.get(0)));
            } catch (NullPointerException e) {
                e.printStackTrace();
                Toast.makeText(this, "Please say something", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void detectText(Bitmap bitmap){
        TextRecognizer recognizer = new TextRecognizer.Builder(this).build();
        if (recognizer.isOperational()){
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            SparseArray<TextBlock> sparseArray = recognizer.detect(frame);
            StringBuilder stringBuilder = new StringBuilder();
            for (int i=0;i<sparseArray.size();i++){
                TextBlock textBlock = sparseArray.valueAt(i);
                stringBuilder.append(textBlock.getValue());
                stringBuilder.append("\n");
            }
            binding.updateNote.setText(binding.updateNote.getText().toString().concat(stringBuilder.toString()));
        }else {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateNote(String noteTitle, String note) {
        Notes note1 = new Notes();
        Date date = new Date();
        CharSequence dateFormat = DateFormat.format("EEE, d MMM yyyy", date.getTime());

        note1.noteTitle = noteTitle;
        note1.note = note;
        note1.noteDate = dateFormat.toString();
        note1.notePriority = priority;
        note1.id = iId;

        try {
            notesViewModel.updateNote(note1);
        } catch (Exception e){
            e.printStackTrace();
        }
        Toast.makeText(this, "note updated", Toast.LENGTH_SHORT).show();
        finish();
    }
}