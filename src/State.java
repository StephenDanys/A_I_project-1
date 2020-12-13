import java.util.ArrayList;
import java.util.Collections;

public class State implements Comparable<State> {

    private int elapsedTime; 
    private ArrayList<Integer> arA,arB;// a is right, b is left
    private boolean torchAtA; //true= lamb to the right,else lamb to the left
    private int priority; //the state with the smallest priority gets examined 1st
    private State father=null; //state from which this was created 

    public State(){
        arA=new ArrayList<Integer>();
        arB=new ArrayList<>();
        elapsedTime=0;
        torchAtA=true;
    }

    public State(ArrayList<Integer> A, ArrayList<Integer> B, int etime, boolean torchAtA)
    {
        this.arA=new ArrayList<Integer>();
        arA.addAll(A);
        this.arB=new ArrayList<Integer>();
        arB.addAll(B);
        this.elapsedTime=etime;
        this.torchAtA=torchAtA;
        this.priority=getPriority();
    }

    /** Moves a couple from A to B
        Also checks for mistakes 
    */
    public boolean fromAtoB(ArrayList<Integer> persons)
    {
        if(  this.arA.removeAll(persons))
        {
            this.arB.addAll(persons);
            return true;
        }
        return false;
        
    }
    //transpots a person from b to a 
    public boolean fromBtoA(int person)
    {
        if(person>0)
        {
            this.arA.add(person);
            this.arB.remove((Object) person);
            return true;
        }
        return false;
    }
    
    
    public ArrayList<State> getChildren()
    {
        ArrayList<State> children=new ArrayList<>();//list to return
        
        /**
            *If lamb is at right, we need to produce every possibl couple
            *Then we check if each couple can indeed pass the bridge, with fromAtoB
         */
        if(torchAtA) {

            ArrayList<ArrayList<Integer>> couplesToPass=new ArrayList<ArrayList<Integer>>();
            //this double for loop produces possible couples 
            for (int personA : this.arA) {
                for (int personB : this.arA) {
                    if (personA < personB)
                    {
                        ArrayList<Integer> couple = new ArrayList<Integer>();
                        couple.add(personA);
                        couple.add(personB);
                        if(!couplesToPass.contains(couple))
                        couplesToPass.add(couple);
                    }
                }

            }

            for(ArrayList<Integer> c:couplesToPass)
            {
                State child=new State(arA,arB,elapsedTime,torchAtA);
                if(child.fromAtoB(c)); //testing couples and creating kids
                {
                    child.elapsedTime=child.elapsedTime+Math.max(c.get(0),c.get(1));
                    child.torchAtA=false;
                    child.priority=child.getPriority();
                    child.setFather(this);
                    children.add(child);
                }
            }
        }
        else //lamb in B, Left side
        {
            for(int person:this.arB)
            {
                State child=new State(this.arA,this.arB,this.elapsedTime,this.torchAtA);

                if(child.fromBtoA(person))
                {
                    child.elapsedTime+=person;
                    child.torchAtA=true;
                    child.priority=child.getPriority();
                    child.setFather(this);
                    children.add(child);
                }
            }
        }
        return children;

    }


    public boolean isTerminal()
    {
        if(this.arA.isEmpty()) return true;
        return false;
    }

    public void print()
    {   System.out.println("----------------");
        System.out.println("A:");
        for(int a:arA) System.out.print("\t "+a);
        System.out.println("\nB:");
        for(int a:arB) System.out.print("\t "+a);
        System.out.println("\nelapsed time: " + this.elapsedTime) ;
        //System.out.println("\npriority: " + this.priority) ;
        System.out.println("----------------");


    }
    //returns the max element of an arraylist used to calculate the heuristic of a pass
    public int getMax(ArrayList<Integer> ar)
    {   if(ar.isEmpty()) return 0;
        int max=-5;
        for(int i:ar)
            if(i>=max) max=i;
            return max;
    }

    public int getElapsedTime(){
        return this.elapsedTime;
    }

    public State getFather(){
        return this.father;
    }

    public void setFather(State f){
        this.father=f;
    }

    public int getPriority()
    {
        return getElapsedTime()+getHeuristic();

    }
    /*relaxing that only 2 people can pass the bridge at any time we get the heuristic of distance meaning if all
    people could pass in a single wave what would the added cost be*/
    public int getHeuristic()
    {   if(this.torchAtA)
        return getMax(this.arA);
        else
            return Collections.min(this.arB)+getMax(this.arA);

    }
    @Override
    public int compareTo(State that)
    {
        return this.getPriority()-that.getPriority();
    }
}