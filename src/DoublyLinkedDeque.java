import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class DoublyLinkedDeque<E extends Comparable<E>> implements DequeADT<E> {
	public Node<E> firstNode;
	public Node<E> currentNode;
	public Node<E> lastNode;
	private int numElements = 0;

	public DoublyLinkedDeque(){
		firstNode = null;
		lastNode = null;
	}
	
	public class Node<E>{  
		public E element;
		public Node<E> previous;
		public Node<E> next;
		
		public Node(E element){  
			this.element = element;
			this.previous = null;
			this.next = null;
		}
		
		public String toString(){
			return "" + element;
		}
	}

	@Override
	public void enqueueRear(E element) {
		Node<E> addNode = new Node<E>(element);
		System.out.println("Adding element " + addNode + " to the rear of the queue");
		if (firstNode == null){
			lastNode = addNode;
			firstNode = lastNode;
		} else {
			Node<E> currentNode = firstNode;
			while (currentNode.next != null) {
				currentNode = currentNode.next;
			}
			currentNode.next = addNode;
			addNode.previous = currentNode;
			lastNode = addNode;
			lastNode.previous = currentNode;
		}
		numElements++;
		System.out.println(toString());
	}

	@Override
	public E dequeueFront() throws NoSuchElementException {
		Node<E> removedNode = firstNode;
		if (this.size() == 1) {
			clear();
			return null;
		}
		try{
			System.out.println("Removing element " + removedNode.element + " from the front of the queue");
			if (firstNode.next != null){
				firstNode = firstNode.next;
				firstNode.previous = null;
			}  
			else if (firstNode != null) {
				firstNode = null;
				lastNode = null;
				
			} 
			numElements--;
			System.out.println(toString());
			return removedNode.element;
		} catch (NullPointerException npe) {
			lastNode = null;
			System.out.println("Cannot remove element from the front of the queue");
			System.out.println(toString());
		}
		return null;
	}

	@Override
	public E first() throws NoSuchElementException {
		try {
			System.out.println("First element in the queue is " + firstNode.element);
			System.out.println(toString());
			return firstNode.element;
		} catch (NullPointerException npe) {
			System.out.println("Cannot return first element in the queue");
			System.out.println(toString());
		}
		return null;
	}

	@Override
	public void enqueueFront(E element) {
		Node<E> newNode = new Node<E>(element);
		System.out.println("Adding element " + newNode.element + " to the front of the queue");
		if (firstNode == null) {
			firstNode = newNode;
			lastNode = newNode;
		} else {
			newNode.next = firstNode;
			firstNode.previous = newNode;
			firstNode = newNode;
		}
		System.out.println(toString());
	}

	@Override
	public E dequeueRear() throws NoSuchElementException {
		Node<E> removedNode = firstNode;
		if (this.size() == 1) {
			clear();
			return null;
		}
		try{
			System.out.println("Removing element " + removedNode.element + " from the back of the queue");
			if (lastNode.previous!= null){
				lastNode = lastNode.previous;
				lastNode.next = null;
			}  
			else if (lastNode != null) {
				firstNode = null;
				lastNode = null;
				
			} 
			numElements--;
			System.out.println(toString());
			return removedNode.element;
		} catch (NullPointerException npe) {
			lastNode = null;
			System.out.println("Cannot remove element from the rear of the queue");
			System.out.println(toString());
		}
		return null;
	}

	@Override
	public E last() throws NoSuchElementException {
		try{
			System.out.println("Last element in the queue is " + lastNode.element);
			return lastNode.element;
		} catch (NullPointerException npe) {
			System.out.println("Cannot return last element in the queue");
		}
		return null;
	}

	@Override
	public boolean isEmpty() {
		if (size() > 0){
			return false;
		} else {
			return true;
		}
	}

	@Override
	public int size() {
		System.out.println("There are " + numElements + " in the queue");
		return numElements;
	}

	@Override
	public Iterator<E> iterator(boolean fromFront) {
		return new DirectionalIterator(fromFront);
	}
	
	private class DirectionalIterator implements Iterator<E>{
		Node<E> current;
		boolean fromFront;
		
		public DirectionalIterator(boolean fromFront){
			this.fromFront = fromFront;
			if (fromFront == true){
				this.current = firstNode;
			} else {
				this.current = lastNode;
			}
		}
		
		public boolean hasNext(){
			return current != null;
		}
		
		@Override
		public E next() {
			E currentElement = current.element;
			if (fromFront == true){
				current = current.next;
			} else {
				current = current.previous;
			}
			return currentElement;
		}
		
		public String toString() {
			if (fromFront == true) {
				return "" + iteratorFromFront();
			} else {
				return "" + iteratorFromBack();
			}
		}
	}
	
	public String iteratorFromFront(){
		String theString = "Printing queue from front to back\n";
		Iterator iteratorTrue = iterator(true); 
		theString += "[";
		while (iteratorTrue.hasNext()){
			theString += iteratorTrue.next();
			if (iteratorTrue.hasNext()){
				theString += ", ";
			} 
		}
		theString += "]\n";
		return "" + theString;
	}
	
	public String iteratorFromBack(){
		String theString = "Printing queue from back to front\n";
		Iterator iteratorFalse = iterator(false);
		theString += "[";
		while (iteratorFalse.hasNext()){
			theString += iteratorFalse.next();
			if (iteratorFalse.hasNext()){
				theString += ", ";
			} 
		}
		theString += "]\n";
		return "" + theString;
	}

	@Override
	public void clear() {
		int x = 0;
		System.out.println("Clearing list");
		while (x < numElements){
			if (firstNode.next == null){
				firstNode = null;
				numElements--;
			} else {
				firstNode = firstNode.next;
				firstNode.previous = null;
				numElements--;
			}
		}
		lastNode = null;
		System.out.println(toString());
	}

	public String toString(){
		Node<E> currentNode = firstNode;
		String theString = "";
		theString += "[";
		while (currentNode != null){
			theString += currentNode.element;
			if (currentNode.next != null){
				theString += ", ";
			}
			currentNode = currentNode.next;	
		}
		theString += "]\n";
		
		try {
			System.out.println("firstNode is " + firstNode);
			System.out.println("firstNode.next is " + firstNode.next);
			System.out.println("lastNode is " + lastNode);
			System.out.println("lastNode.previous is " + lastNode.previous);
		} catch (NullPointerException npe){
			System.out.println("firstNode.next is null");
			System.out.println("lastNode is " + lastNode);
			System.out.println("lastNode.previous is null");
		}
		return theString;
	}
	
	public static void main(String[] args) {
		DoublyLinkedDeque dlq = new DoublyLinkedDeque();
		String[] list = {"F", "D", "C", "A", "B", "G", "H", "E"};
		dlq.dequeueFront();
		for (int i = 0; i < list.length; i++){
			dlq.enqueueRear(list[i]);
		}
		System.out.println(dlq);
		
		System.out.println(dlq.iterator(true));
	}
}
