select b.pod, issuetype,
DiferenciaEnSegundos(createddate,now())/60/60/9 AS Diferencia,
          DiferenciaEnSegundos(createddate,now())/60/60/9-(
			SELECT sla
			FROM priority
			WHERE priority.level = a.priority
		) AS resultado,
a.issueCve,
CASE
	WHEN DiferenciaEnSegundos(createddate,now())/60/60/9-(
			SELECT sla
			FROM priority
			WHERE priority.level = a.priority
		) >= 0 THEN 'VENCIDO'
	ELSE
		CASE WHEN DiferenciaEnSegundos(createddate,now())/60/60/9-(
			SELECT sla
			FROM priority
			WHERE priority.level = a.priority
		) >= -5 THEN 'WARNING'
        ELSE 'Para Trabajar'
        END
    END AS Vencimiento,
    CASE WHEN DiferenciaEnSegundos(createddate,now())/60/60/9-(
			SELECT sla
			FROM priority
			WHERE priority.level = a.priority
		) >= 0 THEN ceil(DiferenciaEnSegundos(createddate,now())/60/60/9-(
							SELECT sla
							FROM priority
							WHERE priority.level = a.priority))
				ELSE 0
	END AS diasvencidos,
        CASE WHEN DiferenciaEnSegundos(createddate,now())/60/60/9-(
			SELECT sla
			FROM priority
			WHERE priority.level = a.priority
		) < 0 THEN floor(abs(DiferenciaEnSegundos(createddate,now())/60/60/9-(
							SELECT sla
							FROM priority
							WHERE priority.level = a.priority)))
				ELSE 0
	END AS diasporvencer,
resumen, priority, assignperson, createddate, state, component, b.resolución
from user_histories a, user_histories2 b
where etiquetas like '%KISS%'
-- and lastmodified >= '2025-01-01 00:00:00'
and state not in ('Released', 'Task Done', 'PO approved','Finalizada','Cancelado')
and pod!='MY TRIPS'
and a.issuecve=b.issuecve
and b.pod != ""
order by pod, resultado desc, priority, createddate desc, state;
