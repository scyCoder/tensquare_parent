package com.tensquare.qa.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tensquare.qa.pojo.Problem;
import org.springframework.data.jpa.repository.Query;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface ProblemDao extends JpaRepository<Problem,String>,JpaSpecificationExecutor<Problem>{

    /**
     * sql:select * from tb_problem where id in (select problemid from tb_pl where labelid = 2)
     * @param labelId 标签id
     * @param pageable 分页对象
     * @return
     * @query:指定语句（sql,jpql）执行  默认执行jpql语句
     */
//    @Query(nativeQuery = true, value = "select * from tb_problem where id in (select problemid from tb_pl where labelid = ?)")
    @Query("from Problem p where p.id in (select problemid from PL where labelid = ?1)")
    Page findNewList(String labelId, Pageable pageable);

    /**
     * 根据标签id查询热门回答问题
     */
    @Query("from Problem p where p.id in (select problemid from PL where labelid = ?1) order by p.reply desc")
    Page findHotList(String labelId, Pageable pageable);

    @Query("from Problem p where p.id in (select problemid from PL where labelid = ?1) and p.reply = 0 order by p.createtime desc")
    Page findWaitList(String labelId, Pageable pageable);

}
