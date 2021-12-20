package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {
   static ListView listView ;
   static ArrayAdapter arrayAdapter;
   static ArrayList<String> titlesList = new ArrayList<>();
    SharedPreferences sharedPreferences;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
         super.onOptionsItemSelected(item);
         switch ((item.getItemId())){
             case R.id.add_note:
                 Intent intent = new Intent(getApplicationContext(),NoteEditorActivity.class);
                 startActivity(intent);
                 return  true;
             default:
                 return false;
         }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences =getApplicationContext().getSharedPreferences("com.example.notes",Context.MODE_PRIVATE);
        listView = findViewById(R.id.listview);

        HashSet<String>titlesSet =(HashSet<String>) sharedPreferences.getStringSet("titles",null);
        if (titlesSet==null){
            titlesList.add("Add a Note");
        }
        else {
            titlesList=new ArrayList<>(titlesSet);
        }
        arrayAdapter = new ArrayAdapter(this,R.layout.custom_text,R.id.list_content,titlesList);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent (getApplicationContext(),NoteEditorActivity.class);
                intent.putExtra("noteId",position);
                startActivity(intent);
            }
        });


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Delete Note")
                        .setMessage("Do you want to delete this note?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                titlesList.remove(position);
                                arrayAdapter.notifyDataSetChanged();
                                HashSet<String> titleSet = new HashSet<>(MainActivity.titlesList);
                                sharedPreferences.edit().putStringSet("titles",titleSet).apply();
                            }
                        })
                        .setNegativeButton("No",null)
                        .show();
                return true;
            }
        });

    }
}