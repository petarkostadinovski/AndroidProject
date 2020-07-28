package com.example.firebaseproject.Model;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.firebaseproject.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AccessoriesList extends ArrayAdapter<Keychain> {

    private Activity context;
    private List<Keychain> keychainList;
    private int lastPosition = -1;

    public AccessoriesList (Activity context, List<Keychain> keychainList){
        super(context, R.layout.layout_keys, keychainList);
        this.context = context;
        this.keychainList = keychainList;
    }

    static class ViewHolder{
        TextView textViewKeyName;
        TextView textViewOnStock;
        TextView textViewKeyPrice;
        ImageView imageViewKey;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final View result;
        ViewHolder holder = new ViewHolder();

        if (convertView == null){
            LayoutInflater inflater = context.getLayoutInflater();

            convertView = inflater.inflate(R.layout.layout_accessories, parent, false);

            holder.textViewKeyName = (TextView)convertView.findViewById(R.id.textViewKeyName);
            holder.textViewOnStock = (TextView)convertView.findViewById(R.id.textViewOnStock);
            holder.textViewKeyPrice = (TextView)convertView.findViewById(R.id.textViewKeyPrice);
            holder.imageViewKey = (ImageView)convertView.findViewById(R.id.imageViewKey);

            result = convertView;

            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(context,
                (position > lastPosition) ? R.anim.load_down_listview : R.anim.load_up_listview);
        result.startAnimation(animation);
        lastPosition = position;
        Keychain keychain = keychainList.get(position);

//        textViewOnStock.setText(keychain.getOn_stock() == 1 ? "Available" : "Not available");
//        textViewKeyName.setText(keychain.getName());
//        textViewKeyPrice.setText(keychain.getPrice() + " den.");
        holder.textViewKeyName.setText("Name: " + keychain.getName());
        holder.textViewKeyPrice.setText(String.valueOf(keychain.getPrice()) + " ден.");
        holder.textViewOnStock.setText(keychain.getOn_stock() == 1 ? "On stock: Available" : "On stock: Not available");
        holder.textViewOnStock.setTextColor(keychain.getOn_stock() == 1 ? Color.GREEN : Color.RED);
        Picasso.get().load(keychain.getImage_url()).into(holder.imageViewKey);

        return convertView;
    }

}
