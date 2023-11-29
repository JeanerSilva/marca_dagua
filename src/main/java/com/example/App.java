package com.example;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.itextpdf.text.DocumentException;

public class App {

  private static File selectedFile = null;
  static String marcaDaguaString = "";

  public static void main(String[] args) throws IOException, DocumentException {
    String DIR = "";
    if (args.length == 0) {
      SwingUtilities.invokeLater(() -> {
        try {
          createAndShowGUI();
        } catch (Exception e) {
          e.printStackTrace();
        }
      });
    } else {
      if (args.length > 1) {
        if (null != args[1]) {
          String nomeDoArquivo = args[0];

          File file = new File(nomeDoArquivo);
          if (file.exists()) {
            if (new VerificarPDF().verificaSeArquivoEhPDF(nomeDoArquivo)) {
              String marca = " ";
              for (String arg : args) {
                if (arg != args[0])
                  marca += arg + " ";
              }
              System.out.println("\nCEPESC, 2023. Insere marca d'água em arquivos PDF. Versão 0.1 - 11421");
              System.out
                  .println("\nInserindo a marca d'água \"" + marca.trim() + "\" no arquivo " + nomeDoArquivo + ".");
              new CriaMarcaDagua().criaMarcaDaguaFile(marca, 6, 45, DIR + nomeDoArquivo, null);
            } else {
              System.out.println("\n\n\n O arquivo \"" + nomeDoArquivo + "\" não é um arquivo PDF válido.");
            }
          } else {
            System.out.println("\n\n\n Arquivo \"" + nomeDoArquivo + "\" não encontrado.\n\n\n");
          }
        }
      } else {
        System.out.println(
            "\n\n\nArgumentos incompletos. \nMarca d'água ausente. \nInforme: java -jar criamarcadagua.jar nome_do_arguivo.pdf \"marca a ser impressa\".\n\n\n");
      }

    }

  }

  private static void createAndShowGUI() {

    System.setProperty("awt.useSystemAAFontSettings", "on");
    System.setProperty("swing.aatext", "true");
    JFrame frame = new JFrame("Insere marca d'água");
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.setSize(new Dimension(1200, 600));

    JLayeredPane layeredPane = new JLayeredPane();
    layeredPane.setPreferredSize(new Dimension(200, 200));

    JPanel panel = new JPanel(new BorderLayout());
    JTextArea textArea = new JTextArea();
    textArea.setLineWrap(true);
    textArea.setWrapStyleWord(true);

    JScrollPane scrollPane = new JScrollPane(textArea);
    scrollPane.setVerticalScrollBarPolicy(
        ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

    textArea.setFont(new Font("Arial", Font.BOLD, 14));
    textArea.setBounds(5, 100, 1170, 570);
    textArea.setEditable(false);
    frame.setLocationRelativeTo(null);
    panel.add(scrollPane, BorderLayout.CENTER);

    JSplitPane splitPane = new JSplitPane(
        JSplitPane.HORIZONTAL_SPLIT,
        layeredPane,
        panel);
    splitPane.setDividerLocation(300);
    frame.add(splitPane);

    JButton buttonSelecionaDiretorio = new JButton("Selecionar PDF");
    JButton buttonInserirMarca = new JButton("Inserir marca");
    JButton buttonLimpar = new JButton("Limpar");

    buttonSelecionaDiretorio.setBounds(30, 50, 150, 50); 
    buttonInserirMarca.setBounds(30, 110, 150, 50); 
    buttonLimpar.setBounds(30, 170, 150, 50);

    JLabel marcaDaguaLabel = new JLabel("Marca d'água");
    marcaDaguaLabel.setBounds(30, 290, 200, 20);
    JTextArea marcaDagua = new JTextArea();
    marcaDagua.setBounds(30, 320, 250, 40);
    marcaDagua.setFont(new Font("Arial", Font.BOLD, 14));

    layeredPane.add(buttonSelecionaDiretorio, JLayeredPane.DEFAULT_LAYER);
    layeredPane.add(buttonInserirMarca, JLayeredPane.PALETTE_LAYER);
    layeredPane.add(buttonLimpar, JLayeredPane.PALETTE_LAYER);
    layeredPane.add(marcaDaguaLabel);
    layeredPane.add(marcaDagua);
    buttonInserirMarca.setEnabled(false);
    buttonLimpar.setEnabled(false);

    buttonSelecionaDiretorio.addActionListener(e -> {
      selectedFile = selecionarDiretorio(frame);
      if (selectedFile != null) {
        buttonLimpar.setEnabled(true);
        SwingUtilities.invokeLater(() -> textArea.append("Selecionado arquivo : " + selectedFile + "\n"));
      } else {
        buttonInserirMarca.setEnabled(false);
        buttonLimpar.setEnabled(false);
      }
    });

    buttonInserirMarca.addActionListener(e -> {
      new Thread(() -> {
        try {
          textArea.append("Iniciando o processo. Aguarde...");
          new CriaMarcaDagua().criaMarcaDaguaFile(marcaDaguaString, 6, 45, selectedFile.getAbsolutePath(), textArea);
        } catch (IOException | DocumentException e1) {
          e1.printStackTrace();
        }
      })
          .start();
    });

    buttonLimpar.addActionListener(e -> {
      textArea.setText("");
      selectedFile = null;
      buttonInserirMarca.setEnabled(false);
      buttonLimpar.setEnabled(false);
    });

    marcaDagua.getDocument().addDocumentListener(new DocumentListener() {
      @Override
      public void removeUpdate(final DocumentEvent paramDocumentEvent) {
        buttonInserirMarca.setEnabled(!marcaDagua.getText().isEmpty());
        marcaDaguaString = marcaDagua.getText();
      }

      @Override
      public void insertUpdate(final DocumentEvent paramDocumentEvent) {
        buttonInserirMarca.setEnabled(!marcaDagua.getText().isEmpty());
        marcaDaguaString = marcaDagua.getText();
      }

      @Override
      public void changedUpdate(final DocumentEvent paramDocumentEvent) {
        buttonInserirMarca.setEnabled(!marcaDagua.getText().isEmpty());
        marcaDaguaString = marcaDagua.getText();
      }
    });
    frame.setVisible(true);
  }

  private static File selecionarDiretorio(JFrame frame) {
    JFileChooser fileChooser = new JFileChooser();

    FileNameExtensionFilter filter = new FileNameExtensionFilter("Arquivos PDF", "pdf");
    fileChooser.setFileFilter(filter);

    int returnValue = fileChooser.showOpenDialog(frame);
    if (returnValue == JFileChooser.APPROVE_OPTION) {
      return fileChooser.getSelectedFile();
    }
    return null;
  }

}
