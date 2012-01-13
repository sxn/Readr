package uid.readr.view.rating;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import org.apache.log4j.Logger;

/**
 * The listener interface for receiving rating events. The class that is
 * interested in processing a rating event implements this interface, and the
 * object created with that class is registered with a component using the
 * component's <code>addRatingListener<code> method. When
 * the rating event occurs, that object's appropriate
 * method is invoked.
 * 
 * @see RatingEvent
 */
public class RatingListener implements ActionListener, MouseListener {
	private static final Logger LOGGER = Logger.getLogger(RatingListener.class
			.getName());
	
	/** The manager. */
	private RatingCellManager manager;
	
	/** The buttons. */
	private StarButton[] buttons;
	
	/** The rating. */
	private int rating;
	
	/**
	 * Instantiates a new rating listener.
	 * 
	 * @param manager
	 *            the manager
	 * @param buttons
	 *            the buttons
	 * @param rating
	 *            the rating
	 */
	public RatingListener(RatingCellManager manager, StarButton[] buttons,
			int rating) {
		super();
		this.manager = manager;
		this.buttons = buttons;
		this.rating = rating;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0) {
		manager.setRating(rating);
		manager.stopCellEditing();
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent arg0) {
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	public void mouseEntered(MouseEvent arg0) {
		for (int i = 0; i < 5; ++i) {
			if (i < rating)
				buttons[i].setState(1);
			else
				buttons[i].setState(0);
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	public void mouseExited(MouseEvent arg0) {
		for (int i = 0; i < 5; ++i) {
			buttons[i].returnToDefault();
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent arg0) {
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent arg0) {
	}
}
