package vn.co.honda.hondacrm.ui.activities.login.ui;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.relex.circleindicator.CircleIndicator;
import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.net.ApiService;
import vn.co.honda.hondacrm.net.core.ApiClient;
import vn.co.honda.hondacrm.net.model.tutorial.Tutorial;
import vn.co.honda.hondacrm.net.model.tutorial.TutorialResponse;
import vn.co.honda.hondacrm.ui.activities.BaseActivity;
import vn.co.honda.hondacrm.ui.activities.home.MainActivity;
import vn.co.honda.hondacrm.ui.adapters.login.ViewPagerAdapter;
import vn.co.honda.hondacrm.utils.Constants;
import vn.co.honda.hondacrm.utils.DialogUtils;
import vn.co.honda.hondacrm.utils.PrefUtils;

public class WelcomeActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    @BindView(R.id.btnNext)
    Button btnNext;

    @BindView(R.id.tvSkip)
    TextView tvSkip;

    private ViewPager viewPager;

    private PagerAdapter adapter;

    private List<Tutorial> tutorials;
    private int currentPage = 0;
    //API
    ApiService apiService;
    String mTypeFullAccessToken;
    private Boolean isFinish = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial_screen);
        ButterKnife.bind(this);

        if (getIntent().getExtras() != null) {
            isFinish = getIntent().getExtras().getBoolean(Constants.KEY_TUTORIAL);
        }
        //api
        apiService = ApiClient.getClient(WelcomeActivity.this.getApplicationContext()).create(ApiService.class);
        mTypeFullAccessToken = PrefUtils.getFullTypeAccessToken(WelcomeActivity.this.getApplicationContext());
        viewPager = findViewById(R.id.pager);
        CircleIndicator indicator = findViewById(R.id.indicator);
        //request api here
        //send image to adapter
        requestTutorial();
        tutorials = new ArrayList<>();
        adapter = new ViewPagerAdapter(WelcomeActivity.this, tutorials);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(this);
        adapter.notifyDataSetChanged();
        indicator.setViewPager(viewPager);
        adapter.registerDataSetObserver(indicator.getDataSetObserver());

        this.getApplicationContext().deleteDatabase(Constants.DATABASE_NAME);
    }

    private void requestTutorial() {
        showProgressDialog();
        apiService.getTutorial()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<TutorialResponse>() {

                    @Override
                    public void onSuccess(TutorialResponse tutorialResponse) {
                        if(tutorialResponse.getData()!=null){
                        hideProgressDialog();

                        if (tutorialResponse.getData() != null && !tutorialResponse.getData().isEmpty()) {
                            if (!tutorials.isEmpty()) {
                                tutorials.clear();
                            }
                            for (Tutorial tutorial :tutorialResponse.getData()) {
                                if(tutorial.getImage()!=null){
                                    tutorials.add(tutorial);
                                }
                            }
                            adapter.notifyDataSetChanged();
                        }
                        }

                    }
                    @Override
                    public void onError(Throwable error) {
                        hideProgressDialog();
                        DialogUtils.showDialogConfirmLogin(WelcomeActivity.this, R.layout.dialog_notification_erro_network, 0.9f, 0.2f, getString(R.string.networkError));
                    }
                });
    }
    private void hideProgressDialog() {
    }

    private void showProgressDialog() {
    }

    @OnClick(R.id.btnNext)
    public void onNextClick() {
        currentPage++;
        if (currentPage == Constants.MAX_TUTORIAL) {
            if (isFinish) {
                finish();
            } else {
               // finish();
                startActivity(this, MainActivity.class, true);
            }
        } else {
            if (currentPage == Constants.MAX_TUTORIAL-1) {
                btnNext.setText(getString(R.string.lb_welcome_got_it));
            } else {
                if(currentPage <(Constants.MAX_TUTORIAL-1)){
                    btnNext.setText(getString(R.string.lb_welcome_next));
                }

            }
            viewPager.setCurrentItem(currentPage, true);
        }
    }

    @OnClick(R.id.tvSkip)
    public void onSkipClick() {
        if (isFinish) {
            finish();
        } else {
            //finish();
            startActivity(this, MainActivity.class, true);
        }
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        if(currentPage < i) {
            if (currentPage == Constants.MAX_TUTORIAL-2) {
                btnNext.setText(getString(R.string.lb_welcome_got_it));
            } else {
                btnNext.setText(getString(R.string.lb_welcome_next));
            }

        } else if(currentPage > i){
            // handle swipe LEFT
            btnNext.setText(getString(R.string.lb_welcome_next));

        }
        currentPage = i; // Update current position
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    @Override
    public void onBackPressed() {
        // prevent close tutorial
    }
}
