import java.util.LinkedList;

/**
 * Project: Chess
 * Course:
 * Created on 28 December, 2014
 */
public class StatePieces {

    private LinkedList<Piece> King, Queen, Rook, Bishop, Knight, Pawn;

    public StatePieces() {
        King = new LinkedList<Piece>();
        Queen = new LinkedList<Piece>();
        Rook = new LinkedList<Piece>();
        Bishop = new LinkedList<Piece>();
        Knight = new LinkedList<Piece>();
        Pawn = new LinkedList<Piece>();
    }

    public LinkedList<Piece> getAll() {
        LinkedList<Piece> all = new LinkedList<Piece>();
        all.addAll(this.King);
        all.addAll(this.Queen);
        all.addAll(this.Rook);
        all.addAll(this.Bishop);
        all.addAll(this.Knight);
        all.addAll(this.Pawn);

        return all;
    }

    public void removePiece(Piece piece) {
        switch (piece.getName()) {
            case KING:
                System.out.println("CHECK MATE StatePieces");
                break;
            case QUEEN:
                Queen.remove(piece);
                break;
            case ROOK:
                Rook.remove(piece);
                break;
            case BISHOP:
                Bishop.remove(piece);
                break;
            case KNIGHT:
                Knight.remove(piece);
                break;
            case PAWN:
                Pawn.remove(piece);
                break;
        }
    }

    public void addPiece(Piece piece) {
        switch (piece.getName()) {
            case KING:
                addKing(piece);
                break;
            case QUEEN:
                addQueen(piece);
                break;
            case ROOK:
                addRook(piece);
                break;
            case BISHOP:
                addBishop(piece);
                break;
            case KNIGHT:
                addKnight(piece);
                break;
            case PAWN:
                addPawn(piece);
                break;
        }
    }

    public void addKing(Piece king) {
        King.add(king);
    }

    public void addQueen(Piece queen) {
        Queen.add(queen);
    }

    public void addRook(Piece rook) {
        Rook.add(rook);
    }

    public void addBishop(Piece bishop) {
        Bishop.add(bishop);
    }

    public void addKnight(Piece knight) {
        Knight.add(knight);
    }

    public void addPawn(Piece pawn) {
        Pawn.add(pawn);
    }

    public LinkedList<Piece> getKing() {
        return King;
    }

    public LinkedList<Piece> getQueens() {
        return Queen;
    }

    public LinkedList<Piece> getRooks() {
        return Rook;
    }

    public LinkedList<Piece> getBishops() {
        return Bishop;
    }

    public LinkedList<Piece> getKnights() {
        return Knight;
    }

    public LinkedList<Piece> getPawns() {
        return Pawn;
    }
}
