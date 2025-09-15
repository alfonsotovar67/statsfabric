select  b.pod, a.issueCve, resumen, assignperson, informador, createddate, state, component, b.resolución
from user_histories a, user_histories2 b
where issuetype in  ('Error','DEFECT')
and etiquetas not like '%KISS%'
and state not in ('Released', 'Task Done', 'PO approved','Finalizada','Cancelado')
and pod in ('ANCILLARIES-BE','KIOSKS','SELF REACOMM-BE','CORPORATE')
and a.issuecve=b.issuecve
and b.pod != ""
order by pod, priority, createddate, state;
