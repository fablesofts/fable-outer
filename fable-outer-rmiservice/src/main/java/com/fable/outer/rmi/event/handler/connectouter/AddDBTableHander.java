package com.fable.outer.rmi.event.handler.connectouter;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.fable.hamal.shuttle.common.utils.constant.DbType;
import com.fable.hamal.shuttle.communication.event.Event;
import com.fable.hamal.shuttle.communication.event.EventHandler;
import com.fable.hamal.shuttle.communication.event.EventRegisterCenter;
import com.fable.outer.biz.connectouter.impl.Web2OutNetWorkServiceImpl;
import com.fable.outer.biz.connectouter.intf.Web2OutNetWorkService;
import com.fable.outer.rmi.event.dto.AddTableDto;
import com.fable.outer.rmi.event.dto.DataSourceDto;
import com.fable.outer.rmi.type.CommonEventTypes;
import com.fable.outer.rmi.type.DataBaseTypes;

@SuppressWarnings("restriction")
@Service
public class AddDBTableHander implements EventHandler{

    // oracle 查询表字段
//    private final String m_oracle_col =
//        "select column_name,data_type from user_tab_columns where table_name='";
    private static final Logger logger  = LoggerFactory.getLogger(Web2OutNetWorkServiceImpl.class);
    private Map<String,ArrayList<String>> colConditionMap = new HashMap<String,ArrayList<String>>();
    private Map<String,ArrayList<String>> colNConditionMap = new HashMap<String,ArrayList<String>>();
    private Map<String,ArrayList<String>> colMConditionMap = new HashMap<String,ArrayList<String>>();
    private Map<String,ArrayList<String>> colDConditionMap = new HashMap<String,ArrayList<String>>();
    private Map<String,ArrayList<String>> colUConditionMap = new HashMap<String,ArrayList<String>>();
    private Connection connection;
    private ResultSet rs;
    
    
    //oracle 创建序列
    private static final String oracle_seq = "CREATE SEQUENCE seq_fableetl_id" +
        " minvalue 1 maxvalue 1.0E+28" +
        "start with 1 increment by 1 cycle";
    
    
    
    /**
     * 创建增量表.
     * @param dbType dbType
     * @param ip ip
     * @param port port
     * @param dbName dbName
     * @param user user
     * @param password password
     * @param tableName tableName
     * @throws ClassNotFoundException 
     * @throws SQLException 
     */
    public void createAddTable(AddTableDto dto) throws SQLException, ClassNotFoundException {
        DataSourceDto dsdto = createDataSourceDto(dto);
        //判断是否有双向表 
        if(!findTable(dsdto,"fl_fabletriggerstatus")){
            createtable(dsdto);
        }
        
        Web2OutNetWorkService impl = new Web2OutNetWorkServiceImpl();
        String [] tablenames = null;
        if(dto.getTablename().contains(",")){
            tablenames = new  String [dto.getTablename().split(",").length];
            tablenames = dto.getTablename().split(",");
        }else {
            tablenames = new  String [1];
            tablenames[0] = dto.getTablename();
        }
        
        if  (DataBaseTypes.ORACLE.equals(dto.getDbtype())) {
            try {
                impl.executeQuery(dsdto, oracle_seq);
            }
            catch (Exception e1) {
                logger.error("the oracle etl sequence create failed the error is : {}",e1.getMessage());
            } 
        }
        if  (DataBaseTypes.DM7.equals(dto.getDbtype()) ||
                        DataBaseTypes.DM.equals(dto.getDbtype())) {
            try {
                String dm_seq = "CREATE SEQUENCE \""+dsdto.getDb_name()+"\".seq_fableetl_id" +
                                " increment by 1 start with 1 maxvalue 9223372036854775806" +
                                " minvalue 1 cycle";
                impl.executeQuery(dsdto, dm_seq);
            }
            catch (Exception e1) {
                e1.printStackTrace();
                logger.error("the oracle etl sequence create failed the error is : {}",e1.getMessage());
            } 
        }
        
        
        String[] colarr = null;
        // 判断如果数据库类型是oracle
        if ( DataBaseTypes.ORACLE.equals(dto.getDbtype()) &&
                        tablenames !=null  ) {
            for(int x=0 ;x<tablenames.length;x++){
                //先查询是否有主键 如果没有则查询是要有唯一约束 如果没有则不可以进行增量同步 
                if (getPremaryKey(dsdto,tablenames[x]).length > 0) {
                    colarr = getPremaryKey(dsdto,tablenames[x]);
                } else {
                    colarr = findTableUDX(dto,tablenames[x]);
                }
                if(colarr!=null && colarr.length!=0){
                    // 先查询是否有增量表
                    boolean flag = findTable(dsdto,"fl_"+tablenames[x]);
                    // 如果没有增量表则给表创建
                    if (!flag) {
                        StringBuffer sqlStr =
                            new StringBuffer("create table fl_" + tablenames[x] +
                                " as select ");
                        for (int i = 0; i < colarr.length; i++){
                            sqlStr.append(tablenames[x] + "." + colarr[i]+",");
                        }
                        sqlStr = sqlStr.deleteCharAt(sqlStr.length() - 1);
                        sqlStr.append(" from " + tablenames[x] + " where 1=2");
                        try {
                            // 执行创建增量表语句
                            impl.executeQuery(dsdto, sqlStr.toString()); 
                            // 给增量表创建额外两个字段（序列号，状态位）
                            StringBuffer addcol = new StringBuffer(" alter table fl_") ;
                                         addcol.append(tablenames[x])
                                               .append(" add ");  
                                addcol.append("(fableetlstate char(1),fableetlid NUMBER(28))");
                                // 执行新增字段语句
                                impl.executeQuery(dsdto, addcol.toString()); 
                              
                            String insertTriggerStatus = "insert into fl_fabletriggerstatus values('"
                                        + tablenames[x] + "',1)";
                            impl.executeQuery(dsdto,insertTriggerStatus);
                        }
                        catch (final Exception e) {
                            logger.error("the oracle addtable create failed the error is : {}",e.getMessage());
                        }
                    }
                }
            }
        }
        else if (DataBaseTypes.MYSQL.equals(dto.getDbtype()) ||
                        DataBaseTypes.DM.equals(dto.getDbtype()) &&
                        tablenames !=null) {
            //给mysql创建一个自增主键的表用来当序列
            if(DataBaseTypes.MYSQL.equals(dto.getDbtype())) {
               if( !findTable(dsdto, "fable_trigger_seq")) {
                   String createSeqSql = "create table fable_trigger_seq " 
                                         +"(seq_id int auto_increment primary key)";
                   impl.executeQuery(dsdto, createSeqSql); 
               }
            }
            for(int x=0 ;x<tablenames.length;x++){
                if (getPremaryKey(dsdto,tablenames[x]).length > 0) {
                    colarr = getPremaryKey(dsdto,tablenames[x]);
                } else {
                    colarr = findTableUDX(dto,tablenames[x]);
                }
                if(colarr!=null && colarr.length!=0){
                    // 先查询是否有增量表
                    boolean flag = findTable(dsdto,"fl_"+tablenames[x]);
                    // 如果没有增量表则给表创建
                    if (!flag) {
                        StringBuffer sqlStr = new StringBuffer("create table fl_"
                                + tablenames[x] + " as select ");
                        for (int i = 0; i < colarr.length; i++){
                            sqlStr.append(tablenames[x] + "." + colarr[i]+",");
                    }
                        sqlStr.append("10000000001 fableetlstate,10000000001 fableetlid from ")
                              .append(tablenames[x])
                              .append(" where 1=2");
                        try {
                        impl.executeQuery(dsdto, sqlStr.toString()); 
                            String insertTriggerStatus = "insert into fl_fabletriggerstatus values('"
                                    + tablenames[x] + "',1)";
                            impl.executeQuery(dsdto,insertTriggerStatus);
                        } catch (Exception e) {
                            logger.error("the mysql/dm addtable create failed the error is : {}",e.getMessage());
                        }
                    }
                }
            }
        } 
        else if (DataBaseTypes.SQLSERVER.equals(dsdto.getDb_type())) {
            if( !findTable(dsdto, "fable_trigger_seq")) {
                String createSeqSql = "create table fable_trigger_seq " 
                                      +"(seq_id int identity(1,1) primary key,value int)";
                impl.executeQuery(dsdto, createSeqSql); 
            }
            for(int x=0 ;x<tablenames.length;x++){
                ArrayList<String> colConditionList = new ArrayList<String>();
                ArrayList<String> colNConditionList = new ArrayList<String>();
                ArrayList<String> colMConditionList = new ArrayList<String>();
                ArrayList<String> colDConditionList = new ArrayList<String>();
                ArrayList<String> colUConditionList = new ArrayList<String>();
                String[] pk = getPremaryKey(dsdto,tablenames[x]);
                if (pk.length > 0) {
                    colarr = pk;
                } else {
                    colarr = findTableUDX(dto,tablenames[x]);
                }
                if(colarr!=null && colarr.length!=0){
                   
                        LinkedList<Object[]> colList = new LinkedList<Object[]>();
                        String sql = "select a.name,c.name type,a.length,a.xprec,a.xscale "
                                        + "from syscolumns a,sysobjects b,systypes c where b.name='"
                                        + tablenames[x]
                                        + "' and b.type='u' and a.id=b.id "
                                        +"and a.xtype=c.xtype and a.xusertype=c.xusertype";
                        colList = impl.executeQuery(dsdto, sql);
                        for (Object[] row : colList) {
                            for(int j = 0;j < colarr.length;j++){
                                //判断字段是否是主键
                                if(row[0].equals(colarr[j])){
                                    if (row[1].toString().equals("text")
                                                    || row[1].toString().equals("ntext")
                                                    || row[1].toString().equals("image")
                                                    || row[1].toString().equals("varbinary")
                                                    || row[1].toString().equals("binary")){
                                        colNConditionList.add(row[0].toString());
                                    } else if(row[1].toString().equals("money")){
                                        colMConditionList.add(row[0].toString());
                                    } else if(row[1].toString().equals("decimal") || row[1].toString().equals("numeric") || row[1].toString().equals("bigint")){
                                        colDConditionList.add(row[0].toString());
                                    } else if(row[1].toString().equals("uniqueidentifier")) {
                                        colUConditionList.add(row[0].toString());
                                    } else {
                                        colConditionList.add(row[0].toString());
                                    }
                                }
                            }
                        }
                        colConditionMap.put(tablenames[x], colConditionList);
                        colNConditionMap.put(tablenames[x], colNConditionList);
                        colMConditionMap.put(tablenames[x], colMConditionList);
                        colDConditionMap.put(tablenames[x], colDConditionList);
                        colUConditionMap.put(tablenames[x], colUConditionList);
                        
                        
                        // 先查询是否有增量表
                        boolean flag = findTable(dsdto,"fl_"+tablenames[x]);
                        // 如果没有增量表则给表创建
                        if (!flag) {
                            StringBuffer sqlStr = new StringBuffer("begin select " );
                            for(String col : colarr){
                                sqlStr.append(col)
                                      .append(",");
                            }
                            sqlStr.append("1 as fableetlstate,2 as fableetlid into fl_")
                                  .append(tablenames[x])
                                  .append(" from ")
                                  .append(tablenames[x])
                                  .append(" where 1=2;end;");
                            impl.executeQuery(dsdto,sqlStr.toString());
                            try {
                                String insertTriggerStatus = "insert into fl_fabletriggerstatus values('"
                                                + tablenames[x] + "',1)";
                                impl.executeQuery(dsdto,insertTriggerStatus);
                            } catch (Exception e) {
                                logger.error("the sqlserver addtable create failed the error is : {}",e.getMessage());
                            }
                        }
                }
            }    
        } else if (DataBaseTypes.DM7.equals(dsdto.getDb_type())) {
            for(int x=0 ;x<tablenames.length;x++){
                //先查询是否有主键 如果没有则查询是要有唯一约束 如果没有则不可以进行增量同步 
                if (getPremaryKey(dsdto,tablenames[x]).length > 0) {
                    colarr = getPremaryKey(dsdto,tablenames[x]);
                } else {
                    colarr = findTableUDX(dto,tablenames[x]);
                }
                if(colarr!=null && colarr.length!=0){
                    // 先查询是否有增量表
                    boolean flag = findTable(dsdto,"fl_"+tablenames[x]);
                    // 如果没有增量表则给表创建
                    if (!flag) {
                        StringBuffer sqlStr =
                            new StringBuffer("create table \""+dsdto.getDb_name()+"\".\"fl_" + tablenames[x] +
                                "\" as select ");
                        for (int i = 0; i < colarr.length; i++){
                            sqlStr.append("\""+tablenames[x] + "\".\"" + colarr[i]+"\",");
                        }
                        sqlStr = sqlStr.deleteCharAt(sqlStr.length() - 1);
                        sqlStr.append(" from \""+dsdto.getDb_name()+"\".\"" + tablenames[x] + "\" where 1=2");
                        try {
                            // 执行创建增量表语句
                            System.out.println(sqlStr.toString());
                            impl.executeQuery(dsdto, sqlStr.toString()); 
                            // 给增量表创建额外两个字段（序列号，状态位）
                            StringBuffer addcol = new StringBuffer(" alter table \""+dsdto.getDb_name()+"\".\"fl_")  
                                               .append(tablenames[x])
                                               .append("\" add fableetlstate char(1)");  
                                         System.out.println(addcol.toString());
                                         impl.executeQuery(dsdto, addcol.toString()); 
                            StringBuffer addcol1 = new StringBuffer(" alter table \""+dsdto.getDb_name()+"\".\"fl_")
                                               .append(tablenames[x])
                                               .append("\" add fableetlid NUMBER(19)");
                                         System.out.println(addcol1.toString());
                                         impl.executeQuery(dsdto, addcol1.toString()); 
                              
                            String insertTriggerStatus = "insert into \""+dsdto.getDb_name()+"\".fl_fabletriggerstatus values('"
                                        + tablenames[x] + "',1)";
                            impl.executeQuery(dsdto,insertTriggerStatus);
                        }
                        catch (final Exception e) {
                            logger.error("the DM7 addtable create failed the error is : {}",e.getMessage());
                        }
                    }
                }
            }
        }
        
    }
    /**
     * 创建触发器.
     * @param dbType dbType
     * @param ip ip
     * @param port port
     * @param dbName dbName
     * @param user user
     * @param password password
     * @param tableName tableName
     * @throws ClassNotFoundException 
     * @throws SQLException 
     */
    public void createTableTrigger(AddTableDto dto) throws SQLException, ClassNotFoundException {
        
        DataSourceDto dsdto = createDataSourceDto(dto);
        Web2OutNetWorkService impl = new Web2OutNetWorkServiceImpl();
        String [] tablenames = null;
        if(dto.getTablename().contains(",")){
            tablenames = new  String [dto.getTablename().split(",").length];
            tablenames = dto.getTablename().split(",");
        }else {
            tablenames = new  String [1];
            tablenames[0] = dto.getTablename();
        }
        
        String[] colarr = null;
        if (DataBaseTypes.ORACLE.equals(dto.getDbtype()) || 
                        DataBaseTypes.DM.equals(dto.getDbtype())) {
            for(int x=0 ;x<tablenames.length;x++){
                if(!isTrigger(dsdto,tablenames[x])){
                    // 查询表的字段 赋值给colList
                    if (getPremaryKey(dsdto,tablenames[x]).length > 0) {
                        colarr = getPremaryKey(dsdto,tablenames[x]);
                    } else {
                        colarr = findTableUDX(dto,tablenames[x]);
                    }
                    //拼接创建insert触发器sql
                    StringBuffer insertStr = new StringBuffer("");
                    //insert 需要触发的字段
                    StringBuffer colInsert = new StringBuffer("");
                    //insert 触发的值
                    StringBuffer valInsert = new StringBuffer("");
                    //拼接创建update触发器sql
                    StringBuffer updateStr = new StringBuffer("");
                    //update 前old 需要触发的字段
                    StringBuffer colUpdateOld = new StringBuffer("");
                    //update 前old 触发的值
                    StringBuffer valUpdateOld = new StringBuffer("");
                    //update 后new 需要触发的字段
                    StringBuffer colUpdateNew = new StringBuffer("");
                    //update 后new 触发的值
                    StringBuffer valUpdateNew = new StringBuffer("");
                    //拼接创建delete触发器sql
                    StringBuffer deleteStr = new StringBuffer("");
                    //delete 需要触发的字段
                    StringBuffer colDelete = new StringBuffer("");
                    //delte 触发的值
                    StringBuffer valDelete = new StringBuffer("");
                    
                    //拼接三个触发器开头
                    insertStr = insertStr
                            .append(" CREATE TRIGGER " + 
                                    tablenames[x].toUpperCase() + 
                                    "_1 AFTER INSERT ON " + 
                                    tablenames[x] + 
                                    " FOR EACH ROW declare action number; begin select status into action from fl_fabletriggerstatus where table_name='"+ 
                                    tablenames[x] + 
                                    "'; if(action=1) then INSERT INTO FL_" + 
                                    tablenames[x] + "(");
                    
                    updateStr = updateStr
                             .append(" CREATE TRIGGER " + 
                                    tablenames[x].toUpperCase() + 
                                    "_3 BEFORE UPDATE ON " + 
                                    tablenames[x] + 
                                    " FOR EACH ROW declare action number; begin select status into action from fl_fabletriggerstatus where table_name='"+ 
                                    tablenames[x] + 
                                    "'; if(action=1) then INSERT INTO FL_" + 
                                    tablenames[x] + "(");
            
                    deleteStr = deleteStr
                            .append(" CREATE TRIGGER " + 
                                    tablenames[x].toUpperCase() + 
                                    "_4 AFTER DELETE ON " + 
                                    tablenames[x] + 
                                    " FOR EACH ROW declare action number; begin select status into action from fl_fabletriggerstatus where table_name='"+ 
                                    tablenames[x] + 
                                    "'; if(action=1) then INSERT INTO FL_" + 
                                    tablenames[x] + "(");
                    
                    //遍历拼接三个触发器需要触发的字段
                    for (int i=0;i<colarr.length;i++) {
                            colInsert = colInsert.append(colarr[i] + ",");
                            valInsert = valInsert.append(" :new." + colarr[i] + ",");
                            colUpdateOld = colUpdateOld.append(colarr[i] + ",");
                            valUpdateOld = valUpdateOld.append(" :old." + colarr[i] + ",");
                            colUpdateNew = colUpdateNew.append(colarr[i] + " , ");
                            valUpdateNew = valUpdateNew.append(":new." + colarr[i] + ",");
                            colDelete = colDelete.append(colarr[i] + ",");
                            valDelete = valDelete.append(":old." + colarr[i] + ",");
                    }
                    
                    //拼接insert触发器后部
                    colInsert = colInsert
                            .append(" fableetlstate,fableetlid) values( ");
                    valInsert = valInsert
                            .append("'1',seq_fableetl_id.nextval);")
                            .append("end if;exception when others then null;end;");
                    insertStr = insertStr
                            .append(colInsert)
                            .append(valInsert);
                    
                    //拼接update触发器后部
                    colUpdateOld = colUpdateOld
                                    .append(" fableetlstate,fableetlid) values(");
                    valUpdateOld = valUpdateOld
                                    .append("'2',seq_fableetl_id.nextval);")
                                    .append("insert into fl_" + tablenames[x] + "(");
                    colUpdateNew = colUpdateNew
                                    .append("fableetlstate,fableetlid) values( ");
                    valUpdateNew = valUpdateNew
                                    .append(" '3',seq_fableetl_id.nextval);")
                                    .append("end if;exception when others then null;")
                                    .append("end;");
                    updateStr = updateStr
                                    .append(colUpdateOld)
                                    .append(valUpdateOld)
                                    .append(colUpdateNew)
                                    .append(valUpdateNew);
                   //拼接delete触发器后部 
                    colDelete = colDelete
                                    .append("fableetlstate,fableetlid) values(");
                    valDelete = valDelete
                                    .append("'4',seq_fableetl_id.nextval);")
                                    .append("end if;exception when others then null;end;");
                    deleteStr = deleteStr.append(colDelete).append(valDelete);        
                    try {
                        //创建insert触发器sql update触发器sql delete触发器sql
                        System.out.println(insertStr.toString());
                        impl.executeQuery(dsdto, insertStr.toString());
                    }catch (Exception e) {
                        logger.error("the oracle trigger_1 create failed the error is : {}",e.getMessage());
                    }
                    try {
                        System.out.println(updateStr.toString());
                        impl.executeQuery(dsdto, updateStr.toString());
                    }catch (Exception e) {
                        logger.error("the oracle trigger_3 create failed the error is : {}",e.getMessage());
                    }
                    try {    
                        System.out.println(deleteStr.toString());
                        impl.executeQuery(dsdto, deleteStr.toString());
                    }catch (Exception e) {
                        logger.error("the oracle trigger_4 create failed the error is : {}",e.getMessage());
                    }   
                }
            }
        }
        else if (DataBaseTypes.MYSQL.equals(dto.getDbtype())) {
            for(int x=0 ;x<tablenames.length;x++){
                if(!isTrigger(dsdto,tablenames[x])){
                    // 查询表的字段 赋值给colarr
                    if (getPremaryKey(dsdto,tablenames[x]).length > 0) {
                        colarr = getPremaryKey(dsdto,tablenames[x]);
                    } else {
                        colarr = findTableUDX(dto,tablenames[x]);
                    }
                    StringBuffer insertStr = new StringBuffer("");
                    insertStr = insertStr
                            .append("CREATE TRIGGER ")
                            .append( tablenames[x])
                            .append("_1 AFTER INSERT ON ")
                            .append(tablenames[x])
                            .append(" FOR EACH ROW begin DECLARE maxseq integer;")
                            .append(" declare actionint integer;select STATUS into actionint")
                            .append(" from fl_fabletriggerstatus where table_name='")    
                            .append(tablenames[x])
                            .append("';if(actionint=1) then")
                            .append(" insert into fable_trigger_seq VALUES (); ")
                            .append(" select max(seq_id) into maxseq from fable_trigger_seq;")
                            .append(" delete from fable_trigger_seq where seq_id = maxseq;")
                            .append(" insert into fl_")
                            .append(tablenames[x])
                            .append(" (");
                    StringBuffer colInsert = new StringBuffer("");
                    StringBuffer valInsert = new StringBuffer("");
                    StringBuffer updateOldStr = new StringBuffer("");
                    updateOldStr = updateOldStr
                            .append("CREATE  TRIGGER ")
                            .append(tablenames[x])
                            .append("_3 BEFORE UPDATE on ")
                            .append(tablenames[x])
                            .append(" for each row begin DECLARE maxseq integer;")
                            .append(" declare actionint integer;")
                            .append("select STATUS into actionint from fl_fabletriggerstatus")
                            .append(" where table_name='")
                            .append(tablenames[x])
                            .append("';if(actionint=1) then ")
                            .append(" insert into fable_trigger_seq VALUES (); ")
                            .append(" select max(seq_id) into maxseq from fable_trigger_seq;")
                            .append(" delete from fable_trigger_seq where seq_id = maxseq;")
                            .append(" insert into fl_")
                            .append(tablenames[x])
                            .append(" (");
                    StringBuffer colUpdateOld = new StringBuffer("");
                    StringBuffer valUpdateOld = new StringBuffer("");
                    StringBuffer colUpdateNew = new StringBuffer("");
                    StringBuffer valUpdateNew = new StringBuffer("");
                    StringBuffer deleteStr = new StringBuffer("");
                    deleteStr = deleteStr
                            .append("CREATE TRIGGER ")
                            .append(tablenames[x])
                            .append("_4 AFTER DELETE on ")
                            .append(tablenames[x])
                            .append(" for each row begin DECLARE maxseq integer;")
                            .append("declare actionint integer;select STATUS into actionint")
                            .append(" from fl_fabletriggerstatus where table_name='")
                            .append(tablenames[x])
                            .append("';if(actionint=1) then ")
                            .append(" insert into fable_trigger_seq VALUES (); ")
                            .append(" select max(seq_id) into maxseq from fable_trigger_seq;")
                            .append(" delete from fable_trigger_seq where seq_id = maxseq;")
                            .append(" insert into fl_")
                            .append(tablenames[x])
                            .append(" (");
                    StringBuffer colDelete = new StringBuffer("");
                    StringBuffer valDelete = new StringBuffer("");
                    for (String col : colarr) {
                        colInsert = colInsert.append("`"+col + "`,");
                        valInsert = valInsert.append("new." + col + ",");
                        colUpdateOld = colUpdateOld.append("`"+col + "`,");
                        valUpdateOld = valUpdateOld.append("old." + col + ",");
                        colUpdateNew = colUpdateNew.append("`"+col + "`,");
                        valUpdateNew = valUpdateNew.append("new." + col + ",");
                        colDelete = colDelete.append("`"+col + "`,");
                        valDelete = valDelete.append("old." + col + ",");
                    }
//                    //先删触发器
//                    String droptrigger1 = "drop trigger " + tablenames[x] + "_1";
//                    String droptrigger2 = "drop trigger " + tablenames[x] + "_2";
//                    String droptrigger4 = "drop trigger " + tablenames[x] + "_4";
                    try {
//                        impl.executeQuery(dsdto, droptrigger1);
//                        impl.executeQuery(dsdto, droptrigger2);
//                        impl.executeQuery(dsdto, droptrigger4);
                    } catch (Exception e) {
                        logger.error("the mysql trigger drop failed the error is : {}",e.getMessage());
                    }
                    
                    colInsert = colInsert
                            .append("fableetlstate,fableetlid) values(");
                    valInsert = valInsert.append("1,maxseq);end if;end");
                    insertStr = insertStr.append(colInsert).append(valInsert);
                    
                    
                    try {
                        impl.executeQuery(dsdto, insertStr.toString());
                    } catch (Exception e) {
                        logger.error("the mysql trigger_1 create failed the error is : {}",e.getMessage());
                    }
                    colUpdateOld = colUpdateOld.append("fableetlstate,fableetlid) values(");
                    valUpdateOld = valUpdateOld.append("2,maxseq);insert into fl_"+tablenames[x]+" (");
                    colUpdateNew = colUpdateNew.append("fableetlstate,fableetlid) values(");
                    valUpdateNew = valUpdateNew.append("3,maxseq);end if;end");
                    updateOldStr = updateOldStr.append(colUpdateOld)
                                               .append(valUpdateOld)
                                               .append(colUpdateNew)
                                               .append(valUpdateNew);
                    try {
                        impl.executeQuery(dsdto,updateOldStr.toString());
                    } catch (Exception e) {
                        logger.error("the mysql trigger_2 create failed the error is : {}",e.getMessage());
                    }
                    colDelete = colDelete
                            .append("fableetlstate,fableetlid) values(");
                    valDelete = valDelete.append("4,maxseq);end if;end");
                    deleteStr = deleteStr.append(colDelete).append(valDelete);
                    try {
                        impl.executeQuery(dsdto, deleteStr.toString());
                    } catch (Exception e) {
                        logger.error("the mysql trigger_4 create failed the error is : {}",e.getMessage());
                    }
                  } 
                }
        } 
        else if (DataBaseTypes.SQLSERVER.equals(dto.getDbtype())) {
            for(int x=0 ;x<tablenames.length;x++){
                if(!isTrigger(dsdto,tablenames[x])){
                    // 查询表的字段 赋值给colarr
                    if (getPremaryKey(dsdto,tablenames[x]).length > 0) {
                        colarr = getPremaryKey(dsdto,tablenames[x]);
                    } else {
                        colarr = findTableUDX(dto,tablenames[x]);
                    }
                    String dropinsertStr = "IF exists(select name from sysobjects where type='TR' and name='"
                                    + tablenames[x] + "_1') drop trigger " + tablenames[x] + "_3";
                            try {
                                //impl.executeQuery(dsdto, dropUpdateStr);
                            } catch (Exception e) {
                            }
                    String insertStr = "CREATE TRIGGER "
                                    + tablenames[x]
                                    + "_1 ON "
                                    + tablenames[x]
                                    + " for insert as set nocount on "
                                    + "declare "
                                    + declareColumn(getKeyType(dsdto,"fl_"+tablenames[x]))
                                    +"@maxid as int,@actionint as int set "
                                    + "@actionint=(select status from fl_fabletriggerstatus where table_name='"
                                    + tablenames[x]
                                    + "') "
                                    + "DECLARE cursor1 CURSOR FOR "
                                    + "SELECT "
                                    + SqlServerSelectcol(createBDelCol(tablenames[x]))
                                    + " from "+tablenames[x]+" a,inserted b"+createConditionCol(tablenames[x])
                                    + " if(@actionint=1)" 
                                    + " OPEN cursor1"
                                    + " FETCH NEXT FROM cursor1 INTO "
                                    + declareColumnforInto(createBDelCol(tablenames[x]))
                                    + " WHILE @@FETCH_STATUS = 0"
                                    + " BEGIN "
                                    + "insert into fable_trigger_seq values(1) " 
                                    + "set @maxid=(select seq_id from fable_trigger_seq) " 
                                    + "delete from fable_trigger_seq where seq_id = @maxid "
                                    + "insert into fl_" + tablenames[x]
                                    + "("+createDelCol(tablenames[x])+") values ("
                                    + declareColumnforInto(createBDelCol(tablenames[x]))
                                    + ",1,@maxid)"
                                    + "FETCH NEXT FROM cursor1 INTO " 
                                    + declareColumnforInto(createBDelCol(tablenames[x]))
                                    + " END CLOSE cursor1 DEALLOCATE cursor1";
                            // String
                            // insertStr="CREATE TRIGGER "+tableName+"_1 ON "+tableName+" FOR insert as declare @actionint as int set @actionint=(select status from fl_fabletriggerstatus where table_name='"+tableName+"') if(@actionint=1) begin insert into fl_"+tableName+" select *,1 from inserted end";
                            // String
                            // insertStr="CREATE TRIGGER "+tableName+"_1 ON "+tableName+" instead of insert as declare @actionint as int set @actionint=(select status from fl_fabletriggerstatus where table_name='"+tableName+"') if(@actionint=1) begin insert into fl_"+tableName+" select *,1 from inserted end";
                    
                            try {
                                impl.executeQuery(dsdto,insertStr);
                            } catch (Exception e) {
                                logger.error("the SQLServer trigger_1 create  failed the error is : {}",e.getMessage());
                            }
                    
                            String dropUpdateStr = "IF exists(select name from sysobjects where type='TR' and name='"
                                    + tablenames[x] + "_3') drop trigger " + tablenames[x] + "_3";
                            try {
                                //impl.executeQuery(dsdto, dropUpdateStr);
                            } catch (Exception e) {
                            }
                            String updateStr = "CREATE TRIGGER "
                                    + tablenames[x]
                                    + "_3 ON "
                                    + tablenames[x]
                                    + " for update as set nocount on " 
                                    + "declare "
                                    + declareColumn(getKeyType(dsdto,"fl_"+tablenames[x]))
                                    +"@maxid as int,@actionint as int set "
                                    + "@actionint=(select status from fl_fabletriggerstatus where table_name='"
                                    + tablenames[x]
                                    + "') "
                                    
                                    + "DECLARE cursor2 CURSOR FOR "
                                    + "SELECT "
                                    + SqlServerSelectcol(createBDelCol(tablenames[x]))
                                    + " from deleted b"
                                    + " if(@actionint=1)" 
                                    + " OPEN cursor2"
                                    + " FETCH NEXT FROM cursor2 INTO "
                                    + declareColumnforInto(createBDelCol(tablenames[x]))
                                    + " WHILE @@FETCH_STATUS = 0"
                                    + " BEGIN "
                                    + "insert into fable_trigger_seq values(1) " 
                                    + "set @maxid=(select seq_id from fable_trigger_seq) " 
                                    + "delete from fable_trigger_seq where seq_id = @maxid "
                                    + "insert into fl_" + tablenames[x]
                                    + "("+createDelCol(tablenames[x])+") values ("
                                    + declareColumnforInto(createBDelCol(tablenames[x]))
                                    + ",2,@maxid)"
                                    + "FETCH NEXT FROM cursor2 INTO " 
                                    + declareColumnforInto(createBDelCol(tablenames[x]))
                                    + " END CLOSE cursor2 DEALLOCATE cursor2 "
                                    
                                    
                                    + "DECLARE cursor1 CURSOR FOR "
                                    + "SELECT "
                                    + SqlServerSelectcol(createBDelCol(tablenames[x]))
                                    + " from "+tablenames[x]+" a,inserted b"+createConditionCol(tablenames[x])
                                    + " if(@actionint=1)" 
                                    + " OPEN cursor1"
                                    + " FETCH NEXT FROM cursor1 INTO "
                                    + declareColumnforInto(createBDelCol(tablenames[x]))
                                    + " WHILE @@FETCH_STATUS = 0"
                                    + " BEGIN "
                                    + "insert into fable_trigger_seq values(1) " 
                                    + "set @maxid=(select seq_id from fable_trigger_seq) " 
                                    + "delete from fable_trigger_seq where seq_id = @maxid "
                                    + "insert into fl_" + tablenames[x]
                                    + "("+createDelCol(tablenames[x])+") values ("
                                    + declareColumnforInto(createBDelCol(tablenames[x]))
                                    + ",3,@maxid)"
                                    + "FETCH NEXT FROM cursor1 INTO " 
                                    + declareColumnforInto(createBDelCol(tablenames[x]))
                                    + " END CLOSE cursor1 DEALLOCATE cursor1";
                            
                            
                            try {
                                impl.executeQuery(dsdto,updateStr);
                            } catch (Exception e) {
                                logger.error("the SQLServer trigger_2 create failed the error is : {}",e.getMessage());
                            }
                    
                            String dropDeleteStr = "IF exists(select name from sysobjects where type='TR' and name='"
                                    + tablenames[x] + "_4') drop trigger " + tablenames[x] + "_4";
                            try {
                                //impl.executeQuery(dsdto,dropDeleteStr);
                            } catch (Exception e) {
                            }
                            String deleteStr = "CREATE TRIGGER "
                                    + tablenames[x]
                                    + "_4 ON "
                                    + tablenames[x]
                                    + " for delete as set nocount on "
                                    + "declare "
                                    + declareColumn(getKeyType(dsdto,"fl_"+tablenames[x]))
                                    +"@maxid as int,@actionint as int set "
                                    + "@actionint=(select status from fl_fabletriggerstatus where table_name='"
                                    + tablenames[x]
                                    + "') "
                                    + "DECLARE cursor1 CURSOR FOR "
                                    + "SELECT "
                                    + createBDelCol(tablenames[x]).substring(0,createBDelCol(tablenames[x]).lastIndexOf(","))
                                    + " FROM deleted"
                                    + " if(@actionint=1)" 
                                    + " OPEN cursor1"
                                    + " FETCH NEXT FROM cursor1 INTO "
                                    + declareColumnforInto(createBDelCol(tablenames[x]))
                                    + " WHILE @@FETCH_STATUS = 0"
                                    + " BEGIN "
                                    + "insert into fable_trigger_seq values(1) " 
                                    + "set @maxid=(select seq_id from fable_trigger_seq) " 
                                    + "delete from fable_trigger_seq where seq_id = @maxid "
                                    + "insert into fl_" + tablenames[x]
                                    + "("+createDelCol(tablenames[x])+") values ("
                                    + declareColumnforInto(createBDelCol(tablenames[x]))
                                    + ",4,@maxid)"
                                    + "FETCH NEXT FROM cursor1 INTO " 
                                    + declareColumnforInto(createBDelCol(tablenames[x]))
                                    + " END CLOSE cursor1 DEALLOCATE cursor1";
                            try {
                                impl.executeQuery(dsdto,deleteStr);
                            } catch (Exception e) {
                                logger.error("the SQLServer trigger_4 create failed the error is : {}",e.getMessage());
                            }
                }
            }    
        } else if (DataBaseTypes.DM7.equals(dsdto.getDb_type())) {
            for(int x=0 ;x<tablenames.length;x++){
                if(!isTrigger(dsdto,tablenames[x])){
                    // 查询表的字段 赋值给colList
                    if (getPremaryKey(dsdto,tablenames[x]).length > 0) {
                        colarr = getPremaryKey(dsdto,tablenames[x]);
                    } else {
                        colarr = findTableUDX(dto,tablenames[x]);
                    }
                    //拼接创建insert触发器sql
                    StringBuffer insertStr = new StringBuffer("");
                    //insert 需要触发的字段
                    StringBuffer colInsert = new StringBuffer("");
                    //insert 触发的值
                    StringBuffer valInsert = new StringBuffer("");
                    //拼接创建update触发器sql
                    StringBuffer updateStr = new StringBuffer("");
                    //update 前old 需要触发的字段
                    StringBuffer colUpdateOld = new StringBuffer("");
                    //update 前old 触发的值
                    StringBuffer valUpdateOld = new StringBuffer("");
                    //update 后new 需要触发的字段
                    StringBuffer colUpdateNew = new StringBuffer("");
                    //update 后new 触发的值
                    StringBuffer valUpdateNew = new StringBuffer("");
                    //拼接创建delete触发器sql
                    StringBuffer deleteStr = new StringBuffer("");
                    //delete 需要触发的字段
                    StringBuffer colDelete = new StringBuffer("");
                    //delte 触发的值
                    StringBuffer valDelete = new StringBuffer("");
                    
                    //拼接三个触发器开头
                    insertStr = insertStr
                            .append(" CREATE TRIGGER \""+dsdto.getDb_name()+"\".\"" + 
                                    tablenames[x] + 
                                    "_1\" AFTER INSERT ON " + 
                                    "\""+dsdto.getDb_name()+"\".\"" + tablenames[x] + "\"" + 
                                    " FOR EACH ROW declare action number; begin select status into action from fl_fabletriggerstatus where table_name='"+ 
                                    tablenames[x] + 
                                    "'; if(action=1) then "+
                                    " INSERT INTO \""+dsdto.getDb_name()+"\".\"fl_" + 
                                    tablenames[x] + "\"(");
                    
                    updateStr = updateStr
                             .append(" CREATE TRIGGER \""+dsdto.getDb_name()+"\".\"" + 
                                    tablenames[x] + 
                                    "_3\" BEFORE UPDATE ON " + 
                                    "\""+dsdto.getDb_name()+"\".\"" + tablenames[x] + "\"" + 
                                    " FOR EACH ROW declare action number; begin select status into action from fl_fabletriggerstatus where table_name='"+ 
                                    tablenames[x] + 
                                    "'; if(action=1) then "+
                                    " INSERT INTO \""+dsdto.getDb_name()+"\".\"fl_" + 
                                    tablenames[x] + "\"(");
            
                    deleteStr = deleteStr
                            .append(" CREATE TRIGGER \""+dsdto.getDb_name()+"\".\"" + 
                                    tablenames[x] + 
                                    "_4\" AFTER DELETE ON " + 
                                    "\""+dsdto.getDb_name()+"\".\"" + tablenames[x] + "\"" + 
                                    " FOR EACH ROW declare action number; begin select status into action from fl_fabletriggerstatus where table_name='"+ 
                                    tablenames[x] + 
                                    "'; if(action=1) then "+
                                    " INSERT INTO \""+dsdto.getDb_name()+"\".\"fl_" + 
                                    tablenames[x] + "\"(");
                    //遍历拼接三个触发器需要触发的字段
                    for (int i=0;i<colarr.length;i++) {
                            colInsert = colInsert.append("\""+colarr[i] + "\",");
                            valInsert = valInsert.append(" :new.\"" + colarr[i] + "\",");
                            colUpdateOld = colUpdateOld.append("\""+colarr[i] + "\",");
                            valUpdateOld = valUpdateOld.append(" :old.\"" + colarr[i] + "\",");
                            colUpdateNew = colUpdateNew.append("\""+colarr[i] + "\", ");
                            valUpdateNew = valUpdateNew.append(":new.\"" + colarr[i] + "\",");
                            colDelete = colDelete.append("\""+colarr[i] + "\",");
                            valDelete = valDelete.append(":old.\"" + colarr[i] + "\",");
                    }
                    
                    //拼接insert触发器后部
                    colInsert = colInsert
                            .append(" fableetlstate,fableetlid) values( ");
                    valInsert = valInsert
                            .append("'1',\""+dsdto.getDb_name()+"\".seq_fableetl_id.nextval);")
                            .append("end if; exception when others then null;end;");
                    insertStr = insertStr
                            .append(colInsert)
                            .append(valInsert);
                    
                    //拼接update触发器后部
                    colUpdateOld = colUpdateOld
                                    .append(" fableetlstate,fableetlid) values(");
                    valUpdateOld = valUpdateOld
                                    .append("'2',\""+dsdto.getDb_name()+"\".seq_fableetl_id.nextval);")
                                    .append("insert into \""+dsdto.getDb_name()+"\".\"fl_" + tablenames[x] + "\"(");
                    colUpdateNew = colUpdateNew
                                    .append("fableetlstate,fableetlid) values( ");
                    valUpdateNew = valUpdateNew
                                    .append(" '3',\""+dsdto.getDb_name()+"\".seq_fableetl_id.nextval);")
                                    .append("end if;exception when others then null;")
                                    .append("end;");
                    updateStr = updateStr
                                    .append(colUpdateOld)
                                    .append(valUpdateOld)
                                    .append(colUpdateNew)
                                    .append(valUpdateNew);
                   //拼接delete触发器后部 
                    colDelete = colDelete
                                    .append("fableetlstate,fableetlid) values(");
                    valDelete = valDelete
                                    .append("'4',seq_fableetl_id.nextval);")
                                    .append("end if;exception when others then null;end;");
                    deleteStr = deleteStr.append(colDelete).append(valDelete);        
                    try {
                        //创建insert触发器sql update触发器sql delete触发器sql
                        System.out.println(insertStr.toString());
                        impl.executeQuery(dsdto, insertStr.toString());
                    }catch (Exception e) {
                        logger.error("the dm7 trigger_1 create failed the error is : {}",e.getMessage());
                    }
                    try {
                        System.out.println(updateStr.toString());
                        impl.executeQuery(dsdto, updateStr.toString());
                    }catch (Exception e) {
                        logger.error("the dm7 trigger_3 create failed the error is : {}",e.getMessage());
                    }
                    try {
                        System.out.println(deleteStr.toString());
                        impl.executeQuery(dsdto, deleteStr.toString());
                    }catch (Exception e) {
                        logger.error("the dm7 trigger_4 create failed the error is : {}",e.getMessage());
                    }   
                }
            }
        }
    }
    /**
     * SqlServer 拼注解
     * @param column Map(columName,columnType)
     * @return
     */
    public String declareColumn(Map<String, String> column) {
        String declareColumn = "";
        Set<String> keys = column.keySet();
        for(String key : keys){
            declareColumn +="@"+key+" as "+column.get(key)+",";
        }
        return declareColumn;
    }
    
    /**
     * SqlServer 拼@
     * @param column Map(columName,columnType)
     * @return
     */
    public String declareColumnforInto(String column) {
        String declareColumn = "";
        String[] columns = column.split(",");
        for(String col : columns){
            declareColumn +="@"+col+",";
        }
        declareColumn = declareColumn.substring(0, declareColumn.lastIndexOf(","));
        return declareColumn;
    }
    
    
    public String createCol(String tablename){
        StringBuffer col=new StringBuffer("");
        for(String colCondition:colConditionMap.get(tablename)){
           col.append(colCondition).append(",");
        }
        for(String colCondition:colDConditionMap.get(tablename)){
               col.append(colCondition).append(",");
        }
        for(String colCondition:colNConditionMap.get(tablename)){
               col.append(colCondition).append(",");
        }
        for(String colCondition:colMConditionMap.get(tablename)){
               col.append(colCondition).append(",");
        }
        for(String colCondition:colUConditionMap.get(tablename)){
               col.append(colCondition).append(",");
        }
        col.append("fableetlstate,fableetlid");
        return col.toString();
    }
    
    public String createDelCol(String tablename){
        StringBuffer col=new StringBuffer("");
        for(String colCondition:colConditionMap.get(tablename)){
           col.append(colCondition).append(",");
        }
        for(String colCondition:colDConditionMap.get(tablename)){
               col.append(colCondition).append(",");
        }
        for(String colCondition:colUConditionMap.get(tablename)){
               col.append(colCondition).append(",");
        }
        col.append("fableetlstate,fableetlid");
        return col.toString();
    }
    
    public String createBDelCol(String tablename){
        StringBuffer col=new StringBuffer("");
        for(String colCondition:colConditionMap.get(tablename)){
               col.append(colCondition).append(",");
        }
        for(String colCondition:colDConditionMap.get(tablename)){
               col.append(colCondition).append(",");
        }
        for(String colCondition:colUConditionMap.get(tablename)){
               col.append(colCondition).append(",");
        }
        return col.toString();
    }
    
    public String createBCol(String tablename){
        StringBuffer col=new StringBuffer("");
        for(String colCondition:colConditionMap.get(tablename)){
               col.append("b.").append(colCondition).append(",");
        }
        for(String colCondition:colDConditionMap.get(tablename)){
               col.append("b.").append(colCondition).append(",");
        }
        for(String colCondition:colNConditionMap.get(tablename)){
                   col.append("a.").append(colCondition).append(",");
        }
        for(String colCondition:colMConditionMap.get(tablename)){
               col.append("isnull(b.").append(colCondition).append(",0),");
        }
        for(String colCondition:colUConditionMap.get(tablename)){
               col.append("b.").append(colCondition).append(",");
        }
        return col.toString();
    }
    
    public String createConditionCol(String tablename){
        StringBuffer conditionCol=new StringBuffer(" where ");
        for(String colCondition:colConditionMap.get(tablename)){
                conditionCol.append("isnull(a.")
                            .append(colCondition)
                            .append(",'')=isnull(b.")
                            .append(colCondition)
                            .append(",'') and ");
        }
        for(String colCondition:colDConditionMap.get(tablename)){
            conditionCol.append("isnull(a.")
                        .append(colCondition)
                        .append(",'0')=isnull(b.")
                        .append(colCondition)
                        .append(",'0') and ");
        }
        for(String colCondition:colUConditionMap.get(tablename)){
            conditionCol.append("isnull(a.")
                        .append(colCondition)
                        .append(",'ffffffff-ffff-ffff-ffff-ffffffffffff')=isnull(b.")
                        .append(colCondition)
                        .append(",'ffffffff-ffff-ffff-ffff-ffffffffffff') and ");
        }
        conditionCol=new StringBuffer(conditionCol.substring(0, conditionCol.lastIndexOf("and ")));
        return conditionCol.toString();
    }
    
    public String SqlServerSelectcol(String cols){
        String columns = "";
        String[] column = cols.split(",");
        for(String col : column) {
            columns += "b."+col+",";
        }
        columns = columns.substring(0,columns.lastIndexOf(","));
        return columns;
    } 
    
    
    /**
     * 获取字段的名称，类型，大小
     * @param dsdto 数据源对象
     * @param tableName 表名
     * @return
     */
    public Map<String,String> getKeyType(DataSourceDto dsdto,String tableName) {
        Map<String,String> KeyType = new HashMap<String,String>();
        try {
        rs = getConnection(dsdto).getMetaData().getColumns(null, null, tableName,null);
            while (rs.next()){
               if(!"fableetlstate".equalsIgnoreCase(rs.getString("COLUMN_NAME")) &&
                               !"fableetlid".equalsIgnoreCase(rs.getString("COLUMN_NAME"))){
                   if(!"int".equalsIgnoreCase(rs.getString("TYPE_NAME"))) {
                       KeyType.put(rs.getString("COLUMN_NAME"), rs.getString("TYPE_NAME")+"("+rs.getString("COLUMN_SIZE")+")" );
                   } else {
                       KeyType.put(rs.getString("COLUMN_NAME"), rs.getString("TYPE_NAME"));
                   }
               }
            }
        }
        catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            close();
        }
        return KeyType;
    }
    
    
    
    /**
     * 获取各个数据库相应表的 主键和唯一约束.
     * @param dto 增量表传输对象
     * @param tableName 表名
     * @return 返回各个数据库对应表名的主键或唯一约束
     * @throws SQLException 查询失败
     * @throws ClassNotFoundException 驱动未找到
     */
    private String[] findTableUDX(AddTableDto dto , String tableName) throws SQLException, ClassNotFoundException {
        StringBuffer  selectkey = null;
        if (DataBaseTypes.ORACLE.equals(dto.getDbtype())) {
            selectkey = new StringBuffer("select distinct b.column_name from sys.user_indexes a,")
            .append(" sys.user_ind_columns b where")
            .append(" a.index_name = b.index_name")
            .append(" and a.uniqueness = 'UNIQUE'")
            .append(" and upper(a.table_name) = upper('")
            .append(tableName)
            .append("') and upper(a.table_owner) = upper('")
            .append(dto.getUsername())
            .append("')");
        }
        else if (DataBaseTypes.MYSQL.equals(dto.getDbtype())) {
            selectkey = new StringBuffer("SELECT distinct K.COLUMN_NAME FROM")
            .append(" information_schema.TABLE_CONSTRAINTS T,")
            .append(" information_schema.KEY_COLUMN_USAGE K")
            .append(" where T.TABLE_NAME = K.TABLE_NAME and")
            .append(" T.CONSTRAINT_NAME = K.CONSTRAINT_NAME")
            .append(" and upper(T.TABLE_NAME) = upper('")
            .append(tableName)
            .append("') and upper(T.CONSTRAINT_SCHEMA)=upper('")
            .append(dto.getDbname())
            .append("') and T.CONSTRAINT_SCHEMA=K.CONSTRAINT_SCHEMA")
            .append(" and T.CONSTRAINT_TYPE in ('PRIMARY KEY','UNIQUE')");
        }
        else if (DataBaseTypes.SQLSERVER.equals(dto.getDbtype())) {
            selectkey = new StringBuffer("select distinct c.name from sysindexkeys a,sysindexes b ,")
            .append(" syscolumns c ,sysobjects d")
            .append(" where a.id=b.id and a.id=c.id")
            .append(" and a.colid=c.colid and a.id=d.id")
            .append(" and a.indid=b.indid and upper(d.name)=upper('")
            .append(tableName)
            .append("')");
        }
        else if (DataBaseTypes.DM.equals(dto.getDbtype())) {
            selectkey = new StringBuffer("SELECT distinct E.NAME AS COLUMN_NAME")
            .append(" FROM SYSDBA.SYSTABLES B,SYSDBA.SYSCONSTRAINTS A,SYSDBA.SYSCOLUMNS E ")
            .append(" WHERE A.TABLEID=B.ID AND A.TABLEID=E.ID ")
            .append(" AND (A.TYPE ='P' or a.type = 'U') AND B.TYPE='U' and b.NAME = '")
            .append(tableName)
            .append("'");
        } 
        else if (DataBaseTypes.DM7.equals(dto.getDbtype())){
            selectkey = new StringBuffer("select distinct b.COLUMN_NAME from USER_CONSTRAINTS a,user_cons_columns b where (a.CONSTRAINT_TYPE='P' or a.CONSTRAINT_TYPE='U')")
                        .append(" and a.CONSTRAINT_NAME =b.CONSTRAINT_NAME")
                        .append(" and a.TABLE_NAME = '")
                        .append(tableName)
                        .append("'");
        }
        //定义一个为DM存储唯一索引的数组
        List<String> li_DM = new ArrayList<String>();
        if (DataBaseTypes.DM.equals(dto.getDbtype()) ||
                        DataBaseTypes.DM7.equals(dto.getDbtype())) {
            rs = getConnection(createDataSourceDto(dto)).getMetaData().getIndexInfo(null, dto.getDbname(), tableName, true, false);
            while (rs.next()) {
                if(!li_DM.contains(rs.getString("COLUMN_NAME"))) {
                    li_DM.add(rs.getString("COLUMN_NAME"));
                }
            }
            close();
        }
        if (selectkey != null) {
            DataSourceDto dsdto = createDataSourceDto(dto);
            Web2OutNetWorkService impl = new Web2OutNetWorkServiceImpl();
            LinkedList<Object[]> resultList =
                            impl.executeQuery(dsdto, selectkey.toString());
            String[] result = new String[resultList.size()];
            int i = 0;
            for (Object[] objects : resultList) {
                result[i++] = (String)objects[0];
            }
            if ((DataBaseTypes.DM.equals(dto.getDbtype()) ||
                            DataBaseTypes.DM7.equals(dto.getDbtype())) &&
                            li_DM.size()>0) {
                for (String key : result) {
                   if(!li_DM.contains(key)) {
                       li_DM.add(key);
                   }
                }
                return li_DM.toArray(new String[li_DM.size()]);
            } else {
                return result;
            }
        }
        return new String[] {};
    }
    
    /**
     * 封装数据源
     * @param dto 增量表传输对象
     * @return 数据源传输对象
     */
    public DataSourceDto createDataSourceDto(AddTableDto dto){
        DataSourceDto dsdto = new DataSourceDto();
        dsdto.setUsername(dto.getUsername());
        dsdto.setPassword(dto.getPassword());
        dsdto.setConnect_url(dto.getConnect_url());
        dsdto.setDb_type(dto.getDbtype());
        dsdto.setDb_name(dto.getDbname());
        return dsdto;
    }
    
    /**
     * 查询是否有表
     * @param dsdto 数据源传输对象
     * @param tablename 表名
     * @return 查询是否有表
     * @throws SQLException 
     */
    public boolean findTable(DataSourceDto dsdto,String tablename) throws SQLException{
        List<Object[]> tableList =
                        new LinkedList<Object[]>();
        StringBuffer tableStr = null;
        Web2OutNetWorkService impl = new Web2OutNetWorkServiceImpl();
        if(DataBaseTypes.ORACLE.equals(dsdto.getDb_type())) {
            tableStr = new StringBuffer("SELECT table_name FROM USER_TABLES WHERE TABLE_NAME='")
            .append(tablename.toUpperCase())
            .append("'");
        } else if(DataBaseTypes.MYSQL.equals(dsdto.getDb_type())) {
            tableStr = new StringBuffer("select table_name from information_schema.tables where table_schema='")
            .append(dsdto.getDb_name())
            .append("'")
            .append(" and table_name ='")
            .append(tablename)
            .append("'");
        } else if(DataBaseTypes.SQLSERVER.equals(dsdto.getDb_type())) {
            tableStr = new StringBuffer("select name from sysobjects where type='u' and name = '")
            .append(tablename)
            .append("'");
        } else if(DataBaseTypes.DM.equals(dsdto.getDb_type())) {
            tableStr=new StringBuffer("SELECT NAME FROM "+dsdto.getDb_name()+".SYSDBA.SYSTABLES");
            tableStr.append(" WHERE  NAME  ='")
            .append(tablename)
            .append("'");
        } else if(DataBaseTypes.DM7.equals(dsdto.getDb_type())) {
//            tableStr=new StringBuffer("SELECT TABLE_NAME FROM USER_TABLES");
//            tableStr.append(" WHERE TABLE_NAME ='")
//                    .append(tablename)
//                    .append("'");
            final String[] types = { "TABLE", "VIEW", "ALIAS", "SYNONYM" };
            rs = getConnection(dsdto).getMetaData().getTables(null, dsdto.getDb_name(), tablename, types);
            while (rs.next()) {
                return true;
            }
            close();
            return false;
        }
        try {
            tableList = impl.executeQuery(dsdto, tableStr.toString());
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
        if(tableList.size()>0){
            return true;
        }
        else {
            return false;
        }
    }
    
    /**
     * 关闭连接.
     */
    private void close() {
        try {
            if (this.rs != null)
                this.rs.close();
        }
        catch (final SQLException e) {
            logger.error("ResultSet close error the error message is: {}",e.getMessage());
        }
        finally {
            try {
                if (this.connection != null)
                    this.connection.close();
            }
            catch (final SQLException e) {
                logger.error("connection close error the error message is: {}",e.getMessage());
            }
        }
    }
    
    
    /**
     * 创建双向表.
     * @param dsdto 数据传输对象
     */
    public void createtable(DataSourceDto dsdto){
        String triggerStatus ="";
        Web2OutNetWorkService impl = new Web2OutNetWorkServiceImpl();
        if (DataBaseTypes.ORACLE.equals(dsdto.getDb_type())) {
            triggerStatus = "create table fl_fabletriggerstatus(table_name varchar2(30) primary key,status number)";
        } else if (DataBaseTypes.SQLSERVER.equals(dsdto.getDb_type()) || 
                        DataBaseTypes.DM.equals(dsdto.getDb_type()) 
                        ) {
            triggerStatus = "create table fl_fabletriggerstatus(table_name varchar(30) primary key,status int)";
        } else if (DataBaseTypes.MYSQL.equals(dsdto.getDb_type())) {
            triggerStatus = "create table fl_fabletriggerstatus(table_name varchar(30) primary key,status integer)";
        } else if (DataBaseTypes.DM7.equals(dsdto.getDb_type())) {
            triggerStatus = "create table \""+dsdto.getDb_name()+"\".fl_fabletriggerstatus(table_name varchar(30) primary key,status int)";
        }
        
        try {
            impl.executeQuery(dsdto, triggerStatus);
        }
        catch (SQLException e) {
            // TODO Auto-generated catch block
            logger.error("the triggerstatus table create failed the error is : {}",e.getMessage());
        }
        catch (ClassNotFoundException e) {
            logger.error("the driver not find the error is : {}",e.getMessage());
        }
    }
    /**
     * 删除触发器.
     * @param tablename 表名
     * @param ds 数据源
     */
    public void dropTrigger(DataSourceDto ds,String tablename){
        Web2OutNetWorkService impl = new Web2OutNetWorkServiceImpl();
        if (DataBaseTypes.DM7.equals(ds.getDb_type())) {
            String droptrigger1 = "drop trigger \""+ds.getDb_name()+"\".\"" + tablename + "\"_1";
            String droptrigger2 = "drop trigger \""+ds.getDb_name()+"\".\"" + tablename + "\"_1";
            String droptrigger4 = "drop trigger \""+ds.getDb_name()+"\".\"" + tablename + "\"_1";
            try {
                impl.executeQuery(ds, droptrigger1);
                impl.executeQuery(ds, droptrigger2);
                impl.executeQuery(ds, droptrigger4);
            }
            catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
           
        } else {
            String droptrigger1 = "drop trigger " + tablename + "_1";
            String droptrigger2 = "drop trigger " + tablename + "_2";
            String droptrigger4 = "drop trigger " + tablename + "_4";
            try {
                impl.executeQuery(ds, droptrigger1);
                impl.executeQuery(ds, droptrigger2);
                impl.executeQuery(ds, droptrigger4);
            } catch (Exception e) {
                logger.error("the drop trigger error the error is : {}",e.getMessage());
            }
        }
    }
    
    /**
     * 删除增量表.
     * @param tablename 表名
     * @param ds 数据源
     */
    public void dropfltable(DataSourceDto ds,String tablename){
        Web2OutNetWorkService impl = new Web2OutNetWorkServiceImpl();
            String dropfltable = "drop table fl_" + tablename;
            try {
                impl.executeQuery(ds, dropfltable);
            } catch (Exception e) {
                logger.error("the drop addtable error the error is : {}",e.getMessage());
            }
    }
    
    /**
     * 只查询表中主键 .
     * @param dsdto 数据源对象
     * @param tablename 表名
     * @return
     */
    public String[] getPremaryKey (DataSourceDto dsdto,String tablename) {
        List<String> pkcols = new ArrayList<String>();
        try {
            String schema ="";
            if (DataBaseTypes.ORACLE.equals(dsdto.getDb_type())) {
                schema = dsdto.getUsername().toUpperCase();
            } else if (DataBaseTypes.SQLSERVER.equals(dsdto.getDb_type())) {
                schema = null;
            } else {
                schema = dsdto.getDb_name();
            }
            rs = getConnection(dsdto).getMetaData().getPrimaryKeys(null, schema, tablename);
            while (rs.next()) {
                pkcols.add(rs.getString("COLUMN_NAME"));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }finally {
            close();
        }
        return pkcols.toArray(new String[pkcols.size()]);
    }
    
    
    /**
     * 查询是否存在触发器
     * @param ds 数据源
     * @param tablename 表名
     * @return
     */
    public boolean isTrigger(DataSourceDto ds,String tablename) {
        Web2OutNetWorkService impl = new Web2OutNetWorkServiceImpl();
        List<Object[]> li = new LinkedList<Object[]>(); 
        if(DataBaseTypes.ORACLE.equals(ds.getDb_type())) {
            String oraclesql = "SELECT NAME FROM USER_SOURCE"  
                            +" WHERE  TYPE='TRIGGER' and NAME = '"
                            +tablename.toUpperCase()
                            +"_1'"
                            +" OR NAME = '"
                            +tablename.toUpperCase()
                            +"_3'"
                            +" OR NAME = '"
                            +tablename.toUpperCase()
                            +"_4'";
            try {
                li = impl.executeQuery(ds, oraclesql);
            }
            catch (SQLException e) {
                logger.error("select trigger error the error is : {}",e.getMessage());
            }
            catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (li != null) {
                if(li.size()==3){
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else if(DataBaseTypes.MYSQL.equals(ds.getDb_type())) {
                String mysql = "SELECT TRIGGER_NAME FROM information_schema.`TRIGGERS`"
                                +" where TRIGGER_NAME = '"
                                +tablename
                                +"_1'"
                                +" OR RIGGER_NAME = '"
                                +tablename
                                +"_3'"
                                +" OR RIGGER_NAME = '"
                                +tablename
                                +"_4'";
                try {
                    li = impl.executeQuery(ds, mysql);
                }
                catch (SQLException e) {
                    logger.error("select trigger error the error is : {}",e.getMessage());
                }
                catch (ClassNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if (li != null) {
                    if(li.size()==3){
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            
        } else if(DataBaseTypes.SQLSERVER.equals(ds.getDb_type())) {
           String SqlServer = "select name from sysobjects where xtype='TR' where name = '" 
                           +tablename
                           +"_1' or name ='"
                           +tablename
                           +"_3' or name ='"
                           +tablename
                           +"_4'";
        } else if(DataBaseTypes.DM.equals(ds.getDb_type())) {
           String DM6 = "select NAME from \"" 
                           +ds.getDb_name()
                           +"\".\"SYSDBA\".\"SYSTRIGGERS\" WHERE NAME = '"
                           +tablename
                           +"_1' OR NAME = '"
                           +tablename
                           +"_3' OR NAME = '"
                           +tablename
                           +"_4'";
           try {
               li = impl.executeQuery(ds, DM6);
           }
           catch (SQLException e) {
               logger.error("select trigger error the error is : {}",e.getMessage());
           }
           catch (ClassNotFoundException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
           }
           if (li != null) {
               if(li.size()==3){
                   return true;
               } else {
                   return false;
               }
           } else {
               return false;
           }
        } else if(DataBaseTypes.DM7.equals(ds.getDb_type())) {
            String DM7 = "select OBJECT_NAME from user_objects where object_type = 'TRIGGER' and "
                            +"OBJECT_NAME = '"
                            +tablename
                            +"_1' OR OBJECT_NAME = '"
                            +tablename
                            +"_3' OR OBJECT_NAME = '"
                            +tablename
                            +"_4'";
            try {
                li = impl.executeQuery(ds, DM7);
            }
            catch (SQLException e) {
                logger.error("select trigger error the error is : {}",e.getMessage());
            }
            catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (li != null) {
                if(li.size()==3){
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
        return false;
    } 
    
    private Connection getConnection(DataSourceDto ds) {
        String driver="";
        if (DataBaseTypes.ORACLE.equals(ds.getDb_type())){
            driver = "oracle.jdbc.driver.OracleDriver";
        }
        else if (DataBaseTypes.MYSQL.equals(ds.getDb_type())){
            driver = "com.mysql.jdbc.Driver";
        }
        else if (DataBaseTypes.SQLSERVER.equals(ds.getDb_type())){
            driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        }
        else if (DataBaseTypes.DM.equals(ds.getDb_type())){
            driver = "dm6.jdbc.driver.DmDriver";
        }
        else if (DataBaseTypes.DM7.equals(ds.getDb_type())){
            driver = "dm.jdbc.driver.DmDriver";
        }
        try {
            Class.forName(driver);
        }
        catch (final ClassNotFoundException e) {
        }
        try {
            try {
                if (DataBaseTypes.DM.equals(ds.getDb_type())){
                                    Thread.sleep(1000);
                }
            }
            catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            this.connection =
                DriverManager.getConnection(ds.getConnect_url(),
                    ds.getUsername(), ds.getPassword());
        }
        catch (final Exception e) {
        }
        return this.connection;
    }
    
    
    @PostConstruct
    public void init() {
        EventRegisterCenter.regist(CommonEventTypes.ADDDBTABLE, this);
    }
    
    public Object handle(Event event) {
        if(CommonEventTypes.ADDDBTABLE.equals(event.getType())){
            try {
                AddTableDto addDTO = (AddTableDto)event.getData();
                createAddTable(addDTO);
                String [] tablenames;
                if(addDTO.isRebuildTrigger()){
                    if(addDTO.getTablename().contains(",")) {
                        tablenames = addDTO.getTablename().split(",");
                        for(int i = 0;i<tablenames.length;i++) {
                            dropTrigger(createDataSourceDto(addDTO),tablenames[i]);
                        }
                    } else {
                        dropTrigger(createDataSourceDto(addDTO),addDTO.getTablename());
                    }
                }
                createTableTrigger(addDTO);
            }
            catch (SQLException e) {
                e.printStackTrace();
                logger.error("the create AddTable or TableTrigger error the error is : {}",e.getMessage());
            }
            catch (ClassNotFoundException e) {
                logger.error("the driver not find the error is : {}",e.getMessage());
            }
        }
        return null;
    }
}
