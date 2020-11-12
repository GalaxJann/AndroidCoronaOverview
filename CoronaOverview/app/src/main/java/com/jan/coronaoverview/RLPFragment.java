package com.jan.coronaoverview;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.textview.MaterialTextView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RLPFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RLPFragment extends Fragment {

    private MaterialTextView totalCases;
    private MaterialTextView totalDeaths;
    private MaterialTextView totalRecovered;
    private MaterialTextView textActiveCases;
    private MaterialTextView textTimestamp;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public RLPFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RLPFragment.
     */
    public static RLPFragment newInstance(String param1, String param2) {
        RLPFragment fragment = new RLPFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rlp, container, false);
        totalCases = view.findViewById(R.id.totalCases);
        totalDeaths = view.findViewById(R.id.totalDeaths);
        totalRecovered = view.findViewById(R.id.totalRecovered);
        textActiveCases = view.findViewById(R.id.activeCases);
        textTimestamp = view.findViewById(R.id.textTimestamp);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int intCases = getCases("https://services7.arcgis.com/mOBPykOjAyBO2ZKk/arcgis/rest/services/RKI_Landkreisdaten/FeatureServer/0/query?f=json&where=BL%3D%27Rheinland-Pfalz%27&returnGeometry=false&spatialRel=esriSpatialRelIntersects&outFields=*&outStatistics=%5B%7B%22statisticType%22%3A%22sum%22%2C%22onStatisticField%22%3A%22cases%22%2C%22outStatisticFieldName%22%3A%22value%22%7D%5D&outSR=102100&resultType=standard&cacheHint=true");
                    int intNewcases = getCases("https://services7.arcgis.com/mOBPykOjAyBO2ZKk/arcgis/rest/services/RKI_COVID19/FeatureServer/0/query?f=json&where=(NeuerFall%20IN(1%2C%20-1))%20AND%20(Bundesland%3D%27Rheinland-Pfalz%27)&returnGeometry=false&spatialRel=esriSpatialRelIntersects&outFields=*&outStatistics=%5B%7B%22statisticType%22%3A%22sum%22%2C%22onStatisticField%22%3A%22AnzahlFall%22%2C%22outStatisticFieldName%22%3A%22value%22%7D%5D&resultType=standard&cacheHint=true");
                    int intDeaths = getCases("https://services7.arcgis.com/mOBPykOjAyBO2ZKk/arcgis/rest/services/RKI_Landkreisdaten/FeatureServer/0/query?f=json&where=BL%3D%27Rheinland-Pfalz%27&returnGeometry=false&spatialRel=esriSpatialRelIntersects&outFields=*&outStatistics=%5B%7B%22statisticType%22%3A%22sum%22%2C%22onStatisticField%22%3A%22deaths%22%2C%22outStatisticFieldName%22%3A%22value%22%7D%5D&outSR=102100&resultType=standard&cacheHint=true");
                    int intNewdeaths = getCases("https://services7.arcgis.com/mOBPykOjAyBO2ZKk/arcgis/rest/services/RKI_COVID19/FeatureServer/0/query?f=json&where=(NeuerTodesfall%20IN(1%2C%20-1))%20AND%20(Bundesland%3D%27Rheinland-Pfalz%27)&returnGeometry=false&spatialRel=esriSpatialRelIntersects&outFields=*&outStatistics=%5B%7B%22statisticType%22%3A%22sum%22%2C%22onStatisticField%22%3A%22AnzahlTodesfall%22%2C%22outStatisticFieldName%22%3A%22value%22%7D%5D&resultType=standard&cacheHint=true");
                    int[] recoveredData = getRecovered("https://services7.arcgis.com/mOBPykOjAyBO2ZKk/arcgis/rest/services/RKI_COVID19_Recovered_BL/FeatureServer/0/query?f=json&where=Bundesland%3D%27Rheinland-Pfalz%27&returnGeometry=false&spatialRel=esriSpatialRelIntersects&outFields=*&orderByFields=IdBundesland%20asc&resultOffset=0&resultRecordCount=1&resultType=standard&cacheHint=true");

                    Locale currentLocale = Locale.getDefault();
                    //Formatting numbers
                    DecimalFormatSymbols symbols = new DecimalFormatSymbols(currentLocale);
                    symbols.setDecimalSeparator(',');
                    symbols.setGroupingSeparator('.');
                    DecimalFormat df = new DecimalFormat("###,###,###,###", symbols);
                    String cases = df.format(intCases);
                    String newcases = df.format(intNewcases);
                    String deaths = df.format(intDeaths);
                    String newdeaths = df.format(intNewdeaths);
                    String recovered = df.format(recoveredData[0]);
                    String newrecovered = df.format(recoveredData[1]);
                    String activeCases = df.format(intCases - recoveredData[0]);
                    String newActiveCases = df.format(intNewcases - recoveredData[1]);
                    String weekday = getWeekday(recoveredData[5]);
                    String timestamp = weekday + "; " + recoveredData[2] + "." + recoveredData[3] + "." + recoveredData[4];
                    SharedPreferences spref = getActivity().getSharedPreferences("values", 0);
                    SharedPreferences.Editor editor = spref.edit();
                    editor.putString("timestamp", timestamp);
                    editor.apply();

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            totalCases.setText(cases + " | +" + newcases);
                            totalDeaths.setText(deaths + " | +" + newdeaths);
                            totalRecovered.setText(recovered + " | +" + newrecovered);
                            if((intNewcases - recoveredData[1]) < 0) {
                                textActiveCases.setText(activeCases + " | " + newActiveCases);
                            } else {
                                textActiveCases.setText(activeCases + " | +" + newActiveCases);
                            }
                            textTimestamp.setText("Stand: " + timestamp);
                        }
                    });
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        //Must be at end
        return view;
    } private int getCases(String url) throws InterruptedException {
        OkHttpClient client = new OkHttpClient().newBuilder().build();

        final Request request = new Request.Builder().url(url).get().header("Accept", "*/*")
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.105 Safari/537.36")
                .build();

        final int[] cases = {10};
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                String mMessage = e.getMessage().toString();
                Log.w("Failure Response", mMessage);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    String responseString = response.body().string();

                    if(responseString.contains("\"value\":null")) {
                        cases[0] = 0;
                    } else {
                        cases[0] = new JSONObject(responseString).getJSONArray("features").getJSONObject(0).getJSONObject("attributes").getInt("value");
                    }

                    countDownLatch.countDown();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        countDownLatch.await();
        return cases[0];
    } private int[] getRecovered(String url) throws InterruptedException {
        OkHttpClient client = new OkHttpClient().newBuilder().build();

        final Request request = new Request.Builder().url(url).get().header("Accept", "*/*")
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.105 Safari/537.36")
                .build();

        final int[] data = {0, 0, 0, 0, 0, 0};
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                String mMessage = e.getMessage().toString();
                Log.w("Failure Response", mMessage);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    String responseString = response.body().string();

                    JSONObject jsonObject = new JSONObject(responseString).getJSONArray("features").getJSONObject(0).getJSONObject("attributes");
                    data[0] = jsonObject.getInt("Genesen");
                    data[1] = jsonObject.getInt("DiffVortag");
                    String dateString = jsonObject.getString("Datenstand");
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy, HH:mm");
                    Date date = dateFormat.parse(dateString);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    data[2] = calendar.get(Calendar.DAY_OF_MONTH);
                    data[3] = calendar.get(Calendar.MONTH) + 1;
                    data[4] = calendar.get(Calendar.YEAR);
                    data[5] = calendar.get(Calendar.DAY_OF_WEEK);

                    countDownLatch.countDown();
                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        countDownLatch.await();
        /*
            data Array -> Data

            0 = Genesen value
            1 = DiffVortag value
            2 = day value
            3 = month value
            4 = year value
            5 = weekday value
        */
        return data;
    } private String getWeekday(int day) {
        String weekday = "";
        switch(day) {
            case 0:
                weekday = "Samstag";
                break;
            case 1:
                weekday = "Sonntag";
                break;
            case 2:
                weekday = "Montag";
                break;
            case 3:
                weekday = "Dienstag";
                break;
            case 4:
                weekday = "Mittwoch";
                break;
            case 5:
                weekday = "Donnerstag";
                break;
            case 6:
                weekday = "Freitag";
                break;
        }
        return weekday;
    }
}