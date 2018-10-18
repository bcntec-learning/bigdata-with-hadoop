A = LOAD ‘/pig/sales.csv’ using PigStorage (‘,’) as (Transaction_date: datetime, Product: chararray, Price: bigdecimal,
        Payment_Type: chararray, Name: chararray,  City: chararray, State: chararray, Country: chararray, Account_Created: chararray,
         Last_Login: datetime, Latitude: float, Longitude: chararray);

B = FOREACH A generate Product, Name, Payment_Type;

DUMP B;