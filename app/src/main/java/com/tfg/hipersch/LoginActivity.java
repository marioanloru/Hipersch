package com.tfg.hipersch;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    private boolean emailVisited = false;
    private boolean passwordVisited = false;
    private boolean loading = false;
    private boolean showPassword = false;
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
                login();
            }
        });

        _registerMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(getApplicationContext());
                //startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });
    }

    public void login() {
        if (!validate()) {
            onLoginFailed();
            hideProgress();
            return;
        }
        _loginButton.setEnabled(false);

        showProgress();

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Please, check login fields", Toast.LENGTH_LONG).show();
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
}
