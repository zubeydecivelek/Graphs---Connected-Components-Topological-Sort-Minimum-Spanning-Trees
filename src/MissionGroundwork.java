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
import java.util.*;

public class MissionGroundwork {

    /**
     * Given a list of Project objects, prints the schedule of each of them.
     * Uses getEarliestSchedule() and printSchedule() methods of the current project to print its schedule.
     *
     * @param projectList a list of Project objects
     */
    public void printSchedule(List<Project> projectList) {
        // TODO: YOUR CODE HERE

        for (Project project:projectList){
            int[] schedule = project.getEarliestSchedule();
            project.printSchedule(schedule);
        }

    }

    /**
     * TODO: Parse the input XML file and return a list of Project objects
     *
     * @param filename the input XML file
     * @return a list of Project objects
     */
    public List<Project> readXML(String filename) {
        List<Project> projectList = new ArrayList<>();
        // TODO: YOUR CODE HERE
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        try {
            factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

            DocumentBuilder documentBuilder = factory.newDocumentBuilder();

            Document document = documentBuilder.parse(new File(filename));

            document.getDocumentElement().normalize();

            NodeList nodeList = document.getElementsByTagName("Project");

            for (int temp = 0; temp < nodeList.getLength(); temp++) {
                Node node = nodeList.item(temp);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    String name = element.getElementsByTagName("Name").item(0).getTextContent();
                    List<Task> taskList = new ArrayList<>();

                    NodeList taskNodeList = element.getElementsByTagName("Task");

                    for (int temp1 = 0; temp1 < taskNodeList.getLength(); temp1++) {
                        Node taskNode = taskNodeList.item(temp1);

                        if (taskNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element taskElement = (Element) taskNode;

                            String taskID = taskElement.getElementsByTagName("TaskID").item(0).getTextContent();
                            String description = taskElement.getElementsByTagName("Description").item(0).getTextContent();
                            String duration = taskElement.getElementsByTagName("Duration").item(0).getTextContent();

                            List<Integer> dependencies = new ArrayList<>();

                            NodeList dependenciesNodeList = taskElement.getElementsByTagName("Dependencies");
                            Node dependenciesNode = dependenciesNodeList.item(0);
                            Element dependenciesElement = (Element) dependenciesNode;

                            NodeList dependsOnTaskIDNodeList = dependenciesElement.getElementsByTagName("DependsOnTaskID");

                            for (int temp3 = 0; temp3<dependsOnTaskIDNodeList.getLength();temp3++){
                                Node dependsOnTaskIDNode = dependsOnTaskIDNodeList.item(temp3);
                                dependencies.add(Integer.parseInt(dependsOnTaskIDNode.getTextContent()));
                            }
                            Task task = new Task(Integer.parseInt(taskID),description,Integer.parseInt(duration),dependencies);
                            taskList.add(task);
                        }
                    }
                    Project project = new Project(name,taskList);
                    //System.out.println(project);
                    projectList.add(project);
                }
            }
        }catch(ParserConfigurationException | SAXException | IOException e){
            e.printStackTrace();
        }
        return projectList;
    }
}

