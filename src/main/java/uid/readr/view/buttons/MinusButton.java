package uid.readr.view.buttons;

import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class MinusButton extends IconButton {
	private static final long serialVersionUID = -7050513789337492778L;
	
	public MinusButton() {
		super(false);
		try {
			icons = new ImageIcon[] {
					new ImageIcon(ImageIO.read(MinusButton.class
							.getResourceAsStream("/images/minus.png")))};
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		setState(0);
		this.setFocusable(false);
	}
}
