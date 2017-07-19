///////////////////////////////////////////////////////////////////////////////
//                  
// Main Class File:  Scheduler.java
// File:             IntervalBSTnode.java
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

/**
 * The recursive building block of the IntervalBST tree.
 * Accounts for the structure of the tree.
 *
 * <p>Bugs: none known
 *
 * @author azietlow
 */

class IntervalBSTnode<K extends Interval> {

	//class vars
	private K key;       //data held within a node
	private long maxEnd; //value used to help prune searches 
	private IntervalBSTnode<K> left, right; //roots of this node's subtrees

	/**
	 * Constructs a new node using only the key data and checks its validity
	 */
	public IntervalBSTnode(K keyValue) {

		this.key = keyValue;
		this.left = null;
		this.right = null;
		this.maxEnd = this.key.getEnd();
		if (key == null) throw new IllegalArgumentException();
	}

	/**
	 * Constructs a new node with all parameters and checks validity
	 */
	public IntervalBSTnode(K keyValue, IntervalBSTnode<K> leftChild, 
			IntervalBSTnode<K> rightChild, long maxEnd) {

		this.key = keyValue;
		this.left = leftChild;
		this.right = rightChild;
		this.maxEnd = maxEnd;
		if ((key == null) || (maxEnd < 0)) throw new IllegalArgumentException();
	}

	/**
	 * Accessor method for a node's data
	 * @return the data stored within this node
	 */
	public K getKey() { 
		return this.key;
	}

	/**
	 * Accessor method for a node's left subtree's root node
	 * @return this node's left branching node
	 */
	public IntervalBSTnode<K> getLeft() { 
		return this.left;
	}

	/**
	 * Accessor method for a node's right subtree's root node
	 * @return this node's right branching node
	 */
	public IntervalBSTnode<K> getRight() { 
		return this.right;
	}

	/**
	 * Returns the maximum end value of the intervals in this node's R subtree.
	 * @return the end variable of this node's rightmost node's data
	 */
	public long getMaxEnd(){
		return this.maxEnd;
	}

	/**
	 * Setter method for the data within a node
	 * @param (newK) the new value for a node's key
	 */
	public void setKey(K newK) { 
		this.key = newK;
	}

	/**
	 * Setter method for a node's left branch node
	 * @param (newL) the new left branch node
	 */
	public void setLeft(IntervalBSTnode<K> newL) { 
		this.left = newL;
	}

	/**
	 * Setter method for a node's right branch node
	 * @param (newR) the new right branch node
	 */
	public void setRight(IntervalBSTnode<K> newR) { 
		this.right = newR;
	}

	/**
	 * Setter method for a node's new maxEnd value
	 * @param (newEnd) the new maxEnd value
	 */
	public void setMaxEnd(long newEnd) { 
		this.maxEnd = newEnd ;
	}

	/**
	 * Accessor method for a node's data's start time
	 * @return the start timestamp of the interval
	 */
	public long getStart(){ 
		return key.getStart();
	}

	/**
	 * Accessor method for a node's data's end time
	 * @return the end timestamp of the interval
	 */
	public long getEnd(){
		return key.getEnd();
	}

	/**
	 * The exact same method as getKey()
	 */
	public K getData(){
		return this.key;
	}

}