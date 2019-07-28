package services;


import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import model.ScrapedContent;

/**
 *
 * @author fintan
 */
//scrapes articles from crypto news website
public class NewsScraper implements IScraper {

    //returns a list of links for each article on the site
    public List<ScrapedContent> scrape() throws IOException, ParseException {

        Document document = Jsoup.connect("https://cryptonews.com/news/").get();
        List<Document> articleLink = new ArrayList<>();
        for (Element article : document.select("div#newsContainer")) {

            Elements links = article.getElementsByTag("a");
            for (Element link : links) {
                String linkHref = link.attr("abs:href");
                Document document1 = Jsoup.connect(linkHref).get();
                articleLink.add(document1);
                String linkText = link.text();
                System.out.println(linkHref);
            }

            List<ScrapedContent> articleContent = new ArrayList<>();
            for (Document document2 : articleLink) {

                String content = document2.select("div.cn-content").html();
                //access time within time div
                String dateStr = document2.select("time").attr("datetime");
                //reformat date to create date object
                SimpleDateFormat formatterTest = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                Date dateTest = formatterTest.parse(dateStr);
                //create new ScrapedContent object
                ScrapedContent scrapedContent = new ScrapedContent(dateTest, content);
                articleContent.add(scrapedContent);        
            }
            return articleContent;
        }
        return null;
    }

}
