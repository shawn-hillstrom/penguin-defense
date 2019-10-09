package penguindefense;

import jig.Entity;
import jig.ResourceManager;

public class Wall extends Entity {

	public String type;
	private int lives = 100;
//	private int count = 200;
	
	/**
	 * Constructor for a Wall Entity.
	 * 
	 * @param x
	 * - x coordinate for position
	 * @param y
	 * - y coordinate for position
	 * @param t
	 * - type of wall (horizontal or vertical)
	 */
	public Wall(final float x, final float y, String t) {
		super(x, y);
		type = t;
		if (t == "horizontal") {
			addImageWithBoundingBox(ResourceManager.getImage(PenguinDefenseGame.IMG_WALL_HOR));
		} else {
			addImageWithBoundingBox(ResourceManager.getImage(PenguinDefenseGame.IMG_WALL_VER));
		}
	}
	
	/**
	 * Set the number of lives for this wall.
	 * 
	 * @param l
	 * - new number of lives
	 */
	public void setLives(int l) {
		lives = l;
	}
	
	/**
	 * Get the number of lives for this wall.
	 * 
	 * @return
	 * - current number of lives
	 */
	public int getLives() {
		return lives;
	}
	
	/**
	 * Damage this wall, decrementing its number of lives by 1 and indicating
	 * damage with a sound and an animation.
	 */
	public void damage() {
		
	}
	
	/**
	 * Update a wall based on how much time has passed...
	 * 
	 * @param delta
	 * - number of milliseconds since last update
	 */
	public void update(final int delta) {
		
	}
}
