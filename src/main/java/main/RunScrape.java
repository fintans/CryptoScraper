package main;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import services.NewsScraper;
import services.CoinData;
import services.MentionsCounter;
import services.RedditScraper;
import model.Coin;
import model.ScrapedContent;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import dao.DataAccess;

/**
 *
 * @author fintan
 */


// Instructions: 
//simply run this class to perform the majority of the apps functionality
//the program is set to scrape every 30 minutes, and is intended to run 24/7

//you must uncomment some code to utilise "tone" functionality, as outlined below (line 70 to 72 inclusive)

//the other methods which are commented out, were only used to build the inital database
//there is no need to uncomment these

public class RunScrape {

    public static void main(String[] args) throws IOException, ParseException, SQLException, ClassNotFoundException {
        final RedditScraper redditScraper = new RedditScraper();
        final NewsScraper newsScraper = new NewsScraper();
        final MentionsCounter mentionsCount = new MentionsCounter();
        final CoinData getCoinData = new CoinData();
        final DataAccess db = new DataAccess();
        
        //used with SceduledExectorService to perform scrape, coin and table update every 30 min
        
        Runnable helloRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    // scrapes content  
                    List<ScrapedContent> redditContent = redditScraper.scrape();
                    List<ScrapedContent> newsContent = newsScraper.scrape();
                    //gets coin info
                    List<String> apiList = getCoinData.getAllCoins();
                    List<Coin> coinList = getCoinData.coinDataList(apiList);

                    for (Coin coin : coinList) {
                        try {
                            //counts mentions
                            List<String> commentMentions = mentionsCount.counter(redditContent, coin);
                            List<String> newsMentions = mentionsCount.counterNews(newsContent, coin);
                            
//                            The below methods associated with "tone" are left commented out the majority of the time due to IBM API query limits
//                            The current aim is for the admin user to do this manually, once a month  
//                            *NOTE to Examiner* -> Please feel free to uncomment below code to see app in action  (line 70 to 72 inclusive)


                            /*  
                            mentionsCount.tone(commentMentions, coin);
                            mentionsCount.tone(newsMentions, coin);                           
                            db.updateTone(coin); //method updates the tone. This should be un commented when admin user wishes to get more tone data and update db
                            */  

//                            Used to initially fill db with coins
//                            db.insertNewCoin(coin);

//                          adds new row for each coin
                            db.insertNewScrapeInfo(coin);
                            
//                          Below method is used to insert the very first data into the tone table                            
//                            db.insertNewToneInfo(coin);


                        } catch (org.json.JSONException e) {
                        }
                    }

                } catch (IOException ex) {
                    Logger.getLogger(RunScrape.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ParseException ex) {
                    Logger.getLogger(RunScrape.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
    
        };
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(helloRunnable, 0, 30, TimeUnit.MINUTES);
    }
}
