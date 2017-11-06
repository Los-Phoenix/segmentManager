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

		String Date="春节 路神生日 元宵节 二月二龙抬头 寒食节 清明节 端午节 天贶节 六月初六姑姑节 七夕 盂兰盆 中元节 鬼节 地藏节 中秋节 重阳节 祭祖节 冬至 腊八节 腊八 过小年 除夕 ";
			   Date+="元旦 腊八节 除夕 春节 情人节 元宵节 妇女节 植树节 国际消费日 愚人节 清明 世界卫生日 国际劳动节 中国青年节 国际护士节 国际家庭日 全国学生营养日 世界无烟日 母亲节 国际儿童节 儿童节 端午节 父亲节 香港回归日 八一建军节 建军节 七夕情人节 七夕 劳动节 教师节 国际聋人节 中秋节 国庆节 国际盲人节 重阳节 万圣节 中国记者日 国际大学生节 感恩节 冬至节 冬至 世界残疾人日 世界足球日 圣诞节 ";
			   Date+="建元 元光 元朔 元狩 元鼎 元封 太初 天汉 太始 征和 后元 始元 元凤 元平 本始 地节 元康 神爵 五凤 甘露 黄龙 初元 永光 建昭 竟宁 建始 河平 阳朔 鸿嘉 永始 元延 绥和 建平 元寿 元始 居摄 初始 始建国 天凤 地皇 更始 建武 中元 永平 建初 元和 章和 永元 元兴 延平 永初 元初 永宁 建光 延光 永建 阳嘉 永和 汉安 建康 永嘉 本初 建和 和平 元嘉 永兴 永寿 延熹 永康 建宁 熹平 光和 中平 光熹 昭宁 永汉 中平 初平 兴平 建安 延康 三国 黄初 太和 青龙 景初 正始 嘉平 正元 甘露 景元 咸熙 章武 建兴 延熙 景耀 炎兴 黄武 黄龙 嘉禾 赤乌 太元 神凤 建兴 五凤 太平 永安 元兴 甘露 宝鼎 建衡 凤凰 天玺 天纪 泰始 咸宁 太康 太熙 永熙 永平 元康 永康 永宁 太安 永安 建武 永安 永兴 光熙 永嘉 建兴 建武 太兴 永昌 太宁 咸和 咸康 建元 永和 升平 隆和 兴宁 太和 咸安 宁康 太元 隆安 元兴 义熙 元熙 永初 景平 元嘉 孝建 大明 永光 景和 泰始 泰豫 元徽 升明 建元 永明 隆昌 延兴 建武 永泰 永元 中兴 天监 普通 大通 中大通 大同 中大同 太清 大宝 天正 承圣 天成 绍泰 太平 永定 天嘉 天康 光大 太建 至德 祯明 登国 皇始 天兴 天赐 永兴 神瑞 泰常 始光 神 延和 太延 太平真君 正平 承平 兴安 兴光 太安 和平 天安 皇兴 延兴 承明 太和 景明 正始 永平 延昌 熙平 神龟 正光 孝昌 武泰 建义 永安 建明 普泰 中兴 太昌 永兴 永熙 东魏 天平 元象 兴和 武定 大统 武成 保定 天和 建德 宣政 大成 大象 天保 干明 皇建 太宁 河清 天统 武平 隆化 承光 开皇 仁寿 大业 义宁 武德 贞观 永徽 显庆 龙朔 麟德 干封 总章 咸亨 上元 仪凤 调露 永隆 开耀 永淳 弘道 嗣圣 文明 光宅 垂拱 永昌 载初 天授 如意 长寿 延载 证圣 万岁登封 万岁通天 神功 圣历 久视 大足 长安 神龙 景龙 唐隆 景云 太极 延和 先天 开元 天宝 至德 干元 上元 宝应 广德 永泰 大历 建中 兴元 贞元 永贞 元和 长庆 宝历 太和 开成 会昌 大中 咸通 干符 广明 中和 光 文德 龙纪 大顺 景福 干宁 光化 天复 天佑 开平 干化 贞明 龙德 同光 天成 长兴 应顺 清泰 天福 开运 天福 干佑 广顺 显德 建隆 干德 开宝 太平兴国 雍熙 端拱 淳化 至道 咸平 景德 大中祥符 元禧 干兴 天圣 明道 景佑 宝元 康定 庆历 皇佑 至和 嘉佑 治平 熙宁 元丰 元佑 绍圣 元符 建中靖国 崇宁 大观 政和 重和 宣和 靖康 建炎 绍兴 隆兴 干道 淳熙 绍熙 庆元 嘉泰 开禧 嘉定 宝庆 绍定 端平 嘉熙 淳佑 宝佑 开庆 景定 咸淳 德佑 景炎 祥兴 神册 天赞 天显 会同 大同 天禄 应历 保宁 干亨 统和 开泰 太平 景福 重熙 清宁 咸雍 太康 太安 寿昌 干统 天庆 保大 收国 天辅 天会 天眷 皇统 天德 贞元 正隆 大定 明昌 承安 泰和 大安 崇庆 至宁 贞佑 兴定 元光 正大 开兴 天兴 中统 至元 元贞 大德 至大 皇庆 延佑 至治 泰定 致和 天历 至顺 元统 至元 至正 显道 开运 广运 大庆天授礼法延祚 延嗣宁国 天佑垂圣 福圣承道 奲都 拱化 干道天赐礼盛国庆 大安 天安礼定 天仪治平 天佑民安 永安 贞观 雍宁 元德 正德 大德 大庆 人庆 天盛 干佑 天庆 应天 皇建 光定 干定 宝庆 洪武 建文 永乐 洪熙 宣德 正统 景泰 天顺 成化 弘治 正德 嘉靖 隆庆 万历 泰昌 天启 崇祯 天命 天聪 崇德 顺治 康熙 雍正 干隆 嘉庆 道光 咸丰 同治 光绪 宣统 ";
			   Date+="星期一 星期二 星期三 星期四 星期五 星期六 星期天 星期日 周一 周二 周三 周四 周五 周六 周日 周末 ";
			   Date+="新年 最近 未来 元旦 牛年 虎年 今年 今后 今晚 明天 今夜 今天 上午 新春 当年 生前 现代 元月 晚上 春节 昨天 子夜 上年 明年 下午 过去 如今 年终 近日 去年 晚年 近期 幼年 岁末 当代 春天 明日 旧岁 一时 后来 童年 中年 夏天 新岁 近年 近些年 冬季 入秋 夜晚 年初 清晨 隆冬 昨晚 早晨 寒冬 元月份 不久前 上旬 年前 前夜 一大早 上周 以往 早年 眼下 产前 产中 产后 岁尾 七一 节前 上月 往日 预备期 会上 周六 周日 古代 周四 中午 岁首 八一 休眠期 将来 本月 中旬 年中 平日 午夜 天明 年底 此前 战国 明清 明代 清代 近代 解放初 西晋 平时 当下 中秋节 夏季 老年 过后 此后 昔日 去年末 夜间 战后 当天 秋天 金秋 周末 傍晚 星期天 去年底 年末 五一 当日 每周一 早上 凌晨 事后 酒后 去年初 生长期 婚后 下旬 当初 圣诞 上个月 早先 本周 前年 月底 赛后 下半时 上半年 前段 本年 前不久 当月 暑假 圣诞节 冬天 日后 旧时 白天 当晚 去冬 严冬 戊寅年 从前 赛前 首日 下半年 斋月 往年 前一天 今年初 节假日 深秋 开春 课外 忌日 中叶 潜伏期 春季 危险期 盛夏 头天 一早 年内 五卅 秋日 仲夏 解放后 翌年 晚期 大雪 下次 会前 本月底 中世纪 晚间 今冬 冬日 前些年 孕期 壮年 星期三 时下 母亲节 黄昏 秋季 深夜 国庆 翌日 午后 那年 会后 初叶 今晨 水晶节 昨日 正月 初三 上星期 次日 半夜 唐代 初秋 古今 正午 前天 来年 即日 汉代 同一天 上一年 震后 今春 腊八 下月 每周五 北宋 南宋 宋代 清末 民初 去岁 端阳 现（后现）代 现代 后现代 往昔 岁暮 饭前 戊寅 那阵子 顷刻间 夏日 后天 初二 战时 黄金时代 上半叶 除夕 秋后 黎明 清早 远古 现时 往常 三八 周三 先前 大清早 假期 淡季 星期日 今朝 三国 年关 初冬 预产期 腊月 冷天 半月 后半期 前周 西周 上年末 现今 秋冬季 去秋 清朝 三伏天 三九天 深冬 上周四 上次 开斋节 早春 中下旬 往后 一夜间 正月十五 元宵节 国庆节 现阶段 年节 暮秋 昨夜 四九 初四 拂晓 儿时 夜深 大前年 寒武纪 下周 一二·九 古时 小时候 劳动节 双休日 早晚 世纪末 四季度 汛前 公元前 明洪武三年 北汉 本周末 星期六 三九 节后 春秋 小寒 明朝 唐朝 雨季 夏商周 周岁 转瞬 晨昏 寒假 秋夜 隋唐 初一 大年初一 正月初一 秦代 明末 一清早 雪后 每周六 成熟期 商周 元年 王年 星期五 初夏 大年初二 腊月二十八 除夕夜 下回 术后 清明 下周三 上半时 中生代 年初一 斋日 事前 腊月十六 日内 商代 商朝 交货期 行前 大年三十 期末 哺乳期 次年 五代 侏罗世 周一 上半期 半年前 腊月廿九 正月初七 初五 西楚 末了 冬至 后晌 半年 前半年 妇女节 下周一 腊月廿一 暑期 肉孜节 初十 正月十四 今儿 年尾 大年夜 中古 一季度 初六 初八 年初二 丑牛 首岁 大年初三 今宵 本世纪 上个世纪 上个月 初春 春 夏 秋 冬 ";
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

		String Time="上午 中午 下午 午夜 凌晨 早上 正午 傍晚 黄昏 三更 半夜 ";
			   Time+="现在 当前 目前 日前 此刻 瞬间 当今 当时 霎时 须臾 一瞬 此时 刹那间 转眼间 眼前 一时间 方才 刹那 ";
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

		String number="十分 百年 一个 一些 许多 首 多 多次 部分 一部分 余 大部分 大部 大量 多种 数千 数万 数十 绝大多数 大多数 一番 一批批 双 半 半生 一只只 很多 百分 一个个 一张张 一段段 一半 一两 一幅幅 一块块 一次次 丝毫 一座座 众多 数 多年 若干 数百 左右 一排排 多数 全部 少量 多样 千千万万 数十万 一路 许许多多 一下子 大批 半数 一束束 半数以上 无数 一册册 些许 诸多 一代代 头 上百万 一曲曲 几亿 不久 一阵子 一面面 一条条 整 来 一朵朵 少数 一道道 一簇簇 一头头 一对对 一幢幢 一把把 大批量 数亿 一辆辆 一整套 绝大部分 俩 批量 一封封 又 一对 双双 许 片刻 好久 数千亿 上亿 一点儿 出头 一幕幕 一篇篇 一个半 成百上千 成千 一点点 一项项 一头 一阵阵 万分 数千万 一阵 数百万 两面 一辈子 把 双十 一声声 几度 大宗 一串串 数十亿 一夜夜 大半 一双双 数次 半点 一枚枚 一行行 一件件 许久 上下 初 一首首 多半 几许 成千万 少部分 一点一点 一桩桩 一步步 极少数 个把 一丛丛 一丁点儿 Ⅲ 一天天 一锤锤 一寸寸 一年年 一股股 一笔笔 半百 好些 一份份 一点点儿 终生 双百 上千亿 一届届 一圈圈 一颗颗 有余 半儿 一半儿 ⑵ 一车车 一群群 十之八九 一多半 小批量 一台台 点点滴滴 一页页 良久 一箱箱 大半天 半截 一片片 一叶叶 一艘艘 两个半 ① ② ③ ④ ⑤ ⑥ ⑦ 一场场 一盏盏 一堆堆 ";
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

		String timeSuffix="点 分 秒 点半 时 点钟 钟 ";
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

		String dateSuffix="日 星期 旬 月 季度 年 年代 世纪 月份 号 年间 天前 年前 月前 日前 小时前 世纪前 天后 年后 月后 日后 小时后 世纪后 ";
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

		String numberSuffix="倍 成 之一 ";
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
