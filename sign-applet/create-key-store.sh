# This Source Code Form is subject to the terms of the Mozilla Public
# License, v. 2.0. If a copy of the MPL was not distributed with this
# file, You can obtain one at http://mozilla.org/MPL/2.0/.
# Copyright (c) 2013, MPL CodeInside http://codeinside.ru

#!/bin/sh
KEYSTORE="src/main/keystore/signing-jar.keystore"
KEYTOOL=$JAVA_HOME/bin/keytool

$KEYTOOL -genkey -alias signer -keystore $KEYSTORE -storepass k0kumbEr -keypass dul1Tl1 -dname "CN=Signer, OU=GSES, O=ru.codeinside, L=Penza, ST=Russia, C=RU"
$KEYTOOL -selfcert -alias signer -keystore $KEYSTORE -storepass k0kumbEr -keypass dul1Tl1
