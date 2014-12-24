import java.awt.image.BufferedImage;


/**
 * Project: Chess
 * Course: COSC 3P71 - Final Project
 * Created: December, 2014
 */

public abstract class Piece {

    protected BufferedImage image = null;
    private Location location, prevLocation = null;
    private Colour colour;
    private Name name;
    private Status status;
    private boolean selected, possibleMove, inCheck;

    public Piece(Name name, Colour colour) {
        this.colour = colour;
        this.name = name;
        this.status = Status.ALIVE;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String printLocation() {
        String string = Character.toString((char) (location.getX() + 65));
        string = string + (location.getY() + 1);
        return string;
    }

    public Colour getColour() {
        return colour;
    }

    public Name getName() {
        return name;
    }

    public Status getStatus() {
        return status;
    }

    public BufferedImage getImage() {
        return image;
    }

    public Location getPrevLocation() {
        return prevLocation;
    }

    public void setPrevLocation(Location prevLocation) {
        this.prevLocation = prevLocation;
    }

    public boolean isPossibleMove() {
        return possibleMove;
    }

    public void setPossibleMove(boolean possibleMove) {
        this.possibleMove = possibleMove;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isInCheck() {
        return inCheck;
    }

    public void setInCheck(boolean inCheck) {
        this.inCheck = inCheck;
    }

    public Piece clone() {
        if (this.getName() == Name.PAWN) {
            Pawn p = new Pawn(this.getColour());
            p.setLocation(new Location(this.getLocation().getX(), this.getLocation().getY()));
            if (!(this.getPrevLocation() == null)) {
                p.setPrevLocation(new Location(this.getPrevLocation().getX(), this.getPrevLocation().getY()));
            } else {
                p.setPrevLocation(null);
            }
            return p;
        } else if (this.getName() == Name.KING) {
            King k = new King(this.getColour());
            k.setLocation(new Location(this.getLocation().getX(), this.getLocation().getY()));
            if (!(this.getPrevLocation() == null)) {
                k.setPrevLocation(new Location(this.getPrevLocation().getX(), this.getPrevLocation().getY()));
            } else {
                k.setPrevLocation(null);
            }
            return k;
        } else if (this.getName() == Name.KNIGHT) {
            Knight knight = new Knight(this.getColour());
            knight.setLocation(new Location(this.getLocation().getX(), this.getLocation().getY()));
            if (!(this.getPrevLocation() == null)) {
                knight.setPrevLocation(new Location(this.getPrevLocation().getX(), this.getPrevLocation().getY()));
            } else {
                knight.setPrevLocation(null);
            }
            return knight;
        } else if (this.getName() == Name.QUEEN) {
            Queen q = new Queen(this.getColour());
            q.setLocation(new Location(this.getLocation().getX(), this.getLocation().getY()));
            if (!(this.getPrevLocation() == null)) {
                q.setPrevLocation(new Location(this.getPrevLocation().getX(), this.getPrevLocation().getY()));
            } else {
                q.setPrevLocation(null);
            }
            return q;
        } else if (this.getName() == Name.ROOK) {
            Rook r = new Rook(this.getColour());
            r.setLocation(new Location(this.getLocation().getX(), this.getLocation().getY()));
            if (!(this.getPrevLocation() == null)) {
                r.setPrevLocation(new Location(this.getPrevLocation().getX(), this.getPrevLocation().getY()));
            } else {
                r.setPrevLocation(null);
            }
            return r;
        } else if (this.getName() == Name.BISHOP) {
            Bishop b = new Bishop(this.getColour());
            b.setLocation(new Location(this.getLocation().getX(), this.getLocation().getY()));
            if (!(this.getPrevLocation() == null)) {
                b.setPrevLocation(new Location(this.getPrevLocation().getX(), this.getPrevLocation().getY()));
            } else {
                b.setPrevLocation(null);
            }
            return b;
        } else {
            Empty e = new Empty(Name.EMPTY, Colour.NEUTRAL);
            if (!(this.getLocation() == null)) {
                e.setLocation(new Location(this.getLocation().getX(), this.getLocation().getY()));
            }
            if (!(this.getPrevLocation() == null)) {
                e.setPrevLocation(new Location(this.getPrevLocation().getX(), this.getPrevLocation().getY()));
            } else {
                e.setPrevLocation(null);
            }
            return e;
        }
    }

    enum Status {
        ALIVE, DEAD;
    }

    enum Name {
        BISHOP, KING, KNIGHT, PAWN, QUEEN, ROOK, EMPTY;
    }

}
