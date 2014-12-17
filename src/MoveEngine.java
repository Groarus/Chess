import java.util.Stack;

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


    public boolean validateMove(Piece piece, Location toLocation, Board board) {
        switch (piece.getName()) {
            case PAWN:
                return pawnCheck(piece, toLocation, board);
            case KNIGHT:
                return knightCheck(piece, toLocation, board);
            case BISHOP:
                return bishopCheck(piece, toLocation, board);
            case ROOK:
                return rookCheck(piece, toLocation, board);
            case QUEEN:
                return rookCheck(piece, toLocation, board) || bishopCheck(piece, toLocation, board);
            case KING:
                return kingCheck(piece, toLocation, board);
        }

        return true;//temporarily, so other pieces can still be moved
    }


    public void move(Piece piece, Location toLocation) {
        if (validateMove(piece, toLocation, this.board) && canMove(piece, toLocation)) {
            boolean overtaken = false;
            if (board.getCurrentState().getPiece(toLocation).getColour() != Colour.NEUTRAL && board.getCurrentState().getPiece(toLocation).getColour() != piece.getColour())
                overtaken = true;

            //increment number of moves and decrement pieces left if overtaken == true
            if (piece.getColour() == Colour.WHITE) {
                board.getWhitePlayer().incrementMoves();
                if (overtaken)
                    board.getBlackPlayer().decPiecesLeft();
            } else {
                board.getBlackPlayer().incrementMoves();
                if (overtaken)
                    board.getWhitePlayer().decPiecesLeft();
            }
            board.move(piece.getLocation(), toLocation);
            highlightCheck();
        }
    }

    private boolean pawnCheck(Piece piece, Location toLocation, Board board) {
        Piece endPiece = board.getCurrentState().getPiece(toLocation.getX(), toLocation.getY());
        int xDif = Math.abs(piece.getLocation().getX() - toLocation.getX());
        int yDif = Math.abs(piece.getLocation().getY() - toLocation.getY());

        int maxSpaces;
        if (piece.getPrevLocation() == null) {
            maxSpaces = 2;//First move
        } else {
            maxSpaces = 1;
        }
         /*
            If the pawn is moving up (or down for computer) and is only moving the right number of spaces and the space occupied is empty
            Allow it to move.
            If it is moving diagonally and the space has an enemy piece, allow it to be taken
         */
        if (yDif <= maxSpaces & ((piece.getColour() == Colour.BLACK & piece.getLocation().getY() > toLocation.getY()) | (piece.getColour() == Colour.WHITE & piece.getLocation().getY() < toLocation.getY()))) {
            if (piece.getLocation().getX() == toLocation.getX() & endPiece.getName() == Piece.Name.EMPTY) {
                return true;
            } else
                return endPiece.getName() != Piece.Name.EMPTY && endPiece.getColour() != piece.getColour() && xDif == 1 && yDif == 1; //Pawn Capture
        } else {
            return false;
        }
    }

    private boolean knightCheck(Piece piece, Location toLocation, Board board) {
        Piece endPiece = board.getCurrentState().getPiece(toLocation.getX(), toLocation.getY());
        int xDif = Math.abs(piece.getLocation().getX() - toLocation.getX());
        int yDif = Math.abs(piece.getLocation().getY() - toLocation.getY());
        int maxSpaces = 3;

        /*
        Test different way for knight move:
        If the xDifference is less than the max spaces
        And the yDifference is less than the max spaces
        And both are greater than 0
        And the knight is being moved exactly 3 spaces
        Return if there is no one there or if the piece enemy.
         */

        if (xDif <= maxSpaces & yDif <= maxSpaces & xDif > 0 & yDif > 0 & (yDif + xDif == maxSpaces)) {
            return endPiece.getColour() != piece.getColour();
        } else {
            return false;
        }
    }

    private boolean bishopCheck(Piece piece, Location toLocation, Board board) {
        Piece endPiece = board.getCurrentState().getPiece(toLocation.getX(), toLocation.getY());
        int xDif = Math.abs(piece.getLocation().getX() - toLocation.getX());
        int yDif = Math.abs(piece.getLocation().getY() - toLocation.getY());
        int maxSpaces = 1;
        try {
            if (piece.getLocation().getY() < toLocation.getY()) { //Moving up
                if (piece.getLocation().getX() > toLocation.getX()) {            //Moving left
                    for (int i = 1; i < xDif; i++) {
                        if (board.getCurrentState().getPiece(piece.getLocation().getX() - i, piece.getLocation().getY() + i).getName() == Piece.Name.EMPTY) {
                            maxSpaces++; //No piece in the way
                        }
                    }
                } else if (piece.getLocation().getX() < toLocation.getX()) {            //Moving right
                    for (int i = 1; i < xDif; i++) {
                        if (board.getCurrentState().getPiece(piece.getLocation().getX() + i, piece.getLocation().getY() + i).getName() == Piece.Name.EMPTY) {
                            maxSpaces++;//No piece in the way
                        }
                    }
                }
            } else {//Moving down
                if (piece.getLocation().getX() > toLocation.getX()) {            //Moving left
                    for (int i = 1; i < xDif; i++) {
                        if (board.getCurrentState().getPiece(piece.getLocation().getX() - i, piece.getLocation().getY() - i).getName() == Piece.Name.EMPTY) {
                            maxSpaces++;//No piece in the way
                        }
                    }
                } else if (piece.getLocation().getX() < toLocation.getX()) {            //Moving right
                    for (int i = 1; i < xDif; i++) {
                        if (board.getCurrentState().getPiece(piece.getLocation().getX() + i, piece.getLocation().getY() - i).getName() == Piece.Name.EMPTY) {
                            maxSpaces++;//No piece in the way
                        }
                    }
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            //Do nothing
        }

        if (xDif == yDif && xDif == maxSpaces) {//Moving diagonally with no one in between
            return endPiece.getColour() != piece.getColour(); //Empty or enemy
        } else {
            return false;
        }
    }

    private boolean rookCheck(Piece piece, Location toLocation, Board board) {
        Piece endPiece = board.getCurrentState().getPiece(toLocation.getX(), toLocation.getY());
        int xDif = Math.abs(piece.getLocation().getX() - toLocation.getX());
        int yDif = Math.abs(piece.getLocation().getY() - toLocation.getY());
        int maxSpaces = 1;

        //Four ifs to check if there is nothing in between where it needs to go
        if (piece.getLocation().getY() < toLocation.getY()) { //Moving up
            for (int i = 1; i < yDif; i++) {
                if (board.getCurrentState().getPiece(piece.getLocation().getX(), piece.getLocation().getY() + i).getName() == Piece.Name.EMPTY) {
                    maxSpaces++; //No piece in the way
                }
            }
        } else if (piece.getLocation().getY() > toLocation.getY()) {//Moving down
            for (int i = 1; i < yDif; i++) {
                if (board.getCurrentState().getPiece(piece.getLocation().getX(), piece.getLocation().getY() - i).getName() == Piece.Name.EMPTY) {
                    maxSpaces++;//No piece in the way
                }
            }
        } else if (piece.getLocation().getX() > toLocation.getX()) {//Moving left
            for (int i = 1; i < xDif; i++) {
                if (board.getCurrentState().getPiece(piece.getLocation().getX() - i, piece.getLocation().getY()).getName() == Piece.Name.EMPTY) {
                    maxSpaces++;//No piece in the way
                }
            }
        } else if (piece.getLocation().getX() < toLocation.getX()) {//Moving right
            for (int i = 1; i < xDif; i++) {
                if (board.getCurrentState().getPiece(piece.getLocation().getX() + i, piece.getLocation().getY()).getName() == Piece.Name.EMPTY) {
                    maxSpaces++;//No piece in the way
                }
            }
        }

        if (((xDif == 0 & yDif > 0) || (yDif == 0 & xDif > 0)) && ((yDif == maxSpaces) || (xDif == maxSpaces))) {
            return endPiece.getColour() != piece.getColour();
        } else {
            return false;
        }

    }

    private boolean kingCheck(Piece piece, Location toLocation, Board board) {
        Piece endPiece = board.getCurrentState().getPiece(toLocation.getX(), toLocation.getY());
        int xDif = Math.abs(piece.getLocation().getX() - toLocation.getX());
        int yDif = Math.abs(piece.getLocation().getY() - toLocation.getY());
        int maxSpaces = 1;

        if ((xDif >= 0 & yDif >= 0) && ((yDif <= maxSpaces) && (xDif <= maxSpaces))) {
            return endPiece.getColour() != piece.getColour();
        } else {
            return false;
        }
        //Going to need to add Check clause since kings cant move into Check

        //No piece can be moved that will either expose the king of the same colour to check or leave that king in check.***
    }

    public Stack<Location> getPossibleMoves(Piece piece) {
        Stack<Location> possible = new Stack<Location>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Location temp = new Location(i, j);
                if ((board.getCurrentState().getPiece(temp).getName() == Piece.Name.EMPTY || board.getCurrentState().getPiece(temp).getColour() != piece.getColour()) && validateMove(piece, temp, this.board) && canMove(piece, temp)) {
                    possible.push(temp);
                }
            }
        }
        return possible;
    }

    public void highlightCheck() {
        //Checks if either king is in check and highlights its location if true//
        Piece tempKing = board.getCurrentState().getPiece(getKingLocation(Colour.BLACK, this.board));
        if (isInCheck(tempKing, this.board))
            tempKing.setInCheck(true);
        tempKing = board.getCurrentState().getPiece(getKingLocation(Colour.WHITE, this.board));
        if (isInCheck(tempKing, this.board))
            tempKing.setInCheck(true);
    }

    private boolean isInCheck(Piece piece, Board board) {
        boolean result = false;
        Location kingLocation = getKingLocation(piece.getColour(), board);
        for (int i = 0; i < board.getCurrentState().getState().length; i++) {
            for (int j = 0; j < board.getCurrentState().getState().length; j++) {
                //Graham changed this because getComputerPlayer doens't exist anymore, could be wrong
//                if (!(board.getCurrentState().getPiece(i, j).getName() == Piece.Name.EMPTY) && board.getCurrentState().getPiece(i, j).getColour() == board.getComputerPlayer().getColour() && validateMove(board.getCurrentState().getPiece(i, j), kingLocation,board)) {
                if (!(board.getCurrentState().getPiece(i, j).getName() == Piece.Name.EMPTY) && board.getCurrentState().getPiece(i, j).getColour() != piece.getColour() && validateMove(board.getCurrentState().getPiece(i, j), kingLocation, board)) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }


    private Location getKingLocation(Colour colour, Board board) {

        for (int i = 0; i < board.getCurrentState().getState().length; i++) {
            for (int j = 0; j < board.getCurrentState().getState().length; j++) {
                if (!(board.getCurrentState().getPiece(i, j).getName() == Piece.Name.EMPTY) && board.getCurrentState().getPiece(i, j).getColour() == colour && board.getCurrentState().getPiece(i, j).getName() == Piece.Name.KING) {
                    return board.getCurrentState().getPiece(i, j).getLocation();
                }
            }
        }
        return null; //No King...Should never happen
    }

    private boolean canMove(Piece piece, Location toLocation) {
        Board tempBoard = board.clone();
        Piece tempPiece = piece.clone();
        Location tempLocation, tempToLocation;
        tempLocation = new Location(tempPiece.getLocation().getX(), tempPiece.getLocation().getY());
        tempToLocation = new Location(toLocation.getX(), toLocation.getY());
        tempBoard.move(tempLocation, tempToLocation);
        tempPiece = tempBoard.getCurrentState().getPiece(tempToLocation);
        return !isInCheck(tempPiece, tempBoard);
    }
}