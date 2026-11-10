namespace Calculadora;

public class Program
{
    static void Main(string[] args)
    {

        bool varContinue = true;

        while(varContinue)
        {
            Console.WriteLine("===== CALCULADORA SIMPLES =====");

            // Primeiro Valor
            Console.Write("Primeiro Número: ");
            double num1 = Convert.ToDouble(Console.ReadLine());

            // Segundo Valor
            Console.Write("Segundo Número: ");
            double num2 = Convert.ToDouble(Console.ReadLine());

            // Selecionar o tipo de Operação
            Console.WriteLine("\nSelecione uma Operação: ");
            Console.WriteLine("1 - Adição: ");
            Console.WriteLine("2 - Subtração: ");
            Console.WriteLine("3 - Multiplicação: ");
            Console.WriteLine("4 - Divisão: ");
            Console.Write("Digite o Número da Operação: ");
            int option = Convert.ToInt32(Console.ReadLine());

            Console.WriteLine("\n===== RESULTADO =====");

            double result;

            // Verificar a Operação Selecionada
            if (option == 1)
            {
                result = num1 + num2;
                Console.WriteLine($"\nSOMA ({num1} + {num2}): {result}");
            }
            else if (option == 2)
            {
                result = num1 - num2;
                Console.WriteLine($"\nSUBTRAÇÃO ({num1} - {num2}): {result}");
            }
            else if (option == 3)
            {
                result = num1 * num2;
                Console.WriteLine($"\nMULTIPLICAÇÃO ({num1} * {num2}): {result}");
            }
            else if (option == 4)
            {
                if (num2 != 0)
                {
                    result = num1 / num2;
                    Console.WriteLine($"\nDIVISÃO ({num1} / {num2}): {result}");
                }
                else
                {
                    Console.WriteLine("ERRO: Não é possível dividir um número por zero.");
                }
            }
            else
            {
                Console.WriteLine("ERRO: Opção inválida.");
            }

            // Perguntar se deseja continuar
            Console.Write("\nDeseja realizar outra operação? (S/N): ");

            string response = Console.ReadLine().ToUpper();

            if (response == "S")
            {
                continue;
            }
            else
            {
                break;
            }
        }

        Console.WriteLine("\nObrigado por utilizar a Calculadora! Até Mais!");
    }
}