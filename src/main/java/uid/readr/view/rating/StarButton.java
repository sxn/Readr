package uid.readr.view.rating;

import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import uid.readr.view.buttons.IconButton;

/**
 * The Class StarButton.
 */
public class StarButton extends IconButton {
	/** For serialization. */
	private static final long serialVersionUID = 7533869371039381133L;

	/** The rating. */
	private int rating;

	/** The default state. */
	private int defaultState;

	/**
	 * Gets the rating.
	 * 
	 * @return the rating
	 */
	public int getRating() {
		return rating;
	}

	/**
	 * Sets the rating.
	 * 
	 * @param rating
	 *            the new rating
	 */
	public void setRating(int rating) {
		this.rating = rating;
	}

	/**
	 * Gets the default state.
	 * 
	 * @return the default state
	 */
	public int getDefaultState() {
		return defaultState;
	}

	/**
	 * Sets the default state.
	 * 
	 * @param defaultState
	 *            the new default state
	 */
	public void setDefaultState(int defaultState) {
		this.defaultState = defaultState;
	}

	/**
	 * Instantiates a new star button.
	 * 
	 * @param rating
	 *            the rating
	 * @param defaultState
	 *            the default state
	 */
	public StarButton(int rating, int defaultState) {
		super();
		try {
			icons = new ImageIcon[] {
					new ImageIcon(ImageIO.read(RatingCellManager.class
							.getResourceAsStream("/images/empty_star.png"))),
					new ImageIcon(ImageIO.read(RatingCellManager.class
							.getResourceAsStream("/images/full_star.png"))), };
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.rating = rating;
		this.defaultState = defaultState;
		this.setState(defaultState);
	}

	/**
	 * Return to default.
	 */
	public void returnToDefault() {
		setState(defaultState);
	}
}
