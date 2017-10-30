import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.*;

/**
 * Main application class
 */

public class Main extends Application{
    private State initialState;
    private State finalState;
    private Button[][] blocks1;
    private Button[][] blocks2;
    private TextArea results;
    private Button go = new Button();
    private Animator animator = new Animator();

    /**
     * main method
     * @param args console arguments
     */
    public static void main(String args[]) {
        launch(args);
    }

    /**
     * JavaFX Application
     * @param primaryStage of the application
     */
    @Override
    public void start(Stage primaryStage) {
        VBox vBox = new VBox();
        HBox hBox1 = new HBox();
        HBox hBox2 = new HBox();
        Text text1 = new Text("initial stack position:");
        TextField initialPos = new TextField();
        initialPos.setPromptText("1-3");
        Region region1 = new Region();
        region1.setMinHeight(10);
        Text text2 = new Text("initial stack order:");
        TextField order = new TextField();
        order.setPromptText("Example: ACBD");
        Button submitOrder = new Button("Submit");
        VBox vBox2 = new VBox();
        vBox2.getChildren().addAll(text1, initialPos, region1, text2, order, submitOrder);
        GridPane grid1 = new GridPane();
        blocks1 = new Button[3][4];
        for (int i = 0; i < blocks1.length; i++) {
            for (int j = 0; j < blocks1[i].length; j++) {
                blocks1[i][j] = new Button();
                blocks1[i][j].setStyle("-fx-base: #DDDDDD");
                blocks1[i][j].setMinSize(25, 25);
                grid1.add(blocks1[i][j], i, j);
            }
        }

        Text text3 = new Text("final stack position:");
        TextField finalPos = new TextField();
        finalPos.setPromptText("1-3");
        Region region2 = new Region();
        region2.setMinHeight(10);
        Text text4 = new Text("final stack order:");
        TextField order2 = new TextField();
        order2.setPromptText("Example: DBAC");
        Button submitOrder2 = new Button("Submit");

        HBox mainHBox = new HBox();

        Scene scene = new Scene(mainHBox, 500, 350);

        submitOrder.setOnAction(event -> {
            if (Integer.parseInt(initialPos.getText()) != 1 && Integer.parseInt(initialPos.getText()) != 2 && Integer.parseInt(initialPos.getText()) != 3) {
                NumberBetween1and3(new Stage());
            } else if (!isValid(order.getText(), order2.getText())) {
                System.out.println("Test");
                InputNotValid(new Stage());
            } else {
                char[] blocks = order.getText().toCharArray();
                int posInArray = Integer.parseInt(initialPos.getText())-1;
                initialState = new State();
                for (int i = 0; i < blocks.length; i++) {
                    initialState.add(blocks[i], posInArray);
                }
                updateButtons(initialState, blocks1);
            }
        });


        submitOrder2.setOnAction(event -> {
            if (Integer.parseInt(finalPos.getText()) != 1 && Integer.parseInt(finalPos.getText()) != 2 && Integer.parseInt(finalPos.getText()) != 3) {
                NumberBetween1and3(new Stage());
            } else if (!isValid(order2.getText(), order.getText())) {
                System.out.println("Test");
                InputNotValid(new Stage());
            } else {
                char[] blocks = order2.getText().toCharArray();
                int posInArray = Integer.parseInt(finalPos.getText())-1;
                finalState = new State();
                for (int i = 0; i < blocks.length; i++) {
                    finalState.add(blocks[i], posInArray);
                }
                updateButtons(finalState, blocks2);
            }
        });

        VBox vBox3 = new VBox();
        vBox3.getChildren().addAll(text3, finalPos, region2, text4, order2, submitOrder2);
        GridPane grid2 = new GridPane();
        grid2.setPadding(new Insets(5));
        grid1.setPadding(new Insets(5));

        blocks2 = new Button[3][4];
        for (int i = 0; i < blocks2.length; i++) {
            for (int j = 0; j < blocks2[i].length; j++) {
                blocks2[i][j] = new Button();
                blocks2[i][j].setStyle("-fx-base: #DDDDDD");
                blocks2[i][j].setMinSize(25, 25);
                grid2.add(blocks2[i][j], i, j);
            }
        }


        hBox1.getChildren().addAll(vBox2, grid1);
        hBox2.getChildren().addAll(vBox3, grid2);
        Region region3 = new Region();
        region3.setMinHeight(40);

        VBox vBox4 = new VBox();
        vBox4.setPadding(new Insets(10, 30, 10, 30));
        results = new TextArea();
        results.setPrefWidth(200);
        Button getActions = new Button("Get Actions");
        go = new Button("Next Step");
        go.setDisable(true);
        vBox4.getChildren().addAll(results, getActions, go);
        vBox4.setSpacing(10);

        go.setOnAction(event -> animate());

        getActions.setOnAction(event -> calculateActions());

        mainHBox.setPadding(new Insets(10));

        vBox.getChildren().addAll(hBox1, region3, hBox2);
        mainHBox.getChildren().addAll(vBox, vBox4);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void animate() {
        System.out.println(animator.states.size());
        if (animator.states.size() > 0) {
            updateButtons(animator.pop(), blocks1);
        } else {
            go.setDisable(true);
        }
    }

    /**
     * Checks if inputs are valid
     * @param text which is the first input.
     * @param text1 which is the second input.
     * @return whether the inputs are valid.
     */
    private boolean isValid(String text, String text1) {
        if (text.length() != 4) {
            return false;
        }
        HashSet<Character> chars = new HashSet<>();
        char[] textArr = text.toCharArray();
        for (int i = 0; i < textArr.length; i++) {
            if (chars.contains(textArr[i])) {
                return false;
            }
            chars.add(textArr[i]);
        }
        if (text1.equals("")) {
            return true;
        }
        if (isAnagram(text, text1)) {
            return true;
        }
        return false;
    }

    /**
     * Checks whether two strings are anagrams. (words made of the same characters and same amount of characters)
     * @param text first string
     * @param text1 second string
     * @return whether two strings are anagrams
     */
    private boolean isAnagram(String text, String text1) {
        if (text.length() != text1.length()) {
            return false;
        }
        HashMap<Character, Integer> hashMap = new HashMap<>();
        HashMap<Character, Integer> hashMap1 = new HashMap<>();
        char[] textArr = text.toCharArray();
        char[] textArr1 = text1.toCharArray();
        for (int i = 0; i < textArr.length; i++) {
            if (hashMap.containsKey(textArr[i])) {
                hashMap.put(textArr[i], hashMap.get(textArr[i]) + 1);
            } else {
                hashMap.put(textArr[i], 0);
            }
        }
        for (int i = 0; i < textArr1.length; i++) {
            if (hashMap1.containsKey(textArr1[i])) {
                hashMap1.put(textArr1[i], hashMap1.get(textArr1[i]) + 1);
            } else {
                hashMap1.put(textArr1[i], 0);
            }
            if (!hashMap.containsKey(textArr1[i])) {
                return false;
            }
            if (hashMap1.get(textArr1[i]) > hashMap.get(textArr1[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * opens a stage (window) that tells the user that the given state is not valid.
     * @param stage where the message is shown
     */
    private void InputNotValid(Stage stage) {
        Text message = new Text("String has to be 4 characters long,\nmust not contain the same character\ntwice and has to use the same\ncharacters in both states.");
        message.setTranslateX(10);
        message.setTranslateY(10);
        Region region = new Region();
        region.setMinHeight(40);
        Button ok = new Button("OK");
        ok.setTranslateX(85);
        VBox vBox = new VBox();
        vBox.getChildren().addAll(message, region, ok);

        ok.setOnAction(event -> stage.close());

        stage.setResizable(false);
        Scene scene = new Scene(vBox, 210, 150);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * opens a stage (window) that tells the user that the position has to be between 1 and 3.
     * @param stage where the message is shown
     */
    private void NumberBetween1and3(Stage stage) {
        Text message = new Text("Position has to be a number\nbetween 1 and 3.");
        message.setTranslateX(10);
        message.setTranslateY(10);
        Region region = new Region();
        region.setMinHeight(40);
        Button ok = new Button("OK");
        ok.setTranslateX(65);
        VBox vBox = new VBox();
        vBox.getChildren().addAll(message, region, ok);

        ok.setOnAction(event -> stage.close());

        stage.setResizable(false);
        Scene scene = new Scene(vBox, 170, 130);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Method that calculates the required actions of the block-world-problem.
     */
    private void calculateActions() {
            results.setText("");

            int startPos = 2;
            int endPos = 2;
            int storage;

            HashMap<Character, Integer> locations = new HashMap<>();

            if (initialState.getStacks().get(0).size() > 0) {
                startPos = 0;
            } else if (initialState.getStacks().get(1).size() > 0) {
                startPos = 1;
            }

            if (finalState.getStacks().get(0).size() > 0) {
                endPos = 0;
            } else if (finalState.getStacks().get(1).size() > 0) {
                endPos = 1;
            }

            int size = initialState.getStacks().get(startPos).size();

            for (int i = 0; i < size; i++) {
                locations.put(initialState.getStacks().get(startPos).get(i), startPos);
            }

            updateButtons(initialState, this.blocks1);

            animator = new Animator();

            if (startPos == endPos) {
                for (int i = 3; i >= 0; i--) {
                    char block = initialState.getStacks().get(startPos).get(i);
                    initialState.remove(startPos);
                    if (startPos == 0) {
                        initialState.add(block, 1);
                        System.out.printf("MOVE(%c, %d)\n", block, 1);
                        State tempState = new State();
                        tempState.copyState(initialState);
                        animator.addState(tempState);
                        results.setText(results.getText() + "MOVE(" + block + ", " + 1 + ")\n");
                        locations.put(block, 1);
                    } else {
                        initialState.add(block, 0);
                        System.out.printf("MOVE(%c, %d)\n", block, 0);
                        State tempState = new State();
                        tempState.copyState(initialState);
                        animator.addState(tempState);
                        results.setText(results.getText() + "MOVE(" + block + ", " + 0 + ")\n");
                        locations.put(block, 0);
                    }
                }
                if (startPos == 0) {
                    startPos = 1;
                } else {
                    startPos = 0;
                }
            }

            for (int i = 0; i < size; i++) {
                char target = finalState.getStacks().get(endPos).get(i);
                for (int j = initialState.getStacks().get(locations.get(target)).size() - 1; j >= 0; j--) {
                    if (initialState.getStacks().get(locations.get(target)).get(j) == target) {
                        if (!(locations.get(target) == endPos && j == 0)) {
                            initialState.remove(locations.get(target));
                            initialState.add(target, endPos);
                            System.out.printf("MOVE(%c, %d)\n", target, endPos);
                            State tempState = new State();
                            tempState.copyState(initialState);
                            animator.addState(tempState);
                            results.setText(results.getText() + "MOVE(" + target + ", " + endPos + ")\n");
                            locations.put(target, endPos);
                            break;
                        }
                    } else {
                        if (locations.get(target) != 0 && endPos != 0) {
                            storage = 0;
                        } else if (locations.get(target) != 1 && endPos != 1) {
                            storage = 1;
                        } else {
                            storage = 2;
                        }
                        char elm = initialState.getStacks().get(locations.get(target)).get(j);
                        System.out.printf("MOVE(%c, %d)\n", elm, storage);
                        results.setText(results.getText() + "MOVE(" + elm + ", " + storage + ")\n");
                        initialState.remove(locations.get(target));
                        initialState.add(elm, storage);
                        State tempState = new State();
                        tempState.copyState(initialState);
                        animator.addState(tempState);
                        locations.put(elm, storage);
                    }
                    //updateButtons(initialState, blocks1);
                    //updateButtons(initialState, blocks1);
                }
            }
            if (animator.states.size() > 0) {
                go.setDisable(false);
            }
        //updateButtons(initialState, blocks1);
    }

    void doSomething() {
        for (int i = 0; i < 1000000; i++) {
            System.out.print(i);
        }
    }

    /**
     * Method that updates the buttons to visualize the stacks.
     * @param state which defines the represented state of blocks.
     * @param blocks which defines the button two-dimensional array where the visualization is supposed to take place.
     */
    void updateButtons(State state, Button[][] blocks) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                blocks[i][3-j].setText("");
                blocks[i][3-j].setStyle("-fx-base: #DDDDDD");
            }
        }

        for (int i = 0; i < state.getStacks().size(); i++) {
            for (int j = 0; j < state.getStacks().get(i).size(); j++) {
                blocks[i][3-j].setText(state.getStacks().get(i).get(j).toString());
                blocks[i][3-j].setStyle("-fx-base: #333333");
            }
        }
    }
}
