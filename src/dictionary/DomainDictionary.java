package dictionary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * {@code DomainDictionary}���װ����<b>����ʵ�</b>�йص�һϵ�в�����
 * ��Щ�����У�
 * <ul>
 * <li>��ȡ�ʵ�
 * <li>��ȡ���Ա��
 * <li>�ʴ��ж�
 * <li>��ǰ׺�ж�
 * <li>��ȡ���ʳ�
 * </ul>
 * @author  ԭ���ߣ�δ֪��
 * @author  Wujialin
 */
public class DomainDictionary{
	
	/* ----- Constants ----- */
	
	// ����ʵ������ļ���ַ��
	private String[] domainFiles = null;
	
	// ���Ա�ǶԼ�
	private Map<String, Set<String>> word_tags = null;
	
	// ��ǰ׺��
	private HashSet<String> wordPrefix = null;
	
	// ���ʳ�
	private int max_word_Len=0;
	
	/* ----- Constructors ----- */
	
	/**
     * ����һ�� {@code DomainDictionary}�ࡣ
     * <p>���������Ҫ��Ϊ��ʼ��({@code initialize()})�ͼ��شʵ�����({@code load()})�������衣
     * @throws IOException ����Ҳ����ʵ������ļ������캯�����׳����쳣��
     */ 
	public DomainDictionary() throws IOException {
		
		initialize();
		load();	
	}
	
	// ��ʼ��DomainDictionary���е�˽������
	private void initialize() {
		word_tags = new HashMap<String, Set<String>>();
		wordPrefix = new HashSet<String>();
		String files = ParameterConfig.getString("domain_txt");
		domainFiles = files.split(" "); // ÿ����Դ�ļ���·���ɿո���ָ�
	}
	
	// ���ļ��б���Ĵʵ����ݼ��ص�˽�������У������Ա�ǶԼ������ع����й����ǰ׺������¼���ʳ���
	private void load() throws IOException {
		
		for (String f : domainFiles){
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(DomainDictionary.class.getResourceAsStream(f)));
			String line = null;
			
			while ((line = reader.readLine()) != null){
				String[] _word_tags = line.split("   "); // �����ļ���ÿ���ʱ�ע���������������ո���ָ�
					
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
				
				// ѭ���������ʳ�
				if(word.length() > max_word_Len){
					max_word_Len=word.length();
				}
				
				// �����ǰ׺�������Կ����Ż���
				for (int i = 1; i < word.length(); i ++ )
				{
					String strPrefix = word.substring(0, i);
				    wordPrefix.add(strPrefix);
				}
			}	
		}
	}
	

	/**
     * ����û��Զ���ʵ�
     * @param dictPath �û��ʵ�·����
     * @throws IOException ����Ҳ����ʵ������ļ������캯�����׳����쳣��
     */ 
	public void addUserDict(String dictPath) throws IOException {
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(DomainDictionary.class.getResourceAsStream(dictPath)));
		String line = null;
		
		while ((line = reader.readLine()) != null){
			String[] _word_tags = line.split("   "); // �����ļ���ÿ���ʱ�ע���������������ո���ָ�
				
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
			
			// ѭ���������ʳ�
			if(word.length() > max_word_Len){
				max_word_Len=word.length();
			}
			
			// �����ǰ׺�������Կ����Ż���
			for (int i = 1; i < word.length(); i ++ )
			{
				String strPrefix = word.substring(0, i);
			    wordPrefix.add(strPrefix);
			}
		}
	}
	
	
	
	/**
     * ȷ��һ���ַ����Ƿ�Ϊ����ʵ���ĳ�ʵ�ǰ׺��
     * @param strInput ���ж�Ϊǰ׺���ַ�����
     * @return ��Ϊǰ׺�򷵻�{@code true}�����򷵻�{@code false}��
     */ 
	public boolean isWordPrefix(String strInput){
		
		return  wordPrefix.contains(strInput);
		
	}
	
	/**
     * ȷ��һ���ַ����Ƿ�Ϊ����ʵ��еĴʡ�
     * @param strInput ���ж�Ϊ����ʵ��ַ�����
     * @return ����������򷵻�{@code true}�����򷵻�{@code false}��
     */ 
	public boolean isWord(String strInput){
		
		return word_tags.containsKey(strInput);
		
	}
	
	/**
     * ��ȡ����ʵ�����ʵĳ��ȡ�
     * @return ��������ʵ�����ʵĳ��ȡ�
     */ 
	public int getMaxWordLen(){
		
		return this.max_word_Len;
		
	}
	
	/**
     * ��ȡ����ʵ���ĳ�ʵĴ��Լ���һ���ʿ����ж�����ԣ���
     * @param word �������ַ�����
     * @return ���������ʶ�Ӧ�Ĵ��Լ���
     */ 
	public Set<String> getTag(String word){
		
		return word_tags.get(word);
		
	}
	
	/**
     * ����{@code DomainDictionary}�������еķ�����
     */ 
	public static void main(String[] args) {
		try {
			DomainDictionary dd = new DomainDictionary();
			
			Set<String> tags = dd.getTag("�й�"); 
			System.out.println(tags);
			
			int maxWordLen = dd.getMaxWordLen();
			System.out.println(maxWordLen);
			
			Boolean isword = dd.isWord("����");
			System.out.println(isword);
			isword = dd.isWord("������");
			System.out.println(isword);
			
			Boolean isPrefix = dd.isWordPrefix("������");
			System.out.println(isPrefix);
			
		} catch(Exception e){
			e.printStackTrace();
		}
	}
}
