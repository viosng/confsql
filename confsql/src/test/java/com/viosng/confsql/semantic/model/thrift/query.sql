SELECT *
FROM table1
     LEFT JOIN (algorithm = 'merge_join',
               precise_bound=0.8)
     table2
ON table1.a = table2.a.