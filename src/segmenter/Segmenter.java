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
     * �������봮�еķ־���Ż����־䣬�����з�;
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
     * processChineseName: ʶ����������
     * ���룺�ִʺ�Ĵ�����
     * �����ʶ�����ֺ�Ĵ�����
     *
     *
     * �������ֹ���ԭ��
     * ���ջ��� + ��������(�������ֽ�����) + ��������(�������ֽ�����)
     * ���ջ��� + ��������(�������ֽ�����)
     * ���õ��ջ��� + ˫������(�������ֽ�����)
     * �ǳ����� + ˫������(�������ֽ����֣��ҷǴ�)
     *
     * ���̣�
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
     * processChineseAddress: ʶ�����ĵ���
     * ���룺�ִʺ�Ĵ�����
     * �����ʶ�����ĵ�����Ĵ�����
     *
     *
     * ���ĵ�������ԭ��
     * (��������(���ǵ���������) + ˫������(���ǵ���������))*+ ������׺
     *  �ҵ������Ȳ�����3
     *
     * ���̣�
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
      * �ж������ַ��Ƿ�Ϊ������
      * @param ch
      * @return
      */
     public boolean isChineseChar(char ch);
}
