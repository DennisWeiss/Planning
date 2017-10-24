import java.util.ArrayList;

/**
 * Class for implementing State representing the blocks
 */
public class State {
    private ArrayList<ArrayList<Character>> stacks = new ArrayList<>();

    /**
     * constructor that initializes the ArrayList representing the stacks
     */
    public State() {
        for (int i = 0; i < 3; i++) {
            stacks.add(new ArrayList<>());
        }
    }

    /**
     *
     * @return the ArrayList representing the stacks
     */
    public ArrayList<ArrayList<Character>> getStacks() {
        return stacks;
    }

    public void add(char block, int pos) {
        try {
            this.stacks.get(pos).add(block);
        } catch (IndexOutOfBoundsException e) {

        }
    }

    /**
     * method to remove top element from stack at pos.
     * @param pos defining from which stack the top element is supposed to be removed
     */
    public void remove(int pos) {
        try {
            this.stacks.get(pos).remove(this.stacks.get(pos).size()-1);
        } catch (IndexOutOfBoundsException e) {

        }
    }
}
