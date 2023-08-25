package com.trybe.conversorcsv;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.text.MaskFormatter;



/**
 * programa serve para pegar os csv da pasta entradas 
 * arrumar os seus dados e mandar para a pasta saidas.
 */
public class Conversor {
  FileReader leitorArquivo = null; // criamos
  BufferedReader bufferedLeitor = null; // criamos
  FileWriter escritorArquivo = null; // criamos
  BufferedWriter bufferedEscritor = null; // criamos
  
  /**
   * Função utilizada apenas para validação da solução do desafio.
   *
   * @param args Não utilizado.
   * @throws IOException Caso ocorra algum problema ao ler os arquivos de entrada ou
   *                     gravar os arquivos de saída.
   */
  public static void main(String[] args) throws IOException {
    File pastaDeEntradas = new File("./entradas/");
    File pastaDeSaidas = new File("./saidas/");

    new Conversor().converterPasta(pastaDeEntradas, pastaDeSaidas);
  }

  /**
   * Converte todos os arquivos CSV da pasta de entradas. Os resultados são gerados
   * na pasta de saídas, deixando os arquivos originais inalterados.
   *
   * @param pastaDeEntradas Pasta contendo os arquivos CSV gerados pela página web.
   * @param pastaDeSaidas Pasta em que serão colocados os arquivos gerados no formato
   *                      requerido pelo subsistema.
   *
   * @throws IOException Caso ocorra algum problema ao ler os arquivos de entrada ou
   *                     gravar os arquivos de saída.
   */
  public void converterPasta(File pastaDeEntradas, File pastaDeSaidas) throws IOException {
    // TODO: Implementar.
    if (pastaDeEntradas.isDirectory() && pastaDeEntradas.canRead()) {
      for (File arquivo : pastaDeEntradas.listFiles()) {

        FileReader leitorArquivo = new FileReader(arquivo.getPath());
        BufferedReader bufferedLeitor = new BufferedReader(leitorArquivo);

        try {
          if (!pastaDeSaidas.exists() && !pastaDeSaidas.isDirectory()) {
            pastaDeSaidas.mkdir();
          }
          String nomeDoArquivoSaida = pastaDeSaidas + "/" + arquivo.getName();
          String conteudoLinha = bufferedLeitor.readLine();
          escritorArquivo = new FileWriter(nomeDoArquivoSaida);
          bufferedEscritor = new BufferedWriter(escritorArquivo);

          while (conteudoLinha != null) {
            System.out.println(conteudoLinha); // imprime tudo para observar no console.
            // coloca para pular uma linha no conteudo Linha.
            String conteudoDalinhaPulandoLinha = conteudoLinha + "\n";
            escreverConteudoNoArquivo(conteudoDalinhaPulandoLinha);
            conteudoLinha = bufferedLeitor.readLine();
            if (conteudoLinha != null) {
              conteudoLinha = nomeToUpperCase(conteudoLinha);
              conteudoLinha = dataHifenFormat(conteudoLinha);
              conteudoLinha = cpfFormat(conteudoLinha);
            }
          }
        } catch (IOException  e) {
          e.printStackTrace();
        } finally {
          this.fecharObjetos(leitorArquivo, bufferedLeitor, escritorArquivo, bufferedEscritor);
        }
      }
    } else {
      System.out.println("não é um diretorio");
    }
  }
  
  private String nomeToUpperCase(String conteudoLinha) {
    String []colunas = conteudoLinha.split(",");
    colunas[0] = colunas[0].toUpperCase();
    return String.join(",", colunas);
  }
  
  private String dataHifenFormat(String conteudoLinha) {
    String []colunas = conteudoLinha.split(",");
    String []data = colunas[1].split("/");
    String aaaammdd = data[2] + "-" + data[1] + "-" + data[0];
    colunas[1] = aaaammdd;
    return String.join(",", colunas);
  }
  
  private String cpfFormat(String conteudoLinha) {
    String []colunas = conteudoLinha.split(",");
    try {
      MaskFormatter formatter = new MaskFormatter("###.###.###-##");
      formatter.setValueContainsLiteralCharacters(false);
      String cpf  = formatter.valueToString(colunas[3]);
      colunas[3] = cpf;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return String.join(",", colunas);
  }
  
  /**
   * escreve no arquivo o conteudo da linha.
   */
  public void escreverConteudoNoArquivo(String conteudoDalinhaPulandoLinha) {
    try {
      // Inserindo o contéudo que será escrito para o buffer.
      bufferedEscritor.write(conteudoDalinhaPulandoLinha);
      // Obtendo o conteudo do bufferd e escrevendo no arquivo.
      bufferedEscritor.flush(); 
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  /**
   * fecha os readers, writers e respectivos buffers.
   */
  private void fecharObjetos(FileReader fileReader, BufferedReader bufferedReader,
      FileWriter filewriter, BufferedWriter bufferedWriter) {
    try {
      fileReader.close();
      bufferedReader.close();
      escritorArquivo.close();
      bufferedEscritor.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}