//package com.honda.connmc.view.dialog;
//
//import android.graphics.Color;
//import android.graphics.drawable.ColorDrawable;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.annotation.StringRes;
//import android.support.v4.app.DialogFragment;
//import android.support.v4.app.FragmentManager;
//import android.text.TextUtils;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.Window;
//import android.widget.Button;
//import android.widget.TextView;
//
//import com.honda.connmc.R;
//
//public class DialogFragmentCommon extends DialogFragment {
//
//    private static final String ARG_MESSAGE = "message";
//    private static final String ARG_BUTTON1 = "button1";
//    private static final String ARG_BUTTON2 = "button2";
//
//    private String mMessage;
//    private String mButton1;
//    private String mButton2;
//
//    private View.OnClickListener mListener;
//    private boolean mNeedDismiss = true;
//
//    public DialogFragmentCommon() {
//        // Required empty public constructor
//    }
//
//    public static DialogFragmentCommon newInstance(@StringRes int message, @StringRes int button1, @StringRes int button2) {
//        DialogFragmentCommon fragment = new DialogFragmentCommon();
//        Bundle args = new Bundle();
//        args.putInt(ARG_MESSAGE, message);
//        args.putInt(ARG_BUTTON1, button1);
//        args.putInt(ARG_BUTTON2, button2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    public static DialogFragmentCommon newInstance(@StringRes int message, @StringRes int button1) {
//        DialogFragmentCommon fragment = new DialogFragmentCommon();
//        Bundle args = new Bundle();
//        args.putInt(ARG_MESSAGE, message);
//        args.putInt(ARG_BUTTON1, button1);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    public void setListener(View.OnClickListener onClickButton) {
//        mListener = onClickButton;
//    }
//
//    public void setDismiss(boolean dismiss) {
//        mNeedDismiss = dismiss;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            int resButton2 = getArguments().getInt(ARG_BUTTON2);
//            if (resButton2 != 0) {
//                mButton2 = getString(resButton2);
//            }
//            mButton1 = getString(getArguments().getInt(ARG_BUTTON1));
//            mMessage = getString(getArguments().getInt(ARG_MESSAGE));
//        }
//        setCancelable(false);
//    }
//
//    public void show(FragmentManager fragmentManager) {
//        show(fragmentManager, getClass().getName());
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//    }
//
//    @Override
//    public void dismiss() {
//        super.dismiss();
//        mListener = null;
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        final View view = inflater.inflate(R.layout.confirm_dialog, container, false);
//        TextView title = view.findViewById(R.id.title);
//        Button button1 = view.findViewById(R.id.btnPositive);
//        Button button2 = view.findViewById(R.id.btnNeutral);
//        title.setText(mMessage);
//        button1.setText(mButton1);
//        if (TextUtils.isEmpty(mButton2)) {
//            button2.setVisibility(View.GONE);
//        } else {
//            button2.setVisibility(View.VISIBLE);
//            button2.setText(mButton2);
//        }
//        button2.setOnClickListener(v -> {
//            if (mListener != null) {
//                mListener.onClick(v);
//            }
//            if (mNeedDismiss) {
//                dismiss();
//            }
//        });
//        button1.setOnClickListener(v -> {
//            if (mListener != null) {
//                mListener.onClick(v);
//            }
//            if (mNeedDismiss) {
//                dismiss();
//            }
//
//        });
//
//        return view;
//    }
//}
