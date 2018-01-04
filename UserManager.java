/**
 * Responsible for loading and saving all user login information.
 */

import java.util.ArrayList;
import java.io.*;

public class UserManager {
	private final String FILE_NAME = "userData.txt";
	private ArrayList<User> userList;;
	private ArrayList<String> usernameList;

	public UserManager() {
		if (!checkFileExists()) {
			createNewFile();
		}
		
		createByFileInput();
	}
	
	private boolean checkFileExists () {
		File file = new File(FILE_NAME);
		return file.exists();
	}

	private void createNewFile () {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(FILE_NAME));
			out.write('0');
			out.close();
		} catch (IOException iox) {}
	}

	private void createByFileInput() {
		try {
			BufferedReader in = new BufferedReader (new FileReader(FILE_NAME));
			int numUsers = Integer.parseInt(in.readLine());
			userList = new ArrayList<>();

			for(int i = 0; i < numUsers; i++) {
				in.readLine();
				userList.add(new User(in.readLine(), in.readLine()));
			}

			in.close();
		} catch (IOException iox) {
			System.out.println("Error loading user data from file.");
		}

		usernameList = new ArrayList<>();
		for(User user : userList) {
			usernameList.add(user.getUsername());
		}
	}

	private boolean saveToFile () {
		try {
			BufferedWriter out = new BufferedWriter (new FileWriter(FILE_NAME));

			// output all user data to text file
			out.write(userList.size() + "");
			for(int i = 0; i < userList.size(); i++) {
				out.newLine();
				out.newLine();
				User user = userList.get(i);
				out.write(user.getUsername());
				out.newLine();
				out.write(user.getPassword());
			}

			out.close();
			return true;
		} catch (IOException iox) {
			return false;
		}
	}

	public void addUser(User user) {
		userList.add(user);
		usernameList.add(user.getUsername());
		saveToFile();
	}

	public boolean checkUserAvailability (String username) {
		username = username.trim().toLowerCase();
		for(User user : userList) {
			if(user.getUsername().equals(username)) {
				return false;
			}
		}
		return true;
	}

	public User checkLogin (String username, String password) {
		for(User user : userList) {
			if(user.checkLogin(username, password)) {
				return user;
			}
		}
		return null;
	}
}
