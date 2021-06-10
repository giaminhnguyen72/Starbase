//I worked on the homework assignment alone, using only course materials.
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 *
 * @author Gia-Minh Nguyen
 * @version 1.1
 */
public class Starbase extends Application {
    private class Docks extends StackPane {
        private boolean occupied;
        private Rectangle rect;
        private Text textBox;
        private Docks() {
            super();
            textBox = new Text("Unoccupied");
            rect = new Rectangle(100, 100);
            rect.setFill(Color.GREEN);
            super.getChildren().add(rect);
            super.getChildren().add(textBox);
            setOccupied(false);
            addListeners();
        }
        public void addListeners() {
            rect.setOnMousePressed((Event t) -> {
                setOccupied(false);
            });
            textBox.setOnMousePressed((Event t) -> {
                setOccupied(false);
            });
        }
        public boolean isOccupied() {
            return occupied;
        }
        public void setOccupied(boolean occupy) {
            if (!occupy) {
                rect.setFill(Color.GREEN);
                textBox.setText("EMPTY");
            }
            if (occupy) {
                rect.setFill(Color.RED);
            }
            occupied = occupy;
        }
        public void changeText(String name, String shipType) {
            textBox.setText(name + "\n" + shipType);
        }
    }
    @Override
    public void start(Stage primaryStage) {
        StackPane background = new StackPane();
        Image spaceImage = new Image("space.jpg");
        ImageView spaceView = new ImageView(spaceImage);
        spaceView.fitWidthProperty().bind(primaryStage.widthProperty());
        spaceView.fitHeightProperty().bind(primaryStage.heightProperty());
        Pane root = createMainComp();
        background.getChildren().add(spaceView);
        background.getChildren().add(root);

        Scene scene = new Scene(background, 1000, 500);

        primaryStage.setTitle("Starbase Command");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    private BorderPane createMainComp() {
        BorderPane mainComp = new BorderPane();
        Label welcomeLabel = new Label("Welcome to Starbase 1331!");
        welcomeLabel.setFont(new Font("Arial", 24));
        welcomeLabel.setTextFill(Color.WHITE);
        welcomeLabel.setAlignment(Pos.CENTER);

        GridPane dock = new GridPane();
        createSpaceDock(dock);
        dock.setAlignment(Pos.CENTER);
        dock.setVgap(100);
        dock.setHgap(50);

        HBox input = new HBox();
        input.setAlignment(Pos.CENTER);
        mainComp.setTop(welcomeLabel);

        TextField inputText = new TextField("Starship name");
        input.getChildren().add(inputText);

        ComboBox dropdown = new ComboBox();
        input.getChildren().add(dropdown);
        Starship[] shipTypes = Starship.values();
        for (Starship s:shipTypes) {
            dropdown.getItems().add(s.toString());
        }
        dropdown.getSelectionModel().selectFirst();

        Button dockButton = new Button("Request Docking");
        input.getChildren().add(dockButton);
        dockButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                if (inputText.getText() != null && !(inputText.getText().equals(""))) {
                    Docks docking;
                    boolean fullyOccupied = true;
                    for (int i = 0; i < dock.getChildren().size(); i++) {
                        if (dock.getChildren().get(i) instanceof Docks) {
                            docking = (Docks) dock.getChildren().get(i);
                            if (!docking.isOccupied()) {
                                docking.setOccupied(true);
                                docking.changeText(inputText.getText(), dropdown.getValue().toString());
                                fullyOccupied = false;
                                break;
                            }
                        }
                    }
                    if (fullyOccupied) {
                        Stage warning = new Stage();
                        StackPane mainPane = new StackPane();
                        Label popupLabel = new Label(inputText.getText() + "did not receive docking clearance!");
                        Scene mainScene = new Scene(mainPane, 500, 700);
                        mainPane.getChildren().add(popupLabel);
                        warning.setTitle("Warning");
                        warning.setScene(mainScene);
                        warning.show();
                    }
                    inputText.setText("");
                }
            }
        });
        Button clearButton = new Button("Clear docks");
        clearButton.setStyle("-fx-background-color: MediumSeaGreen");
        clearButton.setOnAction((ActionEvent e) -> {
            Docks docking;
            for (int i = 0; i < dock.getChildren().size(); i++) {
                if (dock.getChildren().get(i) instanceof Docks) {
                    docking = (Docks) dock.getChildren().get(i);
                    docking.setOccupied(false);
                }
            }
        });
        input.getChildren().add(clearButton);

        BorderPane.setAlignment(welcomeLabel, Pos.CENTER);
        mainComp.setCenter(dock);
        BorderPane.setAlignment(dock, Pos.CENTER);
        mainComp.setBottom(input);
        BorderPane.setAlignment(input, Pos.CENTER);
        return mainComp;
    }
    private void createSpaceDock(GridPane dock) {
        for (int i = 0; i < 8; i++) {
            dock.add(createDocks(), i % 4, i / 4);
        }
    }
    private StackPane createDocks() {
        return new Docks();
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
