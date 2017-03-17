package com.fable.outer.biz.connectouter.intf;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import com.fable.dsp.common.dto.dataswitch.ColumnMappingDto;
import com.fable.hamal.shuttle.communication.event.task.TaskDeleteEvent;
import com.fable.outer.rmi.event.dto.AddTableDto;
import com.fable.outer.rmi.event.dto.ColumnDto;
import com.fable.outer.rmi.event.dto.DataSourceDto;
import com.fable.outer.rmi.event.dto.ListColumnDataSourceDto;
import com.fable.outer.rmi.event.dto.TreeDataDto;


/**
 * @author zhangl
 */
public interface Web2OutNetWorkService {

    /**
     * 根据数据源获取外交换的表集合方法.
     * 
     * @param ds
     *        数据源实体类
     * @return 返回 外交换的list的表传输对象
     */
    List<TreeDataDto> findListTable(DataSourceDto ds);

    /**
     * 判断网络是否连接成功.
     * 
     * @param ds
     *        数据源
     * @return 判断命令执行是否成功
     */
    String judgNetWorkLink(DataSourceDto ds);

    /**
     * 判断数据库是否可以连接 .
     * 
     * @param ds
     *        数据源
     * @return 判断数据库是否可以连接
     */
    boolean judgDBConnect(DataSourceDto ds);

    /**
     * 执行sql.
     * 
     * @param ds
     *        数据源信息
     * @param sql
     *        sql语句
     * @return 结果集.
     * @throws SQLException
     *         数据库连接异常
     * @throws ClassNotFoundException 驱动未找到 
     */
    LinkedList<Object[]> executeQuery(DataSourceDto ds, String sql)
        throws SQLException, ClassNotFoundException;
    /**
     * 根据数据源获取外交换的非增量表集合方法.
     * @param ds 
     * 			数据源
     * @return 根据数据源获取外交换的表集合方法.
     */
    List<TreeDataDto>listTableWithoutFl(DataSourceDto ds);
    /**
     * 根据数据源获取获取外网指定数据表的所有字段信息
     * @param dto
     * 			数据源
     * @param tableName 数据表
     * @return 指定表的所有字段信息
     */
    List<ColumnMappingDto> listColumnByTable(final ListColumnDataSourceDto dto);
    /**
     * 根据表名查从表
     * @param addTableDto
     * @return
     */
    List<Object[]> listFTable(AddTableDto addTableDto);
    /**
     * 加载时间戳字段
     * @param 数据源
     * @param 表名
     * @return
     */
    List<ColumnDto> dateColumnName (ListColumnDataSourceDto columnDataSourceDto);
}
