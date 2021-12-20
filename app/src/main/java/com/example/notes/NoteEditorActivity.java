package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.io.IOException;
import java.util.HashSet;

public class NoteEditorActivity extends AppCompatActivity {
    int noteId;
    EditText title ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);
        title = findViewById(R.id.titleText);
        Intent intent =getIntent();
         noteId = intent.getIntExtra("noteId",-1);
        if (noteId !=-1){
            title.setText(MainActivity.titlesList.get(noteId));
        }
        else {
            MainActivity.titlesList.add("");
            noteId=MainActivity.titlesList.size()-1;
        }
        title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               MainActivity.titlesList.set(noteId,String.valueOf(s));
               MainActivity.arrayAdapter.notifyDataSetChanged();

               SharedPreferences sharedPreferences =getApplicationContext().getSharedPreferences("com.example.notes",Context.MODE_PRIVATE);
                HashSet<String> set = new HashSet<>(MainActivity.titlesList);
                sharedPreferences.edit().putStringSet("titles",set).apply();
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
}