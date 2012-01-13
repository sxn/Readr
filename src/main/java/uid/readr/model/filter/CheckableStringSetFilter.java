package uid.readr.model.filter;

import static uid.readr.model.filter.CheckableString.getInstance;

import java.util.Set;

import uid.readr.model.Book;

public abstract class CheckableStringSetFilter implements Filter {
	protected Set<CheckableString> checkableStrings;
	
	public Set<CheckableString> getCheckableStrings() {
		return checkableStrings;
	}

	public void setCheckableStrings(Set<CheckableString> checkableStrings) {
		this.checkableStrings = checkableStrings;
	}

	public void addString(CheckableString s) {
		checkableStrings.add(s);
	}
	
	public void removeString(CheckableString s) {
		checkableStrings.remove(s);
	}

	public boolean accept(Book book) {
		Set<String> stringsFromBook = getStringsFromBook(book);
		
		// empty string test
		if (stringsFromBook.size() == 0 && checkableStrings.contains(getInstance("", true)))
			return true;
		
		for (String s: stringsFromBook) {
			for (CheckableString cs: checkableStrings) {
				if (cs.getString().equals(s) && !cs.isChecked())
					return false;
			}
		}
		
		return true;
	};
	
	public abstract Set<String> getStringsFromBook(Book book);
}
