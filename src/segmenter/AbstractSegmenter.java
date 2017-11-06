package segmenter;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import dictionary.DictionaryFactory;
import dictionary.WordDictionary;


public abstract class AbstractSegmenter implements Segmenter{
	 
	protected WordDictionary wordDictionary = DictionaryFactory.getWordDictionary();
	
	public abstract List segment(String inputStr);
	
	public abstract String segmentNextWord(String strInput, int nStartIndex);
	
	public List segmentByPunctuation(String str){
		ArrayList segList = new ArrayList();
		int start = 0;
		for (int i=0; i<str.length(); i++){
			if (wordDictionary.isPunctuation(str.substring(i,i+1))){
				if (start == i){
					segList.add(str.substring(i,i+1));
					start = i+1;
				}
				else{
					segList.addAll(segment(str.substring(start,i)));
					segList.add(str.substring(i,i+1));
					start = i+1;
				}
			}
		}
		if ((segList == null) || (start < str.length()))
			segList.addAll(segment(str.substring(start,str.length())));
		return segList;
	}
	
	
	
	public boolean isChineseChar(char ch){
		return ch > '\u00ff';
	}   // isChineseChar
	
	public boolean isChineseWord(String str){
		if (str == null || str.length() == 0){
			return false;
		}
		for (int i=0; i<str.length(); i++){
			char c = str.charAt(i);
			if (!isChineseChar(c))
				return false;
		}
		return true;
	}
	
	
}