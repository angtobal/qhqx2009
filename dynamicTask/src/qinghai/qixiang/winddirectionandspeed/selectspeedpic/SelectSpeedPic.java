package qinghai.qixiang.winddirectionandspeed.selectspeedpic;
/**
 * 
 * @author liu
 *选择表示不同风速的unicode码
 */
public class SelectSpeedPic {
	static String speedChar="!";
	public static String selectSpeedPic(int s){
		
		if(s<=2)
			return speedChar;
		if(s<=4)
			return speedChar="\"";
		if(s<=6)
			return speedChar="#";
		if(s<=8)
			return speedChar="$";
		if(s<=10)
			return speedChar="%";
		if(s<=12)
			return speedChar="&";
		if(s<=14)
			return speedChar="'";
		if(s<=16)
			return speedChar="(";
		if(s<=18)
			return speedChar=")";
		if(s<=20)
			return speedChar="*";
		if(s<=22)
			return speedChar="+";
		if(s<=24)
			return speedChar=",";
		if(s<=26)
			return speedChar="-";
		if(s<=28)
			return speedChar=".";
		if(s<=30)
			return speedChar="/";
		if(s<=32)
			return speedChar="0";
		if(s<=34)
			return speedChar="1";
		if(s<=36)
			return speedChar="2";
		if(s<=38)
			return speedChar="3";
		if(s<=40)
			return speedChar="4";
		if(s<=42)
			return speedChar="5";
		if(s<=44)
			return speedChar="6";
		if(s<=46)
			return speedChar="7";
		if(s<=46)
			return speedChar="8";
		if(s<=50)
			return speedChar="9";
		if(s<=52)
			return speedChar=":";
		if(s<=54)
			return speedChar=";";
		if(s<=56)
			return speedChar="<";
		if(s<=58)
			return speedChar="=";
		else
			return speedChar;
	}
}
