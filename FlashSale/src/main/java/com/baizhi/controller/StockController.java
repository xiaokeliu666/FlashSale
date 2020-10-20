package com.baizhi.controller;

import com.baizhi.service.OrderService;
import com.baizhi.service.UserService;
import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("stock")
@Slf4j
public class StockController {


    @Autowired
    private OrderService orderService;


    //Create token bucket instance
    private RateLimiter rateLimiter = RateLimiter.create(10);


    @Autowired
    private UserService userService;


    //Generate md5
    @RequestMapping("md5")
    public String getMd5(Integer id, Integer userid) {
        String md5;
        try {
            md5 = orderService.getMd5(id, userid);
        }catch (Exception e){
            e.printStackTrace();
            return "Fail to get md5 "+e.getMessage();
        }
        return "md5: "+md5;
    }



//    optimistic locking
    @GetMapping("kill")
    public String kill(Integer id) {
        System.out.println("id = " + id);
        try {
            //根据秒杀商品id 去调用秒杀业务
            int orderId = orderService.kill(id);
            return "success,id: " + String.valueOf(orderId);
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }


    // md5 signature
    @GetMapping("killtokenmd5")
    public String killtoken(Integer id,Integer userid,String md5) {
        System.out.println("id of target item:" + id);
        // add rateLimiter
        if (!rateLimiter.tryAcquire(3, TimeUnit.SECONDS)) {
            log.info("fail");
            return "fail";
        }
        try {
            int orderId = orderService.kill(id,userid,md5);
            return "success,id: " + String.valueOf(orderId);
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }


    // limit the frequency of visit from single user
    @GetMapping("killtokenmd5limit")
    public String killtokenlimit(Integer id,Integer userid,String md5) {
        if (!rateLimiter.tryAcquire(3, TimeUnit.SECONDS)) {
            log.info("fail");
            return "fail";
        }
        try {
            //limit the frequency of visit from single user
            int count = userService.saveUserCount(userid);
            log.info("user visited this Api: [{}]", count);
            //get user count
            boolean isBanned = userService.getUserCount(userid);
            if (isBanned) {
                log.info("Fail: visit too frequently");
                return "Fail: visit too frequently";
            }
            int orderId = orderService.kill(id,userid,md5);
            return "success,id: " + String.valueOf(orderId);
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    @GetMapping("killtoken")
    public String killtoken(Integer id) {
        System.out.println("id of target item" + id);
        if (!rateLimiter.tryAcquire(3, TimeUnit.SECONDS)) {
            log.info("Fail");
            return "Fail";
        }
        try {
            int orderId = orderService.kill(id);
            return "success,id: " + String.valueOf(orderId);
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }


//    @GetMapping("sale")
//    public String sale(Integer id){
//
    // 1. requests that didn't get the token, wait until they get one
//        //log.info("waited time: "+  rateLimiter.acquire());
//
//
//    2. Set a timer, if request gets token before the countdown, then process the request, otherwise discard this request
//        if(!rateLimiter.tryAcquire(2, TimeUnit.SECONDS)){
//            System.out.println("This request has been discarded");
//            return "fail";
//        }
//        System.out.println("Processing.....................");
//        return "Success";
//    }


}
