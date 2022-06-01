import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Project {
    private final String name;
    private final List<Task> tasks;

    private int [] endTimes;

    public Project(String name, List<Task> tasks) {
        this.name = name;
        this.tasks = tasks;
        endTimes = new int[tasks.size()];
    }

    @Override
    public String toString() {
        String str = name;
        str+=("\nTASKS:\n");
        for (int i = 0;i< tasks.size();i++){
            str += tasks.get(i).toString();
            str += "\n";
        }

        return str;
    }

    /**
     * Schedule all tasks within this project such that they will be completed as early as possible.
     *
     * @return An integer array consisting of the earliest start days for each task.
     */
    public int[] getEarliestSchedule() {
        // TODO: YOUR CODE HERE
        Digraph digraph = new Digraph(tasks.size());

        for(Task task:tasks){
            for (int dependencies: task.getDependencies()){
                digraph.addEdge(task.getTaskID(), dependencies);
            }
        }

        DepthFirstOrder depthFirstOrder = new DepthFirstOrder(digraph);
        Stack<Integer> topologicalGraph = depthFirstOrder.reversePost();


        endTimes[0] = tasks.get(0).getDuration();
        for (int i :topologicalGraph){
            int endTime = tasks.get(i).getDuration();
            int maxDay = 0;
            for (int dependencies:digraph.adj(i)){
                if (endTimes[dependencies]>maxDay)
                    maxDay = endTimes[dependencies];
            }
            endTime += maxDay;
            endTimes[i] =endTime;
        }

        int[] startTimes = new int[tasks.size()];
        for (int i =0;i< tasks.size();i++){
            startTimes[i] = endTimes[i] - tasks.get(i).getDuration();
        }
        return startTimes;
    }

    /**
     * @return the total duration of the project in days
     */
    public int getProjectDuration() {
        int projectDuration;
        int[] schedule = getEarliestSchedule();
        projectDuration = tasks.get(schedule.length - 1).getDuration() + schedule[schedule.length - 1];
        // TODO: YOUR CODE HERE
        return projectDuration;
    }

    public static void printlnDash(int limit, char symbol) {
        for (int i = 0; i < limit; i++) System.out.print(symbol);
        System.out.println();
    }

    public void printSchedule(int[] schedule) {
        int limit = 65;
        char symbol = '-';
        printlnDash(limit, symbol);
        System.out.println(String.format("Project name: %s", name));
        printlnDash(limit, symbol);

        // Print header
        System.out.println(String.format("%-10s%-45s%-7s%-5s", "Task ID", "Description", "Start", "End"));
        printlnDash(limit, symbol);
        for (int i = 0; i < schedule.length; i++) {
            Task t = tasks.get(i);
            System.out.println(String.format("%-10d%-45s%-7d%-5d", i, t.getDescription(), schedule[i], schedule[i] + t.getDuration()));
        }
        printlnDash(limit, symbol);
        System.out.println(String.format("Project will be completed in %d days.", tasks.get(schedule.length - 1).getDuration() + schedule[schedule.length - 1]));
        printlnDash(limit, symbol);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;

        int equal = 0;

        for (Task otherTask : ((Project) o).tasks) {
            if (tasks.stream().anyMatch(t -> t.equals(otherTask))) {
                equal++;
            }
        }

        return name.equals(project.name) && equal == tasks.size();
    }

}

class Digraph {
    private final int V;
    private final ArrayList<ArrayList<Integer>> adj;
    public Digraph(int V) {
        this.V = V;
        adj =  new ArrayList<>();
        for (int v = 0; v < V; v++)
            adj.add(new ArrayList<>());
    }
    public void addEdge(int v, int w) {
        adj.get(v).add(w);
    }
    public ArrayList<Integer> adj(int v) { return adj.get(v) ; }

    public int V() {
        return V;
    }
}

class DepthFirstOrder {
    private boolean[] marked;
    private Stack<Integer> reversePost;
    public DepthFirstOrder(Digraph G)
    {
        reversePost = new Stack<Integer>();
        marked = new boolean[G.V()];
        for (int v = 0; v < G.V(); v++)
            if (!marked[v]) dfs(G, v);
    }

    private void dfs(Digraph G, int v) {
        marked[v] = true;
        for (int w : G.adj(v))
            if (!marked[w]) dfs(G, w);
        reversePost.push(v);
    }
    //TOPOLOGICAL SORT
    public Stack<Integer> reversePost() { return reversePost; }
}
