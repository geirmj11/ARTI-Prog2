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
	private int playclock;
	private boolean myTurn;
	private State currentState; 

	/*
		init(String role, int playclock) is called once before you have to select the first action. Use it to initialize the agent. role is either "WHITE" or "RED" and playclock is the number of seconds after which nextAction must return.
	*/
    public void init(String role, int playclock) {
		System.out.println("Start init");
		this.role = role;
		this.playclock = playclock;
		System.out.println("Play clock" + playclock);
		myTurn = !role.equals("WHITE");
		currentState = new State(0,0,myTurn);
		System.out.println("Done init");
		//if(!role.equals("WHITE") myRole = 1;
		//else myRole = 2;
		// TODO: add your own initialization code here
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
		// TODO: 1. update your internal world model according to the action that was just executed
		turnStarted = System.nanoTime();
		// halda utanum stateið sem er í gangi akkurat núna, updatea internal stateið á einhvern hátt...
        System.out.println("Was dropped at " +lastDrop);

		if(lastDrop != 0)
            currentState.addMove(lastDrop,myTurn);

		if (currentState.terminalState() != 0)
		    return "NOOP";

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
			int drop = alphaBeta();
			System.out.println("Droping to " + drop);
			return "(DROP " + drop + ")";
		}
		else
		    return "NOOP";
		// TODO: 2. run alpha-beta search to determine the best move

	}

	/// Returns the column which is best to drop inn.
	int alphaBeta(){
		int[] moveVal = new int[7]; // 7 posible moves.
		State[] moves = new State[7];

		int index = 0;
		int deep = 1;
		for (; (System.nanoTime() - turnStarted) / 100000000 < playclock * 10  - 5; deep++){
			index = 0;
			for (State s : currentState.legalMoves()){
				moves[index] = s;
				if (s != null)
					moveVal[index++] = alphaBeta(deep,s,Integer.MIN_VALUE,Integer.MAX_VALUE);
				else
					moveVal[index++] = Integer.MIN_VALUE;
			}
		}
		System.out.println(deep);
		int maxValue = Integer.MIN_VALUE;
		index = 0;
		for (int i = 0; i < 7; i++){
			if (moveVal[i] > maxValue){
				index = i;
				maxValue = moveVal[i];
			}
		}
		currentState = moves[index];
		return index + 1;
	}

	// Call: value = AlphaBeta( MaxDepth, s, -INF, INF )
	int alphaBeta(int depth, State state, int alpha, int beta) {
		if (depth > 0 || state.terminalState() != 0)
			return heuristic(state);
		int bestValue = Integer.MIN_VALUE;	
		for ( State s : state.legalMoves()) {
			if (s == null)
				continue;
			int value = - alphaBeta( depth - 1, s, -beta, -alpha );
			//(Note: switch and negate bounds)
			bestValue = Math.max(value, bestValue);
			if ( bestValue > alpha ) {
				alpha = bestValue; //(adjust the lower bound)
				if ( alpha >= beta ) break; //(beta cutoff)
			}
		}
		return bestValue;
	}
}
