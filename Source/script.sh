#!/usr/bin/bash

javac -Xlint Coordinator.java
javac -Xlint Key.java
javac -Xlint Node.java
javac -Xlint RoutingTable.java

for k in 2 5 10 20 
do
  for m in 8 16 32 160
  do
    for n in 100 1000 10000 100000
    do
      file='Outputs/output'$m'x'$n'x'$k'.csv'
      #echo $file
      rm -rf $file
      java Coordinator $m $n $k $file
    done
  done
done
