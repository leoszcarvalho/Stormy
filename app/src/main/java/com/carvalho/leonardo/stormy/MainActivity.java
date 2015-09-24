package com.carvalho.leonardo.stormy;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "leoteste";
    private CurrentWeather mCurrentWeather;
    private String jsonData;

    @Bind(R.id.timeLabel) TextView mTimeLabel;
    @Bind(R.id.temperatureLabel) TextView mTemperatureLabel;
    @Bind(R.id.humidityValue) TextView mHumidityValue;
    @Bind(R.id.precipValue) TextView mPrecipValue;
    @Bind(R.id.summaryLabel) TextView mSummaryLabel;
    @Bind(R.id.iconImageView) ImageView mIconImageView;
    @Bind(R.id.refreshImageView) ImageView mRefreshImageView;
    @Bind(R.id.progressBar) ProgressBar mProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        toogleRefresh();

        getForecast();


        mRefreshImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getForecast();
            }
        });


    }

    private void getForecast() {
        String apiKey = "0b86bc16c8a1f3d6984e11a52025e45c";
        double latitude = -23.996943;
        double longitude = -46.2673752;

        String forecastUrl = "https://api.forecast.io/forecast/" + apiKey + "/" + latitude + "," + longitude;


        if(isNetworkAvailable())
        {

            toogleRefresh();

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(forecastUrl)
                    .build();

            Call call = client.newCall(request);


            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {

                    Log.d("leoteste", "Erro na requisição");

                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run() {
                            toogleRefresh();
                        }
                    });

                    alertUserAboutError();

                }

                @Override
                public void onResponse(Response response) throws IOException {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toogleRefresh();
                        }
                    });

                    try {

                        if (response.isSuccessful()) {

                            jsonData = response.body().string();

                            Log.v(TAG, response.body().string());

                            mCurrentWeather = getCurrentDetails(jsonData);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    updateDisplay();

                                }
                            });


                        } else {
                            alertUserAboutError();
                        }

                    } catch (IOException e) {
                        Log.e(TAG, "Exception caught", e);
                    } catch (JSONException e) {
                        Log.e(TAG, "Exception caught", e);
                    }

                }
            });

        }
        else
        {
            Toast.makeText(MainActivity.this, "Network is unavailable!", Toast.LENGTH_LONG).show();
        }
    }

    private void toogleRefresh()
    {

        if (mProgressBar.getVisibility() == View.INVISIBLE)
        {
            mProgressBar.setVisibility(View.VISIBLE);
            mRefreshImageView.setVisibility(View.INVISIBLE);
        }
        else
        {
            mProgressBar.setVisibility(View.INVISIBLE);
            mRefreshImageView.setVisibility(View.VISIBLE);
        }

    }

    private void updateDisplay()
    {

        mTemperatureLabel.setText(String.valueOf(mCurrentWeather.getTemperature()));
        mTimeLabel.setText("At " + mCurrentWeather.getFormattedTime() + " it will be");
        mHumidityValue.setText(String.valueOf(mCurrentWeather.getHumidity()));
        mPrecipValue.setText(String.valueOf(mCurrentWeather.getPrecipChance()) + "%");
        mSummaryLabel.setText(mCurrentWeather.getSummary());

        Drawable drawable = ContextCompat.getDrawable(MainActivity.this, mCurrentWeather.getIconId());
        mIconImageView.setImageDrawable(drawable);



    }

    private CurrentWeather getCurrentDetails(String jsonData) throws JSONException
    {
        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");
        Log.i(TAG, "From JSON: " + timezone);
        JSONObject currently = forecast.getJSONObject("currently");

        CurrentWeather currentWeather = new CurrentWeather();
        currentWeather.setHumidity(currently.getDouble("humidity"));
        currentWeather.setTime(currently.getLong("time"));
        currentWeather.setIcon(currently.getString("icon"));
        currentWeather.setPrecipChance(currently.getDouble("precipProbability"));
        currentWeather.setSummary(currently.getString("summary"));
        currentWeather.setTemperature(currently.getDouble("temperature"));
        currentWeather.setTimezone(timezone);

        Log.d(TAG, currentWeather.getFormattedTime());

        return currentWeather;

    }

    private boolean isNetworkAvailable() {

        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkinfo = manager.getActiveNetworkInfo();

        boolean isAvailable = false;

        if(networkinfo != null && networkinfo.isConnected())
        {
            isAvailable = true;
        }

        return isAvailable;

    }

    private void alertUserAboutError() {

        AlertDialogFragment dialog = new AlertDialogFragment();

        dialog.show(getFragmentManager(), "error_dialog");

    }


}
