package com.miaomiao.service;

import com.miaomiao.domain.MiaoShaUser;
import com.miaomiao.domain.OrderInfo;
import com.miaomiao.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by cjq on 2018-03-08 00:22
 */
@Service
public class MiaoShaService {

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Transactional
    public OrderInfo miaosha(MiaoShaUser user, GoodsVo goods) {
        // 减库存 下订单 写入秒杀订单
        goodsService.reduceStock(goods);

        // order_info miaosha_order
        return orderService.createOrder(user , goods);
    }
}
