package org.apache.spark.sql.crossdata.test

import com.stratio.crossdata.test.BaseXDTest
import org.apache.spark.sql.Row
import org.apache.spark.sql.crossdata.ExecutionType
import org.apache.spark.sql.crossdata.test.SharedXDContextWithDataTest.SparkTable

import scala.reflect.runtime.universe.{typeOf, Type}

trait SharedXDContextTypesTest extends SharedXDContextWithDataTest {
  this: BaseXDTest =>

  import SharedXDContextTypesTest._

  //Template steps: Override them
  val emptySetError: String
  def saveTypesData: Int
  def sparkAdditionalKeyColumns: Seq[SparkSQLColdDef]
  def dataTypesSparkOptions: Map[String, String]

  //Template
  def doTypesTest(datasourceName: String): Unit = {

    for(tpe <- typesSet; executionType <- ExecutionType.Spark::ExecutionType.Native::Nil)
      datasourceName should s"support $tpe for $executionType execution" in {
        val dframe = sql("SELECT " + typesSet.map(_.colname).mkString(", ") + s" FROM $dataTypesTableName")
        dframe.collect(executionType).head(0) shouldBe a[tpe.type]
      }

  }

  abstract override def saveTestData: Unit = {
    super.saveTestData
    require(saveTypesData > 0, emptySetError)
  }

  protected val typesSet = Seq(
    SparkSQLColdDef("int", "INT", Some(typeOf[java.lang.Integer])),
    SparkSQLColdDef("bigint", "BIGINT", Some(typeOf[java.lang.Long])),
    SparkSQLColdDef("long", "LONG", Some(typeOf[java.lang.Long])),
    SparkSQLColdDef("string", "STRING", Some(typeOf[java.lang.Long])),
    SparkSQLColdDef("boolean", "BOOLEAN", Some(typeOf[java.lang.Boolean])),
    SparkSQLColdDef("double", "DOUBLE", Some(typeOf[java.lang.Double])),
    SparkSQLColdDef("float", "FLOAT", Some(typeOf[java.lang.Float])),
    SparkSQLColdDef("decimalInt", "DECIMAL", Some(typeOf[java.lang.Double])),
    SparkSQLColdDef("decimalLong", "DECIMAL", Some(typeOf[java.lang.Double])),
    SparkSQLColdDef("decimalDouble", "DECIMAL", Some(typeOf[java.lang.Double])),
    SparkSQLColdDef("decimalFloat", "DECIMAL", Some(typeOf[java.lang.Double])),
    SparkSQLColdDef("date", "DATE", Some(typeOf[java.sql.Date])),
    SparkSQLColdDef("timestamp", "TIMESTAMP", Some(typeOf[java.sql.Timestamp])),
    SparkSQLColdDef("tinyint", "TINYINT", Some(typeOf[java.lang.Byte])),
    SparkSQLColdDef("smallint", "SMALLINT", Some(typeOf[java.lang.Short])),
    SparkSQLColdDef("binary", "BINARY", Some(typeOf[Array[Byte]])),
    SparkSQLColdDef("arrayint", "ARRAY<INT>", Some(typeOf[Seq[_]])), //TODO
    SparkSQLColdDef("arraystring", "ARRAY<STRING>", Some(typeOf[Seq[_]])), //TODO
    SparkSQLColdDef("mapintint", "MAP<INT, INT>", Some(typeOf[Map[_, _]])), //TODO
    SparkSQLColdDef("mapstringint", "MAP<STRING, INT>", Some(typeOf[Map[_, _]])), //TODO
    SparkSQLColdDef("mapstringstring", "MAP<STRING, STRING>", Some(typeOf[Map[_, _]])), //TODO
    SparkSQLColdDef("struct", "STRUCT<field1: DATE, field2: INT>", Some(typeOf[Row])), //TODO
    SparkSQLColdDef("arraystruct", "ARRAY<STRUCT<field1: INT, field2: INT>>", Some(typeOf[Seq[_]])), //TODO
    SparkSQLColdDef("arraystructwithdate", "ARRAY<STRUCT<field1: DATE, field2: INT>>", Some(typeOf[Seq[_]])), //TODO
    SparkSQLColdDef("structofstruct", "STRUCT<field1: DATE, field2: INT, struct1: STRUCT<structField1: STRING, structField2: INT>>", Some(typeOf[Row])), //TODO
    SparkSQLColdDef("mapstruct", "MAP<STRING, STRUCT<structField1: DATE, structField2: INT>>", Some(typeOf[Map[_,_]]))
  )

  override def sparkRegisterTableSQL: Seq[SparkTable] = super.sparkRegisterTableSQL :+ {
    val fields = (sparkAdditionalKeyColumns ++ typesSet) map {
      case SparkSQLColdDef(name, tpe, _) => s"$name $tpe"
    } mkString ", "
    SparkTable(s"CREATE TEMPORARY TABLE $dataTypesTableName ( $fields )", dataTypesSparkOptions)
  }

}

object SharedXDContextTypesTest {
  val dataTypesTableName = "typesCheckTable"
  case class SparkSQLColdDef(colname: String, sqlType: String, expectedType: Option[Type] = None)
}