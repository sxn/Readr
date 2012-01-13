package uid.readr.model.filter;

public class CheckableString {
	private boolean checked;
	private String string;
	private static CheckableString INST = new CheckableString("");
	
	public static CheckableString getInstance(String string, boolean checked) {
		INST.setString(string);
		INST.setChecked(checked);
		return INST;
	}
	
	public CheckableString(String string, boolean checked) {
		super();
		this.checked = checked;
		this.string = string;
	}
	
	public CheckableString(String s) {
		string = s;
		checked = true;
	}

	public boolean isChecked() {
		return checked;
	}
	public void toggle() {
		checked = checked ? false : true;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	public String getString() {
		return string;
	}
	public void setString(String string) {
		this.string = string;
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof CheckableString && 
			((CheckableString)obj).getString().equals(string);
	}
	
	@Override
	public int hashCode() {
		return string.hashCode();
	}
	
	@Override
	public String toString() {
		return string + " - " + checked;
	}
}
