package com.search.util.pdf;

import com.search.util.framework.DocumentHandler;
import com.search.util.framework.DocumentHandlerException;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.DecryptionMaterial;
import org.apache.pdfbox.pdmodel.encryption.PublicKeyDecryptionMaterial;
import org.apache.pdfbox.pdmodel.encryption.StandardDecryptionMaterial;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.exceptions.InvalidPasswordException;
import org.apache.pdfbox.exceptions.CryptographyException;
import org.apache.pdfbox.util.PDFTextStripper;

public class PDFBoxPDFHandler implements DocumentHandler {

  public Document getDocument(InputStream is)
    throws DocumentHandlerException {
 
    PDDocument document = null;
	String docText = null;
    try{
        document = PDDocument.load( is );

		// extract PDF document's textual content
		PDFTextStripper stripper = new PDFTextStripper();
		docText = stripper.getText(document);
    }
    catch (Exception e) {		
      throw new DocumentHandlerException(
        "Cannot parse PDF document", e);
    }
	
    Document doc = new Document();
    if (docText != null) {
	  doc.add(new Field("body", docText,Field.Store.YES, Field.Index.ANALYZED));
    }

    // extract PDF document's meta-data
    PDDocument pdDoc = null;
    try {
      pdDoc = document;
      PDDocumentInformation docInfo =
          pdDoc.getDocumentInformation();
      String author   = docInfo.getAuthor();
      String title    = docInfo.getTitle();
      String keywords = docInfo.getKeywords();
      String summary  = docInfo.getSubject();
      if ((author != null) && (!author.equals(""))) {
	    doc.add(new Field("author", author,Field.Store.YES, Field.Index.ANALYZED));		  
      }
      if ((title != null) && (!title.equals(""))) {
	     doc.add(new Field("title", title,Field.Store.YES, Field.Index.ANALYZED));			
      }
      if ((keywords != null) && (!keywords.equals(""))) {
	     doc.add(new Field("keywords", keywords,Field.Store.YES, Field.Index.ANALYZED));			
      }
      if ((summary != null) && (!summary.equals(""))) {
	     doc.add(new Field("summary", summary,Field.Store.YES, Field.Index.ANALYZED));		
      }
		
		pdDoc.close();
    }
    catch (Exception e) {
      closePDDocument(pdDoc);
      System.err.println("Cannot get PDF document meta-data: "
        + e.getMessage());
    }

    return doc;
  }

  private void closePDDocument(PDDocument pdDoc) {
    if (pdDoc != null) {
      try {
        pdDoc.close();
      }
      catch (IOException e) {
      }
    }
  }

  public static void main(String[] args) throws Exception {
    PDFBoxPDFHandler handler = new PDFBoxPDFHandler();
    Document doc =
      handler.getDocument(new FileInputStream(new File(args[0])));
    System.out.println(doc);
  }
}

