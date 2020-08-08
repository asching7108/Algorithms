import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Use maxflow formulation to solve the baseball elimination problem.
 *
 * @author Esther Lin
 */

public class BaseballElimination {

    private final int numTeams;                 // number of teams
    private final Map<String, Integer> teams;   // indices of the given team
    private final int[] wins;                   // number of wins for given team
    private final int[] losses;                 // number of losses for given team
    private final int[] left;                   // number of games left for given team
    private final int[][] games;                // number of games left between the two teams
    private final boolean[] isEliminated;       // if the team is eliminated
    private final Map<String, Set<String>> certificates;    // teams that eliminates given team

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        In in = new In(filename);
        numTeams = in.readInt();

        // initiate member variables
        teams = new HashMap<>();
        wins = new int[numTeams];
        losses = new int[numTeams];
        left = new int[numTeams];
        games = new int[numTeams][numTeams];
        isEliminated = new boolean[numTeams];
        certificates = new HashMap<>();

        // store input data
        int maxWins = 0;
        String maxWinsTeam = null;
        for (int i = 0; i < numTeams; i++) {
            String team = in.readString();
            teams.put(team, i);
            wins[i] = in.readInt();
            losses[i] = in.readInt();
            left[i] = in.readInt();
            if (wins[i] > maxWins) {
                maxWins = wins[i];
                maxWinsTeam = team;
            }
            for (int j = 0; j < numTeams; j++) {
                games[i][j] = in.readInt();
            }
        }

        // Trivial elimination
        int finalMaxWins = maxWins;
        String finalMaxWinsTeam = maxWinsTeam;
        teams.forEach((k, v) -> {
            // the team's max possible wins is less than the number of wins of another team
            if (wins[v] + left[v] < finalMaxWins) {
                isEliminated[v] = true;
                Set<String> cert = new HashSet<>();
                cert.add(finalMaxWinsTeam);
                certificates.put(k, cert);
            }
        });

        // Nontrivial elimination
        teams.forEach((k, v) -> {
            if (!isEliminated[v]) maxflowElimination(k);
        });
    }

    // use maxflow formulation to determine if team can be eliminated
    private void maxflowElimination(String team) {
        String[] fnTeams = fnTeams(team);
        int maxWins = wins(team) + remaining(team); // the given team's max possible wins
        int nTeam = fnTeams.length;                 // number of team vertices
        int nGame = nTeam * (nTeam - 1) / 2;        // number of game vertices
        int s = 0, t = nTeam + nGame + 1;           // the start and target vertices
        String[][] fnGames = new String[nGame][2];  // 2d array of all games among every two teams

        FlowNetwork fn = new FlowNetwork(nTeam + nGame + 2);

        int k = 0;  // counter of games
        for (int i = 0; i < nTeam - 1; i++) {
            for (int j = i + 1; j < nTeam; j++) {
                // store the two teams participating in the game
                fnGames[k][0] = fnTeams[i];
                fnGames[k][1] = fnTeams[j];

                // add edges from s to all game vertices
                // edge capacity = games left between team i and j
                fn.addEdge(new FlowEdge(s, k + 1, against(fnTeams[i], fnTeams[j])));

                // add edges from all game vertices to associated team vertices
                // edge capacity = infinity
                fn.addEdge(new FlowEdge(k + 1, nGame + 1 + i, Double.POSITIVE_INFINITY));
                fn.addEdge(new FlowEdge(k + 1, nGame + 1 + j, Double.POSITIVE_INFINITY));
                k++;
            }
        }

        for (int i = 0; i < nTeam; i++) {
            // add edges from all team vertices to t
            // edge capacity = team i can still win this many more games
            fn.addEdge(new FlowEdge(nGame + 1 + i, t, maxWins - wins(fnTeams[i])));
        }

        FordFulkerson ff = new FordFulkerson(fn, s, t);
        Iterable<FlowEdge> gameEdges = fn.adj(s);

        for (FlowEdge e : gameEdges) {
            int v = e.other(s);
            // if an edge from s to a game is not full, the team can be eliminated
            if (e.residualCapacityTo(v) > 0) {
                isEliminated[teams.get(team)] = true;
                break;
            }
        }

        Set<String> cert = new HashSet<>();
        for (FlowEdge e : gameEdges) {
            // if v is on the source side of the mincut, the associated teams are certificates
            if (ff.inCut(e.other(s))) {
                cert.add(fnGames[e.other(s) - 1][0]);
                cert.add(fnGames[e.other(s) - 1][1]);
            }
        }
        certificates.put(team, cert);
    }

    // get all non-eliminated teams except the given team
    private String[] fnTeams(String team) {
        List<String> fnTeams = new ArrayList<>();
        for (String t : teams.keySet()) {
            if (!t.equals(team) && !isEliminated[teams.get(t)]) fnTeams.add(t);
        }
        return fnTeams.toArray(new String[0]);
    }

    // number of teams
    public int numberOfTeams() {
        return numTeams;
    }

    // all teams
    public Iterable<String> teams() {
        return teams.keySet();
    }

    // number of wins for given team
    public int wins(String team) {
        if (!teams.containsKey(team))
            throw new IllegalArgumentException("invalid team");
        return wins[teams.get(team)];
    }

    // number of losses for given team
    public int losses(String team) {
        if (!teams.containsKey(team))
            throw new IllegalArgumentException("invalid team");
        return losses[teams.get(team)];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        if (!teams.containsKey(team))
            throw new IllegalArgumentException("invalid team");
        return left[teams.get(team)];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        if (!teams.containsKey(team1) || !teams.containsKey(team2))
            throw new IllegalArgumentException("invalid team(s)");
        return games[teams.get(team1)][teams.get(team2)];
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        if (!teams.containsKey(team))
            throw new IllegalArgumentException("invalid team");
        return isEliminated[teams.get(team)];
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        if (!teams.containsKey(team))
            throw new IllegalArgumentException("invalid team");
        if (!isEliminated[teams.get(team)]) return null;
        return certificates.get(team);
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
