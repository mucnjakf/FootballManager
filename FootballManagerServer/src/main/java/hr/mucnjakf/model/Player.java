package hr.mucnjakf.model;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class Player implements Externalizable {

    public static final long serialVersionUID = 2L;

    private Integer id;
    private String firstName;
    private String lastName;
    private String nation;
    private String club;
    private String position;
    private Integer overall;

    public Player() {
    }

    public Player(Integer id, String firstName, String lastName, String nation, String club, String position, Integer overall) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.nation = nation;
        this.club = club;
        this.position = position;
        this.overall = overall;
    }

    public Integer getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getNation() {
        return nation;
    }

    public String getClub() {
        return club;
    }

    public String getPosition() {
        return position;
    }

    public Integer getOverall() {
        return overall;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public void setClub(String club) {
        this.club = club;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setOverall(Integer overall) {
        this.overall = overall;
    }

    @Override
    public String toString() {
        return id + " - " + firstName + " - " + lastName + " - " + nation + " - " + club + " - " + position + " - " + overall;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(id);
        out.writeUTF(firstName);
        out.writeUTF(lastName);
        out.writeUTF(nation);
        out.writeUTF(club);
        out.writeUTF(position);
        out.writeInt(overall);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException {
        id = in.readInt();
        firstName = in.readUTF();
        lastName = in.readUTF();
        nation = in.readUTF();
        club = in.readUTF();
        position = in.readUTF();
        overall = in.readInt();
    }
}
