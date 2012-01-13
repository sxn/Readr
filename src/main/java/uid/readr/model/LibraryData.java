package uid.readr.model;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import uid.readr.model.filter.Filter;
import uid.readr.model.filter.FilterPipeline;

/**
 * The Class LibraryData.
 */
public class LibraryData implements Serializable {
	/** For serialization. */
	private static final long serialVersionUID = 4902983191743786221L;

	/** The books. */
	private List<Book> books = new ArrayList<Book>();

	/** The name. */
	private String name;

	/** The pipeline. */
	transient FilterPipeline pipeline = new FilterPipeline();
	
	/**
	 * Gets the books.
	 * 
	 * @return the books
	 */
	public List<Book> getBooks() {
		return books;
	}
	
	/**
	 * Gets the filtered books.
	 * 
	 * @return the filtered books
	 */
	public List<Book> getFilteredBooks() {
		return pipeline.run(books);
	}

	/**
	 * Sets the books.
	 * 
	 * @param books
	 *            the new books
	 */
	public void setBooks(List<Book> books) {
		this.books = books;
	}

	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 * 
	 * @param name
	 *            the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Adds the book.
	 * 
	 * @param book
	 *            the book
	 * @return true, if successful
	 */
	public boolean addBook(Book book) {
		return books.add(book);
	}

	/**
	 * Removes the book.
	 * 
	 * @param book
	 *            the book
	 * @return true, if successful
	 */
	public boolean removeBook(Book book) {
		return books.remove(book);
	}

	/**
	 * Gets the pipeline.
	 * 
	 * @return the pipeline
	 */
	public FilterPipeline getPipeline() {
		return pipeline;
	}
	/**
	 * Sets the pipeline.
	 * 
	 * @param pipeline
	 *            the new pipeline
	 */
	public void setPipeline(FilterPipeline pipeline) {
		this.pipeline = pipeline;
	}

	/**
	 * Adds the filter to the pipeline.
	 * 
	 * @param filter
	 *            the filter
	 */
	public void addFilter(Filter filter) {
		pipeline.addFilter(filter);
	}

	/**
	 * Read object.
	 * 
	 * @param in
	 *            the in
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws ClassNotFoundException
	 *             the class not found exception
	 */
	private void readObject(java.io.ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		in.defaultReadObject();
		pipeline = new FilterPipeline();
	}
}
