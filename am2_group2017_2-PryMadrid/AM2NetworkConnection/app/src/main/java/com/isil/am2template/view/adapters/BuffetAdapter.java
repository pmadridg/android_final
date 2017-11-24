package com.isil.am2template.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.isil.am2template.R;
import com.isil.am2template.model.entity.Buffet;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Pablo Claus on 11/2/2017.
 */

public class BuffetAdapter extends BaseAdapter {

    private Context context;
    private List<Buffet> lsBuffetEntities;




    public BuffetAdapter(Context context, List<Buffet> lsBuffet) {
        this.context = context;
        this.lsBuffetEntities = lsBuffet;
    }

    @Override
    public int getCount() {
        return lsBuffetEntities.size();
    }

    @Override
    public Object getItem(int i) {
        return lsBuffetEntities.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        if(v == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            v = inflater.inflate(R.layout.row_buffet, null);
            BuffetAdapter.ViewHolder holder = new BuffetAdapter.ViewHolder();
            holder.tviName = (TextView)v.findViewById(R.id.tviName);
            holder.iviNote = (ImageView)v.findViewById(R.id.iviBuffet);
            holder.tviPrecio = (TextView)v.findViewById(R.id.tviPrecio);
            //holder.eteQty = (EditText) v.findViewById(R.id.eteQty);
            holder.chkCart = (CheckBox)v.findViewById(R.id.chkCart);
            holder.tviQty = (TextView)v.findViewById(R.id.tviQty);



            v.setTag(holder);
        }
        final Buffet entry = lsBuffetEntities.get(position);
        if(entry != null) {
            BuffetAdapter.ViewHolder holder = (BuffetAdapter.ViewHolder)v.getTag();
            holder.tviName.setText(entry.getTitle());
            Picasso.with(holder.iviNote.getContext()).load(entry.getBimage()).fit().into(
                    holder.iviNote);
            holder.tviPrecio.setText(Integer.toString(entry.getPrice()));
            //holder.eteQty.setText(entry.getQty());
            holder.tviQty.setText(entry.getQty());


            holder.chkCart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    entry.setChecked(isChecked);
                }
            });




        }

        return v;
    }

    static class ViewHolder
    {
        ImageView iviNote;
        TextView tviName;
        ToggleButton tgButton;
        TextView tviPrecio;
        EditText eteQty;
        CheckBox chkCart;
        TextView tviQty;

    }
}
