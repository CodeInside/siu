#!/bin/bash
	psql gses_admin -U gses -h 127.0.0.1 > clean.sql <<EOF
copy (
	select 'drop table ' || tablename || ' cascade;'
	FROM pg_tables WHERE schemaname = 'public'
) to STDOUT;
copy (
	select 'drop sequence ' || c.relname || ' cascade;'
	FROM pg_catalog.pg_class AS c
	LEFT JOIN pg_catalog.pg_namespace AS n ON n.oid = c.relnamespace WHERE relkind = 'S' AND n.nspname = 'public'
) to STDOUT;
EOF
	psql gses_admin -U gses -h 127.0.0.1 < clean.sql
