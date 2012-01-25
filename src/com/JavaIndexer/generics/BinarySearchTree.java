package com.JavaIndexer.generics;

public class BinarySearchTree<T> {
	BinarySearchTreeNode<T> el;
	public BinarySearchTreeNode<T> getRoot(){
		return this.el;
	}
	public void setAsRoot(BinarySearchTreeNode<T> el) {
		this.el = el;
		
	}
}
