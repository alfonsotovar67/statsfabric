select a.pod, spterminados/spcomprometidos eficiencia from
(select a.pod, sum(a.storypoints) spcomprometidos
from user_histories2 a, user_histories b, cierresprint e
where a.issuecve = b.issuecve
and a.issuecve = e.issuecve
and a.comprometida like 'Comprome%'
and e.sprint like '%sprintparam%'
group by POD) a,
(select a.pod, sum(a.storypoints) spterminados
from user_histories2 a, user_histories b, cierresprint e
where a.issuecve = b.issuecve
and a.issuecve = e.issuecve
and a.comprometida like 'Comprome%'
and e.state in ('Task Done','PO Approved','Released')
and e.sprint like '%sprintparam%'
group by POD) b
where a.pod=b.pod
order by a.pod;

