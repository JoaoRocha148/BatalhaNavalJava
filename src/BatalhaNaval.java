import java.util.Scanner;

public class BatalhaNaval {

    static final int TAMANHO_TABULEIRO = 10;
    static final char AGUA = '~';
    static final char NAVIO = 'N';
    static final char ACERTO = 'X';
    static final char ERRO = 'O';
    static final int LIMITE_TENTATIVAS = 40;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        char[][] tabuleiroReal = new char[TAMANHO_TABULEIRO][TAMANHO_TABULEIRO];
        char[][] tabuleiroAtaques = new char[TAMANHO_TABULEIRO][TAMANHO_TABULEIRO];

        inicializarTabuleiro(tabuleiroReal);
        inicializarTabuleiro(tabuleiroAtaques);

        System.out.println("=== BATALHA NAVAL ===");
        System.out.println("Criador: posicione todos os navios no tabuleiro.");
        System.out.println("Legenda: ~ = agua | N = navio | X = acerto | O = erro");

        posicionarNavio(scanner, tabuleiroReal, "Porta Avioes", 4);
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
        System.out.println("O tabuleiro real sera escondido antes da fase do Atacante.");
        pausar(scanner, "Pressione ENTER para esconder o tabuleiro e iniciar os ataques...");
        esconderTabuleiro();

        int totalPartesNavios = contarPartesDeNavios(tabuleiroReal);
        int acertos = 0;
        int tentativas = 0;

        System.out.println("=== FASE DO ATACANTE ===");
        System.out.println("Voce tem " + LIMITE_TENTATIVAS + " tentativas para acertar as " + totalPartesNavios + " partes dos navios.");

        while (!verificarFimDeJogo(acertos, totalPartesNavios, tentativas)) {
            System.out.println();
            System.out.println("Tentativa " + (tentativas + 1) + " de " + LIMITE_TENTATIVAS);
            System.out.println("Tabuleiro de ataques:");
            exibirTabuleiro(tabuleiroAtaques);

            boolean acertou = realizarAtaque(scanner, tabuleiroReal, tabuleiroAtaques);
            tentativas++;

            if (acertou) {
                acertos++;
                System.out.println("ACERTO! Voce atingiu uma parte de navio. (" + acertos + "/" + totalPartesNavios + ")");
            } else {
                System.out.println("ERRO! Essa coordenada tem apenas agua.");
            }
        }

        System.out.println();
        System.out.println("Tabuleiro final de ataques:");
        exibirTabuleiro(tabuleiroAtaques);

        if (acertos == totalPartesNavios) {
            System.out.println("Vencedor: Atacante.");
        } else {
            System.out.println("Vencedor: Criador.");
            System.out.println("Tabuleiro real:");
            exibirTabuleiro(tabuleiroReal);
        }

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
        System.out.print("    ");
        for (int coluna = 0; coluna < TAMANHO_TABULEIRO; coluna++) {
            System.out.print(coluna + " ");
        }
        System.out.println();

        for (int linha = 0; linha < TAMANHO_TABULEIRO; linha++) {
            System.out.print(linha + " | ");
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

            int linha = lerCoordenada(scanner, "Digite a linha inicial (0 a 9): ");
            int coluna = lerCoordenada(scanner, "Digite a coluna inicial (0 a 9): ");

            System.out.println("Direcoes:");
            System.out.println("H = horizontal para a direita");
            System.out.println("V = vertical para baixo");
            System.out.println("D = diagonal para direita");
            System.out.println("E = diagonal para esquerda");
            char direcao = lerDirecao(scanner, "Digite a direcao: ");

            if (validarPosicionamento(tabuleiro, linha, coluna, tamanhoNavio, direcao)) {
                for (int i = 0; i < tamanhoNavio; i++) {
                    int[] posicao = calcularPosicao(linha, coluna, direcao, i);
                    tabuleiro[posicao[0]][posicao[1]] = NAVIO;
                }

                System.out.println(nomeNavio + " posicionado com sucesso.");
                posicionado = true;
            } else {
                System.out.println("Posicao invalida. O navio nao pode sair do tabuleiro nem sobrepor outro navio.");
            }
        }
    }

    public static boolean realizarAtaque(Scanner scanner, char[][] tabuleiroReal, char[][] tabuleiroAtaques) {
        while (true) {
            int linha = lerCoordenada(scanner, "Digite a linha do ataque (0 a 9): ");
            int coluna = lerCoordenada(scanner, "Digite a coluna do ataque (0 a 9): ");

            if (tabuleiroAtaques[linha][coluna] != AGUA) {
                System.out.println("Tentativa repetida. Essa coordenada ja foi atacada.");
                continue;
            }

            if (tabuleiroReal[linha][coluna] == NAVIO) {
                tabuleiroAtaques[linha][coluna] = ACERTO;
                return true;
            }

            tabuleiroAtaques[linha][coluna] = ERRO;
            return false;
        }
    }

    public static boolean verificarFimDeJogo(int acertos, int totalPartesNavios, int tentativas) {
        if (acertos >= totalPartesNavios) {
            System.out.println();
            System.out.println("=== FIM DE JOGO ===");
            System.out.println("Todos os navios foram destruidos.");
            return true;
        }

        if (tentativas >= LIMITE_TENTATIVAS) {
            System.out.println();
            System.out.println("=== FIM DE JOGO ===");
            System.out.println("Limite de tentativas atingido.");
            return true;
        }

        return false;
    }

    public static boolean validarPosicionamento(char[][] tabuleiro, int linha, int coluna, int tamanhoNavio, char direcao) {
        for (int i = 0; i < tamanhoNavio; i++) {
            int[] posicao = calcularPosicao(linha, coluna, direcao, i);
            int novaLinha = posicao[0];
            int novaColuna = posicao[1];

            if (novaLinha < 0 || novaLinha >= TAMANHO_TABULEIRO || novaColuna < 0 || novaColuna >= TAMANHO_TABULEIRO) {
                return false;
            }

            if (tabuleiro[novaLinha][novaColuna] == NAVIO) {
                return false;
            }
        }

        return true;
    }

    public static int[] calcularPosicao(int linha, int coluna, char direcao, int deslocamento) {
        int novaLinha = linha;
        int novaColuna = coluna;

        if (direcao == 'H') {
            novaColuna = coluna + deslocamento;
        } else if (direcao == 'V') {
            novaLinha = linha + deslocamento;
        } else if (direcao == 'D') {
            novaLinha = linha + deslocamento;
            novaColuna = coluna + deslocamento;
        } else if (direcao == 'E') {
            novaLinha = linha + deslocamento;
            novaColuna = coluna - deslocamento;
        }

        return new int[] {novaLinha, novaColuna};
    }

    public static int lerCoordenada(Scanner scanner, String mensagem) {
        while (true) {
            System.out.print(mensagem);

            if (!scanner.hasNextInt()) {
                System.out.println("Entrada invalida. Digite apenas numeros.");
                scanner.next();
                continue;
            }

            int valor = scanner.nextInt();
            if (valor >= 0 && valor < TAMANHO_TABULEIRO) {
                return valor;
            }

            System.out.println("Entrada invalida. Digite um numero entre 0 e 9.");
        }
    }

    public static char lerDirecao(Scanner scanner, String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String entrada = scanner.next().trim().toUpperCase();

            if (entrada.length() == 1) {
                char direcao = entrada.charAt(0);

                if (direcao == 'H' || direcao == 'V' || direcao == 'D' || direcao == 'E') {
                    return direcao;
                }
            }

            System.out.println("Direcao invalida. Use H, V, D ou E.");
        }
    }

    public static void pausar(Scanner scanner, String mensagem) {
        System.out.println(mensagem);
        scanner.nextLine();
        scanner.nextLine();
    }

    public static void esconderTabuleiro() {
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }

    public static int contarPartesDeNavios(char[][] tabuleiro) {
        int total = 0;

        for (int linha = 0; linha < TAMANHO_TABULEIRO; linha++) {
            for (int coluna = 0; coluna < TAMANHO_TABULEIRO; coluna++) {
                if (tabuleiro[linha][coluna] == NAVIO) {
                    total++;
                }
            }
        }

        return total;
    }
}
