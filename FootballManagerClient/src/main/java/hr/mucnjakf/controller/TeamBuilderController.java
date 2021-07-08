package hr.mucnjakf.controller;

import hr.mucnjakf.formation.FormationsRepository;
import hr.mucnjakf.model.Player;
import hr.mucnjakf.model.Team;
import hr.mucnjakf.database.DataRepository;
import hr.mucnjakf.network.TcpClient;
import hr.mucnjakf.network.UdpClient;
import hr.mucnjakf.utilities.alert.AlertUtilities;
import hr.mucnjakf.utilities.serialization.SerializationUtilities;
import hr.mucnjakf.utilities.string.StringUtilities;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class TeamBuilderController implements Initializable {

    @FXML
    private GridPane gpFootballField;
    @FXML
    private TableView<Player> tvPlayers;
    @FXML
    private TableColumn<Player, String> tcId, tcFirstName, tcLastName, tcNation, tcClub, tcPosition, tcOverall;
    @FXML
    private ComboBox<String> cbFormations;
    @FXML
    private TextField tfTeamName;
    @FXML
    private TextField tfFilter;
    @FXML
    private Button btnPlay, btnSaveTeam, btnClearFF;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadPlayersToTableView();
        loadFormationsToComboBox();

        setupTableViewForDragAndDrop();
    }

    private void loadPlayersToTableView() {
        ObservableList<Player> players = FXCollections.observableArrayList(DataRepository.getPlayersFromCsv());

        tcId.setCellValueFactory(new PropertyValueFactory<>("Id"));
        tcFirstName.setCellValueFactory(new PropertyValueFactory<>("FirstName"));
        tcLastName.setCellValueFactory(new PropertyValueFactory<>("LastName"));
        tcNation.setCellValueFactory(new PropertyValueFactory<>("Nation"));
        tcClub.setCellValueFactory(new PropertyValueFactory<>("Club"));
        tcPosition.setCellValueFactory(new PropertyValueFactory<>("Position"));
        tcOverall.setCellValueFactory(new PropertyValueFactory<>("Overall"));

        tvPlayers.setItems(players);

        FilteredList<Player> filteredPlayers = new FilteredList<>(players, p -> true);

        tfFilter.textProperty().addListener((observable, oldValue, newValue) -> filteredPlayers.setPredicate(
                player -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }

                    String lowerCaseFilter = newValue.toLowerCase();

                    if (player.getFirstName().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (player.getLastName().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (player.getNation().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (player.getClub().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (player.getPosition().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (player.getOverall().toString().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }
                    return false;
                }));

        SortedList<Player> sortedPlayers = new SortedList<>(filteredPlayers);
        sortedPlayers.comparatorProperty().bind(tvPlayers.comparatorProperty());
        tvPlayers.setItems(sortedPlayers);
    }

    private void loadFormationsToComboBox() {
        cbFormations.getItems().addAll(
                "4-4-2",
                "3-4-3",
                "4-3-3",
                "4-2-3-1",
                "4-5-1");
    }

    @FXML
    private void loadPlayerFormationOnGridAction() {
        gpFootballField.getChildren().clear();

        btnPlay.setDisable(false);
        btnSaveTeam.setDisable(false);
        btnClearFF.setDisable(false);

        switch (cbFormations.getValue()) {
            case "4-4-2":
                FormationsRepository.loadFormation442(gpFootballField);
                break;
            case "3-4-3":
                FormationsRepository.loadFormation343(gpFootballField);
                break;
            case "4-3-3":
                FormationsRepository.loadFormation433(gpFootballField);
                break;
            case "4-2-3-1":
                FormationsRepository.loadFormation4231(gpFootballField);
                break;
            case "4-5-1":
                FormationsRepository.loadFormation451(gpFootballField);
                break;
            default:
                break;
        }
    }

    private void setupTableViewForDragAndDrop() {
        tvPlayers.setOnDragDetected(event -> {
            Dragboard db = tvPlayers.startDragAndDrop(TransferMode.ANY);

            ClipboardContent content = new ClipboardContent();
            content.putString(tvPlayers.getSelectionModel().getSelectedItem().getId().toString());
            db.setContent(content);

            event.consume();
        });
    }

    @FXML
    private void clearFootballFieldAction() {
        loadPlayerFormationOnGridAction();
        tfTeamName.clear();
    }

    @FXML
    private void saveTeamAction() {
        List<Player> players = new ArrayList<>();
        int counter = 0;

        File file = new File("E:\\dev\\FootballManager\\FootballManagerClient\\save\\"
                + tfTeamName.getText() + ".ser");

        for (Node nStackPane : gpFootballField.getChildren()) {
            StackPane sp = (StackPane) nStackPane;
            for (Node nCircleOrLabel : sp.getChildren()) {
                if (nCircleOrLabel instanceof Label) {
                    String playerId = ((Label) nCircleOrLabel).getText();

                    if (StringUtilities.isNumber(playerId)) {
                        counter++;

                        Player player = DataRepository.getPlayerFromCsv(Integer.parseInt(playerId));
                        players.add(player);
                    }
                }
            }
        }

        if (counter == 11 && !tfTeamName.getText().isEmpty()) {
            if (file.exists()) {
                Optional<ButtonType> result = AlertUtilities.showConfirmationDialog(
                        Alert.AlertType.CONFIRMATION,
                        "Overwrite team " + tfTeamName.getText(),
                        "Are you sure you want to overwrite team " + tfTeamName.getText() + "?");

                if (result.get() == ButtonType.OK) {
                    writePlayersToFile(players, file);
                }
            } else {
                writePlayersToFile(players, file);
            }
        } else {
            AlertUtilities.showAlertWithoutContent(
                    Alert.AlertType.ERROR,
                    "Save fail",
                    "You have to add players to field and enter team name before saving!");
        }
    }

    private void writePlayersToFile(List<Player> players, File file) {
        try {
            String teamName = tfTeamName.getText();
            String formation = cbFormations.getValue();

            Team team = new Team(teamName, formation, players);

            SerializationUtilities.write(team, file);

            AlertUtilities.showAlertWithoutContent(
                    Alert.AlertType.INFORMATION,
                    "Save success",
                    "Team successfully saved!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void loadTeamAction() {
        List<Team> teams = DataRepository.getTeamsFromSaveFolder();

        if (!(teams.size() > 0)) {
            AlertUtilities.showAlertWithoutContent(
                    Alert.AlertType.ERROR,
                    "No saved teams",
                    "No team saves found! Save team before loading!"
            );
            return;
        }

        ChoiceDialog<Team> dialog = new ChoiceDialog<>();

        for (Team team : teams) {
            dialog.getItems().add(team);
        }
        dialog.setTitle("Choose team");
        dialog.setHeaderText("Choose team you want to load!");
        dialog.setSelectedItem(teams.get(0));

        Optional<Team> result = dialog.showAndWait();
        if (result.isPresent()) {
            Team team = result.get();

            cbFormations.getSelectionModel().select(team.getFormation());
            loadPlayersToGpFootballField(team.getPlayers());
            tfTeamName.setText(team.getName());

//            AlertUtilities.showAlertWithoutContent(
//                    Alert.AlertType.INFORMATION,
//                    "Load success",
//                    "Team successfully loaded!");
        }
    }

    private void loadPlayersToGpFootballField(List<Player> players) {
        gpFootballField.getChildren().clear();

        loadPlayerFormationOnGridAction();

        for (Node nStackPane : gpFootballField.getChildren()) {
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

    @FXML
    private void deleteTeamAction() {
        List<Team> teams = DataRepository.getTeamsFromSaveFolder();

        if (!(teams.size() > 0)) {
            AlertUtilities.showAlertWithoutContent(
                    Alert.AlertType.ERROR,
                    "No saved teams",
                    "No team saves found! Save team before loading!"
            );
            return;
        }

        ChoiceDialog<Team> dialog = new ChoiceDialog<>();

        for (Team team : teams) {
            dialog.getItems().add(team);
        }

        dialog.setTitle("Choose team");
        dialog.setHeaderText("Choose team you want to delete!");
        dialog.setSelectedItem(teams.get(0));

        Optional<Team> result = dialog.showAndWait();
        if (result.isPresent()) {
            Team team = result.get();

            String teamName = team.getName();
            File teamFile = new File("E:\\dev\\FootballManager\\FootballManagerClient\\save\\" + teamName + ".ser");

            if (teamFile.delete()) {
                AlertUtilities.showAlertWithoutContent(
                        Alert.AlertType.INFORMATION,
                        "Team " + teamName + " deleted",
                        "You have successfully deleted team " + teamName + "!"
                );
                clearFootballFieldAction();
            } else {
                AlertUtilities.showAlertWithoutContent(
                        Alert.AlertType.ERROR,
                        "Team " + teamName + " delete fail",
                        "Deleting team " + teamName + " failed!"
                );
            }
        }
    }

    private List<Player> getPlayersFromFootballField() {
        List<Player> players = new ArrayList<>();

        for (Node nStackPane : gpFootballField.getChildren()) {
            StackPane sp = (StackPane) nStackPane;
            for (Node nCircleOrLabel : sp.getChildren()) {
                if (nCircleOrLabel instanceof Label) {
                    String playerId = ((Label) nCircleOrLabel).getText();

                    if (StringUtilities.isNumber(playerId)) {
                        Player player = DataRepository.getPlayerFromCsv(Integer.parseInt(playerId));
                        players.add(player);
                    }
                }
            }
        }
        return players;
    }

    @FXML
    public void playAction() {
        List<Player> players = getPlayersFromFootballField();
        Team team = new Team(tfTeamName.getText().trim(), cbFormations.getValue().trim(), players);

        openMatchWindow(team);
    }

    private void openMatchWindow(Team team) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Match.fxml"));

            Scene scene = new Scene(loader.load());

            Stage stage = new Stage();
            stage.setTitle("Match");
            stage.setScene(scene);

            MatchController controller = loader.getController();
            controller.initTeamForSending(team);

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void openLeagueTableAction() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LeagueTable.fxml"));

            Scene scene = new Scene(loader.load());

            Stage stage = new Stage();
            stage.setTitle("League Table");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}