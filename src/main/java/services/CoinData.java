package services;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import model.Coin;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author fintan
 */
public class CoinData {

    public Client client;
    public int port = 49000;

    public CoinData() {
        client = Client.create();
    }

    // get 5 x 100 JSON responses, stored in String list
    public List<String> getAllCoins() {
        List<String> apiList = new ArrayList<>();
        //maximum number of results per call is 100 for this site
        String firstHundred = "https://api.coinmarketcap.com/v2/ticker/?start=1&limit=100";
        WebResource target = client.resource(firstHundred);
        ClientResponse response = target.get(ClientResponse.class);
        String result = response.getEntity(String.class);

        apiList.add(result);

        String secondHundred = "https://api.coinmarketcap.com/v2/ticker/?start=101&limit=100";
        WebResource targetTwo = client.resource(secondHundred);
        ClientResponse responseTwo = targetTwo.get(ClientResponse.class);
        String resultTwo = responseTwo.getEntity(String.class);
        apiList.add(resultTwo);

        String thirdHundred = "https://api.coinmarketcap.com/v2/ticker/?start=202&limit=100";
        WebResource targetThree = client.resource(thirdHundred);
        ClientResponse responseThree = targetThree.get(ClientResponse.class);
        String resultThree = responseThree.getEntity(String.class);
        apiList.add(resultThree);

        String fourthHundred = "https://api.coinmarketcap.com/v2/ticker/?start=303&limit=100";
        WebResource targetFour = client.resource(fourthHundred);
        ClientResponse responseFour = targetFour.get(ClientResponse.class);
        String resultFour = responseFour.getEntity(String.class);
        apiList.add(resultFour);

        String fifthHundred = "https://api.coinmarketcap.com/v2/ticker/?start=404&limit=100";
        WebResource targetFive = client.resource(fifthHundred);
        ClientResponse responseFive = targetFive.get(ClientResponse.class);
        String resultFive = responseFive.getEntity(String.class);
        apiList.add(resultFive);

        return apiList;

    }

    //parse the above JSON to get variables needed to create coin object
    public List<Coin> coinDataList(List<String> api) {
        List<Coin> coinData = new ArrayList<>();
        for (String string : api) {

            //Create the JSONObject from the string returned from the API query
            JSONObject json = new JSONObject(string);
            JSONObject data = json.getJSONObject("data");
            //create json array which is upulated with key values

            JSONArray dataNo = data.names();
            System.out.println(dataNo);

            //list to store each coin as a class
            for (Object object : dataNo) {
                //object.to string returns key value e.g. 2044
                int id = data.getJSONObject(object.toString()).getInt("id");
                String name = data.getJSONObject(object.toString()).getString("name");
                String symbol = data.getJSONObject(object.toString()).getString("symbol");
                int rank = data.getJSONObject(object.toString()).getInt("rank");
                double price = data.getJSONObject(object.toString()).getJSONObject("quotes").getJSONObject("USD").getDouble("price");
                double volume = data.getJSONObject(object.toString()).getJSONObject("quotes").getJSONObject("USD").getDouble("volume_24h");
                double marketCap = data.getJSONObject(object.toString()).getJSONObject("quotes").getJSONObject("USD").getDouble("market_cap");
                double percentChange;
                // on 23/07/2018 the program was interupted by a JSONException error, claiming that percentChange is not a number.
                try {
                    percentChange = data.getJSONObject(object.toString()).getJSONObject("quotes").getJSONObject("USD").getDouble("percent_change_24h");
                } catch (JSONException e) {
                    percentChange = 0;
                }

                Coin allCoins = new Coin(id, name, symbol, rank, price, volume, marketCap, percentChange);
                coinData.add(allCoins);
            }
        }
        return coinData;
    }

}
