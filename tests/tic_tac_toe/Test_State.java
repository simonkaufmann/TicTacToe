package tic_tac_toe;

import static org.junit.jupiter.api.Assertions.*;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class Test_State {

	@Test
	void test_result() {
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
	
	@Test
	void test_nextSteps() {
		State s;
		
		s = new State(new Integer[] {1, 0, 1, 2, 1, 2, 0, 1, 0});
		
		ArrayList<State> l = s.nextSteps(1);
		ArrayList<State> l2 = new ArrayList<State>();
		l2.add(new State(new Integer[]{1, 1, 1, 2, 1, 2, 0, 1, 0}));
		l2.add(new State(new Integer[]{1, 0, 1, 2, 1, 2, 1, 1, 0}));
		l2.add(new State(new Integer[]{1, 0, 1, 2, 1, 2, 0, 1, 1}));
		
		if (!l.containsAll(l2))
			fail("nextSteps function is not correct");
		
		l = s.nextSteps(2);
		l2 = new ArrayList<State>();
		l2.add(new State(new Integer[]{1, 2, 1, 2, 1, 2, 0, 1, 0}));
		l2.add(new State(new Integer[]{1, 0, 1, 2, 1, 2, 2, 1, 0}));
		l2.add(new State(new Integer[]{1, 0, 1, 2, 1, 2, 0, 1, 2}));
		
	}

}
