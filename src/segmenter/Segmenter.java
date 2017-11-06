package segmenter;

import java.util.List;


public interface Segmenter {

    /**
    segmenter the input string
    @param strInput is a string which is being segmented
    @return arraylist records the segmentation sequence
    */
    public List<String> segment(String strInput);
    
    
    /**
     * 根据输入串中的分句符号划分字句，再作切分;
     * @param str
     * @return
     */
    public List segmentByPunctuation(String str);
    

    /**
    segmenter the substring from the character at the specified index to the end of this string
    @param strInput is a string which is being segmented
    @param nStartIndex is the specified index
    @return arraylist records the segmentation sequence of substring
     */
    public String segmentNextWord(String strInput, int nStartIndex);
    
    
    
    /**
     * processChineseName: 识别中文名字
     * 输入：分词后的词序列
     * 输出：识别名字后的词序列
     *
     *
     * 中文名字构成原则：
     * 单姓或复姓 + 单中文字(不是名字禁用字) + 单中文字(不是名字禁用字)
     * 单姓或复姓 + 单中文字(不是名字禁用字)
     * 常用单姓或复姓 + 双中文字(不是名字禁用字)
     * 非常用姓 + 双中文字(不是名字禁用字，且非词)
     *
     * 流程：
     *
     * 1 Initial:
     *  nCurrent = 0;
     *
     * 2 scane the word list, if current word is  a surname,
     *   get the next two words, according to the rules above , to check if
     *   the current word and its succed two words can consist a name
     */
    //public List processChineseName(List wordList);
    
    /**
     * processChineseAddress: 识别中文地名
     * 输入：分词后的词序列
     * 输出：识别中文地名后的词序列
     *
     *
     * 中文地名构成原则：
     * (单中文字(不是地名禁用字) + 双中文字(不是地名禁用字))*+ 地名后缀
     *  且地名长度不超过3
     *
     * 流程：
     *
     * 1 Initial:
     *  nCurrent = 0;
     *
     * 2 scane the word list from the tail to the start, if current word is  a address suffix,
     *   get the previous three words, according to the rules above , to check if
     *   the current word and its previous three words can consist a address
     */
     //public List processChineseAddress(List wordList);
     
     
     /**
      * 判断输入字符是否为中文字
      * @param ch
      * @return
      */
     public boolean isChineseChar(char ch);
}
