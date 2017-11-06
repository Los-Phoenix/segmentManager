package segmenterForMOOC;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;

import com.spreada.utils.chinese.ZHConverter;


public class DBManager {

	private String machineName;
	private String userName;
	private String portNum;
	private String databaseName;
	private String password;
	
	private Connection conn = null;
	private	Statement stmt = null;
	private	ResultSet rs = null;
	
	public DBManager(String _machineName, int _portNum, String _databaseName, String _userName, String _password) {
		machineName = _machineName;
		portNum = Integer.toString(_portNum);
		databaseName = _databaseName;
		userName = _userName;
		password = _password;
	}
	
	public DBManager(int _portNum, String _databaseName, String _userName, String _password) {
		this("localhost", _portNum, _databaseName, _userName, _password);
	}
	
	public DBManager(String _databaseName, String _userName, String _password) {
		this("localhost", 3306, _databaseName, _userName, _password);
	}
	
	public DBManager(String _databaseName) {
		this("localhost", 3306, _databaseName, "root", "");
	}
	
	public DBManager() {
		this("localhost", 3306, "", "root", "");
	}
	
	
	
	public boolean getConnection() {
		try {    			
 			Class.forName("com.mysql.jdbc.Driver");
 			conn = DriverManager
 					.getConnection("jdbc:mysql://" + machineName + 
 									":" + portNum +
 									"/" + databaseName + 
 									"?user=" + userName + 
 									"&password=" + password +
 									"&zeroDateTimeconvertToNull&useCursorFetch=true&defaultFetchSize=1000&useUnicode=true&characterEncoding=UTF8");
			return true;
		} catch (ClassNotFoundException e) {
				e.printStackTrace();
		} catch (SQLException ex) {
	 			// handle any errors
	 			System.out.println("SQLException: " + ex.getMessage());
	 			System.out.println("SQLState: " + ex.getSQLState());
	 			System.out.println("VendorError: " + ex.getErrorCode());
		}
		return false;
	}
	
	public ResultSet getQueryResultByCmd(String queryCommand) {
		try {
			if (stmt == null) {
				stmt = conn.createStatement(); 
			}
			rs = stmt.executeQuery(queryCommand);
			return rs;
		} catch (SQLException ex) {
	 			// handle any errors
	 			System.out.println("SQLException: " + ex.getMessage());
	 			System.out.println("SQLState: " + ex.getSQLState());
	 			System.out.println("VendorError: " + ex.getErrorCode());
		}
		return null;
	}
		
	public int updateDataByCMD(String updateCommand) {
		int updateResult;
		try {
			if (stmt == null) {
				stmt = conn.createStatement(); 
			}
			updateResult = stmt.executeUpdate(updateCommand);
			return updateResult;
		} catch (SQLException ex) {
	 			// handle any errors
	 			System.out.println("SQLException: " + ex.getMessage());
	 			System.out.println("SQLState: " + ex.getSQLState());
	 			System.out.println("VendorError: " + ex.getErrorCode());
		}
		return -1;
	}
	
	public int setColumnData(String tableName, String columnName, String primaryName, String priValue, String value) {
		
		return updateDataByCMD("UPDATE " + tableName + 
								" SET " + columnName + "=" + 
								"'" + value + "'" +" WHERE " + 
								primaryName + "="+priValue+";");
		
	}
	
	

	public int insertRowData(String tableName, String[] columnNameList, String[] valueList) {
		String columnNameString = "";
		String valueListString = "";
		for (int i = 0; i < columnNameList.length-1; i++) {
			columnNameString += (columnNameList[i] + ",");
			valueListString += ("\'" + valueList[i] + "\'" + ",");
		}
		columnNameString += columnNameList[columnNameList.length-1];
		valueListString += ("\'" + valueList[columnNameList.length-1] + "\'");
		return updateDataByCMD("INSERT INTO " + tableName + 
								"(" + columnNameString + ")" +
								" VALUES(" + valueListString + ");");
		
	}
	
	public int insertBatchRowData(String tableName, String[] columnNameList, ArrayList<?>[] valueBatches) {
		
		if (columnNameList.length != valueBatches.length){
			return -1;
		}
		
		String columnNameString = "";
		StringBuilder batchString = new StringBuilder();
		
		ArrayList<Integer> lengthList = new ArrayList<Integer>();
		for (int i = 0; i < valueBatches.length; i++){
			lengthList.add(valueBatches[i].size());
		}
		int minLength = Collections.min(lengthList);
			
		for (int i = 0; i < columnNameList.length-1; i++) {
			columnNameString += (columnNameList[i] + ",");
		}
		columnNameString += columnNameList[columnNameList.length-1];
		
		for (int i = 0; i < minLength; i++) {
			StringBuilder valueListString = new StringBuilder(); 
			for (int j = 0; j < valueBatches.length; j++){
				valueListString.append("\'").append((String)valueBatches[j].get(i)).append("\'").append(",");
			}
			valueListString.deleteCharAt(valueListString.length()-1);
			batchString.append("(").append(valueListString).append(")").append(",");
		}
		batchString.deleteCharAt(batchString.length()-1);
		batchString.append(";");
		
		return updateDataByCMD("INSERT INTO " + tableName + 
				"(" + columnNameString + ")" +
				" VALUES" + batchString);
	}

	
	public ArrayList<String> getColumnData(String tableName, String columnName, String whereName, String whereVal, int limitLow, int limitUp) {
		
		ArrayList<String> dataList = new ArrayList<String>();
		
		try {
			rs = getQueryResultByCmd("SELECT " + columnName + " FROM " + tableName + 
								" WHERE " + whereName + "=" + "'" + whereVal + "'" + 
								" LIMIT " + limitLow + "," + limitUp);
			while(rs.next()) {
				dataList.add(rs.getString(columnName));
			}
			return dataList;
		} catch (SQLException ex) {
 			// handle any errors
 			System.out.println("SQLException: " + ex.getMessage());
 			System.out.println("SQLState: " + ex.getSQLState());
 			System.out.println("VendorError: " + ex.getErrorCode());
		}
		return null;
	}

	public ArrayList<String> getColumnData(String tableName, String columnName, int limitLow, int limitUp) {
		ArrayList<String> dataList = new ArrayList<String>();
		
		try {
			rs = getQueryResultByCmd("SELECT " + columnName + " FROM " + tableName +  
								" LIMIT " + limitLow + "," + limitUp);
			while(rs.next()) {
				dataList.add(rs.getString(columnName));
			}
			return dataList;
		} catch (SQLException ex) {
 			// handle any errors
 			System.out.println("SQLException: " + ex.getMessage());
 			System.out.println("SQLState: " + ex.getSQLState());
 			System.out.println("VendorError: " + ex.getErrorCode());
		}
		return null;
	}
	
	public ArrayList<String> getColumnData(String tableName, String columnName) {
		return getColumnData(tableName, columnName, 0, 1000);
	}
	
	
	public void writeDataToFile(String tableName, String columnName, String filePath) {
		writeDataToFile(tableName, columnName, filePath, 0, 1000);
	}
	
	public void writeDataToFile(String tableName, String columnName, String filePath, int limitLow, int limitUp) {
		
		File target = new File(filePath);
		if (target.exists()) {
			target.delete();
		}
		try {
			PrintWriter out = new PrintWriter(target.getAbsoluteFile());
			int count = 0;
			ArrayList<String> dataList = this.getColumnData(tableName, columnName, limitLow, limitUp);
			try {
				
				for (String dataItem : dataList) {
					ZHConverter converter = ZHConverter.getInstance(ZHConverter.SIMPLIFIED);
					String dataItemSIMPLE = (!dataItem.isEmpty()) ? converter.convert(dataItem) : "";
					out.println(dataItemSIMPLE);
					count++;
				}
			} catch(Exception e) {
				System.out.println(dataList.get(count) + "\n" + dataList.get(count+1)+ "\n" +dataList.size());
				e.printStackTrace();
			}
			finally {
				
				out.close();
			}
			
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	protected void finalize() {
		try {
				if(rs != null) {
					rs.close();
					rs = null;
				}
				if(stmt != null) {
					stmt.close();
					stmt = null;
				}
				if(conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}
	
	public static void main(String[] args) {
		// ���Դ���
		DBManager testDBManager1 = new DBManager("mooc", "root", "fpf930811");
		testDBManager1.getConnection();
		String[] subtitlesInfoCol = new String[]{"subtitlesID","sentence","sengment","entites"};
		String[] subtitlesInfoColVal = new String[]{
				"1000",
				"��������е��ٲ�ģ����һ�ֵ��͵Ĺ���˼�롣",
				"������� �� �� �ٲ�ģ�� �� һ�� ���� �� ����˼�롣",
				"�������,�ٲ�ģ��,����˼��"};
		testDBManager1.insertRowData("subtitles", subtitlesInfoCol, subtitlesInfoColVal);
		testDBManager1.setColumnData("subtitles", "entites", "subtitlesID", "1000", "�������,�ٲ�ģ��,����,˼��");
		ArrayList<String> resultList = new ArrayList<String>();
		resultList = testDBManager1.getColumnData("subtitles", "entites", "subtitlesID", "1000", 0, 1000);
		System.out.println(resultList);
		
	}

}

