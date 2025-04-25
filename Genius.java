
import java.util.*;

public class Genius {
    static List<Character> colors = Arrays.asList('R', 'G', 'B', 'Y', '1', '2', '3', '4');
    static List<Character> sequence = new ArrayList<>();
    static Scanner scanner = new Scanner(System.in);
    static Random random = new Random();

    public static void main(String[] args) throws InterruptedException {
        System.out.println("\nBem-vindo ao Genius! Repita a sequência de cores e números");
        System.out.println("\nCores: R (Vermelho), G (Verde), B (Azul), Y (Amarelo)");
        System.out.println("\nNúmeros: 1, 2, 3, 4");
        System.out.println("\nPressione Enter para começar...");
        scanner.nextLine();

        while (true) {
            addColorToSequence();
            clearScreen();
            showSequence();
            clearScreen();

            if (!getPlayerInput()) {
                System.out.println("Você errou! Fim de jogo.");
                System.out.print("Sequência correta: ");
                printSequence();
                break;
            }

            System.out.println("Correto! Prepare-se para a próxima rodada...");
            Thread.sleep(1500);
            clearScreen();
        }
    }

    // Adiciona uma nova cor ou número à sequência
    static void addColorToSequence() {
        char nextChar = colors.get(random.nextInt(colors.size()));
        sequence.add(nextChar);
    }

    // Mostra a sequência ao jogador
    static void showSequence() throws InterruptedException {
        System.out.println("Memorize a sequência:");
        for (char ch : sequence) {
            System.out.print(ch + " ");
            Thread.sleep(800);
        }
        Thread.sleep(1000);
    }

    // Recebe e valida a entrada do jogador
    static boolean getPlayerInput() {
        System.out.print("Digite a sequência: ");
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

    // Simula limpar o console imprimindo várias linhas em branco
    static void clearScreen() {
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }

    // Imprime a sequência correta
    static void printSequence() {
        for (char ch : sequence) {
            System.out.print(ch + " ");
        }
        System.out.println();
    }
}