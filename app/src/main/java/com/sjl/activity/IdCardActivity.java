package com.sjl.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.sjl.idcard.IdentityCardHandler;
import com.sjl.idcard.entity.IdentityCard;
import com.sjl.idcard.listener.CompoundListener;
import com.sjl.idcard.test.R;
import com.sjl.idcard.util.BitmapUtils;
import com.sjl.idcard.util.ByteUtils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


/**
 * @author song
 */
public class IdCardActivity extends AppCompatActivity {
    private static final String TAG = "IdCardActivity";

    ImageView iv_front, iv_back, iv_full;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_utils_activity);
        iv_front = findViewById(R.id.iv_front);
        iv_back = findViewById(R.id.iv_back);
        iv_full = findViewById(R.id.iv_full);
    }


    public void compoundTest(View view) {
        IdentityCard identityCard = getIDCardInfo();
        IdentityCardHandler identityCardHandler = IdentityCardHandler.getInstance(this);
        identityCardHandler.compound(identityCard, new CompoundListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(IdentityCard identityCard) {
                Bitmap frontBitmap = identityCard.getFrontBitmap();
                Bitmap backBitmap = identityCard.getBackBitmap();
//                String frontImageBase64 = identityCard.getFrontImageBase64();
//                String backImageBase64 = identityCard.getBackImageBase64();
                iv_front.setImageBitmap(BitmapUtils.scaleBitmap(frontBitmap, 1.2f));

                iv_back.setImageBitmap(BitmapUtils.scaleBitmap(backBitmap, 1.2f));

            }

            @Override
            public void onFailed(Throwable t) {
                Log.e(TAG, "身份证合成失败", t);
            }
        });
    }

    public void compoundTest2(View view) {
        IdentityCard identityCard = getIDCardInfo();
        IdentityCardHandler identityCardHandler = IdentityCardHandler.getInstance(this);
        identityCardHandler.compound(true,identityCard, new CompoundListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(IdentityCard identityCard) {
//                Bitmap frontBitmap = identityCard.getFrontBitmap();
//                Bitmap backBitmap = identityCard.getBackBitmap();
//                String frontImageBase64 = identityCard.getFrontImageBase64();
//                String backImageBase64 = identityCard.getBackImageBase64();

                Bitmap fullBitmap = identityCard.getFullBitmap();
//                String fullBitmapBase64 = identityCard.getFullBitmapBase64();
                iv_full.setImageBitmap(BitmapUtils.scaleBitmap(fullBitmap, 1.2f));
            }

            @Override
            public void onFailed(Throwable t) {
                Log.e(TAG, "身份证合成失败", t);
            }
        });
    }


    /**
     * 获取身份证基本信息
     *
     * @return
     */
    private IdentityCard getIDCardInfo() {
        //正面
        String name = "宋某某";
        String sex = "男";
        String nation = "汉";
        String birthday = "19921015";
        String address = "广西北流市新荣镇毓山村塘边组10号";
        String idCardNo = "450981199210150412";
        Bitmap headImg = headImgToBitmap();
        //反面
        String police = "北流市公安局";
        String expiryDate = "2011012620210126";
        IdentityCard identityCard = new IdentityCard(name, sex, nation, birthday, address, idCardNo,
                police, expiryDate, headImg);
        return identityCard;
    }

    private Bitmap headImgToBitmap() {
        InputStream inputStream = BitmapUtils.openAssets(this, "headdata.txt");
        String content = readFile(inputStream);
        byte[] bytes = ByteUtils.hexStringToByteArr(content);
        Bitmap bitmap = BitmapUtils.byteArrToBitmap(bytes);
        return bitmap;
    }

    private String readFile(InputStream inputStream) {
        BufferedReader reader = null;
        String str;
        StringBuilder sb = new StringBuilder();
        try {
            InputStreamReader inputReader = new InputStreamReader(inputStream);
            reader = new BufferedReader(inputReader);
            while ((str = reader.readLine()) != null) {
                sb.append(str);
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        IdentityCardHandler.getInstance(this).release();
    }


}
