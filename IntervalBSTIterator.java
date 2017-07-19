///////////////////////////////////////////////////////////////////////////////
//                  
// Main Class File:  Scheduler.java
// File:             IntervalBSTIterator.java
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

import java.util.*;

/**
 * Traverses the IntervalBST from its root, storing each node along the way
 * in an in-order sorting for later use by other methods in other classes.
 *
 * <p>Bugs: none known
 *
 * @author azietlow
 */
public class IntervalBSTIterator<K extends Interval> implements Iterator<K> {


	private Stack<IntervalBSTnode<K>> stack; //for keeping track of nodes

	/**
	 * Constructs a new IntervalBSTIterator
	 * A new traversal is done for each construction
	 */
	public IntervalBSTIterator(IntervalBSTnode<K> root) {
		if (root == null) throw new IllegalArgumentException();
		stack = new Stack<IntervalBSTnode<K>>();
		buildStackInOrder(root);
	} 
	
	/**
	 * To determine if the iterator has more elements
	 * @return whether or not there are more elements
	 */
	public boolean hasNext() {
		return !stack.empty();
	}

	/**
	 * Advances the pointer on item in the collection and returns the previous
	 * element
	 * @return the element previously being pointed to by the iterator
	 */
	public K next() {
		if (stack.empty()) throw new NoSuchElementException();
		return stack.pop().getKey();
	}
	
	/**
	 * Creates a stack that, when popped, will result in the elements of the
	 * IntervalBST sorted in ascending order
	 * @param (root) the root of the tree to build the stack from
	 */
	private void buildStackInOrder(IntervalBSTnode<K> root) {
		if (root.getRight() != null) buildStackInOrder(root.getRight());
		stack.push(root);
		if (root.getLeft() != null) buildStackInOrder(root.getLeft());
	}

	/**
	 * NOT IMPLEMENTED
	 */
	public void remove() {
		throw new UnsupportedOperationException();
	}
}