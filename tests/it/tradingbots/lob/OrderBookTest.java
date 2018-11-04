package it.tradingbots.lob;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class OrderBookTest {

	@ParameterizedTest
	@ValueSource(strings = {"true", "false"})
	void testFirstOrder(boolean fromTop) {
		OrderBook book = new OrderBook(50);
		book.bid(50,  1000, fromTop);
		assertIterableEquals(Arrays.asList(new PriceLevel(50, 1000)), book.getBids(0));
	}

	@ParameterizedTest
	@ValueSource(strings = {"true", "false"})
	void testSeveralNewOrders(boolean fromTop) {
		OrderBook book = new OrderBook(50);
		book.bid(50,  1000, fromTop);
		book.bid(70, 1500, fromTop);
		book.bid(60, 500, fromTop);
		assertIterableEquals(Arrays.asList(
				new PriceLevel(70, 1500),
				new PriceLevel(60, 500),
				new PriceLevel(50, 1000)), book.getBids(0));
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"true", "false"})
	void testSeveralNewOrdersSorted(boolean fromTop) {
		OrderBook book = new OrderBook(50);
		book.bid(70,  1000, fromTop);
		book.bid(60, 1500, fromTop);
		book.bid(50, 500, fromTop);
		assertIterableEquals(Arrays.asList(
				new PriceLevel(70, 1000),
				new PriceLevel(60, 1500),
				new PriceLevel(50, 500)), book.getBids(0));
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"true", "false"})
	void testSeveralNewOrdersReverseSorted(boolean fromTop) {
		OrderBook book = new OrderBook(50);
		book.bid(50,  1000, fromTop);
		book.bid(60, 1500, fromTop);
		book.bid(70, 500, fromTop);
		assertIterableEquals(Arrays.asList(
				new PriceLevel(70, 500),
				new PriceLevel(60, 1500),
				new PriceLevel(50, 1000)), book.getBids(0));
	}
}