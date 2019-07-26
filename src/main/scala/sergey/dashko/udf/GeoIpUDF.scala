package sergey.dashko.udf

import org.apache.hadoop.hive.ql.exec.UDFArgumentLengthException
import org.apache.hadoop.hive.ql.udf.generic.GenericUDF
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector
import org.apache.hadoop.hive.serde2.objectinspector.primitive.{StringObjectInspector, PrimitiveObjectInspectorFactory => factory}
import java.lang.Boolean

import org.apache.commons.net.util.SubnetUtils

import scala.util.Try



class GeoIpUDF extends GenericUDF {

  var lanInspector: StringObjectInspector = _
  var ipInspector: StringObjectInspector = _

  override def initialize(arguments: Array[ObjectInspector]): ObjectInspector = {

    if (arguments.length != 2) throw new UDFArgumentLengthException("Takes 2 arguments: subnet: String, ip: String") else {
      lanInspector = arguments(0).asInstanceOf[StringObjectInspector]
      ipInspector = arguments(1).asInstanceOf[StringObjectInspector]
    }

    factory.javaBooleanObjectInspector
  }


  override def evaluate(arguments: Array[GenericUDF.DeferredObject]): Boolean = {
    val lan = lanInspector.getPrimitiveJavaObject(arguments(0).get())
    val ip = ipInspector.getPrimitiveJavaObject(arguments(1).get())
    val subnet = Try(new SubnetUtils(lan).getInfo)
    subnet.map(result => new Boolean(result.isInRange(ip))).getOrElse(new Boolean(false))
  }


  override def getDisplayString(children: Array[String]): String = "Checks if ip address is part of subnet"
}
