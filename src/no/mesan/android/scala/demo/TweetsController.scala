package no.mesan.android.scala.demo

import android.app.Activity
import android.os.Bundle
import android.widget.TextView

class TweetsController extends Activity {
	
	private[this] var lblKeyword : TextView = null
	
	override def onCreate(savedInstanceState:Bundle) : Unit = {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tweets_controller)
        initLayout
        renderView
    }
	
	def initLayout() = {
		lblKeyword = findViewById(R.id.lblKeyword).asInstanceOf[TextView]
	}
	
	def renderView() = {
		lblKeyword setText(getIntent().getStringExtra("keyword"))
	}
}