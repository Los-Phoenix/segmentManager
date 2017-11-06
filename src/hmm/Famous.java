package hmm;

import java.util.HashSet;
import java.io.*;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import dictionary.ParameterConfig;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class Famous {
	private static Logger log = LogManager.getLogger(Famous.class);
	//private HashSet hsFamousPeople = new HashSet();
	//private HashSet hsFamousLocations = new HashSet();
	//private HashSet hsFamousOrganizations = new HashSet();
	private HashSet hsTimeSuffix		  = new HashSet();
	private HashSet hsDateSuffix		  = new HashSet();
	private HashSet hsNumberSuffix		  = new HashSet();
	private HashSet hsDate				  = new HashSet();
	private HashSet hsTime				  = new HashSet();
	private HashSet hsNumber			  = new HashSet();

	public Famous(){
		try {
			load();
		} catch (IOException e) {
			log.error(e.getMessage(),e);
		}
	}
	
	public String decode(String s){
		try{
			return new String(s.getBytes("ISO8859_1"), "GBK");
		}
		catch (UnsupportedEncodingException e){
			log.error(e.getMessage(),e);
		}
		return null;
	}
	
	public void load() throws IOException
	{
		/**
		String per = ParameterConfig.getString("famous_people");
		BufferedReader br1 = new BufferedReader(new InputStreamReader(Famous.class.getResourceAsStream(per)));
		String line = "";
		while ((line = br1.readLine()) != null)
		{
			String peoplename = decode(line);
			
			if (!hsFamousPeople.contains(peoplename)){
				hsFamousPeople.add(peoplename);
			}
		}
		br1.close();
	
		
		String loc = ParameterConfig.getString("famous_location");
		BufferedReader br2 = new BufferedReader(new InputStreamReader(Famous.class.getResourceAsStream(loc)));
		line = "";
			while ((line = br2.readLine())!=null)
			{
				String locationname = decode(line);

				if (!hsFamousLocations.contains(locationname)){
					hsFamousLocations.add(locationname);
//					System.out.println(locationname);
				}
			}
			br2.close();
		
		String org = ParameterConfig.getString("famous_organization");
		BufferedReader br3 = new BufferedReader(new InputStreamReader(Famous.class.getResourceAsStream(org)));
		line = "";
		while ((line = br3.readLine())!=null)
			{
				String organizationname = decode(line);

				if (!hsFamousOrganizations.contains(organizationname)){
					hsFamousOrganizations.add(organizationname);
//					System.out.println(organizationname);
				}
			}
		br3.close();
		*/

		String Date="���� ·������ Ԫ���� ���¶���̧ͷ ��ʳ�� ������ ����� ���ܽ� ���³����ùý� ��Ϧ ������ ��Ԫ�� ��� �زؽ� ����� ������ ����� ���� ���˽� ���� ��С�� ��Ϧ ";
			   Date+="Ԫ�� ���˽� ��Ϧ ���� ���˽� Ԫ���� ��Ů�� ֲ���� ���������� ���˽� ���� ���������� �����Ͷ��� �й������ ���ʻ�ʿ�� ���ʼ�ͥ�� ȫ��ѧ��Ӫ���� ���������� ĸ�׽� ���ʶ�ͯ�� ��ͯ�� ����� ���׽� ��ۻع��� ��һ������ ������ ��Ϧ���˽� ��Ϧ �Ͷ��� ��ʦ�� �������˽� ����� ����� ����ä�˽� ������ ��ʥ�� �й������� ���ʴ�ѧ���� �ж��� ������ ���� ����м����� ���������� ʥ���� ";
			   Date+="��Ԫ Ԫ�� Ԫ˷ Ԫ�� Ԫ�� Ԫ�� ̫�� �캺 ̫ʼ ���� ��Ԫ ʼԪ Ԫ�� Ԫƽ ��ʼ �ؽ� Ԫ�� ��� ��� ��¶ ���� ��Ԫ ���� ���� ���� ��ʼ ��ƽ ��˷ ��� ��ʼ Ԫ�� ��� ��ƽ Ԫ�� Ԫʼ ���� ��ʼ ʼ���� ��� �ػ� ��ʼ ���� ��Ԫ ��ƽ ���� Ԫ�� �º� ��Ԫ Ԫ�� ��ƽ ���� Ԫ�� ���� ���� �ӹ� ���� ���� ���� ���� ���� ���� ���� ���� ��ƽ Ԫ�� ���� ���� ���� ���� ���� ��ƽ ��� ��ƽ ���� ���� ���� ��ƽ ��ƽ ��ƽ ���� �ӿ� ���� �Ƴ� ̫�� ���� ���� ��ʼ ��ƽ ��Ԫ ��¶ ��Ԫ ���� ���� ���� ���� ��ҫ ���� ���� ���� �κ� ���� ̫Ԫ ��� ���� ��� ̫ƽ ���� Ԫ�� ��¶ ���� ���� ��� ���� ��� ̩ʼ ���� ̫�� ̫�� ���� ��ƽ Ԫ�� ���� ���� ̫�� ���� ���� ���� ���� ���� ���� ���� ���� ̫�� ���� ̫�� �̺� �̿� ��Ԫ ���� ��ƽ ¡�� ���� ̫�� �̰� ���� ̫Ԫ ¡�� Ԫ�� ���� Ԫ�� ���� ��ƽ Ԫ�� Т�� ���� ���� ���� ̩ʼ ̩ԥ Ԫ�� ���� ��Ԫ ���� ¡�� ���� ���� ��̩ ��Ԫ ���� ��� ��ͨ ��ͨ �д�ͨ ��ͬ �д�ͬ ̫�� �� ���� ��ʥ ��� ��̩ ̫ƽ ���� ��� �쿵 ��� ̫�� ���� ���� �ǹ� ��ʼ ���� ��� ���� ���� ̩�� ʼ�� �� �Ӻ� ̫�� ̫ƽ��� ��ƽ ��ƽ �˰� �˹� ̫�� ��ƽ �찲 ���� ���� ���� ̫�� ���� ��ʼ ��ƽ �Ӳ� ��ƽ ��� ���� Т�� ��̩ ���� ���� ���� ��̩ ���� ̫�� ���� ���� ��κ ��ƽ Ԫ�� �˺� �䶨 ��ͳ ��� ���� ��� ���� ���� ��� ���� �챣 ���� �ʽ� ̫�� ���� ��ͳ ��ƽ ¡�� �й� ���� ���� ��ҵ ���� ��� ��� ���� ���� ��˷ ��� �ɷ� ���� �̺� ��Ԫ �Ƿ� ��¶ ��¡ ��ҫ ���� ��� ��ʥ ���� ��լ ���� ���� �س� ���� ���� ���� ���� ֤ʥ ����Ƿ� ����ͨ�� �� ʥ�� ���� ���� ���� ���� ���� ��¡ ���� ̫�� �Ӻ� ���� ��Ԫ �챦 ���� ��Ԫ ��Ԫ ��Ӧ ��� ��̩ ���� ���� ��Ԫ ��Ԫ ���� Ԫ�� ���� ���� ̫�� ���� ��� ���� ��ͨ �ɷ� ���� �к� �� �ĵ� ���� ��˳ ���� ���� �⻯ �츴 ���� ��ƽ �ɻ� ���� ���� ͬ�� ��� ���� Ӧ˳ ��̩ �츣 ���� �츣 ���� ��˳ �Ե� ��¡ �ɵ� ���� ̫ƽ�˹� Ӻ�� �˹� ���� ���� ��ƽ ���� ������� Ԫ�� ���� ��ʥ ���� ���� ��Ԫ ���� ���� ���� ���� ���� ��ƽ ���� Ԫ�� Ԫ�� ��ʥ Ԫ�� ���о��� ���� ��� ���� �غ� ���� ���� ���� ���� ¡�� �ɵ� ���� ���� ��Ԫ ��̩ ���� �ζ� ���� �ܶ� ��ƽ ���� ���� ���� ���� ���� �̴� ���� ���� ���� ��� ���� ���� ��ͬ ��ͬ ��» Ӧ�� ���� �ɺ� ͳ�� ��̩ ̫ƽ ���� ���� ���� ��Ӻ ̫�� ̫�� �ٲ� ��ͳ ���� ���� �չ� �츨 ��� ��� ��ͳ ��� ��Ԫ ��¡ �� ���� �а� ̩�� �� ���� ���� ���� �˶� Ԫ�� ���� ���� ���� ��ͳ ��Ԫ Ԫ�� ��� ���� ���� ���� ���� ̩�� �º� ���� ��˳ Ԫͳ ��Ԫ ���� �Ե� ���� ���� �������������� �������� ���Ӵ�ʥ ��ʥ�е� �b�� ���� �ɵ������ʢ���� �� �찲�� ������ƽ ������ ���� ��� Ӻ�� Ԫ�� ���� ��� ���� ���� ��ʢ ���� ���� Ӧ�� �ʽ� �ⶨ �ɶ� ���� ���� ���� ���� ���� ���� ��ͳ ��̩ ��˳ �ɻ� ���� ���� �ξ� ¡�� ���� ̩�� ���� ���� ���� ��� ��� ˳�� ���� Ӻ�� ��¡ ���� ���� �̷� ͬ�� ���� ��ͳ ";
			   Date+="����һ ���ڶ� ������ ������ ������ ������ ������ ������ ��һ �ܶ� ���� ���� ���� ���� ���� ��ĩ ";
			   Date+="���� ��� δ�� Ԫ�� ţ�� ���� ���� ��� ���� ���� ��ҹ ���� ���� �´� ���� ��ǰ �ִ� Ԫ�� ���� ���� ���� ��ҹ ���� ���� ���� ��ȥ ��� ���� ���� ȥ�� ���� ���� ���� ��ĩ ���� ���� ���� ���� һʱ ���� ͯ�� ���� ���� ���� ���� ��Щ�� ���� ���� ҹ�� ��� �峿 ¡�� ���� �糿 ���� Ԫ�·� ����ǰ ��Ѯ ��ǰ ǰҹ һ���� ���� ���� ���� ���� ��ǰ ���� ���� ��β ��һ ��ǰ ���� ���� Ԥ���� ���� ���� ���� �Ŵ� ���� ���� ���� ��һ ������ ���� ���� ��Ѯ ���� ƽ�� ��ҹ ���� ��� ��ǰ ս�� ���� ���� ��� ���� ��ų� ���� ƽʱ ���� ����� �ļ� ���� ���� �˺� ���� ȥ��ĩ ҹ�� ս�� ���� ���� ���� ��ĩ ���� ������ ȥ��� ��ĩ ��һ ���� ÿ��һ ���� �賿 �º� �ƺ� ȥ��� ������ ��� ��Ѯ ���� ʥ�� �ϸ��� ���� ���� ǰ�� �µ� ���� �°�ʱ �ϰ��� ǰ�� ���� ǰ���� ���� ��� ʥ���� ���� �պ� ��ʱ ���� ���� ȥ�� �϶� ������ ��ǰ ��ǰ ���� �°��� ի�� ���� ǰһ�� ����� �ڼ��� ���� ���� ���� ���� ��Ҷ Ǳ���� ���� Σ���� ʢ�� ͷ�� һ�� ���� ��ئ ���� ���� ��ź� ���� ���� ��ѩ �´� ��ǰ ���µ� ������ ��� �� ���� ǰЩ�� ���� ׳�� ������ ʱ�� ĸ�׽� �ƻ� �＾ ��ҹ ���� ���� ��� ���� ��� ��Ҷ �� ˮ���� ���� ���� ���� ������ ���� ��ҹ �ƴ� ���� �Ž� ���� ǰ�� ���� ���� ���� ͬһ�� ��һ�� ��� �� ���� ���� ÿ���� ���� ���� �δ� ��ĩ ��� ȥ�� ���� �֣����֣��� �ִ� ���ִ� ���� ��ĺ ��ǰ ���� ������ ��̼� ���� ���� ���� սʱ �ƽ�ʱ�� �ϰ�Ҷ ��Ϧ ��� ���� ���� Զ�� ��ʱ ���� ���� ���� ��ǰ ������ ���� ���� ������ �� ���� ��� ���� Ԥ���� ���� ���� ���� ����� ǰ�� ���� ����ĩ �ֽ� �ﶬ�� ȥ�� �峯 ������ ������ � ������ �ϴ� ��ի�� �紺 ����Ѯ ���� һҹ�� ����ʮ�� Ԫ���� ����� �ֽ׶� ��� ĺ�� ��ҹ �ľ� ���� ���� ��ʱ ҹ�� ��ǰ�� ����� ���� һ������ ��ʱ Сʱ�� �Ͷ��� ˫���� ���� ����ĩ �ļ��� Ѵǰ ��Ԫǰ ���������� ���� ����ĩ ������ ���� �ں� ���� С�� ���� �Ƴ� �꼾 ������ ���� ת˲ ���� ���� ��ҹ ���� ��һ �����һ ���³�һ �ش� ��ĩ һ���� ѩ�� ÿ���� ������ ���� Ԫ�� ���� ������ ���� ������� ���¶�ʮ�� ��Ϧҹ �»� ���� ���� ������ �ϰ�ʱ ������ ���һ ի�� ��ǰ ����ʮ�� ���� �̴� �̳� ������ ��ǰ ������ʮ ��ĩ ������ ���� ��� ٪���� ��һ �ϰ��� ����ǰ ����إ�� ���³��� ���� ���� ĩ�� ���� ���� ���� ǰ���� ��Ů�� ����һ ����إһ ���� ���ν� ��ʮ ����ʮ�� ��� ��β ����ҹ �й� һ���� ���� ���� ����� ��ţ ���� ������� ���� ������ �ϸ����� �ϸ��� ���� �� �� �� �� ";
		for (int i=0;i<Date.length();i++)
		{
			int start=i;
			while (Date.charAt(i)!=' ')
				i++;
			if (!hsDate.contains(Date.substring(start,i))){
				hsDate.add(Date.substring(start,i));
//				System.out.println(Date.substring(start,i));
			}
		}

		String Time="���� ���� ���� ��ҹ �賿 ���� ���� ���� �ƻ� ���� ��ҹ ";
			   Time+="���� ��ǰ Ŀǰ ��ǰ �˿� ˲�� ���� ��ʱ ��ʱ ���� һ˲ ��ʱ ɲ�Ǽ� ת�ۼ� ��ǰ һʱ�� ���� ɲ�� ";
		for (int i=0;i<Time.length();i++)
		{
			int start=i;
			while (Time.charAt(i)!=' ')
				i++;
			if (!hsTime.contains(Time.substring(start,i))){
				hsTime.add(Time.substring(start,i));
//				System.out.println(Time.substring(start,i));
			}
		}

		String number="ʮ�� ���� һ�� һЩ ��� �� �� ��� ���� һ���� �� �󲿷� �� ���� ���� ��ǧ ���� ��ʮ ������� ����� һ�� һ���� ˫ �� ���� һֻֻ �ܶ� �ٷ� һ���� һ���� һ�ζ� һ�� һ�� һ���� һ��� һ�δ� ˿�� һ���� �ڶ� �� ���� ���� ���� ���� һ���� ���� ȫ�� ���� ���� ǧǧ���� ��ʮ�� һ· ������ һ���� ���� ���� һ���� �������� ���� һ��� Щ�� ��� һ���� ͷ �ϰ��� һ���� ���� ���� һ���� һ���� һ���� �� �� һ��� ���� һ���� һ�ش� һͷͷ һ�Զ� һ���� һ�Ѱ� ������ ���� һ���� һ���� ���󲿷� �� ���� һ��� �� һ�� ˫˫ �� Ƭ�� �þ� ��ǧ�� ���� һ��� ��ͷ һĻĻ һƪƪ һ���� �ɰ���ǧ ��ǧ һ��� һ���� һͷ һ���� ��� ��ǧ�� һ�� ������ ���� һ���� �� ˫ʮ һ���� ���� ���� һ���� ��ʮ�� һҹҹ ��� һ˫˫ ���� ��� һöö һ���� һ���� ��� ���� �� һ���� ��� ���� ��ǧ�� �ٲ��� һ��һ�� һ׮׮ һ���� ������ ���� һ�Դ� һ����� �� һ���� һ���� һ��� һ���� һ�ɹ� һ�ʱ� ��� ��Щ һ�ݷ� һ���� ���� ˫�� ��ǧ�� һ��� һȦȦ һ�ſ� ���� ��� һ��� �� һ���� һȺȺ ʮ֮�˾� һ��� С���� һ̨̨ ���ε� һҳҳ ���� һ���� ����� ��� һƬƬ һҶҶ һ���� ������ �� �� �� �� �� �� �� һ���� һյյ һ�Ѷ� ";
		for (int i=0;i<number.length();i++)
		{
			int start=i;
			while (number.charAt(i)!=' ')
				i++;
			if (!hsNumber.contains(number.substring(start,i))){
				hsNumber.add(number.substring(start,i));
//				System.out.println(number.substring(start,i));
			}
		}

		String timeSuffix="�� �� �� ��� ʱ ���� �� ";
		for (int i=0;i<timeSuffix.length();i++)
		{
			int start=i;
			while (timeSuffix.charAt(i)!=' ')
				i++;
			if (!hsTimeSuffix.contains(timeSuffix.substring(start,i))){
				hsTimeSuffix.add(timeSuffix.substring(start,i));
//				System.out.println(timeSuffix.substring(start,i));
			}
		}

		String dateSuffix="�� ���� Ѯ �� ���� �� ��� ���� �·� �� ��� ��ǰ ��ǰ ��ǰ ��ǰ Сʱǰ ����ǰ ��� ��� �º� �պ� Сʱ�� ���ͺ� ";
		for (int i=0;i<dateSuffix.length();i++)
		{
			int start=i;
			while (dateSuffix.charAt(i)!=' ')
				i++;
			if (!hsDateSuffix.contains(dateSuffix.substring(start,i))){
				hsDateSuffix.add(dateSuffix.substring(start,i));
//				System.out.println(dateSuffix.substring(start,i));
			}
		}

		String numberSuffix="�� �� ֮һ ";
		for (int i=0;i<numberSuffix.length();i++)
		{
			int start=i;
			while (numberSuffix.charAt(i)!=' ')
				i++;
			if (!hsNumberSuffix.contains(numberSuffix.substring(start,i))){
				hsNumberSuffix.add(numberSuffix.substring(start,i));
//				System.out.println(numberSuffix.substring(start,i));
			}
		}

	}
	
	/**
	public boolean IsFamousPeople(String name)
	{
		if (hsFamousPeople.contains(name))
			return true;
		else
			return false;
	}

	public boolean IsFamousLocation(String name)
	{
		if (hsFamousLocations.contains(name))
			return true;
		else
			return false;
	}

	public boolean IsFamousOrganization(String name)
	{
		if (hsFamousOrganizations.contains(name))
			return true;
		else
			return false;
	}
	*/

	public boolean IsTimeSuffix(String name)
	{
		if (hsTimeSuffix.contains(name))
			return true;
		else
			return false;
	}

	public boolean IsDateSuffix(String name)
	{
		if (hsDateSuffix.contains(name))
			return true;
		else
			return false;
	}

	public boolean IsNumberSuffix(String numbersuffix)
	{
		if (hsNumberSuffix.contains(numbersuffix))
			return true;
		else
			return false;
	}

	public boolean IsDate(String name)
	{
		if (hsDate.contains(name))
			return true;
		else
			return false;
	}

	public boolean IsTime(String name)
	{
		if (hsTime.contains(name))
			return true;
		else
			return false;
	}

	public boolean IsNumber(String number)
	{
		if (hsNumber.contains(number))
			return true;
		else
			return false;
	}
	
	/**
	public HashSet getpeople(){
		return hsFamousPeople;
	}
	
	public HashSet getlocation(){
		return hsFamousLocations;
	}
	
	public HashSet getorganization(){
		return hsFamousOrganizations;
	}
	*/
}
