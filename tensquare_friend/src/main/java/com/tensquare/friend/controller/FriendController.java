package com.tensquare.friend.controller;

import com.tensquare.friend.service.FriendService;
import entity.Result;
import entity.StatusCode;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @program: tensquare68
 * @description:
 **/
@RestController
@RequestMapping("friend")
@CrossOrigin
public class FriendController {

    @Autowired
    private FriendService friendService;

    @Autowired
    private HttpServletRequest request;

    /**
     * 添加好友/非好友
     * @param friendid
     * @param type 1:喜欢（关注） 2：不喜欢（疲敝）
     * @return
     */
    @PutMapping("/like/{friendid}/{type}")
    public Result like(@PathVariable String friendid, @PathVariable String type){
        //1、先获取当前登陆用户 获取id
        Claims user_claims = (Claims) request.getAttribute("user_claims");
        if(user_claims!=null){
            //2、添加喜欢（关注）好友 疲敝好友等功能
            String userId = user_claims.getId();
            if(type.equals("1")){
                //喜欢  ret:如果是1：添加成功 如果是0：重复添加好友
                String ret = friendService.addFriend(userId, friendid);
                if(ret.equals("1")){
                    return new Result(true, StatusCode.OK, "关注对方成功");
                }
                return  new Result(true, StatusCode.ERROR, "重复关注！");
            }
            //不喜欢
            friendService.addNoFriend(userId, friendid);
            return new Result(true, StatusCode.OK, "屏蔽好友成功！");
        }
        return new Result(false, StatusCode.ACCESS_ERROR, "无权访问");


    }

    /**
     * 互粉成功后，其中一方删除对方
     * @param friendid
     * @return
     */
    @DeleteMapping("/{friendid}")
    public Result deleteFrind(@PathVariable String friendid){
        //1、先获取当前登陆用户 获取id
        Claims user_claims = (Claims) request.getAttribute("user_claims");
        if(user_claims!=null) {
            //2、添加喜欢（关注）好友 疲敝好友等功能
            String userId = user_claims.getId();
            friendService.deleteFrind(userId, friendid);
            return new Result(true, StatusCode.OK, "删除好友成功");
        }
        return new Result(false, StatusCode.ACCESS_ERROR, "无权访问");
    }
}
