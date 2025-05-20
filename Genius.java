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
        int recorde = lerRecorde();

        System.out.println("\nBem-vindo ao Genius! Repita a sequência de cores e números");
        System.out.println("\nCores: R (Vermelho), G (Verde), B (Azul), Y (Amarelo)");
        System.out.println("Números: 1, 2, 3, 4");
        System.out.println("\nModo desafio: você tem " + TEMPO_LIMITE + " segundos para digitar a sequência!");
        System.out.println("Recorde atual: " + recorde);
        System.out.println("\nPressione Enter para começar...");
        scanner.nextLine();

        while (true) {
            addColorToSequence();
            clearScreen();
            System.out.println("Rodada: " + rodada);
            showSequence();
            clearScreen();

            if (!getPlayerInputWithTimeout()) {
                System.out.println("Você errou ou demorou demais! Fim de jogo.");
                System.out.print("Sequência correta: ");
                printSequence();
                System.out.println("Pontuação final: " + score);

                if (score > recorde) {
                    System.out.println("🎉 Novo recorde!");
                    salvarRecorde(score);
                } else {
                    System.out.println("Recorde atual permanece: " + recorde);
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
            Thread.sleep(800);
        }
        Thread.sleep(1000);
        System.out.println();
    }

    static boolean getPlayerInputWithTimeout() {
        System.out.println("Você tem " + TEMPO_LIMITE + " segundos para digitar a sequência:");
        long startTime = System.currentTimeMillis();

        while (!scanner.hasNextLine()) {
            if ((System.currentTimeMillis() - startTime) / 1000 > TEMPO_LIMITE) {
                return false;
            }
        }

        String input = scanner.nextLine().toUpperCase();

        if (input.length() != sequence.size()) {
            return false;
        }

        for (int i = 0; i < sequence.size(); i++) {
            if (input.charAt(i) != sequence.get(i)) {
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

    static int lerRecorde() {
        try (BufferedReader reader = new BufferedReader(new FileReader("recorde.txt"))) {
            return Integer.parseInt(reader.readLine());
        } catch (Exception e) {
            return 0;
        }
    }

    static void salvarRecorde(int novoRecorde) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("recorde.txt"))) {
            writer.write(String.valueOf(novoRecorde));
        } catch (IOException e) {
            System.out.println("Erro ao salvar o recorde.");
        }
    }
}