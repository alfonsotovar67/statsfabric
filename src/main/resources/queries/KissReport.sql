select b.pod, issuetype,
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
and state not in ('Released', 'Task Done', 'PO approved','Finalizada','Cancelado')
and pod!='MY TRIPS'
and a.issuecve=b.issuecve
and a.issuecve != 'CVT20-7067'
and b.pod != ""
order by pod, diasvencidos desc, diasporvencer, priority, createddate desc, state;
