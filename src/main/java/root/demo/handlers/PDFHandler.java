package root.demo.handlers;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import root.demo.model.es.IndexUnit;

public class PDFHandler extends DocumentHandler {

	@Override
	public IndexUnit getIndexUnit(File file) {
		return null;
	}

	@Override
	public String getText(File file) {
		try {
			PDDocument pdDoc = PDDocument.load(file);
			PDFTextStripper textStripper = new PDFTextStripper();
			String text = textStripper.getText(pdDoc);
			return text;
		} catch (IOException e) {
			System.out.println("Greksa pri konvertovanju dokumenta u pdf");
		}
		return null;
	}

}
