import java.util.LinkedList;
import java.util.Random;
import java.util.Stack;

/**
 * Project: Chess
 * Course: COSC 3P71 - Final Project
 * Created: December, 2014
 */
public class MoveEngine {

    private MoveHistory moveHistory;
    private specialMove special;

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
        special = specialMove.NONE;
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

            if (state.getPiece(toLocation).getColour() != Colour.NEUTRAL && state.getPiece(toLocation).getColour() != piece.getColour())
                overtaken = true;

            //increment number of moves and decrement pieces left if overtaken == true
            if (piece.getColour() == Colour.WHITE) {
                state.getWhitePlayer().incrementMoves();
                if (overtaken) {
                    state.getBlackPlayer().decPiecesLeft();
                }
            } else {
                state.getBlackPlayer().incrementMoves();
                if (overtaken)
                    state.getWhitePlayer().decPiecesLeft();
            }
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
                        special = specialMove.PROMOTE;
                    return true;
                }
            } else if (endPiece.getName() != Piece.Name.EMPTY && endPiece.getColour() != piece.getColour() && xDif == 1 && yDif == 1) { //Pawn Capture
                //Piece Promotion
                if (toLocation.getY() == 0 || toLocation.getY() == 7)
                    special = specialMove.PROMOTE;
                return true;
            } else if (endPiece.getColour() != piece.getColour() && xDif == 1 && yDif == 1) { //En Passant
                Piece lastPiece = state.getPiece(moveHistory.lastMove());
                if (piece.getPrevLocation() != null && lastPiece.getPrevLocation() != null)
                    if (Math.abs(lastPiece.getPrevLocation().getY() - lastPiece.getLocation().getY()) == 2 && lastPiece.getName() == Piece.Name.PAWN) // if the last move was a 2 jump and was a pawn
                        if (toLocation.getX() == lastPiece.getLocation().getX() && (Math.abs(toLocation.getY() - lastPiece.getLocation().getY()) == 1 && (Math.abs(toLocation.getY() - lastPiece.getPrevLocation().getY()) == 1))) {
                            special = specialMove.ENPASSANT;
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
        } catch (ArrayIndexOutOfBoundsException e) {
            //Do nothing
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
                special = specialMove.CASTLE1;
                return true;
            } else if (state.getPiece(toLocation.getX() - 1, toLocation.getY()).getName() == Piece.Name.EMPTY && state.getPiece(toLocation.getX() + 1, toLocation.getY()).getName() == Piece.Name.EMPTY && state.getPiece(toLocation.getX() - 2, toLocation.getY()).getName() == Piece.Name.ROOK) {
                special = specialMove.CASTLE2;
                return true;
            }
            return false;
        } else {
            return false;
        }
    }

    public LinkedList<GNode> getPossibleStates(Piece piece, State state) {
        LinkedList<GNode> possible = new LinkedList<GNode>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Location toLocation = new Location(i, j);
                special = specialMove.NONE;
                if ((state.getPiece(toLocation).getColour() != piece.getColour()) && validateMove(piece, toLocation, state) && canMove(piece, toLocation, state)) {
                    State goodState = state.clone();

                    switch (special) {
                        case ENPASSANT:
                            state.setPiece(toLocation.getX(), toLocation.getY() + 1, new Empty());
                            state.setPiece(toLocation.getX(), toLocation.getY() - 1, new Empty());

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
                    goodState.movePiece(piece.getLocation(), toLocation);
                    possible.add(new GNode(goodState));
                }
            }
        }
        return possible;
    }

//    public LinkedList<GNode> getPossibleChildren(Piece piece, State state) {
//        LinkedList<GNode> childNodes = new LinkedList<GNode>();
//
//        for (int i = 0; i < 8; i++) {
//            for (int j = 0; j < 8; j++) {
//                Location toLocation = new Location(i, j);
//                special = specialMove.NONE;
//                if ((state.getPiece(toLocation).getColour() != piece.getColour()) && validateMove(piece, toLocation, state) && canMove(piece, toLocation, state)) {
//                    Piece passant1 = null, passant2 = null;
//                    TempMove castle1 = null, castle2 = null;
//                    switch (special) {
//                        case ENPASSANT:
//                            passant1 = state.getPiece(toLocation.getX(), toLocation.getY() + 1);
//                            passant2 = state.getPiece(toLocation.getX(), toLocation.getY() - 1);
//
//                            state.getPieces(passant1.getColour()).removePiece(passant1);
//                            state.getPieces(passant2.getColour()).removePiece(passant2);
//
//                            state.setPiece(toLocation.getX(), toLocation.getY() + 1, new Empty());
//                            state.setPiece(toLocation.getX(), toLocation.getY() - 1, new Empty());
//                            break;
//                        case CASTLE1:
//                            System.out.println("CASTLE 1");
//                            castle1 = new TempMove(state.getPiece((toLocation.getX() + 1), toLocation.getY()), state);
//                            castle1.move(new Location(toLocation.getX() - 1, toLocation.getY()));
//                            break;
//                        case CASTLE2:
//                            System.out.println("CASTLE 2");
//                            castle2 = new TempMove(state.getPiece((toLocation.getX() - 2), toLocation.getY()), state);
//                            castle2.move(new Location(toLocation.getX() + 1, toLocation.getY()));
//                            break;
//                        case PROMOTE:
//                            state.getPieces(piece.getColour()).removePiece(state.getPiece(piece.getLocation()));
//                            state.setPiece(piece.getLocation().getX(), piece.getLocation().getY(), new Queen(piece.getColour()));
//                            state.getPieces(piece.getColour()).addQueen(state.getPiece(piece.getLocation()));
//                            break;
//                    }
//                    //Move the piece 
//                    TempMove mainMove = new TempMove(piece, state);
//                    mainMove.move(toLocation);
//
//                    //Evaluate the state 
//                    double evaluation = evaluateState(state, piece.getColour());
//                    childNodes.add(new GNode(piece, toLocation, evaluation, mainMove));
//
//                    //Undo move and special cases
//                    mainMove.undoMove();
//                    switch (special) {
//                        case ENPASSANT:
//                            state.setPiece(toLocation.getX(), toLocation.getY() + 1, passant1);
//                            state.setPiece(toLocation.getX(), toLocation.getY() - 1, passant2);
//                            break;
//                        case CASTLE1:
//                            if (castle1 != null) {
//                                castle1.undoMove();
//                            }
//                            break;
//                        case CASTLE2:
//                            if (castle2 != null) {
//                                castle2.undoMove();
//                            }
//                            break;
//                        case PROMOTE:
//                            state.getPieces(piece.getColour()).removePiece(state.getPiece(piece.getLocation()));
//                            state.setPiece(piece.getLocation().getX(), piece.getLocation().getY(), new Pawn(piece.getColour()));
//                            state.getPieces(piece.getColour()).addPawn(state.getPiece(piece.getLocation()));
//                            break;
//                    }
//                }
//            }
//        }
//        return childNodes;
//    }


    public Stack<Location> getPossibleMoves(Piece piece, State state) {
        Stack<Location> possible = new Stack<Location>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Location temp = new Location(i, j);
                if ((state.getPiece(temp).getColour() != piece.getColour()) && validateMove(piece, temp, state) && canMove(piece, temp, state)) {
                    possible.push(temp);
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

    private boolean isInCheck(Colour colour, State state) {
        try {
            Location kingLocation = colour == Colour.WHITE ? state.getWhitePieces().getKing().peek().getLocation() : state.getBlackPieces().getKing().peek().getLocation();

            StatePieces oppositePieces = colour == Colour.WHITE ? state.getBlackPieces() : state.getWhitePieces();

            for (Piece piece : oppositePieces.getAll()) {
                if (validateMove(piece, kingLocation, state)) {
                    return true;
                }
            }
        } catch (NullPointerException e) {
            System.out.println("isincheck no king");
        }
        return false;
    }

    public double evaluateState(State state, Colour colour) {
//        double evaluation = 0;
//
//        StatePieces colour1 = colour == Colour.WHITE ? state1.getWhitePieces() : state1.getBlackPieces();
//        StatePieces opposite1 = colour == Colour.WHITE ? state1.getBlackPieces() : state1.getWhitePieces();
//        StatePieces colour2 = colour == Colour.WHITE ? state2.getWhitePieces() : state2.getBlackPieces();
//        StatePieces opposite2 = colour == Colour.WHITE ? state2.getBlackPieces() : state2.getWhitePieces();
//
////The difference in what we had plus the difference in what we have!
//
//        //                  state2  colour                  state2 opposite                     start1 colour               state1 opposite
//        evaluation += 200 * ((colour2.getKing().size() - opposite2.getKing().size()) - (colour1.getKing().size() - opposite1.getKing().size()));
//        evaluation += 9 * ((colour2.getQueens().size() - opposite2.getQueens().size()) - (colour1.getQueens().size() - opposite1.getQueens().size()));
//        evaluation += 5 * ((colour2.getRooks().size() - opposite2.getRooks().size()) - (colour1.getRooks().size() - opposite1.getRooks().size()));
//        evaluation += 3 * ((colour2.getBishops().size() - opposite2.getBishops().size()) - (colour1.getBishops().size() - opposite1.getBishops().size()));
//        evaluation += 3 * ((colour2.getKnights().size() - opposite2.getKnights().size()) - (colour1.getKnights().size() - opposite1.getKnights().size()));
//        evaluation += (colour2.getPawns().size() - opposite2.getPawns().size()) - (colour1.getPawns().size() - opposite1.getPawns().size());
////        double whiteMobility = 0;
//        for (Piece piece : whitePieces.getAll())
//            whiteMobility += getPossibleMoves(piece, state2).size();
//        whiteMaterial += 0.1 * whiteMobility;
//
//        double blackMobility = 0;
//        for (Piece piece : blackPieces.getAll())
//            blackMobility += getPossibleMoves(piece, state2).size();
//        blackMaterial += 0.1 * blackMobility;

//        return evaluation;
        double evaluation;
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

        evaluation = blackMaterial - whiteMaterial;
      //  evaluation = colour == Colour.BLACK ? (evaluation * 1) : (evaluation * -1);
        return (evaluation);
    }

    private boolean canMove(Piece piece, Location toLocation, State state) {
        Boolean ret;

        Location startLocation = piece.getLocation();
        Location prevLocation = piece.getPrevLocation();
        Piece tempPiece = state.getPiece(toLocation);
        Location lastMoveStart = state.getLastMoveStart();
        Location lastMoveEnd = state.getLastMoveEnd();

        state.movePiece(startLocation, toLocation);

        ret = !isInCheck(piece.getColour(), state);

        if (tempPiece.getName() != Piece.Name.EMPTY && tempPiece.getColour() == Colour.WHITE)
            state.getWhitePieces().addPiece(tempPiece);
        else if (tempPiece.getName() != Piece.Name.EMPTY && tempPiece.getColour() == Colour.BLACK)
            state.getBlackPieces().addPiece(tempPiece);

        state.movePiece(toLocation, startLocation);
        state.setPiece(toLocation.getX(), toLocation.getY(), tempPiece);
        state.setLastMoveStart(lastMoveStart);
        state.setLastMoveEnd(lastMoveEnd);
        piece.setPrevLocation(prevLocation);
        return ret;
    }

    private enum specialMove {
        ENPASSANT, CASTLE1, CASTLE2, PROMOTE, NONE
    }
}