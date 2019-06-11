package com.tfg.hipersch;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TasksFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TasksFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TasksFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    @BindView(R.id.firstButton) MaterialButton _firstButton;
    @BindView(R.id.secondButton) MaterialButton _secondButton;
    @BindView(R.id.thirdButton) MaterialButton _thirdButton;
    @BindView(R.id.fourthButton) MaterialButton _fourthButton;

    public TasksFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TasksFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TasksFragment newInstance(String param1, String param2) {
        TasksFragment fragment = new TasksFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
        setCurrentModeButtons(getCurrentMode());


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tasks, container, false);
        ButterKnife.bind(this, view);
        // Inflate the layout for this fragment
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

    public void setCurrentModeButtons(String mode) {
        switch (mode) {
            case "cycling":
                //  TODO
                //_firstButton.setVisibility(View.VISIBLE);
                //_secondButton.setVisibility(View.VISIBLE);
                //_thirdButton.setVisibility(View.INVISIBLE);
                //_fourthButton.setVisibility(View.INVISIBLE);
                break;
            case "running":
                //_firstButton.setText("DISTANCE");
                //_firstButton.setVisibility(View.VISIBLE);
                //_secondButton.setText("VO2MAX");
                //_secondButton.setVisibility(View.VISIBLE);
                //_thirdButton.setVisibility(View.INVISIBLE);
                //_fourthButton.setVisibility(View.INVISIBLE);
                break;
            case "swimming":
                _firstButton.setText("VELOCITY LT");
                //_firstButton.setVisibility(View.VISIBLE);
                _secondButton.setText("VELOCITY ANAT");
                //_secondButton.setVisibility(View.VISIBLE);
                //_thirdButton.setVisibility(View.INVISIBLE);
                //_fourthButton.setVisibility(View.INVISIBLE);
                break;
        }
    }

    public String getCurrentMode() {
        SharedPreferences prefs = getActivity().getSharedPreferences(((MainActivity)getActivity())
                .getSharedPrefName(), Context.MODE_PRIVATE);

        return prefs.getString(((MainActivity)getActivity()).getCurrentModeKey(), "");
    }
}
