package telran.view;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Function;

public interface InputOutput {

	String readString(String promt);

	void writeObject(Object obj);
	
	 void clrscr();
	
	default void writeLn(Object obj) {
		writeObject(obj.toString() + "\n");
	}

	default <R> R readObject(String promt, String errorPromt, Function<String, R> mapper) {
		while (true) {
			String input = readString(promt);
			try {
				return mapper.apply(input);
			} catch (Exception e) {
				writeLn(errorPromt);
			}
		}
	}

	default String readOption(String promt, List<String> objects) throws IllegalArgumentException {
		return readObject(promt,"Seleted option is not in list",(p)->{
			if (objects.isEmpty() || objects.indexOf(p) == -1)
				throw new IllegalArgumentException();
			return promt;
		});
	}

	default Integer readInteger(String promt) {
		return readObject(promt, "Not integer number", Integer::parseInt);
	}

	default Integer readInteger(String promt, int min, int max) {

		return readObject(promt, String.format("it is not a number in range %s-%s", min, max), x -> {
			int number = Integer.parseInt(x);
			if (number < min || number > max)
				throw new NumberFormatException();
			return number;
		});
	}

	default Long readLong(String promt) {
		return readObject(promt, "Not long number", Long::parseLong);
	}

	default Double readDouble(String promt) {
		return readObject(promt, "Not Double number", Double::parseDouble);
	}

	default LocalDate readDate(String promt, String format) {
		return readObject(promt, String.format("it is not a date in format %s", format),
				d -> LocalDate.parse(d, DateTimeFormatter.ofPattern(format)));
	}
}
