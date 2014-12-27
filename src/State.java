/**
 * Project: Chess
 * Course: COSC 3P71 - Final Project
 * Created: December, 2014
 */


public class State {

    Player whitePlayer, blackPlayer;
    private Piece[][] state;
    private Location lastMoveStart, lastMoveEnd;

    public State() {
        this.state = new Piece[8][8];
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

    public Piece[][] getState() {
        return state;
    }

    public void setState(Piece[][] state) {
        this.state = state;
    }

    public void setPiece(int x, int y, Piece piece) {
        state[x][y] = piece;
        if (!(piece.getName() == Piece.Name.EMPTY)) {
            state[x][y].setLocation(new Location(x, y));
        }
    }

    public Piece getPiece(int x, int y) {
        return state[x][y];
    }

    public Piece getPiece(Location location) {
        return state[location.getX()][location.getY()];
    }

    public void movePiece(Location startLocation, Location endLocation) {
        try {
            int startX = startLocation.getX(), startY = startLocation.getY(), endX = endLocation.getX(), endY = endLocation.getY();
            state[endX][endY] = state[startX][startY]; //move the piece
            state[startX][startY] = new Empty(); //old location to empty piece
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
        setWhitePlayer(this.whitePlayer);
        setBlackPlayer(this.blackPlayer);
        return tempState;
    }
}
