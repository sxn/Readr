package uid.readr.view.reading;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import uid.readr.view.BookView;
import uid.readr.view.buttons.IconButton;
import uid.readr.view.rating.RatingCellManager;

public class ReadButton extends IconButton {
	/** For serialization. */
	private static final long serialVersionUID = 5349109400504900874L;
	
	private File bookFile;
	
	/**
	 * Instantiates a new read button.
	 * 
	 * @param state
	 *            the state (false -> closed, true -> open)
	 */
	public ReadButton(boolean state) {
		try {
			icons = new ImageIcon[] {
					new ImageIcon(ImageIO.read(RatingCellManager.class
							.getResourceAsStream("/images/closed_book.png"))),
					new ImageIcon(ImageIO.read(RatingCellManager.class
							.getResourceAsStream("/images/open_book.png"))), };
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.setState(state ? 1 : 0);
		this.addActionListener(ReadListener.INSTANCE);
	}
	
	public File getBookFile() {
		return bookFile;
	}
	
	public void setBookFile(File bookFile) {
		this.bookFile = bookFile;
	}

	private static class ReadListener implements ActionListener {
		public static ReadListener INSTANCE = new ReadListener();
		
		private ReadListener() {
		}
		
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() instanceof ReadButton) {
				final ReadButton source = (ReadButton) e.getSource();
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							BookView frame = new BookView(source.getBookFile());
							frame.setVisible(true);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		}
	}
}
