/**
 * Project: Chess
 * Course: COSC 3P71 - Final Project
 * Created: December, 2014
 */


public class Board {
	private State currentState = new State();
	private HumanPlayer humanPlayer;

	public Board(GUI gui) {
		setUpBoard();

		//set up the GUI
		PaintBoard paintBoard = new PaintBoard(this);
		gui.addBoardPanel(paintBoard);
	}

	//to be converted to input text file later
	public void setUpBoard() {
		//white -- first row
		currentState.setPiece(0, 0, new Rook(Colour.WHITE));
		currentState.setPiece(1, 0, new Knight(Colour.WHITE));
		currentState.setPiece(2, 0, new Bishop(Colour.WHITE));
		currentState.setPiece(3, 0, new Queen(Colour.WHITE));
		currentState.setPiece(4, 0, new King(Colour.WHITE));
		currentState.setPiece(5, 0, new Bishop(Colour.WHITE));
		currentState.setPiece(6, 0, new Knight(Colour.WHITE));
		currentState.setPiece(7, 0, new Rook(Colour.WHITE));

		//black -- first row
		currentState.setPiece(0, 7, new Rook(Colour.BLACK));
		currentState.setPiece(1, 7, new Knight(Colour.BLACK));
		currentState.setPiece(2, 7, new Bishop(Colour.BLACK));
		currentState.setPiece(3, 7, new Queen(Colour.BLACK));
		currentState.setPiece(4, 7, new King(Colour.BLACK));
		currentState.setPiece(5, 7, new Bishop(Colour.BLACK));
		currentState.setPiece(6, 7, new Knight(Colour.BLACK));
		currentState.setPiece(7, 7, new Rook(Colour.BLACK));

		//white pawns
		for (int i = 0; i < 8; i++) {
			currentState.setPiece(i, 1, new Pawn(Colour.WHITE));
		}
		//black pawns
		for (int i = 0; i < 8; i++) {
			currentState.setPiece(i, 6, new Pawn(Colour.BLACK));
		}
	}

	public HumanPlayer getHumanPlayer() {
		return humanPlayer;
	}

	public void setHumanPlayer(HumanPlayer humanPlayer) {
		this.humanPlayer = humanPlayer;
	}


	//only to be used by MoveEngine
	public void move(Location startLocation, Location endLocation) {
		int startX = startLocation.getX(), startY = startLocation.getY(), endX = endLocation.getX(),  endY = endLocation.getY();

		currentState.setPiece(endX, endY, currentState.getPiece(startX, startY));//moving the piece
		currentState.setPiece(startX, startY, null); //old location to null
		currentState.getPiece(endX, endY).setLocation(new Location(endX, endY)); //setting the new location of the piece
		currentState.getPiece(endX, endY).setPrevLocation(new Location(startX, startY)); //setting the previous location

		displayBoard(); //for testing
	}

	public State getCurrentState() {
		return currentState;
	}



	public void displayBoard() {
		for (int m = 7; m >= 0; m--) {
			System.out.println("\n----------------------------------------------------------------------------------------------------------------------------");
			System.out.print(" " + (m + 1) + " | ");
			for (int n = 0; n < 8; n++) {
				if (currentState.getPiece(n, m) != null) {
					String name = currentState.getPiece(n, m).getName().toString();
					String message;
					if (name.length() == 4)
						message = " " + currentState.getPiece(n, m).getName().toString() + ":" + currentState.getPiece(n, m).getColour() + " ";
					else if (name.length() == 5)
						message = " " + name + ":" + currentState.getPiece(n, m).getColour();
					else
						message = currentState.getPiece(n, m).getName().toString() + ":" + currentState.getPiece(n, m).getColour();

					System.out.print(message + " | ");
				} else
					System.out.print("            " + " | ");
			}
		}
		System.out.println("\n--------- A ------------- B ------------ C ------------ D ------------ E ------------ F ------------ G ------------ H ------");
	}

}