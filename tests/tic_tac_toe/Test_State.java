package tic_tac_toe;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class Test_State {

	@Test
	void test() {
		State s;
		
		s = new State(new Integer[]{0, 0, 0, 0, 0, 0, 0, 0, 0});
		assertEquals(s.result(), -1);
		
		s = new State(new Integer[]{1, 0, 0, 1, 0, 0, 0, 0, 1});
		assertEquals(s.result(), -1);
		
		s = new State(new Integer[]{1, 2, 1, 2, 1, 2, 2, 1, 2});
		assertEquals(s.result(), 0);
		
		s = new State(new Integer[]{1, 1, 1, 0, 0, 0, 0, 0, 0});
		assertEquals(s.result(), 1);
		
		s = new State(new Integer[]{0, 0, 0, 2, 2, 2, 0, 0, 0});
		assertEquals(s.result(), 2);
		
		s = new State(new Integer[]{1, 0, 0, 1, 0, 0, 1, 0, 0});
		assertEquals(s.result(), 1);
		
		s = new State(new Integer[]{0, 2, 0, 0, 2, 0, 0, 2, 0});
		assertEquals(s.result(), 2);
		
		s = new State(new Integer[]{1, 0, 0, 0, 1, 0, 0, 2, 1});
		assertEquals(s.result(), 1);
		
		s = new State(new Integer[]{0, 2, 2, 0, 2, 0, 2, 0, 0});
		assertEquals(s.result(), 2);
		
		s = new State(new Integer[]{0, 2, 2, 0, 2, 0, 1, 1, 0});
		assertEquals(s.result(), -1);
	}

}
