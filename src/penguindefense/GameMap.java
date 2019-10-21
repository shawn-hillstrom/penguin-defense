package penguindefense;

import java.util.PriorityQueue;
import java.util.Stack;

import jig.Entity;
import jig.Vector;

/**
 * A class which represents an entire game map. A GameMap object contains information
 * about the map including tile costs and tile positions.
 */
public class GameMap {
	
	public PenguinDefenseGame myGame;
	public Tile[][] map = new Tile[22][22];
	public Tile mapStart = null;
	public Tile mapEnd = null;
	public Wall[][] walls = new Wall[22][22];
	public Turret[][] turrets = new Turret[22][22];
	public int maxWalls = 10;
	public int maxTurrets = 10;
	public int wallCount = 0;
	public int turretCount = 0;
	
	private Vector corner;
	private double[][] mapCost;
	
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
	
	/**
	 * Hash function for the position of an entity.
	 * 
	 * @param e
	 * - entity to hash
	 * @return
	 * - vector containing two dimensional hash value
	 */
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
	 * Set the cost for a tile on the map to a specified value.
	 * 
	 * @param e
	 * - entity with the position of the specified tile for hashing
	 * @param c
	 * - new cost for the tile
	 */
	public void setMapCost(Entity e, double c) {
		Vector hashIndex = hashPos(e);
		mapCost[(int)hashIndex.getY()][(int)hashIndex.getX()] = c;
	}
	
	/**
	 * Find the edge weight between two tiles.
	 * 
	 * @param u
	 * - destination tile
	 * @param v
	 * - source tile
	 * @return
	 * - weight value
	 */
	private double weight(Tile u, Tile v) {
		Vector uIndex = hashPos(u);
		Vector vIndex = hashPos(v);
		double wu = mapCost[(int)uIndex.getY()][(int)uIndex.getX()]/2;
		double wv = mapCost[(int)vIndex.getY()][(int)vIndex.getX()]/2;
		return wu + wv;
	}
	
	/**
	 * Relax the cost to travel from v to u.
	 * 
	 * @param u
	 * - destination tile
	 * @param v
	 * - source tile
	 */
	private void relax(Tile u, Tile v) {
		double w = weight(u, v);
		if (v.dVal > u.dVal + w) {
			v.dVal = u.dVal + w;
			v.pi = u;
		}
	}
	
	/**
	 * Initialize an empty source for Dijkstra's
	 * 
	 * @param s
	 * - source for dijkstra's
	 */
	private void initializeEmptySource(Tile s) {
		for (Tile[] l : map) {
			for (Tile t : l) {
				t.dVal = Double.POSITIVE_INFINITY;
			}
		}
		s.dVal = 0;
	}
	
	/**
	 * Implementation of Dijkstra's algorithm using a PriorityQueue.
	 */
	public void dijkstra() {
		
		Tile[][] s = new Tile[22][22];
		PriorityQueue<Tile> q = new PriorityQueue<Tile>(map.length * map.length);
		
		initializeEmptySource(mapEnd);
		
		q.add(mapEnd);
		
		while (q.peek() != null) {
			Tile min = q.poll();
			Stack<Tile> adj = min.successors();
			Vector minIndex = hashPos(min);
			s[(int)minIndex.getY()][(int)minIndex.getX()] = min;
			while (!adj.empty()) {
				Tile nextAdj = adj.pop();
				Vector adjIndex = hashPos(nextAdj);
				if (s[(int)adjIndex.getY()][(int)adjIndex.getX()] == null) {
					relax(min, nextAdj);
					q.add(nextAdj);
				}
			}
		}
	}
	
	/**
	 * Generates a game map given the mapCost array.
	 * 
	 * @param costs
	 * - an array of map costs
	 */
	public void generate(double [][] costs) {
		mapCost = costs;
		for (int i = 0; i < mapCost.length; i++) {
			for (int j = 0; j < mapCost.length; j++) {
				String type = "blank";
				String img = PenguinDefenseGame.IMG_TILE_BLANK;
				if (mapCost[i][j] == 1) {
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
				Tile t = new Tile(corner.getX() + (j * 32) + 16, corner.getY() + (i * 32) + 16, type, img, this);
				map[i][j] = t;
				if (j == 0 && type == "straight-horizontal") {
					mapStart = t;
				} else if (j == mapCost.length - 1 && type == "straight-horizontal") {
					mapEnd = t;
				}
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
			if (i - 1 < 0 || mapCost[i - 1][j] == 1)
				up = true;
			if (i + 1 >= mapCost.length || mapCost[i + 1][j] == 1)
				down = true;
			if (j - 1 < 0 || mapCost[i][j - 1] == 1)
				left = true;
			if (j + 1 >= mapCost.length || mapCost[i][j + 1] == 1)
				right = true;
		}
	}
}
