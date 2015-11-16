package entity;

public class Article {
	
	private int id; 		//文章ID  
    private String title;   //文章标题  
    private String brief; 	//文章简介  
    private String avatar; 	//图片路径 
    private String pubtime; //发布时间
    private int hits;		//点击数
    private int bad;		//踩
    private int goods;		//顶
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getBrief() {
		return brief;
	}
	public void setBrief(String brief) {
		this.brief = brief;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public String getPubtime() {
		return pubtime;
	}
	public void setPubtime(String pubtime) {
		this.pubtime = pubtime;
	}
	public int getHits() {
		return hits;
	}
	public void setHits(int hits) {
		this.hits = hits;
	}
	public int getBad() {
		return bad;
	}
	public void setBad(int bad) {
		this.bad = bad;
	}
	public int getGoods() {
		return goods;
	}
	public void setGoods(int goods) {
		this.goods = goods;
	}
	

}
