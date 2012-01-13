package uid.readr.view.buttons;

import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class BookButton extends IconButton {
	private static final long serialVersionUID = -7050513789337492778L;
	
	public BookButton() {
		super(false);
		try {
			icons = new ImageIcon[] {
					new ImageIcon(ImageIO.read(BookButton.class
							.getResourceAsStream("/images/book.png")))};
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		setState(0);
		this.setText("Books");
		this.setFocusable(false);
	}
}
