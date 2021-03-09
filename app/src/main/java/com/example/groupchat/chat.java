package com.example.groupchat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class chat extends AppCompatActivity {

    ListView listView;
    ArrayList<String> list = new ArrayList<>();
    ArrayAdapter arrayAdapter;
    DatabaseReference reference;
    String user_msg_key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getSupportActionBar().setTitle(getIntent().getStringExtra("topic"));

        listView = findViewById(R.id.listView);
        arrayAdapter = new ArrayAdapter(this , android.R.layout.simple_list_item_1 , list);
        listView.setAdapter(arrayAdapter);

        reference = FirebaseDatabase.getInstance().getReference().child(getIntent().getStringExtra("topic"));
        findViewById(R.id.send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = ((EditText)findViewById(R.id.et)).getText().toString();
                if(text.equals("")) return;
                ((EditText)findViewById(R.id.et)).setText("");
                Map<String , Object> map = new HashMap<>();
                user_msg_key = reference.push().getKey();
                reference.updateChildren(map);

                DatabaseReference reference2 = reference.child(user_msg_key);
                Map<String , Object> map2 = new HashMap<>();
                map2.put("msg" , text);
                map2.put("user" , getIntent().getStringExtra("userName"));
                reference2.updateChildren(map2);

            }
        });

        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                updateList(snapshot);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                updateList(snapshot);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void updateList(DataSnapshot snapshot){
        String msg , user;
        Iterator i = snapshot.getChildren().iterator();
        while(i.hasNext()){
            msg = (String) ((DataSnapshot)(i.next())).getValue();
            user = (String) ((DataSnapshot)(i.next())).getValue();

            arrayAdapter.insert(user+": "+msg , 0);
            arrayAdapter.notifyDataSetChanged();
        }
    }
}