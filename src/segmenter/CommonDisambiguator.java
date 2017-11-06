package segmenter;

import java.util.List;

import chineseUnit.Sentence;


public class CommonDisambiguator extends Disambiguator{
	public List preference(List lstemp, List bstemp){
		return bstemp;
	}
	public Sentence preference(Sentence lstemp, Sentence bstemp){
		return bstemp;
	}
}
