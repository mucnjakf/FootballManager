package hr.mucnjakf.controller;

import hr.mucnjakf.formation.FormationsRepository;
import hr.mucnjakf.model.Player;
import hr.mucnjakf.model.Team;
import hr.mucnjakf.network.TcpClient;
import hr.mucnjakf.network.UdpClient;
import hr.mucnjakf.rmi.ChatClient;
import javafx.beans.binding.Bindings;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class MatchController implements Initializable {

    @FXML
    private ListView<Player> lvHomeTeamPlayers;
    @FXML
    private ListView<Player> lvAwayTeamPlayers;
    @FXML
    private VBox vbMatchEvents;
    @FXML
    private ScrollPane spMatchEventsContainer;
    @FXML
    private Label lblHomeTeamName;
    @FXML
    private Label lblHomeTeamFormation;
    @FXML
    private Label lblHomeTeamOverall;
    @FXML
    private Label lblAwayTeamName;
    @FXML
    private Label lblAwayTeamFormation;
    @FXML
    private Label lblAwayTeamOverall;
    @FXML
    private GridPane gpHomeTeamFootballField;
    @FXML
    private GridPane gpAwayTeamFootballField;
    @FXML
    private Label lblHomeTeamGoals;
    @FXML
    private Label lblAwayTeamGoals;
    @FXML
    private TextField tfMessage;
    @FXML
    private ScrollPane spChatContainer;
    @FXML
    private VBox vbChatMessages;

    private static int homeTeamGoal = 0;
    private static int awayTeamGoal = 0;

    private ChatClient chatClient;

    private ObservableList<Node> matchEvents;

    private ObservableList<Node> messages;

    public static final int MESSAGE_MAX_LENGTH = 78;
    public static final int MATCH_EVENT_FONT = 15;
    public static final int CHAT_MESSAGE_FONT = 15;

    private UdpClient udpClient = new UdpClient(this);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        chatClient = new ChatClient();

        matchEvents = FXCollections.observableArrayList();
        Bindings.bindContentBidirectional(matchEvents, vbMatchEvents.getChildren());

        messages = FXCollections.observableArrayList();
        Bindings.bindContentBidirectional(messages, vbChatMessages.getChildren());

        tfMessage.textProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue.length() >= MESSAGE_MAX_LENGTH) {
                ((StringProperty) observable).setValue(oldValue);
            }
        }));
    }

    public void initTeamForSending(Team team) {
        TcpClient.sendTeam(team);
        startUdpClient();
    }

    private void startUdpClient() {
        udpClient.setDaemon(true);
        udpClient.start();
    }

    public void showTeams(List<Team> teams) {
        Team homeTeam = teams.get(0);
        Team awayTeam = teams.get(1);

        List<Player> homeTeamPlayers = homeTeam.getPlayers();
        List<Player> awayTeamPlayers = awayTeam.getPlayers();

        lblHomeTeamName.setText(homeTeam.getName());
        lblHomeTeamFormation.setText(homeTeam.getFormation());
        lblHomeTeamOverall.setText(homeTeam.getOverall().toString());

        lblAwayTeamName.setText(awayTeam.getName());
        lblAwayTeamFormation.setText(awayTeam.getFormation());
        lblAwayTeamOverall.setText(awayTeam.getOverall().toString());

        lvHomeTeamPlayers.setItems(FXCollections.observableArrayList(homeTeam.getPlayers()));
        lvAwayTeamPlayers.setItems(FXCollections.observableArrayList(awayTeam.getPlayers()));

        loadTeamFormations(homeTeam, gpHomeTeamFootballField);
        loadPlayersToFormation(homeTeamPlayers, gpHomeTeamFootballField);

        loadTeamFormations(awayTeam, gpAwayTeamFootballField);
        loadPlayersToFormation(awayTeamPlayers, gpAwayTeamFootballField);
    }

    private void loadTeamFormations(Team team, GridPane teamFootballField) {
        switch (team.getFormation()) {
            case "4-4-2":
                FormationsRepository.loadFormation442(teamFootballField);
                break;
            case "3-4-3":
                FormationsRepository.loadFormation343(teamFootballField);
                break;
            case "4-3-3":
                FormationsRepository.loadFormation433(teamFootballField);
                break;
            case "4-2-3-1":
                FormationsRepository.loadFormation4231(teamFootballField);
                break;
            case "4-5-1":
                FormationsRepository.loadFormation451(teamFootballField);
                break;
            default:
                break;
        }
    }

    private void loadPlayersToFormation(List<Player> players, GridPane teamFootballField) {
        for (Node nStackPane : teamFootballField.getChildren()) {
            StackPane sp = (StackPane) nStackPane;
            for (Node nCircleOrLabel : sp.getChildren()) {
                if (nCircleOrLabel instanceof Label) {
                    Label l = (Label) nCircleOrLabel;

                    for (int i = 0; i < players.size(); i++) {
                        if (l.getText().equals(players.get(i).getPosition())) {
                            l.setText(players.get(i).getId().toString());
                            players.remove(players.get(i));
                        }
                    }
                }
            }
        }
    }

    public void showEvent(String event) {
        if (event.contains("GOAL")) {
            if (event.contains(lblHomeTeamName.getText())) {
                homeTeamGoal++;
                lblHomeTeamGoals.setText(String.valueOf(homeTeamGoal));
            } else if (event.contains(lblAwayTeamName.getText())) {
                awayTeamGoal++;
                lblAwayTeamGoals.setText(String.valueOf(awayTeamGoal));
            }
        }

        addEventToEvents(event);
    }

    private void addEventToEvents(String event) {
        Label evnt = new Label();
        evnt.setFont(new Font(MATCH_EVENT_FONT));
        evnt.setText(event);
        matchEvents.add(evnt);

        moveScrollPane(spMatchEventsContainer);
    }

    @FXML
    private void sendOnEnter(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            sendMessageAction();
        }
    }

    @FXML
    private void sendMessageAction() {
        String message = tfMessage.getText().trim();

        if (message.length() > 0) {
            chatClient.sendMessage(message);

            tfMessage.clear();
        }
    }

    private void addMessageToChat(String message) {
        Label msg = new Label();
        msg.setFont(new Font(CHAT_MESSAGE_FONT));
        msg.setText(String.format("%s: %s", LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")), message));
        messages.add(msg);

        moveScrollPane(spChatContainer);
    }

    private void moveScrollPane(ScrollPane pane) {
        pane.applyCss();
        pane.layout();
        pane.setVvalue(1D);
    }

    public void showMessage(String message) {
        addMessageToChat(message);
    }
}
