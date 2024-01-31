import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class TravelMap {

    // Maps a single Id to a single Location.
    public Map<Integer, Location> locationMap = new HashMap<>();

    // List of locations, read in the given order
    public List<Location> locations = new ArrayList<>();

    // List of trails, read in the given order
    public List<Trail> trails = new ArrayList<>();

    // TODO: You are free to add more variables if necessary.

    public void initializeMap(String filename) {
        // Read the XML file and fill the instance variables locationMap, locations and trails.
        // TODO: Your code here
        try {
            File file = new File(filename);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document document = builder.parse(file);

            document.getDocumentElement().normalize();

            NodeList listLocation = document.getElementsByTagName("Location");

            for (int i = 0; i < listLocation.getLength(); i++){
                Node loc = listLocation.item(i);

                if (loc.getNodeType() == Node.ELEMENT_NODE){
                    Element el = (Element) loc;

                    int id = Integer.parseInt(el.getElementsByTagName("Id").item(0).getTextContent());
                    String name = el.getElementsByTagName("Name").item(0).getTextContent();

                    Location location = new Location(name, id);

                    locationMap.put(id, location);
                    locations.add(location);

                }

            }

            NodeList listTrail = document.getElementsByTagName("Trail");
            for (int i = 0; i < listTrail.getLength(); i++){
                Node tr = listTrail.item(i);

                if (tr.getNodeType() == Node.ELEMENT_NODE){
                    Element ele = (Element) tr;

                    int source = Integer.parseInt(ele.getElementsByTagName("Source").item(0).getTextContent());
                    int destination = Integer.parseInt(ele.getElementsByTagName("Destination").item(0).getTextContent());
                    int danger = Integer.parseInt(ele.getElementsByTagName("Danger").item(0).getTextContent());

                    Trail trail = new Trail(locationMap.get(source), locationMap.get(destination), danger);
                    trails.add(trail);
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Trail> getSafestTrails() {
        List<Trail> safestTrails = new ArrayList<>();
        // Fill the safestTrail list and return it.
        // Select the optimal Trails from the Trail list that you have read.
        // TODO: Your code here
        List<Trail> sortedSafestTrails = new ArrayList<>();
        List<Trail> sorted = new ArrayList<>(trails);
        sorted.sort(Comparator.comparingInt(trail -> trail.danger));


        Set<Location> visited = new HashSet<>();

        Trail currentTrail = sorted.get(0);
        visited.add(currentTrail.source);
        visited.add(currentTrail.destination);

        safestTrails.add(currentTrail);
        while (visited.size() < locations.size()) {
            Trail nextTrail = null;
            for (Trail trail : sorted) {
                if (!visited.contains(trail.source)
                        && visited.contains(trail.destination)) {
                    nextTrail = trail;
                    break;
                }
                if (!visited.contains(trail.destination)
                        && visited.contains(trail.source)) {
                    nextTrail = trail;
                    break;
                }
            }
            if (nextTrail == null) {

                break;
            }
            safestTrails.add(nextTrail);
            visited.add(nextTrail.source);
            visited.add(nextTrail.destination);
        }
        return safestTrails;
    }

    public void printSafestTrails(List<Trail> safestTrails) {
        // Print the given list of safest trails conforming to the given output format.
        // TODO: Your code here
        System.out.println("Safest trails are:");
        int totalDanger = 0;
        for (Trail trail : safestTrails){
            System.out.println("The trail from "+ trail.source.name+ " to " + trail.destination.name + " with danger " + trail.danger);
            totalDanger += trail.danger;
        }
        System.out.println("Total danger: "+ totalDanger);

    }
}
