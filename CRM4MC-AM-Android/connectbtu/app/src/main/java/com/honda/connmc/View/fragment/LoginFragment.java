package com.honda.connmc.View.fragment;

import android.app.Dialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.honda.connmc.Bluetooth.utils.BluetoothPrefUtils;
import com.honda.connmc.R;
import com.honda.connmc.Utils.DumyData;
import com.honda.connmc.Utils.FragmentUtils;
import com.honda.connmc.Utils.KeyboardUtils;
import com.honda.connmc.Utils.LogUtils;
import com.honda.connmc.Utils.PrefUtils;
import com.honda.connmc.Utils.StringUtils;
import com.honda.connmc.View.base.BaseFragment;
import com.honda.connmc.View.dialog.DialogCommon;

import static com.honda.connmc.Bluetooth.utils.BluetoothPrefUtils.RegisterKey.KEY_BTU_NAME_REGISTER_SUCCESS;

public class LoginFragment extends BaseFragment implements View.OnClickListener {

    private EditText mEdtUsername;
    private EditText mEdtPassword;
    private RadioButton mRbThemeBlack;
    private RadioButton mRbThemeRed;
    private RadioButton mRbThemeBlackTexture;
    private RadioButton mRbThemeRedTexture;
    private Button mBtnLogin;

    @Override
    protected int getIdLayout() {
        return R.layout.login;
    }

    @Override
    protected void initView() {
        mEdtUsername = findViewById(R.id.edtUserName);
        mEdtPassword = findViewById(R.id.edtPassword);
        mRbThemeBlack = findViewById(R.id.rbThemeBlack);
        mRbThemeRed = findViewById(R.id.rbThemeRed);
        mRbThemeBlackTexture = findViewById(R.id.rbThemeBlackTexture);
        mRbThemeRedTexture = findViewById(R.id.rbThemeRedTexture);

        mBtnLogin = findViewById(R.id.btnLogin);
        mBtnLogin.setOnClickListener(this);
        mRbThemeBlack.setOnClickListener(this);
        mRbThemeRed.setOnClickListener(this);
        mRbThemeBlackTexture.setOnClickListener(this);
        mRbThemeRedTexture.setOnClickListener(this);

        initThemeSelection();
    }

    @Override
    protected void onBackPressedFragment() {
        getActivity().finish();
    }

    private void initThemeSelection() {
        LogUtils.d("initThemeSelection called");
        String crrTheme = PrefUtils.getString(PrefUtils.Settings.CURRENT_THEME);
        switch (crrTheme) {
            case PrefUtils.Settings.THEME_BLACK:
                mRbThemeBlack.setChecked(true);
                break;
            case PrefUtils.Settings.THEME_RED:
                mRbThemeRed.setChecked(true);
                break;
            case PrefUtils.Settings.THEME_BLACK_TEXTURE:
                mRbThemeBlackTexture.setChecked(true);
                break;
            case PrefUtils.Settings.THEME_RED_TEXTURE:
                mRbThemeRedTexture.setChecked(true);
                break;
            default:
                mRbThemeBlack.setChecked(true);
                break;
        }
    }

    @Override
    protected void setActionForView() {
        mEdtUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable text) {
                if (StringUtils.isEmpty(mEdtUsername) || StringUtils.isEmpty(mEdtPassword)) {
                    mBtnLogin.setEnabled(false);
                } else {
                    mBtnLogin.setEnabled(true);
                }
            }
        });

        mEdtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable text) {
                if (StringUtils.isEmpty(mEdtUsername) || StringUtils.isEmpty(mEdtPassword)) {
                    mBtnLogin.setEnabled(false);
                } else {
                    mBtnLogin.setEnabled(true);
                }
            }
        });
        mEdtUsername.setText(DumyData.USERNAME);
        mEdtPassword.setText(DumyData.PASSWORD);
    }

    /**
     * Set theme for App
     * @param theme PrefUtils.Settings.THEME_NAME.
     */
    private void setTheme(String theme) {
        String crrTheme = PrefUtils.getString(PrefUtils.Settings.CURRENT_THEME);
        if (theme.equals(crrTheme)) return;
        PrefUtils.setString(PrefUtils.Settings.CURRENT_THEME, theme);
        LogUtils.d("setTheme " + theme + " successfully.");
        getActivity().recreate();
    }

    @Override
    public void onClick(View view) {
        if(!isEnableClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.btnLogin:
                String username = mEdtUsername.getText().toString();
                String password = mEdtPassword.getText().toString();
                if (TextUtils.equals(username, DumyData.USERNAME) && TextUtils.equals(password, DumyData.PASSWORD)) {
                    KeyboardUtils.hideKeyboard(mRootView);
                    if (TextUtils.isEmpty(BluetoothPrefUtils.getInstance().getString(KEY_BTU_NAME_REGISTER_SUCCESS))) {
                        FragmentUtils.addFragment(getFragmentManager(), new DeviceNotRegisterFragment(), R.id.fragment_container, true);
                    } else {
                        FragmentUtils.replaceFragment(getFragmentManager(), new VehicleStatusFragment(),
                                R.id.fragment_container, true, FragmentUtils.FragmentAnimationType.TYPE_ANIMATION_GO);
                    }
                } else {
                    showLoginFailedDialog();
                }
                mEdtPassword.setText("");
                break;

            case R.id.rbThemeBlack:
                setTheme(PrefUtils.Settings.THEME_BLACK);
                break;

            case R.id.rbThemeRed:
                setTheme(PrefUtils.Settings.THEME_RED);
                break;

            case R.id.rbThemeBlackTexture:
                setTheme(PrefUtils.Settings.THEME_BLACK_TEXTURE);
                break;
            case R.id.rbThemeRedTexture:
                setTheme(PrefUtils.Settings.THEME_RED_TEXTURE);
                break;
            default:
                break;
        }
    }

    private void showLoginFailedDialog() {
        Dialog dialogLoginFailed = DialogCommon.createPopup(getActivity(), getString(R.string.text_button_dialog_ok),getString(R.string.login_failed), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        dialogLoginFailed.show();
    }
}
