package uid.readr.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.epub.EpubReader;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

public class BookExplorer {
	private static final Logger LOGGER = Logger.getLogger(BookExplorer.class
			.getName());
	private File bookFile;
	private File tmpFolder;
	private Book book;
	private int spineDocIdx = 0;
	private List<String> spineContents = new ArrayList<String>();
	
	public int getSpineDocIdx() {
		return spineDocIdx;
	}
	public void setSpineDocIdx(int spineDocIdx) {
		this.spineDocIdx = spineDocIdx;
	}
	public List<String> getSpineContents() {
		return spineContents;
	}
	
	public BookExplorer(File bookFile) {
		this.bookFile = bookFile;
		init();
	}
	
	private void init() {
		try {
			LOGGER.info("Reading book...");
			book = new EpubReader().readEpub(FileUtils.openInputStream(bookFile));
			
			LOGGER.info("Unpacking epub");
			tmpFolder = File.createTempFile("epub", this.toString());
			tmpFolder.delete();
			tmpFolder.mkdir();
			
			for (Resource rsrc: book.getResources().getAll()) {
				System.out.println(rsrc.getHref());
				File rsrcFile = new File(tmpFolder, rsrc.getHref());
				FileUtils.writeLines(rsrcFile, IOUtils.readLines(rsrc.getInputStream()));
			}
			
			LOGGER.info("Populating spine contents");
			for (int i = 0; i < book.getSpine().size(); ++i) {
				System.out.println("\t" + book.getSpine().getResource(i).getId());
				spineContents.add(book.getSpine().getResource(i).getId());
			}
		} catch (Exception e) {
			LOGGER.info("oops", e);
		}
	}
	
	public void destroy() {
		LOGGER.info("Destroying unpacked epub...");
		try {
			tmpFolder.deleteOnExit();
		} catch (Exception e) {
			LOGGER.info("oops", e);
		}
	}
	
	public String getBookTitle() {
		return book.getTitle();
	}

	public File getCurrentSpineDoc() {
		return new File(tmpFolder, book.getSpine().getResource(spineDocIdx).getHref()); 
	}
	
	public void incPage() {
		spineDocIdx++;
		if (spineDocIdx >= spineContents.size())
			spineDocIdx = spineContents.size() - 1;
	}
	
	public void decPage() {
		spineDocIdx--;
		if (spineDocIdx < 0)
			spineDocIdx = 0;
	}
}
