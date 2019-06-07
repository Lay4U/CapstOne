package com.example.mysubwayproject;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;

import java.io.File;
import java.io.InputStream;
import java.util.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Scanner;
import java.lang.Object;

public class model extends Activity {



    public float modelPredict(String StationNM, int day, int hour, int minute)
    {


        Scanner scan = null;
        try {
            scan = new Scanner(new File(StationNM+".csv"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ArrayList<String[]> records = new ArrayList<String[]>();
        ArrayList<Integer> model_list = new ArrayList<Integer>();

        while (scan.hasNext()) {
            String[] record;
            record = scan.nextLine().split(",");
            records.add(record);
        }

        // 확인용 출력 확인완료
//		int i = 0;
//		for (String[] temp : records) {
//			for (String temp1 : temp) {
//				System.out.print(temp1 + " ");
//				i++;
//			}
//			System.out.print("\n");
//		}
//		System.out.println(i);
        // 과학적표기법 -> 실수

        for (String[] temp : records) {
            for (String temp1 : temp) {
                int val = new BigDecimal(temp1).intValue();
                model_list.add(val);
            }
        }
//		for(int t : model_list) {
//			System.out.println(t);
//		}	//확인완료
        int[] model = new int[model_list.size()];
        for (int i = 0; i < model.length; i++) {
            model[i] = model_list.get(i).intValue();
        }
//		for(int t : model) {
//		System.out.println(t);
//	}	//확인완료

        //가정
        //day 기준점으로부터 8
        //시간은 7시 24분일때
        //0부터 8까지 떨어져있는 8*20 + 7, 8*20 + 8을 직선의 방정식으로 구한다.
        // input 기준점으로부터 day(8)과 시각(7)을 받는다.

        int x1 = day * 20 + hour;
        int x2 = day * 20 + hour + 1;
        float y1 = model[x1];
        float y2 = model[x2];


        float a = (y2 - y1); //(x2-x1)은 항상1
        float b = y2 - a;

        // 24분에 해당하는점을 구한다.

        float x = (float) (1.0 / 60.0 * minute); // input은 24를 받는다.
        float y = a * x + b;
        System.out.println("x1 :" + x1);
        System.out.println("x2 :" + x2);
        System.out.println("y1 :" + y1);
        System.out.println("y2 :" + y2);
        System.out.println("a :" + a);
        System.out.println("b :" + b);
        System.out.println("x :" + x);
        System.out.print(y);

        return y;
    }

}