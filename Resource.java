///////////////////////////////////////////////////////////////////////////////
//                  
// Main Class File:  Scheduler.java
// File:             Resource.java
// Semester:         CS 367 Fall 2015
//
// Author:           Andrew Zietlow arzietlow@wisc.edu
// CS Login:         azietlow
// Lecturer's Name:  Jim Skrentny
// Lab Section:      Lecture 1
//
//
// Pair Partner:     N/A
//
// External Help:   None
//////////////////////////// 80 columns wide //////////////////////////////////

import java.util.Iterator;

/**
 * No comments needed
 *
 * <p>Bugs: none known
 *
 * @author azietlow
 */

public class Resource {
	
	//class variables
	private String name;
	private SortedListADT<Event> events;

	/**
	 * (Write a succinct description of this method here.  If necessary,
	 */
	Resource(String name){
		this.name = name;
		events = new IntervalBST<Event>();
		if (name == null) throw new IllegalArgumentException();
	}

	/**
	 * (Write a succinct description of this method here.  If necessary,
	 * @return (description of the return value)
	 */
	public String getName(){
		return name;
	}

	/**
	 * (Write a succinct description of this method here.  If necessary,
	 * @return (description of the return value)
	 */
	public boolean addEvent(Event e){
		int numEvents = events.size();
		if(e == null) throw new IllegalArgumentException();//not possible
		events.insert(e);
		if (events.size() > numEvents) return true;
		return false;
	}

	/**
	 * (Write a succinct description of this method here.  If necessary,
	 * @return (description of the return value)
	 */
	public boolean deleteEvent(long start){
		if (start < 0) return false;
		return events.delete(new Event(start, start, "", "", "", ""));
	}

	/**
	 * (Write a succinct description of this method here.  If necessary,
	 * @return (description of the return value)
	 */
	public Iterator<Event> iterator(){
		return events.iterator();
	}

}
