package penguindefense;

import java.util.Stack;

import org.newdawn.slick.Input;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

/**
 * A class representing a tile entity. Tiles make up the game map and are stationary.
 * Each tile has a type, and some may be fortified based on their type. 
 * 
 * Tiles that support fortification wait for a player to click on them, and if the 
 * player has sufficient funds, a respective fortification is built.
 */
public class Tile extends Entity implements Comparable<Tile> {

	public String type;
	public double dVal = 0;
	public Tile pi = null;
	private GameMap myMap;
	private boolean fortified = false;
	private boolean hover = false;
	
	/**
	 * Constructor for a Tile object.
	 * 
	 * @param x
	 * - x coordinate for tile
	 * @param y
	 * - y coordinate for tile
	 * @param type
	 * - type of tile, stored in a String
	 * @param img
	 * - image for tile
	 * @param map
	 * - reference to GameMap object
	 */
	public Tile(final float x, final float y, String type, String img, GameMap map) {
		super(x, y);
		this.type = type;
		myMap = map;
		addImageWithBoundingBox(ResourceManager.getImage(img));
	}
	
	/**
	 * Check to see whether a tile is fortified
	 * 
	 * @return
	 * - whether or not the tile is fortified
	 */
	public boolean isFortified() {
		return fortified;
	}
	
	public void setFortified(boolean b) {
		fortified = b;
	}
	
	public void fortify(String type) {
		
	}
	
	public void unfortify(String type) {
		
	}
	
	public Stack<Tile> successors() {
		
		Stack<Tile> s = new Stack<Tile>();
		Vector hashPos = myMap.hashPos(this);
		
		if (hashPos.getX() - 1 >= 0) {
			Tile t = myMap.map[(int)hashPos.getY()][(int)hashPos.getX() - 1];
			if (t.type != "blank")
				s.push(t);
		}
		if (hashPos.getX() + 1 < myMap.map.length) {
			Tile t = myMap.map[(int)hashPos.getY()][(int)hashPos.getX() + 1];
			if (t.type != "blank")
				s.push(t);
		}
		if (hashPos.getY() - 1 >= 0) {
			Tile t = myMap.map[(int)hashPos.getY() - 1][(int)hashPos.getX()];
			if (t.type != "blank")
				s.push(t);
		}
		if (hashPos.getY() + 1 < myMap.map.length) {
			Tile t = myMap.map[(int)hashPos.getY() + 1][(int)hashPos.getX()];
			if (t.type != "blank")
				s.push(t);
		}
		
		return s;
	}
	
	@Override
	public int compareTo(Tile o) {
		double dif = this.dVal - o.dVal;
		if (dif > 0) {
			return 1;
		} else if (dif < 0) {
			return -1;
		} else {
			return 0;
		}
	}
	
	/**
	 * Update a tile based on how much time has passed...
	 * 
	 * @param delta
	 * - number of milliseconds since last update
	 * @param input
	 * - input for the current container
	 */
	public void update(final int delta, Input input) {
		
		int mx = input.getMouseX();
		int my = input.getMouseY();
		
		if (!hover &&
				(getCoarseGrainedMaxX() > mx && getCoarseGrainedMinX() < mx) &&
				(getCoarseGrainedMaxY() > my && getCoarseGrainedMinY() < my)) {
			hover = true;
			if (type == "turn" || fortified ||
					((type == "straight-horizontal" || type == "straight-vertical") && myMap.wallCount >= myMap.maxWalls) ||
					(type == "blank" && myMap.turretCount >= myMap.maxTurrets)) {
				addImage(ResourceManager.getImage(PenguinDefenseGame.IMG_HIGHLIGHT_NO));
			} else {
				addImage(ResourceManager.getImage(PenguinDefenseGame.IMG_HIGHLIGHT_YES));
			}
		} else if (hover &&
				(getCoarseGrainedMaxX() < mx || getCoarseGrainedMinX() > mx) ||
				(getCoarseGrainedMaxY() < my || getCoarseGrainedMinY() > my)) {
			hover = false;
			removeImage(ResourceManager.getImage(PenguinDefenseGame.IMG_HIGHLIGHT_NO));
			removeImage(ResourceManager.getImage(PenguinDefenseGame.IMG_HIGHLIGHT_YES));
		}
		
		if (hover && type != "turn") {
			Vector hashIndex = myMap.hashPos(this);
			if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) && !fortified) {
				if (type == "straight-horizontal") {
					if (myMap.wallCount < myMap.maxWalls) {
						Wall w = new Wall(this.getX(), this.getY(), "vertical", myMap);
						w.updateCost(5);
						myMap.wallCount += 1;
						myMap.walls[(int)hashIndex.getX()][(int)hashIndex.getY()] = w;
						fortified = true;
						ResourceManager.getSound(PenguinDefenseGame.SND_BUILD).play();
					}
				} else if (type == "straight-vertical") {
					if (myMap.wallCount < myMap.maxWalls) {
						Wall w = new Wall(this.getX(), this.getY(), "horizontal", myMap);
						w.updateCost(5);
						myMap.wallCount += 1;
						myMap.walls[(int)hashIndex.getX()][(int)hashIndex.getY()] = w;
						fortified = true;
						ResourceManager.getSound(PenguinDefenseGame.SND_BUILD).play();
					}
				} else {
					if (myMap.turretCount < myMap.maxTurrets) {
						Turret t = new Turret(this.getX(), this.getY(), 144f, myMap);
						t.updateCost(8);
						myMap.turretCount += 1;
						myMap.turrets[(int)hashIndex.getX()][(int)hashIndex.getY()] = t;
						fortified = true;
						ResourceManager.getSound(PenguinDefenseGame.SND_BUILD).play();
					}
				}
			} else if (input.isMouseButtonDown(Input.MOUSE_RIGHT_BUTTON) && fortified) {
				if (type == "straight-horizontal" || type == "straight-vertical") {
					myMap.walls[(int)hashIndex.getX()][(int)hashIndex.getY()].updateCost(1);
					myMap.walls[(int)hashIndex.getX()][(int)hashIndex.getY()] = null;
					myMap.wallCount -= 1;
					fortified = false;
					ResourceManager.getSound(PenguinDefenseGame.SND_BUILD).play();
				} else {
					myMap.turrets[(int)hashIndex.getX()][(int)hashIndex.getY()].updateCost(1);
					myMap.turrets[(int)hashIndex.getX()][(int)hashIndex.getY()] = null;
					myMap.turretCount -= 1;
					fortified = false;
					ResourceManager.getSound(PenguinDefenseGame.SND_BUILD).play();
				}
			}
		}
	}
}
