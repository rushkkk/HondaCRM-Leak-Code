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
import vn.co.honda.hondacrm.net.model.base.Terms;
import vn.co.honda.hondacrm.ui.activities.BaseActivity;
import vn.co.honda.hondacrm.utils.DialogUtils;

public class TermsActivity extends BaseActivity {
    @BindView(R.id.webview)
    WebView webView;

    ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_condition);
        ButterKnife.bind(this);
        setTitleHeader(getString(R.string.lb_terms_title));
        apiService = ApiClient.getClient(TermsActivity.this.getApplicationContext()).create(ApiService.class);
        requestTermCondition();
    }
    private void requestTermCondition() {
        showProgressDialog();
        apiService.getTermCondition()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<Terms>() {
                    @Override
                    public void onSuccess(Terms response) {
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

        DialogUtils.showDialogLoadProgress(TermsActivity.this);
    }

}
