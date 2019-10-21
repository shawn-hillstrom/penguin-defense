package penguindefense;

import java.util.Random;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

/**
 * A class representing an enemy entity. This entity checks for collisions with Walls
 * and Objectives and does damage to both if a collision is detected.
 * 
 * Penguin entities also rely on an implementation of Dijkstra's to path find through
 * the GameMap, always choosing the path of least resistance.
 */
public class Penguin extends Entity {
	
	public int value;
	private Vector velocity;
	private float speed;
	private PenguinDefenseGame myGame;
	private Entity obj;
	private int count = 1000;
	
	/**
	 * Constructor for a Penguin entity.
	 * 
	 * @param x
	 * - initial x position
	 * @param y
	 * - initial y position
	 * @param s
	 * - initial speed
	 * @param v
	 * - value of penguin
	 * @param game
	 * - reference to the game
	 */
	public Penguin(float x, float y, float s, int v, PenguinDefenseGame game) {
		super(x, y);
		speed = s;
		value = v;
		myGame = game;
		addImageWithBoundingBox(ResourceManager.getImage(PenguinDefenseGame.IMG_PENGUIN));
	}
	
	/**
	 * Set the velocity of this entity.
	 * 
	 * @param x
	 * - x component of the velocity
	 * @param y
	 * - y component of the velocity
	 */
	public void setVelocity(float x, float y) {
		velocity = new Vector(x * speed, y * speed);
	}
	
	/**
	 * Set the velocity of this entity.
	 * 
	 * @param v
	 * - Vector representing velocity
	 */
	public void setVelocity(Vector v) {
		velocity = v.setLength(speed);
	}
	
	/**
	 * Get the velocity of this entity.
	 * 
	 * @return
	 * - Vector representing velocity
	 */
	public Vector getVelocity() {
		return velocity;
	}
	
	/**
	 * Set the objective of this entity.
	 * 
	 * @param e
	 * - the objective entity for this entity
	 */
	public void setObjective(Entity e) {
		obj = e;
		setVelocity(new Vector(e.getX() - this.getX(), e.getY() - this.getY()).unit());
	}
	
	/**
	 * Get the objective of this entity.
	 * 
	 * @return
	 * - the objective entity for this entity
	 */
	public Entity getObjective() {
		return obj;
	}
	
	/**
	 * Set the speed of this entity.
	 * 
	 * @param s
	 * - speed
	 */
	public void setSpeed(float s) {
		speed = s;
		velocity.setLength(s);
	}
	
	/**
	 * Get the speed of this entity.
	 * 
	 * @return
	 * - speed
	 */
	public float getSpeed() {
		return speed;
	}
	
	/**
	 * Check to see whether or not this entity collides with another.
	 * 
	 * @param e
	 * - other entity
	 * @return
	 * - true on collision, false otherwise
	 */
	private boolean collidesWith(Entity e) {
		return (collides(e) != null) ? true : false;
	}
	
	/**
	 * Get an encounter object when this entity collides with another.
	 * 
	 * @return
	 * - a new encounter object
	 */
	public Encounter getEncounter() {
		if (collidesWith(myGame.obj)) {
			return new Encounter(myGame.obj, Encounter.E_OBJ);
		} else {
			for (Wall [] l : myGame.myMap.walls) {
				for (Wall w : l) {
					if (w != null && collidesWith(w)) {
						return new Encounter(w, Encounter.E_WALL);
					}
				}
			}
		}
		return new Encounter(null, Encounter.E_NULL);
	}
	
	/**
	 * Update a penguin based on how much time has passed...
	 * 
	 * @param delta
	 * - number of milliseconds since last update
	 */
	public void update(final int delta) {
		
		Random RNG = new Random();
		if (RNG.nextInt(300) == 0)
			ResourceManager.getSound(PenguinDefenseGame.SND_QUACK).play();
		
		if (obj != myGame.obj && getPosition().distance(obj.getPosition()) <= speed) {
			Tile curTile = (Tile)obj;
			if (curTile.pi != null) {
				setObjective(curTile.pi);
			} else {
				setObjective(myGame.obj);	
			}
		}
		
		Encounter curE = getEncounter();
		if (curE.canInteract()) {
			if (count >= 1000) {
				curE.interact();
				count = 0;
			}
			count += delta;
		} else {
			count = 1000;
			translate(velocity); // move the penguin
		}
	}
	
	/**
	 * A private class representing an encounter between this entity and another. This
	 * object contains information about the nature of the other entity, including
	 * whether or not it can be interacted with, and what type it is specifically.
	 */
	private class Encounter {
		
		public static final int E_NULL = 0;
		public static final int E_OBJ = 1;
		public static final int E_WALL = 2;
		
		private Entity e;
		private int eflag;
		
		public Encounter(Entity e, int eflag) {
			this.e = e;
			this.eflag = eflag;
		}
		
		public boolean canInteract() {
			return (e != null) ? true : false;
		}
		
		public void interact() {
			if (eflag == E_OBJ) {
				Objective o = (Objective)e;
				o.damage();
			} else {
				Wall w = (Wall)e;
				w.damage();
			}
		}
		
	}
}
