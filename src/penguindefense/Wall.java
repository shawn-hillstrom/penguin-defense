package penguindefense;

import jig.Entity;
import jig.ResourceManager;

public class Wall extends Entity {

	public String type;
	private int lives = 60;
	private boolean broken = false;
	private boolean damaged = false;
	private int count = 0;
	
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
		lives -= 1;
		ResourceManager.getSound(PenguinDefenseGame.SND_DAMAGE).play();
		if (!damaged) {
			damaged = true;
			if (type == "horizontal") {
				addImage(ResourceManager.getImage(PenguinDefenseGame.IMG_WALL_DAMAGED_HOR));
			} else {
				addImage(ResourceManager.getImage(PenguinDefenseGame.IMG_WALL_DAMAGED_VER));
			}
		}
	}
	
	/**
	 * Update a wall based on how much time has passed...
	 * 
	 * @param delta
	 * - number of milliseconds since last update
	 */
	public void update(final int delta) {
		
		if (damaged) {
			count += delta;
			if (count >= 200) {
				damaged = false;
				count = 0;
				removeImage(ResourceManager.getImage(PenguinDefenseGame.IMG_WALL_DAMAGED_HOR));
				removeImage(ResourceManager.getImage(PenguinDefenseGame.IMG_WALL_DAMAGED_VER));
			}
		}
		
		if (lives <= 10 && !broken) {
			broken = true;
			if (type == "horizontal") {
				removeImage(ResourceManager.getImage(PenguinDefenseGame.IMG_WALL_HOR));
				addImage(ResourceManager.getImage(PenguinDefenseGame.IMG_WALL_BROKEN_HOR));
			} else {
				removeImage(ResourceManager.getImage(PenguinDefenseGame.IMG_WALL_VER));
				addImage(ResourceManager.getImage(PenguinDefenseGame.IMG_WALL_BROKEN_VER));
			}
		}
	}
}
