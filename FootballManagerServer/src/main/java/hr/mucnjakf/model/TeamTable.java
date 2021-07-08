package hr.mucnjakf.model;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class TeamTable implements Externalizable {

    public static final Long serialVersionUID = 3L;

    private String name;
    private Integer gamesPlayed;
    private Integer points;
    private Integer wins;
    private Integer draws;
    private Integer losses;
    private Integer goalsFor;
    private Integer goalsAgainst;

    public TeamTable() {
    }

    public TeamTable(String name, int gamesPlayed, int points, int wins, int draws, int losses, int goalsFor, int goalsAgainst) {
        this.name = name;
        this.gamesPlayed = gamesPlayed;
        this.points = points;
        this.wins = wins;
        this.draws = draws;
        this.losses = losses;
        this.goalsFor = goalsFor;
        this.goalsAgainst = goalsAgainst;
    }

    @Override
    public String toString() {
        return "TeamTable{" +
                "name='" + name + '\'' +
                ", gamesPlayed=" + gamesPlayed +
                ", points=" + points +
                ", wins=" + wins +
                ", draws=" + draws +
                ", losses=" + losses +
                ", goalsFor=" + goalsFor +
                ", goalsAgainst=" + goalsAgainst +
                '}';
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGamesPlayed(int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public void setDraws(int draws) {
        this.draws = draws;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public void setGoalsFor(int goalsFor) {
        this.goalsFor = goalsFor;
    }

    public void setGoalsAgainst(int goalsAgainst) {
        this.goalsAgainst = goalsAgainst;
    }

    public String getName() {
        return name;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public int getPoints() {
        return points;
    }

    public int getWins() {
        return wins;
    }

    public int getDraws() {
        return draws;
    }

    public int getLosses() {
        return losses;
    }

    public int getGoalsFor() {
        return goalsFor;
    }

    public int getGoalsAgainst() {
        return goalsAgainst;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeUTF(name);
        out.writeInt(gamesPlayed);
        out.writeInt(points);
        out.writeInt(wins);
        out.writeInt(draws);
        out.writeInt(losses);
        out.writeInt(goalsFor);
        out.writeInt(goalsAgainst);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException {
        name = in.readUTF();
        gamesPlayed = in.readInt();
        points = in.readInt();
        wins = in.readInt();
        draws = in.readInt();
        losses = in.readInt();
        goalsFor = in.readInt();
        goalsAgainst = in.readInt();
    }
}
