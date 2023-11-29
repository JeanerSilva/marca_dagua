package com.example;

import java.io.*;

import javax.swing.JTextArea;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.pdf.*;

public class CriaMarcaDagua {

	public void criaMarcaDaguaFile(String text, Integer linha, Integer rotation, String src, JTextArea textArea)
			throws IOException, DocumentException {
		String nomeDoArquivo = src.substring(0, src.lastIndexOf('.'));
		String extensao = src.substring(src.lastIndexOf('.') + 1);
		String novoNomeDoArquivo = nomeDoArquivo + "_" +
				text.trim().replaceAll("[^a-zA-Z0-9 ]", "_") + "_marca_dagua" + "." + extensao;
		PdfReader reader = new PdfReader(src);
		PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(novoNomeDoArquivo));
		int n = reader.getNumberOfPages();
		Integer fontsize = 10;
		Font f = new Font(FontFamily.HELVETICA, fontsize);
		Phrase p;
		PdfGState gs1 = new PdfGState();
		gs1.setFillOpacity(0.2f);
		PdfContentByte over;
		com.itextpdf.text.Rectangle pagesize;
		float x = 0, y = 0;
		reader = new PdfReader(src);
		stamper = new PdfStamper(reader, new FileOutputStream(novoNomeDoArquivo));
		if (null != textArea) {
			textArea.append("\nInserindo a marca " + text + " no arquivo " + novoNomeDoArquivo);
		}
		System.out.print("\nPágina(s): ");
		if (null != textArea) {
			textArea.append("\nPágina(s): ");
		}
		for (int i = 1; i <= n; i++) {
			if (i != n)
				System.out.print(i + ", ");
			else
				System.out.print(i + ".");
			if (null != textArea) {
				if (i != n)
					textArea.append(i + ", ");
				else
					textArea.append(i + ".");
			}
			pagesize = reader.getPageSizeWithRotation(i);
			float vertical = (float) (pagesize.getTop() * (1 + Math.sin(rotation)));
			String textoAserImpresso = text;
			for (int w = 1; w < pagesize.getRight() * vertical + textoAserImpresso.length() * fontsize; w = w
					+ textoAserImpresso.length() * fontsize) {
				textoAserImpresso += " " + textoAserImpresso;
			}
			for (int j = 1; j <= pagesize.getTop()
					+ vertical; j = (int) (j + (fontsize * linha) * (1 + Math.sin(rotation)))) {
				y = j;
				p = new Phrase(textoAserImpresso, f);
				over = stamper.getOverContent(i);
				over.saveState();
				over.setGState(gs1);
				ColumnText.showTextAligned(over, Element.ALIGN_JUSTIFIED, p, x, (float) (y - vertical), rotation);
				over.restoreState();
			}

		}
		stamper.close();
		reader.close();
		System.out.print("\n\nMarca inserida com sucesso.\n\n");
		if (null != textArea) {
			textArea.append("\nSalvo arquivo com nome" + novoNomeDoArquivo + ".\n");
		}
		if (null != textArea) {
			textArea.append("Marca d'agua inserida com sucesso.\n");
		}

	}

}