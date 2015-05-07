package lexer;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PushbackReader;
import java.util.Hashtable;

public class LexicalAnalysis {

	private PushbackReader reader;
	private BufferedWriter buffWriter;
	private Hashtable<String, Integer> typeCodeTable;
	private int line = 1;
	private String outputFile;

	public LexicalAnalysis(Hashtable<String, Integer> typeCodeTable) {
		this.typeCodeTable = typeCodeTable;
	}

	/**
	 * 扫描指定文件，做词法分析后输出到指定文件
	 * 
	 * @param inputFile
	 *            输入文件的文件名
	 * @param outputFile
	 *            输出文件的文件名
	 * @throws IOException
	 *             输入或输出文件出现异常
	 */
	public void scanner(String inputFile) throws IOException {
		outputFile = inputFile + ".out.txt";
		try {
			reader = new PushbackReader(new FileReader(inputFile));
			buffWriter = new BufferedWriter(new FileWriter(outputFile));
			StringBuilder strBuilder = new StringBuilder();
			String token = null;
			Integer typeCode = null;
			int ch;
			do {
				do {
					ch = reader.read();
					if (ch == '\n')
						line++;
				} while (isSpaceChar(ch));
				if (isLetter(ch)) {
					strBuilder.append((char) ch);
					ch = reader.read();
					while (isDigit(ch) || isLetter(ch)) {
						strBuilder.append((char) ch);
						ch = reader.read();
					}
					reader.unread(ch);
					token = strBuilder.toString();
					typeCode = typeCodeTable.get(token.toLowerCase());
					if (typeCode == null) // 不是保留字
						typeCode = typeCodeTable.get("ID");
				} else {
					if (isDigit(ch)) {
						strBuilder.append((char) ch);
						ch = reader.read();
						while (isDigit(ch)) {
							strBuilder.append((char) ch);
							ch = reader.read();
						}
						reader.unread(ch);
						token = strBuilder.toString();
						if (isLetter(ch)) {	//数字后跟的不是分界符，出错
							System.out.println("error at line " + line);
							return;
						}
						else
							typeCode = typeCodeTable.get("INT");
					} else {
						strBuilder.append((char) ch);
						switch (ch) {
						case '+':
						case '-':
						case '*':
						case ';':
						case '(':
						case ')':
						case '=':
							break;
						case '>':
						case ':':
							ch = reader.read();
							if (ch == '=')
								strBuilder.append((char) ch);
							else
								reader.unread(ch);
							break;
						case '<':
							ch = reader.read();
							if (ch == '=' || ch == '>')
								strBuilder.append((char) ch);
							else
								reader.unread(ch);
							break;
						case '/':
							ch = reader.read();
							if (ch == '*') {
								do {
									do {
										ch = reader.read();
										if (ch == '\n')
											line++;
										if (ch == -1) {
											System.out
													.println("block comment is incomplete.");
											return;
										}
									} while (ch != '*');
									ch = reader.read();
									if (ch == -1) {
										System.out
												.println("block comment is incomplete.");
										return;
									}
								} while (ch != '/');
								strBuilder.setLength(0);
								continue;
							} else
								reader.unread(ch);
							break;
						default:
							if (ch != -1)
								System.out.println("error at line " + line);
							return;
						}
						token = strBuilder.toString();
						typeCode = typeCodeTable.get(token);
					}
				}
				writeCodeAndToken(token, typeCode);
				strBuilder.setLength(0);
			} while (ch != -1);
		} catch (IOException e) {
			throw e;
		} finally {
			close();
		}
	}

	private void writeCodeAndToken(String token, int typeCode)
			throws IOException {
		buffWriter.write("(" + typeCode + "," + token + ")\r\n");
	}

	private boolean isSpaceChar(int ch) {
		if (ch == ' ' || ch == '\t' || ch == '\r' || ch == '\n')
			return true;
		else
			return false;
	}

	private boolean isLetter(int ch) {
		if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z'))
			return true;
		else
			return false;
	}

	private boolean isDigit(int ch) {
		if (ch >= '0' && ch <= '9')
			return true;
		else
			return false;
	}

	private void close() {
		try {
			if (reader != null)
				reader.close();
			if (buffWriter != null)
				buffWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getOutputFile() {
		return outputFile;
	}
}
