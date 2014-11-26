
//****************************************************************************************	
//	
//		WordPressClient Class allows to send new posts to a Wordpress blog,
//		to remove and edit exitsting posts.
//		
//		It uses the following third-party libraries:
//		Redstone XML-RPC Library (http://xmlrpc.sourceforge.net/)
//		wordpress-java (https://code.google.com/p/wordpress-java/)
//
//****************************************************************************************

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Properties;

import redstone.xmlrpc.XmlRpcFault;
import net.bican.wordpress.Page;
import net.bican.wordpress.Wordpress;


public class WordPressClient
{
	
//****************************************************************************************	
//	
//		References, Variables, etc.
//
//****************************************************************************************

	Properties prop = new Properties();
	InputStream input = null;
	
	private String LOGIN;
	private String PASSWORD;
	@SuppressWarnings("unused")
	private String BLOG_ID;
	private String URL;
	private String tempBlog;
	
	Wordpress wp;

	// temporary:
	List<Page> recentPosts;

//****************************************************************************************	
//	
//		Constructor for the WordPressClient Class
//
//****************************************************************************************
	
	WordPressClient() throws IOException, XmlRpcFault {
	
				    				      
//****************************************************************************************	
//	
//		Loading a Properties File
//
//****************************************************************************************

		input = new FileInputStream(LightBlogging.propFile);
	prop.load(input);
	tempBlog = prop.getProperty("bladdress");
	StringBuffer blogWrite = new StringBuffer("");
	if(!tempBlog.startsWith("http://"))
		blogWrite.append("http://");
	blogWrite.append(tempBlog);
	if(tempBlog.endsWith("/"))
		blogWrite.append("xmlrpc.php");
	if(!tempBlog.endsWith("/"))
		blogWrite.append("/xmlrpc.php");
	URL = blogWrite.toString();
	
	BLOG_ID = prop.getProperty("blid");
	LOGIN = prop.getProperty("bllogin");
	PASSWORD = prop.getProperty("blpassword");
	
	wp = new Wordpress(LOGIN, PASSWORD, URL);	

	}
	
//****************************************************************************************	
//	
//		Posting a New Post to a Blog
//
//****************************************************************************************
	public void posting(String newTitle, String newText) throws Exception
	{
 
	    Page newPost = new Page();

	    newPost.setTitle(newTitle);
	    newPost.setMt_allow_comments( 1 ); // Allow Comments to a Post
	    newPost.setMt_allow_pings( 0 ); // No ping	 
	    newPost.setDescription(newText);	
	    
	    wp.newPost(newPost, true);	    

	}
 
//****************************************************************************************	
//	
//		Deleting an Existing Post from a Blog
//
//****************************************************************************************
	public void deleting(int n) throws Throwable {
	  	 
		wp.deletePost(n, "true");
		finalize();
	}
 
//****************************************************************************************	
//	
//		Editing an Existing Blog Post
//
//****************************************************************************************
	public void editing(int n, String newTitle, String newText) throws XmlRpcFault {

     Page editedPost = new Page();
    
     editedPost.setTitle(newTitle);
     editedPost.setMt_allow_comments( 1 ); // Allow comments
     editedPost.setMt_allow_pings( 0 ); // No ping
     editedPost.setDescription(newText); 
     editedPost.setPost_status("publish");
     Integer nn = new Integer(n);
  //   System.out.println(editedPost.getDescription());
     wp.editPost(nn, editedPost, "true"); 

	}



public void recent() throws XmlRpcFault, MalformedURLException {
	System.out.println("Here are the ten recent posts:");
	wp = null;
	Wordpress wp = new Wordpress(LOGIN, PASSWORD, URL);
	recentPosts = wp.getRecentPosts(10);
    for (Page page : recentPosts) {
      System.out.println(page.getPostid() + ":" + page.getTitle());
    }
   
}
}