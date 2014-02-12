# This Source Code Form is subject to the terms of the Mozilla Public
# License, v. 2.0. If a copy of the MPL was not distributed with this
# file, You can obtain one at http://mozilla.org/MPL/2.0/.
# Copyright (c) 2013, MPL CodeInside http://codeinside.ru

#!/bin/sh -e

# что идентифицирует предыдущий релиз
RED_LINE="web-client-1.0.3"

# в порядке удаления
TO_REMOVE="
web-client-1.0.3
gws-api-1.0.5
gws-s-oep-declarer-1.0.2
"

# в порядке установки
TO_INSTALL="
gws-api-1.0.6
gws-s-oep-declarer-1.0.3
web-client-1.0.4
"

halt() {
	echo $2
	exit $1
}

if [ !  -z "$1" ]; then
  GF=$1
else
  GF="${HOME}/gf"
fi

ASADMIN="${GF}/bin/asadmin"

if [ ! -f "$ASADMIN" ] ; then
  halt 1 "Ошибка конфигурации: $ASADMIN не найден, укажите домашний каталог glassfish первым параметром"
fi

if ( ! $ASADMIN list-components | grep $RED_LINE ) ; then
  halt 2 "Ошибка обновления: компонент $RED_LINE не обнаружен!"
fi

echo "Удаляем все старые модули..."
for COMPONENT in $TO_REMOVE; do
    if ( $ASADMIN list-components | grep $COMPONENT ) ; then
        $ASADMIN undeploy $COMPONENT
    fi
done

echo "Перезапуск для гарантии применения свойств и очистки старых модулей..."
$ASADMIN stop-domain oep-dev
$ASADMIN start-domain oep-dev

echo "Устанавливаем новые модули..."
for B in $TO_INSTALL; do
    if ( ! $ASADMIN --port 4848 deploy --name=$B --type=osgi --target=server "./${B}.jar" ); then
        halt 3 "Ошибка обновления: сбой установки компонента $B"
    fi
done

echo "Применение миграций..."
sleep 20

echo "Перезапуск для гарантии чистой среды исполнения..."
$ASADMIN stop-domain oep-dev
$ASADMIN start-domain oep-dev

halt 0 OK
