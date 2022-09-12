package com.example.photos78;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Scroller;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import java.io.IOException;
import java.util.ArrayList;

public class PhotosAdapter extends BaseAdapter {

    Context context;
    ArrayList<Album> albums;
    int albumIndex;
    String path;

    public PhotosAdapter(@NonNull Context context, ArrayList<Album> albums, int albumIndex, String path) {
        this.context = context;
        this.albums = albums;
        this.albumIndex = albumIndex;
        this.path = path;
    }

    @Override
    public int getCount() {
        if (albums == null)
            return 0;
        if (albums.size() < albumIndex) {
            return 0;
        }
        return albums.get(albumIndex).getPhotoList().size();
    }

    @Override
    public Object getItem(int position) {
        return albums.get(albumIndex).getPhotoList().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(context, R.layout.show_photos, null);

        ImageView image = (ImageView) view.findViewById(R.id.image);
        TextView photoName = (TextView) view.findViewById(R.id.photo_name);
        Button deleteButton = (Button) view.findViewById(R.id.delete_photo);
        Button moveButton = (Button) view.findViewById(R.id.move_photo);

        //image.setImageResource(R.drawable.egg);
        String stringUri = albums.get(albumIndex).getPhotoList().get(position).getStringUri();
        image.setImageURI(Uri.parse(stringUri));
        photoName.setText(albums.get(albumIndex).getPhotoList().get(position).getCaption());

        view.setTag(albums.get(albumIndex).getPhotoList().get(position).getCaption());

        deleteButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete Photo?");

                // Add the buttons
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        albums.get(albumIndex).getPhotoList().remove(position);

                        ((ShowPhotos)context).adapter = new PhotosAdapter(context, albums, albumIndex, path);
                        ((ShowPhotos)context).photoLV.setAdapter(((ShowPhotos)context).adapter);

                        try {
                            Photos.writeAlbumList(albums, context, path);
                        } catch (IOException e) {

                        }
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();


                ((ShowPhotos) context).adapter = new PhotosAdapter(context, albums, albumIndex, path);
                ((ShowPhotos) context).photoLV.setAdapter(((ShowPhotos) context).adapter);


            }
        });

        moveButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Move Photo");

                Spinner destination = new Spinner(context);

                ArrayAdapter<Album> ad = new ArrayAdapter(context, android.R.layout.simple_spinner_item, albums);
                ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                destination.setAdapter(ad);

                builder.setView(destination);

                // Add the buttons
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button

                        int destIndex = destination.getSelectedItemPosition();

                        Photo movePhoto = albums.get(albumIndex).getPhotoList().get(position);

                        if (!albums.get(destIndex).getPhotoList().contains(movePhoto) || destIndex == albumIndex) {
                            albums.get(destIndex).getPhotoList().add(movePhoto);
                            albums.get(albumIndex).getPhotoList().remove(position);
                        } else {
                            Toast.makeText(context, String.format("Destination album already contains this photo"), Toast.LENGTH_SHORT).show();
                        }

                        ((ShowPhotos) context).adapter = new PhotosAdapter(context, albums, albumIndex, path);
                        ((ShowPhotos) context).photoLV.setAdapter(((ShowPhotos) context).adapter);

                        try {
                            Photos.writeAlbumList(albums, context, path);
                        } catch (IOException e) {

                        }

                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();


                ((ShowPhotos) context).adapter = new PhotosAdapter(context, albums, albumIndex, path);
                ((ShowPhotos) context).photoLV.setAdapter(((ShowPhotos) context).adapter);
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //display photo
                ((ShowPhotos)context).displayPhoto(position);
            }
        });

        return view;

    }
}

