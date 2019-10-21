package penguindefense;

import java.util.Iterator;
import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import jig.ResourceManager;
import jig.Vector;

/**
 * This state is active while playing the game. A player can see and interact with the
 * map and all entities on or around the map.
 * 
 * Transitions from StartUpState.
 * 
 * Transitions to GameOverState.
 */
public class PlayingState extends BasicGameState {
	
	private int wave;
	private int enemyTot;
	private int enemyCount;
	private int time;
	private float thresh;
	private boolean debug;

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		
	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame game) {
		
		container.setSoundOn(true);
		
		wave = 1;
		enemyTot = 100;
		enemyCount = 0;
		time = 0;
		thresh = 400;
		debug = false;
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		
		PenguinDefenseGame myGame = (PenguinDefenseGame)game;
		
		// render background
		g.drawImage(ResourceManager.getImage(PenguinDefenseGame.IMG_BACKGROUND), 0, 0);
		
		// render walls
		for (Wall[] l : myGame.myMap.walls) {
			for (Wall w : l) {
				if (w != null) {
					w.render(g);
				}
			}
		}
		
		// render enemies
		for (Penguin p : myGame.enemies) {
			p.render(g);
		}
		
		// render tiles
		for (Tile[] l : myGame.myMap.map) {
			for (Tile t : l) {
				t.render(g);
			}
		}
		
		// render turrets
		for (Turret[] l : myGame.myMap.turrets) {
			for (Turret t : l) {
				if (t != null) {
					t.render(g);
				}
			}
		}
		
		// render lasers
		g.setColor(Color.red);
		for (Laser l : myGame.lasers) {
			g.drawLine(l.start.getX(), l.start.getY(), l.end.getX(), l.end.getY());
		}
		
		// render the objective
		myGame.obj.render(g);
		
		// debug dijkstra's
		if (debug) {
			g.setColor(Color.black);
			for (Tile[] l : myGame.myMap.map) {
				for (Tile t : l) {
					if (t.pi != null) {
						g.drawLine(t.getX(), t.getY(), t.pi.getX(), t.pi.getY());
					}
				}
			}
		}
		
		// render information labels
		g.setColor(Color.darkGray);
		g.drawString("Score: " + myGame.score, 10, 30);
		g.drawString("Wave: " + wave, 10, 50);
		g.drawString("Enemies Remaining: " + (enemyTot - myGame.deathToll), 10, 70);
		g.drawString("Gold: " + myGame.gold, 10, myGame.screenHeight - 30);
		
		g.setColor(Color.red);
		g.drawString("Health: " + myGame.obj.getLives(), myGame.obj.getX() - 57, myGame.obj.getY() - 90);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		
		PenguinDefenseGame myGame = (PenguinDefenseGame)game;
		Input input = container.getInput();
		Random rng = new Random();
		
		// toggle debug
		if (input.isKeyPressed(Input.KEY_TAB))
			debug = !debug;
		
		// spawn a new penguin
		time += delta;
		if (time >= thresh && enemyCount < enemyTot) {
			Penguin newP = new Penguin(0, rng.nextInt(myGame.screenHeight + 1), 1.5f, 2, myGame);
			newP.setObjective(myGame.myMap.mapStart);
			myGame.enemies.add(newP);
			enemyCount += 1;
			time = 0;
		}
		
		// set next wave
		if (myGame.deathToll >= enemyTot) {
			wave += 1;
			enemyTot *= 2;
			enemyCount = 0;
			thresh /= 1.7;
			time = -2500;
			myGame.deathToll = 0;
		}
		
		// update tiles
		for (Tile[] l : myGame.myMap.map) {
			for (Tile t : l) {
				t.update(delta, input);
			}
		}
		
		// update walls
		for (Wall[] l : myGame.myMap.walls) {
			for (Wall w : l) {
				if (w != null) {
					w.update(delta);
					if (w.getLives() <= 0) {
						Vector hashIndex = myGame.myMap.hashPos(w);
						myGame.myMap.walls[(int)hashIndex.getX()][(int)hashIndex.getY()].updateCost(1);
						myGame.myMap.walls[(int)hashIndex.getX()][(int)hashIndex.getY()] = null;
						myGame.myMap.map[(int)hashIndex.getY()][(int)hashIndex.getX()].setFortified(false);
						myGame.myMap.wallCount -= 1;
					}
				}
			}
		}
		
		// update turrets
		for (Turret[] l : myGame.myMap.turrets) {
			for (Turret t : l) {
				if (t != null) {
					t.update(delta);
				}
			}
		}
		
		// update the objective
		myGame.obj.update(delta);
		
		// update enemies
		for (Iterator <Penguin> i = myGame.enemies.iterator(); i.hasNext();) {
			Penguin p = i.next();
			p.update(delta);
		}
		
		// update lasers
		for (Iterator <Laser> i = myGame.lasers.iterator(); i.hasNext();) {
			Laser l = i.next();
			if (l.isActive()) {
				l.update(delta);
			} else {
				i.remove();
			}
		}
		
		// check for a win or loss
		if (myGame.obj.getLives() <= 0) {
			((GameOverState)game.getState(PenguinDefenseGame.GAMEOVERSTATE)).setWin(false);
			myGame.enterState(PenguinDefenseGame.GAMEOVERSTATE);
		} else if (wave >= 5 && myGame.deathToll >= enemyTot) {
			((GameOverState)game.getState(PenguinDefenseGame.GAMEOVERSTATE)).setWin(true);
			myGame.enterState(PenguinDefenseGame.GAMEOVERSTATE);
		}
	}

	@Override
	public int getID() {
		return PenguinDefenseGame.PLAYINGSTATE;
	}

}
