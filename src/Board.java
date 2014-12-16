/**
 * Project: Chess
 * Course: COSC 3P71 - Final Project
 * Created: December, 2014
 */


public class Board {
    private State currentState = new State();
    private Player whitePlayer, blackPlayer;

    private GUI gui;
    public Board(GUI gui) {
        if (!(gui == null)) {
        setUpBoard();
        //set up the GUI
            PaintBoard paintBoard = new PaintBoard(this);
            gui.addBoardPanel(paintBoard);
            this.gui = gui;
        }
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
        //empty places
        for (int j = 2; j < 6; j++) {
            for (int i = 0; i < 8; i++) {
                currentState.setPiece(i, j, new Empty(Piece.Name.EMPTY, Colour.NEUTRAL));
            }
        }
    }



    //only to be used by MoveEngine
    public void move(Location startLocation, Location endLocation) {
        int startX = startLocation.getX(), startY = startLocation.getY(), endX = endLocation.getX(), endY = endLocation.getY();

        currentState.setPiece(endX, endY, currentState.getPiece(startX, startY));//moving the piece
        currentState.setPiece(startX, startY, new Empty(Piece.Name.EMPTY, Colour.NEUTRAL)); //old location to null
        currentState.getPiece(endX, endY).setLocation(new Location(endX, endY)); //setting the new location of the piece
        currentState.getPiece(endX, endY).setPrevLocation(new Location(startX, startY)); //setting the previous location

//        displayBoard(); //for testing
    }

    public State getCurrentState() {
        return currentState;
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
                if (currentState.getPiece(n, m).getName() != Piece.Name.EMPTY) {
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

    public Board clone(){
        Piece[][] temp = new Piece[this.currentState.getState().length][this.currentState.getState()[0].length];

        for (int i = 0; i < this.currentState.getState().length; i++) {
            for (int j = 0; j <this.currentState.getState()[0].length; j++) {
                temp[i][j] = this.currentState.getState()[i][j].clone();
            }
        }
        Board tempBoard = new Board(null);
        tempBoard.getCurrentState().setState(temp);
        tempBoard.setWhitePlayer(this.whitePlayer);
        tempBoard.setBlackPlayer(this.blackPlayer);
        return tempBoard;
    }
}