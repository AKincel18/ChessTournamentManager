package com.example.adam.chesstournamentmanager.staticdata.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.adam.chesstournamentmanager.R;

import java.util.List;

public class ListAdapter extends ArrayAdapter<String> {

    private int rLayout;

    private List<String> itemList;

    private Context context;

    public ListAdapter(Context context, int rLayout, int rId, List<String> itemList) {
        super(context, rLayout, rId, itemList);
        this.context = context;
        this.itemList = itemList;
        this.rLayout = rLayout;
    }

    private static class ViewHolder {

        private TextView noColumn;

        private TextView nameColumn;

        private TextView internationalRankColumn;

        private TextView polishRankColumn;

    }

    public View getView(int position, View convertView, ViewGroup parent){
        View rowView = convertView;

        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(rLayout, parent, false);
            ViewHolder holder = new ViewHolder();
            holder.noColumn = rowView.findViewById(R.id.lp_row_layout);
            holder.nameColumn = rowView.findViewById(R.id.name_player_row_layout);
            holder.internationalRankColumn = rowView.findViewById(R.id.international_ranking_row_layout);
            holder.polishRankColumn = rowView.findViewById(R.id.polish_ranking_row_layout);
            rowView.setTag(holder);
        }

        String[] items = itemList.get(position).split(",");//itemList[position].split(",");
        ViewHolder holder = (ViewHolder) rowView.getTag();

        holder.noColumn.setText(items[0]);
        holder.nameColumn.setText(items[1]);
        holder.internationalRankColumn.setText(items[2]);
        holder.polishRankColumn.setText(items[3]);

        return rowView;
    }

}
