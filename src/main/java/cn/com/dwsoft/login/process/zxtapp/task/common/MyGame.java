package cn.com.dwsoft.login.process.zxtapp.task.common;

import java.util.Random;

/**
 * @author tlk
 * @date 2021/2/3-15:27
 */
public class MyGame {
    public static int silverCoin_1 = 1;
    public static int[] silverCoin_2 = {2, 6};
    public static int[] silverCoin_3 = {7, 14};
    public static int[] silverCoin_4 = {15, 24};
    public static int[] silverCoin_5 = {25, 39};
    public static int[] silverCoin_6 = {40, 214};
    public static int[] silverCoin_7 = {2215, 5000};
    public static int[] silverCoin_8 = {5001, 10000};
    public static int[] silverCoin_9 = {215, 2214};
    public static int goldCoin_1 = 1;
    public static int[] goldCoin_2 = {2, 6};
    public static int[] goldCoin_3 = {7, 14};
    public static int[] goldCoin_4 = {15, 24};
    public static int[] goldCoin_5 = {25, 139};
    public static int[] goldCoin_6 = {140, 814};
    public static int[] goldCoin_7 = {2815, 5000};
    public static int[] goldCoin_8 = {5001, 10000};
    public static int[] goldCoin_9 = {815, 2814};
    ;
    public static Random random = null;
//static{
//    random =new Random();
//    silverCoin_2= random.nextInt(5) + 2;
//    silverCoin_3= random.nextInt(8) + 7;
//    silverCoin_4= random.nextInt(10) + 15;
//    silverCoin_5= random.nextInt(115) + 25;
//    silverCoin_6= random.nextInt(675) + 140;
//    silverCoin_7= random.nextInt(2186) + 2815;
//    silverCoin_8= random.nextInt(5000) + 5001;
//    silverCoin_9= random.nextInt(2000) + 815;
//}

    public static void main(String[] args) {
        Random random = new Random();
        int i = random.nextInt(10000) + 1;
        for (int j = 0; j < 5; j++) {
            i = random.nextInt(10000) + 1;
            System.out.println("........" + i);
            if (i == MyGame.silverCoin_1) {
                System.out.println("===========一等奖");
            } else if (i >= MyGame.silverCoin_2[0] && i <= MyGame.silverCoin_2[1]) {
                System.out.println("========2等奖");
            } else if (i >= MyGame.silverCoin_3[0] && i <= MyGame.silverCoin_3[1]) {
                System.out.println("============3等奖");
            } else if (i >= MyGame.silverCoin_4[0] && i <= MyGame.silverCoin_4[1]) {
                System.out.println("===============4等奖");
            } else if (i >= MyGame.silverCoin_5[0] && i <= MyGame.silverCoin_5[1]) {
                System.out.println("=================5等奖");
            } else if (i >= MyGame.silverCoin_6[0] && i <= MyGame.silverCoin_6[1]) {
                System.out.println("=================6等奖");
            } else if (i >= MyGame.silverCoin_7[0] && i <= MyGame.silverCoin_7[1]) {
                System.out.println("7等奖");
            } else if (i >= MyGame.silverCoin_8[0] && i <= MyGame.silverCoin_8[1]) {
                System.out.println("8等奖");
            } else if (i >= MyGame.silverCoin_9[0] && i <= MyGame.silverCoin_9[1]) {
                System.out.println("9等奖");
            }


            if (i == MyGame.silverCoin_1) {
                System.out.println("===========一等奖");
            } else if (i >= MyGame.goldCoin_2[0] && i <= MyGame.goldCoin_2[1]) {
                System.out.println("========2等奖");
            } else if (i >= MyGame.goldCoin_3[0] && i <= MyGame.goldCoin_3[1]) {
                System.out.println("============3等奖");
            } else if (i >= MyGame.goldCoin_4[0] && i <= MyGame.goldCoin_4[1]) {
                System.out.println("===============4等奖");
            } else if (i >= MyGame.goldCoin_5[0] && i <= MyGame.goldCoin_5[1]) {
                System.out.println("=================5等奖");
            } else if (i >= MyGame.goldCoin_6[0] && i <= MyGame.goldCoin_6[1]) {
                System.out.println("=================6等奖");
            } else if (i >= MyGame.goldCoin_7[0] && i <= MyGame.goldCoin_7[1]) {
                System.out.println("7等奖");
            } else if (i >= MyGame.goldCoin_8[0] && i <= MyGame.goldCoin_8[1]) {
                System.out.println("8等奖");
            } else if (i >= MyGame.goldCoin_9[0] && i <= MyGame.goldCoin_9[1]) {
                System.out.println("9等奖");
            }
        }
    }
}
