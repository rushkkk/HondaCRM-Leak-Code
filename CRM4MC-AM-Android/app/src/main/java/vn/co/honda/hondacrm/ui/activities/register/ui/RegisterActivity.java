package vn.co.honda.hondacrm.ui.activities.register.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

import butterknife.ButterKnife;
import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.ui.activities.BaseActivity;
import vn.co.honda.hondacrm.ui.activities.login.ui.AsteriskPasswordTransformationMethod;
import vn.co.honda.hondacrm.ui.activities.login.ui.LoginActivity;
import vn.co.honda.hondacrm.ui.activities.login.ui.VerifyOtpActivity;
import vn.co.honda.hondacrm.ui.activities.policy.PolicyActivity;
import vn.co.honda.hondacrm.ui.activities.policy.TermsActivity;
import vn.co.honda.hondacrm.ui.activities.register.presenter.RegisterPresenter;
import vn.co.honda.hondacrm.ui.activities.register.presenter.RegisterViewImpl;
import vn.co.honda.hondacrm.utils.Constants;
import vn.co.honda.hondacrm.utils.DateTimeUtils;
import vn.co.honda.hondacrm.utils.DialogUtils;
import vn.co.honda.hondacrm.utils.FirebaseUtil;

public class RegisterActivity extends BaseActivity implements View.OnClickListener, RegisterViewImpl {
    EditText edtName, edtDateOfBrth, edtPhoneNumber, edtPassWord, edtConfirmPassWord;
    AutoCompleteTextView acGenders;
    ArrayAdapter genderArrayAdapter;
    TextView tvAccess, tvPassword, tvConfirmPassword, txtErrorName, txtErrorPhone, txtErrorPass, txtErrorPassConfirm, txtErrorBirth;
    TextView btnSignUp;
    RegisterPresenter registerPresenter;
    CheckBox cbPolicy;
    String name;
    String phone;
    String pass;
    String confirmPass;
    String yearOfBirth;
    int gender = 0;
    ScrollView scrollView;
    boolean isAccessPolicy;
    private String date;
    private long mLastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        tvAccess = findViewById(R.id.tvAccess);
        tvPassword = findViewById(R.id.tv_Password);
        tvConfirmPassword = findViewById(R.id.tv_ConfirmPassword);
        edtDateOfBrth = findViewById(R.id.edt_DateOfBrth);
        acGenders = findViewById(R.id.acGenders);
        edtPhoneNumber = findViewById(R.id.edt_Phone_Number);
        btnSignUp = findViewById(R.id.btnSignUp);
        edtName = findViewById(R.id.edt_Name);
        edtPassWord = findViewById(R.id.edt_PassWord);
        edtConfirmPassWord = findViewById(R.id.edt_Confirm_PassWord);
        cbPolicy = findViewById(R.id.cbPolicy);
        edtPassWord.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        edtConfirmPassWord.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        scrollView = findViewById(R.id.scrollRegister);
        scrollView.fullScroll(View.FOCUS_DOWN);
        txtErrorName = findViewById(R.id.txtErrorName);
        txtErrorPhone = findViewById(R.id.txtErrorPhone);
        txtErrorPass = findViewById(R.id.txtErrorPass);
        txtErrorPassConfirm = findViewById(R.id.txtErrorPassConfirm);
        txtErrorBirth = findViewById(R.id.txtErrorBirth);


        registerPresenter = new RegisterPresenter(this);

        setEnableButton(false);

        String[] genders = {"Male", "Female", "Other"};
        genderArrayAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, genders);

        acGenders.setAdapter(genderArrayAdapter);

        acGenders.setOnClickListener(v -> acGenders.showDropDown());


        edtDateOfBrth.setOnClickListener(v -> {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){

            }else {
                mLastClickTime = SystemClock.elapsedRealtime();
                int years;
                int months;
                int days;
                date = edtDateOfBrth.getText().toString();
                if (!TextUtils.isEmpty(date)) {
                    String[] items1 = date.split(Constants.SPLIT_DATE);
                    String d1 = items1[0];
                    String m1 = items1[1];
                    String y1 = items1[2];
                    days = Integer.parseInt(d1);
                    months = Integer.parseInt(m1);
                    years = Integer.parseInt(y1);
                } else {
                    Calendar c = Calendar.getInstance();
                    days = c.get(Calendar.DAY_OF_MONTH);
                    months = c.get(Calendar.MONTH) + 1;
                    years = c.get(Calendar.YEAR);
                }
                DatePickerDialog dialog = new DatePickerDialog(v.getContext(), (view, year, month, dayOfMonth) -> {
                    date = DateTimeUtils.convertDisplayDateTime(dayOfMonth, month + 1, year);
                    edtDateOfBrth.setText(date);
                }, years, months - 1, days);
                dialog.show();
            }


        });


        // show access policy
        String textAccess = getString(R.string.lb_policy);

        SpannableString spannableString = new SpannableString(textAccess);

        ClickableSpan clickPrivacyPolycy = new ClickableSpan() {
            @Override
            public void onClick(@NotNull View view) {
                startActivity(RegisterActivity.this, PolicyActivity.class, false);
            }

            @Override
            public void updateDrawState(@NotNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.RED);
                ds.setUnderlineText(true);
            }
        };

        ClickableSpan clickTernsAndConditions = new ClickableSpan() {
            @Override
            public void onClick(@NotNull View view) {
                startActivity(RegisterActivity.this, TermsActivity.class, false);
            }

            @Override
            public void updateDrawState(@NotNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.RED);
                ds.setUnderlineText(true);
            }
        };

        spannableString.setSpan(clickPrivacyPolycy, 30, 45, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(clickTernsAndConditions, 49, 69, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        tvAccess.setText(spannableString);
        tvAccess.setMovementMethod(LinkMovementMethod.getInstance());
        tvAccess.setTextSize(14);
        tvAccess.setPadding(0, 0, 0, 30);
        btnSignUp.setOnClickListener(this);
        registerPresenter.validateEmpty(edtName,edtPhoneNumber,edtPassWord,edtConfirmPassWord,cbPolicy);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSignUp:
                name = edtName.getText().toString();
                phone = edtPhoneNumber.getText().toString();
                pass = edtPassWord.getText().toString();
                confirmPass = edtConfirmPassWord.getText().toString();
                yearOfBirth = edtDateOfBrth.getText().toString();
                gender = convertGender(acGenders.getText().toString());
                isAccessPolicy = cbPolicy.isChecked();
                registerPresenter.validate(name, phone, pass, confirmPass, yearOfBirth, isAccessPolicy, gender);
                break;
            default:
                break;
        }
    }

    private int convertGender(String gender) {
        if (gender == null) {
            return 1;
        }
        switch (gender.toLowerCase()) {
            case "male":
                return 1;
            case "female":
                return 0;
            default:
                return 2;
        }
    }

    @Override
    public void showErrName(String err) {
        txtErrorName.setVisibility(View.VISIBLE);
        txtErrorName.setText(err);
    }

    @Override
    public void showErrPhoneNumber(String err) {
        txtErrorPhone.setVisibility(View.VISIBLE);
        txtErrorPhone.setText(err);

    }

    @Override
    public void showExistPhoneNumber() {
        DialogUtils.showDialogConfirmInfo(this, R.layout.diaglog_confirm_exist, 0.9f, 0.3f, new DialogUtils.DialogListener() {
            @Override
            public void okButtonClick(Dialog dialog) {
                dialog.dismiss();
            }

            @Override
            public void cancelButtonClick() {

            }
        });
    }


    @Override
    public void showErrPassWord(String err) {
        txtErrorPass.setVisibility(View.VISIBLE);
        txtErrorPass.setText(err);
    }

    @Override
    public void showErrConfirmPassWorld(String err) {
        txtErrorPassConfirm.setVisibility(View.VISIBLE);
        txtErrorPassConfirm.setText(err);
    }

    @Override
    public void showErrYearOfBirth(String err) {
        txtErrorBirth.setVisibility(View.VISIBLE);
        txtErrorBirth.setText(err);
    }

    @Override
    public void hideErrName() {
        txtErrorName.setVisibility(View.INVISIBLE);
    }

    @Override
    public void hideErrPhone() {
        txtErrorPhone.setVisibility(View.INVISIBLE);
    }

    @Override
    public void hideErrPass() {
        txtErrorPass.setVisibility(View.INVISIBLE);
    }

    @Override
    public void hideErrPassConfirm() {
        txtErrorPassConfirm.setVisibility(View.INVISIBLE);
    }

    @Override
    public void hideErrBirth() {
        txtErrorBirth.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showErrCheckBox(String err) {
        Toast.makeText(this, err, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void validateSuccess(String token) {
        FirebaseUtil.logEventSignup(FirebaseAnalytics.getInstance(RegisterActivity.this.getApplicationContext()),
                FirebaseUtil.EVENT_SINGUP_GOOGLE);
        if (phone.length() == 9) {
            phone = "0" + phone;
        }
        Intent intent = new Intent(this, VerifyOtpActivity.class);
        intent.putExtra("phone", phone);
        intent.putExtra("token", token);
        startActivity(intent);
    }

    @Override
    public void showProgressDialog() {
        DialogUtils.showDialogLoadProgress(RegisterActivity.this);

    }

    @Override
    public void hideProgressDialog() {
        DialogUtils.hideDialogLoadProgress();
    }

    @Override
    public void setEnableButton(boolean b) {
        btnSignUp.setEnabled(b);
        if (b) {
            btnSignUp.setBackground(getDrawable(R.drawable.selector_red_button));
        } else {
            btnSignUp.setBackground(getDrawable(R.drawable.btn_signup_disable));
        }
    }
}
