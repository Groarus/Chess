/**
 * Project: Chess
 * Course:
 * Created on 30 December, 2014
 */
public class TempMove {

    private Piece piece, toPiece;
    private State state;
    private MoveEngine.SpecialMove specialMove;
    private Piece passant1 = null, passant2 = null;
    private TempMove castle1 = null, castle2 = null;

    private Location toLocation, startLocation, prevLocation, lastMoveStart, lastMoveEnd;

    public TempMove(Piece piece, State state) {
        this.piece = piece;
        this.state = state;
    }

    public void move(Location toLocation, MoveEngine.SpecialMove specialMove) {
        this.specialMove = specialMove;
        this.toLocation = toLocation;
        this.startLocation = piece.getLocation();
        this.prevLocation = piece.getPrevLocation();
        this.toPiece = state.getPiece(toLocation);
        this.lastMoveStart = state.getLastMoveStart();
        this.lastMoveEnd = state.getLastMoveEnd();
        state.movePiece(startLocation, toLocation);

        switch (specialMove) {
            case ENPASSANT:
                passant1 = state.getPiece(toLocation.getX(), toLocation.getY() + 1);
                passant2 = state.getPiece(toLocation.getX(), toLocation.getY() - 1);

                state.getPieces(passant1.getColour()).removePiece(passant1);
                state.getPieces(passant2.getColour()).removePiece(passant2);

                state.setPiece(toLocation.getX(), toLocation.getY() + 1, new Empty());
                state.setPiece(toLocation.getX(), toLocation.getY() - 1, new Empty());
                break;
            case CASTLE1:
                castle1 = new TempMove(state.getPiece((toLocation.getX() + 1), toLocation.getY()), state);
                castle1.move(new Location(toLocation.getX() - 1, toLocation.getY()), MoveEngine.SpecialMove.NONE);
                break;
            case CASTLE2:
                castle2 = new TempMove(state.getPiece((toLocation.getX() - 2), toLocation.getY()), state);
                castle2.move(new Location(toLocation.getX() + 1, toLocation.getY()), MoveEngine.SpecialMove.NONE);
                break;
            case PROMOTE:
                state.getPieces(piece.getColour()).removePiece(state.getPiece(piece.getLocation()));
                state.setPiece(piece.getLocation().getX(), piece.getLocation().getY(), new Queen(piece.getColour()));
                state.getPieces(piece.getColour()).addQueen(state.getPiece(piece.getLocation()));
                break;
        }
    }

    public void undoMove() {
        if (toPiece.getName() != Piece.Name.EMPTY)
            state.getPieces(toPiece.getColour()).addPiece(toPiece);

        state.movePiece(toLocation, startLocation);
        state.setPiece(toLocation.getX(), toLocation.getY(), toPiece);
        state.setLastMoveStart(lastMoveStart);
        state.setLastMoveEnd(lastMoveEnd);
        piece.setPrevLocation(prevLocation);

        switch (specialMove) {
            case ENPASSANT:
                state.setPiece(toLocation.getX(), toLocation.getY() + 1, passant1);
                state.setPiece(toLocation.getX(), toLocation.getY() - 1, passant2);
                break;
            case CASTLE1:
                castle1.undoMove();
                break;
            case CASTLE2:
                castle2.undoMove();
                break;
            case PROMOTE:
                state.getPieces(piece.getColour()).removePiece(state.getPiece(startLocation));
                state.setPiece(startLocation.getX(), startLocation.getY(), new Pawn(piece.getColour()));
                state.getPieces(piece.getColour()).addPawn(state.getPiece(startLocation));
                break;
        }
    }

    public Location getToLocation() {
        return toLocation;
    }

    public Location getStartLocation() {
        return startLocation;
    }
}
