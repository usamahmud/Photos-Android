package com.example.photos78;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Toolbar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;


public class Photos extends AppCompatActivity {

    ListView listView;
    ArrayList<Album> albums;
    Context context;

    AlbumsAdapter adapter;

    public static final String PHOTO_NAME = "name";


    String storeFile;
    String path;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album_list);
        context = this;

        storeFile = "albums.dat";
        path = getFilesDir().getAbsolutePath();

        albums = new ArrayList<Album>();


        //find list view
        listView = (ListView)findViewById(R.id.album_list);

        try {
            albums = readAlbumList(context, path);
        } catch (ClassNotFoundException e) {
            albums = new ArrayList<Album>();
        } catch (IOException e) {
            albums = new ArrayList<Album>();
        }


        adapter = new AlbumsAdapter(context, albums, path);
        listView.setAdapter(adapter);



    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            albums = readAlbumList(context, path);
        } catch (ClassNotFoundException e) {
            albums = new ArrayList<Album>();
        } catch (IOException e) {
            albums = new ArrayList<Album>();
        }
    }

    public void showAlbum(int position) {
        Bundle bundle = new Bundle();

        /**
         * we want to show the arraylist of photos contained in the album
         * so we pass an ArrayList of photos
         */

        Intent intent = new Intent(this, ShowPhotos.class); //setting up intent that will transfer info to next activity

        bundle.putSerializable("Albums", (Serializable) albums);
        bundle.putSerializable("path", (Serializable) path);
        bundle.putSerializable("index", (Serializable) position);


        intent.putExtra("Bundle", bundle);
        startActivity(intent);



    }


    public void createAlbum(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Create Album");

        EditText name = new EditText(context);

        name.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(name);

        // Add the buttons
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                Album newAlbum = new Album(name.getText().toString());
                if (!albums.contains(newAlbum) && !name.getText().toString().isEmpty()) {
                    albums.add(newAlbum);
                } else {
                    Toast.makeText(context, String.format("Invalid album name"), Toast.LENGTH_SHORT).show();
                }

                try {
                    writeAlbumList(albums, context, path);
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

        adapter = new AlbumsAdapter(context, albums, path);
        listView.setAdapter(adapter);



    }

    public void search(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Photo Search");

        LayoutInflater inflater = LayoutInflater.from(context);
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.search_box, null, false);

        builder.setView(layout);


        Spinner type1 = (Spinner) layout.findViewById(R.id.spinner);
        EditText value1 = (EditText) layout.findViewById(R.id.tagValue);

        RadioGroup radioGroup = (RadioGroup) layout.findViewById(R.id.radioGroup);



        Spinner type2 = (Spinner) layout.findViewById(R.id.spinner2);
        EditText value2 = (EditText) layout.findViewById(R.id.tagValue2);


        // Add the buttons
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button

                int aon = radioGroup.getCheckedRadioButtonId();

                RadioButton radio = (RadioButton) layout.findViewById(aon);
                if (radio == null) {
                    Toast.makeText(context, String.format("One option must be selected"), Toast.LENGTH_SHORT).show();
                    return;
                }

                String t1 = (String) type1.getSelectedItem();
                String t2 = (String) type2.getSelectedItem();
                String v1 = value1.getText().toString();
                String v2 = value2.getText().toString();


                String text = radio.getText().toString();
                if (text.equalsIgnoreCase("AND")) {
                    aon = 0;
                } else if (text.equalsIgnoreCase("OR")) {
                    aon = 1;
                } else {
                    aon = 2;
                }


                Bundle bundle = new Bundle();

                Intent intent = new Intent(context, SearchResults.class); //setting up intent that will transfer info to next activity

                bundle.putSerializable("Albums", (Serializable) albums);


                bundle.putSerializable("t1", (Serializable) t1);
                bundle.putSerializable("v1", (Serializable) v1);
                bundle.putSerializable("aon", (Serializable) aon);
                bundle.putSerializable("t2", (Serializable) t2);
                bundle.putSerializable("v2", (Serializable) v2);


                intent.putExtra("Bundle", bundle);
                startActivity(intent);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

        adapter = new AlbumsAdapter(context, albums, path);
        listView.setAdapter(adapter);
    }

    public static void writeAlbumList(ArrayList<Album> albumList, Context context, String path) throws IOException {
        String storeFile = "albums.dat";
        //context.openFileInput(storeFile, Context.MODE_PRIVATE).

        //FileOutputStream fos = context.openFileOutput(storeFile, Context.MODE_PRIVATE);
        FileOutputStream fos = new FileOutputStream(path + "/" + storeFile);

        //Toast.makeText(context, String.format("write path: %s", path), Toast.LENGTH_SHORT).show();
        //File f = new File()
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(albumList);
        oos.close();
        fos.close();
    }

    public static ArrayList<Album> readAlbumList(Context context, String path) throws ClassNotFoundException, IOException {
        String storeFile = "albums.dat";

        //FileInputStream fis = context.openFileInput(storeFile);
        FileInputStream fis = new FileInputStream(path + "/" + storeFile);

        //Toast.makeText(context, String.format("read path: %s", path), Toast.LENGTH_SHORT).show();
        //Toast.makeText(context, fis.toString(), Toast.LENGTH_SHORT).show();
        ObjectInputStream ois = new ObjectInputStream(fis);
        ArrayList<Album> albumList = (ArrayList<Album>)ois.readObject();
        ois.close();
        fis.close();
        return albumList;
    }

}