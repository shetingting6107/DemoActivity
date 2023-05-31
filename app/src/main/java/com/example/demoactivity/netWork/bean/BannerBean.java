package com.example.demoactivity.netWork.bean;

import com.example.demoactivity.netWork.base.BaseBean;

/**
 * banner图对象
 */
public class BannerBean extends BaseBean {

    /**
     * {
     * 		"desc": "一起来做个App吧",
     * 		"id": 10,
     * 		"imagePath": "https://www.wanandroid.com/blogimgs/50c115c2-cf6c-4802-aa7b-a4334de444cd.png",
     * 		"isVisible": 1,
     * 		"order": 1,
     * 		"title": "一起来做个App吧",
     * 		"type": 1,
     * 		"url": "https://www.wanandroid.com/blog/show/2"
     *        }
     */

    private String desc;
    private Double id;
    private String imagePath;
    private Double isVisible;
    private Double order;
    private String title;
    private Double type;
    private String url;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Double getId() {
        return id;
    }

    public void setId(Double id) {
        this.id = id;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Double getIsVisible() {
        return isVisible;
    }

    public void setIsVisible(Double isVisible) {
        this.isVisible = isVisible;
    }

    public Double getOrder() {
        return order;
    }

    public void setOrder(Double order) {
        this.order = order;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getType() {
        return type;
    }

    public void setType(Double type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
