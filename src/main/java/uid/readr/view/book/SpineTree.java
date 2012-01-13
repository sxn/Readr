package uid.readr.view.book;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.apache.log4j.Logger;

import uid.readr.model.BookExplorer;
import uid.readr.view.BookView;
import uid.readr.view.tree.TwoStageTreeCellEditor;

public class SpineTree extends JTree {

	/** For serialization. */
	private static final long serialVersionUID = -883427425933759089L;

	private static final Logger LOGGER = Logger.getLogger(SpineTree.class
			.getName());

	private DefaultTreeModel model;
	private DefaultMutableTreeNode rootNode;
	private BookView view;

	public SpineTree(BookView view) {
		this.view = view;
		rootNode = new DefaultMutableTreeNode(view.getBookExplorer()
				.getBookTitle());
		for (String s : view.getBookExplorer().getSpineContents())
			rootNode.add(new DefaultMutableTreeNode(s));

		model = new DefaultTreeModel(rootNode);

		this.setModel(model);
		this.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);
		this.addTreeSelectionListener(new SpineSelectionListener(this));
		this.setEditable(false);

		this.addMouseListener(twoStageEditingListener);
		this.addMouseMotionListener(twoStageEditingListener);
	}

	private class SpineSelectionListener implements TreeSelectionListener {
		private SpineTree tree;

		public SpineSelectionListener(SpineTree tree) {
			this.tree = tree;
		}

		public void valueChanged(TreeSelectionEvent e) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
					.getLastSelectedPathComponent();
			if (node == null || tree.getRowForPath(e.getNewLeadSelectionPath()) <= 0)
				return;
			
			view.getBookExplorer().setSpineDocIdx(tree.getRowForPath(e.getNewLeadSelectionPath()) - 1);
			view.updateBookText();
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

	private void possiblySwitchEditors(MouseEvent e) {
		Point p = e.getPoint();
		if (p != null) {
			TreePath path = this.getPathForLocation(p.x, p.y);
			if (path != this.getEditingPath()) {
				if (isEditing()) {
					TreeCellEditor editor = getCellEditor();
					if (!(editor instanceof TwoStageTreeCellEditor)
							|| (editor instanceof TwoStageTreeCellEditor && !((TwoStageTreeCellEditor) editor)
									.isFullyEngaged()))
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
}
