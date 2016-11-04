// Mancala
// Casey Astiz
// May 16, 2016

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

@SuppressWarnings("serial")

public class Mancala extends Applet implements ActionListener, ItemListener {

    // instance variables
    MancalaCanvas c;            // canvas used to display the game
    Button newgameButton;       // button used to clear the game
    Label p1score;              // label used to show score for player 1
    Label p2score;              // label used to show score for player 2
    Label turn;					//displays who's turn it is
    Choice players;				//choice allows for two humans, one computer
    int gamestate;				//one human one computer, or two humans
    
    // contains all of the elements of the applet
    public void init () {
         setLayout(new BorderLayout());
         
         Panel pn = new Panel();
         pn.setLayout(new BorderLayout());
         pn.add("North", new Label("   Player 2"));
         
         Panel ps = new Panel();
         ps.setLayout(new BorderLayout());
         ps.add("South", new Label("Player 1"
                         + "			 ", Label.RIGHT));
         turn = new Label("It is Player 1's Turn.", Label.CENTER);
         ps.add("Center", turn);
        
         Panel pe = new Panel();
         pe.setLayout(new GridLayout(6,1));
         players = new Choice();
         players.add("Player 1 and Player 2");
         players.add("Player 1 and Computer");
         players.addItemListener(this);
         pe.add(players);
         newgameButton = new Button("New Game");
         newgameButton.setBackground(Color.white);
         newgameButton.addActionListener(this);
         pe.add(newgameButton);
         pe.add(new Label("  Player 1 score:   "));
         p1score = new Label("24", Label.CENTER);
         pe.add(p1score);
         pe.add(new Label("  Player 2 score:   "));
         p2score = new Label("24", Label.CENTER);
         pe.add(p2score);
        
         Color background = new Color(153, 225, 225);
         Panel pc = new Panel();
         pc.setLayout(new BorderLayout());
         pc.setBackground(Color.black);
         add("North", pc);
         c = new MancalaCanvas();
         c.setBackground(background);
         c.addMouseListener(c);
         pc.add("Center", c);        
        
         this.setLayout(new BorderLayout());
         this.add("Center", pc);
         this.add("North", pn);
         this.add("South", ps);  
         this.add("East", pe);
    }
    
    // checks for new game button
    public void actionPerformed(ActionEvent e) {
         if (e.getSource() == newgameButton) {
            c.newgame();
         }
    }
    
    // checks if the drop down menu has been clicked
    public void itemStateChanged(ItemEvent e){
    	if(e.getSource() == players){
    		if(e.getItem() == players.getItem(0)){
       	 		gamestate = 1;
       	 	}else if(e.getItem() == players.getItem(1)){
       	 		gamestate = 2;
       	 	}
        }
    }

//@SuppressWarnings("serial")


public class MancalaCanvas extends Canvas implements MouseListener  {
	
    // instance variables representing the game go here
    int c = 6;
	int[] board = new int[14]; 
    int size = 80; // width & height
    int border = 20;
    int pockets = (board.length - 2)/2; // this is the amount of playable 
    int secondRow = 7;					// pockets on one side
    boolean whosTurn;
    
   // initializes a new board 
   public MancalaCanvas(){
    	for(int i = 0; i<pockets; i++){
    		board[i]=4;
    	}for(int i = 7; i<13; i++){
    		board[i]=4;
    	}whosTurn = true;
    	if (gamestate == 2){
    		whosTurn = false;
    	}
    }

    // draw the board
    public void paint(Graphics g) {
    	Color darkBrown = new Color(185, 128, 14);
    	Color brown = new Color(225, 184, 102);
    	int bx = border;
        int by = border;
        g.setColor(darkBrown);
        g.fillRect(bx, by, size, size*2);
        g.setColor(Color.black);
        g.drawRect(bx, by, size, size*2);
        g.drawString(Integer.toString(board[13]), bx + size/2, by+size);
        for (int i = 0; i < pockets; i++) {
        		g.setColor(brown);
                int x = i * size + border + size;
                int y = border;
                g.fillRect(x, y, size, size);
                g.setColor(Color.black);
                g.drawRect(x, y, size, size);
                int s = board[12-i];
                centerString(g, Integer.toString(s), x, y);
        }
         for (int i = 0; i < pockets; i++) {
        	 g.setColor(brown);
             int x = i * size + border + size;
             int y =size + border;
             g.fillRect(x, y, size, size);
             g.setColor(Color.black);
             g.drawRect(x, y, size, size);
             int s = board[i];
             centerString(g, Integer.toString(s), x, y);
      	}
        int ax = border+size*c+size;
        int ay = border;
        g.setColor(darkBrown);
        g.fillRect(ax, ay, size, size*2);
        g.setColor(Color.black);
        g.drawRect(ax, ay, size, size*2);
        g.drawString(Integer.toString(board[6]), ax + size/2, ay+size);
    }

    // handle mouse events
    public void mousePressed(MouseEvent event) {
        Point p = event.getPoint();
        // check if clicked in box area
        int x = p.x - border - size;
        int y = p.y - border;

        if (x >= 0 && x < c*size &&
            y >= 0 && y < c*size) {

            int k = y / size;
            int l = x / size;
            // if the game is over, no one can play
            if(k>0 && !gameover(board)){
            	if(whosTurn && board[l]!=0){
            		playgame(l);
            		System.out.println("human move");}
            }
            // prevents players from clicking on their side of the board when
            // it's not their turn
            else{
            	if(!whosTurn && board[12-k*7 - l]!=0){
            		playgame(12-k*7 - l);
            		System.out.println("human move"); // debugging code
            										  // shows if human moved
            	}
            }
        }
        repaint();
        
        if(gamestate==2 && !whosTurn){
        	computermove();
        }
    }

    // methods called from the event handler of the main applet
    
    // calls player class to make moves
    public void computermove(){
    	Board board2 = new Board(board);
    	//create player objects
    	Player player2 = new Player(false);
    	if (gamestate==2){
    		int move = player2.makemove(board2, 4);
    		System.out.println("move= " + move); // debugging code; 
    		playgame(move+7);                    // left in to show computer moves
    	}	
    }
    
    // what happens if new game is called
    // resets the game
    public void newgame() {
    	whosTurn=true;
    	turn.setText("It is Player 1's Turn.");
    	board[6] = 0;
    	board[13] = 0;
    	p1score.setText(Integer.toString(24));
		p2score.setText(Integer.toString(24));
        for (int i = 0; i < pockets; i++) {
        	board[i] = 4;
        }for (int i = 0; i < pockets; i++) {
            board[secondRow + i] = 4;
        }
        repaint();
    }
    
    // main method to play the game
    public void playgame(int clicked){
    	int marbles = board[clicked];
    	board[clicked] = 0;
    	if(marbles!=0){
    		// capture
    		if(board[(clicked+marbles)%14]== 0 && (clicked+marbles)%14!= 13
    				&& (clicked+marbles)%14!= 6){
    			for(int i = 1; i< marbles; i++){
    				board[(clicked+i)%14] += 1;
    			}
    			if (clicked>6){
    				capture((clicked+marbles)%14,13);
    			}else{
    				capture((clicked+marbles)%14,6);
    			}
    		}
    		// general gameplay
    		else{
    			for(int i = 1; i<= marbles; i++){
    				board[(clicked+i)%14] += 1;
    			}
    		}
    	}
    	playerTurn((clicked + marbles)%14);
    	// change player turn label
    	if(!whosTurn){
        	turn.setText("It is Player 2's Turn.");}
        else{
        	turn.setText("It is Player 1's Turn.");
        }
		p1score.setText(Integer.toString(score(6)));
		p2score.setText(Integer.toString(score(13)));
		
		// is the game over?
		if(gameover(board)){
			String winner;
			if(score(13)>score(6)){
				winner = ("The Winner is Player 2!");
			}else if (score(13)==score(6)){
				winner = ("It's a tie game!");
			}else{
				winner = ("The Winner is Player 1");
			}
			turn.setText("Game Over. " + winner);
		}
		
    }
  
    // if you land your last marble on an empty spot, you capture
    // that marble, as well as the marble on the parallel spot
    public void capture(int index, int pit){
    	int length = board.length; 
		board[pit] += 1+ board[length-(index+2)];
		board[index] = 0;
		board[length-(index+2)] = 0;
	}
	
    // figures out who's turn it is and updates
    // variable whosTurn
    public boolean playerTurn(int index){
    	if (index== 6 ){
    		whosTurn = true;
    	}else if(index ==13){
    		whosTurn = false;
    	}else{
    		whosTurn = !whosTurn;	
    	}
    	
    	return whosTurn;
    }
	
    // checks if the game is over
    public boolean gameover(int[] board){
        if (board[0]==0&&board[1]==0&&board[2]==0&&
        		board[3]==0&&board[4]==0&&board[5]==0){
                return true;
        }
        else if(board[7]==0&&board[8]==0&&board[9]==0
        		&&board[10]==0&&board[11]==0&&board[12]==0){
                return true;
        }
        return false;
    }
   
    
    // indicates score of a given side or pit
    public int score(int pit){
		int score = 0;
		for(int i = 0; i<= pockets;i++){
			score += board[pit-i];
		}
		return score;
	}
    
    // helper method to draw a String centered at x, y
    public void centerString(Graphics g, String s, int x, int y) {
        FontMetrics fm = g.getFontMetrics(g.getFont());
        int xs = x + size/2 - fm.stringWidth(s)/2;
        int ys = y + size/2 + fm.getAscent()/4;
        g.drawString(s, xs, ys);
    }
    
    // need these also because we implement a MouseListener
    public void mouseReleased(MouseEvent event) { }
    public void mouseClicked(MouseEvent event) { }
    public void mouseEntered(MouseEvent event) { }
    public void mouseExited(MouseEvent event) { }
        
	}
}