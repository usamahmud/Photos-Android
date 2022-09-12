
package com.example.photos78;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class AlbumsAdapter extends BaseAdapter {

    Context context;
    ArrayList<Album> albumList;

    String path;

    public AlbumsAdapter(@NonNull Context context, ArrayList<Album> albumList, String path) {
        this.context = context;
        this.albumList = albumList;
        this.path = path;
    }

    @Override
    public int getCount() {
        if (albumList == null) {
            return 0;
        }
        return albumList.size();
    }

    @Override
    public Object getItem(int position) {
        return albumList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = View.inflate(context, R.layout.show_albums, null);

        TextView albumName = (TextView)view.findViewById(R.id.album_name);
        Button renameButton = (Button)view.findViewById(R.id.rename_button);
        Button deleteButton = (Button)view.findViewById(R.id.delete_button);

        albumName.setText(albumList.get(position).getName());

        view.setTag(albumList.get(position).getName());

        renameButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Rename Album");

                EditText name = new EditText(context);

                name.setInputType(InputType.TYPE_CLASS_TEXT);
                name.setText(albumList.get(position).getName());
                builder.setView(name);

                // Add the buttons
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        String newName = name.getText().toString();
                        Album newAlbum = new Album(newName);
                        //Toast.makeText(context, String.format("%b", albumList.contains(newAlbum)), Toast.LENGTH_SHORT).show();
                        if (!(newName.isEmpty() || (albumList.contains(newAlbum) && !newName.equals(albumList.get(position).getName())))) {
                            albumList.get(position).setName(newName);
                            System.out.println("Wrote list, size: " + albumList.size());
                        } else {
                            Toast.makeText(context, String.format("Invalid album name"), Toast.LENGTH_SHORT).show();
                        }

                        try {
                            Photos.writeAlbumList(albumList, context, path);
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

                ((Photos)context).adapter = new AlbumsAdapter(context, albumList, path);
                ((Photos)context).listView.setAdapter(((Photos)context).adapter);

            }
        });

        deleteButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete Album?");

                // Add the buttons
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        albumList.remove(position);

                        ((Photos)context).adapter = new AlbumsAdapter(context, albumList, path);
                        ((Photos)context).listView.setAdapter(((Photos)context).adapter);


                        try {
                            ((Photos)context).writeAlbumList(albumList, context, path);
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

                ((Photos)context).adapter = new AlbumsAdapter(context, albumList, path);
                ((Photos)context).listView.setAdapter(((Photos)context).adapter);


            }
        });

        
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Photos)context).showAlbum(position);
            }
        });



        return view;
    }


}
