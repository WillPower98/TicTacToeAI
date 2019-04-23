import java.util.*;

import org.w3c.dom.Node;

public class aiTicTacToe {
	public int player; //1 for player 1 and 2 for player 2
	public int opponentPlayer;
	private int getStateOfPositionFromBoard(positionTicTacToe position, List<positionTicTacToe> board)
	{
		//a helper function to get state of a certain position in the Tic-Tac-Toe board by given position TicTacToe
		int index = position.x*16+position.y*4+position.z;
		return board.get(index).state;
	}
	
	private List<positionTicTacToe> deepCopyATicTacToeBoard(List<positionTicTacToe> board)
	{
		//deep copy of game boards
		List<positionTicTacToe> copiedBoard = new ArrayList<positionTicTacToe>();
		for(int i=0;i<board.size();i++)
		{
			copiedBoard.add(new positionTicTacToe(board.get(i).x,board.get(i).y,board.get(i).z,board.get(i).state));
		}
		return copiedBoard;
	}

	//copying the method to make a move on the board
	public boolean makeMove(positionTicTacToe position, int player, List<positionTicTacToe> targetBoard)
	{
		//make move on Tic-Tac-Toe board, given position and player 
		//player 1 = 1, player 2 = 2
		
		//brute force (obviously not a wise way though)
		for(int i=0;i<targetBoard.size();i++)
		{
			if(targetBoard.get(i).x==position.x && targetBoard.get(i).y==position.y && targetBoard.get(i).z==position.z) //if this is the position
			{
				if(targetBoard.get(i).state==0)
				{
					targetBoard.get(i).state = player;
					return true;
				}
				else
				{
					System.out.println("Error: this is not a valid move.");
				}
			}
			
		}
		return false;
	}

	public int getHeuristic(List<positionTicTacToe> targetBoard, int playerNum) {
		List<List<positionTicTacToe>> winningLines = initializeWinningLines();
		List <positionTicTacToe> boardState = targetBoard;
		int heuristicValue = 0;
		//counting the winning combinations theoretical state of the board
		for(List<positionTicTacToe> line: winningLines){
			boolean validPlay = true;
			int playValue = 0;
			for(int i = 0;i<line.size();i++) {
				positionTicTacToe winningPosition = line.get(i);
				//increasing the winPossible count if there's a friendly piece in a winning position
				if (getStateOfPositionFromBoard(winningPosition,boardState)==player) {
					playValue+=100;
				}
				//checks if there's an enemy player in the winning position
				if (getStateOfPositionFromBoard(winningPosition,boardState)!=0 && getStateOfPositionFromBoard(winningPosition,boardState)!=playerNum) {
						validPlay = false;
						break;
					}
				}
			//increasing hueristic if friendly player has to make 1 more move to win
			if (validPlay) {
				heuristicValue+=playValue;
			}
		}
		
		//checking the playerVal for the other player
		for(List<positionTicTacToe> line: winningLines){
			boolean validPlay = true;
			int playValue = 0;
			for(int i = 0;i<line.size();i++) {
				positionTicTacToe winningPosition = line.get(i);
				//increasing the winPossible count if there's a friendly piece in a winning position
				if (getStateOfPositionFromBoard(winningPosition,boardState)==opponentPlayer) {
					playValue+=100;
					break;
				}
				
				//checks if there's an enemy player in the winning position
				if (getStateOfPositionFromBoard(winningPosition,boardState)!=0 && getStateOfPositionFromBoard(winningPosition,boardState)!=opponentPlayer) {
						validPlay = false;
						break;
					}
				}
			//decrasing hueristic if enemy player has to make moves to win
			if (validPlay) {
				heuristicValue-=playValue;
			}
			
		
		}
		return heuristicValue;
	}

	public int miniMax(List<positionTicTacToe> targetBoard, int depth, boolean maxPlayer){
		if(depth == 0){
			return getHeuristic(targetBoard, player);
		}
		if(maxPlayer){
			int max = -9000;
			positionTicTacToe bestMove = null;
			List<positionTicTacToe> deepCopy = deepCopyATicTacToeBoard(targetBoard);
			//adding onto the rootNode all possible moves by both players
			for(positionTicTacToe position: deepCopy){
				int value = 0;
				if(getStateOfPositionFromBoard(position, deepCopy) == 0){
					List<positionTicTacToe> childBoard = deepCopyATicTacToeBoard(targetBoard);
					makeMove(position, player, childBoard);
					value = miniMax(childBoard, depth-1, false);
					if (value > max) {
						max = value;
						bestMove = position;
					}
				}
			}
			makeMove(bestMove,player,targetBoard);
			return max;
			
		}
		else {
			int min = 9000;
			positionTicTacToe bestMove = null;
			List<positionTicTacToe> deepCopy = deepCopyATicTacToeBoard(targetBoard);
			for(positionTicTacToe position: deepCopy){
				int value = 0;
				if(getStateOfPositionFromBoard(position, deepCopy) == 0){
					List<positionTicTacToe> childBoard = deepCopyATicTacToeBoard(targetBoard);
					makeMove(position, opponentPlayer, childBoard);
					value = miniMax(childBoard, depth-1, true);
					if (value < min) {
						min = value;
						bestMove = position;
					}
				}
			}
			
			
			makeMove(bestMove, opponentPlayer, targetBoard);
			return min;
		}
	}
	public positionTicTacToe myAIAlgorithm(List<positionTicTacToe> board, int player)
	{
		//TODO: this is where you are going to implement your AI algorithm to win the game. The default is an AI randomly choose any available move.
		positionTicTacToe myNextMove = null;
		if(player == 1) {
				//using the minMax to get the score
				int max = -9000;
				List<positionTicTacToe> deepCopy = deepCopyATicTacToeBoard(board);
				for(positionTicTacToe position: board){
					int value = 0;
					if(getStateOfPositionFromBoard(position, deepCopy) == 0){
						List<positionTicTacToe> childBoard = deepCopyATicTacToeBoard(board);
						makeMove(position, player, childBoard);
						value = miniMax(childBoard, 2, true);
						if (value > max) {
							max = value;
							myNextMove = position;
						}
					}
				}
				
				System.out.println(max);
		}
				
				//using human input as player 2 User Input
				if(player == 2) {
					Scanner reader = new Scanner(System.in);  
					System.out.println("Please enter your move in (row, col, z) format with only a space in between: ");
					int humanMovex = reader.nextInt(); 
					int humanMovey = reader.nextInt(); 
					int humanMovez = reader.nextInt();
					positionTicTacToe humanmove = new positionTicTacToe(humanMovex,humanMovey,humanMovez);
					myNextMove = humanmove;
				}
		printBoardTicTacToe(board);
		return myNextMove;
			
		
	}
	private List<List<positionTicTacToe>> initializeWinningLines()
	{
		//create a list of winning line so that the game will "brute-force" check if a player satisfied any 	winning condition(s).
		List<List<positionTicTacToe>> winningLines = new ArrayList<List<positionTicTacToe>>();
		
		//48 straight winning lines
		//z axis winning lines
		for(int i = 0; i<4; i++)
			for(int j = 0; j<4;j++)
			{
				List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
				oneWinCondtion.add(new positionTicTacToe(i,j,0,-1));
				oneWinCondtion.add(new positionTicTacToe(i,j,1,-1));
				oneWinCondtion.add(new positionTicTacToe(i,j,2,-1));
				oneWinCondtion.add(new positionTicTacToe(i,j,3,-1));
				winningLines.add(oneWinCondtion);
			}
		//y axis winning lines
		for(int i = 0; i<4; i++)
			for(int j = 0; j<4;j++)
			{
				List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
				oneWinCondtion.add(new positionTicTacToe(i,0,j,-1));
				oneWinCondtion.add(new positionTicTacToe(i,1,j,-1));
				oneWinCondtion.add(new positionTicTacToe(i,2,j,-1));
				oneWinCondtion.add(new positionTicTacToe(i,3,j,-1));
				winningLines.add(oneWinCondtion);
			}
		//x axis winning lines
		for(int i = 0; i<4; i++)
			for(int j = 0; j<4;j++)
			{
				List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
				oneWinCondtion.add(new positionTicTacToe(0,i,j,-1));
				oneWinCondtion.add(new positionTicTacToe(1,i,j,-1));
				oneWinCondtion.add(new positionTicTacToe(2,i,j,-1));
				oneWinCondtion.add(new positionTicTacToe(3,i,j,-1));
				winningLines.add(oneWinCondtion);
			}
		
		//12 main diagonal winning lines
		//xz plane-4
		for(int i = 0; i<4; i++)
			{
				List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
				oneWinCondtion.add(new positionTicTacToe(0,i,0,-1));
				oneWinCondtion.add(new positionTicTacToe(1,i,1,-1));
				oneWinCondtion.add(new positionTicTacToe(2,i,2,-1));
				oneWinCondtion.add(new positionTicTacToe(3,i,3,-1));
				winningLines.add(oneWinCondtion);
			}
		//yz plane-4
		for(int i = 0; i<4; i++)
			{
				List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
				oneWinCondtion.add(new positionTicTacToe(i,0,0,-1));
				oneWinCondtion.add(new positionTicTacToe(i,1,1,-1));
				oneWinCondtion.add(new positionTicTacToe(i,2,2,-1));
				oneWinCondtion.add(new positionTicTacToe(i,3,3,-1));
				winningLines.add(oneWinCondtion);
			}
		//xy plane-4
		for(int i = 0; i<4; i++)
			{
				List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
				oneWinCondtion.add(new positionTicTacToe(0,0,i,-1));
				oneWinCondtion.add(new positionTicTacToe(1,1,i,-1));
				oneWinCondtion.add(new positionTicTacToe(2,2,i,-1));
				oneWinCondtion.add(new positionTicTacToe(3,3,i,-1));
				winningLines.add(oneWinCondtion);
			}
		
		//12 anti diagonal winning lines
		//xz plane-4
		for(int i = 0; i<4; i++)
			{
				List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
				oneWinCondtion.add(new positionTicTacToe(0,i,3,-1));
				oneWinCondtion.add(new positionTicTacToe(1,i,2,-1));
				oneWinCondtion.add(new positionTicTacToe(2,i,1,-1));
				oneWinCondtion.add(new positionTicTacToe(3,i,0,-1));
				winningLines.add(oneWinCondtion);
			}
		//yz plane-4
		for(int i = 0; i<4; i++)
			{
				List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
				oneWinCondtion.add(new positionTicTacToe(i,0,3,-1));
				oneWinCondtion.add(new positionTicTacToe(i,1,2,-1));
				oneWinCondtion.add(new positionTicTacToe(i,2,1,-1));
				oneWinCondtion.add(new positionTicTacToe(i,3,0,-1));
				winningLines.add(oneWinCondtion);
			}
		//xy plane-4
		for(int i = 0; i<4; i++)
			{
				List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
				oneWinCondtion.add(new positionTicTacToe(0,3,i,-1));
				oneWinCondtion.add(new positionTicTacToe(1,2,i,-1));
				oneWinCondtion.add(new positionTicTacToe(2,1,i,-1));
				oneWinCondtion.add(new positionTicTacToe(3,0,i,-1));
				winningLines.add(oneWinCondtion);
			}
		
		//4 additional diagonal winning lines
		List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
		oneWinCondtion.add(new positionTicTacToe(0,0,0,-1));
		oneWinCondtion.add(new positionTicTacToe(1,1,1,-1));
		oneWinCondtion.add(new positionTicTacToe(2,2,2,-1));
		oneWinCondtion.add(new positionTicTacToe(3,3,3,-1));
		winningLines.add(oneWinCondtion);
		
		oneWinCondtion = new ArrayList<positionTicTacToe>();
		oneWinCondtion.add(new positionTicTacToe(0,0,3,-1));
		oneWinCondtion.add(new positionTicTacToe(1,1,2,-1));
		oneWinCondtion.add(new positionTicTacToe(2,2,1,-1));
		oneWinCondtion.add(new positionTicTacToe(3,3,0,-1));
		winningLines.add(oneWinCondtion);
		
		oneWinCondtion = new ArrayList<positionTicTacToe>();
		oneWinCondtion.add(new positionTicTacToe(3,0,0,-1));
		oneWinCondtion.add(new positionTicTacToe(2,1,1,-1));
		oneWinCondtion.add(new positionTicTacToe(1,2,2,-1));
		oneWinCondtion.add(new positionTicTacToe(0,3,3,-1));
		winningLines.add(oneWinCondtion);
		
		oneWinCondtion = new ArrayList<positionTicTacToe>();
		oneWinCondtion.add(new positionTicTacToe(0,3,0,-1));
		oneWinCondtion.add(new positionTicTacToe(1,2,1,-1));
		oneWinCondtion.add(new positionTicTacToe(2,1,2,-1));
		oneWinCondtion.add(new positionTicTacToe(3,0,3,-1));
		winningLines.add(oneWinCondtion);	
		
		return winningLines;
		
	}
	
	public void printBoardTicTacToe(List<positionTicTacToe> targetBoard)
	{
		//print each position on the board, uncomment this for debugging if necessary
		/*
		System.out.println("board:");
		System.out.println("board slots: "+board.size());
		for (int i=0;i<board.size();i++)
		{
			board.get(i).printPosition();
		}
		*/
		
		//print in "graphical" display
		for (int i=0;i<4;i++)
		{
			System.out.println("level(z) "+i);
			for(int j=0;j<4;j++)
			{
				System.out.print("["); // boundary
				for(int k=0;k<4;k++)
				{
					if (getStateOfPositionFromBoard(new positionTicTacToe(j,k,i),targetBoard)==1)
					{
						System.out.print("X"); //print cross "X" for position marked by player 1
					}
					else if(getStateOfPositionFromBoard(new positionTicTacToe(j,k,i),targetBoard)==2)
					{
						System.out.print("O"); //print cross "O" for position marked by player 2
					}
					else if(getStateOfPositionFromBoard(new positionTicTacToe(j,k,i),targetBoard)==0)
					{
						System.out.print("_"); //print "_" if the position is not marked
					}
					if(k==3)
					{
						System.out.print("]"); // boundary
						System.out.println();
					}
					
					
				}

			}
			System.out.println();
		}
	}
	public aiTicTacToe(int setPlayer)
	{
		player = setPlayer;
		if (player==1) {
			opponentPlayer = 2;
		} else if (player==2) {
			opponentPlayer = 1;
		}
	}
}
