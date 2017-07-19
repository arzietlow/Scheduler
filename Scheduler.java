///////////////////////////////////////////////////////////////////////////////
//                   
// Title:            UWScheduler (Scheduler.java)
// Files:            Event.java, Interval.java, IntervalBST.java, Resource.java,
//					 IntervalBSTIterator.java, IntervalBSTnode.java, 
//					 IntervalConflictException.java, SchedulerDB.java, 
//					 SortedListADT.java, Scheduler.java
//
// Semester:         CS 367 - Fall 2015
//
// Author:           Andrew Zietlow
// Email:            arzietlow@wisc.edu
// CS Login:         azietlow
// Lecturer's Name:  Jim Skrentny
// Lab Section:      Lecture 1
//
//////////////////////////// 80 columns wide //////////////////////////////////

import java.io.File;
import java.io.FileNotFoundException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

/**
 * The main class of the program that initializes the database from the 
 * roomSchedule file, processes user commands, and handles output
 *
 * <p>Bugs: none known
 *
 * @author azietlow
 */

public class Scheduler {

	//Scheduler class variables
	private static SchedulerDB schedulerDB = new SchedulerDB(); //the database
	private static Scanner scanner = null; //for user input

	//Main method
	public static void main(String args[]) {
		if (args.length != 1) {			
			System.err.println("Usage: java Scheduler <resourceListFile>");
			System.exit(1);
		}

		boolean didInitialize = 
				initializeFromInputFile(args[0]);

		if(!didInitialize) {
			System.err.println("Failed to initialize the application!");
			System.exit(1);
		}

		System.out.println("Welcome to the UW-Madison Scheduling Client");

		processUserCommands();
	}

	/**
	 * Handles parsing the text file into its Resource and Event objects
	 * and adding those objects to the database
	 * @return whether any problems occured while parsing the file
	 */
	private static boolean initializeFromInputFile(String resourceListFile) {

		boolean hasInitialized = false;
		
		final String RESOURCE_INDICATOR = "#Resource: ";//to know when a 
		//new Resource must be made
		
		File resourceFile = new File(resourceListFile);
		Scanner in = null;
		try {
			in = new Scanner(resourceFile);
		} catch (FileNotFoundException e) {
			System.err.println("File " + resourceFile + " not found");
			System.exit(1);
		}
		while (in.hasNext()) {
			if (!in.nextLine().equals(RESOURCE_INDICATOR)) {
				in.close();
				return hasInitialized;
			}
			while (in.hasNext()) {
				String eventOrg  = null;
				String eventName = null;
				String eventDescrip = null;
				String resourceName = null;
				long eventEnd = 0;
				long eventStart = 1;
				resourceName = in.nextLine();
				if (schedulerDB.addResource(resourceName) == false) {
					in.close();
					return hasInitialized;
				}
				String testResource = in.nextLine();
				do {
					eventName    = testResource;
					eventStart   = convertToTime(in.nextLine());
					eventEnd     = convertToTime(in.nextLine());
					eventOrg     = in.nextLine();
					eventDescrip = in.nextLine();
					if (schedulerDB.addEvent(resourceName, eventStart, eventEnd, 
							eventName, eventOrg, eventDescrip) == false) {
						in.close();
						return hasInitialized;
					}
					if (in.hasNextLine()) {
						testResource = in.nextLine();
					}
					else {
						in.close();
						return true;
					}
				} while (!testResource.equals(RESOURCE_INDICATOR));
			}
		}
		in.close();
		return true;
	}

	/**
	 * Handles user input to properly execute commands
	 */
	private static void processUserCommands() {
		scanner = new Scanner(System.in);
		String command = null;		
		do {

			System.out.print("\nPlease Enter a command ([H]elp):");
			command = scanner.next();
			switch(command.toLowerCase()) {							
			case "v":
				processDisplayCommand();
				break;
			case "a":
				processAddCommand();
				break;
			case "d":
				processDeleteCommand();
				break;
			case "q":
				System.out.println("Quit");
				break;
			case "h":
				System.out.println("\nThis scheduler has commands that are "
						+ "entered as a single character indicated in [].\n"+
						"The main commands are to view, add, delete, or quit.\n"
						+ "The first three main commands need a secondary "
						+ "command possibly with additional input.\n" + "A "
						+ "secondary command's additional input is described "
						+ "within <>.\n"+ "Please note that a comma (,) in the "
						+ "add event command represents a need to press\n" +
						"the return character during the command. Also note "
						+ "that times must be in the format\n" + "of "
						+ "mm/dd/yyyy,hh:mm.\n" +
						"[v]iew\n" +
						"	[r] = view all resources\n" +
						"	[e] = view all events\n" +
						"	[t] <resource name> = view events in a resource\n" +
						"	[o] <organization name> = view events with an "
						+ "organization\n"+"	[n] <start time> <end time> = "
						+ "view events within a time range\n" + "	[s] "
						+ "<start time> <end time> <resource name> = view "
						+ "events within in a time range in a resource\n" +
						"[a]dd\n" +
						"	[r] <resource name> = add a resource\n" +
						"	[e] <resource name>, = add an event\n" +
						"		      <start time> <end time> <event name>, \n"+
						"		      <organization name>, \n" +
						"		      <event description>\n" +
						"[d]elete\n" +
						"	[r] <resource name> = delete a resource\n"+
						"	[e] <event start time> <resource name> = delete an "
						+ "event\n" + "[q]uit\n");
				break;
			default:
				System.out.println("Unrecognized Command!");
				break;
			}
		} while (!command.equalsIgnoreCase("q"));
		scanner.close();
	}


	/**
	 * Used by the commands method to display the proper output
	 */
	private static void processDisplayCommand() {
		String restOfLine = scanner.next();
		Scanner in = new Scanner(restOfLine);
		String subCommand = in.next();
		switch(subCommand.toLowerCase()) {
		//additional input in comments (comma means return)
		case "r": 
			printResourceList(schedulerDB.getResources());
			break;
		case "e": 
			printEventList(schedulerDB.getAllEvents());
			break;
		case "t": // resource,
			printEventList(schedulerDB.getEventsInResource(
					scanner.nextLine().trim()));
			break;
		case "s": // start end resource,
			printEventList(schedulerDB.getEventsInRangeInResource(
					convertToTime(scanner.next()), 
					convertToTime(scanner.next()), scanner.nextLine().trim()));
			break;
		case "o": // organization
			printEventList(schedulerDB.getEventsForOrg(
					scanner.nextLine().trim()));
			break;
		case "n": // start end
			printEventList(schedulerDB.getEventsInRange(
					convertToTime(scanner.next()),
					convertToTime(scanner.next())));
			break;
		default: 
			System.out.println("Unrecognized Command!");
		}
		in.close();
	}

	/**
	 * The command used when adding new events or resources to the database
	 */
	private static void processAddCommand(){
		String restOfLine = scanner.next();
		Scanner in = new Scanner(restOfLine);
		String subCommand = in.next();
		switch(subCommand.toLowerCase()) {
		case "r": //resource
			if(!schedulerDB.addResource(scanner.nextLine().trim())){
				System.out.println("Could not add: no two resources may have "
						+ "the same name.");
			}else{
				System.out.println("Successfully added resource.");
			}
			break;
		case "e": //resource, start end name, organization, description
			try{
				if(!schedulerDB.addEvent(scanner.nextLine().trim(), 
						convertToTime(scanner.next()), 
						convertToTime(scanner.next()), scanner.nextLine().trim()
						, scanner.nextLine().trim(), 
						scanner.nextLine().trim())){
					System.out.println("Could not add: resource not found.");
				}else{
					System.out.println("Successfully added event.");
				}
			}catch(IntervalConflictException expt){
				System.out.println("Could not add: this event conflicted with "
						+ "an existing event.");
			}
			break;
		default: 
			System.out.println("Unrecognized Command!");
		}
		in.close();
	}

	/**
	 * Called when deleting an event or resource from the database
	 */
	private static void processDeleteCommand(){
		String restOfLine = scanner.next();
		Scanner in = new Scanner(restOfLine);
		String subCommand = in.next();
		switch(subCommand.toLowerCase()) {
		case "r": // resource
			if(!schedulerDB.removeResource(scanner.nextLine().trim())){
				System.out.println("Could not delete. Resource not found.");
			}else{
				System.out.println("Successfully deleted resource.");
			}
			break;
		case "e":  // resource, start
			if(!schedulerDB.deleteEvent(convertToTime(scanner.next().trim()), 
					scanner.nextLine().trim())){
				System.out.println("Could not delete. Resource not found.");
			}else{
				System.out.println("Successfully deleted event.");
			}
			break;
		default: 
			System.out.println("Unrecognized Command!");
		}
		in.close();
	}

	/**
	 * Prints the given list of resources
	 * @param (list) the list to print
	 */
	private static void printResourceList(List<Resource> list){
		Iterator<Resource> itr = list.iterator();
		if(!itr.hasNext()){
			System.out.println("No resources to display.");
		}
		while(itr.hasNext()){
			System.out.println(itr.next().getName());
		}
	}

	/**
	 * Prints the given list of Events
	 * @param (list) the list to print
	 */
	private static void printEventList(List<Event> list){
		Iterator<Event> itr = list.iterator();
		if(!itr.hasNext()){
			System.out.println("No events to display.");
		}
		while(itr.hasNext()){
			System.out.println("\n" + itr.next().toString());
		}
	}

	/**
	 * Converts strings of a specific time format to their long time format
	 * @param (time) the string being converted to its long time format
	 * @return the new long format
	 */
	private static long convertToTime(String time){
		long result = 0;
		Format format = new SimpleDateFormat("MM/dd/yyyy,HH:mm");
		try{
			Date date = (Date) format.parseObject(time);
			result = date.getTime()/1000;
		}catch(Exception e){
			System.out.println("Dates are not formatted correctly.  "
					+ "Must be \"MM/dd/yyyy,HH:mm\"");
		}
		return result;
	}

}



