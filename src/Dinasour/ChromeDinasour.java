package Dinasour;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class ChromeDinasour extends JPanel implements KeyListener, ActionListener {
	int boardwidth = 750;
	int boardHeight = 250;

	// Images
	Image dinasourImg;
	Image dinasourDeadImg;
	Image dinasourJumpImg;

	Image cactus1Img;
	Image cactus2Img;
	Image cactus3Img;

	class Block {
		int x, y, width, height;
		Image img;

		public Block(int x, int y, int width, int height, Image img) {
			this.x = x;
			this.y = y;
			this.height = height;
			this.width = width;
			this.img = img;

		}
	}

	// dinasour
	int dinsaourWidth = 88;
	int dinsaourHeight = 94;
	int dinsaourX = 50;
	int dinsourY = boardHeight - dinsaourHeight;

	Block dinsaour;

	// cactus
	int cactus1Width = 34;
	int cactus2Width = 69;
	int cactus3Width = 102;

	int cactusHeight = 70;
	int cactusX = 700;
	int cactusY = boardHeight - cactusHeight;
	ArrayList<Block> cactusArray;

	// physics
	int volecityX = -12;
	int volecityY = 0;
	int gravity = 1;

	boolean gameOver = false;
	int score = 0;

	Timer gameloop;
	Timer placeCactusTimer;

	ChromeDinasour() {
		setPreferredSize(new Dimension(boardwidth, boardHeight));
		setBackground(Color.LIGHT_GRAY);
		setFocusable(true);
		addKeyListener(this);

		dinasourImg = new ImageIcon(getClass().getResource("dino-run.gif")).getImage();
		dinasourDeadImg = new ImageIcon(getClass().getResource("dino-dead.png")).getImage();
		dinasourJumpImg = new ImageIcon(getClass().getResource("dino-jump.png")).getImage();
		cactus1Img = new ImageIcon(getClass().getResource("cactus1.png")).getImage();
		cactus2Img = new ImageIcon(getClass().getResource("cactus2.png")).getImage();
		cactus3Img = new ImageIcon(getClass().getResource("cactus3.png")).getImage();

		// dinasour
		dinsaour = new Block(dinsaourX, dinsourY, dinsaourWidth, dinsaourHeight, dinasourImg);

		// cactus
		cactusArray = new ArrayList<Block>();

		// game timer
		gameloop = new Timer(1000 / 60, this);
		gameloop.start();
		// place catus timer
		placeCactusTimer = new Timer(1500, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				placeCactus();
			}
		});
		placeCactusTimer.start();

	}

	public void placeCactus() {
		if (gameOver) {
			return;
		}

		double placeCactusChance = Math.random(); // 0 - 0.999999
		if (placeCactusChance > .90) { // 10% you get cactus3
			Block cactus = new Block(cactusX, cactusY, cactus3Width, cactusHeight, cactus3Img);
			cactusArray.add(cactus);
		} else if (placeCactusChance > .70) { // 20% you get cactus2
			Block cactus = new Block(cactusX, cactusY, cactus2Width, cactusHeight, cactus2Img);
			cactusArray.add(cactus);
		} else if (placeCactusChance > .50) { // 20% you get cactus1
			Block cactus = new Block(cactusX, cactusY, cactus1Width, cactusHeight, cactus1Img);
			cactusArray.add(cactus);
		}

		if (cactusArray.size() > 10) {
			cactusArray.remove(0); // remove the first cactus from ArrayList
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}

	public void draw(Graphics g) {
		// dinasour
		g.drawImage(dinsaour.img, dinsaour.x, dinsaour.y, dinsaour.width, dinsaour.height, null);

		// cactus
		for (int i = 0; i < cactusArray.size(); i++) {
			Block cactus = cactusArray.get(i);
			g.drawImage(cactus.img, cactus.x, cactus.y, cactus.width, cactus.height, null);
		}
		// score
		g.setColor(Color.black);
		g.setFont(new Font("Courier", Font.PLAIN, 32));
		if (gameOver) {
			g.drawString("Game Over: " + String.valueOf(score), 10, 35);
		} else {
			g.drawString(String.valueOf(score), 10, 35);
		}

	}

	public void move() {
		// dinasour
		volecityY += gravity;
		dinsaour.y += volecityY;

		if (dinsaour.y > dinsourY) {
			dinsaour.y = dinsourY;
			volecityY = 0;
			dinsaour.img = dinasourImg;
		}

		// cactus
		for (int i = 0; i < cactusArray.size(); i++) {
			Block cactus = cactusArray.get(i);
			cactus.x += volecityX;

			if (collision(dinsaour, cactus)) {
				gameOver = true;
				dinsaour.img = dinasourDeadImg;
			}
		}

		// score
		score++;
	}

	boolean collision(Block a, Block b) {
		return a.x < b.x + b.width && // a's top left corner doesn't reach b's top right corner
				a.x + a.width > b.x && // a's top right corner passes b's top left corner
				a.y < b.y + b.height && // a's top left corner doesn't reach b's bottom left corner
				a.y + a.height > b.y; // a's bottom left corner passes b's top left corner
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		move();
		repaint();

		if (gameOver) {
			placeCactusTimer.stop();
			gameloop.stop();
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			if (dinsaour.y == dinsourY) {
				volecityY = -17;
				dinsaour.img = dinasourJumpImg;
			}

			if (gameOver) {
				// restart game by resetting conditions
				dinsaour.y = dinsourY;
				dinsaour.img = dinasourImg;
				volecityY = 0;
				cactusArray.clear();
				score = 0;
				gameOver = false;
				gameloop.start();
				placeCactusTimer.start();
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}
}
