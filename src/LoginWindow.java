//****************************************************************************************	
//	
//		LoginWindow Class provides a GUI to enter or to edit blog settings
//
//****************************************************************************************

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;


@SuppressWarnings("serial")
public class LoginWindow extends JFrame {

//****************************************************************************************	
//	
//		GUI elements and References
//
//****************************************************************************************	
	JTextField blogField;
	JTextField loginField;
	JPasswordField passwordField;
	
	String setBlog;
	String setLogin;
	String setPassword;
	
//****************************************************************************************	
//	
//		Constructor for a LoginWindow Class
//
//****************************************************************************************	
	LoginWindow() {		
		super("Login Settings");
		
		Box box1 = Box.createHorizontalBox();
		JLabel blogLabel = new JLabel("Blog address:"); 
		blogField = new JTextField(20);
		box1.add(blogLabel);
		box1.add(Box.createHorizontalStrut(6));
		box1.add(blogField);
		
		Box box2 = Box.createHorizontalBox();
		JLabel loginLabel = new JLabel("Login:");
		loginField = new JTextField(20);
		
		box2.add(loginLabel);
		box2.add(Box.createHorizontalStrut(6));
		box2.add(loginField);
		
		Box box3 = Box.createHorizontalBox();
		JLabel passwordLabel = new JLabel("Password:");
		passwordField = new JPasswordField(20);
		box3.add(passwordLabel);
		box3.add(Box.createHorizontalStrut(6));
		box3.add(passwordField);
		
		Box box4 = Box.createHorizontalBox();
		JButton ok = new JButton("OK");
		JButton cancel = new JButton("Cancel");
		box4.add(Box.createHorizontalGlue());
		box4.add(ok);
		box4.add(Box.createHorizontalStrut(12));
		box4.add(cancel);
		
		loginLabel.setPreferredSize(passwordLabel.getPreferredSize());
		
		Box mainBox = Box.createVerticalBox();
		mainBox.setBorder(new EmptyBorder(12,12,12,12));
		
		mainBox.add(box1);
		mainBox.add(Box.createVerticalStrut(12));
		mainBox.add(box2);
		mainBox.add(Box.createVerticalStrut(12));
		mainBox.add(box3);
		mainBox.add(Box.createVerticalStrut(18));
		mainBox.add(box4);
		setContentPane(mainBox);
		pack();
		setResizable(false);
		
//****************************************************************************************	
//		
// 		Adding New Settings or Changing Existing Settings
//
//****************************************************************************************
		ok.addActionListener(new ActionListener() { 
	        @Override
	        public void actionPerformed(ActionEvent e) {	       
	        	try {	    		
	        		setBlog = blogField.getText();
	        		setLogin = loginField.getText();
	        		setPassword = new String(passwordField.getPassword()); 
	        		@SuppressWarnings("unused")
					LoginInfo li = new LoginInfo(setBlog, "0", setLogin, setPassword); 
	        		
	        	} catch (Exception ee) {
	    			ee.printStackTrace();
	    		} finally {
	    			dispose();
	    		}
	        	
	        }
	    });
		

//****************************************************************************************	
//		
// 		Do nothing and close the frame
//
//****************************************************************************************		
		cancel.addActionListener(new ActionListener() { 
	        @Override
	        public void actionPerformed(ActionEvent e) {
	        	blogField.setText("");
	        	loginField.setText("");
	        	passwordField.setText("");
	        	dispose();	        	
	        }
	    });
		
	}

}
