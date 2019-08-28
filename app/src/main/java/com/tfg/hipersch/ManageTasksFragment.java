package com.tfg.hipersch;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
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
    //@BindView(R.id.progress_bar) ProgressBar _progressBar;
    @BindView(R.id.previousButton) MaterialButton _previousButton;
    @BindView(R.id.nextButton) MaterialButton _nextButton;
    @BindView(R.id.table) TableLayout _table;

    @BindView(R.id.field1_1) TextView _field11;
    @BindView(R.id.field1_2) TextView _field12;
    @BindView(R.id.field1_3) TextView _field13;
    @BindView(R.id.field1_4) TextView _field14;

    @BindView(R.id.field2_1) TextView _field21;
    @BindView(R.id.field2_2) TextView _field22;
    @BindView(R.id.field2_3) TextView _field23;
    @BindView(R.id.field2_4) TextView _field24;

    @BindView(R.id.field3_1) TextView _field31;
    @BindView(R.id.field3_2) TextView _field32;
    @BindView(R.id.field3_3) TextView _field33;
    @BindView(R.id.field3_4) TextView _field34;

    @BindView(R.id.field4_1) TextView _field41;
    @BindView(R.id.field4_2) TextView _field42;
    @BindView(R.id.field4_3) TextView _field43;
    @BindView(R.id.field4_4) TextView _field44;

    @BindView(R.id.field5_1) TextView _field51;
    @BindView(R.id.field5_2) TextView _field52;
    @BindView(R.id.field5_3) TextView _field53;
    @BindView(R.id.field5_4) TextView _field54;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private int limit;
    private int offset;

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

        System.out.println("On create view manage tasks");
        View view = inflater.inflate(R.layout.fragment_manage_tasks, container, false);
        ButterKnife.bind(this, view);


        this.limit = 5;
        this.offset = 0;

        getUserTestsData();
        _previousButton.setEnabled(false);

        _nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Boton clickado: " + _firstButton.getText());
                _previousButton.setEnabled(true);
                this.offset = this.offset + this.limit;
                getUserTestsData();
            }
        });

        _previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Pagino hacia atras!");
                if (this.offset - this.limit > 0) {
                    this.offset = this.offset - this.limit;
                    getUserTestsData();
                }
            }
        });

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
                call = apiService.getCyclingTests("Bearer " + getToken(), this.limit.toString, this.offset.toString);
                
                break;
            case "running":
                call = apiService.getRunningTests("Bearer " + getToken(), this.limit.toString, this.offset.toString);
                break;
            case "swimming":
                call = apiService.getSwimmingTests("Bearer " + getToken(), this.limit.toString, this.offset.toString);
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
                    String field11, field12, field13, field14, field21, field22, field23, field24, field31, field32, field33, field34, field41, field42, field43, field44, field51, field52, field53, field54;  
                    //  Inicializar campos aqui
                    switch (getCurrentMode()) {
                        case "cycling":
                            break;
                        case "running":
                            entries1.add(new RadarEntry((float)apiResponse.get(0).getVo2max()));
                            entries1.add(new RadarEntry((float)apiResponse.get(0).getMavVo2max()));
                            entries1.add(new RadarEntry((float)apiResponse.get(0).getVat()));

                            field11.setText(apiResponse.get(0).getVat());
                            field12 = apiResponse.get(0).getVat();
                            field13 = apiResponse.get(0).getVat();
                            //field14 = apiResponse.get(0).getVat();

                            field21 = apiResponse.get(1).getVat();
                            field22 = apiResponse.get(1).getVat();
                            field23 = apiResponse.get(1).getVat();
                            //field24 = setText(apiResponse.get(1).etVat());

                            field31 = apiResponse.get(2).getVat();
                            field32 = apiResponse.get(2).getVat();
                            field33 = apiResponse.get(2).getVat();
                            //field34 = apiResponse.get(2).getVat();

                            field41 = apiResponse.get(3).getVat();
                            field42 = apiResponse.get(3).getVat();
                            field43 = apiResponse.get(3).getVat();
                            //field44 = apiResponse.get(3).getVat();

                            field51 = apiResponse.get(4).getVat();
                            field52 = apiResponse.get(4).getVat();
                            field53 = apiResponse.get(4).getVat();
                            //field54 = apiResponse.get(4).getVat();
                        break;
                        case "swimming":
                            break;
                        default:
                            break;

                        _field11.setText(apiResponse.get(0).getVat());
                        _field12.setText(apiResponse.get(0).getVat());
                        _field13.setText(apiResponse.get(0).getVat());
                        //if exists _field14.setText(apiResponse.get(0).getVat());
                        
                        _field21.setText(apiResponse.get(1).getVat());
                        _field22.setText(apiResponse.get(1).getVat());
                        _field23.setText(apiResponse.get(1).getVat());
                        // if exists_field24.setText(apiResponse.get(1).getVat());

                        _field31.setText(apiResponse.get(2).getVat());
                        _field32.setText(apiResponse.get(2).getVat());
                        _field33.setText(apiResponse.get(2).getVat());
                        // if exists _field34.setText(apiResponse.get(2).getVat());

                        _field41.setText(apiResponse.get(3).getVat());
                        _field42.setText(apiResponse.get(3).getVat());
                        _field43.setText(apiResponse.get(3).getVat());
                        // if exists _field44.setText(apiResponse.get(3).getVat());

                        _field51.setText(apiResponse.get(4).getVat());
                        _field52.setText(apiResponse.get(4).getVat());
                        _field53.setText(apiResponse.get(4).getVat());
                        // if exists _field54.setText(apiResponse.get(4).getVat());
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
        /*if (loading) {
            System.out.println("LO PONGO A VISIBLE");
            getView().findViewById(R.id.progress_bar).setVisibility(View.VISIBLE);
            getView().findViewById(R.id.table).setVisibility(View.INVISIBLE);
            getView().findViewById(R.id.previousButton).setVisibility(View.INVISIBLE);
            getView().findViewById(R.id.nextButton).setVisibility(View.INVISIBLE);
        } else {
            getView().findViewById(R.id.progress_bar).setVisibility(View.INVISIBLE);
            getView().findViewById(R.id.table).setVisibility(View.VISIBLE);
            getView().findViewById(R.id.previousButton).setVisibility(View.VISIBLE);
            getView().findViewById(R.id.nextButton).setVisibility(View.VISIBLE);
        }

        System.out.println(_progressBar.getVisibility());*/
    }
}
