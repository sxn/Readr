package uid.readr.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * The Class Book.
 */
public class Book implements Serializable {
	/** For serialization. */
	private static final long serialVersionUID = 1958869401171675651L;

	transient private PropertyChangeSupport changes = new PropertyChangeSupport(
			this);

	/** The Constant LOGGER. */
	transient private static final Logger LOGGER = Logger.getLogger(Book.class
			.getName());

	/** The path. */
	private File path;

	/** The tags. */
	private Set<String> tags = new HashSet<String>();

	/** The rating. */
	private int rating;

	/** The title. */
	private String title;

	/** The authors. */
	private List<String> authors = new ArrayList<String>();

	/** The publisher. */
	private List<String> publishers = new ArrayList<String>();;

	/**
	 * Gets the path.
	 * 
	 * @return the path
	 */
	public File getPath() {
		return path;
	}

	/**
	 * Sets the path.
	 * 
	 * @param path
	 *            the new path
	 */
	public void setPath(File path) {
		this.path = path;
	}

	/**
	 * Gets the tags.
	 * 
	 * @return the tags
	 */
	public Set<String> getTags() {
		return tags;
	}

	/**
	 * Sets the tags.
	 * 
	 * @param tags
	 *            the new tags
	 */
	public void setTags(Set<String> tags) {
		boolean change = !tags.containsAll(this.tags) || !this.tags.containsAll(tags);
		this.tags = tags;
		LOGGER.info(this);
		if (change)
			changes.firePropertyChange("_tags_", "", "n");
	}

	/**
	 * Gets the rating.
	 * 
	 * @return the rating
	 */
	public int getRating() {
		return rating;
	}

	/**
	 * Sets the rating.
	 * 
	 * @param rating
	 *            the new rating
	 */
	public void setRating(int rating) {
		this.rating = rating;
		LOGGER.info(this);
	}

	/**
	 * Gets the title.
	 * 
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the title.
	 * 
	 * @param title
	 *            the new title
	 */
	public void setTitle(String title) {
		this.title = title;
		LOGGER.info(this);
	}

	/**
	 * Gets the authors.
	 * 
	 * @return the authors
	 */
	public List<String> getAuthors() {
		return authors;
	}

	/**
	 * Sets the authors.
	 * 
	 * @param authors
	 *            the new authors
	 */
	public void setAuthors(List<String> authors) {
		boolean change = !authors.containsAll(this.authors) || !this.authors.containsAll(authors);
		this.authors = authors;
		LOGGER.info(this);
		if (change)
			changes.firePropertyChange("_authors_", "", "n");
	}

	/**
	 * Gets the publishers.
	 * 
	 * @return the publishers
	 */
	public List<String> getPublishers() {
		return publishers;
	}

	/**
	 * Sets the publishers.
	 * 
	 * @param publishers
	 *            the new publishers
	 */
	public void setPublishers(List<String> publishers) {
		boolean change = !publishers.containsAll(this.publishers) || !this.publishers.containsAll(publishers);
		this.publishers = publishers;
		LOGGER.info(this);
		if (change)
			changes.firePropertyChange("_publishers_", "", "n");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		return o instanceof Book && ((Book) o).getPath().equals(path);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return path.hashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format("Title: %s, Author(s): %s, Publisher: %s, "
				+ "Tags: %s, Rating: %d", title, authors, publishers, tags,
				rating);
	}

	public void addCtrlPropertyChangeListener(PropertyChangeListener l) {
		changes.addPropertyChangeListener(l);
		LOGGER.info(title + ": Added listener - " + l);
	}

	public void removeCtrlPropertyChangeListener(PropertyChangeListener l) {
		changes.removePropertyChangeListener(l);
		LOGGER.info(title + ": Removed listener");
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		in.defaultReadObject();
		changes = new PropertyChangeSupport(this);
	}
}
