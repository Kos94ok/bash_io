package org.tianara.helloworld.sidedrawer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.tianara.helloworld.R;

import java.util.List;


public class DrawerAdapter extends ArrayAdapter {
    private List<DrawerItem> dataList;

    // Constructor
    public DrawerAdapter(Context context, int resource, List<DrawerItem> list) {
        super(context, resource, list);
        this.dataList = list;
    }

    // Get amount of possible types
    @Override
    public int getViewTypeCount() {
        return DrawerItem.getTypeCount();
    }

    // Get the type index
    @Override
    public int getItemViewType(int position) {
        if (dataList.get(position).isSeparator())
            return DrawerItem.SEPARATOR;
        if (dataList.get(position).isHeader())
            return DrawerItem.HEADER;

        return DrawerItem.NORMAL;
    }
    // Check if the element is clickable
    @Override
    public boolean isEnabled(int position) {
        if (dataList.get(position).isHeader())
            return false;
        return true;
    }
    // Inflate the view
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        int type = getItemViewType(position);
        if (v == null) {
            // Inflate the layout according to the view type
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // Normal element
            if (type == DrawerItem.NORMAL) {
                v = inflater.inflate(R.layout.drawer_list_item, parent, false);

                TextView text = (TextView)v.findViewById(R.id.text);
                text.setText(dataList.get(position).getText());
            }
            // Separator
            else if (type == DrawerItem.SEPARATOR) {
                v = inflater.inflate(R.layout.drawer_list_separator, parent, false);
            }
            // Header
            else if (type == DrawerItem.HEADER) {
                v = inflater.inflate(R.layout.drawer_list_header, parent, false);

                TextView text = (TextView)v.findViewById(R.id.text);
                text.setText(dataList.get(position).getText());
            }
        }
        return v;
    }
}

