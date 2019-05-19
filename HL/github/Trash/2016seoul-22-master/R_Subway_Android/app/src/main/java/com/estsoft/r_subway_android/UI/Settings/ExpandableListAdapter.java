package com.estsoft.r_subway_android.UI.Settings;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.estsoft.r_subway_android.R;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016-07-07.
 */
public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Activity context;
    private Map<String, List<String>> searchSettingCollections;
    private List<String> settings;

    public ExpandableListAdapter(Activity context, List<String> settings,
                                 Map<String, List<String>> searchSettingCollections) {
        this.context = context;
        this.searchSettingCollections = searchSettingCollections;
        this.settings = settings;
    }

    public Object getChild(int groupPosition, int childPosition) {
        return searchSettingCollections.get(settings.get(groupPosition)).get(childPosition);
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }


    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final String setting = (String) getChild(groupPosition, childPosition);
        LayoutInflater inflater = context.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.setting_group_child_item, null);
        }else{

            if(SearchSetting.getActiveLanes().get(childPosition).isActive()){
                convertView.findViewById(R.id.setting_child_check).setVisibility(View.VISIBLE);
            }else{
                convertView.findViewById(R.id.setting_child_check).setVisibility(View.INVISIBLE);
            }

        }

        TextView item = (TextView) convertView.findViewById(R.id.setting_group_child_item);

        item.setText(setting);


        return convertView;
    }

    public int getChildrenCount(int groupPosition) {
        return searchSettingCollections.get(settings.get(groupPosition)).size();
    }

    public Object getGroup(int groupPosition) {
        return settings.get(groupPosition);
    }

    public int getGroupCount() {
        return settings.size();
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String groupName = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.setting_group_item,
                    null);

        }else{
            if(SearchSetting.isActiveExpressOnly() && groupPosition != 1){
                convertView.findViewById(R.id.setting_group_check).setVisibility(View.VISIBLE);
            }else{
                convertView.findViewById(R.id.setting_group_check).setVisibility(View.INVISIBLE);
            }

        }
        TextView item = (TextView) convertView.findViewById(R.id.setting_group_item);
        item.setTypeface(null, Typeface.BOLD);
        item.setText(groupName);

        return convertView;
    }

    public boolean hasStableIds() {
        return true;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}