package com.kpodkov;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/*import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;*/

public class Main {

    public static void main(String[] args) throws IOException {

        redditPost curPost=null;

        String url = "https://www.reddit.com/r/worldnews/top/";
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(url);
        // add request header
        request.addHeader("User-Agent", "Mozilla/4.0");
        HttpResponse response = client.execute(request);

        System.out.println("Response Code : " + response.getStatusLine().getStatusCode());

        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
            System.out.println(line);
        }

        Document doc = Jsoup.parse(result.toString());
        Elements tags;

        tags = doc.select("div");
        System.out.println(">>> Start <<<");

        for (Element score : tags) {
                if (score.attr("class").equals("score unvoted")) {
                    curPost.setScore(score.text());
                    System.out.println(score.text());
                }
            }

        tags = doc.select("a");
        System.out.println(">>> Start 2 <<<");

        for (Element link : tags) {
            // ===============
            // Parse Submission Link
            // ===============
            if (link.attr("class").equals("title may-blank ")) {
                curPost.setTitle(link.text()); // Title
                curPost.setLink(link.attr("href")); // Link
                System.out.println(link.text());
                System.out.println(link.attr("href"));
            }
            // ===============
            // Parse Reddit Link
            // ===============
            if (link.attr("href").indexOf("/comments/") != -1) {

                System.out.println("HREF: " + link.attr("href"));
                curPost.setLink(link.attr("href")); // Link
                
            }

        }
        }


    }

