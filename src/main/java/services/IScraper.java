package services;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import model.ScrapedContent;

public interface IScraper {
	
	public List<ScrapedContent> scrape() throws IOException, ParseException;

}
