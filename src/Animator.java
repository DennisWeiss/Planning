import java.util.ArrayList;

public class Animator {
    ArrayList<State> states;

    Animator() {
        states = new ArrayList<>();
    }

    void addState(State state) {
        states.add(state);
    }

    State pop() {
        State state = states.get(0);
        states.remove(0);
        return state;
    }
}
