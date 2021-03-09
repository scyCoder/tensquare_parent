package com.tensquare.friend.service;

import com.tensquare.friend.clients.UserClient;
import com.tensquare.friend.dao.FriendDao;
import com.tensquare.friend.dao.NoFriendDao;
import com.tensquare.friend.pojo.Friend;
import com.tensquare.friend.pojo.NoFriend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @program: tensquare68
 * @description:
 **/
@Service
public class FriendService {

    @Autowired
    private FriendDao friendDao;

    @Autowired
    private UserClient userClient;

    /**
     * 关注好友
     * @param userId
     * @param friendid
     * @return
     */
    @Transactional
    public String addFriend(String userId, String friendid) {
        //先判断是否是重复添加好友
        if(friendDao.selectCount(userId, friendid)>0){
            return "0";
        }
        Friend friend = new Friend();
        friend.setUserid(userId);
        friend.setFriendid(friendid);
        friend.setIslike("0");   //单向喜欢（关注）
        friendDao.save(friend);
        //判断是否互粉状态
        if(friendDao.selectCount(friendid, userId)>0){
            //互相关注 将islike 改为“1”
            friendDao.updateIslike(userId, friendid, "1");
            friendDao.updateIslike(friendid, userId, "1");
        }

        //喜欢好友（关注好友）
        //对于当前用户关注数+1
        userClient.incFollowsount(userId, 1);
        //对于对方好友粉丝数+1
        userClient.incFansCount(friendid, 1);
        return "1";
    }

    @Autowired
    private NoFriendDao noFriendDao;

    public void addNoFriend(String userId, String friendid) {
        NoFriend noFriend = new NoFriend();
        noFriend.setUserid(userId);
        noFriend.setFriendid(friendid);
        noFriendDao.save(noFriend);
    }

    /**
     * 删除好友
     * @param userId
     * @param friendid
     */
    @Transactional
    public void deleteFrind(String userId, String friendid) {
        //1、将好友表中记录 我关注对方记录删除
        friendDao.deleteFriend(userId, friendid);
        //2、更新好友表中 对方关注我记录中islike改为0 单向喜欢
        friendDao.updateIslike(friendid, userId, "0");
        //3、向非好友表中添加记录
        addNoFriend(userId, friendid);

        //取关操作
        //对于当前用户关注粉丝数-1
        userClient.incFansCount(userId, -1);
        //对于对方好友关注数-1
        userClient.incFollowsount(friendid, -1);
    }
}
