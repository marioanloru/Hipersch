package com.tfg.hipersch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    public final static String SHARED_PREF_NAME = "hipersch.SHARED_PREF";
    public static final String EMAIL = "hipersch.EMAIL";
    public static final String PASSWORD = "hipersch.PASSWORD";
    private boolean emailVisited = false;
    private boolean passwordVisited = false;
    @BindView(R.id.email_field) TextInputEditText _emailText;
    @BindView(R.id.email_layout) TextInputLayout _emailLayout;
    @BindView(R.id.password) TextInputEditText _passwordText;
    @BindView(R.id.password_layout) TextInputLayout _passwordLayout;
    @BindView(R.id.login_button) MaterialButton _loginButton;
    @BindView(R.id.register_message) TextView _registerMessage;
    @BindView(R.id.progress_bar) ProgressBar _progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        _emailText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (!emailVisited) emailVisited = true;
                } else {
                    if (emailVisited) validateEmail();
                }
            }
        });

        _passwordText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (!passwordVisited) passwordVisited = true;
                } else {
                    if (passwordVisited) validatePassword();
                }
            }
        });

        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(v);
            }
        });

        _registerMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });


        checkApiStatus();
    }

    public void login(View v) {
        if (!validate()) {
            onLoginFailed();
            hideProgress();
            return;
        }
        _loginButton.setEnabled(false);

        showProgress();

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        //  Login
        ApiService apiService = ServiceGenerator.createService(ApiService.class);
        Call<ApiResponse> call = apiService.userLogin(email, password);

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    ApiResponse apiResponse = response.body();
                    onLoginSuccess(v, apiResponse);
                } else {
                    onLoginCredentialsFailure();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.d("Error:", t.getMessage());
                onLoginFailed();
            }
        });

    }

    public void onLoginSuccess(View v, ApiResponse response) {
        TokenManager.setToken(v.getContext(),response.getToken());
        Intent intent = new Intent(v.getContext(), MainActivity.class);
        System.out.println("El rol obtenido!!" + response.getUserRole());
        if (response.getUserRole().equals("trainer")) {
            System.out.println("Seteo trainer login!!!");
            intent = new Intent(v.getContext(), TrainerLogin.class);
        }

        intent.putExtra(EMAIL, _emailText.getText().toString());
        intent.putExtra(PASSWORD, _passwordText.getText().toString());

        System.out.println("He puesto email y password: " + _emailText.getText().toString() + _passwordText.getText().toString());

        startActivity(intent);
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Please, try again in a few seconds", Toast.LENGTH_LONG).show();
        hideProgress();
        _loginButton.setEnabled(true);
    }

    public void onLoginCredentialsFailure() {
        Toast.makeText(getBaseContext(), "Wrong credentials", Toast.LENGTH_LONG).show();
        hideProgress();
        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailLayout.setError("Enter a valid email address");
            valid = false;
        } else {
            _emailLayout.setError(null);
        }

        if (password.isEmpty() || password.length() < 4) {
            _passwordLayout.setError("Must have more than 4 characters");
            valid = false;
        } else {
            _passwordLayout.setError(null);
        }

        return valid;
    }

    public boolean validateEmail() {
        boolean valid = true;
        String email = _emailText.getText().toString();

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailLayout.setError("Enter a valid email address");
            valid = false;
        } else {
            _emailLayout.setError(null);
        }

        return valid;
    }

    public boolean validatePassword() {
        boolean valid = true;
        String password = _passwordText.getText().toString();

        if (password.isEmpty() || password.length() < 6) {
            _passwordLayout.setError("Must have more than 6 characters");
            valid = false;
        } else {
            _passwordLayout.setError(null);
        }

        return valid;
    }

    public void showProgress() {
        _progressBar.setVisibility(View.VISIBLE);
        _registerMessage.setVisibility(View.INVISIBLE);
    }

    public void hideProgress() {
        _progressBar.setVisibility(View.INVISIBLE);
        _registerMessage.setVisibility(View.VISIBLE);
    }

    public void checkApiStatus() {
        ApiService apiService = ServiceGenerator.createService(ApiService.class);
        Call<ApiResponse> call = apiService.getApiStatus();

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                System.out.println("Api response received");
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.d("Error:", t.getMessage());
            }
        });
    }
}
