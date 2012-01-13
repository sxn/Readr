package uid.readr.view.buttons;

import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class LeftButton extends IconButton {
	private static final long serialVersionUID = -7050513789337492778L;
	
	public LeftButton() {
		super(false);
		try {
			icons = new ImageIcon[] {
					new ImageIcon(ImageIO.read(LeftButton.class
							.getResourceAsStream("/images/left.png")))};
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		setState(0);
		this.setFocusable(false);
	}
}
