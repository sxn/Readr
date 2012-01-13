package uid.readr.view.tree;

import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.apache.log4j.Logger;

import uid.readr.model.filter.CheckableString;

public class FilterCellRenderer extends DefaultTreeCellRenderer {
	private static final Logger LOGGER = Logger
			.getLogger(FilterCellRenderer.class.getName());
	
	/** For serialization. */
	private static final long serialVersionUID = 8443075109330188820L;
	private JCheckBox checkBox;
	
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean sel, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {
		
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		if (leaf) {
			checkBox = new JCheckBox();
			panel.add(checkBox);
			checkBox.setSelected(((CheckableString)((DefaultMutableTreeNode)value).getUserObject()).isChecked());
			panel.add(new JLabel(((CheckableString)((DefaultMutableTreeNode)value).getUserObject()).getString()));
			checkBox.setBackground(tree.getBackground());
		} else {
			panel.add(new JLabel(((String)((DefaultMutableTreeNode)value).getUserObject())));
		}
		
		panel.setBackground(tree.getBackground());
		return panel;
	}
}