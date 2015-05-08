/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2015, MPL CodeInside http://codeinside.ru
 */
$(function() {
    var $isEsiaAuth = $('#isEsiaAuth');

    var loginFieldSwitcher = function () {
        var checked = $(this).prop('checked'),
                $esiaAuthRow = $('.esiaAuthRow'),
                $standardAuthRow = $('.standardAuthRow');
        if (checked) {
            $esiaAuthRow.css('display', 'table-row');
            $standardAuthRow.css('display', 'none');
        } else {
            $esiaAuthRow.css('display', 'none');
            $standardAuthRow.css('display', 'table-row');
        }
    };

    if ($isEsiaAuth) {
        loginFieldSwitcher.call($isEsiaAuth[0]);
        $("#snils").mask("999-999-999 99");
        $isEsiaAuth.on('change', function () {
            loginFieldSwitcher.call(this);
        });
    }
});

