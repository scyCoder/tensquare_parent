package com.tensquare.friend.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient("tensquare-user")
public interface UserClient {

    @PutMapping("/user/incfans/{userid}/{x}")
    public void incFansCount(@PathVariable("userid") String userid, @PathVariable("x") int x);

    @PutMapping("/user/incfollow/{userid}/{x}")
    public void incFollowsount(@PathVariable("userid") String userid, @PathVariable("x") int x);
}
