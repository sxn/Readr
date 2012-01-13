/*
 * 
 */
package uid.readr.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.List;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;

import org.apache.log4j.Logger;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.swingbinding.JTableBinding;
import org.jdesktop.swingbinding.SwingBindings;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.skin.BusinessBlackSteelSkin;
import org.pushingpixels.substance.api.skin.OfficeBlack2007Skin;

import uid.readr.controller.MainViewController;
import uid.readr.model.Book;
import uid.readr.model.Library;
import uid.readr.model.converter.ListStringConverter;
import uid.readr.model.converter.SetStringConverter;
import uid.readr.model.filter.CheckableString;
import uid.readr.view.buttons.BookButton;
import uid.readr.view.buttons.LibraryButton;
import uid.readr.view.buttons.MinusButton;
import uid.readr.view.buttons.PlusButton;
import uid.readr.view.tree.FilterTree;
import javax.swing.UIManager;

/**
 * The Class MainView.
 * 
 * @author Saca
 */
public class MainView extends JFrame {
	private static final Logger LOGGER = Logger.getLogger(MainView.class
			.getName());
	
	/** For serialization. */
	private static final long serialVersionUID = 4975072355383502955L;

	/** The content pane. */
	private JPanel contentPane;

	/** The library. */
	private Library library = new Library();

	/** The controller. */
	private MainViewController ctrl = new MainViewController(this);
	
	/** The btn books. */
	private JButton btnBooks;
	
	/** The sp books. */
	private JScrollPane spBooks;
	
	/** The books table. */
	private BooksTable booksTable;
	private JPanel filterPanel;
	private JScrollPane spFilters;
	private FilterTree tree = new FilterTree(this);
	private JPanel selectPanel;
	private JButton btnAllSelect;
	private JButton btnNoneSelect;

	/**
	 * Gets the library.
	 * 
	 * @return the library
	 */
	public Library getLibrary() {
		return library;
	}

	/**
	 * Sets the library.
	 * 
	 * @param library
	 *            the new library
	 */
	public void setLibrary(Library library) {
		this.library = library;
	}
	
	/**
	 * Gets the books table.
	 * 
	 * @return the books table
	 */
	public BooksTable getBooksTable() {
		return booksTable;
	}
	
	/**
	 * Launch the application.
	 * 
	 * @param args
	 *            the arguments
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch (Throwable e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainView frame = new MainView();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainView() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent arg0) {
				ctrl.saveLibraryData();
			}
		});
		
		// keep for wb_pro design mode
		setBounds(0, 0, 450, 300);
		
		// fullscreen code
		Toolkit tk = Toolkit.getDefaultToolkit();  
		int xSize = ((int) tk.getScreenSize().getWidth());  
		int ySize = ((int) tk.getScreenSize().getHeight());  
		this.setSize(xSize,ySize - 250);  
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[10.00,grow][grow][grow]", "[][-5.00][][grow]"));
		{
			JButton btnLibrary = new LibraryButton();
			btnLibrary.setToolTipText("Switch the current library.");
			btnLibrary.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ctrl.chooseLibrary();
				}
			});
			contentPane.add(btnLibrary, "cell 1 0,alignx center");
		}
		{
			btnBooks = new BookButton();
			btnBooks.setToolTipText("Add books to the current library.");
			btnBooks.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ctrl.addBooks();
				}
			});
			contentPane.add(btnBooks, "cell 2 0,alignx center");
		}
		{
			filterPanel = new JPanel();
			filterPanel.setBackground(Color.WHITE);
			contentPane.add(filterPanel, "cell 0 3,grow");
			filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.Y_AXIS));
			{
				selectPanel = new JPanel();
				selectPanel.setMaximumSize(new Dimension(322222, 10));
				filterPanel.add(selectPanel);
				selectPanel.setLayout(new GridLayout(0, 2, 0, 0));
				{
					btnAllSelect = new PlusButton();
					btnAllSelect.setToolTipText("Check all filter tags.");
					btnAllSelect.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							if (library.getTagSet() == null)
								return;
							
							for (CheckableString cs: library.getTagSet())
								cs.setChecked(true);
							for (CheckableString cs: library.getAuthorSet())
								cs.setChecked(true);
							for (CheckableString cs: library.getPublisherSet())
								cs.setChecked(true);
							initBindings();
						}
					});
					selectPanel.add(btnAllSelect);
				}
				{
					btnNoneSelect = new MinusButton();
					btnNoneSelect.setToolTipText("Uncheck all filter tags.");
					btnNoneSelect.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							if (library.getTagSet() == null)
								return;
							
							for (CheckableString cs: library.getTagSet())
								cs.setChecked(false);
							for (CheckableString cs: library.getAuthorSet())
								cs.setChecked(false);
							for (CheckableString cs: library.getPublisherSet())
								cs.setChecked(false);
							initBindings();
						}
					});
					selectPanel.add(btnNoneSelect);
				}
			}
			{
				spFilters = new JScrollPane();
				spFilters.setBackground(new Color(255, 255, 255));
				spFilters.setPreferredSize(new Dimension(100, 200));
				filterPanel.add(spFilters);
			}
			tree.setToolTipText("Unchecked tags will disable the corresponding book entries in which they appear.");
			spFilters.setViewportView(tree);
		}
		{
			spBooks = new JScrollPane();
			contentPane.add(spBooks, "cell 1 3 2 1,grow");
			{
				booksTable = new BooksTable();
				booksTable.setShowVerticalLines(false);
				booksTable.setRowSelectionAllowed(false);
				booksTable.setSelectionBackground(Color.WHITE);
				booksTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				booksTable.setRowHeight(30);
				booksTable.setFillsViewportHeight(true);
				booksTable.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
				spBooks.setViewportView(booksTable);
			}
		}
		initDataBindings();
		ctrl.initLibrary();
	}

	/**
	 * Gets the btn books.
	 * 
	 * @return the btn books
	 */
	public JButton getBtnBooks() {
		return btnBooks;
	}

	/**
	 * Inits the bindings.
	 */
	public void initBindings() {
		LOGGER.info("Init bindings");
		
		initDataBindings();
		tree.setLibrary(library);
		tree.updateModel();
	}
	protected void initDataBindings() {
		BeanProperty<Library, List<Book>> libraryBeanProperty = BeanProperty.create("data.filteredBooks");
		JTableBinding<Book, Library, JTable> jTableBinding = SwingBindings.createJTableBinding(UpdateStrategy.READ_ONCE, library, libraryBeanProperty, booksTable, "BooksTableBinding");
		//
		BeanProperty<Book, File> bookBeanProperty = BeanProperty.create("path");
		jTableBinding.addColumnBinding(bookBeanProperty).setColumnName(" ").setColumnClass(File.class);
		//
		BeanProperty<Book, String> bookBeanProperty_1 = BeanProperty.create("title");
		jTableBinding.addColumnBinding(bookBeanProperty_1).setColumnName("Title").setColumnClass(String.class);
		//
		BeanProperty<Book, List<String>> bookBeanProperty_2 = BeanProperty.create("authors");
		JTableBinding<Book, Library, JTable>.ColumnBinding 
		columnBinding = jTableBinding.addColumnBinding(bookBeanProperty_2);
		columnBinding.setColumnName("Author(s)");
		columnBinding.setColumnClass(String.class);
		columnBinding.setConverter(new ListStringConverter());
		//
		BeanProperty<Book, List<String>> bookBeanProperty_3 = BeanProperty.create("publishers");
		JTableBinding<Book, Library, JTable>.ColumnBinding 
		columnBinding_1 = jTableBinding.addColumnBinding(bookBeanProperty_3);
		columnBinding_1.setColumnName("Publisher(s)");
		columnBinding_1.setColumnClass(String.class);
		columnBinding_1.setConverter(new ListStringConverter());
		//
		BeanProperty<Book, Set<String>> bookBeanProperty_4 = BeanProperty.create("tags");
		JTableBinding<Book, Library, JTable>.ColumnBinding 
		columnBinding_2 = jTableBinding.addColumnBinding(bookBeanProperty_4);
		columnBinding_2.setColumnName("Tag(s)");
		columnBinding_2.setColumnClass(String.class);
		columnBinding_2.setConverter(new SetStringConverter());
		//
		BeanProperty<Book, Integer> bookBeanProperty_5 = BeanProperty.create("rating");
		jTableBinding.addColumnBinding(bookBeanProperty_5).setColumnName("Rating").setColumnClass(Integer.class);
		//
		jTableBinding.bind();
		
		booksTable.initColumnWidths();
	}
}
