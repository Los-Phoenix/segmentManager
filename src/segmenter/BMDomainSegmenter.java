package segmenter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

import dictionary.ParameterConfig;
import dictionary.DictionaryFactory;
import dictionary.DomainDictionary;
import dictionary.WordDictionary;


public class BMDomainSegmenter extends AbstractSegmenter implements IDomainSegmenter{
	
	private DomainDictionary wineDic;

	private WordDictionary wordDic;
	
	public BMDomainSegmenter(){
		this.wineDic = DictionaryFactory.getDomainDictionary();
		this.wordDic = DictionaryFactory.getWordDictionary();
	}
	
	public void addUserDic(String dicPath) throws IOException {
		this.wineDic.addUserDict(dicPath);
	}
	
	public List segment(String input){
		List result = new ArrayList();
		int len = input.length();
		int phaseStartIndex = len-1;
//		int phaseEndIndex = len-1;
		int phaseEndIndex = 0;
		int wordTailIndex = len;
		int searchIndex = len;
		while (phaseStartIndex != -1 ){
//			while (phaseEndIndex != 0 && !wordDic.isPunctuation(input.charAt(phaseEndIndex))){
//				phaseEndIndex--;
//			}
			while (phaseStartIndex >= phaseEndIndex){
				wordTailIndex = searchIndex = phaseStartIndex;
				String after = "";
				while (searchIndex >= phaseEndIndex){
					char _tempstr = input.charAt(searchIndex);
					if (wineDic.isWord(_tempstr + after)
					 ||(wordDic.isChineseNumber( _tempstr ) && wordDic.isChineseNumber( after ))
					 ||(wordDic.isWASCII( _tempstr ) && wordDic.isWASCII( after ))){
						wordTailIndex = searchIndex;
					}
//					else if (!isChineseChar(_tempstr) && !isChineseWord(after) && _tempstr != ' ' && !wordDic.isPunctuation(_tempstr)){
//						wordTailIndex = searchIndex;
//					}
					after = _tempstr + after;
					searchIndex--;
				}
				String word = input.substring(wordTailIndex,phaseStartIndex+1);
				result.add(word);
				phaseStartIndex = --wordTailIndex;
			}
			phaseStartIndex = phaseEndIndex-1;
			phaseEndIndex = phaseStartIndex;
		}
		Collections.reverse(result);
		return result;
	}
	
	public String segmentNextWord(String strInput, int nStartIndex){
		int nStringLen = strInput.length();
		if (nStringLen < 0){
			System.err.println("the before printer " + nStartIndex + "is out of the len of " + strInput);
			return null;
		}
		int wordStartIndex = nStartIndex;
		int wordTailIndex = nStartIndex;
		int searchIndex = nStartIndex;
		String strWord = "";
		while (searchIndex>=0){
			char tempChar = strInput.charAt(searchIndex);
			if (wineDic.isWord(tempChar + strWord)
			 ||(wordDic.isChineseNumber( tempChar ) && wordDic.isChineseNumber( strWord ))
		     ||(wordDic.isWASCII( tempChar ) && wordDic.isWASCII( strWord ))){
				wordTailIndex = searchIndex;
			}
//			else if (!isChineseChar(tempChar) && !isChineseWord(strWord) && tempChar != ' ' && !wordDic.isPunctuation(tempChar)){
//				wordTailIndex = searchIndex;
//			}
			strWord = tempChar + strWord;
			searchIndex--;
		}
		String word = strInput.substring(wordTailIndex, wordStartIndex+1);
		return word;
	}
}
