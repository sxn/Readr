package uid.readr.model.filter;

import java.util.ArrayList;
import java.util.List;

import uid.readr.model.Book;

/**
 * The Class FilterPipeline aggregates several filters.
 */
public class FilterPipeline {
	
	/** The filters. */
	private List<Filter> filters = new ArrayList<Filter>();
	
	/**
	 * Adds the filter.
	 * 
	 * @param filter
	 *            the filter
	 */
	public void addFilter(Filter filter) {
		filters.add(filter);
	}
	
	/**
	 * Clear filters.
	 */
	public void clearFilters() {
		filters.clear();
	}
	
	/**
	 * 
	 * @param books
	 *            the books
	 * @return the list
	 */
	public List<Book> run(List<Book> books) {
		List<Book> ret = new ArrayList<Book>();
		
		for (Book book: books) {
			boolean rejected = false;
			for (Filter filter: filters) {
				if (!filter.accept(book)) {
					rejected = true;
					break;
				}
			}
			
			if (!rejected)
				ret.add(book);
		}
		
		return ret;
	}
}
