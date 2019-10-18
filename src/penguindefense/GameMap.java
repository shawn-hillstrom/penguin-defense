package penguindefense;

import jig.Entity;
import jig.Vector;

/**
 * A class which represents an entire game map. A GameMap object contains information
 * about the map including tile costs and tile positions.
 */
public class GameMap {
	
	private float[][] mapCost = {
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1},
			{1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1},
			{1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 0, 0, 1, 1, 1, 0, 1, 1, 1, 0, 0, 0},
			{1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1},
			{1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 1},
			{1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 1, 0, 1, 1, 1, 1, 1, 1},
			{0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1},
			{1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1},
			{1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 0, 0, 1, 1, 1, 1},
			{1, 1, 1, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1},
			{1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 0, 0, 1, 1, 1, 1},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
	};
	
	public PenguinDefenseGame myGame;
	public Tile[][] map = new Tile[22][22];
	public Wall[][] walls = new Wall[22][22];
	public Turret[][] turrets = new Turret[22][22];
	public int maxWalls = 10;
	public int maxTurrets = 10;
	public int wallCount = 0;
	public int turretCount = 0;
	
	private Vector corner;
	
	/**
	 * Constructor for a game map.
	 * 
	 * @param x
	 * - x coordinate for center of map
	 * @param y
	 * - y coordinate for center of map
	 */
	public GameMap(float x, float y, PenguinDefenseGame game) {
		corner = new Vector(x - (map.length * 32 / 2), y - (map.length * 32 / 2));
		myGame = game;
	}
	
	public Vector hashPos(Entity e) {
		int xIndex = (int)(e.getX() - corner.getX() - 16)/32;
		int yIndex = (int)(e.getY() - corner.getY() - 16)/32;
		return new Vector(xIndex, yIndex);
	}
	
	/**
	 * Get the corner of this map.
	 * 
	 * @return
	 * - corner of the map as a Vector
	 */
	public Vector getCorner() {
		return corner;
	}
	
	/**
	 * Generates a game map given the mapCost array.
	 */
	public void generate() {
		for (int i = 0; i < mapCost.length; i++) {
			for (int j = 0; j < mapCost.length; j++) {
				String type = "blank";
				String img = PenguinDefenseGame.IMG_TILE_BLANK;
				if (mapCost[i][j] == 0) {
					PathCheck check = new PathCheck(i, j);
					if (check.up && check.down && check.left && check.right) {
						type = "turn";
						img = PenguinDefenseGame.IMG_PATH_4WAY;
					} else if (check.up && check.down && check.left) {
						type = "turn";
						img = PenguinDefenseGame.IMG_PATH_3WAY_LEFT;
					} else if (check.up && check.down && check.right) {
						type = "turn";
						img = PenguinDefenseGame.IMG_PATH_3WAY_RIGHT;
					} else if (check.up && check.left && check.right) {
						type = "turn";
						img = PenguinDefenseGame.IMG_PATH_3WAY_UP;
					} else if (check.down && check.left && check.right) {
						type = "turn";
						img = PenguinDefenseGame.IMG_PATH_3WAY_DOWN;
					} else if (check.up && check.left) {
						type = "turn";
						img = PenguinDefenseGame.IMG_PATH_TURN_UPLEFT;
					} else if (check.up && check.right) {
						type = "turn";
						img = PenguinDefenseGame.IMG_PATH_TURN_UPRIGHT;
					} else if (check.down && check.left) {
						type = "turn";
						img = PenguinDefenseGame.IMG_PATH_TURN_DOWNLEFT;
					} else if (check.down && check.right) {
						type = "turn";
						img = PenguinDefenseGame.IMG_PATH_TURN_DOWNRIGHT;
					} else if (check.up && check.down) {
						type = "straight-vertical";
						img = PenguinDefenseGame.IMG_PATH_STRAIGHT_VER;
					} else if (check.left && check.right) {
						type = "straight-horizontal";
						img = PenguinDefenseGame.IMG_PATH_STRAIGHT_HOR;
					}
				}
				map[i][j] = new Tile(corner.getX() + (j * 32) + 16, corner.getY() + (i * 32) + 16, type, img, this);
			}
		}
	}
	
	/**
	 * Private class used to check a path for successors in a concise way.
	 */
	private class PathCheck {
		
		public boolean up = false;
		public boolean down = false;
		public boolean left = false;
		public boolean right = false;
		
		public PathCheck(int i, int j) {
			if (i - 1 < 0 || mapCost[i - 1][j] == 0)
				up = true;
			if (i + 1 >= mapCost.length || mapCost[i + 1][j] == 0)
				down = true;
			if (j - 1 < 0 || mapCost[i][j - 1] == 0)
				left = true;
			if (j + 1 >= mapCost.length || mapCost[i][j + 1] == 0)
				right = true;
		}
	}
}
