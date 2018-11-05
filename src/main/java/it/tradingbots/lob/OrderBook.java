package it.tradingbots.lob;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class OrderBook extends AggregatedOrderBook {
	private LinkedList<PriceLevel> asks;
	private LinkedList<PriceLevel> bids;
	
	public OrderBook(int maxDepth) {
		super(maxDepth);
		asks = new LinkedList<PriceLevel>();
		bids = new LinkedList<PriceLevel>();
	}

	@Override
	public List<PriceLevel> getBids(int maxLevels) {
		if (maxLevels <= 0) {
			return bids;
		} else {
			return bids.subList(0, Math.min(maxLevels, bids.size()));
		}
	}

	@Override
	public List<PriceLevel> getAsks(int maxLevels) {
		if (maxLevels <= 0) {
			return asks;
		} else {
			return asks.subList(0, Math.min(maxLevels, asks.size()));
		}
	}

	@Override
	public void clear() {
		asks.clear();
		bids.clear();
	}
	
	private void insert(PriceLevel pl, LinkedListModifier<PriceLevel> modifier) {
		Optional<PriceLevel> insertionPoint = modifier.get(PriceLevel::getPrice, pl.getPrice());
		if (!insertionPoint.isPresent() && pl.getVolume() > 0) {
			/* New element goes to an endpoint (first element is a particular case of that). */
			modifier.add(pl);
		} else if (insertionPoint.isPresent()) {
			if (insertionPoint.get().getPrice() == pl.getPrice()) {
				/* Update */
				if (pl.getVolume() > 0)
					modifier.set(pl);
				else
					modifier.remove();
			} else /* Create */
				modifier.add(pl);
		}
	}

	@Override
	public void bid(long price, long volume, boolean fromTop) {
		LinkedListModifier<PriceLevel> modifier = new LinkedListModifier<PriceLevel>(bids, LinkedListModifier.Order.DESCENDING, fromTop);
		insert(new PriceLevel(price, volume), modifier);
	}
	

	@Override
	public void ask(long price, long volume, boolean fromTop) {
		LinkedListModifier<PriceLevel> modifier = new LinkedListModifier<PriceLevel>(asks, LinkedListModifier.Order.ASCENDING, fromTop);
		insert(new PriceLevel(price, volume), modifier);
	}

	@Override
	public boolean isEmpty() {
		return bids.isEmpty() && asks.isEmpty();
	}

	@Override
	public boolean isConsistent() {
		return bids.size() == 0 || asks.size() == 0 || bids.getFirst().getPrice() <= asks.getFirst().getPrice();
	}
	
	private long cumulateVolumeWhile(List<PriceLevel> list, Predicate<PriceLevel> p) {
		long ret = 0;
		for (PriceLevel pl : list) {
			if (p.test(pl))
				ret += pl.getVolume();
			else	/* list is expected to be sorted wrt p, so once it's false we're done */
				break;
		}
		return ret;
	}

	@Override
	public long bidVolumeAtPrice(long priceLimit) {
		return cumulateVolumeWhile(bids, (pl -> pl.getPrice() >= priceLimit));
	}

	@Override
	public long askVolumeAtPrice(long priceLimit) {
		return cumulateVolumeWhile(asks, (pl -> pl.getPrice() <= priceLimit));
	}
	
	private void toJSONObject(PriceLevel pl, StringBuilder target) {
		target.append('[');
		target.append(pl.getPrice());
		target.append(',');
		target.append(pl.getVolume());
		target.append(']');
	}
	
	private void toJSONObject(List<PriceLevel> list, StringBuilder target) {
		target.append('[');
		Iterator<PriceLevel> it = list.iterator();
		if (it.hasNext()) {
			toJSONObject(it.next(), target);

			while (it.hasNext()) {
				target.append(", ");
				toJSONObject(it.next(), target);
			}
		}
		target.append(']');
	}

	@Override
	public String toJSONObject(int maxLevels) {
		StringBuilder ret = new StringBuilder(String.format("{\"timestamp\": %d, \"bids\": ", getTimestamp()));
		
		toJSONObject(getBids(maxLevels), ret);
		ret.append(", \"asks\": ");
		toJSONObject(getAsks(maxLevels), ret);
		ret.append('}');
			
		return ret.toString();
	}

}
