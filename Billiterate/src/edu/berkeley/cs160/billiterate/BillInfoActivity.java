package edu.berkeley.cs160.billiterate;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class BillInfoActivity extends Activity {
	
	// information for this screen
	String bill_title;
	
	// get view widgets for modification
	LinearLayout bill_view;
	TextView bill_title_textview;
	//ProgressBar	ratings;
	ImageView ratings;
	ImageButton like;
	ImageButton dislike;
	TextView summary;
	EditText commentBox;
	
	String representative = "";
	
	boolean liked = false;;
	boolean disliked = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bill_info);
		
		Bundle extras = this.getIntent().getExtras();
		bill_title = extras.getString("title");
		
		bill_view = (LinearLayout)findViewById(R.id.bill_view);
		bill_title_textview = (TextView)findViewById(R.id.title);
		//ratings = (ProgressBar)findViewById(R.id.ratings);
		ratings = (ImageView)findViewById(R.id.ratings);
		like = (ImageButton)findViewById(R.id.like);
		dislike = (ImageButton)findViewById(R.id.dislike);
		summary = (TextView)findViewById(R.id.bill_summary);
		commentBox = (EditText)findViewById(R.id.comment);
		
		// display bill information
		bill_title_textview.setText(bill_title);
		
		//getBillSettings(bill_title);
		setSummary(bill_title);
		
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
	
	public void setSummary(String title) {
		if (title.equals("Zoning Amendments to Allow Later Hours of Operation on Telegraph Avenue; BMC Section 23E.56.060")) {
			summary.setText(R.string.late_s);
			representative = "Maxwell Anderson";
		} else if (title.equals("Amending BMC Chapter 9.32 Massage Ordinance: Exempting Chair Massage")) {
			summary.setText(R.string.massage_s);
			representative = "Darryl Moore";
		} else if (title.equals("Formal Bid Solicitation and Request for Proposal Scheduled for Possible Issuance After " +
				"Council Approval on April 2, 2013")) {
			summary.setText(R.string.bid_s);
			representative = "Darryl Moore";
		} else if (title.equals("Contract No. 7722D Amendment: Milliman, Inc. for Actuarial Services")) {
			summary.setText(R.string.milliman_s);
			representative = "Linda Maio";
		} else if (title.equals("Formal Bid Solicitation and Request for Proposal Scheduled for " +
				"Possible Issuance After Council Approval on March 19, 2013")) {
			summary.setText(R.string.solicit_s);
			representative = "Darryl Moore";
		} else if (title.equals("Revenue Contract: County of Alameda for Shelter Plus Care Program")) {
			summary.setText(R.string.shelter_s);
			representative = "Linda Maio";
		} else if (title.equals("Classification: Assistant Human Resources Analyst")) {
			summary.setText(R.string.ahra_s);
			representative = "Maxwell Anderson";
		} else if (title.equals("Lease: Women's Daytime Drop-In Center at 2218 Acton Street")) {
			summary.setText(R.string.womens_s);
			representative = "Linda Maio";
		} else if (title.equals("Lease Amendment: International Computer Science Institute at 1947 Center Street")) {
			summary.setText(R.string.csi_s);
			representative = "Darryl Moore";
		} else if (title.equals("City Council Rules of Procedure and Order")) {
			summary.setText(R.string.rules_s);
			representative = "Maxwell Anderson";
		} else if (title.equals("Formal Bid Solicitation and Request for Proposal Scheduled For Possible Issuance After " +
				"Council Approval on March 5, 2013")) {
			summary.setText(R.string.insure_s);
			representative = "Linda Maio";
		} else if (title.equals("Cleanup Amendment to Taxi Ordinance (BMC Chapter 9.52)")) {
			summary.setText(R.string.taxi_s);
			representative = "Darryl Moore";
		} else if (title.equals("Enact Chapter 13.79, Automatically Renewing Leases")) {
			summary.setText(R.string.renew_s);
			representative = "Maxwell Anderson";
		} else if (title.equals("Option Agreement - 3135 Harper Street")) {
			summary.setText(R.string.harper_s);
			representative = "Linda Maio";
		} else if (title.equals("Appointment of City Clerk")) {
			summary.setText(R.string.clerk_s);
			representative = "Maxwell Anderson";
		} else if (title.equals("Formal Bid Solicitation and Request for Proposal Scheduled For Possible Issuance After " +
				"Council Approval on February 5, 2013")) {
			summary.setText(R.string.feb_s);
			representative = "Darryl Moore";
		} else if (title.equals("Option Agreement - 3135 Harper Street (Revised)")) {
			summary.setText(R.string.harper_rev_s);
			representative = "Linda Maio";
		} else if (title.equals("Contract: ACS State and Local Solutions for a Parking Citation Management Solution")) {
			summary.setText(R.string.acs_s);
			representative = "Maxwell Anderson";
		} else if (title.equals("Contract: AJW Construction for FY 2013 Responsive Sidewalk Project Phase 1")) {
			summary.setText(R.string.ajw_s);
			representative = "Darryl Moore";
		} else if (title.equals("Cleanup Amendment to Taxi Ordinance (BMC Chapter 9.52)")) {
			summary.setText(R.string.cleanup_s);
			representative = "Maxwell Anderson";
		} else if (title.equals("Enact Chapter 13.79, Automatically Renewing Leases")) {
			summary.setText(R.string.enact_s);
			representative = "Maxwell Andersom";
		} else if (title.equals("Contract No. 8460A Amendment: NextGen Healthcare Information " +
				"Systems Inc. for an Electronic Records System and Related Services")) {
			summary.setText(R.string.nextgen_s);
			representative = "Linda Maio";
		} else if (title.equals("Amending Waste Management Authority Joint Powers Agreement")) {
			summary.setText(R.string.waste_s);
			representative = "Maxwell Anderson";
		} else if (title.equals("Fee Assessment - State of California Self-Insurance Fund (Workers' Compensation Program)")) {
			summary.setText(R.string.fee_s);
			representative = "Darryl Moore";
		} else if (title.equals("Designate the Line of Succession for the Director of Emergency Services")) {
			summary.setText(R.string.line_s);
			representative = "Maxwell Anderson";
		} else if (title.equals("Donation of Furniture for the Dona Spring Animal Shelter by Crate and Barrel")) {
			summary.setText(R.string.dana_s);
			representative = "Maxwell Anderson";
		} else if (title.equals("Formal Bid Solicitation and Request for Proposal Scheduled For Possible Issuance After" +
				" Council Approval on January 22, 2013")) {
			summary.setText(R.string.jan_s);
			representative = "Darryl Moore";
		} else if (title.equals("Compassionate Sidewalks Background Information")) {
			summary.setText(R.string.side_s);
			representative = "Linda Maio";
		}
	}
	
	public void likeBill(View v) {
		// TODO
		// update ratings and increment progress bar
		if (liked) {
			like.setBackgroundResource(R.drawable.thumbs_up_blk);
			ratings.setImageResource(R.drawable.rating_bar);
			liked = false;
		} else {
			like.setBackgroundResource(R.drawable.thumbs_up_grn);
			dislike.setBackgroundResource(R.drawable.thumbs_down_blk);
			ratings.setImageResource(R.drawable.rating_bar_like);
			liked = true;
			disliked = false;
		}
	}
	
	public void dislikeBill(View v) {
		// TODO
		// update ratings and decrement progress bar
		if (disliked) {
			dislike.setBackgroundResource(R.drawable.thumbs_down_blk);
			ratings.setImageResource(R.drawable.rating_bar);
			disliked = false;
		} else {
			dislike.setBackgroundResource(R.drawable.thumbs_down_red);
			like.setBackgroundResource(R.drawable.thumbs_up_blk);
			ratings.setImageResource(R.drawable.rating_bar_dislike);
			disliked = true;
			liked = false;
		}
	}
	
	public void getInfo(View v) {
		// TODO
		// open webview with more info on bill
	}
	
	public void contact(View v) {
		// TODO
		// takes user to representative's info screen
		Intent i = new Intent(this, RepresentativeActivity.class);
		i.putExtra("representative", representative);
		startActivity(i);
	}
	
	@SuppressWarnings("deprecation")
	public void postComment(View v) {
		TextView commentText = new TextView(this);
		commentText.setText(commentBox.getText());
		commentText.setLayoutParams(new LayoutParams( LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT ));
		commentText.setBackgroundDrawable(BillInfoActivity.this.getResources().getDrawable(R.drawable.black_border));
		bill_view.addView(commentText);
		
		commentBox.setText("");
	}

}
