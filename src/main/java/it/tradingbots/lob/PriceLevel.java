/*
 * Copyright © 2018 TRADING BOTS SRL. All Rights Reserved.
 * Unauthorized copying of this file via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package it.tradingbots.lob;

/**
 * A price level in an order book.
 */
public final class PriceLevel {

	private final long PRICE;
	private final long VOLUME;

	/**
	 *
	 * @param price
	 * @param volume
	 */
	public PriceLevel(long price, long volume) {
		this.PRICE = price;
		this.VOLUME = volume;
	}

	public long getPrice() {
		return PRICE;
	}

	public long getVolume() {
		return VOLUME;
	}
	
	public boolean equals(Object o) {
		if (o == this) return true;
		if (o instanceof PriceLevel)
			return ((PriceLevel) o).PRICE == PRICE && ((PriceLevel) o).VOLUME == VOLUME;
		else
			return false;
	}
	
	public String toString() {
		return String.format("PriceLevel(%d, %d)", PRICE, VOLUME);
	}
}
