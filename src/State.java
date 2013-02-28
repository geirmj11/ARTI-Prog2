import java.util.*;

public class State
{
	static final int width = 7;
	static final int height = 6;
	
	static final long fullBoard = Long.parseLong("3FFFFFFFFFF", 16);

	public State(long white,long red, boolean whiteTurn) {
		this.white = white;
		this.red = red;
		this.whiteTurn = whiteTurn;
	}
	
	boolean whiteTurn;
	long white;
	long red;
	public int Score;
	
	long getOccupied() {
		return white | red;
	}
	
	int getRow(int i) {
		Long ret = (getOccupied() >> width * i ) & (long)0x7F;
		return ret.intValue();
		// mask out other then the intended row.
		// with the first 6 bits set.
	}
	boolean occupied(int i, int j) {
		return ((getRow(i) >> j) & 1) == 1;
	}
	int getTop(int i) {
		long occupied = getOccupied();
		
		for (int x = 0; x < height; x++) {
			if (((occupied >> i) & 1)  == 0)
				return x;
			occupied = occupied >> width;
		}
		return -1;
	}
	
	public int TerminalState() {
		if (getOccupied() == fullBoard) // All Posistions are Occupied
			return 3;
		if (colorWin(red))
			return 2;
		if (colorWin(white))
			return 1;
		return 0;
	}
	
	boolean colorWin(long color) {
		// Check if there is a 4 consecutive piese in diagonal pattern.
		// example: 001010100
		//          001		
		//           010
		//            100
		// For both left and right leaned pieses.
		// color & ((color >> 5) & ~1) & ((color >> 9) & ~3) & ((color >> 12) & ~7)
		// example: 100010001
		//             100		
		//            010
		//           001
		// color & ((color >> 7) & ~7) & ((color >> 15) & ~3) & ((color >> 24) & ~3)
		long tColor = color;
		long mask = 127;
		for (int i = 0; i < 3; i++){
			//Diagonal left
			if (((tColor & (tColor >> 8) & (tColor >> 16)  & (tColor >> 24)) & 7) > 0)
				return true;
			//System.out.println(tColor & (tColor >> 6) & (tColor >> 12) & (tColor >> 18));
			//Diagonal right
			if (((tColor & (tColor >> 6) & (tColor >> 12) & (tColor >> 18)) & 112) > 0)
				return true;
			//Column 
			if (((tColor & (tColor >> 7) & (tColor >> 14) & (tColor >> 21)) & mask) > 0)
				return true;
				
			tColor = tColor >> width;
		}
		
		tColor = color;
		for (int i = 0; i < width; i++){
			//Row
			if ((tColor & (long)15) == 15 || 
			    (tColor & (long)30) == 30 || 
			    (tColor & (long)60) == 60 ||
			    (tColor & (long)120) == 120)
				return true;
			tColor = tColor >> width;
		}
		return false;
	}
	
	public boolean RedWin() {
		return colorWin(red);
	}
	
	public boolean WhiteWin() {
		return colorWin(white);
	}
	
	ArrayList<Node> legalMoves() {
		ArrayList<Node> l = new ArrayList<Node>();
		for (int i = 0; i < width; i++)
		{
			int top = getTop(i);
			if (top != -1){
				long insert = (1 << ((top*width)+i));
				if (whiteTurn) 
					l.add(new Node(white + insert,red,!whiteTurn));
				else 
					l.add(new Node(white,red + insert,!whiteTurn));	
			}
		}
		return l;
	}	
	
	@Override
    public int hashCode(){
		Long ret = (long)19 * white + (long)31 * red;
        return ret.intValue();
    }
}
