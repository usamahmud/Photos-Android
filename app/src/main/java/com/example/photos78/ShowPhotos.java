package com.example.photos78;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class ShowPhotos extends AppCompatActivity {

    TextView albumTitle;
    ListView photoLV;
    ArrayList<Photo> photos;

    ArrayList<Album> albums;
    String albumName;

    PhotosAdapter adapter;
    Context context;


    String path;

    int albumIndex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album);


        context = this;


        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("Bundle" );

        photos = new ArrayList<>();


        albumTitle = (TextView)findViewById(R.id.album_title);
        photoLV = (ListView)findViewById(R.id.photo_list);

        albums = (ArrayList<Album>) args.getSerializable("Albums");
        path = (String) args.getSerializable("path");
        albumIndex = (Integer) args.getSerializable("index");
        albumName = albums.get(albumIndex).getName();


        albumTitle.setText(albumName);

        adapter = new PhotosAdapter(context, albums, albumIndex, path);
        photoLV.setAdapter(adapter);


    }

    public void onResume() {
        super.onResume();
        try {
            albums = Photos.readAlbumList(context, path);
        } catch (ClassNotFoundException e) {
            albums = new ArrayList<Album>();
        } catch (IOException e) {
            albums = new ArrayList<Album>();
        }

        adapter = new PhotosAdapter(context, albums, albumIndex, path);
        photoLV.setAdapter(adapter);
    }

    public void displayPhoto(int position) {
        Bundle bundle = new Bundle();


        Intent intent = new Intent(this, DisplayPhoto.class); //setting up intent that will transfer info to next activity

        bundle.putSerializable("Albums", (Serializable) albums); //getting the photoList and passing it to the next activity
        bundle.putSerializable("Album Index", (Serializable) albumIndex);
        bundle.putSerializable("Photo Index", (Serializable) position);
        bundle.putSerializable("path", (Serializable) path);



        intent.putExtra("Bundle", bundle);
        startActivity(intent);
    }

    public void addPhoto(View view) {

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 0);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, intent);

        if (resultCode == RESULT_OK){
            Uri targetUri = intent.getData();

            String stringUri = targetUri.toString();


            String fileName = new File(targetUri.getPath()).getName();

            Photo newPhoto = new Photo(fileName, stringUri);
            if(!albums.get(albumIndex).getPhotoList().contains(newPhoto)) {
                albums.get(albumIndex).getPhotoList().add(newPhoto);
            } else {
                Toast.makeText(context, String.format("Album already contains this photo"), Toast.LENGTH_SHORT).show();
            }



            try {
                Photos.writeAlbumList(albums, context, path);
            } catch (IOException e) {

            }

            adapter = new PhotosAdapter(context, albums, albumIndex, path);
            photoLV.setAdapter(adapter);

        }
    }

}