package adt.bst;
 
public class BSTImpl<T extends Comparable<T>> implements BST<T> {
 
	protected BSTNode<T> root;
 
	public BSTImpl() {
		root = new BSTNode<T>();
	}
 
	public BSTNode<T> getRoot() {
		return this.root;
	}
 
	@Override
	public boolean isEmpty() {
		return root.isEmpty();
	}
 
	@Override
	public int height() {
		return height(getRoot()) - 1;
	}
 
	private int height(BSTNode<T> node){
		if (node.isEmpty()){
			return 0;
		}
		return 1 +  Math.max(height((BSTNode<T>) node.getLeft()), height((BSTNode<T>) node.getRight()));
	}
 
	@Override
	public BSTNode<T> search(T element) {
		return search(element, getRoot());
	}
 
	public BSTNode<T> search(T element, BSTNode<T> node) {
		if (element == null || node.isEmpty()){
			return new BSTNode<T>();
		}
 
		if (node.isEmpty() || node.getData().equals(element)){
			return node;
		}else if (node.getData().compareTo(element) > 0){
			return search(element, (BSTNode<T>) node.getLeft());
		}else{
			return search(element, (BSTNode<T>) node.getRight());
		}
	}
 
	@Override
	public void insert(T element) {
		if (element == null) return;
		insert (element, getRoot());
	}
 
	private void insert(T element, BSTNode<T> node) {
		if (node.isEmpty()){
			node.setData(element);
			node.setLeft(new BSTNode<T>());
			node.setRight(new BSTNode<T>());
			node.getLeft().setParent(node);
			node.getRight().setParent(node);
		}
		else{
			if (node.getData().compareTo(element) < 0){
				insert(element, (BSTNode<T>) node.getRight()); 
			}else if (node.getData().compareTo(element) > 0){
				insert(element, (BSTNode<T>) node.getLeft());
			}
		}
	}
 
	@Override
	public BSTNode<T> maximum() {
		return maximum(getRoot());
	}
 
	private BSTNode<T> maximum(BSTNode<T> node) {
		if (node.isEmpty()){
			return null;
		}else if(node.getRight().isEmpty()){
			return node;
		}else{
			return maximum((BSTNode<T>) node.getRight());
		}
	}
 
	@Override
	public BSTNode<T> minimum() {
		return minimum(getRoot());
	}
 
	private BSTNode<T> minimum(BSTNode<T> node){
		if (node.isEmpty()){
			return null;
		}else if(node.getLeft().isEmpty()){
			return node;
		}else{
			return minimum((BSTNode<T>) node.getLeft());
		}
	}
 
	@Override
	public BSTNode<T> sucessor(T element) {
		BSTNode<T> node = search(element);
		if (node == null || node.isEmpty()){
			return null;
		}
		BSTNode<T> rightMin = minimum((BSTNode<T>) node.getRight());
		if (rightMin != null){
			return rightMin;
		}else{
			BSTNode<T> parent = (BSTNode<T>) node.getParent();
			while (parent != null && parent.getData().compareTo(node.getData()) < 0){
				parent = (BSTNode<T>) parent.getParent();
			}
			return parent;
		}
 
	}
 
	@Override
	public BSTNode<T> predecessor(T element) {
		BSTNode<T> node = search(element);
		if (node == null || node.isEmpty()){
			return null;
		}
 
		BSTNode<T> leftMax = maximum((BSTNode<T>) node.getLeft());
		if (leftMax != null){
			return leftMax;
		}else{
			BSTNode<T> parent = (BSTNode<T>) node.getParent();
			while (parent != null && parent.getData().compareTo(node.getData()) > 0){
				parent = (BSTNode<T>) parent.getParent();
			}
			return parent;
		}
	}
 
	@Override
	public void remove(T element) {
		if (element == null) return;
 
		BSTNode<T> node = search(element);
		if(!node.isEmpty()) remove(node);
	}
 
	private void remove(BSTNode<T> node){
		if(node.getData().equals(getRoot().getData())){
			BSTNode<T> aux = sucessor(node.getData());
			if (aux == null){
				aux = predecessor(node.getData());
			}
			if (aux == null){
				this.root.setData(null);
			}else{
				T auxData = node.getData();
				node.setData(aux.getData());
				aux.setData(auxData);
				remove(aux);
			}
		}
		else if (node.isLeaf()){
			node.setData(null);
		}else if (!node.getRight().isEmpty() && node.getLeft().isEmpty()){
			if(!node.getParent().getRight().isEmpty() && node.getParent().getRight().equals(node)){
				node.getParent().setRight(node.getRight());
				node.getRight().setParent(node.getParent());
			}else{
				node.getParent().setLeft(node.getRight());
				node.getRight().setParent(node.getParent());
			}
		}else if (node.getRight().isEmpty() && !node.getLeft().isEmpty()){
			if(node.getParent().getRight() != null && node.getParent().getRight().equals(node)){
				node.getParent().setRight(node.getLeft());
				node.getLeft().setParent(node.getParent());
			}else{
				node.getParent().setLeft(node.getLeft());
				node.getLeft().setParent(node.getParent());
			}
		}else{
			BSTNode<T> aux = sucessor(node.getData());
			T auxData = node.getData();
			node.setData(aux.getData());
			aux.setData(auxData);
			remove(aux);
		}
	}
 
	@Override
	public T[] preOrder() {
		T[] array = (T[]) new Comparable[this.size()];
		preOrder(getRoot(), array, 0);
		return array;
	}
 
	private int preOrder(BSTNode<T> node, T[] array, int index) {
		if (!node.isEmpty()){
			array[index++] = node.getData();
			index = preOrder((BSTNode<T>) node.getLeft(), array, index);
			index = preOrder((BSTNode<T>) node.getRight(), array, index);
		}
		return index;
	}
 
	@Override
	public T[] order() {
		T[] array = (T[]) new Comparable[this.size()];
		order(getRoot(), array, 0);
		return array;
	}
 
	private int order(BSTNode<T> node, T[] array, int index) {
		if (!node.isEmpty()){
			index = order((BSTNode<T>) node.getLeft(), array, index);
			array[index++] = node.getData();
			index = order((BSTNode<T>) node.getRight(), array, index);
		}
		return index;
	}
 
	@Override
	public T[] postOrder() {
		T[] array = (T[]) new Comparable[this.size()];
		postOrder(getRoot(), array, 0);
		return array;
	}
 
	private int postOrder(BSTNode<T> node, T[] array, int index) {
		if (!node.isEmpty()){
			index = postOrder((BSTNode<T>) node.getLeft(), array, index);
			index = postOrder((BSTNode<T>) node.getRight(), array, index);
			array[index++] = node.getData();
		}
		return index;
	}
 
	/**
	 * This method is already implemented using recursion. You must understand
	 * how it work and use similar idea with the other methods.
	 */
	@Override
	public int size() {
		return size(root);
	}
 
	private int size(BSTNode<T> node) {
		int result = 0;
		// base case means doing nothing (return 0)
		if (!node.isEmpty()) { // indusctive case
			result = 1 + size((BSTNode<T>) node.getLeft())
					+ size((BSTNode<T>) node.getRight());
		}
		return result;
	}
}