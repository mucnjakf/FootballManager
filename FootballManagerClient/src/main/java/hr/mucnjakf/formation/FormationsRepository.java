package hr.mucnjakf.formation;

import javafx.scene.layout.GridPane;

public class FormationsRepository {

    public static void loadFormation442(GridPane gpFootballField) {
        Formationable fourFourTwo = new Formation442();

        fourFourTwo.draw("Goalkeeper", gpFootballField);
        fourFourTwo.draw("Defender", gpFootballField);
        fourFourTwo.draw("Midfielder", gpFootballField);
        fourFourTwo.draw("Attacker", gpFootballField);
    }

    public static void loadFormation343(GridPane gpFootballField) {
        Formationable threeFourThree = new Formation343();

        threeFourThree.draw("Goalkeeper", gpFootballField);
        threeFourThree.draw("Defender", gpFootballField);
        threeFourThree.draw("Midfielder", gpFootballField);
        threeFourThree.draw("Attacker", gpFootballField);
    }

    public static void loadFormation433(GridPane gpFootballField) {
        Formationable fourThreeThree = new Formation433();

        fourThreeThree.draw("Goalkeeper", gpFootballField);
        fourThreeThree.draw("Defender", gpFootballField);
        fourThreeThree.draw("Midfielder", gpFootballField);
        fourThreeThree.draw("Attacker", gpFootballField);
    }

    public static void loadFormation4231(GridPane gpFootballField){
        Formationable fourTwoThreeOne = new Formation4231();

        fourTwoThreeOne.draw("Goalkeeper", gpFootballField);
        fourTwoThreeOne.draw("Defender", gpFootballField);
        fourTwoThreeOne.draw("Midfielder", gpFootballField);
        fourTwoThreeOne.draw("Attacker", gpFootballField);
    }

    public static void loadFormation451(GridPane gpFootballField) {
        Formationable fourFiveOne = new Formation451();

        fourFiveOne.draw("Goalkeeper", gpFootballField);
        fourFiveOne.draw("Defender", gpFootballField);
        fourFiveOne.draw("Midfielder", gpFootballField);
        fourFiveOne.draw("Attacker", gpFootballField);
    }
}
