package edu.pucgoias.topicos_avancados.arquivos;

/**
 * EXERCÍCIO 2.3: Inversão de Papéis (Refatoração de POO)
 * * Esta classe é criada para assumir a responsabilidade de representar
 * um único registro da agenda.
 * * ANTES: A classe Principal tentava formatar a linha ("Nome: ... Fone: ...")
 * DEPOIS: A classe Contato cuida dos seus próprios dados (nome, telefone)
 * e sabe como se formatar (toString).
 * * Isso é um pilar do POO: Coesão e Separação de Responsabilidades.
 */
public class Contato {

    /**
     * CONCEITO: Encapsulamento
     * Os atributos são 'private' para proteger os dados.
     * O acesso a eles é feito apenas por métodos (construtor e getters).
     */
    private String nome;
    private String telefone;

    /**
     * Construtor que inicializa o objeto Contato em um estado válido.
     */
    public Contato(String nome, String telefone) {
        this.nome = nome;
        this.telefone = telefone;
    }

    // Métodos Getters para permitir a leitura (mas não a alteração)
    public String getNome() {
        return nome;
    }

    public String getTelefone() {
        return telefone;
    }

    /**
     * CONCEITO: Sobrescrita (Override) e Polimorfismo
     * Sobrescrevemos o método toString() da classe Object.
     * * Agora, quando fazemos System.out.println(contato), o Java
     * automaticamente chama este método para "perguntar" ao objeto
     * como ele deve ser representado em forma de texto.
     * * Usamos String.format() para criar uma string formatada e alinhada.
     * "%-30s" significa: alinhe esta string (s) à esquerda (-) em um
     * espaço total de 30 caracteres.
     */
    @Override
    public String toString() {
        return String.format("Nome: %-30sFone: %s", this.nome, this.telefone);
    }
}