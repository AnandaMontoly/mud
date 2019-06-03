package mudApp;
import java.io.*;
import java.util.*;
import java.net.*;

public class ClientHandler implements Runnable{
	private Socket sock;
	private DataInputStream input;
	private DataOutputStream output;
	public String username;
	public String description;
	private volatile boolean loggedIn;
	private Place location;

	//constructor
	public ClientHandler(Socket sock,
			DataInputStream input, DataOutputStream output) {
		this.sock = sock;
		this.input = input;
		this.output = output;
		this.username = null;
		this.description = null;
		this.loggedIn = true;
		this.location = Server.places.get("entrance");
		this.location.addPerson(this);
	}
	
	public void exit() throws IOException {
		this.location.people.remove(this);
		this.loggedIn = false;
		Server.personCounter --;
		Server.peopleHandler.remove(this);
		this.sock.close();
	}
	public void entrance(Vector<ClientHandler> people) throws IOException{
		//people is the people who were previously in the room and who are also still in the room.
		this.output.writeUTF("You walk into a room and see that it looks like "+this.location.getDescription());
		if (people.size()>1){
			this.output.writeUTF("You see the following people in the room that you just entered.");
			for (ClientHandler friend: people) {
				if (friend != this) {
					//if we aren't talking about ourselves.
					this.output.writeUTF(friend.username+". They look like: "+friend.description);
					friend.output.writeUTF("The player "+username+" has entered the room.");
					friend.output.writeUTF("They look like: "+description);
					friend.output.flush();
				}//end if
				
			}//end for
		}//end if for size
	}//end method
	public void tell(String message, Vector<ClientHandler> people) throws IOException {
		for (ClientHandler friend: people) {
			if (friend != this) {
				friend.output.writeUTF(this.username+" : "+message);
				friend.output.flush();
			}
		}

	}
	public void walk(Doorway door) throws IOException {
		//prepare message for when the person leaves
		String leaveMessage = this.username+" has left the room.";
		//tell everyone that they've left
		tell(leaveMessage, this.location.people);
		//remove them from this room.
		this.location.people.remove(this);
		//add them to the room that they're going to
		Server.places.get(door.getTarget()).addPerson(this);
		//they know that they are in this room
		this.location = Server.places.get(door.getTarget());
		//send entrance message
		entrance(this.location.people);
	}
	
	public void run() {
		//the first time around you need to make sure that you read the first two things and make them the username and description.
		try {
			username = input.readUTF();
			description = input.readUTF();
			
			this.entrance(this.location.people);
			

		} catch (IOException e1) {
			e1.printStackTrace();
		}
		String received = null;
		
		while (this.loggedIn) {
			try {
				output.writeUTF("################################################################\n");
				received = input.readUTF();  
                System.out.println("Received: "+received);  
                //here is the body of processing commands.
				if (received.toLowerCase().equals("exit")) {
					this.exit();
					break;
				} else if (received.split(" ")[0].toUpperCase().equals("INFO")) {
					output.writeUTF("The room looks like this: "+ this.location.getDescription());
					output.writeUTF("You see the following people in the room.");
					for (ClientHandler person: this.location.people) {
						output.writeUTF(person.username+", who looks like: "+person.description);
					}
					continue;
				}else if (received.split(" ")[0].toUpperCase().equals("YELL")) {
					output.writeUTF("You breathe in to shout as loud as you can.");
					//https://stackoverflow.com/questions/5067942/what-is-the-best-way-to-extract-the-first-word-from-a-string-in-java
					received = "You hear someone yell "+ received.split(" ",2)[1];
					tell(received, Server.peopleHandler);
					output.writeUTF("Everyone across the world of SMUG hears you.");
					continue;
				}else if (received.split(" ")[0].toUpperCase().equals("DOORS")) {
					//THIS COMMAND PRINTS OUT ALL OF THE PLACES THAT YOU CAN SEE IN YOUR ROOM.
					output.writeUTF("You look around for doorways and see the following:");
					if (this.location.getVisibleExits().size()==0) {
						output.writeUTF("None, sorry!");
					}else {
						for (Doorway door: this.location.getVisibleExits().values()) {
							output.writeUTF(door.getName()+" : "+door.getDescription());
						}
					}
					continue;
					
				}else if (received.split(" ")[0].toUpperCase().equals("WALK")) {
					//if the person wants to walk.
					try {
						String whereTo = received.split(" ")[1];
						if (this.location.getVisibleExits().containsKey(whereTo.toLowerCase())) {
							walk(this.location.getVisibleExits().get(whereTo));
						}else {
							output.writeUTF("There's no exit called that around here.");
						}
					}catch (ArrayIndexOutOfBoundsException poop) {
						//if they don't write anything after walk.
						output.writeUTF("You need to write somewhere that you want to go!");
						continue;
					}
				
				}else {
					//if the person is talking normally
					tell(received, this.location.people);
				}
				
				
				
				
			} catch (IOException e) {
				System.out.println("Someone hung up on us!");
				
				e.printStackTrace();
				try {
					this.exit();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				// when we get an error, stop listening to that person to preserve the server for others.
				break;
			} 
			
		}//end while
		
	}//end run method
	
	
}
