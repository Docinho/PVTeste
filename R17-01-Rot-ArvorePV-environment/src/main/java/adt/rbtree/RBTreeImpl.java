package adt.rbtree;

import javax.print.attribute.standard.RequestingUserName;

import adt.bst.BSTImpl;
import adt.bt.BTNode;
import adt.bt.Util;
import adt.rbtree.RBNode.Colour;

public class RBTreeImpl<T extends Comparable<T>> extends BSTImpl<T> implements RBTree<T> {

	public RBTreeImpl() {
		this.root = new RBNode<T>();
	}

	protected int blackHeight() {
		if (isEmpty())
			return 0;

		return blackHeight((RBNode<T>) this.root);
	}

	public int blackHeight(RBNode<T> node) {
		int retorno = 0;
		if (node.getColour().equals(Colour.BLACK) || (node.getRight() == null && node.getLeft() == null))
			retorno = 1;
		else if (node.isLeaf())
			retorno += Math.max(blackHeight((RBNode<T>) node.getLeft()), blackHeight((RBNode<T>) node.getRight()));

		return retorno;
	}

	protected boolean verifyProperties() {
		boolean resp = verifyNodesColour() && verifyNILNodeColour() && verifyRootColour() && verifyChildrenOfRedNodes()
				&& verifyBlackHeight();

		return resp;
	}

	/**
	 * The colour of each node of a RB tree is black or red. This is guaranteed
	 * by the type Colour.
	 */
	private boolean verifyNodesColour() {
		return true; // already implemented
	}

	/**
	 * The colour of the root must be black.
	 */
	private boolean verifyRootColour() {
		return ((RBNode<T>) root).getColour() == Colour.BLACK; // already
																// implemented
	}

	/**
	 * This is guaranteed by the constructor.
	 */
	private boolean verifyNILNodeColour() {
		return true; // already implemented
	}

	/**
	 * Verifies the property for all RED nodes: the children of a red node must
	 * be BLACK.
	 */
	private boolean verifyChildrenOfRedNodes() {
		return verifyChildrenOfRedNodes((RBNode<T>) root.getLeft())
				&& verifyChildrenOfRedNodes((RBNode<T>) root.getRight());
	}

	public boolean verifyChildrenOfRedNodes(RBNode<T> node) {
		boolean children = false;
		if (node.isEmpty())
			children = true;
		else {
			if (node.isLeaf() || node.getColour().equals(Colour.RED))
				children = ((RBNode) node.getRight()).getColour().equals(Colour.BLACK)
						&& ((RBNode) node.getLeft()).getColour().equals(Colour.BLACK);
			else
				children = verifyChildrenOfRedNodes((RBNode<T>) node.getLeft())
						&& verifyChildrenOfRedNodes((RBNode<T>) node.getRight());

		}
		return children;
	}

	/**
	 * Verifies the black-height property from the root. The method blackHeight
	 * returns an exception if the black heights are different.
	 */
	public boolean verifyBlackHeight() {
		boolean height = this.verifyBlackHeight((RBNode<T>) this.root);
		if (height)
			return height;
		else
			throw new RuntimeException();
	}

	public boolean verifyBlackHeight(RBNode<T> node) {
		boolean height = false;

		if (node.isEmpty())
			height = true;
		else
			height = Math.abs(this.blackHeight((RBNode<T>) node.getLeft()) - this.blackHeight((RBNode<T>) node.getRight())) <= 1;

		return height;
	}

	@Override
	public void insert(T value) {
		if (value != null)
			insert((RBNode<T>) this.root, value);
	}

	public void insert(RBNode<T> node, T element) {
		if (node.isEmpty()) {
			node.setData(element);
			node.setColour(Colour.RED);
			node.setLeft(new RBNode<T>());
			node.getLeft().setParent(node);
			node.setRight(new RBNode<T>());
			node.getRight().setParent(node);
			this.fixUpCase1(node);
		} else if (node.getData().compareTo(element) < 0)
			this.insert((RBNode<T>) node.getRight(), element);
		else if (node.getData().compareTo(element) > 0)
			this.insert((RBNode<T>) node.getLeft(), element);
	}

	@Override
	public RBNode<T>[] rbPreOrder() {
		RBNode[] array = new RBNode[this.size()];

		rbPreOrder(array, 0, (RBNode<T>) this.root);
		return array;
	}

	public int rbPreOrder(RBNode<T>[] array, int index, RBNode<T> node) {
		if (!node.isEmpty()) {
			array[index++] = node;
			index = rbPreOrder(array, index, (RBNode<T>) node.getLeft());
			index = rbPreOrder(array, index, (RBNode<T>) node.getRight());
		}
		return index;
	}

	// FIXUP methods
	protected void fixUpCase1(RBNode<T> node) {
		if (node.equals(this.root))
			node.setColour(Colour.BLACK);
		else
			fixUpCase2(node);
	}

	protected void fixUpCase2(RBNode<T> node) {
		if (node.getColour().equals(Colour.RED) && ((RBNode) (node.getParent())).getColour().equals(Colour.RED))
			fixUpCase3(node);
	}

	protected void fixUpCase3(RBNode<T> node) {
		RBNode grandpa = (RBNode) node.getParent().getParent();
		boolean changed = false;

		if (((!(isLeftChild((RBNode<T>) node.getParent()))
				&& ((RBNode) (grandpa.getLeft())).getColour().equals(Colour.RED)))
				|| (isLeftChild((RBNode<T>) node.getParent())
						&& ((RBNode) grandpa.getRight()).getColour().equals(Colour.RED))) {

			((RBNode) (grandpa.getRight())).setColour(Colour.BLACK);
			((RBNode) (grandpa.getLeft())).setColour(Colour.BLACK);
			grandpa.setColour(Colour.RED);
			this.fixUpCase1(grandpa);
		}

	}

	public boolean isLeftChild(RBNode<T> node) {
		return node.getParent().getLeft().equals((BTNode) node);
	}

	protected void fixUpCase4(RBNode<T> node) {
		if (isLeftChild(node) && !isLeftChild((RBNode<T>) node.getParent()))
			rightRotation((RBNode<T>) node.getParent());
		else if (!isLeftChild(node) && isLeftChild((RBNode<T>) node.getParent()))
			leftRotation((RBNode<T>) node.getParent());
	}

	public void leftRotation(RBNode<T> node) {
		RBNode right = (RBNode) node.getRight();
		// changes the node's right
		node.setRight(node.getRight().getLeft());
		node.getRight().setParent(node);

		// chages the right's parent
		if (node.getParent() != null) {
			if (isLeftChild(node))
				node.getParent().setLeft(right);
			else
				node.getParent().setRight(right);
		}
		right.setParent(node.getParent());

		// changes node's parent
		node.setParent(right);
		right.setLeft(node);

	}

	public void rightRotation(RBNode<T> node) {
		RBNode left = (RBNode) node.getRight();
		// changes node's left
		node.setLeft(left.getRight());
		node.getLeft().setParent(node);

		// chages left's parent
		if (node.getParent() != null) {
			if (isLeftChild(node))
				node.getParent().setLeft(left);
			else
				node.getParent().setRight(left);
		}
		left.setParent(node.getParent());
		
		//changes node's parent
		node.setParent(left);
		left.setRight(node);
	}

	protected void fixUpCase5(RBNode<T> node) {
		if (isLeftChild(node))
			rightRotation((RBNode<T>) node.getParent());
		else
			leftRotation((RBNode<T>) node.getParent());
	}
}
