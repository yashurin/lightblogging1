
//****************************************************************************************	
//	
//		LocalFiles Class allows to save drafts for blog posts to a local directory
//		and to open locally saved drafts (in HTML format).
//
//****************************************************************************************

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import javax.swing.JFileChooser;


public class LocalFiles {

//****************************************************************************************	
//	
//		Variables and References
//
//****************************************************************************************
	static JFileChooser chooser = new JFileChooser();
	static String mytitle;
	static String mytext;	
	
//****************************************************************************************	
//	
//		This method converts draft into a HTML format and saves it locally
//
//****************************************************************************************	
	public static void writeFile(String name, String title1, String content) throws FileNotFoundException {

		StringBuffer buffer = new StringBuffer("");
		buffer.append("<html><title>");
		buffer.append(title1);
		buffer.append("</title><body>");
		buffer.append(content);
		buffer.append("</body></html>");
		String toFile = buffer.toString();
		PrintWriter out = new PrintWriter(name);
		out.println(toFile);
		out.close();
				
	}
	
//****************************************************************************************	
//	
//		This method gets a post title from a local draft
//
//****************************************************************************************	
	public static String getTitle(String buffer) {
        mytitle = "";
        if(buffer.contains("<title>")) {
            int firstPos = buffer.indexOf("<title>");
            String temp = buffer.substring(firstPos);
            temp = temp.replace("<title>", "");
            int lastPos = temp.indexOf("</title>");
            temp = temp.substring(0, lastPos);
            mytitle += temp+"\n";             
        }
		return mytitle;
	}
	
//****************************************************************************************	
//	
//		This method gets a post text from a local draft
//
//****************************************************************************************		
	public static String getContent(String buffer) {

        String mytext = "";
        if(buffer.contains("<body>")) {
            int firstPos = buffer.indexOf("<body>");
            String temp1 = buffer.substring(firstPos);
            temp1 = temp1.replace("<body>", "");
            int lastPos = temp1.indexOf("</body>");
            temp1 = temp1.substring(0, lastPos);
            mytext += temp1+"\n";             
        }

		return mytext;
	
	}
	
}
