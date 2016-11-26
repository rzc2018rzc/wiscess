package com.wiscess;

import com.wiscess.util.DesUtils;
import com.wiscess.util.PasswordUtil;

public class WiscessUtils {
	public static void usage(){
		System.out.println("Wiscess��˾�ڲ�������");
		System.out.println("");
		System.out.println("���");
		System.out.println("");
		System.out.println(" -des           ���ܽ��ܹ���");
		System.out.println(" -pwdcheck      ����ǿ���жϹ���");
		System.out.println("");
		System.out.println("ʹ�� \"java -jar wiscess-util.jar -command_name\" ��ȡ command_name ���÷� ");
	}
	public static void main(String[] args) {
		//û�в���ʱ����ʾ�÷���
		if(args.length<1){
			usage();
			return;
		}
		//��������1ʱ����һ������������ָ����command_name
		String commandName=args[0];
		//���������-h|/h|-?|/?|-help|/help����ʾ�÷�
		if(commandName.toLowerCase().matches("[-|/](help|h|\\?)")){
			usage();
			return;
		}
		//������ȥ����һ���������������Ĳ�����Ϊ������Ĳ���
		String[] subArgs=new String[args.length-1];
		System.arraycopy(args, 1, subArgs, 0, args.length-1);
		if(commandName.equalsIgnoreCase("-des")){
			DesUtils.main(subArgs);
		}else if(commandName.equalsIgnoreCase("-pwdcheck")){
			PasswordUtil.main(subArgs);
		}
	}

}
