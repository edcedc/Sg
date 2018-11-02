package com.fanwang.sg.bean;

import java.util.List;

/**
 * 作者：yc on 2018/9/20.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public class ProductProductDetailBean {

    private int specialSale;//是否特卖  1是 0不是
    private String YouHui;
    private String introduction;
    private int activited;
    private String service;
    private String zhekou;
    private int isMarketable;
    private String parameterValues;
    private String realPrice;
    private boolean manzeng;
    private boolean isCollect;
    private String logo;
    private String specialSales;
    private int collageNum;
    private String id;
    private int isCredited;//  `isCredited` smallint(1) DEFAULT '0' COMMENT '时候积分商品1是0不是',
    private int skustock;//是否有库存


    public int getSkustock() {
        return skustock;
    }
    public int getIsCredited() {
        return isCredited;
    }

    public String getId() {
        return id;
    }

    public int getCollageNum() {
        return collageNum;
    }

    public String getSpecialSales() {
        return specialSales;
    }

    public String getLogo() {
        return logo;
    }

    public boolean isCollect() {
        return isCollect;
    }

    public boolean isManzeng() {
        return manzeng;
    }

    public String getRealPrice() {
        return realPrice;
    }

    public String getParameterValues() {
        return parameterValues;
    }

    public int getIsMarketable() {
        return isMarketable;
    }

    public void setSpecialSale(int specialSale) {
        this.specialSale = specialSale;
    }

    public void setYouHui(String youHui) {
        YouHui = youHui;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public int getActivited() {
        return activited;
    }

    public void setActivited(int activited) {
        this.activited = activited;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getZhekou() {
        return zhekou;
    }

    public void setZhekou(String zhekou) {
        this.zhekou = zhekou;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isManjian() {
        return manjian;
    }

    public void setManjian(boolean manjian) {
        this.manjian = manjian;
    }

    public double getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(double marketPrice) {
        this.marketPrice = marketPrice;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<DataBean> getProdImageList() {
        return prodImageList;
    }

    public void setProdImageList(List<DataBean> prodImageList) {
        this.prodImageList = prodImageList;
    }

    private String image;
    private String name;
    private boolean manjian;
    private double marketPrice;
    private int type;

    private List<DataBean> prodImageList;

    public String getYouHui() {
        return YouHui;
    }

    public int getSpecialSale() {
        return specialSale;
    }

    private BusinessBean business;

    public BusinessBean getBusiness() {
        return business;
    }

    public static class BusinessBean{
        private int prodNum;
        private String name;
        private String logo;
        private String id;

        public int getProdNum() {
            return prodNum;
        }

        public String getName() {
            return name;
        }

        public String getLogo() {
            return logo;
        }

        public String getId() {
            return id;
        }
    }

    private CollageBean collage;

    public CollageBean getCollage() {
        return collage;
    }

    public static class CollageBean{
        private String startTime;
        private String endTime;
        private int state;

        public String getStartTime() {
            return startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public int getState() {
            return state;
        }
    }
    private List<DataBean> collageOrderList;

    public List<DataBean> getCollageOrderList() {
        return collageOrderList;
    }

    private SecKillBean secKill;

    public SecKillBean getSecKill() {
        return secKill;
    }

    public static class SecKillBean{
        private String startTime;
        private String endTime;
        private int state;

        public String getStartTime() {
            return startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public int getState() {
            return state;
        }
    }

}
