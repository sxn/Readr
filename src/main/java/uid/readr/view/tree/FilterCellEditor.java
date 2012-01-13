package uid.readr.view.tree;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.log4j.Logger;

import uid.readr.model.filter.CheckableString;
import uid.readr.view.MainView;

/**
 * The Class FilterCellEditor.
 */
public class FilterCellEditor extends AbstractCellEditor implements TwoStageTreeCellEditor  {
	
	/** For serialization. */
	private static final long serialVersionUID = 6629958572931155633L;

	/** The Constant LOGGER. */
	private static final Logger LOGGER = Logger
			.getLogger(FilterCellEditor.class.getName());	
	
	/** The check box. */
	private JCheckBox checkBox;
	private CheckableString cs;
	private MainView view;
	
	public void setView(MainView view) {
		this.view = view;
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeCellEditor#getTreeCellEditorComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int)
	 */
	public Component getTreeCellEditorComponent(JTree tree, Object value,
			boolean isSelected, boolean expanded, boolean leaf, int row) {
		
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		if (leaf) {
			cs = (CheckableString)((DefaultMutableTreeNode)value).getUserObject();
			checkBox = new JCheckBox(cs.getString());
			panel.add(checkBox);
			checkBox.setSelected(((CheckableString)((DefaultMutableTreeNode)value).getUserObject()).isChecked());
			checkBox.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					cs.toggle();
					if (view.getLibrary() != null) {
						view.getLibrary().updateFilterPipeline(false);
						view.initBindings();
					}
				}
			});
			checkBox.setBackground(tree.getBackground());
		} else {
			cs = new CheckableString(((String)((DefaultMutableTreeNode)value).getUserObject()));
			panel.add(new JLabel(cs.getString()));
			checkBox = null;
		}
		
		panel.setBackground(tree.getBackground());
		return panel;
	}

	/* (non-Javadoc)
	 * @see javax.swing.CellEditor#getCellEditorValue()
	 */
	public Object getCellEditorValue() {
		if (checkBox != null)
			return cs;
		else
			return cs.getString();
	}

	/* (non-Javadoc)
	 * @see uid.readr.view.tree.TwoStageTreeCellEditor#isFullyEngaged()
	 */
	public boolean isFullyEngaged() {
		return false;
	}
}
