package vn.co.honda.hondacrm.ui.activities.policy;

import android.os.Bundle;
import android.webkit.WebView;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.net.ApiService;
import vn.co.honda.hondacrm.net.core.ApiClient;
import vn.co.honda.hondacrm.net.model.base.Policy;
import vn.co.honda.hondacrm.net.model.user.UserProfileResponse;
import vn.co.honda.hondacrm.ui.activities.BaseActivity;
import vn.co.honda.hondacrm.ui.activities.profile.ProfileActivity;
import vn.co.honda.hondacrm.utils.DialogUtils;
import vn.co.honda.hondacrm.utils.PrefUtils;

public class PolicyActivity extends BaseActivity {

    @BindView(R.id.webview)
    WebView webView;

    ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy);
        ButterKnife.bind(this);
        setTitleHeader(getString(R.string.lb_policy_title));
        apiService = ApiClient.getClient(PolicyActivity.this.getApplicationContext()).create(ApiService.class);
        requestPolicy();
    }

    private void requestPolicy() {
        showProgressDialog();
        apiService.getPolicy()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<Policy>() {
                    @Override
                    public void onSuccess(Policy response) {
                        webView.loadDataWithBaseURL(null, response.getData().get(0).getContentEn(), "text/html", "utf-8", null);
                        hideProgressDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        hideProgressDialog();
                    }
                });
    }

    private void hideProgressDialog() {
        DialogUtils.hideDialogLoadProgress();
    }

    private void showProgressDialog() {
        DialogUtils.showDialogLoadProgress(PolicyActivity.this);
    }
}
