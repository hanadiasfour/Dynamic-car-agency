package application;

public class SNode {// the node holds the location object and next addresses in CDLL

	private Object element;
	private SNode next;

	public SNode(Object element) {
		this.element = element;

	}

	public Object getElement() {
		return element;
	}

	public void setElement(Object element) {
		this.element = element;
	}

	public SNode getNext() {
		return next;
	}

	public void setNext(SNode next) {
		this.next = next;
	}

	@Override
	public String toString() {
		return "Node [element=" + element;
	}

}
