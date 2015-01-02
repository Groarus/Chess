/**
 * Project: Chess
 * Course:
 * Created on 30 December, 2014
 */
public class GNode {

    private double evaluation;
    private State state;
    private Location start, end;

    public GNode(State state) {
        this.state = state;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public double getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(double evaluation) {
        this.evaluation = evaluation;
    }

    public void saveMove(Location start, Location end) {
        this.start = start;
        this.end = end;
    }

    public Location getStart() {
        return start;
    }

    public Location getEnd() {
        return end;
    }
}
