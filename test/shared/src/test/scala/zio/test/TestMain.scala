package zio.test

import scala.concurrent.ExecutionContext.Implicits.global

import zio.test.mock._
import zio.test.TestUtils.{ report, scope }

object TestMain {

  def main(args: Array[String]): Unit = {
    val testResults = List(
      scope(AssertionSpec.run, "AssertionSpec"),
      scope(AssertResultSpec.run, "AssertResultSpec"),
      scope(CheckSpec.run, "CheckSpec"),
      scope(ClockSpec.run, "ClockSpec"),
      scope(ConsoleSpec.run, "ConsoleSpec"),
      scope(DefaultTestReporterSpec.run, "DefaultTestReporterSpec"),
      scope(EnvironmentSpec.run, "EnvironmentSpec"),
      scope(FunSpec.run, "FunSpec"),
      scope(GenSpec.run, "GenSpec"),
      scope(LiveSpec.run, "LiveSpec"),
      scope(MathUtilsSpec.run, "MathSpec"),
      scope(RandomSpec.run, "RandomSpec"),
      scope(SampleSpec.run, "SampleSpec"),
      scope(SchedulerSpec.run, "SchedulerSpec"),
      scope(SystemSpec.run, "SystemSpec"),
      scope(TestAspectSpec.run, "TestAspectSpec"),
      scope(TestSpec.run, "TestSpec")
    )
    report(testResults)
  }
}
