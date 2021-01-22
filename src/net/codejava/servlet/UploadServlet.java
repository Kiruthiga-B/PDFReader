package net.codejava.servlet;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

@WebServlet("/UploadServlet")
@MultipartConfig(fileSizeThreshold=1024*1024*50,	// 50MB
				 maxFileSize=1024*1024*100,		// 100MB
				 maxRequestSize=1024*1024*500)	// 500MB
public class UploadServlet extends HttpServlet {


	/**
	 * handles file upload
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {


		for (Part part : request.getParts()) {
			String fileName = extractFileName(part);
			//Loading the document uuploaded by user
		      File file = new File(fileName);
		      PDDocument document = PDDocument.load(file);
		      //Instantiate PDFTextStripper class
		      PDFTextStripper pdfStripper = new PDFTextStripper();
		      //Retrieving text from PDF document
		      String text = pdfStripper.getText(document);
		      fileName = new File(fileName).getName();
		      String FileName =(fileName.substring(0, fileName.length() - 4));
		      PrintStream o = new PrintStream(new File("D:\\"+FileName+".txt")); 
		      System.setOut(o); 
		      System.out.println(text);
		      //Closing the document
		      document.close();
		}

		request.setAttribute("message", "PDF-Text Conversion has been done successfully! File will be saved in you D-Drive");
		getServletContext().getRequestDispatcher("/message.jsp").forward(
				request, response);
	}

	
	//Extracts file name from HTTP header content-disposition
	private String extractFileName(Part part) {
		String contentDisp = part.getHeader("content-disposition");
		String[] items = contentDisp.split(";");
		for (String s : items) {
			if (s.trim().startsWith("filename")) {
				return s.substring(s.indexOf("=") + 2, s.length()-1);
			}
		}
		return "";
	}
}
