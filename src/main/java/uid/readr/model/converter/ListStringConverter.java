package uid.readr.model.converter;

import java.util.ArrayList;
import java.util.List;

import org.jdesktop.beansbinding.Converter;

/**
 * The Class ListStringConverter.
 */
public class ListStringConverter extends Converter {

	/* (non-Javadoc)
	 * @see org.jdesktop.beansbinding.Converter#convertForward(java.lang.Object)
	 */
	@Override
	public Object convertForward(Object obj) {
		String ret = "";
		if (!(obj instanceof List))
			return "invalid";
		
		List<String> set = (List<String>)obj;
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
		List<String> ret = new ArrayList<String>();
		for (String s: text.split(";")) {
			String toAdd = s.trim();
			if (toAdd.length() > 0)
				ret.add(toAdd);
		}
		return ret;
	}

}
