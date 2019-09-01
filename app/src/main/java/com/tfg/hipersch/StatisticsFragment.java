package com.tfg.hipersch;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StatisticsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StatisticsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatisticsFragment extends Fragment {
    @BindView(R.id.change) MaterialButton _change;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private boolean change;

    private CombinedChart chart;

    private OnFragmentInteractionListener mListener;

    public StatisticsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StatisticsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StatisticsFragment newInstance(String param1, String param2) {
        StatisticsFragment fragment = new StatisticsFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);
        ButterKnife.bind(this, view);

        //ConstraintLayout parent = view.findViewById(R.id.parent);
        updateGraphData(view);
        this.change = true;

        ((MainActivity)getActivity())._currentModeGroup.addOnButtonCheckedListener(
                new MaterialButtonToggleGroup.OnButtonCheckedListener() {
                    @Override
                    public void onButtonChecked(MaterialButtonToggleGroup group,
                                                int checkedId, boolean isChecked) {
                        updateGraphData(view);
                    }
                });

        _change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (change) {
                    change = false;
                } else {
                    change = true;
                }
                updateGraphData(view);
            }
        });
        return view;
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

    public void updateGraphData(View view) {

        chart = view.findViewById(R.id.combinedChart);
        chart.getDescription().setEnabled(false);
        chart.setBackgroundColor(Color.WHITE);

        chart.setDrawGridBackground(false);
        chart.setDrawBarShadow(false);
        chart.setHighlightFullBarEnabled(false);

        // draw bars behind lines
        chart.setDrawOrder(new CombinedChart.DrawOrder[]{
                CombinedChart.DrawOrder.BAR, CombinedChart.DrawOrder.BUBBLE,
                CombinedChart.DrawOrder.CANDLE, CombinedChart.DrawOrder.LINE,
                CombinedChart.DrawOrder.SCATTER
        });

        CombinedData data = new CombinedData();

        chart.animateXY(1400, 1400, Easing.EaseInOutQuad);
        String currentMode = getCurrentMode();
        switch (currentMode) {
            case "running":
                _change.setVisibility(View.INVISIBLE);
                getRunningProgress(data);
                break;
            case "swimming":
                _change.setVisibility(View.VISIBLE);
                getSwimmingProgress(data);
                break;
            case "cycling":
                _change.setVisibility(View.INVISIBLE);
                getCyclingProgress(data);
                break;
        }
        /*chart.setData(data);
        chart.invalidate();*/
    }

    public void getRunningProgress(CombinedData combinedData) {
        ApiService apiService = ServiceGenerator.createService(ApiService.class);
        Call<List<ApiResponse>> call = apiService.getRunningProgress("Bearer " + getToken());

        call.enqueue(new Callback<List<ApiResponse>>() {
            @Override
            public void onResponse(Call<List<ApiResponse>> call, Response<List<ApiResponse>> response) {
                try {
                    ArrayList<Entry> lineEntries = new ArrayList<>();

                    List<ApiResponse> data = response.body();
                    for (int i = 0; i < data.size(); i += 1) {
                        String trainingZone = data.get(i).getTrainingZone();
                        if (trainingZone.equals("vam")) trainingZone = "5";
                        if (trainingZone.equals("velocity")) trainingZone = "8";
                        lineEntries.add(new Entry(i, Float.parseFloat(trainingZone)));
                    }

                    LineDataSet set = new LineDataSet(lineEntries, "Training zones");
                    set.setColor(Color.rgb(245, 127, 23));
                    set.setLineWidth(4.5f);
                    set.setCircleColor(Color.rgb(245, 127, 23));
                    set.setCircleRadius(5f);
                    set.setFillColor(Color.rgb(245, 127, 23));
                    set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                    set.setDrawValues(true);
                    set.setValueTextSize(10f);
                    set.setValueTextColor(Color.rgb(245, 127, 23));

                    LineData lineData = new LineData();

                    lineData.addDataSet(set);

                    combinedData.setData(lineData);

                    chart.setData(combinedData);
                    chart.invalidate();
                } catch (Exception e) {
                }

            }

            @Override
            public void onFailure(Call<List<ApiResponse>> call, Throwable t) {
                Log.d("Error:", t.getMessage());
            }
        });
    }

    public void getSwimmingProgress(CombinedData combinedData) {
        ApiService apiService = ServiceGenerator.createService(ApiService.class);
        Call<List<ApiResponse>> call = apiService.getSwimmingProgress("Bearer " + getToken());

        call.enqueue(new Callback<List<ApiResponse>>() {
            @Override
            public void onResponse(Call<List<ApiResponse>> call, Response<List<ApiResponse>> response) {
                try {
                    ArrayList<Entry> lineEntries = new ArrayList<>();
                    ArrayList<Entry> lineEntries2 = new ArrayList<>();

                    List<ApiResponse> data = response.body();
                    for (int i = 0; i < data.size(); i += 1) {
                        String trainingZoneTwoHundred = data.get(i).getTrainingZoneTwoHundred();
                        String trainingZoneFourHundred = data.get(i).getTrainingZoneFourHundred();

                        if (trainingZoneTwoHundred.equals("aei")) trainingZoneTwoHundred = "1";
                        if (trainingZoneTwoHundred.equals("aem")) trainingZoneTwoHundred = "2";
                        if (trainingZoneTwoHundred.equals("ael")) trainingZoneTwoHundred = "3";

                        if (trainingZoneFourHundred.equals("aei")) trainingZoneFourHundred = "1";
                        if (trainingZoneFourHundred.equals("aem")) trainingZoneFourHundred = "2";
                        if (trainingZoneFourHundred.equals("ael")) trainingZoneFourHundred = "3";

                        lineEntries.add(new Entry(i, Float.parseFloat(trainingZoneTwoHundred)));
                        lineEntries2.add(new Entry(i, Float.parseFloat(trainingZoneFourHundred)));
                    }

                    LineDataSet set = new LineDataSet(lineEntries, "Training zone 200m");
                    set.setColor(Color.rgb(245, 127, 23));
                    set.setLineWidth(4.5f);
                    set.setCircleColor(Color.rgb(245, 127, 23));
                    set.setCircleRadius(5f);
                    set.setFillColor(Color.rgb(245, 127, 23));
                    set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                    set.setDrawValues(true);
                    set.setValueTextSize(10f);
                    set.setValueTextColor(Color.rgb(245, 127, 23));

                    LineData lineData = new LineData();

                    lineData.addDataSet(set);

                    LineDataSet set2 = new LineDataSet(lineEntries2, "Training zone 400m");
                    set2.setColor(Color.rgb(0, 96, 100));
                    set2.setLineWidth(4.5f);
                    set2.setCircleColor(Color.rgb(0, 96, 100));
                    set2.setCircleRadius(5f);
                    set2.setFillColor(Color.rgb(0, 96, 100));
                    set2.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                    set2.setDrawValues(true);
                    set2.setValueTextSize(10f);
                    set2.setValueTextColor(Color.rgb(0, 96, 100));

                    LineData lineData2 = new LineData();

                    lineData2.addDataSet(set2);

                    if (change) {
                        _change.setText("See 400m training zone");
                        _change.setBackgroundColor(Color.rgb(0, 96, 100));
                        combinedData.setData(lineData);
                    } else {
                        _change.setText("See 200m training zone");
                        _change.setBackgroundColor(Color.rgb(245, 127, 23));
                        combinedData.setData(lineData2);
                    }

                    chart.setData(combinedData);
                    chart.invalidate();
                } catch (Exception e) {

                }

            }

            @Override
            public void onFailure(Call<List<ApiResponse>> call, Throwable t) {
                Log.d("Error:", t.getMessage());
            }
        });
    }

    public void getCyclingProgress(CombinedData combinedData) {
        ApiService apiService = ServiceGenerator.createService(ApiService.class);
        Call<List<ApiResponse>> call = apiService.getCyclingProgress("Bearer " + getToken());

        call.enqueue(new Callback<List<ApiResponse>>() {
            @Override
            public void onResponse(Call<List<ApiResponse>> call, Response<List<ApiResponse>> response) {
                try {
                    ArrayList<Entry> lineEntries = new ArrayList<>();

                    List<ApiResponse> data = response.body();
                    for (int i = 0; i < data.size(); i += 1) {
                        String trainingZone = data.get(i).getTrainingZone();
                        if (trainingZone.equals("vam")) trainingZone = "5";
                        if (trainingZone.equals("velocity")) trainingZone = "8";
                        lineEntries.add(new Entry(i, Float.parseFloat(trainingZone)));
                    }

                    LineDataSet set = new LineDataSet(lineEntries, "Training zones");
                    set.setColor(Color.rgb(245, 127, 23));
                    set.setLineWidth(4.5f);
                    set.setCircleColor(Color.rgb(245, 127, 23));
                    set.setCircleRadius(5f);
                    set.setFillColor(Color.rgb(245, 127, 23));
                    set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                    set.setDrawValues(true);
                    set.setValueTextSize(10f);
                    set.setValueTextColor(Color.rgb(245, 127, 23));

                    LineData lineData = new LineData();

                    lineData.addDataSet(set);

                    combinedData.setData(lineData);

                    chart.setData(combinedData);
                    chart.invalidate();
                } catch (Exception e) {

                }

            }

            @Override
            public void onFailure(Call<List<ApiResponse>> call, Throwable t) {
                Log.d("Error:", t.getMessage());
            }
        });
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
        } catch(Exception e) {
            e.printStackTrace();
        }
        return token;
    }
}
