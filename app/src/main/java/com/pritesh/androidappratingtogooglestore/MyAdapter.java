package com.pritesh.androidappratingtogooglestore;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by pritesh.patel on 2018-01-04, 10:49 AM.
 * ADESA, Canada
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private ArrayList<CardViewModel> list;
    Activity mActivity;
    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(Activity activity, ArrayList<CardViewModel> myDataset) {
        this.list = myDataset;
        this.mActivity = activity;
    }
    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycle_items, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.text_card_name.setText(list.get(position).getCardName());
        Drawable dr =  mActivity.getDrawable(list.get(position).getImageResourceId());
        holder.image_card_cover.setImageDrawable(dr);
       /*Palette.from(drawableToBitmap(dr)).generate(new Palette.PaletteAsyncListener() {
         public void onGenerated(Palette p) {
           // Use generated instance
           //int titleBackColor = p.getVibrantColor(0);
           int titleBackColor = p.getDarkVibrantColor(0);
           //int titleBackColor = p.getVibrantSwatch();
           ProgressDrawable titleBackDrawable;
           if (titleBackColor != 0) {
             titleBackDrawable = new ProgressDrawable(titleBackColor);
           } else {
             titleBackDrawable = new ProgressDrawable(getThemePrimaryColor());
           }
           //titleBackDrawable.setMax(holder.text_card_name.getHeight());
           //titleBackDrawable.setProgress(holder.text_card_name.getHeight());
           holder.text_card_name.setBackground(titleBackDrawable);
         }
       });*/
    }
    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return list.size();
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        final Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView text_card_name;
        public ImageView image_card_cover;
        public ImageView image_action_like;
        public ImageView image_action_flag;
        public ImageView image_action_share;
        public Toolbar maintoolbar;
        public ViewHolder(View v) {
            super(v);
            text_card_name = (TextView) v.findViewById(R.id.text_card_name);
            image_card_cover = (ImageView) v.findViewById(R.id.image_card_cover);
            image_action_like = (ImageView) v.findViewById(R.id.image_action_like);
            image_action_flag = (ImageView) v.findViewById(R.id.image_action_flag);
            image_action_share = (ImageView) v.findViewById(R.id.image_action_share);
            maintoolbar = (Toolbar) v.findViewById(R.id.card_toolbar);
            maintoolbar.inflateMenu(R.menu.global);
        }
    }

}

