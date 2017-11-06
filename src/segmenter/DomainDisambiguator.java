package segmenter;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import chineseUnit.Sentence;
import chineseUnit.Word;
import dictionary.DictionaryFactory;
import dictionary.DomainDictionary;
import segmenter.Disambiguator;


public class DomainDisambiguator extends Disambiguator{
	
	DomainDictionary domainDic = DictionaryFactory.getDomainDictionary();	
	
	public List preference(List lstemp, List bstemp){
		Boolean lsHasODEntity = false;
//		Boolean lsHasEntity = false;
		int lsMaxLen = -1;
		Boolean bsHasODEntity = false;
//		Boolean bsHasEntity = false;
		int bsMaxLen = -1;
		Iterator lsItr = lstemp.iterator();
		Iterator bsItr = bstemp.iterator();
		while (lsItr.hasNext()){
			String str = (String)lsItr.next();
//			String tag = ((Word)lsItr.next()).getTag();
			if (domainDic.isWord(str)){
				lsHasODEntity = true;
				int _len = str.length();
				if (_len > lsMaxLen)
					lsMaxLen = _len;
			}
		}
		while (bsItr.hasNext()){
			String str = (String)bsItr.next();
//			String tag = ((Word)bsItr.next()).getTag();
			if (domainDic.isWord(str)){
				bsHasODEntity = true;
				int _len = str.length();
				if (_len > bsMaxLen)
					bsMaxLen = _len;
			}
		}
		
		if (lsHasODEntity && !bsHasODEntity)
			return lstemp;
		else if (!lsHasODEntity && bsHasODEntity)
			return bstemp;
		else if (lsHasODEntity && bsHasODEntity && (lsMaxLen > bsMaxLen))
			return lstemp;
		else if (lsHasODEntity && bsHasODEntity && (lsMaxLen < bsMaxLen))
			return bstemp;
		else return bstemp;
	
	}
	/**
	 * 以实体为依据，选取包含有实体的分词序列
	 * ,比如“大望路上”，则选取“大望路/上”而非“大/望/路上”
	 */
	public Sentence preference(Sentence lstemp, Sentence bstemp){
		Boolean lsHasODEntity = false;
		Boolean lsHasEntity = false;
		Boolean bsHasODEntity = false;
		Boolean bsHasEntity = false;
		Iterator lsItr = lstemp.iterator();
		Iterator bsItr = bstemp.iterator();
		while (lsItr.hasNext()){
			String str = ((Word)lsItr.next()).getText();
			String tag = ((Word)lsItr.next()).getTag();
			if (domainDic.isWord(str)){
				lsHasODEntity = true;
			}else if (tag.equalsIgnoreCase("nr")||tag.equalsIgnoreCase("ns")||tag.equalsIgnoreCase("nt")){
				lsHasEntity = true;
			}
		}
		while (bsItr.hasNext()){
			String str = ((Word)bsItr.next()).getText();
			String tag = ((Word)bsItr.next()).getTag();
			if (domainDic.isWord(str)){
				bsHasODEntity = true;
			}else if (tag.equalsIgnoreCase("nr")||tag.equalsIgnoreCase("ns")||tag.equalsIgnoreCase("nt")){
				bsHasEntity = true;
			}
		}
		
		if (lsHasODEntity && !bsHasODEntity)
			return lstemp;
		else if (!lsHasODEntity && bsHasODEntity)
			return bstemp;
		else if (!lsHasODEntity && !bsHasODEntity && lsHasEntity && !bsHasEntity)
			return lstemp;
		else if (!lsHasODEntity && !bsHasODEntity && bsHasEntity && !lsHasEntity)
			return bstemp;
		else return bstemp;
	}
	
	public void addUserDic(String dicPath) throws IOException {
		this.domainDic.addUserDict(dicPath);
	}	
}
