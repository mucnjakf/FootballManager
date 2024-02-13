package hr.mucnjakf.model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Team implements Externalizable {

    private static final long serialVersionUID = 1L;

    private String name;
    private String formation;
    private Integer overall;
    private Integer attRating;
    private Integer midRating;
    private Integer defRating;
    private List<Player> players;

    public Team(){
    }

    public Team(String name, String formation, List<Player> players) {
        this.name = name;
        this.formation = formation;
        this.players = players;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFormation() {
        return formation;
    }

    public void setFormation(String formation) {
        this.formation = formation;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public Integer getOverall() {
        int overall = 0;

        for (Player player : players) {
            overall += player.getOverall();
        }

        return overall;
    }

    public Integer getAttRating() {
        int attRating = 0;

        for (Player player : players) {
            if (player.getPosition().equals("ATT")) {
                attRating += player.getOverall();
            }
        }

        return attRating;
    }

    public Integer getMidRating() {
        int midRating = 0;

        for (Player player : players) {
            if (player.getPosition().equals("MID")) {
                midRating += player.getOverall();
            }
        }

        return midRating;
    }

    public Integer getDefRating() {
        int defRating = 0;

        for (Player player : players) {
            if (player.getPosition().equals("DEF")) {
                defRating += player.getOverall();
            }
        }

        return defRating;
    }

    @Override
    public String toString() {
        return "Team: " + name + " - Formation: " + formation + " - Overall: " + getOverall();
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeUTF(name);
        out.writeUTF(formation);
        out.writeObject(overall);
        out.writeObject(attRating);
        out.writeObject(midRating);
        out.writeObject(defRating);
        out.writeObject(players);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        name = in.readUTF();
        formation = in.readUTF();
        overall = (Integer)in.readObject();
        attRating = (Integer)in.readObject();
        midRating = (Integer)in.readObject();
        defRating = (Integer)in.readObject();
        players = (List<Player>) in.readObject();
    }
}
