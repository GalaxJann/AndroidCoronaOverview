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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MykFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MykFragment extends Fragment {

    private MaterialTextView totalCases;
    private MaterialTextView totalDeaths;
    private MaterialTextView per100k;
    private MaterialTextView textTimestamp;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public MykFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MykFragment.
     */
    public static MykFragment newInstance(String param1, String param2) {
        MykFragment fragment = new MykFragment();
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
        View view = inflater.inflate(R.layout.fragment_myk, container, false);
        totalCases = view.findViewById(R.id.totalCases);
        totalDeaths = view.findViewById(R.id.totalDeaths);
        per100k = view.findViewById(R.id.per100k);
        textTimestamp = view.findViewById(R.id.textTimestamp);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String json = getCases("https://services7.arcgis.com/mOBPykOjAyBO2ZKk/arcgis/rest/services/RKI_Landkreisdaten/FeatureServer/0/query?f=json&where=BL%3D%27Rheinland-Pfalz%27&returnGeometry=false&spatialRel=esriSpatialRelIntersects&outFields=*&orderByFields=cases%20desc&outSR=102100&resultOffset=0&resultRecordCount=1000&resultType=standard&cacheHint=true");
                    double cases = 0;
                    double deaths = 0;
                    double casesper100k = 0;
                    double casesperweek100k = 0;
                    JSONArray jsonArray = new JSONObject(json).getJSONArray("features");
                    for(int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i).getJSONObject("attributes");
                        String city = jsonObject.getString("GEN");
                        if(city.contains("Mayen-Koblenz")) {
                            cases = jsonObject.getDouble("cases");
                            deaths = jsonObject.getDouble("deaths");
                            casesper100k = jsonObject.getDouble("cases_per_100k");
                            casesperweek100k = jsonObject.getDouble("cases7_per_100k");
                        }
                    }

                    //Formatting numbers to Strings
                    Locale currentLocale = Locale.getDefault();
                    DecimalFormatSymbols symbols = new DecimalFormatSymbols(currentLocale);
                    symbols.setDecimalSeparator(',');
                    symbols.setGroupingSeparator('.');
                    DecimalFormat df = new DecimalFormat("###,###,###,###.##", symbols);
                    final String finalCases = df.format(cases);
                    final String finalDeaths = df.format(deaths);
                    final String finalCasesper100k = df.format(casesper100k);
                    final String finalCasesperweek100k = df.format(casesperweek100k);
                    SharedPreferences spref = getActivity().getSharedPreferences("values", 0);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            totalCases.setText(finalCases);
                            totalDeaths.setText(finalDeaths);
                            per100k.setText(finalCasesper100k + "\n7 Tage: " + finalCasesperweek100k);
                            textTimestamp.setText("Stand: " + spref.getString("timestamp", "Fehler, bitte erneut versuchen"));
                        }
                    });
                } catch (JSONException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        //Must be at end
        return view;
    } private String getCases(String url) throws InterruptedException {
        OkHttpClient client = new OkHttpClient().newBuilder().build();

        final Request request = new Request.Builder().url(url).get().header("Accept", "*/*")
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.105 Safari/537.36")
                .build();

        final String[] responseString = new String[1];
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                String mMessage = e.getMessage();
                Log.w("Failure Response", mMessage);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                responseString[0] = response.body().string();

                countDownLatch.countDown();
            }
        });

        countDownLatch.await();
        return responseString[0];
    }
}