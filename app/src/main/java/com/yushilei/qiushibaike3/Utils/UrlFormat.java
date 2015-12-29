package com.yushilei.qiushibaike3.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yushilei on 2015/12/29.
 */
public class UrlFormat {
    private static String imageType = "small";

    public static String getImageType() {
        return imageType;
    }

    public static void setImageType(String imageType) {
        UrlFormat.imageType = imageType;
    }

    public static String getImageUrl(String image) {
//        内容图片获取(+图片名从数字开始去掉后4位+图片名从数字开始数全部+"/"+"/"+small或者medium+"/"+图片名)
//        ====图片Url=http://pic.qiushibaike.com/system/pictures/7128/71288069/small/app71288069.jpg
        String url = "http://pic.qiushibaike.com/system/pictures/%s/%s/%s/%s";
        Pattern pattern = Pattern.compile("(\\d+)\\d{4}");
        Matcher matcher = pattern.matcher(image);
        matcher.find();

        return String.format(url, matcher.group(1), matcher.group(), imageType, image);
    }

    public static String getIconUrl(long id, String icon) {
        //userIcon======http://pic.qiushibaike.com/system/avtnew/1499/14997026/thumb/20140404194843.jpg
        //public final static String URL_USER_ICON="http://pic.qiushibaike.com/system/avtnew/%s/%s/thumb/%s";
        String url = "http://pic.qiushibaike.com/system/avtnew/%s/%s/thumb/%s";
        return String.format(url, id / 10000, id, icon);
    }
}
