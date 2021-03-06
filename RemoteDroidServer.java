package test.java;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket; 
public class RemoteDroidServer { 
    	private static ServerSocket server = null; 
	private static Socket client = null; 
	private static BufferedReader in = null; 
	private static String line; 
	private static boolean isConnected=true; 
	private static Robot robot; 
	private static final int SERVER_PORT = 3362;
	@SuppressWarnings("unused")
	private static boolean leftpressed;
	@SuppressWarnings("unused")     
	private static boolean rightpressed; 
  
	public static void main(String[] args) { 
		leftpressed = false; 
		rightpressed = false; 
  
	    try{ 
	    		robot = new Robot(); 
			server = new ServerSocket(SERVER_PORT); //Create a server socket on port 8998 
			client = server.accept(); //Listens for a connection to be made to this socket and accepts it 
			in = new BufferedReader(new InputStreamReader(client.getInputStream())); //the input stream where data will come from client 
		}catch (IOException e) { 
			System.out.println("Error in opening Socket"+e); 
			System.exit(-1); 
		}catch (AWTException e) { 
			System.out.println("Error in creating robot instance"); 
			System.exit(-1); 
		} 
			 
		//read input from client while it is connected 
	    while(isConnected){ 
	        try{ 
			line = in.readLine(); //read input from client 
			System.out.println(line); //print whatever we get from client 
			  
			//if user clicks on next 
			if(line.equalsIgnoreCase("right")){ 
				//Simulate press and release of key 'n' 
				robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
				robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
			} 
		 	//if user clicks on previous 
			else if(line.equalsIgnoreCase("left")){ 
				//Simulate press and release of key 'p' 
				robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
				robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
				        	 
			} 
			//if user clicks on play/pause 
			else if(  line.equalsIgnoreCase("space")){ 
				//Simulate press and release of spacebar 
				robot.keyPress(KeyEvent.VK_SPACE); 
				robot.keyRelease(KeyEvent.VK_SPACE); 
			} 
			//input will come in x,y format if user moves mouse on mousepad 
			else if(line.contains(",")){ 
				float movex=Float.parseFloat(line.split(",")[0]);//extract movement in x direction 
				float movey=Float.parseFloat(line.split(",")[1]);//extract movement in y direction 
				Point point = MouseInfo.getPointerInfo().getLocation(); //Get current mouse position 
				float nowx=point.x; 
				float nowy=point.y; 
				robot.mouseMove((int)(nowx+movex),(int)(nowy+ movey));//Move mouse pointer to new location 
			} 
			//if user taps on mousepad to simulate a left click 
			else if(line.contains("left ")){ 
				//Simulate press and release of mouse button 1(makes sure correct button is pressed based on user's dexterity) 
				robot.mousePress(InputEvent.BUTTON1_DOWN_MASK); 
				robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK); 
			} 
			else if(line.contains("tab")){
				robot.keyPress(KeyEvent.VK_TAB); 
				robot.keyRelease(KeyEvent.VK_TAB);  
			}
			//Exit if user ends the connection 
			else if(line.equalsIgnoreCase("exit")){ 
				isConnected=false; 
			 	//Close server and client socket 
				server.close(); 
				client.close(); 
			} 
	        } catch (IOException e) { 
				System.out.println("Read failed"); 
				System.exit(-1); 
	        } 
      	}  
	} 
} 
 
