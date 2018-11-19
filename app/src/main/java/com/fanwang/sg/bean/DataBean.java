package com.fanwang.sg.bean;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yc on 2017/8/17.
 */

public class DataBean implements Serializable {

    private String name;
    private String img;
    private boolean isSelect = false;
    private int position;
    private double price;
    private String title;
    private String content;
    private String image;
    private int type;
    private int atype;
    private String ids;
    private BigDecimal realPrice;//原价格
    private BigDecimal marketPrice;//优惠价格
    private String zhekou;//折扣
    private String img1;
    private String id;
    private String remark;
    private String startTime;
    private int totalStock;//库存
    private int surplusStock;//总库存
    private String cat_name;
    private String phone;
    private String email;
    private String bankName;//银行名字
    private String cardNumber;//银行卡号
    private int defaulted;//  `defaulted` int(1) DEFAULT '0' COMMENT '是否默认，缺省是0表示该卡不是默认的银行卡，该用户所有的银行卡当中只有一张是默认卡',
    private String productcategoryid;
    private String YouHui;
    private String imageUrl;
    private String service;//商品服务
    private int isMarketable;//是否上架 1是 0不是
    private String parameterValues;//参数规格
    private String specificationItems;
    private String value;
    private boolean isSelected;
    private int cost;
    private int stock;
    private boolean allSelected = false;
    private int prodNum;
    private String logo;
    private String nickname;
    private String image_url;
    private int collageNum;//拼团人数
    private String withdrawRate;
    private String withDrawMoney;
    private String create_time;
    private int state;//1001待付款、\r\n1002待发货、\r\n1003待收货、\r\n1004交易完成\r\n1005退款/售后\r\n1006已关闭/10000待成团
    private String withdraw_money;
    private String mobile;
    private String address;
    private String detailedAddress;
    private int isChoice;
    private String stateType;
    private String specificationValues;
    private int num;
    private String skuId;
    private String pid;
    private String orderNo;//订单号
    private double original_price;//订单价格
    private String showImg;
    private double huangoujia;//换购价
    private int isGroupPurchase;//  `isGroupPurchase` smallint(1) NOT NULL DEFAULT '0' COMMENT '是否团购单1是0不是',
    private String recName;//地址收货人
    private String recMobile;//地址收货人电话
    private String recAddress;//收货人地址
    private String endTime;
    private String head;
    private String createTime;
    private String updateTime;
    private int currentCollageNum;
    private String url;
    private String activityId;
    private int isCredited;//  `isCredited` smallint(1) DEFAULT '0' COMMENT '时候积分商品1是0不是',
    private String deliveryTime;//发货时间
    private int isRefund;// `isRefund` int(1) NOT NULL DEFAULT '0' COMMENT '申请退款状态 0 未申请，1已申请，2申请退款失败，3申请退款成功',
    private int cargoType;//  `cargoType` smallint(1) NOT NULL COMMENT '货物类型0已收货，1未收货',
    private String logisticsCompany;
    private String logisticsNo;
    private String orderId;
    private double realPayMoney;//实付金额
    private String userId;
    private String link;
    private int refund_num;//大于2不能申请退款
    private int skustock;//是否有库存
    private String distribution_img;//海报
    private String record;
    private BigDecimal money;
    private String payfinishedTime;//支付成功时间
    private String subheading;
    private int overdue;//0进行中 1未开启 2过期
    private int discount;// `discount` decimal(10,2) DEFAULT '0.00' COMMENT '折扣积分',
    private int integral;//  `integral` int(1) DEFAULT '0' COMMENT '是否积分商品，0否，1是',
    private String rongyunid;//客服ID

    public String getRongyunid() {
        return rongyunid;
    }

    public int getIntegral() {
        return integral;
    }

    public int getDiscount() {
        return discount;
    }

    public int getOverdue() {
        return overdue;
    }

    public String getSubheading() {
        return subheading;
    }

    public String getPayfinishedTime() {
        return payfinishedTime;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public String getRecord() {
        return record;
    }

    public String getDistribution_img() {
        return distribution_img;
    }

    public int getRefund_num() {
        return refund_num;
    }

    public int getSkustock() {
        return skustock;
    }

    public String getLink() {
        return link;
    }

    public String getUserId() {
        return userId;
    }

    public double getRealPayMoney() {
        return realPayMoney;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getLogisticsNo() {
        return logisticsNo;
    }

    public String getLogisticsCompany() {
        return logisticsCompany;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public int getCargoType() {
        return cargoType;
    }

    public int getIsRefund() {
        return isRefund;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public int getIsCredited() {
        return isCredited;
    }

    public void setZhekou(String zhekou) {
        this.zhekou = zhekou;
    }

    public String getActivityId() {
        return activityId;
    }

    public String getUrl() {
        return url;
    }

    public int getAtype() {
        return atype;
    }

    public int getCurrentCollageNum() {
        return currentCollageNum;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getHead() {
        return head;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getRecAddress() {
        return recAddress;
    }

    public String getRecMobile() {
        return recMobile;
    }

    public String getRecName() {
        return recName;
    }

    public int getIsGroupPurchase() {
        return isGroupPurchase;
    }

    public double getHuangoujia() {
        return huangoujia;
    }

    public String getShowImg() {
        return showImg;
    }

    public double getOriginal_price() {
        return original_price;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public String getPid() {
        return pid;
    }

    public void setSpecificationValues(String specificationValues) {
        this.specificationValues = specificationValues;
    }

    public void setMarketPrice(BigDecimal marketPrice) {
        this.marketPrice = marketPrice;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public String getSkuId() {
        return skuId;
    }

    public int getNum() {
        return num;
    }

    public String getSpecificationValues() {
        return specificationValues;
    }

    public String getStateType() {
        return stateType;
    }

    public int getIsChoice() {
        return isChoice;
    }

    public void setIsChoice(int isChoice) {
        this.isChoice = isChoice;
    }

    public String getDetailedAddress() {
        return detailedAddress;
    }

    public String getAddress() {
        return address;
    }

    public String getMobile() {
        return mobile;
    }

    public String getWithdraw_money() {
        return withdraw_money;
    }

    public int getState() {
        return state;
    }

    public String getCreate_time() {
        return create_time;
    }

    public String getWithDrawMoney() {
        return withDrawMoney;
    }

    public String getWithdrawRate() {
        return withdrawRate;
    }

    public int getCollageNum() {
        return collageNum;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getNickname() {
        return nickname;
    }

    public String getLogo() {
        return logo;
    }

    public int getProdNum() {
        return prodNum;
    }

    public boolean isAllSelected() {
        return allSelected;
    }

    public void setAllSelected(boolean allSelected) {
        this.allSelected = allSelected;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setRealPrice(BigDecimal realPrice) {
        this.realPrice = realPrice;
    }

    public int getStock() {
        return stock;
    }

    public int getCost() {
        return cost;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getSpecificationItems() {
        return specificationItems;
    }

    public String getParameterValues() {
        return parameterValues;
    }

    public int getIsMarketable() {
        return isMarketable;
    }

    public String getService() {
        return service;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getYouHui() {
        return YouHui;
    }

    public String getProductcategoryid() {
        return productcategoryid;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setDefaulted(int defaulted) {
        this.defaulted = defaulted;
    }

    public int getDefaulted() {
        return defaulted;
    }

    public String getBankName() {
        return bankName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getCat_name() {
        return cat_name;
    }

    public int getSurplusStock() {
        return surplusStock;
    }

    public int getTotalStock() {
        return totalStock;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getRemark() {
        return remark;
    }

    public String getId() {
        return id;
    }

    public String getImg1() {
        return img1;
    }

    public String getZhekou() {
        return zhekou;
    }

    public BigDecimal getMarketPrice() {
        return marketPrice;
    }

    public BigDecimal getRealPrice() {
        return realPrice;
    }

    public String getIds() {
        return ids;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public String getImg() {
        return img;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    private List<DataBean> listOrderDetails;

    public List<DataBean> getListOrderDetails() {
        return listOrderDetails;
    }

    private List<DataBean> prod = new ArrayList<>();

    public List<DataBean> getProd() {
        return prod;
    }

    public void setProd(List<DataBean> prod) {
        this.prod = prod;
    }

    public List<DataBean> adsList;

    public List<DataBean> getAdsList() {
        return adsList;
    }




    //首页label
    private List<DataBean> labelList;

    public List<DataBean> getLabelList() {
        return labelList;
    }

    public void setLabelList(List<DataBean> labelList) {
        this.labelList = labelList;
    }

    //新品专区
    private NewProductBean newProduct;

    public NewProductBean getNewProduct() {
        return newProduct;
    }

    public void setNewProduct(NewProductBean newProduct) {
        this.newProduct = newProduct;
    }

    public static class NewProductBean{
        private String title;

        public String getTitle() {
            return title;
        }
        public List<DataBean> manJianList;

        public List<DataBean> getManJianList() {
            return manJianList;
        }

        public void setManJianList(List<DataBean> manJianList) {
            this.manJianList = manJianList;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

    //优惠特卖
    private SpecialSaleBean specialSale;

    public SpecialSaleBean getSpecialSale() {
        return specialSale;
    }

    public void setSpecialSale(SpecialSaleBean specialSale) {
        this.specialSale = specialSale;
    }

    public static class SpecialSaleBean{
        private String title;
        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }
        public List<DataBean> specialSaleList;

        public List<DataBean> getSpecialSaleList() {
            return specialSaleList;
        }
    }

    //满赠
    private ManZengBean manZeng;

    public ManZengBean getManZeng() {
        return manZeng;
    }

    public void setManZeng(ManZengBean manZeng) {
        this.manZeng = manZeng;
    }

    public static class ManZengBean{
        private List<DataBean> prodLists;

        public List<DataBean> getProdLists() {
            return prodLists;
        }
        private List<DataBean> acts;

        public List<DataBean> getActs() {
            return acts;
        }
    }

    //满减
    private ManJianBean manJian;

    public ManJianBean getManJian() {
        return manJian;
    }

    public void setManJian(ManJianBean manJian) {
        this.manJian = manJian;
    }

    public static class ManJianBean{
        private List<DataBean> prodLists;

        public List<DataBean> getProdLists() {
            return prodLists;
        }
        private List<DataBean> acts;

        public List<DataBean> getActs() {
            return acts;
        }
    }


    //秒杀和拼团
    private Seckill_CollageBean seckill_collageBean;

    public Seckill_CollageBean getSeckill_collageBean() {
        return seckill_collageBean;
    }

    public void setSeckill_collageBean(Seckill_CollageBean seckill_collageBean) {
        this.seckill_collageBean = seckill_collageBean;
    }

    public static class Seckill_CollageBean{
        private SecKillBean secKill;
        private CollageBean collage;

        public SecKillBean getSecKill() {
            return secKill;
        }

        public void setSecKill(SecKillBean secKill) {
            this.secKill = secKill;
        }

        public CollageBean getCollage() {
            return collage;
        }

        public void setCollage(CollageBean collage) {
            this.collage = collage;
        }
    }

    //秒杀
    private SecKillBean secKill;

    public SecKillBean getSecKill() {
        return secKill;
    }

    public void setSecKill(SecKillBean secKill) {
        this.secKill = secKill;
    }

    public static class SecKillBean{
        private String title;
        private String endTime;

        public String getEndTime() {
            return endTime;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        private List<DataBean> prodList;

        public List<DataBean> getProdList() {
            return prodList;
        }
    }

    //拼团
    private CollageBean collage;

    public void setCollage(CollageBean collage) {
        this.collage = collage;
    }

    public CollageBean getCollage() {
        return collage;
    }

    public static class CollageBean{
        private String title;

        public String getTitle() {
            return title;
        }

        private List<DataBean> collageList;

        public List<DataBean> getCollageList() {
            return collageList;
        }
    }

    private List<DataBean> collageList;

    public List<DataBean> getCollageList() {
        return collageList;
    }

    private List<DataBean> prodImageList;

    public List<DataBean> getProdImageList() {
        return prodImageList;
    }

    private List<DataBean> entries  = new ArrayList<>();

    public List<DataBean> getEntries() {
        return entries;
    }

    public void setEntries(List<DataBean> entries) {
        this.entries = entries;
    }

    private List<DataBean> product = new ArrayList<>();

    public List<DataBean> getProduct() {
        return product;
    }

    private List<DataBean> recommendProd;

    public List<DataBean> getRecommendProd() {
        return recommendProd;
    }

    private List<DataBean> listUser;

    public List<DataBean> getListUser() {
        return listUser;
    }

    private TeamLeaderBean teamLeader;

    public TeamLeaderBean getTeamLeader() {
        return teamLeader;
    }

    public static class TeamLeaderBean{
        private String head;
        private String nickname;

        public String getHead() {
            return head;
        }

        public String getNickname() {
            return nickname;
        }
    }

    private OrderRefundBean orderRefund;

    public OrderRefundBean getOrderRefund() {
        return orderRefund;
    }

    public static class OrderRefundBean{
        private int audit;

        public int getAudit() {
            return audit;
        }
    }

    private OrderSalesReturnBean orderSalesReturn;

    public OrderSalesReturnBean getOrderSalesReturn() {
        return orderSalesReturn;
    }
    public static class OrderSalesReturnBean{
        private int audit;
        private int state;
        private String content;
        private String createTime;
        private String updateTime;

        public String getUpdateTime() {
            return updateTime;
        }

        public String getCreateTime() {
            return createTime;
        }

        public String getContent() {
            return content;
        }

        public int getState() {
            return state;
        }

        public int getAudit() {
            return audit;
        }
    }

}