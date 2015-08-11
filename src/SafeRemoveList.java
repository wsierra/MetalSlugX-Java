import java.util.ArrayList;

public class SafeRemoveList<E> extends ArrayList<E> {
	
	private static final long serialVersionUID = 1234L;
	
	private ArrayList<Object> remover = new ArrayList<Object>();
	
	public SafeRemoveList() {}
	
	public SafeRemoveList(int initialCapacity) {
		super(initialCapacity);
	}
	
	@Override
	public boolean remove(Object o) {
		remover.add(o);
		return contains(o);
	}
	
	public void compactar() {
		for (Object o : remover)
			super.remove(o);
		remover = new ArrayList<Object>();
	}
}
