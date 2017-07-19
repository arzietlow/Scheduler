///////////////////////////////////////////////////////////////////////////////
//                  
// Main Class File:  Scheduler.java
// File:             IntervalBST.java
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
 * A modified Binary Search Tree that has an Interval object
 * as its key and uses the start time of the Interval to sort its nodes.
 *
 * <p>Bugs: none known
 *
 * @author azietlow
 */

public class IntervalBST<K extends Interval> implements SortedListADT<K> {

	//class vars
	private int size;                //the number of nodes in this tree
	private IntervalBSTnode<K> root; //the root node of this tree                

	/**
	 * Constructs an empty tree
	 */
	public IntervalBST() {
		this.root = null;
		this.size = 0;
	}

	/**
	 * Inserts a key into the tree at a location based on that key's value
	 * @param (key) the key value to insert into the tree
	 */
	public void insert(K key){
		IntervalBSTnode<K> toAdd = new IntervalBSTnode<K>(key);//the new node
		boolean didAdd = true; //To determine whether size should be incremented

		//foundation expression
		if (root == null) root = toAdd; 

		else try { insert(key, root); }
		catch (IntervalConflictException e) { 
			didAdd = false; 
		}
		if (didAdd == true) size++;
	}

	//you can decide whether to go down that  subtree or not when inserting NN 
	//For example, when newNode(start) <= maxEnd && maxEnd <= newNode(end),
	//a conflicting event is already present in that subtree. 

	/**
	 * Recursive helper method for Insert
	 * @param (key) the key value to insert into the tree
	 */
	private void insert(K key, IntervalBSTnode<K> parent) {

		//compareTo returns -1 if the root is older than the key, 1 if not
		//must check for overlap in each case
		IntervalBSTnode<K> toAdd = new IntervalBSTnode<K>(key);

		long max = parent.getMaxEnd();

		if ((key.getStart() <= max) && (max <= key.getEnd())) {
			throw new IntervalConflictException();
		}

		//case root and key are equal
		if (parent.getKey().compareTo(key) == 0) 
			throw new IntervalConflictException();

		//case root is older than key (key happened after root)
		if (parent.getKey().compareTo(key) == -1) {

			if (parent.getKey().overlap(key)) {
				throw new IntervalConflictException();
			}

			if (parent.getRight() == null) {
				parent.setRight(toAdd);
				parent.setMaxEnd(toAdd.getMaxEnd());
			}
			else insert(key, parent.getRight());
		}

		//case root is younger than key (key happened before root)
		else if (parent.getKey().compareTo(key) == 1) {

			if (parent.getKey().overlap(key)) {
				throw new IntervalConflictException();
			}

			if (parent.getLeft() == null) parent.setLeft(toAdd);
			else insert(key, parent.getLeft());
		}
	}

	/**
	 * Deletes a key from this tree if that key is found and reorders the 
	 * tree if necessary
	 * @param (key) the key value to delete from the tree
	 * @return true if key is in tree and is deleted, false if otherwise
	 */
	public boolean delete(K key) {
		if (key == null) throw new IllegalArgumentException();
		if (this.size <= 0) return false;
		if (root.getKey().compareTo(key) == 0) {

			//key found at root
			if ((root.getRight() == null) && (root.getLeft()) == null) {
				root = null;
			}

		//cases where root has 1 child
			//right subtree
			else if ((root.getRight() != null) && (root.getLeft() == null)) {
				root = root.getRight();
			}

			//left subtree
			else if ((root.getRight() == null) && (root.getLeft() != null)) {
				root = root.getLeft();
				fixMaxEnds(root);
			}
			
		//final case where root has 2 children
			else {
				IntervalBSTnode<K> temp = findSuccessor(root);
				delete(temp.getKey());
				temp.setLeft(root.getLeft());
				temp.setRight(root.getRight());
				root = temp;
				fixMaxEnds(root);
			}
			size--;
			return true;
		}
	//the searching process for delete
		else if (root.getKey().compareTo(key) == 1) {
			return delete(key, root.getLeft());
		}
		else return delete(key, root.getRight());
	}


	/**
	 * The recursive helper method for delete
	 * @param (key) the key value to delete from the tree
	 */
	private boolean delete(K key, IntervalBSTnode<K> parent) {

		if (parent == null) return false;

		//parent comes after key (search before parent) (left)
		if (parent.getKey().compareTo(key) == 1) {

			if (parent.getLeft() != null) {
				//if parent's left node is the key to delete
				if (parent.getLeft().getKey().compareTo(key) == 0) {
					
				//no children
					if ((parent.getLeft().getLeft() == null) && 
							(parent.getLeft().getRight() == null)) {
						parent.setLeft(null);
					}
					
				//1 child
					//left isn't null, right is null
					else if ((parent.getLeft().getLeft() != null) && 
							(parent.getLeft().getRight() == null)) {
						parent.setLeft(parent.getLeft().getLeft());
					}
					//right isn't null, left is null
					else if ((parent.getLeft().getLeft() == null) && 
							(parent.getLeft().getRight() != null)) {
						parent.setLeft(parent.getLeft().getRight());
					}
					
				//2 children
					else {
						IntervalBSTnode<K> temp = 
								findSuccessor(parent.getLeft());
						delete(temp.getKey(), root);
						temp.setLeft(parent.getLeft().getLeft());
						temp.setRight(parent.getLeft().getRight());
						parent.setLeft(temp);
					}
					size--;
					return true;
				}
				else return delete(key, parent.getLeft());
			}
		}

		//parent comes before key (search after parent) (right)
		else {
			if (parent.getRight() != null) {
				//if parent's right node is the key to delete
				if (parent.getRight().getKey().compareTo(key) == 0) {
					
				//no children
					if ((parent.getRight().getLeft() == null) && 
							(parent.getRight().getRight() == null)) {
						parent.setRight(null);
					}
					
				//1 child
					//left isn't null, right is null
					else if ((parent.getRight().getLeft() != null) && 
							(parent.getRight().getRight() == null)) {
						parent.setRight(parent.getRight().getLeft());
					}
					//right isn't null, left is null
					else if ((parent.getRight().getLeft() == null) && 
							(parent.getRight().getRight() != null)) {
						parent.setRight(parent.getRight().getRight());
					}
					
				//2 children
					else {
						IntervalBSTnode<K> temp = 
								findSuccessor(parent.getRight());
						delete(temp.getKey(), root);
						temp.setLeft(parent.getRight().getLeft());
						temp.setRight(parent.getRight().getRight());
						parent.setRight(temp);
					}
					size--;
					fixMaxEnds(root);
					return true;
				}
				else return delete(key, parent.getRight());
			}
		}
		return false;
	}


	/**
	 * Recursive method to fix the maxEnd fields of the tree's nodes.
	 * Only called in the case where a node or multiple nodes would have 
	 * their maxEnd needing an update.
	 * @param (parent) the root of the current subtree being updated
	 */
	private void fixMaxEnds(IntervalBSTnode<K> parent) {

		//base cases
		if (parent == null) return;
		if (parent.getRight() == null) {
			parent.setMaxEnd(parent.getEnd());
			fixMaxEnds(parent.getLeft());
		}

		//recursive variables
		IntervalBSTnode<K> temp = parent; //to traverse without losing parent							
		int levels = 0; //number of levels traversed

		//traverses to root's rightmost node, sets root's maxEnd to this root's
		while (temp.getRight() != null) {
			temp = temp.getRight();
			if (temp.getLeft() != null) fixMaxEnds(temp.getLeft());
			levels++;
		}
		long maxEnd = temp.getMaxEnd();
		parent.setMaxEnd(maxEnd);

		//for each level, parent to largest, resets maxEnds along the way
		temp = parent;
		for (int i = 1; i < levels; i++) {
			temp = temp.getRight();
			temp.setMaxEnd(maxEnd);
		}
	}

	/**
	 * Searches for a key within the tree
	 * @param (key) the key value to search for within the tree
	 * @return the key if found, null if not found
	 */
	public K lookup(K key) {
		if (key == null) throw new IllegalArgumentException();
		return lookup(key, root);
	}


	/**
	 * Lookup method's helper method
	 * @param (key) the key value to search for
	 * @return the key if found, null if not found
	 */
	private K lookup(K key, IntervalBSTnode<K> root) {
		if (root == null) return null;
		if (root.getKey().compareTo(key) == 0) return key;
		if (root.getKey().compareTo(key) == -1) {
			return lookup(key, root.getRight());
		}
		else return lookup(key, root.getLeft());
	}


	/**
	 * Accessor method for this tree's size
	 * @return the number of nodes in the BST
	 */
	public int size() {
		return this.size;
	}


	/**
	 * For knowing if tree has no more nodes
	 * @return whether or not the tree is empty
	 */
	public boolean isEmpty() {
		return this.size <= 0;
	}

	/**
	 * Creates an iterator to traverse the nodes within this ordered list
	 * @return an in-order iterator for the nodes in the tree
	 */
	public Iterator<K> iterator() {
		return new IntervalBSTIterator<K>(this.root);
	}

	/**
	 * Traverses from a given root to find that root's largest in-order 
	 * successor
	 * @param (parent) the reference node
	 * @return the given node's node containing its largest key successor
	 */
	private IntervalBSTnode<K> findSuccessor(IntervalBSTnode<K> parent) {
		IntervalBSTnode<K> temp = parent.getLeft();
		if (temp != null) {
			while (temp.getRight() != null) {
				temp = temp.getRight();
			}
			return temp;
		}
		else return parent;
	}
}