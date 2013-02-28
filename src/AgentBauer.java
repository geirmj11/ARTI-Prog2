import java.util.Collection;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AgentBauer implements Agent
{
	private Random random = new Random();
	private String role;
	//private int myRole;
	private int playclock;
	private boolean myTurn;
	private State currentState;
	
	/*
		init(String role, int playclock) is called once before you have to select the first action. Use it to initialize the agent. role is either "WHITE" or "RED" and playclock is the number of seconds after which nextAction must return.
	*/
    public void init(String role, int playclock) {
		this.role = role;
		this.playclock = playclock;
		currentState = new State(0,0);
		myTurn = !role.equals("WHITE");
		//if(!role.equals("WHITE") myRole = 1;
		//else myRole = 2;
		// TODO: add your own initialization code here
    }

    public int heuristic(){
        //þarf að skoða stateið og gefa því einkun, meta einkunina út frá 
/*        Extend 3-out-of-4 heuristic to n-out-of-4 for n ≤ 3
Award weighted points based on the value of n
Score(p,G) = 100(n3) + 10(n2) + 1(n1)
ni is the number of i-out-of-4 winning lines for player p on game board G
  Five 1-out-of-4 winning lines (n1 = 5)
Five 2-out-of-4 winning lines (n2 = 5)
Score = 100(0) + 10(5) + 1(5) = 55
Compare players’ scores
Utility(p,G)
          = Score(p,G) – Score(opponent(p),G)
  */  
        return 0;
    }
	// lastDrop is 0 for the first call of nextAction (no action has been executed),
	// otherwise it is a number n with 0<n<8 indicating the column that the last piece was dropped in by the player whose turn it was
 
    public String nextAction(int lastDrop) { 
		// TODO: 1. update your internal world model according to the action that was just executed

		// halda utanum stateið sem er í gangi akkurat núna, updatea internal stateið á einhvern hátt...
        if(lastDrop != 0)
            currentState.addMove(lastDrop,myTurn);
    
        int drop = alphaBeta();
        myTurn = !myTurn;        
        if(!myTurn) 
            return "(DROP " + drop + ")";
		else
		    return "NOOP";
		// TODO: 2. run alpha-beta search to determine the best move

	}
	private int alphaBeta(node, depth, alpha, beta, player){
        if(depth == 0 || node.TerminalState());
            //return the heuristic value of the node
        
        bestValue = -INF;
        for (for all successors s’ of s) {
            value = -AlphaBeta(depth–1,s’,- beta,-alpha);  
            // Alpha - A lower­bound on best that a player to move can achieve
            // Beta - An upper­bound on the best that a player can achieve – (without opponent playing a different line up the tree)
        
	}

}
