package uid.readr.model.converter;

import java.util.HashSet;
import java.util.Set;

import org.jdesktop.beansbinding.Converter;

/**
 * The Class SetStringConverter.
 */
public class SetStringConverter extends Converter {
	
	/* (non-Javadoc)
	 * @see org.jdesktop.beansbinding.Converter#convertForward(java.lang.Object)
	 */
	@Override
	public Object convertForward(Object obj) {
		String ret = "";
		if (!(obj instanceof Set))
			return "invalid";
		
		Set<String> set = (Set<String>)obj;
		for (String s: set) {
			ret += s + "; ";
		}
		
		return ret.substring(0, ret.length() - 2);
	}

	/* (non-Javadoc)
	 * @see org.jdesktop.beansbinding.Converter#convertReverse(java.lang.Object)
	 */
	@Override
	public Object convertReverse(Object obj) {
		String text = (String)obj;
		Set<String> ret = new HashSet<String>();
		for (String s: text.split(";")) {
			String toAdd = s.trim();
			if (toAdd.length() > 0)
				ret.add(toAdd);
		}
		return ret;
	}
	
}
