package hmm;

import java.util.HashSet;
import java.util.Set;

  public class WordInfo {
      	public final static String STRING_SONGTAG = "song";
      	public final static String STRING_SINGERTAG = "singer";
      	public final static String STRING_RESTAURANT = "restaurant";
      	public final static String STRING_FOOD = "food";
      	public final static String STRING_BRAND = "brand";
      	public final static String STRING_LOCATION = "location";
      	public final static String STRING_PRODUCT = "product";
      	public final static String STRING_PUBLISH = "publish";
      	public final static String STRING_ORGANIZATION = "organization";
      	public final static String STRING_ACTOR = "actor";
      	public final static String STRING_ESTATE = "estate";
      	public final static String STRING_ALBUM = "album";
      	public final static String STRING_PERSON = "person";
      	public final static String STRING_TVPROGRAM = "TV_program";
      	public final static String STRING_PET = "pet";
      	public final static String STRING_MOVIE = "movie";
      	
      	public final static String STRING_PUNCTUATION = "w"; 
      	
        private String word = "";
        private Set wordTags = new HashSet();
        private Set entityTags = new HashSet();
        
        WordInfo(String st, Set al){
            this.word = st;
            if (al != null)
                this.wordTags = al;
            else wordTags = null;
        }
        
        WordInfo(String st, Set wordTag, Set entityTag){
            this.word = st;
            if (wordTag != null)
                this.wordTags = wordTag;     
            else this.wordTags = null;
            if (entityTag != null)
                this.entityTags = entityTag;         
            else this.entityTags = null;
        }
        
         boolean hsVerb(){
             if (wordTags != null)
                 return wordTags.contains("v")||wordTags.contains("Vg")
                       ||wordTags.contains("vd")||wordTags.contains("vn");
             else return false;
        }
        
         boolean hsQAword (){
             if (wordTags != null)
                return wordTags.contains("r");
             else return false;
            //注意,在wordDictionary中必须加入疑问词的特定标注才能实现此功能,否则有误
        }
         
         boolean hsSong(){
             return entityTags.contains(STRING_SONGTAG);
         }
         
         boolean hsSinger(){
             return entityTags.contains(STRING_SINGERTAG);
         }
         
         boolean hsPersonName(){
             if (wordTags != null)
               return wordTags.contains("nr");
             else return false;
         }
         
         boolean hsPunctuation(){
             if (wordTags != null)
               return wordTags.contains(STRING_PUNCTUATION);
             else return false;
         }
         
         void setEntityTag(String st){
             
         }
         
         int getTagsLength(){return wordTags.size();}
         
         public String getWord(){return word;}
         
         public Set getWordTagList(){return wordTags;}
         
         public Set getEntityTagList(){return entityTags;}
         
         public String toString (){
             String result = word;
             if (wordTags != null)
                 result += wordTags.toString();
             if (entityTags != null)
                 result += entityTags.toString();
             return result;
         }
    }

    