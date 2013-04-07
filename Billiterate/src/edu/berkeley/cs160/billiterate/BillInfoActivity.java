package edu.berkeley.cs160.billiterate;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class BillInfoActivity extends Activity {
	
	// information for this screen
	String bill_title;
	
	// get view widgets for modification
	LinearLayout bill_view;
	TextView bill_title_textview;
	ProgressBar	ratings;
	ImageButton like;
	ImageButton dislike;
	TextView summary;
	EditText comment;
	
	boolean liked;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bill_info);
		
		Bundle extras = this.getIntent().getExtras();
		bill_title = extras.getString("title");
		
		bill_view = (LinearLayout)findViewById(R.id.bill_view);
		bill_title_textview = (TextView)findViewById(R.id.title);
		ratings = (ProgressBar)findViewById(R.id.ratings);
		like = (ImageButton)findViewById(R.id.like);
		dislike = (ImageButton)findViewById(R.id.dislike);
		summary = (TextView)findViewById(R.id.bill_summary);
		comment = (EditText)findViewById(R.id.comment);
		
		// display bill information
		bill_title_textview.setText(bill_title);
		
		getBillSettings(bill_title);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.bill_info, menu);
		return true;
	}
	
	public void getBillSettings(String title) {
		// TODO
		// get bill information and display to screen
	}
	
	public void likeBill(View v) {
		// TODO
		// update ratings and increment progress bar
		like.setBackgroundResource(R.drawable.thumbs_up_grn);
	}
	
	public void dislikeBill(View v) {
		// TODO
		// update ratings and decrement progress bar
		dislike.setBackgroundResource(R.drawable.thumbs_down_red);
	}
	
	public void getInfo(View v) {
		// TODO
		// open webview with more info on bill
	}
	
	public void contact(View v) {
		// TODO
		// takes user to representative's info screen
	}

}
