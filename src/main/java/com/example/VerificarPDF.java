package com.example;

import java.io.File;
import java.io.IOException;
import org.apache.tika.Tika;

public class VerificarPDF {
    public static void main(String[] args) throws IOException {

    }

    public boolean verificaSeArquivoEhPDF(String caminhoArquivo) throws IOException {
        File arquivo = new File(caminhoArquivo);
        Tika tika = new Tika();
        String tipo = tika.detect(arquivo);
        return "application/pdf".equals(tipo);
    }
}
