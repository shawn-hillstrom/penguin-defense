package penguindefense;

import java.util.ArrayList;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import jig.Entity;
import jig.ResourceManager;

/**
 * Penguin Defense is a tile-based, tower defense game. Your objective: to defend your
 * town from ravenous penguins trying to steal your fish.
 * 
 * This class represents an entire game of Penguin Defense and contains all resources,
 * entities, and other important information concerning the current game.
 * 
 * The state-based game has three states, StartUp, Playing, and GameOver. A player
 * progresses through these states based on game conditions and user input.
 */
public class PenguinDefenseGame extends StateBasedGame {
	
	// state variables
	public static final int STARTUPSTATE = 0;
	public static final int PLAYINGSTATE = 1;
	public static final int GAMEOVERSTATE = 2;
	
	// screen size
	public final int screenWidth;
	public final int screenHeight;
	
	// constraint variables
	public int maxEnemies = 1600;
	public int deathToll = 0;
	
	// game variables
	public int score = 0;
	public int gold = 100;
	
	//sounds
	public static final String SND_QUACK = "penguindefense/resource/quack.wav";
	public static final String SND_DAMAGE = "penguindefense/resource/damage.wav";
	public static final String SND_SHOT = "penguindefense/resource/shot.wav";
	public static final String SND_BUILD = "penguindefense/resource/build.wav";
	
	// images
	public static final String IMG_BANNER_START = "penguindefense/resource/banner-start.png";
	public static final String IMG_BANNER_DEFEAT = "penguindefense/resource/banner-defeat.png";
	public static final String IMG_BANNER_VICTORY = "penguindefense/resource/banner-victory.png";
	public static final String IMG_BACKGROUND = "penguindefense/resource/background.png";
	public static final String IMG_TILE_BLANK = "penguindefense/resource/tile-blank.png";
	public static final String IMG_PATH_STRAIGHT_HOR = "penguindefense/resource/path-straight-horizontal.png";
	public static final String IMG_PATH_STRAIGHT_VER = "penguindefense/resource/path-straight-vertical.png";
	public static final String IMG_PATH_TURN_DOWNLEFT = "penguindefense/resource/path-turn-downleft.png";
	public static final String IMG_PATH_TURN_DOWNRIGHT = "penguindefense/resource/path-turn-downright.png";
	public static final String IMG_PATH_TURN_UPLEFT = "penguindefense/resource/path-turn-upleft.png";
	public static final String IMG_PATH_TURN_UPRIGHT = "penguindefense/resource/path-turn-upright.png";
	public static final String IMG_PATH_3WAY_DOWN = "penguindefense/resource/path-3way-down.png";
	public static final String IMG_PATH_3WAY_LEFT = "penguindefense/resource/path-3way-left.png";
	public static final String IMG_PATH_3WAY_RIGHT = "penguindefense/resource/path-3way-right.png";
	public static final String IMG_PATH_3WAY_UP = "penguindefense/resource/path-3way-up.png";
	public static final String IMG_PATH_4WAY = "penguindefense/resource/path-4way.png";
	public static final String IMG_PENGUIN = "penguindefense/resource/penguin.png";
	public static final String IMG_IGLOO = "penguindefense/resource/igloo.png";
	public static final String IMG_IGLOO_DAMAGED = "penguindefense/resource/igloo-damaged.png";
	public static final String IMG_WALL_HOR = "penguindefense/resource/wall-horizontal.png";
	public static final String IMG_WALL_VER = "penguindefense/resource/wall-vertical.png";
	public static final String IMG_WALL_BROKEN_HOR = "penguindefense/resource/wall-broken-horizontal.png";
	public static final String IMG_WALL_BROKEN_VER = "penguindefense/resource/wall-broken-vertical.png";
	public static final String IMG_WALL_DAMAGED_HOR = "penguindefense/resource/wall-damaged-horizontal.png";
	public static final String IMG_WALL_DAMAGED_VER = "penguindefense/resource/wall-damaged-vertical.png";
	public static final String IMG_TURRET = "penguindefense/resource/turret.png";
	public static final String IMG_HIGHLIGHT_YES = "penguindefense/resource/highlight-yes.png";
	public static final String IMG_HIGHLIGHT_NO = "penguindefense/resource/highlight-no.png";
	
	// entities
	GameMap myMap;
	Objective obj;
	ArrayList<Penguin> enemies;
	ArrayList<Laser> lasers;
	
	// maps
	public double[][] map1 = {
			{9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9},
			{9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9},
			{9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9},
			{9, 9, 9, 9, 9, 9, 9, 9, 9, 1, 1, 1, 1, 9, 9, 9, 9, 9, 9, 9, 9, 9},
			{9, 9, 9, 9, 9, 9, 9, 9, 9, 1, 9, 9, 1, 1, 1, 1, 9, 9, 9, 9, 9, 9},
			{9, 9, 9, 9, 9, 9, 9, 1, 1, 1, 9, 9, 9, 9, 9, 1, 9, 9, 9, 9, 9, 9},
			{9, 9, 9, 9, 9, 9, 9, 1, 9, 1, 1, 1, 9, 9, 9, 1, 9, 9, 9, 1, 1, 1},
			{9, 9, 9, 9, 9, 9, 9, 1, 9, 9, 9, 1, 9, 9, 9, 1, 9, 9, 9, 1, 9, 9},
			{9, 9, 9, 9, 1, 1, 1, 1, 9, 9, 9, 1, 9, 1, 1, 1, 1, 1, 1, 1, 9, 9},
			{9, 9, 9, 1, 1, 9, 9, 9, 9, 9, 9, 1, 1, 1, 9, 1, 9, 9, 9, 9, 9, 9},
			{1, 1, 1, 1, 9, 9, 9, 9, 9, 9, 9, 9, 1, 9, 9, 1, 9, 9, 9, 9, 9, 9},
			{9, 9, 9, 1, 9, 9, 9, 9, 9, 9, 9, 9, 1, 9, 9, 1, 9, 9, 9, 9, 9, 9},
			{9, 9, 9, 1, 9, 9, 9, 9, 9, 9, 9, 9, 1, 9, 9, 1, 1, 1, 9, 9, 9, 9},
			{9, 9, 9, 1, 1, 1, 1, 1, 1, 9, 9, 9, 1, 9, 9, 9, 9, 1, 9, 9, 9, 9},
			{9, 9, 9, 9, 9, 9, 9, 9, 1, 1, 1, 1, 1, 9, 9, 9, 9, 1, 9, 9, 9, 9},
			{9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 1, 9, 9, 9, 9, 9, 1, 9, 9, 9, 9},
			{9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 1, 9, 9, 9, 9, 9, 1, 9, 9, 9, 9},
			{9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 1, 1, 9, 9, 9, 1, 1, 9, 9, 9, 9},
			{9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 1, 9, 9, 9, 1, 9, 9, 9, 9, 9},
			{9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 1, 1, 1, 1, 1, 9, 9, 9, 9, 9},
			{9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9},
			{9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9}
	};
	
	public double[][] map2 = {
			{9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9},
			{9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9},
			{9, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 9},
			{9, 1, 9, 1, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 1, 9, 1, 9},
			{9, 1, 9, 1, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 1, 9, 1, 9},
			{9, 1, 1, 1, 9, 9, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 9, 9, 1, 9, 1, 9},
			{9, 1, 9, 1, 9, 9, 1, 9, 9, 1, 9, 9, 9, 9, 9, 1, 1, 1, 1, 1, 1, 1},
			{9, 1, 9, 1, 9, 9, 1, 9, 9, 1, 9, 9, 9, 9, 9, 1, 9, 9, 1, 9, 1, 9},
			{9, 1, 1, 1, 9, 9, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 9, 9, 1, 9, 1, 9},
			{9, 1, 9, 1, 9, 9, 1, 9, 9, 1, 9, 9, 9, 9, 9, 1, 9, 9, 1, 9, 1, 9},
			{9, 1, 9, 1, 9, 9, 1, 9, 9, 1, 9, 9, 9, 9, 9, 1, 9, 9, 1, 1, 1, 9},
			{9, 1, 1, 1, 9, 9, 1, 9, 9, 1, 9, 9, 9, 9, 9, 1, 9, 9, 1, 9, 1, 9},
			{9, 1, 9, 1, 9, 9, 1, 9, 9, 1, 9, 9, 9, 9, 9, 1, 9, 9, 1, 9, 1, 9},
			{9, 1, 9, 1, 9, 9, 1, 9, 9, 1, 9, 9, 9, 9, 9, 1, 9, 9, 1, 1, 1, 9},
			{9, 1, 9, 1, 9, 9, 1, 9, 9, 1, 9, 9, 9, 9, 9, 1, 9, 9, 1, 9, 1, 9},
			{1, 1, 1, 1, 1, 1, 1, 9, 9, 1, 9, 9, 9, 9, 9, 1, 9, 9, 1, 9, 1, 9},
			{9, 1, 9, 1, 9, 9, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 9, 9, 1, 1, 1, 9},
			{9, 1, 9, 1, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 1, 9, 1, 9},
			{9, 1, 9, 1, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 1, 9, 1, 9},
			{9, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 9},
			{9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9},
			{9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9}
	};
	
	public double[][] map3 = {
			{9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9},
			{9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9},
			{9, 9, 9, 9, 9, 1, 1, 1, 1, 9, 9, 1, 1, 1, 1, 9, 9, 1, 1, 1, 1, 9},
			{9, 9, 9, 9, 9, 1, 9, 9, 1, 9, 9, 1, 9, 9, 1, 9, 9, 1, 9, 9, 1, 9},
			{9, 9, 9, 9, 9, 1, 9, 9, 1, 9, 9, 1, 9, 9, 1, 9, 9, 1, 9, 9, 1, 9},
			{9, 9, 9, 9, 9, 1, 9, 9, 1, 9, 9, 1, 9, 9, 1, 9, 9, 1, 9, 9, 1, 9},
			{9, 9, 9, 9, 9, 1, 9, 9, 1, 9, 9, 1, 9, 9, 1, 9, 9, 1, 1, 1, 1, 9},
			{1, 1, 1, 9, 9, 1, 9, 9, 1, 1, 1, 1, 9, 9, 1, 9, 9, 1, 9, 9, 1, 9},
			{9, 9, 1, 9, 9, 1, 9, 9, 1, 9, 9, 1, 9, 9, 1, 9, 9, 1, 9, 9, 1, 9},
			{9, 9, 1, 9, 9, 1, 9, 9, 1, 9, 9, 1, 9, 9, 1, 9, 9, 1, 9, 9, 1, 9},
			{9, 9, 1, 9, 9, 1, 9, 9, 1, 9, 9, 1, 9, 9, 1, 1, 1, 1, 9, 9, 1, 9},
			{9, 9, 1, 9, 9, 1, 9, 9, 1, 9, 9, 1, 9, 9, 1, 9, 9, 1, 9, 9, 1, 9},
			{9, 9, 1, 9, 9, 1, 9, 9, 1, 9, 9, 1, 9, 9, 1, 9, 9, 1, 9, 9, 1, 9},
			{9, 9, 1, 9, 9, 1, 9, 9, 1, 9, 9, 1, 1, 1, 1, 9, 9, 1, 9, 9, 1, 9},
			{9, 9, 1, 1, 1, 1, 1, 1, 1, 9, 9, 1, 9, 9, 1, 9, 9, 1, 1, 1, 1, 1},
			{9, 9, 1, 9, 9, 1, 9, 9, 1, 9, 9, 1, 9, 9, 1, 9, 9, 1, 9, 9, 1, 9},
			{9, 9, 1, 9, 9, 1, 9, 9, 1, 9, 9, 1, 9, 9, 1, 9, 9, 1, 9, 9, 1, 9},
			{9, 9, 1, 9, 9, 1, 9, 9, 1, 1, 1, 1, 9, 9, 1, 9, 9, 1, 9, 9, 1, 9},
			{9, 9, 1, 9, 9, 1, 9, 9, 1, 9, 9, 1, 9, 9, 1, 9, 9, 1, 9, 9, 1, 9},
			{9, 9, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 9, 9, 1, 1, 1, 1, 1, 1, 1, 9},
			{9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9},
			{9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9}
	};

	/**
	 * Constructor for a game of Penguin Defense.
	 * 
	 * @param name
	 * - the name of the game, to be displayed in the window
	 * @param height
	 * - the height (in pixels) of the game window
	 * @param width
	 * - the width (in pixels) of the game window
	 */
	public PenguinDefenseGame(String name, int width, int height) {
		super(name);
		screenWidth = width;
		screenHeight = height;
		Entity.setCoarseGrainedCollisionBoundary(Entity.AABB);
	}
	
	public void setGameVars(int s, int g) {
		score = s;
		gold = g;
	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		
		addState(new StartUpState());
		addState(new PlayingState());
		addState(new GameOverState());
		
		// initialize sounds
		ResourceManager.loadSound(SND_QUACK);
		ResourceManager.loadSound(SND_DAMAGE);
		ResourceManager.loadSound(SND_SHOT);
		ResourceManager.loadSound(SND_BUILD);
		
		// initialize images
		ResourceManager.loadImage(IMG_BANNER_START);
		ResourceManager.loadImage(IMG_BANNER_DEFEAT);
		ResourceManager.loadImage(IMG_BANNER_VICTORY);
		ResourceManager.loadImage(IMG_BACKGROUND);
		ResourceManager.loadImage(IMG_TILE_BLANK);
		ResourceManager.loadImage(IMG_PATH_STRAIGHT_HOR);
		ResourceManager.loadImage(IMG_PATH_STRAIGHT_VER);
		ResourceManager.loadImage(IMG_PATH_TURN_DOWNLEFT);
		ResourceManager.loadImage(IMG_PATH_TURN_DOWNRIGHT);
		ResourceManager.loadImage(IMG_PATH_TURN_UPLEFT);
		ResourceManager.loadImage(IMG_PATH_TURN_UPRIGHT);
		ResourceManager.loadImage(IMG_PATH_3WAY_DOWN);
		ResourceManager.loadImage(IMG_PATH_3WAY_LEFT);
		ResourceManager.loadImage(IMG_PATH_3WAY_RIGHT);
		ResourceManager.loadImage(IMG_PATH_3WAY_UP);
		ResourceManager.loadImage(IMG_PATH_4WAY);
		ResourceManager.loadImage(IMG_PENGUIN);
		ResourceManager.loadImage(IMG_IGLOO);
		ResourceManager.loadImage(IMG_IGLOO_DAMAGED);
		ResourceManager.loadImage(IMG_WALL_HOR);
		ResourceManager.loadImage(IMG_WALL_VER);
		ResourceManager.loadImage(IMG_WALL_BROKEN_HOR);
		ResourceManager.loadImage(IMG_WALL_BROKEN_VER);
		ResourceManager.loadImage(IMG_WALL_DAMAGED_HOR);
		ResourceManager.loadImage(IMG_WALL_DAMAGED_VER);
		ResourceManager.loadImage(IMG_TURRET);
		ResourceManager.loadImage(IMG_HIGHLIGHT_YES);
		ResourceManager.loadImage(IMG_HIGHLIGHT_NO);
	}

	public static void main(String args[]) {
		AppGameContainer app;
		try {
			app = new AppGameContainer(new PenguinDefenseGame("Penguin Defense", 1280, 720));
			app.setDisplayMode(1280, 720, false);
			app.setVSync(true);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
}
