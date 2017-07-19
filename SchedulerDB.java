///////////////////////////////////////////////////////////////////////////////
//                  
// Main Class File:  Scheduler.java
// File:             SchedulerDB.java
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

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Holds all of the program's Resource objects in a list for use by the main
 * class. It cannot contain duplicate Resource objects.
 *
 * <p>Bugs: none known
 *
 * @author azietlow
 */

@SuppressWarnings("unused")
public class SchedulerDB {

	//class variable
	List<Resource> resources; //list of all Resource objects in the scheduler

	/**
	 * Constructs a new database with an empty list
	 */
	SchedulerDB() {
		this.resources = new ArrayList<Resource>();
	}

	/**
	 * Adds a resource to the resource list
	 * @return whether or not the resource was successfully added
	 */
	public boolean addResource(String resource) {
		if ((resource == null) || (resource.length() == 0)) { return false; }
		for (Resource r: resources) {
			if (r.getName().equals(resource)) return false;
		}
		resources.add(new Resource(resource));
		return true;
	}

	/**
	 * Removes a resource from the resource list
	 * @return whether or not the resource was successfully removed
	 */
	public boolean removeResource(String r) {
		for (Resource o: resources) {
			if (o.getName().equals(r)) return resources.remove(o);
		}
		return false;
	}

	/**
	 * Adds an event to a single resource within the resource list
	 * @return whether or not the event was successfully added
	 */
	public boolean addEvent(String r, long start, long end, String name, 
			String organization, String description) {
		Event toAdd = null;
		try {
			toAdd = new Event(start, end, name, r, organization, description);
		}
		catch (IllegalArgumentException e) {
			return false;
		}
		for (Resource o: resources) {
			if (o.getName().equals(r)) return o.addEvent(toAdd);
		}
		return false;
	}

	/**
	 * Removes an event from a single resource within the resource list
	 * @return whether or not the event was successfully removed
	 */
	public boolean deleteEvent(long start, String resource) {
		if ((start < 0) || (resource == null)) return false;
		for (Resource o: resources) {
			if (o.getName().equals(resource)) return o.deleteEvent(start);
		}
		return false;
	}

	/**
	 * Searches for a resource from within the resource list, given a name
	 * @param (r) the name of the Resource object to search for
	 * @return the Resource if it was found, null if it was not 
	 */
	public Resource findResource(String r) {
		if (r == null) return null;
		for (Resource o: resources) {
			if (o.getName().equals(r)) return o;
		}
		return null;
	}

	/**
	 * Accessor for the list of all Resources
	 * @return the database's resource list
	 */
	public List<Resource> getResources() {
		return this.resources;
	}

	/**
	 * Gathers the list of events that occur within a given resource
	 * @param (resource) the name of the resource to search within
	 * @return the list of events that were found to match
	 */
	public List<Event> getEventsInResource(String resource) {
		List<Event> eventList = new ArrayList<Event>();
		Resource toList = this.findResource(resource);
		if (toList == null) return eventList;
		Iterator<Event> itr = toList.iterator();
		while (itr.hasNext()) {
			eventList.add(itr.next());
		}
		return eventList;
	}

	/**
	 * Gathers the list of events that occur within a given time window
	 * @param (start, end) the window to search within
	 * @return the list of events that were found to match
	 */
	public List<Event> getEventsInRange(long start, long end){
		List<Event> eventsInRange = new ArrayList<Event>();
		if ((start < 0) || (end < 0)) return eventsInRange;
		if (start > end) return eventsInRange;
		Event dummyEvent = null;
		try {
			dummyEvent = new Event(start, end, "", "", "", "");
		}
		catch (IllegalArgumentException e) {
			return eventsInRange;
		}
		List<Event> allEvents = this.getAllEvents();
		for (Event o: allEvents) {
			if (o.overlap(dummyEvent)) {
				eventsInRange.add(o);
			}
		}
		return eventsInRange;
	}	

	/**
	 * Gathers the list of events that occur within a given time window and also
	 * within a given resource
	 * @param (start, end) the window to search within
	 * @param (resource) the name of the resource to search within
	 * @return the list of events that were found to match
	 */
	public List<Event> getEventsInRangeInResource(long start, long end, 
			String resource){
		List<Event> eventsInRange = new ArrayList<Event>();
		Event dummyEvent = null;
		try {
			dummyEvent = new Event(start, end, "", "", "", "");
		}
		catch (IllegalArgumentException e) { return eventsInRange; }
		List<Event> eventsInResource = this.getEventsInResource(resource);
		if (eventsInResource.size() == 0) return eventsInRange;
		for (Event o: eventsInResource) {
			if (o.overlap(dummyEvent)) {
				eventsInRange.add(o);
			}
		}
		return eventsInRange;
	}

	/**
	 * Gathers all events across all resources into a single list
	 * @return the complete list of events
	 */
	public List<Event> getAllEvents(){
		List<Event> eventList = new ArrayList<Event>();
		for (Resource o: resources) {
			Iterator<Event> itr = o.iterator();
			while (itr.hasNext()) {
				eventList.add(itr.next());
			}
		}
		return eventList;
	}

	/**
	 * Gathers the list of events that are being held by a given organization
	 * @param (org) the name of the org to find events by
	 * @return the list of events that were found to match
	 */
	public List<Event> getEventsForOrg(String org){
		List<Event> orgEventList = new ArrayList<Event>();
		if (org == null) return orgEventList;
		List<Event> eventList = this.getAllEvents();
		for (Event o: eventList) {
			if (o.getOrganization().equals(org)) orgEventList.add(o);
		}
		return orgEventList;
	}
}
