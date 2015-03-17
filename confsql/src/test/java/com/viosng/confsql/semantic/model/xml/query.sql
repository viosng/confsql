select a as tt from (select a,b, nest(b as i, c.e as w, a.e) n from (select a,b,c from t) as t) as t join t.n
 where b > 3
 order(q=(select a from (select a,q from t) as t where q < b.e)) by a