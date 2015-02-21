select field, 
       aggregateFunction(field; param1="p", param2=round(score))
from source
group(algorithm="nearest_neighbours", k=10) by field, otherField -- to parameters
order by otherField desc

-- t1 join t2 on expr -> args(t1, t2) params(expr)