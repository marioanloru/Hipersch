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
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
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
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private int limit;
    private int offset;

    private ApiResponse response1;
    private ApiResponse response2;
    private ApiResponse response3;
    private ApiResponse response4;
    private ApiResponse response5;

    private String discipline1;
    private String discipline2;
    private String discipline3;
    private String discipline4;
    private String discipline5;

    private OnFragmentInteractionListener mListener;

    //@BindView(R.id.progress_bar) ProgressBar _progressBar;
    @BindView(R.id.previousButton) MaterialButton _previousButton;
    @BindView(R.id.nextButton) MaterialButton _nextButton;
    @BindView(R.id.table) TableLayout _table;

    @BindView(R.id.header1) TextView _header1;
    @BindView(R.id.header2) TextView _header2;
    @BindView(R.id.header3) TextView _header3;
    @BindView(R.id.header4) TextView _header4;

    @BindView(R.id.deleteButton1) MaterialButton _deleteButton1;
    @BindView(R.id.deleteButton2) MaterialButton _deleteButton2;
    @BindView(R.id.deleteButton3) MaterialButton _deleteButton3;
    @BindView(R.id.deleteButton4) MaterialButton _deleteButton4;
    @BindView(R.id.deleteButton5) MaterialButton _deleteButton5;

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

        View view = inflater.inflate(R.layout.fragment_manage_tasks, container, false);
        ButterKnife.bind(this, view);
        this.limit = 5;
        this.offset = 0;

        View inflatedView = inflater.inflate(R.layout.fragment_manage_tasks, container, false);

        MaterialButton buttoon = (MaterialButton) inflatedView.findViewById(R.id.nextButton);
        buttoon.setText("asdasd");
        getUserTestsData();
        _previousButton.setEnabled(false);


        ((MainActivity)getActivity())._currentModeGroup.addOnButtonCheckedListener(
                new MaterialButtonToggleGroup.OnButtonCheckedListener() {
                    @Override
                    public void onButtonChecked(MaterialButtonToggleGroup group,
                                                int checkedId, boolean isChecked) {
                        _previousButton.setEnabled(false);
                        getUserTestsData();

                    }
                });

        _nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _previousButton.setEnabled(true);
                increaseOffset();
                getUserTestsData();
                if (_field21.getVisibility() == View.INVISIBLE) {
                    _nextButton.setEnabled(false);
                }
            }
        });

        _previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _nextButton.setEnabled(true);
                decreaseOffset();
                getUserTestsData();
                _deleteButton1.setVisibility(View.VISIBLE);
                _deleteButton2.setVisibility(View.VISIBLE);
                _deleteButton3.setVisibility(View.VISIBLE);
                _deleteButton4.setVisibility(View.VISIBLE);
                _deleteButton5.setVisibility(View.VISIBLE);
            }
        });

        _deleteButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (discipline1) {
                    case "running":
                        ApiService apiService = ServiceGenerator.createService(ApiService.class);
                        Call<ApiResponse> call = apiService.deleteRunningTest("Bearer " + getToken(), response1.getTestId());

                        call.enqueue(new Callback<ApiResponse>() {
                            @Override
                            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                                showDeleteSuccess();
                            }

                            @Override
                            public void onFailure(Call<ApiResponse> call, Throwable t) {
                                Log.d("Error:", t.getMessage());
                                showDeleteError();
                            }
                        });
                        break;
                    case "cycling":
                        apiService = ServiceGenerator.createService(ApiService.class);
                        call = apiService.deleteCyclingTest("Bearer " + getToken(), response1.getTestId());

                        call.enqueue(new Callback<ApiResponse>() {
                            @Override
                            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                                showDeleteSuccess();
                            }

                            @Override
                            public void onFailure(Call<ApiResponse> call, Throwable t) {
                                Log.d("Error:", t.getMessage());
                                showDeleteError();
                            }
                        });
                        break;
                    case "swimming":
                        apiService = ServiceGenerator.createService(ApiService.class);
                        call = apiService.deleteSwimmingTest("Bearer " + getToken(), response1.getTestId());

                        call.enqueue(new Callback<ApiResponse>() {
                            @Override
                            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                                showDeleteSuccess();
                            }

                            @Override
                            public void onFailure(Call<ApiResponse> call, Throwable t) {
                                Log.d("Error:", t.getMessage());
                                showDeleteError();
                            }
                        });
                        break;
                }

            }
        });

        _deleteButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (discipline1) {
                    case "running":
                        ApiService apiService = ServiceGenerator.createService(ApiService.class);
                        Call<ApiResponse> call = apiService.deleteRunningTest("Bearer " + getToken(), response2.getTestId());

                        call.enqueue(new Callback<ApiResponse>() {
                            @Override
                            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                                showDeleteSuccess();
                            }

                            @Override
                            public void onFailure(Call<ApiResponse> call, Throwable t) {
                                Log.d("Error:", t.getMessage());
                                showDeleteError();
                            }
                        });
                        break;
                    case "cycling":
                        apiService = ServiceGenerator.createService(ApiService.class);
                        call = apiService.deleteRunningTest("Bearer " + getToken(), response2.getTestId());

                        call.enqueue(new Callback<ApiResponse>() {
                            @Override
                            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                                showDeleteSuccess();
                            }

                            @Override
                            public void onFailure(Call<ApiResponse> call, Throwable t) {
                                Log.d("Error:", t.getMessage());
                                showDeleteError();
                            }
                        });
                        break;
                    case "swimming":
                        apiService = ServiceGenerator.createService(ApiService.class);
                        call = apiService.deleteSwimmingTest("Bearer " + getToken(), response2.getTestId());

                        call.enqueue(new Callback<ApiResponse>() {
                            @Override
                            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                                showDeleteSuccess();
                            }

                            @Override
                            public void onFailure(Call<ApiResponse> call, Throwable t) {
                                Log.d("Error:", t.getMessage());
                                showDeleteError();
                            }
                        });
                        break;
                }

                getUserTestsData();
            }
        });

        _deleteButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (discipline1) {
                    case "running":
                        ApiService apiService = ServiceGenerator.createService(ApiService.class);
                        Call<ApiResponse> call = apiService.deleteRunningTest("Bearer " + getToken(), response3.getTestId());

                        call.enqueue(new Callback<ApiResponse>() {
                            @Override
                            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                                showDeleteSuccess();
                            }

                            @Override
                            public void onFailure(Call<ApiResponse> call, Throwable t) {
                                Log.d("Error:", t.getMessage());
                                showDeleteError();
                            }
                        });
                        break;
                    case "cycling":
                        apiService = ServiceGenerator.createService(ApiService.class);
                        call = apiService.deleteRunningTest("Bearer " + getToken(), response3.getTestId());

                        call.enqueue(new Callback<ApiResponse>() {
                            @Override
                            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                                showDeleteSuccess();
                            }

                            @Override
                            public void onFailure(Call<ApiResponse> call, Throwable t) {
                                Log.d("Error:", t.getMessage());
                                showDeleteError();
                            }
                        });
                        break;
                    case "swimming":
                        apiService = ServiceGenerator.createService(ApiService.class);
                        call = apiService.deleteSwimmingTest("Bearer " + getToken(), response3.getTestId());

                        call.enqueue(new Callback<ApiResponse>() {
                            @Override
                            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                                showDeleteSuccess();
                            }

                            @Override
                            public void onFailure(Call<ApiResponse> call, Throwable t) {
                                Log.d("Error:", t.getMessage());
                                showDeleteError();
                            }
                        });
                        break;
                }

                getUserTestsData();
            }
        });

        _deleteButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (discipline1) {
                    case "running":
                        ApiService apiService = ServiceGenerator.createService(ApiService.class);
                        Call<ApiResponse> call = apiService.deleteRunningTest("Bearer " + getToken(), response4.getTestId());

                        call.enqueue(new Callback<ApiResponse>() {
                            @Override
                            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                                showDeleteSuccess();
                            }

                            @Override
                            public void onFailure(Call<ApiResponse> call, Throwable t) {
                                Log.d("Error:", t.getMessage());
                                showDeleteError();
                            }
                        });
                        break;
                    case "cycling":
                        apiService = ServiceGenerator.createService(ApiService.class);
                        call = apiService.deleteRunningTest("Bearer " + getToken(), response4.getTestId());

                        call.enqueue(new Callback<ApiResponse>() {
                            @Override
                            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                                showDeleteSuccess();
                            }

                            @Override
                            public void onFailure(Call<ApiResponse> call, Throwable t) {
                                Log.d("Error:", t.getMessage());
                                showDeleteError();
                            }
                        });
                        break;
                    case "swimming":
                        apiService = ServiceGenerator.createService(ApiService.class);
                        call = apiService.deleteSwimmingTest("Bearer " + getToken(), response4.getTestId());

                        call.enqueue(new Callback<ApiResponse>() {
                            @Override
                            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                                showDeleteSuccess();
                            }

                            @Override
                            public void onFailure(Call<ApiResponse> call, Throwable t) {
                                Log.d("Error:", t.getMessage());
                                showDeleteError();
                            }
                        });
                        break;
                }

                getUserTestsData();
            }
        });

        _deleteButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (discipline1) {
                    case "running":
                        ApiService apiService = ServiceGenerator.createService(ApiService.class);
                        Call<ApiResponse> call = apiService.deleteRunningTest("Bearer " + getToken(), response5.getTestId());

                        call.enqueue(new Callback<ApiResponse>() {
                            @Override
                            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                                showDeleteSuccess();
                            }

                            @Override
                            public void onFailure(Call<ApiResponse> call, Throwable t) {
                                Log.d("Error:", t.getMessage());
                                showDeleteError();
                            }
                        });
                        break;
                    case "cycling":
                        apiService = ServiceGenerator.createService(ApiService.class);
                        call = apiService.deleteRunningTest("Bearer " + getToken(), response5.getTestId());

                        call.enqueue(new Callback<ApiResponse>() {
                            @Override
                            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                                showDeleteSuccess();
                            }

                            @Override
                            public void onFailure(Call<ApiResponse> call, Throwable t) {
                                Log.d("Error:", t.getMessage());
                                showDeleteError();
                            }
                        });
                        break;
                    case "swimming":
                        apiService = ServiceGenerator.createService(ApiService.class);
                        call = apiService.deleteSwimmingTest("Bearer " + getToken(), response5.getTestId());

                        call.enqueue(new Callback<ApiResponse>() {
                            @Override
                            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                                showDeleteSuccess();
                            }

                            @Override
                            public void onFailure(Call<ApiResponse> call, Throwable t) {
                                Log.d("Error:", t.getMessage());
                                showDeleteError();
                            }
                        });
                        break;
                }

                getUserTestsData();
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

        Call<List<ApiResponse>> call = null;
        switch (currentMode) {
            case "cycling":

                call = apiService.getCyclingTests("Bearer " + getToken(),
                        Integer.toString(this.limit), Integer.toString(this.offset));
                
                break;
            case "running":
                call = apiService.getRunningTests("Bearer " + getToken(),
                        Integer.toString(this.limit), Integer.toString(this.offset));
                break;
            case "swimming":
                call = apiService.getSwimmingTests("Bearer " + getToken(),
                        Integer.toString(this.limit), Integer.toString(this.offset));
                break;
            default:
                break;
        }

        call.enqueue(new Callback<List<ApiResponse>>() {
            @Override
            public void onResponse(Call<List<ApiResponse>> call, Response<List<ApiResponse>> response) {
                if (response.isSuccessful()) {
                    List<ApiResponse> apiResponse = response.body();

                    String modoActual = getCurrentMode();

                    updateLoading(false);
                    updateResponses(apiResponse);
                    //  Inicializar campos aqui
                    _field11.setVisibility(View.VISIBLE);
                    _field21.setVisibility(View.VISIBLE);
                    _field31.setVisibility(View.VISIBLE);
                    _field41.setVisibility(View.VISIBLE);
                    _field51.setVisibility(View.VISIBLE);

                    _field12.setVisibility(View.VISIBLE);
                    _field22.setVisibility(View.VISIBLE);
                    _field32.setVisibility(View.VISIBLE);
                    _field42.setVisibility(View.VISIBLE);
                    _field52.setVisibility(View.VISIBLE);

                    _field13.setVisibility(View.VISIBLE);
                    _field23.setVisibility(View.VISIBLE);
                    _field33.setVisibility(View.VISIBLE);
                    _field43.setVisibility(View.VISIBLE);
                    _field53.setVisibility(View.VISIBLE);
                    switch (modoActual) {
                        case "cycling":
                            discipline1 = discipline2 = discipline3 = discipline4 = discipline5 = "cycling";
                            _field14.setVisibility(View.VISIBLE);
                            _field24.setVisibility(View.VISIBLE);
                            _field34.setVisibility(View.VISIBLE);
                            _field44.setVisibility(View.VISIBLE);
                            _field54.setVisibility(View.VISIBLE);

                            _header1.setText("6sec");
                            _header2.setText("1min");
                            _header3.setText("6min");
                            _header4.setText("20sec");
                            _header4.setVisibility(View.VISIBLE);

                            try {
                                _field11.setText("" + apiResponse.get(0).getP6sec());
                                _field12.setText(Double.toString(apiResponse.get(0).getP1min()));
                                _field13.setText(Double.toString(apiResponse.get(0).getP6min()));
                                _field14.setText(Double.toString(apiResponse.get(0).getP20min()));

                            } catch (Exception e) {
                                _field11.setVisibility(View.INVISIBLE);
                                _field12.setVisibility(View.INVISIBLE);
                                _field13.setVisibility(View.INVISIBLE);
                                _field14.setVisibility(View.INVISIBLE);
                                _deleteButton1.setVisibility(View.INVISIBLE);
                            }

                            try {
                                _field21.setText(Double.toString(apiResponse.get(1).getP6sec()));
                                _field22.setText(Double.toString(apiResponse.get(1).getP1min()));
                                _field23.setText(Double.toString(apiResponse.get(1).getP6min()));
                                _field24.setText(Double.toString(apiResponse.get(1).getP20min()));
                            } catch (Exception e) {
                                _field21.setVisibility(View.INVISIBLE);
                                _field22.setVisibility(View.INVISIBLE);
                                _field23.setVisibility(View.INVISIBLE);
                                _field24.setVisibility(View.INVISIBLE);
                                _deleteButton2.setVisibility(View.INVISIBLE);
                            }

                            try {
                                _field31.setText("" + apiResponse.get(2).getP6sec());
                                _field32.setText(Double.toString(apiResponse.get(2).getP1min()));
                                _field33.setText(Double.toString(apiResponse.get(2).getP6min()));
                                _field34.setText(Double.toString(apiResponse.get(2).getP20min()));
                            } catch (Exception e) {
                                _field31.setVisibility(View.INVISIBLE);
                                _field32.setVisibility(View.INVISIBLE);
                                _field33.setVisibility(View.INVISIBLE);
                                _field34.setVisibility(View.INVISIBLE);
                                _deleteButton3.setVisibility(View.INVISIBLE);
                            }


                            try {
                                _field41.setText("" + apiResponse.get(3).getP6sec());
                                _field42.setText(Double.toString(apiResponse.get(3).getP1min()));
                                _field43.setText(Double.toString(apiResponse.get(3).getP6min()));
                                _field44.setText(Double.toString(apiResponse.get(3).getP20min()));
                            } catch (Exception e) {
                                _field41.setVisibility(View.INVISIBLE);
                                _field42.setVisibility(View.INVISIBLE);
                                _field43.setVisibility(View.INVISIBLE);
                                _field44.setVisibility(View.INVISIBLE);
                                _deleteButton4.setVisibility(View.INVISIBLE);
                            }

                            try {
                                _field51.setText("" + apiResponse.get(4).getP6sec());
                                _field52.setText(Double.toString(apiResponse.get(4).getP1min()));
                                _field53.setText(Double.toString(apiResponse.get(4).getP6min()));
                                _field54.setText(Double.toString(apiResponse.get(4).getP20min()));
                            } catch (Exception e) {
                                _field51.setVisibility(View.INVISIBLE);
                                _field52.setVisibility(View.INVISIBLE);
                                _field53.setVisibility(View.INVISIBLE);
                                _field54.setVisibility(View.INVISIBLE);
                                _deleteButton5.setVisibility(View.INVISIBLE);
                            }
                            break;
                        case "running":

                            _header1.setText("Vo2max");
                            _header2.setText("Vat");
                            _header3.setText("Mavvo2max");
                            _header4.setVisibility(View.INVISIBLE);


                            _field14.setVisibility(View.INVISIBLE);
                            _field24.setVisibility(View.INVISIBLE);
                            _field34.setVisibility(View.INVISIBLE);
                            _field44.setVisibility(View.INVISIBLE);
                            _field54.setVisibility(View.INVISIBLE);
                            discipline1 = discipline2 = discipline3 = discipline4 = discipline5 = "running";

                            try {

                                _field11.setText("" + apiResponse.get(0).getVat());
                                _field12.setText(Double.toString(apiResponse.get(0).getVo2max()));
                                _field13.setText(Double.toString(apiResponse.get(0).getMavVo2max()));

                            } catch (Exception e) {
                                _field11.setVisibility(View.INVISIBLE);
                                _field12.setVisibility(View.INVISIBLE);
                                _field13.setVisibility(View.INVISIBLE);
                                _field14.setVisibility(View.INVISIBLE);
                                _deleteButton1.setVisibility(View.INVISIBLE);
                            }

                            try {
                                _field21.setText(Double.toString(apiResponse.get(1).getVat()));
                                _field22.setText(Double.toString(apiResponse.get(1).getVo2max()));
                                _field23.setText(Double.toString(apiResponse.get(1).getMavVo2max()));
                            } catch (Exception e) {
                                _field21.setVisibility(View.INVISIBLE);
                                _field22.setVisibility(View.INVISIBLE);
                                _field23.setVisibility(View.INVISIBLE);
                                _field24.setVisibility(View.INVISIBLE);
                                _deleteButton2.setVisibility(View.INVISIBLE);
                            }

                            try {
                                _field31.setText(Double.toString(apiResponse.get(2).getVat()));
                                _field32.setText(Double.toString(apiResponse.get(2).getVo2max()));
                                _field33.setText(Double.toString(apiResponse.get(2).getMavVo2max()));
                            } catch (Exception e) {
                                _field31.setVisibility(View.INVISIBLE);
                                _field32.setVisibility(View.INVISIBLE);
                                _field33.setVisibility(View.INVISIBLE);
                                _field34.setVisibility(View.INVISIBLE);
                                _deleteButton3.setVisibility(View.INVISIBLE);
                            }


                            try {
                                _field41.setText(Double.toString(apiResponse.get(3).getVat()));
                                _field42.setText(Double.toString(apiResponse.get(3).getVo2max()));
                                _field43.setText(Double.toString(apiResponse.get(3).getMavVo2max()));
                            } catch (Exception e) {
                                _field41.setVisibility(View.INVISIBLE);
                                _field42.setVisibility(View.INVISIBLE);
                                _field43.setVisibility(View.INVISIBLE);
                                _field44.setVisibility(View.INVISIBLE);
                                _deleteButton4.setVisibility(View.INVISIBLE);
                            }

                            try {
                                _field51.setText(Double.toString(apiResponse.get(4).getVat()));
                                _field52.setText(Double.toString(apiResponse.get(4).getVo2max()));
                                _field53.setText(Double.toString(apiResponse.get(4).getMavVo2max()));
                            } catch (Exception e) {
                                _field51.setVisibility(View.INVISIBLE);
                                _field52.setVisibility(View.INVISIBLE);
                                _field53.setVisibility(View.INVISIBLE);
                                _field54.setVisibility(View.INVISIBLE);
                                _deleteButton5.setVisibility(View.INVISIBLE);
                            }
                            break;
                        case "swimming":
                            discipline1 = discipline2 = discipline3 = discipline4 = discipline5 = "swimming";

                            _header1.setText("Lactate Threshold");
                            _header2.setText("ANA Threshold");
                            _header3.setText("Index ANAT");
                            _header4.setText("Index LT");
                            _header4.setVisibility(View.VISIBLE);


                            _field14.setVisibility(View.VISIBLE);
                            _field24.setVisibility(View.VISIBLE);
                            _field34.setVisibility(View.VISIBLE);
                            _field44.setVisibility(View.VISIBLE);
                            _field54.setVisibility(View.VISIBLE);

                            try {
                                _field11.setText("" + apiResponse.get(0).getLactateThreshold());
                                _field12.setText(Double.toString(apiResponse.get(0).getAnaThreshold()));
                                _field13.setText(Double.toString(apiResponse.get(0).getIndexANAT()));
                                _field14.setText(Double.toString(apiResponse.get(0).getIndexLT()));

                            } catch (Exception e) {
                                _field11.setVisibility(View.INVISIBLE);
                                _field12.setVisibility(View.INVISIBLE);
                                _field13.setVisibility(View.INVISIBLE);
                                _field14.setVisibility(View.INVISIBLE);
                                _deleteButton1.setVisibility(View.INVISIBLE);
                            }

                            try {
                                _field21.setText("" + apiResponse.get(1).getLactateThreshold());
                                _field22.setText(Double.toString(apiResponse.get(1).getAnaThreshold()));
                                _field23.setText(Double.toString(apiResponse.get(1).getIndexANAT()));
                                _field24.setText(Double.toString(apiResponse.get(1).getIndexLT()));
                            } catch (Exception e) {
                                _field21.setVisibility(View.INVISIBLE);
                                _field22.setVisibility(View.INVISIBLE);
                                _field23.setVisibility(View.INVISIBLE);
                                _field24.setVisibility(View.INVISIBLE);
                                _deleteButton2.setVisibility(View.INVISIBLE);
                            }

                            try {
                                _field31.setText("" + apiResponse.get(2).getLactateThreshold());
                                _field32.setText(Double.toString(apiResponse.get(2).getAnaThreshold()));
                                _field33.setText(Double.toString(apiResponse.get(2).getIndexANAT()));
                                _field34.setText(Double.toString(apiResponse.get(2).getIndexLT()));
                            } catch (Exception e) {
                                _field31.setVisibility(View.INVISIBLE);
                                _field32.setVisibility(View.INVISIBLE);
                                _field33.setVisibility(View.INVISIBLE);
                                _field34.setVisibility(View.INVISIBLE);
                                _deleteButton3.setVisibility(View.INVISIBLE);
                            }


                            try {
                                _field41.setText("" + apiResponse.get(3).getLactateThreshold());
                                _field42.setText(Double.toString(apiResponse.get(3).getAnaThreshold()));
                                _field43.setText(Double.toString(apiResponse.get(3).getIndexANAT()));
                                _field44.setText(Double.toString(apiResponse.get(3).getIndexLT()));
                            } catch (Exception e) {
                                _field41.setVisibility(View.INVISIBLE);
                                _field42.setVisibility(View.INVISIBLE);
                                _field43.setVisibility(View.INVISIBLE);
                                _field44.setVisibility(View.INVISIBLE);
                                _deleteButton4.setVisibility(View.INVISIBLE);
                            }

                            try {
                                _field51.setText("" + apiResponse.get(4).getLactateThreshold());
                                _field52.setText(Double.toString(apiResponse.get(4).getAnaThreshold()));
                                _field53.setText(Double.toString(apiResponse.get(4).getIndexANAT()));
                                _field54.setText(Double.toString(apiResponse.get(4).getIndexLT()));
                            } catch (Exception e) {
                                _field51.setVisibility(View.INVISIBLE);
                                _field52.setVisibility(View.INVISIBLE);
                                _field53.setVisibility(View.INVISIBLE);
                                _field54.setVisibility(View.INVISIBLE);
                                _deleteButton5.setVisibility(View.INVISIBLE);
                            }
                            break;
                        default:
                            break;
                    }
                } else {
                }
            }

            @Override
            public void onFailure(Call<List<ApiResponse>> call, Throwable t) {
                Log.d("Error:", t.getMessage());
                //onLoginFailed();
            }
        });
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

    private void updateLoading(Boolean loading) {
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

    private void increaseOffset() {
        this.offset = this.offset + this.limit;
    }

    private void decreaseOffset() {
        if (this.offset - this.limit > 0) {
            this.offset = this.offset - this.limit;
        } else {
            _previousButton.setEnabled(false);
        }
    }

    private void updateResponses(List<ApiResponse> data) {
        try {
            this.response1 = data.get(0);
            this.response2 = data.get(1);
            this.response3 = data.get(2);
            this.response4 = data.get(3);
            this.response5 = data.get(4);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    private void showDeleteError() {
        Toast toast = Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG);
        toast.show();
    }

    private void showDeleteSuccess() {
        getUserTestsData();
        Toast toast = Toast.makeText(getContext(), "Task succesfully sent", Toast.LENGTH_LONG);
        toast.show();
    }

}
