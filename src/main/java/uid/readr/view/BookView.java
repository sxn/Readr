package uid.readr.view;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.miginfocom.swing.MigLayout;

import org.apache.log4j.Logger;
import org.xhtmlrenderer.simple.XHTMLPanel;

import uid.readr.model.BookExplorer;
import uid.readr.view.book.SpineTree;
import uid.readr.view.buttons.LeftButton;
import uid.readr.view.buttons.RightButton;

/**
 * The Class BookView.
 */
public class BookView extends JFrame {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = Logger.getLogger(BookView.class
			.getName());
	
	/** For serialization. */
	private static final long serialVersionUID = 6124588690049104848L;

	/** The content pane. */
	private JPanel contentPane;
	
	private BookExplorer bookExplorer;
	
	XHTMLPanel bookTextPanel;
	private SpineTree spineTree;
	private JSlider slider_1;
	private JLabel lblPage = new JLabel("unknown page");
	
	public SpineTree getSpineTree() {
		return spineTree;
	}
	public BookExplorer getBookExplorer() {
		return bookExplorer;
	}
	public JSlider getSlider() {
		return slider_1;
	}
	public JLabel getLblPage() {
		return lblPage;
	}

	/**
	 * Launch the application.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BookView frame = new BookView();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public BookView() throws Exception {
		this(new File("d:\\lib\\epub_lib\\austen-pride-and-prejudice-illustrations.epub"));
	}
	
	/**
	 * Create the frame.
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public BookView(File bookFile) throws Exception {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		bookExplorer = new BookExplorer(bookFile);
		
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent arg0) {
				bookExplorer.destroy();
			}
		});
		
		// keep for wb_pro design mode
		setBounds(0, 0, 450, 300);
		
		// fullscreen code
		Toolkit tk = Toolkit.getDefaultToolkit();  
		int xSize = ((int) tk.getScreenSize().getWidth());  
		int ySize = ((int) tk.getScreenSize().getHeight());  
		this.setSize(xSize,ySize);  
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[][grow]", "[grow][][]"));
		{
			JPanel panel = new JPanel();
			contentPane.add(panel, "cell 0 0 1 3,grow");
			panel.setLayout(new MigLayout("", "[]", "[grow]"));
			{
				JScrollPane scrollPane = new JScrollPane();
				panel.add(scrollPane, "cell 0 0,grow");
				{
					spineTree = new SpineTree(this);
					spineTree.setToolTipText("Epub spine.");
					scrollPane.setViewportView(spineTree);
				}
			}
		}
		{
			JScrollPane spText = new JScrollPane();
			contentPane.add(spText, "cell 1 0,grow");
			{
				
				this.setTitle(bookExplorer.getBookTitle());
				
				bookTextPanel = new XHTMLPanel();
				updateBookText();
				
				spText.setViewportView(bookTextPanel);
			}
		}
		{
			JPanel panel = new JPanel();
			contentPane.add(panel, "cell 1 1,grow");
			{
				JButton btnRight = new RightButton();
				btnRight.setToolTipText("Previous page.");
				btnRight.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ev) {
						bookExplorer.decPage();
						updateBookText();
					}
				});
				panel.add(btnRight);
			}
			{
				panel.add(lblPage);
			}
			{
				JButton btnLeft = new LeftButton();
				btnLeft.setToolTipText("Next page.");
				btnLeft.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ev) {
						bookExplorer.incPage();
						updateBookText();
					}
				});
				panel.add(btnLeft);
			}
		}
		{
			JPanel panel = new JPanel();
			panel.setPreferredSize(new Dimension(1000, 10));
			contentPane.add(panel, "cell 1 2,grow");
			panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
			{
				slider_1 = new JSlider(0, bookExplorer.getSpineContents().size() - 1, bookExplorer.getSpineDocIdx());
				slider_1.setToolTipText("Current page.");
				panel.add(slider_1);
				
				slider_1.setMajorTickSpacing(10);
				slider_1.setMinorTickSpacing(1);
				slider_1.setPaintTicks(true);
				slider_1.setPaintLabels(true);
				
				slider_1.addChangeListener(new ChangeListener() {
					public void stateChanged(ChangeEvent e) {
						JSlider slider = (JSlider)e.getSource();
						
						if (!slider.getValueIsAdjusting()) {
							int idx = (int)slider.getValue();
							bookExplorer.setSpineDocIdx(idx);
							updateBookText();
						}
					}
				});
			}
		}
		this.setFocusable(true);
		this.addKeyListener(new KeyListener() {
			private long lastTime = System.currentTimeMillis();
			private long delay = 300;
			private boolean pressed = false;
			
			public void keyTyped(KeyEvent arg0) {
				
			}
			
			public void keyReleased(KeyEvent arg0) {
				pressed = false;
			}
			
			public void keyPressed(KeyEvent e) {
				long crtTime = System.currentTimeMillis();
				LOGGER.info("pressed: " + e.getKeyCode());
				
				if (!pressed || crtTime - lastTime > delay) {
					lastTime = crtTime;
					pressed = true;
					if (e.getKeyCode() == 39) { // right key
						bookExplorer.incPage();
						updateBookText();
						LOGGER.info("right pressed");
					} else if (e.getKeyCode() == 37) { // left key
						bookExplorer.decPage();
						updateBookText();
						LOGGER.info("left pressed");
					}
				} else {
					LOGGER.info("too early");
				}
			}
		});
	}
	
	public void updateBookText() {
		try {
			bookTextPanel.setDocument(bookExplorer.getCurrentSpineDoc());
			lblPage.setText("pag. " + Integer.toString(bookExplorer.getSpineDocIdx()) + " / " + (bookExplorer.getSpineContents().size() - 1));
			if (slider_1 != null)
				slider_1.setValue(bookExplorer.getSpineDocIdx());
		} catch (Exception e) {
			LOGGER.info("oops", e);
		}
	}
}
