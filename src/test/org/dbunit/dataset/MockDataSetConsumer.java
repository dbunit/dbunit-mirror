package org.dbunit.dataset;

import com.mockobjects.Verifiable;
import com.mockobjects.ExpectationList;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import junit.framework.Assert;

/**
 *
 * <p> Copyright (c) 2002 OZ.COM.  All Rights Reserved. </p>
 * @author manuel.laflamme$
 * @since Apr 29, 2003$
 */
public class MockDataSetConsumer implements Verifiable, IDataSetConsumer
{
    private static final ProducerEvent START_DATASET_EVENT =
            new ProducerEvent("startDataSet()");
    private static final ProducerEvent END_DATASET_EVENT =
            new ProducerEvent("endDataSet()");

    private final ExpectationList _expectedList = new ExpectationList("");
    private String _actualTableName;
    private int _actualTableRow = 0;

    public void addExpectedStartDataSet() throws Exception
    {
        _expectedList.addExpected(START_DATASET_EVENT);
    }

    public void addExpectedEndDataSet() throws Exception
    {
        _expectedList.addExpected(END_DATASET_EVENT);
    }

    public void addExpectedStartTable(ITableMetaData metaData) throws Exception
    {
        _expectedList.addExpected(new StartTableEvent(metaData));
    }

    public void addExpectedStartTable(String tableName, Column[] columns) throws Exception
    {
        addExpectedStartTable(new DefaultTableMetaData(tableName, columns));
    }

    public void addExpectedEndTable(String tableName) throws Exception
    {
        _expectedList.addExpected(new EndTableEvent(tableName));
    }

    public void addExpectedRow(String tableName, int row, Object[] values) throws Exception
    {
        _expectedList.addExpected(new RowEvent(tableName, row, values));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Verifiable interface

    public void verify()
    {
        _expectedList.verify();
    }

    ////////////////////////////////////////////////////////////////////////////
    // IDataSetConsumer interface

    public void startDataSet() throws DataSetException
    {
        _expectedList.addActual(START_DATASET_EVENT);
    }

    public void endDataSet() throws DataSetException
    {
        _expectedList.addActual(END_DATASET_EVENT);
    }

    public void startTable(ITableMetaData metaData) throws DataSetException
    {
        _expectedList.addActual(new StartTableEvent(metaData));
        _actualTableName = metaData.getTableName();
        _actualTableRow = 0;
    }

    public void endTable() throws DataSetException
    {
        _expectedList.addActual(new EndTableEvent(_actualTableName));
        _actualTableName = null;
        _actualTableRow = 0;
    }

    public void row(Object[] values) throws DataSetException
    {
        _expectedList.addActual(
                new RowEvent(_actualTableName, _actualTableRow, values));
        _actualTableRow++;
    }

    ////////////////////////////////////////////////////////////////////////////
    //

    private static class ProducerEvent
    {
        protected final String _name;

        public ProducerEvent(String name)
        {
            _name = name;
        }

        public boolean equals(Object o)
        {
            if (this == o) return true;
            if (!(o instanceof ProducerEvent)) return false;

            final ProducerEvent item = (ProducerEvent)o;

            if (!_name.equals(item._name)) return false;

            return true;
        }

        public int hashCode()
        {
            return _name.hashCode();
        }

        public String toString()
        {
            return _name;
        }
    }

    private static class StartTableEvent extends ProducerEvent
    {
        private final String _tableName;
        private final Column[] _columns;

        public StartTableEvent(ITableMetaData metaData) throws DataSetException
        {
            super("startTable()");
            _tableName = metaData.getTableName();
            _columns = metaData.getColumns();
        }

        public boolean equals(Object o)
        {
            if (this == o) return true;
            if (!(o instanceof StartTableEvent)) return false;
            if (!super.equals(o)) return false;

            final StartTableEvent startTableItem = (StartTableEvent)o;

            if (!Arrays.equals(_columns, startTableItem._columns)) return false;
            if (!_tableName.equals(startTableItem._tableName)) return false;

            return true;
        }

        public int hashCode()
        {
            int result = super.hashCode();
            result = 29 * result + _tableName.hashCode();
            return result;
        }

        public String toString()
        {
            return _name + ": table=" + _tableName + ", columns=" + Arrays.asList(_columns);
        }
    }

    private static class EndTableEvent extends ProducerEvent
    {
        private final String _tableName;

        public EndTableEvent(String tableName)
        {
            super("endTable()");
            _tableName = tableName;
        }

        public boolean equals(Object o)
        {
            if (this == o) return true;
            if (!(o instanceof EndTableEvent)) return false;
            if (!super.equals(o)) return false;

            final EndTableEvent endTableItem = (EndTableEvent)o;

            if (!_tableName.equals(endTableItem._tableName)) return false;

            return true;
        }

        public int hashCode()
        {
            int result = super.hashCode();
            result = 29 * result + _tableName.hashCode();
            return result;
        }

        public String toString()
        {
            return _name + ": table=" + _tableName;
        }
    }

    private static class RowEvent extends ProducerEvent
    {
        private final String _tableName;
        private final int _row;
        private final Object[] _values;

        public RowEvent(String tableName, int row, Object[] values)
        {
            super("row()");
            _tableName = tableName;
            _row = row;
            _values = values;
        }

        public boolean equals(Object o)
        {
            if (this == o) return true;
            if (!(o instanceof RowEvent)) return false;
            if (!super.equals(o)) return false;

            final RowEvent rowItem = (RowEvent)o;

            if (_row != rowItem._row) return false;
            if (!_tableName.equals(rowItem._tableName)) return false;
// Probably incorrect - comparing Object[] arrays with Arrays.equals
            if (!Arrays.equals(_values, rowItem._values)) return false;

            return true;
        }

        public int hashCode()
        {
            int result = super.hashCode();
            result = 29 * result + _tableName.hashCode();
            result = 29 * result + _row;
            return result;
        }

        public String toString()
        {
            return _name + ": table=" + _tableName + ", row=" + _row +
                    ", values=" + Arrays.asList(_values);
        }

    }
}
