/*
 *
 * The DbUnit Database Testing Framework
 * Copyright (C)2002-2009, DbUnit.org
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
package org.dbunit.ext.postgresql;

import java.sql.Types;
import junit.framework.TestCase;
import org.dbunit.dataset.datatype.DataType;
import org.dbunit.dataset.datatype.IntegerDataType;

/**
 *
 * @author Jarvis Cochrane (jarvis@cochrane.com.au)
 * @since 2.4.5 (Apr 27, 2009)
 */
public class PostgresqlDataTypeFactoryTest extends TestCase {
    
    public PostgresqlDataTypeFactoryTest(String testName) {
        super(testName);
    }

    /**
     * Test of createDataType method, of class PostgresqlDataTypeFactory.
     */
    public void testCreateDataType() throws Exception {
        
        PostgresqlDataTypeFactory instance = new PostgresqlDataTypeFactory();
        
        // Test UUID type created properly
        int sqlType = Types.OTHER;
        String sqlTypeName = "uuid";
        
        DataType result = instance.createDataType(sqlType, sqlTypeName);
        assertTrue(result instanceof UuidType);

        // Test a type from DefaultDataTypeFactory created properly

        sqlType = Types.INTEGER;
        sqlTypeName = "int";
        result = instance.createDataType(sqlType, sqlTypeName);
        assertTrue(result instanceof IntegerDataType);

    }

}
