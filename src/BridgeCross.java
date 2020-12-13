import java.util.*;

public class BridgeCross {

    public static void main(String[] args) {
        long startTime =System.currentTimeMillis();
        //checking values of parametres
        for( String s:args) { 
            if (Integer.parseInt(s)<= 0){
                System.out.println("Negative parameter given");
                System.exit(0);
            } 
        }        
        // 1st arg: people number. then idividual people crossing times. Lastly Time limit.
        
        int n= Integer.parseInt(args[0]); //number of family members
        if (n!= args.length-2) {
            System.out.println("Wrong number of parameters given");
            System.exit(0);
        }
        int Time= Integer.parseInt(args[args.length-1]); //time limit
        //initializing lists
        ArrayList<Integer> A=new ArrayList<>();//list to save time of family members
        ArrayList<Integer> B=new ArrayList<>();//empty list to pass as value to initial State
        for (int i=1;i<=n; i++) A.add(Integer.parseInt(args[i]));
        Collections.sort(A);

        //initiallizing class objects
        State initial=new State(A,B,0,true);
        Solver solver=new Solver(Time);

        //solution starts
        State terminal=solver.Astar(initial);

        //results
        if(terminal==null) System.out.println("No solution found");
        else
        {    
            System.out.println("Terminal State reached. Elapsed Time= " + terminal.getElapsedTime());
            System.out.println("Route to initial:");
            solver.findPath(terminal).forEach(State::print);
        }
        long endTime =System.currentTimeMillis();
        System.out.println("Execution Time: " + (endTime-startTime) + "ms.");
    }
}
    