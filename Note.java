/**
 * Stores information about a single note.
 */

public class Note {
	private String title;
	private String content;

	public Note (String title, String content) {
		this.title = title;
		this.content = content;
	}

	public String getTitle () {
		return title;
	}

	public String getContent () {
		return content;
	}

	public void updateNote (String newTitle, String newContent) {
		title = newTitle;
		content = newContent;
	}
}