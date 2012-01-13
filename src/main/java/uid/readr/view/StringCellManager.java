package uid.readr.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultCellEditor;
import javax.swing.JTextField;

/**
 * The Class StringCellManager - renderer / editor.
 */
public class StringCellManager extends DefaultCellEditor implements
	TwoStageTableCellEditor {
	/** For serialization. */
	private static final long serialVersionUID = 2180305488878497843L;
	
	/** The fully engaged flag. */
	private boolean fullyEngaged = false;

	/**
	 * Instantiates a new string cell manager.
	 * 
	 * @param txtField
	 *            the txt field
	 */
	public StringCellManager(JTextField txtField) {
		super(txtField);
		txtField.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				fullyEngaged = true;
			}
		});
		txtField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fullyEngaged = false;
			}
		});
		txtField.addFocusListener(new FocusListener() {
			public void focusLost(FocusEvent e) {
				fullyEngaged = false;
				
			}
			public void focusGained(FocusEvent e) {
				fullyEngaged = true;
			}
		});
	}
	
	/* (non-Javadoc)
	 * @see uid.readr.view.TwoStageTableCellEditor#isFullyEngaged()
	 */
	public boolean isFullyEngaged() {
		return fullyEngaged;
	}
}
