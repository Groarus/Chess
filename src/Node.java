import java.util.ArrayList;

/**
 * Created by Eric on 12/27/2014.
 */
public class Node {

    private ArrayList<Node> children;
    private State state;
    private Node parent;
    private double evaluation;
    private double min;
    private double max;
    private int depth;

    public Node(Node parent, State state, double evaluation) {
        this.state = state;
        this.parent = parent;
        this.evaluation = evaluation;
        children = new ArrayList<Node>();
        if (parent == null) {
            depth = 0;
        } else {
            depth = parent.getDepth() + 1;
        }
    }

    public void addChild(Node child) {
        children.add(child);
    }

    public ArrayList<Node> getChildren() {
        return children;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public boolean hasChildren() {
        return children.size() > 0;
    }

    public State getState() {
        return state;
    }

    public Node getParent() {
        return parent;
    }

    public int getDepth() {
        return depth;
    }
}
