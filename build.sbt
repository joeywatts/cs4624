lazy val PredictionEngine = project
lazy val PricingData = project
lazy val VirtualPortfolio = project.dependsOn(PricingData)
lazy val root = (project in file("."))
.aggregate(
    PredictionEngine,
    PricingData,
    VirtualPortfolio
)
retrieveManaged := true

// Exclude Emacs autosave files.
excludeFilter in unmanagedSources := ".#*"

import sbt.complete.Parsers.spaceDelimited
lazy val sync = inputKey[Unit]("Sync the source code to a remote server")
sync := {
  val params = spaceDelimited("<arg>").parsed
  val property = System.getProperty("sync.remote")
  val remote = params.headOption.orElse(Option(property))
  remote match {
    case Some(r) => Seq("rsync", "-avz", "--delete", "--filter=:- .gitignore", "--exclude", ".git/", "./", r) !
    case None => println("Please specify the remote path as a parameter or set the 'sync.remote' system property. Example:\n\tuser@host:mypath/")
  }
}
