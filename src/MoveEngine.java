/**
 * Project: Chess
 * Course: COSC 3P71 - Final Project
 * Created: December, 2014
 */
public class MoveEngine {

	private Board board;

	public MoveEngine(Board board) {
		this.board = board;
	}


	public boolean validateMove(Piece piece, Location toLocation) {
		switch (piece.getName()) {
			case PAWN:
				return pawnCheck(piece, toLocation);
			case KNIGHT:
				return knightCheck(piece, toLocation);
		}

		return true;//temporarily, so other pieces can still be moved
	}


	public void move(Piece piece, Location toLocation) {
		if (validateMove(piece, toLocation)) {
			board.move(piece.getLocation(), toLocation);
		}
	}

	private boolean pawnCheck(Piece piece, Location toLocation) {
		int black = piece.getColour() == Colour.BLACK ? -1 : 1; //if black then the checks are negated, else are positive
		Piece endPiece = board.getCurrentState().getPiece(toLocation.getX(), toLocation.getY());

				/*For the first move
				* Can either jump 1 or 2 spaces
				* Only if the previous location is null*/
		if (endPiece == null) { //if the end space is empty
			if (piece.getLocation().getX() == toLocation.getX() && (toLocation.getY() == piece.getLocation().getY() + (1 * black) || toLocation.getY() == piece.getLocation().getY() + (2 * black)) && piece.getPrevLocation() == null)
				return true;
				//for all moves after the first
			else if (piece.getLocation().getX() == toLocation.getX() && toLocation.getY() == piece.getLocation().getY() + (1 * black))
				return true;
		} else { //if the end space is not empty
			//If it is the opposite colour, and is either diagonally left or right
			if (piece.getColour() != endPiece.getColour() && (piece.getLocation().getX() == toLocation.getX() - 1 || piece.getLocation().getX() == toLocation.getX() + 1) && toLocation.getY() == piece.getLocation().getY() + (1 * black))
				return true;
		}
		return false;
	}

	private boolean knightCheck(Piece piece, Location toLocation) {
		System.out.println("No. of active thread: " + Thread.activeCount());

		Piece endPiece = board.getCurrentState().getPiece(toLocation.getX(), toLocation.getY());

		/*Checks all the x positions first, then checks the y positions
		* inside the if statement.
		 */
		if (endPiece == null || endPiece.getColour() != piece.getColour()) {
			if (toLocation.getX() == piece.getLocation().getX() - 2) {
				if (toLocation.getY() == piece.getLocation().getY() + 1) {
					return true;
				} else if (toLocation.getY() == piece.getLocation().getY() - 1) {
					return true;
				}
			} else if (toLocation.getX() == piece.getLocation().getX() - 1) {
				if (toLocation.getY() == piece.getLocation().getY() + 2) {
					return true;
				} else if (toLocation.getY() == piece.getLocation().getY() - 2) {
					return true;
				}
			} else if (toLocation.getX() == piece.getLocation().getX() + 1) {
				if (toLocation.getY() == piece.getLocation().getY() + 2) {
					return true;
				} else if (toLocation.getY() == piece.getLocation().getY() - 2) {
					return true;
				}
			} else if (toLocation.getX() == piece.getLocation().getX() + 2) {
				if (toLocation.getY() == piece.getLocation().getY() + 1) {
					return true;
				} else if (toLocation.getY() == piece.getLocation().getY() - 1) {
					return true;
				}
			}
		}
		return false;
	}
}