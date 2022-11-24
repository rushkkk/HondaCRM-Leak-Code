package vn.co.honda.hondacrm.ui.adapters.home;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.ui.fragments.home.models.SliderEvents;

public class SlideEventPagerAdapter  extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<SliderEvents> sliderImg;
    private TextView title,content, time, address;



    public SlideEventPagerAdapter(List<SliderEvents> sliderImg, Context context) {
        this.sliderImg = sliderImg;
        this.context = context;
    }

    @Override
    public int getCount() {
        return sliderImg.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.item_slide_events_home, null);
        title = view.findViewById(R.id.txtTitleEvent);
        content = view.findViewById(R.id.txtContentEvent);
        time =  view.findViewById(R.id.txtTimeEvent);
        address = view.findViewById(R.id.txtAddressEvent);

        SliderEvents utils = sliderImg.get(position);
        title.setText(utils.getTitle());
        content.setText(utils.getContent());
        time.setText(utils.getStartTime() + " - " + utils.getEndTime());
        address.setText(utils.getAddress());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if(position == 0){
//                    Toast.makeText(context, "Slide 1 Clicked", Toast.LENGTH_SHORT).show();
//                } else if(position == 1){
//                    Toast.makeText(context, "Slide 2 Clicked", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(context, "Slide 3 Clicked", Toast.LENGTH_SHORT).show();
//                }

            }
        });

        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);

    }
}