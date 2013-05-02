package edu.berkeley.cs160.billiterate;

public class Meeting {
	
	int pk;
	String date;
	String time;
	String type;
	String location;
	
	public Meeting(int pk, String date, String time, String type, String location) {
		this.pk = pk;
		this.date = date;
		this.time = time;
		this.type = type;
		this.location = location;
	}
	
}
