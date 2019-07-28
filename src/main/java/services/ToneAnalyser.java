package services;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ibm.watson.developer_cloud.tone_analyzer.v3.ToneAnalyzer;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneAnalysis;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneOptions;

import model.Coin;

public class ToneAnalyser {
	
    public void tone(List<String> mentionComment, Coin coin) throws NullPointerException {
        // tone variables
        int fear = coin.getFear();
        int sadness = coin.getSadness();
        int anger = coin.getAnger();
        int tentative = coin.getTentative();
        int analytical = coin.getAnalytical();
        int confident = coin.getConfident();
        int joy = coin.getJoy();

        ToneAnalyzer service = new ToneAnalyzer("2017-09-21");
        service.setUsernameAndPassword("9626125d-8d1f-46d5-b0e1-8f508cff679f", "psVv2e4LU8nN");
        // analysing low number of comments due to IBM api query limit
        
        for (String string : mentionComment) {
            System.out.println(string);
            
            try {
                ToneOptions toneOptions = new ToneOptions.Builder()
                        //analyse whole string at once, not by sentence
                        .html(string).sentences(Boolean.FALSE)
                        .build();
                ToneAnalysis tone = service.tone(toneOptions).execute();
                System.out.println(tone.getDocumentTone());
                // tone analysis to JSON object
                JSONObject obj = new JSONObject(tone.getDocumentTone());
                JSONArray arr = obj.getJSONArray("tones");
                JSONObject jsonObj = arr.getJSONObject(0);
                if (jsonObj.toString().contains("sadness")) {
                    sadness++;
                } else if (jsonObj.toString().contains("fear")) {
                    fear++;
                } else if (jsonObj.toString().contains("sadness")) {
                    sadness++;
                } else if (jsonObj.toString().contains("anger")) {
                    anger++;
                } else if (jsonObj.toString().contains("tentative")) {
                    tentative++;
                } else if (jsonObj.toString().contains("analytical")) {
                    analytical++;
                } else if (jsonObj.toString().contains("confident")) {
                    confident++;
                } else if (jsonObj.toString().contains("joy")) {
                    joy++;
                }
            } catch (JSONException je) {
            }
        }
        
        coin.setFear(fear);
        coin.setSadness(sadness);
        coin.setAnger(anger);
        coin.setTentative(tentative);
        coin.setAnalytical(analytical);
        coin.setConfident(confident);
        coin.setJoy(joy);
    }

}
