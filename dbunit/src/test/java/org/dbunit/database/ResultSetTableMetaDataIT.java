package org.dbunit.database;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dbunit.AbstractDatabaseIT;
import org.dbunit.DdlExecutor;
import org.dbunit.HypersonicEnvironment;
import org.dbunit.dataset.Column;
import org.dbunit.dataset.Columns;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITableMetaData;
import org.dbunit.testutil.TestUtils;

/**
 * @author gommma (gommma AT users.sourceforge.net)
 * @author Last changed by: $Author$
 * @version $Revision$ $Date$
 * @since 2.4.0
 */
public class ResultSetTableMetaDataIT extends AbstractDatabaseIT
{
    
    public ResultSetTableMetaDataIT(String s)
    {
        super(s);
    }

    protected IDataSet createDataSet() throws Exception
    {
        return _connection.createDataSet();
    }

    /**
     * Tests the pattern-like column retrieval from the database. DbUnit
     * should not interpret any table names as regex patterns. 
     * @throws Exception
     */
    public void testGetColumnsForTablesMatchingSamePattern() throws Exception
    {
        Connection jdbcConnection = HypersonicEnvironment.createJdbcConnection("tempdb");
        DdlExecutor.executeDdlFile(TestUtils.getFile("sql/hypersonic_dataset_pattern_test.sql"),
                jdbcConnection);
        IDatabaseConnection connection = new DatabaseConnection(jdbcConnection);

        try {
            String tableName = "PATTERN_LIKE_TABLE_X_";
            String[] columnNames = {"VARCHAR_COL_XUNDERSCORE"};
    
            String sql = "select * from " + tableName;
            ForwardOnlyResultSetTable resultSetTable = new ForwardOnlyResultSetTable(tableName, sql, connection);
            ResultSetTableMetaData metaData = (ResultSetTableMetaData) resultSetTable.getTableMetaData();
            
            Column[] columns = metaData.getColumns();
    
            assertEquals("column count", columnNames.length, columns.length);
    
            for (int i = 0; i < columnNames.length; i++)
            {
                Column column = Columns.getColumn(columnNames[i], columns);
                assertEquals(columnNames[i], columnNames[i], column.getColumnName());
            }
        }
        finally {
            HypersonicEnvironment.shutdown(jdbcConnection);
            jdbcConnection.close();
            HypersonicEnvironment.deleteFiles("tempdb");
        }
    }
    
    /**
     * Test that the column metadata generated by ResultSetTableMetaData for a given 
     * query is consistent with the metadata obtained directly for the source tables.  
     * @throws Exception
     */
    public void testMetadataCoherence() throws Exception 
    {
        Connection jdbcConnection = HypersonicEnvironment.createJdbcConnection("tempdb");
        HypersonicEnvironment.executeDdlFile(TestUtils.getFile("sql/hypersonic_dataset_metadata_coherence.sql"), jdbcConnection);
        IDatabaseConnection connection = new DatabaseConnection(jdbcConnection);

        try {
            ITableMetaData tableAMetadata = new DatabaseTableMetaData("A", connection, true);
            ITableMetaData tableBMetadata = new DatabaseTableMetaData("B", connection, true);
            List<Column> dbTableColumns = new ArrayList<Column>();
            for (Column col : tableAMetadata.getColumns()) {
            	dbTableColumns.add(col);
            }
            for (Column col : tableBMetadata.getColumns()) {
            	dbTableColumns.add(col);
            }
    
            String sql = "select * from A inner join B on B.FK_A = A.PK_A";
            ForwardOnlyResultSetTable resultSetTable = new ForwardOnlyResultSetTable("A", sql, connection);
            ResultSetTableMetaData resultSetMetadata = (ResultSetTableMetaData) resultSetTable.getTableMetaData();
            Column[] resultSetColumns = resultSetMetadata.getColumns();
            
            Iterator<Column> tableColIterator = dbTableColumns.iterator();
            for (int i = 0; i < resultSetColumns.length; i++) {
            	Column tableCol = tableColIterator.next();
            	String msg = "Comparing col i: " + resultSetColumns[i].getColumnName() + " vs " + tableCol.getColumnName();
            	//System.out.println(msg);
            	assertEquals(msg, tableCol, resultSetColumns[i]);
            }
        }
        finally {
            HypersonicEnvironment.shutdown(jdbcConnection);
            jdbcConnection.close();
            HypersonicEnvironment.deleteFiles("tempdb");
        }
    }

}
