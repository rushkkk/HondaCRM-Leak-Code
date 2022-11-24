package vn.co.honda.hondacrm.ui.fragments.home.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import java.util.ArrayList;

import javax.inject.Inject;

import vn.co.honda.hondacrm.App;
import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.di.home.HomeModule;
import vn.co.honda.hondacrm.ui.fragments.common.BaseFragment;
import vn.co.honda.hondacrm.ui.fragments.home.presenter.IHomeFragmentPresenter;
import vn.co.honda.hondacrm.ui.fragments.news.models.NewsModel;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends BaseFragment implements IHomeFragmentView {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    @Inject
    IHomeFragmentPresenter mHomePresenter;
    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DealerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_home;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        App.getApplication().getAppComponent().plus(new HomeModule()).inject(this);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mHomePresenter.bindView(this);
        mHomePresenter.loadNews();
    }



    @Override
    public void setAppBarTitle() {

    }

    @Override
    public void initNewsAdapter() {

    }

    @Override
    public void setNewsAdapterData(ArrayList<NewsModel> data) {

    }

    @Override
    public void showProgressDialog() {

    }

    @Override
    public void hideProgressDialog() {

    }

    @Override
    public void showContentContainer() {

    }

    @Override
    public void hideContentContainer() {

    }

    @Override
    public void showNoContentContainer() {

    }

    @Override
    public void hideNoContentContainer() {

    }

    @Override
    public void openNewsDetailedFragment(NewsModel newsModel) {

    }
}
