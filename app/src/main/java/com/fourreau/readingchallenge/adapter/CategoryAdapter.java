package com.fourreau.readingchallenge.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.opengl.Visibility;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fourreau.readingchallenge.R;
import com.fourreau.readingchallenge.activity.BaseActivity;
import com.fourreau.readingchallenge.core.ReadingChallengeApplication;
import com.fourreau.readingchallenge.model.Category;
import com.fourreau.readingchallenge.util.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by Pierre on 06/07/2015.
 */

public final class CategoryAdapter extends BaseAdapter {
    private final List<Item> mItems = new ArrayList<Item>();
    private final LayoutInflater mInflater;
    private Context context;

    public CategoryAdapter(Context context, List<Category> categories) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;

        for (Category cat : categories) {
            mItems.add(new Item(cat.getId(), cat.getLibelle_en(), cat.getLibelle_fr(), cat.getImage()));
        }
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Item getItem(int i) {
        return mItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
        //return mItems.get(i).drawableId;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;
        TextView name, id;
        ImageView picture, pictureRead;

        if (v == null) {
            v = mInflater.inflate(R.layout.grid_item, viewGroup, false);
            v.setTag(R.id.id_category, v.findViewById(R.id.id_category));
            v.setTag(R.id.picture_category, v.findViewById(R.id.picture_category));
            v.setTag(R.id.picture_category_read, v.findViewById(R.id.picture_category_read));
            v.setTag(R.id.label_category, v.findViewById(R.id.label_category));
        }

        id = (TextView) v.getTag(R.id.id_category);
        picture = (ImageView) v.getTag(R.id.picture_category);
        pictureRead = (ImageView) v.getTag(R.id.picture_category_read);
        name = (TextView) v.getTag(R.id.label_category);

        Item item = getItem(i);

        //id
        id.setText(item.id);
        //image
        if (!item.image_name.isEmpty()) {
            Picasso.with(context).load(Utils.BASE_URL + Utils.URL_UPLOAD + item.image_name).fit().centerCrop().into(picture);
        } else {
            Picasso.with(context).load(R.drawable.default_category).fit().centerCrop().into(picture);
        }
        //if book is already read or not
        SharedPreferences sharedPref = context.getSharedPreferences("readingchallenge", Context.MODE_PRIVATE);
        String key = context.getString(R.string.category_id) + item.id;

        //TODO!!!

        
        if (sharedPref.getInt(key, 0) == 1) {
            Picasso.with(context).load(R.drawable.ic_action_accept).into(pictureRead);
        }
        else {
            pictureRead.setVisibility(View.GONE);
        }
        //label
        if (((ReadingChallengeApplication) context.getApplicationContext()).getLanguage().equals(Utils.FR)) {
            name.setText(item.name_fr);
        } else {
            name.setText(item.name);
        }

        return v;
    }

    private static class Item {
        public final String id;
        public final String name;
        public final String name_fr;
        public final String image_name;

        Item(String id, String name, String name_fr, String image_name) {
            this.id = id;
            this.name = name;
            this.name_fr = name_fr;
            this.image_name = image_name;
        }
    }
}