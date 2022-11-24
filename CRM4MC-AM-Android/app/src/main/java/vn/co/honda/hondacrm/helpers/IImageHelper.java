package vn.co.honda.hondacrm.helpers;

import android.content.Context;
import android.widget.ImageView;

import rx.Subscription;

/**
 * @author CuongNV31
 */
public interface IImageHelper {

    Subscription loadImageRx(Context context,
                             String url,
                             ImageView view,
                             int width,
                             int height);

    void clearMemory(Context context);
}
