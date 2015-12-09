package vm;

import org.junit.Before;

import princeGame.UnbreakableStratyegy;

public class UnbreakableStrategyTest extends VmStrategyTest{

	@Override
	@Before
	public void setUp() throws Exception {
		// TODO Auto-generated method stub
		strategy = new UnbreakableStratyegy();
	}
}
