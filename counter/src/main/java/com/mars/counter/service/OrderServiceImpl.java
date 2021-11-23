package com.mars.counter.service;

import com.mars.counter.bean.res.OrderInfo;
import com.mars.counter.bean.res.PosiInfo;
import com.mars.counter.bean.res.TradeInfo;
import com.mars.counter.config.CounterConfig;
import com.mars.counter.util.DbUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thirdpart.order.CmdType;
import thirdpart.order.OrderCmd;
import thirdpart.order.OrderDirection;
import thirdpart.order.OrderType;

import java.util.List;

/**
 * 委托
 */
@Service
@Log4j2
public class OrderServiceImpl implements IOrderService {
    //查资金
    @Override
    public Long getBalance(long uid) {
        return DbUtil.getBalance(uid);
    }
    //查持仓
    @Override
    public List<PosiInfo> getPostList(long uid) {
        return DbUtil.getPosiList(uid);
    }
    //查委托
    @Override
    public List<OrderInfo> getOrderList(long uid) {
        return DbUtil.getOrderList(uid);
    }
    //查成交
    @Override
    public List<TradeInfo> getTradeList(long uid) {
        return DbUtil.getTradeList(uid);
    }

    @Autowired
    private CounterConfig config;
    //发送委托
    @Override
    public boolean sendOrder(int uid, short type, long timestamp, int code, byte direction, long price, long volume, byte ordertype) {
        final OrderCmd orderCmd = OrderCmd.builder()
                .type(CmdType.of(type))
                .timestamp(timestamp)
                .mid(config.getId())
                .uid(uid)
                .code(code)
                .direction(OrderDirection.of(ordertype))
                .price(price)
                .volume(volume)
                .orderType(OrderType.of(ordertype))
                .build();
        int oid = DbUtil.saveOrder(orderCmd);
        if(oid < 0){
            return false;
        }else{
            log.info(orderCmd);
            return true;
        }
    }

}
