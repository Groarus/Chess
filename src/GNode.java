import java.util.LinkedList;

/**
 * Project: Chess
 * Course:
 * Created on 30 December, 2014
 */
public class GNode {

    private double evaluation;
    private LinkedList<TempMove> tempMoves = new LinkedList<TempMove>();

    public GNode() {
    }

    public GNode(LinkedList<TempMove> tempMoves, Double evaluation) {
        this.tempMoves = tempMoves;
        this.evaluation = evaluation;
    }

    public double getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(double evaluation) {
        this.evaluation = evaluation;
    }

    public LinkedList<TempMove> getTempMoves() {
        return tempMoves;
    }

    public void setTempMoves(LinkedList<TempMove> tempMoves) {
        this.tempMoves = tempMoves;
    }
}
