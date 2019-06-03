package mudApp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

	/**
	 * This represents a place in our text adventure.
	 * @author jfoley
	 *
	 */
	public class Place {
		/**
		 * This is a list of places we can get to from this place.
		 */
		private Map<String, Doorway> doorways;
		/**
		 * This is the identifier of the place.
		 */
		//* @param id - the internal id of this place.
		private String id;
		//description of the location.
		private String description;
		/*this is the map of people in the room */
		public Vector<ClientHandler> people;
		
		
		/**
		 * Internal only constructor for Place. Use {@link #create(String, String)} or {@link #terminal(String, String)} instead.
		 * @param id - the internal id of this place.
		 * @param description - the user-facing description of the place.
		 * @param terminal - whether this place ends the game.
		 */
		protected Place(String id, String description) {
			this.id = id;
			this.description = description;
			this.doorways = new HashMap<>();
			this.people = new Vector<>();
		}
		
		/**
		 * Create a doorway for the user to navigate to another Place.
		 */
		public void addDoor(Doorway doorway) {
			this.doorways.put(doorway.getName(), doorway);
		}

		
		/**
		 * The internal id of this place, for referring to it in {@link Exit} objects.
		 * @return the id.
		 */
		public String getId() {
			return this.id;
		}
		
		/**
		 * The narrative description of this place.
		 * @return what we show to a player about this place.
		 */
		public String printDescription() {
			System.out.println(this.description);
			return this.description;

		}

		/**
		 * Get a view of the exits from this Place, for navigation.
		 * @return all the exits from this place.
		 */
		public HashMap<String, Doorway> getVisibleExits() {
			return (HashMap<String, Doorway>) this.doorways;
		} 
		
		
		/**
		 * Create a place with an id and description.
		 * @param id - this is the id of the place (for creating {@link Exit} objects that go here).
		 * @param description - this is what we show to the user.
		 * @return the new Place object (add exits to it).
		 */
		public static Place create(String id, String description) {
			return new Place(id, description);
		}
		
		/**
		 * Implements what we need to put Place in a HashSet or HashMap.
		 */
		public int hashCode() {
			return this.id.hashCode();
		}
		
		/**
		 * Give a string for debugging what place is what.
		 */
		public String toString() {
			return "Place("+this.id+" with "+this.doorways.size()+" doorways.)";
		}
		
		/**
		 * Whether this is the same place as another.
		 */
		public boolean equals(Object other) {
			if (other instanceof Place) {
				return this.id.equals(((Place) other).id);
			}
			return false;
		}
		public String getDescription() {
			return this.description;
		}
		//this adds a person to the list of people within the room.
		public void addPerson(ClientHandler playerPerson) {
			people.add(playerPerson);
		}
		
	}//end class.


