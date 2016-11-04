// Mancala
// Casey Astiz
// May 16, 2016

public class Board {
	
	int[] board = new int[14]; 
	
	//initializes the board
	public Board(int[] array){
		this.board= array.clone();
	}
	
	// calculates the score of the move
	public int heuristic(){
		//true = player 1
		int score = 0;
		
		//if computer wins, score is positive infinity
		if(isTerminal()==1){
			score = Integer.MAX_VALUE;
		}//if opponent wins, score is negative infinity
		if(isTerminal()==-1){
			score = Integer.MIN_VALUE;
		}if(isTerminal()==0){
			score = score(6) - score(13);
		}
		return score;
	}
	
	// returns if the move is a terminal board state or not
	// 1 = player1 wins; -1 = player 2 wins; 0 = neutral
	public int isTerminal(){

		if(board[0]==0 && board[1]==0 && board[2]==0 && 
				board[3]==0 && board[4]==0 && board[5]==0){
			if(score(6)>score(13)){
				return 1;
			}else{
				return -1;
			}
			
		}if(board[7]==0 && board[8]==0 && board[9]==0 && 
					board[10]==0 && board[11]==0 && board[12]==0){
			
			if(score(6)>score(13)){
				return 1;
			}else{
				return -1;
			}
		}
		return 0;
	}
	
	//indicates score of a given side depending on pit
	public int score(int pit){
		int score = 0;
		if(pit == 6){
			for(int i = 0; i<7;i++){
				score += board[pit-i];
			}
		}else{
			for(int i = 0; i<7;i++){
				score += board[pit-i];
			}
		}
		return score;
	}
    
	// creates all possible children boards
	public Board[] children(boolean player1){
		Board[] children = new Board[6];
		if(player1){
			for(int i = 0; i<6; i++){
				if(board[i]==0){
					children[i] = null;
			}else{
					children[i] = playgame(i, board.clone());
				}
			}
		}else{
			for(int i = 0; i<6; i++){
				if(board[i+7]==0){
					children[i] = null;
			}else{
					children[i] = playgame(i+7, board.clone());
				}		
			}
		}
		return children;
	}
	
	// creates the boards based on which move is selected
	public static Board playgame(int clicked, int[] startingBoard){
    	int marbles = startingBoard[clicked];
    	if(marbles!=0){
    		if(startingBoard[(clicked+marbles)%14]== 0 && (clicked+marbles)%14!= 13
    				&& (clicked+marbles)%14!= 6){
    			for(int i = 1; i< marbles; i++){
    				startingBoard[(clicked+i)%14] += 1;
    			}
    			if (clicked>6){
    				capture((clicked+marbles)%14,13, startingBoard);
    			}else{
    				capture((clicked+marbles)%14,6, startingBoard);
    			}
    		}else{
    			for(int i = 1; i<= marbles; i++){
    				startingBoard[(clicked+i)%14] += 1;
    			}
    		}
    	}
    	startingBoard[clicked] = 0;
    	return new Board(startingBoard);
    	}
	
	// called on in case move creates a capture
	public static void capture(int index, int pit, int[] b){
    	if(b != null){
    		int length = b.length; 
    		b[pit] += 1+ b[length-(index+2)];
    		b[index] = 0;
    		b[length-(index+2)] = 0;
		}
	}
}