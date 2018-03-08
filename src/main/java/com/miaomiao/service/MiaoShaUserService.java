package com.miaomiao.service;

import com.miaomiao.dao.MiaoShaUserDao;
import com.miaomiao.domain.MiaoShaUser;
import com.miaomiao.exception.GlobalException;
import com.miaomiao.redis.MiaoShaUserKey;
import com.miaomiao.redis.RedisService;
import com.miaomiao.result.CodeMsg;
import com.miaomiao.util.MD5Util;
import com.miaomiao.util.UUIDUtil;
import com.miaomiao.vo.LoginVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by cjq on 2018-03-07 17:35
 */
@Service
public class MiaoShaUserService {

    public static final String COOKI_NAME_TOKEN = "token";

    @Autowired
    MiaoShaUserDao miaoShaUserDao;

    @Autowired
    RedisService redisService;

    public MiaoShaUser getByToken(HttpServletResponse response , String token) {
        if(StringUtils.isEmpty(token))
            return null;
        MiaoShaUser user = redisService.get(MiaoShaUserKey.token , token , MiaoShaUser.class);
        // 延长有效期
        if(user != null) {
            addCookie(response , token , user);
        }
        return user;
    }

    public boolean login(HttpServletResponse response , LoginVo loginVo) {
        if(loginVo == null)
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        String mobile = loginVo.getMobile();
        String formPass = loginVo.getPassword();
        MiaoShaUser user = miaoShaUserDao.getById(Long.parseLong(mobile));
        if(user == null)
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        // 验证密码
        String dbPass = user.getPassword();
        String saltDB = user.getSalt();
        String calcPass = MD5Util.formPassToDBPass(formPass , saltDB);
        if(!dbPass.equals(calcPass))
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        String token = UUIDUtil.uuid();
        addCookie(response , token , user);
        return true;
    }

    private void addCookie(HttpServletResponse response, String token, MiaoShaUser user) {
        redisService.set(MiaoShaUserKey.token , token , user);
        Cookie cookie = new Cookie(COOKI_NAME_TOKEN , token);
        cookie.setMaxAge(MiaoShaUserKey.token.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
    }

}
