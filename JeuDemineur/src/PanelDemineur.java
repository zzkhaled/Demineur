import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.Timer;

public class PanelDemineur extends JPanel implements MouseListener, ActionListener {
	private static final long serialVersionUID = 1L;

	private Grid demineur;
	private int borderSize = 100;
	private int gridSize;
	private int cellSize;
	private boolean gameOver = false;
	private boolean youWin = false;
	private int time = 0;
	private int tic = 0;
	Timer timerGame;
	Timer timerBlink;
	private int sizeChoise = 10;

	Font titleFont = new Font("arial", Font.BOLD, 30);
	Font winFont = new Font("arial", Font.BOLD, 50);
	Font hudFont = new Font("Futura", Font.ITALIC, 15);
	Font numberFont;

	JComboBox<String> liste;

	ImageIcon grayB = new ImageIcon("grayBombe.png");
	ImageIcon orangeB = new ImageIcon("orangeBombe.png");
	ImageIcon redF = new ImageIcon("redFlag.png");

	public PanelDemineur() {

		setBackground(Color.lightGray);

		String[] elements = new String[] { "10 x 10", "20 x 20", "30 x 30" };
		liste = new JComboBox<String>(elements);
		this.add(liste);

		JButton bouton = new JButton("Start Game");
		bouton.addActionListener(this);
		this.add(bouton);
		
		initialize(sizeChoise) ; 
		
		addMouseListener(this);

		ActionListener clock = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				time++;
				repaint();
			}
		};
		timerGame = new Timer(1000, clock);
		

		ActionListener blink = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				tic++;
				repaint();
			}
		};
		timerBlink = new Timer(600, blink);
		
		
	}

	@Override
	
	public void paint(Graphics graphics) {
		super.paint(graphics);
	
		drawHUD(graphics) ; 

		if(gameOver) {				
			graphics.setColor(Color.red);
			graphics.setFont(titleFont);
			graphics.drawString("GAME OVER", 300, 80);
			for (int ligne = 0; ligne < sizeChoise ; ligne++) {
				for (int colonne = 0; colonne < sizeChoise ; colonne++) {
					demineur.getCells()[ligne][colonne].setFlag(false);
					if(demineur.getCells()[ligne][colonne].getBomb())
						demineur.getCells()[ligne][colonne].setShow(true);
				}
			}
			timerGame.stop();
			if(tic == 0)
			timerBlink.start();	
		}
		
		else {
			if(youWin) {
				if(tic%2==0)
					graphics.setColor(Color.yellow);
				else
					graphics.setColor(Color.green);
					
				graphics.setFont(winFont);
				graphics.drawString("YOU WIN", 300, 80);
				timerGame.stop();
				if(tic == 0)
					timerBlink.start() ; 
			}
			else {
				graphics.setColor(Color.green.darker());
				graphics.setFont(titleFont);
				graphics.drawString("DEMINEUR CCI", 290, 80);
				if(showed() + demineur.nbrBomb() == sizeChoise * sizeChoise)
					youWin = true ; 
			}
		}
			
		graphics.setFont(numberFont);
		String str ; 
		
		for (int ligne = 0; ligne < sizeChoise ; ligne++) {
			for (int colonne = 0; colonne < sizeChoise ; colonne++) {
				
				if(demineur.bombArround(ligne, colonne) == 0)
					str = "" ; 
				else
					str = "" + demineur.bombArround(ligne, colonne);
				
				if(!demineur.getCells()[ligne][colonne].getShow()) 
					drawCell(graphics, getX(colonne), getY(ligne), Color.darkGray, Color.white) ;
				
				else {
					
			
					if(demineur.getCells()[ligne][colonne].getBomb()) {
						
						drawCell(graphics , getX(colonne) , getY(ligne) , this.getBackground().darker(), Color.white);
						if(tic %2 == 0) 
							graphics.drawImage(grayB.getImage(), getX(colonne) , getY(ligne) , cellSize , cellSize , null ) ; 
						else
							graphics.drawImage(orangeB.getImage(), getX(colonne) , getY(ligne) , cellSize , cellSize , null ) ; 		
					}
					
					else {
						drawCell(graphics,getX(colonne) , getY(ligne) , this.getBackground().darker(), Color.white);
			
						switch(str) {
							case "1" : graphics.setColor(Color.blue);
								break ;
							case "2" : graphics.setColor(Color.green);
								break ;
							case "3" : graphics.setColor(Color.red);
								break ;
							case "4" : graphics.setColor(Color.yellow);
								break ;
							case "5" : graphics.setColor(Color.cyan);
								break ;
							case "6" :
							case "7" : 
							case "8" : graphics.setColor(Color.pink);
								break ;
							default  : graphics.setColor(Color.black);
							break ; 
						}
							
							graphics.drawString(str, getCenterX(colonne), getCenterY(ligne));	
					}
					
				}
				/*if(demineur.getCells()[ligne][colonne].getBomb()) 
					graphics.drawString("X" , getCenterX(colonne) , getCenterY(ligne));	*/
				
				if(demineur.getCells()[ligne][colonne].getFlag()) 
					graphics.drawImage(redF.getImage() , getX(colonne) , getY(ligne) ,cellSize , cellSize ,  null) ;
			}
		}
		drawGrid(graphics) ;
	}

	public int getX(int colonne) {
		return borderSize + colonne * cellSize ;	
	}
	
	public int getY(int ligne) {
		return borderSize + ligne * cellSize ;
	}
	
	public int getCenterX(int colonne) {
		return getX(colonne) + cellSize / 2 - 3 ;
	}
	
	public int getCenterY(int ligne) {
		return getY(ligne) + cellSize / 2 + 3 ;
	}
	
	private void drawHUD(Graphics graphics) {
		graphics.setFont(hudFont) ; 
		graphics.setColor(Color.white);
		graphics.drawString("TIME : "+ time , 50 , 70) ; 
		graphics.drawString("SHOW : "+ showed() + " / " + (sizeChoise * sizeChoise - demineur.nbrBomb()) , 50, 90);
		graphics.setColor(Color.yellow);
		int score = sizeChoise/10 * showed() ;
		graphics.drawString("SCORE : " + score , 600 , 80) ;
		graphics.setColor(Color.red);
		graphics.drawString("BOMBS : "+ demineur.nbrBomb(), 600 , 60) ; 
		
	}

	private int showed() {
		int score = 0 ; 
		for (int ligne = 0 ; ligne < sizeChoise ; ligne++) {
			for (int colonne = 0 ; colonne < sizeChoise ; colonne++) {
				if(demineur.getCells()[ligne][colonne].getShow() && !demineur.getCells()[ligne][colonne].getBomb())
					score++ ; 
			}
		}
		return score ;
	}

	public void drawGrid(Graphics graphics) {
		graphics.setColor(Color.white);
		graphics.drawRect(borderSize, borderSize, gridSize, gridSize);
	}

	public void drawCell(Graphics graphics, int x, int y, Color fill, Color border) {
		graphics.setColor(fill);
		graphics.fillRect(x, y, cellSize, cellSize);
		graphics.setColor(border);
		graphics.drawRect(x, y, cellSize, cellSize);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (!gameOver && !youWin) {
			Point coordinateClic = new Point();
			
			if(time == 0)
				timerGame.start();
			
			int colonneClic;
			int ligneClic;

			coordinateClic = e.getPoint();

			if (onGrid(coordinateClic)) {
				ligneClic = (coordinateClic.y - borderSize) / cellSize;
				colonneClic = (coordinateClic.x - borderSize) / cellSize;

				if (e.getButton() == 3) {
					boolean bool = demineur.getCells()[ligneClic][colonneClic].getFlag();
					demineur.getCells()[ligneClic][colonneClic].setFlag(!bool);

				} else {
					demineur.getCells()[ligneClic][colonneClic].setShow(true);

					if (demineur.getCells()[ligneClic][colonneClic].getBomb())
						gameOver = true;
					else {
						if (demineur.bombArround(ligneClic, colonneClic) == 0)
							propager(ligneClic, colonneClic);
					}
				}
			}
			repaint();
		}
	}
	
	public void initialize(int size) {
		demineur = new Grid(size) ;
		gameOver = false ; 
		cellSize = (800 - borderSize * 2) / size ;
		gridSize =  cellSize * size ; 
		
		int sizeNumberFont = 9;
		if(cellSize < 16) 
			sizeNumberFont = 5 ;
		if(cellSize <= 19)
			sizeNumberFont = 12 ; 
		if(cellSize > 19)
			sizeNumberFont = 14 ;
		
		numberFont = new Font("arial",Font.BOLD , sizeNumberFont) ;
		repaint() ; 
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		sizeChoise = (liste.getSelectedIndex()+1) * 10 ;
		initialize(sizeChoise) ; 
		time = 0 ; 
		timerGame.restart();
		if(tic != 0) {
			tic = 0 ; 
			timerBlink.stop() ;
		}
		youWin = false ; 
	}

	public boolean onGrid(Point p) {
		if (p.x < borderSize || p.x > borderSize + gridSize || p.y < borderSize || p.y > borderSize + gridSize)
			return false;
		return true;
	}

	public void propager(int ligne, int colonne) {

		for (int l = ligne - 1; l <= ligne + 1; l++) {
			for (int c = colonne - 1; c <= colonne + 1; c++) {

				if (colonne == c && ligne == l)
					continue;
				if (!demineur.estCellValid(l, c))
					continue;
				if (demineur.getCells()[l][c].getShow())
					continue;

				demineur.getCells()[l][c].setShow(true);
				if (demineur.bombArround(l, c) == 0)
					propager(l, c);

			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
