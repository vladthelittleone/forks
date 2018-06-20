package com.savik.http;

import com.github.alessiop86.antiantibotcloudflare.AntiAntiBotCloudFlare;
import com.github.alessiop86.antiantibotcloudflare.OkHttpAntiAntibotCloudFlareFactory;
import com.github.alessiop86.antiantibotcloudflare.exceptions.AntiAntibotException;
import com.savik.http.exception.DownloadException;
import lombok.extern.log4j.Log4j2;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@Component
public class Downloader {

    public Document download(String url) {
        return download(url, new HashMap<>());
    }

    public Document download(String url, Map<String, String> headers) {
        try {
            return Jsoup.connect(url).headers(headers).get();
        } catch (IOException e) {
            throw new DownloadException(e);
        }
    }

    public Document downloadAntibot(String url) {
        try {
            AntiAntiBotCloudFlare antibot = new OkHttpAntiAntibotCloudFlareFactory().createInstanceNonAndroid();
            String html = antibot.getUrl(url);
            return Jsoup.parse(html);
        } catch (AntiAntibotException e) {
            throw new DownloadException(e);
        }
    }

    public String downloadJson(String url, Map<String, String> headers) {
        try {
            return Jsoup.connect(url).headers(headers).ignoreContentType(true).execute().body();
        } catch (IOException e) {
            throw new DownloadException(e);
        }
    }
}
