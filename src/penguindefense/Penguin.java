package penguindefense;

import java.util.Random;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

public class Penguin extends Entity {
	
	private Vector velocity;
	private float speed;
	
	/**
	 * Constructor for a Penguin entity.
	 * 
	 * @param x
	 * - initial x position
	 * @param y
	 * - initial y position
	 * @param s
	 * - initial speed
	 */
	public Penguin(float x, float y, float s) {
		super(x, y);
		speed = s;
		addImageWithBoundingBox(ResourceManager.getImage(PenguinDefenseGame.IMG_PENGUIN));
	}
	
	/**
	 * Set the velocity of this entity.
	 * 
	 * @param x
	 * - x component of the velocity.
	 * @param y
	 * - y component of the velocity.
	 */
	public void setVelocity(float x, float y) {
		velocity = new Vector(x * speed, y * speed);
	}
	
	/**
	 * Set the velocity of this entity.
	 * 
	 * @param v
	 * - Vector representing velocity.
	 */
	public void setVelocity(Vector v) {
		velocity = v.setLength(speed);
	}
	
	/**
	 * Get the velocity of this entity.
	 * 
	 * @return
	 * - Vector representing velocity.
	 */
	public Vector getVelocity() {
		return velocity;
	}
	
	/**
	 * Set the speed of this entity.
	 * 
	 * @param s
	 * - speed.
	 */
	public void setSpeed(float s) {
		speed = s;
		velocity.setLength(s);
	}
	
	/**
	 * Get the speed of this entity.
	 * 
	 * @return
	 * - speed.
	 */
	public float getSpeed() {
		return speed;
	}
	
	/**
	 * Update a tile based on how much time has passed...
	 * 
	 * @param delta
	 * - number of milliseconds since last update
	 */
	public void update(final int delta) {
		
		Random RNG = new Random();
		
		if (RNG.nextInt(300) == 0)
			ResourceManager.getSound(PenguinDefenseGame.SND_QUACK).play();
		
		translate(velocity); // move the penguin
	}
}
