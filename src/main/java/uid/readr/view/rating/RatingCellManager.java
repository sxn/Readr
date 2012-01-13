package uid.readr.view.rating;

import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.AbstractCellEditor;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import org.apache.log4j.Logger;

import uid.readr.view.TwoStageTableCellEditor;

/**
 * The Class RatingCellManager - renderer / editor.
 */
public class RatingCellManager extends AbstractCellEditor implements
		TwoStageTableCellEditor, TableCellRenderer {
	// TODO: improve instantiation control

	/** The Constant LOGGER. */
	private static final Logger LOGGER = Logger
			.getLogger(RatingCellManager.class.getName());

	/** For serialization. */
	private static final long serialVersionUID = 4477063350115325324L;

	/** The rating. */
	private int rating;

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax
	 * .swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int col) {
		int rating = (Integer) value;

		return getTableCellManager(table, rating, isSelected, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.table.TableCellEditor#getTableCellEditorComponent(javax.swing
	 * .JTable, java.lang.Object, boolean, int, int)
	 */
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int col) {
		int rating = (Integer) value;

		return getTableCellManager(table, rating, isSelected, true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uid.readr.view.TwoStageTableCellEditor#isFullyEngaged()
	 */
	public boolean isFullyEngaged() {
		return false;
	}

	/**
	 * Gets the table cell manager.
	 * 
	 * @param table
	 *            the table
	 * @param rating
	 *            the rating
	 * @param isSelected
	 *            the is selected
	 * @return the table cell manager
	 */
	private Component getTableCellManager(JTable table, int rating,
			boolean isSelected, boolean isEditor) {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));

		StarButton[] buttons = new StarButton[5];
		for (int i = 1; i < 6; ++i) {
			StarButton btnRating;
			if (rating >= i) {
				btnRating = new StarButton(i, 1);
			} else {
				btnRating = new StarButton(i, 0);
			}
			buttons[i - 1] = btnRating;
			panel.add(btnRating);
		}

		if (isEditor) {
			for (int i = 1; i < 6; ++i) {
				RatingListener listener = new RatingListener(this, buttons, i);
				buttons[i - 1].addMouseListener(listener);
				buttons[i - 1].addActionListener(listener);
			}
		}

		panel.setBackground(table.getBackground());
		return panel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.CellEditor#getCellEditorValue()
	 */
	public Object getCellEditorValue() {
		return rating;
	}
}
