package com.corp.corp_music;


import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    String[] items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        listView = findViewById(R.id.listview);
        runtimePermission();
    }

    public void runtimePermission() {
        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
//                        Toast.makeText(MainActivity.this, "Runtime permission Given", Toast.LENGTH_SHORT).show();
                        displaySong();

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                })
                .check();
    }

    public ArrayList<File> findsong(File file) {
        ArrayList<File> arrayList = new ArrayList<>();
        File[] songs = file.listFiles();
        if (songs != null) {
            for (File myFile : songs) {
                if (!myFile.isHidden() && myFile.isDirectory()) {
                    arrayList.addAll(findsong(myFile));
                } else {
                    if (myFile.getName().endsWith(".mp3") || myFile.getName().endsWith(".m4a")) {
                        arrayList.add(myFile);
                    }
                }
            }
        }
        return arrayList;
    }

    public void displaySong() {
        ArrayList<File> mySongs = findsong(Environment.getExternalStorageDirectory());

        items = new String[mySongs.size()];
        for (int i = 0; i < mySongs.size(); i++) {
            items[i] = mySongs.get(i).getName().replace(".mp3", "").replace(".m4a", "");

        }
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, items);
//        listView.setAdapter(adapter);
//


        customAdapter customAdapter = new customAdapter();
        listView.setAdapter(customAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              String songName = (String) listView.getItemAtPosition(position);

              startActivity(new Intent(getApplicationContext(),PlaySong.class)
              .putExtra("songs",mySongs)
              .putExtra("songName",songName)
              .putExtra("pos",position));

            }
        });


    }

    class customAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return items.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = getLayoutInflater().inflate(R.layout.list_item, null);
            TextView txtSong = view.findViewById(R.id.txtSong);
            txtSong.setSelected(true);
            txtSong.setText(items[position]);
            return view;
        }
    }
}