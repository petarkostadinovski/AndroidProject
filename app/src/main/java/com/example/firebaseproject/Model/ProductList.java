package com.example.firebaseproject.Model;

import android.app.Activity;
import android.graphics.Color;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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

public class ProductList extends ArrayAdapter<UserProduct>{

    private Activity context;
    private List<UserProduct> productList;
    private int lastPosition = -1;

    public ProductList (Activity context, List<UserProduct> productList){
        super(context,R.layout.layout_keys, productList);
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final View result;
        AccessoriesList.ViewHolder holder = new AccessoriesList.ViewHolder();

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
            holder = (AccessoriesList.ViewHolder) convertView.getTag();
            result = convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(context,
                (position > lastPosition) ? R.anim.load_down_listview : R.anim.load_up_listview);
        result.startAnimation(animation);
        lastPosition = position;
        UserProduct product = productList.get(position);

        holder.textViewKeyName.setText(product.getName());
        holder.textViewKeyPrice.setText(String.valueOf(product.getPrice()) + " ден.");
        holder.textViewOnStock.setText(product.getOn_stock() == 1 ? "On stock: Available" : "On stock: Not available");
        holder.textViewOnStock.setTextColor(product.getOn_stock() == 1 ? Color.GREEN : Color.RED);
        Picasso.get().load(product.getImage_url()).into(holder.imageViewKey);

        return convertView;
    }
}
