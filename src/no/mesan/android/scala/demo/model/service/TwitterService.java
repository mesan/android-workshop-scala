package no.mesan.android.scala.demo.model.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import no.mesan.android.scala.demo.model.application.Application;
import no.mesan.android.scala.demo.model.dto.TweetDTO;
import no.mesan.android.scala.demo.model.dto.TwitterDTO;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

/**
 * Service which searches Twitter for tweets by keyword
 * 
 * @author Thomas Pettersen
 * 
 */
public class TwitterService {

	private static final String TWITTER_SEARCH_URL = "http://search.twitter.com/search.json?result_type=recent&q=";
	private static final String TWITTER_TRENDING_TOPICS = "http://api.twitter.com/1/trends.json";

	private Context context;

	public TwitterService(Context context) {
		this.context = context;
	}

	/**
	 * Search online at Twitter for Tweets by keyword
	 * 
	 * @param keyword
	 *            - String
	 * @return TwitterDTO - Containing the information returned. null if not
	 *         found
	 */
	public TwitterDTO getTweetFromWeb(String keyword) {
		if (Application.isNetworkAvailable(context)) {
			return searchWeb(keyword);
		}

		return searchOffline(keyword);
	}

	private TwitterDTO searchWeb(String keyword) {
		try {
			// Execute the request
			Request request = new Request();
			HttpResponse response = request.sendGetRequestForUrl(TWITTER_SEARCH_URL + keyword);

			StatusLine status = response.getStatusLine();

			if (status.getStatusCode() == 200) {

				return parseTwitterJson(EntityUtils.toString(response.getEntity()), keyword);
			}
		} catch (IOException ioex) {
			Log.e(TwitterService.class.getSimpleName(), ioex.getMessage(), ioex);
		}
		return null;
	}

	private TwitterDTO searchOffline(String keyword) {
		String line = null;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open("dummies/java_twitter.json")), 2048);
			StringBuilder sb = new StringBuilder();
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			return parseTwitterJson(sb.toString(), keyword);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Search online at Twitter for Trending Topics
	 * 
	 * @return ArrayList<String> - List of top ten trending topics
	 */
	public ArrayList<String> searchForTrendingTopics() {

		if (Application.isNetworkAvailable(context)) {
			Request request = new Request();
			HttpResponse response = request.sendGetRequestForUrl(TWITTER_TRENDING_TOPICS);
			StatusLine status = response.getStatusLine();

			if (status.getStatusCode() == 200) {
				try {
					return parseTrendingTopicsJson(EntityUtils.toString(response.getEntity()));
				} catch (IOException ioex) {
					Log.e(TwitterService.class.getSimpleName(), ioex.getMessage(), ioex);
				}
			}
		}

		return null;
	}

	private TwitterDTO parseTwitterJson(String json, String keyword) {
		TwitterDTO twitterDTO = null;

		try {

			twitterDTO = new TwitterDTO();

			JSONObject twitterObject = new JSONObject(json);
			JSONArray resultArray = twitterObject.optJSONArray("results");
			int resultSize = resultArray.length();

			ArrayList<TweetDTO> tweetList = new ArrayList<TweetDTO>();
			TweetDTO tweetDTO = null;
			JSONObject tweet = null;
			DateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US);
			formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

			for (int i = 0; i < resultSize; i++) {
				tweetDTO = new TweetDTO();

				tweet = resultArray.optJSONObject(i);

				tweetDTO.setContent(tweet.optString("text"));
				tweetDTO.setProfileName(tweet.optString("from_user"));

				tweetDTO.setProfileUrl(tweet.optString("profile_image_url"));

				try {
					tweetDTO.setDate((Date) formatter.parse(tweet.optString("created_at")));
				} catch (ParseException pex) {
					Log.i(TwitterService.class.getSimpleName(), "unparseable date", pex);
				}

				tweetList.add(tweetDTO);
			}

			twitterDTO.setKeyword(keyword);
			twitterDTO.setTweets(tweetList);

		} catch (JSONException e) {
			Log.e(TwitterService.class.getSimpleName(), e.getMessage(), e);
		}
		return twitterDTO;
	}

	private ArrayList<String> parseTrendingTopicsJson(String json) {

		try {

			JSONObject trendingTopics = new JSONObject(json);
			JSONArray topicsArray = trendingTopics.optJSONArray("trends");

			int numOfTopics = topicsArray.length();

			ArrayList<String> topicsList = new ArrayList<String>();

			for (int i = 0; i < numOfTopics; i++) {
				JSONObject topic = topicsArray.optJSONObject(i);
				topicsList.add(topic.optString("name"));
			}

			return topicsList;
		} catch (JSONException e) {
			Log.e(TwitterService.class.getSimpleName(), e.getMessage(), e);
		}

		return null;
	}
}
