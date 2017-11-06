/*
 * Created on 2004-3-25
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package hmm;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window>Preferencds>Java>Code Generation>Code and Comments
 */
public class Sentence {

	private String sentence;
	private String rawSentence;
	private int noWord;
	private int noTag;
	private String[] wordSeries;
	private String[] tagSeries;
	private String prodSentence;
    
    
    private List tagList;
    private List wordList;
    private String firstTag="";
    private int wordSize=0;
    private int tagSize=0;
	
	/**
	 * 处理单句,获得每句的第一个词,词性序列,观察值序列
	 */
	Sentence(String str)
	{
		processSentence(str);
	}
    
    private void processSentence(String str){
        tagList = new LinkedList();
        wordList = new LinkedList();
        StringTokenizer st = new StringTokenizer(str);
        boolean first = true;
        while (st.hasMoreTokens()){
            String phase = st.nextToken();
            int cut = phase.indexOf("/"); //特定的分割符
            String word = phase.substring(0,cut); //must be tested
            String tag = phase.substring(cut+1);
            if (first){
                firstTag = tag;
                first = false;
            }
            tagList.add(tag);
            wordList.add(word);
            wordSize++;
            tagSize++;
        }
    }
    
    public static void main(String[] orgs){
        String str = "新/a  的/u  机遇/n  和/c  挑战/vn  ，/w  催/v  人/n  进取/v  ；/w";
        Sentence senT = new Sentence(str);
        System.out.println(senT.getFirstTag());
        System.out.println(senT.getTagList());
        System.out.println(senT.getWordList());
        
    }
    
    
    
    public Iterator tagIterator(){
        return tagList.iterator();
    }
    
    public Iterator wordIterator(){
        return wordList.iterator();
    }
    
	public int getWordSize(){
	    return wordSize;
    }
    public int getTagSize(){
        return tagSize;
    }
    public String getFirstTag(){
        return firstTag;
    }
    public List getTagList(){
        if (tagList == null){
            System.out.println("sentence tagList error!");
            System.exit(1);
        }   
        return tagList;
    }
    public List getWordList(){
        if (wordList == null){
            System.out.println("sentence wordList error!");
            System.exit(1);
        }
        return wordList;
    }
    
    public void ProSentence()//合并人名,组织名等
	{
		String TempSentence = sentence;
		String NewSentence = "";
		int point = 0;
		while (point < TempSentence.length())
		{
			//if (point==26)
			//	System.out.println(TempSentence.charAt(point)=='/');
			if (((point+3)<TempSentence.length())&&(TempSentence.substring(point,point+3).equalsIgnoreCase("/nr")))
			{
				int temp = point;
				temp++;
				while ((temp<TempSentence.length())&&(TempSentence.charAt(temp)!='/')) 
					temp++;
				if ((temp+3)<TempSentence.length())
				{
					if (TempSentence.substring(temp,temp+3).equalsIgnoreCase("/nr"))
					{
						while (TempSentence.charAt(temp)!=' ') temp--;
						NewSentence = TempSentence.substring(0,point) + TempSentence.substring(temp+1,TempSentence.length());
						TempSentence = NewSentence;
					}
				}
			}
			
			if (((point+1)<TempSentence.length())&&(TempSentence.substring(point,point+1).equalsIgnoreCase("[")))
			{
				int temp = point;
				while ((temp<TempSentence.length())&&(TempSentence.charAt(temp)!=']')) temp++;
				if (temp==TempSentence.length()) 
					break;
				String substring1 = TempSentence.substring(0,point);
				String substring2 = TempSentence.substring(temp+1,TempSentence.length());
				String substring3 = TempSentence.substring(point,temp+1);
				String tempstring = "";
				for (int index=point+1 ; index<temp ; index++)
				{
					//System.out.println(substring3.substring((index-point),(index-point+1)));
					if (!((substring3.charAt(index-point)== '/')|(substring3.charAt(index-point)== ' ')|((substring3.charAt(index-point)<= 'z')&&(substring3.charAt(index-point)>= 'a'))|((substring3.charAt(index-point)<= 'Z')&&(substring3.charAt(index-point)>= 'A'))))
							tempstring = tempstring + substring3.substring((index-point),(index-point+1));
				}
				NewSentence = substring1+ tempstring + "/" + substring2;
				TempSentence = NewSentence;
			}
			//if (point==180)
			//	System.out.println(TempSentence.charAt(point));		
			point++;
		}
		if (NewSentence.equalsIgnoreCase(""))
			 prodSentence = TempSentence;
		else prodSentence = NewSentence;
			
	}
	
	public void ProcessSentence()//句子中包含多少单词和标记
	{
		int index = 0;
		int count =0;
		int i=0;
		prodSentence = sentence;
		while (i < prodSentence.length())
		{
			if (prodSentence.charAt(i) == '/')
				count++;
			i++;
		}
		
		wordSeries= new String[count];
		tagSeries= new String[count];

		int point = 0;		
		while (point <= prodSentence.length())
		{
			int oldpoint=point;
			if (prodSentence.charAt(point) == '/')
			{
				while ((oldpoint>0)&&(prodSentence.charAt(oldpoint)!=' '))
					oldpoint--;
				if (oldpoint!=0)
					oldpoint++;
				//System.out.println(prodSentence.substring(oldpoint,point));
				wordSeries[index] = prodSentence.substring(oldpoint,point);
				point++;

				int newpoint = point;
				while ((newpoint<prodSentence.length())&&(prodSentence.charAt(newpoint)!=' '))
					newpoint++;
				//System.out.println(prodSentence.substring(point,newpoint));
				tagSeries[index++] = prodSentence.substring(point,newpoint);
				point=newpoint+1;
			} 
			point++;
			
		}
		noWord = index;
		noTag = index;
	}
	
	public void GenRawSentence()//生成原始即未标注语料
	{
		String temp = "";
		for (int x=0 ; x<noWord ; x++)
		{
			temp = temp + this.GetWord(x);
		}
		rawSentence = temp;
	}

	public String GetNewSentence()
	{
		return prodSentence;
	} 
	
	public String GetWord(int index)
	{
		return wordSeries[index];
	}
	
	public String GetTag(int index)
	{
		return tagSeries[index];
	}
	
	public void SetSentence(String LineSubSentence)
	{
		sentence = LineSubSentence;
	}

	public String GetSentence()
	{
		return sentence;
	}
	
	public int GetNoWord()
	{
		return noWord;
	}
	
	public int GetNoTag()
	{
		return noTag;
	}
	
	public String GetRawSentence()
	{
		return rawSentence;
	}
	
}
