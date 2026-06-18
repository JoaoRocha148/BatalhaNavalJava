import java.util.Scanner;

public class BatalhaNaval {

    static final int TAMANHO_TABULEIRO = 10;
    static final char AGUA = '~';
    static final char NAVIO = 'N';

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        char[][] tabuleiroReal = new char[TAMANHO_TABULEIRO][TAMANHO_TABULEIRO];
        char[][] tabuleiroAtaques = new char[TAMANHO_TABULEIRO][TAMANHO_TABULEIRO];

        inicializarTabuleiro(tabuleiroReal);
        inicializarTabuleiro(tabuleiroAtaques);

        System.out.println("=== BATALHA NAVAL ===");
        System.out.println("Parte do Criador: posicionamento dos navios");
        System.out.println();

        posicionarNavio(scanner, tabuleiroReal, "Porta Aviões", 4);

        posicionarNavio(scanner, tabuleiroReal, "Fragata", 3);

        for (int i = 1; i <= 3; i++) {
            posicionarNavio(scanner, tabuleiroReal, "Submarino " + i, 2);
        }

        for (int i = 1; i <= 3; i++) {
            posicionarNavio(scanner, tabuleiroReal, "Bote " + i, 1);
        }

        System.out.println();
        System.out.println("Tabuleiro final do Criador:");
        exibirTabuleiro(tabuleiroReal);

        System.out.println();
        System.out.println("Agora o tabuleiro será escondido do Atacante.");

        esconderTabuleiro();

        System.out.println("Parte do Atacante será implementada pelo outro integrante.");
        System.out.println("Aqui entrará o sistema de ataques usando o tabuleiro de ataques.");
        System.out.println();

        scanner.close();
    }

    public static void inicializarTabuleiro(char[][] tabuleiro) {
        for (int linha = 0; linha < TAMANHO_TABULEIRO; linha++) {
            for (int coluna = 0; coluna < TAMANHO_TABULEIRO; coluna++) {
                tabuleiro[linha][coluna] = AGUA;
            }
        }
    }

    public static void exibirTabuleiro(char[][] tabuleiro) {
        System.out.print("   ");

        for (int coluna = 0; coluna < TAMANHO_TABULEIRO; coluna++) {
            System.out.print(coluna + " ");
        }

        System.out.println();

        for (int linha = 0; linha < TAMANHO_TABULEIRO; linha++) {
            System.out.print(linha + "  ");

            for (int coluna = 0; coluna < TAMANHO_TABULEIRO; coluna++) {
                System.out.print(tabuleiro[linha][coluna] + " ");
            }

            System.out.println();
        }
    }

    public static void posicionarNavio(Scanner scanner, char[][] tabuleiro, String nomeNavio, int tamanhoNavio) {
        boolean posicionado = false;

        while (!posicionado) {
            System.out.println();
            System.out.println("Posicionando: " + nomeNavio + " | Tamanho: " + tamanhoNavio);
            exibirTabuleiro(tabuleiro);

            int linha = lerInteiro(scanner, "Digite a linha inicial entre 0 e 9: ");
            int coluna = lerInteiro(scanner, "Digite a coluna inicial entre 0 e 9: ");

            System.out.println("Direções disponíveis:");
            System.out.println("H = Horizontal");
            System.out.println("V = Vertical");
            System.out.println("D = Diagonal para direita");
            System.out.println("E = Diagonal para esquerda");

            char direcao = lerDirecao(scanner, "Digite a direção: ");

            if (validarPosicionamento(tabuleiro, linha, coluna, tamanhoNavio, direcao)) {
                for (int i = 0; i < tamanhoNavio; i++) {
                    int novaLinha = linha;
                    int novaColuna = coluna;

                    if (direcao == 'H') {
                        novaColuna = coluna + i;
                    } else if (direcao == 'V') {
                        novaLinha = linha + i;
                    } else if (direcao == 'D') {
                        novaLinha = linha + i;
                        novaColuna = coluna + i;
                    } else if (direcao == 'E') {
                        novaLinha = linha + i;
                        novaColuna = coluna - i;
                    }

                    tabuleiro[novaLinha][novaColuna] = NAVIO;
                }

                System.out.println(nomeNavio + " posicionado com sucesso!");
                posicionado = true;
            } else {
                System.out.println("Posição inválida!");
                System.out.println("O navio não pode sair do tabuleiro nem sobrepor outro navio.");
                System.out.println("Tente novamente.");
            }
        }
    }

    public static boolean validarPosicionamento(char[][] tabuleiro, int linha, int coluna, int tamanhoNavio, char direcao) {
        for (int i = 0; i < tamanhoNavio; i++) {
            int novaLinha = linha;
            int novaColuna = coluna;

            if (direcao == 'H') {
                novaColuna = coluna + i;
            } else if (direcao == 'V') {
                novaLinha = linha + i;
            } else if (direcao == 'D') {
                novaLinha = linha + i;
                novaColuna = coluna + i;
            } else if (direcao == 'E') {
                novaLinha = linha + i;
                novaColuna = coluna - i;
            } else {
                return false;
            }

            if (novaLinha < 0 || novaLinha >= TAMANHO_TABULEIRO || novaColuna < 0 || novaColuna >= TAMANHO_TABULEIRO) {
                return false;
            }

            if (tabuleiro[novaLinha][novaColuna] == NAVIO) {
                return false;
            }
        }

        return true;
    }

    public static int lerInteiro(Scanner scanner, String mensagem) {
        while (true) {
            System.out.print(mensagem);

            if (scanner.hasNextInt()) {
                int valor = scanner.nextInt();

                if (valor >= 0 && valor < TAMANHO_TABULEIRO) {
                    return valor;
                } else {
                    System.out.println("Entrada inválida. Digite um número entre 0 e 9.");
                }
            } else {
                System.out.println("Entrada inválida. Digite apenas números.");
                scanner.next();
            }
        }
    }

    public static char lerDirecao(Scanner scanner, String mensagem) {
        while (true) {
            System.out.print(mensagem);

            String entrada = scanner.next().toUpperCase();

            if (entrada.length() == 1) {
                char direcao = entrada.charAt(0);

                if (direcao == 'H' || direcao == 'V' || direcao == 'D' || direcao == 'E') {
                    return direcao;
                }
            }

            System.out.println("Direção inválida. Use H, V, D ou E.");
        }
    }

    public static void esconderTabuleiro() {
        for (int i = 0; i < 30; i++) {
            System.out.println();
        }
    }
}