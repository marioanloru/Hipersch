package com.tfg.hipersch;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ManageTasksFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ManageTasksFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ManageTasksFragment extends Fragment {
    @BindView(R.id.progress_bar) ProgressBar _progressBar;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String limit;
    private String offset;

    private OnFragmentInteractionListener mListener;

    public ManageTasksFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ManageTasksFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ManageTasksFragment newInstance(String param1, String param2) {
        ManageTasksFragment fragment = new ManageTasksFragment();
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
            //mParam1 = getArguments().getString(ARG_PARAM1);
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);


        this.limit = "5";
        this.offset = "0";
        //TableLayout table = (TableLayout) getView().findViewById(R.id.table);

        getUserTestsData();
        /*TableRow row1 = new TableRow(getContext());
        row1.setLayoutParams(new TableLayout.LayoutParams( TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
        TextView textview = new TextView(getContext());
        textview.setText("pruebaaa");
        //textview.getTextColors(R.color.)
        //textview.setTextColor(Color.YELLOW);
        row1.addView(textview);
        table.addView(row1);*/


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_manage_tasks, container, false);
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

    private void getUserTestsData() {
        ApiService apiService = ServiceGenerator.createService(ApiService.class);
        String currentMode = getCurrentMode();
        System.out.println("Get user tests data, current mode --> " + currentMode);

        Call<List<ApiResponse>> call = null;
        switch (currentMode) {
            case "cycling":
                call = apiService.getCyclingTests("Bearer " + getToken(), this.limit, this.offset);
                break;
            case "running":
                call = apiService.getRunningTests("Bearer " + getToken(), this.limit, this.offset);
                break;
            case "swimming":
                call = apiService.getSwimmingTests("Bearer " + getToken(), this.limit, this.offset);
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
                            break;
                        case "running":
                            break;
                        case "swimming":
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

    private void updateLoading(Boolean loading) {
        System.out.println("Escondo progress bar: " + loading.toString());
        if (loading) {
            System.out.println("LO PONGO A VISIBLE");
            _progressBar.setVisibility(View.VISIBLE);
        } else {
            System.out.println("LO PONGO A INVISIBLE");
            _progressBar.setVisibility(View.INVISIBLE);
        }

        System.out.println(_progressBar.getVisibility());
    }
}
