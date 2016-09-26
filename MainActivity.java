package kdh.com.myrestaurant;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView ;
        final RestaurantAdapter myadapter;
        final ArrayList<Restaurant> r_info_list;
        Restaurant restaurant1,restaurant2,restaurant3;
        listView = (ListView)findViewById(R.id.listView);
        restaurant1 = new Restaurant("우리식당", "서울시 서대문구",  BitmapFactory.decodeResource(getResources(), R.drawable.woori));
        restaurant2 = new Restaurant("목동분식", "서울시 강서구",  BitmapFactory.decodeResource(getResources(), R.drawable.mokdong));
        restaurant3 = new Restaurant("봉구스밥버거", "서울시 서대문구",  BitmapFactory.decodeResource(getResources(), R.drawable.bob));
        r_info_list = new ArrayList<Restaurant>();
        r_info_list.add(restaurant1);
        r_info_list.add(restaurant2);
        r_info_list.add(restaurant3);


        myadapter = new RestaurantAdapter(getApplicationContext(),R.layout.restaurant_info, r_info_list);
        listView.setAdapter(myadapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ImageActivity.class); // 다음넘어갈 화면
                Bitmap sendBitmap = r_info_list.get(position).image;

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                sendBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                intent.putExtra("image",byteArray);
                startActivity(intent);
            }
        });
    }
}
class Restaurant {
    public String name;
    public String location;
    public Bitmap image;

    Restaurant(String name, String location,  Bitmap image){
        this.image = image;
        this.name =name;
        this.location = location;

    }
}
