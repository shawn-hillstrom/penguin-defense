package penguindefense;

import org.newdawn.slick.Input;

import jig.Entity;
import jig.ResourceManager;

/**
 * A class representing a tile entity. Tiles make up the game map and are stationary.
 * Each tile has a type, and some may be fortified based on their type. 
 * 
 * Tiles that support fortification wait for a player to click on them, and if the 
 * player has sufficient funds, a respective fortification is built.
 */
public class Tile extends Entity {

	public String type;
	private int fortIndex = -1;
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
	 */
	public Tile(final float x, final float y, String type, String img) {
		super(x, y);
		this.type = type;
		addImageWithBoundingBox(ResourceManager.getImage(img));
	}
	
	/**
	 * Check to see whether a tile is fortified
	 * 
	 * @return
	 * - whether or not the tile is fortified
	 */
	public boolean isFortified() {
		return (fortIndex >= 0) ? true : false;
	}
	
	/**
	 * Get the index in the respective object array for the current fortification.
	 * 
	 * @return
	 * - index of fortification object
	 */
	public int getFortIndex() {
		return fortIndex;
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
			if (type != "turn" && fortIndex < 0) {
				addImage(ResourceManager.getImage(PenguinDefenseGame.IMG_HIGHLIGHT_YES));
			} else {
				addImage(ResourceManager.getImage(PenguinDefenseGame.IMG_HIGHLIGHT_NO));
			}
		} else if (hover &&
				(getCoarseGrainedMaxX() < mx || getCoarseGrainedMinX() > mx) ||
				(getCoarseGrainedMaxY() < my || getCoarseGrainedMinY() > my)) {
			hover = false;
			if (type != "turn" && fortIndex < 0) {
				removeImage(ResourceManager.getImage(PenguinDefenseGame.IMG_HIGHLIGHT_YES));
			} else {
				removeImage(ResourceManager.getImage(PenguinDefenseGame.IMG_HIGHLIGHT_NO));
			}
		}
	}
}
