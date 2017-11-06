package hmm;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import dictionary.ParameterConfig;


public class Parameter{
    Statistic statistic;
    private final String CopusFileName = 
        "nlp/software/nudt/edu/cn/QAanalysis/txt/199801.txt";
    private final String ParameterFileName = 
        ParameterConfig.getString("hmm");
    private final String wordsFileName = "nlp/software/nudt/edu/cn/QAanalysis/txt/wordpos.txt";
    
//    词性表,45
    private List tagsList;
    
//    观察值表,108750
    private List wordsList;
    
//    参数Pi,45
    private double[] matrixPi;
    
//    参数A,大小:45*45
    private double[][] matrixA;
    
//    参数B 
//    key:String = 当前词性索引 + 当前观察值索引
//    value:Double 
    private Map matrixB;
    private static Logger log = LogManager.getLogger(Parameter.class);
    public Parameter(){
        try{
        load();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    
    /**
     * 对参数重估计,重新保存至hmm.dat
     */
    Parameter(String str) throws IOException{
        if (str.equalsIgnoreCase("reload")){
            loadFromCopus(CopusFileName);
            save(ParameterFileName);
        }else {
            System.out.println("please input 'reload' to load txt");
            load();
        }
    }
    
    void load()throws IOException{
//    	URL url = Parameter.class.getResource(ParameterFileName);
        InputStream url = Parameter.class.getResourceAsStream(ParameterFileName);
        ObjectInputStream ois = null;
        try
        {
//        	fis = new FileInputStream(new File(url.toURI()));
        	ois = new ObjectInputStream(url);
            tagsList = (List)ois.readObject();
            wordsList = (List)ois.readObject();
            matrixPi = (double[])ois.readObject();
            matrixA = (double[][])ois.readObject();
            matrixB = (Map)ois.readObject();
        }
        catch ( java.lang.ClassNotFoundException e){
            log.error("Load hmm.dat file error.",e);
        } catch (FileNotFoundException e) {
			log.error(e.getMessage(),e);
		}
        ois.close();
        log.debug("Load hmm file success......");
    }
    
    /**
     * 对参数重估计,调用statisticFrequencyFromTXT()
     */
    void loadFromCopus(String fileName) throws IOException {
        try{
            loadTagList();
            loadWordsList();
            statistic = new Statistic(tagsList,wordsList);
            statistic.statisticFrequencyFromTXT(fileName);
            matrixPi = statistic.getPi_element();
            matrixA = statistic.getA_element();
            matrixB = statistic.getB_element();
        }
        catch (IOException e){
            System.out.println("Load copus file error.");
            e.printStackTrace();
        }
    }
    
    void loadTagList(){
        final String[] tagArray = {
                "w","i","s","r","ns","v","Vg","f","x","d","vd","ad","Dg",
                "k","nt","j","p","c","q","n","vn","an","Ng","o","nz","h",
                "b","nr","t","Tg","m","e","l","a","Ag","y","g","u","z",
                "nx","Bg","Rg","vvn","Mg","na"};
        tagsList = Arrays.asList(tagArray);
    }
    
    /**
     * 从wordsFileName中读取词典,词典带词性
     */
    void loadWordsList(){
        try{
            wordsList = new ArrayList();
            BufferedReader br = 
                new BufferedReader(new FileReader(wordsFileName));
            String line;
            while ((line = br.readLine()) != null){
                StringTokenizer st = new StringTokenizer(line);
                if (st.hasMoreTokens())
                  wordsList.add((String)st.nextToken());
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    
    void save(String saveFile) throws IOException {
        FileOutputStream fos = new FileOutputStream(saveFile);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(tagsList);
        oos.writeObject(wordsList);
        oos.writeObject(matrixPi);
        oos.writeObject(matrixA);
        oos.writeObject(matrixB);
        oos.close();
    }
    
    public int getIndexOfWord(String word){
        return wordsList.indexOf(word);
    }
    
    public int getIndexOfTag(String tag){
        return tagsList.indexOf(tag);
    }
    
    public String getTag(int index){
        return (String)tagsList.get(index);
    }
    
    
    public double getA_element(int fromTag, int toTag) {
        return matrixA[fromTag][toTag];
    }

    public double getPi_element(int Tag) {
        return matrixPi[Tag];
    }
    
//    通过(String)tag + (String)word 查找matrixB
    public double getB_element(int tag, int word) {
        double dou = 0.0;
        StringBuffer bf = new StringBuffer();
        bf.append(String.valueOf(tag));
        bf.append(String.valueOf(word));
        String index = bf.toString();
        if (matrixB.containsKey(index))
            dou = ((Double)matrixB.get(index)).doubleValue();
        return dou;
    }
    
    public double getPi_element(String str) {
        double prob = 0.0;
        int index = tagsList.indexOf(str);
        if (index ==  -1){
            prob = getPi_element(index);
        }
        else {
            System.out.println("unvalid tag: " + str);
            System.exit(1);
        }
        return prob;
    }
    
    public double getA_element(String from, String to){
        double prob = 0.0;
        int fromindex = tagsList.indexOf(from);
        int toindex = tagsList.indexOf(to);
        if (fromindex != -1 || toindex != -1){
            prob = getA_element(fromindex,toindex);
        }else {
            System.out.println("unvalid tag: " + from  + " " + to);
            System.exit(1);
        }
        return prob;
    }
    
    
    
    public double getB_element(String tag, String word) {
        double prob = 0.0;
        int tagindex = tagsList.indexOf(tag);
        int wordindex = wordsList.indexOf(word);
        if (tagindex == -1){
            System.out.println("unvalid tag: " + tag);
            System.exit(1);
        }else if (wordsList.contains(word))
            prob = getB_element(tagindex,wordindex);
        return prob;
    }
    
    public List getTagsList(){
        return this.tagsList;
    }
    
    /*
    public static void main(String[] orgs) throws IOException{
        Date startDateTime = new Date();
        long startTime = startDateTime.getTime();
        
        Parameter par = new Parameter("reload");
        
        Date endDateTime = new Date();
        long endTime = endDateTime.getTime();
        System.out.println("al处理时间: " + (startTime - endTime) + "ms");
        
    }
  */
    
}