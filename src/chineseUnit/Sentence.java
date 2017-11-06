package chineseUnit;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.ListIterator;

import org.apache.log4j.Logger;



public class Sentence {
	private Word first;
	private Word last;
	private int size;
	private int offset;
	private static Logger log = Logger.getLogger(Sentence.class);
	
	public Sentence(){
		this(null,null,0,0);
	}
	public Sentence(Sentence sen){
		this();
		addWordsLast(sen);
		
	}
	
	/**
	 * 通过集合初始化sentence类
	 * @param c 元素类型是Word或者String
	 */
	public Sentence(Collection c){
		this(null,null,0,0);
		Iterator itr = c.iterator();
		while (itr.hasNext()){
			Object obj = itr.next();
			String str = "";
			if (obj instanceof Word){
			  str = ((Word)obj).getText();
			}else if (obj instanceof String){
				str = (String)obj;
			}
			else{
				log.error("...initialize sentence fail...");
			}
//			System.out.println(str);
			addLast(str);
		}
	}
	
	public Sentence(List wordList, List tagList){
		this(null,null,0,0);
		if (wordList.size() != tagList.size()){
			log.error("...initialize sentence fail...");
		}
		for (int i=0, n=wordList.size(); i<n; i++){
			String word = (String)wordList.get(i);
			String tag = (String)tagList.get(i);
			addLast(tag,word);
		}
		
	}
	
	public Sentence(List wordList){
		this(null,null,0,0);
		for (int i=0,n=wordList.size(); i<n; i++){
			String word=(String)wordList.get(i);
			addLast("",word);
		}
	}
	
	private Sentence(Word first, Word last, int size, int offset) {
		this.first = first;
		this.last = last;
		this.size = size;
		this.offset = offset;
	}
	
	public Sentence subSentence(Word begin,Word end){
		int begIndex = this.contain(begin);
		int endIndex = this.contain(end);
		return subSentence(begIndex,endIndex);
	}
	
	public Sentence subSentence(int begin,int end){
		Sentence sen = new Sentence();
		if (begin!=-1 && end!=-1 && begin<=end){
			for (int i=begin; i<=end; i++){
				sen.addLast(this.get(i).getText());
			}
		}
		return sen;
	}
	
	/**
	 * 截取从begin到end的子句
	 * @param begin
	 * @param end
	 * @return
	 */
	public Sentence subSentenceNoChanged(Word begin,Word end){
		
		int begIndex = this.contain(begin);
		int endIndex = this.contain(end);
		Sentence sen = new Sentence();
		if (begIndex!=-1 && endIndex!=-1 && begIndex<=endIndex){
			for (int i=begIndex; i<=endIndex; i++){
				sen.addLast(this.get(i));
			}
//			Word word = sen.getLast();
//			word.setNext(null);
		}
		return sen;
	}
	
	private class WordsIterator implements ListIterator{
		 private Word prev;
		 private Word next;
		 private boolean isForward;
		 
		 public WordsIterator(boolean isForward){
			 this.isForward = isForward;
			 if (isForward){
				 prev = null;
				 next = first;
			 }else {
				 prev = last;
				 next = null;
			 }
		 }
		 
		 public WordsIterator(){
			 this(true);
		 }
		 
		 public boolean hasNext(){
			 return next != null;
		 }
		 
		 public boolean hasPrevious(){
			 return prev != null;
		 }
		 
		 public Object next(){
			 prev = next;
			 next = next.getNext();
			 return prev;
		 }
		 
		 public Object previous(){
			 next = prev;
			 prev = prev.getPrev();
			 return next;
		 }
		 
		 public int previousIndex(){
			 return 0;
		 }
		 
		 public int nextIndex(){
			 return 0;
		 }
		 
		 public void add(Object o){
			 if (!(o instanceof Word)){
				 System.err.println("o is not a word!!!");
				 return ;
			 }
			 if (isForward){
				 prev.setNext((Word)o);
				 ((Word)o).setPrev(prev);
				 next.setPrev((Word)o);
				 ((Word)o).setNext(next);
				 next =  (Word)o;
			 }
			 else{
				 prev.setNext((Word)o);
				 ((Word)o).setPrev(prev);
				 next.setPrev((Word)o);
				 ((Word)o).setNext(next);
				 next =  (Word)o;
				 prev = (Word)o;
			 }
		 }
		 
		 public void remove(){
			 if (isForward)
				 Sentence.this.remove(next);
			 else
				 Sentence.this.remove(prev);
		 }
		 
		 public void set(Object o){
			 if (!(o instanceof Word)){
				 System.err.println("o is not a word!!!");
				 return ;
			 }
			 
			 if (isForward){
				 prev.setNext((Word)o);
				 (next.getNext()).setPrev((Word)o);
				 next = (Word)o;
			 }
			 else{
				 (prev.getPrev()).setNext((Word)o);
				 next.setPrev((Word)o);
				 prev = (Word)o;
			 }
		 }
	}
	
	public WordsIterator iterator(){
		return new WordsIterator();
	}
	
	private void addWordsLast(Sentence sen){
		if (!sen.isEmpty()){
			Iterator itr = sen.iterator();
			while (itr.hasNext()){
				Word theWord = new Word(((Word)itr.next()));
				addLast(theWord);
			}
		}
	}
	
	/**
	 * just be used by subsentenceNoChanged()
	 * @param word
	 */
	private void addLast(Word word){
		Word theWord = new Word(word);
		if (last == null){
			first=last=theWord;
		}else{
			last.setNext(theWord);
			last=theWord;
		}
		size++;
	}
	
	public void addLast(String str){
		Word word = new Word(this.getOffset(),str,last,null);
		if (last == null){
			first = last = word;
		}else {
			last.setNext(word);
			last = word;
		}
		size++;
		offset += word.getText().length();
	}

	public void addLast(String tag,String str){
		Word word = new Word(this.getOffset(),tag,str,last,null);
		if (last == null){
			first = last = word;
		}else {
			last.setNext(word);
			last = word;
		}
		size++;
		offset += word.getText().length();
	}
	
	public void addFirst(String str){
		int len = str.length();
		for (int i=0,n=getSize(); i<n; i++){
			Word temp = get(i);
			temp.setIndex(temp.getIndex() + len);
		}
		
		Word word = new Word(0,str,null,first);
		if (first == null){
			first = last = word;
		}else {
			first.setPrev(word);
			first = word;
		}
		size++;
	}
	
	public void remove(Word word){
		int index = this.contain(word);
		int len = word.getText().length();
		if (index != -1){
			for (int i=index+1,n=getSize(); i<n; i++){
				Word temp = this.get(i);
//				System.out.println(temp);
				temp.setIndex(temp.getIndex()-len);
			}
		}
		
		if (word.getPrev() == null){
			first = word.getNext();
		}else {
			word.getPrev().setNext(word.getNext());
		}
		if (word.getNext() == null){
			last = word.getPrev();
		}else{
			word.getNext().setPrev(word.getPrev());
		}
		size--;
	}
	
	public Word get(int index){
		if (index < 0 || index >= this.getSize()){
			return null;
		}else {
			Word word = first;
			for (int i=0; i<index; i++){
				word = word.getNext();
			}
			return word;
		}
	}
	
	public Word getByOffset(int offsetnumber){
		if (offsetnumber<0 || offsetnumber>=this.offset){
			return null;
		}
		else {
			Word word = first;
			int left = first.getIndex();
			int right = left + first.getText().length() - 1;
			if (offsetnumber>=left && offsetnumber<=right)
				return first;
			for (int i=1,n=getSize(); i<n; i++){
				word = word.getNext();
				left = word.getIndex();
				right = left + word.getText().length() - 1;
				if (offsetnumber>=left && offsetnumber<=right)
					return word;
			}
		}
		return null;
	}
	
	public int getOffset(Word word){
		int offset = 0;
		if (contain(word) == -1){
			offset = -1;
		}
		else{
			Iterator itr = this.iterator();
			while (itr.hasNext()){
				Word temp = (Word)itr.next();
				if (word.equals(temp)){
					break;
				}
				else {
					offset += (temp.getText()).length();
				}
			}
		}
		return offset;
	}
	
	public String toString(){
		StringBuffer sb = new StringBuffer();
		WordsIterator it = iterator();
		while (it.hasNext()) {
			sb.append(it.next());
			if (it.hasNext()) {
				sb.append(" ");
			}
		}
		return sb.toString();
	}
	
	public String toStringbyME(){
		StringBuffer sb = new StringBuffer();
		WordsIterator it = iterator();
		while (it.hasNext()) {
			Word theWord = (Word)it.next();
			if (theWord.equals(last)){
				sb.append(theWord);
				return sb.toString();
			}
			else{
				sb.append(theWord + " ");
			}
		}
		return sb.toString();
	}
	
	/**
	public String toStringDetail(){
		Word word = new Word();
		int index = 0;
		StringBuffer sb = new StringBuffer();
		WordsIterator it = iterator();
		while (it.hasNext()) {
			word = (Word)it.next();
			index = word.getIndex();
			sb.append(word.getWord() + "(" + index + ")");
			if (it.hasNext()) {
				sb.append(" ");
			}
		}
		return sb.toString();
	}
	*/
	
	public int hashCode(){
		int code = this.getSize();
		Iterator itr = this.iterator();
		while (itr.hasNext()){
			code += itr.next().hashCode();
		}
		return code;
	}
	
	public boolean equals(Object sentence){
		if (! (sentence instanceof Sentence)){
			return false;
		}
		if (((Sentence)sentence).getSize()!= this.getSize())
			return false;
		
		Iterator itr1 = this.iterator();
		Iterator itr2 = ((Sentence)sentence).iterator();
		while (itr1.hasNext()){
			if (!itr1.next().equals(itr2.next()))
				return false;
		}
		return true;
	}
	/**
	 * 返回相同的word的index
	 * @param word
	 * @return
	 */
	public int contain(Word word){
		Iterator itr = this.iterator();
		int count = -1;
		while (itr.hasNext()){
			count++;
			if (word.equals(itr.next()))
				return count;
		}
		return -1;
	}
	
	/**
	 * 返回第一个具有与参数相同的word的index
	 * @param str
	 * @return
	 */
	public int contain(String str){
		Iterator itr = this.iterator();
		int count = -1;
		while(itr.hasNext()){
			count++;
			Word tempWord = (Word)itr.next();
			String temp = tempWord.getText();
			if (str.equalsIgnoreCase(temp)){
				return count;
			}
		}
		return -1;
	}
	
	public boolean isEmpty(){
		return first == null;
	}
	
	public Word getFirst() {
		return first;
	}
	public void setFirst(Word first) {
		this.first = first;
	}
	public Word getLast() {
		return last;
	}
	public void setLast(Word last) {
		this.last = last;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public int getOffset() {
		return offset;
	}
	
	public List getWordsList(){
		List wordsList = new ArrayList();
		Iterator itr = this.iterator();
		while (itr.hasNext()){
			Word theWord = (Word)itr.next();
			wordsList.add(theWord.getText());
		}
		return wordsList;
	}
	
	public List getTagsList(){
		List tagsList = new ArrayList();
		Iterator itr = this.iterator();
		while (itr.hasNext()){
			Word theWord = (Word)itr.next();
			tagsList.add(theWord.getTag());
		}
		return tagsList;
	}
	
}
