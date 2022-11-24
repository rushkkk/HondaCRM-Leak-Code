package vn.co.honda.hondacrm.ui.adapters.login;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;

import java.util.List;

import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.net.model.tutorial.Tutorial;
import vn.co.honda.hondacrm.utils.Constants;

public class ViewPagerAdapter extends PagerAdapter {
    private Context context;
    private List<Tutorial> tutorials;

    public ViewPagerAdapter(Context context, List<Tutorial> tutorials) {
        this.context = context;
        this.tutorials=tutorials;
    }

    @Override
    public int getCount() {
        if (tutorials == null) {
            return Constants.ZERO;
        }
        return tutorials.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = inflater.inflate(R.layout.item_introduce, container, false);
        ImageView imgTrailing = rootView.findViewById(R.id.img_car);

        //load anh api
        if (tutorials.get(position).getImage() != null) {
            Picasso.with(context)
                    .load(tutorials.get(position).getImage())
                    .placeholder(R.drawable.fake_slide_image)
                    .error(R.drawable.fake_slide_image)
                    .into(imgTrailing);
        } else {
            imgTrailing.setImageResource(R.drawable.fake_slide_image);
        }

        container.addView(rootView);
        return rootView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        (container).removeView((RelativeLayout) object);
    }
}
