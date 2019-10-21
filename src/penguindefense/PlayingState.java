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

public class PlayingState extends BasicGameState {
	
	private int wave = 1;
	private int enemyCount = 100;
	private int enemyTot = 100;
	private int time = 0;
	private int thresh = 500;
	private boolean debug = false;

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		
	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame game) {
		container.setSoundOn(true);
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		
		PenguinDefenseGame myGame = (PenguinDefenseGame)game;
		
		g.drawImage(ResourceManager.getImage(PenguinDefenseGame.IMG_BACKGROUND), 0, 0);
		
		for (Wall[] l : myGame.myMap.walls) {
			for (Wall w : l) {
				if (w != null) {
					w.render(g);
				}
			}
		}
		
		for (Penguin p : myGame.enemies) {
			p.render(g);
		}
		
		for (Tile[] l : myGame.myMap.map) {
			for (Tile t : l) {
				t.render(g);
			}
		}
		
		for (Turret[] l : myGame.myMap.turrets) {
			for (Turret t : l) {
				if (t != null) {
					t.render(g);
				}
			}
		}
		
		g.setColor(Color.red);
		for (Laser l : myGame.lasers) {
			g.drawLine(l.start.getX(), l.start.getY(), l.end.getX(), l.end.getY());
		}
		
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
		
		g.setColor(Color.darkGray);
		g.drawString("Score: " + myGame.score, 10, 30);
		g.drawString("Wave: " + wave, 10, 50);
		g.drawString("Enemies Remaining: " + enemyCount + "/" + enemyTot, 10, 70);
		
		g.setColor(Color.orange);
		g.drawString("Gold: " + myGame.gold, 10, 90);
		
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
		
		time += delta;
		if (time >= thresh) {
			Penguin newP = new Penguin(0, rng.nextInt(myGame.screenHeight + 1), 1.2f, 5, myGame);
			newP.setObjective(myGame.myMap.mapStart);
			myGame.enemies.add(newP);
			time = 0;
		}
		
		for (Tile[] l : myGame.myMap.map) {
			for (Tile t : l) {
				t.update(delta, input);
			}
		}
		
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
		
		for (Turret[] l : myGame.myMap.turrets) {
			for (Turret t : l) {
				if (t != null) {
					t.update(delta);
				}
			}
		}
		
		myGame.obj.update(delta);
		
		for (Iterator <Penguin> i = myGame.enemies.iterator(); i.hasNext();) {
			Penguin p = i.next();
			p.update(delta);
		}
		
		for (Iterator <Laser> i = myGame.lasers.iterator(); i.hasNext();) {
			Laser l = i.next();
			if (l.isActive()) {
				l.update(delta);
			} else {
				i.remove();
			}
		}
	}

	@Override
	public int getID() {
		return PenguinDefenseGame.PLAYINGSTATE;
	}

}
