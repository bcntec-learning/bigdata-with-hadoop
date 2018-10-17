
#####Ir al directorio
```cd /home/hadoop/share/01-mapred-sales```

#####Construimos de trabajo directorio
```hadoop fs -mkdir /ejemplos/```

#####Construimos de trabajo directorio
```hadoop fs -mkdir /ejemplos/01-mapred-sales```
```hadoop fs -mkdir /ejemplos/01-mapred-sales/input```


#####Copiamos fichero
```hadoop fs -copyFromLocal ./data/Sales.csv /ejemplos/01-mapred-sales/input```
```hadoop fs -ls /ejemplos/01-mapred-wordcount/input```

```hadoop jar ./target/01-mapred-sales-1.0-SNAPSHOT.jar bcntec.learning.bigdata.hadoop.mapred.sales.Sales  /ejemplos/01-mapred-sales/input /ejemplos/01-mapred-sales/output ​```


##### Visulizar ficheros
```hadoop fs -cat /ejemplos/01-mapred-sales/output/_SUCCESS```
```hadoop fs -cat /ejemplos/01-mapred-sales/output/part-r-00000```