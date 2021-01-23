package DBConnect;

import java.io.Console;
import java.util.Scanner;

public class ConnectTesting {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		System.out.println("請輸入HOST:");
		String host = sc.nextLine().trim();

		System.out.println("請輸入DBNAME:");
		String DBName = sc.nextLine().trim();

		System.out.println("請輸入USERNAME:");
		String username = sc.nextLine().trim();

		Console console = System.console();
		console.printf("請輸入PASSWORD:%n");
		String password = new String(console.readPassword()).trim();

		DBFactory.getOracleDBConnection(host, DBName, username, password);
	}

}
