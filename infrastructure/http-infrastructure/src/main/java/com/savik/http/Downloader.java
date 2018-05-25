package com.savik.http;

import com.savik.http.exception.DownloadException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Map;


public class Downloader {

    public Document download(String url) {
        try {
            return Jsoup.connect(url).get();
        } catch (IOException e) {
            throw new DownloadException(e);
        }
    }

    public Document download(String url, Map<String, String> headers) {
        try {
            return Jsoup.connect(url).headers(headers).get();
        } catch (IOException e) {
            throw new DownloadException(e);
        }
    }
}
