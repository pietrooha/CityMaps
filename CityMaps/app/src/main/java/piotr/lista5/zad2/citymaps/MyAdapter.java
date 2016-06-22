package piotr.lista5.zad2.citymaps;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.LinkedList;

/**
 * Created by piotr on 26.04.15.
 */
public class MyAdapter extends ArrayAdapter<MyElement> {

        Context contextt;
        int layout_id;
        LinkedList<MyElement> tab;

        public MyAdapter(Context context, int layout_id, LinkedList<MyElement> elements)
        {

           super(context, layout_id, elements);
            contextt = context;
            this.layout_id = layout_id;
            tab = elements;
        }

        @Override
        public View getView(int position, View converView, ViewGroup parent)
        {
            View view = converView;
            Item item = null;

            if (view == null)
            {
                LayoutInflater layoutInflater = ((Activity)contextt).getLayoutInflater();
                view = layoutInflater.inflate(layout_id, parent, false);

                item = new Item();
                item.text_field = (TextView)view.findViewById(R.id.name);

                view.setTag(item);
            }
            else
                item = (Item)view.getTag();

            MyElement newElement = tab.get(position);
            item.text_field.setText(newElement.city_name);

            return view;

        }

        static class Item
        {
            TextView text_field;
        }
}