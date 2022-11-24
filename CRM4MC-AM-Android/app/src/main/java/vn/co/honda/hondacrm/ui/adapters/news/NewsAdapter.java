package vn.co.honda.hondacrm.ui.adapters.news;

import android.view.View;
import android.view.ViewGroup;

import rx.subjects.PublishSubject;
import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.ui.adapters.common.BaseAdapter;
import vn.co.honda.hondacrm.ui.fragments.news.models.NewsModel;

/**
 * @author CuongNV31
 */
public class NewsAdapter extends BaseAdapter<NewsModel, NewsHolder, NewsModel> {

    public NewsAdapter() {
        super(R.layout.item_news);
    }

    @Override
    public NewsHolder createViewHolder(View view,
                                       ViewGroup viewGroup,
                                       PublishSubject<NewsModel> click) {
        return new NewsHolder(view, click);
    }

    @Override
    public void onBindViewHolder(NewsHolder holder, int position) {
        holder.init(getItem(position));
    }
}