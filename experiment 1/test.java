package JavaCourse6;

import java.util.*;

public class test {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int n;
        System.out.println("请输入学生人数");
        n=sc.nextInt();
        int score[]=new int[n];
        int sum=0;
        for(int i=0;i<n;i++)
        {   System.out.println("输入第"+i+"个学生成绩");
        score[i]=sc.nextInt();
        sum=sum+score[i];
        }
        double avg=sum/n;
        System.out.println("平均分是"+avg);
        for(int i=0;i<score.length-1;i++){
        for(int j=0;j<score.length-1-i;j++){
        if(score[j]<score[j+1]){
        int t=score[j];
        score[j]=score[j+1];
        score[j+1]=t;
        }}}
        System.out.println("成绩降序排列：");
        for(int i=0;i<score.length;i++){
        System.out.print(score[i]+" ");}
        int max=score[0];
        for(int i=1;i<score.length;i++){
        if(score[i]>max)
        {max=score[i];}}
        System.out.println("\n最高分："+max);
    }
}