/*
 * Created on 2004-4-13
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package hmm;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */

public class Temporary {
//	private String tag;
    private int tagIndex;
	private double prob;
//    private TagSet tagSet = new TagSet();
	
    Temporary(int index, double prob){
        this.tagIndex = index;
        this.prob = prob;
    }
    
//	public void setTag(String strTag)
//	{
//		tag=strTag;
//	}
//
//	public void setProb(double i)
//	{
//		prob=i;
//	}

	public int getTagIndex()
	{
		return tagIndex;
	}

	public double getProb()
	{
		return prob;
	}

}
