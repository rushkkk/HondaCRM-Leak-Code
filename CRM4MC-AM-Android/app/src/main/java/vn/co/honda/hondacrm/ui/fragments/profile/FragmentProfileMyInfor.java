package vn.co.honda.hondacrm.ui.fragments.profile;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.navigator.bus.Events;
import vn.co.honda.hondacrm.navigator.bus.GlobalBus;
import vn.co.honda.hondacrm.net.ApiService;
import vn.co.honda.hondacrm.net.core.ApiClient;
import vn.co.honda.hondacrm.net.model.user.UserProfile;
import vn.co.honda.hondacrm.net.model.user.UserProfileResponse;
import vn.co.honda.hondacrm.ui.fragments.profile.models.DateTime;
import vn.co.honda.hondacrm.utils.Constants;
import vn.co.honda.hondacrm.utils.DateTimeUtils;
import vn.co.honda.hondacrm.utils.DialogUtils;
import vn.co.honda.hondacrm.utils.PrefUtils;
import vn.co.honda.hondacrm.utils.Utils;


public class FragmentProfileMyInfor extends Fragment {
    private Spinner spnCategory;
    EditText edit_profile_birthday, edit_profile_mail, edit_profile_job, edit_profile_idnumber, edit_profile_address;
    String date = "";
    TextView txtBirthdate, txtEmailUser, txtfill_Update, txtLoyalti, txtAddress, txtfill_Gender, lb_Invalid_Birthday, lb_Invalid_Email;
    ImageButton imgLogin;
    UserProfile user;
    SeekBar process_profile;
    int idlogin = 0;
    View viewContainerEdit;
    View viewContainerDone;
    Status status;
    Boolean validate_id_number = false;
    StatusDate statusDate;
    StatusIdNumber statusIdNumber;
    DataUser dataUser;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    //Set API
    Bitmap bitmap;
    ApiService apiService;
    String mTypeFullAccessToken;
    private DateTime mDateTime;
    //id image social
    ImageView ic_fb_active,ic_zalo_active,ic_fb_inactive,ic_zalo_inactive,ic_google_active,ic_google_inactive;
    ImageView ic_fb_active_edit,ic_zalo_active_edit,ic_fb_inactive_edit,ic_zalo_inactive_edit,ic_google_active_edit,ic_google_inactive_edit;
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_my_infor_edit, container, false);
        viewContainerEdit = view.findViewById(R.id.llEdit);
        viewContainerDone = view.findViewById(R.id.llDone);
        spnCategory = view.findViewById(R.id.spnCategory);
        //get api
        apiService = ApiClient.getClient(getContext()).create(ApiService.class);
        mTypeFullAccessToken = PrefUtils.getFullTypeAccessToken(getContext());
        //userApi= new User("19/2/1995","Male","hoangminhtrung1902@gmail.com","Dev","+84964761897","K82/45 Nguyễn Lương Bằng,Liên Chiểu,Đà Nẵng",0,((BitmapDrawable) getActivity().getDrawable(R.drawable.profile)).getBitmap());

        //view edit profile
        edit_profile_birthday = view.findViewById(R.id.edit_profile_birthday);
        edit_profile_birthday.setEnabled(true);
        edit_profile_mail = view.findViewById(R.id.edit_profile_mail);
        edit_profile_job = view.findViewById(R.id.edit_profile_job);
        edit_profile_idnumber = view.findViewById(R.id.edit_profile_idnumber);
        edit_profile_address = view.findViewById(R.id.edit_profile_address);
        //imgLogin = view.findViewById(R.id.imgLogin);
        //view text info
        txtBirthdate = view.findViewById(R.id.txtBirthdate);
        txtEmailUser = view.findViewById(R.id.txtEmailUser);
        txtfill_Update = view.findViewById(R.id.txtfill_Update);
        txtLoyalti = view.findViewById(R.id.txtLoyalti);
        txtAddress = view.findViewById(R.id.txtAddress);
        txtfill_Gender = view.findViewById(R.id.txtfill_Gender);

        process_profile=view.findViewById(R.id.process_profile);
        //process_profile.setEnabled(false);
        //image view social
//        ic_fb_active=view.findViewById(R.id.ic_fb_active);
//        ic_zalo_active=view.findViewById(R.id.ic_zalo_active);
//        ic_fb_inactive=view.findViewById(R.id.ic_fb_inactive);
//        ic_zalo_inactive=view.findViewById(R.id.ic_zalo_inactive);
//        ic_google_active=view.findViewById(R.id.ic_google_active);
//        ic_google_inactive=view.findViewById(R.id.ic_google_inactive);
        //image view social edit
//        ic_fb_active_edit=view.findViewById(R.id.ic_fb_active_edit);
//        ic_zalo_active_edit=view.findViewById(R.id.ic_zalo_active_edit);
//        ic_fb_inactive_edit=view.findViewById(R.id.ic_fb_inactive_edit);
//        ic_zalo_inactive_edit=view.findViewById(R.id.ic_zalo_inactive_edit);
//        ic_google_active_edit=view.findViewById(R.id.ic_google_active_edit);
//        ic_google_inactive_edit=view.findViewById(R.id.ic_google_inactive_edit);
        //text error
        lb_Invalid_Birthday = view.findViewById(R.id.lb_Invalid_Birthday);
        lb_Invalid_Email = view.findViewById(R.id.lb_Invalid_Email);
        List<String> list = new ArrayList<>();
        list.add(getString(R.string.Male));
        list.add(getString(R.string.Female));
        list.add(getString(R.string.Other));
        mDateTime = new DateTime();
        // strtext = getArguments().getString("status");
        //validate
        edit_profile_mail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if((edit_profile_mail.getText().toString()!=null)&&(!edit_profile_mail.getText().toString().isEmpty())) {
                    if (edit_profile_mail.getText().toString().matches(emailPattern) && edit_profile_mail.getText().toString().length() > 0) {
                        lb_Invalid_Email.setVisibility(View.INVISIBLE);
                        validate_id_number = true;
                        passData(true);
                    } else {
                        passData(false);
                        lb_Invalid_Email.setVisibility(View.VISIBLE);
                        validate_id_number = false;
                    }
                }else {
                    passData(true);
                    lb_Invalid_Email.setVisibility(View.INVISIBLE);
                }
            }
        });
        process_profile.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        ArrayAdapter<String> adapter = new ArrayAdapter(getActivity(), R.layout.spiner_item, list);
        adapter.setDropDownViewResource(R.layout.check_spiner_gener);
        spnCategory.setAdapter(adapter);
        requestProfile();

        spnCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Toast.makeText(getActivity(), spnCategory.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        if (imgLogin != null) {
            imgLogin.setOnClickListener(v -> idlogin = 1);
        }

        //set birthday
        edit_profile_birthday.setOnClickListener(view1 -> {
            if (!TextUtils.isEmpty(edit_profile_birthday.getText().toString())) {
                date = (edit_profile_birthday.getText().toString());
                String[] items1;
                if (date.contains("-")) {
                    items1 = date.split("-");
                } else  {
                    items1 = date.split(Constants.SPLIT_DATE);
                }
                String d1 = items1[0];
                String m1 = items1[1];
                String y1 = items1[2];
                int day = Integer.parseInt(d1);
                int month = Integer.parseInt(m1);
                int year = Integer.parseInt(y1);
                mDateTime.setDayOfMonth(day);
                mDateTime.setMonth(month);
                mDateTime.setYear(year);

                // Check error validate from server
                if (day > 31) {
                    day = Integer.parseInt(y1);
                    year = Integer.parseInt(d1);
                    mDateTime.setDayOfMonth(day);
                    mDateTime.setYear(year);
                }
                showDateTime(year, month - 1, day);

            } else {
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                showDateTime(year, month, day);
            }
        });
        //validate date
        edit_profile_birthday.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    if (DateTimeUtils.isValidateBirthDate(edit_profile_birthday.getText().toString())) {
                        passDataDate(true);
                        lb_Invalid_Birthday.setVisibility(View.INVISIBLE);

                        // edit_profile_birthday.setError(null);
                    } else {
                        lb_Invalid_Birthday.setVisibility(View.VISIBLE);
                        passDataDate(false);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }

    public boolean valiIdNumber(String idnumber) {
        try {
            Integer.parseInt(idnumber);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * This method using to show Date Time Dialog.
     *
     * @param year        Year
     * @param monthOfYear month
     * @param dayOfMonth  day
     */
    private void showDateTime(int year, int monthOfYear, int dayOfMonth) {
        if (getContext() != null) {
            DatePickerDialog dialog =
                    new DatePickerDialog(getContext(), (datePicker, yearPicker, monthOfYearPicker, dayOfMonthPicker) -> {
                        date = DateTimeUtils.convertDisplayDateTime(dayOfMonthPicker,monthOfYearPicker + 1, yearPicker);
                        mDateTime.setDayOfMonth(dayOfMonthPicker);
                        mDateTime.setMonth(monthOfYearPicker);
                        mDateTime.setYear(dayOfMonthPicker);
                        edit_profile_birthday.setText(date);
                    }, year, monthOfYear, dayOfMonth);
            dialog.show();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dataUser = (DataUser) context;
        status = (Status) context;
        statusDate = (StatusDate) context;
        statusIdNumber = (StatusIdNumber) context;
        GlobalBus.getBus().register(this);
    }

    @Subscribe
    public void getMessage(Events.ActivityFragmentMessage activityFragmentMessage) throws ParseException {
        if (activityFragmentMessage.getMessage().equals("0")) {
            if (activityFragmentMessage.getType() == 0) {
                viewContainerEdit.setVisibility(View.VISIBLE);
                viewContainerDone.setVisibility(View.INVISIBLE);
                //send id object
            } else {
                String textDate = DateTimeUtils.convertToSendServer(edit_profile_birthday.getText().toString());
                String textDateVali= edit_profile_birthday.getText().toString()+Constants.EMPTY;
                String textEmail = edit_profile_mail.getText().toString()+Constants.EMPTY;
                String txtGender = spnCategory.getSelectedItem().toString();
                String txtJob = edit_profile_job.getText().toString();
                String txtIdNumber = edit_profile_idnumber.getText().toString()+Constants.EMPTY;
                String tAddress = edit_profile_address.getText().toString();
                if (kiemTraNullBirthDay(date)) {
                    if (kiemTraEmptyEmail(textEmail)) {
                        viewContainerEdit.setVisibility(View.GONE);
                        viewContainerDone.setVisibility(View.VISIBLE);
                        //get
                        user = new UserProfile();
                        user.setDateOfBirth(textDate);
                        if (txtGender.equals("Male")) {
                            user.setGender("1");
                        } else if (txtGender.equals("Female")) {
                            user.setGender("0");
                        } else {
                            user.setGender("2");
                        }
                        user.setEmail(textEmail);
                        user.setJob(txtJob);
                        user.setIdNumber(txtIdNumber);
                        user.setAddress(tAddress);
                        if (user != null) {
                            process_profile.setProgress(checkProfile(user));

                            //requestUpdateProfile(user);
                            passDataUser(user);
                            txtBirthdate.setText(textDateVali);
                            txtEmailUser.setText(user.getEmail());
                            txtfill_Update.setText(user.getJob());
                            txtLoyalti.setText(user.getIdNumber());
                            txtAddress.setText(user.getAddress());

                            txtfill_Gender.setText(txtGender);
                        }
                        passData(true);
                        lb_Invalid_Email.setVisibility(View.INVISIBLE);
                        lb_Invalid_Birthday.setVisibility(View.INVISIBLE);
                    } else {
                        // edit_profile_mail.setError(getString(R.string.Invalid_email_address));
                        lb_Invalid_Email.setVisibility(View.VISIBLE);
                        passData(false);

                    }
                } else {
                    //  edit_profile_birthday.setError(getString(R.string.Invalid_Birthday));
                    lb_Invalid_Birthday.setVisibility(View.VISIBLE);
                }
            }
        }

    }

    //fuction kiem tra empty birthday
    public boolean kiemTraNullBirthDay(String date){
        if(date.isEmpty()){
            return true;
        }else {
            try {
                return DateTimeUtils.isValidateBirthDate(date);
            } catch (ParseException e) {
                e.printStackTrace();
                return false;
            }
        }

    }
    //fuction kiem tra empty email
    public boolean kiemTraEmptyEmail(String email){
        if(email.isEmpty()){
            return true;
        }else {
            return email.matches(emailPattern) && email.length() > 0;
        }

    }
    //fuction kiem tra empty idNumber
    public boolean kiemTraEmptyIdNumber(String idNumber){
        if(idNumber.isEmpty()){
            return true;
        }else {
            return valiIdNumber(idNumber);
        }

    }

    private void requestProfile() {
        apiService.getUserProfile(
                mTypeFullAccessToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<UserProfileResponse>() {
                    @Override
                    public void onSuccess(UserProfileResponse response) {
                        URL imageurl = null;
                        initViewProfile(response.getUserProfile());
                    }

                    @Override
                    public void onError(Throwable e) {
                        DialogUtils.showDialogConfirmLogin(getContext(), R.layout.dialog_add_vehicle_success, 0.9f, 0.2f, getString(R.string.networkError));
                    }
                });
    }
    public int checkProfile(UserProfile userProfile){
        int dem=0;
        if(userProfile.getAddress()!=null && !userProfile.getAddress().isEmpty() ){
            dem++;
        }
        if(userProfile.getEmail()!=null && !userProfile.getEmail().isEmpty() ){
            dem++;
        }
        if(userProfile.getDateOfBirth()!=null && !userProfile.getDateOfBirth().isEmpty() ){
            dem++;
        }
        if(userProfile.getIdNumber()!=null && !userProfile.getIdNumber().isEmpty() ){
            dem++;
        }
        if(userProfile.getGender()!=null && !userProfile.getGender().isEmpty() ){
            dem++;
        }
        if(userProfile.getJob()!=null && !userProfile.getJob().isEmpty() ){
            dem++;
        }
        return dem;
    }

    public void initViewProfile(UserProfile userProfile) {
        //set text edit user
        edit_profile_birthday.setText(convertStringObject(DateTimeUtils.convertToDisplay(userProfile.getDateOfBirth())));
        txtBirthdate.setText(convertStringObject(DateTimeUtils.convertToDisplay(userProfile.getDateOfBirth())));
        //

        edit_profile_mail.setText(convertStringObject(userProfile.getEmail()));
        txtEmailUser.setText(convertStringObject(userProfile.getEmail()));

        //

        edit_profile_job.setText(convertStringObject(userProfile.getJob()));
        txtfill_Update.setText(convertStringObject(userProfile.getJob()));

        //

        edit_profile_idnumber.setText(convertStringObject(userProfile.getIdNumber()));
        txtLoyalti.setText(convertStringObject(userProfile.getIdNumber()));

        //

        edit_profile_address.setText(convertStringObject(userProfile.getAddress()));
        txtAddress.setText(convertStringObject(userProfile.getAddress()));



        process_profile.setProgress(checkProfile(userProfile));
        if (userProfile.getGender() == null) {
            spnCategory.setSelection(0);
            txtfill_Gender.setText("Male");
        } else {
            if (TextUtils.equals(userProfile.getGender(), "0")) {
                spnCategory.setSelection(1);
            } else if (userProfile.getGender().equals("1")) {
                spnCategory.setSelection(0);
            } else {
                spnCategory.setSelection(2);
            }

            if (userProfile.getGender().equals("0")) {
                txtfill_Gender.setText("Female");
            } else if (userProfile.getGender().equals("1")) {
                txtfill_Gender.setText("Male");
            } else {
                txtfill_Gender.setText("Other");
            }
        }
//        if(userProfile.getZaloId()!=null){
//            ic_zalo_active.setVisibility(View.VISIBLE);
//            ic_zalo_inactive.setVisibility(View.INVISIBLE);
//        }else{
//            ic_zalo_active.setVisibility(View.INVISIBLE);
//            ic_zalo_inactive.setVisibility(View.VISIBLE);
//        }
//        if(userProfile.getGoogleId()!=null){
//            ic_google_active.setVisibility(View.VISIBLE);
//            ic_google_inactive.setVisibility(View.INVISIBLE);
//        }else{
//            ic_google_active.setVisibility(View.INVISIBLE);
//            ic_google_inactive.setVisibility(View.VISIBLE);
//        }
//        if(userProfile.getZaloId()!=null){
//            ic_fb_active.setVisibility(View.VISIBLE);
//            ic_fb_inactive.setVisibility(View.INVISIBLE);
//        }else{
//            ic_fb_active.setVisibility(View.INVISIBLE);
//            ic_fb_inactive.setVisibility(View.VISIBLE);
//        }
//        if(userProfile.getZaloId()!=null){
//            ic_zalo_active_edit.setVisibility(View.VISIBLE);
//            ic_zalo_inactive_edit.setVisibility(View.INVISIBLE);
//        }else{
//            ic_zalo_active_edit.setVisibility(View.INVISIBLE);
//            ic_zalo_inactive_edit.setVisibility(View.VISIBLE);
//        }
//        if(userProfile.getGoogleId()!=null){
//            ic_google_active_edit.setVisibility(View.VISIBLE);
//            ic_google_inactive_edit.setVisibility(View.INVISIBLE);
//        }else{
//            ic_google_active_edit.setVisibility(View.INVISIBLE);
//            ic_google_inactive_edit.setVisibility(View.VISIBLE);
//        }
//        if(userProfile.getZaloId()!=null){
//            ic_fb_active_edit.setVisibility(View.VISIBLE);
//            ic_fb_inactive_edit.setVisibility(View.INVISIBLE);
//        }else{
//            ic_fb_active_edit.setVisibility(View.INVISIBLE);
//            ic_fb_inactive_edit.setVisibility(View.VISIBLE);
//        }
    }

    private String convertStringObject(String object) {
        return object == null ? "" : object;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        GlobalBus.getBus().unregister(this);
    }

    //validate email
    public void passData(boolean changeSattus) {
        status.ChangeStatus(changeSattus);
    }

    public interface Status {
        void ChangeStatus(boolean changeSattus);
    }

    //validate birthday
    public void passDataDate(boolean changeSattus) {
        statusDate.ChangeStatusDate(changeSattus);

    }

    public interface StatusDate {
        void ChangeStatusDate(boolean changeSattus);
    }

    //validate id_andress
    public void passDataIdNumber(boolean changeSattus) {
        statusIdNumber.ChangeStatusId(changeSattus);

    }

    public interface StatusIdNumber {
        void ChangeStatusId(boolean changeSattus);
    }

    //send data user
    public void passDataUser(UserProfile userProfile) {
        dataUser.SendDataUser(userProfile);

    }

    public interface DataUser {
        void SendDataUser(UserProfile userProfile);
    }
}
