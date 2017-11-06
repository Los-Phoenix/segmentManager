package segmenterForMOOC;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.sun.corba.se.spi.orb.StringPair;
import com.sun.xml.internal.bind.v2.model.core.ID;

import dictionary.DomainDictionary;
import segmenter.SegmenterManager;

public class SegmenterForMOOC {
	
	private SegmenterManager sm;
	
	public SegmenterForMOOC(){
		sm = new SegmenterManager();
	}
	
	List<String> segment(String sentence){
		return sm.segment(sentence);
	}
	
	String segment(String sentence, char separator){
		return listToString(sm.segment(sentence), separator);
	}
	
	public void loadUserDict(String dictPath){
		try {
			sm.addUserDict(dictPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
    private static String listToString(List<String> list, char separator) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
		    sb.append(list.get(i)).append(separator);
		}
		return sb.toString().substring(0,sb.toString().length()-1);
    }
    
    public Set<String> generateEntitySetByTxt(String txtPath) {
		Set<String> entitesSet = new HashSet<String>();
		BufferedReader reader = new BufferedReader(new InputStreamReader(DomainDictionary.class.getResourceAsStream(txtPath)));
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				entitesSet.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return entitesSet;
    }
    
    
    
    public HashMap<String, String> generateEntitySetByDB(DBManager dbm, String tableName, String entityColName, String idColName, int entityNum) {

    	ArrayList<String> entities = dbm.getColumnData(tableName, entityColName, 0, entityNum);
    	ArrayList<String> entityIDs = dbm.getColumnData(tableName, idColName, 0, entityNum);
    	
    	HashMap<String, String> entityMap = new HashMap<String, String>(); 
    	for(int i = 0; i < entities.size(); i++) {
    		entityMap.put(entities.get(i), entityIDs.get(i));
    	}
    	
		return entityMap;
		
    }
    
    
    
    
    
	// 寻找entity在字幕文本中的出现，即mention,并将结果存入数据库
    public void generateMentionsToDB(DBManager dbm, HashMap<String, String> entitesMap, List<?> sentences, List<?> sentID, List<?> videoID) {
    	
    	String[] colNames = new String[] {"name", "sent", "entity", "vid"};
    	
    	List<String> mentions = new ArrayList<String>();
    	List<String> sentIDs = new ArrayList<String>();
    	List<String> entityIDs = new ArrayList<String>();
    	List<String> videoIDs = new ArrayList<String>();
		for(int i = 0; i < sentences.size(); i++) {
			System.out.println(this.segment((String)sentences.get(i), ' '));
			List<String> words = this.segment((String)sentences.get(i));
			
			for(String word : words) {
				if (entitesMap.containsKey(word)) {
					mentions.add(word);
					sentIDs.add((String)sentID.get(i));
					entityIDs.add(entitesMap.get(word));
					videoIDs.add((String)videoID.get(i));
				}
			}
		}
			
		ArrayList<?>[] dataBatches = new ArrayList<?>[] {(ArrayList<String>)mentions, (ArrayList<String>)sentIDs, (ArrayList<String>)entityIDs, (ArrayList<String>)videoIDs};
		dbm.updateDataByCMD("TRUNCATE TABLE mention");
		dbm.insertBatchRowData("mention", colNames, dataBatches);
		
	}
    
    
    
	
	
	public static void main(String[] args){
		//输入为：url usr pwd

//		String DBname = args[0];
//		String usrIn = args[1];
//		String pwdIn = args[2];
		
		// 链接数据库
		DBManager dbm = new DBManager("mooclink2", "root", "1111");
		//DBManager wikiDBm = new DBManager("wiki", "root", "fpf930811");
		dbm.getConnection();
		//wikiDBm.getConnection();
		
		String entityPath = "bin/entity.txt";
		String loadPath = "/entity.txt";
		
		final int ENTITY_NUM = 380000;
		
		SegmenterForMOOC sm = new SegmenterForMOOC();
		dbm.writeDataToFile("entity", "name", entityPath, 0, ENTITY_NUM); //从数据库实体表中生成词典文件
		sm.loadUserDict(loadPath); // 载入自定义词典
		
		// 从数据库中读取字幕文本及其id号
		ArrayList<String> sentences = new ArrayList<String>();
		sentences = dbm.getColumnData("sentence", "content", 0, 99999);
		ArrayList<String> sentenceIDs = new ArrayList<String>();
		sentenceIDs = dbm.getColumnData("sentence", "id", 0, sentences.size());
		ArrayList<String> videoIDs = new ArrayList<String>();
		videoIDs = dbm.getColumnData("sentence", "vid", 0, sentences.size());
		
		System.out.println(sentences);
		
		HashMap<String, String> entitesMap = sm.generateEntitySetByDB(dbm, "entity", "name", "id", ENTITY_NUM);
		
		// 从句中找出entitiy的mention,并放入数据库中
		sm.generateMentionsToDB(dbm, entitesMap, sentences, sentenceIDs, videoIDs);
	
	}

}
