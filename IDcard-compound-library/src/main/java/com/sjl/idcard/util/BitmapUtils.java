package com.sjl.idcard.util;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Build;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * 位图操作工具类
 *
 *
 * @author Kelly
 * @version 1.0.0
 * @filename BitmapUtils.java
 * @time 2017/4/13 18:13
 * @copyright(C) 2017 song
 */
public class BitmapUtils {
	/**
	 * bitmap转为base64
	 *
	 * @param bitmap
	 * @return
	 */
	public static String bitmapToBase64(Bitmap bitmap, int quality) {
		String result = null;
		ByteArrayOutputStream baos = null;
		try {
			if (bitmap != null) {
				baos = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
				baos.flush();
				baos.close();
				byte[] bitmapBytes = baos.toByteArray();
				result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (baos != null) {
					baos.flush();
					baos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * base64转为bitmap
	 *
	 * @param base64Data
	 * @return
	 */
	public static Bitmap base64ToBitmap(String base64Data) {
		byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
		return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
	}

	/**
	 * 把bitmap转成图片输出到指定SD卡路径(常规方法)
	 *
	 * @param bitmap
	 *            位图
	 * @param outFilePath
	 *            输出文件路径
	 * @param fileName
	 *            文件名
	 * @param quality
	 *            压缩质量
	 */
	public static void saveBitmapToFile(Bitmap bitmap, String outFilePath, String fileName, int quality) {
		File dir = new File(outFilePath);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		try {
			File file = new File(dir.getAbsolutePath() + File.separator + fileName);
			if (file.exists()) {// 存在删除
				file.delete();
			}
			FileOutputStream out = new FileOutputStream(file);
			String suffix = fileName.substring(fileName.lastIndexOf(".") + 1).toUpperCase();
			if (!suffix.contains("PNG")) {// 常用这两种格式
				bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out);
			} else {
				bitmap.compress(Bitmap.CompressFormat.PNG, quality, out);
			}
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * 把bitmap转成图片输出到指定SD卡路径
	 *
	 * @param bitmap
	 *            位图
	 * @param filePath
	 *            文件路径
	 * @param quality
	 *            压缩质量
	 */
	public static void saveBitmapToFile(Bitmap bitmap, String filePath, int quality) {
		try {
			File file = new File(filePath);
			if (file.exists()) {// 存在删除
				file.delete();
			}
			FileOutputStream out = new FileOutputStream(file);
			String suffix = filePath.substring(filePath.lastIndexOf(".") + 1).toUpperCase();
			if (!suffix.contains("PNG")) {// 常用这两种格式
				bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out);
			} else {
				bitmap.compress(Bitmap.CompressFormat.PNG, quality, out);
			}
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}



	/**
	 * 获取Bitmap任意透明度
	 *
	 * @param sourceImg
	 * @param number
	 *            number的范围是0-100，0表示完全透明即完全看不到
	 * @return
	 */
	public static Bitmap getTransparentBitmap(Bitmap sourceImg, int number) {
		int[] argb = new int[sourceImg.getWidth() * sourceImg.getHeight()];

		sourceImg.getPixels(argb, 0, sourceImg.getWidth(), 0, 0, sourceImg

				.getWidth(), sourceImg.getHeight());// 获得图片的ARGB值
		number = number * 255 / 100;
		for (int i = 0; i < argb.length; i++) {
			argb[i] = (number << 24) | (argb[i] & 0x00FFFFFF);
		}
		sourceImg = Bitmap.createBitmap(argb, sourceImg.getWidth(), sourceImg

				.getHeight(), Config.ARGB_8888);
		return sourceImg;
	}

	/**
	 * 根据给定的宽和高进行拉伸
	 *
	 * @param origin
	 *            原图
	 * @param newWidth
	 *            新图的宽
	 * @param newHeight
	 *            新图的高
	 * @return new Bitmap
	 */
	public static Bitmap scaleBitmap(Bitmap origin, int newWidth, int newHeight) {
		if (origin == null) {
			return null;
		}
		int height = origin.getHeight();
		int width = origin.getWidth();
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, true);
		if (origin.equals(newBM)) {
			return newBM;
		}
		if (!origin.isRecycled()) {
			origin.recycle();
		}
		return newBM;
	}

	/**
	 * 根据指定路径压缩图片
	 *
	 * @param imagePath
	 * @return
	 */
	public static Bitmap compressImg(String imagePath, int newWidth, int newHeight) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true; // 只获取图片的大小信息
		BitmapFactory.decodeFile(imagePath, options);
		int height = options.outHeight;
		int width = options.outWidth;
		int inSampleSize = 1;
		if (height > newHeight || width > newWidth) {
			int hRatio = Math.round((float) height / (float) newHeight);
			int wRatio = Math.round((float) width / (float) newWidth);
			inSampleSize = Math.min(hRatio, wRatio);
		}
		options.inSampleSize = inSampleSize;
		options.inJustDecodeBounds = false;
		Bitmap bm = BitmapFactory.decodeFile(imagePath, options);
		return bm;
	}

	/**
	 * 等比例缩放Bitmap
	 *
	 * @param origin
	 * @param newWidth
	 * @param newHeight
	 * @return
	 */
	public static Bitmap scaleBitmapByScale(Bitmap origin, int newWidth, int newHeight) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		origin.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		newOpts.inJustDecodeBounds = true;
		Bitmap newBitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		float hh = newHeight;
		float ww = newWidth;
		int inSampleSize = 1;// be=1表示不缩放
		if (w > h && w > ww) {// 只用高或者宽其中一个数据进行计算即可
			inSampleSize = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {
			inSampleSize = (int) (newOpts.outHeight / hh);
		}
		if (inSampleSize <= 0)
			inSampleSize = 1;
		newOpts.inSampleSize = inSampleSize;// 设置缩放比例
		isBm = new ByteArrayInputStream(baos.toByteArray());
		newBitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		if (origin.equals(newBitmap)) {
			return newBitmap;
		}
		if (!origin.isRecycled()) {
			origin.recycle();
		}
		return newBitmap;
	}


	/**
	 * 按比例缩放图片
	 *
	 * @param origin
	 *            原图
	 * @param ratio
	 *            比例,比例越小图片越小
	 * @return 新的bitmap
	 */
	public static Bitmap scaleBitmap(Bitmap origin, float ratio) {
		if (origin == null) {
			return null;
		}
		int width = origin.getWidth();
		int height = origin.getHeight();
		Matrix matrix = new Matrix();
		matrix.preScale(ratio, ratio);
		Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, true);
		if (origin.equals(newBM)) {
			return newBM;
		}
		if (!origin.isRecycled()) {
			origin.recycle();
		}
		return newBM;
	}

	/**
	 * 偏移效果
	 *
	 * @param origin
	 *            原图
	 * @return 偏移后的bitmap
	 */
	public static Bitmap skewBitmap(Bitmap origin) {
		if (origin == null) {
			return null;
		}
		int width = origin.getWidth();
		int height = origin.getHeight();
		Matrix matrix = new Matrix();
		matrix.postSkew(-0.6f, -0.3f);
		Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, true);
		if (origin.equals(newBM)) {
			return newBM;
		}
		if (!origin.isRecycled()) {
			origin.recycle();
		}
		return newBM;
	}

	/**
	 * 得到bitmap的大小
	 */
	public static int getBitmapSize(Bitmap bitmap) {
		int sdkInt = Build.VERSION.SDK_INT;
		if (sdkInt >= Build.VERSION_CODES.KITKAT) { // API 19 Android 4.4
			return bitmap.getAllocationByteCount();
		}
		if (sdkInt >= Build.VERSION_CODES.HONEYCOMB_MR1) {// API 12 Android 3.1
			return bitmap.getByteCount();
		}
		return bitmap.getRowBytes() * bitmap.getHeight();// 更低版本
	}

	//以下方法来源开源库

	/**
	 * 逐行扫描 清除边界空白
	 *
	 * @param bp
	 * @param blank 边距留多少个像素
	 * @return
	 */
	public static Bitmap clearBitmapBlank(Bitmap bp, int blank) {
		int HEIGHT = bp.getHeight();
		int WIDTH = bp.getWidth();
		int top = 0, left = 0, right = 0, bottom = 0;
		int[] pixs = new int[WIDTH];
		boolean isStop;
		for (int y = 0; y < HEIGHT; y++) {
			bp.getPixels(pixs, 0, WIDTH, 0, y, WIDTH, 1);
			isStop = false;
			for (int pix : pixs) {
				if (pix != Color.WHITE) {
					top = y;
					isStop = true;
					break;
				}
			}
			if (isStop) {
				break;
			}
		}
		for (int y = HEIGHT - 1; y >= 0; y--) {
			bp.getPixels(pixs, 0, WIDTH, 0, y, WIDTH, 1);
			isStop = false;
			for (int pix : pixs) {
				if (pix != Color.WHITE) {
					bottom = y;
					isStop = true;
					break;
				}
			}
			if (isStop) {
				break;
			}
		}
		pixs = new int[HEIGHT];
		for (int x = 0; x < WIDTH; x++) {
			bp.getPixels(pixs, 0, 1, x, 0, 1, HEIGHT);
			isStop = false;
			for (int pix : pixs) {
				if (pix != Color.WHITE) {
					left = x;
					isStop = true;
					break;
				}
			}
			if (isStop) {
				break;
			}
		}
		for (int x = WIDTH - 1; x > 0; x--) {
			bp.getPixels(pixs, 0, 1, x, 0, 1, HEIGHT);
			isStop = false;
			for (int pix : pixs) {
				if (pix !=  Color.WHITE) {
					right = x;
					isStop = true;
					break;
				}
			}
			if (isStop) {
				break;
			}
		}
		if (blank < 0) {
			blank = 0;
		}
		left = left - blank > 0 ? left - blank : 0;
		top = top - blank > 0 ? top - blank : 0;
		right = right + blank > WIDTH - 1 ? WIDTH - 1 : right + blank;
		bottom = bottom + blank > HEIGHT - 1 ? HEIGHT - 1 : bottom + blank;
		return Bitmap.createBitmap(bp, left, top, right - left, bottom - top);
	}

	/**
	 * 图像灰度化
	 * @param bmSrc
	 * @return
	 */
	public static Bitmap bitmap2Gray(Bitmap bmSrc) {
		// 得到图片的长和宽
		int width = bmSrc.getWidth();
		int height = bmSrc.getHeight();
		// 创建目标灰度图像
		Bitmap bmpGray = null;
		bmpGray = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		// 创建画布
		Canvas c = new Canvas(bmpGray);
		Paint paint = new Paint();
		ColorMatrix cm = new ColorMatrix();
		cm.setSaturation(0);
		ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
		paint.setColorFilter(f);
		c.drawBitmap(bmSrc, 0, 0, paint);
		return bmpGray;
	}

	/**
	 * 对图像进行线性灰度变化
	 * @param image
	 * @return
	 */
	public static Bitmap lineGrey(Bitmap image) {
		//得到图像的宽度和长度
		int width = image.getWidth();
		int height = image.getHeight();
		//创建线性拉升灰度图像
		Bitmap linegray = null;
		linegray = image.copy(Config.ARGB_8888, true);
		//依次循环对图像的像素进行处理
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				//得到每点的像素值
				int col = image.getPixel(i, j);
				int alpha = col & 0xFF000000;
				int red = (col & 0x00FF0000) >> 16;
				int green = (col & 0x0000FF00) >> 8;
				int blue = (col & 0x000000FF);
				// 增加了图像的亮度
				red = (int) (1.1 * red + 30);
				green = (int) (1.1 * green + 30);
				blue = (int) (1.1 * blue + 30);
				//对图像像素越界进行处理
				if (red >= 255)
				{
					red = 255;
				}

				if (green >= 255) {
					green = 255;
				}

				if (blue >= 255) {
					blue = 255;
				}
				// 新的ARGB
				int newColor = alpha | (red << 16) | (green << 8) | blue;
				//设置新图像的RGB值
				linegray.setPixel(i, j, newColor);
			}
		}
		return linegray;
	}

	/**
	 * 对图像进行二值化
	 * @param graymap
	 * @return
	 */
	public static Bitmap gray2Binary(Bitmap graymap) {
		//得到图形的宽度和长度
		int width = graymap.getWidth();
		int height = graymap.getHeight();
		//创建二值化图像
		Bitmap binarymap = null;
		binarymap = graymap.copy(Config.ARGB_8888, true);
		//依次循环，对图像的像素进行处理
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				//得到当前像素的值
				int col = binarymap.getPixel(i, j);
				//得到alpha通道的值
				int alpha = col & 0xFF000000;
				//得到图像的像素RGB的值
				int red = (col & 0x00FF0000) >> 16;
				int green = (col & 0x0000FF00) >> 8;
				int blue = (col & 0x000000FF);
				// 用公式X = 0.3×R+0.59×G+0.11×B计算出X代替原来的RGB
				int gray = (int) ((float) red * 0.3 + (float) green * 0.59 + (float) blue * 0.11);
				//对图像进行二值化处理
				if (gray <= 95) {
					gray = 0;
				} else {
					gray = 255;
				}
				// 新的ARGB
				int newColor = alpha | (gray << 16) | (gray << 8) | gray;
				//设置新图像的当前像素值
				binarymap.setPixel(i, j, newColor);
			}
		}
		return binarymap;
	}

	/**
	 * bitmap转字节数组
	 *
	 * @param bitmap
	 * @param quality
	 * @return
	 */
	public static byte[] bitmapToByteArr(Bitmap bitmap, int quality) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);// (0 - 100)压缩文件
		byte[] bytes = stream.toByteArray();
		return bytes;
	}

	/**
	 * 字节数组转bitmap
	 *
	 * @param bytes
	 * @return
	 */
	public static Bitmap byteArrToBitmap(byte[] bytes) {
		Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		return bitmap;
	}


	/**
	 *
	 * 把Assets目录下的文件转成InputStream
	 * @param context
	 * @param fileName
	 * @return
	 */
	public static InputStream openAssets(android.content.Context context, String fileName) {
		InputStream inputStream = null;
		try {
			inputStream = context.getAssets().open(fileName);
			return inputStream;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
