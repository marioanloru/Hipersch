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
                        System.out.println("Evento captado, hay que actualizar el grafo");
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
            System.out.println("Token que se envia!" + token);
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
                System.out.println("!!!!!!!!!!!!!");
                switch (currentMode) {
                    case "running":
                        _trainingZone.setText(response.body().getTrainingZone());
                        _description.setText("Lorem ipsum blablalbasdasdf asdfas f asdfasdf asd fas df asdf asdf as dfasdfabalblabal");
                        break;
                    case "swimming":
                        _trainingZone.setText(response.body().getTrainingZoneTwoHundred());
                        System.out.println(response.body().getTrainingZoneFourHundred());
                        _description.setText("Lorem ipsum blablalbasdasdf asdfas f asdfasdf asd fas df asdf asdf as dfasdfabalblabal");

                        break;
                    case "cycling":
                        _trainingZone.setText(response.body().getTrainingZone());
                        System.out.println("TAG!!!! " + response.body().getTrainingZoneTag());
                        _description.setText("Lorem ipsum blablalbasdasdf asdfas f asdfasdf asd fas df asdf asdf as dfasdfabalblabal");

                        break;
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.d("Error:", t.getMessage());
            }
        });
    }
}
