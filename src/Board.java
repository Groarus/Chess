/**
 * Course: COSC 2P03
 * Name: Graham Burgsma
 * Date: 14-09-20
 */

public class Board {
	private Piece[][] board = new Piece[8][8];
	private HumanPlayer humanPlayer;
	private GUI gui;

	public Board(GUI gui) {
		setUpBoard();
		this.gui = gui;

		//set up the GUI
		PaintBoard paintBoard = new PaintBoard(this);
		gui.addBoardPanel(paintBoard);
	}

	//to be converted to input text file later
	public void setUpBoard() {
		//white -- first row
		board[0][0] = new Rook(Colour.WHITE);
		board[1][0] = new Knight(Colour.WHITE);
		board[2][0] = new Bishop(Colour.WHITE);
		board[3][0] = new Queen(Colour.WHITE);
		board[4][0] = new King(Colour.WHITE);
		board[5][0] = new Bishop(Colour.WHITE);
		board[6][0] = new Knight(Colour.WHITE);
		board[7][0] = new Rook(Colour.WHITE);

		//black -- first row
		board[0][7] = new Rook(Colour.BLACK);
		board[1][7] = new Knight(Colour.BLACK);
		board[2][7] = new Bishop(Colour.BLACK);
		board[3][7] = new Queen(Colour.BLACK);
		board[4][7] = new King(Colour.BLACK);
		board[5][7] = new Bishop(Colour.BLACK);
		board[6][7] = new Knight(Colour.BLACK);
		board[7][7] = new Rook(Colour.BLACK);

		//white pawns
		for (int i = 0; i < 8; i++) {
			board[i][1] = new Pawn(Colour.WHITE);
		}
		//black pawns
		for (int i = 0; i < 8; i++) {
			board[i][6] = new Pawn(Colour.BLACK);
		}
	}

	public HumanPlayer getHumanPlayer() {
		return humanPlayer;
	}

	public void setHumanPlayer(HumanPlayer humanPlayer) {
		this.humanPlayer = humanPlayer;
	}

	public Piece[][] getBoard() {
		return board;
	}

	//only to be used by MoveEngine
	public void move(Location startLocation, Location endLocation) {
		int startX = startLocation.getX(), startY = startLocation.getY(), endX = endLocation.getX(),  endY = endLocation.getY();

		board[endX][endY] = board[startX][startY]; //moving the piece
		board[startX][startY] = null; //old location to null
		board[endX][endY].setLocation(new Location(endX, endY)); //setting the new location of the piece
		board[endX][endY].setPrevLocation(new Location(startX, startY)); //setting the previous location

		displayBoard(); //for testing
	}

	public Piece getPiece(int x, int y) {
		return board[x][y];
	}



	public void displayBoard() {
		for (int m = 7; m >= 0; m--) {
			System.out.println("\n----------------------------------------------------------------------------------------------------------------------------");
			System.out.print(" " + (m + 1) + " | ");
			for (int n = 0; n < 8; n++) {
				if (board[n][m] != null) {
					String name = board[n][m].getName().toString();
					String message;
					if (name.length() == 4)
						message = " " + board[n][m].getName().toString() + ":" + board[n][m].getColour() + " ";
					else if (name.length() == 5)
						message = " " + name + ":" + board[n][m].getColour();
					else
						message = board[n][m].getName().toString() + ":" + board[n][m].getColour();

					System.out.print(message + " | ");
				} else
					System.out.print("            " + " | ");
			}
		}
		System.out.println("\n--------- A ------------- B ------------ C ------------ D ------------ E ------------ F ------------ G ------------ H ------");
	}

}