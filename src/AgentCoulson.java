import java.util.Collection;
import java.util.ArrayList;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.lang.System;

public class AgentCoulson implements Agent
{
    private boolean whiteTurn;
      
    public void init(Collection<String> percepts) {
        Pattern perceptNamePattern = Pattern.compile("\\(\\s*([^\\s]+).*");
        for (String percept:percepts) {
			Matcher perceptNameMatcher = perceptNamePattern.matcher(percept);
			if (perceptNameMatcher.matches()) {
				String perceptName = perceptNameMatcher.group(1);
				if (perceptName.equals("HOME")) {
					Matcher m = Pattern.compile("\\(\\s*HOME\\s+([0-9]+)\\s+([0-9]+)\\s*\\)").matcher(percept);
					if (m.matches()) {
						this.home.x = Integer.parseInt(m.group(1));
					    this.home.y = Integer.parseInt(m.group(2));
					    this.x = this.home.x;
					    this.y = this.home.y;
						//System.out.println(":::::::::::::  home at " + this.x + "," + this.y);
					}
				} 
				else if (perceptName.equals("ORIENTATION")) {
			    	Matcher m = Pattern.compile("\\(\\s*ORIENTATION\\s+([^\\s]+)\\s*\\)").matcher(percept);
			    	if (m.matches()) {
			    	    if(m.group(1) == "NORTH") this.orientation = 0;
			            if(m.group(1) == "EAST")  this.orientation = 1;
			            if(m.group(1) == "SOUTH") this.orientation = 2;
			            if(m.group(1) == "WEST")  this.orientation = 3;
						//System.out.println("::::::::::::: orientation " + m.group(1) + " " +  this.orientation);
					}
				} 
				else if (perceptName.equals("SIZE")){
				    Matcher m = Pattern.compile("\\(\\s*SIZE\\s+([0-9]+)\\s+([0-9]+)\\s*\\)").matcher(percept);
				    if(m.matches()) {
				        this.size.x = Integer.parseInt(m.group(1));
					    this.size.y = Integer.parseInt(m.group(2));
    					//System.out.println(":::::::::::::  size is " + this.size.x + "," + this.size.y);
					}
				}
				else if(perceptName.equals("AT")) {
			        Matcher m = Pattern.compile("\\(\\s*AT\\s*DIRT\\s+([0-9]+)\\s+([0-9]+)\\s*\\)").matcher(percept);
			        if (m.matches()) {
			            System.out.println(":::::::::::::  Dirt at " + m.group(1) + "," + m.group(2));
			            dirt.add(new Point(Integer.parseInt(m.group(1)),Integer.parseInt(m.group(2))));
   			        }
   			        Matcher n = Pattern.compile("\\(\\s*AT\\s*OBSTACLE\\s+([0-9]+)\\s+([0-9]+)\\s*\\)").matcher(percept);
   			        if(n.matches()) {
   			            //System.out.println(":::::::::::::  OBSTACLE at " + n.group(1) + "," + n.group(2));
   			            obstacles.add(new Point(Integer.parseInt(n.group(1)),Integer.parseInt(n.group(2))));
   			        }
			    }
				else {
					System.out.println("other percept:" + percept);
				}
			} else {
				System.err.println("strange percept that does not match pattern: " + percept);
			}
		}
		//this.path = testSearch(this.home, this.size, this.dirt, this.obstacles);
		long timeComp = System.nanoTime();
//		this.path = AstarSearch.getPath(this.home, this.size, this.dirt, this.obstacles);
//		this.path = DepthSearch.getPath(this.home, this.size, this.dirt, this.obstacles);
//		this.path = UniformSearch.getPath(this.home, this.size, this.dirt, this.obstacles);
		this.path = AstarSearch.getPath(this.home, this.size, this.dirt, this.obstacles);
		timeComp = System.nanoTime() - timeComp;
		System.out.println("Time complexity: " + timeComp);
	}

    public String nextAction(Collection<String> percepts) {
		//EF dirt á staðnum, returna suck. 
        //Annars poppa næsta stak af listanum og stefna á þann punkt.
        //Ef listinn tómur, slökkva.
        //String[] actions = { "TURN_ON", "TURN_OFF", "TURN_RIGHT", "TURN_LEFT", "GO", "SUCK" };
        if(!power) {
            power = true;
            return "TURN_ON";
        }
	    if(path.empty()) {
	        power = false;
    	    return "TURN_OFF";
    	}
        int spot = dirt.indexOf(new Point(this.x,this.y));
        if(spot >= 0){
            dirt.remove(spot);
            return "SUCK";
        }
   
	    Point dest = path.peek();
        if(dest.x == this.x){
	        if(dest.y > this.y) return pickAction(0, 1, dest);
	        else return pickAction(2, 1, dest);
	    }
	    else{
	        if(dest.x > this.x) return pickAction(1, 0, dest);
	        else return pickAction(3,0,dest);
	    }   
	}    
	public String pickAction(int correctOrient, int xy, Point dest) {
	    if(orientation == correctOrient){
	        if(xy == 0)this.x = dest.x;
	        else this.y= dest.y;
	        path.pop(); 
	        return "GO";
	    }
	    else {
	        if(((orientation+1)%4) == correctOrient) {
    	        orientation = (orientation+1)%4;
	            return "TURN_RIGHT";
	        }
	        else {
	            orientation = (orientation+3)%4;
	            return "TURN_LEFT";
	        }
	    }
	}
}
