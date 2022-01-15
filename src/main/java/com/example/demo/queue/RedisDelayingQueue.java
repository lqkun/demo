package com.example.demo.queue;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.entity.RedisTaskItem;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Set;
import java.util.UUID;

/**
 * @className: com.example.demo.queue-> RedisDelayingQueue
 * @description: TODO
 * @author: LQKun
 * @date: 2021-08-14 22:28
 * @version: 1.0
 */
public class RedisDelayingQueue {

    private RedisTemplate redisTemplate;

    /**
     * zset键
     **/
    private String queueKey;

    public RedisDelayingQueue(RedisTemplate redisTemplate, String queueKey) {
        this.redisTemplate = redisTemplate;
        this.queueKey = queueKey;
    }

   /**
    * @description 存数据方法
    * @param msg
    * @return
    **/
    public void delay(String msg){
        RedisTaskItem item = new RedisTaskItem();
        item.id = UUID.randomUUID().toString();
        item.msg = msg;
        String s = JSONObject.toJSONString(item);
        redisTemplate.opsForZSet().add(queueKey,s,Double.valueOf(System.currentTimeMillis() + 5000));
    }

   /**
    * @description
    * @param
    * @return
    **/
    public void loop(){
        while (!Thread.interrupted()){
            //每次只取一条
            Set<Object> values = redisTemplate.opsForZSet().rangeByScore(queueKey,0L,System.currentTimeMillis(),0L,1L);
            if(values.isEmpty()){
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    break;
                }
                continue;
            }
            String s = values.iterator().next().toString();
            //loop方法可能会被多个线程调用，所以要通过remove来决定唯一的属主
            if(redisTemplate.opsForZSet().remove(queueKey,s)>0){
                //抢到了 反序列化
                RedisTaskItem taskItem = JSONObject.parseObject(s,RedisTaskItem.class);
                System.out.println(taskItem.msg);
            }
        }
    }
}
