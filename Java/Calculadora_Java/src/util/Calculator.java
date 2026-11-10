package util;

import java.util.Locale;
import java.util.Scanner;

public class Calculator {
    public static void main(String[] args) {

        Locale.setDefault(Locale.US);
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n== CALCULADORA ==");
            System.out.print("Digite um Número: ");
            int num1 = scanner.nextInt();

            System.out.print("Digite outro Número: ");
            int num2 = scanner.nextInt();

            System.out.println("\nQual operação deseja escolher?");
            System.out.println("[1] - SOMA");
            System.out.println("[2] - SUBTRAÇÃO");
            System.out.println("[3] - MULTIPLICAÇÃO");
            System.out.println("[4] - DIVISÃO");
            System.out.print("OPERAÇÃO: ");
            int operacao = scanner.nextInt();

            double result;

            if (operacao < 1 || operacao > 4) {
                System.out.println("Erro: Digite um valor de 1 até 4!");
            } else {
                switch (operacao) {
                    case 1:
                        result = num1 + num2;
                        System.out.println("\nRESULTADO: ");
                        System.out.println("SOMA = " + result);
                        break;
                    case 2:
                        result = num1 - num2;
                        System.out.println("\nRESULTADO: ");
                        System.out.println("SUBTRAÇÃO = " + result);
                        break;
                    case 3:
                        result = num1 * num2;
                        System.out.println("\nRESULTADO: ");
                        System.out.println("MULTIPLICAÇÃO = " + result);
                        break;
                    case 4:
                        result = (double) num1 / num2;
                        if (num2 == 0) {
                            System.out.println("N EXISTE DIVISAO POR ZERO");
                        } else {
                            System.out.println("\nRESULTADO: ");
                            System.out.println("DIVISÃO = " + result);
                        }
                }
            }

            System.out.print("Deseja continuar (s/n)? ");
            String continuar = scanner.next();

            if (continuar.equals("s")) {
                continue;
            } else {
                break;
            }

        }
    }
}
