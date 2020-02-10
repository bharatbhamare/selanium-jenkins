import static org.hamcrest.CoreMatchers.nullValue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScriptExecuter {

	private static StringBuilder sb = new StringBuilder();
	private static String folderpath = "C:\\Users\\bharat.bhamare\\Desktop\\Hotfix\\FEATURE_GROUPS\\DEV\\SCRIPTS";
	private static Connection conn = null;
	private static final String DB_URL ="dburl";

	private static final String USER = "usr_name";
	private static final String PASS = "pass";
	private static final String ENV = "QA";

	public static void main(String[] str) throws IOException, ClassNotFoundException, SQLException {

		final File folder = new File(folderpath);
		listFilesForFolder(folder);

		String scriptPath = "D:\\FEATUR-MIGRATION\\DEV\\SCRIPTS";

		writeFile(ENV, "SCRIPT", sb);

		// System.out.println(sb);

		createConnection();

		if (sb != null && conn != null) {
			String msg = execute(sb);
			System.out.println("msg " + msg);
			System.out.println("STATUS: " + msg);
		} else {
			System.out.println("DATABASE CONNECTION FAIL OR SCRPTS NOT FOUND");
		}
	}

	public static void listFilesForFolder(final File folder) throws IOException, ClassNotFoundException, SQLException {
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				listFilesForFolder(fileEntry);
			} else {
				// System.out.println("-- ------------------------------------------------------------\n");
				// System.out.println(" -- FILE NAME: " + fileEntry.getName());

				readContent(fileEntry);
				sb.append("-- ------------------------------------------------------------\n");
			}
		}
	}

	public static void readContent(File file) throws IOException {
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String strLine;
			while ((strLine = br.readLine()) != null) {
				// System.out.println(strLine);
				sb.append(strLine);
				sb.append(System.getProperty("line.separator"));
				
			}
		}
	}

	public static String writeFile(final String release, final String fname, final StringBuilder value) {
		final String todat = new SimpleDateFormat("yyyyMMdd").format(new Date());
		final String time = new SimpleDateFormat("HH").format(new Date());
		final String directoryName = new File(folderpath).toString() + release + "/" + todat + "/" + time + "";
		final String date = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
		final String fileName = fname + date + ".sql";
		final File directory = new File(directoryName);
		if (!directory.exists()) {
			directory.mkdirs();
		}
		final File file = new File(directoryName + "/" + fileName);
		try {
			final FileWriter fileWriter = new FileWriter(file);
			fileWriter.write(value.toString());
			fileWriter.flush();
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		return file.getAbsolutePath();
	}

	public static String execute(StringBuilder sb) throws SQLException, ClassNotFoundException {
		try (java.sql.Connection con = createConnection()) {
			con.setAutoCommit(false);
			try (Statement stm = con.createStatement()) {
				stm.execute(sb.toString()); // causes SQLException
			} catch (SQLException ex) {
				con.rollback();
				con.setAutoCommit(true);
				return ex.getMessage();
			}
			con.commit();
			con.setAutoCommit(true);
		}
		return "OK";
	}

	public static Connection createConnection() throws SQLException, ClassNotFoundException {
		try {

			if (conn == null) {
				Class.forName("org.postgresql.Driver");

				// STEP 3: Open a connection
				System.out.println("Connecting to database...");
				conn = DriverManager.getConnection(DB_URL, USER, PASS);
			} else {
				return conn;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		if (conn != null) {
			return conn;
		} else {
			System.out.println("Connn not found");
			return null;
		}

	}

}
