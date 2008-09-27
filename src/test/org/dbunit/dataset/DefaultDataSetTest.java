/*
 *
 * The DbUnit Database Testing Framework
 * Copyright (C)2002-2004, DbUnit.org
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */

package org.dbunit.dataset;

import org.dbunit.database.AmbiguousTableNameException;
import org.dbunit.dataset.xml.XmlDataSet;

import java.io.FileReader;

/**
 * @author Manuel Laflamme
 * @version $Revision$
 * @since Feb 22, 2002
 */
public class DefaultDataSetTest extends AbstractDataSetTest
{
    public DefaultDataSetTest(String s)
    {
        super(s);
    }

    protected IDataSet createDataSet() throws Exception
    {
        IDataSet dataSet = new XmlDataSet(
                new FileReader("src/xml/dataSetTest.xml"));
        ITable[] tables = DataSetUtils.getTables(dataSet);

        return new DefaultDataSet(tables);
    }

    protected IDataSet createDuplicateDataSet() throws Exception
    {
        return createDuplicateDataSet(false);
    }
    
    protected IDataSet createMultipleCaseDuplicateDataSet() throws Exception 
    {
        return createDuplicateDataSet(true);
    }

    private IDataSet createDuplicateDataSet(boolean multipleCase) throws AmbiguousTableNameException 
    {
        ITable[] tables = super.createDuplicateTables(multipleCase);
        return new DefaultDataSet(tables);
    }

}




