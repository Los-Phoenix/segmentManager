package segmenter;

import java.util.ArrayList;
import java.util.List;


public class BMSegmenter extends AbstractSegmenter implements Segmenter {

	public List segment(String strInput) {
		/** Implement this mt.seg.Segmenter method */

		// Scan the sentence strSent from left to right:
		// Initial:
		List segTemp = new ArrayList();
		List segWordList = new ArrayList();
		int nStringLen = strInput.length();
		int nWordStartIndex = nStringLen;
		int nWordTailIndex = nStringLen - 1;
		int nSearchIndex = nStringLen - 1;

		// Segment Words
		String strWord = "";
		while (nSearchIndex >= 0)
		// 每一次循环都是以 nSearchIndex=nWordTailIndex=nWordStartIndex+1
		{
			char chCurChar = strInput.charAt(nSearchIndex);

			if (!isChineseChar(chCurChar)) {
				// Plain ascii text,
				if (strWord != "") {
					// accumulated to number and foreign name
					if ((wordDictionary.isChineseNumber(chCurChar) && wordDictionary
							.isChineseNumber(strWord)))
					/**
					 * 不对外国人名作识别 || wordDictionary.isForeign( chCurChar ) &&
					 * wordDictionary.isForeign( strWord ) || (chCurChar == '·' ) &&
					 * wordDictionary.isForeign( strWord ))
					 */
					{
						strWord = chCurChar + strWord;
						nWordStartIndex--;
						nSearchIndex--;
						// 所有挨个出现的非中文字符视为一个词
					} // number and foreign name
					else // strWord is not number and foreign name
					// 说明strWord是一个中文词，chCurChar是一个非中文字符
					{
						// strWord form a word, add it to list
						segTemp.add(strWord);

						strWord = "";
						nWordTailIndex = nWordStartIndex - 1;
						nWordStartIndex = nWordTailIndex + 1;
						nSearchIndex = nWordTailIndex;
						// 注意nSearchIndex没有加1，只是把前面strWord的词尾
						// 作为下一个待判断字符
					} // strWord is not number and foreign name
				} else // strWord == ""
				// 是串首,然后判断后面是否为非中文字符,是则累加
				{
					// accumulate all the following ASCII char
					do {
						strWord = chCurChar + strWord;
						nWordStartIndex--;
						nSearchIndex--;
					} while ((nSearchIndex >= 0) && !isChineseChar(chCurChar = strInput.charAt(nSearchIndex))
							&& chCurChar != ' ' && !wordDictionary.isPunctuation(chCurChar));

					// strWord form a word, add it to list
					segTemp.add(strWord);

					strWord = "";

					nWordTailIndex = nWordStartIndex - 1;
					nWordStartIndex = nWordTailIndex + 1;
					nSearchIndex = nWordTailIndex;

				} // // strWord == ""

			} // it is not Chinese Char, plain ascii text,
			else // it is Chinese Char
			{

				if (strWord == "") {
					// it is the first char of the word
					strWord = chCurChar + strWord;
					nWordStartIndex = nSearchIndex;
					nWordTailIndex = nSearchIndex;
					nSearchIndex--;
				} // it is the first char of the word
				else // it is NOT the first char of the word
				{
					// part of the word has been formed
					if (wordDictionary.isWholeWord(chCurChar + strWord)) {
						// accumulated word and current char to form a word
						strWord = chCurChar + strWord;
						nWordStartIndex--;
						nSearchIndex--;
					} else if (wordDictionary.isChineseNumber(chCurChar)
							&& wordDictionary.isChineseNumber(strWord)) {
						// strWord is number(Chinese or ASCII) word and ch is
						// also a number char
						strWord = chCurChar + strWord;
						nWordStartIndex--;
						nSearchIndex--;
					} else if (wordDictionary.isWASCII(chCurChar)
							&& wordDictionary.isWASCII(strWord)) {
						// strWord is 全角 non-Chinese word and chCurChar is also
						// 全角 non-Chinese char)
						strWord = chCurChar + strWord;
						nWordStartIndex--;
						nSearchIndex--;
					}

					/**
					 * 不对外国人名作识别 else if ( wordDictionary.isForeign( chCurChar ) &&
					 * wordDictionary.isForeign( strWord ) && (
					 * !((strWord.length() > 1) &&
					 * wordDictionary.isWholeWord(strWord))) && ( nSearchIndex >
					 * 0) && !wordDictionary.isWholeWordOrSuffix(
					 * strInput.substring( nSearchIndex - 1, nSearchIndex + 1 ) ) ) { //
					 * strWord is (外文翻译常用字 and not word(len>=2)) and chCurCharh
					 * is also 外文翻译常用字) and // chCurChar and its succed char is
					 * not word and part of word) strWord = chCurChar + strWord;
					 * nWordStartIndex --; nSearchIndex --; }
					 */

					else // strWord is whole Word, but strWord + chCurChar is
							// not whole word
					{
						/*
						 * strWord is whole Word, but strWord + chCurChar is not
						 * whole word consider strWord + chCurChar be a prefix
						 * of word
						 */

						/*
						 * continue search until characters from nWordStartIndex
						 * to nSearchIndex CANNOT form word nor the prefix of
						 * any word
						 */
						String strSearchWord = strInput.substring(nSearchIndex,
								nWordTailIndex + 1);
						while (wordDictionary
								.isWholeWordOrSuffix(strSearchWord)
								&& (nSearchIndex >= 0)) {
							if (wordDictionary.isWholeWord(strSearchWord)) {
								// if characters from nWordStartIndex to
								// nSearchIndex form a word
								strWord = strSearchWord;
								nWordStartIndex = nSearchIndex;
								nSearchIndex--;
							} else
								// if characters from nWordStartIndex to
								// nSearchIndex form the prefix of any word
								nSearchIndex--;
							if (nSearchIndex < 0)
								break;
							else
								strSearchWord = strInput.substring(
										nSearchIndex, nWordTailIndex + 1);
						}

						// strWord form a word, add it to list
						segTemp.add(strWord);

						strWord = "";
						nWordTailIndex = nWordStartIndex - 1;
						nWordStartIndex = nWordTailIndex + 1;
						nSearchIndex = nWordTailIndex;

					} // strWord is whole Word, but strWord + chCurChar is not
						// whole word

				} // it is NOT the first char of the word
			} // // it is Chinese Char
		} // while ( nSearchIndex < nStringLen )

		// add the last word
		if (strWord != "")
			segTemp.add(strWord);
		// segTemp.add("END!!!");
		for (int i = segTemp.size() - 1; i >= 0; i--) {
			segWordList.add(segTemp.get(i));
		}
		// process chinese address
		// segWordList = processChineseAddress(segWordList);

		// process chinese name
		// segWordList = processChineseName(segWordList);

		return segWordList;

	} // segment

	public String segmentNextWord(String strInput, int nStartIndex) {
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
			if (wordDictionary.isWholeWord(tempChar + strWord)
			 ||(wordDictionary.isChineseNumber( tempChar ) && wordDictionary.isChineseNumber( strWord ))
		     ||(wordDictionary.isWASCII( tempChar ) && wordDictionary.isWASCII( strWord ))){
				wordTailIndex = searchIndex;
			}
			//add one 'else if' for English words
			else if (!isChineseChar(tempChar) && !isChineseWord(strWord) && tempChar != ' ' && !wordDictionary.isPunctuation(tempChar)){
				wordTailIndex = searchIndex;
			}
			else if (!wordDictionary.isWordSuffix(tempChar+ strWord)){
				break;
			}
			
			strWord = tempChar + strWord;
			searchIndex--;
		}
		String result = strInput.substring(wordTailIndex, wordStartIndex+1);
		return result;
	}
	
	public static void main(String[] args){
		try {
			BMSegmenter lm = new BMSegmenter();
			String test = "I want to love you你";
			List ar = new ArrayList();
			ar = lm.segment(test);
			for (int i=0;i<ar.size();i++){
				System.out.print(ar.get(i)+" ");
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}