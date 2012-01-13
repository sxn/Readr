package uid.readr.model;

import java.io.File;
import java.io.FileInputStream;

import nl.siegmann.epublib.domain.Author;
import nl.siegmann.epublib.domain.Metadata;
import nl.siegmann.epublib.epub.EpubReader;

import org.apache.log4j.Logger;

/**
 * The Enum EReader (singleton).
 */
public enum EReader {
	INSTANCE;
	private static final Logger LOGGER = Logger.getLogger(INSTANCE.getClass());

	private EpubReader reader = new EpubReader();

	/**
	 * Read book from file.
	 * 
	 * @param file
	 *            the file
	 * @return the book, null on failure
	 */
	public Book readBookFromFile(File file) {
		LOGGER.info("Creating book from: " + file);
		try {
			nl.siegmann.epublib.domain.Book eBook = 
				reader.readEpub(new FileInputStream(file));
			
			Metadata meta = eBook.getMetadata();
			
			Book book = new Book();
			book.setTitle(eBook.getTitle());
			
			for (Author author: meta.getAuthors())
				book.getAuthors().add(author.toString());
			
			for (String subject: meta.getSubjects())
				book.getTags().add(subject);
			
			book.setPublishers(meta.getPublishers());
			book.setRating(0);
			
			book.setPath(file);
			
			LOGGER.info(book);
			
			return book;
		} catch (Exception e) {
			LOGGER.error("Book read error");
			return null;
		}
	}
}
