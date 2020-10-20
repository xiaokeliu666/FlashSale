package com.baizhi.service;

import com.baizhi.dao.OrderDAO;
import com.baizhi.dao.StockDAO;
import com.baizhi.dao.UserDAO;
import com.baizhi.entity.Order;
import com.baizhi.entity.Stock;
import com.baizhi.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class OrderServiceImpl implements OrderService {


    @Autowired
    private StockDAO stockDAO;

    @Autowired
    private OrderDAO orderDAO;



    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private UserDAO userDAO;

    @Override
    public int kill(Integer id, Integer userid, String md5) {

        // check if the target is overtime in redis
//        if(!stringRedisTemplate.hasKey("kill"+id))
//            throw new RuntimeException("Item not available");

        // check the signature
        String hashKey = "KEY_"+userid+"_"+id;
        String s = stringRedisTemplate.opsForValue().get(hashKey);
        if (s==null) throw  new RuntimeException("Illegal request without signature");
        if (!s.equals(md5)) throw  new RuntimeException("Illegal request with wrong signature");


        Stock stock = checkStock(id);

        updateSale(stock);

        return createOrder(stock);
    }

    @Override
    public String getMd5(Integer id, Integer userid) {

        // check the user
        User user = userDAO.findById(userid);
        if(user==null)throw new RuntimeException("user doesnt' exist");
        log.info("user info:[{}]",user.toString());

        // check the item
        Stock stock = stockDAO.checkStock(id);
        if(stock==null) throw new RuntimeException("Illegal item");
        log.info("item info:[{}]",stock.toString());

        //ashkey
        String hashKey = "KEY_"+userid+"_"+id;
        //md5 + salt
        String key = DigestUtils.md5DigestAsHex((userid+id+"!Q*jS#").getBytes());
        stringRedisTemplate.opsForValue().set(hashKey, key, 120, TimeUnit.SECONDS);

        log.info("Redis input：[{}] [{}]", hashKey, key);
        return key;
    }


    @Override
    public  int kill(Integer id) {

        // check if the target is overtime in redis
        if(!stringRedisTemplate.hasKey("kill"+id))
            throw new RuntimeException("item not available");

        Stock stock = checkStock(id);
        updateSale(stock);
        return createOrder(stock);
    }



    private Stock checkStock(Integer id){
        Stock stock = stockDAO.checkStock(id);
        if(stock.getSale().equals(stock.getCount())){
            throw  new RuntimeException("Out of stock");
        }
        return stock;
    }


    private void updateSale(Stock stock){
        // in sql: sold+1, version+1
        // check the item by both id and version
        int updateRows = stockDAO.updateSale(stock);
        if (updateRows==0){
            throw new RuntimeException("Fail");
        }
    }

    //创建订单
    private Integer createOrder(Stock stock){
        Order order = new Order();
        order.setSid(stock.getId()).setName(stock.getName()).setCreateDate(new Date());
        orderDAO.createOrder(order);
        return order.getId();
    }


    public static void main(String[] args) {

        int[] arr2 = {234, 2, 23, 777, 2};

        List<Integer> list2 = Arrays.stream(arr2).boxed().collect(Collectors.toList());
        for (Integer integer : list2) {
            System.out.println(integer);
        }
    }
}
