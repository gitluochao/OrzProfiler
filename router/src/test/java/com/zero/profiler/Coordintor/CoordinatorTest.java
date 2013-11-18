package com.zero.profiler.Coordintor;

import java.util.BitSet;

/**
 * User: luochao
 * Date: 13-10-29
 * Time: 涓婂崃10:32
 */
public class CoordinatorTest {
    public static void main(String[] args) {
         CoordinatorTest test = new CoordinatorTest();
         System.out.println(test.perfect("adsfadfsadfsfadaaa"));
    }
    public int perfect(String s){
      s = s.toLowerCase();
      int[] count = new int[26];
      for(int j = 0;j<26;j++){
          count[j] = 0;
      }
      if(s == null||s.length() == 0){
          return  0;
      }
      for(int i=0;i<s.length();i++){
          char c = s.charAt(i);
          count[c-97]  += 1;
      }
      quickSort(count,0,count.length-1);
      int result = 0;
      for(int i=25;i>=0;i--){
         result += count[i]*(i+1);
      }
      return  result;
    }
    public static void quickSort(int a[], int start, int end)
    {        int i,j;
             i = start;
             j = end;
             if((a==null)||(a.length==0))
                 return;
             while(i<j){
                 while(i<j&&a[i]<=a[j]){     //浠ユ暟缁剆tart涓嬫爣镄勬暟鎹负key锛屽彸渚ф壂鎻?
                     j--;
                 }
                 if(i<j){                   //鍙充晶镓弿锛屾垒鍑虹涓€涓瘮key灏忕殑锛屼氦鎹綅缃?
                     int temp = a[i];
                     a[i] = a[j];
                     a[j] = temp;
                 }
                  while(i<j&&a[i]<a[j]){    //宸︿晶镓弿锛堟镞禷[j]涓瓨鍌ㄧ潃key链硷级
                     i++;
                   }
                 if(i<j){                 //镓惧嚭绗竴涓瘮key澶х殑锛屼氦鎹綅缃?
                     int temp = a[i];
                     a[i] = a[j];
                     a[j] = temp;
                 }
            }
            if(i-start>1){
                 //阃掑綊璋幂敤锛屾妸key鍓嶉溃镄勫畲鎴愭帓搴?
                quickSort(a,0,i-1);
            }
            if(end-j>1){
                quickSort(a,j+1,end);    //阃掑綊璋幂敤锛屾妸key鍚庨溃镄勫畲鎴愭帓搴?
            }
    }
}
