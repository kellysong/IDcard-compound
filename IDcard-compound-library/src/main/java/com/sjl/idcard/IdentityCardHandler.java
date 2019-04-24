package com.sjl.idcard;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.sjl.idcard.entity.IdentityCard;
import com.sjl.idcard.listener.CompoundListener;
import com.sjl.idcard.util.BitmapUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 身份证正反面合成处理类
 *
 * @author Kelly
 * @version 1.0.0
 * @filename IdentityCardHandler.java
 * @time 2017/4/13 18:15
 * @copyright(C) 2017 song
 */
public class IdentityCardHandler {
    private static final String TAG = "IdentityCardHandler";

    private static final String FRONT_IMAGE_PATH = "res/identity_card/front_image.jpg";
    private static final String BACK_IMAGE_PATH = "res/identity_card/back_image.jpg";
    private static volatile IdentityCardHandler identityCardHandler = null;

    /**
     * 证件宽
     */
    private static final int MAP_WIDTH = 390;
    /**
     * 证件高
     */
    private static final int MAP_HEIGHT = 246;
    private static final int MAP_DIV1000 = (int) (MAP_WIDTH * 0.01);

    /**
     * 多行行间距
     */
    private static final int ROW_SIZE = (int) (MAP_WIDTH * 0.0432);
    /**
     * 每行开始X坐标
     */
    private static final int START_UP = (int) (MAP_WIDTH * 0.1837);
    /**
     * 姓名的Y坐标
     */
    private static final int NAME_Y = (int) (MAP_HEIGHT * 0.2102);
    /**
     * 性别的Y坐标
     */
    private static final int SEX_Y = (int) (MAP_HEIGHT * 0.3299);
    /**
     * 民族的X坐标
     */
    private static final int NATION_X = (int) (MAP_WIDTH * 0.4012);
    /**
     * 民族的Y坐标
     */
    private static final int NATION_Y = (int) (MAP_HEIGHT * 0.3299);
    /**
     * 生日的Y坐标
     */
    private static final int BIRTH_Y = (int) (MAP_HEIGHT * 0.4512);
    /**
     * 年
     */
    private static final int YX = (int) (MAP_WIDTH * 0.1837);
    /**
     * 月
     */
    private static final int MX = (int) (MAP_WIDTH * 0.3437);
    /**
     * 日
     */
    private static final int DX = (int) (MAP_WIDTH * 0.4337);
    private int m_MWith;
    private int m_DWith;

    /**
     * 地址的Y坐标
     */
    private static final int ADDRESS_Y = (int) (MAP_HEIGHT * 0.5906);
    /**
     * 身份证号码的X坐标
     */
    private static final int ID_NO_X = (int) (MAP_WIDTH * 0.3612);
    /**
     * 身份证号码的Y坐标
     */
    private static final int ID_NO_Y = (int) (MAP_HEIGHT * 0.9112);

    /**
     * 反面的发证机关X坐标
     */
    private static final int BACK_X1 = (int) (MAP_WIDTH * 0.4012);
    /**
     * 反面的发证机关Y坐标
     */
    private static final int BACK_Y1 = (int) (MAP_HEIGHT * 0.7540);
    /**
     * 反面的有效日期X坐标
     */
    private static final int BACK_X2 = (int) (MAP_WIDTH * 0.4012);
    /**
     * 反面的有效日期Y坐标
     */
    private static final int BACK_Y2 = (int) (MAP_HEIGHT * 0.8888);

    /**
     * 头像x坐标
     */
    public static final int IMAGE_X = (int) (MAP_WIDTH * 0.6270);
    /**
     * 头像y坐标
     */
    public static final int IMAGE_Y = (int) (MAP_HEIGHT * 0.2564);
    /**
     * 390 保存图片高度
     */
    private static final int SAVE_IMAGE_WIDTH = 390;
    /**
     * 246 保存图片宽度
     */
    private static final int SAVE_IMAGE_HEIGHT = 246;

    /**
     * 保存图片质量
     */
    private static final int SAVE_QUALITY = 90;

    /**
     * 文字颜色
     */
    private static final int TEXT_COLOR = Color.rgb(30, 30, 30);
    /**
     * 正面位图
     */
    private Bitmap frontBitmap;//
    /**
     * 正面画布
     */
    private Canvas frontCanvas;
    /**
     * 反面位图
     */
    private Bitmap backBitmap;
    /**
     * 反面画布
     */
    private Canvas backCanvas;

    /**
     * 头像位图
     */
    private Bitmap headBitmap;

    /**
     * 完整的位图
     */
    private Bitmap fullBitmap;

    /**
     * 文本画笔
     */
    private Paint fontPaint;
    private Context context;

    /**
     * 合成标志,false不合成身份证正反面(仅输出基本信息)，true合成身份证正反面，默认true
     */
    private boolean compoundFlag = true;

    /**
     * true输出合成文件到sd卡，false不输出，默认false
     */
    private boolean outSdCardFlag = false;

    /**
     * 默认false,false头像带有背景色，true去掉白色背景
     */
    private boolean drawBg4 = false;


    /**
     * 私有
     */
    private IdentityCardHandler(Context context) {
        this.context = context.getApplicationContext();
        initFontPaint();
    }

    /**
     * 单例实例
     *
     * @return
     */
    public static IdentityCardHandler getInstance(Context context) {
        if (identityCardHandler == null) {
            synchronized (IdentityCardHandler.class) {
                if (identityCardHandler == null) {
                    identityCardHandler = new IdentityCardHandler(context);
                }
            }
        }
        return identityCardHandler;
    }



    /**
     * 初始化字体画笔
     */
    private void initFontPaint() {
        fontPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DEV_KERN_TEXT_FLAG);
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "res/fonts/cert.TTF");
        fontPaint.setTypeface(typeface);// 设置字体类型
        fontPaint.setColor(TEXT_COLOR);// 字体颜色

    }

    /**
     * 配置合成参数
     *
     * @param compoundFlag  false不合成身份证正反面(仅输出基本信息)，true合成身份证正反面，默认true
     * @param outSdCardFlag true输出合成文件到sd卡，false不输出，默认false
     * @return
     */
    public IdentityCardHandler configParams(boolean compoundFlag, boolean outSdCardFlag) {
        this.compoundFlag = compoundFlag;
        this.outSdCardFlag = outSdCardFlag;
        return this;
    }


    /**
     * 合成身份证（仅输出正反面数据）
     * <p style="color:red;">输出的本地图片（路径：/storage/emulated/0/应用包名/identitycard/）及位图都是原始身份证标准宽高,如果需要在xml布局显示，可以对位图进行一定比例缩放</p>
     *
     * @param identityCard     身份证实体
     * @param compoundListener 合成回调
     */
    public void compound(IdentityCard identityCard, CompoundListener compoundListener) {
        compound(false, identityCard, compoundListener);
    }

    /**
     * 合成身份证
     *
     * @param mergeBitmapFlag  合成位图标志，false不合成，输出正反面数据;true身份证正反面合成一页， 输出全量数据
     * @param identityCard     身份证实体
     * @param compoundListener 合成回调
     */
    public void compound(final boolean mergeBitmapFlag, final IdentityCard identityCard, final CompoundListener compoundListener) {
        if (identityCard == null) {
            if (compoundListener != null) {
                compoundListener.onFailed(new Exception("identityCard is null."));
            }
        }
        resetFontPaint();
        new AsyncTask<IdentityCard, Void, Map<String, Object>>() {
            @Override
            protected void onPreExecute() {
                if (compoundListener != null) {
                    compoundListener.onStart();
                }
            }

            @Override
            protected Map<String, Object> doInBackground(IdentityCard... params) {
                Map<String, Object> result = new HashMap<String, Object>();
                if (params.length == 1) {
                    IdentityCard temp = params[0];
                    try {
                        if (!compoundFlag) {
                            String headImage = buildHeadImage(identityCard);
                            if (headImage == null) {
                                result.put("code", "-1");
                                result.put("msg", "headImage is null.");
                                return result;
                            }
                            temp.setHeadImageBase64(headImage);
                        } else {
                            String headImage = buildHeadImage(identityCard);
                            String frontImage = buildFrontImage(identityCard);
                            String backImage = buildBackImage(identityCard);
                            if (headImage == null) {
                                Log.i(TAG, "头像headImage为空");
                                result.put("code", "-1");
                                result.put("msg", "headImage is null.");
                                return result;
                            }
                            if (frontImage == null) {
                                Log.i(TAG, "正面frontImage身份证为空");
                                result.put("code", "-1");
                                result.put("msg", "frontImage  is null.");
                                return result;
                            }
                            if (backImage == null) {
                                Log.i(TAG, "反面backImage身份证为空");
                                result.put("code", "-1");
                                result.put("msg", "backImage is null.");
                                return result;
                            }
                            temp.setHeadImageBase64(headImage);
                            temp.setFrontImageBase64(frontImage);
                            temp.setBackImageBase64(backImage);
                            if (mergeBitmapFlag) {
                                temp.setHeadImageBase64(headImage);
                                temp.setFrontImageBase64(frontImage);
                                temp.setBackImageBase64(backImage);
                                fullBitmap = mergeBitmap(temp.getFrontBitmap(), temp.getBackBitmap());
                                String fullBase64 = BitmapUtils.bitmapToBase64(fullBitmap, 100);
                                identityCard.setFullBitmap(fullBitmap);
                                identityCard.setFullBitmapBase64(fullBase64);
                            }
                        }
                        result.put("code", "0");
                        result.put("identityCard", temp);
                    } catch (Exception e) {
                        result.put("code", "-1");
                        result.put("msg", e.getMessage());
                    }
                }
                return result;
            }

            @Override
            protected void onPostExecute(Map<String, Object> result) {
                Object code = result.get("code");
                if ("0".equals(code)) {
                    if (compoundListener != null) {
                        compoundListener.onSuccess((IdentityCard) result.get("identityCard"));
                    }
                } else {
                    if (compoundListener != null) {
                        compoundListener.onFailed(new Exception(result.get("msg").toString()));
                    }
                }
            }

        }.execute(identityCard);

    }


    /**
     * 重置字体画笔
     */
    private void resetFontPaint() {
        fontPaint.setTextSize(15);// 字体大小
    }


    /**
     * 清空画布
     */
    private void clearCanvas() {
        if (frontCanvas != null) {
            frontCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        }
        if (backCanvas != null) {
            backCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        }
    }


    /**
     * 获取身份证头像
     *
     * @param identityCard 身份证实体
     * @return
     */
    private String buildHeadImage(IdentityCard identityCard) {
        if (identityCard.getHeadImageByte() != null) {
            headBitmap = removeHeadBg(identityCard.getHeadImageByte(), IMAGE_X, IMAGE_Y);
        } else if (identityCard.getHeadImage() != null) {
            headBitmap = removeHeadBg(identityCard.getHeadImage(), IMAGE_X, IMAGE_Y);
            setDrawBg4(true);//需要加这个
        }
        if (headBitmap != null) {
            String headImageBase64 = BitmapUtils.bitmapToBase64(headBitmap, 100);
            saveBitmap(headBitmap, "head.bmp", outSdCardFlag);
            return headImageBase64;
        }

        return null;
    }


    /**
     * 生成正面图像
     *
     * @param identityCard 身份证实体
     * @return
     */
    private String buildFrontImage(IdentityCard identityCard) {
        String name = identityCard.getName();
        String sex = identityCard.getSex();
        String nation = identityCard.getNation();
        String birthday = identityCard.getBirthday();
        String address = identityCard.getAddress();
        String idCardNo = identityCard.getIdCardNo();
        Bitmap photo = decodeStream(FRONT_IMAGE_PATH);
        if (photo == null) {
            throw new NullPointerException("front template is null.");
        }
        int width = photo.getWidth(), height = photo.getHeight();
        frontBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888); // 建立一个空的BItMap
        frontCanvas = new Canvas(frontBitmap);
        Paint photoPaint = new Paint();
        photoPaint.setDither(true); // 设置防抖动
        photoPaint.setFilterBitmap(true);
        frontCanvas.drawBitmap(photo, 0, 0, photoPaint);
        drawName(name);
        drawSex(sex);
        drawNation(nation);
        drawBirthday(formatBirthday(birthday));
        drawAddress(address);
        drawIDCardNo(idCardNo);
        drawHeadImg(headBitmap, photoPaint);
        identityCard.setFrontBitmap(frontBitmap);
        saveBitmap(frontBitmap, "frontImg.jpg", outSdCardFlag);
        return BitmapUtils.bitmapToBase64(frontBitmap, 100);
    }

    /**
     * 生成反面图像
     *
     * @param identityCard 身份证实体
     * @return
     */
    private String buildBackImage(IdentityCard identityCard) {
        String police = identityCard.getPolice();
        String expiryDate = identityCard.getExpiryDate();
        String fullExpiryDate;
        if (!expiryDate.contains("长期")){
            if (expiryDate.length() != 16) {
                Log.w(TAG, "有效日期不符合长度不对或格式非法");
                throw new IllegalArgumentException("expiryDate is an incorrect length or invalid format.");
            }
            String beginDate = expiryDate.substring(0, 8);
            String endDate = expiryDate.substring(8, expiryDate.length());
            fullExpiryDate = formatExpiryDate(beginDate) + "-" + formatExpiryDate(endDate);
        }else {
            if (expiryDate.length() != 10) {
                Log.w(TAG, "有效日期不符合长度不对或格式非法");
                throw new IllegalArgumentException("expiryDate is an incorrect length or invalid format.");
            }
            String beginDate = expiryDate.substring(0, 8);
            fullExpiryDate = formatExpiryDate(beginDate) + "-长期";
        }

        Bitmap photo = decodeStream(BACK_IMAGE_PATH);
        if (photo == null) {
            throw new NullPointerException("back template is null.");
        }
        int width = photo.getWidth(), height = photo.getHeight();
        backBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888); // 建立一个空的BItMap
        backCanvas = new Canvas(backBitmap);
        Paint photoPaint = new Paint();
        photoPaint.setDither(true);// 设置防抖动
        photoPaint.setFilterBitmap(true);
        backCanvas.drawBitmap(photo, 0, 0, photoPaint);
        drawPolice(police);
        drawExpiryDate(fullExpiryDate);
        identityCard.setBackBitmap(backBitmap);
        saveBitmap(backBitmap, "backImg.jpg", outSdCardFlag);
        return BitmapUtils.bitmapToBase64(backBitmap, 100);
    }

    /**
     * 释放内存
     */
    public void release() {
        if (frontBitmap != null && !frontBitmap.isRecycled()) { // 先判断是否已经回收
            frontBitmap.recycle();
            frontBitmap = null;
        }
        if (backBitmap != null && !backBitmap.isRecycled()) { // 先判断是否已经回收
            backBitmap.recycle();
            backBitmap = null;
        }
        if (headBitmap != null && !headBitmap.isRecycled()) { // 先判断是否已经回收
            headBitmap.recycle();
            headBitmap = null;
        }
        if (fullBitmap != null && !fullBitmap.isRecycled()) { // 先判断是否已经回收
            fullBitmap.recycle();
            fullBitmap = null;
        }

    }


    /**
     * 避免不同分辨率缩放，模板图片必须放到assets目录下
     *
     * @param fileName 文件名
     * @return
     */
    private Bitmap decodeStream(String fileName) {
        InputStream inputStream;
        try {
            inputStream = context.getAssets().open(fileName);
            return BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            Log.e(TAG, "获取资源图片异常" + fileName, e);
        }
        return null;
    }

    /**
     * 绘制签发机关
     *
     * @param police 签发机关
     */
    private void drawPolice(String police) {
        backCanvas.drawText(police, BACK_X1, BACK_Y1, fontPaint);
    }

    /**
     * 绘制有效期限
     *
     * @param fullExpiryDate 有效期限
     */
    private void drawExpiryDate(String fullExpiryDate) {
        backCanvas.drawText(fullExpiryDate, BACK_X2, BACK_Y2, fontPaint);
    }

    /**
     * 格式化有效期限
     *
     * @param date 日期
     * @return
     */
    private static String formatExpiryDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date tempDate = null;
        try {
            tempDate = sdf.parse(date);
        } catch (ParseException e) {
            System.out.println(e);
        }
        sdf = new SimpleDateFormat("yyyy.MM.dd");
        return sdf.format(tempDate);
    }

    /**
     * 格式化出生年月日
     *
     * @param birthday 出生年月日
     * @return
     */
    private String[] formatBirthday(String birthday) {
        String[] str = new String[3];
        if (TextUtils.isEmpty(birthday)) {
            return str;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(sdf.parse(birthday));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        str[0] = String.valueOf(calendar.get(Calendar.YEAR));// 年
        str[1] = String.valueOf(calendar.get(Calendar.MONTH) + 1);// 月
        str[2] = String.valueOf(calendar.get(Calendar.DATE));// 日
        return str;
    }

    /**
     * 绘制头像
     *
     * @param headImg    头像位图
     * @param photoPaint 画笔
     */
    private void drawHeadImg(Bitmap headImg, Paint photoPaint) {
        if (headImg != null) {
            int posX, posY;
            posX = IMAGE_X;
            posY = IMAGE_Y;
            if (drawBg4) {
                headImg = drawBg4Bitmap(headImg);
            }
            frontCanvas.drawBitmap(headImg, posX, posY, photoPaint);
            drawBg4 = false;//恢复默认
        }
    }

    /**
     * 修复drawBitmap后背景透明边黑色问题
     *
     * @param originBitmap 原始位图
     * @return
     */
    private Bitmap drawBg4Bitmap(Bitmap originBitmap) {
        Paint paint = new Paint();
        Bitmap bitmap = Bitmap.createBitmap(originBitmap.getWidth(),
                originBitmap.getHeight(), originBitmap.getConfig());
        Canvas canvas = new Canvas(bitmap);
        canvas.drawRect(0, 0, originBitmap.getWidth(), originBitmap.getHeight(), paint);
        canvas.drawBitmap(originBitmap, 0, 0, paint);
        return bitmap;
    }


    /**
     * 绘制身份证号码
     *
     * @param idCardNo 份证号码
     */
    private void drawIDCardNo(String idCardNo) {
        fontPaint.setTextSize(18);
        frontCanvas.drawText(idCardNo, ID_NO_X, ID_NO_Y, fontPaint);
    }


    /**
     * 绘制地址
     *
     * @param address 地址
     */
    private void drawAddress(String address) {
        // 换行显示住址
        int pointy = ADDRESS_Y;
        String csRow;
        String csPro = address;
        int iLen = address.length();
        char[] addressArr = address.toCharArray();
        int iRowFlag = 0;

        int startIndex = 0;
        int endIndex = 0;
        for (int i = 0; i < iLen; i++) {
            char ch = addressArr[i];
            if (isChinese(ch)) {
                iRowFlag += 2;// 中文加2个
            } else {
                iRowFlag += 1;// 非中文加1个
            }

            boolean bPrint = false;
            if (iRowFlag == 21) {
                if (isChinese(ch)) {
                    bPrint = true;// 长度是21，是中文换行
                } else {
                    char ch2 = addressArr[i + 1];
                    if (i < iLen - 1 && isChinese(ch2)) {
                        bPrint = true;// 长度是21，下一个是中文换行
                    }
                }
            } else if (iRowFlag == 22) {
                bPrint = true;// 长度是22，换行
            }

            if (bPrint == true) {
                endIndex = i + 1;
                csRow = csPro.substring(startIndex, endIndex);
                Log.i(TAG, "地址：" + csRow);
                frontCanvas.drawText(csRow, START_UP, pointy, fontPaint);
                pointy += ROW_SIZE;
                iRowFlag = 0;
                startIndex = endIndex;
                continue;
            }
        }
        int surplusLength = iLen - endIndex;
        if (surplusLength > 0) {
            csPro = csPro.substring(endIndex, iLen);
            if (csPro.length() > 0) {
                frontCanvas.drawText(csPro, START_UP, pointy, fontPaint);
//                pointy += ROW_SIZE;
            }
        }

    }

    /**
     * 判断一个字符是否是中文
     *
     * @param c 字符
     * @return
     */
    private boolean isChinese(char c) {
        return c >= 0x4E00 && c <= 0x9FA5;// 根据字节码判断
    }

    /**
     * 绘制出生年月日
     *
     * @param birthday ymd
     */
    private void drawBirthday(String[] birthday) {
        m_MWith = MX;
        m_DWith = DX;
        if (birthday[1].length() == 1) {
            m_MWith += MAP_DIV1000;
        }

        if (birthday[2].length() == 1) {
            m_DWith += MAP_DIV1000;
        }
        frontCanvas.drawText(birthday[0], YX, BIRTH_Y, fontPaint);// 年
        frontCanvas.drawText(birthday[1], m_MWith, BIRTH_Y, fontPaint);// 月
        frontCanvas.drawText(birthday[2], m_DWith, BIRTH_Y, fontPaint);// 日

    }

    /**
     * 绘制民族
     *
     * @param nation 民族
     */
    private void drawNation(String nation) {
        frontCanvas.drawText(nation, NATION_X, NATION_Y, fontPaint);
    }

    /**
     * 绘制性别
     *
     * @param sex
     */
    private void drawSex(String sex) {
        frontCanvas.drawText(sex, START_UP, SEX_Y, fontPaint);
    }

    /**
     * 绘制姓名
     *
     * @param name 姓名
     */
    private void drawName(String name) {
        // if (name.length() < 5) {
        // name = name.substring(0, 2) + " " + name.substring(2, name.length());
        // }
        frontCanvas.drawText(name, START_UP, NAME_Y, fontPaint);
    }

    /**
     * 保存图片到SD卡上
     *
     * @param bitmap   位图
     * @param fileName 文件名
     * @param outFlag  输出标志
     */
    protected void saveBitmap(Bitmap bitmap, String fileName, boolean outFlag) {
        if (outFlag) {
            try {
                String outFilePath = Environment.getExternalStorageDirectory() + File.separator + context.getPackageName()
                        + File.separator + "identitycard";
                File dir = new File(outFilePath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File file = new File(outFilePath + File.separator + fileName);
                if (file.exists()) {
                    file.delete();
                }
                FileOutputStream stream = new FileOutputStream(file);
                bitmap.compress(CompressFormat.JPEG, SAVE_QUALITY, stream);
                stream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    /**
     * 头像背景透明处理
     *
     * @param colors 头像颜色数组
     * @param pointX x坐标
     * @param pointY y坐标
     * @return
     */
    private Bitmap removeHeadBg(int[] colors, int pointX, int pointY) {
        Bitmap headBitmap = Bitmap.createBitmap(colors, 102, 126, Config.ARGB_8888);
        return removeHeadBg(headBitmap, pointX, pointY);

    }

    /**
     * 头像背景透明处理
     *
     * @param headBitmap 头像位图
     * @param pointX     x坐标
     * @param pointY     y坐标
     * @return
     */
    private Bitmap removeHeadBg(Bitmap headBitmap, int pointX, int pointY) {
        int imgWidth = SAVE_IMAGE_WIDTH;
        int imgHeight = SAVE_IMAGE_HEIGHT;
        boolean mutable = headBitmap.isMutable();
        if (mutable) {
            Log.i(TAG, "可修改");
        } else {
            Log.i(TAG, "不可修改");
        }
        Bitmap tempBitmap = headBitmap.copy(Bitmap.Config.ARGB_8888, true);// 可修改的
        int bitmap_w = tempBitmap.getWidth();
        int bitmap_h = tempBitmap.getHeight();

        if ((imgWidth - pointX) < bitmap_w) {
            bitmap_w = imgWidth - pointX;
        }

        if ((imgHeight - pointY) < bitmap_h) {
            bitmap_h = imgHeight - pointY;
        }

        for (int i = 0; i < bitmap_h; i++) {
            for (int j = 0; j < bitmap_w; j++) {

                int color = tempBitmap.getPixel(j, i);// 获得Bitmap 图片中每一个点的color颜色值
                int r = Color.red(color);
                int g = Color.green(color);
                int b = Color.blue(color);
                int a = Color.alpha(color);
                if ((r + g + b) >= 740) {
                    r = 0; // 完全透明
                    g = 0;
                    b = 0;
                    a = 0;
                }
                color = Color.argb(a, r, g, b);
                tempBitmap.setPixel(j, i, color);
            }
        }

        if (!headBitmap.isRecycled()) {
            headBitmap.recycle();
        }
        return tempBitmap;
    }


    /**
     * 合并位图
     *
     * @param firstBitmap  第一个位图
     * @param secondBitmap 第二个位图
     * @return
     */
    private Bitmap mergeBitmap(Bitmap firstBitmap, Bitmap secondBitmap) {
        Bitmap bitmap = Bitmap.createBitmap(firstBitmap.getWidth(), firstBitmap.getHeight() + 20 + secondBitmap.getHeight(), firstBitmap.getConfig());
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(firstBitmap, new Matrix(), null);
        canvas.drawBitmap(secondBitmap, 0, firstBitmap.getHeight() + 10, null);
        return bitmap;
    }

    private void setDrawBg4(boolean drawBg4) {
        this.drawBg4 = drawBg4;
    }


}
