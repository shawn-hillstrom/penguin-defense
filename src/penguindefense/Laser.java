package penguindefense;

import jig.Vector;

/**
 * A class representing a laser. Lasers spawn and then are destroyed when their timer
 * is finished.
 */
public class Laser {
	
	public Vector start;
	public Vector end;
	private int count = 80;
	
	/**
	 * Constructor for a laser.
	 * 
	 * @param startPos
	 * - start point for the laser
	 * @param endPos
	 * - end point for the laser
	 */
	public Laser(Vector startPos, Vector endPos) {
		start = startPos;
		end = endPos;
	}
	
	/**
	 * Check whether or not this laser is active.
	 * 
	 * @return
	 * - false if the timer is up, true otherwise
	 */
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
