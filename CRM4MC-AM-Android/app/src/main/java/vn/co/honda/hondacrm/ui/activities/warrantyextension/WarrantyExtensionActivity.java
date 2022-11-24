package vn.co.honda.hondacrm.ui.activities.warrantyextension;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.net.ApiService;
import vn.co.honda.hondacrm.net.core.ApiClient;
import vn.co.honda.hondacrm.net.model.base.Policy;
import vn.co.honda.hondacrm.net.model.vehicle.warranty.WarrantyExtension;
import vn.co.honda.hondacrm.net.model.vehicle.warranty.WarrantyResponse;
import vn.co.honda.hondacrm.ui.activities.BaseActivity;
import vn.co.honda.hondacrm.ui.activities.home.MainActivity;
import vn.co.honda.hondacrm.ui.activities.policy.PolicyActivity;
import vn.co.honda.hondacrm.ui.activities.profile.ProfileActivity;
import vn.co.honda.hondacrm.utils.DialogUtils;
import vn.co.honda.hondacrm.utils.PrefUtils;

public class WarrantyExtensionActivity extends BaseActivity {
    TextView btnFindDealer;
    WebView webView;
    ApiService apiService;
    String mTypeFullAccessToken;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extension_warranty);
        setTitleHeader(getString(R.string.title_extension));
        apiService = ApiClient.getClient(WarrantyExtensionActivity.this.getApplicationContext()).create(ApiService.class);
        mTypeFullAccessToken = PrefUtils.getFullTypeAccessToken(getApplicationContext());
        initView();

        btnFindDealer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle= new Bundle();
                bundle.putString("booking","booking");
                startActivity(WarrantyExtensionActivity.this, MainActivity.class,false, bundle);
            }
        });
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        requestPolicy();
    }

    private void requestPolicy() {
        showProgressDialog();
        apiService.getWarranty(
                mTypeFullAccessToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<WarrantyResponse>() {
                    @Override
                    public void onSuccess(WarrantyResponse warrantyResponse) {
                        if(warrantyResponse!=null && warrantyResponse.getData()!=null && warrantyResponse.getData().getContent()!=null) {
                            webView.loadDataWithBaseURL(null, warrantyResponse.getData().getContent(), "text/html", "utf-8", null);
                        }
                        hideProgressDialog();
                    }

                    @Override
                    public void onError(Throwable error) {
                        hideProgressDialog();
                    }
                });
    }
    private void hideProgressDialog() {
        DialogUtils.hideDialogLoadProgress();
    }

    private void showProgressDialog() {
        DialogUtils.showDialogLoadProgress(WarrantyExtensionActivity.this);
    }

    public void initView(){
       btnFindDealer = findViewById(R.id.btnFindDealer);
       webView=findViewById(R.id.wv_benefits_warranty);
    }
}
