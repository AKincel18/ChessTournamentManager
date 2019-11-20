package com.example.adam.chesstournamentmanager.staticdata;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.adam.chesstournamentmanager.R;

import org.w3c.dom.Text;

import java.util.List;

public class ListAdapter extends ArrayAdapter<String> {

    private int vg;

    private List<String> items_list;

    private Context context;

    public ListAdapter(Context context, int vg, int id, List<String> items_list) {
        super(context, vg, id, items_list);
        this.context = context;
        this.items_list = items_list;
        this.vg = vg;
    }

    private static class ViewHolder {

        private TextView nameColumn;

        private TextView internationalRankColumn;

        private TextView polishRankColumn;

    }

    public View getView(int position, View convertView, ViewGroup parent){
        View rowView = convertView;

        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(vg, parent, false);
            ViewHolder holder = new ViewHolder();
            holder.nameColumn = rowView.findViewById(R.id.name_player_row_layout);
            holder.internationalRankColumn = rowView.findViewById(R.id.international_ranking_row_layout);
            holder.polishRankColumn = rowView.findViewById(R.id.polish_ranking_row_layout);
            rowView.setTag(holder);
        }

        String[] items = items_list.get(position).split(",");//items_list[position].split(",");
        ViewHolder holder = (ViewHolder) rowView.getTag();

        holder.nameColumn.setText(items[0]);
        holder.internationalRankColumn.setText(items[1]);
        holder.polishRankColumn.setText(items[2]);

        return rowView;
    }

}
