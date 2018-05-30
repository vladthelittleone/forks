package com.savik.http;

import com.savik.http.exception.DownloadException;
import lombok.extern.log4j.Log4j2;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Map;

@Log4j2
public class Downloader {

    public Document download(String url) {
        try {
            log.debug("download url: " + url);
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
