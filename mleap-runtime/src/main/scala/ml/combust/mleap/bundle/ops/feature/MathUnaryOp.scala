package ml.combust.mleap.bundle.ops.feature

import ml.combust.bundle.BundleContext
import ml.combust.bundle.dsl._
import ml.combust.bundle.op.{OpModel, OpNode}
import ml.combust.mleap.core.feature.{MathUnaryModel, UnaryOperation}
import ml.combust.mleap.runtime.MleapContext
import ml.combust.mleap.runtime.transformer.feature.MathUnary

/**
  * Created by hollinwilkins on 12/27/16.
  */
class MathUnaryOp extends OpNode[MleapContext, MathUnary, MathUnaryModel] {
  override val Model: OpModel[MleapContext, MathUnaryModel] = new OpModel[MleapContext, MathUnaryModel] {
    override val klazz: Class[MathUnaryModel] = classOf[MathUnaryModel]

    override def opName: String = Bundle.BuiltinOps.feature.math_unary

    override def store(model: Model, obj: MathUnaryModel)
                      (implicit context: BundleContext[MleapContext]): Model = {
      model.withAttr("operation", Value.string(obj.operation.name))
    }

    override def load(model: Model)
                     (implicit context: BundleContext[MleapContext]): MathUnaryModel = {
      MathUnaryModel(UnaryOperation.forName(model.value("operation").getString))
    }
  }

  override val klazz: Class[MathUnary] = classOf[MathUnary]

  override def name(node: MathUnary): String = node.uid

  override def model(node: MathUnary): MathUnaryModel = node.model

  override def load(node: Node, model: MathUnaryModel)
                   (implicit context: BundleContext[MleapContext]): MathUnary = {
    MathUnary(uid = node.name,
      inputCol = node.shape.standardInput.name,
      outputCol = node.shape.standardOutput.name,
      model = model)
  }

  override def shape(node: MathUnary): Shape = Shape().withStandardIO(node.inputCol, node.outputCol)
}
