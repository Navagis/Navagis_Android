package com.reliance.models;

public class Waypoint {
	String waypoints[];

	public String[] getWaypoints() {
		return waypoints;
	}

	public void setWaypoints(String[] waypoints) {
		this.waypoints = waypoints;
	}

	Waypoint() {
		this(null);
	}
	
	public Waypoint(String[] waypoints) {
		super();
		this.waypoints = waypoints;
	}


}
