package vn.co.honda.hondacrm.helpers;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Single;
import rx.Subscription;
import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.utils.RxIoTransformer;

/**
 * @author CuongNV31
 */
@Singleton
public class ImageHelper implements IImageHelper {

    private static final float THUMBNAIL_SIZE = 0.1f;

    @Inject
    public ImageHelper() {
    }

    @Override
    public Subscription loadImageRx(Context context,
                                    String url,
                                    ImageView view,
                                    int width,
                                    int height) {

        final float density = context.getResources().getDisplayMetrics().density;

        return Single
                .just(Glide.with(context)
                        .load(url)
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .skipMemoryCache(true)
                        .placeholder(R.drawable.glide_placeholder)
                        .error(R.drawable.glide_error_placeholder)
                        .thumbnail(THUMBNAIL_SIZE)
                        .override((int) (width * density), (int) (height * density))
                        .dontAnimate()
                        .centerCrop()
                        .into(view))
                .compose(new RxIoTransformer<>())
                .subscribe();
    }

    @Override
    public void clearMemory(Context context) {
        Glide.get(context).clearMemory();
    }
}
