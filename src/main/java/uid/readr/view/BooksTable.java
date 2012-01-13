package uid.readr.view;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;

import uid.readr.view.rating.RatingCellManager;
import uid.readr.view.reading.ReadingCellManager;

/**
 * The Class BooksTable.
 */
public class BooksTable extends JTable {

	/** For serialization. */
	private static final long serialVersionUID = -2884879586896244509L;
	
	private final int[] COLUMN_WIDTHS = new int[] {
			3,	// reading
			22,	// title
			22, // authors
			15,	// publishers
			25,	// tags
			13	// rating
	};
	
	/**
	 * Instantiates a new books table.
	 */
	public BooksTable() {
		// rating editor / renderer for integers
		this.setDefaultRenderer(Integer.class, new RatingCellManager());
		this.setDefaultEditor(Integer.class, new RatingCellManager());

		// string editor for strings
		JTextField editorTF = new JTextField();
		this.setDefaultEditor(String.class, new StringCellManager(editorTF));
		
		// reading editor / renderer for files
		ReadingCellManager rcm = new ReadingCellManager();
		this.setDefaultEditor(File.class, rcm);
		this.setDefaultRenderer(File.class, rcm);
		
		this.addMouseListener(twoStageEditingListener);
		this.addMouseMotionListener(twoStageEditingListener);
	}
	
	public void initColumnWidths() {
		for (int i = 0; i < COLUMN_WIDTHS.length; ++i) {
			this.getColumnModel().getColumn(i).
				setPreferredWidth(COLUMN_WIDTHS[i] * 10);
			this.getColumnModel().getColumn(i).
				setMinWidth(COLUMN_WIDTHS[i]);
			this.getColumnModel().getColumn(i).
				setMaxWidth(COLUMN_WIDTHS[i] * 20);
		}
	}
	
	/** The two stage editing listener. */
	private final MouseAdapter twoStageEditingListener = new MouseAdapter() {
		@Override
		public void mouseMoved(MouseEvent e) {
			possiblySwitchEditors(e);
		}
		@Override
		public void mouseEntered(MouseEvent e) {
			possiblySwitchEditors(e);
		}
		@Override
		public void mouseExited(MouseEvent e) {
			possiblySwitchEditors(e);
		}
	};

	/**
	 * Possibly switch editors.
	 * 
	 * @param e
	 *            the e
	 */
	private void possiblySwitchEditors(MouseEvent e) {
		Point p = e.getPoint();
		if (p != null) {
			int row = rowAtPoint(p);
			int col = columnAtPoint(p);
			if (row != getEditingRow() || col != getEditingColumn()) {
				if (isEditing()) {
					TableCellEditor editor = getCellEditor();
					if (!(editor instanceof TwoStageTableCellEditor) ||
						(editor instanceof TwoStageTableCellEditor && !((TwoStageTableCellEditor)editor).isFullyEngaged()))
						editor.cancelCellEditing();
				}
				if (!isEditing()) {
					if (row != -1 && col != -1 && isCellEditable(row, col)) {
						editCellAt(row, col);
					}
				}
			}
		}
	}
}
