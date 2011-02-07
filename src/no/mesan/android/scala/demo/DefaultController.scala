package no.mesan.android.scala.demo

import android.app.Activity
import android.os.Bundle
import android.widget.{EditText, Button, ListView}
import android.view.View.OnClickListener
import android.view.{View, KeyEvent}
import android.widget.{ArrayAdapter, AdapterView}
import java.util.ArrayList
import android.content.Context
import _root_.android.content.Intent

class DefaultController extends Activity {
	
	private[this] var txtKeyword : EditText = null
	private[this] var btnSearch : Button = null
	private[this] var lstKeywords : ListView = null
	private[this] var arrAdapter : ArrayAdapter[String] = null
	private[this] var keywords : ArrayList[String] = null
	private[this] var context : Context = null

	override def onCreate(savedInstanceState:Bundle) : Unit = {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)
        context = this
        initComponents()
        initListeners()
    }
	
	def initComponents() = {
		txtKeyword = findViewById(R.id.txtKeyword).asInstanceOf[EditText]
		btnSearch = findViewById(R.id.btnSearch).asInstanceOf[Button]
		lstKeywords = findViewById(R.id.lstKeywords).asInstanceOf[ListView]
		
		keywords = new ArrayList[String];
		arrAdapter = new ArrayAdapter[String](context, android.R.layout.simple_list_item_1, keywords);
		lstKeywords setAdapter(arrAdapter);
	}
	
	def initListeners() = {
		btnSearch setOnClickListener new OnClickListener {
			def onClick(v: View) {
				populateList(txtKeyword.getText.toString)				
			}
		}
		
		txtKeyword setOnKeyListener new View.OnKeyListener {

			def onKey(v: View, keyCode : Int, event : KeyEvent) : Boolean = {
				
				if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction == KeyEvent.ACTION_DOWN) {
					var keyword : String = txtKeyword.getText.toString
					if ("" != keyword) {
						populateList(keyword);
						return true;
					}
				}
				return false;
			}
		}
		
		lstKeywords setOnItemClickListener new AdapterView.OnItemClickListener {
			
			def onItemClick(arg0 : AdapterView[_], view : View , index : Int, id: Long) {
				gotoActivity(keywords.get(index));
			}
		}
	}
	
	def gotoActivity(keyword: String) {
		val intent = new Intent(context, classOf[TweetsController])
		intent putExtra("keyword", keyword)
		startActivity(intent)
	}
	
	def populateList(keyword : String) = {		
		txtKeyword.setText("")
		keywords.add(keyword)
		arrAdapter.notifyDataSetChanged		
		gotoActivity(keyword)
	}
}