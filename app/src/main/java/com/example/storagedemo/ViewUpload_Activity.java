package com.example.storagedemo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewUpload_Activity extends AppCompatActivity {

    ListView listView;
int j;
    //DatabaseRefrence to get uploaded data
    DatabaseReference mDatabaseReference;
    FirebaseAuth mAuth;
    FirebaseUser user;

    //list to store uploads data
    List<Upload> uploadList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_upload_);

        mAuth = FirebaseAuth.getInstance();

        uploadList = new ArrayList<>();
        listView = findViewById(R.id.listView);

        ViewAllFiles();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //getting upload
                Upload upload = uploadList.get(position);

                //opening the upload file in browser using upload url
                Intent intent = new Intent();
                intent.setType(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(upload.getUrl()));
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        user = mAuth.getCurrentUser();
    }

    public void ViewAllFiles()
    {
        //getting the database reference
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("upload");

        //retrieveing upload data from firebase database
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                j = 0;
                for(DataSnapshot postSnapShot: dataSnapshot.getChildren())
                {
                    Upload upload = postSnapShot.getValue(com.example.storagedemo.Upload.class);
                    uploadList.add(upload);
                    j = j + 1;
                }
                String[] uploads = new String[uploadList.size()];
                Log.e("J:", String.valueOf(j));

                for(int i = 0; i < uploads.length; i++)
                {
                    uploads[i] = uploadList.get(i).getName();
                }

                //displaying it to list
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,uploads)
                {
                    @NonNull
                    @Override
                    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

                        View view = super.getView(position, convertView, parent);

                        TextView textView = view.findViewById(R.id.txtView);
                        textView.setTextColor(Color.BLACK);

                        return view;
                    }
                };
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ViewUpload_Activity.this,"Cancel, Try Again",Toast.LENGTH_LONG).show();
            }
        });
    }
}
