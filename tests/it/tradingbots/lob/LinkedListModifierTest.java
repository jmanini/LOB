package it.tradingbots.lob;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;
import java.util.Optional;

import org.junit.jupiter.api.Test;

class LinkedListModifierTest {
	
	@Test
	void testGetOnEmptyListDescFromTop() {
		LinkedList<PriceLevel> l = new LinkedList<PriceLevel>();
		
		LinkedListModifier<PriceLevel> m = new LinkedListModifier<PriceLevel>(l, LinkedListModifier.Order.DESCENDING, true);
		Optional<PriceLevel> ret = m.get(PriceLevel::getPrice, 50);
		
		assertFalse(ret.isPresent());
	}

	@Test
	void testGetNonExistentOnNonEmptyListDescFromTop() {
		LinkedList<PriceLevel> l = new LinkedList<PriceLevel>();
		l.add(new PriceLevel(50, 1000));
		l.add(new PriceLevel(20, 1500));
		l.add(new PriceLevel(10, 400));
		
		LinkedListModifier<PriceLevel> m = new LinkedListModifier<PriceLevel>(l, LinkedListModifier.Order.DESCENDING, true);
		Optional<PriceLevel> ret = m.get(PriceLevel::getPrice, 30);
		
		assertTrue(ret.isPresent());
		assertEquals(ret.get().getPrice(), 20);
	}
	
	@Test
	void testGetExistentOnNonEmptyListDescFromTop() {
		LinkedList<PriceLevel> l = new LinkedList<PriceLevel>();
		l.add(new PriceLevel(50, 1000));
		l.add(new PriceLevel(20, 1500));
		l.add(new PriceLevel(10, 400));
		
		LinkedListModifier<PriceLevel> m = new LinkedListModifier<PriceLevel>(l, LinkedListModifier.Order.DESCENDING, true);
		Optional<PriceLevel> ret = m.get(PriceLevel::getPrice, 20);
		
		assertTrue(ret.isPresent());
		assertEquals(ret.get().getPrice(), 20);
	}
	
	@Test
	void testGetOnEmptyListDescDescFromBottom() {
		LinkedList<PriceLevel> l = new LinkedList<PriceLevel>();
		
		LinkedListModifier<PriceLevel> m = new LinkedListModifier<PriceLevel>(l, LinkedListModifier.Order.DESCENDING, false);
		Optional<PriceLevel> ret = m.get(PriceLevel::getPrice, 50);
		
		assertFalse(ret.isPresent());
	}

	@Test
	void testGetNonExistentOnNonEmptyListDescFromBottom() {
		LinkedList<PriceLevel> l = new LinkedList<PriceLevel>();
		l.add(new PriceLevel(50, 1000));
		l.add(new PriceLevel(20, 1500));
		l.add(new PriceLevel(10, 400));
		
		LinkedListModifier<PriceLevel> m = new LinkedListModifier<PriceLevel>(l, LinkedListModifier.Order.DESCENDING, false);
		Optional<PriceLevel> ret = m.get(PriceLevel::getPrice, 30);
		
		assertTrue(ret.isPresent());
		assertEquals(ret.get().getPrice(), 50);
	}
	
	@Test
	void testGetExistentOnNonEmptyListDescFromBottom() {
		LinkedList<PriceLevel> l = new LinkedList<PriceLevel>();
		l.add(new PriceLevel(50, 1000));
		l.add(new PriceLevel(20, 1500));
		l.add(new PriceLevel(10, 400));
		
		LinkedListModifier<PriceLevel> m = new LinkedListModifier<PriceLevel>(l, LinkedListModifier.Order.DESCENDING, false);
		Optional<PriceLevel> ret = m.get(PriceLevel::getPrice, 20);
		
		assertTrue(ret.isPresent());
		assertEquals(ret.get().getPrice(), 20);
	}
	
	@Test
	void testGetOnEmptyListAscFromBottom() {
		LinkedList<PriceLevel> l = new LinkedList<PriceLevel>();
		
		LinkedListModifier<PriceLevel> m = new LinkedListModifier<PriceLevel>(l, LinkedListModifier.Order.ASCENDING, false);
		Optional<PriceLevel> ret = m.get(PriceLevel::getPrice, 50);
		
		assertFalse(ret.isPresent());
	}

	@Test
	void testGetNonExistentOnNonEmptyListAscFromBottom() {
		LinkedList<PriceLevel> l = new LinkedList<PriceLevel>();
		l.add(new PriceLevel(10, 1000));
		l.add(new PriceLevel(20, 1500));
		l.add(new PriceLevel(50, 400));
		
		LinkedListModifier<PriceLevel> m = new LinkedListModifier<PriceLevel>(l, LinkedListModifier.Order.ASCENDING, false);
		Optional<PriceLevel> ret = m.get(PriceLevel::getPrice, 30);
		
		assertTrue(ret.isPresent());
		assertEquals(ret.get().getPrice(), 20);
	}
	
	@Test
	void testGetExistentOnNonEmptyListAscFromBottom() {
		LinkedList<PriceLevel> l = new LinkedList<PriceLevel>();
		l.add(new PriceLevel(10, 1000));
		l.add(new PriceLevel(20, 1500));
		l.add(new PriceLevel(50, 400));
		
		LinkedListModifier<PriceLevel> m = new LinkedListModifier<PriceLevel>(l, LinkedListModifier.Order.ASCENDING, false);
		Optional<PriceLevel> ret = m.get(PriceLevel::getPrice, 20);
		
		assertTrue(ret.isPresent());
		assertEquals(ret.get().getPrice(), 20);
	}
	
	@Test
	void testGetOnEmptyListAscFromTop() {
		LinkedList<PriceLevel> l = new LinkedList<PriceLevel>();
		
		LinkedListModifier<PriceLevel> m = new LinkedListModifier<PriceLevel>(l, LinkedListModifier.Order.ASCENDING, true);
		Optional<PriceLevel> ret = m.get(PriceLevel::getPrice, 50);
		
		assertFalse(ret.isPresent());
	}

	@Test
	void testGetNonExistentOnNonEmptyListAscFromTop() {
		LinkedList<PriceLevel> l = new LinkedList<PriceLevel>();
		l.add(new PriceLevel(10, 1000));
		l.add(new PriceLevel(20, 1500));
		l.add(new PriceLevel(50, 400));
		
		LinkedListModifier<PriceLevel> m = new LinkedListModifier<PriceLevel>(l, LinkedListModifier.Order.ASCENDING, true);
		Optional<PriceLevel> ret = m.get(PriceLevel::getPrice, 30);
		
		assertTrue(ret.isPresent());
		assertEquals(ret.get().getPrice(), 50);
	}
	
	@Test
	void testGetExistentOnNonEmptyListAscFromTop() {
		LinkedList<PriceLevel> l = new LinkedList<PriceLevel>();
		l.add(new PriceLevel(10, 1000));
		l.add(new PriceLevel(20, 1500));
		l.add(new PriceLevel(50, 400));
		
		LinkedListModifier<PriceLevel> m = new LinkedListModifier<PriceLevel>(l, LinkedListModifier.Order.ASCENDING, true);
		Optional<PriceLevel> ret = m.get(PriceLevel::getPrice, 20);
		
		assertTrue(ret.isPresent());
		assertEquals(ret.get().getPrice(), 20);
	}
}
