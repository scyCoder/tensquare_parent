package com.tensquare.spit.service;

import com.tensquare.spit.dao.SpitDao;
import com.tensquare.spit.pojo.Spit;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import util.IdWorker;

import java.util.Date;
import java.util.List;

/**
 * @program: tensquare68
 * @description:
 **/
@Service
public class SpitService {

    @Autowired
    private SpitDao spitDao;

    @Autowired
    private IdWorker idWorker;

    /**
     * 设置默认值 当对某个吐槽进行回复，上级回复数+1
     * @param spit
     */
    public void save(Spit spit) {
        spit.set_id(idWorker.nextId()+"");
        spit.setThumbup(0);
        spit.setVisits(0);
        spit.setComment(0);
        spit.setShare(0);
        spit.setPublishtime(new Date());
        spit.setState("1");
        //当进行是回复，将上级吐槽记录修改
        if(StringUtils.isNotBlank(spit.getParentid())){
            //评论
            Spit parentSpit = spitDao.findById(spit.getParentid()).get();
            parentSpit.setComment(parentSpit.getComment()+1);
            parentSpit.setVisits(parentSpit.getVisits()+1);
            spitDao.save(parentSpit);
        }
        spitDao.save(spit);
    }

    public Spit findById(String spitId) {
        return spitDao.findById(spitId).get();
    }

    public List<Spit> findAll() {
        return spitDao.findAll();
    }

    public void deleteById(String spitId) {
        spitDao.deleteById(spitId);
    }

    public Page<Spit> findByParendId(String parentid, Pageable pageable) {
        return spitDao.findByParentid(parentid, pageable);
    }

    @Autowired
    private MongoTemplate mongoTemplate;

    public void updateThumbup(String spitId) {
//        Spit spit = spitDao.findById(spitId).get();
//        spit.setThumbup(spit.getThumbup()+1);
//        spitDao.save(spit);
        //参数一：更新条件  参数二：更新内容
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(spitId));
        Update update = new Update();
        update.inc("thumbup", 1);
        //参数三：集合名称
        mongoTemplate.updateFirst(query, update, "spit");
    }
}
