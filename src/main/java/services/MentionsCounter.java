package services;

import model.Coin;
import model.ScrapedContent;

import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author fintan
 */
// https://stackoverflow.com/questions/40209550/reading-a-json-array-in-java

public class MentionsCounter {

	Coin coin = new Coin();
	CoinData coinData = new CoinData();

	// searches each ScrapedContent.getComment() for matches
	public List<String> counter(List<ScrapedContent> comments, Coin coin) throws NullPointerException {
		// array list to store comments that contain coin mentions, for tone analysis
		List<String> mentionComment = new ArrayList<>();
		int count = 0;
		for (ScrapedContent comment : comments.subList(1, comments.size() - 1)) {
			String commentString = comment.getComment();

			if (commentString == null) {
				commentString = "will this work";
			}
			if (commentString.matches("(?i).*\\b" + coin.getName() + "\\b.*")
					|| commentString.matches("(?i).*\\b" + coin.getSymbol() + "\\b.*")) {
				count++;
				mentionComment.add(commentString);
			}
		} // sets mentions
		coin.setMentions(count);
		return mentionComment;
	}

	// counts mentions from news contents, sets news mentions
	public List<String> counterNews(List<ScrapedContent> comments, Coin coin) {
		List<String> mentionArticle = new ArrayList<>();
		int count = 0;
		try {
			for (ScrapedContent comment : comments.subList(0, comments.size() - 1)) {
				String commentString = comment.getComment();

				if (commentString == null) {
					commentString = "will this work";
				}
				String a[] = commentString.split(" ");
				for (int j = 0; j < a.length; j++) {
					if (coin.getName().equalsIgnoreCase(a[j]) || coin.getSymbol().equalsIgnoreCase(a[j])) {
						count++;
						mentionArticle.add(commentString);
						break;
					}
				}
			}
		} catch (NullPointerException e) {
		}

		coin.setNewsMentions(count);
		return mentionArticle;
	}

}
