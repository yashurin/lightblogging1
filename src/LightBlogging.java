
//****************************************************************************************	
//	
//			LightBlogging Application (c) by Andrey Yashurin, 2014
//
//			LightBlogging is an easy–to–use program for working with blogs on Wordpress publishing platform. 
//			It includes following functionality:
//
//			– viewing all existing blog posts via a built–in RSS client;
//			– adding new blog posts, both in plain text and HTML–formatted;
//			– editing and deleting blog posts;
//			– keeping drafts in a local directory. 
//
//			It includes the following third-party libraries:
//			Redstone XML-RPC Library (http://xmlrpc.sourceforge.net/)
//			wordpress-java (https://code.google.com/p/wordpress-java/)
//
//****************************************************************************************

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import redstone.xmlrpc.XmlRpcFault;

import java.util.Scanner;


@SuppressWarnings("serial")
public class LightBlogging extends JFrame {

//****************************************************************************************	
//	
//		GUI Elements, References, Variables, etc.
//
//****************************************************************************************	
	
	JTabbedPane tabs;
	JPanel pan1;
	final JEditorPane posttext;
	final JList <String>titles;
	JTextField ttext;
	JTextArea blogpost;
		
	static File propFile = new File("properties.dat");
	
	RSSreader reader;
	EditingWindow ew;	
	JFileChooser chooser;	
	
	WordPressClient wpc;
	
	private int tempItem = -1;	
	String content;
	int myNumber;
	String []elements = {"", "", ""};
	
	private boolean lock;

//****************************************************************************************	
//	
//			Constructor for the GUI Class
//
//****************************************************************************************
	public LightBlogging() throws IOException, XmlRpcFault {
		wpc = new WordPressClient();
		Container c = getContentPane();
		tabs = new JTabbedPane();
		pan1 = new JPanel();
		pan1.setLayout(new BorderLayout());
		
		
		JButton newb = new JButton("New");
		newb.setToolTipText("Create a New Post");
		JButton save = new JButton("Save");
		save.setToolTipText("Save as a Local Draft");
		JButton open = new JButton("Open");
		open.setToolTipText("Open a Local Draft");
		JButton post = new JButton("Post");
		post.setToolTipText("Post to Blog");
		JButton settings = new JButton("Settings");
		settings.setToolTipText("Edit Blog Settings");
		
		JLabel label = new JLabel("Title: ");
		ttext = new JTextField(50);
		ttext.setToolTipText("Type a Title of Your Blog Post");
		
		JPanel controls = new JPanel();
		
		JPanel title = new JPanel();
//		JPanel content = new JPanel();
		
		blogpost = new JTextArea("");
		blogpost.setToolTipText("Type a Text of Your Blog Post");
		JScrollPane scrollPane = new JScrollPane(blogpost);
		blogpost.setLineWrap(true);
		
		
		controls.setLayout(new FlowLayout(FlowLayout.LEADING));
		title.setLayout(new FlowLayout(FlowLayout.LEADING));
		
		controls.add(newb);
		controls.add(save);
		controls.add(open);
		controls.add(post);
		controls.add(settings);
		
		title.add(label);
		title.add(ttext);
		
		JSplitPane blogging = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		blogging.setDividerLocation(30);
		blogging.setTopComponent(title);
		blogging.setBottomComponent(scrollPane);
		
		pan1.add(controls, BorderLayout.NORTH);
		pan1.add(blogging, BorderLayout.CENTER);
	
		
		tabs.addTab("Local", pan1);
		
		
		JSplitPane pan2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		
		JPanel blogcont = new JPanel(); // blog controls (buttons etc)
		blogcont.setLayout(new FlowLayout(FlowLayout.LEADING));
		
		JPanel blogControls = new JPanel();
		blogControls.setLayout(new FlowLayout(FlowLayout.LEADING));
		
		JButton readBlog = new JButton("Read Blog");
		readBlog.setToolTipText("Download RSS Feed of a Blog");
		JButton editPost = new JButton("Edit Post");
		editPost.setToolTipText("Edit an Existing Blog Post");
		JButton deletePost = new JButton("Delete Post");
		deletePost.setToolTipText("Delete an Existing Blog Post");
		JButton view = new JButton("View");
		view.setToolTipText("View in External Browser");
		JButton settings1 = new JButton("Settings");
		settings1.setToolTipText("Edit Blog Settings");
		
		
		blogControls.add(readBlog);
		blogControls.add(editPost);
		blogControls.add(deletePost);
		blogControls.add(view);
		blogControls.add(settings1);
				
		blogcont.add(blogControls);
		
		JSplitPane rsspanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
						
		pan2.setDividerLocation(40);
		pan2.setTopComponent(blogcont);
		pan2.setBottomComponent(rsspanel);
		
//****************************************************************************************	
//	
//			Changing Tabs
//
//****************************************************************************************
		ChangeListener changeListener = new ChangeListener() {
		      public void stateChanged(ChangeEvent changeEvent) {
		        JTabbedPane sourceTabbedPane = (JTabbedPane) changeEvent.getSource();
		        int index = sourceTabbedPane.getSelectedIndex();
		        if(index == 1) {
		       	try {
				//		reader = new RSSreader();
					} catch (Exception e) {
						e.printStackTrace();
					}		        	
		        }
		      }
		    };
		    tabs.addChangeListener(changeListener);
		
		titles = new JList<String>(elements);
		
		JScrollPane titlesList = new JScrollPane(titles);
		
		posttext = new JEditorPane(); // where post to be displayed goes
		JScrollPane textPane = new JScrollPane(posttext);
		posttext.setContentType("text/html");
		posttext.setEditable(false);
		posttext.setText("");
				
		rsspanel.setDividerLocation(150);
		rsspanel.setLeftComponent(titlesList);
		rsspanel.setRightComponent(textPane);
		
		tabs.add("Blog", pan2);
		c.add(tabs);	
		

//****************************************************************************************	
//	
//			Buttons on the "Local" Tab Panel
//
//****************************************************************************************
				

//****************************************************************************************	
//	
//			Writing a new Blog Post
//
//****************************************************************************************
		newb.addActionListener(new ActionListener() { 
	        @Override
	        public void actionPerformed(ActionEvent e) {
	        	ttext.setText("");
    			blogpost.setText("");       	
	        }
	    });
		

//****************************************************************************************	
//	
//			Saving a Draft to a Local File
//
//****************************************************************************************
		save.addActionListener(new ActionListener() {
		      public void actionPerformed(ActionEvent ae) {
		    	  String fileTitle = ttext.getText();
		            String fileContent = blogpost.getText();
		            // If Post Title or Post Body are not Entered, Shows a Dialog
		            if(fileTitle.length()==0) {
      				JOptionPane.showMessageDialog(tabs,
      					    "You need to enter a title for your draft.");	        				
      				return;
      				}
      			if(fileContent.length()==0) {
      				JOptionPane.showMessageDialog(tabs,
      					    "You need to enter a text of your draft.");
      				return;
      			}
		    	  chooser = new JFileChooser();
		          File workingDirectory = new File(System.getProperty("user.dir"));
		          chooser.setCurrentDirectory(workingDirectory);
		          FileNameExtensionFilter filter = new FileNameExtensionFilter("HTML FILES", "html", "htm");
		          chooser.setFileFilter(filter);
		          int returnValue = chooser.showSaveDialog(null);
		          if (returnValue == JFileChooser.APPROVE_OPTION) 
		        	  
		          {
		        	  File selectedFile = chooser.getSelectedFile();
		        	  String fileName = selectedFile.getName();
		        	  if(!fileName.endsWith(".htm") && !fileName.endsWith(".html")) {
			            fileName = fileName + ".html"; }			            
		        	  try {
		        		  LocalFiles.writeFile(fileName, fileTitle, fileContent);
		                 
		        	  } catch (Exception ex) {
		        		  ex.printStackTrace();
		               }
		        	  // Setting Texts in a Text Field  and a Text Area to Empty Strings
		        	  ttext.setText("");
		        	  blogpost.setText("");		        	  
		          }
		        }
		      });		
		
//****************************************************************************************	
//	
//			Opening a Local File
//
//****************************************************************************************		
		open.addActionListener(new ActionListener() {
		      public void actionPerformed(ActionEvent ae) {
		    	chooser = new JFileChooser();
		    	String buffer = "";
		  		File workingDirectory = new File(System.getProperty("user.dir"));
		  	    chooser.setCurrentDirectory(workingDirectory);
		  	    FileNameExtensionFilter filter = new FileNameExtensionFilter("HTML FILES", "html", "htm");
		  	    chooser.setFileFilter(filter);
		          int returnValue = chooser.showOpenDialog(null);
		          if (returnValue == JFileChooser.APPROVE_OPTION) {
		        	  try {
		                    Scanner fin = new Scanner(chooser.getSelectedFile());
		                    while(fin.hasNext()) {
		                        buffer += fin.nextLine() + "\n";
		                    }
		                    fin.close();
		        	  		} catch(Exception ex) {
		        	  			ex.printStackTrace();
		        	  		}
		          	}		
		          // Displays Text in a Text Field and a Text Area
		          ttext.setText(LocalFiles.getTitle(buffer));
		    	  blogpost.setText(LocalFiles.getContent(buffer));
		    	  
		        }
		});
		
//****************************************************************************************	
//	
//			Posting to a Blog
//
//****************************************************************************************
		post.addActionListener(new ActionListener() { 
	        @Override
	        public void actionPerformed(ActionEvent e) {
	        			// Strings are Read from Text Field and Text Area
	        			String one = ttext.getText();
	        			String two = blogpost.getText();
	        			ttext.setText("");
	        			blogpost.setText("");
	        			// If Post Title or Post Text are not Entered, a Dialog is Displayed
	        			if(one.length()==0) {
	        				JOptionPane.showMessageDialog(tabs,
	        					    "You need to enter a title for your post.");	        				
	        				return;
	        				}
	        			if(two.length()==0) {
	        				JOptionPane.showMessageDialog(tabs,
	        					    "You need to enter a text of your post.");
	        				return;
	        			}
						try {
							WordPressClient wpc = new WordPressClient();							
							wpc.posting(one, two);							
							
						} catch (Exception e1) {
							e1.printStackTrace();
						}	        		
	        	}
	    });
		
//****************************************************************************************	
//	
//			Blog Settings on the First Panel
//
//****************************************************************************************
		settings.addActionListener(new ActionListener() { 
	        @Override
	        public void actionPerformed(ActionEvent e) {
	        	LoginWindow lw = new LoginWindow();  
        		lw.setVisible(true);
	        }
		});    
				
//****************************************************************************************	
//	
//			Buttons on the "Blog" Tab Panel
//
//****************************************************************************************

//****************************************************************************************	
//	
//			Read the RSS feed of the Blog
//
//**************************************************************************************** 
		readBlog.addActionListener(new ActionListener() { 
	        @Override
	        public void actionPerformed(ActionEvent e) {
	        	String content1 = "";	        	
	        	try {
					reader = new RSSreader();
					reader.writeFeed();

				} catch (IOException e1) {
					System.out.println("error");
					}	        	
				try {
					content1 = reader.showPost(0);
					
				} catch (Exception e1) {
					System.out.println("shit 1");
					e1.printStackTrace();
				}
	        	
	        	StringBuffer readContent = new StringBuffer("<center><h1>"); 
	        	try {
					readContent.append(reader.showTitle(0));
				} catch (Exception e2) {
					e2.printStackTrace();
					}
	        	readContent.append("</h1></center>");
	        	readContent.append(content1);
	        	posttext.setText(readContent.toString());	

	        		try {	
	        			String []tempo = new String[reader.itemsNumber];
	        		//	String []tempo = reader.headlines;
	        			for(int i=0; i<tempo.length; i++)
	        				tempo[i] = reader.showTitle(i);
						titles.setListData(tempo);
					} catch (Exception e1) {
						e1.printStackTrace();
					} 	        		        	        	       
	        }
	    });
		
//****************************************************************************************	
//		
// 		Edit an Existing Blog Post
//
//****************************************************************************************

		editPost.addActionListener(new ActionListener() { 
	        @Override
	        public void actionPerformed(ActionEvent e) {	        	
	        		if(tempItem == -1) {
	        			JOptionPane.showMessageDialog(tabs, "You need to select a post.");
	        			return;
	        			}	        		
					try {
						System.out.println(tempItem);
						myNumber = reader.showItem(tempItem);
					} catch (Exception e2) {

						e2.printStackTrace();
					}  

					try {
						ew = new EditingWindow(tempItem, myNumber);		
						reader.writeFeed();
						} catch (Exception e1) {
;
							e1.printStackTrace();
						}     
						ew.setVisible(true);
						posttext.setText("");

						lock = true;
	        }
	    	});

		
		
		
//****************************************************************************************	
//		
// 		Deleting an Existing Blog Post
//
//****************************************************************************************		
		deletePost.addActionListener(new ActionListener() { 
			@Override
			public void actionPerformed(ActionEvent e) {
				// If Post is Not Selected, a Dialog is Displayed
				if(tempItem == -1) {
					JOptionPane.showMessageDialog(tabs, "You need to select a post.");
					return;
    				}
				try {						
						 int myNumber = reader.showItem(tempItem);
						 
						 WordPressClient wpc = new WordPressClient();        		
						 wpc.deleting(myNumber); 						 
						 reader.removeItem();
						 reader.writeFeed();
						 posttext.setText("");	
						 reader = null;
       		        		
					} catch (Throwable e1) {
						e1.printStackTrace();
					}
					finally {
						try {
							reader = new RSSreader();
							reader.writeFeed();
							
							String []tempo = new String[reader.itemsNumber];							
		        			for(int i=0; i<tempo.length; i++) {
		        				tempo[i] = reader.showTitle(i); }
							titles.setListData(tempo); 

						} catch (Exception e1) {
							 e1.printStackTrace(); 
							}														
						}
					}
				});

		
//****************************************************************************************	
//		
// 		View Blog in an External Browser
//
//**************************************************************************************** 
		
		view.addActionListener(new ActionListener() { 
	        @Override
	        public void actionPerformed(ActionEvent e) {
	        		try { 
	        			
	        			reader.openWebpage(reader.getURL());
				} catch (MalformedURLException e1) {
					
					e1.printStackTrace();
				}
			
	        
	        }
		});    
		
		
//****************************************************************************************	
//		
// 		Blog Settings on the Second Panel
//
//**************************************************************************************** 
	settings1.addActionListener(new ActionListener() { 
        @Override
        public void actionPerformed(ActionEvent e) {
        	LoginWindow lw = new LoginWindow();  
    		lw.setVisible(true);
        }
	});    
	
//****************************************************************************************	
//	
//		Selecting a Post from the List
//
//**************************************************************************************** 
	titles.addListSelectionListener(new ListSelectionListener() {
        @Override
        public void valueChanged(ListSelectionEvent arg0) {
            if(lock) {
            	try {
					refresh();
					lock = false;
				} catch (IOException e) {
					e.printStackTrace();
				}
            	return;
            }
        	        	
        	if (!arg0.getValueIsAdjusting()) {
        		
              int x = titles.getSelectedIndex();
              tempItem = x;
              String content2 = "";
              reader = null;
              
			try {
				reader = new RSSreader();
				content2 = reader.showPost(x);
			} catch (Exception e) {
			//	e.printStackTrace();
			}
	  			    	  StringBuffer readContent = new StringBuffer("<center><h1>"); 
		        	try {
						readContent.append(reader.showTitle(x));
					} catch (Exception e) {
						e.printStackTrace();
					}
		        	readContent.append("</h1></center>");
		        	readContent.append(content2);
              posttext.setText(readContent.toString());   
             
            }
        }
    });

	}
	
	public void refresh() throws IOException {		
		reader = new RSSreader();
		int numElements = reader.itemsNumber;
		String []tempo = new String[numElements];
		for(int i=0; i<tempo.length; i++)
			tempo[i] = reader.showTitle(i);
		titles.setListData(tempo);		
	}

	
//****************************************************************************************	
//	
//		The Main Method: Displays GUI of the Program
//
//**************************************************************************************** 	
	public static void main(String[] args) throws IOException, XmlRpcFault {
		        
		LightBlogging lb = new LightBlogging();
	
		lb.setSize(750, 500);
		lb.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		lb.setVisible(true);

		// If file with Properties Settings doesn't Exist in a Local Directory, it is Created on the First Launch
		if(!propFile.exists()) {
		    try {
				propFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
		

}