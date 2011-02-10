package no.mesan.android.scala.demo.model.service;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import no.mesan.android.scala.demo.model.application.Application;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * Handles common operations for requesting urls and 
 * images from web
 * 
 * @author Thomas Pettersen
 *
 */
public class Request {

	/**
	 * Send a  GET request for a given url
	 * 
	 * @param url - String
	 * @return HttpResponse - the GET response. null if not found
	 */
	public HttpResponse sendGetRequestForUrl(String url) {
		
		url = url.replaceAll(" ", "+");
		
		// Execute the request
		HttpResponse response = null;
		HttpGet httpget = null;

		try {

			// Prepare a request object
			httpget = new HttpGet(url);

			DefaultHttpClient client = new DefaultHttpClient();

			response = client.execute(httpget);

		} catch (ClientProtocolException cpex) {
			Log.e(Application.class.getSimpleName(), "", cpex);
		} catch (IOException ioex) {
			Log.e(Application.class.getSimpleName(), "", ioex);
		}
		return response;
	}
	
	/**
	 * Send a request for a Image on a given url
	 * 
	 * @param imageUrl - String
	 * @return Drawable - containing the Image. null if not found
	 */
	public Drawable getImageFromWeb(String imageUrl){
		InputStream is = null;
		try {
			URL url = new URL(imageUrl);
			is = new BufferedInputStream(url.openStream());
			return Drawable.createFromStream(is, "src" + (int)Math.random() * 1000);

		} catch (MalformedURLException e) {
			Log.d(Application.class.getSimpleName(), e.getMessage(), e);
		} catch (IOException e) {
			Log.d(Application.class.getSimpleName(), e.getMessage(), e);
		} finally{
			if(is != null) {
				try {
					is.close();
				} catch (IOException e) {
					Log.d(Application.class.getSimpleName(), e.getMessage(), e);
				}
			}
		}
		return null;
	}
}
