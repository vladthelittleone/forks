package com.savik.http;

import com.github.alessiop86.antiantibotcloudflare.AntiAntiBotCloudFlare;
import com.github.alessiop86.antiantibotcloudflare.OkHttpAntiAntibotCloudFlareFactory;
import com.github.alessiop86.antiantibotcloudflare.exceptions.AntiAntibotException;
import com.savik.http.exception.DownloadException;
import lombok.extern.log4j.Log4j2;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClients;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@Component
public class HttpClient {
    
    public Document get(String url) {
        return get(url, new HashMap<>());
    } 
    
    public Document get(String url, Map<String, String> headers) {
        try {
            return createConnection(url, headers).get();
        } catch (IOException e) {
            throw new DownloadException(e);
        }
    }    
    
    public Document post(String url, Map<String, String> data) {
        return post(url, data, new HashMap<>());
    }    
    public Document post(String url, Map<String, String> data, Map<String, String> headers) {
        try {
            return createConnection(url, headers).data(data).post();
        } catch (IOException e) {
            throw new DownloadException(e);
        }
    }

    public Document getAntibot(String url) {
        try {
            AntiAntiBotCloudFlare antibot = new OkHttpAntiAntibotCloudFlareFactory().createInstanceNonAndroid();
            String html = antibot.getUrl(url);
            return Jsoup.parse(html);
        } catch (AntiAntibotException e) {
            throw new DownloadException(e);
        }
    }

    public String getJson(String url) {
        return getJson(url, new HashMap<>());
    }

    public String getJson(String url, Map<String, String> headers) {
        try {
            return createConnection(url, headers).ignoreContentType(true).execute().body();
        } catch (IOException e) {
            throw new DownloadException(e);
        }
    }

    public String getPinnacleApacheJson(String url) {
        return getPinnacleApacheJson(url, new HashMap<>());
    }

    public String getPinnacleApacheJson(String url, Map<String, String> headers) {
        try {
            final org.apache.http.client.HttpClient build = HttpClients.custom().build();
            final HttpUriRequest uriRequest = RequestBuilder.get()
                    .setUri(url)
                    .setHeader("Authorization", headers.get("Authorization"))
                    .build();
            final HttpResponse response = build.execute(uriRequest);
            return new BasicResponseHandler().handleResponse(response);

        } catch (IOException e) {
            throw new DownloadException(e);
        }
    }

    private Connection createConnection(String url, Map<String, String> headers) {
        return Jsoup.connect(url).headers(headers);
    }
}
