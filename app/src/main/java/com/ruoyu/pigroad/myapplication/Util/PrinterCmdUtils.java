package com.ruoyu.pigroad.myapplication.Util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.ruoyu.pigroad.myapplication.R;

import java.io.UnsupportedEncodingException;

/**
 * Created by Administrator on 2017/6/28.
 */

public class PrinterCmdUtils {

    /**
     * 这些数据源自爱普生指令集,为POS机硬件默认
     */

    public static final byte ESC = 27;//换码
    public static final byte FS = 28;//文本分隔符
    public static final byte GS = 29;//组分隔符
    public static final byte DLE = 16;//数据连接换码
    public static final byte EOT = 4;//传输结束
    public static final byte ENQ = 5;//询问字符
    public static final byte SP = 32;//空格
    public static final byte HT = 9;//横向列表
    public static final byte LF = 10;//打印并换行（水平定位）
    public static final byte CR = 13;//归位键
    public static final byte FF = 12;//走纸控制（打印并回到标准模式（在页模式下） ）
    public static final byte CAN = 24;//作废（页模式下取消打印数据 ）


//------------------------打印机初始化-----------------------------


    /**
     * 打印机初始化
     *
     * @return
     */
    public static byte[] init_printer() {
        byte[] result = new byte[2];
        result[0] = ESC;
        result[1] = 64;
        return result;
    }


//------------------------换行-----------------------------


    /**
     * 换行
     *
     * @param lineNum 要换几行
     * @return
     */
    public static byte[] nextLine(int lineNum) {
        byte[] result = new byte[lineNum];
        for (int i = 0; i < lineNum; i++) {
            result[i] = LF;
        }

        return result;
    }

    /**
     * 实时状态传送
     *
     * @return
     */
    public static byte[] transfer() {
        byte[] result = new byte[3];
        result[0] = DLE;
        result[1] = EOT;
        result[2] = 1;
        return result;
    }


//------------------------下划线-----------------------------


    /**
     * 绘制下划线（1点宽）
     *
     * @return
     */
    public static byte[] underlineWithOneDotWidthOn() {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 45;
        result[2] = 1;
        return result;
    }


    /**
     * 绘制下划线（2点宽）
     *
     * @return
     */
    public static byte[] underlineWithTwoDotWidthOn() {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 45;
        result[2] = 2;
        return result;
    }

    /**
     * 取消绘制下划线
     *
     * @return
     */
    public static byte[] underlineOff() {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 45;
        result[2] = 0;
        return result;
    }


//------------------------加粗-----------------------------


    /**
     * 选择加粗模式
     *
     * @return
     */
    public static byte[] boldOn() {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 69;
        result[2] = 0xF;
        return result;
    }


    /**
     * 取消加粗模式
     *
     * @return
     */
    public static byte[] boldOff() {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 69;
        result[2] = 0;
        return result;
    }


//------------------------对齐-----------------------------


    /**
     * 左对齐
     *
     * @return
     */
    public static byte[] alignLeft() {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 97;
        result[2] = 0;
        return result;
    }


    /**
     * 居中对齐
     *
     * @return
     */
    public static byte[] alignCenter() {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 97;
        result[2] = 1;
        return result;
    }


    /**
     * 右对齐
     *
     * @return
     */
    public static byte[] alignRight() {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 97;
        result[2] = 2;
        return result;
    }


    /**
     * 水平方向向右移动col列
     *
     * @param col
     * @return
     */
    public static byte[] set_HT_position(byte col) {
        byte[] result = new byte[4];
        result[0] = ESC;
        result[1] = 68;
        result[2] = col;
        result[3] = 0;
        return result;
    }
//------------------------字体变大-----------------------------


//    /**
//     * 字体变大为标准的n倍
//     * @param num
//     * @return
//     */
//    public static byte[] fontSizeSetBig(int num)
//    {
//        byte realSize = 0;
//        switch (num)
//        {
//            case 1:
//                realSize = 0;break;
//            case 2:
//                realSize = 17;break;
//            case 3:
//                realSize = 34;break;
//            case 4:
//                realSize = 51;break;
//            case 5:
//                realSize = 68;break;
//            case 6:
//                realSize = 85;break;
//            case 7:
//                realSize = 102;break;
//            case 8:
//                realSize = 119;break;
//        }
//        byte[] result = new byte[3];
//        result[0] = 29;
//        result[1] = 33;
//        result[2] = realSize;
//        return result;
//    }


    /**
     * 字体变大为标准的n倍
     *
     * @param num 1:正常大小 2:两倍高 3:两倍宽 4:两倍大小 5:三倍高 6:三倍宽 7:三倍大小
     * @return
     */
    public static byte[] fontSizeSetBig(int num) {
        byte realSize = 0;
        switch (num) {
            case 1:
                realSize = 0;
                break;
            case 2:
                realSize = 1;
                break;
            case 3:
                realSize = 16;
                break;
            case 4:
                realSize = 17;
                break;
            case 5:
                realSize = 2;
                break;
            case 6:
                realSize = 32;
                break;
            case 7:
                realSize = 34;
                break;
        }
        byte[] result = new byte[3];
        result[0] = 29;
        result[1] = 33;
        result[2] = realSize;
        return result;
    }


//------------------------字体变小-----------------------------


    /**
     * 字体取消倍宽倍高
     *
     * @param num
     * @return
     */
    public static byte[] fontSizeSetSmall(int num) {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 33;

        return result;
    }


//------------------------切纸-----------------------------


    /**
     * 进纸并全部切割
     *
     * @return
     */
    public static byte[] feedPaperCutAll() {
        byte[] result = new byte[4];
        result[0] = GS;
        result[1] = 86;
        result[2] = 65;
        result[3] = 0;
        return result;
    }


    /**
     * 进纸并切割（左边留一点不切）
     *
     * @return
     */
    public static byte[] feedPaperCutPartial() {
        byte[] result = new byte[4];
        result[0] = GS;
        result[1] = 86;
        result[2] = 66;
        result[3] = 0;
        return result;
    }

    //------------------------切纸-----------------------------
    public static byte[] byteMerger(byte[] byte_1, byte[] byte_2) {
        byte[] byte_3 = new byte[byte_1.length + byte_2.length];
        System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
        System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
        return byte_3;
    }


    public static byte[] byteMerger(byte[][] byteList) {

        int length = 0;
        for (int i = 0; i < byteList.length; i++) {
            length += byteList[i].length;//所有byte的元素数量
        }
        byte[] result = new byte[length];

        int index = 0;
        for (int i = 0; i < byteList.length; i++) {
            byte[] nowByte = byteList[i];
            for (int k = 0; k < byteList[i].length; k++) {
                result[index] = nowByte[k];
                index++;
            }
        }
        return result;
    }


    public static byte[] clientPaper() {
        try {
            byte[] next2Line = nextLine(2);

            byte[] title = "出餐单（午餐）**万通中心店".getBytes("gb2312");

            byte[] boldOn = boldOn();

            byte[] fontSize2Big = fontSizeSetBig(3);

            byte[] center = alignCenter();

            byte[] Focus = "网 507 ".getBytes("gb2312");

            byte[] boldOff = boldOff();

            byte[] fontSize2Small = fontSizeSetSmall(3);

            byte[] left = alignLeft();

            byte[] orderSerinum = "订单编号：11234 ".getBytes("gb2312");

            boldOn = boldOn();

            byte[] fontSize1Big = fontSizeSetBig(2);

            byte[] FocusOrderContent = "韭菜鸡蛋饺子-小份（单） ".getBytes("gb2312");

            boldOff = boldOff();

            byte[] fontSize1Small = fontSizeSetSmall(2);

            next2Line = nextLine(2);

            byte[] priceInfo = "应收:22元 优惠：2.5元  ".getBytes("gb2312");

            byte[] nextLine = nextLine(1);

            byte[] priceShouldPay = "实收:19.5元 ".getBytes("gb2312");

            nextLine = nextLine(1);


            byte[] takeTime = "取餐时间:2015-02-13 12:51:59 ".getBytes("gb2312");

            nextLine = nextLine(1);

            byte[] setOrderTime = "下单时间：2015-02-13 12:35:15 ".getBytes("gb2312");


            byte[] tips_1 = "微信关注 ** 自助下单每天免1元 ".getBytes("gb2312");

            nextLine = nextLine(1);

            byte[] tips_2 = "饭后点评再奖5毛 ".getBytes("gb2312");

            nextLine = nextLine(1);

            byte[] breakPartial = feedPaperCutPartial();

            byte[][] cmdBytes = {

                    title, nextLine,

                    center, boldOn, fontSize2Big, Focus, boldOff, fontSize2Small, next2Line,

                    left, orderSerinum, nextLine,

                    center, boldOn, fontSize1Big, FocusOrderContent, boldOff, fontSize1Small, nextLine,

                    left, next2Line,

                    priceInfo, nextLine,

                    priceShouldPay, next2Line,

                    takeTime, nextLine,

                    setOrderTime, next2Line,

                    center, tips_1, nextLine,

                    center, tips_2, next2Line,

                    breakPartial

            };

            return byteMerger(cmdBytes);

        } catch (UnsupportedEncodingException e) {

// TODO Auto-generated catch block

            e.printStackTrace();
        }
        return null;

    }


    /**
     * 测试打印
     *
     * @param msg
     * @return
     */
    public static byte[] printerText(String msg) {
        try {
            byte[] text = msg.getBytes("gb2312");

            byte[][] cmdBytes = {
                    text, nextLine(1)
            };

            return byteMerger(cmdBytes);

        } catch (UnsupportedEncodingException e) {

// TODO Auto-generated catch block

            e.printStackTrace();
        }
        return null;

    }


    static String hen = "--------------------------------";
    static String shu = "12345678901234567890123456789012";
    static String lan3 = "              已收       退款   ";
    static String lan = "菜单               数量  价格   ";
    static String lan2 = "菜单               数量  积分   ";
    static String lan4 = "菜单                数量    价格";


    /**
     * 测试打印方法
     *
     * @return
     */
    public static byte[] clientPaper2(String title, String time, String order_id, String oil_station, String oil_type, String oil_price, String oil_lit,
                                      String oil_gun, String total, String pay) {
        try {

            byte[] Store_name = ("油客来"+title + "\n\n").getBytes("gb2312");
            byte[] Time = (time + "\n").getBytes("gb2312");
            byte[] Order_id = (order_id + "\n").getBytes("gb2312");
            byte[] Oil_station = (oil_station + "\n").getBytes("gb2312");
            byte[] Oil_type = (oil_type + "\n").getBytes("gb2312");
            byte[] Oil_price = (oil_price + "\n").getBytes("gb2312");
            byte[] Oil_lit = (oil_lit + "\n").getBytes("gb2312");
            byte[] Oil_gun = (oil_gun + "\n").getBytes("gb2312");
            byte[] Total = (total + "\n").getBytes("gb2312");
            byte[] Pay = (pay + "\n").getBytes("gb2312");


            byte[] Down_prompt = "  谢谢惠顾，欢迎再次光临\n".getBytes("gb2312");


            byte[][] cmdBytes = {

                    nextLine(2),

                    alignCenter(), fontSizeSetBig(1), Store_name,

                    alignLeft(), fontSizeSetBig(1), Time,
                    alignLeft(), fontSizeSetBig(1), Order_id,
                    alignLeft(), fontSizeSetBig(1), Oil_station,
                    alignLeft(), fontSizeSetBig(1), Oil_type,
                    alignLeft(), fontSizeSetBig(1), Oil_price,
                    alignLeft(), fontSizeSetBig(1), Oil_lit,
                    alignLeft(), fontSizeSetBig(1), Oil_gun,
                    alignLeft(), fontSizeSetBig(1), Total,
                    alignLeft(), fontSizeSetBig(1), Pay,

                    nextLine(2),

                    alignLeft(), Down_prompt, nextLine(3),

                    feedPaperCutPartial()
            };

            return byteMerger(cmdBytes);

        } catch (UnsupportedEncodingException e) {

// TODO Auto-generated catch block

            e.printStackTrace();
        }
        return null;

    }


    public static String CMD_SetPos() {
        return new StringBuffer().append((char) 27).append((char) 64).toString();
    }

    public static String CMC_QianXiang() {
        return new StringBuffer().append((char) 27).append((char) 112).append((char) 0).append((char) 60).append((char) 255).toString();
    }

    public static String CMC_QianXiangTest() {
        return new StringBuffer().append((char) 27).append((char) 122).append((char) 1).append((char) 60).append((char) 255).toString();
    }

    public static byte[] printMoneyData() {
        String openMoney = CMC_QianXiang();
        byte[] initArray = new byte[0];
        byte[] tiArray = new byte[0];
        try {
            tiArray = openMoney.getBytes("gb2312");
            // sssArray = s1.getBytes("gb2312");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[][] cmdBytes = {initArray, tiArray};
        return byteMerger(cmdBytes);
    }


    /**
     * 菜品
     *
     * @param food_name
     * @param food_num
     * @param food_price
     * @param num
     * @return
     */
    public static String dataStringFoods(String food_name, String food_num, String food_price, int num) {
        String result = null;
        StringBuffer sb = new StringBuffer();
        try {

            char[] name_arr = food_name.toCharArray();//字符數
            Log.d("abcd", "name_arr: " + name_arr.length);
            char[] num_arr = food_num.toCharArray();
            Log.d("abcd", "num_arr: " + num_arr.length);
            char[] price_arr = food_price.toCharArray();

            for (int i = 0; i < num; i++) {
                if (i == 0) {
                    if (name_arr.length > 9) {
                        sb.append(name_arr[0]);
                        sb.append(name_arr[1]);
                        sb.append(name_arr[2]);
                        sb.append(name_arr[3]);
                        sb.append(name_arr[4]);
                        sb.append(name_arr[5]);
                        sb.append(name_arr[6]);
                        sb.append(name_arr[7]);
                        sb.append(".");
                        sb.append(".");
                        sb.append(name_arr[name_arr.length - 1]);
                        int znum = 0;
                        char[] chars = sb.toString().toCharArray();
                        for (int j = 0; j < chars.length; j++) {
                            if (isZhongWen(chars[j])) {
                                znum++;//中文個數
                            }
                        }
                        Log.d("dataStringSet2", "znum: " + znum);
                        for (int j = 0; j < 9 - znum; j++) {
                            //一個中文占2位，數字占1位
                            sb.append(" ");//40-4*znum =
                        }
//                        sb.append(" ");
                        i = 20;
                    } else {
                        int zhongwen = 0;
                        for (int j = 0; j < name_arr.length; j++) {
                            sb.append(name_arr[j]);
                            if (isZhongWen(name_arr[j])) {
                                zhongwen++;
                            }
                        }
                        i = zhongwen * 2 + name_arr.length - zhongwen;
                        Log.d("dataStringSet2", "dataStringSet2: i--->" + i);
//                        sb.append(" ");

                    }
                } else if (i == 22) {
                    for (int j = 0; j < num_arr.length; j++) {
                        sb.append(num_arr[j]);
                        i++;
                    }
                } else if (i == 26) {
                    for (int j = 0; j < price_arr.length; j++) {
                        sb.append(price_arr[j]);
                        i++;
                    }
                } else {
                    sb.append(" ");
                }
            }
            result = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 根据Unicode编码判断中文汉字和符号
     * 判断中文汉字和符号
     *
     * @param c
     * @return
     */
    public static boolean isZhongWen(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
            Log.d("222isZhongWen", "isZhongWen: " + c);
            return true;
        }
        return false;
    }

    // 根据UnicodeBlock方法判断中文标点符号
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static boolean isChinesePunctuation(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_FORMS
                || ub == Character.UnicodeBlock.VERTICAL_FORMS) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 图片转 byte[] 数组
     *
     * @param context
     * @return
     */
    public static byte[] getBitmapData(Context context) {

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_face_scan);

        byte[] printBitmap = printBitmap(bitmap);
        if (bitmap != null && bitmap.isRecycled()) {
            bitmap.recycle();
        }
        return printBitmap;
    }

    public static byte[] printBitmap(Bitmap bmp) {
        if (bmp == null) {
            byte[] bytes = "".getBytes();
            return bytes;
        }
        bmp = compressPic(bmp);
        byte[] bmpByteArray = draw2PxPoint(bmp);
        return bmpByteArray;
    }

    /**
     * 对图片进行压缩（去除透明度）
     * 可以设置图片大小
     * int newWidth = 150;
     * int newHeight = 150;
     *
     * @param bitmapOrg
     */
    private static Bitmap compressPic(Bitmap bitmapOrg) {
        // 获取这个图片的宽和高
        int width = bitmapOrg.getWidth();
        int height = bitmapOrg.getHeight();
        // 定义预转换成的图片的宽度和高度
        int newWidth = 150;
        int newHeight = 150;
        Bitmap targetBmp = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);
        Canvas targetCanvas = new Canvas(targetBmp);
        targetCanvas.drawColor(0xffffffff);
        targetCanvas.drawBitmap(bitmapOrg, new Rect(0, 0, width, height), new Rect(0, 0, newWidth, newHeight), null);
        return targetBmp;
    }


    /*************************************************************************
     * 假设一个360*360的图片，分辨率设为24, 共分15行打印 每一行,是一个 360 * 24 的点阵,y轴有24个点,存储在3个byte里面。
     * 即每个byte存储8个像素点信息。因为只有黑白两色，所以对应为1的位是黑色，对应为0的位是白色
     **************************************************************************/
    private static byte[] draw2PxPoint(Bitmap bmp) {
        //先设置一个足够大的size，最后在用数组拷贝复制到一个精确大小的byte数组中
        int size = bmp.getWidth() * bmp.getHeight() / 8 + 1000;
        byte[] tmp = new byte[size];
        int k = 0;
        // 设置行距为0
        tmp[k++] = 0x1B;
        tmp[k++] = 0x33;
        tmp[k++] = 0x00;
        // 居中打印
        tmp[k++] = 0x1B;
        tmp[k++] = 0x61;
        tmp[k++] = 1;
        for (int j = 0; j < bmp.getHeight() / 24f; j++) {
            tmp[k++] = 0x1B;
            tmp[k++] = 0x2A;// 0x1B 2A 表示图片打印指令
            tmp[k++] = 33; // m=33时，选择24点密度打印
            tmp[k++] = (byte) (bmp.getWidth() % 256); // nL
            tmp[k++] = (byte) (bmp.getWidth() / 256); // nH
            for (int i = 0; i < bmp.getWidth(); i++) {
                for (int m = 0; m < 3; m++) {
                    for (int n = 0; n < 8; n++) {
                        byte b = px2Byte(i, j * 24 + m * 8 + n, bmp);
                        tmp[k] += tmp[k] + b;
                    }
                    k++;
                }
            }
            tmp[k++] = 10;// 换行
        }
        // 恢复默认行距
        tmp[k++] = 0x1B;
        tmp[k++] = 0x32;
        byte[] result = new byte[k];
        System.arraycopy(tmp, 0, result, 0, k);
        return result;
    }

    /**
     * 图片二值化，黑色是1，白色是0
     *
     * @param x   横坐标
     * @param y   纵坐标
     * @param bit 位图
     * @return
     */
    private static byte px2Byte(int x, int y, Bitmap bit) {
        if (x < bit.getWidth() && y < bit.getHeight()) {
            byte b;
            int pixel = bit.getPixel(x, y);
            int red = (pixel & 0x00ff0000) >> 16; // 取高两位
            int green = (pixel & 0x0000ff00) >> 8; // 取中两位
            int blue = pixel & 0x000000ff; // 取低两位
            int gray = RGB2Gray(red, green, blue);
            if (gray < 128) {
                b = 1;
            } else {
                b = 0;
            }
            return b;
        }
        return 0;
    }

    /**
     * 图片灰度的转化
     */
    private static int RGB2Gray(int r, int g, int b) {
        // 灰度转化公式
        int gray = (int) (0.29900 * r + 0.58700 * g + 0.11400 * b);
        return gray;
    }

    public static String getInfoString(String food_name, int num) {
        String result = null;
        StringBuffer sb = new StringBuffer();
        try {

            char[] name_arr = food_name.toCharArray();//字符數

            for (int i = 0; i < num; i++) {
                if (i == 0) {
                    if (name_arr.length > 9) {
                        sb.append(name_arr[0]);
                        sb.append(name_arr[1]);
                        sb.append(name_arr[2]);
                        sb.append(name_arr[3]);
                        sb.append(name_arr[4]);
                        sb.append(name_arr[5]);
                        sb.append(name_arr[6]);
                        sb.append(name_arr[7]);
                        sb.append(".");
                        sb.append(".");
                        sb.append(name_arr[name_arr.length - 1]);
                        int znum = 0;
                        char[] chars = sb.toString().toCharArray();
                        for (int j = 0; j < chars.length; j++) {
                            if (isZhongWen(chars[j])) {
                                znum++;//中文個數
                            }
                        }
                        Log.d("dataStringSet2", "znum: " + znum);
                        for (int j = 0; j < 9 - znum; j++) {
                            //一個中文占2位，數字占1位
                            sb.append(" ");//40-4*znum =
                        }
//                        sb.append(" ");
                        i = 20;
                    } else {
                        int zhongwen = 0;
                        for (int j = 0; j < name_arr.length; j++) {
                            sb.append(name_arr[j]);
                            if (isZhongWen(name_arr[j])) {
                                zhongwen++;
                            }
                        }
                        i = zhongwen * 2 + name_arr.length - zhongwen;
                        Log.d("dataStringSet2", "dataStringSet2: i--->" + i);
//                        sb.append(" ");

                    }
                } else {
                    sb.append(" ");
                }
            }
            result = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 测试打印方法
     *
     * @return
     */
    public static byte[] printTicket() {
        try {

            byte[] Store_name = "    油客来支付小票-顾客联\n".getBytes("gb2312");

            byte[] You = (getInfoString("测试测试", 32) + "\n").getBytes("gb2312");

            byte[] Time = (getInfoString("测试测试", 32) + "\n").getBytes("gb2312");

            byte[] Order_id = (getInfoString("测试测试", 32) + "\n").getBytes("gb2312");

            byte[] Order_station = (getInfoString("测试测试", 32) + "\n").getBytes("gb2312");

            byte[] Oil_type = (getInfoString("测试测试", 32) + "\n").getBytes("gb2312");

            byte[] Oil_price = (getInfoString("测试测试", 32) + "\n").getBytes("gb2312");

            byte[] Oil_lit = (getInfoString("测试测试", 32) + "\n").getBytes("gb2312");

            byte[] Oil_gun = (getInfoString("测试测试", 32) + "\n").getBytes("gb2312");

            byte[] All_price = (getInfoString("测试测试", 32) + "\n").getBytes("gb2312");

            byte[] Pay = (getInfoString("测试测试", 32) + "\n").getBytes("gb2312");


            byte[][] cmdBytes = {

                    alignCenter(), fontSizeSetBig(3), Store_name,

                    You,

                    Time,

                    Order_id,

                    Order_station,

                    Oil_type,

                    Oil_price,

                    Oil_lit,

                    alignLeft(), fontSizeSetBig(2), Oil_gun,

                    alignLeft(), fontSizeSetBig(2), All_price,

                    alignLeft(), fontSizeSetBig(2), Pay,

                    feedPaperCutPartial()
            };

            return byteMerger(cmdBytes);

        } catch (UnsupportedEncodingException e) {

// TODO Auto-generated catch block

            e.printStackTrace();
        }
        return null;

    }
}
