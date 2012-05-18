mvn -Darguments='-Dmaven.test.skip=true' scm:status clean release:clean release:prepare -DautoVersionSubmodules=true -P google-scm
