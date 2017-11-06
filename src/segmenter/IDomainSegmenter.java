package segmenter;

import java.io.IOException;
import java.util.List;

public interface IDomainSegmenter {
	public List segment(String input);
	public String segmentNextWord(String strInput, int nWordStartIndex);
	public void addUserDic(String dictPath) throws IOException;
}
