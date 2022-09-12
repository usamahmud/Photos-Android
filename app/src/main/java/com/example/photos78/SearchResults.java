package com.example.photos78;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class SearchResults extends AppCompatActivity {

    String t1, v1;
    String t2, v2;
    int aon;

    ArrayList<Album> albums;
    ArrayList<Photo> results;

    ListView resultListView;
    searchPhotosAdapter adapter;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_results);

        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("Bundle" );

        results = new ArrayList<Photo>();

        context = this;

        resultListView = (ListView)findViewById(R.id.result_list);

        albums = (ArrayList<Album>) args.getSerializable("Albums");
        aon = (int) args.getSerializable("aon");
        t1 = (String) args.getSerializable("t1");
        v1 = (String) args.getSerializable("v1");
        Tag tag1 = new Tag(t1, v1);
        t2 = (String) args.getSerializable("t2");
        v2 = (String) args.getSerializable("v2");
        Tag tag2 = new Tag(t2, v2);

        for (int i = 0; i < albums.size(); i ++) {
            ArrayList<Photo> photos = albums.get(i).getPhotoList();
            for (int j = 0; j < photos.size(); j ++) {
                ArrayList<Tag> tags = photos.get(j).getTags();
                boolean match1 = false;
                for (int k = 0; k < tags.size(); k ++) {
                    boolean match = tags.get(k).searchEquals(tag1);
                    if (!match && (aon == 0 || aon == 1)) {
                        match = tags.get(k).searchEquals(tag2);
                    }
                    if (match) {
                        if (aon == 1 || aon == 2 || match1) {
                            results.add(photos.get(j));
                            break;
                        }
                        match1 = true;
                    }
                }
            }
        }


        adapter = new searchPhotosAdapter(context, results);
        resultListView.setAdapter(adapter);
    }

}
