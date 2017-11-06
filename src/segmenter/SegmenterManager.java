package segmenter;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;



import dictionary.ParameterConfig;
import dictionary.DictionaryFactory;


public class SegmenterManager {
	
	private static final Logger log = Logger.getLogger(SegmenterManager.class); 
	
	private Boolean hasCommonSeg = null;
	
	private Boolean hasDomainSeg = null;
	
	private Segmenter ls_common_segmenter = null;
	
	private Segmenter bs_common_segmenter = null;
	
	private IDomainSegmenter ls_domain_segmenter = null;
	
	private IDomainSegmenter bs_domain_segmenter = null;
	
	private Disambiguator domainDisambiguator = null;
	
	private Disambiguator commonDisambiguator = null;
	
	public SegmenterManager(){
		hasDomainSeg = Boolean.parseBoolean(ParameterConfig.getString("domain_segmenter"));
		hasCommonSeg = Boolean.parseBoolean(ParameterConfig.getString("common_segmenter"));
		if (hasCommonSeg){
			ls_common_segmenter = DictionaryFactory.getLMSegmenter();
			bs_common_segmenter = DictionaryFactory.getBMSegmenter();
			commonDisambiguator = new CommonDisambiguator();
		}
		if (hasDomainSeg){
			ls_domain_segmenter = new LMDomainSegmenter();
			bs_domain_segmenter = new BMDomainSegmenter();
			domainDisambiguator = new DomainDisambiguator();
		}
	}
	
	public SegmenterManager(boolean hasCommonSeg, boolean hasDomainSeg){
		this.hasCommonSeg = hasCommonSeg;
		this.hasDomainSeg = hasDomainSeg;
		if (hasCommonSeg){
			ls_common_segmenter = DictionaryFactory.getLMSegmenter();
			bs_common_segmenter = DictionaryFactory.getBMSegmenter();
			commonDisambiguator = new CommonDisambiguator();
		}
		if (hasDomainSeg){
			ls_domain_segmenter = new LMDomainSegmenter();
			bs_domain_segmenter = new BMDomainSegmenter();
			domainDisambiguator = new DomainDisambiguator();
		}
	}
	
	public void addUserDict(String dictPath) throws IOException {
		if (hasDomainSeg){
			ls_domain_segmenter.addUserDic(dictPath);
			bs_domain_segmenter.addUserDic(dictPath);
			domainDisambiguator.addUserDic(dictPath);
		}
	}
	
	public List< String > segment(String strInput){
		List result = new ArrayList();
		if (hasCommonSeg && hasDomainSeg){
			List<String> lsresult = lsegByMaxLen(ls_common_segmenter, ls_domain_segmenter, strInput);
			List<String> bsresult = bsegByMaxLen(bs_common_segmenter, bs_domain_segmenter, strInput);
			result = domainDisambiguator.disambiguate(lsresult, bsresult);
			log.debug("the cross ambiguation have: " + domainDisambiguator.getCrossSet());
			log.debug("the combine ambiguation have: " + domainDisambiguator.getCombinSet());
		}
		else if (hasCommonSeg && !hasDomainSeg){
			List<String> lsresult = ls_common_segmenter.segment(strInput);
			List<String> bsresult = bs_common_segmenter.segment(strInput);
			result = commonDisambiguator.disambiguate(lsresult, bsresult);
			log.debug("the cross ambiguation have: " + commonDisambiguator.getCrossSet());
			log.debug("the combine ambiguation have: " + commonDisambiguator.getCombinSet());
		}
		else if (!hasCommonSeg && hasDomainSeg){
			List<String> lsresult = ls_domain_segmenter.segment(strInput);
			List<String> bsresult = bs_domain_segmenter.segment(strInput);
			result = domainDisambiguator.disambiguate(lsresult, bsresult);
			log.debug("the cross ambiguation have: " + domainDisambiguator.getCrossSet());
			log.debug("the combine ambiguation have: " + domainDisambiguator.getCombinSet());
		}
		return result;
	}
	
	private List<String> lsegByMaxLen(Segmenter commonSeg, IDomainSegmenter domainSeg, String strInput){
		List<String> result = new ArrayList<String>();
		int len = strInput.length();
		int step = -1;
		for (int i = 0; i < len; i += step){
			String commonStr = commonSeg.segmentNextWord(strInput, i);
			String domainStr = domainSeg.segmentNextWord(strInput, i);
			int comLen = commonStr.length();
			int domLen = domainStr.length();
			if (comLen == domLen){
				result.add(commonStr);
				step = comLen;
			}
			else if (comLen > domLen){
				result.add(commonStr);
				step = comLen;
			}
			else if (comLen < domLen){
				result.add(domainStr);
				step = domLen;
			}
		}
		return result;
	}
	
	private List<String> bsegByMaxLen(Segmenter commonSeg, IDomainSegmenter domainSeg, String strInput){
		List<String> result = new ArrayList();
		int len = strInput.length();
		int step = -1;
		for (int i = len -1 ; i >= 0; i -= step){
			String commonStr = commonSeg.segmentNextWord(strInput, i);
			String domainStr = domainSeg.segmentNextWord(strInput, i);
			int comLen = commonStr.length();
			int domLen = domainStr.length();
			if (comLen == domLen){
				result.add(commonStr);
				step = comLen;
			}
			else if (comLen > domLen){
				result.add(commonStr);
				step = comLen;
			}
			else if (comLen < domLen){
				result.add(domainStr);
				step = domLen;
			}
		}
		Collections.reverse(result);
		return result;
	}
	
	public List< String > lmSegmenter(String strInput){
		return lsegByMaxLen(this.ls_common_segmenter,this.ls_domain_segmenter,strInput);
	}
	
	public List< String > bmSegmenter(String strInput){
		return bsegByMaxLen(this.bs_common_segmenter, this.bs_domain_segmenter, strInput);
	}
	
	public Set<List<String>> getCrossDisambiguation(List lsresult, List bsresult){
		commonDisambiguator.disambiguate(lsresult, bsresult);
		return commonDisambiguator.getCrossSet();
	}
	
	public Set<List<String>> getCombinDisambiguation(List lsresult, List bsresult){
		commonDisambiguator.disambiguate(lsresult, bsresult);
		return commonDisambiguator.getCombinSet();
	}
	
	public List< List<String> > graphSegmenter(String strInput){
		List< List<String> > result = new ArrayList();
		List<String> lsresult = new ArrayList();
		List<String> bsresult = new ArrayList();
		if (hasCommonSeg && hasDomainSeg){
			lsresult = lsegByMaxLen(ls_common_segmenter, ls_domain_segmenter, strInput);
			bsresult = bsegByMaxLen(bs_common_segmenter, bs_domain_segmenter, strInput);
			result = domainDisambiguator.allPossibleSentences(lsresult, bsresult);
			log.debug("the cross ambiguation have: " + domainDisambiguator.getCrossSet());
			log.debug("the combine ambiguation have: " + domainDisambiguator.getCombinSet());
		}
		else if (hasCommonSeg && !hasDomainSeg){
			lsresult = ls_common_segmenter.segment(strInput);
			bsresult = bs_common_segmenter.segment(strInput);
			result = commonDisambiguator.allPossibleSentences(lsresult, bsresult);
			log.debug("the cross ambiguation have: " + commonDisambiguator.getCrossSet());
			log.debug("the combine ambiguation have: " + commonDisambiguator.getCombinSet());
		}
		else if (!hasCommonSeg && hasDomainSeg){
			lsresult = ls_domain_segmenter.segment(strInput);
			bsresult = bs_domain_segmenter.segment(strInput);
			result = domainDisambiguator.allPossibleSentences(lsresult, bsresult);
			log.debug("the cross ambiguation have: " + domainDisambiguator.getCrossSet());
			log.debug("the combine ambiguation have: " + domainDisambiguator.getCombinSet());
		}
		return result;
	}
}
