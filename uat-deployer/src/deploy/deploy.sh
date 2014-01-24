# This Source Code Form is subject to the terms of the Mozilla Public
# License, v. 2.0. If a copy of the MPL was not distributed with this
# file, You can obtain one at http://mozilla.org/MPL/2.0/.
# Copyright (c) 2013, MPL CodeInside http://codeinside.ru

#!/bin/sh
export JAVA_HOME="/usr/lib/jvm/java"
export LANG="ru_RU.UTF-8"

BASE="${HOME}/deploy-${1}"
BUNDLES="bundles-${1}.tar.gz"
GF="${HOME}/gf"
ASADMIN="${GF}/bin/asadmin"
DOMAIN="${GF}/glassfish/domains/oep-dev"
CLUSTER="oep-dev-cluster"
AUTODEPLOY="${DOMAIN}/autodeploy"

DATABASES="gws"

# в порядке удаления
COMPONENTS="
web-client-1.0.1
web-client-1.0.2
web-client-1.0.3
web-client-1.0.4
gses-liquibase-2.0.2
gses-liquibase-api-1.0.0
gws-client-fss-1.0.1
gws-crypto-cryptopro-1.0.2
gws-core-1.0.4
gws-core-1.0.5
gws-wsdl-1.0.3
gws-s-oep-declarer-1.0.2
gws-s-oep-dict-1.0.1
gws-p-router-1.0.2
gws-p-router-1.0.3
gws-p-adapter-1.0.1
gws-p-adapter-1.0.2
gws-log-1.0.0
gws-log-1.0.1
gws-p-registry-hc-1.0.1
gws-p-registry-api-1.0.1
gws-c-oep-declarer-1.0.2
gws-api-1.0.3
gws-api-1.0.4
gws-api-1.0.5
gws-api-1.0.6
gws-client-mvd-3456-1.0.0
"

# в порядке установки
OSGI="
gws-api-1.0.6
gws-wsdl-1.0.3
gws-core-1.0.5
gws-crypto-cryptopro-1.0.2
gws-client-fss-1.0.1
gws-client-mvd-3456-1.0.0
gses-liquibase-api-1.0.0
gses-liquibase-2.0.2
gws-p-registry-api-1.0.1
gws-p-registry-hc-1.0.1
gws-log-1.0.1
gws-p-adapter-1.0.2
gws-p-router-1.0.3
gws-s-oep-dict-1.0.1
gws-s-oep-declarer-1.0.2
gws-c-oep-declarer-1.0.2
web-client-1.0.4
"

WAR=""


halt() {
	cd "${HOME}"
	rm -rf "${BASE}"
	echo $2
	exit $1
}

if [ ! -f "${HOME}/${BUNDLES}" ]; then
	halt 1 "Пропущен архив бандлов ${BUNDLES}"
fi

rm -rf "${BASE}"
mkdir "${BASE}"
cd "${BASE}"
mv "${HOME}/${BUNDLES}" "./"
tar -xf "${BUNDLES}"
rm "${BUNDLES}"

# убрать на всякий случай чтоб не мешались
rm -f "${AUTODEPLOY}/*.war"
rm -f "${AUTODEPLOY}/bundles/*.jar"

if [ "$2" = "cluster" ]; then
    echo "Разворачиваем в кластере"
else
    echo "Разворачиваем в домене"
fi

if ( ! $ASADMIN generate-jvm-report ); then
	if [ "$2" = "cluster" ] ; then
        if ( ! $ASADMIN start-cluster $CLUSTER ) ; then
            halt 2 "Тузик мертв?"
        fi
    else
        if ( ! $ASADMIN start-domain ) ; then
            halt 2 "Тузик мертв?"
        fi
    fi
fi

for COMPONENT in $COMPONENTS; do
  if [ "$2" = "cluster" ] ; then
    if ( $ASADMIN list-components $CLUSTER | grep $COMPONENT ) ; then
        $ASADMIN undeploy --target $CLUSTER $COMPONENT
    fi
  else
    if ( $ASADMIN list-components | grep $COMPONENT ) ; then
        $ASADMIN undeploy $COMPONENT
    fi
  fi
done

if [ "$2" = "cluster" ] ; then
    $ASADMIN stop-cluster $CLUSTER
else
    $ASADMIN stop-domain oep-dev
fi

for DB in $DATABASES; do
	echo "=====> $DB"
	psql $DB -U as > clean.sql <<EOF
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
	psql $DB -U as < clean.sql
done

if [ "$2" = "cluster" ] ; then
    $ASADMIN start-cluster $CLUSTER
else
    $ASADMIN start-domain oep-dev
fi

for B in $OSGI; do
	echo "=====> $B"
	if [ "$2" = "cluster" ] ; then
        if ( ! $ASADMIN deploy --availabilityenabled=true --name=$B --type=osgi --target=$CLUSTER "${B}.jar" ); then
            tail -n 100 "${DOMAIN}/logs/server.log"
            halt 3 "Ошибка установки $B"
        fi
    else
        if ( ! $ASADMIN deploy --availabilityenabled=true --name=$B --type=osgi --target=server "${B}.jar" ); then
            tail -n 100 "${DOMAIN}/logs/server.log"
            halt 3 "Ошибка установки $B"
        fi
    fi
done
for W in $WAR; do
	echo "=====> $W"
	if [ "$2" = "cluster" ] ; then
        if ( ! $ASADMIN deploy --availabilityenabled=true --name=$W --target=$CLUSTER "${W}.war" ); then
            tail -n 200 "${DOMAIN}/logs/server.log"
            halt 4 "Ошибка равзертывания $W"
        fi
    else
        if ( ! $ASADMIN deploy --availabilityenabled=true --name=$W --target=server "${W}.war" ); then
            tail -n 200 "${DOMAIN}/logs/server.log"
            halt 4 "Ошибка равзертывания $W"
        fi
    fi
done
halt 0 OK