package no.mesan.android.scala.demo.model.service;

import java.io.IOException;
import java.util.ArrayList;

import no.mesan.android.scala.demo.model.application.Application;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.StatusLine;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

/**
 * Service which searches the Flickr DB for images by keyword
 * 
 * @author Thomas Pettersen
 *
 */
public class FlickrService {
	
	private Context context;
	private static final String IMAGE_SIZE = "url_m";
	private static final String FLICKR_SEARCH_API_URL = "http://api.flickr.com/services/rest/?method=flickr.photos.search&nojsoncallback=1&format=json&api_key=6b3b39e81d8f4b5f527250506e146d4b&sort=interestingness-asc&extras=" + IMAGE_SIZE + "&per_page=10&tags=";
	
	public FlickrService(Context context){
		this.context = context;
	}

	/**
	 * Search online at Flickr for images
	 * 
	 * @param keyword
	 * @return ArrayList<String> - List of urls
	 */
	public ArrayList<String> getImagesFromFlickr(String keyword) {
		
		if(Application.isNetworkAvailable(context)){
			
			// Execute the request
			Request request = new Request();
			HttpResponse response = request.sendGetRequestForUrl(FLICKR_SEARCH_API_URL + keyword);

			StatusLine status = response.getStatusLine();

			if (status.getStatusCode() == 200) {
				try {
					return parseJson(EntityUtils.toString(response.getEntity()),
							keyword);
				} catch (ParseException pex) {
					Log.e(TwitterService.class.getSimpleName(), pex.getMessage(),
							pex);
				} catch (IOException ioex) {
					Log.e(TwitterService.class.getSimpleName(), ioex.getMessage(),
							ioex);
				}
			}
		}

		return null;
	}

	private ArrayList<String> parseJson(String json, String keyword) {
		ArrayList<String> urlList = new ArrayList<String>();

		try {

			JSONObject shipmentObject = new JSONObject(json);
			JSONObject photos = shipmentObject.optJSONObject("photos");
			JSONArray photoArray = photos.optJSONArray("photo");

			int photoArrSize = photoArray.length();

			JSONObject photo = null;

			for (int i = 0; i < photoArrSize; i++) {
				photo = photoArray.optJSONObject(i);

				urlList.add(photo.optString(IMAGE_SIZE).replaceAll("\\\\", ""));
			}

		} catch (JSONException e) {
			Log.e(TwitterService.class.getSimpleName(), e.getMessage());
		}
		return urlList;
	}
}
