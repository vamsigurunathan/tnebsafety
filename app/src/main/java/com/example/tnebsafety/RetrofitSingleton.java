package com.example.tnebsafety;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitSingleton {
    private static Retrofit retrofitInstance;
    private static final String loginurl = "https://gis.tneb.in/gisapp/";

    public static Retrofit getRetrofitInstance() {
        TrustManager[] trustAllCertificates = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] chain, String authType) {
                        // Allow all client certificates
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType) {
                        // Allow all server certificates
                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }
                }
        };

        try {
            SSLContext sslContext = null;
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCertificates, new SecureRandom());


        // Create an OkHttpClient with the custom SSLContext
            OkHttpClient client =new OkHttpClient.Builder()
                    .sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustAllCertificates[0])
                    .cookieJar(new MyCookieJar())
                    .connectTimeout(30, TimeUnit.SECONDS) // Set connection timeout to 30 seconds
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build();

            if (retrofitInstance == null) {
                retrofitInstance = new Retrofit.Builder()
                        .baseUrl(loginurl)
                        .client(client)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }




        } catch (Exception e) {
            e.printStackTrace();
        }
        return retrofitInstance;
    }



    }


