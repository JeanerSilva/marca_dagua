package com.example;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
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

public class App 
{

    private static File selectedFile = null;
    static String marcaDaguaString = "";
    
	public static void main(String[] args) throws IOException, DocumentException {
		
            SwingUtilities.invokeLater(() -> {
      try {
        createAndShowGUI();
      } catch (URISyntaxException e) {
        e.printStackTrace();
      }
    });
      
    
    }


 private static void createAndShowGUI() throws URISyntaxException {

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
      ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS
    );

    textArea.setFont(new Font("Arial", Font.BOLD, 14));
    textArea.setBounds(5, 100, 1170, 570);
    textArea.setEditable(false);
    frame.setLocationRelativeTo(null);
    panel.add(scrollPane, BorderLayout.CENTER);

    JSplitPane splitPane = new JSplitPane(
      JSplitPane.HORIZONTAL_SPLIT,
      layeredPane,
      panel
    );
    splitPane.setDividerLocation(300);
    frame.add(splitPane);

    JButton buttonSelecionaDiretorio = new JButton("Selecionar PDF");
    JButton buttonInserirMarca = new JButton("Inserir marca");
    JButton buttonLimpar = new JButton("Limpar");

    buttonSelecionaDiretorio.setBounds(30, 50, 150, 50); // x, y, largura, altura
    buttonInserirMarca.setBounds(30, 110, 150, 50); // x, y, largura, altura
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
        SwingUtilities.invokeLater(() ->
      textArea.append("Selecionado arquivo : " + selectedFile + "\n")
    );
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
          if (marcaDagua.getText().isEmpty()) {
            buttonInserirMarca.setEnabled(false);
          } else {
          buttonInserirMarca.setEnabled(true);
          }
      }
  
      @Override
      public void insertUpdate(final DocumentEvent paramDocumentEvent) {
         if (marcaDagua.getText().isEmpty()) {
            buttonInserirMarca.setEnabled(false);
            
          } else {
          marcaDaguaString = marcaDagua.getText();
          buttonInserirMarca.setEnabled(true);
          }
      }
  
      @Override
      public void changedUpdate(final DocumentEvent paramDocumentEvent) {
         if (marcaDagua.getText().isEmpty()) {
          
            buttonInserirMarca.setEnabled(false);
          } else {
          marcaDaguaString = marcaDagua.getText();
          buttonInserirMarca.setEnabled(true);
          }
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
        } else {
        }  
        return null;
  }

	}

