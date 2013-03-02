import java.util.*;

public class AgentBauer implements Agent
{
	private Random random = new Random();
	private String role;
	//private int myRole;
	private long turnStarted;
	private int playClock;
	private boolean myTurn;
	private State currentState; 
	private int[] curDepthLevel;
	private int maxDepth = 43;

	/*
		init(String role, int playClock) is called once before you have to select the first action. Use it to initialize the agent. role is either "WHITE" or "RED" and playClock is the number of seconds after which nextAction must return.
	*/
    public void init(String role, int playClock) {
		System.out.println("Start init");
		this.role = role;
		this.playClock = playClock;
		this.curDepthLevel = new int[7];
		System.out.println("Play clock" + playClock);
		
		myTurn = role.equals("WHITE");
		currentState = new State(0,0,myTurn);
        
		for(State i : currentState.legalMoves()) 
			System.out.print(heuristic(i) + " ");
		System.out.println();
		
        //for(State i : currentState.legalMoves()) {
		//    for (State j : i.legalMoves())
		//	    System.out.print(heuristic(j) + " ");
		//	System.out.println();
		//}
		
		System.out.println("Done init");
    }

    public int heuristic(State state){
        int Value = 0;
		ArrayList<Integer> comobs =  state.getCombos();
		//System.out.println("combos: " + comobs.size());
        for(int i : comobs) {
            //System.out.print(i);
            if(i > 0xF) {//white
                i = i >> 4;   
				if (role.equals("WHITE")){
					if(i == 0xF) return Integer.MAX_VALUE;
					if(i == 1 || i == 2 || i == 4 || i == 8) 			Value += 1;
					else if(i == 7 || i == 11 || i == 13 || i == 14)	Value += 100;
					else 												Value += 10;
				}		
				else {
					if(i == 0xF) return Integer.MIN_VALUE;
					if(i == 1 || i == 2 || i == 4 || i == 8) 			Value -= 1;
					else if(i == 7 || i == 11 || i == 13 || i == 14)	Value -= 100;
					else 												Value -= 10;
				}
			}
            else {//red
				if (role.equals("RED")){
					if(i == 0xF) return Integer.MAX_VALUE;
					if(i == 1 || i == 2 || i == 4 || i == 8) 			Value += 1;
					else if(i == 7 || i == 11 || i == 13 || i == 14)	Value += 100;
					else 												Value += 10;
				}		
				else {
					if(i == 0xF) return Integer.MIN_VALUE;
					if(i == 1 || i == 2 || i == 4 || i == 8) 			Value -= 1;
					else if(i == 7 || i == 11 || i == 13 || i == 14)	Value -= 100;
					else 												Value -= 10;
				}
			}
		}
		return Value;
	}
	// lastDrop is 0 for the first call of nextAction (no action has been executed),
	// otherwise it is a number n with 0<n<8 indicating the column that the last piece was dropped in by the player whose turn it was
 
    public String nextAction(int lastDrop) {
		turnStarted = System.nanoTime();
		maxDepth--;
        System.out.println("-------------------->  Enemy dropped at " +lastDrop);

		if(lastDrop != 0)
            currentState.addMove(lastDrop,myTurn); 
		System.out.println("Red: "+ (long)currentState.red + " White: " +currentState.white );
		
		myTurn = !myTurn; 		
        if(!myTurn){
			int drop = whereToDrop();
			System.out.println("------------------------->   Droping to " + drop);
			return "(DROP " + drop + ")";
		}
		else
		    return "NOOP";
	}
	
	/// Returns the column which is best to drop inn.
	int whereToDrop(){
		//System.out.println("hasTime: " + hasTime());
		for (int deep = 1; hasTime(); deep++){
			calculate(deep,currentState);
			System.out.println("depth: " + deep);
			if (deep > maxDepth)
				break;
		}
		int best = 0;
		for (int i = 0; i < 7; i++ ) {
			System.out.println( i +" " + curDepthLevel[i]);
			if (curDepthLevel[i] > curDepthLevel[best]) 
				best = i;
		
		}
		return best + 1;
	}
	
	// Call: value = AlphaBeta( MaxDepth, s, -INF, INF )
	void calculate(int depth, State state) {
		int bestIndex = 0;
		int i = 0;
		
		int[] bestValues = new int[7];
		Integer beta = Integer.MAX_VALUE; //Call by referance :)
		Integer alpha = Integer.MIN_VALUE;
		for ( State s : state.legalMoves()) {
			if (s == null) {
				bestValues[i++] = Integer.MIN_VALUE;
				continue;
			}
			bestValues[i++] = alphaBeta( depth-1, s, -beta, -alpha );
			
		}
		if (hasTime()){
			//We did not run out of time :)
			for (int k = 0; k < 7; k++ ) {// and can safely use these values.
				System.out.println( k +" " + bestValues[k]);
				curDepthLevel[k] = bestValues[k];
			}
		}
	}
	
	// Call: value = AlphaBeta( MaxDepth, s, -INF, INF )
	int alphaBeta(int depth, State state, Integer alpha, Integer beta) {
		if (depth <= 0 || state.terminalState() != 0 || !hasTime()){
			//System.out.println("Heuristics: " + heuristics + " size " + state.getCombos().size() + " ocupied " + state.getOccupied());
			return heuristic(state);
		}
		int bestValue = Integer.MIN_VALUE;	
		for ( State s : state.legalMoves()) {
			if (s == null) continue;
			int value = -alphaBeta( depth - 1, s, -beta, -alpha );
			//(Note: switch and negate bounds)
			if (value > bestValue) bestValue = value;
			if ( bestValue > alpha ) {
				alpha = bestValue;
				//System.out.println("New Alpha value "+alpha+" Heuristics: " + heuristics + " size " + state.getCombos().size() + " ocupied " + state.getOccupied());
				if ( alpha >= beta ) {
					//System.out.println("Prooning!!");
					break;
				}
			}
		}
		return bestValue;
	}
	//returns in ms.
	//return example 5 sec is 500.
	boolean hasTime()
	{
		return (System.nanoTime() - turnStarted) / 10000000 <= playClock * 100  - 50;
	}
		
}
