package hmm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import chineseUnit.Sentence;

import segmenter.SegmenterManager;

public class testHmm {
	public static void main(String[] orgs){
		Hmm hmm = new Hmm();
		SegmenterManager sm = new SegmenterManager();
		BufferedReader input = new BufferedReader(new InputStreamReader(
				System.in));
		String req = null;
		System.out.println("«Î ‰»Î”Ôæ‰£∫");
		try {
			while (!(req = input.readLine()).equalsIgnoreCase("exit") ) {
				System.out.println("input: " + req);
				long start = System.currentTimeMillis();
				List< String> words = sm.segment(req);
				Sentence result = hmm.tag(words);
				long end = System.currentTimeMillis();
				System.out.println("Response: (in " + (end - start) + " ms)");
				System.out.println("tagger result------------------\n" + result.toString());
				System.out.println("«Î ‰»Î≤È—Ø”Ôæ‰( ‰»ÎexitÕÀ≥ˆ)£∫");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
