package mudApp;

//A Java program for a Client 
import java.net.*;
import java.util.Scanner;
import java.io.*;

public class Client {
	Scanner writer;
	InetAddress ip;
	Socket theSock;
	DataInputStream input;
	DataOutputStream output;
	volatile boolean stillRunning = true;
	String userName;
	String description;

	public Client() throws IOException {
		// this reads in user writing. it's the user input.
		this.writer = new Scanner(System.in);
		// ip address
		this.ip = InetAddress.getByName("localhost");
		// socket
		this.theSock = new Socket(ip, serverPort);
		// data input thread.
		this.input = new DataInputStream(theSock.getInputStream());
		// data output thread.
		this.output = new DataOutputStream(theSock.getOutputStream());
	}

	public void initUser() {
		out("  _    _  _____ ______ _____  _   _          __  __ ______ \r\n" + 
				" | |  | |/ ____|  ____|  __ \\| \\ | |   /\\   |  \\/  |  ____|\r\n" + 
				" | |  | | (___ | |__  | |__) |  \\| |  /  \\  | \\  / | |__   \r\n" + 
				" | |  | |\\___ \\|  __| |  _  /| . ` | / /\\ \\ | |\\/| |  __|  \r\n" + 
				" | |__| |____) | |____| | \\ \\| |\\  |/ ____ \\| |  | | |____ \r\n" + 
				"  \\____/|_____/|______|_|  \\_\\_| \\_/_/    \\_\\_|  |_|______|\r\n" + 
				"");
		System.out.println("Please enter your user/nickname");
		userName = writer.nextLine();
		out("  _____  ______  _____  _____ _____  _____ _____ _______ _____ ____  _   _ \r\n" + 
				" |  __ \\|  ____|/ ____|/ ____|  __ \\|_   _|  __ \\__   __|_   _/ __ \\| \\ | |\r\n" + 
				" | |  | | |__  | (___ | |    | |__) | | | | |__) | | |    | || |  | |  \\| |\r\n" + 
				" | |  | |  __|  \\___ \\| |    |  _  /  | | |  ___/  | |    | || |  | | . ` |\r\n" + 
				" | |__| | |____ ____) | |____| | \\ \\ _| |_| |      | |   _| || |__| | |\\  |\r\n" + 
				" |_____/|______|_____/ \\_____|_|  \\_\\_____|_|      |_|  |_____\\____/|_| \\_|\r\n" + 
				"");
		System.out.println("Please enter a description of who you're playing.");
		description = writer.nextLine();
		System.out.println("=============================HELP=============================");
		System.out.println("If you're ever lost, type help to get a guide to the world.");
	}

	public void sendMessageLoop() {
		try {
			// this is when we see the first stuff happen.
			output.writeUTF(userName);
			output.writeUTF(description);
			System.out.println("Starting up the client. . . .");
			System.out.println("You can write what you want to say below!");
			System.out.println(" ");

			while (this.stillRunning) {
				String msg = writer.nextLine();
				if (msg.equals("exit")) {
					sendExitMessage();
					break;
				} else if (msg.equals("help")) {
					help();
					continue;
				} // end if
				output.writeUTF(msg);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void readMessageLoop() {
		while (this.stillRunning) {
			try {
				String message = input.readUTF();
				// if (message.equals("exit") {
				// return;
				System.out.println(message);
			} catch (IOException e) {
				if (!stillRunning) {
					// don't care
				} else {
					e.printStackTrace();
				}
				return;
			}
		}		
		stillRunning = false;
	}

	public void sendExitMessage() {
		if (!this.stillRunning) {
			return;
		}
		try {
			this.stillRunning = false;

			output.writeUTF("exit");
			System.out.println("Thank you for using SMUG!");
			System.out.println("Client closed.");
			theSock.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	final static int serverPort = 6432;

	public static void main(String[] args) throws UnknownHostException, IOException {
		intro();
		Client client = new Client();
		client.initUser();
		
		Thread sendMessage = new Thread(new Runnable() {
			@Override
			public void run() {
				client.sendMessageLoop();
			}
		});
		Thread readMessage = new Thread(new Runnable() {
			@Override
			public void run() {
				client.readMessageLoop();
			}
		});

		try {
			// Runtime.getRuntime().addShutdownHook(shutDown);
			sendMessage.start();
			readMessage.start();
			
			// wait until the sendMessage Thread ends.
			sendMessage.join();
			// wait until the readMessage thread ends.
			readMessage.join();
			// shutDown.start();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			client.stillRunning = false;
			// wait until the sendMessage Thread ends.
			try {
				sendMessage.join();
				readMessage.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// wait until the readMessage thread ends.
			client.sendExitMessage();

		}
	}// end main method

	public static void intro() {
		System.out.println("Welcome to. . .");
		System.out.println("   _____ __  __ _    _  _____ \r\n" + 
		         "  / ____|  \\/  | |  | |/ ____|\r\n"
			   + " | (___ | \\  / | |  | | |  __ \r\n" + 
		         "  \\___ \\| |\\/| | |  | | | |_ |\r\n"
			   + "  ____) | |  | | |__| | |__| |\r\n" + 
		         " |_____/|_|  |_|\\____/ \\_____|\r\n" + "");
		System.out.println("The Smith College Multi-User Game.");
	}

	public static void out(Object words) {
		System.out.println(words);
	}
	public static void help() {
		out("  _    _ ______ _      _____  _ \r\n" + 
				" | |  | |  ____| |    |  __ \\| |\r\n" + 
				" | |__| | |__  | |    | |__) | |\r\n" + 
				" |  __  |  __| | |    |  ___/| |\r\n" + 
				" | |  | | |____| |____| |    |_|\r\n" + 
				" |_|  |_|______|______|_|    (_)");
		out("Welcome to SMUG's Help Menu");
		out("The world of SMUG has a few things to get used to.");
		out("=============================BACKGROUND=============================");
		out("Number one, you are all in different rooms in this world.");
		out("This means that you can travel from room to room if you want to see new things.");
		out(" ");
		out("=============================COMMANDS=============================");
		out("TALKING AND INFORMATION");
		out("To say something to your room, just type it!");
		out("If you want to say something to EVERYONE, type yell and then type what else you would say");
		out("To get info on the room that you're in and the people in it,  type info.");
		out("And if you want to exit SMUG (Not that you would ever want to), type exit.");
		out("To see all of the doors in a room, type doors.");
		out(" ");
		out("EXPLORATION");
		out("Using that last door command, you can see everywhere that you can walk to from whatever room you're in.");
		out("If you want to do that, type walk and then the name of the door that you want to go through.");
		out("However, if you have friends in your current room, you'll have to leave them behind!");
		out("Of course, you can always come back and say hi.");

	}

}