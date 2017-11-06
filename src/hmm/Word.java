/*
 * Created on 2004-5-12
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package hmm;

import java.util.Set;

//import java.util.HashSet;
/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class Word {
	private String word;
	private int index;
    private Set wordTags;
    private Set entityTags;
    
    Word(int index,String word){
        this(index,word,null,null);
    }
	

    Word(int index,String word, Set wordTags, Set entityTags){
        this.index = index;
        this.word = word;
        this.wordTags = wordTags;
        this.entityTags = entityTags;
    }

	public int getIndex(){
		return index;
	}
    public String getWord(){
		return word;
	}
    public Set getWordTags(){
        return wordTags;
    }
    public Set getEntityTags(){
        return entityTags;
    }
}
