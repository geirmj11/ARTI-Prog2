import java.util.Collection;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AgentBauer implements Agent
{
	private Random random = new Random();
	private String role;
	//private int myRole;
	private long turnStarted;
	private int playClock;
	private boolean myTurn;
	private State currentState; 

	/*
		init(String role, int playClock) is called once before you have to select the first action. Use it to initialize the agent. role is either "WHITE" or "RED" and playClock is the number of seconds after which nextAction must return.
	*/
    public void init(String role, int playClock) {
		System.out.println("Start init");
		this.role = role;
		this.playClock = playClock;
		System.out.println("Play clock" + playClock);
		
		myTurn = !role.equals("WHITE");
		currentState = new State(0,0,myTurn);
		
		System.out.println("Done init");
    }

    public int heuristic(State state){
        int Value = 0;
        boolean isWhite;
        for(int i : state.getCombos()) {
            if(i > 0xF){          //white
                i = i >> 4;   
                isWhite = true;
            }
            else {                //red
                isWhite = false;
            }
            
            if(i == 0xF){
                if(this.role == "WHITE" && isWhite == true)
                    return Integer.MAX_VALUE;
                else
                    return Integer.MIN_VALUE;
            }
            if(i == 1 || i == 2 || i == 4 || i == 8){
                if(isWhite == true) Value++;
                else Value--;
            }
            else if(i == 7 || i == 11 || i == 13 || i == 14){
                if(isWhite == true) Value += 100;
                else Value -= 100;
            }
            else {
                if(isWhite == true) Value += 10;
                else Value -= 10;
            }
                           
        }
        if (this.role == "WHITE") return Value;
        else return -Value;
    }
	// lastDrop is 0 for the first call of nextAction (no action has been executed),
	// otherwise it is a number n with 0<n<8 indicating the column that the last piece was dropped in by the player whose turn it was
 
    public String nextAction(int lastDrop) { 
		turnStarted = System.nanoTime();
        System.out.println("Was dropped at " +lastDrop);

		if(lastDrop != 0)
            currentState.addMove(lastDrop,myTurn);

		switch (currentState.terminalState()){
			case 3: // Full Board
				System.out.print("Full Board");
			break;

			case 2: // red
				System.out.println("Red Won!");
			break;

			case 1: // white
				System.out.println("White Won!");
			break;

			case 0: // None
				System.out.println("Not a terminal state.");
			break;
		}

		myTurn = !myTurn;  
        if(!myTurn) {
			int drop = whereToDrop();
			System.out.println("Droping to " + drop);
			return "(DROP " + drop + ")";
		}
		else
		    return "NOOP";
	}
	
	/// Returns the column which is best to drop inn.
	int whereToDrop(){
		int index = 0;
		//System.out.println("hasTime: " + hasTime());
		for (int deep = 1; hasTime(); deep++){
			index = indexOfBest(deep,currentState);
			//System.out.println("hasTime: " + hasTime());
			System.out.println("depth: " + deep);
		}
		return index + 1;
	}
	
	// Call: value = AlphaBeta( MaxDepth, s, -INF, INF )
	int indexOfBest(int depth, State state) {
		int bestValue = Integer.MIN_VALUE;	
		int bestIndex = 0;
		int i = 0;
		
		Integer beta = Integer.MIN_VALUE; //Call by referance :)
		Integer alpha = Integer.MAX_VALUE;
		for ( State s : state.legalMoves()) {
			i++;
			if (s == null)
				continue;
			int value = -alphaBeta( depth, s, -beta, -alpha );
			
			System.out.println("Value "+value+" index "+i);
			bestValue = Math.max(value, bestValue);
			if ( bestValue > alpha ) {
				alpha = bestValue;
				if ( alpha >= beta ) break;
				bestIndex = i;
			}
		}
		return bestIndex;
	}
	
	// Call: value = AlphaBeta( MaxDepth, s, -INF, INF )
	int alphaBeta(int depth, State state, int alpha, int beta) {
		if (depth <= 0 || state.terminalState() != 0 || !hasTime())
			return heuristic(state);
		int bestValue = Integer.MIN_VALUE;	
		for ( State s : state.legalMoves()) {
			if (s == null)
				continue;
			int value = -alphaBeta( depth - 1, s, -beta, -alpha );
			//(Note: switch and negate bounds)
			bestValue = Math.max(value, bestValue);
			if ( bestValue > alpha ) {
				alpha = bestValue; //(adjust the lower bound)
				if ( alpha >= beta ) break; //(beta cutoff)
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
