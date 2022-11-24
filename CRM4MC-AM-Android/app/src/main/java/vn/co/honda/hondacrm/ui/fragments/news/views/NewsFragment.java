package vn.co.honda.hondacrm.ui.fragments.news.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import vn.co.honda.hondacrm.App;
import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.di.news.NewsModule;
import vn.co.honda.hondacrm.ui.adapters.news.NewsAdapter;
import vn.co.honda.hondacrm.ui.fragments.common.BaseFragment;
import vn.co.honda.hondacrm.ui.fragments.news.models.NewsModel;
import vn.co.honda.hondacrm.ui.fragments.news.presenter.INewsFragmentPresenter;
import vn.co.honda.hondacrm.ui.fragments.newsDetailed.views.NewsDetailedFragment;

/**
 * @author CuongNV31
 */
public class NewsFragment extends BaseFragment implements INewsFragmentView {

    @Inject
    INewsFragmentPresenter mNewsFragmentPresenter;
    @BindView(R.id.app_bar_title_tv)
    TextView mAppBarTitle;
    @BindView(R.id.news_content_container) View mContentContainer;
    @BindView(R.id.news_progress_dialog) ProgressBar mProgressDialog;
    @BindView(R.id.news_rv)
    RecyclerView mRecyclerView;
    @BindView(R.id.news_no_content_tv) TextView mNoContentContainer;
    private NewsAdapter mNewsAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        App.getApplication().getAppComponent().plus(new NewsModule()).inject(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mNewsFragmentPresenter.bindView(this);
        mNewsFragmentPresenter.initStartViewActions();
        mNewsFragmentPresenter.loadNews();
    }

    @Override
    public void onDestroyView() {
        mNewsFragmentPresenter.unbindView();
        super.onDestroyView();
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_news;
    }

    @Override
    public void setAppBarTitle() {
        mAppBarTitle.setText(getString(R.string.news_app_bar_title));
    }

    @Override
    public void initNewsAdapter() {
        mNewsAdapter = new NewsAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mNewsAdapter);
        mNewsFragmentPresenter.observeNewsAdapterItemClick(mNewsAdapter.observeItemClick());
    }

    @Override
    public void setNewsAdapterData(ArrayList<NewsModel> data) {
        mNewsAdapter.setData(data);
    }

    @Override
    public void showProgressDialog() {
        mProgressDialog.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressDialog() {
        mProgressDialog.setVisibility(View.GONE);
    }

    @Override
    public void showContentContainer() {
        mContentContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideContentContainer() {
        mContentContainer.setVisibility(View.GONE);
    }

    @Override
    public void showNoContentContainer() {
        mNoContentContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideNoContentContainer() {
        mNoContentContainer.setVisibility(View.GONE);
    }

    @Override
    public void openNewsDetailedFragment(NewsModel newsModel) {
        if (getActivity() != null) {
            final FragmentTransaction transaction = getActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.main_fragment_container,
                             NewsDetailedFragment.newInstance(newsModel));
            transaction.commit();
        }
    }
}
