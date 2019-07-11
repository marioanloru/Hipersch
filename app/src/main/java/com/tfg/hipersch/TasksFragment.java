package com.tfg.hipersch;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


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

    private String currentMode;
    private String cyclingTestSelected;

    private OnFragmentInteractionListener mListener;

    @BindView(R.id.firstButton) TextInputEditText _firstButton;
    @BindView(R.id.secondButton) TextInputEditText _secondButton;
    @BindView(R.id.sendButton) MaterialButton _sendTest;
    @BindView(R.id.cycling_spinner) Spinner _cyclingTestTypes;
    @BindView(R.id.cycling_spinner_message) TextView _cyclingSpinnerMessage;

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


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tasks, container, false);
        ButterKnife.bind(this, view);
        this.currentMode = getCurrentMode();
        setCurrentModeButtons(currentMode);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.cycling_test_types, android.R.layout.simple_spinner_dropdown_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        _cyclingTestTypes.setAdapter(adapter);

        ((MainActivity)getActivity())._currentModeGroup.addOnButtonCheckedListener(
                new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group,
                                        int checkedId, boolean isChecked) {
                clearButtons();
                System.out.println("Evento captado, el id es este " + checkedId);
                setCurrentModeButtons(getCurrentMode());

            }
        });

        _firstButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Boton clickado: " + _firstButton.getText());
            }
        });

        _secondButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Segundo boton");
            }
        });

        _sendTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("Envio la tarea: " + getCurrentMode());

                switch (getCurrentMode()) {
                    case "running":
                        sendRunningTest(_firstButton.getText().toString());
                        break;
                    case "swimming":
                        sendSwimmingTest(_firstButton.getText().toString(),
                                _secondButton.getText().toString());
                        break;
                    case "cycling":
                        sendCyclingTest(_firstButton.getText().toString());
                        break;
                    default:
                        break;
                }

            }
        });

        _cyclingTestTypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                cyclingTestSelected = _cyclingTestTypes.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
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
        System.out.println("Modo en set current: " + mode);
        this.currentMode = mode;
        switch (mode) {
            case "cycling":
                _firstButton.setVisibility(View.VISIBLE);
                _secondButton.setVisibility(View.INVISIBLE);
                _cyclingSpinnerMessage.setVisibility(View.VISIBLE);
                _cyclingTestTypes.setVisibility(View.VISIBLE);

                _firstButton.setHint("Peak power");
                break;
            case "running":
                _firstButton.setVisibility(View.VISIBLE);
                _secondButton.setVisibility(View.INVISIBLE);
                _cyclingSpinnerMessage.setVisibility(View.INVISIBLE);
                _cyclingTestTypes.setVisibility(View.INVISIBLE);

                _firstButton.setHint("Distance");
                break;
            case "swimming":
                _firstButton.setVisibility(View.VISIBLE);
                _secondButton.setVisibility(View.VISIBLE);
                _cyclingSpinnerMessage.setVisibility(View.INVISIBLE);
                _cyclingTestTypes.setVisibility(View.INVISIBLE);

                _firstButton.setHint("200 meters mark");
                _secondButton.setHint("400 meters mark");
                break;
        }
    }

    public String getCurrentMode() {
        return ((MainActivity)getActivity()).getCurrentMode();
    }

    public boolean sendRunningTest(String distance) {
        ApiService apiService = ServiceGenerator.createService(ApiService.class);
        Call<ApiResponse> call = apiService.sendRunningTest("Bearer " +
                TokenManager.getToken(getActivity()), _firstButton.getText().toString());

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    ApiResponse apiResponse = response.body();
                    showSuccesfulMessage();
                } else {
                    System.out.println("Something failed");
                    showErrorMessage("Something went wrong");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.d("Error:", t.getMessage());
                //onLoginFailed();
            }
        });
        return true;
    }

    public boolean sendSwimmingTest(String timeTwoHundred, String timeFourHundred) {
        System.out.println("Envio tarea con estos paramettros 400 200 " + timeFourHundred + timeTwoHundred);
        ApiService apiService = ServiceGenerator.createService(ApiService.class);
        Call<ApiResponse> call = apiService.sendSwimmingTest("Bearer " +
                TokenManager.getToken(getActivity()),
                timeFourHundred, timeTwoHundred);

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    ApiResponse apiResponse = response.body();
                    showSuccesfulMessage();
                    //System.out.println(apiResponse.getMessage())
                } else {
                    System.out.println("Something failed");
                    showErrorMessage("Something went wrong");

                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.d("Error:", t.getMessage());
                //onLoginFailed();
            }
        });
        return true;
    }

    public boolean sendCyclingTest(String peakPower) {
        ApiService apiService = ServiceGenerator.createService(ApiService.class);
        Call<ApiResponse> call = null;
        switch (cyclingTestSelected) {
            case "Six seconds":
                call = apiService.sendCyclingSixSecTest("Bearer " +
                        TokenManager.getToken(getActivity()), _firstButton.getText().toString());
                break;
            case "One minute":
                call = apiService.sendCyclingOneMinTest("Bearer " +
                        TokenManager.getToken(getActivity()), _firstButton.getText().toString());
                break;
            case "Six minutes":
                call = apiService.sendCyclingSixMinTest("Bearer " +
                        TokenManager.getToken(getActivity()), _firstButton.getText().toString());
                break;
            case "Twenty minutes":
                call = apiService.sendCyclingTwentyMinTest("Bearer " +
                        TokenManager.getToken(getActivity()), _firstButton.getText().toString());
                break;
            default:
                break;
        }

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    ApiResponse apiResponse = response.body();
                    showSuccesfulMessage();
                } else {
                    System.out.println("Something failed");
                    showErrorMessage("Something went wrong");

                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.d("Error:", t.getMessage());
                //onLoginFailed();
            }
        });
        return true;
    }

    public void showSuccesfulMessage() {
        Context context = getContext();

        Toast toast = Toast.makeText(context, "Task succesfully sent", Toast.LENGTH_LONG);
        toast.show();
        /*View contextView = findView findViewById(R.id.fragmentContainer);

        Snackbar.make(contextView, "idontknow", Snackbar.LENGTH_SHORT)
                .show();*/
    }

    public void showErrorMessage(String message) {
        Context context = getContext();

        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.show();
    }

    public void clearButtons() {
        _firstButton.setText("");
        _secondButton.setText("");
    }
}
