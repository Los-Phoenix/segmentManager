package dictionary;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.Arrays;

import segmenter.BMSegmenter;
import segmenter.LMSegmenter;
import segmenter.Segmenter;


public class DictionaryFactory {
    private static WordDictionary wordDic = null;
    private static DomainDictionary domainDic = null;
    private static Segmenter lsseg = null;
    private static Segmenter bsseg = null;
    private static Set hsStopWords = null;
    private static Set stopTags = null;
    
    
    public static WordDictionary getWordDictionary() {
        if (wordDic == null){
            wordDic = new WordDictionary();
            return wordDic;
        }
        else return wordDic;
    }
   
    public static DomainDictionary getDomainDictionary(){
    	if (domainDic == null){
    		try {
				domainDic = new DomainDictionary();
			} catch (IOException e) {
				e.printStackTrace();
			}
    		return domainDic;
    	}else return domainDic;
    }
    
    public static Segmenter getBMSegmenter(){
    	if (bsseg == null)
    		bsseg = new BMSegmenter();
    	return bsseg;
    }
    
    public static Segmenter getLMSegmenter(){
    	if (lsseg == null)
    		lsseg = new LMSegmenter();
    	return lsseg;
    }
    
    public static Set<String> stopWords(){
    	if(hsStopWords != null)
            return hsStopWords;
        try{
        	String stopPath = ParameterConfig.getString("stop_words");
        	InputStream in = DictionaryFactory.class.getResourceAsStream(stopPath);
            ObjectInputStream ois = new ObjectInputStream(in);
            hsStopWords = (HashSet)ois.readObject();
            ois.close();
        }
        catch(Exception e)
        {
            System.err.println(e.getMessage());
        }
        return hsStopWords;
    }
    
    public static Set<String> stopTags(){
    	if (stopTags != null){
    		return stopTags;
    	}
    	stopTags = new HashSet();
    	String[] tags = new String[]{
    		"Ag","n", "v", "a", "d", "s"	
    	};
    	stopTags.addAll(Arrays.asList(tags));
    	return stopTags;
    }
        
    public static void main(String[] orgs){
    	Segmenter seg = DictionaryFactory.getBMSegmenter();
    	System.out.println(seg.segment("上海上的船!"));
    }
    
}