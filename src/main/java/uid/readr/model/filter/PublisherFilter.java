package uid.readr.model.filter;

import java.util.HashSet;
import java.util.Set;

import uid.readr.model.Book;

public class PublisherFilter extends CheckableStringSetFilter {
	@Override
	public Set<String> getStringsFromBook(Book book) {
		return new HashSet<String>(book.getPublishers());
	}
}
