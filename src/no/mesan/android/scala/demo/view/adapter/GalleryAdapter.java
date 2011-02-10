package no.mesan.android.scala.demo.view.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageView;

public class GalleryAdapter extends BaseAdapter{
	private ArrayList<Drawable> listOfFlickrImg;
	private Context context;
	
	public GalleryAdapter(Context context, ArrayList<Drawable> images){
		this.context = context;
		listOfFlickrImg = images;
	}

	public int getCount() {
		return listOfFlickrImg.size();
	}

	public Object getItem(int position) {
		return listOfFlickrImg.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView iv = new ImageView(context);
		iv.setLayoutParams(new Gallery.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		iv.setScaleType(ImageView.ScaleType.FIT_XY);
		iv.setImageDrawable(listOfFlickrImg.get(position));	
	
		return iv;
	}
}
