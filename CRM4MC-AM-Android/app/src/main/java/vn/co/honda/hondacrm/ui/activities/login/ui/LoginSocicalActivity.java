package vn.co.honda.hondacrm.ui.activities.login.ui;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.net.ApiService;
import vn.co.honda.hondacrm.net.core.ApiClient;
import vn.co.honda.hondacrm.net.model.user.UserProfileResponse;
import vn.co.honda.hondacrm.ui.activities.BaseActivity;
import vn.co.honda.hondacrm.ui.activities.login.model.User;
import vn.co.honda.hondacrm.ui.activities.login.presenter.LoginSocialPresenter;
import vn.co.honda.hondacrm.ui.activities.login.presenter.LoginSocialPresenterImpl;
import vn.co.honda.hondacrm.ui.activities.policy.PolicyActivity;
import vn.co.honda.hondacrm.ui.activities.policy.TermsActivity;
import vn.co.honda.hondacrm.utils.Constants;
import vn.co.honda.hondacrm.utils.DateTimeUtils;
import vn.co.honda.hondacrm.utils.DialogUtils;
import vn.co.honda.hondacrm.utils.FirebaseUtil;
import vn.co.honda.hondacrm.utils.Utils;

public class LoginSocicalActivity extends BaseActivity implements LoginSocialView, View.OnClickListener {
    private EditText edtName, edtDateOfBrth, edtPhoneNumber;
    private AutoCompleteTextView acGenders;
    private ArrayAdapter genderArrayAdapter;
    private TextView tvAccess, txtErrorName, txtErrorPhone, txtErrorPass, txtErrorPassConfirm, txtErrorBirth;
    private TextView btnSignUp;
    private LoginSocialPresenter loginSocialPresenter;
    private CheckBox cbPolicy;
    private String name;
    private String phone;
    int gender = 0;
    private String yearOfBirth;
    private ScrollView scrollView;
    boolean isAccessPolicy;
    private String date;
    private User userProfile;
    private ApiService apiService;
    private String mTypeFullAccessToken;
    int sumTrue;
    private String mSocialType;
    private String names = "";
    private String phones = "";
    private boolean isCheck = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_social);
        ButterKnife.bind(this);

        tvAccess = findViewById(R.id.tvAccess);
        edtDateOfBrth = findViewById(R.id.edt_DateOfBrth);
        acGenders = findViewById(R.id.acGenders);
        edtPhoneNumber = findViewById(R.id.edt_Phone_Number);
        btnSignUp = findViewById(R.id.btnSignUp);
        edtName = findViewById(R.id.edt_Name);
        cbPolicy = findViewById(R.id.cbPolicy);
        scrollView = findViewById(R.id.scrollRegister);
        scrollView.fullScroll(View.FOCUS_DOWN);
        txtErrorName = findViewById(R.id.txtErrorName);
        txtErrorPhone = findViewById(R.id.txtErrorPhone);
        txtErrorPass = findViewById(R.id.txtErrorPass);
        txtErrorPassConfirm = findViewById(R.id.txtErrorPassConfirm);
        txtErrorBirth = findViewById(R.id.txtErrorBirth);
        loginSocialPresenter = new LoginSocialPresenterImpl(this);
        setEnableButton(false);
        mSocialType = getIntent().getStringExtra(Constants.KEY_TYPE_SOCIAL);
        userProfile = getIntent().getParcelableExtra(Constants.KEY_SOCIAL);
        mTypeFullAccessToken = getIntent().getStringExtra(Constants.KEY_ACCESS_TOKEN);
        apiService = ApiClient.getClient(this).create(ApiService.class);
//        String fullname = SaveSharedPreference.getFullname(getApplicationContext());
//        String birtday = SaveSharedPreference.getBirtday(getApplicationContext());
        if (userProfile.getFullName() != null) {
            edtName.setText(userProfile.getFullName());
        }

        if (userProfile.getBirtDay() != null) {
            edtDateOfBrth.setText(userProfile.getBirtDay());
        }
        String[] genders = {"Male", "Female", "Other"};
        genderArrayAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, genders);

        acGenders.setAdapter(genderArrayAdapter);

        acGenders.setOnClickListener(v -> acGenders.showDropDown());
        acGenders.setSelection(Utils.convertGender(userProfile.getGender()));
        edtDateOfBrth.setOnClickListener(v -> {
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
        });
        // show access polycy
        String textAccess = "I have read and accepted with Privacy Policy and Terms and Conditions";

        SpannableString spannableString = new SpannableString(textAccess);

        ClickableSpan clickPrivacyPolycy = new ClickableSpan() {
            @Override
            public void onClick(@NotNull View view) {
                startActivity(LoginSocicalActivity.this, PolicyActivity.class, false);
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
                startActivity(LoginSocicalActivity.this, TermsActivity.class, false);
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

        cbPolicy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                isCheck =b ;
                if (TextUtils.isEmpty(edtName.getText().toString()) || TextUtils.isEmpty(edtPhoneNumber.getText().toString()) || isCheck==false){
                    setEnableButton(false);
                }else {
                    setEnableButton(true);
                }
            }
        });
//        mTypeFullAccessToken = PrefUtils.getFullTypeAccessToken(this);
        edtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                names =  charSequence.toString();
                if (TextUtils.isEmpty(names)||TextUtils.isEmpty(phones) || isCheck==false){
                    setEnableButton(false);
                }else {
                    setEnableButton(true);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edtPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                phones =  charSequence.toString();
                if (TextUtils.isEmpty(names)||TextUtils.isEmpty(phones)|| isCheck==false){
                    setEnableButton(false);
                }else {
                   setEnableButton(true);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSignUp:
                name = edtName.getText().toString();
                phone = edtPhoneNumber.getText().toString();
                yearOfBirth = edtDateOfBrth.getText().toString();
                gender = convertGender(acGenders.getText().toString());
                isAccessPolicy = cbPolicy.isChecked();
                validate(name, phone, yearOfBirth, isAccessPolicy, gender);
                break;
        }
    }

    private void requestApiSocial(String name, String phone, String dateOfBirth, Integer gender) {
        showProgressDialog();
        RequestBody requestFile;
        MultipartBody.Part body = null;
        RequestBody userBody = RequestBody.create(
                MediaType.parse("text/plain"),
                name);

        RequestBody phoneBody = RequestBody.create(
                MediaType.parse("text/plain"),
                phone);

        RequestBody dateOfBirthBody = null;
        if (!TextUtils.isEmpty(dateOfBirth)) {
            dateOfBirthBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    DateTimeUtils.convertToSendServer(dateOfBirth));
        } else {
            dateOfBirthBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    Constants.EMPTY);
        }

        apiService.updateProfileSocial(
                mTypeFullAccessToken,
                userBody,
                phoneBody,
                gender,
                dateOfBirthBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<UserProfileResponse>() {
                    @Override
                    public void onSuccess(UserProfileResponse response) {
                        hideProgressDialog();
//                        SaveSharedPreference.setUser((Context) loginSocialView, new User("", confirmPassWorld));
//                        loginSocialView.validateSuccess();
                        if (mSocialType != null) {
                            FirebaseUtil.logEventSignup(FirebaseAnalytics.getInstance(LoginSocicalActivity.this.getApplicationContext()),
                                    mSocialType);
                        }
                        String phone = edtPhoneNumber.getText().toString();
                        Intent intent = new Intent(LoginSocicalActivity.this, VerifyOtpSocialActivity.class);
                        intent.putExtra(Constants.KEY_PHONE, phone);
                        intent.putExtra(Constants.KEY_ACCESS_TOKEN, mTypeFullAccessToken);
                        startActivity(intent);
                    }

                    @Override
                    public void onError(Throwable e) {
//                        loginSocialView.showErrPhoneNumber(((Context) loginSocialView).getString(R.string.err_phone_is_exits));
                        hideProgressDialog();
//                        DialogUtils.showDialogConfirmLogin(ProfileActivity.this, R.layout.dialog_notification_erro_network, 0.9f, 0.2f, e.getMessage());
                    }
                });
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
    public void showErrBirth(String err) {
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
    public void hideErrBirth() {
        txtErrorBirth.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showErrPassWord(String err) {
        txtErrorPass.setVisibility(View.VISIBLE);
        txtErrorPass.setText(err);
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
    public void showErrCheckBox(String err) {
        Toast.makeText(this, err, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void validateSuccess() {
        if (phone.length() == 9) {
            phone = "0" + phone;
        }
        Intent intent = new Intent(this, VerifyOtpSocialActivity.class);
        intent.putExtra("phone", phone);
        startActivity(intent);
    }

    @Override
    public void showProgressDialog() {
        DialogUtils.showDialogLoadProgress(LoginSocicalActivity.this);

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

    private void validate(String name, String phoneNumber,
                          String yearBirth, boolean isAccessPolicy, int gender) {
        Log.d("TAGGGGGGGGGG", mTypeFullAccessToken);
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        sumTrue = 0;
        if (TextUtils.isEmpty(name)) {
            showErrName(getString(R.string.errNameEmpty));
        } else {
            hideErrName();
            sumTrue += 1;
        }
        if (phoneNumber.isEmpty()) {
            showErrPhoneNumber(getString(R.string.errPhoneEmpty));
        } else if (phoneNumber.length() == 9 || phoneNumber.length() == 10) {
            if (phoneNumber.length() == 9) {
                hideErrPhone();
                sumTrue += 1;
            } else {
                if (phoneNumber.charAt(0) != '0') {
                    showErrPhoneNumber(getString(R.string.errPhoneIncorrect));
                } else {
                    hideErrPhone();
                    sumTrue += 1;
                }
            }
        } else {
            showErrPhoneNumber(getString(R.string.errPhoneIncorrect));
        }

        if (!isAccessPolicy) {
        } else {
            sumTrue += 1;

        }
        if (!TextUtils.isEmpty(yearBirth)) {
            try {
                if (!DateTimeUtils.isValidateBirthDate(yearBirth)) {
                    showErrYearOfBirth(getString(R.string.err_year_incorrect));
                } else {
                    hideErrBirth();
                    sumTrue += 1;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            hideErrBirth();
            sumTrue += 1;
        }

        if (sumTrue == 4) {
            requestApiSocial(name, phoneNumber, yearBirth, gender);

        }
    }

    private boolean checkPassword(String password) {
        final String PASSWORD_PATTERN = "(?=\\S+$)";
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(password);
        if (matcher.matches()) {
            return true;
        }
        return false;
    }


}
