package dictionary;

public class testDomainDictionary {
	
	public static void main(String[] orgs){
		String line = " дь";
		String[] _word_tags = line.split(" ");
		for (String s : _word_tags){
			System.out.println(s);
		}
	}
}
