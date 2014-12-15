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
            case BISHOP:
                return bishopCheck(piece, toLocation);
        }

        return true;//temporarily, so other pieces can still be moved
    }


    public void move(Piece piece, Location toLocation) {
        if (validateMove(piece, toLocation)) {
            board.move(piece.getLocation(), toLocation);
        }
    }

    private boolean pawnCheck(Piece piece, Location toLocation) {
        //int black = piece.getColour() == Colour.BLACK ? -1 : 1; //if black then the checks are negated, else are positive
    /*			*//*For the first move
                * Can either jump 1 or 2 spaces
				* Only if the previous location is null*//*
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
        return false;*/

        /*
         Redid pawn movement, let me know what you think!
         */

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
            if (piece.getLocation().getX() == toLocation.getX() & endPiece == null) {
                return true;
            } else return endPiece != null && endPiece.getColour() != piece.getColour() && xDif > 0; //Pawn Capture
        } else {
            return false;
        }
    }

    private boolean knightCheck(Piece piece, Location toLocation) {
        System.out.println("No. of active thread: " + Thread.activeCount());

        Piece endPiece = board.getCurrentState().getPiece(toLocation.getX(), toLocation.getY());

        /*  Can we do this? Now its short AND clean - TEAMWORK  */ //*Fist pump*
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
            return endPiece == null || endPiece.getColour() != piece.getColour();
        } else {
            return false;
        }
    }

    private boolean bishopCheck(Piece piece, Location toLocation) {
        Piece endPiece = board.getCurrentState().getPiece(toLocation.getX(), toLocation.getY());
        int xDif = Math.abs(piece.getLocation().getX() - toLocation.getX());
        int yDif = Math.abs(piece.getLocation().getY() - toLocation.getY());
        int maxSpaces = 1;

        if (piece.getLocation().getY() < toLocation.getY()) { //Moving up
            if (piece.getLocation().getX() > toLocation.getX()) {            //Moving left
                for (int i = 1; i < xDif; i++) {
                    if (board.getCurrentState().getPiece(piece.getLocation().getX() - i, piece.getLocation().getY() + i) == null) {
                        maxSpaces++; //No piece in the way
                    }
                }
            } else if (piece.getLocation().getX() < toLocation.getX()) {            //Moving right
                for (int i = 1; i < xDif; i++) {
                    if (board.getCurrentState().getPiece(piece.getLocation().getX() + i, piece.getLocation().getY() + i) == null) {
                        maxSpaces++;//No piece in the way
                    }
                }
            }
        } else {//Moving down
            if (piece.getLocation().getX() > toLocation.getX()) {            //Moving left
                for (int i = 1; i < xDif; i++) {
                    if (board.getCurrentState().getPiece(piece.getLocation().getX() - i, piece.getLocation().getY() - i) == null) {
                        maxSpaces++;//No piece in the way
                    }
                }
            } else if (piece.getLocation().getX() < toLocation.getX()) {            //Moving right
                for (int i = 1; i < xDif; i++) {
                    if (board.getCurrentState().getPiece(piece.getLocation().getX() + i, piece.getLocation().getY() - i) == null) {
                        maxSpaces++;//No piece in the way
                    }
                }
            }
        }
        if (xDif == yDif && xDif == maxSpaces) {//Moving diagonally with no one in between
            return endPiece == null || endPiece.getColour() != piece.getColour(); //Empty or enemy
        } else {
            return false;
        }
    }
}