package vn.co.honda.hondacrm.ui.activities.booking_service;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.net.model.dealer.Dealers;
import vn.co.honda.hondacrm.ui.activities.BaseActivity;
import vn.co.honda.hondacrm.ui.activities.Dealer.model.Dealer;
import vn.co.honda.hondacrm.ui.fragments.booking_service.views.SelectTimeSlotFragment;
import vn.co.honda.hondacrm.ui.fragments.booking_service.views.SelectVehicleFragment;
import vn.co.honda.hondacrm.utils.IndicatorUtils;

public class BookingServiceActivity extends BaseActivity implements INumberStepCallBack {
    ImageView imgCircle1rd, imgCircle2rd, imgCircle3rd, imgCircle4rd, imgCircle5rd;
    TextView tvStep1rd, tvStep2rd, tvStep3rd, tvStep4rd, tvStep5rd;
    private String recall = "";
    private Dealers dealers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_service);
        setTitleHeader(getString(R.string.label_title_booking_service));
        initViews();
            setIndicator(1);
            replaceFragment(R.id.frame_booking_service, new SelectVehicleFragment(), true);
    //    }
    }

    private void initViews() {
        dealers=getIntent().getParcelableExtra("recall");
        imgCircle1rd = findViewById(R.id.img_step_select_vehicle);
        imgCircle2rd = findViewById(R.id.img_step_select_dealer);
        imgCircle3rd = findViewById(R.id.img_step_time_slot);
        imgCircle4rd = findViewById(R.id.img_step_select_service);
        imgCircle5rd = findViewById(R.id.img_step_submit);
        tvStep1rd = findViewById(R.id.tv_select_vehicle);
        tvStep2rd = findViewById(R.id.tv_select_dealer);
        tvStep3rd = findViewById(R.id.tv_select_time_slot);
        tvStep4rd = findViewById(R.id.tv_select_service);
        tvStep5rd = findViewById(R.id.tv_submit);
    }

    public void setIndicator(Integer step) {
        switch (step) {
            case 1:
                IndicatorUtils.displayImageIndicator(imgCircle1rd, 1);
                IndicatorUtils.displayImageIndicator(imgCircle2rd, 0);
                IndicatorUtils.displayImageIndicator(imgCircle3rd, 0);
                IndicatorUtils.displayImageIndicator(imgCircle4rd, 0);
                IndicatorUtils.displayImageIndicator(imgCircle5rd, 0);

                IndicatorUtils.displayTextIndicator(this, tvStep1rd, 1);
                IndicatorUtils.displayTextIndicator(this, tvStep2rd, 0);
                IndicatorUtils.displayTextIndicator(this, tvStep3rd, 0);
                IndicatorUtils.displayTextIndicator(this, tvStep4rd, 0);
                IndicatorUtils.displayTextIndicator(this, tvStep5rd, 0);

                break;
            case 2:

                IndicatorUtils.displayImageIndicator(imgCircle1rd, 2);
                IndicatorUtils.displayImageIndicator(imgCircle2rd, 1);
                IndicatorUtils.displayImageIndicator(imgCircle3rd, 0);
                IndicatorUtils.displayImageIndicator(imgCircle4rd, 0);
                IndicatorUtils.displayImageIndicator(imgCircle5rd, 0);

                IndicatorUtils.displayTextIndicator(this, tvStep1rd, 2);
                IndicatorUtils.displayTextIndicator(this, tvStep2rd, 1);
                IndicatorUtils.displayTextIndicator(this, tvStep3rd, 0);
                IndicatorUtils.displayTextIndicator(this, tvStep4rd, 0);
                IndicatorUtils.displayTextIndicator(this, tvStep5rd, 0);

                break;
            case 3:
                IndicatorUtils.displayImageIndicator(imgCircle1rd, 2);
                IndicatorUtils.displayImageIndicator(imgCircle2rd, 2);
                IndicatorUtils.displayImageIndicator(imgCircle3rd, 1);
                IndicatorUtils.displayImageIndicator(imgCircle4rd, 0);
                IndicatorUtils.displayImageIndicator(imgCircle5rd, 0);

                IndicatorUtils.displayTextIndicator(this, tvStep1rd, 2);
                IndicatorUtils.displayTextIndicator(this, tvStep2rd, 2);
                IndicatorUtils.displayTextIndicator(this, tvStep3rd, 1);
                IndicatorUtils.displayTextIndicator(this, tvStep4rd, 0);
                IndicatorUtils.displayTextIndicator(this, tvStep5rd, 0);

                break;
            case 4:
                IndicatorUtils.displayImageIndicator(imgCircle1rd, 2);
                IndicatorUtils.displayImageIndicator(imgCircle2rd, 2);
                IndicatorUtils.displayImageIndicator(imgCircle3rd, 2);
                IndicatorUtils.displayImageIndicator(imgCircle4rd, 1);
                IndicatorUtils.displayImageIndicator(imgCircle5rd, 0);

                IndicatorUtils.displayTextIndicator(this, tvStep1rd, 2);
                IndicatorUtils.displayTextIndicator(this, tvStep2rd, 2);
                IndicatorUtils.displayTextIndicator(this, tvStep3rd, 2);
                IndicatorUtils.displayTextIndicator(this, tvStep4rd, 1);
                IndicatorUtils.displayTextIndicator(this, tvStep5rd, 0);
                break;
            case 5:
                IndicatorUtils.displayImageIndicator(imgCircle1rd, 2);
                IndicatorUtils.displayImageIndicator(imgCircle2rd, 2);
                IndicatorUtils.displayImageIndicator(imgCircle3rd, 2);
                IndicatorUtils.displayImageIndicator(imgCircle4rd, 2);
                IndicatorUtils.displayImageIndicator(imgCircle5rd, 1);

                IndicatorUtils.displayTextIndicator(this, tvStep1rd, 2);
                IndicatorUtils.displayTextIndicator(this, tvStep2rd, 2);
                IndicatorUtils.displayTextIndicator(this, tvStep3rd, 2);
                IndicatorUtils.displayTextIndicator(this, tvStep4rd, 2);
                IndicatorUtils.displayTextIndicator(this, tvStep5rd, 1);
                break;
        }
    }

    @Override
    public void setStepIndicator(Integer step) {
        setIndicator(step);
    }
}
