package com.example.jongyepn.subwayinfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class StaionAdapter extends BaseAdapter {
    private Context mContext;
    private int mResource;
    private ArrayList<MyItem> mItems = new ArrayList<MyItem>();

    public StaionAdapter(Context context, int resource, ArrayList<MyItem> items) {
        mContext = context;
        mItems = items;
        mResource = resource;
}
    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mResource, parent,false);
        }
        // Set Icon
        ImageView icon = (ImageView) convertView.findViewById(R.id.iconItem);
        icon.setImageResource(mItems.get(position).mIcon);

        // Set Text 01
        TextView name = (TextView) convertView.findViewById(R.id.textItem1);
        String infos = mItems.get(position).nStaionname + mItems.get(position).nlinename;
        name.setText(infos);


        return convertView;
    }
}

class MyItem {
    int mIcon; // image resource
    String nStaionname; // text
    String nlinename;  // text

    MyItem(int aIcon, String aName, String aAge) {
        mIcon = aIcon;
        nStaionname = aName;
        nlinename = aAge;
    }
}
