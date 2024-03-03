package application;

public interface List {// interface implemented by both CDLL & SLL

	public void addFirst(Object element);

	public void addLast(Object element);

	public void add(Object element, int index);

	public boolean deleteFirst();

	public boolean deleteLast();

	public boolean delete(int index);

	public boolean delete(Object element);

	public void addSorted(Object element);

	public Object get(Object element);

	public Object getFirst();

	public Object getLast();

	public int getSize();

}
