package mudApp;

public class Player {
	private String username;
	private String description;
	private ClientHandler thePerson;
	public Place location;
	//this is the code for the entry of the person who will be stored.

	public Player(String username, String description, ClientHandler thePerson, Place location) {
		this.username = username;
		this.description = description;
		this.setThePerson(thePerson);
		this.location = location;
		location.addPerson(thePerson);
		
	}

	public ClientHandler getThePerson() {
		return thePerson;
	}

	public void setThePerson(ClientHandler thePerson) {
		this.thePerson = thePerson;
	}

}
