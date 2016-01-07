package com.yushilei.qiushibaike3.entitys;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yushilei on 2016/1/1.
 */

public class SuggestResponse {

    /**
     * count : 30
     * err : 0
     * items
     * total : 300
     * page : 1
     * refresh : 300
     */
    @SerializedName("count")
    private int count;
    private int err;
    @SerializedName("total")
    private int total;
    @SerializedName("page")
    private int page;
    private int refresh;
    /**
     * format : image
     * image : app114494063.jpg
     * published_at : 1451553902
     * tag :
     * user : {"avatar_updated_at":1427831217,"last_visited_at":1427831216,"created_at":1427831216,"state":"active","last_device":"android_6.5.0","role":"n","login":"男人的奥迪","id":27173314,"icon":"20150401034657.jpg"}
     * image_size : {"s":[220,165,7700],"m":[500,375,67512]}
     * id : 114494063
     * votes : {"down":-381,"up":16314}
     * created_at : 1451547498
     * content : 2013年搞投资担保公司赚了点，14年生意走下坡路，资不抵债，15年年中实在抗不住，把家里的两辆车卖了，亲戚朋友借了一圈，父母帮忙还了半数银行贷款。。。。 人生不如意十之八九，每天都是浑浑噩噩过日子。早上不起床，中午吃了饭就开始打游戏看电影，没有了年轻时候的斗志。 老婆天天流泪，靠着每个月1540的工资还着交通银行的最低还款，从她的眼神中，看到尽是失望，找不到原来的欣赏。 那天看了个综艺节目，关于一个才艺表演的，没有记住叫啥，但是里边有一个人，让我记忆犹新。 这个人在2000年就已经是千万富翁，有很多的产业，员工就有一千多人。前段时间经济不景气的时候，生意一落千丈，负债近千万。这个人没有跑路，而是选择了坚持，在深圳重新打拼卖起了包子。虽然近千万的负债靠着卖包子很难偿还。。。 表演开始他唱了一首刘欢的从头再来，评委给他了很高的评价。当问及他的年龄，评委们看到的，只有他满头的白发，这个人才40岁。 听完他唱的，我哭了。想想自己这么久，在家休息这么久，靠着父母和老婆给的零花钱度日，想抽自己几巴掌。。。 今天即将迎来我跑出租车的第4个夜班，生意还不错。第一天夜班赚了80多，第二个夜班赚了100多点，昨天晚上的夜班跑了126。师傅教育我说，新人，不要懒，跑下去，不要停。 图中的这些零钱，是这几天跑的一部分，我拿出来两百，作为本钱，跑出租不能没有零钱。 我相信，以后的路，我会好好的走下去。负债几百万的人，还那么自信，我负债几十万，其实还是有很大的机会的。。。大家都加油，祝2016年，大家都找到好项目。
     * state : publish
     * comments_count : 2510
     * allow_comment : true
     * share_count : 330
     * type : hot
     */
    @SerializedName("items")
    private List<ItemsEntity> items;

    public void setCount(int count) {
        this.count = count;
    }

    public void setErr(int err) {
        this.err = err;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setRefresh(int refresh) {
        this.refresh = refresh;
    }

    public void setItems(List<ItemsEntity> items) {
        this.items = items;
    }

    public int getCount() {
        return count;
    }

    public int getErr() {
        return err;
    }

    public int getTotal() {
        return total;
    }

    public int getPage() {
        return page;
    }

    public int getRefresh() {
        return refresh;
    }

    public List<ItemsEntity> getItems() {
        return items;
    }

    public static class ItemsEntity implements Serializable {
        private boolean isSupport;
        private boolean isUnSupport;

        public void setPublished_at(long published_at) {
            this.published_at = published_at;
        }

        public boolean isSupport() {
            return isSupport;
        }

        public void setIsSupport(boolean isSupport) {
            this.isSupport = isSupport;
        }

        public boolean isUnSupport() {
            return isUnSupport;
        }

        public void setIsUnSupport(boolean isUnSupport) {
            this.isUnSupport = isUnSupport;
        }

        @SerializedName("format")
        private String format;
        @SerializedName("image")
        private String image;
        @SerializedName("published_at")
        private long published_at;
        @SerializedName("tag")
        private String tag;
        @SerializedName("pic_url")
        private String pic_url;
        @SerializedName("low_url")
        private String low_url;

        public String getLow_url() {
            return low_url;
        }

        public void setLow_url(String low_url) {
            this.low_url = low_url;
        }

        public String getPic_url() {
            return pic_url;
        }

        public void setPic_url(String pic_url) {
            this.pic_url = pic_url;
        }

        /**
         * avatar_updated_at : 1427831217
         * last_visited_at : 1427831216
         * created_at : 1427831216
         * state : active
         * last_device : android_6.5.0
         * role : n
         * login : 男人的奥迪
         * id : 27173314
         * icon : 20150401034657.jpg
         */
        @SerializedName("user")
        private UserEntity user;
        private ImageSizeEntity image_size;
        @SerializedName("id")
        private long id;
        /**
         * down : -381
         * up : 16314
         */
        @SerializedName("votes")
        private VotesEntity votes;

        private long created_at;
        @SerializedName("content")
        private String content;
        private String state;
        @SerializedName("comments_count")
        private int comments_count;
        private boolean allow_comment;
        @SerializedName("share_count")
        private int share_count;
        @SerializedName("type")
        private String type;

        public void setFormat(String format) {
            this.format = format;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public void setPublished_at(int published_at) {
            this.published_at = published_at;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public void setUser(UserEntity user) {
            this.user = user;
        }

        public void setImage_size(ImageSizeEntity image_size) {
            this.image_size = image_size;
        }

        public void setId(long id) {
            this.id = id;
        }

        public void setVotes(VotesEntity votes) {
            this.votes = votes;
        }

        public void setCreated_at(long created_at) {
            this.created_at = created_at;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public void setState(String state) {
            this.state = state;
        }

        public void setComments_count(int comments_count) {
            this.comments_count = comments_count;
        }

        public void setAllow_comment(boolean allow_comment) {
            this.allow_comment = allow_comment;
        }

        public void setShare_count(int share_count) {
            this.share_count = share_count;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getFormat() {
            return format;
        }

        public String getImage() {
            return image;
        }

        public long getPublished_at() {
            return published_at;
        }

        public String getTag() {
            return tag;
        }

        public UserEntity getUser() {
            return user;
        }

        public ImageSizeEntity getImage_size() {
            return image_size;
        }

        public long getId() {
            return id;
        }

        public VotesEntity getVotes() {
            return votes;
        }

        public long getCreated_at() {
            return created_at;
        }

        public String getContent() {
            return content;
        }

        public String getState() {
            return state;
        }

        public int getComments_count() {
            return comments_count;
        }

        public boolean isAllow_comment() {
            return allow_comment;
        }

        public int getShare_count() {
            return share_count;
        }

        public String getType() {
            return type;
        }

        public static class UserEntity implements Serializable {
            private long avatar_updated_at;
            private long last_visited_at;
            private long created_at;
            private String state;
            private String last_device;
            private String role;
            @SerializedName("login")
            private String login;
            @SerializedName("id")
            private long id;
            @SerializedName("icon")
            private String icon;

            public void setAvatar_updated_at(int avatar_updated_at) {
                this.avatar_updated_at = avatar_updated_at;
            }

            public void setLast_visited_at(int last_visited_at) {
                this.last_visited_at = last_visited_at;
            }

            public void setCreated_at(int created_at) {
                this.created_at = created_at;
            }

            public void setState(String state) {
                this.state = state;
            }

            public void setLast_device(String last_device) {
                this.last_device = last_device;
            }

            public void setRole(String role) {
                this.role = role;
            }

            public void setLogin(String login) {
                this.login = login;
            }

            public void setId(long id) {
                this.id = id;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }

            public long getAvatar_updated_at() {
                return avatar_updated_at;
            }

            public long getLast_visited_at() {
                return last_visited_at;
            }

            public long getCreated_at() {
                return created_at;
            }

            public String getState() {
                return state;
            }

            public String getLast_device() {
                return last_device;
            }

            public String getRole() {
                return role;
            }

            public String getLogin() {
                return login;
            }

            public long getId() {
                return id;
            }

            public String getIcon() {
                return icon;
            }
        }

        public static class ImageSizeEntity implements Serializable {
            private List<Integer> s;
            private List<Integer> m;

            public void setS(List<Integer> s) {
                this.s = s;
            }

            public void setM(List<Integer> m) {
                this.m = m;
            }

            public List<Integer> getS() {
                return s;
            }

            public List<Integer> getM() {
                return m;
            }
        }

        public static class VotesEntity implements Serializable {
            @SerializedName("down")
            private int down;
            @SerializedName("up")
            private int up;

            public void setDown(int down) {
                this.down = down;
            }

            public void setUp(int up) {
                this.up = up;
            }

            public int getDown() {
                return down;
            }

            public int getUp() {
                return up;
            }
        }
    }
}
