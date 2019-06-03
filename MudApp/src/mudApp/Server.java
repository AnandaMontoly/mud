package mudApp;
import java.io.*;
import java.util.*;
import java.net.*;


public class Server {
	//this is all of the people who are inside of the server
	//https://www.tutorialspoint.com/java/util/java_util_vector.htm
	static Vector<ClientHandler> peopleHandler = new Vector<>();
	//this is the person counter.
	static int personCounter = 0;
	//list of rooms where people can be
	static HashMap<String, Place> places = new HashMap<>();
	
	public static void initPlaces() {
		//mud locations.
		places.put("entrance", new Place("entrance","The entrance to Smith College"));
		places.get("entrance").addDoor(new Doorway("s","ford","The door to Ford."));
		places.get("entrance").addDoor(new Doorway("n","gillett","To Gillett. A house on campus."));
		places.get("entrance").addDoor(new Doorway("w","campusCenter","The way to the campus center."));
		
		places.put("ford", new Place("ford","The Science Hall of Smith College."));
		places.get("ford").addDoor(new Doorway("n","entrance","Back to the entrance of Smith College."));
		
		places.put("gillett", new Place("gillett","The Vegetarian dining hall on campus."));
		places.get("gillett").addDoor(new Doorway("s","entrance","The way back to the entrance gate at Smith College."));
		places.get("gillett").addDoor(new Doorway("sw","campusCenter","If you want to go to the Campus Center."));
		
		
		places.put("campusCenter", new Place("campusCenter","The campus center, where someone can go to the cafe or meet up with friends."));
		places.get("campusCenter").addDoor(new Doorway("e","entrance","The way back to the entrance to Smith."));
		places.get("campusCenter").addDoor(new Doorway("ne","gillett","To a vegetarian dining hall."));

	}
	//this is where listening and the like will actually happen
	
	public static void main(String[] args) throws IOException{
		//server is listening on port 6000
		ServerSocket serveSock = new ServerSocket(6432);
		
		System.out.println("Server listening on port="+serveSock.getLocalPort()+"...");
		initPlaces();
		//run forever

		try {
			while (true) {
				Socket plainSocket = null;
				//this is where your actual code goes
				//the first bit is made for accepting a new user and 
				plainSocket = serveSock.accept();
				System.out.println("just accepted a new user.");
				//a new client has just come onto the server.
				//getting their input and output information
				DataInputStream input = new DataInputStream(plainSocket.getInputStream());
				DataOutputStream output = new DataOutputStream(plainSocket.getOutputStream());
				//new handler must then be made. this is where all of the commands go through.
				ClientHandler newPlayerHandler = new ClientHandler(plainSocket, input,output);
				//the client handler implements https://docs.oracle.com/javase/7/docs/api/java/lang/Runnable.html this. 
				//it means that threads happen here.
				Thread newPlayerThread = new Thread((Runnable) newPlayerHandler);
				System.out.println("adding this person to client list.");
				peopleHandler.add(newPlayerHandler);
				//starting up the thread and letting it do its wild and wacky thing.
				newPlayerThread.start();
				personCounter++;
			}//end while
		}catch (Exception e) {
			e.printStackTrace();
		}

	}
	public void out(Object words) {
		System.out.println(words);
	}

	
}
