package segmenter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import chineseUnit.Sentence;
import chineseUnit.Word;


public abstract class Disambiguator {

	private Set<List<String>> crossSet;

	private Set<List<String>> combinSet;


	public Sentence disambiguate(Sentence lsen, Sentence bsen){
		crossSet = new LinkedHashSet();
		combinSet = new LinkedHashSet();
		Sentence result = new Sentence();
		Sentence ls_temp = new Sentence();
		Sentence bs_temp = new Sentence();
		Iterator lsItr = lsen.iterator();
		Iterator bsItr = bsen.iterator();
		Word ls_word = null;
		Word bs_word = null;
		String ls_str = null;
		String bs_str = null;
		int ls_len = -1; int bs_len = -1;
		int ls_count = -1; int bs_count = -1;
		while (true){
			if (lsItr.hasNext() && bsItr.hasNext()){
				ls_word = (Word)lsItr.next();
				ls_str = ls_word.getText();
				bs_word = (Word)bsItr.next();
				bs_str = bs_word.getText();
				ls_len = ls_str.length();
				bs_len = bs_str.length();
				ls_temp.addLast(ls_word.getTag(), ls_str);
				bs_temp.addLast(bs_word.getTag(), bs_str);
				ls_count = bs_count = 0;
			}else break;
			if (ls_str.equalsIgnoreCase(bs_str)){
				result.addLast(ls_word.getTag(), ls_str);
				ls_temp = new Sentence();
				bs_temp = new Sentence();
			}
			if ((ls_len > bs_len) && bsItr.hasNext()){
//				isSame = false;
				Word temp = (Word)bsItr.next();
				bs_temp.addLast(temp.getTag(), temp.getText());
				bs_len += temp.getText().length();
				bs_count++;
				bs_str += temp.getText();

				if (ls_str.equals(bs_str)){
					if (ls_count == bs_count || (ls_count != bs_count && ls_count != 0)){
						crossSet.add(ls_temp.getWordsList());
						crossSet.add(bs_temp.getWordsList());
					}
					else if (ls_count != bs_count && ls_count == 0){
						combinSet.add(ls_temp.getWordsList());
					}
					Sentence _sen = preference(ls_temp,bs_temp);
					Iterator _itr = _sen.iterator();
					while (_itr.hasNext()){
						Word _word = (Word)_itr.next();
						result.addLast(_word.getTag(), _word.getText());
					}
					ls_temp = new Sentence();
					bs_temp = new Sentence();
				}
			}

			if ((bs_len > ls_len) && lsItr.hasNext()){
//				isSame = false;
				Word temp = (Word)lsItr.next();
				ls_temp.addLast(temp.getTag(),temp.getText());
				ls_len += temp.getText().length();
				ls_count++;
				ls_str += temp.getText();
				if (bs_str.equals(ls_str)){
					if (ls_count == bs_count || (ls_count != bs_count && bs_count != 0)){
						crossSet.add(ls_temp.getWordsList());
						crossSet.add(bs_temp.getWordsList());
					}
					else if (ls_count != bs_count && bs_count == 0){
						combinSet.add(ls_temp.getWordsList());
					}
					Sentence _sen = preference(ls_temp,bs_temp);
					Iterator _itr = _sen.iterator();
					while (_itr.hasNext()){
						Word _word = (Word)_itr.next();
						result.addLast(_word.getTag(), _word.getText());
					}
					ls_temp = new Sentence();
					bs_temp = new Sentence();
				}
			}
		}
		return result;
	}
	
	public List disambiguate(List ls, List bs){
		crossSet = new LinkedHashSet();
		combinSet = new LinkedHashSet();
		List result = new ArrayList();
		List ls_temp = new ArrayList();
		List bs_temp = new ArrayList();
		Iterator lsItr = ls.iterator();
		Iterator bsItr = bs.iterator();
		String ls_str = null;
		String bs_str = null;
		int ls_len = -1; int bs_len = -1;
		int ls_count = -1; int bs_count = -1;
		while (true){
			if (lsItr.hasNext() && bsItr.hasNext() && (ls_len == bs_len)){
				ls_str = (String)lsItr.next();
				bs_str = (String)bsItr.next();
				ls_len = ls_str.length();
				bs_len = bs_str.length();
				ls_temp.add(ls_str);
				bs_temp.add(bs_str);
				ls_count = bs_count = 0;
			}
			else if (!lsItr.hasNext() || !bsItr.hasNext()) break;
			
			if (ls_str.equals(bs_str)){
				result.add(ls_str);
				ls_temp = new ArrayList();
				bs_temp = new ArrayList();
			}

			if ((ls_len > bs_len) && bsItr.hasNext()){
//				isSame = false;
				String temp = (String)bsItr.next();
				bs_temp.add(temp);
				bs_len += temp.length();
				bs_count++;
				bs_str += temp;

				if (ls_str.equals(bs_str)){
					if (ls_count == bs_count || (ls_count != bs_count && ls_count != 0)){
						result.addAll(preference(ls_temp,bs_temp));
						crossSet.add(ls_temp);
						crossSet.add(bs_temp);
					}
					else if (ls_count != bs_count && ls_count == 0){
						result.addAll(preference(ls_temp,bs_temp));
						combinSet.add(bs_temp);
					}
					ls_temp = new ArrayList();
					bs_temp = new ArrayList();
				}
			}

			if ((bs_len > ls_len) && lsItr.hasNext()){
				String temp = (String)lsItr.next();
				ls_temp.add(temp);
				ls_len += temp.length();
				ls_count++;
				ls_str += temp;
				if (bs_str.equals(ls_str)){
					if (ls_count == bs_count || (ls_count != bs_count && bs_count != 0)){
						result.addAll(preference(ls_temp,bs_temp));
						crossSet.add(ls_temp);
						crossSet.add(bs_temp);
					}
					else if (ls_count != bs_count && bs_count == 0){
						result.addAll(preference(ls_temp,bs_temp));
						combinSet.add(ls_temp);
					}
					ls_temp = new ArrayList();
					bs_temp = new ArrayList();
				}
			}
		}
		
		//目前采用最简单的形式，上面的代码仅用于找出歧义
		if (ls.size() == bs.size())
			return bs;
		else if (ls.size() > bs.size())
			return bs;
		else return ls;
//		return result;
	}
	
	public List<List<String>> allPossibleSentences(List ls, List bs){
		List<Sentence> result = new ArrayList();
		result.add(new Sentence());
		Sentence lsen = new Sentence(ls);
		Sentence bsen = new Sentence(bs);
		Sentence ls_temp = new Sentence();
		Sentence bs_temp = new Sentence();
		Iterator lsItr = lsen.iterator();
		Iterator bsItr = bsen.iterator();
		Word ls_word = null;
		Word bs_word = null;
		String ls_str = "";
		String bs_str = "";
		int ls_len = -1; int bs_len = -1;
		int ls_count = -1; int bs_count = -1;
		while (true){
			if (lsItr.hasNext() && bsItr.hasNext() && ls_str.length() == bs_str.length()){
				ls_word = (Word)lsItr.next();
				ls_str = ls_word.getText();
				bs_word = (Word)bsItr.next();
				bs_str = bs_word.getText();
				ls_len = ls_str.length();
				bs_len = bs_str.length();
				ls_temp.addLast(ls_word.getTag(), ls_str);
				bs_temp.addLast(bs_word.getTag(), bs_str);
				ls_count = bs_count = 0;
			}else if (!lsItr.hasNext() || !bsItr.hasNext()) 
				break;
			if (ls_str.equalsIgnoreCase(bs_str)){
				for (Sentence s : result){
					s.addLast(ls_str);
				}
				ls_temp = new Sentence();
				bs_temp = new Sentence();
			}
			if ((ls_len > bs_len) && bsItr.hasNext()){
//				isSame = false;
				Word temp = (Word)bsItr.next();
				bs_temp.addLast(temp.getTag(), temp.getText());
				bs_len += temp.getText().length();
				bs_count++;
				bs_str += temp.getText();

				if (ls_str.equals(bs_str)){
					Set<Sentence> _result = new HashSet(result.size());
					for (Sentence s : result){
						_result.add(new Sentence(s));
					}
					for (Sentence s : _result){
						Iterator itr = ls_temp.iterator();
						while (itr.hasNext()){
							Word _word = (Word)itr.next();
							s.addLast(_word.getText());
						}
					}
					for (Sentence s : result){
						Iterator itr = bs_temp.iterator();
						while (itr.hasNext()){
							Word _word = (Word)itr.next();
							s.addLast(_word.getText());
						}
					}
					result.addAll(_result);
					ls_temp = new Sentence();
					bs_temp = new Sentence();
				}
			}

			if ((bs_len > ls_len) && lsItr.hasNext()){
//				isSame = false;
				Word temp = (Word)lsItr.next();
				ls_temp.addLast(temp.getTag(),temp.getText());
				ls_len += temp.getText().length();
				ls_count++;
				ls_str += temp.getText();
				if (bs_str.equals(ls_str)){
					List<Sentence> _result = new ArrayList(result.size());
					for (Sentence s : result){
						_result.add(new Sentence(s));
					}
					for (Sentence s : _result){
						Iterator itr = ls_temp.iterator();
						while (itr.hasNext()){
							Word _word = (Word)itr.next();
							s.addLast(_word.getText());
						}
					}
					for (Sentence s : result){
						Iterator itr = bs_temp.iterator();
						while (itr.hasNext()){
							Word _word = (Word)itr.next();
							s.addLast(_word.getText());
						}
					}
					result.addAll(_result);
					ls_temp = new Sentence();
					bs_temp = new Sentence();
				}
			}
		}
		List<List<String>> _result = new ArrayList();
		for (Sentence s : result){
			_result.add(s.getWordsList());
		}
		return _result;
	}
	
	

	public abstract List preference(List lstemp, List bstemp);
	
	public abstract Sentence preference(Sentence lstemp, Sentence bstemp);
	
	public Set<List<String>> getCombinSet() {
		return combinSet;
	}

	public Set<List<String>> getCrossSet() {
		return crossSet;
	}
	
	public void addUserDic(String dicPath) throws IOException {

	}
	
}
