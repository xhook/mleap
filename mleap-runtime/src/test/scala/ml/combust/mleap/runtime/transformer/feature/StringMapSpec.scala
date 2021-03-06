package ml.combust.mleap.runtime.transformer.feature

import ml.combust.mleap.core.feature.StringMapModel
import ml.combust.mleap.runtime.{LeapFrame, LocalDataset, Row}
import ml.combust.mleap.runtime.types.{StringType, StructField, StructType}
import org.scalatest.FunSpec

/**
  * Created by hollinwilkins on 1/5/17.
  */
class StringMapSpec extends FunSpec {
  val schema = StructType(Seq(StructField("test_string", StringType()))).get
  val dataset = LocalDataset(Seq(Row("index1"), Row("index2"), Row("index3")))
  val frame = LeapFrame(schema, dataset)

  val stringMap = StringMap(inputCol = "test_string",
    outputCol = "test_index",
    model = StringMapModel(Map("index1" -> 1.0, "index2" -> 1.0, "index3" -> 2.0)))

  describe("#transform") {
    it("converts input string into the mapped double") {
      val data = stringMap.transform(frame).get.dataset

      assert(data(0).getDouble(1) == 1.0)
      assert(data(1).getDouble(1) == 1.0)
      assert(data(2).getDouble(1) == 2.0)
    }

    describe("with invalid string") {
      val frame2 = frame.copy(dataset = LocalDataset(Array(Row("bad_index"))))

      it("returns a Failure") { assert(stringMap.transform(frame2).isFailure) }
    }
  }
}
