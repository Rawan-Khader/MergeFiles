import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.SequenceInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.logging.Logger;


public class Multipart {
	public static void main(String[] args) {
        try {
            Multipart.openStream("C:/Users/Rawan Khader/Desktop/New Folder");
        } catch (IOException ex) {
            
        }
    }

	public static InputStream openStream(String url) throws IOException {

		
//	     merge inputStreams into single stream
		 
    	File file = new File(url);
    	File outF = new File("C:/Users/Rawan Khader/Desktop/New Folder/assembled");
    	
		ArrayList<InformationFile> list = new ArrayList<InformationFile>();

		PrintWriter out = new PrintWriter(outF);
		
		 if(file!=null && file.exists()){
		        File[] listOfFiles = file.listFiles();

		        if(listOfFiles!=null){

		            for (int i = 0; i < listOfFiles.length; i++) {
		                if (listOfFiles[i].isFile()) {
		                	Scanner in = new Scanner(listOfFiles[i]);
		                	while(in.hasNext()){
		                		String line = in.next();
		                		out.print(line);
		                	}
		                	
		                	String NumberOfParts = null;
		        			String NameOfFile = null;
		        			ArrayList<String> links = new ArrayList<>();
                   list.add(new InformationFile(NameOfFile,NumberOfParts,links));
		                }
		            }
		        }
		    }
		 
		 InformationFile newFile =  list.get(0);
			boolean downloadedComplete = false;

		ArrayList<InputStream> stream = new ArrayList<InputStream>();
		
		
		for(String link : newFile.Link){
			try{
				stream.add(new URL(link).openStream());
				downloadedComplete = true;
				System.out.println(link);
			}catch(Exception e){
				continue;
			}
		}
		if(!downloadedComplete){
			throw new IOException("one of the file parts are currupted");
		}
		
		for (int i = 1; i < list.size(); i++) {

			InputStream inputStream = null;
			
			newFile =  list.get(i);
			downloadedComplete = false;
			
			for(String link : newFile.Link){
				try{
					inputStream = new URL(link).openStream();
					stream.add(inputStream);
					downloadedComplete = true;
					System.out.println(link);
					
					break;
				}catch(Exception e){
					continue;
				}
			}
			if(!downloadedComplete){
				throw new IOException("one of the file parts are currupted");
			}
			

		}// forloop files

		Enumeration en =  Collections.enumeration(stream);
		SequenceInputStream sequenceInputStream = new SequenceInputStream(en);
		
		return sequenceInputStream;
	}
		public static class InformationFile {

			String NameOfFile;
			String NumberOfParts;
			ArrayList<String> Link = new ArrayList<>();

			public InformationFile() {

			}

			public InformationFile(String nameOfFile, String numberOfParts, ArrayList<String> link) {
				NameOfFile = nameOfFile;
				NumberOfParts = numberOfParts;
				Link = link;
			}

			public String getFileName() {
				return NameOfFile;
			}
			
			public void setFileName(String nameOfFile) {
				NameOfFile = nameOfFile;
			}

			public String getPartNum() {
				return NumberOfParts;
			}

			public void setPartNum(String numberOfParts) {
				NumberOfParts = numberOfParts;
			}
			
			public ArrayList<String> getLinks() {
				return Link;
			}

			public void setLinks(ArrayList<String> link) {
				Link = link;
			}

		}
}
