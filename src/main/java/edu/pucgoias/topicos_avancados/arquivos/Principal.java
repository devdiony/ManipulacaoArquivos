package edu.pucgoias.topicos_avancados.arquivos;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException; // Importado para tratamento específico
import java.util.InputMismatchException; // Importado para o menu
import java.util.Scanner;

/**
 * Classe principal que gerencia a agenda em um arquivo de texto.
 * Esta classe foi refatorada para aplicar as melhorias da atividade.
 */
public class Principal {

    /**
     * CONCEITO: Encapsulamento (Parte 1.1)
     * O atributo é 'private' para que NENHUMA outra classe possa
     * alterar o nome do arquivo depois que o objeto 'Principal' é criado.
     * Isso garante que a classe sempre trabalhe com o mesmo arquivo.
     */
    private String nomeDoArquivo;

    /**
     * CONCEITO: Construtor (Parte 1.1)
     * A responsabilidade do construtor é inicializar o objeto.
     * Ele "força" quem o chama a fornecer um nome de arquivo,
     * garantindo que o objeto nasça em um estado válido.
     */
    public Principal(String nomeArquivo) {
        this.nomeDoArquivo = nomeArquivo;
    }

    /* métodos */

    /**
     * MELHORIA (Parte 1.2) e TRY-WITH-RESOURCES
     * * O código original usava try-catch-finally e fechava os recursos
     * (BufferedWriter, FileWriter) manualmente.
     * * Esta versão usa o 'try-with-resources', que é a forma moderna
     * e segura de lidar com arquivos em Java.
     * * 1. (fwArquivo, bw): São declarados dentro do parêntese do 'try'.
     * 2. O Java garante que, ao final do bloco try (ou se um erro ocorrer),
     * os métodos .close() de ambos serão chamados AUTOMATICAMENTE.
     * 3. Simplificamos a lógica de 'append': new FileWriter(fArquivo, true)
     * já faz o que é preciso (cria se não existe, e sempre anexa).
     */
    public void inserirDados(String registro) {
        File fArquivo = new File(this.nomeDoArquivo);

        // try-with-resources: Os recursos (fwArquivo, bw) são fechados sozinhos.
        try (FileWriter fwArquivo = new FileWriter(fArquivo, true);
             BufferedWriter bw = new BufferedWriter(fwArquivo)) {

            bw.write(registro + "\n");
            System.out.println("Registro adicionado com sucesso...");

        } catch (IOException e) { // CONCEITO: Exceção Específica (Parte 1.2)
            System.err.println("Erro ao inserir linhas no arquivo: " + e.getMessage());
        }
    }

    /**
     * MELHORIA (Parte 1.3): Leitura com try-with-resources
     * * Assim como no 'inserirDados', usamos o 'try-with-resources'
     * para o 'Scanner'. O bloco 'finally' original para fechar o
     * 'lendoArquivo' não é mais necessário, pois o Java faz isso
     * automaticamente.
     */
    public void listarDados() {
        File arquivo = new File(this.nomeDoArquivo);

        // try-with-resources: 'lendoArquivo' será fechado automaticamente.
        try (Scanner lendoArquivo = new Scanner(arquivo)) {

            System.out.println("\n--- Lista de Contatos ---");
            while (lendoArquivo.hasNextLine()) {
                // Chama o método refatorado (Exercício 2.1 e 2.3)
                this.processandoLinha(lendoArquivo.nextLine());
            }
            System.out.println("-------------------------");

        } catch (FileNotFoundException e) {
            System.err.println("Erro: arquivo nao existe. " + arquivo);
        }
    }

    /**
     * EXERCÍCIO 2.2: Implementando a Busca por Nome
     * * Este método é novo. Ele lê o arquivo linha por linha
     * e compara o nome com o termo de busca.
     */
    public void buscarDados(String nomeBusca) {
        File arquivo = new File(this.nomeDoArquivo);
        boolean encontrou = false;

        // try-with-resources para garantir que o Scanner feche
        try (Scanner lendoArquivo = new Scanner(arquivo)) {

            System.out.println("\n--- Resultado da Busca por \"" + nomeBusca + "\" ---");
            while (lendoArquivo.hasNextLine()) {
                String linha = lendoArquivo.nextLine();

                // Quebramos a linha e verificamos a segurança (similar ao 2.1)
                if (linha != null && !linha.trim().isEmpty()) {
                    String[] campos = linha.split(":");

                    if (campos.length >= 2) {
                        String nomeNoArquivo = campos[0].trim();

                        // A comparação usa .equalsIgnoreCase() para ignorar maiúsculas
                        if (nomeNoArquivo.equalsIgnoreCase(nomeBusca)) {
                            // Se encontrar, usa o processandoLinha para formatar
                            this.processandoLinha(linha);
                            encontrou = true;
                        }
                    }
                }
            }

            if (!encontrou) {
                System.out.println("Nenhum contato encontrado com este nome.");
            }
            System.out.println("-------------------------------------");

        } catch (FileNotFoundException e) {
            System.err.println("Erro: arquivo da agenda não existe. " + arquivo);
        }
    }

    /**
     * EXERCÍCIO 2.1 (Segurança) e 2.3 (Refatoração POO)
     * * Este método foi totalmente modificado para atender às tarefas.
     */
    private void processandoLinha(String linha) {
        // 1. Verifica se a linha não é nula ou vazia
        if (linha != null && !linha.trim().isEmpty()) {

            // 2. EXERCÍCIO 2.1: Adiciona try-catch para isolar erros
            try {
                String[] campos = linha.split(":");

                // 3. EXERCÍCIO 2.1: Verificação CRÍTICA
                //    Garante que temos pelo menos 2 campos (nome e telefone)
                if (campos.length >= 2) {

                    // 4. EXERCÍCIO 2.3: Refatoração de POO
                    //    Cria um objeto Contato com os dados
                    Contato c = new Contato(campos[0].trim(), campos[1].trim());

                    // 5. Imprime o objeto. O Java chama o c.toString()
                    System.out.println(c);

                } else {
                    // Avisa sobre a linha mal formatada, mas não quebra o programa
                    System.err.println("[ERRO] Linha com formato inválido no arquivo: " + linha);
                }
            } catch (Exception e) {
                // Captura qualquer outro erro inesperado (ex: split falha)
                System.err.println("[ERRO INESPERADO] Falha ao processar linha: " + linha);
            }
        }
    }

    /**
     * EXERCÍCIO 2.2: Menu Atualizado
     * O menu foi atualizado para incluir a opção "4 - Buscar por Nome"
     * e "5 - Sair".
     */
    public void menu() {
        Scanner teclado = new Scanner(System.in);
        int op = 0;
        do {
            System.out.println("\n..:: Trabalhando com Arquivos Texto ::..");
            System.out.println("1 - Inserir contato");
            System.out.println("2 - Listar todos os contatos");
            System.out.println("4 - Buscar por Nome");
            System.out.println("5 - Sair");
            System.out.print("Entre com uma opcao: ");

            try {
                op = teclado.nextInt();
                teclado.nextLine(); // Limpa o buffer do teclado (consome o "Enter")
            } catch (InputMismatchException e) {
                System.err.println("Erro: Por favor, digite apenas números.");
                teclado.nextLine(); // Limpa o buffer em caso de erro
                op = -1; // Define uma opção inválida para forçar o loop
            }

            switch (op) {
                case 1:
                    String nome;
                    String telefone;
                    System.out.println("Entre com os dados:");
                    System.out.print("Nome: ");
                    nome = teclado.nextLine();
                    System.out.print("Fone: ");
                    telefone = teclado.nextLine();
                    // Salva no formato "nome:telefone"
                    this.inserirDados(nome + ":" + telefone);
                    break;
                case 2:
                    this.listarDados();
                    break;
                case 4: // EXERCÍCIO 2.2: Lógica da Busca
                    System.out.print("Digite o nome para buscar: ");
                    String nomeBusca = teclado.nextLine();
                    this.buscarDados(nomeBusca);
                    break;
                case 5:
                    System.out.println("Saindo do sistema...");
                    break;
                case -1:
                    // Apenas ignora, o loop continua
                    break;
                default:
                    System.out.println("Opção inválida!");
            }

        } while (op != 5);

        teclado.close(); // Fecha o scanner do menu ao sair
    }

    public static void main(String[] args) {
        /**
         * MELHORIA: Caminho do Arquivo
         * * O caminho original "c:/dev/logs/agenda-poo.txt" só funciona
         * no computador do professor.
         * * Ao usar "agenda-poo.txt" (caminho relativo), o Java
         * cria o arquivo na pasta raiz do projeto, tornando
         * o programa portátil e funcional em qualquer computador.
         */
        Principal p = new Principal("agenda-poo.txt");
        p.menu();
    }
}