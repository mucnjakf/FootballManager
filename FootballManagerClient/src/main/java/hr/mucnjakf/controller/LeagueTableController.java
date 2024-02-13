package hr.mucnjakf.controller;

import hr.mucnjakf.model.TeamTable;
import hr.mucnjakf.network.TcpClient;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class LeagueTableController implements Initializable {

    @FXML
    private TableView<TeamTable> tvLeagueTable;

    @FXML
    private TableColumn<TeamTable, String> tcNumber, tcTeam, tcGamesPlayed, tcPoints, tcWins, tcDraws, tcLosses, tcGoalsFor, tcGoalsAgainst;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadLeagueTable();
    }

    private void loadLeagueTable() {
        ObservableList<TeamTable> leagueTable = FXCollections.observableArrayList(TcpClient.requestLeagueTable());

        tcNumber.setCellValueFactory(param -> new ReadOnlyObjectWrapper(tvLeagueTable.getItems().indexOf(param.getValue()) + 1 + ""));
        tcNumber.setSortable(false);

        tcTeam.setCellValueFactory(new PropertyValueFactory<>("name"));
        tcGamesPlayed.setCellValueFactory(new PropertyValueFactory<>("gamesPlayed"));
        tcPoints.setCellValueFactory(new PropertyValueFactory<>("points"));
        tcWins.setCellValueFactory(new PropertyValueFactory<>("wins"));
        tcDraws.setCellValueFactory(new PropertyValueFactory<>("draws"));
        tcLosses.setCellValueFactory(new PropertyValueFactory<>("losses"));
        tcGoalsFor.setCellValueFactory(new PropertyValueFactory<>("goalsFor"));
        tcGoalsAgainst.setCellValueFactory(new PropertyValueFactory<>("goalsAgainst"));

        tvLeagueTable.setItems(leagueTable);
    }
}
