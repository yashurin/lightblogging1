
//****************************************************************************************	
//	
//		RSSreader Class allows to read a blog via RSS,
//		to retrive, store and display titles and texts of blog posts.
//
//****************************************************************************************

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;  
import java.util.ArrayList;
import java.util.Properties;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.xml.parsers.DocumentBuilder;  
import javax.xml.parsers.DocumentBuilderFactory;  

import org.w3c.dom.Document;  
import org.w3c.dom.Element;  
import org.w3c.dom.NodeList;  
      
    public class RSSreader implements ActionListener {  
            
//****************************************************************************************	
//		
// 		Variables and References
//
//****************************************************************************************    	
       private URL rssURL;  
       private URL siteURL;
       
       public static ArrayList<String> headlines = new ArrayList<String>();    
       public static ArrayList<String> maintext = new ArrayList<String>();
       public static ArrayList<String> itemAddresses = new ArrayList<String>();
       public static ArrayList<String> allItems = new ArrayList<String>();
       
       public static String[] headlines1 = new String[10];
       
        String currentHeadline;
        String currentPost;
        String siteAddress;
        
        private String blogTempAddress;
        private String blogReadingAddress;
        
        Properties prop = new Properties();
        InputStream input = null;
        
        public int itemsNumber;
        
        
//****************************************************************************************	
//		
// 		Constructor for a RSSreader Class
//
//****************************************************************************************	       
        RSSreader() throws IOException {
        	
        	input = new FileInputStream(LightBlogging.propFile);		    				     
    		// load a properties file
    		prop.load(input);
    		
    		blogTempAddress = prop.getProperty("bladdress");
    		StringBuffer blogRead = new StringBuffer("");
    		if(!blogTempAddress.startsWith("http://"))
    			blogRead.append("http://");
    		blogRead.append(blogTempAddress);
    		siteAddress = blogRead.toString();
    		if(blogTempAddress.endsWith("/"))
    			blogRead.append("?feed=rss2");
    		else
    			blogRead.append("/?feed=rss2");
    		blogReadingAddress = blogRead.toString();    	
    	       showTitles();

       }  
    

       
        public void setURL(URL url) {  
            rssURL = url;  
         }    
       
        public URL getURL() throws MalformedURLException {
        	siteURL = new URL(siteAddress);
        	return siteURL;
        }

//****************************************************************************************	
//		
// 		This method parses RSS feed and adds elements to ArrayLists
//
//****************************************************************************************        
       public void writeFeed() throws MalformedURLException {  
    	   
    	  setURL(new URL(blogReadingAddress));  
    	      	  
          try {  
             DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();  
             Document doc = builder.parse(rssURL.openStream());  
      
             NodeList items = doc.getElementsByTagName("item");  
                          
            itemsNumber = items.getLength();
            
            for (int i = 0; i < itemsNumber; i++) {  
                Element item = (Element)items.item(i);  
                currentHeadline = getValue(item, "title");
                headlines.add(currentHeadline);
                headlines1[i] = currentHeadline;
        
                currentPost = getValue(item, "content:encoded");
                maintext.add(currentPost);
                
                String itemAddress = getValue(item, "guid"); // web address of a post
                itemAddresses.add(itemAddress);
                
                String itemNumber = itemAddress.replaceAll("\\D+","");                                        
                allItems.add(itemNumber);
                
                // Perhaps, other fields can be added - PubDate, Link                           
             }             
          } catch (Exception e) {  
        	  System.out.println("something happened");
        	    e.printStackTrace();  
          }  
       }  
 
//****************************************************************************************	
//		
//		Removes elements of a certain index from ArrayLists (when a post is deleted)
//
//****************************************************************************************        
       public void removeItem() throws MalformedURLException {
    	  
    	headlines = null;
    	maintext = null;
    	itemAddresses = null;
    	allItems = null;
    	   writeFeed();
    	
       }

       
//****************************************************************************************	
//		
//		Returns body of a post of a certain index
//
//****************************************************************************************        
       public String showPost(int n) {   
    	 ;
    	  return maintext.get(n);
       }
       
//****************************************************************************************	
//		
//		Returns title of a post of a certain index
//
//****************************************************************************************        
       public String showTitle(int n){   	     	   
    	   return headlines.get(n);
       }
       
//****************************************************************************************	
//		
//		Returns address of a post of a certain index (method not used)
//
//****************************************************************************************        
       public String showAddress(int n) {
    	   return itemAddresses.get(n);
       }
       
//****************************************************************************************	
//		
//		Returns unique ID of a post of a certain index
//
//****************************************************************************************        
       public int showItem(int n){   	   
    	   int tempItem = Integer.parseInt(allItems.get(n));
    	   return tempItem;
       }
       
      
//****************************************************************************************	
//		
//		Returns titles of posts as an array of strings
//
//****************************************************************************************       
       public String[] showTitles() {  
    	      	  
    	   return headlines.toArray(new String[20]);
       }
       
//****************************************************************************************	
//		
//		Returns value of a certain blog post element
//
//****************************************************************************************    
       public String getValue(Element parent, String nodeName) {  
           return parent.getElementsByTagName(nodeName).item(0).getFirstChild().getNodeValue();  
        }  
    
//****************************************************************************************	
//		
//		Methods for viewing a blog in an external (default) desktop browser
//
//****************************************************************************************         
       public void openWebpage(URI uri) {
    	   Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
    	   if(desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
    		   try {
    			   desktop.browse(uri);
    		   } catch(Exception e) {
    			   e.printStackTrace();
    		   }
    	   }
       }
       
       public void openWebpage(URL url) {
    	   try {
    		   openWebpage(url.toURI());
    	   } catch(URISyntaxException e) {
    		   e.printStackTrace();
    	   }
       }
       
       
       public void setTitle(int n, String title){   	     	   
    	  // @SuppressWarnings("unused")
    	   System.out.println(headlines.get(n));
    	   System.out.println("what?");
		String currentTitle = headlines.get(n);
    	   System.out.println(currentTitle);
    	   currentTitle = title;
       }
       
       public void setText(int n, String text){   	     	   
    	   @SuppressWarnings("unused")
		String currentText = maintext.get(n);
    	   currentText = text;
       }
       
       public void updateItem(int index, String title, String text) {
    	   @SuppressWarnings("unused")
		String currentTitle = headlines.get(index);
    	   currentTitle = title;
    	   @SuppressWarnings("unused")
		String currentText = maintext.get(index);
    	   currentText = text;;
       }
       
       @Override
       public void actionPerformed(ActionEvent e) {
     //  Do Nothing
   }
    
}  
