mvn -Darguments='-Dmaven.test.skip=true' scm:status clean release:clean release:prepare release:perform clean release:clean -DautoVersionSubmodules=true -P google-scm
