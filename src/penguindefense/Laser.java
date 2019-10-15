package penguindefense;

import jig.Vector;

public class Laser {
	
	public Vector start;
	public Vector end;
	private int count = 80;
	
	public Laser(Vector startPos, Vector endPos) {
		start = startPos;
		end = endPos;
	}
	
	public boolean isActive() {
		return (count >= 0) ? true : false;
	}
	
	/**
	 * Update a laser based on how much time has passed...
	 * 
	 * @param delta
	 * - number of milliseconds since last update
	 */
	public void update(final int delta) {
		count -= delta;
	}
}
