package com.leyou.search.mq;

import com.leyou.search.service.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ItemListener {
    @Autowired
    private SearchService searchService;
    private Long preSpuId;
    private int consumNumber;

    // ignoreDeclarationExceptions: This option instructs the RabbitAdmin to log the exception and continue declaring other elements.
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "search.item.insert.queue3", durable = "true"),
            exchange = @Exchange(value = "ly.item.exchange",ignoreDeclarationExceptions = "true", type = ExchangeTypes.TOPIC),
            key = {"item.insert","item.update"}
    ))
    public void listenInsertOrUpdate(Long spuId){
        if(spuId == null){
            return;
        }

        // 处理消息，对索引库进行新增或修改;
        // 处理失败，会回滚消息;

        // 比较消息是否是前面消息
        setConsumNumber(spuId);

        try{
            searchService.createOrUpdateIndex(spuId);
        }catch (Exception e){
            if(this.consumNumber <3){

                // 少于3次 则抛出异常，让其重新消费信息;
                throw new RuntimeException();
            }

            // 不再消费，记录日志;
            log.error(spuId + "消费异常"+e.getMessage());
        }
    }

    private void setConsumNumber(Long spuId) {

        if(this.preSpuId != null && this.preSpuId.equals(spuId)){
            // 是 重发的消息;
            this.consumNumber++;
        }else {
            // 如果不是重复的消息;
            // 保存到内存
            this.preSpuId = spuId;
            this.consumNumber =0;
        }
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "search.item.delete.queue", durable = "true"),
            exchange = @Exchange(name = "ly.item.exchange", type = ExchangeTypes.TOPIC),
            key = {"item.delete"}
    ))
    public void listenDelete(Long spuId){
        if(spuId == null){
            return;
        }

        searchService.deleteIndex(spuId);
    }
}
