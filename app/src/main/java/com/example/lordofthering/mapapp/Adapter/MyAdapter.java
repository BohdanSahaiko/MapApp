package com.example.lordofthering.mapapp.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lordofthering.mapapp.R;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends ArrayAdapter<PojoForList> implements Filterable {

    private List<PojoForList> restList;
    private Context context;
    private List<PojoForList> orignRestList;


    public MyAdapter(List<PojoForList> restList, Context ctx) {
        super(ctx, R.layout.my_listview);
        this.restList = restList;
        this.context = ctx;
        this.orignRestList = restList;
    }

    public int getCount() {
        return restList.size();
    }

    public PojoForList getItem(int position) {
        return restList.get(position);
    }

    public long getItemId(int position) {
        return restList.get(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // задаем вид элемента списка, который мы создали высше
        View view = inflater.inflate(R.layout.my_listview, parent, false);

        // проставляем данные для элементов
        TextView title = (TextView) view.findViewById(R.id.title);
        TextView subitem = (TextView) view.findViewById(R.id.time);
        ImageView thumbImage = (ImageView) view.findViewById(R.id.imageView);

        // получаем элемент со списка
        PojoForList pojoForList = restList.get(position);

        // устанавливаем значения компонентам одного эелемента списка
        title.setText(pojoForList.getTitle());
        subitem.setText(pojoForList.getSubitem());
        thumbImage.setImageDrawable(pojoForList.getDrawable());

        return view;
    }

    public void resetData() {
        restList = orignRestList;
    }


	/* *********************************
	 * We use the holder pattern
	 * It makes the view faster and avoid finding the component
	 * **********************************/

    private static class PlanetHolder {
        public TextView planetNameView;
        public TextView distView;
    }



    @Override
    public Filter getFilter() {


        return new PlanetFilter();
    }



    private class PlanetFilter extends Filter {



        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            // We implement here the filter logic
            if (constraint == null || constraint.length() == 0) {
                // No filter implemented we return all the list
                results.values = orignRestList;
                results.count = orignRestList.size();
            }
            else {
                // We perform filtering operation
                List<PojoForList> nPlanetList = new ArrayList<>();

                for (PojoForList p : restList) {
                    if (p.getTitle().toUpperCase().startsWith(constraint.toString().toUpperCase()))
                        nPlanetList.add(p);
                }

                results.values = nPlanetList;
                results.count = nPlanetList.size();

            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {

            // Now we have to inform the adapter about the new list filtered
            if (results.count == 0)
                notifyDataSetInvalidated();
            else {
                restList = (List<PojoForList>) results.values;
                notifyDataSetChanged();
            }

        }
    }
}