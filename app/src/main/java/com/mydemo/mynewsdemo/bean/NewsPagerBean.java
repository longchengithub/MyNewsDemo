package com.mydemo.mynewsdemo.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by chenlong on 2016/12/22.
 */

public class NewsPagerBean implements Serializable
{
    /**
     * data : {"more":"/10007/list_2.json","news":[{"id":35311,"listimage":"/10007/2078369924F9UO.jpg","pubdate":"2014-10-1113:18","title":"网上大讲堂第368期预告：义务环保人人有责","url":"/10007/724D6A55496A11726628.html"},{"id":35312,"listimage":"/10007/1509585620ASS3.jpg","pubdate":"2014-10-1111:20","title":"马路改建为停车场车位年费高达3000元","url":"/10007/724D6A55496A11726628.html"},{"id":35313,"listimage":"/10007/1506815057D99I.jpg","pubdate":"2014-10-1110:34","title":"北京两年内将迁出1200家工业污染企业","url":"/10007/724D6A55496A11726628.html"},{"id":35314,"listimage":"/10007/1505891536Z82T.jpg","pubdate":"2014-10-1110:08","title":"大雾再锁京城机场航班全部延误","url":"/10007/724D6A55496A11726628.html"},{"id":35315,"listimage":"/10007/1483727032VMXT.jpg","pubdate":"2014-10-1110:03","title":"APEC会议期间调休企业员工盼同步放假","url":"/10007/724D6A55496A11726628.html"},{"id":35316,"listimage":"/10007/1481879990BEMG.jpg","pubdate":"2014-10-1109:59","title":"机械\u201c龙马\u201d巡演17日亮相奥园","url":"/10007/724D6A55496A11726628.html"},{"id":35317,"listimage":"/10007/14800329488K7F.jpg","pubdate":"2014-10-1109:54","title":"门头沟获批100套限价房","url":"/10007/724D6A55496A11726628.html"},{"id":35318,"listimage":"/10007/14791094274LT9.jpg","pubdate":"2014-10-1109:52","title":"APEC期间净空区放带灯风筝可拘留","url":"/10007/724D6A55496A11726628.html"},{"id":35319,"listimage":"/10007/1478185906G9WQ.jpg","pubdate":"2014-10-1109:48","title":"今起两自住房摇号","url":"/10007/724D6A55496A11726628.html"},{"id":35320,"listimage":"/10007/1477262385PASS.jpg","pubdate":"2014-10-1109:45","title":"故宫神武门广场拟夜间开放停车","url":"/10007/724D6A55496A11726628.html"}],"title":"北京","topnews":[{"id":35401,"pubdate":"2014-04-0814:24","title":"蜗居生活","topimage":"/10007/1452327318UU91.jpg","url":"/10007/724D6A55496A11726628.html"},{"id":35402,"pubdate":"2014-04-0809:09","title":"中秋赏月","topimage":"/10007/1452327318UU92.jpg","url":"/10007/724D6A55496A11726628.html"},{"id":35403,"pubdate":"2014-04-0809:09","title":"天空翱翔","topimage":"/10007/1452327318UU93.jpg","url":"/10007/724D6A55496A11726628.html"},{"id":35404,"pubdate":"2014-04-0809:09","title":"感官设计","topimage":"/10007/1452327318UU94.png","url":"/10007/724D6A55496A11726628.html"}]}
     * retcode : 200
     */

    private DataBean data;
    private int retcode;

    public DataBean getData()
    {
        return data;
    }

    public void setData(DataBean data)
    {
        this.data = data;
    }

    public int getRetcode()
    {
        return retcode;
    }

    public void setRetcode(int retcode)
    {
        this.retcode = retcode;
    }

    public static class DataBean
    {
        /**
         * more : /10007/list_2.json
         * news : [{"id":35311,"listimage":"/10007/2078369924F9UO.jpg","pubdate":"2014-10-1113:18","title":"网上大讲堂第368期预告：义务环保人人有责","url":"/10007/724D6A55496A11726628.html"},{"id":35312,"listimage":"/10007/1509585620ASS3.jpg","pubdate":"2014-10-1111:20","title":"马路改建为停车场车位年费高达3000元","url":"/10007/724D6A55496A11726628.html"},{"id":35313,"listimage":"/10007/1506815057D99I.jpg","pubdate":"2014-10-1110:34","title":"北京两年内将迁出1200家工业污染企业","url":"/10007/724D6A55496A11726628.html"},{"id":35314,"listimage":"/10007/1505891536Z82T.jpg","pubdate":"2014-10-1110:08","title":"大雾再锁京城机场航班全部延误","url":"/10007/724D6A55496A11726628.html"},{"id":35315,"listimage":"/10007/1483727032VMXT.jpg","pubdate":"2014-10-1110:03","title":"APEC会议期间调休企业员工盼同步放假","url":"/10007/724D6A55496A11726628.html"},{"id":35316,"listimage":"/10007/1481879990BEMG.jpg","pubdate":"2014-10-1109:59","title":"机械\u201c龙马\u201d巡演17日亮相奥园","url":"/10007/724D6A55496A11726628.html"},{"id":35317,"listimage":"/10007/14800329488K7F.jpg","pubdate":"2014-10-1109:54","title":"门头沟获批100套限价房","url":"/10007/724D6A55496A11726628.html"},{"id":35318,"listimage":"/10007/14791094274LT9.jpg","pubdate":"2014-10-1109:52","title":"APEC期间净空区放带灯风筝可拘留","url":"/10007/724D6A55496A11726628.html"},{"id":35319,"listimage":"/10007/1478185906G9WQ.jpg","pubdate":"2014-10-1109:48","title":"今起两自住房摇号","url":"/10007/724D6A55496A11726628.html"},{"id":35320,"listimage":"/10007/1477262385PASS.jpg","pubdate":"2014-10-1109:45","title":"故宫神武门广场拟夜间开放停车","url":"/10007/724D6A55496A11726628.html"}]
         * title : 北京
         * topnews : [{"id":35401,"pubdate":"2014-04-0814:24","title":"蜗居生活","topimage":"/10007/1452327318UU91.jpg","url":"/10007/724D6A55496A11726628.html"},{"id":35402,"pubdate":"2014-04-0809:09","title":"中秋赏月","topimage":"/10007/1452327318UU92.jpg","url":"/10007/724D6A55496A11726628.html"},{"id":35403,"pubdate":"2014-04-0809:09","title":"天空翱翔","topimage":"/10007/1452327318UU93.jpg","url":"/10007/724D6A55496A11726628.html"},{"id":35404,"pubdate":"2014-04-0809:09","title":"感官设计","topimage":"/10007/1452327318UU94.png","url":"/10007/724D6A55496A11726628.html"}]
         */

        private String more;
        private String title;
        private List<NewsBean> news;
        private List<TopnewsBean> topnews;

        public String getMore()
        {
            return more;
        }

        public void setMore(String more)
        {
            this.more = more;
        }

        public String getTitle()
        {
            return title;
        }

        public void setTitle(String title)
        {
            this.title = title;
        }

        public List<NewsBean> getNews()
        {
            return news;
        }

        public void setNews(List<NewsBean> news)
        {
            this.news = news;
        }

        public List<TopnewsBean> getTopnews()
        {
            return topnews;
        }

        public void setTopnews(List<TopnewsBean> topnews)
        {
            this.topnews = topnews;
        }

        public static class NewsBean
        {
            /**
             * id : 35311
             * listimage : /10007/2078369924F9UO.jpg
             * pubdate : 2014-10-1113:18
             * title : 网上大讲堂第368期预告：义务环保人人有责
             * url : /10007/724D6A55496A11726628.html
             */

            private int id;
            private String listimage;
            private String pubdate;
            private String title;
            private String url;

            public int getId()
            {
                return id;
            }

            public void setId(int id)
            {
                this.id = id;
            }

            public String getListimage()
            {
                return listimage;
            }

            public void setListimage(String listimage)
            {
                this.listimage = listimage;
            }

            public String getPubdate()
            {
                return pubdate;
            }

            public void setPubdate(String pubdate)
            {
                this.pubdate = pubdate;
            }

            public String getTitle()
            {
                return title;
            }

            public void setTitle(String title)
            {
                this.title = title;
            }

            public String getUrl()
            {
                return url;
            }

            public void setUrl(String url)
            {
                this.url = url;
            }
        }

        public static class TopnewsBean
        {
            /**
             * id : 35401
             * pubdate : 2014-04-0814:24
             * title : 蜗居生活
             * topimage : /10007/1452327318UU91.jpg
             * url : /10007/724D6A55496A11726628.html
             */

            private int id;
            private String pubdate;
            private String title;
            private String topimage;
            private String url;

            public int getId()
            {
                return id;
            }

            public void setId(int id)
            {
                this.id = id;
            }

            public String getPubdate()
            {
                return pubdate;
            }

            public void setPubdate(String pubdate)
            {
                this.pubdate = pubdate;
            }

            public String getTitle()
            {
                return title;
            }

            public void setTitle(String title)
            {
                this.title = title;
            }

            public String getTopimage()
            {
                return topimage;
            }

            public void setTopimage(String topimage)
            {
                this.topimage = topimage;
            }

            public String getUrl()
            {
                return url;
            }

            public void setUrl(String url)
            {
                this.url = url;
            }
        }
    }
}
