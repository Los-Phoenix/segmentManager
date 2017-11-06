package chineseUnit;

public class Word {
	private String word;
	private int index;
	private String tag;
	private Word prev;
	private Word next;
	
	public Word(int index,String tag,String word, Word prev, Word next) {
		this.index = index;
		this.tag = tag;
		this.word = word;
		this.prev = prev;
		this.next = next;
	}
	
	public Word(int index,String word,Word prev,Word next){
		this(index,"",word,prev,next);
	}
	
	public Word(String tag,String word){
		this(-1,tag,word,null,null);
	}
	
	public Word(int index,String tag,String word) {
		this(index,tag,word,null,null);
	}
	
	public Word(String word){
		this(-1,"",word,null,null);
	}
	
	public Word(Word word){
		this(word.index,word.tag,word.word,word.prev,word.next);
	}
	
	public Word(){
		this(-1,"","",null,null);
	}
	
	public boolean equals(Object word){
		if (word instanceof Word 
		   && this.word.equals(((Word)word).getText())
		   && this.index == ((Word)word).getIndex())
			return true;
		else return false;
	}
	
	
	public int hashCode(){
		int code=0;
		if (this.prev != null)
			code += this.prev.hashCode();
		if (this.next != null)
			code += this.next.hashCode();
		return code + word.hashCode();
	}
	
	public String toString(){
		return word + "[" + tag + "," + index + "]";
	}
	
	public String getText() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}

	public Word getNext() {
		return next;
	}

	public void setNext(Word nextword) {
//		System.out.println(nextword);
		this.next = nextword;
//		toString();
	}

	public Word getPrev() {
		return prev;
	}

	public void setPrev(Word prev) {
		this.prev = prev;
	}
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}
	
}

    