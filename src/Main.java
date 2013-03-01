import java.io.IOException;

public class Main {
	
	/**
	 * starts the game player and waits for messages from the game master <br>
	 * Command line options: [port]
	 */
	public static void main(String[] args){
		State n = new State(0,1,true);
		//n.addMove(64,false);
		//for (int i = 0; i < 7; i++)
		//	System.out.println(i+1 + " "+ n.getTop(i));
		
		
		//n = new State(1,2,true);
		//for (State s : n.legalMoves())
		//	System.out.println(s.getOccupied());
		
		//System.out.print("Terminal state: " );
		//switch (n.TerminalState()){
		//	case 3: // Full Board
		//		System.out.print("Full Board");
		//		System.out.print("Full Board");
		//	break;
		//	
		//	case 2: // red
		//		System.out.print("Red Won!");
		//	break;
		//	
		//	case 1: // white
		//		System.out.print("White Won!");
		//	break;
		//	
		//	case 0: // None
		//		System.out.print("Not a terminal state.");
		//	break;
		//}
		
		try{
			// TODO: put in your agent here
			Agent agent = new AgentBauer();
        
			int port=4001;
			if(args.length>=1){
				port=Integer.parseInt(args[0]);
			}
			GamePlayer gp=new GamePlayer(port, agent);
			gp.waitForExit();
		}catch(Exception ex){
			ex.printStackTrace();
			System.exit(-1);
		}
	}
}
