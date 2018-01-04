/**
 * Stores all information about a single user.
 */

public class User {
	private String username;
	private String password;

	public User(String user, String pass) {
		username = user.trim().toLowerCase();
		password = pass;
	}

	public String getUsername () {
		return username;
	}

	public String getPassword () {
		return password;
	}

	public boolean checkLogin (String user, String pass) {
		user = user.trim().toLowerCase();
		if (username.equals(user) && password.equals(pass)) {
			return true;
		}
		return false;
	}
}