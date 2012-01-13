package uid.readr.view.reading;

import java.awt.Component;
import java.awt.FlowLayout;
import java.io.File;

import javax.swing.AbstractCellEditor;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import org.apache.log4j.Logger;

import uid.readr.view.TwoStageTableCellEditor;

/**
 * The Class ReadingCellManager - renderer / editor.
 */
public class ReadingCellManager extends AbstractCellEditor implements
		TwoStageTableCellEditor, TableCellRenderer {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = Logger
			.getLogger(ReadingCellManager.class.getName());
	
	/** For serialization. */
	private static final long serialVersionUID = 4477063350115325324L;
	
	private ReadButton button;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax
	 * .swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int col) {
		File bookFile = (File)value;

		return getTableCellManager(table, bookFile, isSelected, false);
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
		File bookFile = (File)value;

		return getTableCellManager(table, bookFile, isSelected, true);
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
	private Component getTableCellManager(JTable table, File bookFile,
			boolean isSelected, boolean isEditor) {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		button = new ReadButton(isEditor);
		button.setBookFile(bookFile);
		panel.add(button);
		
		panel.setBackground(table.getBackground());
		return panel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.CellEditor#getCellEditorValue()
	 */
	public Object getCellEditorValue() {
		return button.getBookFile();
	}
}
