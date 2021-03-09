package com.tensquare.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tensquare.user.pojo.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface UserDao extends JpaRepository<User,String>,JpaSpecificationExecutor<User>{

    public User findByMobile(String mobile);

    @Query("update User u  set u.fanscount = u.fanscount+?2 where u.id=?1")
    @Modifying
    void incFansCount(String userid, int x);


    @Query("update User u  set u.followcount = u.followcount+?2 where u.id=?1")
    @Modifying
    void incFollowsount(String userid, int x);
}
