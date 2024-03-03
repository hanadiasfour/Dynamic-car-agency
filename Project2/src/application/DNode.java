package application;

public class DNode {// the node holds the location object and next/prev addresses in CDLL

	private Object element;
	private DNode next, prev;

	public DNode(Object element) {
		this.element = element;
	}

	// getters & setters
	public Object getElement() {
		return element;
	}

	public void setElement(Object element) {
		this.element = element;
	}

	public DNode getNext() {
		return next;
	}

	public void setNext(DNode next) {
		this.next = next;
	}

	public DNode getPrev() {
		return prev;
	}

	public void setPrev(DNode prev) {
		this.prev = prev;
	}

	@Override
	public String toString() {
		return "DNode [element=" + element + "]";
	}

}
