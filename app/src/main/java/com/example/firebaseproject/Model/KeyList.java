package com.example.firebaseproject.Model;

import android.app.Activity;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.firebaseproject.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

public class KeyList extends ArrayAdapter<Key>{

    private Activity context;
    private List<Key> keyList;

    public KeyList (Activity context, List<Key> keyList){
        super(context,R.layout.layout_keys, keyList);
        this.context = context;
        this.keyList = keyList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.layout_keys, null, true);

        TextView textViewKeyName = (TextView)listViewItem.findViewById(R.id.textViewKeyName);
        TextView textViewOnStock = (TextView)listViewItem.findViewById(R.id.textViewOnStock);
        TextView textViewMoreDetails = (TextView)listViewItem.findViewById(R.id.textViewMoreDetails);
        TextView textViewKeyPrice = (TextView)listViewItem.findViewById(R.id.textViewKeyPrice);
        ImageView imageViewKey = (ImageView)listViewItem.findViewById(R.id.imageViewKey);


        Key key = keyList.get(position);

        Picasso.get().load(key.getImage_url()).into(imageViewKey);
        textViewOnStock.setText(key.getOn_stock() == 1 ? "Available" : "Not available");
        textViewKeyName.setText(key.getName());
        textViewKeyPrice.setText(key.getPrice() + " den.");

        return listViewItem;
    }
}
