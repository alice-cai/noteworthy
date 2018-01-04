/**
 * Responsible for loading, saving, and updating the current user's notes.
 */

import java.util.ArrayList;
import java.io.*;

public class NoteManager {
	private ArrayList<Note> noteList;
	private String fileName;

	public NoteManager (User user) {
		fileName = "notes/" + user.getUsername() + ".txt";

		if(!checkFileExists()) {
			createNewFile();
		}

		loadUserData();
	}

	public ArrayList<Note> getNoteList () {
		return noteList;
	}

	public int getNumNotes () {
		return noteList.size();
	}

	private boolean checkFileExists () {
		File file = new File(fileName);
		return file.exists();
	}

	private void createNewFile () {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
			out.write('0');
			out.close();
		} catch (IOException iox) {}
	}

	private void loadUserData () {
		try {
			BufferedReader in = new BufferedReader (new FileReader(fileName));
			int numNotes = Integer.parseInt(in.readLine());
			noteList = new ArrayList<>();

			for(int i = 0; i < numNotes; i++) {
				in.readLine(); // blank line
				String noteTitle = in.readLine();
				int numContentLines = Integer.parseInt(in.readLine());
				String noteContent = in.readLine();
				for(int j = 1; j < numContentLines; j++) {
					noteContent = noteContent + "\n" + in.readLine();
				}
				noteList.add(new Note(noteTitle, noteContent));
			}

			in.close();
		} catch (IOException iox) {
			System.out.println("Error loading notes from file.");
		}
	}

	private int countNewLines (String string) {
		int count = 1;
		for(int i = 0; i < string.length(); i++) {
			if(string.charAt(i) == '\n') {
				count++;
			}
		}
		return count;
	}

	public void saveUserData () {
		try {
			BufferedWriter out = new BufferedWriter (new FileWriter(fileName));

			out.write(noteList.size() + ""); // output num notes
			for(int i = 0; i < noteList.size(); i++) {
				out.newLine();
				out.newLine();

				Note note = noteList.get(i);

				out.write(note.getTitle()); // output note title
				out.newLine();

				out.write(countNewLines(note.getContent()) + ""); // output num lines in content
				out.newLine();

				out.write(note.getContent()); // output note content
			}

			out.close();
		} catch (IOException iox) {
		}
	}

	public boolean addNote (String title, String content) {
		if (checkDuplicateTitle(title)) {
			return false;
		} else {
			Note note = new Note(title, content);
			noteList.add(note);
			saveUserData();
			return true;
		}
	}

	public boolean deleteNote (String title) {
		for(int i = 0; i < noteList.size(); i++) {
			Note note = noteList.get(i);
			if(title.equals(note.getTitle())) {
				noteList.remove(i);
				saveUserData();
				return true;
			}
		}
		return false;
	}

	public boolean updateNote (String curTitle, String newTitle, String newContent) {
		if (!newTitle.equals(curTitle) && checkDuplicateTitle(newTitle)) {
			return false;
		} else {
			Note note = findNoteByTitle(curTitle);
			note.updateNote(newTitle, newContent);
			saveUserData();
			return true;
		}
	}

	public Note findNoteByTitle(String title) {
		for(Note note : noteList) {
			if(title.equals(note.getTitle())) {
				return note;
			}
		}
		return null;
	}

	private boolean checkDuplicateTitle (String title) {
		for(Note note : noteList) {
			if(title.equals(note.getTitle())) {
				return true;
			}
		}
		return false;
	}
}