package penguindefense;

import java.util.Iterator;

import jig.Entity;
import jig.ResourceManager;

public class Turret extends Entity {

	private float range;
	private GameMap myMap;
	private int count = 500;
	
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
	public Turret(final float x, final float y, float r, GameMap map) {
		super(x, y);
		range = r;
		myMap = map;
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
	
	public void fire (Penguin p) {
		ResourceManager.getSound(PenguinDefenseGame.SND_SHOT).play();
		myMap.myGame.lasers.add(new Laser(this.getPosition(), p.getPosition()));
		myMap.myGame.enemies.remove(p);
	}
	
	/**
	 * Update a turret based on how much time has passed...
	 * 
	 * @param delta
	 * - number of milliseconds since last update
	 */
	public void update(final int delta) {
		
		Penguin target = null;
		float distance = range;
		
		if (count >= 500) {
			for (Iterator <Penguin> i = myMap.myGame.enemies.iterator(); i.hasNext();) {
				Penguin p = i.next();
				float d = this.getPosition().distance(p.getPosition());
				if (d <= distance) {
					target = p;
					distance = d;
				}
			}
			if (target != null) {
				fire(target);
				count = 0;
			}
		} else {
			count += delta;
		}
	}
}
