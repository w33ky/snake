/*
 * Little Snake-Clone Game Project by Stefan Weckend
 * Main class
 * launches the game
 * contains the main method
 */

package de.w33ky;

import java.util.ArrayList;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.GL11.*;

public class Main {
	public static int random(int low, int high) {
		return (int) (Math.random() * (high - low) + low);
	}
	
	public static void main(String[] args) throws LWJGLException {
		// some tests for debugging
		Game.sizex = 80;
		Game.sizey = 60;
		Snake s = new Snake(5, 5, 15);
		Game.run = true;
		
		ArrayList<Food> food = new ArrayList<Food>();
		
		Direction direction = Direction.RIGHT;
		
		try {
			Display.setDisplayMode(new DisplayMode(Game.sizex*10, Game.sizey*10));
			Display.setTitle("Snake");
			Display.create();
		} catch(LWJGLException e) {
			e.printStackTrace();
			Display.destroy();
			System.exit(0);
		}
		
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, Game.sizex, 0, Game.sizey, 1, -1);
		glMatrixMode(GL_MODELVIEW);
		
		glClear(GL_COLOR_BUFFER_BIT);
		glColor3f(.5f, .5f, .5f);

		while(Game.run && !Display.isCloseRequested()) {
			
			while(Keyboard.next()) {
				if(Keyboard.getEventKeyState()) {
					if(Keyboard.getEventKey() == Keyboard.KEY_UP) {
						if(direction != Direction.DOWN)
							direction = Direction.UP;
					}
					if(Keyboard.getEventKey() == Keyboard.KEY_DOWN) {
						if(direction != Direction.UP)
							direction = Direction.DOWN;
					}
					if(Keyboard.getEventKey() == Keyboard.KEY_LEFT) {
						if(direction != Direction.RIGHT)
							direction = Direction.LEFT;
					}
					if(Keyboard.getEventKey() == Keyboard.KEY_RIGHT) {
						if(direction != Direction.LEFT)
							direction = Direction.RIGHT;
					}
					if(Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) {
						Game.run = false;
					}
				}
			}
			
			if(random(0, 100) == 1 || food.size() == 0) {
				int px = 0;
				int py = 0;
				int f = 0;
				boolean valid = false;
				while(!valid) {
					px = random(0, Game.sizex-1);
					py = random(0, Game.sizey-1);
					f = random(1, 5);
					valid = true;
					for(int i = 0; i < food.size(); i++) {
						if((food.get(i).posx == px) && (food.get(i).posy == py)) {
							valid = false;
						}
					}
				}
				food.add(new Food(px, py, f));
			}
			
			s.move(direction);
			
			glClear(GL_COLOR_BUFFER_BIT);
			glColor3f(1f, 1f, .5f);
			for(int i = 0; i < food.size(); i++) {
				int fx, fy;
				fx = food.get(i).posx;
				fy = food.get(i).posy;
				glBegin(GL_QUADS);
					glVertex2f(fx, fy);
					glVertex2f(fx+1, fy);
					glVertex2f(fx+1, fy+1);
					glVertex2f(fx, fy+1);
				glEnd();
			}
			
			glColor3f(.5f, .5f, .5f);
			for(int i = 0; i < s.segments.size(); i++) {
				int px = s.segments.get(i).posx;
				int py = s.segments.get(i).posy;
				glBegin(GL_QUADS);
					glVertex2f(px, py);
					glVertex2f(px+1, py);
					glVertex2f(px+1, py+1);
					glVertex2f(px, py+1);
				glEnd();
			}
			
			int hx, hy, del;
			del = -1;
			hx = s.segments.getFirst().posx;
			hy = s.segments.getFirst().posy;
			for(int i = 0; i < food.size(); i++) {
				int fx, fy;
				fx = food.get(i).posx;
				fy = food.get(i).posy;
				if((hx == fx) && (hy == fy)) {
					s.addFood(food.get(i).food);
					del = i;
				}
			}
			if(del > -1) {
				food.remove(del);
			}

			if(!s.validate())
				Game.run = false;
			
			Display.update();
			Display.sync(30);
		}
		
		// Game Over screen
		Game.gorun = true;
		while(Game.gorun && !Display.isCloseRequested()) {
			
			while(Keyboard.next()) {
				if(Keyboard.getEventKeyState()) {
					if(Keyboard.getEventKey() == Keyboard.KEY_RETURN) {
						Game.gorun = false;
					}
					if(Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) {
						Game.gorun = false;
					}
				}
			}
			
			glClear(GL_COLOR_BUFFER_BIT);
			glColor3f(.5f, .5f, .5f);
			glBegin(GL_QUADS);
			glVertex2f(0, 0);
			glVertex2f(Game.sizex, 0);
			glVertex2f(Game.sizex, Game.sizey);
			glVertex2f(0, Game.sizey);
			glEnd();
			
			Display.update();
			Display.sync(10);
		}
		
		Display.destroy();
		System.exit(0);
	}
}
