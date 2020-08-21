package cn.huanzi.qch.baseadmin.util;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;
/*

 外部调用：

 //获取验证码图片和文本(验证码文本会保存在HttpSession中)
 public void getVerifyCodeImage(HttpServletRequest request, HttpServletResponse response) throws IOException {
 //设置页面不缓存
 response.setHeader("Pragma", "no-cache");
 response.setHeader("Cache-Control", "no-cache");
 response.setDateHeader("Expires", 0);
 response.getOutputStream();
 String verifyCode = VerifyCodeImageUtil.generateTextCode(VerifyCodeUtil.TYPE_NUM_UPPER, 4, null);

 //将验证码放到HttpSession里面
 request.getSession().setAttribute("verifyCode", verifyCode);
 log.debug("本次生成的验证码为[" + verifyCode + "],已存放到HttpSession中");

 //设置输出的内容的类型为JPEG图像
 response.setContentType("image/jpeg");
 BufferedImage bufferedImage = VerifyCodeImageUtil.generateImageCode(verifyCode, 90, 30, 3, true, Color.WHITE, Color.BLACK, null);

 //写给浏览器
 ImageIO.write(bufferedImage, "JPEG", response.getOutputStream());
 }

 */

/**
 * Captcha Util
 */
public class VerifyCodeImageUtil {

    /**
     * Num only
     */
    public static final int TYPE_NUM_ONLY = 0;

    /**
     * Letter Only
     */
    public static final int TYPE_LETTER_ONLY = 1;

    /**
     * Mixed(Num and letter)
     */
    public static final int TYPE_ALL_MIXED = 2;

    /**
     * Mixed(Num and Uppercase letter)
     */
    public static final int TYPE_NUM_UPPER = 3;

    /**
     * Mixed(Num and Lowercase letter)
     */
    public static final int TYPE_NUM_LOWER = 4;

    /**
     * Uppercase only
     */
    public static final int TYPE_UPPER_ONLY = 5;

    /**
     * lowercase only
     */
    public static final int TYPE_LOWER_ONLY = 6;

    /**
     * random color
     */
    public static Color generateRandomColor() {
        Random random = new Random();
        return new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255));
    }

    /**
     * Generate image captcha
     *
     * @param type           验证码类型,参见本类的静态属性
     * @param length         验证码字符长度,要求大于0的整数
     * @param excludeString  需排除的特殊字符
     * @param width          图片宽度(注意此宽度若过小,容易造成验证码文本显示不全,如4个字符的文本可使用85到90的宽度)
     * @param height         图片高度
     * @param interLine      图片中干扰线的条数
     * @param randomLocation 每个字符的高低位置是否随机
     * @param backColor      图片颜色,若为null则表示采用随机颜色
     * @param foreColor      字体颜色,若为null则表示采用随机颜色
     * @param lineColor      干扰线颜色,若为null则表示采用随机颜色
     * @return 图片缓存对象
     */
    public static BufferedImage generateImageCode(int type, int length, String excludeString, int width, int height, int interLine, boolean randomLocation, Color backColor, Color foreColor, Color lineColor) {
        String textCode = generateTextCode(type, length, excludeString);
        return generateImageCode(textCode, width, height, interLine, randomLocation, backColor, foreColor, lineColor);
    }

    /**
     * 生成验证码字符串
     *
     * @param type          验证码类型,参见本类的静态属性
     * @param length        验证码长度,要求大于0的整数
     * @param excludeString 需排除的特殊字符（无需排除则为null）
     * @return 验证码字符串
     */
    public static String generateTextCode(int type, int length, String excludeString) {
        if (length <= 0) {
            return "";
        }
        StringBuilder verifyCode = new StringBuilder();
        int i = 0;
        Random random = new Random();
        switch (type) {
            case TYPE_NUM_ONLY:
                while (i < length) {
                    int t = random.nextInt(10);
                    //排除特殊字符
                    if (null == excludeString || !excludeString.contains(t + "")) {
                        verifyCode.append(t);
                        i++;
                    }
                }
                break;
            case TYPE_LETTER_ONLY:
                while (i < length) {
                    int t = random.nextInt(123);
                    if ((t >= 97 || (t >= 65 && t <= 90)) && (null == excludeString || excludeString.indexOf((char) t) < 0)) {
                        verifyCode.append((char) t);
                        i++;
                    }
                }
                break;
            case TYPE_ALL_MIXED:
                while (i < length) {
                    int t = random.nextInt(123);
                    if ((t >= 97 || (t >= 65 && t <= 90) || (t >= 48 && t <= 57)) && (null == excludeString || excludeString.indexOf((char) t) < 0)) {
                        verifyCode.append((char) t);
                        i++;
                    }
                }
                break;
            case TYPE_NUM_UPPER:
                while (i < length) {
                    int t = random.nextInt(91);
                    if ((t >= 65 || (t >= 48 && t <= 57)) && (null == excludeString || excludeString.indexOf((char) t) < 0)) {
                        verifyCode.append((char) t);
                        i++;
                    }
                }
                break;
            case TYPE_NUM_LOWER:
                while (i < length) {
                    int t = random.nextInt(123);
                    if ((t >= 97 || (t >= 48 && t <= 57)) && (null == excludeString || excludeString.indexOf((char) t) < 0)) {
                        verifyCode.append((char) t);
                        i++;
                    }
                }
                break;
            case TYPE_UPPER_ONLY:
                while (i < length) {
                    int t = random.nextInt(91);
                    if ((t >= 65) && (null == excludeString || excludeString.indexOf((char) t) < 0)) {
                        verifyCode.append((char) t);
                        i++;
                    }
                }
                break;
            case TYPE_LOWER_ONLY:
                while (i < length) {
                    int t = random.nextInt(123);
                    if ((t >= 97) && (null == excludeString || excludeString.indexOf((char) t) < 0)) {
                        verifyCode.append((char) t);
                        i++;
                    }
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
        return verifyCode.toString();
    }

    /**
     * Generate the picture
     *
     * @param textCode       captcha in plaintext
     * @param width          image width
     * @param height         image height
     * @param interLine      lines on the image(for interference)
     * @param randomLocation Height of each letter random or not
     * @param backColor      Image color(null for random)
     * @param foreColor      Font color(null for random)
     * @param lineColor      Interference color(null for random)
     * @return Image cache
     */
    public static BufferedImage generateImageCode(String textCode, int width, int height, int interLine, boolean randomLocation, Color backColor, Color foreColor, Color lineColor) {
        // Create buffer image
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        // Get image context
        Graphics graphics = bufferedImage.getGraphics();
        // Background color
        graphics.setColor(null == backColor ? generateRandomColor() : backColor);
        graphics.fillRect(0, 0, width, height);
        // Draw interference line
        Random random = new Random();
        if (interLine > 0) {
            int x = 0, y, y1;
            for (int i = 0; i < interLine; i++) {
                graphics.setColor(null == lineColor ? generateRandomColor() : lineColor);
                y = random.nextInt(height);
                y1 = random.nextInt(height);
                graphics.drawLine(x, y, width, y1);
            }
        }
        // font size equals to the 80% of image's height
        int fsize = (int) (height * 0.8);
        int fx = height - fsize;
        int fy = fsize;
        // set font
        graphics.setFont(new Font("Default", Font.PLAIN, fsize));
        // write the text
        for (int i = 0; i < textCode.length(); i++) {
            fy = randomLocation ? (int) ((Math.random() * 0.3 + 0.6) * height) : fy;
            graphics.setColor(null == foreColor ? generateRandomColor() : foreColor);
            // Show the captcha on the image
            graphics.drawString(textCode.charAt(i) + "", fx, fy);
            fx += fsize * 0.9;
        }
        graphics.dispose();
        return bufferedImage;
    }
}
