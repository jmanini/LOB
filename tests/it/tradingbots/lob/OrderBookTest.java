package it.tradingbots.lob;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

class OrderBookTest {

	@Test
	void testFirstOrderFromTop() {
		OrderBook book = new OrderBook(50);
		book.bid(50,  1000, true);
		assertIterableEquals(Arrays.asList(new PriceLevel(50, 1000)), book.getBids(0));
	}

	@Test
	void testFirstOrderFromBottom() {
		OrderBook book = new OrderBook(50);
		book.bid(50,  1000, false);
		assertIterableEquals(Arrays.asList(new PriceLevel(50, 1000)), book.getBids(0));
	}

	@Test
	void testSeveralNewOrdersFromTop() {
		OrderBook book = new OrderBook(50);
		book.bid(50,  1000, true);
		book.bid(70, 1500, true);
		book.bid(60, 500, true);
		assertIterableEquals(Arrays.asList(
				new PriceLevel(70, 1500),
				new PriceLevel(60, 500),
				new PriceLevel(50, 1000)), book.getBids(0));
	}
	
	@Test
	void testSeveralNewOrdersFromTopSorted() {
		OrderBook book = new OrderBook(50);
		book.bid(70,  1000, true);
		book.bid(60, 1500, true);
		book.bid(50, 500, true);
		assertIterableEquals(Arrays.asList(
				new PriceLevel(70, 1000),
				new PriceLevel(60, 1500),
				new PriceLevel(50, 500)), book.getBids(0));
	}
	
	@Test
	void testSeveralNewOrdersFromBottom() {
		OrderBook book = new OrderBook(50);
		book.bid(50,  1000, false);
		book.bid(70, 1500, false);
		book.bid(60, 500, false);
		assertIterableEquals(Arrays.asList(
				new PriceLevel(70, 1500),
				new PriceLevel(60, 500),
				new PriceLevel(50, 1000)), book.getBids(0));
	}
}
