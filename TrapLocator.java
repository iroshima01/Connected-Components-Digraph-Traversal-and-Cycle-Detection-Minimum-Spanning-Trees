import java.util.*;

public class TrapLocator {
    public List<Colony> colonies;

    public TrapLocator(List<Colony> colonies) {
        this.colonies = colonies;
    }

    public List<List<Integer>> revealTraps() {
        List<List<Integer>> traps = new ArrayList<>();

        for (Colony colony : colonies) {
            Set<Integer> visited = new HashSet<>();
            Set<Integer> onPath = new HashSet<>();
            List<Integer> trap = new ArrayList<>();
            boolean hasCycle = false;

            for (Integer city : colony.roadNetwork.keySet()) {
                if (!visited.contains(city) && hasCycleInColony(city, -1, colony, visited, onPath, trap)) {
                    hasCycle = true;
                    break;
                }
            }

            if (hasCycle) {
                Collections.reverse(trap);
                traps.add(trap);
            } else {
                traps.add(new ArrayList<>());
            }
        }

        return traps;
    }
    


    private boolean hasCycleInColony(int city, int parent, Colony colony, Set<Integer> visited, Set<Integer> onPath, List<Integer> trap) {
        visited.add(city);
        onPath.add(city);

        for (Integer neighbor : colony.roadNetwork.get(city)) {
            if (!visited.contains(neighbor)) {
                if (hasCycleInColony(neighbor, city, colony, visited, onPath, trap)) {
                    trap.add(city);
                    return true;
                }
            } else if (onPath.contains(neighbor) && neighbor != parent) {

                trap.add(city);

                return true;
            }
        }

        onPath.remove(city);
        return false;
    }

    public void printTraps(List<List<Integer>> traps) {
        System.out.println("Danger exploration conclusions:");

        for (int i = 0; i < traps.size(); i++) {
            System.out.printf("Colony %d: ", (i + 1));
            List<Integer> trap = traps.get(i);

            if (!trap.isEmpty()) {
                System.out.print("Dangerous. Cities on the dangerous path: ");

                for (int a = 0; a < trap.size(); a++){
                    trap.set(a, trap.get(a) + 1);
                }

                Collections.sort(trap);
                System.out.println(trap);
            } else {
                System.out.println("Safe");
            }
        }
    }
}
