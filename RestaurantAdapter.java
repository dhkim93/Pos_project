package kdh.com.myrestaurant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by chris on 2016-08-09.
 */
public class RestaurantAdapter extends BaseAdapter {
        private Context mContext = null;
        private int layout = 0;
        private ArrayList<Restaurant> data = null;
        private LayoutInflater inflater = null;

        public RestaurantAdapter(Context c, int l, ArrayList<Restaurant> d) {
            this.mContext = c;
            this.layout = l;
            this.data = d;
            this.inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return data;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }



        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub

            if(convertView == null) {
                convertView = inflater.inflate(this.layout, parent, false);
            }
            ImageView Re_image = (ImageView) convertView.findViewById(R.id.Restaurant_image);
            TextView Re_name = (TextView) convertView.findViewById(R.id.Restaurant_name);
            TextView Re_location = (TextView) convertView.findViewById(R.id.Restaurant_location);

            Re_image.setImageBitmap(data.get(position).image);
            Re_name.setText(data.get(position).name);
            Re_location.setText(data.get(position).location);

            return convertView;
        }
    }





