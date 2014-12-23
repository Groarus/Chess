import java.util.Stack;

/**
 * Project: Chess
 * Course: COSC 3P71 - Final Project
 * Created: December, 2014
 */
public class MoveEngine {

//    private State state;

    public MoveEngine(State state) {
//        this.state = state;
    }


    public boolean validateMove(Piece piece, Location toLocation, State state) {
        switch (piece.getName()) {
            case PAWN:
                return pawnCheck(piece, toLocation, state);
            case KNIGHT:
                return knightCheck(piece, toLocation, state);
            case BISHOP:
                return bishopCheck(piece, toLocation, state);
            case ROOK:
                return rookCheck(piece, toLocation, state);
            case QUEEN:
                return rookCheck(piece, toLocation, state) || bishopCheck(piece, toLocation, state);
            case KING:
                return kingCheck(piece, toLocation, state);
        }

        return true;//temporarily, so other pieces can still be moved
    }


    public boolean move(Piece piece, Location toLocation, State state) {
        if (validateMove(piece, toLocation, state) && canMove(piece, toLocation, state)) {
            boolean overtaken = false;
            if (state.getPiece(toLocation).getColour() != Colour.NEUTRAL && state.getPiece(toLocation).getColour() != piece.getColour())
                overtaken = true;

            //increment number of moves and decrement pieces left if overtaken == true
            if (piece.getColour() == Colour.WHITE) {
                state.getWhitePlayer().incrementMoves();
                if (overtaken)
                    state.getBlackPlayer().decPiecesLeft();
            } else {
                state.getBlackPlayer().incrementMoves();
                if (overtaken)
                    state.getWhitePlayer().decPiecesLeft();
            }
            state.movePiece(piece.getLocation(), toLocation);
            highlightCheck(state);
            return true;
        }
        return false;
    }

    private boolean pawnCheck(Piece piece, Location toLocation, State state) {
        Piece endPiece = state.getPiece(toLocation.getX(), toLocation.getY());
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

    private boolean knightCheck(Piece piece, Location toLocation, State state) {
        Piece endPiece = state.getPiece(toLocation.getX(), toLocation.getY());
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

    private boolean bishopCheck(Piece piece, Location toLocation, State state) {
        Piece endPiece = state.getPiece(toLocation.getX(), toLocation.getY());
        int xDif = Math.abs(piece.getLocation().getX() - toLocation.getX());
        int yDif = Math.abs(piece.getLocation().getY() - toLocation.getY());
        int maxSpaces = 1;
        try {
            if (piece.getLocation().getY() < toLocation.getY()) { //Moving up
                if (piece.getLocation().getX() > toLocation.getX()) {            //Moving left
                    for (int i = 1; i < xDif; i++) {
                        if (state.getPiece(piece.getLocation().getX() - i, piece.getLocation().getY() + i).getName() == Piece.Name.EMPTY) {
                            maxSpaces++; //No piece in the way
                        }
                    }
                } else if (piece.getLocation().getX() < toLocation.getX()) {            //Moving right
                    for (int i = 1; i < xDif; i++) {
                        if (state.getPiece(piece.getLocation().getX() + i, piece.getLocation().getY() + i).getName() == Piece.Name.EMPTY) {
                            maxSpaces++;//No piece in the way
                        }
                    }
                }
            } else {//Moving down
                if (piece.getLocation().getX() > toLocation.getX()) {            //Moving left
                    for (int i = 1; i < xDif; i++) {
                        if (state.getPiece(piece.getLocation().getX() - i, piece.getLocation().getY() - i).getName() == Piece.Name.EMPTY) {
                            maxSpaces++;//No piece in the way
                        }
                    }
                } else if (piece.getLocation().getX() < toLocation.getX()) {            //Moving right
                    for (int i = 1; i < xDif; i++) {
                        if (state.getPiece(piece.getLocation().getX() + i, piece.getLocation().getY() - i).getName() == Piece.Name.EMPTY) {
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

    private boolean rookCheck(Piece piece, Location toLocation, State state) {
        Piece endPiece = state.getPiece(toLocation.getX(), toLocation.getY());
        int xDif = Math.abs(piece.getLocation().getX() - toLocation.getX());
        int yDif = Math.abs(piece.getLocation().getY() - toLocation.getY());
        int maxSpaces = 1;

        //Four ifs to check if there is nothing in between where it needs to go
        if (piece.getLocation().getY() < toLocation.getY()) { //Moving up
            for (int i = 1; i < yDif; i++) {
                if (state.getPiece(piece.getLocation().getX(), piece.getLocation().getY() + i).getName() == Piece.Name.EMPTY) {
                    maxSpaces++; //No piece in the way
                }
            }
        } else if (piece.getLocation().getY() > toLocation.getY()) {//Moving down
            for (int i = 1; i < yDif; i++) {
                if (state.getPiece(piece.getLocation().getX(), piece.getLocation().getY() - i).getName() == Piece.Name.EMPTY) {
                    maxSpaces++;//No piece in the way
                }
            }
        } else if (piece.getLocation().getX() > toLocation.getX()) {//Moving left
            for (int i = 1; i < xDif; i++) {
                if (state.getPiece(piece.getLocation().getX() - i, piece.getLocation().getY()).getName() == Piece.Name.EMPTY) {
                    maxSpaces++;//No piece in the way
                }
            }
        } else if (piece.getLocation().getX() < toLocation.getX()) {//Moving right
            for (int i = 1; i < xDif; i++) {
                if (state.getPiece(piece.getLocation().getX() + i, piece.getLocation().getY()).getName() == Piece.Name.EMPTY) {
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

    private boolean kingCheck(Piece piece, Location toLocation, State state) {
        Piece endPiece = state.getPiece(toLocation.getX(), toLocation.getY());
        int xDif = Math.abs(piece.getLocation().getX() - toLocation.getX());
        int yDif = Math.abs(piece.getLocation().getY() - toLocation.getY());
        int maxSpaces = 1;

        if ((xDif >= 0 & yDif >= 0) && ((yDif <= maxSpaces) && (xDif <= maxSpaces))) {
            return endPiece.getColour() != piece.getColour();
        } else {
            return false;
        }
    }

    public Stack<Location> getPossibleMoves(Piece piece, State state) {
        Stack<Location> possible = new Stack<Location>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Location temp = new Location(i, j);
                if ((state.getPiece(temp).getName() == Piece.Name.EMPTY || state.getPiece(temp).getColour() != piece.getColour()) && validateMove(piece, temp, state) && canMove(piece, temp, state)) {
                    possible.push(temp);
                }
            }
        }
        return possible;
    }

    public void highlightCheck(State state) {
        //Checks if either king is in check and highlights its location if true//
        Piece tempKing = state.getPiece(getKingLocation(Colour.BLACK, state));
        if (isInCheck(tempKing, state))
            tempKing.setInCheck(true);
        tempKing = state.getPiece(getKingLocation(Colour.WHITE, state));
        if (isInCheck(tempKing, state))
            tempKing.setInCheck(true);
    }

    private boolean isInCheck(Piece piece, State state) {
        Location kingLocation = getKingLocation(piece.getColour(), state);
        for (int i = 0; i < state.getState().length; i++) {
            for (int j = 0; j < state.getState().length; j++) {
                if (!(state.getPiece(i, j).getName() == Piece.Name.EMPTY) && state.getPiece(i, j).getColour() != piece.getColour() && validateMove(state.getPiece(i, j), kingLocation, state)) {
                    return true;
                }
            }
        }
        return false;
    }


    private Location getKingLocation(Colour colour, State state) {
        for (int i = 0; i < state.getState().length; i++) {
            for (int j = 0; j < state.getState().length; j++) {
                if (!(state.getPiece(i, j).getName() == Piece.Name.EMPTY) && state.getPiece(i, j).getColour() == colour && state.getPiece(i, j).getName() == Piece.Name.KING) {
                    return state.getPiece(i, j).getLocation();
                }
            }
        }
        return null; //No King...Should never happen
    }

    private boolean canMove(Piece piece, Location toLocation, State state) {
        State tempstate = state.clone();
        Piece tempPiece = piece.clone();
        Location tempLocation, tempToLocation;
        tempLocation = new Location(tempPiece.getLocation().getX(), tempPiece.getLocation().getY());
        tempToLocation = new Location(toLocation.getX(), toLocation.getY());
        tempstate.movePiece(tempLocation, tempToLocation);
        tempPiece = tempstate.getPiece(tempToLocation);
        return !isInCheck(tempPiece, tempstate);
    }
}