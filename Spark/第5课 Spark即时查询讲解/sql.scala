/**
  * Created by ding on 2018/1/6.
  */
import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, SQLContext}
import org.apache.spark.storage.StorageLevel


object sql {
  //模式类 默认序列化与支持模式匹配
  case class Person(name:String, age:Int)

  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("SQL").setMaster("local[4]")
    val sc = new SparkContext(conf)
    sc.setLogLevel("WARN")
    val wordcount = sc.textFile("C:\\Users\\zxy\\Desktop\\Spark1.txt").flatMap(_.split(" ")).map((_, 1)).reduceByKey(_+_).persist(StorageLevel.MEMORY_AND_DISK)
    wordcount.collect().foreach(println)

    val sqlContext = new SQLContext(sc)
    /*  val spark = SparkSession
        .builder()
        .appName("Spark SQL basic example")
        .config("spark.some.config.option", "some-value")
        .getOrCreate()
    */
    import sqlContext.implicits._
    //创建一个数据集
    val peopleDF: DataFrame = sqlContext.read.json("C:\\Users\\zxy\\Desktop\\people.json")


    println("=====================数据集查看==========================")
    //数据集查看
    peopleDF.show()
    //查看数据表的结构schema
    peopleDF.printSchema()
    //选择列进行查看
    peopleDF.select("name").show()
    peopleDF.select($"name", $"age" + 1).show()

    //过滤列进行查看
    peopleDF.filter($"age" > 21).show()
    //聚合列
    peopleDF.groupBy("age").count().show()
    //peopleDF.createOrReplaceTempView("people")
    peopleDF.registerTempTable("peopleTable")
    peopleDF.registerTempTable("peopleTable_back")
    sqlContext.sql("select a.*, b.age from peopleTable a join peopleTable_back b on a.name = b.name").show
    peopleDF.join(peopleDF).show

    println("=====================程序化查询==========================")
    //程序式的运行SQL查询
    val temporaryPeopleDF1: DataFrame =  peopleDF.select("name")
    temporaryPeopleDF1.show()


    val temporaryPeopleDF2: DataFrame = sqlContext.sql("select age from peopleTable")
    temporaryPeopleDF2.show()
    sqlContext.sql("select max(age) as maxAge from peopleTable").show
    peopleDF.map(_.getAs[String]("name")).foreach(println)
    peopleDF.map(_.getValuesMap[Any](List("name","age"))).foreach(println)



    println("=====================反射生成==========================")
    //DataFrame与RDD的互相转换
    //1 使用反射推断schema
    //代码更加简洁 并且你已经确定RDD的结构


    import sqlContext.implicits._
    val people = sc.makeRDD(Seq("july,35", "tine,18")).map(_.split(","))
    val peopleDF_1: RDD[Person] = people.map(p => Person(p(0),p(1).trim.toInt))
    peopleDF_1.toDF().show()
    sqlContext.createDataFrame(peopleDF_1).show()


    println("====================显式指明===========================")
    //2 程序式的指明schema结构
    //代码复杂，数据运行时才能确定结构
    val schemaString = "name age"
    import org.apache.spark.sql.Row
    import org.apache.spark.sql.types.{StructType,StructField,StringType};
    val schema: StructType = StructType(
      schemaString.split(" ").map(p=> StructField(p , StringType, true))
    )
    val rowRDD: RDD[Row] = people.map(p => Row(p(0), p(1)))
    val peopleDF_2: DataFrame = sqlContext.createDataFrame(rowRDD, schema)
    peopleDF_2.show()

    Console.readLine()
    //保存数据
    //peopleDF_2.write.save("")
  }
}
