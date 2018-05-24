package com.savik.service.bookmaker;

import lombok.AllArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author Savushkin Yauheni
 * @since 13.04.2017
 */
@AllArgsConstructor
@Service
public class Downloader {


    public Document download(String url) {
        try {
            return Jsoup.connect(url).get();
        } catch (IOException e) {
            throw new DownloadException(e);
        }
    }
}
