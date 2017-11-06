package hmm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import chineseUnit.Sentence;
import dictionary.DictionaryFactory;
import segmenter.Segmenter;
import segmenter.SegmenterManager;


public class Hmm implements Serializable {
//    private static final int MAX_ITERATION = 100;
//    private static final double MIN_ERROR = 0.001;
//    private static final int MAX_LEN_OBSEQ = 10;
    private static final long serialVersionUID = 1L;
    private static Logger log = LogManager.getLogger(Hmm.class);
//    用于加载参数概率值
    private Parameter para;
    private SearchTags searchTags;

//    每个分词结果用一个word类表示,类保存信息:观察值,词性列表,实体词性列表,观察值索引
    private Word[] wordList;
   
    public Hmm(){
        para = new Parameter();
        searchTags = new SearchTags();
    }
    
    private int[][] tagsSpaceSearch(WordInfo[] wordInfos){
        wordList = new Word[wordInfos.length];
        for (int i=0; i<wordInfos.length; i++){
        	String temp = wordInfos[i].getWord();
            int index = para.getIndexOfWord(wordInfos[i].getWord());
            Set tagS = wordInfos[i].getWordTagList();
            wordList[i] = new Word(index,temp,tagS,null);
        } 
        //状态空间转换 strNewpos[i][j] indicate the j-th tag of i-th word
        int[][] wordTags = new int[wordInfos.length][];
        for (int i=0;i<wordTags.length;i++){
            wordTags[i] = new int[wordList[i].getWordTags().size()];
            int j=0;
            Iterator itr = wordList[i].getWordTags().iterator();
            while (itr.hasNext()){
                String tag = (String)itr.next();
                int index = para.getIndexOfTag(tag);
                if (index==-1 && wordList[i].getWordTags().size()==1){
                	tag = tag.substring(0,tag.length()-1);
                	tag = tag.toLowerCase();
                	wordTags[i][j++] = para.getIndexOfTag(tag);
                }else if (index==-1 && !(wordList[i].getWordTags().size()==1)){
                	int[] tempwordTags = new int[wordTags[i].length-1];
                	for (int k=0; k<j; k++){
                		tempwordTags[k]=wordTags[i][k];
                	}
                	wordTags[i]=tempwordTags;
                }else if (index != -1){
                	wordTags[i][j++] = index;
                }
            }
        }
        return wordTags;
    }
    
    
    
    private int[] viterbiAlgorithm(WordInfo[] wordInfos){
    	final int LENGTH = wordInfos.length;
    	if (LENGTH == 0)
    		return null;
    	int[][] wordTags = tagsSpaceSearch(wordInfos);
        //初始化
        Temporary[][] delta = new Temporary[wordTags.length][] ;
        Temporary[][] psi = new Temporary[wordTags.length][];
        
        for (int i=0; i<wordTags.length; i++){
            delta[i] = new Temporary[wordTags[i].length];
            psi[i] = new Temporary[wordTags[i].length];
        }
        
        
        for (int i=0 ; i<wordTags[0].length ; i++){
            Temporary theTemporary;
            int state = wordTags[0][i];
            String tag = para.getTag(state);
            double p=0.0;
            if ((tag.equalsIgnoreCase("nr"))||(tag.equalsIgnoreCase("ns"))||(tag.equalsIgnoreCase("nt")))
                p = para.getPi_element(state)*1;
            else
                p = para.getPi_element(state)*para.getB_element(state,wordList[0].getIndex());
            theTemporary = new Temporary(state,p);
            delta[0][i]=theTemporary;
            psi[0][i] = new Temporary(-1,0.0);
        }
        //初始化完毕
        
        //递归
        for (int i=1 ; i<LENGTH; i++){
            delta[i]=new Temporary[wordTags[i].length];
            psi[i]=new Temporary[wordTags[i].length];
            
            for (int stateno=0 ; stateno<wordTags[i].length ; stateno++){
//            	if (i==14)
//            		System.out.println("carefully");
                double maxval=0.0;
                int maxvalind = -1;
                
                //针对第二个观察值的第j个可能标注,遍历前一个的delta值,计算delta[1][i]*a[i][j],并max
                //MatrixA中有一些值为NaN，表示不为数值
                for (int j=0 ; j<wordTags[i-1].length ; j++){
                    double val = para.getA_element(
                            delta[i-1][j].getTagIndex(),wordTags[i][stateno]);
                    if (!Double.isNaN(val)){
                    	val = val * delta[i-1][j].getProb();
                    }else val = 0;
                    if (val>=maxval){
                        maxval=val;
                        maxvalind=delta[i-1][j].getTagIndex();
                    }
                }
                
                //theTemporary 保存着deltNew[length-1][]-> deltNew[length][]的最大概率和对应的状态
                double p=0.0;
                String tag = para.getTag(wordTags[i][stateno]);
                if ((tag.equalsIgnoreCase("nr"))||(tag.equalsIgnoreCase("ns"))||(tag.equalsIgnoreCase("nt"))
                    || tag.equalsIgnoreCase("m") || tag.equalsIgnoreCase("t"))
                    p = maxval*1;
                else
                    p = maxval*para.getB_element(wordTags[i][stateno],wordList[i].getIndex()); //这里的第二个参数必须修改

                delta[i][stateno] = new Temporary(wordTags[i][stateno],p);
                psi[i][stateno]= new Temporary(maxvalind,0.0);
            }
        }
        //递归完毕
        
        //终止
        double pprob=0.0;
        int[] qstar = new int[wordInfos.length];
        qstar[LENGTH-1] = -1;
        int[] pos4 = wordTags[LENGTH-1];
        for (int i=0 ; i<pos4.length ; i++)
        {
            int state = delta[LENGTH-1][i].getTagIndex();
            if (delta[LENGTH-1][i].getProb()>=pprob)
            {
                pprob=delta[LENGTH-1][i].getProb();
                qstar[LENGTH-1] = state;
            }
        }
        //终止完毕
        
        //最佳路径
        for (int i=(LENGTH-2) ; i>=0 ; i--)
        {
            int[] pos5 = wordTags[i+1];
            for (int indexPos=0 ;indexPos<pos5.length ; indexPos++)
            {
                int state = pos5[indexPos];
                if (state == qstar[i+1]){
                    qstar[i]=psi[i+1][indexPos].getTagIndex();
//                    if (qstar[i] == -1)
//                    	System.err.println("carefully "+qstar[i+1]);
                    break;
                }
            }
        }
        //最佳路径完毕
        return qstar;
    }
    
    
    
    public int getNumStates() {
        return para.getTagsList().size();
    }

    private List getMaxLikelyState(WordInfo[] wordInfos){
        int[] tagList = viterbiAlgorithm(wordInfos);
        List tags = new ArrayList();
        if (tagList == null)
        	return tags;
        for (int i=0; i<tagList.length; i++){
            tags.add(para.getTag(tagList[i]));
        }
        return tags;
    }
    
    @SuppressWarnings("unused")
	private String getTagsList(List al){
        StringBuffer sb = new StringBuffer();
        if (al == null || al.size() != wordList.length){
            System.out.println("Part of speech error!");
            System.exit(1);
        }
        else{
            for (int i=0; i<al.size(); i++){
                sb.append(wordList[i].getWord() + "/" + al.get(i) + "  ");
            }
        }
        return sb.toString();
    }
    
    public chineseUnit.Sentence tag(List segList){
    	WordInfo[] words = searchTags.getWordTag(segList);
    	List tagList = getMaxLikelyState(words);
    	if (tagList.size() != segList.size()){
    		System.err.print("error getMaxLikelyState(List) of Hmm");
    		System.exit(1);
    	}
    	return new chineseUnit.Sentence(segList,tagList);
    }
   
    public static void main(String[] args) throws Exception {
        
        log.info("begin");
        Hmm hmm = new Hmm();
        SegmenterManager sm = new SegmenterManager();
        String str = "什么是hardware？";
        List<String> words = sm.segment(str);
        System.out.println(words);
//        Sentence sen = hmm.tag(words);
//        log.info(sen);
//        log.info("over");
    }
}
