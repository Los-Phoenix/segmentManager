package segmenter;

import java.util.ArrayList;

public class LMSegmenter extends AbstractSegmenter implements Segmenter {
	public enum WordType{ChineseNum,Foreign,WASCII}
	// private final static String STR_LexFilePath = "D:/Projects/work/MT/seg/WordList.txt";
	// private final static String STR_LexFilePath = "D:/Projects/work/MT/seg/SegLex.dat";
	// private final static String STR_LexFilePath = "SegLex.dat";
	/**
	 * @param strInput
	 * @return ArrayList of String of each segmented word
	 * @exception
	 * @author
	 * Segment the input string, the result is in ArrayList of String
	 */
	public ArrayList segment(String strInput)
	{
		/** Implement this mt.seg.Segmenter method*/

		// Scan the sentence strSent from left to right:
		// Initial:
		ArrayList segWordList = new ArrayList();
		int nStringLen = strInput.length();
		int nWordStartIndex = 0;
		int nWordTailIndex = -1;
		int nSearchIndex = 0;

		// Segment Words
		String strWord = "";
		WordType wt=null;
		while ( nSearchIndex < nStringLen )
		{
			//当循环回到此处时,start,search索引值总是指向下个词段的首字,tail总是指向start-1
			char chCurChar = strInput.charAt( nSearchIndex );

			if ( ! isChineseChar( chCurChar ) )
			{
				// Plain ascii text,
				if (strWord != "")
				{
					// accumulated to number and foreign name
					if ( (wordDictionary.isChineseNumber( chCurChar ) && wt==WordType.ChineseNum ) ||
							wordDictionary.isForeign( chCurChar ) && wordDictionary.isForeign( strWord ) ||
							(chCurChar == '·' ) && wordDictionary.isForeign( strWord ))
					{
						strWord += chCurChar;
						nWordTailIndex ++;
						nSearchIndex ++;
						//所有挨个出现的非中文字符视为一个词
					}   // number and foreign name
					else   // strWord is not number and foreign name
						//说明strWord是一个中文词，chCurChar是一个非中文字符
					{
						// strWord form a word, add it to list
						segWordList.add( strWord );

						strWord = "";
						nWordStartIndex = nWordTailIndex + 1;
						nWordTailIndex = nWordStartIndex - 1;
						nSearchIndex = nWordStartIndex;
						//注意nSearchIndex没有加1，只是把前面strWord的词尾
						//作为下一个待判断字符
					}   // strWord is not number and foreign name
				}
				else    // strWord == ""
					// 是串首,然后判断后面是否为非中文字符,是则累加
				{
					// accumulate all the following ASCII char

					do
					{
						strWord += chCurChar;
						nWordTailIndex ++;
						nSearchIndex ++;
						
					} while ( ( nSearchIndex < nStringLen ) &&
							!isChineseChar(chCurChar = strInput.charAt( nSearchIndex )) && chCurChar != ' ' && !wordDictionary.isPunctuation(chCurChar));

					// strWord form a word, add it to list
					segWordList.add( strWord );
					//当前的nWordTailIndex总是保持nSearchIndex-1
					strWord = "";
					nWordStartIndex = nWordTailIndex + 1;
					nWordTailIndex = nWordStartIndex -1 ;
					nSearchIndex = nWordStartIndex;

				}   // // strWord == ""

			}   // it is not  Chinese Char, plain ascii text,
			else // it is Chinese Char
			{

				if ( strWord == "")
				{
					// it is the first char of the word
					strWord += chCurChar;
					nWordStartIndex = nSearchIndex;
					nWordTailIndex = nSearchIndex;
					nSearchIndex ++ ;
					if(wordDictionary.isChineseNumber( chCurChar ))
						wt=WordType.ChineseNum;
				}   // it is the first char of the word
				else   // it is NOT the first char of the word
				{
					// part of the word has been formed
					if ( wordDictionary.isWholeWord( strWord + chCurChar ) )
					{
						// accumulated word and current char to form a word
						strWord += chCurChar;
						nWordTailIndex ++;
						nSearchIndex ++;
					}
					else if ( wordDictionary.isChineseNumber( chCurChar ) && wt==WordType.ChineseNum )
					{
						// strWord is number(Chinese or ASCII) word and ch is also a number char
						strWord += chCurChar;
						nWordTailIndex ++;
						nSearchIndex ++;
					}
					else if ( wordDictionary.isWASCII( chCurChar ) && wordDictionary.isWASCII( strWord ) )
					{
						// strWord is 全角 non-Chinese word  and chCurChar is also 全角 non-Chinese char)
						strWord += chCurChar;
						nWordTailIndex ++;
						nSearchIndex ++;
					}


					else if ( wordDictionary.isForeign( chCurChar ) && wordDictionary.isForeign( strWord ) &&
							( !((strWord.length() > 1) && wordDictionary.isWholeWord(strWord))) && ( nSearchIndex < strInput.length() - 1)
							&& !wordDictionary.isWholeWordOrPrefix( strInput.substring( nSearchIndex,  nSearchIndex + 2 ) ) )
					{
						// strWord is (外文翻译常用字 and not word(len>=2)) and chCurCharh is also 外文翻译常用字) and
						//  chCurChar and its succed char is not word and part of word)
						strWord += chCurChar;
						nWordTailIndex ++;
						nSearchIndex ++;
					}


					else     // strWord is whole Word, but strWord + chCurChar is not whole word
					{
						/* strWord is whole Word, but strWord + chCurChar is not whole word
                          consider strWord + chCurChar be a prefix of word */

						/* continue search until characters from nWordStartIndex to nSearchIndex
                          CANNOT form word nor the prefix of any word  */
						//substring(int,int)是从int到int-1取子串
						String strSearchWord = strInput.substring( nWordStartIndex, nSearchIndex + 1 );
						while ( wordDictionary.isWholeWordOrPrefix( strSearchWord ) &&
								( nSearchIndex <= nStringLen - 1) )
						{
							if ( wordDictionary.isWholeWord( strSearchWord ))
							{
								// if characters from nWordStartIndex to nSearchIndex form a word
								strWord = strSearchWord;
								nWordTailIndex = nSearchIndex;
								nSearchIndex ++;
							}
							else  // if characters from nWordStartIndex to nSearchIndex form the prefix of any word
								nSearchIndex ++;
							if (! (nSearchIndex == nStringLen) )
								strSearchWord = strInput.substring( nWordStartIndex, nSearchIndex + 1 );
						}

						// strWord form a word, add it to list
						segWordList.add( strWord );

						strWord = "";
						nWordStartIndex = nWordTailIndex + 1;
						nWordTailIndex = nWordStartIndex - 1;
						nSearchIndex = nWordStartIndex;

					}   // strWord is whole Word, but strWord + chCurChar is not whole word

				}   // it is NOT the first char of the word
			}   // // it is Chinese Char
		}  // while ( nSearchIndex < nStringLen )

		// add the last word
		if ( strWord != "")
			segWordList.add( strWord );

		return segWordList;

	}   // segment

	/**
	 * @param strInput
	 * @return ArrayList of token of all the possible FIRST segmented words of
	 *         the given string
	 * @exception
	 * @author
	 * Segment the input string, the result is in ArrayList of String
	 */
	public String segmentNextWord(String strInput, int nStartIndex){

		int nStringLen = strInput.length();
		if (nStartIndex >= nStringLen){
			System.err.println("the next printer " + nStartIndex + "is out of the len of " + strInput);
			return null;
		}
		int wordStartIndex = nStartIndex;
		int wordTailIndex = nStartIndex;
		int searchIndex = nStartIndex;
		String strWord = "";
		WordType wt=null;
		while ((searchIndex < nStringLen)){
			char tempChar = strInput.charAt(searchIndex);
			if(wt==null){
				if(wordDictionary.isChineseNumber( tempChar ) )
						wt=WordType.ChineseNum;
				else if(wordDictionary.isWASCII(tempChar))
					wt=WordType.WASCII;
				else if (!isChineseWord(tempChar + ""))
					wt=WordType.Foreign;
			}
			
			if (wordDictionary.isWholeWord(strWord + tempChar)
					||(wt==WordType.ChineseNum && wordDictionary.isChineseNumber( tempChar ))
					||(wt==WordType.WASCII && wordDictionary.isWASCII( tempChar ))){
				wordTailIndex = searchIndex;
			}
//			add one 'else if' for English words
			else if (!isChineseChar(tempChar)  && strWord != "" && !isChineseChar(strWord.charAt(strWord.length()-1)) && tempChar != ' ' && !wordDictionary.isPunctuation(tempChar)&& wt==WordType.Foreign){
				wordTailIndex = searchIndex;
			}
			else if (!wordDictionary.isWordPrefix(strWord + tempChar)){
				break;
			}

			strWord += tempChar;
			searchIndex++;
		}
		String result = strInput.substring(wordStartIndex, wordTailIndex+1);
		return result;
		/**
        while ( ( nWordTailIndex == nStartIndex - 1 || wordDictionary.isWordPrefix(strWord) )
                && ( nWordTailIndex < nStringLen -1) ){
            nWordTailIndex ++ ;
            char chCurChar = strInput.charAt( nWordTailIndex );

            strWord += chCurChar;
            if (wordDictionary.isWholeWord(strWord) || wordDictionary.isPunctuation(strWord)){
                // strWord form a word, add it to list
//                Token token = new Token(strWord, Token.INT_Flag_ChineseWord, nStartIndex, nWordTailIndex);
//                segWordList.add(token);

            }
        }

        return result;
		 */
	}   // segmentFirstWords

	public static void main(String[] args){
		try {
			LMSegmenter lm = new LMSegmenter();
			String test = "北京大学中有百分之三十的学生是北京大学生!";
			ArrayList ar = new ArrayList();

			System.out.println(lm.wordDictionary.isWASCII("a"));
			System.out.println(lm.isChineseChar('a'));

//			ar = lm.segment(test);
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
