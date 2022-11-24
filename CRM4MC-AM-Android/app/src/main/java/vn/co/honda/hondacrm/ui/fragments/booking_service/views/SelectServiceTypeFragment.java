package vn.co.honda.hondacrm.ui.fragments.booking_service.views;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.net.ApiService;
import vn.co.honda.hondacrm.net.core.ApiClient;
import vn.co.honda.hondacrm.net.model.booking_service.ListTypeService;
import vn.co.honda.hondacrm.net.model.booking_service.TypeService;
import vn.co.honda.hondacrm.ui.activities.booking_service.BookingServiceActivity;
import vn.co.honda.hondacrm.ui.activities.booking_service.INumberStepCallBack;
import vn.co.honda.hondacrm.ui.fragments.booking_service.models.BookService;
import vn.co.honda.hondacrm.ui.fragments.profile.models.DateTime;
import vn.co.honda.hondacrm.utils.Constants;
import vn.co.honda.hondacrm.utils.DateTimeUtils;
import vn.co.honda.hondacrm.utils.PrefUtils;


public class SelectServiceTypeFragment extends Fragment{
    public BookService bookService = new BookService();
    private INumberStepCallBack mNumberStepCallBack;
    public static final int MY_FRAGMENT_ID = 1;
    private Spinner spnTypeService;
    private Context mContext;
    private TextView txtNameDealer, txtPhonDealer, txtAddressDealer, txtTimeWorking, txtCavityWay, txtExpecDay, txtYourAdditional, txtSelectedTime;
    private TextView txtErrYourAdditional,txtErrDateExpec,txtErrTimeExpec,txtErrTypeService;
    private String date;
    private DateTime mDateTime;
    private ImageView btnNextStep4;

   private AutoCompleteTextView acExpecTime;
    private ArrayAdapter genderArrayAdapter;
    private ApiService apiService;
    private String mTypeFullAccessToken;
    private List<TypeService> typeServices;
    private vn.co.honda.hondacrm.ui.adapters.bookingservice.TypeServiceAdapter adapter;
    private String stringTypeService ="";

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BookingServiceActivity) {
            this.mNumberStepCallBack = (BookingServiceActivity) context;
        }
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_service_type, container, false);
        if (getArguments() != null){
            bookService = (BookService) getArguments().getSerializable("bookService");
        }
        initView(view);
        return view;
    }

    public void initView(View view) {
        mDateTime = new DateTime();
        txtNameDealer = view.findViewById(R.id.tv_dealer_name_step4);
        txtPhonDealer = view.findViewById(R.id.tv_dealer_phone_number_step4);
        txtAddressDealer = view.findViewById(R.id.tv_dealer_address_step4);
        txtTimeWorking = view.findViewById(R.id.tv_dealer_work_time);
        txtCavityWay = view.findViewById(R.id.tv_dealer_distance);
        txtExpecDay = view.findViewById(R.id.txt_expec_day);
        acExpecTime = view.findViewById(R.id.acExpectTime);
        txtYourAdditional = view.findViewById(R.id.txt_your_additianal);
        txtSelectedTime = view.findViewById(R.id.txt_select_time);
        spnTypeService = view.findViewById(R.id.spn_selecte_type_service);
        txtErrYourAdditional = view.findViewById(R.id.txtErrorYourAdditional);
        txtErrDateExpec = view.findViewById(R.id.txtErrorDateExpec);
        txtErrTimeExpec = view.findViewById(R.id.txtErrorTimeExpec);
        btnNextStep4 = view.findViewById(R.id.btnNextStep5);
        txtErrTypeService = view.findViewById(R.id.txtErrTypeService);

        String typeVerhicle = bookService.getDealer().getDealerType();
        if (TextUtils.equals(typeVerhicle,"1")){
            int maxLength = 40;
            InputFilter[] fArray = new InputFilter[1];
            fArray[0] = new InputFilter.LengthFilter(maxLength);
            txtYourAdditional.setFilters(fArray);
        }else if(TextUtils.equals(typeVerhicle,"2")){
            int maxLength = 120;
            InputFilter[] fArray = new InputFilter[1];
            fArray[0] = new InputFilter.LengthFilter(maxLength);
            txtYourAdditional.setFilters(fArray);
        }

    }

    private void showDateTime(int year, int monthOfYear, int dayOfMonth) {
        if (getContext() != null) {
            DatePickerDialog dialog =
                    new DatePickerDialog(getContext(), (datePicker, yearPicker, monthOfYearPicker, dayOfMonthPicker) -> {
                        date = DateTimeUtils.convertDisplayDateTime(dayOfMonthPicker, monthOfYearPicker + 1, yearPicker);
                        mDateTime.setDayOfMonth(dayOfMonthPicker);
                        mDateTime.setMonth(monthOfYearPicker);
                        mDateTime.setYear(dayOfMonthPicker);
                        SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy");
                        SimpleDateFormat dt1 = new SimpleDateFormat("yyyy/MM/dd");
                        try {

                            Date dateLast = dt.parse(date);
                            txtExpecDay.setText(dt1.format(dateLast));

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }, year, monthOfYear, dayOfMonth);

            dialog.show();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txtNameDealer.setText(bookService.getDealer().getDealerName());
        txtAddressDealer.setText(bookService.getDealer().getAddress());
        txtTimeWorking.setText(bookService.getDealer().getWorkingStart()+" - "+bookService.getDealer().getWorkingEnd());
        txtCavityWay.setText(bookService.getDealer().getDistance()+"km");
        txtPhonDealer.setText(bookService.getDealer().getPhone());

        txtExpecDay.setText(bookService.getDateSelected());
        txtSelectedTime.setText(bookService.getDateSelected() + " "+ bookService.getTimeSelected());
        apiService = ApiClient.getClient(mContext).create(ApiService.class);
        mTypeFullAccessToken = PrefUtils.getFullTypeAccessToken(mContext);
        String[] genders = getActivity().getResources().getStringArray(R.array.list_time_expec);
        ArrayAdapter adapterCountries = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,genders);
        acExpecTime.setAdapter(adapterCountries);
        acExpecTime.setOnClickListener(v -> acExpecTime.showDropDown());
        typeServices= new ArrayList<>();
        adapter = new vn.co.honda.hondacrm.ui.adapters.bookingservice.TypeServiceAdapter(getContext(), typeServices);
        spnTypeService.setAdapter(adapter);
        getListTypeService();

        spnTypeService.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TypeService  typeService =  adapter.getItem(i);
                stringTypeService = typeService.getNameVi();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        txtExpecDay.setOnClickListener(view1 -> {
            if (!TextUtils.isEmpty(txtExpecDay.getText().toString())) {
                date = (txtExpecDay.getText().toString());
                String[] items1 = date.split(Constants.SPLIT_DATE);
                String d1 = items1[0];
                String m1 = items1[1];
                String y1 = items1[2];
                int year = Integer.parseInt(d1);
                int month = Integer.parseInt(m1);
                int day = Integer.parseInt(y1);
                showDateTime(year, month-1, day);
            }
        });
        btnNextStep4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateBookingStep4();

            }
        });


    }
    private void getListTypeService(){

        apiService.getListTypeService(
                mTypeFullAccessToken
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<ListTypeService>() {
                    @Override
                    public void onSuccess(ListTypeService response) {
                        if (typeServices != null) {
                            typeServices.clear();
                            for (TypeService x: response.getData()) {
                                if (x.getVehicleType().equals(x.getVehicleType())){
                                    typeServices.add(x);
                                }
                            }

                            adapter.notifyDataSetChanged();
                        }
                    }
                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }
    private void validateBookingStep4(){
        int sumVilidate = 0;
        SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat dt1 = new SimpleDateFormat("yyyy/MM/dd");

        String yourAdditional = txtYourAdditional.getText().toString();
        String dateExpec = txtExpecDay.getText().toString();
        String timeExpect =  acExpecTime.getText().toString();
        if (TextUtils.isEmpty(stringTypeService)){
            txtErrTypeService.setText(getActivity().getString(R.string.label_type_service));
        }else {
            txtErrTypeService.setText("");
            bookService.setTypeService(stringTypeService);
            sumVilidate +=1;
        }
        if (yourAdditional.isEmpty()){
            txtErrYourAdditional.setText(getActivity().getString(R.string.label_your_additional_isempty));
        }else{ txtErrYourAdditional.setText(""); sumVilidate +=1;
        bookService.setYourAdditionalInfor(txtYourAdditional.getText().toString());}

        if (TextUtils.isEmpty(dateExpec)){
                txtErrDateExpec.setText(getActivity().getString(R.string.label_date_expec));
        }else {
            txtErrDateExpec.setText("");

            try {

                Date dateLast = dt1.parse(bookService.getDateSelected());
                Date dateNext = dt1.parse(dateExpec);

                if (dateNext.before(dateLast)){
                    txtErrDateExpec.setText(getActivity().getString(R.string.label_date_expec_incorrect));
                }else {
                    txtErrDateExpec.setText("");
                    sumVilidate +=1;
                    bookService.setExpectDate(txtExpecDay.getText().toString());
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (TextUtils.isEmpty(timeExpect)){
            txtErrTimeExpec.setText(getActivity().getString(R.string.label_time_expec));
        }else {
            txtErrTimeExpec.setText("");
            String last = bookService.getTimeSelected().replaceAll("\\D+","");
            String next =  timeExpect.replaceAll("\\D+","");


            try {

                Date dateLast = dt1.parse(bookService.getDateSelected());
                Date dateNext = dt1.parse(dateExpec);

                if (dateNext.equals(dateLast)){
                    if(Integer.parseInt(last)>Integer.parseInt(next)){
                        txtErrTimeExpec.setText(getActivity().getString(R.string.label_time_expec_incorrect));
                    }else if(Integer.parseInt(last)==Integer.parseInt(next)){
                        txtErrTimeExpec.setText(getActivity().getString(R.string.label_time_expec_incorrect));
                    }else {
                        txtErrTimeExpec.setText("");
                        sumVilidate +=1;
                        bookService.setExpectTime(acExpecTime.getText().toString());
                    }
                }else  if (dateNext.before(dateLast)){
                    txtErrTimeExpec.setText(getActivity().getString(R.string.label_time_expec_incorrect));
                }else {
                    txtErrTimeExpec.setText("");
                    sumVilidate +=1;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if(sumVilidate==4){
            Bundle bundle = new Bundle();
            bundle.putSerializable("bookingservice", bookService);
//                bundle.putSerializable("bookService", bookService);
            Fragment toFragment = new ReviewAndSubmitBookingFragment();
            toFragment.setArguments(bundle);
            getFragmentManager().beginTransaction().replace(R.id.frame_booking_service, toFragment).commit();
        }else {
        }
    }
}
