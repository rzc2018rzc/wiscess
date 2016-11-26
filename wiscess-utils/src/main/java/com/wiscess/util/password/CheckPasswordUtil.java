package com.wiscess.util.password;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.wiscess.util.PasswordUtil;
/**
 * �ܴa���Ҫ��8������
    ���ٷ���������������Ҏ�t:
    ��Ӣ�ĳ���
    С��Ӣ�ĳ���
    ���ֳ���
    ��̖����

    ������Ԫ��׃������߷֔�.
    ����ķ֔���ӷ��Ŀ�͜p���Ŀ�Ŀ���.
    �֔��Ĺ�����0~100��.
    �֔������_����ͳ��ȼ���Ӌ��.
 */
public final class CheckPasswordUtil extends PasswordUtil{
	
	private int length;//���볤��
    private int upperAlp = 0;//��д��ĸ����
    private int lowerAlp = 0;//Сд��ĸ����
    private int num = 0;//���ֳ���
    private int charlen = 0;//�����ַ�����

	public CheckPasswordUtil(String psw) {
		super(psw);
		this.psw = psw.replaceAll("\\s", "");
        this.length = psw.length();
	}
    //���볤�Ȼ���
    protected int CheckPswLength(){
        return this.length*4;
    }

    //��д��ĸ����
    protected int CheckPswUpper() {
        String reg = "[A-Z]";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(psw);
        int j = 0;
        while (matcher.find()) {
            j++;
        }
        this.upperAlp = j;
        if (j<=0) {
            return 0;
        }
        return (this.length-j)*2;
    }
    //����Сд��ĸ��Ԫ
    protected int CheckPwsLower(){
        String reg = "[a-z]";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(this.psw);
        int j = 0;
        while (matcher.find()) {
            j++;
        }
        this.lowerAlp = j;
        if (j<=0) {
            return 0;
        }
        return (this.length-j)*2;
    }

    //����������Ԫ
    protected int checkNum(){
        String reg = "[0-9]";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(this.psw);
        int j = 0;
        while (matcher.find()) {
            j++;
        }
        this.num = j;
        if (this.num == this.length) {
            return 0;
        }
        return j*4;
    }
    //���Է�����Ԫ
    protected int checkChar(){
        charlen = this.length -this.upperAlp
                -this.lowerAlp - this.num;
        return this.charlen*6;
    }

    //�ܴa���g���唵�ֻ��̖��Ԫ
    protected int checkNumOrCharInStr(){
        int j = this.num + this.charlen -1;
        if (j<0) {
            j=0;
        }
        if (this.num+this.charlen == this.length) {
            j = this.length - 2;
        }
        return j*2;
    }
    /**
     * ���Ҫ���׼
     * �÷�����Ҫ�����ϼӷַ���ʹ�ú�ſ���ʹ��
     * @return
     */
    protected int LowerQuest(){
        int j = 0;
        if (this.length>=8) {
            j++;
        }
        if (this.upperAlp>0) {
            j++;
        }
        if (this.lowerAlp > 0) {
            j++;
        }
        if (this.num>0) {
            j++;
        }
        if (this.charlen >0 ) {
            j++;
        }
        if (j>=4) {

        }else {
            j = 0;
        }
        return j*2;
    }
    /**=================�ָ���===�۷���Ŀ=====================**/
    //ֻ����Ӣ����ĸ
    protected int OnlyHasAlp(){
        if (this.length == (this.upperAlp+this.lowerAlp)) {
            return -this.length;
        }
        return 0;
    }

    //ֻ��������
    protected int OnlyHasNum(){
        if (this.length == this.num) {
            return -this.length;
        }
        return 0;
    }
    //�ظ���Ԫ�۷�
    protected int repeatDex(){
        char[] c = this.psw.toLowerCase().toCharArray();
        HashMap<Character, Integer> hashMap =
                new HashMap<Character, Integer>();
        for (int i = 0; i < c.length; i++) {
            if (hashMap.containsKey(c[i])) {
                hashMap.put(c[i], hashMap.get(c[i])+1);
            }else {
                hashMap.put(c[i], 1);
            }
        }
        int sum = 0;
        Iterator<Entry<Character, Integer>> iterator =
                hashMap.entrySet().iterator();
        while (iterator.hasNext()) {
            int j = iterator.next().getValue();
            if(j>0){
                sum = sum + j*(j-1);
            }
        }
        return -sum;
    }

    //����Ӣ�Ĵ�д��Ԫ
    protected int seriseUpperAlp(){
        int j=0;
        char[] c = this.psw.toCharArray();
        for (int i = 0; i < c.length-1; i++) {
            if(Pattern.compile("[A-Z]").matcher(c[i]+"").find()){
                if (Pattern.compile("[A-Z]").matcher(c[i+1]+"").find()) {
                    j++;
                }
            }
        }
        return -2*j;
    }

    //����Ӣ��Сд��Ԫ
    protected int seriseLowerAlp(){
        String reg = "[a-z]";
        int j=0;
        char[] c = this.psw.toCharArray();
        for (int i = 0; i < c.length-1; i++) {
            if (Pattern.compile(reg).matcher(c[i]+"").find()
                &&c[i]+1==c[i+1]) {
                j++;
            }
        }
        return -2*j;
    }

    //����������Ԫ
    protected int seriseNum(){
        String reg = "[0-9]";
        Pattern pattern = Pattern.compile(reg);
        char[] c = this.psw.toCharArray();
        int j=0;
        for (int i = 0; i < c.length-1; i++) {
            if (pattern.matcher(c[i]+"").matches()
                &&pattern.matcher(c[i+1]+"").matches()) {
                j++;
            }
        }
        return -2*j;
    }
    //������ĸabc def֮�೬��3���۷�  �����ִ�Сд��ĸ
    protected int seriesAlp2Three(){
        int j=0;
        char[] c = this.psw.toLowerCase(Locale.CHINA).toCharArray();
        for (int i = 0; i < c.length-2; i++) {
            if (Pattern.compile("[a-z]").matcher(c[i]+"").find()) {
                if ((c[i+1]==c[i]+1) && (c[i+2]==c[i]+2)) {
                    j++;
                }
            }
        }
        return -3*j;
    }

    //��������123 234֮�೬��3���۷�  
    protected int seriesNum2Three(){
        int j=0;
        char[] c = this.psw.toLowerCase(Locale.CHINA).toCharArray();
        for (int i = 0; i < c.length-2; i++) {
            if (Pattern.compile("[0-9]").matcher(c[i]+"").find()) {
                if ((c[i+1]==c[i]+1) && (c[i+2]==c[i]+2)) {
                    j++;
                }
            }
        }
        return -3*j;
    }

    public int jiafen(){
        System.out.println("�ܴa�֔�="+CheckPswLength());
        System.out.println("��Ӣ����Ԫ="+CheckPswUpper());
        System.out.println("С��Ӣ����Ԫ="+CheckPwsLower());
        System.out.println("������Ԫ="+checkNum());
        System.out.println("��̖��Ԫ="+checkChar());
        System.out.println("�ܴa���g���唵�ֻ��̖��Ԫ="+checkNumOrCharInStr());
        System.out.println("���_�ܴa���Ҫ���Ŀ="+LowerQuest());
        return CheckPswLength()+CheckPswUpper()+CheckPwsLower()+checkNum()+checkChar()+checkNumOrCharInStr()+LowerQuest();
    }

    public int jianfen(){
        System.out.println("ֻ��Ӣ����Ԫ="+OnlyHasAlp());
        System.out.println("ֻ�Д�����Ԫ="+OnlyHasNum());
        System.out.println("���}��Ԫ (Case Insensitive)="+repeatDex());
        System.out.println("�B�mӢ�Ĵ���Ԫ="+seriseUpperAlp());
        System.out.println("�B�mӢ��С����Ԫ="+seriseLowerAlp());
        System.out.println("�B�m������Ԫ="+seriseNum());
        System.out.println("�B�m��ĸ���^����(��abc,def)="+seriesAlp2Three());
        System.out.println("�B�m���ֳ��^����(��123,234)="+seriesNum2Three());
        return OnlyHasAlp()+OnlyHasNum()+repeatDex()+seriseUpperAlp()+seriseLowerAlp()+seriseNum()+seriesAlp2Three()+seriesNum2Three();
    }
	@Override
	protected int check() {
		int jiafen=CheckPswLength()+CheckPswUpper()+CheckPwsLower()+checkNum()+checkChar()+checkNumOrCharInStr()+LowerQuest();
		int jianfen=OnlyHasAlp()+OnlyHasNum()+repeatDex()+seriseUpperAlp()+seriseLowerAlp()+seriseNum()+seriesAlp2Three()+seriesNum2Three();
		return jiafen+jianfen;
	}
}
