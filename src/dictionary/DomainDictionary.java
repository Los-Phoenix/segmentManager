package dictionary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * {@code DomainDictionary}类封装了与<b>领域词典</b>有关的一系列操作。
 * 这些操作有：
 * <ul>
 * <li>读取词典
 * <li>获取词性标记
 * <li>词存判定
 * <li>词前缀判定
 * <li>获取最大词长
 * </ul>
 * @author  原作者（未知）
 * @author  Wujialin
 */
public class DomainDictionary{
	
	/* ----- Constants ----- */
	
	// 领域词典数据文件地址集
	private String[] domainFiles = null;
	
	// 词性标记对集
	private Map<String, Set<String>> word_tags = null;
	
	// 词前缀集
	private HashSet<String> wordPrefix = null;
	
	// 最大词长
	private int max_word_Len=0;
	
	/* ----- Constructors ----- */
	
	/**
     * 构造一个 {@code DomainDictionary}类。
     * <p>构造过程主要分为初始化({@code initialize()})和加载词典数据({@code load()})两个步骤。
     * @throws IOException 如果找不到词典数据文件，构造函数会抛出该异常。
     */ 
	public DomainDictionary() throws IOException {
		
		initialize();
		load();	
	}
	
	// 初始化DomainDictionary类中的私有属性
	private void initialize() {
		word_tags = new HashMap<String, Set<String>>();
		wordPrefix = new HashSet<String>();
		String files = ParameterConfig.getString("domain_txt");
		domainFiles = files.split(" "); // 每个词源文件的路径由空格符分割
	}
	
	// 将文件中保存的词典数据加载到私有属性中，即词性标记对集。加载过程中构造词前缀集并记录最大词长。
	private void load() throws IOException {
		
		for (String f : domainFiles){
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(DomainDictionary.class.getResourceAsStream(f)));
			String line = null;
			
			while ((line = reader.readLine()) != null){
				String[] _word_tags = line.split("   "); // 数据文件中每个词标注对由连续的三个空格符分割
					
				String word = _word_tags[0];
				Set< String > tags = new HashSet<String>();
				
				if (_word_tags.length >= 2) {
					for (int i=1; i<_word_tags.length; i++){
						tags.add(_word_tags[i]);
					}
					
					if (word_tags.containsKey(word)){
						Set<String> _tags = word_tags.get(word);
						tags.addAll(_tags);
					}
				}
				else if (_word_tags.length == 1) {
					if (word_tags.containsKey(word)){
						tags = word_tags.get(word);
					}
				}
				word_tags.put(word, tags);
				
				// 循环更新最大词长
				if(word.length() > max_word_Len){
					max_word_Len=word.length();
				}
				
				// 构造词前缀集（可以考虑优化）
				for (int i = 1; i < word.length(); i ++ )
				{
					String strPrefix = word.substring(0, i);
				    wordPrefix.add(strPrefix);
				}
			}	
		}
	}
	

	/**
     * 添加用户自定义词典
     * @param dictPath 用户词典路径。
     * @throws IOException 如果找不到词典数据文件，构造函数会抛出该异常。
     */ 
	public void addUserDict(String dictPath) throws IOException {
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(DomainDictionary.class.getResourceAsStream(dictPath)));
		String line = null;
		
		while ((line = reader.readLine()) != null){
			String[] _word_tags = line.split("   "); // 数据文件中每个词标注对由连续的三个空格符分割
				
			String word = _word_tags[0];
			Set< String > tags = new HashSet<String>();
			
			if (_word_tags.length >= 2) {
				for (int i=1; i<_word_tags.length; i++){
					tags.add(_word_tags[i]);
				}
				
				if (word_tags.containsKey(word)){
					Set<String> _tags = word_tags.get(word);
					tags.addAll(_tags);
				}
			}
			else if (_word_tags.length == 1) {
				if (word_tags.containsKey(word)){
					tags = word_tags.get(word);
				}
			}
			word_tags.put(word, tags);
			
			// 循环更新最大词长
			if(word.length() > max_word_Len){
				max_word_Len=word.length();
			}
			
			// 构造词前缀集（可以考虑优化）
			for (int i = 1; i < word.length(); i ++ )
			{
				String strPrefix = word.substring(0, i);
			    wordPrefix.add(strPrefix);
			}
		}
	}
	
	
	
	/**
     * 确定一个字符串是否为领域词典中某词的前缀。
     * @param strInput 待判定为前缀的字符串。
     * @return 若为前缀则返回{@code true}，否则返回{@code false}。
     */ 
	public boolean isWordPrefix(String strInput){
		
		return  wordPrefix.contains(strInput);
		
	}
	
	/**
     * 确定一个字符串是否为领域词典中的词。
     * @param strInput 待判定为领域词的字符串。
     * @return 若是领域词则返回{@code true}，否则返回{@code false}。
     */ 
	public boolean isWord(String strInput){
		
		return word_tags.containsKey(strInput);
		
	}
	
	/**
     * 获取领域词典中最长词的长度。
     * @return 返回领域词典中最长词的长度。
     */ 
	public int getMaxWordLen(){
		
		return this.max_word_Len;
		
	}
	
	/**
     * 获取领域词典中某词的词性集（一个词可能有多个词性）。
     * @param word 索引词字符串。
     * @return 返回索引词对应的词性集。
     */ 
	public Set<String> getTag(String word){
		
		return word_tags.get(word);
		
	}
	
	/**
     * 测试{@code DomainDictionary}类中所有的方法。
     */ 
	public static void main(String[] args) {
		try {
			DomainDictionary dd = new DomainDictionary();
			
			Set<String> tags = dd.getTag("中国"); 
			System.out.println(tags);
			
			int maxWordLen = dd.getMaxWordLen();
			System.out.println(maxWordLen);
			
			Boolean isword = dd.isWord("哈哈");
			System.out.println(isword);
			isword = dd.isWord("哈哈哈");
			System.out.println(isword);
			
			Boolean isPrefix = dd.isWordPrefix("北京大");
			System.out.println(isPrefix);
			
		} catch(Exception e){
			e.printStackTrace();
		}
	}
}
