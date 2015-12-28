package com.kpodkov;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.util.Collection;
import java.util.Iterator;
import java.sql.*;
import static java.nio.charset.StandardCharsets.*;

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

    public static void main(String[] args) throws IOException, SQLException {
        urlParser urlParser = new urlParser();


        // ===============
        // Send request/get response
        // ===============
        String url = "https://www.reddit.com/r/pics/top";
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(url);
        request.addHeader("User-Agent", "Mozilla/4.0");
        HttpResponse response = client.execute(request);
//        System.out.println("Response Code : " + response.getStatusLine().getStatusCode());


        // ===============
        // Add Response to StringBuffer
        // ===============
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
//            System.out.println(line);
        }

        // ===============
        // Parse Document
        // ===============
        Document doc = Jsoup.parse(result.toString());
        // ===============
        // Number of Posts
        // ===============
        Iterator<Element> eleIterator = doc.select("div[class=score unvoted").iterator();

        int i;
        for (i = 0; eleIterator.hasNext(); ++i) {
            eleIterator.next();
        }
        System.out.println("LENGTH IS: " + i);
        redditPost[] redditPosts = new redditPost[i];
        for (i = 0; i < redditPosts.length; i++) {
            redditPosts[i] = new redditPost("", "", "", "");
        }
        // ===============
        // Get Score
        // ===============
        eleIterator = doc.select("div[class=score unvoted").iterator();
        i = 0;
        while (eleIterator.hasNext()) {
            Element score = eleIterator.next();
            redditPosts[i].setScore(score.text());
            i++;
            //System.out.println(score.text());
        }
        // ===============
        // Get Title / Link
        // ===============
        eleIterator = doc.select("a[class=title may-blank").iterator();
        i = 0;
        while (eleIterator.hasNext()) {
            Element title = eleIterator.next();
            String fileName;
            String fileExt;
            String imageURL;
            String postTitle;
            postTitle = title.text().replaceAll("\"", "");  // Remove Double Quotes (SQL doesnt like)
            imageURL = title.attr("href");
            redditPosts[i].setTitle(postTitle);
            redditPosts[i].setLink(imageURL);
            fileName = urlParser.getFileName(title.attr("href"));
            fileExt  = urlParser.getExtension(title.attr("href"));
            if (fileExt.equals("gifv")) { imageURL = imageURL.replaceAll("gifv","webm"); }
            postTitle = urlParser.cleanTitle(postTitle);
            downloadImage(imageURL,"D:/Development/Java Projects/Java Spring/img-getter/in/" + fileName);
            //System.out.println(title.text());
            //System.out.println(title.attr("href"));
            i++;
        }

        // ===============
        // Get Comment's Link
        // ===============
        eleIterator = doc.select("a").iterator();
        i = 0;
        while (eleIterator.hasNext()) {
            Element commentLink = eleIterator.next();
            //System.out.println(divEle.attr("class"));
            if (commentLink.attr("href").indexOf("/comments/") != -1 && commentLink.attr("href").indexOf("/gilded") == -1) {
                if (i<redditPosts.length) {
                    redditPosts[i].setCommentLink(commentLink.attr("href"));
                }
                i++;
            }
        }

        // ===============
        // SQL Connection
        // ===============
        Connection c;
        Statement stmt = null;
        String sql="";

        try {

            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:worldnews.db");
            c.setAutoCommit(false);

            System.out.println("opened db");

            sql = "DELETE FROM WorldNews;";

            stmt = c.createStatement();
            stmt.executeUpdate(sql);
            c.commit();

            System.out.println("table cleared");

            for (i = 0; i < redditPosts.length; i++) {

                sql = "INSERT INTO WorldNews (Link,Title,Score,CommentLink) VALUES(" +
                        "\"" + redditPosts[i].getLink() + "\"," +
                        "\"" + redditPosts[i].getTitle() + "\"," +
                        "\"" + redditPosts[i].getScore() + "\"," +
                        "\"" + redditPosts[i].getCommentLink() + "\");";
                byte ptext[] = sql.getBytes(UTF_8);
                String sqlQuery = new String(ptext,UTF_8);

                stmt = c.createStatement();
                stmt.executeUpdate(sqlQuery);
                c.commit();
            }
            stmt.close();

            c.close();
        } catch (Exception e) {
            System.out.println("OOPS");
            System.out.println("SQL Query Was:\r\n\r\n" + sql);

            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);

            }
        System.out.println("Records created");




        }

public static void downloadImage (String remotePath, String localPath) {
    // ===============
    // Download Image
    // ===============
    try {
        URL imgUrl = new URL(remotePath);
        InputStream in = new BufferedInputStream(imgUrl.openStream());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int n = 0;
        while (-1!=(n=in.read(buf)))
        {
            out.write(buf, 0, n);
        }
        out.close();
        in.close();
        byte[] imgResponse = out.toByteArray();

        FileOutputStream fos = new FileOutputStream(localPath);
        fos.write(imgResponse);
        fos.close();
    } catch (Exception e) {
        System.out.println("Exception:" + e);
    }

}

    }






