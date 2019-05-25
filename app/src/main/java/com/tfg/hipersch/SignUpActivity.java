package com.tfg.hipersch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "SignUpActivity";
    public static final String NAME = "hipersch.NAME";
    public static final String EMAIL = "hipersch.EMAIL";
    public static final String PASSWORD = "hipersch.PASSWORD";

    private boolean nameVisited = false;
    private boolean emailVisited = false;
    private boolean passwordVisited = false;
    private boolean passwordConfirmVisited = false;

    @BindView(R.id.name_layout) TextInputLayout _nameLayout;
    @BindView(R.id.name_field) TextInputEditText _nameText;
    @BindView(R.id.email_layout) TextInputLayout _emailLayout;
    @BindView(R.id.email_field) TextInputEditText _emailText;
    @BindView(R.id.password_layout) TextInputLayout _passwordLayout;
    @BindView(R.id.password) TextInputEditText _passwordText;
    @BindView(R.id.password_confirm_layout) TextInputLayout _passwordConfirmLayout;
    @BindView(R.id.password_confirm) TextInputEditText _passwordConfirmText;
    @BindView(R.id.next_button) MaterialButton _nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);

        //  Validation listeners
        _nameText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (!nameVisited) nameVisited = true;
                } else {
                    if (nameVisited) validateName();
                }
            }
        });
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
        _passwordConfirmText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (!passwordConfirmVisited) passwordConfirmVisited = true;
                } else {
                    if (passwordConfirmVisited) validatePasswordConfirm();
                }
            }
        });

        //  Go to next signup activity
        _nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()) {
                    next(v);
                }
            }
        });


    }

    public void next(View v) {
        Intent intent = new Intent(v.getContext(), SignUpPersonalActivity.class);
        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        intent.putExtra(NAME, name);
        intent.putExtra(EMAIL, email);
        intent.putExtra(PASSWORD, password);
        startActivity(intent);
    }

    public boolean validate() {
        boolean validEmail = validateEmail();
        boolean validPassword = validatePassword();
        boolean validPasswordConfirm = validatePasswordConfirm();

        return validEmail && validPassword && validPasswordConfirm;
    }

    public boolean validateName() {
        boolean valid = true;
        String name = _nameText.getText().toString();

        if (name.isEmpty()) {
            _nameLayout.setError("This field cannot be empty");
            valid = false;
        } else {
            _nameLayout.setError(null);
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

    public boolean validatePasswordConfirm() {
        boolean valid = true;
        String password = _passwordText.getText().toString();
        String passwordConfirm = _passwordConfirmText.getText().toString();

        if (passwordConfirm.isEmpty() || passwordConfirm.length() < 6 || (!password.equals(passwordConfirm))) {
            _passwordLayout.setError("Passwords must be the same");
            _passwordConfirmLayout.setError("Passwords must be the same");
            valid = false;
        } else {
            _passwordLayout.setError(null);
            _passwordConfirmLayout.setError(null);
        }

        return valid;
    }
}
