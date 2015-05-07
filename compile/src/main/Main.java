package main;

import grammar.GrammarAnalysis;
import grammar.RecursiveDescent;
import java.io.IOException;
import lexer.LexicalAnalysis;
import lexer.TypeCode;

public class Main {

	public static void main(String[] args) {

		LexicalAnalysis lex = new LexicalAnalysis(TypeCode.getTypeCodeTable());
		try {
			lex.scanner("input.txt");
			GrammarAnalysis ga = new RecursiveDescent(lex.getOutputFile());
			ga.analysis();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
