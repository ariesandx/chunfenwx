package com.chunfen.wx;

import java.io.Serializable;

/**
 *  作为 json 字符串的生成 对象
 * @author chunfen.wx
 */
public class JsonDomain implements Serializable{
    //封顶价	string	false	增价拍封顶价，不设置表示无封顶价
    private String ceilPrice;

    //增价拍出价延迟时间，单位分钟	number	false	为0则表示不延迟，默认为5
    private Integer delayInMinutes = 5;

    //结束时间	string	true
    private String endTime;

    //底价	string	false	降价拍专用，表示底价
    private String floorPrice;

    //保证金	string	true	100元以下货物保证金5元、1000以下保证金20元、10000以上50元
    private String foregift;

    //增价（降价）幅度，单位元	string	true
    private String increment;

    //商品id	string	true
    private String itemId;

    //宝贝数量	number	true	拍品数量，增价拍必须为1，降价拍为1~10000
    private Integer quantity;

    //重复上架次数	number	false	默认为0，0表示不自动重新上架
    private String repeatTimes = "0";

    //起拍价	string	true
    private String startPrice;

    //开始时间	string	false
    private String startTime;

    // 发布状态	string	false	立即上架（0）放入仓库（1）设置定时开拍（3）
    private String status;

    //拍卖类型	string	false	增价 1，降价 2
    private String type;

    public String getCeilPrice() {
        return ceilPrice;
    }

    public void setCeilPrice(String ceilPrice) {
        this.ceilPrice = ceilPrice;
    }

    public Integer getDelayInMinutes() {
        return delayInMinutes;
    }

    public void setDelayInMinutes(Integer delayInMinutes) {
        this.delayInMinutes = delayInMinutes;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getFloorPrice() {
        return floorPrice;
    }

    public void setFloorPrice(String floorPrice) {
        this.floorPrice = floorPrice;
    }

    public String getForegift() {
        return foregift;
    }

    public void setForegift(String foregift) {
        this.foregift = foregift;
    }

    public String getIncrement() {
        return increment;
    }

    public void setIncrement(String increment) {
        this.increment = increment;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getRepeatTimes() {
        return repeatTimes;
    }

    public void setRepeatTimes(String repeatTimes) {
        this.repeatTimes = repeatTimes;
    }

    public String getStartPrice() {
        return startPrice;
    }

    public void setStartPrice(String startPrice) {
        this.startPrice = startPrice;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
