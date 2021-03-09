package com.example.groupchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    ArrayList<String> list = new ArrayList<>();
    ArrayAdapter arrayAdapter;
    String userName = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView x = listView = findViewById(R.id.listView);
        arrayAdapter = new ArrayAdapter(this , android.R.layout.simple_list_item_1 , list);
        listView.setAdapter(arrayAdapter);
        showDialog();
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference().getRoot();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Set<String> set = new HashSet<String>();
                Iterator i = snapshot.getChildren().iterator();
                while(i.hasNext()){
                    set.add(   ((DataSnapshot) i.next())   .getKey()  );
                }
                arrayAdapter.clear();
                arrayAdapter.addAll(set);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        x.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int ii, long l) {
                Intent i = new Intent(MainActivity.this , chat.class);
                i.putExtra("userName" , userName);
                i.putExtra("topic" , ((TextView)view).getText().toString());
                startActivity(i);
            }
        });


    }

    private void showDialog(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        EditText temp = new EditText(this);

        dialog.setView(temp);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                userName = temp.getText().toString();
            }
        });
        dialog.setNegativeButton("Cencel ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                showDialog();
            }
        });
        dialog.show();
    }
}