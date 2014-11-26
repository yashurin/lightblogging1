//****************************************************************************************	
//	
//		Object of the LoginInfo Class stores blog settings,
//		saves them into a local file and provides methods to access those settings.
//
//****************************************************************************************

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;


public class LoginInfo {
	
//****************************************************************************************	
//	
//		Variables and References
//
//**************************************************************************************** 	
	Properties prop = new Properties();
	OutputStream output = null;
	InputStream input = null;
			
	// URL of a Blog	
	private String blog;
	
	// URL for Posting to a Blog
	private String blogAddr;
	
	// URL for Reading RSS of a Blog
	private String blogRSS;
	
	// ID of a Blog	
	@SuppressWarnings("unused")
	private String blogId;
	
	// Blog Login
	private String login;
	
	// Blog Password as a String
	@SuppressWarnings("unused")
	private String passwordString;
		
	// Password as a Character Array
	char[] passArr;
	
//****************************************************************************************	
//	
//		Constructor for a LoginInfo Class takes parameters from LoginWindow Input Fields
//
//****************************************************************************************	
	public LoginInfo(String blog, String blogId, String login, String passwordString) {
		this.blog = blog;
		this.login = login;
		this.passwordString = passwordString;
		this.blogId = blogId;
		
		try {			 

			output = new FileOutputStream(LightBlogging.propFile);

			// Set the Properties Values
			prop.setProperty("bladdress", blog);
			prop.setProperty("bllogin", login);
			prop.setProperty("blpassword", passwordString);
			prop.setProperty("blid", blogId);
	 
			// save properties to project root folder
			prop.store(output, null);
	 
			} catch (Exception io) {
				io.printStackTrace();
			} finally {
			if (output != null) {
				try {
					output.close();
				} catch (Exception e) {
					e.printStackTrace();
					}
				}	 
			}
		}
		
//****************************************************************************************	
//	
//		This method sets a blog URL
//
//****************************************************************************************	
	public void setBlogAddress(String blogAddrTemp) {
		blog = blogAddrTemp;
	}
	
//****************************************************************************************	
//	
//		This method returns an address for posting into Blog
//
//****************************************************************************************	
	public String getBlogWritingAddress() throws IOException {
		input = new FileInputStream(LightBlogging.propFile);		    				     
		// Load a properties file
		prop.load(input);
		blog = prop.getProperty("bladdress");
		StringBuffer blogWrite = new StringBuffer("");
		if(!blog.startsWith("http://"))
			blogWrite.append("http://");
		blogWrite.append(blog);
		if(blog.endsWith("/"))
			blogWrite.append("xmlrpc.php");
		if(!blog.endsWith("/"))
			blogWrite.append("/xmlrpc.php");
		blogAddr = blogWrite.toString();
		return blogAddr;
	}
	

//****************************************************************************************	
//	
//		This method returns an address for reading of Blog RSS
//
//****************************************************************************************	
	public String getBlogReadingAddress() throws IOException {
		input = new FileInputStream(LightBlogging.propFile);		    				     
		// load a properties file
		prop.load(input);
		blog = prop.getProperty("bladdress");
		StringBuffer blogRead = new StringBuffer("");
		if(!blog.startsWith("http://"))
			blogRead.append("http://");
		blogRead.append(blog);
		if(blog.endsWith("/"))
			blogRead.append("?feed=rss");
		else
			blogRead.append("/?feed=rss");
		blogRSS = blogRead.toString();
		return blogRSS;		
	}


//****************************************************************************************	
//	
//		This method returns URL that has been imputed
//
//****************************************************************************************
	public String getBlogAddress() {
		return blog;
	}
	
	public String getLogin() {
		return login;
	}
	public void setLogin(String loginTemp) {
		login = loginTemp;
	}
	public String getBlogPassword() {
		String blogpass = passArr.toString();
		
		return blogpass;
	}
	
//****************************************************************************************	
//	
//		This method converts a password character array to a string
//
//****************************************************************************************
	public void setBlogPassword(char tempArr[]) {
		passArr = tempArr;
		System.out.println(passArr.toString());
	}

	public String getRSS() {
		String rssString;
		rssString = blog.concat("?feed=rss");
	//	rssString = "https://vn-mir/feed";
			
		return rssString;
	}
	
}
