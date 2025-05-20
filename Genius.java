import java.io.*;
import java.util.*;

public class Genius {
    static List<Character> colors = Arrays.asList('R', 'G', 'B', 'Y', '1', '2', '3', '4');
    static List<Character> sequence = new ArrayList<>();
    static Scanner scanner = new Scanner(System.in);
    static Random random = new Random();

    static int score = 0;
    static int rodada = 1;
    static final int TEMPO_LIMITE = 10; // segundos

    public static void main(String[] args) throws InterruptedException, IOException {
        int recorde = lerRecordeDoArquivo();

        System.out.println("\nBem-vindo ao Genius! Repita a sequência de cores e números");
        System.out.println("\nCores: R (Vermelho), G (Verde), B (Azul), Y (Amarelo)");
        System.out.println("Números: 1, 2, 3, 4");
        System.out.println("\nVocê terá " + TEMPO_LIMITE + " segundos para digitar a sequência em cada rodada!");
        System.out.println("Recorde atual: " + recorde);
        System.out.println("\nPressione Enter para começar...");
        scanner.nextLine();

        while (true) {
            addColorToSequence();
            clearScreen();
            System.out.println("Rodada: " + rodada + " | Tempo limite: " + TEMPO_LIMITE + " segundos");
            showSequence();
            clearScreen();

            if (!getPlayerInputWithTimeout()) {
                System.out.println("Você errou ou demorou demais! Fim de jogo.");
                System.out.print("Sequência correta: ");
                printSequence();
                System.out.println("Pontuação final: " + score);

                salvarPontuacao(score);

                int novoRecorde = lerRecordeDoArquivo();
                if (score >= novoRecorde) {
                    System.out.println("🎉 Novo recorde!");
                } else {
                    System.out.println("Recorde atual permanece: " + novoRecorde);
                }
                break;
            }

            score++;
            rodada++;
            System.out.println("Correto! Pontuação: " + score);
            Thread.sleep(1500);
            clearScreen();
        }
    }

    static void addColorToSequence() {
        char nextChar = colors.get(random.nextInt(colors.size()));
        sequence.add(nextChar);
    }

    static void showSequence() throws InterruptedException {
        System.out.println("Memorize a sequência:");
        for (char ch : sequence) {
            printColor(ch);
            // beep removido aqui
            Thread.sleep(800);
        }
        Thread.sleep(1000);
        System.out.println();
    }

    // beepForChar removido

    static boolean getPlayerInputWithTimeout() {
        System.out.println("Você tem " + TEMPO_LIMITE + " segundos para digitar a sequência:");
        final String[] input = new String[1];

        Thread inputThread = new Thread(() -> input[0] = scanner.nextLine().toUpperCase());
        inputThread.start();

        Thread timerThread = new Thread(() -> {
            try {
                for (int i = TEMPO_LIMITE; i > 0; i--) {
                    System.out.print("\rTempo restante: " + i + " segundos ");
                    Thread.sleep(1000);
                }
                System.out.print("\r");
            } catch (InterruptedException e) {
                // interrompido se jogador terminar antes
            }
        });
        timerThread.start();

        try {
            inputThread.join(TEMPO_LIMITE * 1000L);
        } catch (InterruptedException e) {
            return false;
        }

        timerThread.interrupt();
        System.out.print("\r"); // limpa linha do tempo

        if (input[0] == null) {
            System.out.println("\n⏰ Tempo esgotado!");
            return false;
        }

        if (input[0].length() != sequence.size()) {
            return false;
        }

        for (int i = 0; i < sequence.size(); i++) {
            if (input[0].charAt(i) != sequence.get(i)) {
                return false;
            }
        }
        return true;
    }

    static void clearScreen() {
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }

    static void printSequence() {
        for (char ch : sequence) {
            printColor(ch);
        }
        System.out.println();
    }

    static void printColor(char ch) {
        String colorCode = switch (ch) {
            case 'R' -> "\u001B[31m"; // vermelho
            case 'G' -> "\u001B[32m"; // verde
            case 'B' -> "\u001B[34m"; // azul
            case 'Y' -> "\u001B[33m"; // amarelo
            default -> "\u001B[0m";   // sem cor
        };
        System.out.print(colorCode + ch + "\u001B[0m ");
    }

    static void salvarPontuacao(int pontos) {
        String data = new Date().toString();
        String linha = data + " - Pontuação: " + pontos;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("pontuacoes.txt", true))) {
            writer.write(linha);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Erro ao salvar pontuação.");
        }
    }

    static int lerRecordeDoArquivo() {
        int maior = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader("pontuacoes.txt"))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                if (linha.contains("Pontuação:")) {
                    String[] partes = linha.split("Pontuação: ");
                    if (partes.length > 1) {
                        int valor = Integer.parseInt(partes[1].trim());
                        if (valor > maior) {
                            maior = valor;
                        }
                    }
                }
            }
        } catch (IOException e) {
            // arquivo pode não existir na primeira vez
        }
        return maior;
    }
}