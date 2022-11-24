//package vn.co.honda.hondacrm.ui.activities.login.ui;
//
//import android.app.DatePickerDialog;
//import android.app.Dialog;
//import android.content.Intent;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.text.SpannableString;
//import android.text.Spanned;
//import android.text.TextPaint;
//import android.text.method.LinkMovementMethod;
//import android.text.style.ClickableSpan;
//import android.util.Log;
//import android.view.View;
//import android.widget.ArrayAdapter;
//import android.widget.AutoCompleteTextView;
//import android.widget.CheckBox;
//import android.widget.EditText;
//import android.widget.ScrollView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import org.jetbrains.annotations.NotNull;
//
//import butterknife.ButterKnife;
//import vn.co.honda.hondacrm.R;
//import vn.co.honda.hondacrm.ui.activities.BaseActivity;
//import vn.co.honda.hondacrm.ui.activities.login.presenter.LoginSocialPresenter;
//import vn.co.honda.hondacrm.ui.activities.login.presenter.LoginSocialPresenterImpl;
//import vn.co.honda.hondacrm.ui.activities.login.presenter.SaveSharedPreference;
//import vn.co.honda.hondacrm.ui.activities.policy.PolicyActivity;
//import vn.co.honda.hondacrm.ui.activities.policy.TermsActivity;
//import vn.co.honda.hondacrm.utils.DialogUtils;
//
//public class InputNumberPhoneActivity extends BaseActivity implements LoginSocialView,View.OnClickListener {
//    EditText edtName, edtDateOfBrth, edtPhoneNumber, edtPassWord, edtConfirmPassWord;
//    AutoCompleteTextView acGenders;
//    ArrayAdapter genderArrayAdapter;
//    TextView tvAccess, tvPassword, tvConfirmPassword;
//    TextView btnSignUp;
//    LoginSocialPresenter loginSocialPresenter;
//    CheckBox cbPolicy;
//    String name ;
//    String phone ;
//    String pass ;
//    String confirmPass ;
//    String yearOfBirth;
//    ScrollView scrollView;
//    boolean isAccessPolicy ;
//    private Dialog dialog;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login_social);
//        changeStatusBarColor(Color.TRANSPARENT);
//        ButterKnife.bind(this);
//
//        tvAccess = findViewById(R.id.tvAccess);
//        tvPassword = findViewById(R.id.tv_Password);
//        tvConfirmPassword = findViewById(R.id.tv_ConfirmPassword);
//        edtDateOfBrth = findViewById(R.id.edt_DateOfBrth);
//        acGenders = findViewById(R.id.acGenders);
//        edtPhoneNumber = findViewById(R.id.edt_Phone_Number);
//        btnSignUp = findViewById(R.id.btnSignUp);
//        edtName = findViewById(R.id.edt_Name);
//        cbPolicy = findViewById(R.id.cbPolicy);
//        scrollView =  findViewById(R.id.scrollRegister);
//        scrollView.fullScroll(View.FOCUS_DOWN);
//        loginSocialPresenter=  new LoginSocialPresenterImpl(this);
//
//        String fullname =  SaveSharedPreference.getFullname(getApplicationContext());
//        String birtday = SaveSharedPreference.getBirtday(getApplicationContext());
//        if (fullname != null){
//            edtName.setText(fullname);
//        }
//
//        if (birtday !=  null){
//            edtDateOfBrth.setText(birtday);
//        }
//
//        String[] genders = {"Male", "Female", "Other"};
//        genderArrayAdapter = new ArrayAdapter<>(
//                this, android.R.layout.simple_list_item_1, genders);
//
//        acGenders.setAdapter(genderArrayAdapter);
//
//        acGenders.setOnClickListener(v -> acGenders.showDropDown());
//
//
//        edtDateOfBrth.setOnClickListener(v -> {
//            DatePickerDialog dialog = new DatePickerDialog(v.getContext(), (view, year, month, dayOfMonth) -> {
//                String birth = dayOfMonth + "/" + month + "/" + year;
//                edtDateOfBrth.setText(birth);
//            }, 2019, 1, 1);
//            dialog.show();
//        });
//
//
//        // show access polycy
//        String textAccess = "I have read and accepted with Privacy Policy and Terns and Conditions";
//
//        SpannableString spannableString = new SpannableString(textAccess);
//
//        ClickableSpan clickPrivacyPolycy = new ClickableSpan() {
//            @Override
//            public void onClick(@NotNull View view) {
//                startActivity(InputNumberPhoneActivity.this, PolicyActivity.class, false);
//            }
//
//            @Override
//            public void updateDrawState(@NotNull TextPaint ds) {
//                super.updateDrawState(ds);
//                ds.setColor(Color.RED);
//                ds.setUnderlineText(true);
//            }
//        };
//
//        ClickableSpan clickTernsAndConditions = new ClickableSpan() {
//            @Override
//            public void onClick(@NotNull View view) {
//                startActivity(InputNumberPhoneActivity.this, TermsActivity.class, false);
//            }
//
//            @Override
//            public void updateDrawState(@NotNull TextPaint ds) {
//                super.updateDrawState(ds);
//                ds.setColor(Color.RED);
//                ds.setUnderlineText(true);
//            }
//        };
//
//        spannableString.setSpan(clickPrivacyPolycy, 30, 45, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        spannableString.setSpan(clickTernsAndConditions, 49, 69, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//        tvAccess.setText(spannableString);
//        tvAccess.setMovementMethod(LinkMovementMethod.getInstance());
//        tvAccess.setTextSize(14);
//        tvAccess.setPadding(0, 0, 0, 30);
//
//
//        btnSignUp.setOnClickListener(this);
//
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.btnSignUp:
//                name = edtName.getText().toString();
//                phone = edtPhoneNumber.getText().toString();
//                yearOfBirth = edtDateOfBrth.getText().toString();
//                String yearBirth = "";
//                if (!yearOfBirth.isEmpty()) {
//                    yearBirth = yearOfBirth.split("/")[2];
//                }
//                isAccessPolicy = cbPolicy.isChecked();
//                loginSocialPresenter.validate(name, phone , yearBirth, isAccessPolicy);
//                Log.d("cb", cbPolicy.isChecked() + "");
//                break;
//
//
//        }
//    }
//
//    @Override
//    public void showErrName(String err) {
//        edtName.setError(err);
//    }
//
//    @Override
//    public void showErrPhoneNumber(String err) {
//        edtPhoneNumber.setError(err);
//    }
//
//    @Override
//    public void showErrPassWord(String err) {
//        edtPassWord.setError(err);
//    }
//
//    @Override
//    public void showErrConfirmPassWorld(String err) {
//        edtConfirmPassWord.setError(err);
//    }
//
//    @Override
//    public void showErrYearOfBirth(String err) {
//        edtDateOfBrth.setError(err);
//    }
//
//    @Override
//    public void showErrCheckBox(String err) {
//        Toast.makeText(this, err, Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void validateSuccess() {
//        Intent intent = new Intent(this, VerifyOtpActivity.class);
//        intent.putExtra("phone",phone);
//        startActivity(intent);
//    }
//
//    @Override
//    public void showProgressDialog() {
//        DialogUtils.showDialogLoadProgress(InputNumberPhoneActivity.this);
//
//    }
//
//    @Override
//    public void hideProgressDialog() {
//        DialogUtils.hideDialogLoadProgress();
//    }
//}
