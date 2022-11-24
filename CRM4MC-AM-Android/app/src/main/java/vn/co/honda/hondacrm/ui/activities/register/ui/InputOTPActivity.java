package vn.co.honda.hondacrm.ui.activities.register.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.utils.Constants;

public class InputOTPActivity extends AppCompatActivity {

    @BindView(R.id.btnVerify)
    Button btnVerify;

    private boolean verify;

    TextView txtCountTime, txtRemaningTime, tvMessageWrong, tvMessageTryAgain;
    EditText editOTP;
    private int numberRequest = 0;

    private CountDownTimer mCountDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_otp);
        ButterKnife.bind(this);

        txtCountTime = findViewById(R.id.txtCount);
        txtRemaningTime = findViewById(R.id.time);
        tvMessageWrong = findViewById(R.id.tvMessageWrong);
        editOTP = findViewById(R.id.editOTP);
        btnVerify = findViewById(R.id.btnVerify);

        // Default display remaining time send OTP
        setRemainingTime();

        btnVerify.setOnClickListener(view -> {
            String number = editOTP.getText().toString();
            if (number.length() != Constants.MAX_LENGHT_OTP) {
                Toast.makeText(InputOTPActivity.this, "Wrong OTP! Please try again", Toast.LENGTH_SHORT).show();
                tvMessageWrong.setVisibility(View.VISIBLE);
                tvMessageTryAgain.setVisibility(View.INVISIBLE);
                txtRemaningTime.setVisibility(View.INVISIBLE);
                txtCountTime.setVisibility(View.INVISIBLE);
            } else {
                numberRequest++;
                if (numberRequest == Constants.MAX_REQUEST_OTP) {

                } else {
                    tvMessageTryAgain.setVisibility(View.INVISIBLE);
                    tvMessageWrong.setVisibility(View.INVISIBLE);
                    txtRemaningTime.setVisibility(View.VISIBLE);
                    txtCountTime.setVisibility(View.VISIBLE);
                    setRemainingTime();
                }
//                    Toast.makeText(InputOTPActivity.this, "OK!!!", Toast.LENGTH_SHORT).show();
//                    tvMessageWrong.setVisibility(View.INVISIBLE);
//                    txtRemaningTime.setVisibility(View.VISIBLE);
//                    txtCountTime.setVisibility(View.VISIBLE);
            }
        });

        final int maxLength = 3;
        editOTP.setOnKeyListener((v, keyCode, event) -> {
            if (editOTP.getText().toString().length() >= maxLength) {
                //Show toast here
                Toast.makeText(InputOTPActivity.this, "Wrong OTP! You have exceeded maximum number  of\n" +
                        "retries. Please request a new OTP.", Toast.LENGTH_SHORT).show();
            }
            return false;
        });
    }

    @OnClick(R.id.btnVerify)
    public void onVerifyClick() {
        if (verify) {
            Intent intent = new Intent(this, SendOTPSuccessActivity.class);
            startActivity(intent);
        } else {

        }
    }

    private void setRemainingTime() {

        new CountDownTimer(10000, 1000) {

            public void onTick(long millisUntilFinished) {
//                txtCounttime.setText("" + millisUntilFinished / 1000);
                //here you can have your logic to set text to edittext
                txtCountTime.setText("" + String.format("%d:%d ",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            public void onFinish() {
                txtRemaningTime.setVisibility(View.INVISIBLE);
                txtCountTime.setVisibility(View.INVISIBLE);
                tvMessageTryAgain.setVisibility(View.VISIBLE);
            }

        }.start();
    }

}
