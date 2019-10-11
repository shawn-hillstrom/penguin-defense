package penguindefense;

import jig.Entity;
import jig.ResourceManager;

/**
 * A class representing an Objective entity. An objective takes damage when an enemy
 * comes into contact with it, changing its image for a short time and playing a
 * damage sound.
 */
public class Objective extends Entity {

	private int lives;
	private boolean damaged = false;
	private int count = 0;
	
	/**
	 * Constructor for an Objective entity.
	 * 
	 * @param x
	 * - x coordinate for objective.
	 * @param y
	 * - y coordinate for objective.
	 * @param l
	 * - initial number of lives.
	 */
	public Objective(final float x, final float y, int l) {
		super(x, y);
		lives = l;
		addImageWithBoundingBox(ResourceManager.getImage(PenguinDefenseGame.IMG_IGLOO));
	}
	
	/**
	 * Set the lives of this Objective entity.
	 * 
	 * @param l
	 * - new amount of lives.
	 */
	public void setLives(int l) {
		lives = l;
	}
	
	/**
	 * Get the lives of this Objective entity.
	 * 
	 * @return
	 * - number of lives.
	 */
	public int getLives() {
		return lives;
	}
	
	/**
	 * Damage this entity by decrementing its number of lives, changing the image, and
	 * playing a damage sound.
	 */
	public void damage() {
		lives -= 1;
		ResourceManager.getSound(PenguinDefenseGame.SND_DAMAGE).play();
		if (!damaged) {
			damaged = true;
			addImage(ResourceManager.getImage(PenguinDefenseGame.IMG_IGLOO_DAMAGED));
		}
	}
	
	/**
	 * Update an objective based on how much time has passed...
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
				removeImage(ResourceManager.getImage(PenguinDefenseGame.IMG_IGLOO_DAMAGED));
			}
		}
	}
}
