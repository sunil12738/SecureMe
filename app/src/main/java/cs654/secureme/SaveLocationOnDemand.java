package cs654.secureme;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by sunil on 5/4/16.
 */
public class SaveLocationOnDemand extends Service {

    GPS gps;
    double latitude, longitude;
    String url1;



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        gps = new GPS(this);
        try {

            StoreLocation str = new StoreLocation();
            str.execute(url1);


        } catch (Exception e) {
            e.printStackTrace();
        }

        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }


    private class StoreLocation extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            while (true) {
                if (gps.canGetLocation()) {
                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();
                    System.out.println("sunil sunil " + latitude + " " + longitude);
                    System.out.println("sunil sunil 2" + latitude + " " + longitude);

                    BufferedReader reader = null;
                    StringBuilder sb = new StringBuilder();
                    for (String url1 : urls) {
                        url1 = "http://172.20.176.195/cs654/project/save_location.php/sunil/" + latitude + "/" + longitude;

                        try {
                            URL url = new URL(url1);
                            HttpURLConnection con = (HttpURLConnection) url.openConnection();
                            sb = new StringBuilder();
                            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                            String line;
                            while ((line = reader.readLine()) != null) {
                                sb.append(line + "\n");
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        Thread.sleep(10000);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if(F1.saveStopLocation==false){break;}
            }

            return "";
        }

        @Override
        protected void onPostExecute(String result) {
        }
    }

}
