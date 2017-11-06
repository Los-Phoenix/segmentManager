package hmm;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Statistic {
    
    private  final int TAGLENGTH;
    private  final int WORDLENGTH;
    
    private int[] wordsFrequency;
    private int[] tagsFrequency;
    private int[] firstTagFrequency;
    private int[][] tagsTransferFrequency;
    private Map[] tags_WordsFrequency;
    
    private double[] matrixPi;
    private double[][] matrixA;
    private Map matrixB;
    
    private List tagsList;
    private List wordsList;
    
    
    /**
    ��Paramter�д������tagsList,wordsList
    tagsList:�����б�,���ڽ�����������
    wordsList:�۲�ֵ�б�,���ڽ����۲�ֵ����
    @return  Number of observation symbols.
    */
    Statistic(List tagsList, List wordsList){
        this.tagsList = tagsList;
//        this.wordsMap = new LinkedHashMap(wordMap);
        this.wordsList = wordsList;
        TAGLENGTH = tagsList.size();
        WORDLENGTH = wordsList.size();
    }
    
    /**
    fileName:ѵ������,����ÿ��һ��,ÿ������: �۲�ֵ1/����1 + �ո� + �۲�ֵ2/����2 + ....
    ͳ�Ƹ���Ƶ��Ϣ
    ÿ�����ӵ���Ϣ����sentence��
    ����computerPi() computerA() computerB()�������
    */
    public void statisticFrequencyFromTXT(String fileName) throws IOException {
        wordsFrequency =        new int[WORDLENGTH];
        tagsFrequency =         new int[TAGLENGTH];
        firstTagFrequency =     new int[TAGLENGTH];
        tagsTransferFrequency = new int[TAGLENGTH][TAGLENGTH];
        //Map's key is the index of word in wordsMap,
        //Map's value is the tags of single word pointed by key
        tags_WordsFrequency =   new Map[TAGLENGTH];
        for (int i=0; i<TAGLENGTH; i++){
            tags_WordsFrequency[i] = new HashMap();
        }
        
        Set removeSet = new HashSet();
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String line = "";
        int count = 0;
        while ((line = br.readLine()) != null){
            System.out.println(count++ + line);
            Sentence senT = new Sentence(line);
            
            //����firstTagFrequency
            String firstTag = senT.getFirstTag();
            int indexPreTag = tagsList.indexOf(firstTag);
            if (indexPreTag != -1)
                firstTagFrequency[indexPreTag]++;
            else {
                System.out.println(" tag error:" + firstTag);
                System.exit(1);
            }
            
            List tagList = senT.getTagList();
            List wordList = senT.getWordList();
            
            for (int i=0, wordNo = senT.getWordSize(); i<wordNo; i++){
                String tag = (String)tagList.get(i);
                String word = (String)wordList.get(i);
                int indextag = tagsList.indexOf(tag);
                int indexword = wordsList.indexOf(word);
                
                // ����tagsFrequency
                tagsFrequency[indextag]++;
                
                //����wordsFrequency tags_WordsFrequency
                if (indexword != -1){
                    wordsFrequency[indexword]++;
                    Integer indexI = new Integer(indexword);
                    if (tags_WordsFrequency[indextag].containsKey(indexI)){
                        int in = ((Integer)tags_WordsFrequency[indextag].get(indexI)).intValue();
                        tags_WordsFrequency[indextag].put(indexI,new Integer(in + 1));
                    }else {
                        tags_WordsFrequency[indextag].put(indexI,new Integer(1));
                    }
                }else {
                    removeSet.add(word);
                }
                
                //����tagsTransferFrequency
                if (i > 0){
                    tagsTransferFrequency[indexPreTag][indextag]++;
                    indexPreTag = indextag;
                }
            }
        }
        br.close();
        
        //����
        Iterator itr = removeSet.iterator();
        while (itr.hasNext()){
            System.out.println(itr.next());
        }
        
        computerPi();
        computerA();
        computerB();
    }
    
    private double[] computerPi(){
        int totalFirstTagFrequency = 0;
        matrixPi = new double[TAGLENGTH];
        for (int i=0; i<TAGLENGTH; i++){
            totalFirstTagFrequency += firstTagFrequency[i];
        }
        for (int i=0; i<TAGLENGTH; i++){
            matrixPi[i] = (double)firstTagFrequency[i]/totalFirstTagFrequency;
        }
        return matrixPi;
    }
    
    private double[][] computerA(){
        matrixA  = new double[TAGLENGTH][TAGLENGTH]; 
        for (int i=0; i<TAGLENGTH; i++)
            for (int j=0; j<TAGLENGTH; j++){
                matrixA[i][j] = (double)tagsTransferFrequency[i][j]/tagsFrequency[i];
            }
        return matrixA;
    }
    
    
    /**
    ���ּ��㷽��ȡ���һ�ּ���
    */
    private Map computerB(){
        matrixB  = new HashMap();
        int total=0;
        int totalTagFrequency=0;
        for (int i=0 ; i<TAGLENGTH; i++){
            totalTagFrequency += tagsFrequency[i];
            Iterator itr = tags_WordsFrequency[i].keySet().iterator();
            while (itr.hasNext()){
                total += ((Integer)tags_WordsFrequency[i].get(itr.next())).intValue();
            }
        }
        int totalWordFrequency=0;
        for (int i=0 ; i<WORDLENGTH ; i++){
            totalWordFrequency += wordsFrequency[i]; //all word frequency ?SumWord == sum?
        }
        
        //�򵥼���
        for (int i=0; i<TAGLENGTH; i++){
            Iterator itr = tags_WordsFrequency[i].keySet().iterator();
            while (itr.hasNext()){
                Integer temp = (Integer)itr.next();
                StringBuffer sf = new StringBuffer();
                sf.append(i);
                sf.append(temp.intValue());
                double dou = (double)((Integer)tags_WordsFrequency[i].get(temp)).intValue()/tagsFrequency[i];
                matrixB.put(sf.toString(),new Double(dou));
            }
        }
            
//            for (int j=0; j<WORDLENGTH; j++)
//                matrixB[i][j]=((double)(totalTagFrequency)/(double)total)
//                *((double)((tags_WordsFrequency[i][j]))
//                        /(double)((tagsFrequency[i])));
        
        //���Ӽ���
        
//        for (int i=0; i<TAGLENGTH; i++)
//            for (int j=0; j<WORDLENGTH; j++){ 
//                double pwt= (double)(tags_WordsFrequency[i][j])/(double)(tagsFrequency[i]);
//                double po = (double)(wordsFrequency[j])/(double)(totalWordFrequency);
//                double pt = (double)(tagsFrequency[i])/(double)(totalTagFrequency);
//                matrixB[i][j]=(double)(pwt)*(double)(po)/(double)(pt);
//            }
        return matrixB;
    }
    
    
    
    
//    private void clear(){
//        for (int i=0; i<TAGLENGTH; i++){
//            tagsFrequency[i] = 0;
//            firstTagFrequency[i] = 0;
//            matrixPi[i] = 0;
//            for (int j=0; j<WORDLENGTH; j++){
//                wordsFrequency[j] = 0;
//                tags_WordsFrequency[i][j] = 0;
//                matrixB[i][j] = 0;
//            }
//        }
//        for (int i=0; i<TAGLENGTH; i++)
//            for (int j=0; j<TAGLENGTH; j++){
//                tagsTransferFrequency[i][j] = 0;
//                matrixA[i][j] = 0;
//            }
//    }
    
    
    public double[] getPi_element(){
        return matrixPi;
    }
    public double[][] getA_element(){
        return matrixA;
    }
    public Map getB_element(){
        return matrixB;
    }
    
    
}
