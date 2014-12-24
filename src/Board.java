/**
 * Project: Chess
 * Course: COSC 3P71 - Final Project
 * Created: December, 2014
 */


public class Board extends State {
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
        this.setPiece(0, 0, new Rook(Colour.WHITE));
        this.setPiece(1, 0, new Knight(Colour.WHITE));
        this.setPiece(2, 0, new Bishop(Colour.WHITE));
        this.setPiece(3, 0, new Queen(Colour.WHITE));
        this.setPiece(4, 0, new King(Colour.WHITE));
        this.setPiece(5, 0, new Bishop(Colour.WHITE));
        this.setPiece(6, 0, new Knight(Colour.WHITE));
        this.setPiece(7, 0, new Rook(Colour.WHITE));

        //black -- first row
        this.setPiece(0, 7, new Rook(Colour.BLACK));
        this.setPiece(1, 7, new Knight(Colour.BLACK));
        this.setPiece(2, 7, new Bishop(Colour.BLACK));
        this.setPiece(3, 7, new Queen(Colour.BLACK));
        this.setPiece(4, 7, new King(Colour.BLACK));
        this.setPiece(5, 7, new Bishop(Colour.BLACK));
        this.setPiece(6, 7, new Knight(Colour.BLACK));
        this.setPiece(7, 7, new Rook(Colour.BLACK));

        //white pawns
        for (int i = 0; i < 8; i++) {
            this.setPiece(i, 1, new Pawn(Colour.WHITE));
        }
        //black pawns
        for (int i = 0; i < 8; i++) {
            this.setPiece(i, 6, new Pawn(Colour.BLACK));
        }
        //empty places
        for (int j = 2; j < 6; j++) {
            for (int i = 0; i < 8; i++) {
                this.setPiece(i, j, new Empty(Piece.Name.EMPTY, Colour.NEUTRAL));
            }
        }
    }
}