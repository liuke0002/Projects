package com.liuke.zhbj.bean;

import java.util.List;

public class RecommandCardBean {
    private String type;

    private String timestamp;

    private String article_id;

    private String author_id;

    private String author_image;

    private String author_nickname;

    private String text;

    private List<String> article_tag;

    private List<String> image_url ;

    private String like_count;

    private String comment_count;

    private String activity_id;

    private String activity_image_url;

    private String activity_book_num;

    private String activity_title;

    private String has_followed;

    private String has_praised;

    public RecommandCardBean(String type, String timestamp, String article_id, String author_id, String author_image, String author_nickname, String text, List<String> article_tag, List<String> image_url, String like_count, String comment_count, String has_followed, String has_praised) {
        this.type = type;
        this.timestamp = timestamp;
        this.article_id = article_id;
        this.author_id = author_id;
        this.author_image = author_image;
        this.author_nickname = author_nickname;
        this.text = text;
        this.article_tag = article_tag;
        this.image_url = image_url;
        this.like_count = like_count;
        this.comment_count = comment_count;
        this.has_followed = has_followed;
        this.has_praised = has_praised;
    }

    public RecommandCardBean(String type, String timestamp, String activity_id, String activity_image_url, String activity_book_num, String activity_title) {
        this.type = type;
        this.timestamp = timestamp;
        this.activity_id = activity_id;
        this.activity_image_url = activity_image_url;
        this.activity_book_num = activity_book_num;
        this.activity_title = activity_title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getArticle_id() {
        return article_id;
    }

    public void setArticle_id(String article_id) {
        this.article_id = article_id;
    }

    public String getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(String author_id) {
        this.author_id = author_id;
    }

    public String getAuthor_image() {
        return author_image;
    }

    public void setAuthor_image(String author_image) {
        this.author_image = author_image;
    }

    public String getAuthor_nickname() {
        return author_nickname;
    }

    public void setAuthor_nickname(String author_nickname) {
        this.author_nickname = author_nickname;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getArticle_tag() {
        return article_tag;
    }

    public void setArticle_tag(List<String> article_tag) {
        this.article_tag = article_tag;
    }

    public List<String> getImage_url() {
        return image_url;
    }

    public void setImage_url(List<String> image_url) {
        this.image_url = image_url;
    }

    public String getLike_count() {
        return like_count;
    }

    public void setLike_count(String like_count) {
        this.like_count = like_count;
    }

    public String getComment_count() {
        return comment_count;
    }

    public void setComment_count(String comment_count) {
        this.comment_count = comment_count;
    }

    public String getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(String activity_id) {
        this.activity_id = activity_id;
    }

    public String getActivity_image_url() {
        return activity_image_url;
    }

    public void setActivity_image_url(String activity_image_url) {
        this.activity_image_url = activity_image_url;
    }

    public String getActivity_book_num() {
        return activity_book_num;
    }

    public void setActivity_book_num(String activity_book_num) {
        this.activity_book_num = activity_book_num;
    }

    public String getActivity_title() {
        return activity_title;
    }

    public void setActivity_title(String activity_title) {
        this.activity_title = activity_title;
    }

    public String getHas_followed() {
        return has_followed;
    }

    public void setHas_followed(String has_followed) {
        this.has_followed = has_followed;
    }

    public String getHas_praised() {
        return has_praised;
    }

    public void setHas_praised(String has_praised) {
        this.has_praised = has_praised;
    }
}
