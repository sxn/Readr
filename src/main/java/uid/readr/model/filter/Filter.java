package uid.readr.model.filter;

import uid.readr.model.Book;

public interface Filter {
	public boolean accept(Book book);
}
