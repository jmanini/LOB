/*
 * Copyright © 2018 TRADING BOTS SRL. All Rights Reserved.
 * Unauthorized copying of this file via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package it.tradingbots.lob;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class implements an aggregated limit order book.
 * <p>
 * Price and volume data are stored in an immutable PriceLevel object. Only one
 * PriceLevel object per price is allowed.
 *
 */
public abstract class AggregatedOrderBook {

	protected final int MAX_DEPTH; // Max number of price levels in both bids and asks.
	protected long timestamp; // A timestamp with default value the time of the order book instantiation.

	/**
	 * Computes the set of operations required to convert the original price level
	 * list (bids or asks) to the new price level list.
	 * <p>
	 * Each operation is encoded as a PriceLevel object containing the price and
	 * volume of the operation.<br>
	 * If volume &lt;=0 then the price level must be deleted otherwise the price
	 * level must be created if non existent or its volume updated if existent.<br>
	 * <p>
	 * An example: [[1200,5600][1500,1800][1520,0]] meaning:<br>
	 * Update or create 1200 price level with volume 5600<br>
	 * Update or create 1500 price level with volume 1800<br>
	 * Delete price level 1520<br>
	 * 
	 * @param originalList The original price level list
	 * @param newList      The new price level list.
	 * @return
	 */
	public static final List<PriceLevel> diff(List<PriceLevel> originalList, List<PriceLevel> newList) {
		HashMap<Long, Long> originalMap = new HashMap<Long, Long>();
		HashMap<Long, Long> newMap = new HashMap<Long, Long>();

		for (PriceLevel i : originalList)
			originalMap.put(i.getPrice(), i.getVolume());
		for (PriceLevel i : newList)
			newMap.put(i.getPrice(), i.getVolume());

		List<PriceLevel> result = new ArrayList<PriceLevel>();

		for (Long key : originalMap.keySet()) {
			// if (p1, v1) ∈ originalLits, (p1, v2) ∈ newList / v1 != v2 -> (p1, v2) ∈ diff
			if (newMap.containsKey(key) && !originalMap.get(key).equals(newMap.get(key))) {
				PriceLevel pl = new PriceLevel(key, newMap.get(key));
				result.add(pl);
			}
			// if (p1, v1) ∈ originalList, !∃ v2 | (p1, v2) ∈ newList -> (p1, 0) ∈ diff
			else if (!newMap.containsKey(key)) {
				PriceLevel pl = new PriceLevel(key, 0);
				result.add(pl);
			}
		}

		// if !∃ v1 | (p1, v1) ∈ originalList , (p1, v2) ∈ newList -> (p1, v2) ∈ diff
		for (Long key : newMap.keySet()) {
			if (!originalMap.containsKey(key)) {
				PriceLevel pl = new PriceLevel(key, newMap.get(key));
				result.add(pl);
			}
		}

		return result;
	}

	/**
	 *
	 * @param maxDepth &lt;=0 MAX_DEPTH = Integer.MAX_VALUE.
	 */
	public AggregatedOrderBook(int maxDepth) {
		if (maxDepth <= 0) {
			this.MAX_DEPTH = Integer.MAX_VALUE;
		} else {
			this.MAX_DEPTH = maxDepth;
		}
		this.timestamp = System.currentTimeMillis();
	}

	/**
	 * Returns the timestamp of the order book. If the value was not explicitly set
	 * then it returns the default value that is the system's time at object
	 * instantiation.
	 *
	 * @return long
	 */
	public long getTimestamp() {
		return timestamp;
	}

	/**
	 * Sets the order book's timestamp.
	 *
	 * @param timestamp The timestamp to be set.
	 */
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * Returns the bids list ordered by price descending.
	 *
	 * @param maxLevels The maximum number of price levels to return. If &lt= 0
	 *                  returns all price levels.
	 * @return
	 */
	public abstract List<PriceLevel> getBids(int maxLevels);

	/**
	 * Returns the asks lists ordered by price ascending.
	 *
	 * @param maxLevels The maximum number of price levels to return. If &lt= 0
	 *                  returns all price levels.
	 * @return
	 */
	public abstract List<PriceLevel> getAsks(int maxLevels);

	/**
	 * Clear the contents of the order book.
	 */
	public abstract void clear();

	/**
	 * Create, update or delete a bid in the order book.
	 * <p>
	 * If the volume is &lt;= 0 the price level is deleted.
	 * <p>
	 * If the volume is &gt; 0 then create or update the bid accordingly.
	 *
	 * @param price   Price
	 * @param volume  Volume
	 * @param fromTop true Start iteration from top of the bids list (descending
	 *                order). Set to true if the price is expected to be closer to
	 *                the top than the bottom of the bids list. Set false otherwise.
	 */
	public abstract void bid(long price, long volume, boolean fromTop);

	/**
	 * Create, update or delete an ask in the order book.
	 * <p>
	 * If the volume is &lt;= 0 the price level is deleted.
	 * <p>
	 * If the volume is &gt; 0 then create or update the bid accordingly.
	 *
	 * @param price   Price
	 * @param volume  Volume
	 * @param fromTop true Start iteration from top of asks list (ascending order).
	 *                Set to true if the price is expected to be closer to the top
	 *                than the bottom of the asks list. Set false otherwise.
	 */
	public abstract void ask(long price, long volume, boolean fromTop);

	/**
	 * The book is empty if both the bids and asks lists are empty.
	 *
	 * @return true if there are now bids and asks in the book.
	 */
	public abstract boolean isEmpty();

	/**
	 * Check if the order book is consistent. In a consistent book the prices of all
	 * bids are less than the prices of all asks.
	 *
	 * @return true if bids &lt;= asks for all bids and asks.
	 */
	public abstract boolean isConsistent();

	/**
	 * Computes the sum of volume of bids that have prices as good as the price
	 * limit. These are price levels with prices &gt;= priceLimit.
	 *
	 * @param priceLimit The cut-off price.
	 * @return long
	 */
	public abstract long bidVolumeAtPrice(long priceLimit);

	/**
	 * Computes the sum of volume of asks that have prices as good as the price
	 * limit. For asks these are price levels with prices &lt;= priceLimit.
	 *
	 * @param priceLimit The cut-off price.
	 * @return long
	 */
	public abstract long askVolumeAtPrice(long priceLimit);

	/**
	 * Serializes the order book as a String in JSON format.
	 * <p>
	 * The JSON format is as follows:
	 * {"timestamp":&lt;timestamp&gt;,"bids":"[&lt;bids array&gt;]","asks":"[
	 * &lt;asks array&gt;]}"
	 *
	 * @param maxLevels The maximum number of price levels to return. If &lt= 0
	 *                  returns all price levels.
	 * @return String
	 */
	public abstract String toJSONObject(int maxLevels);
;
}
