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

package org.dbunit.database;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.dbunit.DatabaseEnvironment;
import org.dbunit.TestFeature;

/**
 * @author Manuel Laflamme
 * @version $Revision$
 */
public class AllTests
{
    public static Test suite() throws Exception
    {
        TestSuite suite = new TestSuite();
        suite.addTest(org.dbunit.database.statement.AllTests.suite());
        suite.addTest(new TestSuite(CachedResultSetTableTest.class));
        suite.addTest(new TestSuite(DatabaseConnectionTest.class));
        suite.addTest(new TestSuite(DatabaseDataSetTest.class));
        suite.addTest(new TestSuite(DatabaseTableIteratorTest.class));
        suite.addTest(new TestSuite(DatabaseTableMetaDataTest.class));
        suite.addTest(new TestSuite(ForwardOnlyResultSetTableTest.class));
        suite.addTest(new TestSuite(QueryDataSetTest.class));

        DatabaseEnvironment environment = DatabaseEnvironment.getInstance();
        if (environment.support(TestFeature.SCOLLABLE_RESULTSET))
        {
            suite.addTest(new TestSuite(ScrollableResultSetTableTest.class));
        }

        return suite;
    }
}






