/**
 * Project: Chess
 * Course:
 * Created on 30 December, 2014
 */
public class TempMove {

    private Piece piece, toPiece;
    private State state;

    private Location toLocation, startLocation, prevLocation, lastMoveStart, lastMoveEnd;

    public TempMove(Piece piece, State state) {
        this.piece = piece;
        this.state = state;
    }

    public void move (Location toLocation) {
        this.toLocation = toLocation;
        this.startLocation = piece.getLocation();
        this.prevLocation = piece.getPrevLocation();
        this.toPiece = state.getPiece(toLocation);
        this.lastMoveStart = state.getLastMoveStart();
        this.lastMoveEnd = state.getLastMoveEnd();
        state.movePiece(startLocation, toLocation);
    }

    public void undoMove () {
        if (toPiece.getName() != Piece.Name.EMPTY && toPiece.getColour() == Colour.WHITE)
            state.getWhitePieces().addPiece(toPiece);
        else if (toPiece.getName() != Piece.Name.EMPTY && toPiece.getColour() == Colour.BLACK)
            state.getBlackPieces().addPiece(toPiece);
        state.movePiece(toLocation, startLocation);
        state.setPiece(toLocation.getX(), toLocation.getY(), toPiece);
        state.setLastMoveStart(lastMoveStart);
        state.setLastMoveEnd(lastMoveEnd);
        piece.setPrevLocation(prevLocation);
    }

    public Location getToLocation() {
        return toLocation;
    }
}
