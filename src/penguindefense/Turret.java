package penguindefense;

import jig.Entity;
import jig.ResourceManager;

public class Turret extends Entity {

	private float range;
//	private int count = 500;
	
	/**
	 * Constructor for a Turret Entity.
	 * 
	 * @param x
	 * - x coordinate of position
	 * @param y
	 * - y coordinate of position
	 * @param r
	 * - range of turret
	 */
	public Turret(final float x, final float y, float r) {
		super(x, y);
		range = r;
		addImageWithBoundingBox(ResourceManager.getImage(PenguinDefenseGame.IMG_TURRET));
	}
	
	/**
	 * Set the range of this turret.
	 * 
	 * @param r
	 * - new range
	 */
	public void setRange(float r) {
		range = r;
	}
	
	/**
	 * Get the range of this turret.
	 * 
	 * @return
	 * - current range
	 */
	public float getRange() {
		return range;
	}
	
	/**
	 * Update a turret based on how much time has passed...
	 * 
	 * @param delta
	 * - number of milliseconds since last update
	 */
	public void update(final int delta) {
		
	}
}
