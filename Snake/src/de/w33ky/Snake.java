/*
 * Little Snake-Clone Game Project by Stefan Weckend
 * Snake class
 * contains the structure of the snake
 */

package de.w33ky;

import java.util.LinkedList;

public class Snake {
	public class Segment {
		public int posx, posy;
		public Segment(int x, int y) {
			posx = x;
			posy = y;
		}
	}
	
	// contains the Segments of the snake
	public LinkedList<Segment> segments;
	private int food;
	
	// x, y start coordinates
	// f start food
	public Snake(int x, int y, int f) {
		segments = new LinkedList<Segment>();
		segments.addFirst(new Segment(x, y));
		food = f;
	}
	
	public boolean validate() {
		for(int i = 0; i < segments.size(); i++) {
			int posx = segments.get(i).posx;
			int posy = segments.get(i).posy;
			
			if((posx < 0) || (posx >= Game.sizex) || (posy < 0) || (posy >= Game.sizey)) {
				return false;
			}
			
			for(int j = i+1; j < segments.size(); j++) {
				if((segments.get(i).posx == segments.get(j).posx) && (segments.get(i).posy == segments.get(j).posy)) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	//move the snake to a direction
	public boolean move(Direction d) {
		Segment s = segments.getFirst();
		Segment snew = new Segment(0, 0);
		boolean doit = true;
		
		switch (d) {
		case UP:
			snew = new Segment(s.posx, s.posy+1);
			break;
		case DOWN:
			snew = new Segment(s.posx, s.posy-1);
			break;
		case LEFT:
			snew = new Segment(s.posx-1, s.posy);
			break;
		case RIGHT:
			snew = new Segment(s.posx+1, s.posy);
			break;
		default:
			doit = false;
			break;
		}
		
		if(doit) {
			segments.addFirst(snew);
			if(food > 0) {
				food--;
			}
			else {
				segments.removeLast();
			}
			return true;
		}
		else {
			return false;
		}
	}
	
	// add to food
	//called on food colision
	public void addFood(int f) {
		food += f;
	}
}
