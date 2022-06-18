

Square logcat
https://github.com/square/logcat

Application file onCreate():
Log all priorities in debug builds, no-op in release builds.
AndroidLogcatLogger.installOnDebuggableApp(this, minPriority = VERBOSE)
