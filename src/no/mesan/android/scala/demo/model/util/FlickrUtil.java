package no.mesan.android.scala.demo.model.util;

import java.util.ArrayList;

import no.mesan.android.scala.demo.R;
import no.mesan.android.scala.demo.model.application.Application;
import no.mesan.android.scala.demo.model.service.FlickrService;
import no.mesan.android.scala.demo.model.service.Request;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * Utility class to abstract communications with Flickr API
 * 
 * @author Thomas
 * 
 */
public class FlickrUtil {

	private Context context;

	public FlickrUtil(Context context) {
		this.context = context;
	}

	/**
	 * Get the top 10 Flickr images by a given keyword. In offline mode, the
	 * returned list will be made up of dummy images
	 * 
	 * @param keyword
	 *            - String
	 * @return ArrayList<Drawable> - list of images
	 */
	public ArrayList<Drawable> getFlickrImagesByKeywordFromWeb(String keyword) {
		ArrayList<Drawable> flickrList = null;

		try {
			flickrList = new ArrayList<Drawable>();

			if (Application.isNetworkAvailable(context)) {

				FlickrService flickrService = new FlickrService(context);
				ArrayList<String> urls = flickrService.getImagesFromFlickr(keyword);
				
				int urlsLength = urls.size();				
				Request request = new Request();
				
				for (int i = 0; i < urlsLength; i++) {
					flickrList.add(request.getImageFromWeb(urls.get(i)));
				}
			} else {
				flickrList.add(context.getResources().getDrawable(R.drawable.dummy_java1));
				flickrList.add(context.getResources().getDrawable(R.drawable.dummy_java2));
				flickrList.add(context.getResources().getDrawable(R.drawable.dummy_java3));
				flickrList.add(context.getResources().getDrawable(R.drawable.dummy_java4));
			}

		} catch (Exception e) {
			Log.d(FlickrUtil.class.getSimpleName(), e.getMessage(), e);
		}

		return flickrList;
	}
}
