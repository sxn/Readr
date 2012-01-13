package uid.readr.view.buttons;

import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class LibraryButton extends IconButton {
	private static final long serialVersionUID = -7050513789337492778L;
	
	public LibraryButton() {
		super(false);
		try {
			icons = new ImageIcon[] {
					new ImageIcon(ImageIO.read(LibraryButton.class
							.getResourceAsStream("/images/library.png")))};
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		setState(0);
		this.setText("Library");
		this.setFocusable(false);
	}
}
