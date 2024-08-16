package com.example.tnebsafety;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

public class MyCookieJar implements CookieJar {

    private final List<Cookie> cookies = new ArrayList<>();

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        this.cookies.addAll(cookies);
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> validCookies = new ArrayList<>();
        long currentTimeMillis = System.currentTimeMillis();

        for (Cookie cookie : cookies) {
            if (cookie.expiresAt() >= currentTimeMillis) {
                validCookies.add(cookie);
            }
        }

        return validCookies;
    }
}

