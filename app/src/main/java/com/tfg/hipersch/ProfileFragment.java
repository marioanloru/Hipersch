package com.tfg.hipersch;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.anychart.APIlib;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    //@BindView(R.id.height) TextView _height;
    @BindView(R.id.bmi) TextInputLayout _bmi;
    @BindView(R.id.height) TextInputLayout _height;
    @BindView(R.id.weight) TextInputLayout _weight;
    @BindView(R.id.chart) RadarChart _chart;
    @BindView(R.id.height_field) TextInputEditText _heightField;
    @BindView(R.id.weight_field) TextInputEditText _weightField;
    @BindView(R.id.bmi_field) TextInputEditText _bmiField;
    @BindView(R.id.progress_bar) ProgressBar _progressBar;

    private int height;
    private int bodyWeight;
    private double bodyMassIndex;

    private RadarDataSet set1;
    private RadarDataSet set2;
    private RadarDataSet set3;

    private ArrayList<RadarEntry> entries2 = new ArrayList<>();


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RadarChart chart;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);
        getUserData(TokenManager.getToken(getActivity()));

        updateLoading(true);
        getUserTestsData();

        chart = view.findViewById(R.id.chart);
        chart.getDescription().setEnabled(false);


        chart.setWebLineWidth(1.2f);
        chart.setWebColor(Color.LTGRAY);
        chart.setWebLineWidthInner(1f);
        chart.setWebColorInner(Color.LTGRAY);
        chart.setWebAlpha(100);

        //setData();

        chart.animateXY(1400, 1400, Easing.EaseInOutQuad);

        ((MainActivity)getActivity())._currentModeGroup.addOnButtonCheckedListener(
                new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group,
                                        int checkedId, boolean isChecked) {
                System.out.println("Evento captado, hay que actualizar el grafo");
                updateLoading(true);
                getUserTestsData();
                updateLoading(false);

            }
        });

        _bmi.setEnabled(false);


        _heightField.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    updateHeight(v, getToken());
                    System.out.println("Se ha pulsado enter!!!");
                }
                return false;
            }
        });

        _weightField.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                System.out.println("Weight field key pressed");
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    updateWeight(v, getToken());
                    System.out.println("Se ha pulsado enter!!!");
                }
                return false;
            }
        });
        /*_heightField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    System.out.println("Tiene el foco");
                    //if (!emailVisited) emailVisited = true;
                } else {
                    System.out.println("TNOOOOOiene el foco");

                    //if (emailVisited) validateEmail();
                }
            }
        });*/
        //  MarkerView mv = new RadarMarkerView(getContext(), R.layout.);
        // Inflate the layout for this fragment
        return view;
    }

    private SpannableString generateCenterText() {
        SpannableString s = new SpannableString("Revenues\nQuarters 2015");
        s.setSpan(new RelativeSizeSpan(2f), 0, 8, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 8, s.length(), 0);
        return s;
    }

        // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void getUserData(String token) {
        ApiService apiService = ServiceGenerator.createService(ApiService.class);
        Call<ApiResponse> call = apiService.getUserData("Bearer " + token);

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    ApiResponse apiResponse = response.body();
                    addUserDataParameters(apiResponse);
                } else {
                    System.out.println("Something failed");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.d("Error:", t.getMessage());
                //onLoginFailed();
            }
        });
    }

    private void updateHeight(View v, String token) {
        System.out.println("Update height!!");

        ApiService apiService = ServiceGenerator.createService(ApiService.class);
        Call<ApiResponse> call = apiService.updateUserData("Bearer " + token,
                _heightField.getText().toString(), _weightField.getText().toString());

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    Toast toast = Toast.makeText(v.getContext(),
                            "Personal data succesfully updated", Toast.LENGTH_LONG);
                    toast.show();
                    TokenManager.updateToken(v.getContext());
                } else {
                    System.out.println("Something failed");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.d("Error:", t.getMessage());
                //onLoginFailed();
            }
        });
    }

    private void updateWeight(View v, String token) {
        System.out.println("Update weight!!");
        ApiService apiService = ServiceGenerator.createService(ApiService.class);
        Call<ApiResponse> call = apiService.updateUserData("Bearer " + token,
                _heightField.getText().toString(), _weightField.getText().toString());

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    System.out.println("Todo okk!!" + response.body());
                    Toast toast = Toast.makeText(v.getContext(),
                            "Personal data succesfully updated", Toast.LENGTH_LONG);
                    toast.show();
                    TokenManager.updateToken(v.getContext());
                } else {
                    System.out.println("Something failed");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.d("Error:", t.getMessage());
                //onLoginFailed();
            }
        });
    }
    /*private void updateWeight(View v, String token) {
        ApiService apiService = ServiceGenerator.createService(ApiService.class);
        Call<ApiResponse> call = apiService.updateUserData("Bearer " + token, "",
                _weightField.getText().toString());


        call.enqueue(new Callback<ApiResponse>()
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    TokenManager.updateToken(v.getContext());
                } else {
                    System.out.println("Something failed");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.d("Error:", t.getMessage());
                //onLoginFailed();
            }
        });
    }*/

    private void addUserDataParameters(ApiResponse response) {
        //_height.setText(Integer.toString(response.getHeight()));
        _heightField.setText(Integer.toString(response.getHeight()));
        _weightField.setText(Integer.toString(response.getBodyWeight()));
        _bmiField.setText(Double.toString(response.getBodyMassIndex()));
    }

    public String getCurrentMode() {
        String currentMode = "running";
        try {
            currentMode = ((MainActivity)getActivity()).getCurrentMode();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return currentMode;
    }

    public String getToken() {
        String token = "";
        try {
            token = ((MainActivity)getActivity()).getToken();
            System.out.println("Token que se envia!" + token);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return token;
    }

    private void getUserTestsData() {
        ApiService apiService = ServiceGenerator.createService(ApiService.class);
        String currentMode = getCurrentMode();
        System.out.println("Get user tests data, current mode --> " + currentMode);
        String limit = "3";
        String offset = "0";
        Call<List<ApiResponse>> call = null;
        switch (currentMode) {
            case "cycling":
                call = apiService.getCyclingTests("Bearer " + getToken(), limit, offset);
                break;
            case "running":
                call = apiService.getRunningTests("Bearer " + getToken(), limit, offset);
                break;
            case "swimming":
                call = apiService.getSwimmingTests("Bearer " + getToken(), limit, offset);
                break;
            default:
                break;
        }

        call.enqueue(new Callback<List<ApiResponse>>() {
            @Override
            public void onResponse(Call<List<ApiResponse>> call, Response<List<ApiResponse>> response) {
                if (response.isSuccessful()) {
                    List<ApiResponse> apiResponse = response.body();
                    System.out.println("-------Api response: " + apiResponse.toString());

                    updateLoading(false);

                    switch (getCurrentMode()) {
                        case "cycling":
                            loadGraphData(apiResponse);
                            break;
                        case "running":
                            loadGraphData(apiResponse);
                            break;
                        case "swimming":
                            loadGraphData(apiResponse);
                            break;
                        default:
                            break;
                    }
                } else {
                    System.out.println("-----Something failed");
                }
            }

            @Override
            public void onFailure(Call<List<ApiResponse>> call, Throwable t) {
                System.out.println("-------- Ha fallado!" + t.getMessage());
                Log.d("Error:", t.getMessage());
                //onLoginFailed();
            }
        });
    }

    private void loadGraphData(List<ApiResponse> apiResponse) {
        String currentMode = getCurrentMode();
        ArrayList<RadarEntry> entries = new ArrayList<>();

        System.out.println("El modo es: " + currentMode + entries.toString());
        System.out.println("La respuesta de la api: :" + apiResponse.toString());

        ArrayList<RadarEntry> entries1 = new ArrayList<>();
        ArrayList<RadarEntry> entries2= new ArrayList<>();
        ArrayList<RadarEntry> entries3 = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();

        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
        DateFormat outputFormat = new SimpleDateFormat("EEE MMM dd", Locale.ENGLISH);
        Date dateParsed1 = null;
        Date dateParsed2 = null;
        Date dateParsed3 = null;

        switch (currentMode) {
            case "running":
                labels.add("vo2max");
                labels.add("mavVo2max");
                labels.add("vat");

                entries1.add(new RadarEntry((float)apiResponse.get(0).getVo2max()));
                entries1.add(new RadarEntry((float)apiResponse.get(0).getMavVo2max()));
                entries1.add(new RadarEntry((float)apiResponse.get(0).getVat()));
                System.out.println("Entries 1 ->" + entries1.toString());

                entries2.add(new RadarEntry((float)apiResponse.get(1).getVo2max()));
                entries2.add(new RadarEntry((float)apiResponse.get(1).getMavVo2max()));
                entries2.add(new RadarEntry((float)apiResponse.get(1).getVat()));
                System.out.println("Entries 2 ->" + entries2.toString());

                entries3.add(new RadarEntry((float)apiResponse.get(2).getVo2max()));
                entries3.add(new RadarEntry((float)apiResponse.get(2).getMavVo2max()));
                entries3.add(new RadarEntry((float)apiResponse.get(2).getVat()));
                System.out.println("Entries 3 ->" + entries3.toString());
                break;
            case "swimming":
                labels.add("Index ANAT");
                labels.add("Index LT");
                labels.add("ANA threshold");
                labels.add("Lactate Threshold");

                entries1.add(new RadarEntry((float)apiResponse.get(0).getIndexANAT()));
                entries1.add(new RadarEntry((float)apiResponse.get(0).getIndexLT()));
                entries1.add(new RadarEntry((float)apiResponse.get(0).getAnaThreshold()));
                entries1.add(new RadarEntry((float)apiResponse.get(0).getLactateThreshold()));
                System.out.println("Entries 1 ->" + entries1.toString());

                entries2.add(new RadarEntry((float)apiResponse.get(1).getIndexANAT()));
                entries2.add(new RadarEntry((float)apiResponse.get(1).getIndexLT()));
                entries2.add(new RadarEntry((float)apiResponse.get(1).getAnaThreshold()));
                entries2.add(new RadarEntry((float)apiResponse.get(1).getLactateThreshold()));
                System.out.println("Entries 2 ->" + entries2.toString());

                entries3.add(new RadarEntry((float)apiResponse.get(2).getIndexANAT()));
                entries3.add(new RadarEntry((float)apiResponse.get(2).getIndexLT()));
                entries3.add(new RadarEntry((float)apiResponse.get(2).getAnaThreshold()));
                entries3.add(new RadarEntry((float)apiResponse.get(2).getLactateThreshold()));
                System.out.println("Entries 3 ->" + entries3.toString());
                break;
            case "cycling":
                labels.add("p6sec");
                labels.add("p1min");
                labels.add("p6min");
                labels.add("p20min");
                ArrayList samplesP6sec = new ArrayList<Float>();
                ArrayList samplesP1min = new ArrayList<Float>();
                ArrayList samplesP6min = new ArrayList<Float>();
                ArrayList samplesP20min = new ArrayList<Float>();

                for (int i = 0; i < apiResponse.size(); i += 1) {
                    switch (apiResponse.get(i).getType()) {
                        case "p6sec":
                            samplesP6sec.add(apiResponse.get(i).getP6sec());
                            break;
                        case "p1min":
                            samplesP1min.add(apiResponse.get(i).getP1min());
                            break;
                        case "p6min":
                            samplesP6min.add(apiResponse.get(i).getP6min());
                            break;
                        case "p20min":
                            samplesP20min.add(apiResponse.get(i).getP20min());
                            break;
                        default:
                            break;
                    }
                }

                System.out.println("Samples 1" + samplesP6sec.toString());
                System.out.println("Samples 2" + samplesP1min.toString());
                System.out.println("Samples 3" + samplesP6min.toString());
                System.out.println("Samples 4" + samplesP20min.toString());

                for (int i = 0; i < samplesP6sec.size(); i += 1) {
                    if (i == 0) entries1.add(new RadarEntry((float)samplesP6sec.get(i)));
                    if (i == 1) entries2.add(new RadarEntry((float)samplesP6sec.get(i)));
                    if (i == 2) entries3.add(new RadarEntry((float)samplesP6sec.get(i)));
                }

                for (int i = 0; i < samplesP1min.size(); i += 1) {
                    if (i == 0) entries1.add(new RadarEntry((float)samplesP1min.get(i)));
                    if (i == 1) entries2.add(new RadarEntry((float)samplesP1min.get(i)));
                    if (i == 2) entries3.add(new RadarEntry((float)samplesP1min.get(i)));
                }

                for (int i = 0; i < samplesP6min.size(); i += 1) {
                    if (i == 0) entries1.add(new RadarEntry((float)samplesP6min.get(i)));
                    if (i == 1) entries2.add(new RadarEntry((float)samplesP6min.get(i)));
                    if (i == 2) entries3.add(new RadarEntry((float)samplesP6min.get(i)));
                }

                for (int i = 0; i < samplesP20min.size(); i += 1) {
                    if (i == 0) {
                        entries1.add(new RadarEntry((float)samplesP20min.get(i)));
                    }
                    if (i == 1) entries2.add(new RadarEntry((float)samplesP20min.get(i)));
                    if (i == 2) entries3.add(new RadarEntry((float)samplesP20min.get(i)));
                }

                break;
            default:
                break;
        }

        try {
            dateParsed1 = inputFormat.parse(apiResponse.get(0).getDate());
            dateParsed2 = inputFormat.parse(apiResponse.get(1).getDate());
            dateParsed3 = inputFormat.parse(apiResponse.get(2).getDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        System.out.println("Entries 1 ---> " + entries1.toString());
        System.out.println("Entries 2 ---> " + entries2.toString());
        System.out.println("Entries 3 ---> " + entries3.toString());

        RadarDataSet set1 = new RadarDataSet(entries1, outputFormat.format(dateParsed1));
        RadarDataSet set2 = new RadarDataSet(entries2, outputFormat.format(dateParsed2));
        RadarDataSet set3 = new RadarDataSet(entries3, outputFormat.format(dateParsed3));

        set1.setColor(Color.rgb(103, 110, 129));
        set1.setFillColor(Color.rgb(103, 110, 129));
        set1.setDrawFilled(true);
        set1.setFillAlpha(180);
        set1.setLineWidth(1f);

        set2.setColor(Color.rgb(0, 96, 100));
        set2.setFillColor(Color.rgb(0, 96, 100));
        set2.setDrawFilled(true);
        set2.setFillAlpha(180);
        set2.setLineWidth(1f);

        set3.setColor(Color.rgb(245, 127, 23));
        set3.setFillColor(Color.rgb(245, 127, 23));
        set3.setDrawFilled(true);
        set3.setFillAlpha(180);
        set3.setLineWidth(1f);

        ArrayList<IRadarDataSet> sets = new ArrayList<>();
        sets.add(set1);
        sets.add(set2);
        sets.add(set3);

        RadarData data = new RadarData(sets);
        data.setValueTextSize(8f);
        data.setDrawValues(false);
        data.setValueTextColor(Color.WHITE);


        XAxis xAxis = chart.getXAxis();
        xAxis.setTextSize(9f);
        xAxis.setAxisMaximum(10f);
        xAxis.setAxisMinimum(0f);
        xAxis.setYOffset(0f);
        xAxis.setXOffset(0f);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));

        YAxis yAxis = chart.getYAxis();
        yAxis.setLabelCount(5);
        yAxis.setDrawLabels(true);
        yAxis.setTextSize(9f);
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisMaximum(10f);

        chart.setData(data);

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(true);
        //l.setTypeface(tfLight);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(16f);
        //l.setTextColor(Color.WHITE);


        chart.invalidate();
    }

    private void updateLoading(Boolean loading) {
        System.out.println("Cargando!! " + loading.toString());
        if (loading) {
            _progressBar.setVisibility(View.VISIBLE);
            _height.setVisibility(View.INVISIBLE);
            _weight.setVisibility(View.INVISIBLE);
            _bmi.setVisibility(View.INVISIBLE);
            _chart.setVisibility(View.INVISIBLE);
        } else {
            _progressBar.setVisibility(View.INVISIBLE);
            _height.setVisibility(View.VISIBLE);
            _weight.setVisibility(View.VISIBLE);
            _bmi.setVisibility(View.VISIBLE);
            _chart.setVisibility(View.VISIBLE);
        }
    }



}
