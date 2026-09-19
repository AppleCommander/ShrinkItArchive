# Usage: 'shell env.sh'

# Try to accommodate Mac and Linux
platform=$(uname | tr '[:upper:]' '[:lower:]' | sed 's/darwin/macosx/')
machine=$(uname -m | tr '[:upper:]' '[:lower:]' | sed 's/arm64/aarch64/')

# These are useful aliases while developing
alias nufxscan="java -jar ${PWD}/build/libs/ShrinkItArchive-*.jar"
alias nufxscann="${PWD}/build/native/nativeCompile/NufxScan-*"
