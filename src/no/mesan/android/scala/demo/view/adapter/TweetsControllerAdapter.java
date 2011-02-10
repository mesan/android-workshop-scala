package no.mesan.android.scala.demo.view.adapter;

import java.util.ArrayList;

import no.mesan.android.scala.demo.R;
import no.mesan.android.scala.demo.model.application.Application;
import no.mesan.android.scala.demo.model.dto.TweetDTO;
import no.mesan.android.scala.demo.model.service.Request;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TweetsControllerAdapter extends BaseAdapter {
	ArrayList<TweetDTO> listOfTweetDTO;
	Context context;
	private LayoutInflater layoutInflater;
	private TweetDTO tweetDTO;

	public TweetsControllerAdapter(Context context, ArrayList<TweetDTO> listOfTweetDTO) {
		this.context = context;
		this.listOfTweetDTO = listOfTweetDTO;
		layoutInflater = LayoutInflater.from(context);
	}

	public int getCount() {
		return listOfTweetDTO.size();
	}

	public Object getItem(int position) {
		return listOfTweetDTO.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int index, View tweetListItemView, ViewGroup parent) {
		ViewHolder holder;

		if (tweetListItemView == null) {
			tweetListItemView = layoutInflater.inflate(R.layout.tweets_controller_list_item, parent, false);

			holder = new ViewHolder();
			holder.txtProfileName = (TextView) tweetListItemView.findViewById(R.id.txtProfileName);
			holder.txtTweetText = (TextView) tweetListItemView.findViewById(R.id.txtTweetText);
			holder.txtTweetDate = (TextView) tweetListItemView.findViewById(R.id.txtTweetDate);
			holder.imgProfileImage = (ImageView) tweetListItemView.findViewById(R.id.imgProfileImage);

			tweetListItemView.setTag(holder);
		} else {
			holder = (ViewHolder) tweetListItemView.getTag();
		}

		tweetDTO = listOfTweetDTO.get(index);

		// la stå
		if (index % 2 != 0) {
			tweetListItemView.setBackgroundResource(R.drawable.tweets_gradient_list_element_darker);
		} else {
			tweetListItemView.setBackgroundResource(R.drawable.tweets_gradient_list_element);
		}

		// fjernes. Teksten skal settes i oppgaven
		holder.txtProfileName.setText(tweetDTO.getProfileName());
		holder.txtTweetText.setText(tweetDTO.getContent());
		holder.txtTweetDate.setText(Application.formatDateToTimeDiff(tweetDTO.getDate()));

		// la stå
		if (!tweetDTO.hasImage() && Application.isNetworkAvailable(context)) {
			tweetDTO.setImgProfile(context.getResources().getDrawable(R.drawable.twiter_01));

			new ImageFromWebTask(tweetListItemView, index).execute(tweetDTO.getProfileUrl());
			// Request request = new Request();
			// request.getImageFromWeb(tweetDTO.getProfileUrl());
		}

		holder.imgProfileImage.setImageDrawable(tweetDTO.getImgProfile());

		return tweetListItemView;
	}

	static class ViewHolder {
		TextView txtProfileName, txtTweetText, txtTweetDate;
		ImageView imgProfileImage;
	}

	private class ImageFromWebTask extends AsyncTask<String, Void, Drawable> {
		private int index;

		public ImageFromWebTask(View tweetListItemView, int index) {
			this.index = index;
		}

		@Override
		protected Drawable doInBackground(String... params) {
			Request request = new Request();
			return request.getImageFromWeb(params[0]);
		}

		@Override
		protected void onPostExecute(Drawable image) {

			if (image != null) {
				TweetDTO tweetDTO = listOfTweetDTO.get(index);
				tweetDTO.setImgProfile(image);
			}
			
			// Update rows
			notifyDataSetChanged();
		}
	}
}
