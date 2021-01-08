package com.demo.wallpappers.Repository;

import android.os.AsyncTask;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PhotoRepo {
    
    static String API_KEY = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";

    public interface onDataListener{
        void returnData(String data);
    }

    public static class getData extends AsyncTask<String, String, String>{

        onDataListener listener;
        private String query;
        private boolean search = false;
        private HttpUrl url;
        private int page;


        public getData(onDataListener listener){
            this.listener = listener;
        }

        public getData(onDataListener listener, String query, int page){
            this.listener = listener;
            this.query = query;
            this.page = page;
            search = true;
        }


        @Override
        protected String doInBackground(String... strings) {
            OkHttpClient client = new OkHttpClient();

            if (search){
                url = HttpUrl.parse(strings[0])
                        .newBuilder()
                        .addQueryParameter("query", query)
                        .addQueryParameter("page", String.valueOf(page))
                        .build();
            }
            else {
                url = HttpUrl.parse(strings[0])
                        .newBuilder()
                        .addQueryParameter("count", String.valueOf(10))
                        .build();
            }


            Request request = new Request.Builder()
                    .url(url)
                    .header("User-Agent", System.getProperty("http.agent"))
                    .header("Authorization", "Client-ID " + API_KEY)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s == null) {
                listener.returnData("");
            }
            else {
                listener.returnData(s);
            }
        }
    }


}
