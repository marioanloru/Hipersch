package com.tfg.hipersch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpPersonalActivity extends AppCompatActivity {
    public boolean heightVisited = false;
    public boolean weightVisited = false;
    private String password;
    private String email;
    private String firstName;
    private String lastName;

    @BindView(R.id.height_layout) TextInputLayout _heightLayout;
    @BindView(R.id.height_field) TextInputEditText _heightField;
    @BindView(R.id.weight_layout) TextInputLayout _weightLayout;
    @BindView(R.id.weight_field) TextInputEditText _weightField;
    @BindView(R.id.gender_group) RadioGroup _genderGroup;
    @BindView(R.id.male_option) RadioButton _maleOption;
    @BindView(R.id.female_option) RadioButton _femaleOption;
    @BindView(R.id.role_group) RadioGroup _roleGroup;
    @BindView(R.id.finish_button) MaterialButton _finishButton;
    @BindView(R.id.progress_bar) ProgressBar _progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        setContentView(R.layout.activity_sign_up_personal);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        firstName = intent.getStringExtra(SignUpActivity.NAME);
        lastName = intent.getStringExtra(SignUpActivity.LASTNAME);
        email = intent.getStringExtra(SignUpActivity.EMAIL);
        password = intent.getStringExtra(SignUpActivity.PASSWORD);

        _finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) register(v);
            }
        });

        _heightField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (!heightVisited) heightVisited = true;
                } else {
                    if (heightVisited) validateHeight();
                }
            }
        });

        _weightField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (!weightVisited) weightVisited = true;
                } else {
                    if (weightVisited) validateWeight();
                }
            }
        });
    }

    public void register(View v) {

        _finishButton.setEnabled(false);
        showProgress();

        String gender = genderSelected();
        String role = roleSelected();
        String height = _heightField.getText().toString();
        String bodyWeight = _weightField.getText().toString();

        ApiService apiService = ServiceGenerator.createService(ApiService.class);
        Call<ApiResponse> call = apiService.userRegister(email, password, firstName,
                lastName, bodyWeight, height, gender, role);

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    ApiResponse apiResponse = response.body();
                    if (apiResponse.getMessage() == "User already created") {
                        onRegisterFailed(apiResponse.getMessage());
                    } else onRegisterSuccess(v, apiResponse);
                } else {
                    onRegisterFailed("");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.d("Error:", t.getMessage());
                onRegisterFailed("");
            }
        });

    }

    public void onRegisterSuccess(View v, ApiResponse response) {
        if (response.getUserRole() == "trainer") {
            Toast.makeText(getBaseContext(), "This action needs to be aproved by an admin", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getBaseContext(), "Register succeeded", Toast.LENGTH_LONG).show();
        }
        //  Comprobar role
        hideProgress();
        Intent intent = new Intent(v.getContext(), LoginActivity.class);
        startActivity(intent);
    }

    public void onRegisterFailed(String message) {
        if (message.isEmpty()) message = "Something went wrong";
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
        hideProgress();
        _finishButton.setEnabled(true);
    }

    public String roleSelected() {
        int id = _roleGroup.getCheckedRadioButtonId();
        String role = "";
        switch(id) {
            case R.id.athlete_option:
                role = "athlete";
                break;
            case R.id.trainer_option:
                role = "trainer";
                break;
        }
        System.out.println("El rol seleccionado es: " + role);
        return role;
    }

    public String genderSelected() {
        int id = _genderGroup.getCheckedRadioButtonId();
        System.out.println("LO QUE CONTIENE EL ID: " + id);
        String gender = "";
        switch(id) {
            case R.id.male_option:
                gender = "male";
                break;
            case R.id.female_option:
                gender = "trainer";
                break;
        }
        System.out.println("El gender seleccionado es: " + gender);
        return gender;
    }

    public boolean validate() {
        boolean valid = true;
        String sHeight = _heightField.getText().toString();
        String sWeight = _weightField.getText().toString();
        int genderId = _genderGroup.getCheckedRadioButtonId();

        //  Height validation
        if (sHeight.isEmpty()) {
            _heightLayout.setError("This field cannot be empty");
            valid = false;
        } else {
            int height = Integer.parseInt(sHeight);
            if (height < 1 || height > 300) {
                _heightLayout.setError("Enter a valid height value");
                valid = false;
            } else _heightLayout.setError(null);
        }


        //  Weigth validation
        if (sWeight.isEmpty()) {
            _weightLayout.setError("This field cannot be empty");
            valid = false;
        } else {
            int weight = Integer.parseInt(sWeight);
            if (weight < 1 || weight > 300) {
                _weightLayout.setError("Enter a valid weight value");
                valid = false;
            } else _weightLayout.setError(null);
        }

        return valid;
    }

    public boolean validateHeight() {
        boolean valid = true;
        String sHeight = _heightField.getText().toString();

        if (sHeight.isEmpty()) {
            valid = false;
            _heightLayout.setError("This field cannot be empty");
        } else {
            int height = Integer.parseInt(sHeight);
            if (height < 1 || height > 300) {
                valid = false;
                _heightLayout.setError("Enter a valid height value");
            } else _heightLayout.setError(null);
        }

        return valid;
    }

    public boolean validateWeight() {
        boolean valid = true;
        String sWeight = _weightField.getText().toString();

        if (sWeight.isEmpty()) {
            valid = false;
            _weightLayout.setError("This field cannot be empty");
        } else {
            int weight = Integer.parseInt(sWeight);
            if (weight < 1 || weight > 300) {
                valid = false;
                _weightLayout.setError("Enter a valid height value");
            } else _weightLayout.setError(null);
        }

        return valid;
    }



    public void showProgress() {
        _progressBar.setVisibility(View.VISIBLE);
        _finishButton.setEnabled(false);
    }

    public void hideProgress() {
        _progressBar.setVisibility(View.INVISIBLE);
        _finishButton.setEnabled(true);
    }

}
