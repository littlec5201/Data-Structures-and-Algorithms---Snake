import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JOptionPane;

public class Snake implements Runnable {
	SnakeGameGui gui;
	Food food;
	DoublyLinkedDeque<Segment> segmentDeque = new DoublyLinkedDeque<Segment>();

	private boolean alive;
	private int x, y;
	public int rating = 0;
	private int panelWidth, panelHeight;
	private int SIZE = 25;
	private int movement = 5;

	public enum Direction {
		U, D, L, R
	};

	private Direction direction = Direction.R;
	Thread thread;

	public Snake(int panelWidth, int panelHeight) {
		this.panelWidth = panelWidth;
		this.panelHeight = panelHeight;
		this.x = 50;
		this.y = 550;
		this.alive = true;
		thread = new Thread(this);
		thread.start();
	}

	@Override
	public void run() {
		while (isAlive()) {
			moveSnake();
			try {
				Thread.sleep(30);
			} catch (InterruptedException e) {
			}
		}
	}

	public int getSize() {
		return this.SIZE;
	}

	public Direction getDirection() {
		return this.direction;
	}

	public int getMovement() {
		return this.movement;
	}

	public synchronized void moveSnake() {
		if (x < 5 || x > (panelWidth - 30) || y < 5 || y > (panelHeight - 30)) {
			killSnake();
			JOptionPane.showMessageDialog(null, "Oh dear, you died. Please try again");
		} else {
			try {
				if (segmentDeque.size() == 1) {
					segmentDeque.firstNode.element.setX(this.x + 12);
					segmentDeque.firstNode.element.setY(this.y + 12);
				}
				if (segmentDeque.size() > 1) {
					segmentDeque.currentNode = segmentDeque.lastNode;

					while (segmentDeque.currentNode != segmentDeque.firstNode) {
						segmentDeque.currentNode.element.setX(segmentDeque.currentNode.previous.element.getX());
						segmentDeque.currentNode.element.setY(segmentDeque.currentNode.previous.element.getY());
						segmentDeque.currentNode = segmentDeque.currentNode.previous;
					}
					segmentDeque.firstNode.element.setX(this.x + 12);
					segmentDeque.firstNode.element.setY(this.y + 12);

				}

				if (direction == Direction.U) {
					this.y -= movement;
				} else if (direction == Direction.D) {
					this.y += movement;
				} else if (direction == Direction.L) {
					this.x -= movement;
				} else if (direction == Direction.R) {
					this.x += movement;
				}
			} catch (NullPointerException npe) {

			}
		}
	}

	public boolean isAlive() {
		return this.alive;
	}

	public void killSnake() {
		this.alive = false;
		segmentDeque.clear();
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public boolean checkIfSnakeHitFood(Food food) {
		int xComponent = this.x - food.getX();
		int yComponent = this.y - food.getY();
		double distance = Math.sqrt((xComponent * xComponent) + (yComponent * yComponent));
		if (distance < (food.getSize() + SIZE) / 3) {
			return true;
		} else {
			return false;
		}
	}

	public boolean eatFoodIfInRange(Food food) {
		if (rating < food.getValue()) {
			killSnake();
			return false;
		} else {
			rating = food.getValue() + 1;
			food.killFood();
			return true;
		}
	}

	public void drawSnake(Graphics g) {
		g.setColor(new Color(255, 67, 12, 255));
		g.fillOval(this.x, this.y, SIZE, SIZE);
	}
}
