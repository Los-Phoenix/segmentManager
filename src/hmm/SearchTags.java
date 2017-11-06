package hmm;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import dictionary.DictionaryFactory;
import dictionary.DomainDictionary;
import dictionary.WordDictionary;


public class SearchTags {
	
	
//	分词词典
    private static WordDictionary wordDic;
    
    private static DomainDictionary domainDic;
    
    private Famous theFamous;
	
	//private HashSet hsPeopleCache;
	//private HashSet hsLocationCache;
	//private HashSet hsOrganizationCache;
	
	public SearchTags(){
		try{
			theFamous = new Famous();
			wordDic = DictionaryFactory.getWordDictionary();
			domainDic = DictionaryFactory.getDomainDictionary();
			
			if(wordDic == null)
				throw new Exception("dictionary load failed");
			//initialize();
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	/**
	private void initialize() throws java.io.IOException{
		hsPeopleCache = theFamous.getpeople();
		hsLocationCache = theFamous.getlocation();
		hsOrganizationCache = theFamous.getorganization();
	}
	*/
//  需进一步考虑 当词典内没有对应词时，默认为Ng,表示名词素
	private Set getWordTag(String str){
		
		Set hs = wordDic.getWordTag(str);
		if (hs==null)
			hs = new HashSet();
		if (theFamous.IsDate(str)||theFamous.IsTime(str))
			hs.add("t");
		if (theFamous.IsNumber(str) || wordDic.isChineseNumber(str))
			hs.add("m");
		/**
		if ((theFamous.IsFamousLocation(str))||(hsLocationCache.contains(str)))
			hs.add("ns");
		if ((theFamous.IsFamousOrganization(str))||(hsOrganizationCache.contains(str)))
			hs.add("nt");
		if ((theFamous.IsFamousPeople(str))||(hsPeopleCache.contains(str)))
			hs.add("nr");
		*/
		if (wordDic.isPunctuation(str))
			hs.add("w");
		
		if (domainDic != null && domainDic.isWord(str)){
			hs.addAll(domainDic.getTag(str));
		}
		
		if (hs.size() == 0){
			hs.add("nz");
		}
		return hs;
	}
	
	@SuppressWarnings("unused")
	private Set< String > getExtendWordTag(String str){
		String suffix = "";
		Set< String > tags = new HashSet();
		boolean flag = true;
		if (domainDic.isWord(str)){
			tags = domainDic.getTag(str);
			return tags;
		}
		for (int i=str.length()-1; i>=0 && flag; i--){
			suffix = str.substring(i);
			if (wordDic.isAddressSuffix(suffix)){
				tags.add("ns");
				flag = false;
			}
			else if (wordDic.isOrganizationSuffix(suffix)){
				tags.add("nt");
				flag = false;
			}
		}
		if (tags.size()== 0)
			tags.add("nz");
		return tags;
	}
	
	public WordInfo[] getWordTag(List words){
		WordInfo[] wordalt= new WordInfo[words.size()];
		for (int i=0,n=words.size(); i<n; i++){
			String str=(String)words.get(i);
			Set tags=getWordTag(str);
			wordalt[i]=new WordInfo(str,tags);
		}
		return wordalt;
	}
	
	
}
