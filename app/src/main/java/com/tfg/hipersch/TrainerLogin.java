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

public class TrainerLogin extends AppCompatActivity {
  private String selectedAthlete;
  private String email;
  private String password;

    @BindView(R.id.spinner) Spinner _athleteSpinner;
    @BindView(R.id.progress_bar) ProgressBar _progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        setContentView(R.layout.activity_sign_up_personal);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        email = intent.getStringExtra(SignUpActivity.EMAIL);
        password = intent.getStringExtra(SignUpActivity.PASSWORD);

        //email =
        //password =

        ArrayList<String> athletes = new ArrayList<>();
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                athletes, android.R.layout.simple_spinner_dropdown_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        _athleteSpinner.setAdapter(adapter);

        _athleteSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {  
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
              selectedAthlete = _athleteSpinner.getSelectedItem().toString();
              if (validate()) {
                next(selectedItemView);
              }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        _finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) register(v);
            }
        });
    }

  public void next(View v) {
    Intent intent = new Intent(v.getContext(), MainActivity.class);
    intent.putExtra(ATHLETE, this.selectedAthlete);
    startActivity(intent);
  }

  public boolean validate() {
    String sHeight = this.selectedAthlete;
    boolean valid = true;
    if (sHeight.isEmpty()) {
        valid = false;
        _athleteSpinner.setError("This field cannot be empty");
    }

    return valid;
  }

  public void sendAthlete(View v) {
      if (!validate()) {
          //onLoginFailed();
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

  public void showProgress() {
      _progressBar.setVisibility(View.VISIBLE);
      _registerMessage.setVisibility(View.INVISIBLE);
  }

  public void hideProgress() {
      _progressBar.setVisibility(View.INVISIBLE);
      _registerMessage.setVisibility(View.VISIBLE);
  }

}
