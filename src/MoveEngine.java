import javafx.util.Pair;

import java.util.LinkedList;

/**
 * Project: Chess
 * Course: COSC 3P71 - Final Project
 * Created: December, 2014
 */
public class MoveEngine {

    private MoveHistory moveHistory;
    private SpecialMove special;

    public MoveEngine(MoveHistory moveHistory) {
        this.moveHistory = moveHistory;
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
        special = SpecialMove.NONE;
        if (validateMove(piece, toLocation, state) && canMove(piece, toLocation, state)) {
            boolean overtaken = false;
            switch (special) {
                case ENPASSANT:
                    state.setPiece(toLocation.getX(), toLocation.getY() + 1, new Empty());
                    state.setPiece(toLocation.getX(), toLocation.getY() - 1, new Empty());
                    overtaken = true;

                    Piece temp = state.getPiece(toLocation.getX(), toLocation.getY() + 1);
                    state.getPieces(temp.getColour()).removePiece(temp);
                    temp = state.getPiece(toLocation.getX(), toLocation.getY() - 1);
                    state.getPieces(temp.getColour()).removePiece(temp);
                    break;
                case CASTLE1:
                    state.movePiece(new Location(toLocation.getX() + 1, toLocation.getY()), new Location(toLocation.getX() - 1, toLocation.getY()));
                    break;
                case CASTLE2:
                    state.movePiece(new Location(toLocation.getX() - 2, toLocation.getY()), new Location(toLocation.getX() + 1, toLocation.getY()));
                    break;
                case PROMOTE:
                    state.getPieces(piece.getColour()).removePiece(piece);
                    if ((state.getWhitePlayer().getColour() == piece.getColour() && state.getWhitePlayer() instanceof ComputerPlayer) || (state.getBlackPlayer().getColour() == piece.getColour() && state.getBlackPlayer() instanceof ComputerPlayer)) {
                        state.setPiece(piece.getLocation().getX(), piece.getLocation().getY(), new Queen(piece.getColour()));
                    } else {
                        state.setPiece(piece.getLocation().getX(), piece.getLocation().getY(), ((Pawn) piece).convert());
                    }
                    state.getPieces(piece.getColour()).addPiece(state.getPiece(piece.getLocation()));
                    break;

            }


            //increment number of moves and decrement pieces left if overtaken == true
            if (piece.getColour() == Colour.WHITE)
                state.getWhitePlayer().incrementMoves();
            else
                state.getBlackPlayer().incrementMoves();
            state.movePiece(piece.getLocation(), toLocation);
            return true;
        }
        return false;
    }


    private boolean pawnCheck(Piece piece, Location toLocation, State state) {
        Piece endPiece = state.getPiece(toLocation.getX(), toLocation.getY());
        int xDif = Math.abs(piece.getLocation().getX() - toLocation.getX());
        int yDif = Math.abs(piece.getLocation().getY() - toLocation.getY());

        int maxSpaces = piece.getPrevLocation() == null ? 2 : 1;
         /*
            If the pawn is moving up (or down for computer) and is only moving the right number of spaces and the space occupied is empty
            Allow it to move.
            If it is moving diagonally and the space has an enemy piece, allow it to be taken
         */
        if (yDif <= maxSpaces & ((piece.getColour() == Colour.BLACK & piece.getLocation().getY() > toLocation.getY()) | (piece.getColour() == Colour.WHITE & piece.getLocation().getY() < toLocation.getY()))) {
            if (piece.getLocation().getX() == toLocation.getX() & endPiece.getName() == Piece.Name.EMPTY) {
                if ((piece.getColour() == Colour.BLACK && state.getPiece(toLocation.getX(), piece.getLocation().getY() - 1).getName() == Piece.Name.EMPTY) || (piece.getColour() == Colour.WHITE && state.getPiece(toLocation.getX(), piece.getLocation().getY() + 1).getName() == Piece.Name.EMPTY)) {
                    //Piece Promotion
                    if (toLocation.getY() == 0 || toLocation.getY() == 7)
                        special = SpecialMove.PROMOTE;
                    return true;
                }
            } else if (endPiece.getName() != Piece.Name.EMPTY && endPiece.getColour() != piece.getColour() && xDif == 1 && yDif == 1) { //Pawn Capture
                //Piece Promotion
                if (toLocation.getY() == 0 || toLocation.getY() == 7)
                    special = SpecialMove.PROMOTE;
                return true;
            } else if (endPiece.getColour() != piece.getColour() && xDif == 1 && yDif == 1) { //En Passant
                Piece lastPiece = state.getPiece(moveHistory.lastMove());
                if (piece.getPrevLocation() != null && lastPiece.getPrevLocation() != null)
                    if (Math.abs(lastPiece.getPrevLocation().getY() - lastPiece.getLocation().getY()) == 2 && lastPiece.getName() == Piece.Name.PAWN) // if the last move was a 2 jump and was a pawn
                        if (toLocation.getX() == lastPiece.getLocation().getX() && (Math.abs(toLocation.getY() - lastPiece.getLocation().getY()) == 1 && (Math.abs(toLocation.getY() - lastPiece.getPrevLocation().getY()) == 1))) {
                            special = SpecialMove.ENPASSANT;
                            return true;
                        }
            }
            return false;
        }
        return false;
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

        return xDif <= maxSpaces & yDif <= maxSpaces & xDif > 0 & yDif > 0 & (yDif + xDif == maxSpaces) && endPiece.getColour() != piece.getColour();
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
        } catch (ArrayIndexOutOfBoundsException ignore) {
        }

        //Moving diagonally with no one in between
        //Empty or enemy
        return xDif == yDif && xDif == maxSpaces && endPiece.getColour() != piece.getColour();
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

        return ((xDif == 0 & yDif > 0) || (yDif == 0 & xDif > 0)) && ((yDif == maxSpaces) || (xDif == maxSpaces)) && endPiece.getColour() != piece.getColour();

    }


    private boolean kingCheck(Piece piece, Location toLocation, State state) {
        Piece endPiece = state.getPiece(toLocation.getX(), toLocation.getY());
        int xDif = Math.abs(piece.getLocation().getX() - toLocation.getX());
        int yDif = Math.abs(piece.getLocation().getY() - toLocation.getY());
        int maxSpaces = 1;

        if ((xDif >= 0 & yDif >= 0) && ((yDif <= maxSpaces) && (xDif <= maxSpaces))) {
            return endPiece.getColour() != piece.getColour();
        } else if (piece.getPrevLocation() == null && yDif == 0 && xDif == 2) {
            if (state.getPiece(toLocation.getX() - 1, toLocation.getY()).getName() == Piece.Name.EMPTY && state.getPiece(toLocation.getX() + 1, toLocation.getY()).getName() == Piece.Name.ROOK) {
                special = SpecialMove.CASTLE1;
                return true;
            } else if (state.getPiece(toLocation.getX() - 1, toLocation.getY()).getName() == Piece.Name.EMPTY && state.getPiece(toLocation.getX() + 1, toLocation.getY()).getName() == Piece.Name.EMPTY && state.getPiece(toLocation.getX() - 2, toLocation.getY()).getName() == Piece.Name.ROOK) {
                special = SpecialMove.CASTLE2;
                return true;
            }
            return false;
        } else {
            return false;
        }
    }


    public LinkedList<Pair<Location, SpecialMove>> getPossibleMoves(Piece piece, State state) {
        LinkedList<Pair<Location, SpecialMove>> possible = new LinkedList<Pair<Location, SpecialMove>>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Location temp = new Location(i, j);
                special = SpecialMove.NONE;
                if ((state.getPiece(temp).getColour() != piece.getColour()) && validateMove(piece, temp, state) && canMove(piece, temp, state)) {
                    possible.add(new Pair<Location, SpecialMove>(temp, special));
                }
            }
        }
        return possible;
    }

    public void highlightCheck(State state) {
        //Checks if either king is in check and highlights its location if true//
        Piece tempKing = state.getWhitePieces().getKing().peek();
        if (isInCheck(Colour.WHITE, state))
            tempKing.setInCheck(true);
        tempKing = state.getBlackPieces().getKing().peek();
        if (isInCheck(Colour.BLACK, state))
            tempKing.setInCheck(true);
    }

    public boolean isInCheck(Colour colour, State state) {
        try {
            Location kingLocation = state.getPieces(colour).getKing().peek().getLocation();

            StatePieces oppositePieces = colour == Colour.WHITE ? state.getBlackPieces() : state.getWhitePieces();

            for (Piece piece : oppositePieces.getAll()) {
                if (validateMove(piece, kingLocation, state)) {
                    return true;
                }
            }
        } catch (NullPointerException ignored) {
        }
        return false;
    }

    public double evaluateState(State state, Colour colour) {
        double whiteMaterial = 0;
        double blackMaterial = 0;

        StatePieces whitePieces = state.getWhitePieces();
        StatePieces blackPieces = state.getBlackPieces();

        whiteMaterial += 200 * (whitePieces.getKing().size());
        whiteMaterial += 9 * (whitePieces.getQueens().size());
        whiteMaterial += 5 * (whitePieces.getRooks().size());
        whiteMaterial += 3 * (whitePieces.getBishops().size());
        whiteMaterial += 3 * (whitePieces.getKnights().size());
        whiteMaterial += (whitePieces.getPawns().size());
        double whiteMobility = 0;
        for (Piece piece : whitePieces.getAll())
            whiteMobility += getPossibleMoves(piece, state).size();
        whiteMaterial += 0.1 * whiteMobility;

        blackMaterial += 200 * (blackPieces.getKing().size());
        blackMaterial += 9 * (blackPieces.getQueens().size());
        blackMaterial += 5 * (blackPieces.getRooks().size());
        blackMaterial += 3 * (blackPieces.getBishops().size());
        blackMaterial += 3 * (blackPieces.getKnights().size());
        blackMaterial += (blackPieces.getPawns().size());
        double blackMobility = 0;
        for (Piece piece : blackPieces.getAll())
            blackMobility += getPossibleMoves(piece, state).size();
        blackMaterial += 0.1 * blackMobility;

        //special things
        if (colour == Colour.WHITE && whiteMobility == 0) {//checkmate
            return Double.POSITIVE_INFINITY;
        } else if (colour == Colour.BLACK && blackMobility == 0) { //checkmate
            return Double.POSITIVE_INFINITY;
        }

        return colour == Colour.WHITE ? whiteMaterial - blackMaterial : blackMaterial - whiteMaterial;
    }

    private boolean canMove(Piece piece, Location toLocation, State state) {
        TempMove tempMove = new TempMove(piece, state);
        tempMove.move(toLocation, SpecialMove.NONE);
        Boolean ret = !isInCheck(piece.getColour(), state);
        tempMove.undoMove();
        return ret;
    }

    public boolean isInCheckmate(State board, Colour colour) {
        boolean movable = false;
        for (Piece piece : board.getPieces(colour).getAll()) {
            if (getPossibleMoves(piece, board).size() > 0) {
                movable = true;
                break;
            }
        }
        return isInCheck(colour, board) && !movable;
    }

    public boolean isInStalemate(State board, Colour colour) {
        boolean movable = false;
        for (Piece piece : board.getPieces(colour).getAll()) {
            if (getPossibleMoves(piece, board).size() > 0) {
                movable = true;
                break;
            }
        }
        return !isInCheck(colour, board) && !movable;
    }

    public enum SpecialMove {
        ENPASSANT, CASTLE1, CASTLE2, PROMOTE, NONE
    }
}