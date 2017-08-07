package adt.bt;

import adt.bst.BSTNode;

public class Util {


	/**
	 * A rotacao a esquerda em node deve subir e retornar seu filho a direita
	 * @param node
	 * @return
	 */
	public static <T extends Comparable<T>> BSTNode<T> leftRotation(BSTNode<T> node) {
		BSTNode<T> right = (BSTNode<T>) node.getRight();
		 
        node.setRight(right.getLeft());
        right.getLeft().setParent(node);
        right.setParent(node.getParent());
        if (node.getParent() != null)
        	if (node.getParent().getData().compareTo(right.getData()) > 0)
        		node.getParent().setLeft(right);
        	else{
        		node.getParent().setRight(right);
        	}
        node.setParent(right);
        right.setLeft(node);
 
        return right;
 
	}

	/**
	 * A rotacao a direita em node deve subir e retornar seu filho a esquerda
	 * @param node
	 * @return
	 */
	public static <T extends Comparable<T>> BSTNode<T> rightRotation(BSTNode<T> node) {
		BSTNode<T> left = (BSTNode<T>) node.getLeft();
		 
        node.setLeft(left.getRight());
        left.getRight().setParent(node);
        left.setParent(node.getParent());
        if (node.getParent() != null)
        	if (node.getParent().getData().compareTo(left.getData()) < 0)
        		node.getParent().setRight(left);
        	else {
        		node.getParent().setLeft(left);
        	}
        node.setParent(left);
        left.setRight(node);
 
        return left;
	}

	public static <T extends Comparable<T>> T[] makeArrayOfComparable(int size) {
		@SuppressWarnings("unchecked")
		T[] array = (T[]) new Comparable[size];
		return array;
	}
}
