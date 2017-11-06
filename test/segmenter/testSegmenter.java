package segmenter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader; 
import java.util.List;
import java.util.Set;

import dictionary.DictionaryFactory;

import segmenter.Segmenter;

public class testSegmenter {
	public static void testLM(){
		Segmenter ls_common_segmenter = DictionaryFactory.getLMSegmenter();
		LMDomainSegmenter ls_domain_segmenter = new LMDomainSegmenter();
		SegmenterManager sm = new SegmenterManager();
		BufferedReader input = new BufferedReader(new InputStreamReader(
				System.in));
		String req = null;
		System.out.println("«Î ‰»Î”Ôæ‰£∫");
		try {
			while (!(req = input.readLine()).equalsIgnoreCase("exit") ) {
				System.out.println("input: " + req);
				long start = System.currentTimeMillis();
				List result1 = ls_common_segmenter.segment(req);
				List result2 = ls_domain_segmenter.segment(req);
				List result3 = sm.lmSegmenter(req);
				
				long end = System.currentTimeMillis();
				System.out.println("Response: (in " + (end - start) + " ms)");
				System.out.println("ls_common_egmenter result------------------\n" + result1);
				System.out.println("ls_domain_segmenter result------------------\n" + result2);
				System.out.println("ls_segmenter result------------------\n" + result3);
				System.out.println("«Î ‰»Î≤È—Ø”Ôæ‰( ‰»ÎexitÕÀ≥ˆ)£∫");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void testBM(){
		Segmenter bs_common_segmenter = DictionaryFactory.getBMSegmenter();
		BMDomainSegmenter bs_domain_segmenter = new BMDomainSegmenter();
		SegmenterManager sm = new SegmenterManager();
		BufferedReader input = new BufferedReader(new InputStreamReader(
				System.in));
		String req = null;
		System.out.println("«Î ‰»Î”Ôæ‰£∫");
		try {
			while (!(req = input.readLine()).equalsIgnoreCase("exit") ) {
				System.out.println("input: " + req);
				long start = System.currentTimeMillis();
				List result1 = bs_common_segmenter.segment(req);
				List result2 = bs_domain_segmenter.segment(req);
				List result3 = sm.bmSegmenter(req);
				
				long end = System.currentTimeMillis();
				System.out.println("Response: (in " + (end - start) + " ms)");
				System.out.println("bs_common_egmenter result------------------\n" + result1);
				System.out.println("bs_domain_segmenter result------------------\n" + result2);
				System.out.println("bs_segmenter result------------------\n" + result3);
				System.out.println("«Î ‰»Î≤È—Ø”Ôæ‰( ‰»ÎexitÕÀ≥ˆ)£∫");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void testFile(String file){
		SegmenterManager sm = new SegmenterManager();
		try {
			BufferedReader input = new BufferedReader(new InputStreamReader(
					new FileInputStream(file),"UTF-16"));
			String req=input.readLine();
			long time=System.currentTimeMillis();
			while (req!=null){
			//System.out.println(req);
			sm.lmSegmenter(req);
			//	System.out.println("ls result------------------\n" + result1);
			req=input.readLine();
			}
			time=System.currentTimeMillis()-time;
			System.out.println(time);
			input.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void testSM(){
		SegmenterManager sm = new SegmenterManager();
		BufferedReader input = new BufferedReader(new InputStreamReader(
				System.in));
		String req = null;
		System.out.println("«Î ‰»Î”Ôæ‰£∫");
		try {
			while (!(req = input.readLine()).equalsIgnoreCase("exit") ) {
				System.out.println("input: " + req);
				long start = System.currentTimeMillis();
				List result1 = sm.lmSegmenter(req);
				List result2 = sm.bmSegmenter(req);
				List result3 = sm.segment(req);
				List<List<String>> result4 = sm.graphSegmenter(req);
				long end = System.currentTimeMillis();
				System.out.println("Response: (in " + (end - start) + " ms)");
				System.out.println("ls result------------------\n" + result1);
				System.out.println("bs result------------------\n" + result2);
				System.out.println("bd result------------------\n" + result3);
				System.out.println("cross ambiguation-----------\n" + sm.getCrossDisambiguation(result1, result2));
				System.out.println("combin ambiguation-----------\n" + sm.getCombinDisambiguation(result1, result2));
				System.out.println("graph result-----------------\n");
				for (List<String> l : result4){
					System.out.println(l);
				}
				System.out.println("«Î ‰»Î≤È—Ø”Ôæ‰( ‰»ÎexitÕÀ≥ˆ)£∫");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] orgs){
	    System.out.println();
		testSegmenter.testFile("E:\\1.txt");
	}
}
