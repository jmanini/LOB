package it.tradingbots.lob;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class OrderBookTest {

	@ParameterizedTest
	@ValueSource(strings = {"true", "false"})
	void testRandomCreate(boolean fromTop) {
		OrderBook book = new OrderBook(50);
		
		book.bid(50,  1000, fromTop);
		assertIterableEquals(Arrays.asList(new PriceLevel(50, 1000)), book.getBids(0));
		
		book.bid(70,  500, fromTop);
		assertIterableEquals(Arrays.asList(
				new PriceLevel(70, 500),
				new PriceLevel(50, 1000)), book.getBids(0));
		
		book.bid(60,  1500, fromTop);
		assertIterableEquals(Arrays.asList(
				new PriceLevel(70, 500),
				new PriceLevel(60, 1500),
				new PriceLevel(50, 1000)), book.getBids(0));
	}

	@ParameterizedTest
	@ValueSource(strings = {"true", "false"})
	void testOrderedCreate(boolean fromTop) {
		OrderBook book = new OrderBook(50);
		book.bid(70,  500, fromTop);
		assertIterableEquals(Arrays.asList(new PriceLevel(70, 500)), book.getBids(0));
		
		book.bid(60,  1500, fromTop);
		assertIterableEquals(Arrays.asList(
				new PriceLevel(70, 500),
				new PriceLevel(60, 1500)), book.getBids(0));
		
		book.bid(50,  1000, fromTop);
		assertIterableEquals(Arrays.asList(
				new PriceLevel(70, 500),
				new PriceLevel(60, 1500),
				new PriceLevel(50, 1000)), book.getBids(0));
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"true", "false"})
	void testReverseOrderedCreate(boolean fromTop) {
		OrderBook book = new OrderBook(50);
		book.bid(50, 1000, fromTop);
		assertIterableEquals(Arrays.asList(new PriceLevel(50, 1000)), book.getBids(0));
		
		book.bid(60,  1500, fromTop);
		assertIterableEquals(Arrays.asList(
				new PriceLevel(60, 1500),
				new PriceLevel(50, 1000)), book.getBids(0));
		
		book.bid(70,  500, fromTop);
		assertIterableEquals(Arrays.asList(
				new PriceLevel(70, 500),
				new PriceLevel(60, 1500),
				new PriceLevel(50, 1000)), book.getBids(0));
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"true", "false"})
	void testUpdate(boolean fromTop) {
		OrderBook book = new OrderBook(50);
		book.bid(60,  1000, fromTop);
		book.bid(50, 1500, fromTop);
		book.bid(70, 500, fromTop);
		
		book.bid(60, 300, fromTop);
		assertIterableEquals(Arrays.asList(
				new PriceLevel(70, 500),
				new PriceLevel(60, 300),
				new PriceLevel(50, 1500)), book.getBids(0));
		
		book.bid(70, 100, fromTop);
		assertIterableEquals(Arrays.asList(
				new PriceLevel(70, 100),
				new PriceLevel(60, 300),
				new PriceLevel(50, 1500)), book.getBids(0));
		
		book.bid(50, 7000, fromTop);
		assertIterableEquals(Arrays.asList(
				new PriceLevel(70, 100),
				new PriceLevel(60, 300),
				new PriceLevel(50, 7000)), book.getBids(0));
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"true", "false"})
	void testRemove(boolean fromTop) {
		OrderBook book = new OrderBook(50);
		book.bid(60,  1000, fromTop);
		book.bid(50, 1500, fromTop);
		book.bid(70, 500, fromTop);
		
		book.bid(60, 0, fromTop);
		assertIterableEquals(Arrays.asList(
				new PriceLevel(70, 500),
				new PriceLevel(50, 1500)), book.getBids(0));
		
		book.bid(70, 0, fromTop);
		assertIterableEquals(Arrays.asList(
				new PriceLevel(50, 1500)), book.getBids(0));
		
		book.bid(50, 0, fromTop);
		assertEquals(0, book.getBids(0).size());
	}
	
	@Test
	void testEmptyToJSON() {
		OrderBook book = new OrderBook(50);
		long timestamp = book.getTimestamp();
		assertEquals(String.format("{\"timestamp\": %d, \"bids\": [], \"asks\": []}", timestamp), book.toJSONObject(0));
	}
	
	@Test
	void testSingleBidToJSON() {
		OrderBook book = new OrderBook(50);
		long timestamp = book.getTimestamp();
		book.bid(60,  1000, true);
		assertEquals(String.format("{\"timestamp\": %d, \"bids\": [[60,1000]], \"asks\": []}", timestamp), book.toJSONObject(0));
	}
	
	@Test
	void testSingleAskToJSON() {
		OrderBook book = new OrderBook(50);
		long timestamp = book.getTimestamp();
		book.ask(60,  1000, true);
		assertEquals(String.format("{\"timestamp\": %d, \"bids\": [], \"asks\": [[60,1000]]}", timestamp), book.toJSONObject(0));
	}
	
	@Test
	void testToJSON() {
		OrderBook book = new OrderBook(50);
		long timestamp = book.getTimestamp();
		book.bid(60,  1000, true);
		book.bid(20,  400, true);
		book.bid(90,  700, true);
		
		book.ask(10,  200, true);
		book.ask(80,  100, true);
		assertEquals(String.format("{\"timestamp\": %d, \"bids\": [[90,700], [60,1000], [20,400]], \"asks\": [[10,200], [80,100]]}", timestamp), book.toJSONObject(0));
	}
	
	@Test
	void testBidVolumeAtPrice() {
		OrderBook book = new OrderBook(50);
		book.bid(90,  1000, true);
		book.bid(60,  400, true);
		book.bid(40,  700, true);
		book.bid(20,  300, true);
		book.bid(10,  1700, true);
		assertEquals(1000 + 400 + 700, book.bidVolumeAtPrice(40));
	}
	
	@Test
	void testAskVolumeAtPrice() {
		OrderBook book = new OrderBook(50);
		book.ask(90,  1000, true);
		book.ask(60,  400, true);
		book.ask(40,  700, true);
		book.ask(20,  300, true);
		book.ask(10,  1700, true);
		assertEquals(700 + 300 + 1700, book.askVolumeAtPrice(40));
	}
	
	@Test
	void testDiffRemove() {
		List<PriceLevel> original = Arrays.asList(
				new PriceLevel(50, 1000),
				new PriceLevel(20, 500));
		List<PriceLevel> newOne = Arrays.asList();
		assertIterableEquals(Arrays.asList(
				new PriceLevel(50, 0),
				new PriceLevel(20, 0)), AggregatedOrderBook.diff(original, newOne));
	}
	
	@Test
	void testDiffAdd() {
		List<PriceLevel> original = Arrays.asList();
		List<PriceLevel> newOne = Arrays.asList(
				new PriceLevel(50, 1000),
				new PriceLevel(20, 500));
		assertIterableEquals(Arrays.asList(
				new PriceLevel(50, 1000),
				new PriceLevel(20, 500)), AggregatedOrderBook.diff(original, newOne));
	}
	
	@Test
	void testDiffNoChange() {
		List<PriceLevel> original = Arrays.asList(
				new PriceLevel(50, 1000),
				new PriceLevel(20, 500));
		List<PriceLevel> newOne = Arrays.asList(
				new PriceLevel(50, 1000),
				new PriceLevel(20, 500));
		assertIterableEquals(Arrays.asList(), AggregatedOrderBook.diff(original, newOne));
	}
	
	@Test
	void testDiffUpdate() {
		List<PriceLevel> original = Arrays.asList(
				new PriceLevel(50, 1000),
				new PriceLevel(20, 500));
		List<PriceLevel> newOne = Arrays.asList(
				new PriceLevel(50, 700),
				new PriceLevel(20, 550));
		assertIterableEquals(Arrays.asList(
				new PriceLevel(50, 700),
				new PriceLevel(20, 550)), AggregatedOrderBook.diff(original, newOne));
	}
}