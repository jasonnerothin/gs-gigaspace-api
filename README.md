gs-gigaspace-api
====================

This codebase serves as executable specification of the [org.openspaces.core.GigaSpace](http://www.gigaspaces.com/docs/JavaDoc10.0/index.html) api.

+ Once you've checked this repository out, put a valid [License Key](http://docs.gigaspaces.com/xap100/license-key.html) in the [src/test/resources](https://github.com/jasonnerothin/gs-gigaspace-api/tree/master/src/test/resources) directory. (This demo uses 2 partitions, which is incompatible with ``XAP Lite`` licensing terms.)

+ To build a [Processing Unit](http://docs.gigaspaces.com/xap100/java-tutorial-part5.html), do:

`
	$ gradle -C rebuild clean ear -x test
`

This will produce a [Processing Unit](http://docs.gigaspaces.com/xap100/java-tutorial-part5.html), named **gigaspace-api-pu.jar**
in **build/libs**.

+ Start a grid:

`
    $ gs-agent.(sh|bat)  gsa.global.lus 1 gsa.global.gsm 1 gsa.gsc 2
`    

+ Deploy the Processing Unit with settings `cluster_schema=partitioned-sync2backup total_members=2,1` 

+ Run the WatchRepairSuite of tests from the command line: 

`
    $ gradle clean test -i
`    

All tests are run.