package uid.readr.model.filter;

import java.util.Set;

import uid.readr.model.Book;

public class TagFilter extends CheckableStringSetFilter {
	@Override
	public Set<String> getStringsFromBook(Book book) {
		return book.getTags();
	}
}
