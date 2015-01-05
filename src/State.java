/**
 * Project: Chess
 * Course: COSC 3P71 - Final Project
 * Created: December, 2014
 */


public class State {

    Player whitePlayer, blackPlayer;
    private Piece[][] state;
    private Location lastMoveStart, lastMoveEnd;
    private StatePieces whitePieces, blackPieces;

    public State() {
        this.state = new Piece[8][8];
        this.whitePieces = new StatePieces();
        this.blackPieces = new StatePieces();
    }

    public StatePieces getPieces(Colour colour) {
        return colour == Colour.WHITE ? getWhitePieces() : getBlackPieces();
    }

    public StatePieces getWhitePieces() {
        return whitePieces;
    }


    public StatePieces getBlackPieces() {
        return blackPieces;
    }


    public Location getLastMoveStart() {
        return lastMoveStart;
    }

    public void setLastMoveStart(Location lastMoveStart) {
        this.lastMoveStart = lastMoveStart;
    }

    public Location getLastMoveEnd() {
        return lastMoveEnd;
    }

    public void setLastMoveEnd(Location lastMoveEnd) {
        this.lastMoveEnd = lastMoveEnd;
    }

    public void setState(Piece[][] state) {
        this.state = state;
    }

    public void setPiece(int x, int y, Piece piece) {
        state[x][y] = piece;
        piece.setLocation(new Location(x, y));
    }

    public Piece getPiece(int x, int y) {
        return state[x][y];
    }

    public Piece getPiece(Location location) {
        return state[location.getX()][location.getY()];
    }

    public void movePiece(Location startLocation, Location endLocation) {
        try {
            //Overtaking a piece
            if (getPiece(endLocation).getColour() != Colour.NEUTRAL) {
                getPieces(getPiece(endLocation).getColour()).removePiece(getPiece(endLocation));
                if (this instanceof Board) {
                    getWhitePlayer().setPiecesLeft(getWhitePieces().getAll().size());
                    getBlackPlayer().setPiecesLeft(getBlackPieces().getAll().size());
                }
            }


            int startX = startLocation.getX(), startY = startLocation.getY(), endX = endLocation.getX(), endY = endLocation.getY();
            state[endX][endY] = state[startX][startY]; //move the piece
            state[startX][startY] = new Empty(); //old location to empty piece
            state[startX][startY].setLocation(new Location(startX, startY));
            state[endX][endY].setLocation(endLocation);
            state[endX][endY].setPrevLocation(startLocation);
            lastMoveStart = startLocation;
            lastMoveEnd = endLocation;

        } catch (NullPointerException e) {
            System.out.print("More than likely, Checkmate"); //Not quite 100% this is always the case
        }
    }

    public Player getWhitePlayer() {
        return whitePlayer;
    }

    public void setWhitePlayer(Player whitePlayer) {
        this.whitePlayer = whitePlayer;
    }

    public Player getBlackPlayer() {
        return blackPlayer;
    }

    public void setBlackPlayer(Player blackPlayer) {
        this.blackPlayer = blackPlayer;
    }


    public void setStatePieces() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece tempPiece = state[i][j];
                StatePieces tempPieces = tempPiece.getColour() == Colour.WHITE ? getWhitePieces() : getBlackPieces();
                switch (tempPiece.getName()) {
                    case KING:
                        tempPieces.addKing(tempPiece);
                        break;
                    case QUEEN:
                        tempPieces.addQueen(tempPiece);
                        break;
                    case ROOK:
                        tempPieces.addRook(tempPiece);
                        break;
                    case BISHOP:
                        tempPieces.addBishop(tempPiece);
                        break;
                    case KNIGHT:
                        tempPieces.addKnight(tempPiece);
                        break;
                    case PAWN:
                        tempPieces.addPawn(tempPiece);
                        break;
                }
            }
        }
    }

    public void displayBoard() {
        for (int m = 7; m >= 0; m--) {
            System.out.println("\n----------------------------------------------------------------------------------------------------------------------------");
            System.out.print(" " + (m + 1) + " | ");
            for (int n = 0; n < 8; n++) {
                if (state[n][m].getName() != Piece.Name.EMPTY) {
                    String name = state[n][m].getName().toString();
                    String message;
                    if (name.length() == 4)
                        message = " " + state[n][m].getName().toString() + ":" + state[n][m].getColour() + " ";
                    else if (name.length() == 5)
                        message = " " + name + ":" + state[n][m].getColour();
                    else
                        message = state[n][m].getName().toString() + ":" + state[n][m].getColour();

                    System.out.print(message + " | ");
                } else
                    System.out.print("            " + " | ");
            }
        }
        System.out.println("\n--------- A ------------- B ------------ C ------------ D ------------ E ------------ F ------------ G ------------ H ------");
    }

    @Override
    public State clone() {
        Piece[][] temp = new Piece[this.state.length][this.state[0].length];

        for (int i = 0; i < this.state.length; i++) {
            for (int j = 0; j < this.state[0].length; j++) {
                temp[i][j] = this.state[i][j].clone();
            }
        }
        State tempState = new State();
        tempState.setState(temp);
        tempState.setWhitePlayer(this.whitePlayer);
        tempState.setBlackPlayer(this.blackPlayer);
        try {
            tempState.setLastMoveStart(getLastMoveStart().clone());
            tempState.setLastMoveEnd(getLastMoveEnd().clone());
        } catch (NullPointerException ignored) {
        }
        tempState.setStatePieces();
        return tempState;
    }
}
