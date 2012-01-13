package uid.readr.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

import uid.readr.model.Book;
import uid.readr.view.MainView;

/**
 * The Class MainViewController.
 */
public class MainViewController implements PropertyChangeListener {
	/** The Constant LOGGER. */
	private static final Logger LOGGER = Logger
			.getLogger(MainViewController.class.getName());
	
	/** The Constant LIBRARY_LOCATION. */
	private static final String LIBRARY_LOCATION = "/lib";
	
	/** The main view. */
	private MainView mainView;
	
	/**
	 * Instantiates a new main view controller.
	 * 
	 * @param mainView
	 *            the main view
	 */
	public MainViewController(MainView mainView) {
		this.mainView = mainView;
	}
	
	/**
	 * Choose the library folder path.
	 */
	public void chooseLibrary() {
		JFileChooser fc = new JFileChooser(LIBRARY_LOCATION);
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		int retValue = fc.showDialog(mainView, "Open/Create");
		if (retValue == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fc.getSelectedFile();
			LOGGER.info("Selected directory: " + selectedFile);
			
			saveLibraryData();
			mainView.getLibrary().removeCtrlPropertyChangeListener(this);
			
			mainView.getLibrary().setLibraryPath(selectedFile);
			initLibrary();
		} else {
			LOGGER.info("Selected directory: invalid");
		}
	}
	
	/**
	 * <pre>
	 * Inits the library, by:
	 * 	- setting the appropriate window title
	 * 	- populating library data
	 * </pre>
	 */
	public void initLibrary() {
		mainView.setTitle(mainView.getLibrary().getLibraryPath().toString());
		
		if (mainView.getLibrary().isValidLibraryPath()) {
			populateLibraryData();
			mainView.getBtnBooks().setEnabled(true);
		}
		else {
			mainView.getBtnBooks().setEnabled(false);
		}
	}
	
	/**
	 * Populates the current library.
	 * 
	 * Prerequisites:
	 * 	- valid library directory
	 */
	public void populateLibraryData() {
		if (!mainView.getLibrary().isValidLibraryPath())
			throw new AssertionError("Invalid library path");
		mainView.getLibrary().populateLibraryData();
		mainView.getLibrary().addCtrlPropertyChangeListener(this);
		mainView.getLibrary().updateFilterPipeline(true);
		mainView.initBindings();
	}
	
	/**
	 * Adds books via a file chooser.
	 */
	public void addBooks() {
		JFileChooser fc = new JFileChooser("/");
		fc.setMultiSelectionEnabled(true);
		
		fc.setFileFilter(new FileFilter() {
			@Override
			public boolean accept(File f) {
				if (f.isDirectory())
					return true;
				if (!f.isFile())
					return false;
				return FilenameUtils.
					getExtension(f.getName()).equalsIgnoreCase("epub");
			}

			@Override
			public String getDescription() {
				return ".epub files";
			}
		});
		
		int retValue = fc.showDialog(mainView, "Add");
		if (retValue == JFileChooser.APPROVE_OPTION) {
			File[] selectedFiles = fc.getSelectedFiles();
			
			LOGGER.info("Selected files: ");
			for (File file: selectedFiles)
				LOGGER.info("\t" + file.toString());
			for (File file: selectedFiles) {
				try {
					File newFile = file;
					File libPath = mainView.getLibrary().getLibraryPath(); 
					
					if (!file.getAbsolutePath().startsWith(libPath.getAbsolutePath())) {
						newFile = new File(libPath, file.getName());
						FileUtils.copyFile(file, newFile);
						
						LOGGER.info("Copied book to:" + newFile);
					} else {
						LOGGER.info("Book imported from original location");
					}
					
					Book book = mainView.getLibrary().addBookFromFile(newFile);
					if (book != null) {
						mainView.getLibrary().updateFilterPipeline(false);
						mainView.initBindings();
						
						book.addCtrlPropertyChangeListener(this);
					}
				} catch (IOException e) {
					LOGGER.warn("Could not copy book file: ", e);
				}
			}
			mainView.initBindings();
		} else {
			LOGGER.info("Selected files: invalid");
		}
	}

	/**
	 * Save library data.
	 */
	public void saveLibraryData() {
		if (mainView.getLibrary().isValidLibraryPath()) {
			mainView.getLibrary().saveMetadata();
		}
	}
	
	public void propertyChange(PropertyChangeEvent e) {
		LOGGER.info("Property changed: " + e.getPropertyName());
		
		mainView.getLibrary().updateFilterPipeline(false);
		mainView.initBindings();
	}
}	
