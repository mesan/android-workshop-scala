package no.mesan.android.scala.demo

import android.app.Activity
import android.os.Bundle
import android.widget.{TextView, ListView}
import android.content.Context
import no.mesan.android.scala.demo.model.dto.{TwitterDTO,TweetDTO}
import no.mesan.android.scala.demo.model.util.TwitterUtil
import no.mesan.android.scala.demo.view.adapter.TweetsControllerAdapter
import java.util.ArrayList
import android.widget.AdapterView.OnItemClickListener
import android.widget.AdapterView
import android.view.View
import android.net.Uri
import android.content.Intent
import android.os.AsyncTask
import android.app.ProgressDialog

class TweetsController extends Activity {
	
	private[this] var lstTweets : ListView = null
	private[this] var twitterDTO : TwitterDTO = null
	private[this] var tweetList : ArrayList[TweetDTO] = null
	private[this] final var URL : String = null
	private[this] var twitterUtil : TwitterUtil = null
	private[this] var tweetsControllerAdapter : TweetsControllerAdapter = null
	private[this] var context : Context = null
	
	override def onCreate(savedInstanceState:Bundle) : Unit = {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tweets_controller)
        context = this
        initLayout
        renderView
        initListeners
    }
	
	def initLayout() = {
		lstTweets = findViewById(R.id.lstTweets).asInstanceOf[ListView]
		twitterUtil = new TwitterUtil(context)
		tweetList = new ArrayList[TweetDTO]
	}
	
	def renderView() = {		
		// Get tweets
		// Due to bug in the Scala Language v 2.8.1, we will run into an AbstractMethodError when invoking SearchForNewTweetsTasks.
		// Read more here: http://lampsvn.epfl.ch/trac/scala/ticket/1459
		// new SearchForNewTweetsTask(context).execute(getIntent().getStringExtra("keyword"));
		
		val twitterDTO = twitterUtil.getTwitterDTO(getIntent().getStringExtra("keyword"), true)
		tweetList = twitterDTO.getTweets();
		tweetsControllerAdapter = new TweetsControllerAdapter(context, tweetList);
		lstTweets.setAdapter(tweetsControllerAdapter);
	}
	
	def initListeners() = {
		lstTweets setOnItemClickListener new OnItemClickListener() {
			
			def onItemClick(arg0 : AdapterView[_], view : View , index : Int, id: Long) {
				val uri = Uri.parse(URL + tweetList.get(index).getProfileName);
				val intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
			}
		}
	}
	
	class SearchForNewTweetsTask(var context: Context) extends AsyncTask[String, Void, Boolean] {
		
		private[this] var progress : ProgressDialog = null
		
		override def onPreExecute() = {
			progress = ProgressDialog.show(context, "Kontakter Twitter", "søker etter tweets", true, true);
			super.onPreExecute();
		}
		
		def doInBackground(params : String*) : Boolean = {
			val twitterDTO = twitterUtil.getTwitterDTO(params(0), true)
			if (twitterDTO != null) {
				return true;
			}
			return false;
		}
		

		override def onPostExecute(searchSuccess : Boolean) = {
			if (searchSuccess) {
				tweetList = twitterDTO.getTweets();
				tweetsControllerAdapter = new TweetsControllerAdapter(context, tweetList);
				lstTweets.setAdapter(tweetsControllerAdapter);
			}

			progress.dismiss();

			super.onPostExecute(searchSuccess);
		}
	}
}