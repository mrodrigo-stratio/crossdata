/*
 * Licensed to STRATIO (C) under one or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information regarding copyright ownership. The STRATIO
 * (C) licenses this file to you under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.stratio.meta2.common.data;

public class ColumnName extends Name {

  /**
   * Name of the column.
   */
  private final String name;

  private TableName tableName;

  /**
   * Default constructor.
   * 
   * @param columnName Name of the column.
   */
  public ColumnName(String catalogName, String tableName, String columnName) {
    if(catalogName==null || catalogName.isEmpty() || tableName==null || tableName.isEmpty()) {
      this.tableName=null;
    }else {
      this.tableName = new TableName(catalogName, tableName);
    }
    this.name = columnName;
  }



  public TableName getTableName() {
    return tableName;
  }

  public void setTableName(TableName tableName) {
    this.tableName = tableName;
  }

  public String getName() {
    return name;
  }

  @Override
  public boolean isCompletedName() {
    return tableName != null;
  }

  public String getQualifiedName() {
    String result;
    if (isCompletedName()) {
      result= QualifiedNames.getColumnQualifiedName(this.getTableName().getCatalogName().getName(),
          getTableName().getName(), getName());
    }else {
      result=QualifiedNames.getColumnQualifiedName(UNKNOWN_NAME, UNKNOWN_NAME, getName());
    }
    return result;
  }

}