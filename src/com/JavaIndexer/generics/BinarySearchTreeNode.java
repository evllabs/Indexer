package com.JavaIndexer.generics;

public class BinarySearchTreeNode<T>{
	T el;
	BinarySearchTreeNode<T> left;
	BinarySearchTreeNode<T> right;
	
	public BinarySearchTreeNode(T el) {
		this.el = el;
	}
	public BinarySearchTreeNode(T el,
			BinarySearchTreeNode<T> left,
			BinarySearchTreeNode<T> right) {
		this.el = el;
		this.left = left;
		this.right = right;
	}
	public boolean isLeaf(){
		return this.left == null && this.right == null;
	}
	public T getEl(){
		return this.el;
	}
	public BinarySearchTreeNode<T> getLeft(){
		return this.left;
	}
	public BinarySearchTreeNode<T> getRight(){
		return this.right;
	}
}
