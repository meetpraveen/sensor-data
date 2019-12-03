package sensordata

import cloudflow.akkastream._
import cloudflow.akkastream.scaladsl._
import cloudflow.streamlets._
import cloudflow.streamlets.avro._

class ValidMetricLogger extends AkkaStreamlet {
  val inlet = AvroInlet[Metric]("in")
  val shape = StreamletShape.withInlets(inlet)

  override def createLogic = new RunnableGraphStreamletLogic() {

    def log(metric: Metric) = {
      system.log.info(metric.toString)
    }

    def flow = {
      FlowWithOffsetContext[Metric]
        .map { validMetric ⇒
          log(validMetric)
          validMetric
        }
    }

    def runnableGraph = {
      sourceWithOffsetContext(inlet).via(flow).to(sinkWithOffsetContext)
    }
  }
}
