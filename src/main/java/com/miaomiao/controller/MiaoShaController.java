package com.miaomiao.controller;

import com.miaomiao.domain.MiaoShaUser;
import com.miaomiao.domain.MiaoshaOrder;
import com.miaomiao.domain.OrderInfo;
import com.miaomiao.result.CodeMsg;
import com.miaomiao.service.GoodsService;
import com.miaomiao.service.MiaoShaService;
import com.miaomiao.service.OrderService;
import com.miaomiao.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by cjq on 2018-03-08 00:11
 */
@Controller
@RequestMapping("/miaosha")
public class MiaoShaController {

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    MiaoShaService miaoShaService;

    @RequestMapping("/do_miaosha")
    public String list(Model model , MiaoShaUser user , @RequestParam("goodsId") long goodsId) {
        model.addAttribute("user" , user);
        if(user == null)
            return "login";
        // 判断库存
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goods.getStockCount();
        if(stock <= 0) {
            model.addAttribute("errmsg" , CodeMsg.MIAO_SHA_OVER.getMsg());
            return "miaosha_fail";
        }

        // 判断是否已经秒杀到了
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId() , goodsId);
        if(order != null) {
            model.addAttribute("errmsg" , CodeMsg.REPEATE_MIAOSHA.getMsg());
            return "miaosha_fail";
        }

        // 减库存 下订单 写入秒杀订单
        OrderInfo orderInfo = miaoShaService.miaosha(user , goods);
        model.addAttribute("orderInfo" , orderInfo);
        model.addAttribute("goods" , goods);
        return "order_detail";
    }

}
