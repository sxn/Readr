package uid.readr.view.tree;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.apache.log4j.Logger;

import uid.readr.model.Library;
import uid.readr.model.filter.CheckableString;
import uid.readr.view.MainView;

public class FilterTree extends JTree {
	private static final Logger LOGGER = Logger.getLogger(FilterTree.class
			.getName());
	
	/** For serialization. */
	private static final long serialVersionUID = 3177724889228181027L;
	
	private Library library;
	private DefaultTreeModel model;
	private DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("filters");
	private DefaultMutableTreeNode authorsNode = new DefaultMutableTreeNode("authors");
	private DefaultMutableTreeNode publishersNode = new DefaultMutableTreeNode("publishers");
	private DefaultMutableTreeNode tagsNode = new DefaultMutableTreeNode("tags");
	
	private FilterCellRenderer fcr = new FilterCellRenderer();
	private FilterCellEditor fce = new FilterCellEditor();
	
	public FilterTree(MainView view) {
		rootNode.add(authorsNode);
		authorsNode.add(new DefaultMutableTreeNode(new CheckableString("", true)));
		rootNode.add(publishersNode);
		publishersNode.add(new DefaultMutableTreeNode(new CheckableString("", true)));
		rootNode.add(tagsNode);
		tagsNode.add(new DefaultMutableTreeNode(new CheckableString("", true)));
		model = new DefaultTreeModel(rootNode);
		
		this.setModel(model);
		this.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		
		this.setEditable(true);
		
		this.setCellRenderer(fcr);
		this.setCellEditor(fce);
		
		fce.setView(view);
		
		this.addMouseListener(twoStageEditingListener);
		this.addMouseMotionListener(twoStageEditingListener);
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
	
	private void possiblySwitchEditors(MouseEvent e) {
		Point p = e.getPoint();
		if (p != null) {
			TreePath path = this.getPathForLocation(p.x, p.y);
			if (path != this.getEditingPath()) {
				if (isEditing()) {
					TreeCellEditor editor = getCellEditor();
					if (!(editor instanceof TwoStageTreeCellEditor) ||
						(editor instanceof TwoStageTreeCellEditor && !((TwoStageTreeCellEditor)editor).isFullyEngaged()))
						editor.cancelCellEditing();
				}
				if (!isEditing()) {
					if (path != null) {
						this.startEditingAtPath(path);
					}
				}
			}
		}
	}
	
	public void setLibrary(Library library) {
		this.library = library;		
	}
	
	public void updateModel() {
		if (library == null || library.getAuthorSet() == null)
			return;
		
		// push expanded rows
		boolean[] rowExpanded = new boolean[3];
		for (int row = 0, idx = 0; row < this.getRowCount(); ++row)
			if (this.getPathForRow(row).getPathCount() == 2)
				rowExpanded[idx++] = this.isExpanded(row);
		
		authorsNode.removeAllChildren();
		for (CheckableString s: library.getAuthorSet())
			authorsNode.add(new DefaultMutableTreeNode(s));
		publishersNode.removeAllChildren();
		for (CheckableString s: library.getPublisherSet())
			publishersNode.add(new DefaultMutableTreeNode(s));
		tagsNode.removeAllChildren();
		for (CheckableString s: library.getTagSet())
			tagsNode.add(new DefaultMutableTreeNode(s));
		
		model = new DefaultTreeModel(rootNode);
		this.setModel(model);
		
		// pop expanded rows
		for (int row = 0, idx = 0; row < this.getRowCount(); ++row)
			if (this.getPathForRow(row).getPathCount() == 2)
				if (rowExpanded[idx++])
					this.expandRow(row);
		
		this.repaint();
	}
}
