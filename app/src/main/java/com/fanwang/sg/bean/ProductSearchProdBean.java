package com.fanwang.sg.bean;

/**
 * 作者：yc on 2018/10/10.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public class ProductSearchProdBean {

    private String image;
    private String name;
    private int isMarketable;
    private double marketPrice;
    private double zhekou;
    private String id;
    private double realPrice;
    private int specialSale;
    private int activited;

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

    public int getIsMarketable() {
        return isMarketable;
    }

    public void setIsMarketable(int isMarketable) {
        this.isMarketable = isMarketable;
    }

    public double getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(double marketPrice) {
        this.marketPrice = marketPrice;
    }

    public double getZhekou() {
        return zhekou;
    }

    public void setZhekou(double zhekou) {
        this.zhekou = zhekou;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getRealPrice() {
        return realPrice;
    }

    public void setRealPrice(double realPrice) {
        this.realPrice = realPrice;
    }

    public int getSpecialSale() {
        return specialSale;
    }

    public void setSpecialSale(int specialSale) {
        this.specialSale = specialSale;
    }

    public int getActivited() {
        return activited;
    }

    public void setActivited(int activited) {
        this.activited = activited;
    }
}
