package edu.berkeley.cs160.billiterate;

import android.app.Activity;
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
			summary.setText("Berkeley Municipal Code Section 23E.56.060 of the Zoning Ordinance to extend the allowable hours " +
					"of operation for businesses in the Commercial Telegraph (C-T) District to include 24/7 hours north of " +
					"Dwight Way and a required closing time of 12:00 a.m. south of Dwight Way.");
			representative = "Eric Angstadt";
		} else if (title.equals("Amending BMC Chapter 9.32 Massage Ordinance: Exempting Chair Massage")) {
			summary.setText("Adopt first reading of an Ordinance amending Sections 9.32.020 and 9.32.060 of the Berkeley " +
					"Municipal Code to exempt chair massage from the requirement for a massage establishment permit.");
			representative = "Zach Cowan";
		} else if (title.equals("Formal Bid Solicitation and Request for Proposal Scheduled for Possible Issuance After " +
				"Council Approval on April 2, 2013")) {
			summary.setText("Approve the request for proposals or invitation for bids (attached to staff report) that " +
					"will be, or are planned to be, issued upon final approval by the requesting department or division. " +
					" All contracts over the City Manager's threshold will be returned to Council for final approval.");
			representative = "Robert Hicks";
		} else if (title.equals("Contract No. 7722D Amendment: Milliman, Inc. for Actuarial Services")) {
			summary.setText("Adopt a Resolution authorizing the City Manager to amend the existing Contract " +
					"No. 7722D with Milliman, Inc., (hereinafter \"Milliman\") by increasing expenditure authority " +
					"in an amount not to exceed $120,000, for a total contract amount of $369,999 through June 30, " +
					"2015 for actuarial services pertaining to retiree medical benefit plans.");
			representative = "David Abel";
		} else if (title.equals("Formal Bid Solicitation and Request for Proposal Scheduled for " +
				"Possible Issuance After Council Approval on March 19, 2013")) {
			summary.setText("Approve the request for proposals or invitation for bids (attached " +
					"to staff report) that will be, or are planned to be, issued upon final approval " +
					"by the requesting department or division.  All contracts over the City Manager's " +
					"threshold will be returned to Council for final approval.");
			representative = "Robert Hicks";
		} else if (title.equals("Revenue Contract: County of Alameda for Shelter Plus Care Program")) {
			summary.setText("Adopt a Resolution authorizing the City Manager to execute any resultant " +
					"agreements or amendments with the County of Alameda for Shelter Plus Care in an " +
					"amount not to exceed $241,057 to provide tenant-based rental assistance for people " +
					"living with HIV/AIDS for the 12-month period of March 1, 2013 through February 28, 2014.");
			representative = "Jane Micallef";
		} else if (title.equals("Classification: Assistant Human Resources Analyst")) {
			summary.setText("Adopt a Resolution amending Resolution No. 65,376-N.S., Classification and Salary " +
					"Resolution for Unrepresented Employees, to establish the classification of Assistant Human " +
					"Resources Analyst with a monthly salary range of $5,590 to $6,800 effective March 20, 2013.");
			representative = "David Abel";
		} else if (title.equals("Lease: Women's Daytime Drop-In Center at 2218 Acton Street")) {
			summary.setText("Adopt second reading of Ordinance No. 7,280–N.S. authorizing the City Manager to " +
					"execute a five-year lease agreement of 2218 Acton Street to the Women's Daytime Drop-In " +
					"Center (WDDC) for use as a drop-in center and social service site for homeless women and families.");
			representative = "Jane Micallef";
		} else if (title.equals("Lease Amendment: International Computer Science Institute at 1947 Center Street")) {
			summary.setText("Adopt second reading of Ordinance No. 7,281–N.S. authorizing the City Manager to execute " +
					"an amendment to the City’s lease (Contract No. R7593) with International Computer Science Institute " +
					"to provide for an adjustment in rent for the first five-year option period.");
			representative = "Robert Hicks";
		} else if (title.equals("City Council Rules of Procedure and Order")) {
			summary.setText("Adopt a Resolution revising the City Council Rules of Procedure and Order to clarify " +
					"the procedures for public comment relating to appeals on Council agendas and rescinding Resolution " +
					"No. 65,910-N.S.");
			representative = "Eric Angstadt";
		} else if (title.equals("Formal Bid Solicitation and Request for Proposal Scheduled For Possible Issuance After " +
				"Council Approval on March 5, 2013")) {
			summary.setText("Approve the request for proposals or invitation for bids (attached to staff report) that will " +
					"be, or are planned to be, issued upon final approval by the requesting department or division.  All " +
					"contracts over the City Manager's threshold will be returned to Council for final approval.");
			representative = "Robert Hicks";
		} else if (title.equals("Cleanup Amendment to Taxi Ordinance (BMC Chapter 9.52)")) {
			summary.setText("Adopt second reading of Ordinance No. 7,277–N.S. amending Berkeley Municipal Code Section " +
					"9.52.050.B.1.f to correct an inadvertent omission.");
			representative = "Zach Cowan";
		} else if (title.equals("Enact Chapter 13.79, Automatically Renewing Leases")) {
			summary.setText("Adopt second reading of Ordinance No. 7,278–N.S. enacting Berkeley Municipal Code Chapter " +
					"13.79 Automatically Renewing Leases Ordinance.");
			representative = "Zach Cowan";
		} else if (title.equals("Option Agreement - 3135 Harper Street")) {
			summary.setText("Adopt second reading of Ordinance No. 7,279–N.S. approving an Option Agreement between the " +
					"City of Berkeley and Satellite AHA Development, Inc. (SAHAD) for the disposition and development of " +
					"the City-owned property at 3135 Harper Street in Berkeley.");
			representative = "Jane Micallef";
		} else if (title.equals("Appointment of City Clerk")) {
			summary.setText("Adopt a Resolution confirming the appointment of Mark Numainville as the City Clerk to be " +
					"effective March 3, 2013.");
			representative = "Eric Angstadt";
		} else if (title.equals("Formal Bid Solicitation and Request for Proposal Scheduled For Possible Issuance After " +
				"Council Approval on February 5, 2013")) {
			summary.setText("Approve the request for proposals or invitation for bids (attached to staff report) that will " +
					"be, or are planned to be, issued upon final approval by the requesting department or division.  All " +
					"contracts over the City Manager's threshold will be returned to Council for final approval.");
			representative = "Robert Hicks";
		} else if (title.equals("Option Agreement - 3135 Harper Street (Revised)")) {
			summary.setText("Adopt first reading of an Ordinance approving an Option Agreement between the City of Berkeley " +
					"and Satellite AHA Development, Inc. (SAHAD) for the disposition and development of the City-owned " +
					"property at 3135 Harper Street in Berkeley.");
			representative = "Jane Micallef";
		} else if (title.equals("Contract: ACS State and Local Solutions for a Parking Citation Management Solution")) {
			summary.setText("Adopt a Resolution authorizing the City Manager to execute a contract and any amendments with " +
					"ACS State and Local Solutions to provide Parking Citation Management Services for a total amount not to " +
					"exceed $1,440,000 from April 1, 2013 to June 30, 2016.");
			representative = "Zach Cowan";
		} else if (title.equals("Contract: AJW Construction for FY 2013 Responsive Sidewalk Project Phase 1")) {
			summary.setText("Adopt a Resolution: 1. Approving specifications for the FY 2013 Responsive Sidewalk " +
					"Project Phase 1, Specification No. 13-10702-C; 2. Accepting the bid of AJW Construction; and " +
					"3. Authorizing the City Manager to execute a contract and any amendments, extensions, or other " +
					"change orders until completion of the project in accordance with the approved specifications, in " +
					"an amount not to exceed $248,100, for the period of 120 calendar days from the date of the " +
					"execution of the contract.");
			representative = "Robert Hicks";
		} else if (title.equals("Cleanup Amendment to Taxi Ordinance (BMC Chapter 9.52)")) {
			summary.setText("Adopt first reading of an Ordinance amending Berkeley Municipal " +
					"Code Section 9.52.050.B.1.f to correct an inadvertent omission.");
			representative = "Zach Cowan";
		} else if (title.equals("Enact Chapter 13.79, Automatically Renewing Leases")) {
			summary.setText("Adopt first reading of an Ordinance enacting Berkeley Municipal" +
					" Code Chapter 13.79 Automatically Renewing Leases Ordinance.");
			representative = "Zach Cowan";
		} else if (title.equals("Contract No. 8460A Amendment: NextGen Healthcare Information " +
				"Systems Inc. for an Electronic Records System and Related Services")) {
			summary.setText("Adopt a Resolution authorizing the City Manager to amend Contract " +
					"No. 8460A with NextGen Healthcare Information Systems, Inc. increasing the " +
					"amount by $105,897, for a total amount not to exceed $430,094, and extending " +
					"the term to September 30, 2010 through June 30, 2016.");
			representative = "Jane Micallef";
		} else if (title.equals("Amending Waste Management Authority Joint Powers Agreement")) {
			summary.setText("Adopt a Resolution approving an amendment to the Alameda County Waste" +
					" Management Authority Joint Powers Agreement to clarify rules in case of member " +
					"agency rule conflict.");
			representative = "Eric Angstadt";
		} else if (title.equals("Fee Assessment - State of California Self-Insurance Fund (Workers' Compensation Program)")) {
			summary.setText("Ratifying the action taken by the City Manager during recess authorizing payment to the State of " +
					"California Department of Industrial Relations for Fiscal Year 2013 for administering the Workers' Compensation " +
					"Program, in an amount not to exceed $158,194, which is $19,895 greater than the original preliminary fee authorized " +
					"by Council on November 13, 2012.");
			representative = "David Abel";
		} else if (title.equals("Designate the Line of Succession for the Director of Emergency Services")) {
			summary.setText("Adopt a Resolution approving the designated line of succession to the position of Director of Emergency Services" +
					" in the event of an officially declared disaster, and rescinding Resolution No. 65,510-N.S.");
			representative = "Eric Angstadt";
		} else if (title.equals("Donation of Furniture for the Dona Spring Animal Shelter by Crate and Barrel")) {
			summary.setText("Adopt a Resolution authorizing the City Manager to accept the donation of furniture for " +
					"the Dona Spring Animal Shelter from Crate and Barrel.");
			representative = "Robert Hicks";
		} else if (title.equals("Formal Bid Solicitation and Request for Proposal Scheduled For Possible Issuance After" +
				" Council Approval on January 22, 2013")) {
			summary.setText("Approve the request for proposals or invitation for bids (attached to staff report) that will be, or are planned " +
					"to be, issued upon final approval by the requesting department or division.  All contracts over the City Manager's threshold " +
					"will be returned to Council for final approval.");
			representative = "Robert Hicks";
		} else if (title.equals("Compassionate Sidewalks Background Information")) {
			summary.setText("The count combines statistical analysis of people staying in emergency shelter and " +
					"transitional housing with surveys of people using various daytime service sites include " +
					"drop-in centers, meal programs, and food pantries.");
			representative = "Jane Micallef";
		}
	}
	
	public void likeBill(View v) {
		// TODO
		// update ratings and increment progress bar
		if (liked) {
			like.setBackgroundResource(R.drawable.thumbs_up_blk);
			ratings.setBackgroundResource(R.drawable.rating_bar);
			liked = false;
		} else {
			like.setBackgroundResource(R.drawable.thumbs_up_grn);
			dislike.setBackgroundResource(R.drawable.thumbs_down_blk);
			ratings.setBackgroundResource(R.drawable.rating_bar_like);
			liked = true;
		}
	}
	
	public void dislikeBill(View v) {
		// TODO
		// update ratings and decrement progress bar
		if (disliked) {
			dislike.setBackgroundResource(R.drawable.thumbs_down_blk);
			ratings.setBackgroundResource(R.drawable.rating_bar);
			disliked = false;
		} else {
			dislike.setBackgroundResource(R.drawable.thumbs_down_red);
			like.setBackgroundResource(R.drawable.thumbs_up_blk);
			ratings.setBackgroundResource(R.drawable.rating_bar_dislike);
			disliked = true;
		}
	}
	
	public void getInfo(View v) {
		// TODO
		// open webview with more info on bill
	}
	
	public void contact(View v) {
		// TODO
		// takes user to representative's info screen
		//Intent i = new Intent(this, ReprActivity.class);
		//i.putExtra("representative", representative);
		//startActivity(i);
	}
	
	public void postComment(View v) {
		TextView commentText = new TextView(this);
		commentText.setText(commentBox.getText());
		commentText.setLayoutParams(new LayoutParams( LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT ));
		bill_view.addView(commentText);
	}

}
