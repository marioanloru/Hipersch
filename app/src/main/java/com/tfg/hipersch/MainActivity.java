package com.tfg.hipersch;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private int height;
    private int bodyWeight;
    private double bodyMassIndex;

    @BindView(R.id.height) TextView _height;
    @BindView(R.id.weight) TextView _weight;
    @BindView(R.id.bmi) TextView _bmi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //  get token from app shared info
        System.out.println("Getting user info");
        getUserData(TokenManager.getToken(this).toString());
    }

    public void getUserData(String token) {
        ApiService apiService = ServiceGenerator.createService(ApiService.class);
        Call<ApiResponse> call = apiService.getUserData("Bearer " + token);

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    ApiResponse apiResponse = response.body();
                    System.out.println("Respuesta de la api!!: " + apiResponse);
                    addUserDataParameters(apiResponse);
                    //onLoginSuccess(v, apiResponse);
                } else {
                    System.out.println("-----------Ha fallado " + response.body());
                    //onLoginFailed();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.d("Error:", t.getMessage());
                System.out.println("--------------- erroooor");
                //onLoginFailed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        // Do nothing on back pressed at main activity
    }

    public void addUserDataParameters(ApiResponse response) {
        _height.setText(Integer.toString(response.getHeight()));
        _weight.setText(Integer.toString(response.getBodyWeight()));
        _bmi.setText(Double.toString(response.getBodyMassIndex()));
    }
}
