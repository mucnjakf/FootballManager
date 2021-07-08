package hr.mucnjakf.formation;

import javafx.scene.control.Label;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;

public class Formation433 implements Formationable {

    @Override
    public void draw(String playerPosition, GridPane gpFootballField) {

        switch (playerPosition) {
            case "Goalkeeper":
                Circle gkC = new Circle(30);
                Label gkL = new Label("GK");
                gkL.setTextFill(Color.WHITE);
                gkL.setFont(Font.font(24));
                StackPane gkSp = new StackPane();
                gkL.setDisable(true);
                gkSp.setMaxWidth(50);
                gkSp.setMaxHeight(50);
                gkSp.getChildren().addAll(gkC, gkL);

                gkSp.setOnDragOver(event -> {
                    if (event.getGestureSource() != gkSp && event.getDragboard().hasString()) {
                        event.acceptTransferModes(TransferMode.ANY);
                    }
                    event.consume();
                });

                gkSp.setOnDragEntered(event -> {
                    if (event.getGestureSource() != gkSp && event.getDragboard().hasString()) {
                        gkC.setFill(Color.GREEN);
                    }
                    event.consume();
                });

                gkSp.setOnDragExited(event -> {
                    gkC.setFill(Color.BLACK);
                    event.consume();
                });

                gkSp.setOnDragDropped(event -> {
                    Dragboard db = event.getDragboard();
                    boolean success = false;

                    if (db.hasString()) {
                        gkL.setText(db.getString());
                        success = true;
                    }

                    event.setDropCompleted(success);
                    event.consume();
                });

                gpFootballField.add(gkSp, 2, 3, 1, 2);
                break;
            case "Defender":
                for (int i = 0; i < 4; i++) {
                    Circle defC = new Circle(30);
                    Label defL = new Label("DEF");
                    defL.setTextFill(Color.WHITE);
                    defL.setFont(Font.font(24));
                    StackPane defSp = new StackPane();
                    defL.setDisable(true);
                    defSp.setMaxWidth(50);
                    defSp.setMaxHeight(50);
                    defSp.getChildren().addAll(defC, defL);

                    defSp.setOnDragOver(event -> {
                        if (event.getGestureSource() != defSp && event.getDragboard().hasString()) {
                            event.acceptTransferModes(TransferMode.ANY);
                        }
                        event.consume();
                    });

                    defSp.setOnDragEntered(event -> {
                        if (event.getGestureSource() != defSp && event.getDragboard().hasString()) {
                            defC.setFill(Color.GREEN);
                        }
                        event.consume();
                    });

                    defSp.setOnDragExited(event -> {
                        defC.setFill(Color.BLACK);
                        event.consume();
                    });

                    defSp.setOnDragDropped(event -> {
                        Dragboard db = event.getDragboard();
                        boolean success = false;

                        if (db.hasString()) {
                            defL.setText(db.getString());
                            success = true;
                        }

                        event.setDropCompleted(success);
                        event.consume();
                    });

                    gpFootballField.add(defSp, i, 2, 2, 2);
                }
                break;
            case "Midfielder":
                for (int i = 0; i < 3; i++) {
                    Circle midC = new Circle(30);
                    Label midL = new Label("MID");
                    midL.setTextFill(Color.WHITE);
                    midL.setFont(Font.font(24));
                    StackPane midSp = new StackPane();
                    midL.setDisable(true);
                    midSp.setMaxWidth(50);
                    midSp.setMaxHeight(50);
                    midSp.getChildren().addAll(midC, midL);

                    midSp.setOnDragOver(event -> {
                        if (event.getGestureSource() != midSp && event.getDragboard().hasString()) {
                            event.acceptTransferModes(TransferMode.ANY);
                        }
                        event.consume();
                    });

                    midSp.setOnDragEntered(event -> {
                        if (event.getGestureSource() != midSp && event.getDragboard().hasString()) {
                            midC.setFill(Color.GREEN);
                        }
                        event.consume();
                    });

                    midSp.setOnDragExited(event -> {
                        midC.setFill(Color.BLACK);
                        event.consume();
                    });

                    midSp.setOnDragDropped(event -> {
                        Dragboard db = event.getDragboard();
                        boolean success = false;

                        if (db.hasString()) {
                            midL.setText(db.getString());
                            success = true;
                        }

                        event.setDropCompleted(success);
                        event.consume();
                    });

                    gpFootballField.add(midSp, i + 1, 1, 1, 2);
                }
                break;
            case "Attacker":
                for (int i = 0; i < 3; i++) {
                    Circle attC = new Circle(30);
                    Label attL = new Label("ATT");
                    attL.setTextFill(Color.WHITE);
                    attL.setFont(Font.font(24));
                    StackPane attSp = new StackPane();
                    attL.setDisable(true);
                    attSp.setMaxWidth(50);
                    attSp.setMaxHeight(50);
                    attSp.getChildren().addAll(attC, attL);

                    attSp.setOnDragOver(event -> {
                        if (event.getGestureSource() != attSp && event.getDragboard().hasString()) {
                            event.acceptTransferModes(TransferMode.ANY);
                        }
                        event.consume();
                    });

                    attSp.setOnDragEntered(event -> {
                        if (event.getGestureSource() != attSp && event.getDragboard().hasString()) {
                            attC.setFill(Color.GREEN);
                        }
                        event.consume();
                    });

                    attSp.setOnDragExited(event -> {
                        attC.setFill(Color.BLACK);
                        event.consume();
                    });

                    attSp.setOnDragDropped(event -> {
                        Dragboard db = event.getDragboard();
                        boolean success = false;

                        if (db.hasString()) {
                            attL.setText(db.getString());
                            success = true;
                        }

                        event.setDropCompleted(success);
                        event.consume();
                    });

                    gpFootballField.add(attSp, i + 1, 0, 1, 2);
                }
                break;
        }
    }
}
