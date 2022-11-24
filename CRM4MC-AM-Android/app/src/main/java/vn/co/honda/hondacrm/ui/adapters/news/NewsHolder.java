package vn.co.honda.hondacrm.ui.adapters.news;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscription;
import rx.subjects.PublishSubject;
import vn.co.honda.hondacrm.App;
import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.helpers.ImageHelper;
import vn.co.honda.hondacrm.ui.adapters.common.BaseHolder;
import vn.co.honda.hondacrm.ui.fragments.news.models.NewsModel;

/**
 * @author CuongNV31
 */
public class NewsHolder extends BaseHolder<NewsModel> {

    private static final int NEWS_IMG_WIDTH = 64;
    private static final int NEWS_IMG_HEIGHT = 64;

    @Inject
    ImageHelper mImageHelper;
    @BindView(R.id.item_news_title_tv) TextView mNewsTitleTv;
    @BindView(R.id.item_news_body_tv) TextView mNewsBodyTv;
    @BindView(R.id.item_news_img) ImageView mNewsImg;

    private NewsModel mNewsModel;
    private Context mContext;

    public NewsHolder(View v, PublishSubject<NewsModel> click) {
        super(v, click);
        App.getApplication().getAppComponent().inject(this);
    }

    @Override
    public void unbindView() {
        super.unbindView();
        mImageHelper.clearMemory(mContext);
    }

    @OnClick(R.id.item_news_root)
    void onClickItem() {
        mClick.onNext(mNewsModel);
    }

    public void init(NewsModel newsModel) {
        mNewsModel = newsModel;
        mContext = mNewsTitleTv.getContext();
//
//        mNewsTitleTv.setText(mNewsModel.getTitle());
//        mNewsBodyTv.setText(mNewsModel.getBody().trim());
//
//        if (mNewsModel.getEnclosure() != null) {
//            final Subscription subscription = mImageHelper.loadImageRx(
//                    mContext,
//                    mNewsModel.getEnclosure().getUrl(),
//                    mNewsImg,
//                    NEWS_IMG_WIDTH,
//                    NEWS_IMG_HEIGHT);
//            mCompositeSubscription.add(subscription);
//        }
    }
}