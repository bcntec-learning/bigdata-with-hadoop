
#####Ir al directorio
```cd /sources/hadoop/02-partitioner-wordcount```

#####Construimos de trabajo directorio
```hadoop fs -mkdir /ejemplos/01-mapred-wordcount/input```


```hadoop jar ./target/02-partitioner-wordcount-1.0-SNAPSHOT.jar bcntec.learning.bigdata.hadoop.mapred.partitioner1.WordCounterWithTotalOrderPartitioner  /ejemplos/01-mapred-wordcount/input /ejemplos/01-mapred-wordcount/output ​```


##### Visulizar ficheros
```hadoop fs -cat /ejemplos/01-mapred-wordcount/output/_SUCCESS```
```hadoop fs -cat /ejemplos/01-mapred-wordcount/output/part-r-00000```