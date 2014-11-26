//****************************************************************************************	
//	
//		EditingWindow Class provides a GUI for editing or deleting an existing blog post
//
//****************************************************************************************


import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import redstone.xmlrpc.XmlRpcFault;
import net.bican.wordpress.Wordpress;

@SuppressWarnings("serial")
public class EditingWindow extends JFrame {
		
//****************************************************************************************	
//	
//		GUI elements and References
//
//****************************************************************************************	
	private JTextField title;
	private JEditorPane mainText;
	private JPanel buttons;
	
	private JButton update; 
	private JButton delete;
	private JButton cancel; 
	
	private String tempTitle;
	private String tempText;
	
	private int postNum;
	private int postId;
	
	Wordpress wp;
	WordPressClient wpc;
	
	RSSreader reader;

	LightBlogging lb = new LightBlogging();
//****************************************************************************************	
//	
//		Constructor for an EditingWindow Class takes as parameters ID and an index of a blog post
//
//****************************************************************************************	
	public EditingWindow(int n, int m) throws IOException, XmlRpcFault {
		super("Edit Post");
        setBounds(200, 200, 600, 400);
        
        
        reader = new RSSreader();
        
        JPanel panel = new JPanel();
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.setLayout(new BorderLayout(10, 10));
        
        title = new JTextField(50);
        panel.add(title, BorderLayout.NORTH);
        
        mainText = new JEditorPane();
        
		JScrollPane textPane = new JScrollPane(mainText);
		mainText.setContentType("text");
		mainText.setEditable(true);
		mainText.setText("");
        panel.add(textPane, BorderLayout.CENTER);
        
        update = new JButton("Update");
        delete = new JButton("Delete");
        cancel = new JButton("Cancel");
        
        buttons = new JPanel();
        
        buttons.setLayout(new FlowLayout(FlowLayout.LEADING));
        
        buttons.add(update);
     //   buttons.add(delete);
        buttons.add(cancel);
        
        panel.add(buttons, BorderLayout.SOUTH);
        
        setContentPane(panel);
        
        this.postNum = n;
        this.postId = m; 
        
   //    RSSreader reader = new RSSreader(); 
        
        tempTitle = reader.showTitle(postNum);
               
        tempText = reader.showPost(postNum);
               
     //   String ending = "<a rel=\"nofollow\"";
    //	int firstPos = tempText.indexOf(ending);	
    //	tempText = tempText.substring(0, firstPos);      
        title.setText(tempTitle);
        mainText.setText(tempText);
        
     
                
//****************************************************************************************	
//		
// 		This method updates an existing blog post
//
//****************************************************************************************         
	update.addActionListener(new ActionListener() { 
        @Override
        public void actionPerformed(ActionEvent e) {
        String newTitle = title.getText();
        String newText = mainText.getText();
                    	
        try {
        	wpc = new WordPressClient();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (XmlRpcFault e1) {
			e1.printStackTrace();
		}
        try {
			wpc.editing(postId, newTitle, newText);
			reader.setTitle(postId, newTitle);
			reader.setText(postId,  newText);
			// reader = null;
			reader.writeFeed();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
       finally {
    	   try {
	//		reader = new RSSreader();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	/*
    	   
    	   String []tempo = new String[20];
			for(int i=0; i<tempo.length; i++)
				tempo[i] = reader.showTitle(i);
		
    	   	lb.titles.setListData(tempo);
    	   */
    	   dispose();
       }
        }
	});    
	
//****************************************************************************************	
//	
//		This method deletes an existing blog post
//
//**************************************************************************************** 	
	delete.addActionListener(new ActionListener() { 
        @Override
        public void actionPerformed(ActionEvent e) {        	
			try {
				wpc = new WordPressClient();
			} catch (IOException e2) {
				e2.printStackTrace();
			} catch (XmlRpcFault e1) {
				e1.printStackTrace();
			}			
				try {
					wpc.deleting(postId);
				//	reader.removeItem(postNum);
				//	 reader = null;
					reader.removeItem();
					reader.writeFeed();
					
				} catch (Throwable e1) {
					e1.printStackTrace();
				}
				finally {
					try {
				//		reader = new RSSreader();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				/*	try {
						lb.refresh();
					} catch (IOException e1) {
						e1.printStackTrace();
					}*/
					dispose();
				}
			
        }
	});    
	 
//****************************************************************************************	
//	
//		Don't Do Anything and Exit the Editing Window
//
//**************************************************************************************** 	
	cancel.addActionListener(new ActionListener() { 
        @Override
        public void actionPerformed(ActionEvent e) {       
        dispose();
        }
		});    	
	
	
	}		
}