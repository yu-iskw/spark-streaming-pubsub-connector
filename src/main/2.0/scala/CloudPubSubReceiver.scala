import com.google.cloud.sparkdemo.CloudPubsubReceiver

import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.dstream.DStream

/**
  * This class is a helper class to make dealing with Google Pub/Sub easy
  */
object CloudPubSubReceiverHelper {

  /**
    * create a DStream getting data from Google Pub/Sub
    *
    * @param ssc Streaming context
    * @param projectId Google Cloud project ID
    * @param numReceivers the number of receivers
    * @param topic Google Pub/Sub topic
    * @param subscription Google Pub/Sub subscription
    * @param deleteAfter delete the subscription when a spark streaming job stops
    */
  def createDStream(
      ssc: StreamingContext,
      projectId: String,
      numReceivers: Int,
      topic: String,
      subscription: String,
      deleteAfter: Boolean): DStream[String] = {
    val receivers = (1 to numReceivers).map { i =>
      ssc.receiverStream(new CloudPubsubReceiver(projectId, topic, subscription, deleteAfter))
    }
    ssc.union(receivers)
  }

  /**
    * create a DStream getting data from Google Pub/Sub
    *
    * @param ssc Streaming context
    * @param projectId Google Cloud project ID
    * @param numReceivers the number of receivers
    * @param topic Google Pub/Sub topic
    * @param subscription Google Pub/Sub subscription
    */
  def createDStream(
    ssc: StreamingContext,
    projectId: String,
    numReceivers: Int,
    topic: String,
    subscription: String): DStream[String] = {
    createDStream(ssc, projectId, numReceivers, topic, subscription, false)
  }
}
