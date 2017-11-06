package segmenter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dictionary.DictionaryFactory;
import dictionary.DomainDictionary;
import dictionary.WordDictionary;


public class LMDomainSegmenter implements IDomainSegmenter{
	
	private DomainDictionary domainDic;

	private WordDictionary wordDic;
	
	public LMDomainSegmenter(){
		this.domainDic = DictionaryFactory.getDomainDictionary();
		this.wordDic = DictionaryFactory.getWordDictionary();
	}
	
	public void addUserDic(String dicPath) throws IOException {
		this.domainDic.addUserDict(dicPath);
	}
	
	public List segment(String input){
		List result = new ArrayList();
		int len = input.length();
		int phaseStartIndex = 0;
//		int phaseEndIndex = 0;
		int phaseEndIndex = input.length()-1;
		int wordTailIndex = -1;
		int searchWordIndex = 0;
		while (phaseStartIndex < len){
//			while ((phaseEndIndex != len-1) && !wordDic.isPunctuation(input.charAt(phaseEndIndex))){
//				phaseEndIndex++;
//			}
			while (phaseStartIndex <= phaseEndIndex){
				wordTailIndex = searchWordIndex = phaseStartIndex;
				String before = "";
				while (searchWordIndex <= phaseEndIndex){
					char _tempStr = input.charAt(searchWordIndex);
					if (domainDic.isWord(before + _tempStr)
					 ||(wordDic.isChineseNumber( before ) && wordDic.isChineseNumber( _tempStr ))
				     ||(wordDic.isWASCII( before ) && wordDic.isWASCII( _tempStr ))){
						wordTailIndex = searchWordIndex;
					}
					before = before + _tempStr;
					searchWordIndex++;
				}
				String word = input.substring(phaseStartIndex,wordTailIndex+1);
				result.add(word);
				phaseStartIndex = ++wordTailIndex;
			}
			phaseStartIndex = phaseEndIndex + 1;
			phaseEndIndex = phaseStartIndex;
		}
		return result;
	}
	
	public String segmentNextWord(String strInput, int nStartIndex){
		int nStringLen = strInput.length();
		if (nStartIndex >= nStringLen){
        	System.err.println("the next printer " + nStartIndex + "is out of the len of " + strInput);
        	return null;
        }
		int wordStartIndex = nStartIndex;
        int wordTailIndex = nStartIndex;
        int searchIndex = 0;
        int compare=Math.min(domainDic.getMaxWordLen(), (nStringLen-nStartIndex));
        String before = "";
        boolean isChineseNum=false;
        while (searchIndex < compare){
        	char _tempStr = strInput.charAt(nStartIndex+searchIndex);
        	if(before=="" &&  wordDic.isChineseNumber( _tempStr ))
        		isChineseNum=true;
        	if (domainDic.isWord(before + _tempStr)
			 ||(isChineseNum && wordDic.isChineseNumber( _tempStr ))
			 ||(wordDic.isWASCII( before ) && wordDic.isWASCII( _tempStr ))){
				wordTailIndex =nStartIndex+ searchIndex;
				
			}
        	else if(!domainDic.isWordPrefix(before+_tempStr))
        		break;
        	before = before + _tempStr;
        	searchIndex++;
        	
        }
        String word = strInput.substring(wordStartIndex,wordTailIndex+1);
        return word;
	}
}
