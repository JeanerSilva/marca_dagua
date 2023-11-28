package com.example;

import java.io.File;
import java.io.IOException;
import org.apache.tika.Tika;

public class VerificarPDF {
    public static void main(String[] args) throws IOException {
        //String caminhoArquivo = "/home/jeaner/marca/marcadagua/Testepdf.pdf";
        //verificaSeArquivoEhPDF(caminhoArquivo);
    }

    public boolean verificaSeArquivoEhPDF(String caminhoArquivo) throws IOException {
        File arquivo = new File(caminhoArquivo);
        Tika tika = new Tika();
        String tipo = tika.detect(arquivo);
        return "application/pdf".equals(tipo);
    }
}

/* 
    public static void main(String[] args) throws IOException {
        String caminhoArquivo = "/home/jeaner/marca/marcadagua/Testepdf.pdf";
        File arquivo = new File(caminhoArquivo);
        MediaType pdf = new MediaType("application", "pdf");
        Detector detector = new MagicDetector(pdf, "(?s)\\A.{0,144}%PDF-"
            .getBytes(StandardCharsets.US_ASCII), null, true, 0, 0);
        Metadata metadata = new Metadata();
        ParseContext parseContext = new ParseContext();
        Parser parser = new AutoDetectParser(detector);

        try {
            parser.parse(arquivo.toURI().toURL().openStream(), new org.apache.tika.sax.BodyContentHandler(), metadata, parseContext);
            MediaType mediaType = MediaType.parse(metadata.get(Metadata.CONTENT_TYPE));
            if ("application/pdf".equals(mediaType)) {
                System.out.println("O arquivo é um PDF.");
            } else {
                System.out.println("O arquivo não é um PDF.");
            }
        } catch (Exception e) {
            System.out.println("Erro ao verificar o arquivo: " + e.getMessage());
        }
    }

*/