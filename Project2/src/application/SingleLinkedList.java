package application;

public class SingleLinkedList implements List {// overrides unimplemented methods in List interface

	private SNode first, last;
	private int count;// number of nodes

	@Override
	public void addSorted(Object element) {// adds element at its right location in the SLL O(n)

		SNode temp = new SNode(element);
		SNode prev = first, curr = first;

		// looping the CDLL until reaching a node with an element that is greater
		while (curr != null && ((Car) curr.getElement()).compareTo((Car) element) < 0) {
			prev = curr;
			curr = curr.getNext();
		}
		if (count == 0) {// empty list O(1)
			first = last = temp;
		} else if (curr == prev) { // insert at head (loop wasn't entered O(1))
			temp.setNext(first);
			first = temp;
		} else { // insert between or at last O(n)
			temp.setNext(curr);
			prev.setNext(temp);
		}
		count++;

	}

	@Override
	public boolean delete(Object element) {
		SNode current = first.getNext();
		SNode prev = first;

		if (count != 0) {// list not empty
			if (element.equals(first.getElement()))
				return deleteFirst();// deleting first

			else if (element.equals(last.getElement()))
				return deleteLast();// deleting last

			else {// deleting node in-between
				for (int i = 1; i < count - 1; i++) {// looping the SLL until element is matched to delete O(n)
					if (element.equals((current.getElement())))
						break;
					prev = current;
					current = current.getNext();

				}
				prev.setNext(current.getNext());
				current.setNext(null);
				count--;
				return true;
			}
		}
		return false;// not found
	}

	public Object get(String name) {// gets Car with the same name O(n)

		if (count != 0) {// list is not empty
			if (((Car) getFirst()).getModel().toLowerCase().contains(name.toLowerCase()))
				return getFirst();// first element O(1)

			else if (((Car) getLast()).getModel().toLowerCase().contains(name.toLowerCase()))
				return getLast();// last element O(1)

			else {// element in the middle of the SLL O(n)
				SNode current = first.getNext();
				for (int i = 1; i < count; i++) {// looping SLL until name matched O(n)
					if (((Car) current.getElement()).getModel().toLowerCase().contains(name.toLowerCase()))
						return current.getElement();
					current = current.getNext();

				}
			}
		}
		return null;// not found
	}

	@Override
	public Object get(Object element) {// gets martyr object O(n)

		if (count != 0) {// not empty
			if (((Car) element).equals((Car) getFirst()))
				return getFirst();// returning first O(1)

			else if (((Car) element).equals((Car) getLast()))
				return getLast();// returning last O(1)

			else {// element in the middle of the SLL O(n)
				SNode current = first.getNext();
				for (int i = 1; i < count; i++) {
					if (((Car) current.getElement()).equals((Car) element))
						return current.getElement();
					current = current.getNext();
				}
			}
		}
		return null;// not found

	}

	public SNode getFirstNode() {// returns head node O(1)

		if (count == 0)// empty
			return null;
		return first;// not empty

	}

	@Override
	public int getSize() {// return size of SLL O(1)
		return count;
	}

	@Override
	public Object getFirst() {
		if (count == 0)// empty
			return null;

		return first.getElement();// not empty

	}

	public Object get(int index) {// returns element with certain index O(n)

		if (count != 0) {// not empty
			if (index == 1)
				return getFirst();// returning first O(1)

			else if (index == count)
				return getLast();// returning last O(1)

			else {// element in the middle of the SLL O(n)
				SNode current = first;
				for (int i = 1; i <= count - 1; i++) {
					if (i == index)
						return current.getElement();
					current = current.getNext();
				}
			}
		}
		return null;// element not found

	}

	@Override
	public boolean deleteFirst() {// deletes first node (head) O(1)
		if (count == 0)// empty SLL
			return false;

		else if (count == 1)// only one element
			first = last = null;

		else {// more than one element
			SNode temp = first;
			first = first.getNext();
			temp.setNext(null);
		}
		count--;
		return true;

	}

	@Override
	public boolean deleteLast() {// deletes last node O(n)
		if (count == 0)// empty SLL
			return false;

		else if (count == 1)// only one element
			first = last = null;

		else {// more than one element O(n)
			SNode current = first;
			for (int i = 0; i < count - 2; i++)// stops at before last
				current = current.getNext();

			last = current;
			last.setNext(null);
		}
		count--;
		return true;

	}

	@Override
	public boolean delete(int index) {// deleting at a certain index O(n)
		SNode current = first;
		SNode prev = null;

		if (index <= 0 || index > count)// index out of bounds
			return false;

		else if (index == 1)// deleting head O(1)
			return deleteFirst();

		else if (index == count)// deleting last O(1
			return deleteLast();

		else {// deleting node in the middle O(n)
			for (int i = 0; i < index - 1; i++) {// loop until reaching right index
				prev = current;
				current = current.getNext();
			}

			prev.setNext(current.getNext());
			current.setNext(null);
		}
		count--;
		return true;

	}

	@Override
	public Object getLast() {
		if (count == 0)// empty
			return null;

		return last.getElement();// not empty
	}

	@Override
	public void addFirst(Object element) {// adds element at the beginning of the SLL O(1)

		if (count == 0)// empty
			first = last = new SNode(element);

		else {// list isn't empty (adds as a head of SLL)
			SNode temp = new SNode(element);
			temp.setNext(first);
			first = temp;
		}
		count++;

	}

	@Override
	public void addLast(Object element) {// adds element at the end of the SLL O(1)

		if (count == 0)// empty
			first = last = new SNode(element);

		else {// list isn't empty (adds as a end of SLL)
			SNode temp = new SNode(element);
			last.setNext(temp);
			last = temp;
		}
		count++;

	}

	@Override
	public void add(Object element, int index) {// adding at a specific index in the SLL O(n)

		if (count == 0 || index == 1)// adding to the beginning O(1)
			addFirst(element);

		else if (index == count)// adding to the end O(1)
			addLast(element);

		else if (index > count || index <= 0) // index out of bounds
			throw new IllegalArgumentException("Index out of bounds");

		else {// adding at a specific index in the SLL O(n)
			SNode current = first;
			for (int i = 1; i < index - 1; i++)
				current = current.getNext();

			SNode temp = new SNode(element);
			temp.setNext(current.getNext());
			current.setNext(temp);
			count++;

		}

	}

	public boolean isEmpty() {

		return first == null;

	}

}