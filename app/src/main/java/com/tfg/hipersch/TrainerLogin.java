package com.tfg.hipersch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.session.MediaSession;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrainerLogin extends AppCompatActivity {
    @BindView(R.id.spinner) Spinner _athleteSpinner;
    @BindView(R.id.progress_bar) ProgressBar _progressBar;
    @BindView(R.id.finish_button) MaterialButton _finishButton;

    public static final String ATHLETE = "hipersch.ATHLETE";
    public static final String EMAIL = "hipersch.EMAIL";
    public static final String PASSWORD = "hipersch.PASSWORD";
    private String email;
    private String password;
    private ArrayList<String> athletes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        setContentView(R.layout.activity_trainer_login);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        this.email = intent.getStringExtra(LoginActivity.EMAIL);
        this.password = intent.getStringExtra(LoginActivity.PASSWORD);


        getAthletes();
        _athleteSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
              //selectedAthlete = _athleteSpinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        _finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) sendAthlete(v);
            }
        });
    }

  public void next(View v) {
    /*Intent intent = new Intent(v.getContext(), MainActivity.class);
    intent.putExtra(ATHLETE, this.selectedAthlete);
    startActivity(intent);*/
  }

  public boolean validate() {
        return true;
    /*String sHeight = this.selectedAthlete;
    boolean valid = true;
    if (sHeight.isEmpty()) {
        valid = false;
        System.out.println("El campo no puede estar vacio!!!!");
        //_athleteSpinner.setsetError("This field cannot be empty");
    }

    return valid;*/
  }

  public void sendAthlete(View v) {

      if (!validate()) {
          //onLoginFailed();
          hideProgress();
          return;
      }
      _finishButton.setEnabled(false);

      showProgress();

      Intent intent = getIntent();
      String email = this.email;
      String password = this.password;
      String athlete = _athleteSpinner.getSelectedItem().toString();

      //  Login
      ApiService apiService = ServiceGenerator.createService(ApiService.class);
      Call<ApiResponse> call = apiService.userLoginTrainer(email, password, athlete);

      call.enqueue(new Callback<ApiResponse>() {
          @Override
          public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
              if (response.isSuccessful()) {
                  ApiResponse apiResponse = response.body();
                  onLoginSuccess(v, apiResponse);
              } else {
                  onLoginFailed();
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
        startActivity(intent);
    }

    public void onLoginFailed() {
        System.out.println("El login ha fallado");
        /*Toast.makeText(getBaseContext(), "Please, try again in a few seconds", Toast.LENGTH_LONG).show();
        hideProgress();
        _loginButton.setEnabled(true);*/
    }

  public void showProgress() {
      _progressBar.setVisibility(View.VISIBLE);
      //_registerMessage.setVisibility(View.INVISIBLE);
  }

  public void hideProgress() {
      _progressBar.setVisibility(View.INVISIBLE);
      //_registerMessage.setVisibility(View.VISIBLE);
  }

  public void getAthletes() {
      TrainerLogin that = this;
      ApiService apiService = ServiceGenerator.createService(ApiService.class);
      Call<List<ApiResponse>> call = apiService.getUserAthletes("Bearer " + TokenManager.getToken(this));

      call.enqueue(new Callback<List<ApiResponse>>() {
          @Override
          public void onResponse(Call<List<ApiResponse>> call, Response<List<ApiResponse>> response) {

              List<ApiResponse> apiResponse = response.body();
              ArrayList<String> athletes = new ArrayList<>();
              for (int i = 0; i < apiResponse.size(); i += 1) {
                  athletes.add(apiResponse.get(i).getEmail());
              }

              ArrayAdapter<String> adapter = new ArrayAdapter<String>
                      (that, android.R.layout.simple_spinner_item,
                              athletes);
              // Specify the layout to use when the list of choices appears
              adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
              // Apply the adapter to the spinner
              _athleteSpinner.setAdapter(adapter);
              that.athletes = athletes;
          }

          @Override
          public void onFailure(Call<List<ApiResponse>> call, Throwable t) {
              Log.d("Error:", t.getMessage());
          }
      });
  }
}
