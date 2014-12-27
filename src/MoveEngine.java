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
                    break;
                case CASTLE1:
                    state.movePiece(new Location(toLocation.getX() + 1, toLocation.getY()), new Location(toLocation.getX() - 1, toLocation.getY()));
                    break;
                case CASTLE2:
                    state.movePiece(new Location(toLocation.getX() - 2, toLocation.getY()), new Location(toLocation.getX() + 1, toLocation.getY()));
                    break;
                case PROMOTE:
                    state.setPiece(piece.getLocation().getX(), piece.getLocation().getY(), ((Pawn) piece).convert());
                    break;
            }

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
                if ((piece.getColour() == Colour.BLACK && state.getPiece(toLocation.getX(), 5).getName() == Piece.Name.EMPTY) || (piece.getColour() == Colour.WHITE && state.getPiece(toLocation.getX(), 2).getName() == Piece.Name.EMPTY)) {
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

    public Stack<State> getPossibleStates(Piece piece, State state) {
        Stack<State> possible = new Stack<State>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Location temp = new Location(i, j);
                special = specialMove.NONE;
                if ((state.getPiece(temp).getColour() != piece.getColour()) && validateMove(piece, temp, state) && canMove(piece, temp, state)) {
                    State goodState = state.clone();

                    switch (special) {
                        case ENPASSANT:
                            goodState.setPiece(temp.getX(), temp.getY() + 1, new Empty());
                            goodState.setPiece(temp.getX(), temp.getY() - 1, new Empty());
                            break;
                        case CASTLE1:
                            goodState.movePiece(new Location(temp.getX() + 1, temp.getY()), new Location(temp.getX() - 1, temp.getY()));
                            break;
                        case CASTLE2:
                            goodState.movePiece(new Location(temp.getX() - 2, temp.getY()), new Location(temp.getX() + 1, temp.getY()));
                            break;
                        case PROMOTE:
                            goodState.setPiece(piece.getLocation().getX(), piece.getLocation().getY(), ((Pawn) piece).convert());
                            break;
                    }
                    goodState.movePiece(piece.getLocation(), temp);
                    possible.push(goodState);
                }
            }
        }
        return possible;
    }


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


    public int[][] numPieces(State state) {
        int numPieces[][] = new int[2][6];
      /*     [0][x] = white 
      [1][x] = black 
      [x][0] = pawn 
      [x][1] = knight 
      [x][2] = bishop 
      [x][3] = rook 
      [x][4] = queen 
      [x][5] = king 
      */
        for (int i = 0; i < state.getState().length; i++) {
            for (int j = 0; j < state.getState().length; j++) {
                Piece temp = state.getPiece(i, j);
                int colour = temp.getColour() == Colour.WHITE ? 0 : 1;
                switch (temp.getName()) {
                    case PAWN:
                        numPieces[colour][0]++;
                        break;
                    case ROOK:
                        numPieces[colour][1]++;
                        break;
                    case KNIGHT:
                        numPieces[colour][2]++;
                        break;
                    case BISHOP:
                        numPieces[colour][3]++;
                        break;
                    case QUEEN:
                        numPieces[colour][4]++;
                        break;
                    case KING:
                        numPieces[colour][5]++;
                        break;
                }
            }
        }
        return numPieces;
    }

    private int numTotalMoves(State state, Colour colour) {
        //returns the number of total possible moves for that state of the board
        int total = 0;
        for (int i = 0; i < state.getState().length; i++) {
            for (int j = 0; j < state.getState().length; j++) {
                if (state.getPiece(i, j).getColour() == colour)
                    total += getPossibleMoves(state.getPiece(i, j), state).size();
            }
        }
        return total;
    }

    public double evaluateState(State state1, State state2, Colour colour) {
        double evaluation = 0;
        int colourInt = colour == Colour.WHITE ? 0 : 1;
        int oppositeColourInt = colour == Colour.WHITE ? 1 : 0;

        int numPieces1[][] = numPieces(state1);
        int numPieces2[][] = numPieces(state2);


//The difference in what we had plus the difference in what we have!
        evaluation += 200 * ((numPieces2[colourInt][5] - numPieces2[oppositeColourInt][5]) - (numPieces1[colourInt][5] - numPieces1[oppositeColourInt][5]));
        evaluation += 9 * ((numPieces2[colourInt][4] - numPieces2[oppositeColourInt][4]) - (numPieces1[colourInt][4] - numPieces1[oppositeColourInt][4]));
        evaluation += 5 * ((numPieces2[colourInt][3] - numPieces2[oppositeColourInt][3]) - (numPieces1[colourInt][3] - numPieces1[oppositeColourInt][3]));
        evaluation += 3 * ((numPieces2[colourInt][2] - numPieces2[oppositeColourInt][2]) - (numPieces1[colourInt][2] - numPieces1[oppositeColourInt][2]));
        evaluation += 3 * ((numPieces2[colourInt][1] - numPieces2[oppositeColourInt][1]) - (numPieces1[colourInt][1] - numPieces1[oppositeColourInt][1]));
        evaluation += (numPieces2[colourInt][0] - numPieces2[oppositeColourInt][0]) - (numPieces1[colourInt][0] - numPieces1[oppositeColourInt][0]);

        if (evaluation == 0) {
            evaluation = randomDouble(0, 1);
        }

/*
f(p) = 200(K-K')
+ 9(Q-Q')
+ 5(R-R')
+ 3(B-B' + N-N')
+ 1(P-P')
- 0.5(D-D' + S-S' + I-I')
+ 0.1(M-M') + ...

KQRBNP = number of kings, queens, rooks, bishops, knights and pawns
D,S,I = doubled, blocked and isolated pawns
M = Mobility (the number of legal moves)

https://chessprogramming.wikispaces.com/Evaluation
*/

        return Math.abs(evaluation);
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

    private double randomDouble(int min, double max) {
        Random r = new Random();
        return (min + (max - min) * r.nextDouble());

    }

    private enum specialMove {
        ENPASSANT, CASTLE1, CASTLE2, PROMOTE, NONE
    }
}