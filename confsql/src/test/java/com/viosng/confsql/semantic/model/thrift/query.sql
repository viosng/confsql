select Severity, max(StatusDate) as c, count(*) from xdoctor4centera.dist_rap
 where case when 3 then 4 when 5 then 6 end
 group by Severity
 having c < 10000
 order by D
 LIMIT f(4)
