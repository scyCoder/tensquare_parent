package com.tensquare.friend.dao;

import com.tensquare.friend.pojo.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface FriendDao extends JpaRepository<Friend, String> {

    @Query("select count(f) from Friend f where f.userid = ?1 and f.friendid = ?2")
    public int selectCount(String userid, String friendid);

    @Query("update Friend f set f.islike = ?3 where f.userid = ?1 and f.friendid = ?2")
    @Modifying
    public void updateIslike(String userid, String friendid, String islike);

    @Query("delete from Friend f where f.userid =?1 and f.friendid=?2")
    @Modifying
    void deleteFriend(String userId, String friendid);
}
