select id, age from file join (select * from asterix where age > 3) as asterixQuery on asterixQuery.id = file.id