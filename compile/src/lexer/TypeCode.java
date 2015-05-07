package lexer;

import java.util.Hashtable;

public class TypeCode {

	private static Hashtable<String, Integer> typeCodeTable;

	private final static String[] DEFAULTTOKENS = { "begin", "end", "if", "then",
			"else", "for", "do", "while", "and", "or", "not", "ID", "INT", "<",
			"<=", "<>", "=", ">", ">=", ":", ":=", "/", "+", "-", "*", ";",
			"(", ")" };
	private static int code = 1;

	private TypeCode() {

	}

	public static Hashtable<String, Integer> getTypeCodeTable() {
		if (typeCodeTable == null) {
			typeCodeTable = new Hashtable<String, Integer>();
			for (int i = 0; i < DEFAULTTOKENS.length; i++)
				typeCodeTable.put(DEFAULTTOKENS[i], code++);
		}
		return typeCodeTable;
	}
}
