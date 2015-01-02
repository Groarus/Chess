import java.util.ArrayList;

/**
 * Created by Eric on 12/27/2014.
 */
public class Node {

    private ArrayList<Node> children;
    private State state;
    private Node parent;
    private double evaluation;
    private boolean min;
    private boolean max;
    private int depth;
    private Location startLocation;
    private Location endLocation;
    private Node bestChild = null; //used to store the child that gave the min or max value

    public Node(Node parent, State state) {
        this.state = state;
        this.parent = parent;
        // this.evaluation = evaluation;
        children = new ArrayList<Node>();
        if (parent == null) {
            depth = 0;
/*            if (state.getBlackPlayer() instanceof ComputerPlayer) {
                min = true;
                max = false;
            } else {*/
            max = true;
            min = false;
//            }
        } else {
            depth = parent.getDepth() + 1;
            min = !parent.getMin();
            max = !parent.getMax();
        }
    }

    public void addChild(Node child) {
        children.add(child);
    }

    public ArrayList<Node> getChildren() {
        return children;
    }

    public boolean getMin() {
        return min;
    }

    public void setMin(boolean min) {
        this.min = min;
    }

    public boolean getMax() {
        return max;
    }

    public void setMax(boolean max) {
        this.max = max;
    }

    public boolean hasChildren() {
        return children.size() > 0;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }


    public Node getParent() {
        return parent;
    }

    public double getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(double evaluation) {
        this.evaluation = evaluation;
    }

    public int getDepth() {
        return depth;
    }

    public Location getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(Location startLocation) {
        this.startLocation = startLocation;
    }

    public Location getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(Location endLocation) {
        this.endLocation = endLocation;
    }

    public void minimize() {
        double bestEvaluation = Double.POSITIVE_INFINITY;
        for (int i = 0; i < children.size(); i++) {
            double childEval = children.get(i).getEvaluation();
            if (childEval < bestEvaluation) {
                bestEvaluation = childEval;
                bestChild = children.get(i);
                evaluation = bestEvaluation;
                startLocation = children.get(i).getStartLocation();
                endLocation = children.get(i).getEndLocation();
            }
        }
    }

    public void maximize() {
        double bestEvaluation = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < children.size(); i++) {
            double childEval = children.get(i).getEvaluation();
            if (childEval > bestEvaluation) {
                bestEvaluation = childEval;
                bestChild = children.get(i);
                evaluation = bestEvaluation;
                startLocation = children.get(i).getStartLocation();
                endLocation = children.get(i).getEndLocation();
            }
        }
    }

    public Node getBestChild() {
        return bestChild;
    }
}
