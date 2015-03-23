SELECT coord_to_state(s.lat, s.lng), AVG(m.temp) AS avg_temp
 FROM sensors AS s JOIN measurements AS m ON s.id = m.sid
 WHERE (s.lat>32.6 AND s.lat<32.9 AND s.lng>-117.0 AND s.lng<-117.3)
       AND NOT EXISTS (SELECT 1 FROM measurements AS me WHERE me.sid=s.id AND (me.temp>140 OR me.temp<-40))
 GROUP BY s.id, s.lat, s.lng