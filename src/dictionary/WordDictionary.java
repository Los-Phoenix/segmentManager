package dictionary;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * {@code WordDictionary}类封装了与<b>基本词典</b>有关的一系列操作。</ul>
 * 同时构建了基于hash表的词典存储结构。<i>这点可以考虑用trie树重写来进行优化</i>
 * 该类提供的操作有：
 * <ul>
 * <li>读取、保存词典
 * <li>获取词性标记
 * <li>判断词义类型，如中文数字、中文人名、外文词、外文人名、地址名、组织名、标点、WASCII等
 * <li>判断前缀后缀
 * </ul>
 * @author  原作者（未知）
 * @author  Wujialin
 */
public class WordDictionary {
	/* ----- Constants ----- */
	
	// 词类型的标示，包括：完整词、词前缀、词后缀
	public final static int INT_WordType_Whole  = 0x01;
	public final static int INT_WordType_Prefix = 0x02;
	public final static int INT_WordType_Suffix = 0x04;
	
	// 这里定义了一系列构建词典类的数据存储结构，均为hash存储
	private HashMap hsWordTag = null;
	private HashMap htWordMap = null;//0
	private HashSet hsChineseNumbers = null;//1
	private HashSet hsWASCIIs = null;//2
	private HashSet hsForeigns = null;//3
	private HashSet hsChineseSurnames = null;//4
	private HashSet hsChineseUncommonSurnames = null;//5
	private HashSet hsCompSurNames = null;//6
	private HashSet hsTitles = null;//7
	private HashSet hsSurnameFrequent = null;//8
	private HashSet hsNotNames = null;//9 10
	private HashSet hsPunctuations = null;//11
	private HashSet hsAddressSuffixes = null;//12 13 14 15
	private HashSet hsNotAddresses = null;//16 17
	private HashSet hsNotOrganizationes = null;//18 19
	private HashSet hsOrganizationSuffixes = null;//20
	private HashSet hsNameWords = null;//21 22 23 24
	private HashMap hsQAWords = null; //bt cyd
	
	// 
	private String segDatFile = null; //保存的词典文件路径
	private String STR_QAWordTxTPath = null;
	
	// 
	private static Logger log = LogManager.getLogger(WordDictionary.class);
	
	/* ----- Constructors ----- */
	
	/**
     * 构造一个 {@code WordDictionary}类。
     * <p>构造过程主要分为初始化({@code initialize()})和加载词典数据({@code load()})两个步骤。
     */
	public WordDictionary(){
		try{
			this.initialize();
			this.load();
		}
		catch ( ClassNotFoundException e){
			log.error(e.getMessage(),e);
		} catch (FileNotFoundException e) {
			log.error(e.getMessage(),e);
		} 
		catch (IOException e) {
			log.error(e.getMessage(),e);
		}
	}
	
	/**
     * 引用<b>基础词词典</b>和<b>特殊词词典</b>文件构造一个 {@code WordDictionary}类。
     * @param basewords 基础词词典文件路径，文件格式为<b>.txt</b>
     * @param spcialwords 特殊词词典文件路径，文件格式为<b>.txt</b>
     */ 
	public WordDictionary(String basewords, String spcialwords){
		if (basewords != null && spcialwords != null){
			try {
				this.initialize();
				this.loadFromTXT(basewords, spcialwords);
				this.save();
			} catch (IOException e) {
				log.error(e.getMessage(),e);
			}
		}
		else {
			log.error("update dic has not success because of wrong input strings...");
			System.exit(0);
		}
	}
	
	// 从segDatFile文件中加载各类词典数据。
	private void load() throws IOException, ClassNotFoundException {
		
//		FileInputStream fis = new FileInputStream(new File(segDatFile.toURI()));
		ObjectInputStream ois = new ObjectInputStream(WordDictionary.class.getResourceAsStream(segDatFile));
		// read magic
		int nMagic = ois.readInt();
		// read version
		int nVersion = ois.readInt();
		
		// read hashtable and hashsets
		hsWordTag=(HashMap)ois.readObject();
		htWordMap = (HashMap) ois.readObject();
		hsChineseNumbers = (HashSet) ois.readObject();
		hsWASCIIs = (HashSet) ois.readObject();
		hsForeigns = (HashSet) ois.readObject();
		hsChineseSurnames = (HashSet) ois.readObject();
		hsChineseUncommonSurnames = (HashSet) ois.readObject();
		hsCompSurNames = (HashSet) ois.readObject();
		hsTitles = (HashSet) ois.readObject();
		hsSurnameFrequent = (HashSet) ois.readObject();
		hsNotNames = (HashSet) ois.readObject();
		hsPunctuations = (HashSet) ois.readObject();
		hsAddressSuffixes = (HashSet) ois.readObject();
		hsNotAddresses = (HashSet) ois.readObject();
		hsNotOrganizationes = (HashSet) ois.readObject();
		hsOrganizationSuffixes = (HashSet) ois.readObject();
		hsNameWords = (HashSet) ois.readObject();
		hsQAWords = (HashMap)ois.readObject();
		ois.close();
		log.debug(nMagic);
		log.debug(nVersion);

		log.debug("...load Dictionary DAT file sucess in WordDictionary...");
	}
	
	// 初始化操作，对保存各词典的存储结构类进行实例化
	private void initialize(){
		hsWordTag=new HashMap();
		htWordMap = new HashMap();//0
		hsChineseNumbers = new HashSet();//1
		hsWASCIIs = new HashSet();//2
		hsForeigns = new HashSet();//3
		hsChineseSurnames = new HashSet();//4
		hsChineseUncommonSurnames = new HashSet();//5
		hsCompSurNames = new HashSet();//6
		hsTitles = new HashSet();//7
		hsSurnameFrequent = new HashSet();//8
		hsNotNames = new HashSet();//9 10
		hsPunctuations = new HashSet();//11
		hsAddressSuffixes = new HashSet();//12 13 14 15
		hsNotAddresses = new HashSet();//16 17
		hsNotOrganizationes = new HashSet();//18 19
		hsOrganizationSuffixes = new HashSet();//20
		hsNameWords = new HashSet();//21 22 23 24
		hsQAWords = new HashMap(); //bt cyd
		segDatFile = ParameterConfig.getString("segLex");
		STR_QAWordTxTPath = "conf/QAWord.txt";
	}
	
	
	/* ----- Methods ----- */
	
	/**
     * 将词典类中的各词典数据保存至segDatFile文件中。
     */ 
	public void save(){
		try
		{
			FileOutputStream fos  = new FileOutputStream(new File(segDatFile));
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			int nVersion = 1;
			int nMagic = 0x1234;
			// write magic
			oos.writeInt(nMagic);
			// write version
			oos.writeInt(nVersion);
			
			// read hashtable and hashsets
			oos.writeObject(hsWordTag);
			oos.writeObject(htWordMap);
			oos.writeObject(hsChineseNumbers);
			oos.writeObject(hsWASCIIs);
			oos.writeObject(hsForeigns);
			oos.writeObject(hsChineseSurnames);
			oos.writeObject(hsChineseUncommonSurnames);
			oos.writeObject(hsCompSurNames);
			oos.writeObject(hsTitles);
			oos.writeObject(hsSurnameFrequent);
			oos.writeObject(hsNotNames);
			oos.writeObject(hsPunctuations);
			oos.writeObject(hsAddressSuffixes);
			oos.writeObject(hsNotAddresses);
			oos.writeObject(hsNotOrganizationes);
			oos.writeObject(hsOrganizationSuffixes);
			oos.writeObject(hsNameWords);
			oos.writeObject(hsQAWords);
			oos.close();
		}
		catch ( java.io.IOException e)
		{
			log.error("save Dictionary DAT file error",e);
		}
		
		log.info("...save Dictionary DAT file sucess...");
	}
	
	
	/**
     * 将外部<b>基础词词典</b>和<b>特殊词词典</b>文件数据加载到 {@code WordDictionary}类中。
     * @param wordsTxtFile 基础词词典文件路径，文件格式为<b>.txt</b>
     * @param specialTxtFile 特殊词词典文件路径，文件格式为<b>.txt</b>
     * @throws IOException 如果找不到词典数据文件，则抛出该异常。
     */ 
	public void loadFromTXT(String wordsTxtFile, String specialTxtFile) throws IOException{
		// load the word dictionary
		
		RandomAccessFile fileA = new RandomAccessFile(wordsTxtFile,"r");
		while (fileA.getFilePointer() != fileA.length())
		{
			StringTokenizer wordInfo= new StringTokenizer(decode(fileA.readLine()));
			String strWord=wordInfo.nextToken() ;
			int nType = getWordType( strWord );
			htWordMap.put(strWord, new Integer(nType | INT_WordType_Whole));
			
			int len = strWord.length();
			//add prefix
			for (int i = 1; i < len; i ++ )
			{
				String strPrefix = strWord.substring(0, i);
				nType = getWordType( strPrefix );
				htWordMap.put(strPrefix, new Integer(nType | INT_WordType_Prefix));
			}
			
			//add suffix
			for (int i = 1; i < len; i ++ )
			{
				String suffixStr = strWord.substring(i, len);
				nType = getWordType(suffixStr);
				htWordMap.put(suffixStr, new Integer(nType | INT_WordType_Suffix));
			}
			
			Set tagSet=new HashSet();
			while(wordInfo.hasMoreTokens()){
				tagSet.add(wordInfo.nextToken());
			}
			hsWordTag.put(strWord,tagSet);
		}
		fileA.close() ;
		
		// load special character dictionary
		RandomAccessFile fileB = new RandomAccessFile(specialTxtFile,"r");
		//load Chinese Number character
		// 中文数字 1
		String strChineseNumbers  = decode(fileB.readLine()); 
		for (int i = 0; i <  strChineseNumbers.length(); i ++)
			hsChineseNumbers.add(strChineseNumbers.substring(i,  i + 1));
		
		// load wide Ascii character
		// Ascii 2
		String strWASCIIs = decode(fileB.readLine()); 
		for (int i = 0; i <  strWASCIIs.length(); i ++)
			hsWASCIIs.add(strWASCIIs.substring(i, i + 1));
		//	load foreign name transliteration characters
		// 外国译名用字 3
		String strForeigns =  decode(fileB.readLine());
		for (int i = 0; i <  strForeigns.length(); i ++)
			hsForeigns.add(strForeigns.substring(i,  i + 1));
		//	load Chinesesurnames
		// 中文姓氏 4
		String strChineseSurnames=decode(fileB.readLine());
		for (int i = 0; i <  strChineseSurnames.length(); i ++){
			if (!hsChineseSurnames.contains(strChineseSurnames.substring(i,  i + 1)))
				hsChineseSurnames.add(strChineseSurnames.substring(i,  i + 1));
		}
		// 特殊姓氏 5
		String strChineseUncommonSurnames =decode(fileB.readLine());
		for (int i = 0; i <  strChineseUncommonSurnames.length(); i ++){
			hsChineseUncommonSurnames.add( strChineseUncommonSurnames.substring(i,  i + 1) );
			
			// add to hsChineseSurnames also
			hsChineseSurnames.add( strChineseSurnames.substring(i, i + 1) );
		}
		
		// 2 character Chinese surnames; also add to lexicon so they'll be segmented as one unit
		// 双字姓氏 6
		String strChineseCompSurnames = decode(fileB.readLine());
		for (int i = 0; i <  strChineseCompSurnames.length(); i += 2){
			hsChineseSurnames.add(strChineseCompSurnames.substring(i,  i + 2));
			
			// add to seg lexicon also
			htWordMap.put(strChineseCompSurnames.substring(i,  i + 2), new Integer(INT_WordType_Whole));
		}
		//	人名称谓 7
		String strNrTitle=decode(fileB.readLine());
		for (int i=0;i<strNrTitle.length();i++){
			int start=i;
			while (strNrTitle.charAt(i)!=' ')
				i++;
			if (!hsTitles.contains(strNrTitle.substring(start,i)))
				hsTitles.add(strNrTitle.substring(start,i));
		}
		// 常用姓氏用字 8
		String strSurnameFrequent=decode(fileB.readLine());
		for (int i = 0; i <  strSurnameFrequent.length(); i ++)
			hsSurnameFrequent.add(strSurnameFrequent.substring(i,  i + 1));
		// 禁用单字人名 9
		String strNotNames  = decode(fileB.readLine());
		// 禁用双字人名 10
		String strNotNamesTwo = decode(fileB.readLine());//主要是中性词或贬义词，二字名字形容词居多，动词较少10
		// 标点符号 11
		String strPunctuations = decode(fileB.readLine());
		for (int i = 0; i <  strPunctuations.length(); i ++)
			hsPunctuations.add(strPunctuations.substring(i,  i + 1));
		for (int i = 0; i <  strNotNames.length(); i ++)
			hsNotNames.add(strNotNames.substring(i,  i + 1));
		for (int i = 0; i <  strNotNamesTwo.length()-1; i += 2)
			hsNotNames.add(strNotNamesTwo.substring(i,  i + 2));
		hsNotNames.addAll( hsPunctuations );
		// 单字地名后缀 12
		String strSingleAddressSuffixes  = decode(fileB.readLine());
		// 双字地名后缀 13
		String strDoubleAddressSuffixes  = decode(fileB.readLine());
		// 三字地名后缀 14
		String strTriAddressSuffixes  = decode(fileB.readLine());
		// 其它地名后缀 15
		String strAddressSuffixes=decode(fileB.readLine());
		for (int i = 0; i <  strSingleAddressSuffixes.length(); i ++)
			hsAddressSuffixes.add(strSingleAddressSuffixes.substring(i,  i + 1));
		for (int i = 0; i <  strDoubleAddressSuffixes.length()-1; i += 2)
			hsAddressSuffixes.add(strDoubleAddressSuffixes.substring(i,  i + 2));
		for (int i = 0; i <  strTriAddressSuffixes.length() - 2; i += 3 )
			hsAddressSuffixes.add(strTriAddressSuffixes.substring(i,  i + 3));
		for (int i=0;i<strAddressSuffixes.length();i++){
			int start=i;
			while (strAddressSuffixes.charAt(i)!=' ')
				i++;
			if (!hsAddressSuffixes.contains(strAddressSuffixes.substring(start,i)))
				hsAddressSuffixes.add(strAddressSuffixes.substring(start,i));
		}
		// 禁用单字地名后缀 16
		String strNotAddresses  =decode(fileB.readLine()) ;
		// 禁用双字地名后缀 17
		String strNotAddressTwo =decode(fileB.readLine());
		for (int i = 0; i <  strNotAddresses.length(); i ++)
			hsNotAddresses.add(strNotAddresses.substring(i,  i + 1));
		for (int i = 0; i <  strNotAddressTwo.length()-1; i += 2)
			hsNotAddresses.add(strNotAddressTwo.substring(i,  i + 2));
		hsNotAddresses.addAll( hsPunctuations );
		
		//	禁用组织后缀 18 19
		String strNotOrganization=decode(fileB.readLine());
		String strNotOrganizationTwo=decode(fileB.readLine());
		for (int i = 0; i <  strNotOrganization.length(); i ++)
			hsNotOrganizationes.add(strNotOrganization.substring(i,  i + 1));
		for (int i = 0; i <  strNotOrganizationTwo.length()-1; i += 2)
			hsNotOrganizationes.add(strNotOrganizationTwo.substring(i,  i + 2));
		hsNotOrganizationes.addAll( hsPunctuations );
		
		//	组织名后缀 20
		String strOrganizationSuffixes=decode(fileB.readLine());
		for (int i=0;i<strOrganizationSuffixes.length();i++)
		{
			int start=i;
			while (strOrganizationSuffixes.charAt(i)!=' ')
				i++;
			if (!hsOrganizationSuffixes.contains(strOrganizationSuffixes.substring(start,i)))
				hsOrganizationSuffixes.add(strOrganizationSuffixes.substring(start,i));
		}
		
		// 人名用字  21 22 23 24
		String strNameWords ="";
		while(fileB.getFilePointer() !=fileB.length() ){
			strNameWords+=decode(fileB.readLine()); 
		}
		for (int i = 0; i <  strNameWords.length(); i ++)
			hsNameWords.add(strNameWords.substring(i,  i + 1));
		//// end of load special character dictionar
		
		System.out.println("load Dictionary TXT file sucess...");
	}
	
	public void loadQAWord(String path) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(path));
		String wordInfo = "";
		while ((wordInfo = br.readLine())!= null){
			StringTokenizer token = new StringTokenizer(wordInfo);
			String word = token.nextToken().trim() ;
			Integer data = new Integer((token.nextToken().trim()));
			hsQAWords.put(word,data);
		}
		br.close();
	}
	
	public int getWordType(String strWord){
		if(htWordMap.containsKey( strWord)){
			Integer intType = (Integer)htWordMap.get(strWord);
			return intType.intValue();
		}
		else return 0;
		
	}   // getWordType
	
	public boolean isWordPrefix(String strWord)
	{
		int nType = getWordType( strWord );
		if ( (nType & INT_WordType_Prefix) != 0)
			return true;
		else return false;
		
	}   // isWordPrefix
	
	//check subroutines: check the string if  it is word or word suffix
	public boolean isWordSuffix(String wordStr) {
		int type = getWordType(wordStr);
		if ( (type & INT_WordType_Suffix) != 0) {
			return true;
		}
		else {
			return false;
		}
		
	} // isWordSuffix
	
	public boolean isWholeWord(String strWord) {
		int nType = getWordType( strWord );
		if ( (nType & INT_WordType_Whole) != 0)
			return true;
		else return false;
		
	}   // isWholeWord
	
	public boolean isWholeWordAndPrefix(String strWord)
	{
		int nType = getWordType( strWord );
		if (((nType & INT_WordType_Whole) != 0) && ((nType & INT_WordType_Prefix) != 0))
			return true;
		else return false;
		
	}   // isWholeWordAndPrefix
	
	public boolean isWholeWordAndSuffix(String wordStr) {
		int type = getWordType(wordStr);
		if ( ( (type & INT_WordType_Whole) != 0) &&
				( (type & INT_WordType_Suffix) != 0)) {
			return true;
		}
		else {
			return false;
		}
		
	} // isWholeWordAndSuffix
	
	public boolean isWholeWordOrSuffix(String wordStr) {
		int type = getWordType(wordStr);
		if ( ( (type & INT_WordType_Whole) != 0) ||
				( (type & INT_WordType_Suffix) != 0)) {
			return true;
		}
		else {
			return false;
		}
	} // isWholeWordOrSuffix
	
	public boolean isWholeWordOrPrefix(String strWord)
	{
		int nType = getWordType( strWord );
		if (((nType & INT_WordType_Whole) != 0) || ((nType &INT_WordType_Prefix) != 0))
			return true;
		else return false;
		
	}   // issWholeWordOrPrefix
	
	// check subroutines: if every char in the string is Chinese Number
	// NOTICE: the plain ascii digital character is also treated as Chinese Number
	public boolean isChineseNumber(String strWord)
	{
		if ( strWord == null )
			return false;
		if ( strWord.length() == 0 )
			return false;
		
		for (int i = 0; i < strWord.length() ; i ++ ){
			if (  !hsChineseNumbers.contains(strWord.substring(i,  i + 1))
					&& ! ((strWord.charAt(i)>= '0' &&  strWord.charAt(i)<= '9')||strWord.charAt( i)=='.')){
				if (((i+1) <strWord.length())
						&&(strWord.substring(i,  i + 1).equalsIgnoreCase("分"))&&(strWord.substring(i+1,  i+2).equalsIgnoreCase("之"))){
					i=i+2;
				}else {
					return false;
				}
			}
		}
		
		return true;
		
	}   // isChineseNumber(String strWord)
	
	public boolean isChineseNumber(char ch)
	{
		String str = "" + ch;
		
		return hsChineseNumbers.contains( str ) || (ch>= '0' &&  ch<= '9');
	}   // isChineseNumber(char ch)
	
	// check subroutines: if every char in the string is WASCII
	public boolean isWASCII(String strWord)
	{
		if ( strWord == null )
			return false;
		if ( strWord.length() == 0 )
			return false;
		
		for (int i = 0; i < strWord.length() ; i ++ )
			if (!hsWASCIIs.contains(strWord.substring(i,  i + 1)))
				return false;
		
		return true;
		
	}   // isWASCII
	
	public boolean isWASCII(char ch)
	{
		String str = "" + ch;
		
		return hsWASCIIs.contains( str );
		
	}   // isWASCII(char ch)
	
	// check subroutines: if the string is Chinese Surname
	public boolean isChineseSurname(String strWord)
	{
		if ( strWord == null )
			return false;
		
		return hsChineseSurnames.contains(strWord);
		
	}   // isChineseSurname
	
	// check subroutines: if the string is Chinese Uncommon Surname
	public boolean isChineseUncommonSurname(String strWord)
	{
		if ( strWord == null )
			return false;
		
		return hsChineseUncommonSurnames.contains(strWord);
		
	}   // isChineseUncommonSurname
	
	// check subroutines: if the string is not name char
	public boolean isNotName(String strWord)
	{
		if ( strWord == null )
			return false;
		
		return hsNotNames.contains(strWord);
		
	}   // isNotName
	
	//check if the String is name word or not
	public boolean isNameWord(String strWord){
		
		if ( strWord == null )
			return false;
		
		return hsNameWords.contains(strWord);
		
	}//isNameWord
	
	//check if the string is name title or not
	public boolean isNameTitle(String strWord){
		
		if ( strWord == null )
			return false;
		
		return hsTitles.contains(strWord);
	}//isNameWord
	
//	by cyd
	public int isQAWord(String strWord){
		if ( strWord == null )
			return 0;
		else if ((Integer)(hsQAWords.get(strWord)) != null)
			return ((Integer)(hsQAWords.get(strWord))).intValue();
		else return 0;
		
	}
	
	// check subroutines: if the string is not Address char
	public boolean isNotAddress(String strWord)
	{
		if ( strWord == null )
			return false;
		
		return hsNotAddresses.contains(strWord);
		
	}   // isNotAddress
	
	//check subroutines: if the string is not Organization string
	public boolean isNotOrganization(String strWord){
		if ( strWord == null )
			return false;
		
		for (int i=0 ; i<strWord.length() ; i++){
			if (hsNotOrganizationes.contains(strWord.substring(i,i+1)))
				return true;
		}
		
		return false;
	}
	
//	check subroutines: if the string is not Address suffix
	public boolean isAddressSuffix(String strWord)
	{
		if ( strWord == null )
			return false;
		
		return hsAddressSuffixes.contains(strWord);
		
	}   // isNotAddress
	
	//check subroutines: if the String is not organization suffix
	public boolean isOrganizationSuffix(String strWord){
		
		if ( strWord == null )
			return false;
		
		return hsOrganizationSuffixes.contains(strWord);
		
	}
	
	// check subroutines: if the string is Punctuation char
	public boolean isPunctuation(String strWord)
	{
		if ( strWord == null )
			return false;
		
		return hsPunctuations.contains(strWord);
		
	}   // isPunctuation
	
	// check subroutines: if the char is Punctuation char
	public boolean isPunctuation(char ch)
	{
		String str = "" + ch;
		return hsPunctuations.contains(str);
		
	}   // isPunctuation
	
//	check subroutines: if every char in the string is foreign name transliteration characters
	public boolean isForeign(String strWord)
	{
		if ( strWord == null )
			return false;
		if ( strWord.length() == 0 )
			return false;
		
		for (int i = 0; i < strWord.length() ; i ++ )
			if (!hsForeigns.contains(strWord.substring(i,  i + 1)))
				return false;
		
		return true;
		
	}    // isForeign(String strWord)
	
	public boolean isForeign(char ch)
	{
		String str = "" + ch;
		
		return hsForeigns.contains( str );
		
	}   // isForeign(char ch)
	
	public boolean isSurnameFrequent(String word){
		if ( word == null )
			return false;
		
		return hsSurnameFrequent.contains(word);
	}
	public Set getWordTag(String word){
		if(hsWordTag.containsKey( word)){
			return (Set)hsWordTag.get(word);
		}
		else return null;
	}
	
	public String decode(String s)
	{
		try
		{
			return new String(s.getBytes("ISO8859_1"), "GBK");
		}
		catch(Exception e){
		}
		return null;
	}
	
	public static void main(String[] orgs) throws IOException{
//		WordDictionary dic = new WordDictionary("basewords.txt","specialword.txt");
		WordDictionary dic = new WordDictionary();
	}
}
