import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import telran.view.*;

interface Operation {
	double execute(double x, double y);
}

public class MiniCalcAppl {
	static InputOutput io = new ConsoleInputOutput();
	public static void main(String[] args) {
		Menu menu = new Menu("Menu",
							new Menu("Operations with numbers",
									Item.of("add 2 numbers", op -> performOperationWithNumbers('+')),
									Item.of("substract 2 numbers", op -> performOperationWithNumbers('-')),
									Item.of("multiply 2 numbers", op -> performOperationWithNumbers('*')),
									Item.of("divide 2 numbers", op -> performOperationWithNumbers('/')), 
									Item.exit()),
							new Menu("Operations with dates", 
									Item.of("Date after adding a given number of days to a given date", op -> daysToDay(true)),
									Item.of("Date after subtracting a given number of days from a given date", op -> daysToDay(false)),
									Item.of("Number days between two dates", MiniCalcAppl::daysBetweenDates),
									Item.exit()),
							new Menu("Operations with strings", 
									Item.of("Joining string from the given options with a given string", MiniCalcAppl::joinString),
									Item.of("Defining if a given string is  anagram of  another string", MiniCalcAppl::isStringsAnagram),
									Item.exit()),
							Item.exit());
		menu.perform(io);
	}
	public static void performOperationWithNumbers( char operation) {
		Double firstNum = io.readDouble("Enter first number");
		Double secondNum = io.readDouble("Enter second number");
		io.writeLn(String.format("%s %c %s = %s", firstNum, operation, secondNum,
				action(operation).execute(firstNum, secondNum)));
	}

	private static void daysToDay(boolean action) {
		String pattern = "dd/MM/yyyy";
		LocalDate day = io.readDate("Enter date in format " + pattern, pattern);
		Long countOfDays = io.readLong("Enter how many day's to " + (action ? "add" : "remove"));
		io.writeLn(String.format("%s %s %s days will be %s", day, action ? "+" : "-", countOfDays,
				action ? day.plusDays(countOfDays).format(DateTimeFormatter.ofPattern(pattern))
						: day.minusDays(countOfDays).format(DateTimeFormatter.ofPattern(pattern))));
	}

	private static void daysBetweenDates(InputOutput ioP) {
		String pattern = "dd/MM/yyyy";
		LocalDate dateFrom = io.readDate("Enter date \"from\" in format " + pattern, pattern);
		LocalDate dateTo = io.readDate("Enter date \"to\" in format " + pattern, pattern);
		io.writeLn(String.format("between %s and %s is %s days", dateFrom, dateTo,
				ChronoUnit.DAYS.between(dateFrom, dateTo)));
	}

	private static void joinString(InputOutput ioP) {
		List<String> roles = new ArrayList<String>(3);
		roles.add("front-end");
		roles.add("back-end");
		roles.add("full-stack");
		String destiny = io.readString(
				"Whom you will be after finishing this course? You need to choose from [\"front-end\",\"back-end\",\"full-stack\"]");
		String selectedOption = io.readOption(destiny,roles);
		io.writeLn(String.format("Endeed Telran teaches the best %s developers",selectedOption));
	}

	private static void isStringsAnagram(InputOutput ioP) {
		String str1 = io.readString("Enter fist string");
		String str2 = io.readString("Enter second string");
		io.writeLn(String.format("%s %s of %s", str1, (isAnagram(str1, str2) ? "is anagram" : "is not anagram"), str2));
	}

	private static Operation action(char action) {
		switch (action) {
		case '+':
			return (x, y) -> x + y;
		case '-':
			return (x, y) -> x - y;
		case '*':
			return (x, y) -> x * y;
		case '/':
			return (x, y) -> {
				if (y == 0)
					throw new IllegalArgumentException("Can not devide by zero");
				return x / y;
			};
		default:
			return (x, y) -> 0;
		}
	}

	public static boolean isAnagram(String word, String anagram) {
		if (word.length() == 0 || anagram.length() == 0 || word.length() != anagram.length() || word.equals(anagram))
			return false;
		int[] count = new int[256];
		for (int i = 0; i < word.length(); i++) {
			count[word.charAt(i)]++;
			count[anagram.charAt(i)]--;
		}
		for (int num : count) {
			if (num != 0) {
				return false;
			}
		}
		return true;
	}
}
