package edu.berkeley.cs160.billiterate;

import java.util.ArrayList;

public class Representative {
	private String name;
	private String summary;
	private String phone;
	private String email;
	private String website;
	private String imageUrl;

	public String getName() {
		return name;
	}

	public String getSummary() {
		return summary;
	}

	public String getPhone() {
		return phone;
	}

	public String getEmail() {
		return email;
	}

	public String getWebsite() {
		return website;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	private Representative(String name) {
		this.name = name;
		if (this.name.equals("Linda Maio")) {
			this.summary = "City Council Member representing the first district";
			this.email = "lmaio@cityofberkeley.gov";
			this.website = "http://www.ci.berkeley.ca.us/council1/";
			this.phone = "5109817110";
			this.imageUrl = "http://www.ci.berkeley.ca.us/uploadedImages/Council_1/Level_3_-_General/thumb_Maio_front.jpg";
		} else if (this.name.equals("Tom Bates")) {
			this.summary = "Mayor of Berkeley";
			this.email = "mayor@cityofberkeley.info";
			this.website = "http://www.ci.berkeley.ca.us/mayor/";
			this.phone = "5109817100";
			this.imageUrl = "http://www.ci.berkeley.ca.us/uploadedImages/Mayor/Level_2_-_Department_Master_and_Collections/tom_front_smallest.jpg";
		} else if (this.name.equals("Darryl Moore")) {
			this.summary = "City Council Member representing the second district";
			this.email = "dmoore@cityofberkeley.info";
			this.website = "http://www.ci.berkeley.ca.us/council2/";
			this.phone = "5109817120";
			this.imageUrl = "http://www.ci.berkeley.ca.us/uploadedImages/Council_2/Level_3_-_General/Darryl-Web-Picture2.jpg";
		} else if (this.name.equals("Maxwell Anderson")) {
			this.summary = "City Council Member representing the third district";
			this.email = "manderson@cityofberkeley.info";
			this.website = "http://www.ci.berkeley.ca.us/council3/";
			this.phone = "5109817130";
			this.imageUrl = "http://www.ci.berkeley.ca.us/uploadedImages/Council_3/Level_3_-_General/Anderson.jpg";
		} else {
			this.summary = "Unknown";
			this.email = "nowhere@example.com";
			this.website = "http://www.example.com";
			this.phone = "1234567890";
			this.imageUrl = "http://www.example.com/noimage.jpg";
		}
	}

	public static ArrayList<Representative> getAll() {
		ArrayList<Representative> ret = new ArrayList<Representative>();
		ret.add(new Representative("Tom Bates"));
		ret.add(new Representative("Linda Maio"));
		ret.add(new Representative("Darryl Moore"));
		ret.add(new Representative("Maxwell Anderson"));
		return ret;
	}
}
