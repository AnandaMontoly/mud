package mudApp;

import java.util.Objects;
	/**
	 * This class represents an doorway from a Place to another Place.
	 * @author jfoley
	 *
	 */
public class Doorway {
		/**
		 * How do we describe this exit to a user, e.g., "A door with a spiderweb."
		 */
		private String description;
		/**
		 * How do we identify the Place that this is going.
		 */
		private String target;
		private String name;
	



		/**
		 * Create a new Exit.
		 * @param target - where it goes.
		 * @param description - how it looks.
		 * @return 
		 */
		
		public Doorway(String name, String target, String description) {
			this.name = name;
			this.description = description;
			this.target = target;
		}


		/**
		 * A getter for the target place of this exit.
		 * @return where it goes.
		 */
		public String getTarget() {
			return this.target;
		}
		/*
		 * get the name of your door and return it.
		 */
		public String getName() {
			return this.name;
		}
		/*
		 * get the description of your door and return it.
		 */
		public String getDescription() {
			return this.description;
		}
		/**
		 * Make this debuggable when we print it for ourselves.
		 */
		public String toString() {
			return "Exit("+this.description+", "+this.target+")";
		}
		
		/**
		 * Make it so we can put this in a HashMap or HashSet.
		 */
		public int hashCode() {
			return Objects.hash(this.description, this.target);
		}
		
		/**
		 * This is a useful definition of being the same.
		 * @param other - another exit.
		 * @return if they go to the same place.
		 */
		public boolean goesToSamePlace(Doorway other) {
			return this.target.equals(other.target);
		}
		
		/**
		 * The other half of hashCode that lets us put it in a HashMap or HashSet.
		 */
		public boolean equals(Object other) {
			if (other instanceof Doorway) {
				Doorway rhs = (Doorway) other;
				return this.target.equals(rhs.target) && this.description.equals(rhs.description); 
			}
			return false;
		}


}


