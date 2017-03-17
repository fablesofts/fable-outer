package com.fable.outer.dao.ftpserver.impl;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.fable.dsp.core.dao.hibernate.UniversalDaoHibernate;
import com.fable.hamal.shuttle.common.model.config.FtpMapping;
import com.fable.outer.dao.ftpserver.intf.FtpMappingDao;

@Repository("ftpMappingDao")
public class FtpMappingDaoHibernate extends UniversalDaoHibernate implements FtpMappingDao {

    @Override
    public boolean insertFtpMapping(FtpMapping ftpMapping) {

        
        String sql = "INSERT INTO `dsp_ftp_mapping` (`id`,`INNER_ADDRESS`, `INNER_USERNAME`, `OUTER_ADDRESS`, `OUTER_USERNAME`, `USER_FLAG`) VALUES(?,?,?,?,?,?)";
        Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
        query.setParameter(0, ftpMapping.getId());
        query.setParameter(1, ftpMapping.getInnerAddress());
        query.setParameter(2, ftpMapping.getInnerUserName());
        query.setParameter(3, ftpMapping.getOuterAddress());
        query.setParameter(4, ftpMapping.getOuterUserName());
        query.setParameter(5, ftpMapping.getUserFlag());
        int flag = query.executeUpdate();
        if(flag > 0){
            return true;
        }
        
        return false;
    }

    @Override
    public boolean updateFtpMapping(FtpMapping ftpMapping) {
        String sql = "UPDATE dsp_ftp_mapping SET INNER_ADDRESS = ? , INNER_USERNAME = ? , OUTER_ADDRESS = ? , OUTER_USERNAME = ?, USER_FLAG = ? WHERE id = ?";
        Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
        query.setParameter(0, ftpMapping.getInnerAddress());
        query.setParameter(1, ftpMapping.getInnerUserName());
        query.setParameter(2, ftpMapping.getOuterAddress());
        query.setParameter(3, ftpMapping.getOuterUserName());
        query.setParameter(4, ftpMapping.getUserFlag());
        query.setParameter(5, ftpMapping.getId());
        int flag = query.executeUpdate();
        if(flag > 0){
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteFtpMapping(FtpMapping ftpMapping) {
        String sql = "DELETE FROM dsp_ftp_mapping WHERE id = ?";
        Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
        query.setParameter(0, ftpMapping.getId());
        int flag = query.executeUpdate();
        if(flag > 0){
            return true;
        }
        return false;
    }

}
