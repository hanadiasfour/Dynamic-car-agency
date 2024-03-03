package application;

public class CircularDoublyLinkedList implements List {// overrides unimplemented methods in List interface

	private DNode first, last;// head and tail
	private int count;// number of nodes

	@Override
	public void addSorted(Object element) {// adds element at its right location in the CDLL O(n)

		DNode temp = new DNode(element);
		DNode prev = first, curr = first;

		if (count == 0) {// empty list (adds at beginning O(1))
			first = last = temp;
			last.setNext(first);
			first.setPrev(last);
		} else {// list is not empty

			// looping the CDLL until reaching a node with an element that is greater
			for (int i = 1; i <= count && ((Brand) curr.getElement()).compareTo((Brand) element) < 0; i++) {
				prev = curr;
				curr = curr.getNext();
			}

			// insert at head (loop wasn't entered O(1))
			if (curr == prev && ((Brand) temp.getElement()).compareTo((Brand) curr.getElement()) < 0) {
				temp.setNext(first);
				first.setPrev(temp);
				temp.setPrev(last);
				first = temp;
				last.setNext(first);

			} else if (prev.equals(last)) {// insert at end (this element is the largest O(n))
				temp.setNext(first);
				temp.setPrev(prev);
				prev.setNext(temp);
				last = temp;
				first.setPrev(last);
			} else { // insert in between O(n)
				curr.setPrev(temp);
				temp.setNext(curr);
				prev.setNext(temp);
				temp.setPrev(prev);
			}
		}
		count++;
	}

	@Override
	public boolean delete(Object element) {// deleting node with same element O(n)
		DNode current = first.getNext();
		DNode previous = first;

		if (count != 0) {// list is not empty

			if (element.equals(first.getElement()))// deleting first
				return deleteFirst();

			else if (element.equals(last.getElement()))// deleting last
				return deleteLast();

			else {// deleting node in-between
				for (int i = 1; i < count - 1; i++) {// looping the CDLL until the element is matched to delete O(n)
					if (((Brand) element).equals((Brand) current.getElement()))
						break;
					previous = current;
					current = current.getNext();
				}
				previous.setNext(current.getNext());
				current.getNext().setPrev(previous);
				current.setNext(null);
				count--;
				return true;
			}
		}
		return false;
	}

	public Object get(String name) {// gets location with the same name O(n)

		if (count != 0) {// list is not empty
			if (((Brand) getFirst()).getBrandName().toLowerCase().contains(name.toLowerCase()))
				return getFirst();// first element O(1)

			else if (((Brand) getLast()).getBrandName().toLowerCase().contains(name.toLowerCase()))
				return getLast();// last element O(1)

			else {// element in the middle of the CDLL O(n)
				DNode current = first.getNext();
				for (int i = 1; i < count; i++) {
					if (((Brand) current.getElement()).getBrandName().toLowerCase().contains(name.toLowerCase()))
						return current.getElement();
					current = current.getNext();

				}
			}
		}
		return null;// not found
	}

	@Override
	public Object get(Object element) {// gets location object O(n)

		DNode current = null;
		if (count > 0) {// list not empty
			if (element.equals(first.getElement())) {
				return getFirst();// first element O(1)

			} else if (((Brand) element).equals((Brand) last.getElement())) {
				return getLast();// last element O(1)

			} else {// element in the middle of the CDLL O(n)
				current = first.getNext();
				for (int i = 1; i < count; i++) {
					if (((Brand) element).equals((Brand) current.getElement()))
						return current.getElement();
					current = current.getNext();
				}
			}
		}
		return null;// not found
	}

	public Brand getNext(Brand element) {// gets the next node element O(n)

		if (count > 0) {// not empty
			if (element.equals(last.getElement()))// next of the last is first O(1)
				return (Brand) first.getElement();

			else if (element.equals(first.getElement()))// next of the first O(1)
				return (Brand) first.getNext().getElement();

			else {// looping until we find the element O(n)
				DNode current = first.getNext();

				for (int i = 1; i < count - 1; i++) {
					if (element.equals(current.getElement()))
						break;// found
					current = current.getNext();

				}
				return (Brand) current.getNext().getElement();// returning the next of the element found
			}
		}
		return null;// not found
	}

	public Brand getPrevious(Brand element) {// gets the previous node element O(n)

		if (count > 0) {// not empty
			if (element.equals(last.getElement()))// previous of the last O(1)
				return (Brand) last.getPrev().getElement();

			else if (element.equals(first.getElement()))// previous of the first is last O(1)
				return (Brand) last.getElement();

			else {// looping until we find the element O(n)
				DNode current = last.getPrev();

				for (int i = 1; i < count - 1; i++) {
					if (element.equals(current.getElement()))
						break;
					current = current.getPrev();

				}
				return (Brand) current.getPrev().getElement();// returning the previous of the element found
			}
		}
		return null;// not found
	}

	public DNode getFirstNode() {// returns head node O(1)
		if (count == 0)// empty
			return null;
		return first;// not empty

	}

	@Override
	public int getSize() {// return size of CDLL O(1)
		return count;
	}

	@Override
	public Object getFirst() {// returns first element O(1)
		if (count == 0)// empty
			return null;
		return first.getElement();// not empty

	}

	@Override
	public Object getLast() {// returns last element O(1)
		if (count == 0)// empty
			return null;
		return last.getElement();// not empty

	}

	@Override
	public void addFirst(Object element) {// adds element at the beginning of the CDLL O(1)

		if (count == 0) {// list is empty
			first = last = new DNode(element);
			last.setNext(first);
			first.setPrev(last);

		} else {// list isn't empty (adds as a head of CDLL)
			DNode temp = new DNode(element);
			temp.setNext(first);
			first.setPrev(temp);
			temp.setPrev(last);
			last.setNext(temp);
			first = temp;
		}
		count++;

	}

	@Override
	public void addLast(Object element) {// adds element at the end of the CDLL O(1)

		if (count == 0) {// list is empty
			first = last = new DNode(element);
			last.setNext(first);
			first.setPrev(last);

		} else {// list isn't empty(adds as tail of CDLL)
			DNode temp = new DNode(element);
			last.setNext(temp);
			temp.setPrev(last);
			temp.setNext(first);
			first.setPrev(temp);
			last = temp;
		}
		count++;

	}

	@Override
	public void add(Object element, int index) {// adding at a specific index in the CDLL O(n)

		if (count == 0 || index == 1)// adding to the beginning O(1)
			addFirst(element);

		else if (index == count)// adding to the end O(1)
			addLast(element);

		else if (index > count || index <= 0) // index out of bounds
			throw new IllegalArgumentException("Index out of bounds");

		else { // adding in the middle O(n)
			DNode current = first;
			for (int i = 1; i < index - 1; i++)// looping until we reach the right index
				current = current.getNext();

			DNode temp = new DNode(element);
			temp.setNext(current.getNext());
			temp.setPrev(current);
			current.setNext(temp);
			count++;

		}

	}

	@Override
	public boolean deleteFirst() {// deletes first node (head) O(1)

		if (count == 0)// CDLL is empty
			return false;

		if (count == 1) {// only one element
			first = last = null;
			last.setNext(null);
			first.setPrev(null);

		} else {// more than one element
			DNode temp = first;
			first = first.getNext();
			first.setPrev(last);
			last.setNext(first);
			temp.setNext(null);
		}
		count--;
		return true;

	}

	@Override
	public boolean deleteLast() {// deleting last node (tail) O(1)

		if (count == 0)// CDLL is empty
			return false;

		if (count == 1) {// only one element
			first = last = null;
			last.setNext(null);
			first.setPrev(null);

		} else {// more than one element
			DNode temp = last.getPrev();
			last.setPrev(null);
			last.setNext(null);
			last = temp;
			last.setNext(first);
			first.setPrev(last);
		}
		count--;
		return true;

	}

	@Override
	public boolean delete(int index) {// deleting at a certain index O(n)
		DNode current = first;
		DNode previous = null;

		if (index <= 0 || index > count)// index out of bounds
			return false;

		else if (index == 1)// deleting head O(1)
			return deleteFirst();

		else if (index == count)// deleting tail O(1)
			return deleteLast();

		else {// deleting node in the middle O(n)

			for (int i = 0; i < index - 1; i++) {// loop until reaching right index
				previous = current;
				current = current.getNext();
			}

			previous.setNext(current.getNext());
			current.getNext().setPrev(previous);
			current.setNext(null);
		}
		count--;
		return true;

	}

}
