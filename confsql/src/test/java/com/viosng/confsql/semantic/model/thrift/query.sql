select Severity, max(StatusDate) as c from xdoctor4centera.dist_rap
 group by Severity
 having c < 10000
 order by D
 LIMIT f(4)