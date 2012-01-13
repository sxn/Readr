/*
 * 
 */
package uid.readr.model;

import static uid.readr.model.filter.CheckableString.getInstance;

import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.log4j.Logger;

import uid.readr.model.filter.AuthorFilter;
import uid.readr.model.filter.CheckableString;
import uid.readr.model.filter.FilterPipeline;
import uid.readr.model.filter.PublisherFilter;
import uid.readr.model.filter.TagFilter;

/**
 * The Class Library.
 */
public class Library {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = Logger.getLogger(Library.class
			.getName());

	/** The Constant METADATA_FILE. */
	private static final String METADATA_FILE = "metadata.bin";

	/** The Constant NO_LIBRARY_SEL. */
	public static final File NO_LIBRARY_SEL = new File("No library selected");

	/** The library path. */
	private File libraryPath = NO_LIBRARY_SEL;

	/** The data. */
	private LibraryData data = new LibraryData();

	/** The author set. */
	private Set<CheckableString> authorSet;

	/** The publisher set. */
	private Set<CheckableString> publisherSet;

	/** The tag set. */
	private Set<CheckableString> tagSet;

	/** The tag filter. */
	private TagFilter tagFilter = new TagFilter();

	/** The author filter. */
	private AuthorFilter authorFilter = new AuthorFilter();

	/** The publisher filter. */
	private PublisherFilter publisherFilter = new PublisherFilter();

	public Set<CheckableString> getAuthorSet() {
		return authorSet;
	}

	public Set<CheckableString> getPublisherSet() {
		return publisherSet;
	}

	public Set<CheckableString> getTagSet() {
		return tagSet;
	}

	public PublisherFilter getPublisherFilter() {
		return publisherFilter;
	}

	/**
	 * Gets the library path.
	 * 
	 * @return the library path
	 */
	public File getLibraryPath() {
		return libraryPath;
	}

	/**
	 * Sets the library path.
	 * 
	 * @param libraryPath
	 *            the new library path
	 */
	public void setLibraryPath(File libraryPath) {
		this.libraryPath = libraryPath;
	}

	/**
	 * Gets the data.
	 * 
	 * @return the data
	 */
	public LibraryData getData() {
		return data;
	}

	/**
	 * Sets the data.
	 * 
	 * @param data
	 *            the new data
	 */
	public void setData(LibraryData data) {
		this.data = data;
	}

	/**
	 * Checks if is valid library path.
	 * 
	 * @return true, if is valid library path
	 */
	public boolean isValidLibraryPath() {
		return libraryPath != NO_LIBRARY_SEL && libraryPath.isDirectory();
	}

	/**
	 * Populate library data.
	 */
	public void populateLibraryData() {
		LOGGER.info("Populating books from library");
		File metadata = new File(libraryPath, METADATA_FILE);
		if (!metadata.isFile()) {
			LOGGER.info("Metadata not found, building new library");
			data = getLibraryDataFromFolder(libraryPath);
		} else {
			LOGGER.info("Loading metadata");
			try {
				data = (LibraryData) SerializationUtils.deserialize(FileUtils
						.readFileToByteArray(new File(libraryPath,
								METADATA_FILE)));
			} catch (IOException e) {
				LOGGER.error("Deserialization error: " + e.getMessage());
			}
		}
	}

	/**
	 * Gets the library data from folder.
	 * 
	 * @param libraryPath
	 *            the library path
	 * @return the library data from folder
	 */
	public LibraryData getLibraryDataFromFolder(File libraryPath) {
		LOGGER.info("Searching directory for stored books");
		LibraryData data = new LibraryData();
		for (File f : (Collection<File>) FileUtils.listFiles(libraryPath, null,
				true)) {
			if (f.isFile()
					&& FilenameUtils.getExtension(f.getName())
							.equalsIgnoreCase("epub")) {
				Book book = EReader.INSTANCE.readBookFromFile(f);
				if (book != null) {
					data.addBook(book);
				}
			}
		}

		return data;
	}

	/**
	 * Adds the book from file.
	 * 
	 * @param path
	 *            the path
	 * @return the book
	 */
	public Book addBookFromFile(File path) {
		Book book = EReader.INSTANCE.readBookFromFile(path);
		if (book != null) {
			if (data.getBooks().contains(book)) {
				LOGGER.info("Book already contained: " + book);
				return null;
			} else {
				LOGGER.info("Added book: " + book);
				data.getBooks().add(book);
				return book;
			}
		}
		return null;
	}

	/**
	 * Adds a listener for book data changes.
	 * 
	 * @param l
	 *            the l
	 */
	public void addCtrlPropertyChangeListener(PropertyChangeListener l) {
		if (data != null)
			for (Book book : data.getBooks()) {
				book.addCtrlPropertyChangeListener(l);
			}
	}

	/**
	 * Removes a listener for book data changes.
	 * 
	 * @param l
	 *            the l
	 */
	public void removeCtrlPropertyChangeListener(PropertyChangeListener l) {
		if (data != null)
			for (Book book : data.getBooks()) {
				book.removeCtrlPropertyChangeListener(l);
			}
	}

	/**
	 * Save metadata by serialization.
	 */
	public void saveMetadata() {
		LOGGER.info("Saving metadata");
		try {
			FileUtils.writeByteArrayToFile(
					new File(libraryPath, METADATA_FILE),
					SerializationUtils.serialize(data));
		} catch (IOException e) {
			LOGGER.error("Serialization error: " + e.getMessage());
		}
	}

	/**
	 * Update filter pipeline.
	 */
	public void updateFilterPipeline(boolean newLibrary) {
		LOGGER.info("Updating filter pipeline");

		if (newLibrary) {
			authorSet = new HashSet<CheckableString>();
			authorSet.add(new CheckableString("", true));
			publisherSet = new HashSet<CheckableString>();
			publisherSet.add(new CheckableString("", true));
			tagSet = new HashSet<CheckableString>();
			tagSet.add(new CheckableString("", true));
		}

		if (data != null)
			if (data.getBooks().size() == 0) {

			} else {
				Set<String> presentBookAuthors = new HashSet<String>();
				Set<String> presentBookPublishers = new HashSet<String>();
				Set<String> presentBookTags = new HashSet<String>();

				for (Book book : data.getBooks()) {
					Set<String> bookAuthors = new HashSet<String>(
							book.getAuthors());

					for (String author : bookAuthors) {
						presentBookAuthors.add(author);
						if (!checkContains(authorSet, author)) {
							authorSet.add(new CheckableString(author, true));
						}
					}

					Set<String> bookPublishers = new HashSet<String>(
							book.getPublishers());

					for (String publisher : bookPublishers) {
						presentBookPublishers.add(publisher);
						if (!checkContains(publisherSet, publisher)) {
							publisherSet.add(new CheckableString(publisher,
									true));
						}
					}

					Set<String> bookTags = book.getTags();

					for (String tag : bookTags) {
						presentBookTags.add(tag);
						if (!checkContains(tagSet, tag)) {
							tagSet.add(new CheckableString(tag, true));
						}
					}
				}

				Iterator<CheckableString> it = authorSet.iterator();
				while (it.hasNext()) {
					CheckableString cs = it.next();
					if (!presentBookAuthors.contains(cs.getString()))
						it.remove();
				}

				it = publisherSet.iterator();
				while (it.hasNext()) {
					CheckableString cs = it.next();
					if (!presentBookPublishers.contains(cs.getString()))
						it.remove();
				}

				it = tagSet.iterator();
				while (it.hasNext()) {
					CheckableString cs = it.next();
					if (!presentBookTags.contains(cs.getString()))
						it.remove();
				}
			}

		tagFilter.setCheckableStrings(tagSet);
		authorFilter.setCheckableStrings(authorSet);
		publisherFilter.setCheckableStrings(publisherSet);

		FilterPipeline pipeline = data.getPipeline();
		pipeline.clearFilters();
		pipeline.addFilter(tagFilter);
		pipeline.addFilter(authorFilter);
		pipeline.addFilter(publisherFilter);
		data.setPipeline(pipeline);
	}

	private boolean checkContains(Set<CheckableString> strings, String string) {
		return strings.contains(getInstance(string, true))
				|| strings.contains(getInstance(string, false));
	}
}