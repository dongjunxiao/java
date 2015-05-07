package grammar;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;

import lexer.TypeCode;

public class RecursiveDescent implements GrammarAnalysis {

	private BufferedReader buffReader;
	private int typeCode;
	private String token;
	private Hashtable<String, Integer> typeCodeTable;

	/**
	 * 
	 * @param inputFile
	 *            二元式序列文件
	 * @throws IOException
	 */
	public RecursiveDescent(String inputFile) throws IOException {
		buffReader = new BufferedReader(new FileReader(inputFile));
		typeCodeTable = TypeCode.getTypeCodeTable();
	}

	@Override
	public void analysis() {
		advance();
		if (nonterminal_E() && "".equals(token)) // 连续出现两个标识符将提前返回结果，所以加入是否到达文件尾验证
			System.out.println("ok");
		else
			System.out.println("error");
		close();
	}

	/**
	 * 读取下一个二元式序列
	 */
	private void advance() {
		try {
			String strLine = buffReader.readLine();
			if (strLine == null) { // 文件结束
				strLine = "";
				typeCode = 0;
				token = "";
			} else {
				String[] strs = strLine.substring(1, strLine.length() - 1)
						.split(",");
				typeCode = Integer.valueOf(strs[0]);
				token = strs[1];
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * E→TE′
	 * 
	 * @return
	 */
	private boolean nonterminal_E() {
		if (nonterminal_T())
			return nonterminal_E_();
		return false;
	}

	/**
	 * E′→ATE′|ε
	 * 
	 * @return
	 */
	private boolean nonterminal_E_() {
		if (nonterminal_A()) {
			if (nonterminal_T())
				return nonterminal_E_();
			return false;
		}
		return true;
	}

	/**
	 * A→+|-
	 * 
	 * @return
	 */
	private boolean nonterminal_A() {
		if (typeCode == typeCodeTable.get("+").intValue()
				|| typeCode == typeCodeTable.get("-")) {
			advance();
			return true;
		}
		return false;
	}

	/**
	 * T→FT′
	 * 
	 * @return
	 */
	private boolean nonterminal_T() {
		if (nonterminal_F())
			return nonterminal_T_();
		return false;
	}

	/**
	 * T′→MFT′|ε
	 * 
	 * @return
	 */
	private boolean nonterminal_T_() {
		if (nonterminal_M()) {
			if (nonterminal_F())
				return nonterminal_T_();
			return false;
		}
		return true;
	}

	/**
	 * M→*|/
	 * 
	 * @return
	 */
	private boolean nonterminal_M() {
		if (typeCode == typeCodeTable.get("*").intValue()
				|| typeCode == typeCodeTable.get("/")) {
			advance();
			return true;
		}
		return false;
	}

	/**
	 * F→ (E)|i
	 * 
	 * @return
	 */
	private boolean nonterminal_F() {
		if ("(".equals(token)) {
			advance();
			if (nonterminal_E()) {
				if (")".equals(token)) {
					advance();
					return true;
				}
			}
		}
		if (typeCode == typeCodeTable.get("ID").intValue()) {
			advance();
			return true;
		}
		return false;
	}

	private void close() {
		try {
			if (buffReader != null)
				buffReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
