SELECT
  asterixQuery.name,
  file.name
 FROM file
  JOIN (SELECT *
        FROM asterix
        WHERE asterix.age > 3) AS asterixQuery ON asterixQuery.department_id = file.id
