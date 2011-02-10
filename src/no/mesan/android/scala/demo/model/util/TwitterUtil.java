package no.mesan.android.scala.demo.model.util;

import java.util.ArrayList;

import no.mesan.android.scala.demo.model.dto.TwitterDTO;
import no.mesan.android.scala.demo.model.service.TwitterService;
import android.content.Context;

/**
 * Utility class to abstract communications with Twitter API and the DB
 * 
 * @author Thomas
 * 
 */
public class TwitterUtil {

	private Context context;

	public TwitterUtil(Context context) {
		this.context = context;
	}

	/**
	 * Use Twitter Search API to get 15 latest tweets regarding given keyword
	 * 
	 * @param keyword
	 *            - String
	 * @param searchWeb
	 *            - searches the web if true, uses only persisted values if
	 *            false
	 * @return TwitterDTO - containing all information about tweets from Twitter
	 */
	public TwitterDTO getTwitterDTO(String keyword, boolean searchWeb) {

		if (searchWeb) {
			TwitterService twitterService = new TwitterService(context);
			return twitterService.getTweetFromWeb(keyword);
		}

		return null;
	}

	/**
	 * Use Twitter Trending Topics API to get the top 10 topics at the moment
	 * 
	 * @return ArrayList<String> - containing all trending topics, max 10
	 */
	public ArrayList<String> getTrendingTopics() {
		TwitterService twitterService = new TwitterService(context);
		return twitterService.searchForTrendingTopics();
	}
}