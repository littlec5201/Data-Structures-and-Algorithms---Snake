
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author sehall
 */
public class SnakeGameGui extends JPanel implements ActionListener, KeyListener, ChangeListener {
	Snake snake = new Snake(PANEL_WIDTH, PANEL_HEIGHT);
	DoublyLinkedDeque<Food> foodDeque = new DoublyLinkedDeque<Food>();
	private static Random rand = new Random();
	public final static int PANEL_WIDTH = 600;
	public final static int PANEL_HEIGHT = 600;
	public int NUM_FOOD = 15;
	public Timer timer;
	public DrawingCanvas drawingCanvas;
	private JButton restartButton;
	private static JFrame frame;
	private JSlider difficultySlider;

	public SnakeGameGui() {
		super(new BorderLayout());

		// food = new Food(randInt(0, PANEL_WIDTH - food.getSize()), randInt(0,
		// PANEL_HEIGHT - food.getSize()), i);
		difficultySlider = new JSlider(0, 50, 15);
        configureJSlider(difficultySlider);
        difficultySlider.addChangeListener(this);
		for (int i = 0; i < NUM_FOOD; i++) {
			foodDeque.enqueueRear(new Food(600, 600, i));
		}

		drawingCanvas = new DrawingCanvas();
		timer = new Timer(50, this);
		timer.start();

		JPanel sliderPanel = new JPanel();
		restartButton = new JButton("Restart");
		restartButton.addActionListener(this);
		sliderPanel.add(restartButton);
		sliderPanel.add(difficultySlider);

		add(drawingCanvas, BorderLayout.CENTER);
		add(sliderPanel, BorderLayout.SOUTH);
	}
	
	public void configureJSlider(JSlider jSlider){
		jSlider.setMajorTickSpacing(10);
		jSlider.setPaintTicks(true);
		jSlider.setPaintLabels(true);
	}

	public int randInt(int min, int max) {
		int randomNum = rand.nextInt((max - min) + 1) + min;
		return randomNum;
	}

	private class DrawingCanvas extends JPanel {
		public DrawingCanvas() {
			setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
			setBackground(Color.WHITE);
		}

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			foodDeque.currentNode = foodDeque.lastNode;
			while (foodDeque.currentNode != null) {
				foodDeque.currentNode.element.drawFood(g);
				foodDeque.currentNode = foodDeque.currentNode.previous;
			}
			if (snake.isAlive()) {
				snake.drawSnake(g);
			}
			if (snake.segmentDeque != null) {
				snake.segmentDeque.currentNode = snake.segmentDeque.firstNode;
				while (snake.segmentDeque.currentNode != null) {
					snake.segmentDeque.currentNode.element.drawSegment(g);
					snake.segmentDeque.currentNode = snake.segmentDeque.currentNode.next;
				}
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		Object source = event.getSource();
		foodDeque.currentNode = foodDeque.firstNode;
		snake.segmentDeque.currentNode = snake.segmentDeque.firstNode;
		if (source == timer) {
			if (snake.isAlive()) {
				while (foodDeque.currentNode != null) {
					if (snake.checkIfSnakeHitFood(foodDeque.currentNode.element)) {
						if (snake.eatFoodIfInRange(foodDeque.currentNode.element)) {
							foodDeque.dequeueFront();
							for (int i = 0; i < foodDeque.currentNode.element.getValue() + 1; i++) {
								snake.segmentDeque.enqueueRear(new Segment(snake.getX(), snake.getY(), snake.getSize(),
										foodDeque.currentNode.element.getColour()));
								// snake.segmentDeque.lastNode.element.setColour(new
								// Color(0,0,0,0));
							}
							break;
						} else {
							break;
						}
					} else {
						foodDeque.currentNode = foodDeque.currentNode.next;
					}
				}
			}
			drawingCanvas.repaint();
		}
		if (source == restartButton) {
			snake.killSnake();
			JOptionPane.showMessageDialog(drawingCanvas,
					"You have pressed the restart button\n Please press \"Ok\" to restart", "SNAKE GAME",
					JOptionPane.INFORMATION_MESSAGE);
			// Give Keyboard Focus back to the Frame DO NOT REMOVE!
			frame.setFocusable(true);
			frame.requestFocusInWindow();
			snake = null;
			snake = new Snake(PANEL_WIDTH, PANEL_HEIGHT);
			foodDeque.clear();
			for (int i = 0; i < NUM_FOOD; i++) {
				foodDeque.enqueueRear(new Food(600, 500, i));
			}
			snake.segmentDeque.clear();
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			snake.setDirection(Snake.Direction.U);
			System.out.println("UP PRESSED");
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			snake.setDirection(Snake.Direction.D);
			System.out.println("DOWN PRESSED");
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			snake.setDirection(Snake.Direction.L);
			System.out.println("LEFT PRESSED");
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			snake.setDirection(Snake.Direction.R);
			System.out.println("RIGHT PRESSED");
		} else {
			System.out.println("SOME DIFFERENT KEY!");
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// IGNORE
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// IGNORE
	}
	
	@Override
	public void stateChanged(ChangeEvent e) {
		Object source = e.getSource();
		if (source == difficultySlider) {
			int value = difficultySlider.getValue();
		    if(difficultySlider.getValueIsAdjusting() == false) {
		    	setNUM_FOOD(value);
		    }
		}
		
	}
	
	public int getNUM_FOOD() {
		return NUM_FOOD;
	}

	public void setNUM_FOOD(int NUM_FOOD) {
		this.NUM_FOOD = NUM_FOOD;
	}

	public static void main(String[] args) {
		System.out.println("============SNAKE===============");
		SnakeGameGui game = new SnakeGameGui();
		frame = new JFrame("SNAKE GAME GUI");
		frame.setFocusable(true);
		// add a keylistener
		frame.addKeyListener(game);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(game);
		// gets the dimensions for screen width and height to calculate center
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension dimension = toolkit.getScreenSize();
		int screenHeight = dimension.height;
		int screenWidth = dimension.width;
		frame.pack(); // resize frame apropriately for its content
		// positions frame in center of screen
		frame.setLocation(
				new Point((screenWidth / 2) - (frame.getWidth() / 2), (screenHeight / 2) - (frame.getHeight() / 2)));
		frame.setVisible(true);
	}
}
