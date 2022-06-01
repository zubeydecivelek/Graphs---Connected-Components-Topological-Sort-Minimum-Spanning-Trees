import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MissionExploration {

    /**
     * Given a Galaxy object, prints the solar systems within that galaxy.
     * Uses exploreSolarSystems() and printSolarSystems() methods of the Galaxy object.
     *
     * @param galaxy a Galaxy object
     */
    public void printSolarSystems(Galaxy galaxy) {
        // TODO: YOUR CODE HERE
        List<SolarSystem> solarSystems =galaxy.exploreSolarSystems();
        galaxy.printSolarSystems(solarSystems);
    }

    /**
     * TODO: Parse the input XML file and return a list of Planet objects.
     *
     * @param filename the input XML file
     * @return a list of Planet objects
     */
    public Galaxy readXML(String filename) {
        List<Planet> planetList = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        try {
            factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

            DocumentBuilder documentBuilder = factory.newDocumentBuilder();

            Document document = documentBuilder.parse(new File(filename));

            document.getDocumentElement().normalize();

            NodeList nodeList = document.getElementsByTagName("Planet");

            for (int temp = 0; temp < nodeList.getLength(); temp++) {
                Node node = nodeList.item(temp);
                Element element = (Element) node;

                String id = element.getElementsByTagName("ID").item(0).getTextContent();
                String techLevel = element.getElementsByTagName("TechnologyLevel").item(0).getTextContent();

                List<String> neighbors = new ArrayList<>();

                NodeList neighborNodeList = element.getElementsByTagName("Neighbors");
                Node neighborNode = neighborNodeList.item(0);
                Element neighborElement = (Element) neighborNode;

                NodeList neighborPlanetNodeList = neighborElement.getElementsByTagName("PlanetID");

                for (int temp1 = 0;temp1<neighborPlanetNodeList.getLength();temp1++){
                    Node neighborPlanetNode = neighborPlanetNodeList.item(temp1);
                    neighbors.add(neighborPlanetNode.getTextContent());
                }

                Planet planet = new Planet(id,Integer.parseInt(techLevel),neighbors);
                planetList.add(planet);
            }
        }
        catch(ParserConfigurationException | SAXException | IOException e){
            e.printStackTrace();
        }
        return new Galaxy(planetList);
    }
}
