package hr.mucnjakf.utilities.xml;

import hr.mucnjakf.model.TeamTable;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class XmlUtilities {

    public static final String FILENAME = "LeagueTable.xml";

    public static boolean isUsingResource;

    public synchronized void readFromXml(List<TeamTable> teams) {
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(FILENAME))) {
            while (isUsingResource) {
                wait();
            }

            isUsingResource = true;

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
            Document xmlDocument = documentBuilder.parse(bis);

            NodeList nodes = xmlDocument.getElementsByTagName("Team");
            for (int i = 0; i < nodes.getLength(); i++) {
                Node team = nodes.item(i);

                if (team.getNodeType() == Node.ELEMENT_NODE) {
                    Element teamElement = (Element) team;

                    String name = teamElement.getElementsByTagName("Name").item(0).getTextContent();
                    int gamesPlayed = Integer.parseInt(teamElement.getElementsByTagName("GamesPlayed").item(0).getTextContent());
                    int points = Integer.parseInt(teamElement.getElementsByTagName("Points").item(0).getTextContent());
                    int wins = Integer.parseInt(teamElement.getElementsByTagName("Wins").item(0).getTextContent());
                    int draws = Integer.parseInt(teamElement.getElementsByTagName("Draws").item(0).getTextContent());
                    int losses = Integer.parseInt(teamElement.getElementsByTagName("Losses").item(0).getTextContent());
                    int goalsFor = Integer.parseInt(teamElement.getElementsByTagName("GoalsFor").item(0).getTextContent());
                    int goalsAgainst = Integer.parseInt(teamElement.getElementsByTagName("GoalsAgainst").item(0).getTextContent());

                    TeamTable teamTable = new TeamTable(name, gamesPlayed, points, wins, draws, losses, goalsFor, goalsAgainst);
                    teams.add(teamTable);
                }
            }

            isUsingResource = false;
            notifyAll();
        } catch (IOException | ParserConfigurationException | SAXException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void writeToXml(List<TeamTable> teams) {
        try {
            while (isUsingResource) {
                wait();
            }

            isUsingResource = true;

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
            Document xmlDocument = documentBuilder.newDocument();

            Element rootLeagueTable = xmlDocument.createElement("LeagueTable");
            xmlDocument.appendChild(rootLeagueTable);

            for (TeamTable team : teams) {
                Element teamElement = xmlDocument.createElement("Team");
                rootLeagueTable.appendChild(teamElement);

                Element nameElement = xmlDocument.createElement("Name");
                nameElement.appendChild(xmlDocument.createTextNode(team.getName()));
                teamElement.appendChild(nameElement);

                Element gamesPlayedElement = xmlDocument.createElement("GamesPlayed");
                gamesPlayedElement.appendChild(xmlDocument.createTextNode(String.valueOf(team.getGamesPlayed())));
                teamElement.appendChild(gamesPlayedElement);

                Element pointsElement = xmlDocument.createElement("Points");
                pointsElement.appendChild(xmlDocument.createTextNode(String.valueOf(team.getPoints())));
                teamElement.appendChild(pointsElement);

                Element winsElement = xmlDocument.createElement("Wins");
                winsElement.appendChild(xmlDocument.createTextNode(String.valueOf(team.getWins())));
                teamElement.appendChild(winsElement);

                Element drawsElement = xmlDocument.createElement("Draws");
                drawsElement.appendChild(xmlDocument.createTextNode(String.valueOf(team.getDraws())));
                teamElement.appendChild(drawsElement);

                Element lossesElement = xmlDocument.createElement("Losses");
                lossesElement.appendChild(xmlDocument.createTextNode(String.valueOf(team.getLosses())));
                teamElement.appendChild(lossesElement);

                Element goalsForElement = xmlDocument.createElement("GoalsFor");
                goalsForElement.appendChild(xmlDocument.createTextNode(String.valueOf(team.getGoalsFor())));
                teamElement.appendChild(goalsForElement);

                Element goalsAgainstElement = xmlDocument.createElement("GoalsAgainst");
                goalsAgainstElement.appendChild(xmlDocument.createTextNode(String.valueOf(team.getGoalsAgainst())));
                teamElement.appendChild(goalsAgainstElement);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            Source xmlSource = new DOMSource(xmlDocument);
            Result outputTarget = new StreamResult(new File(FILENAME));

            transformer.transform(xmlSource, outputTarget);

            isUsingResource = false;
            notifyAll();
        } catch (ParserConfigurationException | TransformerException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
