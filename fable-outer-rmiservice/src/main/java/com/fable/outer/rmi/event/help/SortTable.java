package com.fable.outer.rmi.event.help;

import java.util.List;

import com.fable.hamal.shuttle.common.model.config.metadata.Db;
import com.fable.hamal.shuttle.common.utils.constant.DbType;

/**
 * 判断是否有主从表的工具类
 * @author Administrator
 *
 */
public class SortTable {
    
    //更具表名查询主表
    public static String findPTableSQL(Db db,String tablename){
        
        if (DbType.ORACLE_FULL.equals(db.getDbType())) {
            return "select b.table_name ptable from "
                    + "(select table_name,r_constraint_name from user_constraints "
                    + "where constraint_type='R' and status='ENABLED') a," 
                    + "(select table_name,constraint_name "
                    + "from user_constraints where constraint_type='P' "
                    + "or constraint_type='U') b "
                    + " where a.r_constraint_name=b.constraint_name " 
                    + "and a.table_name = '"
                    + tablename.toUpperCase()+"'"
                    + " order by a.table_name";
        } else if (DbType.MSSQL_FULL.equals(db.getDbType())) {
            return "select (select name from sysobjects where id=rkeyid) ptable "
                   +" from sysforeignkeys "
                   +" where (select name from sysobjects where id=fkeyid)='"
                   +tablename+"'";
        } else if (DbType.MYSQL_FULL.equals(db.getDbType())) {
            return "select referenced_table_name ptable from" 
                    +" information_schema.REFERENTIAL_CONSTRAINTS"
                    +" where constraint_schema='"
                    +db.getDbName()+"'"
                    +" and table_name='"
                    +tablename+"'";
        } else if (DbType.DAMENG6_FULL.equals(db.getDbType())) {
          return "select (select b.name from sysdba.sysindexes a,sysdba.systables b"
                 +" where b.type='U' and a.tableid=b.id and a.id=c.rid) ptable"
                 +" from sysdba.sysrefconstraints c"
                 +" where (select b.name from sysdba.sysindexes a,sysdba.systables b"
                 +" where b.type='U' and a.tableid=b.id and a.id=c.fid)='"
                 +tablename+"'";
        } else {
            return "";
        }
    }
    
    //更具表名查询从表
    public static String findFTableSQL(Db db,String tablename){
        if (DbType.ORACLE_FULL.equals(db.getDbType())) {
            return "select a.table_name ftable from "
                    + "(select table_name,r_constraint_name from user_constraints "
                    + "where constraint_type='R' and status='ENABLED') a," 
                    + "(select table_name,constraint_name "
                    + "from user_constraints where constraint_type='P' "
                    + "or constraint_type='U') b "
                    + "and b.table_name ptable = '"
                    + tablename.toUpperCase()+"'"
                    +" where a.r_constraint_name=b.constraint_name order by a.table_name";
        } else if (DbType.MSSQL_FULL.equals(db.getDbType())) {
            return "select (select name from sysobjects where id=fkeyid) ftable"
                   +" from sysforeignkeys "
                   +" where (select name from sysobjects where id=rkeyid)='"
                   +tablename+"'";
        } else if (DbType.MYSQL_FULL.equals(db.getDbType())) {
            return "select table_name ftable from" 
                    +" information_schema.REFERENTIAL_CONSTRAINTS"
                    +" where constraint_schema='"
                    +db.getDbName()+"'"
                    +" and referenced_table_name='"
                    +tablename+"'";
        } else if (DbType.DAMENG6_FULL.equals(db.getDbType())) {
          return "select (select b.name from sysdba.sysindexes a,sysdba.systables b"
                 +" where b.type='U' and a.tableid=b.id and a.id=c.fid) ftable"
                 +" from sysdba.sysrefconstraints c"
                 +" where (select b.name from sysdba.sysindexes a,sysdba.systables b"
                 +" where b.type='U' and a.tableid=b.id and a.id=c.rid)='"
                 +tablename+"'";
        } else {
            return "";
        }
    }
}
