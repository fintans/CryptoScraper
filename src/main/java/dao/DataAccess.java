package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import model.Coin;



public class DataAccess {
	
    public void insertNewScrapeInfo(Coin coin) {

        Connection c = null;
        Statement stmt = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:tone.db");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            int id = coin.getId();
            int rank = coin.getRank();
            double price = coin.getPrice();
            double volume = coin.getVolume();
            double marketcap = coin.getMarketCap();
            double percentChange = coin.getPercentChange();
            int mentions = coin.getMentions();
            int newsMentions = coin.getNewsMentions();

            stmt = c.createStatement();

            String sql = "INSERT INTO SCRAPEINFO (ID,RANK,PRICE,VOLUME,MARKETCAP,PERCENTCHANGE,MENTIONS,TIMESTAMP,NEWSMENTIONS) "
                    + "VALUES (" + id + "," + rank + "," + price + "," + volume + "," + marketcap + "," + percentChange + "," + mentions + "," + "(CURRENT_TIMESTAMP)" + "," + newsMentions + ");";
            stmt.executeUpdate(sql);

            stmt.close();
            c.commit();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Records created successfully");
    }

}
