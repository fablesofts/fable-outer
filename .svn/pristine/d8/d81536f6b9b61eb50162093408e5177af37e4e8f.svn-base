package com.fable.outer.biz.connectouter.impl;

import java.net.InetAddress;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fable.dsp.common.dto.dataswitch.ColumnMappingDto;
import com.fable.dsp.common.util.TransferSqlTypeUtil;
import com.fable.hamal.shuttle.common.model.config.metadata.Db;
import com.fable.hamal.shuttle.common.utils.ListFlPlTableUtil;
import com.fable.outer.biz.connectouter.intf.Web2OutNetWorkService;
import com.fable.outer.biz.exception.ConnectException;
import com.fable.outer.biz.exception.DBTypeException;
import com.fable.outer.biz.exception.DataBaseException;
import com.fable.outer.rmi.event.dto.AddTableDto;
import com.fable.outer.rmi.event.dto.ColumnDto;
import com.fable.outer.rmi.event.dto.DataSourceDto;
import com.fable.outer.rmi.event.dto.ListColumnDataSourceDto;
import com.fable.outer.rmi.event.dto.TreeDataDto;
import com.fable.outer.rmi.type.DataBaseTypes;

/**
 * @author zhangl
 */
public class Web2OutNetWorkServiceImpl implements Web2OutNetWorkService {
    
    private static final Logger logger  = LoggerFactory.getLogger(Web2OutNetWorkServiceImpl.class);
    private static final String COL_TABLE_NAME = "TABLE_NAME";
    private String driver = "";
    private final int timeout = 3000;
    /**
     * 数据库连接.
     */
    private Connection connection = null;

    /**
     * 数据结果集.
     */
    private ResultSet rs;

    private List<TreeDataDto> list_table_tree;

    /**
     * @param ds
     *        数据源
     * @throws ClassNotFoundException
     *         未找到驱动
     * @throws SQLException
     *         连接失败
     * @return 连接Connection
     */
    public Connection getConnection(final DataSourceDto ds) {
        try {
            Class.forName(this.driver);
        }
        catch (final ClassNotFoundException e) {
            logger.error("driver not find the error message is: {}",e.getMessage());
        }
        try {
            try {
                if (DataBaseTypes.DM.equals(ds.getDb_type())){
                    Thread.sleep(1000);
                }
            }
            catch (InterruptedException e) {
            }
            this.connection =
                DriverManager.getConnection(ds.getConnect_url(),
                    ds.getUsername(), ds.getPassword());
        }
        catch (final SQLException e) {
        	e.printStackTrace();
            logger.error("Connection get failed the error message is: {}",e.getMessage());
            e.printStackTrace();
        }
        return this.connection;
    }

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
     * 该方法用于更具数据库类型判断赋值驱动.
     * 
     * @param dbtype
     *        数据库类型（枚举）
     */
    public void dbTypeToDriver(final DataBaseTypes dbtype) {
        if (DataBaseTypes.ORACLE.equals(dbtype)){
            this.driver = "oracle.jdbc.driver.OracleDriver";
        }
        else if (DataBaseTypes.MYSQL.equals(dbtype)){
            this.driver = "com.mysql.jdbc.Driver";
        }
        else if (DataBaseTypes.SQLSERVER.equals(dbtype)){
            this.driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        }
        else if (DataBaseTypes.DM.equals(dbtype)){
            this.driver = "dm6.jdbc.driver.DmDriver";
        }
        else if (DataBaseTypes.DM7.equals(dbtype) || 
                        DataBaseTypes.OTHER.equals(dbtype) ){
            this.driver = "dm.jdbc.driver.DmDriver";
        }
        else{
            if (logger.isDebugEnabled()) {
                logger.debug("数据库类型不支持");
            }
            throw new DBTypeException("数据库类型不支持！");
        }
    }

    /**
     * 获取oracle的表.
     * 
     * @param dbmd
     *        根据connect 获得的 DatabaseMetaData对象
     * @param dbName
     *        数据库名
     * @throws SQLException
     *         ...
     */
    public void getOracleTable(final DatabaseMetaData dbmd,
        final String dbName) throws SQLException {

        final String user = dbmd.getUserName();

        while (this.rs.next()) {

            final String schem = this.rs.getString("TABLE_SCHEM");
            if (user.equalsIgnoreCase(schem)) {
                final TreeDataDto dto = new TreeDataDto();
                dto.setText(this.rs
                    .getString(Web2OutNetWorkServiceImpl.COL_TABLE_NAME));
                dto.setId(this.rs
                    .getString(Web2OutNetWorkServiceImpl.COL_TABLE_NAME));
                // m_tableList.add(new
                // TableDto(rs.getString("TABLE_NAME"),new Long(index++)));
                this.list_table_tree.add(dto);
            }
        }
    }

    /**
     * 获取其他数据库的表.
     * 
     * @throws SQLException
     *         ...
     */
    public void getOtherDBTable() throws SQLException {
        while (this.rs.next()) {
            final TreeDataDto dto = new TreeDataDto();
            dto.setText(this.rs
                .getString(Web2OutNetWorkServiceImpl.COL_TABLE_NAME));
            dto.setId(this.rs
                .getString(Web2OutNetWorkServiceImpl.COL_TABLE_NAME));
            // m_tableList.add(new TableDto(rs.getString("TABLE_NAME"),new
            // Long(index++)));

            this.list_table_tree.add(dto);
            // m_tableList.add(new TableDto(rs.getString("TABLE_NAME"),new
            // Long(index++)));

        }
    }
    /**
     * 查询表集合.
     * 
     * @param ds
     *        数据源对象
     * @return 表对象集合
     */
    public List<TreeDataDto> findListTable(final DataSourceDto ds) {
        // 根据数据库类型选择对应驱动
        this.dbTypeToDriver(ds.getDb_type());
        try {
            // 打开连接
            if (this.getConnection(ds) != null) {
                // 用connection方法得到下面的DatabaseMetaData对象
                final DatabaseMetaData dbmd =
                    this.connection.getMetaData();
                // int index=0;
                // 视图，同义词
                final String[] types =
                    { "TABLE", "VIEW", "ALIAS", "SYNONYM" };
                if (this.list_table_tree == null)
                    this.list_table_tree = new ArrayList<TreeDataDto>();
                else
                    this.list_table_tree.clear();

                /**
                 * DatabaseMetaData类的getTables方法 ResultSet getTables(String
                 * catalog, String schemaPattern, String tableNamePattern,
                 * String[] types) throws SQLException检索可在给定类别中使用的表的描述。
                 * 仅返回与类别、模式、表名称和类型标准匹配的表描述。 它们根据 TABLE_TYPE、TABLE_SCHEM 和
                 * TABLE_NAME 进行排序。
                 */
                this.rs = dbmd.getTables(null, ds.getDb_name(), null, types);
                final String dbName = dbmd.getDatabaseProductName();
                if (dbName.equalsIgnoreCase("Oracle"))
                    this.getOracleTable(dbmd, dbName);
                else
                    this.getOtherDBTable();
            }

        }
        catch (final Exception e) {
            logger.error("Failed to acquire tables of a datasource the error message is: {}",e.getMessage());
            throw new DataBaseException("Failed to acquire tables of a datasource.");

        }
        finally {

            this.close();

        }

        return this.list_table_tree;
    }
    
    /**
     * @param ds
     *        数据源对象
     * @return 网络连接是否成功
     */
    public String judgNetWorkLink(final DataSourceDto ds) {
        try {
            // 创建socket测试端口是否能见建立连接
            final Socket so =
                new Socket(ds.getServer_ip(), ds.getPort().intValue());
            so.close();
            return ds.getServer_ip() + ": 网络连接成功，" + ds.getPort() + ": 端口连接通畅";
            // 当端口测试不通过，抛出异常
        }
        catch (final Exception e) {
            boolean status;
            try {
                // 对ip进行进一步测试判断ip是否能通
                status =
                    InetAddress.getByName(ds.getServer_ip()).isReachable(
                        this.timeout);
                if (status){
                    if (logger.isDebugEnabled()) {
                        logger.debug("The network connect state is: [{}]",ds.getServer_ip() + "连接成功，" + ds.getPort() +
                            "端口连接失败 ");
                    }
                    return ds.getServer_ip() + "连接成功，" + ds.getPort() +
                                    "端口连接失败 ";
                }
                else{
                    if (logger.isDebugEnabled()) {
                        logger.debug("The network connect state is: [{}]",ds.getServer_ip() + "网络连接失败");
                    }
                    return ds.getServer_ip() + "网络连接失败";
                    
                }
            }
            catch (final Exception e1) {
                if (logger.isDebugEnabled()) {
                    logger.debug("The network connect state is: [{}]",ds.getServer_ip() + "网络连接失败");
                }
                return "IP连接失败";
            }
        }
    }

    /**
     * @param ds
     *        数据源对象
     * @return 数据库连接是否成功
     */
    public boolean judgDBConnect(final DataSourceDto ds) {
        this.dbTypeToDriver(ds.getDb_type());
        if (this.getConnection(ds) != null){
            if (logger.isInfoEnabled()) {
                logger.info("database connect success~!");
            }
            this.close();
            return true;
        }
        else{
            if (logger.isInfoEnabled()) {
                logger.info("database connect failed~!");
            }
            return false;
        }
    }

    /**
     * @param sql
     *        sql 语句
     * @param ds
     *        数据源
     * @return 结果集
     */
    public LinkedList<Object[]> executeQuery(final DataSourceDto ds,
        final String sql) {
        this.dbTypeToDriver(ds.getDb_type());
        Statement stat = null;
        Connection conn = null;
        conn = this.getConnection(ds);
        if (conn == null) {
            logger.error("database connect error");
            throw new ConnectException("数据库连接异常");
        }
        //进行sql的执行
        else {
            try {
                stat = conn.createStatement();
                conn.setAutoCommit(false);
                stat = conn.createStatement();
                //判断sql是DML还是DDL
                return this.checkDML(sql) ? this.executeDML(sql, stat) :
                    this.executeDDL(sql, stat);
            }
            catch (final Exception e) {
                if (conn != null)
                    try {
                        conn.rollback();
                    }
                    catch (final SQLException e1) {
                        logger.error("Failed to acquire tables of a datasource the error message is:{}",e.getMessage());
                    }
            }
            finally {
                if (stat != null)
                    try {
                        stat.close();
                    }
                    catch (final SQLException e) {
                        logger.error("Statement close error the error message is:{}",e.getMessage());
                    }
                if (conn != null) {
                    try {
                        conn.commit();
                    }
                    catch (final SQLException e) {
                        logger.error("Connection commit error the error message is:{}",e.getMessage());
                    }
                    try {
                        conn.close();
                    }
                    catch (final SQLException e) {
                        logger.error("Connection commit error the error message is:{}",e.getMessage());
                    }
                }
            }
        }
        return null;
    }

    /**
     * @param sql
     *        sql语句
     * @return
     */
    private boolean checkDML(String sql) {
        sql = sql.trim().toUpperCase();
        if (sql.startsWith("INSERT") || sql.startsWith("UPDATE") ||
            sql.startsWith("DELETE") || sql.startsWith("DROP") ||
            sql.startsWith("CREATE") || sql.startsWith("ALTER") ||
            sql.startsWith("BEGIN") || sql.startsWith("DELIMITER") ||
            sql.startsWith("IF") || sql.startsWith("SET"))
            return false;
        return true;
    }

    /**
     * 执行ddl语句.
     * 
     * @param sql
     *        sql语句
     * @param stmt
     * @return 结果集
     * @throws SQLException
     */
    private LinkedList<Object[]> executeDDL(final String sql,
        final Statement stmt) throws SQLException {
        stmt.executeUpdate(sql);
        final LinkedList<Object[]> result = new LinkedList<Object[]>();
        result.add(new Object[] {});
        return result;
    }

    /**
     * 执行dml语句.
     * 
     * @param sql
     *        sql语句
     * @param stmt
     * @return 结果集
     * @throws SQLException
     */
    private LinkedList<Object[]> executeDML(final String sql,
        final Statement stmt) throws SQLException {
        final ResultSet rst = stmt.executeQuery(sql);
        final LinkedList<Object[]> result = new LinkedList<Object[]>();
        Object[] row = null;
        final ResultSetMetaData rsmd = rst.getMetaData();
        final int iCountCol = rsmd.getColumnCount();
        while (rst.next()) {
            row = new Object[iCountCol];
            for (int i = 0; i < iCountCol; i++)
                row[i] = rst.getObject(i + 1);
            result.add(row);
        }
        rst.close();
        return result;
    }

   

	@Override
	public List<TreeDataDto> listTableWithoutFl(DataSourceDto ds) {
		 List<TreeDataDto>list=new ArrayList<TreeDataDto>();
		List<Object[]> tableList =
                new LinkedList<Object[]>();
                StringBuffer tableStr = null;
                if(DataBaseTypes.ORACLE.equals(ds.getDb_type())){
                	 tableStr = new StringBuffer("SELECT table_name FROM USER_TABLES");
                	 tableStr.append(" WHERE table_name NOT LIKE 'FL_%'");
                }else if(DataBaseTypes.MYSQL.equals(ds.getDb_type())) {
                	tableStr=new StringBuffer("SELECT TABLE_NAME FROM information_schema.tables WHERE TABLE_SCHEMA='"+ds.getDb_name()+"'");
                	tableStr.append("  AND TABLE_NAME  NOT LIKE 'FL_%'");
                }else if(DataBaseTypes.SQLSERVER.equals(ds.getDb_type())) {
                	tableStr=new StringBuffer("SELECT * FROM SYSOBJECTS WHERE XTYPE='U' AND CATEGORY=0");
                	tableStr.append(" AND NAME  NOT LIKE 'FL_%'");
                }else if(DataBaseTypes.DM.equals(ds.getDb_type())) {
                	tableStr=new StringBuffer("SELECT NAME FROM "+ds.getDb_name()+".SYSDBA.SYSTABLES");
                	tableStr.append(" WHERE  NAME  NOT LIKE 'FL_%' AND  NAME  NOT LIKE 'SYS_%'");
                }
                if(DataBaseTypes.DM7.equals(ds.getDb_type())||DataBaseTypes.OTHER.equals(ds.getDb_type())) {
                	list=this.findListTable(ds);
                	for(int i=0;i<list.size();i++) {
                		if((list.get(i).getText().indexOf("FL_")==0) || (list.get(i).getText().indexOf("fl_")==0)) {
                			list.remove(list.get(i));
                		}
                	}
                }else{
                	 tableList=this.executeQuery(ds,tableStr.toString());
                	 for(int i=0;i<tableList.size();i++){
                		 if("fable_trigger_seq".equals(tableList.get(i)[0].toString())){
                			 continue;
                		 }else{
                			TreeDataDto dto=new TreeDataDto();
                          	dto.setId(tableList.get(i)[0].toString());
                          	dto.setText(tableList.get(i)[0].toString());
                          	list.add(dto);
                		 }
                     	
                     }
                }
                return list;
	}

	@Override
	public List<ColumnMappingDto> listColumnByTable(ListColumnDataSourceDto dto) {
		//根据数据库类型选择对应驱动
		List<ColumnMappingDto>list=new ArrayList<ColumnMappingDto>();
    	List<ColumnDto>sourcelist=new ArrayList<ColumnDto>();
    	List<ColumnDto>targetlist=new ArrayList<ColumnDto>();
    	this.dbTypeToDriver(dto.getSourceDto().getDb_type());
    	//打开连接
    	if(this.getConnection(dto.getSourceDto()) !=null) {
    		try {
				final DatabaseMetaData metaData=connection.getMetaData();
				ResultSet rs=metaData.getColumns(null,null,dto.getSourceTable(), null);
				while(rs.next()) {
					//name,index,type
					ColumnDto columnDto=new ColumnDto();
					columnDto.setColumnName(rs.getString("COLUMN_NAME"));
					columnDto.setColumnIndex(rs.getInt("ORDINAL_POSITION"));
					columnDto.setColumnType(rs.getInt("DATA_TYPE"));
					sourcelist.add(columnDto);
				}
			} catch (SQLException e) {
				throw new RuntimeException(e);
			} finally {
				this.close();
			}
    	}
    	this.dbTypeToDriver(dto.getTargetDto().getDb_type());
    	if(this.getConnection(dto.getTargetDto()) !=null) {
    		try {
				final DatabaseMetaData metaData=connection.getMetaData();
				ResultSet rs=metaData.getColumns(null,null,dto.getTargetTable(), null);
				while(rs.next()) {
					//name,index,type
					ColumnDto columnDto=new ColumnDto();
					columnDto.setColumnName(rs.getString("COLUMN_NAME"));
					columnDto.setColumnIndex(rs.getInt("ORDINAL_POSITION"));
					columnDto.setColumnType(rs.getInt("DATA_TYPE"));
					targetlist.add(columnDto);
				}
			} catch (SQLException e) {
				throw new RuntimeException(e);
			} finally {
				this.close();
			}
    	}
    	//
    	int length;
		if(sourcelist.size()==targetlist.size()) {
			length=sourcelist.size();
		}else{
			length=sourcelist.size()>targetlist.size()?sourcelist.size():targetlist.size();
		}
		try {
			for(int i=0; i<length ;i++) {
				ColumnMappingDto columnMappingDto=new ColumnMappingDto();
				if(i<sourcelist.size()) {
					columnMappingDto.setSourceColumnIndex(sourcelist.get(i).getColumnIndex());
					columnMappingDto.setSourceColumnName(sourcelist.get(i).getColumnName());
					columnMappingDto.setSourceColumnType(TransferSqlTypeUtil.transferFromSqlTypeToText(sourcelist.get(i).getColumnType()));
				}
				if(i<targetlist.size()) {
					columnMappingDto.setTargetColumnIndex(targetlist.get(i).getColumnIndex());
					columnMappingDto.setTargetColumnName(targetlist.get(i).getColumnName());
					columnMappingDto.setTargetColumnType(TransferSqlTypeUtil.transferFromSqlTypeToText(targetlist.get(i).getColumnType()));
				}
				list.add(columnMappingDto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return list;
	}
	
	@Override
	public List<Object[]> listFTable(AddTableDto addTableDto) {
		//根据主表查从表
		Db db=new Db();
		db.setDbType(addTableDto.getDbType());
		db.setDbName(addTableDto.getDbname());
		DataSourceDto dataSourceDto=new DataSourceDto();
		dataSourceDto.setConnect_url(addTableDto.getConnect_url());
		dataSourceDto.setDb_name(addTableDto.getDbname());
		dataSourceDto.setDb_type(addTableDto.getDbtype());
		dataSourceDto.setPassword(addTableDto.getPassword());
		dataSourceDto.setUsername(addTableDto.getUsername());
		String sql=null;
		try {
			sql = ListFlPlTableUtil.findFTableSQL(db,addTableDto.getTablename());
		} catch (Exception e) {
			e.printStackTrace();
		}
		LinkedList<Object[]>list=this.executeQuery(dataSourceDto,sql);
		return list;
	}
	
	/**
	 * 查询表中时间字段
	 * @param ds 数据源传输对象
	 * @param tableName 表名
	 * @return
	 */
	public List<ColumnDto> dateColumnName (ListColumnDataSourceDto columnDataSourceDto) {
		DataSourceDto ds = columnDataSourceDto.getSourceDto();
		String tableName = columnDataSourceDto.getSourceTable();
		List<ColumnDto>columnList=new ArrayList<ColumnDto>();
	    if(this.getConnection(ds) !=null) {
	        ResultSet rs;
	        String columns = "";
            try {
                final DatabaseMetaData metaData=connection.getMetaData();
                rs = metaData.getColumns(null,null,tableName, null);
                while(rs.next()) {
                    String col_type= rs.getString("TYPE_NAME");
                    ColumnDto columnDto = new ColumnDto();
                    if("DATE".equalsIgnoreCase(col_type) ||
                       "TIME".equalsIgnoreCase(col_type) ||
                       "DATETIME".equalsIgnoreCase(col_type) ||
                       "TIMESTAMP".equalsIgnoreCase(col_type) ||
                       "YEAR".equalsIgnoreCase(col_type)
                       ){
                        //columns += rs.getString("COLUMN_NAME")+","; 
                    	columnDto.setColumnName(rs.getString("COLUMN_NAME"));
                    	columnDto.setColumnIndex(rs.getInt("ORDINAL_POSITION"));
                    	columnList.add(columnDto);
                    }
                    
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            if(columns.length()>0) {
                columns.substring(0, columns.length()-1);
            }
            return columnList;
	    }
	    return columnList;
	}
	
}
