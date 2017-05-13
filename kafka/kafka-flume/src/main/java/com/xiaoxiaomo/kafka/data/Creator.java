package com.xiaoxiaomo.kafka.data;

import org.apache.log4j.Logger;

import java.util.*;

/**
 * Created by xiaoxiaomo on 2015/12/30.
 */
public class Creator {

    static Map<Integer,Integer> appid=new HashMap<Integer, Integer>();
    static Map<Integer,String> mid=new HashMap<Integer, String>();
    static Map<Integer,String> ip=new HashMap<Integer, String>();
    static Map<Integer,Integer> userid=new HashMap<Integer, Integer>();
    static Map<Integer,String> request=new HashMap<Integer, String>();
    static Map<Integer,Integer> status=new HashMap<Integer, Integer>();
    static Map<Integer,String> http_referer=new HashMap<Integer, String>();
    static Map<Integer,String> user_agent=new HashMap<Integer, String>();
    static Map<Integer,String> param=new HashMap<Integer, String>();



    private static void Initdata(){

        //web
        appid.put(0,1000);
        //android
        appid.put(1,1001);
        //ios
        appid.put(2,1002);
        //ipad
        appid.put(3,1003);

        ip.put(0,"61.172.249.96");
        ip.put(1,"211.155.234.99");
        ip.put(2,"218.75.100.114");
        ip.put(3,"211.167.248.22");
        ip.put(4,"60.12.227.208");
        ip.put(5,"221.8.9.6 80");
        ip.put(6,"218.26.219.186");
        ip.put(7,"222.68.207.11");
        ip.put(8,"61.53.137.50");
        ip.put(9,"218.75.75.133");
        ip.put(10,"221.204.246.11");
        ip.put(11,"125.39.129.67");
        ip.put(12,"220.194.55.244");
        ip.put(13,"125.70.229.30");
        ip.put(14,"220.194.55.160");
        ip.put(15,"202.98.11.101");
        ip.put(16,"59.76.81.3 808");
        ip.put(17,"121.11.87.171");
        ip.put(18,"121.9.221.188");
        ip.put(19,"221.195.40.145");


        //init mid

        for (int i = 0; i < 20; i++) {
            mid.put(i, UUID.randomUUID().toString());
        }


        //init userId
        userid.put(0,20101);
        userid.put(1,10201);
        userid.put(2,10022);
        userid.put(3,20201);
        userid.put(4,20202);
        userid.put(5,30303);
        userid.put(6,40604);
        userid.put(7,10207);
        userid.put(8,10608);
        userid.put(9,10709);
        userid.put(10,0);
        userid.put(11,0);

        //request  HTTP协议 1.1和 1.0
        request.put(0,"GET /tologin HTTP/1.1");
        request.put(1,"GET /userList HTTP/1.1");
        request.put(2,"GET /user/add HTTP/1.1");
        request.put(3,"POST /updateById?id=21 HTTP/1.1");
        request.put(4,"GET /top HTTP/1.1");
        request.put(5,"GET /tologin HTTP/1.0");
        request.put(6,"GET /index HTTP/1.1");
        request.put(7,"POST /stat HTTP/1.1");
        request.put(8,"GET /getDataById HTTP/1.0");
        request.put(9,"GET /tologin HTTP/1.1");
        request.put(10,"POST /check/init HTTP/1.1");
        request.put(11,"GET /check/detail HTTP/1.1");
        request.put(12,"GET /top HTTP/1.0");
        request.put(13,"POST /passpword/getById?id=11 HTTP/1.1");
        request.put(14,"GET /update/pass HTTP/1.0");

        http_referer.put(0,"/tologin");
        http_referer.put(1,"/userList");
        http_referer.put(2,"/user/add");
        http_referer.put(3,"/updateById?id=21");
        http_referer.put(4,"/top");
        http_referer.put(5,"/tologin");
        http_referer.put(6,"/index");
        http_referer.put(7,"/stat");
        http_referer.put(8,"/getDataById");
        http_referer.put(9,"/tologin");
        http_referer.put(10,"/check/init");
        http_referer.put(11,"/check/detail");
        http_referer.put(12,"/top");
        http_referer.put(13,"/passpword/getById?id=11");
        http_referer.put(14,"/update/pass");



        //status
        //200 ok
        status.put(0,200);
        //404 not found
        status.put(1,404);
        //408 Request Timeout
        status.put(2,408);
        // 500 Internal Server Error
        status.put(3,500);
        //504 Gateway Timeout
        status.put(4,504);


        user_agent.put(0,"Mozilla/4.0 (compatible; MSIE 8.0; Windows NT6.0)");
        user_agent.put(1,"Mozilla/4.0 (compatible; MSIE 7.0; Windows NT5.2)");
        user_agent.put(2,"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT5.1)");
        user_agent.put(3,"Mozilla/4.0 (compatible; MSIE 5.0; WindowsNT)");
        user_agent.put(4,"Mozilla/5.0 (Windows; U; Windows NT 5.2)Gecko/2008070208 Firefox/3.0.1");
        user_agent.put(5,"Mozilla/5.0 (Windows; U; Windows NT 5.1)Gecko/20070309 Firefox/2.0.0.3");
        user_agent.put(6,"Mozilla/5.0 (Windows; U; Windows NT 5.1)Gecko/20070803 Firefox/1.5.0.12");
        user_agent.put(7,"Mozilla/5.0 (Windows; U; Windows NT 5.2)AppleWebKit/525.13 (KHTML, like Gecko) Version/3.1Safari/525.13");
        user_agent.put(8,"Mozilla/5.0 (Windows; U; Windows NT 5.2) AppleWebKit/525.13 (KHTML,like Gecko) Chrome/0.2.149.27 Safari/525.13");
        user_agent.put(9,"Mozilla/5.0 (iPhone; U; CPU like Mac OS X)AppleWebKit/420.1 (KHTML, like Gecko) Version/3.0 Mobile/4A93Safari/419.3");



        param.put(0,"{\"ugctype\":\"consumer\",\"userId\":\"10022\",\"coin\":\"50\",\"number\":\"2\"}");
        param.put(1,"{\"ugctype\":\"consumer\",\"userId\":\"20202\",\"coin\":\"20\",\"number\":\"3\"}");
        param.put(2,"{\"ugctype\":\"consumer\",\"userId\":\"40604\",\"coin\":\"10\",\"number\":\"2\"}");
        param.put(3,"{\"ugctype\":\"recharge\",\"userId\":\"20202\",\"rmb\":\"50\",\"number\":\"10\"}");
        param.put(4,"{\"ugctype\":\"recharge\",\"userId\":\"40604\",\"rmb\":\"100\",\"number\":\"20\"}");
        param.put(5,"{\"ugctype\":\"recharge\",\"userId\":\"10022\",\"rmb\":\"100\",\"number\":\"20\"}");
        param.put(6,"{\"ugctype\":\"fav\",\"userId\":\"10608\",\"item\":\"10\"}");
        param.put(7,"{\"ugctype\":\"fav\",\"userId\":\"10709\",\"item\":\"11\"}");
        param.put(8,"{\"ugctype\":\"fav\",\"userId\":\"10207\",\"item\":\"12\"}");
        param.put(9,"{\"ugctype\":\"fav\",\"userId\":\"40604\",\"item\":\"13\"}");




    }

 static    class  AccessLogger implements Runnable{

        public void run() {
            //appid	ip	mid	userid	login_type	request		status	http_referer	user_agent	time
            Initdata();
            Random r=new Random();
            Logger accessLogger=Logger.getLogger("access");
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                accessLogger.info(String.format("%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s"
                        , appid.get(r.nextInt(4))
                        , ip.get(r.nextInt(20))
                        , mid.get(r.nextInt(20))
                        , userid.get(r.nextInt(12)), r.nextInt(2)
                        , request.get(r.nextInt(15))
                        , status.get(r.nextInt(5))
                        , http_referer.get(r.nextInt(15))
                        , user_agent.get(r.nextInt(10)), new Date().getTime()));
            }
        }
    }

   static class  UgcLogger implements  Runnable{

        public void run() {
            Initdata();
            Random r=new Random();
            String seid;
            Logger headLogger=Logger.getLogger("ugchead");
            Logger tailLogger=Logger.getLogger("ugctail");

            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                seid=UUID.randomUUID().toString();

                //appid	ip	mid	seid	userid	param	time
                headLogger.info(String.format("%s\t%s\t%s\t%s\t%s\t%s\t%s"
                        , appid.get(r.nextInt(4))
                        , ip.get(r.nextInt(20))
                        , mid.get(r.nextInt(20))
                        , seid
                        , userid.get(r.nextInt(10))
                        , param.get(r.nextInt(10)), new Date().getTime()));
                //appid	ip	mid	seid	userid	param	result	time
                tailLogger.info(String.format("%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s"
                        , appid.get(r.nextInt(4))
                        , ip.get(r.nextInt(20))
                        , mid.get(r.nextInt(20))
                        , seid
                        , userid.get(r.nextInt(10))
                        , param.get(r.nextInt(10))
                        ,r.nextInt(2)
                        , new Date().getTime()));
            }
        }
    }



    public static void main(String[] args) {


        new Thread(new AccessLogger()).start();
        new Thread(new UgcLogger()).start();

    }

}
