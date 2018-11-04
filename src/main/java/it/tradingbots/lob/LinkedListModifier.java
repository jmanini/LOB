package it.tradingbots.lob;

import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Optional;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToLongFunction;

public class LinkedListModifier<T> {
	
	enum Order {
		ASCENDING,
		DESCENDING
	}

	private ListIterator<T> iterator;
	private Supplier<T> forward;
	private Supplier<T> back;
	private BooleanSupplier more;
	private Order order;
	private boolean fromTop;

	public LinkedListModifier(LinkedList<T> list, Order order, boolean fromTop) {
		iterator = list.listIterator(fromTop ? 0 : list.size());

		forward = fromTop ? iterator::next : iterator::previous;
		back = !fromTop ? iterator::next : iterator::previous;
		more = fromTop ? iterator::hasNext : iterator::hasPrevious;
		
		this.order = order;
		this.fromTop = fromTop;
	}
	
	public Optional<T> get(ToLongFunction<T> key, long target) {
		/* fromTop    descending
		 *      T                  T             key <= p
		 *      F                  T             key >= p
		 *      T                  F             key >= p
		 *      F                  F             key <= p
		 */
		Predicate<T> found = fromTop == (order == Order.DESCENDING) ? (t -> key.applyAsLong(t) <= target) : (t -> key.applyAsLong(t) <= target);
		
		while (more.getAsBoolean()) {
			T current = forward.get();
			if (found.test(current))
				return Optional.of(current);
		}
		return Optional.empty();	
	}
	
	public void add(T e) {
		if (iterator.hasPrevious() || iterator.hasNext())
			back.get();
		iterator.add(e);
	}
	
	public void set(T e) {
		iterator.set(e);
	}
	
	public void remove() {
		iterator.remove();
	}
}
