import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class Solver {
    private MinPQ<State> queue;
    private int time;
    
    //constuctor
    public Solver(int time){
        this.time=time;
    }

    //method for finding the path from solution to initial state
    public List<State> findPath(State solution)
    {
        LinkedList<State> ll=new LinkedList<State>();
        while (solution!=null)
        {
            ll.addFirst(solution);
            solution=solution.getFather();
        }
        return ll;
    }

    //implemetation of Astar using a minPriorityQueue
    public State Astar(State initialState)
    {
        this.queue =new MinPQ<State>(); //pq object
        if(initialState.isTerminal()) return initialState; //checking initial state 

        queue.insert(initialState);
        while(!queue.isEmpty())
        {
            State current= queue.delMin(); //examine State with least prioroty
            if (current.getElapsedTime()> this.time) {
               System.out.println("Time Limit Expired");
               return null; 
            } 
            if(current.isTerminal()) return current;
            for(State s:current.getChildren())
            {
                queue.insert(s); //insert children in queue 
            }
        }
        return null;
    }
}