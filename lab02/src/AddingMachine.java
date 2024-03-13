import java.util.*;

public class AddingMachine {

	public static void main (String[] args) {

		Scanner scanner = new Scanner(System.in);
		boolean isPreviousZero = false;
		int total = 0;
		int subtotal = 0;
		int input;
		int MAXIMUM_NUMBER_OF_INPUTS = 100;
		int[] arrayOfInputs = new int[MAXIMUM_NUMBER_OF_INPUTS];
		int index = 0;

		while (true) {
			input = scanner.nextInt();
			if (input == 0) {
				if (isPreviousZero) {
					System.out.println("total " + total);
					break; /*it shouldn't be return; since it is not the end of the process.**/
				} else {
					System.out.println("subtotal " + subtotal);
					total += subtotal;
					subtotal = 0;
					isPreviousZero = true;
				}
			} else {
				arrayOfInputs[index] = input;
				subtotal += input;
				isPreviousZero = false;
				index += 1;
			}
		}
		for (int i = 0; i < index; i++) {
			System.out.println(arrayOfInputs[i]);
		}
	}
}
