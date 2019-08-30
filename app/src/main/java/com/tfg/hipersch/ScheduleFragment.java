package com.tfg.hipersch;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.button.MaterialButtonToggleGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ScheduleFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ScheduleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScheduleFragment extends Fragment {
    @BindView(R.id.trainingZone) TextView _trainingZone;
    @BindView(R.id.trainingZoneText) TextView _trainingZoneText;
    @BindView(R.id.trainingZoneTag) TextView _trainingZoneTag;

    @BindView(R.id.trainingZone2) TextView _trainingZone2;
    @BindView(R.id.trainingZoneText2) TextView _trainingZoneText2;
    @BindView(R.id.description) TextView _description;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ScheduleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ScheduleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ScheduleFragment newInstance(String param1, String param2) {
        ScheduleFragment fragment = new ScheduleFragment();
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
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);
        ButterKnife.bind(this, view);
        ((MainActivity)getActivity())._currentModeGroup.addOnButtonCheckedListener(
                new MaterialButtonToggleGroup.OnButtonCheckedListener() {
                    @Override
                    public void onButtonChecked(MaterialButtonToggleGroup group,
                                                int checkedId, boolean isChecked) {
                        getTrainingZone();

                    }
                });

        getTrainingZone();
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

    private void getTrainingZone() {
        String currentMode = getCurrentMode();
        ApiService apiService = ServiceGenerator.createService(ApiService.class);
        Call<ApiResponse> call = null;
        switch (currentMode) {
            case "running":
                call = apiService.getRunningTrainingZone("Bearer " + getToken());
                break;
            case "swimming":
                call = apiService.getSwimmingTrainingZone("Bearer " + getToken());
                break;
            case "cycling":
                call = apiService.getCyclingTrainingZone("Bearer " + getToken());
                break;
        }


        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                try {
                    String description = "";
                    String trainingZone;
                    _trainingZoneText.setText("Training Zone:");
                    _trainingZone2.setVisibility(View.INVISIBLE);
                    _trainingZoneText2.setVisibility(View.INVISIBLE);
                    switch (currentMode) {
                        case "running":
                            trainingZone = response.body().getTrainingZone();
                            _trainingZone.setText(trainingZone);
                            _trainingZoneTag.setText("");
                            _description.setText("");

                            switch (trainingZone) {
                                case "0":
                                    break;
                                case "1":
                                    _trainingZoneTag.setText("Lactate Threshold");
                                    description = "This is the highest level of your Aerobic Threshold, that is why you lactate level in the blood will not increase. Working inside this zone teaches your body to burn fat as an energy source, and by stimulating it to produce more mitochondria. Work has to be done inside this zone to improve your basic physical condition. Sessions inside this zone can be six or seven hours, but it is not needed to spend all this time training to progress.";
                                    break;
                                case "2":
                                    break;
                                case "3":
                                    _trainingZoneTag.setText("Anaerobic Threshold");
                                    description = "This zone will work your Anaerobic Threshold. It starts below the Aerobic Threshold and it extends above it. This allows to increase your Anaerobic Threshold with longer intervals and exceed it in shorter periods. Training in this zone creates a big quantity of lactic acid, producing a bigger stimulus to increase the number of mitochondria at muscles. Training at this zone is done by short limited periods with recovery intervals between them.";
                                    break;

                                case "4":
                                    break;

                                case "5":
                                    _trainingZoneTag.setText("Maximum Adaptative Volume");
                                    break;

                                case "6":
                                    break;

                                case "7":
                                    break;
                            }
                            _description.setText(description);

                            break;
                        case "swimming":
                            _trainingZone2.setVisibility(View.VISIBLE);
                            _trainingZoneText2.setVisibility(View.VISIBLE);
                            _trainingZoneText.setText("Training Zone 200m:");
                            _trainingZone.setText(response.body().getTrainingZoneTwoHundred());
                            _trainingZoneText2.setText("Training Zone 400m:");
                            _trainingZone2.setText(response.body().getTrainingZoneFourHundred());

                            break;
                        case "cycling":
                            trainingZone = response.body().getTrainingZone();
                            _trainingZone.setText(trainingZone);
                            switch (trainingZone) {
                                case "0":
                                    _trainingZoneTag.setText("Active Recovery");
                                    description = "Active Recovery. Training with this intensity means that it is possible to maintain active without fatigue. After hard work, your body will try to heal as soon as possible. This will create an impression of slowness. Training inside this zone will keep your legs working without increasing your fatigue levels. It is perfect when you need to recover yourself after a hard session, avoiding the feeling of slowness the next day.";
                                    break;
                                case "1":
                                    _trainingZoneTag.setText("Aerobic Threshold");
                                    description = "This is the highest level of your Aerobic Threshold, that is why you lactate level in the blood will not increase. Working inside this zone teaches your body to burn fat as an energy source, and by stimulating it to produce more mitochondria. Work has to be done inside this zone to improve your basic physical condition. Sessions inside this zone can be six or seven hours, but it is not needed to spend all this time training to progress.";
                                    break;
                                case "2":
                                    _trainingZoneTag.setText("Tempo");

                                    description = "This zone works with your capacity to keep a constant tempo. This zone has a lot of the advantages of the Aerobic Threshold zone but is more exhausting. That is why the number of sessions has to be limited. The main reason to train in this zone is to stimulate your body to increase the quantity of glycogen that it can stock up. The amount of glycogen used starts to surpass fat as an energy source. The amount of time to keep at this zone is one to three hours.";
                                    break;
                                case "3":
                                    _trainingZoneTag.setText("Anaerobic Threshold");

                                    description = "This zone will work your Anaerobic Threshold. It starts below the Aerobic Threshold and it extends above it. This allows to increase your Anaerobic Threshold with longer intervals and exceed it in shorter periods. Training in this zone creates a big quantity of lactic acid, producing a bigger stimulus to increase the number of mitochondria at muscles. Training at this zone is done by short limited periods with recovery intervals between them.";
                                    break;

                                case "4":
                                    _trainingZoneTag.setText("VO2MAX. Max Power");
                                    description = "Centered on the intensity the athlete can keep from three to eight minutes. In shorter efforts, the heart may not have the chance to answer the stimulus and the maximum heartbeat can appear after the athlete reaches the summit of an incline. This is the limit where heartbeat is usable. Training at this zone works athlete's cardiac use, in other words, the quantity and speed which your heart is able to pump blood to muscles.";
                                    break;

                                case "5":
                                    _trainingZoneTag.setText("Anaerobic");
                                    description = "This zone can be maintained with a maximum time of three minutes. It consists of doing the maximum effort, going as hard has it is possible. As mentioned in VO2MAX zone, the heartbeat is not a good measurement here, as the heart will not have time to answer the stimulus.";
                                    break;

                                case "6":
                                    description = "";
                                    break;

                                case "7":
                                    _trainingZoneTag.setText("Neuromuscular Power");
                                    description = "Neuromuscular power. Training in this zone works to increase sprint power. This zone generates hypertrophy with the consequent increase of muscles.";
                                    break;
                            }
                            _description.setText(description);

                            break;
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.d("Error:", t.getMessage());
            }
        });
    }
}
