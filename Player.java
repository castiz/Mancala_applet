// Mancala
// Casey Astiz
// May 16, 2016

public class Player {
	
	boolean player1;
	
	// initializes the player
	public Player(boolean player){
		this.player1 = player;
	}
	
	// finds out optimal board move to make
	// returns Integer so as to call board[i]
	// in MancalaCanvas class
	public Integer makemove(Board board, int maxdepth){
		Board[] children = board.children(player1);
		Integer child = 0;
		Integer minOrmax = 0;
		if(player1){
			minOrmax = Integer.MIN_VALUE;
		}else{
			minOrmax = Integer.MAX_VALUE;
			}
		
		for (int i = 0; i<children.length; i++){
			if(children[i]==null){
				continue;
			}
			Integer score = calculateMove(children[i], player1, maxdepth-1);
			if(!player1){
				if (score< minOrmax){
					child = i;
					minOrmax = score;	
				}
			}else{
				if (score> minOrmax){
					child = i;
					minOrmax = score;
				}
			}
		} 
		return child;
	}
	
	// recursive function that calculates the move at each branch
	public Integer calculateMove(Board board, boolean minimizing, int depth){
		//base cases
		int score = board.heuristic();
		Integer minOrMaxChildScore = 0;
		Board[] children = board.children(!minimizing);
		
		// game ending position
		if(score == Integer.MIN_VALUE ||score == Integer.MAX_VALUE){
			return score;
			
		}// at farthest depth
		if (depth == 0){
			return score;
		}// no child boards
		if (children.length==0){
			System.out.println("base case there are no children boards");
			return score;
		}
		
		if(!minimizing){
			minOrMaxChildScore = Integer.MIN_VALUE;
		}else{
			minOrMaxChildScore = Integer.MAX_VALUE;
		}
		
		for(int i=0;i<children.length; i++){
			if(children[i]==null){
				continue;
			}
			Integer childscore = calculateMove(children[i], !minimizing,depth-1);
			if(!minimizing){
				minOrMaxChildScore = Math.max(Integer.MIN_VALUE, childscore);
			}else{
				minOrMaxChildScore = Math.min(Integer.MAX_VALUE, childscore);
			}
		}
		return minOrMaxChildScore;
	}
	
}
