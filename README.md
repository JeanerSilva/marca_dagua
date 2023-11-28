# Cria Marca D'água
Arquivo .jar que cria marcas d'água em arquivos PDF

Insere marca d'água de 45 graus ao longo de todas as páginas PDF

## Compilação

Para compilar usar: mvn clean package


## Utilização

java -jar insere_marca_dagua.jar <nome_do_arquivo.pdf> "marca d'água a ser inserida no arquivo"

java -jar target/insere_marca_dagua.jar teste.pdf "marca d'água de teste"

## Correções

- permitir que o nome do arquivo contenha espaços em branco

## Melhorias

- permitir indicar o número da página que se quer colocar a marca
- permitir criar marcas a partir de um arquivo contento a lista de marcas a serem inseridas em um mesmo arquivo pdf
- permitir forçar uma inserção de marca ainda que o arquivo não seja detectado como pdf

