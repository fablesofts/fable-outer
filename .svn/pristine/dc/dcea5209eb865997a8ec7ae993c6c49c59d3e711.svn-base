package com.fable.outer.dao.ftpserver.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.fable.dsp.core.dao.hibernate.UniversalDaoHibernate;
import com.fable.hamal.shuttle.common.model.config.FtpUser;
import com.fable.outer.dao.ftpserver.intf.FtpUserDao;

@Repository("ftpUserDao")
public class FtpUserDaoHibernate extends UniversalDaoHibernate implements FtpUserDao {

    @Override
    public boolean insertFtpUser(FtpUser ftpUser) {
        try {
            String sql = "INSERT INTO ftp_user (userid, userpassword, homedirectory, enableflag) VALUES(?,?,?,?)";
            Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
            query.setParameter(0, ftpUser.getFtpUsername());
            query.setParameter(1, ftpUser.getFtpPassword());
            query.setParameter(2, ftpUser.getHomeDirectory());
            query.setParameter(3, ftpUser.getWebFlag());
            int flag = query.executeUpdate();
            if(flag > 0){
                return true;
            }
        }
        catch (HibernateException e) {
            e.printStackTrace();
            return false;
        }
        
       return false;
    }

    @Override
    public List<FtpUser> getFtpUserList() {
        
        String sql = "SELECT * FROM ftp_user";
        
        List<com.fable.outer.dmo.ftpserver.FtpUser> ftpUserList = (List<com.fable.outer.dmo.ftpserver.FtpUser>)this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql).addEntity(com.fable.outer.dmo.ftpserver.FtpUser.class).list();
//        System.out.println("#####count:"+ftpUserList.size()+"#####");
        List<FtpUser> ftpUserDtoList = new ArrayList<FtpUser>();
        for(com.fable.outer.dmo.ftpserver.FtpUser ftp : ftpUserList){
            FtpUser ftpUser = new FtpUser();
            ftpUser.setFtpUsername(ftp.getUserid());
            ftpUser.setFtpPassword(ftp.getUserpassword());
            ftpUser.setHomeDirectory(ftp.getHomeDirectory());
            ftpUser.setWebFlag(ftp.getEnableflag());
            ftpUserDtoList.add(ftpUser);
        }
        return ftpUserDtoList;
    }

    @Override
    public boolean updateFtpUser(FtpUser ftpUser) {
        
        String sql = "UPDATE ftp_user SET homedirectory = ? WHERE userid = ?";
        Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
        query.setParameter(0, ftpUser.getHomeDirectory());
        query.setParameter(1, ftpUser.getFtpUsername());
        int flag = query.executeUpdate();
        if(flag > 0){
            return true;
        }
        return false;
    }

    @Override
    public boolean updateFtpUserPass(FtpUser ftpUser) {
        String sql = "UPDATE ftp_user SET userpassword = ?  WHERE userid = ?";
        Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
        query.setParameter(0, ftpUser.getFtpPassword());
        query.setParameter(1, ftpUser.getFtpUsername());
        int flag = query.executeUpdate();
        if(flag > 0){
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteFtpUser(FtpUser ftpUser) {
        String sql = "SELECT * FROM dsp_ftp_mapping WHERE OUTER_USERNAME = '" + ftpUser.getFtpUsername() + "'";
        List<com.fable.outer.dmo.ftpserver.FtpUser> ftpUserList = (List<com.fable.outer.dmo.ftpserver.FtpUser>)this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql).addEntity(com.fable.outer.dmo.ftpserver.FtpMapping.class).list();
        System.out.println("######"+ftpUserList.size()+"#######");
        if(ftpUserList.size() > 0){
            return false;
        }
        sql = "DELETE FROM ftp_user WHERE userid = ?";
        Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
        query.setParameter(0, ftpUser.getFtpUsername());
        int flag = query.executeUpdate();
        if(flag > 0){
            return true;
        }
        return false;
    }

    @Override
    public FtpUser getFtpUser(String userid) {
        
        String sql = "SELECT * FROM ftp_user WHERE userid = '" + userid + "'";
        com.fable.outer.dmo.ftpserver.FtpUser ftpUser = (com.fable.outer.dmo.ftpserver.FtpUser)this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql).addEntity(com.fable.outer.dmo.ftpserver.FtpUser.class).uniqueResult();
        FtpUser ftpDto = new FtpUser();
        ftpDto.setFtpUsername(ftpUser.getUserid());
        ftpDto.setFtpPassword(ftpUser.getUserpassword());
        ftpDto.setHomeDirectory(ftpUser.getHomeDirectory());
        ftpDto.setWebFlag(ftpUser.getEnableflag());
        return ftpDto;
    }
    
    
    
}
