package com.example.photos78;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;

public class DisplayPhoto extends AppCompatActivity {

    TextView photoCaption;
    ImageView image;

    TextView tags;

    ArrayList<Album> albums;
    String photoName;

    String path;

    Context context;

    int albumIndex;
    int photoIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_detail);


        context = this;

        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("Bundle");


        photoCaption = (TextView) findViewById(R.id.photo_caption);
        image = (ImageView) findViewById(R.id.imageView);
        tags = (TextView) findViewById(R.id.photo_tags);



        albums = (ArrayList<Album>) args.getSerializable("Albums");
        albumIndex = (Integer) args.getSerializable("Album Index");
        photoIndex = (Integer) args.getSerializable("Photo Index");
        path = (String) args.getSerializable("path");

        displayPhoto();

    }

    public void addTag(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Add Tag");

        LinearLayout ll = new LinearLayout(context);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setPadding(20, 20, 20, 20);


        ArrayList<String> tagTypes = new ArrayList<String>();
        tagTypes.add("Person");
        tagTypes.add("Location");

        Spinner type = new Spinner(context);
        ArrayAdapter<String> ad = new ArrayAdapter(context, android.R.layout.simple_spinner_item, tagTypes);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(ad);

        EditText value = new EditText(context);
        value.setInputType(InputType.TYPE_CLASS_TEXT);
        value.setHint("Value");

        ll.addView(type);
        ll.addView(value);

        builder.setView(ll);



        // Add the buttons
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                String typ = (String) type.getSelectedItem();
                String val = value.getText().toString();

                Tag newTag = new Tag(typ, val);

                if (!albums.get(albumIndex).getPhotoList().get(photoIndex).getTags().contains(newTag)) {
                    albums.get(albumIndex).getPhotoList().get(photoIndex).getTags().add(newTag);
                } else {
                    Toast.makeText(context, String.format("Invalid tag"), Toast.LENGTH_SHORT).show();
                }

                displayPhoto();

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


    }

    public void deleteTag(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete Tag");

        LinearLayout ll = new LinearLayout(context);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setPadding(20, 20, 20, 20);


        ArrayList<Tag> tagTypes;
        tagTypes = albums.get(albumIndex).getPhotoList().get(photoIndex).getTags();

        Spinner type = new Spinner(context);
        ArrayAdapter<Tag> ad = new ArrayAdapter(context, android.R.layout.simple_spinner_item, tagTypes);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(ad);


        ll.addView(type);

        builder.setView(ll);



        // Add the buttons
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                Tag delTag = (Tag) type.getSelectedItem();

                albums.get(albumIndex).getPhotoList().get(photoIndex).getTags().remove(delTag);

                displayPhoto();

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
    }

    public void prev(View view) {
        if (photoIndex > 0)
            photoIndex--;
        displayPhoto();
    }

    public void next(View view) {
        if (photoIndex < albums.get(albumIndex).getPhotoList().size()-1)
            photoIndex++;
        displayPhoto();
    }

    public void displayPhoto() {


        String stringUri = albums.get(albumIndex).getPhotoList().get(photoIndex).getStringUri();
        image.setImageURI(Uri.parse(stringUri));

        photoName = albums.get(albumIndex).getPhotoList().get(photoIndex).getCaption();

        photoCaption.setText(photoName);

        String tag = "";

        for (int i = 0; i < albums.get(albumIndex).getPhotoList().get(photoIndex).getTags().size(); i ++) {
            tag = tag + "\t<" + albums.get(albumIndex).getPhotoList().get(photoIndex).getTags().get(i).toString() + ">";
        }

        tags.setText(tag);
    }
    
}
