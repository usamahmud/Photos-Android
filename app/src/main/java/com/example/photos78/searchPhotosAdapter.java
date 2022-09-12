package com.example.photos78;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class searchPhotosAdapter extends BaseAdapter {

    Context context;
    ArrayList<Photo> photoList;

    public searchPhotosAdapter(@NonNull Context context, ArrayList<Photo> photoList) {
        this.context = context;
        this.photoList = photoList;
    }


    @Override
    public int getCount() {
        if (photoList == null) {
            return 0;
        }
        return photoList.size();
    }

    @Override
    public Object getItem(int position) {
        return photoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(context, R.layout.show_photos_search, null);

        ImageView image = (ImageView) view.findViewById(R.id.search_image);
        TextView photoName = (TextView) view.findViewById(R.id.search_photo_name);


        String stringUri = photoList.get(position).getStringUri();
        image.setImageURI(Uri.parse(stringUri));
        //image.setImageResource(R.drawable.egg);
        photoName.setText(photoList.get(position).getCaption());

        view.setTag(photoList.get(position).getCaption());

        return view;

    }
}
