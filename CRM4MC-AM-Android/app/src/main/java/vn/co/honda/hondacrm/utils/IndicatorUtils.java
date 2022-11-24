package vn.co.honda.hondacrm.utils;

import android.content.Context;
import android.widget.EdgeEffect;
import android.widget.ImageView;
import android.widget.TextView;

import vn.co.honda.hondacrm.R;

public class IndicatorUtils {
    public static void displayImageIndicator(final ImageView imageView, final int type) {
        switch (type) {
            case 0:
                imageView.setImageResource(R.drawable.ic_circle_black_step_view);
                break;
            case 1:
                imageView.setImageResource(R.drawable.ic_circle_orange_step_view);
                break;
            case 2:
                imageView.setImageResource(R.drawable.ic_circle_green_step_view);
                break;
        }
    }

    public static void displayTextIndicator(Context context, final TextView textView, final int type) {
        switch (type) {
            case 0:
                textView.setTextColor(context.getResources().getColor(R.color.black));
                break;
            case 1:
                textView.setTextColor(context.getResources().getColor(R.color.colorCurrentStep));
                break;
            case 2:
                textView.setTextColor(context.getResources().getColor(R.color.colorDoneStep));
                break;
        }
    }

}
