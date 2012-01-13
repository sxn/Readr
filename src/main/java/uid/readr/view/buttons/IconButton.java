package uid.readr.view.buttons;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * The Class IconButton.
 */
public class IconButton extends JButton {
	/** For serialization. */
	private static final long serialVersionUID = -1897950822596040006L;

	protected ImageIcon[] icons;
	protected int state;
	
	public IconButton() {
		this.setBorder(BorderFactory.createEmptyBorder());
		this.setContentAreaFilled(false);
	}
	
	public IconButton(boolean withoutBorder) {
		this.setContentAreaFilled(false);
		if (withoutBorder)
			this.setBorder(BorderFactory.createEmptyBorder());
	}
	
	public void setState(int state) {
		if (state < 0 || state >= icons.length)
			throw new IllegalArgumentException("Illegal state argument");
		this.setIcon(icons[state]);
	}
}
