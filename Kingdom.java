import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Kingdom {

    // TODO: You should add appropriate instance variables.

    Colony colony = new Colony();
    Set<Integer> visitedCities = new HashSet<>();
    int[][] adjacencyMatrix;

    public HashMap<Integer, List<Integer>> directedMap = new HashMap<>();

    public void initializeKingdom(String filename) {
        // Read the txt file and fill your instance variables
        // TODO: Your code here
        try {

            ArrayList<ArrayList<Integer>> a = new ArrayList<ArrayList<Integer>>();

            Scanner input = new Scanner(new File(filename));
            while(input.hasNextLine())
            {
                Scanner colReader = new Scanner(input.nextLine());
                ArrayList<Integer> col = new ArrayList<>();
                while(colReader.hasNextInt())
                {
                    col.add(colReader.nextInt());
                }
                a.add(col);
            }

            HashMap<Integer, List<Integer>> undirectedGraph = new HashMap<>();
            for (int i = 0; i < a.size(); i++) {
                List<Integer> neighbor = new ArrayList<>();
                for (int j = 0; j < a.size(); j++) {

                    int edges = a.get(i).get(j);

                    if (edges == 1) {
                        neighbor.add(j);

                    }
                }

                directedMap.put(i, neighbor);
            }


            int numCities = a.size();
            adjacencyMatrix = new int[numCities][numCities];
            for (int i = 0; i < numCities; i++) {
                List<Integer> neighbors = directedMap.get(i);
                for (int j = 0; j < numCities; j++) {
                    if (neighbors.contains(j)) {
                        adjacencyMatrix[i][j] = 1;
                        adjacencyMatrix[j][i] = 1;
                    }
                }
            }

        }
        catch (Exception e){
            System.out.println("Unable to read file " + filename);
        }
    }


    public void dfs(int node, Colony colony) {
        visitedCities.add(node);
        colony.cities.add(node);

        for (int neighbor = 0; neighbor < adjacencyMatrix[node].length; neighbor++) {
            if (adjacencyMatrix[node][neighbor] == 1 && !visitedCities.contains(neighbor)) {
                dfs(neighbor, colony);
            }
        }

    }

    public List<Colony> getColonies() {
        List<Colony> colonies = new ArrayList<>();
        // TODO: DON'T READ THE .TXT FILE HERE!
        // Identify the colonies using the given input file.
        // TODO: Your code here

        for (int i = 0; i < directedMap.size(); i++) {
            if (!visitedCities.contains(i)) {
                Colony colony = new Colony();
                dfs(i, colony);
                colonies.add(colony);

                for (int city: colony.cities){
                    colony.roadNetwork.put(city, directedMap.get(city));
                }
            }
        }
        return colonies;
    }

    public void printColonies(List<Colony> discoveredColonies) {
        // Print the given list of discovered colonies conforming to the given output format.
        // TODO: Your code here

        if (discoveredColonies.isEmpty()) {
            throw new IllegalArgumentException("The list of colonies cannot be empty.");
        }
        System.out.println("Discovered colonies are:");
        int colonyNum = 1;
        for (Colony colony : discoveredColonies) {
            for (int i = 0; i < colony.cities.size(); i++) {
                //colony.cities.set(i, colony.cities.get(i) + 1);
                colony.cities.set(i, colony.cities.get(i) + 1);
            }
        }

        for (Colony colony : discoveredColonies) {
            Collections.sort(colony.cities);
            System.out.printf("Colony %d: %s%n", colonyNum++, colony.cities);
        }
    }



}
