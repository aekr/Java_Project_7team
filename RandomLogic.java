import java.util.Random;

public class RandomLogic {
    private static Random rand = new Random();

    public static String generateProblem() {
        int operation = rand.nextInt(4);  // 0 for addition, 1 for subtraction, 2 for multiplication, 3 for division
        int num1, num2;
        String problem;

        switch (operation) {
            case 0: // Addition
                num1 = rand.nextInt(26);  // 0 to 25
                num2 = rand.nextInt(26);  // 0 to 25
                problem = num1 + " + " + num2 + " = ";
                break;

            case 1: // Subtraction
                num1 = rand.nextInt(51);  // 0 to 50
                num2 = rand.nextInt(num1 + 1);  // 0 to num1
                problem = num1 + " - " + num2 + " = ";
                break;

            case 2: // Multiplication
                num1 = rand.nextInt(11);  // 0 to 10
                num2 = rand.nextInt(6);   // 0 to 5
                problem = num1 + " * " + num2 + " = ";
                break;

            case 3: // Division
                num2 = rand.nextInt(10) + 1;  // 1 to 10
                num1 = num2 * (rand.nextInt(5) + 1);  // num2 multiplied by (1 to 5)
                problem = num1 + " / " + num2 + " = ";
                break;

            default:
                problem = "Error";
                break;
        }

        return problem;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            System.out.println(generateProblem());
        }
    }
}
