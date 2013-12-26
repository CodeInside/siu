$(function() {
    /*$( "#anchor" ).html(jsonField.firstName + "  " + jsonField.lastName);
    $( "#anchor" ).after( "<p>"+jsonField.lastName+"</p>" );
    $( "#anchor" ).after( "<p>"+jsonField.firstName+"</p>" );*/

    $("#test").val("test2");
    //alert(JSON.stringify(jsonField));
    
	for (var name in jsonField) {
		//console.log(name + " = " + JSON.stringify(jsonField[name]));
		var field = jsonField[name];
			if (typeof(field) == 'object'){
				$("#"+name).val(JSON.stringify(field));	
			}else
				$("#"+name).val(field);
		   
	}
	//$("#idCurrentForm").text("2");
	$("#listSteps").val("1\n2\n4\n7\n8\n18");
	/*
	$("#listSteps").val("1\n2\n3\n4\n5\n6\n7\n8\n9\n10\n11\n12\n13\n14\n15\n16\n17\n18\n19");
	$("#step_7_pbDocs").val(JSON.stringify(jsonField));
	alert($("#step_7_pbDocs").val());

    $("#step_12_msp").val('{"familyMember":[{"code":"1212121212121212","surname":"Иволгин","name":"Антон","patronymic":"Львович","birthday":"01.01.1966","position":"1212121212121212","relationDegree":{"name":"1212121212121212","code":"1212121212121212","dependents":{"dependent":true}},"identityCard":{"type":"Паспорт","series":"5605","number":"123461","dateIssue":"01.01.1975","organization":"Тестовая организация6"},"address":{"postCode":"440010","region":"Нижегородская область","area":"Нижегородская область","city":"Нижний Новгород г","community":"нас.пункт 10","street":"Горького ул","codeKladr":"121212121212121212121212121212","house":"11","housing":"N10","construction":"50","apartment":"120","room":"3","type":"1212121212121212"},"document":{"group":{"name":"1212121212121212","code":"1212121212121212"},"name":"1212121212121212","code":"1212121212121212","series":"1212121212121212","number":"1212121212121212","dateIssue":"01.01.1970","privateStorage":true,"electronicForm":true,"params":{"param":[{"name":"1212121212121212","code":"1212121212121212","type":"1212121212121212","value":"1212121212121212"}]},"reqParams":{"reqParam":[{"name":"1212121212121212","code":"1212121212121212","type":"12121212121212121212121212121212","value":"v1212121212121212"}]}}}],"appealPersonal":true,"reqDept":true}');

	$("#step_13_sdd").val('{"familyMember":[{"code":"123456","surname":"Семгин","name":"Григорий","patronymic":"Валентинович","birthday":"01.01.1966","position":"1313132131313131","relationDegree":{"name":"брат","code":"1313132131313131","dependents":{"dependent":true}},"identityCard":{"type":"Паспорт","series":"5606","number":"123462","dateIssue":"01.01.1976","organization":"Тестовая организация7"},"address":{"postCode":"440011","region":"Московская область","area":"Московская область","city":"Москва г","community":"нас.пункт 11","street":"Ленина ул","codeKladr":"131313213131313131313131313131","house":"12","housing":"N11","construction":"55","apartment":"201","room":"2","type":"фактический"},"document":{"group":{"name":"группа докум.тестовая","code":"1313132131313131"},"name":"документ тестовый","code":"123456","series":"1234","number":"123456","dateIssue":"01.01.1970","privateStorage":true,"electronicForm":true,"params":{"param":[{"name":"Паспорт","code":"123456","type":"1234","value":"12345"}]},"reqParams":{"reqParam":[{"name":"Паспорт","code":"654321","type":"4321","value":"54321"}]}}}],"appealPersonal":true,"reqDept":true}');
	
    $("#step_14_sddnr").val('{"familyMember":[{"code":"123456","surname":"Быстрый","name":"Иннокентий","patronymic":"Петрович","birthday":"01.01.1966","position":"1313132131313131","relationDegree":{"name":"брат","code":"1313132131313131","dependents":{"dependent":true}},"identityCard":{"type":"Паспорт","series":"5606","number":"123462","dateIssue":"01.01.1976","organization":"Тестовая организация7"},"address":{"postCode":"440011","region":"Московская область","area":"Московская область","city":"Москва г","community":"нас.пункт 11","street":"Ленина ул","codeKladr":"131313213131313131313131313131","house":"12","housing":"N11","construction":"55","apartment":"201","room":"2","type":"фактический"},"document":{"group":{"name":"группа докум.тестовая","code":"1313132131313131"},"name":"документ тестовый","code":"123456","series":"1234","number":"123456","dateIssue":"01.01.1970","privateStorage":true,"electronicForm":true,"params":{"param":[{"name":"Паспорт","code":"123456","type":"1234","value":"12345"}]},"reqParams":{"reqParam":[{"name":"Паспорт","code":"654321","type":"4321","value":"54321"}]}}}],"appealPersonal":true,"reqDept":true}');
	$("#step_15_infmi").val('{"familyMembers":[{"code":"151515","surname":"Тезкин","name":"Фрол","patronymic":"Валентинович","birthday":"01.01.1930","profits":{"profit":{"year":"2013","month":"01","amount":"2000","type":"основной","reqParams":{"reqParam":[{"name":"141414141414","code":"141414141414","type":"141414141414","value":"141414141414"}]}}}}],"appealPersonal":true,"reqDept":true}');
	$("#step_16_ir").val('{"infRequest":[{"group":{"name":"1616161616161","code":"1616161616161"},"name":"1616161616161","code":"1616161616161","reqParams":[{"reqParam":[{"name":"1616161616161","code":"16161616161611616161616161","type":"1616161616161","value":"1616161616161"}]}]}]}');
	$("#step_17_ir").val('{"information":[{"comment":"null", "group":{"code":"9","name":"Сведения о наименовании специальности, экзамена, тестирования, вступительного испытания и образовательного учреждения"},"data":[{"Name":"Укажите сведения о наименовании  образовательного учреждения, специальности и (или)  экзамена, тестирования, вступительного испытания","Code":"12","params":{"param":[{"name":"Наименование экзамена, тестирования, вступительного испытания","code":"24","type":"text","value":"111"},{"name":"Образовательное учреждение","code":"25","type":"text","value":"2222"},{"name":"Специальность","code":"26","type":"text","value":"33333"}]}}]}]}');

    $("#step_wait_nd").val('{"document":[{"group":{"name":"Удостоверение","code":"88"},"name":"Пасспорт","code":"52","series":"3421","number":"888888","dateIssue":"01.02.2013","privateStorage":false,"electronicForm":true,"params":{"param":[{"name":"Первый","code":"75","type":"Тип1","value":"Значение1"},{"name":"Второй","code":"44","type":"Тип2","value":"Значение2"}]},"reqParams":{"reqParam":[{"name":"Ntcn","code":"99","type":"Тиииип","value":"Да"}]},"hasDocument":true,"fio":"3444","dateOfRecipt":"0000000000"},{"hasDocument":true,"fio":"uuuuuuuuu","dateOfRecipt":"ttttttttttt","privateStorage":true,"name":"ffffffff","number":"1111"}]}');

    $("#step_18_nd").val('{"document":[{"group":{"name":"18181818","code":"18181818","privateStorage":true},"name":"18181818","code":"18181818","series":"18181818","number":"18181818","dateIssue":"18181818","privateStorage":true,"electronicForm":true,"params":{"param":[{"name":"18181818","code":"18181818","type":"18181818","value":"18181818"}]},"reqParams":{"reqParam":[{"name":"18181818","number":"1111","code":"18181818","type":"18181818","value":"18181818"}]}}]}');	
	
	$("#confirmedDocs").val($("#step_18_nd").val());		
	
	$("#appData_cond").val('[{"number":"1","code":"InformationService","name":"Шаг 1 – Предварительные сведения"},{"number":"4","code":"InformationApplicantLegalEntity","name":"Шаг 4 – Сведения о правообладающем физическом лице"},{"number":"16","code":"InformationRequested","name":"Информация об образовательном учреждении"},{"number":"17","code":"MoreInformation","name":"Сведения для предоставления информации о результатах сданного экзамена, тестирования, вступительного испытания"}]');
	
	$("#step_1_subservices").val("Тестовая подуслуга");
	$("#step_1_category").val("Опекун");
	$("#step_1_category_legal_representative").val("Родитель");
	
	$("#step_2_last_name_legal_representative").val("Иванов");
	$("#step_2_first_name_legal_representative").val("Сергей");
	$("#step_2_middle_name_legal_representative").val("Петрович");
	$("#step_2_birthday_legal_representative").val("01.01.1970");

	$("#step_2_doc_legal_representative_type").val("Паспорт");
	$("#step_2_doc_legal_representative_series").val("1234");
	$("#step_2_doc_legal_representative_number").val("123456");
	$("#step_2_doc_legal_representative_date").val("01.01.1984");
	$("#step_2_doc_legal_representative_org").val("Тестовый ОВД г.Теста");
	
	$("#step_2_address_legal_representative_postal").val("123456");
	$("#step_2_address_legal_representative_house").val("10");
	$("#step_2_address_legal_representative_region").val("Республика Мордовия");
	$("#step_2_address_legal_representative_body").val("1");
	$("#step_2_address_legal_representative_f_district").val("Ленинский");
	$("#step_2_address_legal_representative_build").val("2");
	$("#step_2_address_legal_representative_city").val("Саранск г");
	$("#step_2_address_legal_representative_flat").val("5");
	$("#step_2_address_legal_representative_settlement").val("");
	$("#step_2_address_legal_representative_room").val("2");
	$("#step_2_address_legal_representative_street").val("Суворова ул");
	
	$("#step_2_name_doc").val("Паспорт");
	$("#step_2_series_doc").val("1234");
	$("#step_2_number_doc").val("123456");
	$("#step_2_date_doc").val("01.01.1971");
	$("#step_2_org_doc").val("Тестовый УВД1 г.Саранска");
	
	$("#step_3_full_name_org").val("Тестовая организация 1");
	$("#step_3_reduced_name_org").val("ТО1");
	
	$("#step_3_legal_address_org").val("г.Саранск ул.Тестовая д.1");
	$("#step_3_identity_org_reg").val("г.Саранск ул.Тестовая д.2");
	
	$("#step_3_juridical_inn").val("1234567890");
	$("#step_3_juridical_kpp").val("123456789");
	$("#step_3_juridical_ogrn").val("1234567890123");
	$("#step_3_lastname_org").val("Ильясов");
	$("#step_3_name_org").val("Рустам");
	$("#step_3_middlename_org").val("Ибрагимович");
	$("#step_3_birth_date_org").val("01.01.1966");
	$("#step_3_pozition_manager").val("гл.Бухгалтер");
	
	$("#step_3_step_3_document_type_org").val("Паспорт");
	$("#step_3_document_series_org").val("5601");
	$("#step_3_document_number_org").val("123457");
	$("#step_3_document_issue_date_org").val("01.01.1971");
	$("#step_3_document_org").val("Тестовая организация2 г.Саранск");
	
    $("#step_3_name_doc").val("Паспорт");
	$("#step_3_series_doc").val("4567");
	$("#step_3_number_doc").val("893456");
	$("#step_3_date_doc").val("01.01.1976");
	$("#step_3_org_doc").val("Тест ОВд г.Саранск");
	
	$("#step_4_last_name_declarant").val("Петров");
	$("#step_4_first_name_declarant").val("Алексей");
	$("#step_4_middle_name_declarant").val("Васильевич");
	$("#step_4_birthday_declarant").val("01.01.1966");

	$("#step_4_doc_declarant_type").val("Паспорт");
	$("#step_4_doc_declarant_series").val("5602");
	$("#step_4_doc_declarant_number").val("123458");
	$("#step_4_doc_declarant_date").val("01.01.1972");
	$("#step_4_doc_declarant_org").val("Тестовая организация 3");	
	
	$("#step_4_address_declarant_postal").val("440003");
	$("#step_4_address_declarant_house").val("1");
	$("#step_4_address_declarant_region").val("Республика Мордовия");
	$("#step_4_address_declarant_body").val("4");
	$("#step_4_address_declarant_district").val("Запрудный");
	$("#step_4_address_declarant_city").val("Саранск г");
	$("#step_4_address_declarant_flat").val("6");
	$("#step_4_address_declarant_settlement").val("");
	$("#step_4_address_declarant_room").val("1");
	$("#step_4_address_declarant_street").val("Липовская ул");
	
	$("#step_5_last_name_declarant").val("Сидоров");
	$("#step_5_first_name_declarant").val("Антон"); 
	$("#step_5_patronymic_declarant").val("Сергеевич");
	$("#step_5_birthday_declarant").val("01.01.1936");

	$("#step_5_INN").val("123456789012");
	$("#step_5_OGRNIP").val("123456789012345");
	
	$("#step_5_doc_declarant_type").val("Паспорт");
	$("#step_5_doc_declarant_series").val("5603");
	$("#step_5_doc_declarant_number").val("123459");
	$("#step_5_doc_declarant_date").val("01.01.1973");
	$("#step_5_document_org").val("Тестовая организация4");
	
	$("#step_5_address_declarant_postal").val("440004");
	$("#step_5_address_declarant_house").val("2");
	$("#step_5_address_declarant_region").val("Магаданская область");
	$("#step_5_address_declarant_body").val("5");
	$("#step_5_address_declarant_district").val("Октябрьский");
	$("#step_5_address_declarant_build").val("2");
	$("#step_5_address_declarant_city").val("Магадан");
	$("#step_5_address_declarant_flat").val("3");
	$("#step_5_address_declarant_settlement").val("Праздничный");
	$("#step_5_address_declarant_room").val("4");
	$("#step_5_address_declarant_street").val("Железнодорожная");

	$("#step_6_full_name_org").val("ООО тестовая");
	$("#step_6_reduced_name_org").val("тест");
	$("#step_6_legal_address_org").val("440005, Липецкая область, Липецк г, нас.пункт.5, Южная ул, 6, N5, 25, 52, 2");
	$("#step_6_identity_org_reg").val("440005, Липецкая область, Липецк г, нас.пункт.5, Южная ул, 6, N5, 25, 52, 2");
	$("#step_6_juridical_inn").val("1234567890");
	$("#step_6_juridical_kpp").val("123456789");
	$("#step_6_juridical_ogrn").val("1234567890123");
	
	$("#step_6_lastname_org").val("Кирсанов");
	$("#step_6_name_org").val("Николай");
	$("#step_6_middlename_org").val("Никифорович");
	$("#step_6_birth_date_org").val("01.01.1996");
	$("#step_6_pozition_manager").val("гл.Инженер");
	
	$("#step_6_step_6_document_type_org").val("Паспорт");
	$("#step_6_document_series_org").val("6767");
	$("#step_6_document_number_org").val("456456");	
	$("#step_6_document_issue_date_org").val("01.01.2010");
	$("#step_6_document_org").val("Тестовая организация5");
	
	
	$("#step_7_last_name_people").val("Скорый");
	$("#step_7_first_name_people").val("Осип");
	$("#step_7_middle_name_people").val("Степанович");
	$("#step_7_birthday_people").val("01.04.1935");
	$("#step_7_relation_degree").val("дед");
	$("#step_7_is_dependency").val("");
	
	$("#step_7_people_address_v").val("879435");
	$("#step_7_people_house_v").val("17");
	$("#step_7_people_region_v").val("Пензенская область");
	$("#step_7_people_housing_v").val("3");
	$("#step_7_people_district_v").val("Первомайский");
	$("#step_7_people_building_v").val("5");
	$("#step_7_people_city_v").val("Заречный г");
	$("#step_7_people_flat_v").val("87");
	$("#step_7_people_settement_v").val("dfgdf");
	$("#step_7_people_room_v").val("4");
	$("#step_7_people_street_v").val("Восточная ул");
	
	$("#step_8_last_name_recept").val("Пронин");
	$("#step_8_first_name_recept").val("Артур");
	$("#step_8_middle_name_recept").val("Эдуардович");
	$("#step_8_birthday_recept").val("02.03.1945");
	
	$("#step_8_payment_type").val("Почта");
	
	$("#step_8_postal_number_system").val("35");
	$("#step_8_postal_address_v").val("440035");
	$("#step_8_house_v").val("1");
	$("#step_8_region_v").val("Пензенская обл");
	$("#step_8_housing_v").val("1");
	$("#step_8_district_v").val("Центральный");
	$("#step_8_building_v").val("3");
	$("#step_8_city_v").val("Кузнецк");
	$("#step_8_flat_v").val("65");
	$("#step_8_settment_v").val("Кузнецк г");
	$("#step_8_room_v").val("1");
	$("#step_8_street_v").val("Степная");	
	
	$("#step_8_bank_name_system").val("РосСельхозБанк");
	$("#step_8_bank_subdivision_system").val("Ближнее");
	$("#step_8_bank_account_system").val("12345678");
	
	$("#step_9_last_name_recept").val("Галкин");
	$("#step_9_first_name_recept").val("Владимир");
	$("#step_9_middle_name_recept").val("Юрьевич");
	$("#step_9_birthday_recept").val("01.07.1961");
	
	$("#step_9_payment_type").val("Банк");
	
	$("#step_9_bank_name_system").val('Банк "Связной"');
	$("#step_9_bank_subdivision_system").val('Южное');
	$("#step_9_bank_account_system").val('34646454878');
	
	$("#step_10_full_name_org_akcept").val('Организация тестовая 10');
	$("#step_10_payment_type").val("Банк"); 	
	
	$("#step_10_bank_name_system").val('Банк "Кузнецкий"');
	$("#step_10_bank_subdivision_system").val("Северное");
	$("#step_10_bank_account_system").val("142254435");
	$("#step_10_number_cor_account").val("5457846883425");
	$("#step_10_bik").val("1122423452314");
	
	$("#step_11_render_address_type_system").val("по месту жительства");
	$("#step_11_index_pu").val("325434");
	$("#step_11_house_pu").val("14");	
	$("#step_11_region_pu").val("Республика Мордовия");
	$("#step_11_corps_pu").val("5");
	$("#step_11_district_pu").val("Ленинский");
	$("#step_11_building_pu").val("15");
	$("#step_11_city_pu").val("Саранск г");
	$("#step_11_flat_pu").val("15");	
	$("#step_11_settment_pu").val("44");
	$("#step_11_room_pu").val("1");
	$("#step_11_street_pu").val("Трудовая");*/
    
	function showstyle()
	{

	    $('span.print_title').css({'text-align':'center','display':'block','font-weight':'bold'});	
		$('span.print_text').css({'text-align':'left','font-style':'italic','font-weight':'normal'});
		$('span.print_textr').css({'text-align':'right','font-style':'italic','font-weight':'normal'});
		$('span.print_subTitle').css({'text-align':'left','display':'block','font-weight':'bold'});	
		$('span.print_hint').css({'text-align':'center','display':'block','font-size':'12px','padding-bottom':'20px'});
		$('span.print_label').css({'text-align':'left','display':'block','font-weight':'bold'});
		$('span.print_tlabel').css({'text-align':'center','display':'block','font-weight':'bold'});
		
		$('table tr').css('background-color', '#efebde');
		$('fieldset').css('background-color', '#efebde');
		
		$('.indent_clear').css({'margin':'0','padding':'0'});
		$('input[type=text]').css('width','100%');
		$('select').css('width','100%');
		$('textarea').css('width','100%');
		$('input.input_w1').css('width','60%');
		$('input.input_w2').css('width','25%');
		$('input.input_w3').css('width','15%');
		$('input.input_w4').css('width','35%');
		$('select.select_w5').css('width','45%');
		$('select.select_w1').css('width','65%');
		$('select.select_w2').css('width','25%');
		$('textarea').css('height','40px');
		$('textarea').attr("resizable",true);
		
		
		

		$('table').css('width','100%');
		$('table.mid').css({'width':'94%','margin':'auto'});
		$('table.mid.right').css({'margin-left':'auto','margin-right':'5px'});
		$('table.mid.right table').css({'width':'98%','margin-right':'auto'});
		$('table.mid2').css({'width':'74%','margin':'auto'});
		$('table.small').css({'width':'50%','margin':'auto'});
			
		$('table tr td.td_ext').css('width', '31%');
		$('table tr td.td_ext2').css('width', '23%');
	        $('table tr td.td_ext3').css('width', '50%');
	        $('table tr td.td_ext4').css('width', '40%');
	        $('table tr td.td_ext5').css('width', '15%');
			$('table tr td.td_ext6').css('width', '65%');

		$('fieldset').css({'border':'1px solid #d8d7c7', 'margin-bottom':'15px', 'margin-left':'30px', 'margin-right':'30px', '-webkit-padding-before': '0.35em','-webkit-padding-start': '0.75em',	'-webkit-padding-end': '0.75em','-webkit-padding-after':'0.625em'});
		$('fieldset.mid').css({ 'width':'80%','margin':'auto'});
		$('fieldset.manual').css({'border':'1px solid #d8d7c7', 'margin-bottom':'15px', 'margin-left':'30px', 'margin-right':'30px', '-webkit-padding-before': '0.35em','-webkit-padding-start': '0.75em',	'-webkit-padding-end': '0.75em','-webkit-padding-after':'0.625em'});
		
		$('span.form_label').css({'text-align':'center','display':'block','color':'#0000ff','font-weight':'bold','font-size':'14px'});

		$('span.title').css({'text-align':'center','display':'block', 'color':'#ff0000','font-weight':'bold'});
		$('legend').css('font-weight','normal');
	    $('legend.group_label').css({'color':'#0000ff', 'font-style':'italic', 'text-align':'left',  'font-size':'13px'});
	    $('legend.group_label.group_hint').css('color','#898989');
	    $('span.label').css({'color':'#000000','font-weight':'bold', 'font-size':'12px'});
	    $('span.label.label_hint').css('color','#898989');
		$('span.step_label').css({'text-align':'left','display':'block','color':'#0000ff'});
	    $('span.hint').css({'color':'#898989','font-style':'italic','font-size':'11px', 'margin-left':'25px', 'padding-bottom':'20px'});
	    $('span.hint.no_indent').css({'margin':'0','padding':'0'});
	    $('#tabbs').css({'position':'relative', 'padding':'20px', 'margin':'0 auto', 'background-color':'#fff', 'width':'98%', 'min-width':'1006px'});
	    $('.content').css({'border-left':'1px solid #ddd','border-right':'1px solid #ddd', 'width':'100%', 'margin-left':'0px'});
	    $('.content div').css({'padding':'7px 20px 20px 13px', 'color':'black'});
	    $('.clone_text').css('width','355px');
	    $('.field-requiredMarker').css({'color':'#f00', 'font-style':'italic'});
		
		
		
	}
	
	if ($("#idCurrentForm").text()=="0" || $("#idCurrentForm").text()=="2") 
	{
	    
		f_steps = $("#listSteps");
	    lst_steps = f_steps.val().split("\n");
	    
		for (i=1; i<=18; i++)
		{	
			$("fieldset#tab_"+i).hide(); 
		}

		for (var i in lst_steps)
		{	
			$("fieldset#tab_"+lst_steps[i]).show(); 
		}	
		
		$("#print-tab").remove();    
		
	    $("#switch_tab_30").show();
			$("#fieldset_tab_0").hide();
	    $("#switch_tab_30").val("Экранная форма заявления");	
	}

	if ( $("#idCurrentForm").text()=="2" ) 
	{  
	 
	  $("#date_reference_number").val(getCurDate());   
	}
	else
	{  
	  $("#date_reference_number").parent().find(".ui-datepicker-trigger").hide();  
	}

	if ($("#idCurrentForm").text()=="5" || $("#idCurrentForm").text()=="6") 
	{
		switchStateDateTimePicker(false);
		f_steps = $("#listSteps");
	    lst_steps = f_steps.val().split("\n");
		
		for (i=1; i<=18; i++)
		{	
			$("fieldset#tab_"+i).hide(); 
		}

		for (var i in lst_steps)
		{	
			$("fieldset#tab_"+lst_steps[i]).show(); 
		}
		
		$("fieldset#tab_19").show(); 		
		
		$("#print-tab").remove();	
	    switchStateDateTimePicker(true);	
		
		$("#fieldset_tab_0").hide();		
	    $("#fieldset_tab_30").hide();
	    $("#fieldset_tab_20").hide();

		$("#switch_tab_20").val("+");	  
	    $("#switch_tab_0").val("Печатная форма заявления");		
		
		$("#date_registration_solutions_p_1").val(getCurDate());
	}


	if ($("#idCurrentForm").text()=="4" || $("#idCurrentForm").text()=="7") 
	{    
		f_steps = $("#listSteps");
	    lst_steps = f_steps.val().split("\n");
		
		for (i=1; i<=18; i++)
		{	
			$("fieldset#tab_"+i).hide(); 
		}

		for (var i in lst_steps)
		{	
			$("fieldset#tab_"+lst_steps[i]).show(); 
		}
	    switchStateDateTimePicker(false);
		
	    $("fieldset#tab_19").show(); 		
		
		$("#print-tab").remove();
				
	      $("#switch_tab_0").val("Печатная форма заявления");	
		  $("#switch_tab_20").val("+");
		
		  $("#fieldset_tab_0").hide();		
	      $("#fieldset_tab_30").hide();
	      $("#fieldset_tab_20").hide();	
	}


	if ($("#idCurrentForm").text()=="3") {
	    
	    switchStateDateTimePicker(false);	
		
		f_steps = $("#listSteps");
	    lst_steps = f_steps.val().split("\n");
		
		for (i=1; i<=18; i++)
		{	
			$("fieldset#tab_"+i).hide(); 
		}

		for (var i in lst_steps)
		{	
			$("fieldset#tab_"+lst_steps[i]).show(); 
		}
		
		$("fieldset#tab_19").show(); 
		
		
		$("#transitions").find('.ui-button').each(function(){
		  $(this).click(function(){
		    $('#save').click();
		  });
		});	

		$("#print-tab").remove();		
			

		$("input[name='mark_receipt_l_y']").live("click", function(){
		    testReguest($(this),false);
		});
		
		$("input[name='mark_request_mv_t']").live("click", function(){	    
		    testReguestMv($(this),false);		
		});
		
		
		
		
		
		$("#save").click(function save() {
		    var data = new Array();
			
			if (window.flagSave == true) return;
			window.flagSave = true;
			
			$(".clone_block_1w:visible").each(function () {
				var checkbox = $(this).find("textarea[name='name_document_2_y']").attr("id");
				var pos = checkbox.lastIndexOf('_');
				var i = checkbox.substr(pos + 1);			

				if(window.outputData.document[i]==null){
				  window.outputData.document[i] = {};
				}						
			    window.outputData.document[i].name = $("#name_document_2_y_"+ i).val();			
				window.outputData.document[i].hasDocument = $("#mark_receipt_l_y_"+ i).attr("checked");			
				window.outputData.document[i].rekvDocument = $("#rekv_document_2_y_"+ i).val();
				window.outputData.document[i].fio = $("#fio_document_2_y_"+ i).val();
				window.outputData.document[i].dateOfRecipt = $("#date_receipt_l_y_"+ i).val();
				window.outputData.document[i].privateStorage = true;
				window.outputData.document[i].isAdded = $("#isAdded_"+ i).attr("checked");
				data.push(window.outputData.document[i]);
			});		
			
			$(".clone_block_2w:visible").each(function () {            
				var checkbox = $(this).find("textarea[name='name_data_mv_t']").attr("id");
				var pos = checkbox.lastIndexOf('_');
				var i = checkbox.substr(pos + 1);
	            
				
				if(window.outputData.document[i]==null){
				  window.outputData.document[i] = {};
				}
				window.outputData.document[i].privateStorage = false;
				window.outputData.document[i].hasRequest = $("#mark_request_mv_t_"+ i).attr("checked");
				window.outputData.document[i].name = $("#name_data_mv_t_"+ i).val();
				window.outputData.document[i].fio = $("#fio_document_3_y_"+ i).val();
				window.outputData.document[i].dateOfRecipt = $("#date_receipt_mv_t_"+ i).val();
				
				window.outputData.document[i].reqNumber = $("#number_reguest_mv_"+ i).val();
				
				window.outputData.document[i].sendDate = $("#date_request_mv_t_"+ i).val();
				window.outputData.document[i].isAdded = $("#isAdded_"+ i).attr("checked");
				data.push(window.outputData.document[i]);
				
			});
			
			window.outputData = null;
			window.outputData = data;	
	        $("#step_wait_nd").val("");		
	        $("#step_wait_nd").val(JSON.stringify(window.outputData));
		});

		$("#add").unbind("click");
		$("#add").click(function () {
		    
	        switchStateDateTimePicker(false);
			
			var i = getFreeIndex();		
			
			
			var el = $("#docsPrivate .clone_block_1w:first").clone();
			$("#docsPrivate").append(el);		
			
			

			el.find("input").each(function () {           
			    if ($(this).attr("name")!="") $(this).val("");
				$(this).attr("id", $(this).attr("name") + "_" + i);
				if ($(this).attr("name")=="choice_trustee_rek") 
				{
				  $(this).attr("disabled",true);	
				  $(this).attr("checked",true);
				  $(this).closest("tr").hide();
				}			
				if ($(this).attr("name")=="date_receipt_l_y")
				{
				  $(this).attr("disabled",true);	
				}			
			});			
			
			
			el.find("textarea").each(function () {            		    		
				$(this).attr("id", $(this).attr("name") + "_" + i);			
				$(this).attr("required",true);			
			});
			el.find("#rekv_clone").show();		
			switchStateDateTimePicker(true);		
			
			el.find(".btn_delete_step_18_reg_info:last").show();
			
			el.show();
			
			el.find('input[name="isAdded"]').attr("checked",true);
			
			$(".datepicker").each(function () 
			{
			  if ($(this).attr("disabled")) $(this).parent().find(".ui-datepicker-trigger").hide();
			  else $(this).parent().find(".ui-datepicker-trigger").show();	  
			});
			
			
			return false;
		});
		
		
		$("#add_mv").unbind("click");
		$("#add_mv").click(function () {		
	        switchStateDateTimePicker(false);		
			
			var i = getFreeIndex();
			
			
			var el = $(".clone_block_2w:first").clone();
			$("#docsMV").append(el);
			

			el.find("input").each(function () {           
			    if ($(this).attr("name")!="") $(this).val("");		
				$(this).attr("id", $(this).attr("name") + "_" + i);
				if ($(this).attr("name") == "number_reguest_mv") {$(this).attr("disabled",true);}
				if ($(this).attr("name") == "date_request_mv_t") {$(this).attr("disabled",true);}
				if ($(this).attr("name") == "date_receipt_mv_t") {$(this).attr("disabled",true);}			
			});			
					
			el.find("textarea").each(function () {            
		        if ($(this).attr("name")!="") $(this).val("");			
				$(this).attr("id", $(this).attr("name") + "_" + i);						
				$(this).attr("disabled",false);
			});
			switchStateDateTimePicker(true);		
			
			el.find(".btn_delete_step_18_mv_info").show();
			el.find('input[name="isAdded"]').attr("checked",true);
			el.find('input[name="choice_trustee_rek"]').attr("disabled",true);		
			el.find('input[name="choice_trustee_rek"]').closest("tr").hide();
			el.show();
			
			
			$(".datepicker").each(function () 
			{
			  if ($(this).attr("disabled")) 
			  {$(this).parent().find(".ui-datepicker-trigger").hide();}
			  else 
			  {$(this).parent().find(".ui-datepicker-trigger").show();}	  
			});
			

		});
	    
		$('input[name="add"]').val("Добавить"); 
		$('input[name="add_mv"]').val("Добавить"); 		
		$(".btn_delete_step_18_reg_info").val("Удалить");
		$(".btn_delete_step_18_mv_info").val("Удалить");
		
		$(".save").hide();	
		
		  $("#fieldset_tab_0").hide();
		  $("#fieldset_tab_30").hide();
	      $("#fieldset_tab_20").hide();	  
		  $("#switch_tab_0").val("Печатная форма заявления");
	}



	f_steps = $("#listSteps");
	lst_steps = f_steps.val().split("\n");

	$(".step_7_clone_block").hide();
	
	var text_html='<br/><br/><span class="print_label">исх N:&nbsp;<span class="print_text" id="print_num_query"></span></span><br/>'+
	'<span class="print_title" id="printNameStep1" name="printNameStep1">Учреждение</span>'+
	'		<span class="print_hint" id="print_step_1_social_institution"></span>'+
	'<br/>';

	text_html+='<div id="requestParametersUni"></div>';
	text_html+='<textarea id="isPortal" style="display:none;">false</textarea>';



	if ($("#reference_number").val() != "")
	{
	text_html+=
	(
	  $("#reference_number").val() == "" ? "":'		<span class="print_title">ЗАЯВЛЕНИЕ N&nbsp;<span class="print_text" id ="print_reference_number"></span>&nbsp;от&nbsp;<span class="print_text" id="print_date_reference_number"></span></span>')+
	  '		<span class="print_hint">о предоставлении государственной (муниципальной) услуги</span>';
	}  
	text_html+='		<span class="print_label">Наименование услуги&nbsp;<span class="print_text" id="print_name_serv"></span></span>'+		
	( $("#step_1_subservices").val()=="" ? "":'		<span class="print_label">Подуслуга&nbsp;<span class="print_text" id="print_sub_serv"></span></span>');


	text_html+=
	'		<span class="print_label">Категория получателя&nbsp;<span class="print_text" id="print_cat_recipient"></span></span>';

	if (lst_steps.indexOf("2")>=0 || lst_steps.indexOf("3")>=0)
	{
	  text_html+='<span class="print_label">Категория законного представителя&nbsp;'+'<span class="print_text" id="print_step_1_category_legal_representative"></span></span>';
	}
	text_html+='		<br/>';



	if (lst_steps.indexOf("2")>=0)
	{
	text_html+=
	'   <!-- 2 -->'+				
	'		<span class="print_title" id="print_2_title">Сведения о законном представителе (ФЛ)&nbsp;</span>'+	
	'		<br/>'+
	'		<span class="print_label">ФИО&nbsp;<span class="print_text" id="print_step_2_last_name_legal_representative"></span></span>'+
	'		<span class="print_label">Дата рождения&nbsp;<span class="print_text" id="print_step_2_birthday_legal_representative"></span></span>'+
	'       <span class="print_label">Данные документа, удостоверяющего личность&nbsp;<span class="print_text" id="print_step_2_legal_representative"></span></span>'+
	'       <span class="print_label">Адрес регистрации&nbsp;<span class="print_text" id="print_step_2_reg_adr"></span></span>'+		
	'       <span class="print_label">Данные документа, удостоверяющего полномочия&nbsp;<span class="print_text" id="print_step_2_name_doc"></span></span>'+		
	'		<br/>';
	}


	if (lst_steps.indexOf("3")>=0)
	{
	text_html+=
	'	<!-- 3 -->'+				
	'		<span class="print_title"  id="print_3_title">Сведения о законном представителе (ЮЛ)</span>'+		
	'		<br/>'+
	'	    <span class="print_label" >Полное наименование организации&nbsp;<span class="print_text" id="print_step_3_full_name_org"></span></span>'+
	'		<span class="print_label" >Сокращенное наименование организации&nbsp;<span class="print_text" id="print_step_3_reduced_name_org"></span></span>'+
	'		<span class="print_label" >Юридический адрес организации&nbsp;<span class="print_text" id="print_step_3_legal_address_org"></span></span>'+
	'		<span class="print_label" >Фактический адрес организации&nbsp;<span class="print_text" id="print_step_3_identity_org_reg"></span></span>'+
	'		<span class="print_label">ИНН&nbsp;<span class="print_text" id="print_step_3_juridical_inn"></span></span>'+
	'		<span class="print_label">КПП&nbsp;<span class="print_text" id="print_step_3_juridical_kpp"></span></span>'+
	'		<span class="print_label">ОГРН&nbsp;<span class="print_text" id="print_step_3_juridical_ogrn"></span></span>'+
	'		<span class="print_label">Сведения об уполномоченном лице&nbsp;</span>'+
	'		<span class="print_label">ФИО&nbsp;<span class="print_text" id="print_step_3_fio_org"></span></span>'+
	'		<span class="print_label">Дата рождения&nbsp;<span class="print_text" id="print_step_3_birth_date_org"></span></span>'+
	'		<span class="print_label">Должность&nbsp;<span class="print_text" id="print_step_3_pozition_manager"></span></span>'+
	'		<span class="print_label">Данные документы, удостоверяющего личность&nbsp;<span class="print_text" id="print_step_3_document_type_org"></span></span>'+
	'		<span class="print_label">Данные документы, удостоверяющего полномочия законного представителя&nbsp;<span class="print_text" id="print_step_3_name_doc"></span></span>'+		
	'		<br/>';
	}


	if (lst_steps.indexOf("4")>=0)
	{
	text_html+=
	'    <!-- 4 -->'+
	'		<span class="print_title" id="print_4_title">Сведения о правообладателе (ФЛ)</span>'+
	'		<br/>'+
	'		<span class="print_label">ФИО&nbsp;<span class="print_text" id="print_step_4_fio_declarant"></span></span>'+
	'		<span class="print_label">Дата рождения&nbsp;<span class="print_text" id="print_step_4_birthday_declarant"></span></span>'+
	'		<span class="print_label">Данные документы, удостоверяющего личность&nbsp;<span class="print_text" id="print_step_4_doc_declarant_type"></span></span>'+
	'		<span class="print_label">Адрес регистрации&nbsp;<span class="print_text" id="print_step_4_address_declarant_postal"></span></span>'+
	'		<br/>';
	}

	if (lst_steps.indexOf("6")>=0)
	{
	text_html+=
	'    <!-- 6 -->'+		
	'		<span class="print_title"  id="print_6_title">Сведения о правообладателе (ЮЛ)</span>'+
	'		<br/>'+
	'	    <span class="print_label">Полное наименование организации&nbsp;<span class="print_text" id="print_step_6_full_name_org"></span></span>'+
	'		<br/>'+
	'		<span class="print_label">Сокращенное наименование организации&nbsp;<span class="print_text" id="print_step_6_reduced_name_org"></span></span>'+
	'		<span class="print_label">Юридический адрес организации&nbsp;<span class="print_text" id="print_step_6_legal_address_org"></span></span>'+
	'		<span class="print_label">Фактический адрес организации&nbsp;<span class="print_text" id="print_step_6_identity_org_reg"></span></span>'+
	'		<span class="print_label">ИНН&nbsp;<span class="print_text" id="print_step_6_juridical_inn"></span></span>'+
	'		<span class="print_label">КПП&nbsp;<span class="print_text" id="print_step_6_juridical_kpp"></span></span>'+
	'		<span class="print_label">ОГРН&nbsp;<span class="print_text" id="print_step_6_juridical_ogrn"></span></span>'+
	'		<span class="print_label">Сведения об уполномоченном лице:</span>'+
	'		<span class="print_label">ФИО&nbsp;<span class="print_text" id="print_step_6_fio_org"></span></span>'+
	'		<span class="print_label">Дата рождения&nbsp;<span class="print_text" id="print_step_6_birth_date_org"></span></span>'+
	'		<span class="print_label">Должность&nbsp;<span class="print_text" id="print_step_6_pozition_manager"></span></span>'+
	'		<span class="print_label">Данные документы, удостоверяющего личность&nbsp;<span class="print_text" id="print_step_6_document_type_org"></span></span>'+		
	'		<br/>';
	}

	if (lst_steps.indexOf("5")>=0)
	{
	text_html+=
	'    <!-- 5 -->'+	
	'		<span class="print_title" id="print_5_title">Сведения о правообладателе (ИП)</span>'+
	'		<br/>'+
	'		<span class="print_label">ФИО&nbsp;<span class="print_text" id="print_step_5_fio_declarant"></span></span>'+
	'		<span class="print_label">Дата рождения&nbsp;<span class="print_text" id="print_step_5_birthday_declarant"></span></span>'+
	'		<span class="print_label">Данные документы, удостоверяющего личность&nbsp;<span class="print_text" id="print_step_5_doc_declarant"></span></span>'+
	'		<span class="print_label">Адрес регистрации&nbsp;<span class="print_text" id="print_step_5_address_declarant_postal"></span></span>'+
	'		<span class="print_label">ИНН&nbsp;<span class="print_text" id="print_step_5_INN"></span></span>'+
	'		<span class="print_label">ОГРНИП&nbsp;<span class="print_text" id="print_step_5_OGRNIP"></span></span>'+
	'		<br/>';
	}



	if (lst_steps.indexOf("7")>=0)
	{
	text_html+=
	'	<!-- 7 -->'+
	'		<span class="print_title"  id="print_7_title">Сведения о лице, на основании данных которого оказывается услуга</span>'+
	'		<br/>'+
	'		<span class="print_label">ФИО&nbsp;<span class="print_text" id="print_step_7_fio_people"></span></span>'+
	'		<span class="print_label">Дата рождения&nbsp;<span class="print_text" id="print_step_7_birthday_people"></span></span>';
	'		<span class="print_label">Степень родства&nbsp;<span class="print_text" id="print_step_7_relation_degree">></span></span>';

	text_html+='<span class="step_7_print_block print_label">Данные документа, удостоверяющего личность&nbsp;</span>'+
	'<span id="print7_doc_after"><span class="print7_doc_clone print_text"><span class="print_text" name="print7_doc"></span></span></span>';

	text_html+='		<span class="print_label">Адрес регистрации&nbsp;<span class="print_text" id="print_step_7_people_address_v"></span></span>'+
	'		<br/>';
	}

	if (lst_steps.indexOf("12")>=0)
	{

	var tmp = $("#step_12_msp").val();	
	if (tmp != "" && tmp != null && tmp !="null")
	{

	text_html+=
	'	<!-- 12 -->'+
	'		<span class="print_title"  id="print_12_title">Сведения о членах семьи правообладателя для предоставления государственной услуги</span>'+
	'		<br/>'+
	'		<table border="1" id="printTable12">'+
	'			<tbody>'+
	'			<tr>'+
	'				<td align="center">'+
	'					<span class="print_tlabel">N п/п</span>'+
	'				</td>'+
	'				<td align="center">'+
	'					<span class="print_tlabel">Фамилия, имя, отчество</span>'+
	'				</td>'+
	'				<td align="center">'+
	'					<span class="print_tlabel">Дата рождения</span>'+
	'				</td>'+				
	'			</tr>'+
	'			<tr class="print_step_12_clone_block">'+
	'				<td align="center">'+
	'					<span class="print_step_12_num_pp print_text"></span>'+
	'				</td>'+
	'				<td align="center">'+
	'					<span class="print_step_12_fio print_text"></span>'+
	'				</td>'+
	'				<td align="center">'+
	'					<span class="print_step_12_birthday print_text"></span>'+
	'				</td>'+				
	'			</tr>'+
	'			</tbody>'+
	'		</table>'+	
	'		<br/>';
	}

	}

	if (lst_steps.indexOf("13")>=0)
	{
	text_html+=
	'	<!-- 13 -->'+
	'		<span class="print_title" id="print_13_title"  id="titStep13">Сведения о членах семьи для расчета СДД (среднедушевой доход), зарегистрированных по адресу регистрации правообладающего лица</span>'+
	'		<br/>'+
	'		<table border="1" id="printTable13">'+
	'			<tbody>'+
	'			<tr>'+
	'				<td align="center">'+
	'					<span class="print_tlabel">N п/п</span>'+
	'				</td>'+
	'				<td align="center">'+
	'					<span class="print_tlabel">Фамилия, имя, отчество</span>'+
	'				</td>'+
	'				<td align="center">'+
	'					<span class="print_tlabel">Дата рождения</span>'+
	'				</td>'+
	'				<td align="center">'+
	'					<span class="print_tlabel">Степень родства</span>'+
	'				</td>'+				
	'			</tr>'+
	'			<tr class="print_step_13_clone_block">'+
	'				<td align="center">'+
	'					<span class="print_step_13_num_pp print_text"></span>'+
	'				</td>'+
	'				<td align="center">'+
	'					<span class="print_step_13_fio print_text"></span>'+
	'				</td>'+
	'				<td align="center">'+
	'					<span class="print_step_13_birthday print_text"></span>'+
	'				</td>'+				
	'				<td align="center">'+
	'					<span class="print_step_13_relationdegree print_text"></span>'+
	'				</td>'+				
	'			</tr>'+
	'			</tbody>'+
	'		</table>'+	
	'		<br/>';
	}

	if (lst_steps.indexOf("14")>=0)
	{
	text_html+=
	'	<!-- 14 -->'+
	'		<span class="print_title" id="print_14_title">Сведения о членах семьи для расчета СДД (среднедушевой доход), не зарегистрированных по адресу регистрации правообладающего лица</span>'+
	'		<br/>'+
	'		<table border="1" id="printTable14">'+
	'			<tbody>'+
	'			<tr>'+
	'				<td align="center">'+
	'					<span class="print_tlabel">N п/п</span>'+
	'				</td>'+
	'				<td align="center">'+
	'					<span class="print_tlabel">Фамилия, имя, отчество</span>'+
	'				</td>'+
	'				<td align="center">'+
	'					<span class="print_tlabel">Дата рождения</span>'+
	'				</td>'+
	'				<td align="center">'+
	'					<span class="print_tlabel">Степень родства</span>'+
	'				</td>'+				
	'			</tr>'+
	'			<tr class="print_step_14_clone_block">'+
	'				<td align="center">'+
	'					<span class="print_step_14_num_pp print_text"></span>'+
	'				</td>'+
	'				<td align="center">'+
	'					<span class="print_step_14_fio print_text"></span>'+
	'				</td>'+
	'				<td align="center">'+
	'					<span class="print_step_14_birthday print_text"></span>'+
	'				</td>'+				
	'				<td align="center">'+
	'					<span class="print_step_14_relationdegree print_text"></span>'+
	'				</td>'+				
	'			</tr>'+
	'			</tbody>'+
	'		</table>'+	
	'		<br/>';
	}

	if (lst_steps.indexOf("15")>=0)
	{   

	text_html+=
	'		<!-- 15 -->'+
	'		<span class="print_title" id="print_15_title">Сведения о доходах всех членов семьи правообладателя за XX месяца(ев), предшествующих месяцу подачи заявления</span>'+
	'		<br/>'+
	'		<table border="1" id="printTable15">'+
	'			<tbody>'+
	'			<tr>'+
	'				<td align="center">'+
	'					<span class="print_tlabel">N п/п</span>'+
	'				</td>'+
	'				<td align="center">'+
	'					<span class="print_tlabel">Фамилия, имя, отчество</span>'+
	'				</td>'+
	'				<td align="center">'+
	'					<span class="print_tlabel">Дата рождения</span>'+
	'				</td>'+
	'				<td align="center">'+
	'					<span class="print_tlabel">Месяц</span>'+
	'				</td>'+
	'				<td align="center">'+
	'					<span class="print_tlabel">Год</span>'+
	'				</td>'+
	'				<td align="center">'+
	'					<span class="print_tlabel">Вид дохода</span>'+
	'				</td>'+
	'				<td align="center">'+
	'					<span class="print_tlabel">Сумма дохода</span>'+
	'				</td>'+
	'			</tr>'+
	'			<tr class="print_step_15_clone_block">'+
	'				<td align="center">'+
	'					<span class="print_step_15_num_pp print_text"></span>'+
	'				</td>'+
	'				<td align="center">'+
	'					<span class="print_step_15_fio print_text"></span>'+
	'				</td>'+
	'				<td align="center">'+
	'					<span class="print_step_15_birthday print_text"></span>'+
	'				</td>'+				
	'				<td align="center">'+
	'					<span class="print_step_15_month print_text"></span>'+
	'				</td>'+				
	'				<td align="center">'+
	'					<span class="print_step_15_year print_text"></span>'+
	'				</td>'+
	'				<td align="center">'+
	'					<span class="print_step_15_type print_text"></span>'+
	'				</td>'+
	'				<td align="center">'+
	'					<span class="print_step_15_amount print_text"></span>'+
	'				</td>'+
	'			</tr>'+
	'			</tbody>'+
	'		</table>'+
	'		<br/>';

	}

	if (lst_steps.indexOf("11")>=0)
	{
	text_html+=
	'		<!-- 11 -->'+
	'		<span class="print_title" id="print_11_title">Сведения об адресе предоставления услуги</span>'+
	'		<br/>'+
	'		<span class="print_label" id="print_step_11_render_address_type_system"></span><span class="print_text" id="print_step_11_index_pu"></span>'+
	'<br/>';
	}


	if (lst_steps.indexOf("8")>=0)
	{
		text_html+=
		'<br/>'+
		'		<!-- 8 -->'+
		'		<span class="print_title" id="print_8_title">Сведения о почтовых (банковских) реквизитах (ФЛ)</span>'+
		'		<br/>'+
		'		<span class="print_label">ФИО&nbsp;<span class="print_text" id="print_step_8_fio_recept"></span></span>'+
		'		<span class="print_label">Дата рождения&nbsp;<span class="print_text" id="print_step_8_birthday_recept" ></span></span>'+
		'<br/>';	

		if ($("#step_8_payment_type").val()=="Почта") 
		{
			text_html+=		
			'		<span class="print_subTitle">Почтовые реквизиты</span>'+		
			'		<br/>'+				
			'		<span class="print_label">Номер почтового отделения&nbsp;<span class="print_text"  id="print_step_8_postal_number_system"></span></span>'+
			'		<span class="print_label">Адрес&nbsp;<span class="print_text"  id="print_step_8_postal_address_v"></span></span>';
		}	
		else
		{
		 text_html+='</span></span>'+	 
		 '		<span class="print_subTitle" id="svevBankRekv">Банковские реквизиты</span>'+ 	 
		 '		<br/>'+			 
		 '		<span class="print_label">Наименование банка&nbsp;<span class="print_text" id="print_step_8_bank_name_system"></span></span>'+
		 '		<span class="print_label">Наименование подразделения банка&nbsp;<span class="print_text" id="print_step_8_bank_subdivision_system"></span></span>'+
		 '		<span class="print_label">Номер лицевого счета&nbsp;<span class="print_text" id="print_step_8_bank_account_system"></span></span>';
		}

	}

	if (lst_steps.indexOf("10")>=0)
	{
	text_html+=
	'		<!-- 10 -->'+
	'		<span class="print_title" id="print_10_title">Сведения о выплатных реквизитах (ЮЛ)</span>'+
	'		<br/>'+
	'		<span class="print_label">Полное наименование организации&nbsp;<span class="print_text" id="print_step_10_full_name_org_akcept"></span></span>'+
	'		<br/>'+
	'		<span class="print_subTitle">Банковские реквизиты</span>'+
	'		<br/>'+
	'		<span class="print_label">Наименование банка&nbsp;<span class="print_text" id="print_step_10_bank_name_system"></span></span>'+
	'		<span class="print_label">Наименование подразделения банка&nbsp;<span class="print_text" id="print_step_10_bank_subdivision_system"></span></span>'+
	'		<span class="print_label">Номер лицевого счета&nbsp;<span class="print_text" id="print_step_10_bank_account_system"></span></span>'+
	'		<span class="print_label">Номер К/С&nbsp;<span class="print_text" id="print_step_10_number_cor_account"></span></span>'+
	'		<span class="print_label">Номер Бик&nbsp;<span class="print_text" id="print_step_10_bik"></span></span>'+
	'		<br/>';
	}

	if (lst_steps.indexOf("9")>=0)
	{
	text_html+=
	'		<!-- 9 -->'+
	'		<span class="print_title" id="print_9_title">Сведения о выплатных реквизитах (ИП)</span>'+
	'		<br/>'+
	'		<span class="print_label">ФИО&nbsp;<span class="print_text" id="print_step_9_fio_recept"></span></span>'+
	'		<span class="print_label">Дата рождения&nbsp;<span class="print_text" id="print_step_9_birthday_recept"></span></span>'+
	'		<br/>'+
	'		<span class="print_label">Банковские реквизиты&nbsp;</span>'+
	'		<br/>'+
	'		<span class="print_label">Наименование банка&nbsp;<span class="print_text" id="print_step_9_bank_name_system"></span></span>'+
	'		<span class="print_label">Наименование подразделения банка&nbsp;<span class="print_text" id="print_step_9_bank_subdivision_system"></span></span>'+
	'		<span class="print_label">Номер лицевого счета&nbsp;<span class="print_text" id="print_step_9_bank_account_system"></span></span>'+
	'		<br/>';
	}

	if (lst_steps.indexOf("18")>=0)
	{
	text_html+=			
	'		<!-- 18 -->'+
	'		<span class="print_title" id="print_18_title">Перечень документов подтвержденных заявителем</span>'+
	'		<br/>'+
	'		<table border="1" id="print18_table">'+
	'			<tbody>'+
	'			<tr>'+
	'				<td align="center">'+
	'					<span class="print_tlabel">N п/п</span>'+
	'				</td>'+
	'				<td align="center">'+
	'					<span class="print_tlabel">Наименование документа</span>'+
	'				</td>'+
	'				<td align="center">'+
	'					<span class="print_tlabel">Реквизиты документа</span>'+
	'				</td>'+
	'				<td align="center">'+
	'					<span class="print_tlabel">Фамилия Имя Отчество</span>'+
	'				</td>'+
	'			</tr>'+
	'			<tr class="print_step_18_clone_block">'+
	'				<td align="center">'+
	'					<span class="print_step_18_num_pp print_text"></span>'+
	'				</td>'+
	'				<td align="center">'+
	'					<span class="print_step_18_name_doc print_text"></span>'+
	'				</td>'+
	'				<td align="center">'+
	'					<span class="print_step_18_rekvizit print_text"></span>'+
	'				</td>'+				
	'				<td align="center">'+
	'					<span class="print_step_18_fio print_text"></span>'+
	'				</td>'+
	'			</tr>'+
	'			</tbody>'+
	'		</table>'+	
	'		<br/>';
	}



	if (lst_steps.indexOf("16")>=0)
	{
	  

	  $("#step_16_name_info").val("");
	  var tmp = $("#step_16_ir").val();
	  
	  tmp = jsonParse(16,tmp);
	  
	  if (tmp != "" && tmp!=null && tmp !="null")
	  {
	    var arrObjectMembers = [ "infRequest[].reqParams[].reqParam.name", "infRequest[].name", "infRequest[].group.name"];		
	    var result = validObjectMembers(tmp, arrObjectMembers);	
		
		  var count0 = result.infRequest.length;	
		  
		  text_html+=
		  '		<!-- 16 -->'+
		  '		<span class="print_title" id="print_16_title">Дополнительные сведения</span>'+
		  '		<br/>'+
		  '		<table border="1">'+
		  '			<tbody>';
		  
		  for (var i=0; i<count0; i++)
		  {	
			
			text_html+=
			'			<tr>'+
			'				<td align="left">'+
			'					<span class="print_step_16_group print_title" id="print_group_16_info_'+i+'"></span>'+
			'				</td>'+			
			'			</tr>';			
			
			text_html+=
			'			<tr>'+
			'				<td align="left">'+
			'					<span class="print_step_16_group1 print_title" id="print_group1_16_info_'+i+'"></span>'+
			'				</td>'+			
			'			</tr>';
			
		    var count_1 = result.infRequest[i].reqParams.length;	
		    for (var j=0; j<count_1; j++)
		    {
		      if (result.infRequest[i].reqParams[j].reqParam!=null)
			  {
				var count_2=result.infRequest[i].reqParams[j].reqParam.length;
				for (var k=0; k<count_2; k++)
				{
				 
				text_html+=
				'			<tr class="print_step_16_clone_block">'+
				'				<td align="left">'+
				'					<span class="print_step_16_info print_text" id="print_step_16_info_'+(i*count_1+j)*count_2+k+'"></span>'+			
				'				</td>'+
				'			</tr>';
				}  
			  }  
		    }	
		  }
		text_html+=
		'			</tbody>'+
		'		</table>'+
		'		<br/>';	  
	  }  
	}


	if (lst_steps.indexOf("17")>=0)
	{
	    var tmp = $("#step_17_ir").val();
		
		tmp = jsonParse(17,tmp);
		
	if (tmp != "" && tmp!=null && tmp !="null")
	{	
	    var arrObjectMembers = 
		[ "information[].data[].params.param[].name"];
		
	    result = validObjectMembers(tmp, arrObjectMembers);
	   
		var count = result.information.length;
		for (var i=0; i < count; i++) 
		{
	text_html+=
	'    <!-- 17 -->'+
	(i==0 ? '		<span class="print_title" name="printNameStep17" id="print_17_title">Запрашиваемые сведения</span>':"")+
	'		<br/>'+
	'		<table border="1" class="print_step_17_clone_block_1" id="t_print17">'+
	'			<tbody>';

			var count_2 = result.information[i].data.length;
			for (var j=0; j < count_2; j++) 
			{				
	text_html+='			<tr>'+
	'				<td align="left" colspan="3">' +
	'				<span class="print_step_17_name_group_info print_title" id="print_step_17_name_group_info_'+((i*count_2)+j)+'"></span>' +
	'				</td>'+
	'			</tr>'+
	'			<tr class="print_step_17_clone_block_2" id="print_step_17_clone_block2">'+
	'				<td align="left" colspan="3">'+
	'					<span class="print_step_17_name_info print_title" id="print_step_17_name_info_'+((i*count_2)+j)+'"></span>'+
	'				</td>';

				count_3 = result.information[i].data[j].params.param.length;			
				for (var k=0; k<count_3; k++)
				{		
					
	text_html+=		
	'			<tr class="print_step_17_clone_block_2" id="print_step_17_clone_block2">'+
	'				<td align="left" colspan="2">'+
	'				<span class="print_step_17_name_rekvizit_info print_title" id="print_step_17_name_rekvizit_info_'+((i*count_2+j)*count_3+k)+'">'+result.information[i].data[j].params.param[k].name+'</span>'+    
	'				</td>'+
	'				<td align="left">'+
	'					<span class="print_step_17_rekvizit_info print_text" id="print_step_17_rekvizit_info_'+((i*count_2+j)*count_3+k)+'">'+result.information[i].data[j].params.param[k].value+'</span>'+
	'				</td>'+
	'			</tr>';	



				}
	text_html+=	
	'			</tr>';		
		}	
	text_html+='</tbody>'+
	'		</table>'+
	'		<br/>';
	}

	}

	}
	
	function to_print_step_18() {
	    var tmp = $("#confirmedDocs").val();
		
		tmp = jsonParse(18,'{"document":'+tmp+'}');
		
		if (tmp == "" || tmp==null || tmp =="null") 
		{
		  $("#tab_18").hide();
		  return;
		}  
		
	    var arrObjectMembers = 
		[ "document[].number",
		"document[].rekvDocument",
		"document[].name",
		"document[].series",
		"document[].dateIssue",
		"document[].fio"	
		];		
	    var result = validObjectMembers(tmp, arrObjectMembers);	
		
		
	    var count = result.document.length;
		var op = true;
		
		var countDoc=0;
		
	    for (i=0; i < count; i++) 
	    {	
		var isAccept = ( (result.document[i].number!=null && result.document[i].number!='') || (result.document[i].rekvDocument!=null && result.document[i].rekvDocument!='') ? true:false);
		
		if (isAccept)
		{	
	        countDoc ++;	
			$(".print_step_18_clone_block:last").after($(".print_step_18_clone_block:first").clone());
			$(".print_step_18_num_pp:last").text(i+1);
			$(".print_step_18_name_doc:last").text(tu(result.document[i].name));
			$(".print_step_18_rekvizit:last").text(tu(result.document[i].series) +" "+ tu(result.document[i].number) +" выдан(о) "+ tu(result.document[i].dateIssue));
			$(".print_step_18_fio:last").text(result.document[i].fio);		
		}
		}
	    $(".print_step_18_clone_block:first").remove(); 
		
		if (countDoc==0)
		{
		  $("#print_18_title").remove("");
		  $("#print18_table").remove();
		}
	}


	$("#fieldset_tab_30").html(text_html);
	getDataRequest();
	

	newPrintStatic();

	if (lst_steps.indexOf("7")>=0)  {newPrint7();}
	if (lst_steps.indexOf("12")>=0) {newPrint12();}
	if (lst_steps.indexOf("13")>=0) {newPrint13();}
	if (lst_steps.indexOf("14")>=0) {newPrint14();}
	if (lst_steps.indexOf("15")>=0) {newPrint15();}
	if (lst_steps.indexOf("16")>=0) {newPrint16();}
	if (lst_steps.indexOf("17")>=0) {newPrint17();}
	if (lst_steps.indexOf("18")>=0) {to_print_step_18();}


	$("#btnOutToPrint").val("Печать");
	  
	  	
	  if ($("#step_8_payment_type").val()=="Почта") 
	  {$("#step_8_info_4").hide();}
	  else {$("#step_8_info_3").hide();}
	  

	  if (lst_steps.indexOf('2')>=0)
	  {  
	    $("#tab_2").find("#category_legal_representative").text('Законный представитель: '+$("#step_1_category_legal_representative").val() );
	  }else
	  {
		  $("#tab_3").find("#category_legal_representative").text('Законный представитель: '+ $("#step_1_category_legal_representative").val());	 

	  }
		
		  var aFields=["step_4_doc_declarant_type", "step_4_doc_declarant_series","step_4_doc_declarant_number","step_4_doc_declarant_date","step_4_doc_declarant_org"];
		  hideEmptyFieldset(aFields,'fieldset');	
		  
		  aFields=["step_7_people_address_v", "step_7_people_region_v", "step_7_people_district_v", "step_7_people_city_v", "step_7_people_settement_v", "step_7_people_street_v", "step_7_people_house_v", "step_7_people_housing_v", 
		  "step_7_people_building_v", "step_7_people_flat_v", "step_7_people_room_v"];
		  hideEmptyFieldset(aFields,'fieldset');
		  
		  if ($("#step_1_subservices").val()=="") {$("#step_1_subservices").hide(); $("#step_1_subservices").parent().parent().hide(); }

		if ($("#reference_number").attr("disabled"))
		{	  
		  if ($("#idCurrentForm").text()=="3") 
		  {
		    switchStateDateTimePicker(false);	
		    execution_fromJson(false);		
			switchStateDateTimePicker(true);
		  }
		  else
		  {
		    switchStateDateTimePicker(false);	
		    execution_fromJson(true);
			switchStateDateTimePicker(true);
		    $("#comment_doc").attr("disabled",true);
			$("#add_mv").hide();
			$("#add").hide();
		  }	  
	    }
		else
		{	  
		  if ( $("#isPortal").text()=="true")
		  {
		    show19();
		  }	
		}	
		
		if ( $("#isPortal").text()=="true")
		{
			
			$("input").each(function () 
			{
			  if (!$(this).attr("disabled")) 
			  {
			    if ($(this).attr("type") == "button") 
				{
				  if ( $(this).attr("id") != "switch_tab_30" && $(this).attr("id")!="switch_tab_0" && $(this).attr("id")!="switch_tab_20"  ) 
			      {
				    $(this).attr("disabled",true);
				  }	
				}  
				else
				{
				  $(this).attr("disabled",true);
				}
			  }  
			});
			
			$("textarea").each(function () 
			{
			  if (!$(this).attr("disabled")) 
			  {		    
			    {$(this).attr("disabled",true);}
			  }  
			});
			
			
		}
		
		showstyle();
		step_18_fromJson();		
	
	
	$(".datepicker").each(function () 
	{
	  if ($(this).attr("disabled")) $(this).parent().find(".ui-datepicker-trigger").hide();
	  else $(this).parent().find(".ui-datepicker-trigger").show();	  
	});
	
	
	/*

	if ( $("#idCurrentForm").text()!="2" ) 
	{  
	  $("#date_reference_number").parent().find(".ui-datepicker-trigger").hide();
	}

	if ($("#idCurrentForm").text()!="5" && $("#idCurrentForm").text()!="6")
	{
	  $("#date_registration_solutions_p_1").parent().find(".ui-datepicker-trigger").hide();  
	}
	
	if ($("#date_reference_number").attr("disabled"))
	{
	  $("#date_reference_number").parent().find(".ui-datepicker-trigger").hide();  
	}
	

    
	
	if ($("#date_receipt_l_y").attr("disabled"))
	{
	  $("#date_receipt_l_y").parent().find(".ui-datepicker-trigger").hide();  
	}
	
	if ($("#date_request_mv_t").attr("disabled"))
	{
	  $("#date_request_mv_t").parent().find(".ui-datepicker-trigger").hide();  
	}
	*/

formatTextarea();

$("input").each(function () 
{
  $(this).removeAttr("error");
});


changeStepNames();

$("#reference_number").parent().parent().parent().parent().parent().parent().css({'width':'80%'});


function show19() {			
	var el1=$("#printNameStep1").clone();
	$("#registrationForm").append(el1);
	$("[name='printNameStep1']:last").text("Документы, которые необходимо принести лично");

	var el1=$("br:last").clone();
	$("#registrationForm").append(el1);	
	
		var json = $("#step_wait_nd").val();	
        json = jsonParse("регистрация",'{"document":'+json+'}');
		
		if(json==null || json=="")return;		
		
    var arrObjectMembers = 
	[ 	"document[].privateStorage",
		"document[].number",
		"document[].rekvDocument",
		"document[].name",
		"document[].fio",
		"document[].isAdded",
		"document[].rekvDocument"
	];		
    var result = validObjectMembers(json, arrObjectMembers);	
		
		
		var fullListName = ["code","fio","series","number","dateIssue","privateStorage","hasDocument","dateOfRecipt","hasRequest","reqNumber","sendDate","isAdded","rekvDocument"];
		constraintArray(result.document, ["name","fio"], ["arrObj[i].isAdded==true", "(arrObj[i].number!=null && arrObj[i].number!='')  || (arrObj[i].rekvDocument!=null && arrObj[i].rekvDocument!='')==true"],fullListName);		
		
		var count = result.document.length;				
		
		var indDoc=0;
		for (var i=0; i < count; i++) 
		{		    
		    if (result.document[i]!=null) 
			{			    
				var isNotMV = result.document[i].privateStorage;
				var isAccept = ( (result.document[i].number!=null && result.document[i].number!='')  || (result.document[i].rekvDocument!=null && result.document[i].rekvDocument!='') ? true:false);				
				
				if (isNotMV)
				{						
					if (!isAccept)
					{						  
						indDoc++;
						break;
					}
				}
			}
		}	
		
		var nameDoc1="";
		var fio1="";

		
		if (indDoc>0)
		{
		  if ($("#step_4_doc_declarant_type").val().trim() != "")
		  {
		    nameDoc1 = $("#step_4_doc_declarant_type").val();
			fio1 = $("#step_4_last_name_declarant").val()+" "+$("#step_4_first_name_declarant").val().substring(0,1)+". "+$("#step_4_middle_name_declarant").val().substring(0,1)+".";
		  }
		  
		  if  (nameDoc1 != "")
		  {
			var el2=$(".print_text:first").clone();
			el2.css({'display':'block'});				  
			$("#registrationForm").append(el2);
			el2.text(nameDoc1+" ( "+fio1+" )" );		  
		  }
		  
		}
		
		
		for (var i=0; i < count; i++) 
		{		    
		    if (result.document[i]!=null) 
			{			    
				var isNotMV = result.document[i].privateStorage;				
				var isAccept = ( (result.document[i].number!=null && result.document[i].number!='')  || (result.document[i].rekvDocument!=null && result.document[i].rekvDocument!='') ? true:false);				
				var fio = tu(result.document[i].fio);
					if (isNotMV)
					{												
						if (isAccept)
						{	
                           						
						  if (indDoc>0)
						  {
						    
							
							
						  }
						  
						}
						else
						{			
						  if ((result.document[i].name.trim()!="") && !((result.document[i].name.trim()==nameDoc1) && (result.document[i].fio==fio1)) )
						  {
						    var el2=$(".print_text:first").clone();	
						    el2.css({'display':'block'});						
						    $("#registrationForm").append(el2);
						    el2.text(result.document[i].name+(fio!="" ? " ( "+tu(result.document[i].fio)+" )":"") );						  
						  }	
						}						
					}					
			}		
		}
	}
            
});

function jsonParse(stepNumber, json)
{
  var result = null;
  if (json == null || json=="")
  {
    return result;
  }
  try 
  { 
    result = JSON.parse(json);
  }
  catch(err)
  {
    alert("Ошибка формата json. Данные шага: "+stepNumber+"\n"+err+"\n"+json);
  } 	  	  
  return result;
} 

function formatTextarea()
{  
  var stringLength;
  var stringHight;
  
  if ( $("#isPortal").val()=="true") 
  {stringLength = 105;
   stringHight=18;
  }
  else
  {stringLength = 113;
   stringHight=13;
  }
  $('textarea').each(function () 
  {    	
    var l = $(this).val().length;
    if (l > stringLength) 
    {
      l = Math.round(((l/stringLength)+3)*stringHight);	
	  $(this).css({'height':l+'px'});		
    }  
    $(this).removeAttr("error");  		
});

}

	function step_18_fromJson () {

		var json = $("#confirmedDocs").val();		
		
		json = jsonParse("18",json);

		if (json=="" || json==null  || json =="null")
		{ 		  
		  $(".clone_block_1:first").remove();
		  $("#tab_18").remove();
		  return;
		}
			var result = json;			
			window.result = result;
			
			if (typeof(result.document)=="undefined") {result.document=result;}			
			var count = result.document.length;
			
			if (count ==0)
			{			
		  $(".clone_block_1:first").remove();
		  $("#tab_18").remove();
		  return;
			}
			

			for (i=0; i < count; i++) {	
			        if (typeof(result.document[i].number) == 'undefined') result.document[i].number='';
					if (typeof(result.document[i].rekvDocument) == 'undefined') result.document[i].rekvDocument='';
					if (typeof(result.document[i].name) == 'undefined') result.document[i].name='';					
					if (typeof(result.document[i].series) == 'undefined') result.document[i].series='';					
					if (typeof(result.document[i].dateIssue) == 'undefined') result.document[i].dateIssue='';					
					if (typeof(result.document[i].organization) == 'undefined') result.document[i].organization='';
			
					var isAccept = ( (result.document[i].number!=null && result.document[i].number!="") || (result.document[i].rekvDocument!=null && result.document[i].rekvDocument!="") ? true:false);	
					if (isAccept)
					{
						var el = $(".clone_block_1:first").clone();
						$("#tab_18").append(el);					
						
						el.find("input").each(function () 
						{
							$(this).attr("id", $(this).attr("name") + "_" + i);
						});
						
						el.find("textarea").each(function () {										
							$(this).attr("id", $(this).attr("name") + "_" + i);
						});
						
						el.find("#step_18_name_doc_"+ i).val(result.document[i].name);					
						el.find("#step_18_doc_series_doc_"+ i).val(result.document[i].series);
						el.find("#step_18_doc_number_doc_"+ i).val(result.document[i].number);
						el.find("#step_18_doc_issue_date_doc_"+ i).val(result.document[i].dateIssue);
						el.find("#step_18_doc_org_doc_"+ i).val(result.document[i].organization);					
					}			
			}			
		
		$(".clone_block_1:first").remove();
		if ($(".clone_block_1:first").length == 0) {$("#tab_18").remove();}
	}

function switchStateDateTimePicker(state){
     var datepicker = $('.datepicker');
     if(datepicker==null || datepicker.datepicker==null){
	       return; 
		}
     if(state==true){
		$.datepicker.regional['ru'] = {
		          closeText: 'Закрыть',
 		         prevText: '&#x3c;Пред',
 		         nextText: 'След&#x3e;',
 		         currentText: 'Сегодня',
		          monthNames: ['Январь', 'Февраль', 'Март', 'Апрель', 'Май', 'Июнь',
   	           'Июль', 'Август', 'Сентябрь', 'Октябрь', 'Ноябрь', 'Декабрь'],
		          monthNamesShort: ['Янв', 'Фев', 'Мар', 'Апр', 'Май', 'Июн',
   	           'Июл', 'Авг', 'Сен', 'Окт', 'Ноя', 'Дек'],
   	       dayNames: ['воскресенье', 'понедельник', 'вторник', 'среда', 'четверг', 'пятница', 'суббота'],
   	       dayNamesShort: ['вск', 'пнд', 'втр', 'срд', 'чтв', 'птн', 'сбт'],
   	       dayNamesMin: ['Вс', 'Пн', 'Вт', 'Ср', 'Чт', 'Пт', 'Сб'],
   	       weekHeader: 'Не',
   	       dateFormat: 'dd-mm-yy',
   	       firstDay: 1,
   	       isRTL: false,
   	       showMonthAfterYear: false,
   	       yearSuffix: ''
   	   };
   	   $.datepicker.setDefaults($.datepicker.regional['ru']);
   	   datepicker.datepicker({ dateFormat: 'dd.mm.yy', showOn: "button", changeYear: true, changeMonth : true, yearRange: 'c-100:c+10' });
      	   $('.datepicker').unbind('change');
          $('.datepicker').change(function() {
           var selVal = new Date(toDate($('#step_2_birthday_legal_representative').val()));
           now = new Date();
           if (now < selVal){
             errorMsg('Дата не может быть больше текущей!');
             $(this).val('');
             return false;
           }
       });
     }else{
       datepicker.datepicker("destroy");
     }
   }

function tu(val)
{	

  if (val == 'undefined' || typeof(val)=='undefined')
  {	    
    return "";
  }
  else
  {
    return val.trim();
  }	  
}



function hideEmptyFieldset(aFields,condParent)
{
 var needHide = true;
 for (i=0; i<aFields.length; i++)
 {
    if (!($("#"+aFields[i]).val() =="" ||  typeof($("#"+aFields[i])) =="undefined" )) { needHide = false; }		 
 }	  
  if ( needHide ) 
  {		
	$("#"+aFields[0]).closest(condParent).remove();
  }	
}

 function toDate(source){
   var from = null;
   if(source.indexOf(" ")>0){
      source = source.replace(' ',"."); 
	  source = source.replace(/:/g,"."); 
   }
   from = source.split(".");
   return new Date(from[2],from[1]-1,from[0]);
 }
 

function switch_tab(step_ind,source)
{     
 switch_tab1(step_ind,source);
} 


function switch_tab1(step_ind,source)
{	  
  if (step_ind == 30 )
  {
	  if (source.id == "switch_tab_0" && $("#idCurrentForm").text()!="0" && $("#idCurrentForm").text()!="2")
	  {			  
	  source.value = "Экранная форма заявления";
	  source.id="switch_tab_30";
	  $("#fieldset_tab_30").show();		  
	  $("#fieldset_tab_0").hide();
	  }
	  else
	  {
		  if (source.id == "switch_tab_30")
		  { 			  
			if ($("#idCurrentForm").text()=="0" || $("#idCurrentForm").text()=="2")  
			{
				source.value = "Печатная форма заявления";					
			}
			else
			{		      
				source.value = "Свернуть форму";			  					
			}			  
		    source.id="switch_tab_1";
			$("#fieldset_tab_30").hide();				  
			$("#fieldset_tab_0").show();						  
		  }
		  else
		  {			      
		      if ($("#idCurrentForm").text()=="0" || $("#idCurrentForm").text()=="2")
			  {
				source.id="switch_tab_30";
				$("#fieldset_tab_30").show();		  
				$("#fieldset_tab_0").hide();
				source.value = "Экранная форма заявления";							  
			  }
			  else
			  {
				  source.value = "Печатная форма заявления";
				  if (source.id == "switch_tab_1")
				  {	
				  source.id="switch_tab_0";
				  $("#fieldset_tab_30").hide();		  
				  $("#fieldset_tab_0").hide();
				  }
			  }
		  }		  
	  }		  
  }
  if (source.value == "-")
  { 		    
	source.value = "+";			
	$("#fieldset_tab_"+step_ind).hide();		
	return;
  }
  if (source.value == "+")
  {	
	source.value = "-"; 	  
	$("#fieldset_tab_"+step_ind).show();		
  }	  
}	

function getFreeIndex()
{
	var i = -1;
	if ($(".clone_block_1w:visible").length > 0)
	{		
	  var tmpStr = $(".clone_block_1w:visible:last").find('input').attr('id');		
	  var pos = tmpStr.lastIndexOf('_');
	  i = tmpStr.substr(pos + 1);
	}  
	
	if ($(".clone_block_2w:visible").length > 0)
	{
	  tmpStr = $(".clone_block_2w:visible:last").find('input').attr('id');
	  pos = tmpStr.lastIndexOf('_');
	  if (parseInt(i) < parseInt(tmpStr.substr(pos + 1))) 
      {i=tmpStr.substr(pos + 1);}		
	}
	i++;		
	return i;	
}


function execution_fromJson (viewOnly) {
    
	var json = $("#step_wait_nd").val();		
	json = jsonParse("Ожидание",json);
	
	if(json==null || json=="") return;
	var result = json;
	
	window.outputData = result;		
	if ( typeof(result.document)=="undefined") {result.document=result;}
	
	
	for (var i=0; i<result.document.length; i++)
	{
	  if (typeof(result.document[i].isAdded) == 'undefined') result.document[i].isAdded = false;		  
    }
	
	
	 
	 
	 
	 
	var fullListName = ["code","fio","series","number","dateIssue","privateStorage","hasDocument","dateOfRecipt","hasRequest","reqNumber","sendDate","isAdded","rekvDocument"];
	constraintArray(result, ["name","fio"], ["arrObj[i].isAdded==true", "(arrObj[i].number!=null && arrObj[i].number!='')  || (arrObj[i].rekvDocument!=null && arrObj[i].rekvDocument!='')==true"],fullListName);		
	
	var count = result.document.length;		
	
	for (i=0; i < count; i++) {			
	    
	    if (result.document[i]!=null) 
		{			
			var isNotMV = result.document[i].privateStorage;
			var isAccept = ( (result.document[i].number!=null && result.document[i].number!='')  || (result.document[i].rekvDocument!=null && result.document[i].rekvDocument!='') ? true:false);
			
		
			if (isNotMV) {
				
				if (result.document[i].name!="")
				{
			
				var el = $(".clone_block_1w:first").clone();
				$("#docsPrivate").append(el);
				el.find("input").each(function () {
					
					if ($(this).attr("type") == "button") {return;} 
					$(this).attr("id", $(this).attr("name") + "_" + i);
					if ($(this).attr("name")=="choice_trustee_rek") { $(this).attr("disabled", true);}
					$(this).attr("disabled", true);
				});			
				
				el.find("textarea").each(function () {            
					$(this).attr("id", $(this).attr("name") + "_" + i);
					$(this).attr("disabled", true);
					
					if ($(this).attr("name")!="rekv_document_2_y") 
					{  $(this).attr("disabled", true);  
					}
					else 
					{
					  if ( result.document[i].rekvDocument == null)
					  {						  
					   $("#rekv_document_2_y_"+ i).val(
					   (typeof(result.document[i].series)=='undefined' || result.document[i].series=='' ? "":"Серия: "+result.document[i].series)   +   
					   (typeof(result.document[i].number)=='undefined' || result.document[i].number=='' ? "":" Номер: "+result.document[i].number) +
					   (typeof(result.document[i].dateIssue)=='undefined' || result.document[i].dateIssue=='' ? "":" Дата выдачи: "+result.document[i].dateIssue)
					   );
					  }
					  else
					  {$("#rekv_document_2_y_"+ i).val(result.document[i].rekvDocument);}
					}					
				});

				$("#name_document_2_y_"+ i).val(result.document[i].name);
				
				if (result.document[i].hasDocument || viewOnly)  {					  					 
				  $("#date_receipt_l_y_"+ i).val(result.document[i].dateOfRecipt).attr("disabled",true);;
				}
				else
				{
				  $("#date_receipt_l_y_"+ i).val(result.document[i].dateOfRecipt);
				}
				
				if (result.document[i].hasDocument)  {					  
				  $("#mark_receipt_l_y_"+ i).attr("checked",true);					  
				}
				
				if (viewOnly) 
				{ 
				  $("#mark_receipt_l_y_"+ i).attr("disabled",true);					  
			    }
				else
				{ 
				  $("#mark_receipt_l_y_"+ i).attr("disabled",false);
				   
			    }					
				
				if(isAccept){				
				  
				  $("#choice_trustee_rek_"+ i).attr("checked",true);
				}
				$("#fio_document_2_y_"+ i).val(result.document[i].fio);
				
				if (viewOnly)
				{
				    $("#name_document_2_y_"+ i).attr("disabled", true);
					$("#rekv_document_2_y_"+ i).attr("disabled", true);						
					$("#rekv_document_2_y_"+ i).closest("tr").find(".field-requiredMarker").hide();						
					$("#fio_document_2_y_"+ i).attr("disabled", true);						
					 
				     
					 
				}					 
				
				if (!result.document[i].isAdded) 
				{
				    $("#name_document_2_y_"+ i).attr("disabled", true);
					$("#rekv_document_2_y_"+ i).attr("disabled", true);
					$("#rekv_document_2_y_"+ i).closest("tr").find(".field-requiredMarker").hide();						
					$("#fio_document_2_y_"+ i).attr("disabled", true);
					 
				     
					 
				}
				else 
				{					 
				 $("#isAdded_"+ i).attr("checked",true);
				 
				 if (!viewOnly)
				 {
				     el.find(".btn_delete_step_18_reg_info").show();
					 $("#name_document_2_y_"+ i).attr("disabled", false);
					 $("#rekv_document_2_y_"+ i).attr("disabled", false);
					 $("#rekv_document_2_y_"+ i).closest("tr").find(".field-requiredMarker").show();
					 $("#fio_document_2_y_"+ i).attr("disabled", false);
					 $("#mark_receipt_l_y_"+ i).attr("disabled", false);						 
					 if ( $("#mark_receipt_l_y_"+ i).attr("checked") )
					 { $("#date_receipt_l_y_"+ i).attr("disabled",false); }
				 }
				}
				

				if (result.document[i].hasDocument === false) {
				    if (!viewOnly)
					{
					  if ($("#mark_receipt_l_y_"+ i).attr("checked"))
					   
					  {
					    
					   $("#date_receipt_l_y_"+i).attr("disabled", false);
					  }
					    
					   $("#mark_receipt_l_y_"+i).attr("disabled", false);
					}						
				}
				
				 
				 
				  testReguest($("#mark_receipt_l_y_"+i),viewOnly);
				 

				
				 
				 
				/*
				if ( ($("#rekv_document_2_y_"+i).val() == "") && !viewOnly ) 
				{  
				  $("#rekv_document_2_y_"+i).attr("disabled",false);
				}					
				
				if ( $("#name_document_2_y_"+i).val() == "" && !viewOnly) 
				{
				  $("#name_document_2_y_"+i).attr("disabled",false);
				}
				
				if ( $("#fio_document_2_y_"+i).val() == "" && !viewOnly) 
				{
				  $("#fio_document_2_y_"+i).attr("disabled",false);
				}
				*/
				}
			} 
			else 
			{		
				if (result.document[i].name != "")
				{
			        var el = $(".clone_block_2w:first").clone();
					$("#docsMV").append(el);
					
					el.find("input").each(function () {            
						if ($(this).attr("type") == "button") {return;} 
						$(this).attr("id", $(this).attr("name") + "_" + i);
			            if(!result.document[i].isAdded) $(this).attr("disabled",true); 
					});									
					
					el.find("textarea").each(function () {            
						if ($(this).attr("type") == "button") {return;} 
						$(this).attr("id", $(this).attr("name") + "_" + i);
			            if(!result.document[i].isAdded) $(this).attr("disabled",true); 
					});
					

					$("#name_data_mv_t_"+ i).val(result.document[i].name);
					
					if(isAccept){
					  $("#choice_trustee_rek_"+ i).attr("checked",true);						  
					}			

					if (result.document[i].hasDocument) {
					  $("#mark_request_mv_t_"+ i).attr("checked",true);
					  $("#mark_request_mv_t_"+ i).attr("disabled",true);
					}
					
					$("#fio_document_3_y_"+ i).val(result.document[i].fio);
					
					if (result.document[i].hasRequest) 
					{
					  $("#mark_request_mv_t_"+ i).attr("checked",true).attr("disabled",true);
					  $("#date_receipt_mv_t_"+ i).val(result.document[i].dateOfRecipt);				  
					}
					
					
					if (!result.document[i].isAdded) {							  
					  $("#date_receipt_mv_t_"+ i).attr("disabled",true);				  
					  $("#number_reguest_mv_"+ i).attr("disabled",true);				  
					  $("#date_request_mv_t_"+ i).attr("disabled",true);				  
					}						
					else
					{						
					  $("#isAdded_"+ i).attr("checked",true);
					  if (!viewOnly)	
					  {$("#mark_request_mv_t_"+ i).attr("disabled",false);}
					  
					  
					  if (!$("#mark_request_mv_t_"+ i).attr("checked"))
					  {						  
					    $("#date_receipt_mv_t_"+ i).attr("disabled",true);
					    $("#number_reguest_mv_"+ i).attr("disabled",true);
					    $("#date_request_mv_t_"+ i).attr("disabled",true);							
					  }						  
					}
					
					if (typeof(result.document[i].reqNumber)!='undefined')
					{$("#number_reguest_mv_"+ i).val(result.document[i].reqNumber);}
					else
					{$("#number_reguest_mv_"+ i).val("");}
					
					
					$("#date_request_mv_t_"+ i).val(result.document[i].sendDate);
					if (result.document[i].isAdded && !viewOnly) {	
						el.find(".btn_delete_step_18_mv_info").show();
					}
					
					$(".clone_block_2w:last input[name='choice_trustee_rek']").attr("disabled", true);
					
					
					el.find("[name='mark_request_mv_t']").attr("disabled",false);						
					el.find("[name='choice_trustee_rek']").attr("disabled",true);
				
					 
					 
					testReguestMv(el.find("[name='mark_request_mv_t']"),viewOnly);
					 
					
					/*
					if ( $("#name_data_mv_t_"+i).val() == "" && !viewOnly) 
					{
					  $("#name_data_mv_t_"+i).attr("disabled",false);
					}
					*/
					/*
					if ( $("#fio_document_3_y_"+i).val() == "" && !viewOnly) 
					{
					  $("#fio_document_3_y_"+i).attr("disabled",false);
					}
					*/
					
					if (viewOnly)
					{
					  $("#name_data_mv_t_"+i).attr("disabled",true);
					  $("#fio_document_3_y_"+i).attr("disabled",true);
					  $("#number_reguest_mv_"+i).attr("disabled",true);
					  $("#date_request_mv_t_"+i).attr("disabled",true);
					  $("#date_receipt_mv_t_"+i).attr("disabled",true);
					  $("#mark_request_mv_t_"+i).attr("disabled",true);
					}
					
					/*
					if (!viewOnly)
					{
					  if (  !$("#choice_trustee_rek_"+i).attr("checked") )
					  {							
						$("#name_data_mv_t_"+i).attr("disabled",flase));
					  }
					}
					*/
				}	
					
			}
			
			  
		}
	
	}
    $(".clone_block_1w:first").hide();
    $(".clone_block_2w:first").hide();

    formatTextarea();
}  

function hideRequest(block){
    var request = $("#" + block + " #mark_request_mv_t");	
	if(!request.is('checked')){
	  $("#" + block + " #request").hide();
	}
}


function needAddClone(aFields)
{
var needAdd=false;
for (var i=0; i<aFields.length; i++)
{    
if (aFields[i][1]!="")
{
  needAdd=true;
  break
}
}
return needAdd;
}

function addClone(etalonKeyType,etalonKey,parentId,ind,aFields)
{   
if (etalonKeyType == "id")
{
etalonJqueryName = "#"+etalonKey;
}
else
{
etalonJqueryName = "."+etalonKey;
}  

var el_id=etalonKey+"_"+ind;

var el = $(etalonJqueryName+":first").clone().attr("id",el_id).show();
$("#"+parentId).append(el);

	  
for (var i_f=0; i_f<aFields.length; i_f++)
{  
if (aFields[i_f][3]=='name')
{	  	
  
	if (aFields[i_f][2] == 'span')
	{	   
		el.find('[name="'+aFields[i_f][0]+'"]').text(aFields[i_f][1]);				
	}
	else
	{	   
	   el.find('[name="'+aFields[i_f][0]+'"]').val(aFields[i_f][1]);
	}	  
	el.find('[name="'+aFields[i_f][0]+'"]').attr('id',aFields[i_f][0]+"_"+ind);
	 
}
else
{
	if (aFields[i_f][2] == 'span')
	{	   
		 
		el.find('.'+aFields[i_f][0]).text(aFields[i_f][1]);	
	}
	else
	{	   
	    
	   el.find('.'+aFields[i_f][0]).val(aFields[i_f][1]);
	}	  
	$("#"+el_id).find('.'+aFields[i_f][0]).attr('id',aFields[i_f][0]+"_"+ind);		
	 
}

/*
if (aFields[i_f][2] == 'span')
{	   
	$("#"+aFields[i_f][0]+"_"+ind+"_"+i_f).text(aFields[i_f][1]);	
}
else
{	   
   $("#"+aFields[i_f][0]+"_"+ind+"_"+i_f).val(aFields[i_f][1]);
   
   
   
   
   
   
   
   
   
   
}
*/

}	  

return el;
}  

function getDataRequest()
{

var isPortal = false;//$(".task-table tr:eq(1) td:eq(1)").text() != "Номер заявления:";  

var aName = [["number",1,2], ["favourName",5,1], ["procedureName",6,1], ["organizationName",7,1], ["creationDate",4,1,0,10]];
for (var i=0; i<aName.length; i++)
{    
if (!isPortal)
{   
	
	if (typeof(aName[i][1]) != 'undefined') 
	{
	  if (typeof(aName[i][3]) != 'undefined') 
	  {
	    var el2 = $("span:first").clone();	
        el2.attr("id",aName[i][0]);				  
	    el2.text($(".task-table tr:eq("+aName[i][1]+") td:eq("+aName[i][2]+")").text().substring(aName[i][3],aName[i][4]));
        $("#requestParametersUni").append(el2);
	    el2.hide();	    			
	  }
	  else
	  {
	    var el2 = $("span:first").clone();	
        el2.attr("id",aName[i][0]);				  		  
	    el2.text($(".task-table tr:eq("+aName[i][1]+") td:eq("+aName[i][2]+")").text());
        $("#requestParametersUni").append(el2);
	    el2.hide();
	  }	
	}  		
}
} 

if (isPortal)
{
    $("#isPortal").text("true"); 
    $("#isPortal").val("true"); 
}


}

function newPrintStatic()
{ 

	var cat_recipient = $("#step_1_category").val();

	$("#print_num_query").text($("#number").text());
	$("#print_step_1_social_institution").text($("#step_1_social_institution").val());

	if ($("#reference_number").val() != "") 
	{
	  $("#print_reference_number").text($("#reference_number").val());
	  $("#print_date_reference_number").text($("#date_reference_number").val());
	}  
  
	$("#print_name_serv").text($("#favourName").text());

	$("#print_sub_serv").text($("#procedureName").text());

	$("#print_cat_recipient").text(cat_recipient);

	if (lst_steps.indexOf("2")>=0 || lst_steps.indexOf("3")>=0) 
	{
	  $("#print_step_1_category_legal_representative").text($("#step_1_category_legal_representative").val());

		if (lst_steps.indexOf("2")>=0)
		{  
			$("#print_step_2_last_name_legal_representative").text($("#step_2_last_name_legal_representative").val()+' '+$("#step_2_first_name_legal_representative").val()+' '+$("#step_2_middle_name_legal_representative").val());	  
			$("#print_step_2_birthday_legal_representative").text($("#step_2_birthday_legal_representative").val());	  
			var doc = formDoc(["step_2_doc_legal_representative_type","step_2_doc_legal_representative_series","step_2_doc_legal_representative_number","step_2_doc_legal_representative_date","step_2_doc_legal_representative_org"]);	  
			var doc1 = formDoc(["step_2_name_doc","step_2_series_doc","step_2_number_doc","step_2_date_doc","step_2_org_doc"]);
	  
			$("#print_step_2_legal_representative").text(doc);	  
			$("#print_step_2_name_doc").text(doc1);	  
			var adr = formAddress(	["step_2_address_legal_representative_postal","step_2_address_legal_representative_region","step_2_address_legal_representative_f_district","step_2_address_legal_representative_city",
																"step_2_address_legal_representative_settlement","step_2_address_legal_representative_street","step_2_address_legal_representative_house","step_2_address_legal_representative_body",
  																"step_2_address_legal_representative_build","step_2_address_legal_representative_flat","step_2_address_legal_representative_room"]);  
	  
			$("#print_step_2_reg_adr").text(adr);

			conditionShow([[ $("#step_2_birthday_legal_representative").val(), 											"#print_step_2_birthday_legal_representative", 	"#step_2_birthday_legal_representative", 			"tr" 		]]);	  
			conditionShow([[ doc, 																						"#print_step_2_legal_representative",			"#step_2_doc_legal_representative_type", 			"fieldset" 	]]);	  
			conditionShow([[ doc1, 																						"#print_step_2_name_doc", 						"#step_2_name_doc", 								"fieldset" 	]]);	  
			conditionShow([[$("#step_2_series_doc").val() + $("#step_2_number_doc").val() +$("#step_2_date_doc").val(), 	"", 											"#step_2_series_doc",					 			"tr"		]]);
			conditionShow([[$("#step_2_org_doc").val(), 																	"", 											"#step_2_org_doc",					 	 			"tr"		]]);  
		}    

		if (lst_steps.indexOf("3")>=0)
		{
		  $("#print_step_3_full_name_org").text($("#step_3_full_name_org").val());
		  $("#print_step_3_reduced_name_org").text($("#step_3_reduced_name_org").val());
		  $("#print_step_3_legal_address_org").text($("#step_3_legal_address_org").val());
		  $("#print_step_3_identity_org_reg").text($("#step_3_identity_org_reg").val());
		  $("#print_step_3_juridical_inn").text($("#step_3_juridical_inn").val());
		  $("#print_step_3_juridical_kpp").text($("#step_3_juridical_kpp").val());
		  $("#print_step_3_juridical_ogrn").text($("#step_3_juridical_ogrn").val());
		  
		  var fio = $("#step_3_lastname_org").val()+' '+$("#step_3_name_org").val()+' '+$("#step_3_middlename_org").val();
		  $("#print_step_3_fio_org").text(fio);
		  $("#print_step_3_birth_date_org").text($("#step_3_birth_date_org").val());
		  $("#print_step_3_pozition_manager").text($("#step_3_pozition_manager").val());
		  var doc1 = formDoc(["step_3_step_3_document_type_org","step_3_document_series_org","step_3_document_number_org","step_3_document_issue_date_org","step_3_document_org"]);
		  $("#print_step_3_document_type_org").text(doc1);
		  var doc2 = formDoc(["step_3_name_doc","step_3_series_doc","step_3_number_doc","step_3_date_doc","step_3_org_doc"]);
		  $("#print_step_3_name_doc").text(doc2);	  
		  
		  
		  conditionShow([[$("#step_3_full_name_org").val(), 		"#print_step_3_full_name_org", 		"#step_3_full_name_org", 			"tr"		]]);
		  conditionShow([[$("#step_3_reduced_name_org").val(), 		"#print_step_3_reduced_name_org", 	"#step_3_reduced_name_org", 		"tr" 		]]);
		  conditionShow([[$("#step_3_legal_address_org").val(), 	"#print_step_3_legal_address_org", 	"#step_3_legal_address_org", 		"tr" 		]]);
		  conditionShow([[$("#step_3_identity_org_reg").val(), 		"#print_step_3_identity_org_reg", 	"#step_3_identity_org_reg", 		"tr" 		]]);	
		  
		  conditionShow([[$("#step_3_juridical_inn").val(), 		"#print_step_3_juridical_inn", 		"#step_3_juridical_inn", 			"td" 		]]);
		  conditionShow([[$("#step_3_juridical_kpp").val(), 		"#print_step_3_juridical_kpp", 		"#step_3_juridical_kpp", 			"td" 		]]);
		  conditionShow([[$("#step_3_juridical_ogrn").val(), 		"#print_step_3_juridical_ogrn",		"#step_3_juridical_ogrn", 			"td" 		]]);
		  
		  conditionShow([[fio, 										"#print_step_3_fio_org",			"#step_3_lastname_org", 			"tr" 		]]);
		  conditionShow([[$("#step_3_birth_date_org").val(), 		"#print_step_3_birth_date_org",		"#step_3_birth_date_org", 			"tr" 		]]);
		  conditionShow([[$("#step_3_pozition_manager").val(), 		"#print_step_3_pozition_manager",	"#step_3_pozition_manager", 		"tr"		]]);  
		  conditionShow([[doc1, 									"#print_step_3_document_type_org",	"#step_3_step_3_document_type_org", "fieldset"	]]);  
		  conditionShow([[doc2, 									"#print_step_3_name_doc",			"#step_3_name_doc", 				"fieldset"	]]);	  
		}
	}  


	if (lst_steps.indexOf("4")>=0)
	{
	  $("#print_step_4_fio_declarant").text($("#step_4_last_name_declarant").val()+' '+$("#step_4_first_name_declarant").val()+' '+$("#step_4_middle_name_declarant").val());
	  $("#print_step_4_birthday_declarant").text($("#step_4_birthday_declarant").val());
	  var doc = formDoc(["step_4_doc_declarant_type","step_4_doc_declarant_series","step_4_doc_declarant_number","step_4_doc_declarant_date","step_4_doc_declarant_org"]);
	  $("#print_step_4_doc_declarant_type").text(doc);  
	  var adr = formAddress(	["step_4_address_declarant_postal","step_4_address_declarant_region","step_4_address_declarant_district","step_4_address_declarant_city",
																	"step_4_address_declarant_settlement","step_4_address_declarant_street","step_4_address_declarant_house","step_4_address_declarant_body",
																	"step_4_address_declarant_build","step_4_address_declarant_flat","step_4_address_declarant_room"]);  
	  $("#print_step_4_address_declarant_postal").text(adr);  
	  
	  conditionShow([[$("#step_4_birthday_declarant").val(), 	"#print_step_4_birthday_declarant", 		"#step_4_birthday_declarant", 			"tr"		]]);
	  conditionShow([[doc, 										"#print_step_4_doc_declarant_type", 		"#step_4_doc_declarant_type", 			"fieldset"	]]);  
	  conditionShow([[adr, 										"#print_step_4_address_declarant_postal", 	"#step_4_address_declarant_postal", 	"fieldset"	]]);
	}

	if (lst_steps.indexOf("5")>=0)
	{
	  $("#print_step_5_fio_declarant").text($("#step_5_last_name_declarant").val()+' '+$("#step_5_first_name_declarant").val()+' '+$("#step_5_patronymic_declarant").val());  
	  $("#print_step_5_birthday_declarant").text($("#step_5_birthday_declarant").val());    
	  
	  var doc = formDoc(["step_5_doc_declarant_type","step_5_doc_declarant_series","step_5_doc_declarant_number","step_5_doc_declarant_date"]);
	  $("#print_step_5_doc_declarant").text(doc);  
	  var adr = formAddress(["step_5_address_declarant_postal","step_5_address_declarant_region","step_5_address_declarant_district","step_5_address_declarant_city","step_5_address_declarant_settlement",
											"step_5_address_declarant_street","step_5_address_declarant_house","step_5_address_declarant_body","step_5_address_declarant_build","step_5_address_declarant_flat","step_5_address_declarant_room"]);
	  $("#print_step_5_address_declarant_postal").text(adr);  
	  $("#print_step_5_INN").text($("#step_5_INN").val());  
	  $("#print_step_5_OGRNIP").text($("#step_5_OGRNIP").val());    
	  
	  conditionShow([[$("#step_5_birthday_declarant").val(), 	"#print_step_5_birthday_declarant", 		"#step_5_birthday_declarant", 			"td"		]]);  
	  conditionShow([[doc, 										"#print_step_5_doc_declarant", 				"#step_5_doc_declarant_type", 			"fieldset"	]]);
	  conditionShow([[adr, 										"#print_step_5_address_declarant_postal", 	"#step_5_address_declarant_postal", 	"fieldset"	]]);
	  conditionShow([[$("#step_5_INN").val(), 					"#print_step_5_INN", 						"#step_5_INN", 							"td"		]]);
	  conditionShow([[$("#step_5_OGRNIP").val(), 				"#print_step_5_OGRNIP",						"#step_5_OGRNIP", 						"td"		]]);  
	}

	if (lst_steps.indexOf("6")>=0)
	{
		$("#print_step_6_full_name_org").text($("#step_6_full_name_org").val());  
		$("#print_step_6_reduced_name_org").text($("#step_6_reduced_name_org").val());  	
		$("#print_step_6_legal_address_org").text($("#step_6_legal_address_org").val());	
		$("#print_step_6_identity_org_reg").text($("#step_6_identity_org_reg").val());  	
		$("#print_step_6_juridical_inn").text($("#step_6_juridical_inn").val());  	
		$("#print_step_6_juridical_kpp").text($("#step_6_juridical_kpp").val());  	
		$("#print_step_6_juridical_ogrn").text($("#step_6_juridical_ogrn").val());  	
		var fio = $("#step_6_lastname_org").val()+' '+$("#step_6_name_org").val()+' '+$("#step_6_middlename_org").val();	
		$("#print_step_6_fio_org").text(fio);  	
		$("#print_step_6_birth_date_org").text($("#step_6_birth_date_org").val());  	
		$("#print_step_6_pozition_manager").text($("#step_6_pozition_manager").val());  
		var doc = formDoc(["step_6_step_6_document_type_org","step_6_document_series_org","step_6_document_number_org","step_6_document_issue_date_org","step_6_document_org"]);
		$("#print_step_6_document_type_org").text(doc);	
		
		conditionShow([[$("#step_6_full_name_org").val(), 		"#print_step_6_full_name_org", 		"#step_6_full_name_org", 			"tr"		]]);
		conditionShow([[$("#step_6_reduced_name_org").val(), 	"#print_step_6_reduced_name_org", 	"#step_6_reduced_name_org", 		"tr"		]]);
		conditionShow([[$("#step_6_legal_address_org").val(), 	"#print_step_6_legal_address_org", 	"#step_6_legal_address_org", 		"tr"		]]);
		conditionShow([[$("#step_6_identity_org_reg").val(), 	"#print_step_6_identity_org_reg", 	"#step_6_identity_org_reg", 		"tr"		]]);	
		conditionShow([[$("#step_6_juridical_inn").val(), 		"#print_step_6_juridical_inn", 		"#step_6_juridical_inn", 			"td"		]]);
		conditionShow([[$("#step_6_juridical_kpp").val(), 		"#print_step_6_juridical_kpp", 		"#step_6_juridical_kpp", 			"td"		]]);
		conditionShow([[$("#step_6_juridical_ogrn").val(), 		"#print_step_6_juridical_ogrn", 	"#step_6_juridical_ogrn", 			"td"		]]);	
		conditionShow([[fio, 									"#print_step_6_fio_org", 			"#step_6_lastname_org", 			"tr"		]]);	
		conditionShow([[$("#step_6_birth_date_org").val(), 		"#print_step_6_birth_date_org", 	"#step_6_birth_date_org", 			"td"		]]);	
		conditionShow([[$("#step_6_pozition_manager").val(), 	"#print_step_6_pozition_manager", 	"#step_6_pozition_manager", 		"td"		]]);
		conditionShow([[doc, 									"#print_step_6_document_type_org", 	"#step_6_pozition_manager", 		"fieldset"	]]);	
	}



	if (lst_steps.indexOf("8")>=0)
	{  
	  var fio = $("#step_8_last_name_recept").val()+' '+$("#step_8_first_name_recept").val()+' '+$("#step_8_middle_name_recept").val();
	  $("#print_step_8_fio_recept").text(fio);  
	  $("#print_step_8_birthday_recept").text($("#step_8_birthday_recept").val());   
	  
		conditionShow([[fio, 									"#print_step_8_fio_recept", 		"#step_8_last_name_recept",	"fieldset"	]]);
		conditionShow([[$("#step_8_birthday_recept").val(), 	"#print_step_8_birthday_recept", 	"#step_8_birthday_recept",	"tr"		]]);  	  	
	  
		if ($("#step_8_payment_type").val()=="Почта") 
		{	  
		  $("#print_step_8_postal_number_system").text($("#step_8_postal_number_system").val());
		  var adr = formAddress(["step_8_postal_address_v","step_8_region_v","step_8_district_v","step_8_city_v","step_8_settment_v","step_8_street_v","step_8_house_v","step_8_housing_v","step_8_building_v","step_8_flat_v","step_8_room_v"]);
		  if (adr.trim() == "") 
		  { 	    
			$("#step_8_info_3").hide(); 
			$("#svevPostRekv").text("");
		  }
			$("#print_step_8_postal_address_v").text(adr);	  	  
			conditionShow([[$("#step_8_postal_number_system").val(), 		"#print_step_8_postal_number_system", 		"#step_8_postal_number_system", 			"tr"			]]);  
			conditionShow([[adr, 											"#print_step_8_postal_address_v", 			"#step_8_postal_address_v", 				"table"			]]);
		}
		else	
		{
		  $("#print_step_8_bank_name_system").text($("#step_8_bank_name_system").val());
		  $("#print_step_8_bank_subdivision_system").text($("#step_8_bank_subdivision_system").val());
		  $("#print_step_8_bank_account_system").text($("#step_8_bank_account_system").val());  
		  
		  if ($("#step_8_bank_name_system").val().trim() == "") 
		  {		 
			 $("#step_8_info_4").hide(); 		 		 
			 $("#svevBankRekv").text("");		 
		  }
			conditionShow([[$("#step_8_bank_name_system").val(), 			"#print_step_8_bank_name_system", 			"#step_8_bank_name_system", 				"table"			]]);
			conditionShow([[$("#step_8_bank_subdivision_system").val(), 	"#print_step_8_bank_subdivision_system", 	"#step_8_bank_subdivision_system", 			"tr"			]]);
			conditionShow([[$("#step_8_bank_account_system").val(), 		"#print_step_8_bank_account_system", 		"#step_8_bank_account_system", 				"tr"			]]);
		}
		
	}


	if (lst_steps.indexOf("9")>=0)
	{  
	  $("#print_step_9_fio_recept").text($("#step_9_last_name_recept").val()+' '+$("#step_9_first_name_recept").val()+' '+$("#step_9_middle_name_recept").val());  
	  $("#print_step_9_birthday_recept").text($("#step_9_birthday_recept").val());  
	  $("#print_step_9_bank_name_system").text($("#step_9_bank_name_system").val());  
	  $("#print_step_9_bank_subdivision_system").text($("#step_9_bank_subdivision_system").val());  
	  $("#print_step_9_bank_account_system").text($("#step_9_bank_account_system").val());
	  
	  conditionShow([[$("#step_9_birthday_recept").val(), 			"#print_step_9_birthday_recept", 			"#step_9_birthday_recept", 		"tr"			]]);  
	  conditionShow([[$("#step_9_bank_name_system").val(), 			"#print_step_9_bank_name_system", 			"#step_9_bank_name_system", 	"tr"			]]);
	  conditionShow([[$("#step_9_bank_subdivision_system").val(), 	"#print_step_9_bank_subdivision_system", 	"#step_9_bank_name_system", 	"tr"			]]);
	  conditionShow([[$("#step_9_bank_account_system").val(), 		"#print_step_9_bank_account_system", 		"#step_9_bank_account_system", 	"tr"			]]);  
	}

	if (lst_steps.indexOf("10")>=0)
	{
	  $("#print_step_10_full_name_org_akcept").text($("#step_10_full_name_org_akcept").val());  
	  $("#print_step_10_bank_name_system").text($("#step_10_bank_name_system").val());    
	  $("#print_step_10_bank_subdivision_system").text($("#step_10_bank_subdivision_system").val());  
	  $("#print_step_10_bank_account_system").text($("#step_10_bank_account_system").val());    
	  $("#print_step_10_number_cor_account").text($("#step_10_number_cor_account").val());    
	  $("#print_step_10_bik").text($("#step_10_bik").val());  
	  
	  conditionShow([[$("#step_10_full_name_org_akcept").val(), 	"#print_step_10_bank_name_system", 			"#step_10_full_name_org_akcept", 	"tr"			]]);  
	  conditionShow([[$("#step_10_bank_name_system").val(), 		"#print_step_10_bank_name_system", 			"#step_10_bank_name_system", 		"tr"			]]);  
	  conditionShow([[$("#step_10_bank_subdivision_system").val(), 	"#print_step_10_bank_subdivision_system", 	"#step_10_bank_subdivision_system", "tr"			]]);  
	  conditionShow([[$("#step_10_bank_account_system").val(), 		"#print_step_10_bank_account_system", 		"#step_10_bank_account_system", 	"tr"			]]);  
	  conditionShow([[$("#step_10_number_cor_account").val(), 		"#print_step_10_number_cor_account", 		"#step_10_number_cor_account", 		"tr"			]]);  
	  conditionShow([[$("#step_10_bik").val(), 						"#print_step_10_bik", 						"#step_10_bik", 					"tr"			]]);    
	}
	
	if (lst_steps.indexOf("11")>=0)
	{   
	  $("#print_step_11_render_address_type_system").text("Адрес "+$("#step_11_render_address_type_system").val()+":");  
	  var adr = formAddress(["step_11_index_pu","step_11_region_pu","step_11_district_pu","step_11_city_pu","step_11_settment_pu","step_11_street_pu","step_11_house_pu","step_11_corps_pu","step_11_building_pu","step_11_flat_pu","step_11_room_pu"]);  
	  $("#print_step_11_index_pu").text(adr);
	  
	  conditionShow([[$("#step_11_render_address_type_system").val(), 	"#print_step_11_render_address_type_system", 	"#step_11_render_address_type_system",  "tr"			]]);    
	  conditionShow([[adr, 												"#print_step_11_index_pu", 						"#step_11_index_pu",  					"fieldset"		]]);      
	}



}

function formAddress(aAddress)
{
  var resultAddr="";  
  for (var i=0; i<aAddress.length; i++)
  {    
    if ($("#"+aAddress[i]).val()!='') 
	{
	  resultAddr+=$("#"+aAddress[i]).val(); 
	  if (i<(aAddress.length-1)) resultAddr+=', ';	
	}  
  }	
  return resultAddr;
}  

function formDoc(aDoc)
{ 
  var result="";
  if ($("#"+aDoc[0]).val()!="") 
  {
    result = tu($("#"+aDoc[0]).val())+' '+tu($("#"+aDoc[1]).val())+' '+tu($("#"+aDoc[2]).val())+' выдан(о) '+tu($("#"+aDoc[3]).val())+' '+tu($("#"+aDoc[4]).val());
  }
  return result;
}  

function newPrint7()
{ 

   
  $("#print_step_7_fio_people").text(" "+$("#step_7_last_name_people").val()+' '+$("#step_7_first_name_people").val()+' '+$("#step_7_middle_name_people").val());  
  $("#print_step_7_birthday_people").text($("#step_7_birthday_people").val());
  $("#print_step_7_relation_degree").text($("#step_7_relation_degree").val());  
  var adr = formAddress(["step_7_people_address_v", "step_7_people_region_v", "step_7_people_district_v", "step_7_people_city_v", "step_7_people_settement_v", "step_7_people_street_v", "step_7_people_house_v",
	"step_7_people_housing_v", "step_7_people_building_v", "step_7_people_flat_v", "step_7_people_room_v"]);
  $("#print_step_7_people_address_v").text(adr);
  	
  conditionShow([[$("#step_7_birthday_people").val(), 	"#print_step_7_birthday_people", 	"#step_7_birthday_people",  "tr"		]]);
  conditionShow([[$("#step_7_relation_degree").val(), 	"#print_step_7_relation_degree", 	"#step_7_relation_degree",  "tr"		]]);  
  conditionShow([[adr, 									"#print_step_7_people_address_v", 	"#step_7_people_address_v", "fieldset"	]]);  
	
   
   
  $("#tab_7").find("table:eq(0) tr:eq(1) td:eq(2)").hide();
  $("#tab_7").find("table:eq(0) tr:eq(1) td:eq(3)").hide();  
  
  $("#step_7_is_dependency").closest("td").hide();

  var nameJsonClone="step_7_pbDocs";
  var namePrintClone='print7_doc_clone';
  var typePrintClone='class';
  var parentPrintClone='print7_doc_after';  
  var nameScreenClone='step_7_clone_block';
  var typeScreenClone='class';
  var parentScreenClone = "tab_7";  
  
  if ($(namePrintClone).length > 1) return;

  $("."+namePrintClone).hide();
  $("."+nameScreenClone).hide();
  
  var i = -1;

/*  

  var tmp = $("#"+nameJsonClone).val();	
  if (tmp == "" || tmp ==null || tmp =="null") return;
  
  var result = JSON.parse(tmp);  
  
  var count = result.length; 
  for (i=0; i < count; i++) 
  {  
  
  var isAccept = ( result[i].number!=null ? true:false);	
  if (isAccept)
  {
    $(".step_7_print_block").show();
	var aScreen=
	[ 
	  ['step_7_doc_name',		result[i].name, 		'textarea',	'name'],
	  ['step_7_series_doc',		result[i].series,		'input',	'name'],
	  ['step_7_number_doc',		result[i].number,		'input',	'name'],
	  ['step_7_date_doc',		result[i].dateIssue,	'input',	'name'],
	  ['step_7_org_doc',		result[i].organization,	'textarea', 'name']

	];   
   addClone(typeScreenClone,nameScreenClone,parentScreenClone,i,aScreen);
	 
 	var aPrint=
	[
	 ['print7_doc',formDoc(["step_7_doc_name_"+i+"_0","step_7_series_doc_"+i+"_1","step_7_number_doc_"+i+"_2","step_7_date_doc_"+i+"_3","step_7_org_doc_"+i+"_4"]), 'span',	'name']
	];

    addClone(typePrintClone,namePrintClone,parentPrintClone,i,aPrint);
   }	 
   
  }
*/

if (i<1) $(".step_7_print_block").remove();


 
}   



function newPrint12()
{   
  var nameJsonClone="step_12_msp";

  var namePrintClone='print_step_12_clone_block';
  var typePrintClone='class';
  var parentPrintClone='printTable12';

  var nameScreenClone='step_12_clone_block';
  var typeScreenClone='class';
  var parentScreenClone = "tab_12";  
  
  if ($(namePrintClone).length > 1) {return;}
  
  $(".print_step_12_clone_block").hide();
  $(".step_12_clone_block").hide(); 

  var tmp = $("#"+nameJsonClone).val();	
  
  tmp = jsonParse(12,tmp);
  
  if (tmp == "" || tmp ==null || tmp =="null") 
  {
      $("#tab_12").hide();
      return;
  }
   
  
  var arrObjectMembers = 
  [   
  "familyMember[].surname", 
  "familyMember[].name", 
  "familyMember[].patronymic", 
  "familyMember[].birthday", 
  "familyMember[].identityCard.type", 
  "familyMember[].identityCard.series", 
  "familyMember[].identityCard.number", 
  "familyMember[].identityCard.dateIssue", 
  "familyMember[].identityCard.organization"
  ];		
  var result = validObjectMembers(tmp, arrObjectMembers);	

  
   
  
   
   
   
   
  
  var count = result.familyMember.length; 
  
  var cur_ind = 0;
  
  var el;
  
  for (var i=0; i < count; i++) 
  {
    cur_ind++;
	var aFields=
	[ 
	  ['step_12_last_name_declarant_mf',		result.familyMember[i].surname, 					'input',	'name'],
	  ['step_12_first_name_declarant_mf',		result.familyMember[i].name,						'input',	'name'],
	  ['step_12_middle_name_declarant_mf',		result.familyMember[i].patronymic,					'input',	'name'],
	  ['step_12_birthday_declarant_mf',			result.familyMember[i].birthday,					'input',	'name'],
	  ['step_12_name_doc_declarant_mf',			result.familyMember[i].identityCard.type,			'textarea', 'name'],
	  ['step_12_doc_declarant_series_mf',		result.familyMember[i].identityCard.series,			'input',	'name'],
	  ['step_12_doc_declarant_number_mf',		result.familyMember[i].identityCard.number,			'input',	'name'],
	  ['step_12_doc_declarant_date_mf',			result.familyMember[i].identityCard.dateIssue,		'input',	'name'],
	  ['step_12_doc_declarant_who_issued_mf',	result.familyMember[i].identityCard.organization,	'textarea', 'name']
	];
	
	if (needAddClone(aFields))
	{	   
	   el = addClone(typeScreenClone,nameScreenClone,parentScreenClone,cur_ind,aFields);	   
		aFields=
		[ 
		 ['print_step_12_num_pp',		cur_ind, 																							 'span',	'class'],
		 ['print_step_12_fio',		result.familyMember[i].surname +" "+ result.familyMember[i].name +" "+result.familyMember[i].patronymic, 'span',	'class'],
		 ['print_step_12_birthday',	result.familyMember[i].birthday, 																		 'span',	'class']
		];
	  addClone(typePrintClone,namePrintClone,parentPrintClone,cur_ind,aFields);	  	  
	  
	  
	  
	  if (result.familyMember[i].identityCard.number.trim()=="")
	  {
	    $("#step_12_name_doc_declarant_mf_"+cur_ind).closest("fieldset").closest("fieldset").hide();		
		$("#step_12_name_doc_declarant_mf_"+cur_ind).closest("fieldset").parent().closest("fieldset").find("legend:first").hide();	  
	  }
	  
	   if ( result.familyMember[i].identityCard.type.trim() == "" || result.familyMember[i].identityCard.number == "") 
	   {
		 el.find("[name='step_12_name_doc_declarant_mf']").closest("fieldset").hide();		 
		 el.find("[name='step_12_name_doc_declarant_mf']").closest("fieldset").parent().closest("fieldset").find(".group_label").hide();
	   }	 
	  
   }   
  }
}   


function newPrint13()
{ 
  
  if ($("#step_13_clone_block_0").length > 0) return;  

var arrObjectMembers = [
"familyMember[].name",  
"familyMember[].surname",
"familyMember[].patronymic",
"familyMember[].birthday",						  
"familyMember[].relationDegree.name",
"familyMember[].relationDegree.dependents.dependent",
"familyMember[].identityCard.type",
"familyMember[].identityCard.series",
"familyMember[].identityCard.number",
"familyMember[].identityCard.dateIssue",
"familyMember[].identityCard.organization",
"familyMember[].document.name",
"familyMember[].document.series",
"familyMember[].document.number",
"familyMember[].document.dateIssue",
"familyMember[].document.organization"];		  

  var nameJsonClone="step_13_sdd";
  
  var namePrintClone='print_step_13_clone_block';
  var nameScreenClone='step_13_clone_block';  
  var nameScreenClone1='clone_step_13_info_3';  
  
  var typePrintClone='class';
  var typeScreenClone='id';
  var typeScreenClone1='class';  
  
  var parentPrintClone='printTable13';
  var parentScreenClone = "tab_13";
  var parentScreenClone1 = "tab_13";

  $(".print_step_13_clone_block").hide();  
  $("#step_13_clone_block").hide();
  $(".clone_step_13_info_3").hide();	  	  

  var tmp = $("#"+nameJsonClone).val();	
  
  tmp = jsonParse(13,tmp);
  
  if (tmp == "" || tmp ==null || tmp =="null") 
  {
    $("#tab_13").hide();
	$("#print_13_title").text("");
	$("#printTable13").hide();
    return;	
  }	  
   
  var result = validObjectMembers(tmp, arrObjectMembers);    
  
   
   
   
   
  
  var count = result.familyMember.length;
  
  var cur_ind = 0;
  var n_pp=1;

  if (count==0)
  {
    $("#tab_13").hide();
	$("#print_13_title").text("");
	$("#printTable13").hide();
    return;	
  }
  
  $("[name='step_13_last_name_declarant_sdd_z']").closest("fieldset").parent().closest("fieldset").find("legend:first").remove();			
  
  for (var i=0; i < count; i++) 
  {
   
   var ballast="";   
   
    
    
     var relDegree = (typeof(result.familyMember[i].relationDegree) == 'undefined' ? '':result.familyMember[i].relationDegree.name);  
     if (result.familyMember[i].relationDegree!=null)
	 {	  
	    
	    
	     if (result.familyMember[i].relationDegree.dependents.dependent === true)
		 {		    
		   ballast='Да';		   
		 }
	    
	 }
    
   
	var aFileds=
	[ 
	  ['step_13_last_name_declarant_sdd_z',		result.familyMember[i].surname, 					'input',	'name'],
	  ['step_13_first_name_declarant_sdd_z',	result.familyMember[i].name,						'input',	'name'],
	  ['step_13_middle_name_declarant_sdd_z',	result.familyMember[i].patronymic,					'input',	'name'],
	  ['step_13_birthday_declarant_sdd_z',		result.familyMember[i].birthday,					'input',	'name'],  
	  ['step_13_relation_degree_sdd_z',			relDegree,											'input', 	'name'],
	  ['step_13_presence_dependency_sdd_z',		(ballast=="Да" ? "checked":""),						'input',	'name']
	];	
    
    
	if (result.familyMember[i].surname.trim()!="" && needAddClone(aFileds))
	{
	   cur_ind++;
  	   addClone(typeScreenClone,nameScreenClone,parentScreenClone,cur_ind,aFileds);
	   $(".clone_step_13_info_3:last").hide();	   
	   $("#step_13_presence_dependency_sdd_z_"+cur_ind).closest("td").hide();	   
	   
		aFileds=
		[ 
		 ['print_step_13_num_pp',			n_pp++, 																								 	'span',	'class'],
		 ['print_step_13_fio',				result.familyMember[i].surname +" "+ result.familyMember[i].name +" "+result.familyMember[i].patronymic, 	'span',	'class'],
		 ['print_step_13_birthday',			result.familyMember[i].birthday, 																		 	'span',	'class'],
		 ['print_step_13_relationdegree',	relDegree, 																									'span',	'class'],
		 ['print_step_13_dependents',		ballast, 																		 							'span',	'class']
		];
		
		addClone(typePrintClone,namePrintClone,parentPrintClone,cur_ind,aFileds);		
		
		aFileds=
		[
		  ['step_13_name_doc_declarant_sdd_z',			result.familyMember[i].identityCard.type,			'textarea',	'name'],
		  ['step_13_doc_declarant_series_sdd_z',		result.familyMember[i].identityCard.series,			'input',	'name'],
		  ['step_13_doc_declarant_number_sdd_z',		result.familyMember[i].identityCard.number,			'input',	'name'],
		  ['step_13_doc_declarant_date_sdd_z',			result.familyMember[i].identityCard.dateIssue,		'input',	'name'],
		  ['step_13_doc_declarant_who_issued_sdd_z',	result.familyMember[i].identityCard.organization,	'textarea', 'name']   
		];		
		
		if (needAddClone(aFileds))
        {		 
		  cur_ind++;		
		  addClone(typeScreenClone1,nameScreenClone1,parentScreenClone1,cur_ind,aFileds);
		  
			conditionShow([
				[ result.familyMember[i].identityCard.type, 		"", 	"#step_13_name_doc_declarant_sdd_z_"+cur_ind,	 	"fieldset" ],
				[ result.familyMember[i].identityCard.series, 		"", 	"#step_13_doc_declarant_series_sdd_z_"+cur_ind, 	"td" ],
				[ result.familyMember[i].identityCard.number,   	"",     "#step_13_doc_declarant_number_sdd_z_"+cur_ind, 	"td" ],
				[ result.familyMember[i].identityCard.dateIssue,	"",		"#step_13_doc_declarant_date_sdd_z_"+cur_ind,   	"td" ],
				[ result.familyMember[i].identityCard.organization, "", 	"#step_13_doc_declarant_who_issued_sdd_z_"+cur_ind,	"td" ]
			]);				  
			
		  if (result.familyMember[i].identityCard.number.trim()=="")
		  {
			$("#step_13_name_doc_declarant_sdd_z_"+cur_ind).closest("fieldset").hide();            
		  }
		  		  
		}  
		
		aFileds=
		[
		  ['step_13_name_doc_declarant_sdd_z',			result.familyMember[i].document.name,				'textarea',	'name'],
		  ['step_13_doc_declarant_series_sdd_z',		result.familyMember[i].document.series,				'input',	'name'],
		  ['step_13_doc_declarant_number_sdd_z',		result.familyMember[i].document.number,				'input',	'name'],
		  ['step_13_doc_declarant_date_sdd_z',			result.familyMember[i].document.dateIssue,			'input',	'name'],
		  ['step_13_doc_declarant_who_issued_sdd_z',	result.familyMember[i].document.organization,		'textarea', 'name']   
		];

		if (needAddClone(aFileds))
        {
          cur_ind++;		
		  addClone(typeScreenClone1,nameScreenClone1,parentScreenClone1,cur_ind,aFileds);
		  
			conditionShow([
				[ result.familyMember[i].document.name, 		"", 	"#step_13_name_doc_declarant_sdd_z_"+cur_ind,	 	"fieldset" ],
				[ result.familyMember[i].document.series, 		"", 	"#step_13_doc_declarant_series_sdd_z_"+cur_ind, 	"td" ],
				[ result.familyMember[i].document.number,   	"",     "#step_13_doc_declarant_number_sdd_z_"+cur_ind, 	"td" ],
				[ result.familyMember[i].document.dateIssue,	"",		"#step_13_doc_declarant_date_sdd_z_"+cur_ind,   	"td" ],
				[ result.familyMember[i].document.organization, "", 	"#step_13_doc_declarant_who_issued_sdd_z_"+cur_ind,	"td" ]
			]);			

	  if (result.familyMember[i].document.number.trim()=="")
	  {
	    $("#step_13_name_doc_declarant_sdd_z_"+cur_ind).closest("fieldset").hide();				
	  }
			
		  
		}
		
		 
	}	
  }  
}   


function newPrint14()
{
 
  if ($("#step_14_clone_block_1").length > 0) return;  

 var arrObjectMembers = [
"familyMember[].name",  
"familyMember[].surname",
"familyMember[].patronymic",
"familyMember[].birthday",						  
"familyMember[].relationDegree.name",
"familyMember[].relationDegree.dependents.dependent",
"familyMember[].identityCard.type",
"familyMember[].identityCard.series",
"familyMember[].identityCard.number",
"familyMember[].identityCard.dateIssue",
"familyMember[].identityCard.organization",						  
"familyMember[].document.name",
"familyMember[].document.series",
"familyMember[].document.number",
"familyMember[].document.dateIssue",
"familyMember[].document.organization"];		  



  var nameJsonClone="step_14_sddnr";
  
  var namePrintClone='print_step_14_clone_block';  
  var typePrintClone='class';
  var parentPrintClone='printTable14';
  
  var nameScreenClone='step_14_clone_block';  
  var typeScreenClone='id';  
  var parentScreenClone = "tab_14";
  
  var nameScreenClone1='clone_step_14_info_4';  
  var typeScreenClone1='class';  
  var parentScreenClone1 = "tab_14";
  
  if ($("."+namePrintClone).length > 1) return;  

  $("."+namePrintClone).hide();
  $("#"+nameScreenClone).hide();
  $("."+nameScreenClone1).hide();	  	  

  var tmp = $("#"+nameJsonClone).val();	
  
  tmp = jsonParse(14,tmp);
  
  if (tmp == "" || tmp ==null || tmp =="null") 
  {
    $("#tab_14").hide();
	$("#print_14_title").text("");
	$("#printTable14").hide();
    return;  
  }	
  
   
  var result = validObjectMembers(tmp, arrObjectMembers);
  
  var count = result.familyMember.length;     
  
  if (count == 0)
  {
	  $("#tab_14").hide();
	  $("#print_14_title").text("");
	  $("#printTable14").hide();
	  return;  
  }
  
  
   
  
  var cur_ind = 0;
  var n_pp = 1;
  
  for (var i=0; i < count; i++) 
  {
   var ballast="";   
   
    
    
     var relDegree = (typeof(result.familyMember[i].relationDegree) == 'undefined' ? '':result.familyMember[i].relationDegree.name);  
     if (result.familyMember[i].relationDegree!=null)
	 {
	    
	    
	     if (result.familyMember[i].relationDegree.dependents.dependent === true)
		 {		    
		   ballast='Да';		   
		 }
	    
	 }   
    
   
	aFileds=
	[ 
	  ['step_14_last_name_declarant_sdd_nz',		result.familyMember[i].surname, 					'input',	'name'],
	  ['step_14_first_name_declarant_sdd_nz',		result.familyMember[i].name,						'input',	'name'],
	  ['step_14_middle_name_declarant_sdd_nz',		result.familyMember[i].patronymic,					'input',	'name'],
	  ['step_14_birthday_declarant_sdd_nz',			result.familyMember[i].birthday,					'input',	'name'],  
	  ['step_14_relation_degree_sdd_nz',			relDegree,											'input', 	'name'],
	  ['step_14_presence_dependency_sdd_nz',		(ballast=="Да" ? "checked":""),						'input',	'name']
	];   

   if (needAddClone(aFileds))
	{
	   cur_ind++;
	   addClone(typeScreenClone,nameScreenClone,parentScreenClone,cur_ind,aFileds);
	   $(".clone_step_14_info_4:last").hide();
	   $("#step_14_presence_dependency_sdd_nz_"+cur_ind).closest("td").hide();
   
		var aFileds=
		[ 
		 ['print_step_14_num_pp',			(n_pp++).toString(),																				 		'span',	'class'],
		 ['print_step_14_fio',				result.familyMember[i].surname +" "+ result.familyMember[i].name +" "+result.familyMember[i].patronymic, 	'span',	'class'],
		 ['print_step_14_birthday',			result.familyMember[i].birthday, 																		 	'span',	'class'],
		 ['print_step_14_relationdegree',	relDegree, 																									'span',	'class'],
		 ['print_step_14_dependents',		ballast, 																		 							'span',	'class']
		];

		if (needAddClone(aFileds))
		{
		    cur_ind++;
		    addClone(typePrintClone,namePrintClone,parentPrintClone,cur_ind,aFileds);		

			aFileds=
			[
			  ['step_14_name_doc_declarant_sdd_nz',			result.familyMember[i].identityCard.type,			'textarea',	'name'],
			  ['step_14_doc_declarant_series_sdd_nz',		result.familyMember[i].identityCard.series,			'input',	'name'],
			  ['step_14_doc_declarant_number_sdd_nz',		result.familyMember[i].identityCard.number,			'input',	'name'],
			  ['step_14_doc_declarant_date_sdd_nz',			result.familyMember[i].identityCard.dateIssue,		'input',	'name'],
			  ['step_14_doc_declarant_who_issued_sdd_nz',	result.familyMember[i].identityCard.organization,	'textarea', 'name']   
			];
			
		}
		
		    if (needAddClone(aFileds))
		    {
			cur_ind++;			
			addClone(typeScreenClone1,nameScreenClone1,parentScreenClone1,cur_ind,aFileds);
			
			conditionShow([
				[ result.familyMember[i].identityCard.type, 		"", 	"#step_14_name_doc_declarant_sdd_nz_"+cur_ind,	 	"fieldset" ],
				[ result.familyMember[i].identityCard.series, 		"", 	"#step_14_doc_declarant_series_sdd_nz_"+cur_ind, 	"td" ],
				[ result.familyMember[i].identityCard.number,   	"",     "#step_14_doc_declarant_number_sdd_nz_"+cur_ind, 	"td" ],
				[ result.familyMember[i].identityCard.dateIssue,	"",		"#step_14_doc_declarant_date_sdd_nz_"+cur_ind,   	"td" ],
				[ result.familyMember[i].identityCard.organization, "", 	"#step_14_doc_declarant_who_issued_sdd_nz_"+cur_ind,"td" ]
			]);			
			
			if (result.familyMember[i].identityCard.number.trim()=="")
			{
				$("#step_14_doc_declarant_number_sdd_nz_"+cur_ind).closest("fieldset").hide();				
			}
			
			
			}
		
		
		   
			aFileds=
			[
			  ['step_14_name_doc_declarant_sdd_nz',			result.familyMember[i].document.name,				'textarea',	'name'],
			  ['step_14_doc_declarant_series_sdd_nz',		result.familyMember[i].document.series,				'input',	'name'],
			  ['step_14_doc_declarant_number_sdd_nz',		result.familyMember[i].document.number,				'input',	'name'],
			  ['step_14_doc_declarant_date_sdd_nz',			result.familyMember[i].document.dateIssue,			'input',	'name'],
			  ['step_14_doc_declarant_who_issued_sdd_nz',	result.familyMember[i].document.organization,		'textarea', 'name']   
			];
		    
		   if (needAddClone(aFileds))
		   {			
			cur_ind++;
			addClone(typeScreenClone1,nameScreenClone1,parentScreenClone1,cur_ind,aFileds);
			
			conditionShow([
				[ result.familyMember[i].document.name, 			"", 	"#step_14_name_doc_declarant_sdd_nz_"+cur_ind,	 	"fieldset" ],
				[ result.familyMember[i].document.series, 			"", 	"#step_14_doc_declarant_series_sdd_nz_"+cur_ind, 	"td" ],
				[ result.familyMember[i].document.number,   		"",     "#step_14_doc_declarant_number_sdd_nz_"+cur_ind, 	"td" ],
				[ result.familyMember[i].document.dateIssue,		"",		"#step_14_doc_declarant_date_sdd_nz_"+cur_ind,   	"td" ],
				[ result.familyMember[i].document.organization, 	"", 	"#step_14_doc_declarant_who_issued_sdd_nz_"+cur_ind,"td" ]
			]);			
			
		   }
	}	
  }
}   

function newPrint15()
{
  var nameJsonClone="step_15_infmi";
  
  var namePrintClone='print_step_15_clone_block';  
  var typePrintClone='class';
  var parentPrintClone='printTable15';

  var nameScreenClone='step_15_clone_block';  
  var typeScreenClone='class';  
  var parentScreenClone = "tab_15";
  
   

  $("."+namePrintClone).hide();
  $("."+nameScreenClone).hide();  

  var tmp = $("#"+nameJsonClone).val();	
  
  tmp = jsonParse(15,tmp);
  
  if (tmp == "" || tmp ==null || tmp =="null") 
  {
	$("#print_15_title").text("");
	$("#print_15_title").hide();
	$("#printTable15").hide();  
    $("#tab_15").hide();
    return;   
  }	
  
  var arrObjectMembers = [
  "familyMembers[].profits.profit[]",
  "familyMembers[].surname",
  "familyMembers[].name",
  "familyMembers[].patronymic",
  "familyMembers[].birthday",
  "familyMembers[].profits.profit[].reqParams.reqParam.name",
  "familyMembers[].profits.profit[].amount",
  "familyMembers[].profits.profit[].month)",
  "familyMembers[i].profits.profit[].year",
  "familyMembers[].profits.profit[].type"
];
  
   
   
  var result = validObjectMembers(tmp, arrObjectMembers);    
  
  var count = result.familyMembers.length;
  
  count_tr=0;
  
  var cur_ind = 0;  
  
  for (var i=0; i < count; i++) 
  {
	var count2 = result.familyMembers[i].profits.profit.length;	

	var aFileds= 
	[
	 ['step_15_step_15_name_declarant_m',	result.familyMembers[i].surname +' ' +result.familyMembers[i].name +' ' + result.familyMembers[i].patronymic, 	'input',	'name'],
	 ['step_15_birthday_declarant_m',		result.familyMembers[i].birthday, 																				'input',	'name']	 
	];		
	
   if (needAddClone(aFileds))
	{
	     
	    cur_ind++;	   
		var el_screen = addClone(typeScreenClone,nameScreenClone,parentScreenClone,cur_ind,aFileds);					
		
		if ( $("#step_15_step_15_name_declarant_m_"+cur_ind).val().length>45) { convertToTextarea("#step_15_step_15_name_declarant_m_"+cur_ind); }
		
		
		
		var aFileds=
		[ 
		 ['print_step_15_num_pp',	i*count2+1,																							 			'span',	'class'],
		 ['print_step_15_fio',		result.familyMembers[i].surname +' ' +result.familyMembers[i].name +' ' + result.familyMembers[i].patronymic, 	'span',	'class'],
		 ['print_step_15_birthday',	result.familyMembers[i].birthday, 																		 		'span',	'class']
		]; 

		var el = addClone(typePrintClone,namePrintClone,parentPrintClone,cur_ind,aFileds);
		
		var sumCount = 0;
	
		for (var j=0; j<count2; j++)
		{		    
		    if (result.familyMembers[i].profits.profit[j].amount.trim()=="" || result.familyMembers[i].profits.profit[j].amount.trim()=="0")
			{
				 
				 
			}
			else
			{			
			sumCount+=result.familyMembers[i].profits.profit[j].amount;
			
			var svd = el_screen.find('[name="step_15_name_doc_sdd"]:last');
			svd.attr("id","step_15_name_doc_sdd"+"_"+count2*i+"_"+j);			
			svd.closest("table").hide();
			
			count_tr++;
			var aFileds=
			[ 
			 ['print_step_15_num_pp',	i*count2+j+1,												'span',	'class'],			 
			 ['print_step_15_month',	monthName(result.familyMembers[i].profits.profit[j].month),	'span',	'class'],
			 ['print_step_15_year',		result.familyMembers[i].profits.profit[j].year, 			'span',	'class'],
			 ['print_step_15_type',		result.familyMembers[i].profits.profit[j].type, 			'span',	'class'], 
			 ['print_step_15_amount',	result.familyMembers[i].profits.profit[j].amount, 			'span',	'class']             
			];	
			
			for (k=0; k<aFileds.length; k++) 
			{
			  el.find("."+aFileds[k][0]).text(aFileds[k][1]);		
			}
			
			if (j<(count2-1)) 			
			{
			  if (result.familyMembers[i].profits.profit[j+1].amount.trim()!="" && result.familyMembers[i].profits.profit[j+1].amount.trim()!="0")  
			  {			
			    el = addClone(typePrintClone,namePrintClone,parentPrintClone,count2*i+j,aFileds);
			  }
			} 
			
			var aFileds= 
			[		 			 
			 ['step_15_mm',				monthName(result.familyMembers[i].profits.profit[j].month), 	'input',	'name'],
			 ['step_15_gg',				result.familyMembers[i].profits.profit[j].year, 				'input',	'name'],
			 ['step_15_type_profit',	result.familyMembers[i].profits.profit[j].type, 				'input',	'name'], 
			 ['step_15_sum_profit',		result.familyMembers[i].profits.profit[j].amount, 				'input',	'name']             
			];	
		
			for (k=0; k<aFileds.length; k++) 
			{
			  var cur_e = el_screen.find('[name="'+aFileds[k][0]+'"]:last');			  
			  cur_e.val(aFileds[k][1]);
			  cur_e.attr("id",aFileds[k][0]+"_"+count2*i+"_"+j);			  
			  if (aFileds[k][1].length>30) 
			  { 
			    convertToTextarea(cur_e.attr("id"));
			  }
			}		
			
			if (j<(count2-1))
			{
			   
			  if (result.familyMembers[i].profits.profit[j+1].amount.trim()!="" && result.familyMembers[i].profits.profit[j+1].amount.trim()!="0")  
			  {
			    var el_screenId = el_screen.attr("id");
			    var el_screen1 = el_screen.find("table:eq(1) tr:last").clone(); 		  		  				
			    $("#"+aFileds[1][0]+"_"+count2*i+"_"+j).parent().parent().parent().append(el_screen1);
			  }
			   
			}
			 
			}
		}
		if (sumCount==0)
		{
			el.hide();
			el_screen.hide();		
		}
		
  }
  
  if (count_tr == 0)
  {
	$("#print_15_title").text("");
	$("#print_15_title").hide();
	$("#printTable15").hide();
	$("#tab_15").hide();
  }
  
}
}   

function monthName(monthNumber)
{
  var name ="";
  if (monthNumber == "01" || monthNumber == "1") name = "Январь";  
  if (monthNumber == "02" || monthNumber == "2") name = "Февраль";  
  if (monthNumber == "03" || monthNumber == "3") name = "Март";  
  if (monthNumber == "04" || monthNumber == "4") name = "Апрель";  
  if (monthNumber == "05" || monthNumber == "5") name = "Май";  
  if (monthNumber == "06" || monthNumber == "6") name = "Июнь";  
  if (monthNumber == "07" || monthNumber == "7") name = "Июль";  
  if (monthNumber == "08" || monthNumber == "8") name = "Август";  
  if (monthNumber == "09" || monthNumber == "9") name = "Сентябрь";  
  if (monthNumber == "10" || monthNumber == "10") name = "Октябрь";  
  if (monthNumber == "11" || monthNumber == "11") name = "Ноябрь";  
  if (monthNumber == "12" || monthNumber == "12") name = "Декабрь";  
  return name;
}


function newPrint16()
{
if (lst_steps.indexOf("16")>=0)
{
  $("#step_16_name_info").val("");
  var tmp = $("#step_16_ir").val();
  
  $("#tab_16").find("table").attr("id","tab_step_16");
  
  tmp = jsonParse(16,tmp);
  
  if (tmp != "" && tmp!=null && tmp !="null")
  { 
	
  var arrObjectMembers = [ "infRequest[].reqParams[].reqParam.name", "infRequest[].name", "infRequest[].group.name"];	
	
  var result = validObjectMembers(tmp, arrObjectMembers);
	
	  var count0 = result.infRequest.length;	  
	  
	  el=$("#tab_16").find("tr:eq(0)");
	  
	  for (var i=0; i<count0; i++)
	  {	  	            	  
	  
	    el.find("#step_16_name_info").parent().prev().hide();		
		
		if (result.infRequest[i].name.trim()!="")
		{
		  $("#print_group_16_info_"+i).text(result.infRequest[i].name);
		  el.find("#step_16_name_info").parent().parent().before('<span class="print_subTitle" style="text-align:left;">'+ result.infRequest[i].name +':</span>');		  
		  el.find("#step_16_name_info").css({'text-align':'left'});
		}
        else
		{
		  $("#print_group_16_info_"+i).closest("tr").hide();		
		}		
		
		if (result.infRequest[i].group.name.trim()!="")
		{$("#print_group1_16_info_"+i).text(result.infRequest[i].group.name);}
		else
		{
		  $("#print_group1_16_info_"+i).closest("tr").hide();
		}
		
	    var count_1 = result.infRequest[i].reqParams.length;	
	    for (var j=0; j<count_1; j++)
	    {
	      if (result.infRequest[i].reqParams[j].reqParam!=null)
		  {
			var count_2=result.infRequest[i].reqParams[j].reqParam.length;
			for (var k=0; k<count_2; k++)
			{			   
			   
			  el.find("#step_16_name_info").val(result.infRequest[i].reqParams[j].reqParam[k].name);
			  $("#print_step_16_info_"+(i*count_1+j)*count_2+k).text(result.infRequest[i].reqParams[j].reqParam[k].name);			  
			}  
		  }  
	    }	

		if (i<(count0-1))
		{
          el=$("#tab_16").find("tr:eq(0)").clone();		  
	      $("#tab_step_16").append(el);
		}

	  }
  }
  else
  {
    $("#tab_16").hide();
  }  
}
}   


	function delete_step(el,className) 
	{   
	  var str_id = el.id;  
	  var pos = str_id.lastIndexOf('_');
	  var i = str_id.substr(pos + 1);  
	  $(el).closest(className).hide();  
	  
	  
	  el.find("input").each(function () 
		{
			$(this).attr("required",false);
			$(this).removeAttr("error");
		});
		
	  el.find("textarea").each(function () 
		{
			$(this).attr("required",false);
			$(this).removeAttr("error");
		});
		
	  
	  
	   
	}
	
function newPrint17()
{
   
   
   
  
  
   
  var nameJsonClone="step_17_ir";
  
  var namePrintClone='print_step_17_clone_block_1';  
  var typePrintClone='class';
  var parentPrintClone='t_print17';

  var nameScreenClone='step_17_clone_block_1';  
  var typeScreenClone='class';  
  var parentScreenClone = "tab_17";
  
   


  var tmp = $("#"+nameJsonClone).val();	
  
  tmp = jsonParse(17,tmp);
  
  if (tmp == "" || tmp ==null || tmp =="null") 
  {
    $("#tab_17").hide();
    return;   
  }	
  
   
  
  var arrObjectMembers = 
	[ "information[].data[].params.param[].name"];				
	
  var  result = validObjectMembers(tmp, arrObjectMembers);	  
  
  var count = result.information.length;  
  
  for (var i=0; i < count; i++) 
  {	
    var el = $(".step_17_clone_block_1:first").clone();
    $("#tab_17").append(el);	
	
	el.find("tbody").attr("id","step_17_body_"+i);
	
	el.find("input").each(function () 
		{
			$(this).attr("id", $(this).attr("name") + "_" + i);
		});

	el.find("textarea").each(function () 
	{
		$(this).attr("id", $(this).attr("name") + "_" + i);
	});
	
  
	var count2 = result.information[i].data.length;
	for (j=0; j < count2; j++) 
	{	        
	
		if  (j==0)
		{
		  if (tu(result.information[i].group.name) == "") 
		  {
		  $("#print_step_17_name_group_info_"+((i*count2)+j)).closest("tr").hide();
		  el.find("#step_17_name_group_info_"+i).closest("tr").hide();
		  }
		  else
		  {
		   $("#print_step_17_name_group_info_"+((i*count2)+j)).text(tu(result.information[i].group.name));
		    el.find("#step_17_name_group_info_"+i).val(result.information[i].group.name);	
		  }
		}  		
		
		if (tu(result.information[i].data[j].Name) == "")
		{
		  $("#print_step_17_name_info_"+((i*count2)+j)).closest("tr").hide();
		}
		else
		{
		  $("#print_step_17_name_info_"+((i*count2)+j)).text(tu(result.information[i].data[j].Name));	
		}
		
		if (result.information[i].data[j].Name.trim()=="")
		{
		  el.find("#step_17_name_info_"+i).closest("tr").hide();
		}
		else
		{
          el.find("#step_17_name_info_"+i).val(result.information[i].data[j].Name);		
		}
	

	  count_3 = result.information[i].data[j].params.param.length;
	  for (var k=0; k<count_3; k++)
	  {  
         
		 
		 
		
		  
		  el.find(".step_17_name_rekvizit_info").hide();		  
		  el.find("[name='step_17_rekvizit_info']").hide();		  
		  $("#step_17_body_"+i).append("<tr><td  align='right' valign='top'  class='ext5'><span class='label'>"+result.information[i].data[j].params.param[k].name+"</span></td><td><textarea cols='58' rows='6' style='resize:none' disabled='disabled'>"+result.information[i].data[j].params.param[k].value+"</textarea></td></tr>");
      }
		
		if (k>0)
		{
          $("#step_17_body_"+i).append("<tr><td>"+result.information[i].data[j].params.param[k-1].name+"</td><td>"+result.information[i].data[j].params.param[k-1].value+"</td></tr>");				  
		}
		$("#step_17_body_"+i+" tr:last").hide();	
	}

    }
	
	$(".step_17_clone_block_1:first").remove();
	$("#tab_17").find("fieldset:first").hide();
}
	
	
	
 
function testDateEnter(source,nameTest,ind)
{   
    
   
   var tmp_cur = new Date();    
   var tmp_d2  = toDate(source.value);

      
   if ( tmp_d2.getTime() > tmp_cur.getTime() )
   {	    
     alert("Дата не может быть больше текущей");	 
	 source.value = null;
	 return;
   }
   
   if (nameTest=="creationDate")
   {     	
	var valTest = $("#creationDate").text();	
   }
   else
   {
     valTest=$("#"+nameTest).text();
	 if (valTest == "" || valTest == null)
	 {
	   valTest=$("#"+nameTest).val();
	 }
   }

   if (typeof(ind)=='undefined')
   {	  
	  var mes = "Неверная дата";	
	  if ($("#idCurrentForm").text()=="2") 
	  {
	    mes = "Дата меньше даты создания заявления";
	  }

	  if ($("#idCurrentForm").text()=="5" || $("#idCurrentForm").text()=="6")   
	  {	  
		mes = "Дата меньше даты регистрации";
	  }	
	  
	  if ($("#idCurrentForm").text()=="4" || $("#idCurrentForm").text()=="7")  
	  {
	    "Дата меньше даты регистрации заявления"
 	  }	  
	  
	  var tmp_d1  = toDate(valTest);
	  
      if (tmp_d1.getTime() > tmp_d2.getTime())
	  {	    
        alert(mes);
		source.value = null;
	  }
	}  
	else
    {
      if (ind=="i")
      {	  
	    var checkbox = source.id;	  
	    var pos = checkbox.lastIndexOf('_');	    
		var i = checkbox.substr(pos + 1);	        		
	    if (($("#"+nameTest+"_"+i).val() == null) || ( source.value == null)) {return;}	  
	    var tmp_d1  = toDate($("#"+nameTest+"_"+i).val());
	  }
      else
      {
	    if ( (valTest == null) || ( source.value == null)	 ) {return;}	  
	    var tmp_d1  = toDate(valTest);
      }	
	  
	  
      if (tmp_d1.getTime() > tmp_d2.getTime())
	  {	    
        if (nameTest == 'date_reference_number') {alert("Дата меньше даты регистрации заявления");}
		if (nameTest == 'date_request_mv_t') {alert("Дата меньше даты отправки запроса");}
		source.value = null;
	  }   
    } 
	

	
}

function conditionShow(aCond)
{  
  for (var i=0; i<aCond.length; i++)
  {
	 
	 
	 
	 
	
    var testValue;
	
	if (typeof(aCond[i][0]) == 'undefined') testValue = "";
	else testValue = aCond[i][0].toString();	
	
	if (testValue == "") 
	  {
		if (aCond[i][1] != "") $(aCond[i][1]).parent().text("");		
		if (aCond[i][3] == "") 
		{		  
		  $(aCond[i][2]).hide(); 
		}
		else
		{		  		
		  $(aCond[i][2]).closest(aCond[i][3]).hide();
		  if (aCond[i][3] == "td") 
		  {
			 $(aCond[i][2]).parent().prev().hide();
		  }
		}  
	  }  
  }
}

function conditionShowTr(aCond)
{
   
   
   
  var allEmpty=true;  

  for (var i=0; i<aCond.length; i++)
  {
    if ( $("#"+aCond[i]).val().trim() != "")
	{
	  allEmpty=false;
	}
  }
  
  if (allEmpty) 
  {  
    $("#"+aCond[0]).closest("tr").hide();
  } 
  else
  {
    for (var i=0; i<aCond.length; i++)
    {
      if ( $("#"+aCond[i]).val().trim() == "")	  
	  {	  
	    $("#"+aCond[i]).parent().prev().text("");
	    $("#"+aCond[i]).parent().text("");
		 
		 
	  }	
	  else
	  {
	    $("#"+aCond[i]).parent().prev().css({'align':'center'});
		$("#"+aCond[i]).parent().css({'align':'center'});
	  }
    }  
  }	
}


	function testReguestMv(source,viewOnly)
	{	    
		if (source.attr("checked") && !viewOnly)
		{		    
			source.parent().find("input[name='number_reguest_mv']").attr("disabled",false);
			source.parent().find("input[name='date_request_mv_t']").attr("disabled",false);
			source.parent().find("input[name='date_receipt_mv_t']").attr("disabled",false);
			
			source.parent().find(".dateMarker").show();
			source.parent().find("input[name='number_reguest_mv']").attr("required",true);
			source.parent().find("input[name='date_request_mv_t']").attr("required",true);
			
			source.parent().find("input[name='date_request_mv_t']").val(getCurDate());
			 
			
			source.parent().find("input[name='date_receipt_mv_t']").attr("required",true);
			
			source.parent().find("input[name='number_reguest_mv']").removeAttr("error");
			source.parent().find("input[name='date_request_mv_t']").removeAttr("error");
			source.parent().find("input[name='date_receipt_mv_t']").removeAttr("error");
		} else 
		{		    
		    source.parent().find(".dateMarker").hide();
			source.parent().find("input[name='number_reguest_mv']").attr("disabled",true);
			source.parent().find("input[name='date_request_mv_t']").attr("disabled",true);
			source.parent().find("input[name='date_receipt_mv_t']").attr("disabled",true);
			if (!source.attr("checked"))
			{
			  source.parent().find("input[name='date_request_mv_t']").val(null);
			  source.parent().find("input[name='date_receipt_mv_t']").val(null);
			}			
			source.parent().find("input[name='number_reguest_mv']").attr("required",false);
			source.parent().find("input[name='date_request_mv_t']").attr("required",false);
			source.parent().find("input[name='date_receipt_mv_t']").attr("required",false);
			
			source.parent().find("input[name='number_reguest_mv']").removeAttr("error");
			source.parent().find("input[name='date_request_mv_t']").removeAttr("error");
			source.parent().find("input[name='date_receipt_mv_t']").removeAttr("error");
		}	
		
				
		$(".datepicker").each(function () 
		{
		  if ($(this).attr("disabled")) $(this).parent().find(".ui-datepicker-trigger").hide();
		  else $(this).parent().find(".ui-datepicker-trigger").show();	  
		});	
		
		
	}

	
	function testReguest(source,viewOnly)
	{	
	    
	    if (source.attr("checked") && !viewOnly)
		{
			source.parent().find(".dateMarker").show();
			source.parent().find("input[name='date_receipt_l_y']").attr("required","required");	
			source.parent().find("input[name='date_receipt_l_y']").attr("disabled",false);
			source.parent().find("input[name='date_receipt_l_y']").removeAttr("error");			
			source.parent().find("input[name='date_receipt_l_y']").val(getCurDate());			  			
			
		    if (!source.parent().parent().prev().parent().find("[name='choice_trustee_rek']").attr("checked"))
		    {			
		      source.parent().parent().prev().parent().find("[name='rekv_document_2_y']").attr("disabled",false);
			  source.parent().parent().prev().parent().find("[name='rekv_document_2_y']").closest("tr").find(".field-requiredMarker").show();
		    }
			
			
			
		} else 
		{
			source.parent().find(".dateMarker").hide();
			source.parent().find("input[name='date_receipt_l_y']").removeAttr("required");
			source.parent().find("input[name='date_receipt_l_y']").attr("disabled",true);
			source.parent().find("input[name='date_receipt_l_y']").removeAttr("error");						
			if (!source.attr("checked"))
			{
			  source.parent().find("input[name='date_receipt_l_y']").val(null);
			}
			
			source.parent().parent().prev().parent().find("[name='rekv_document_2_y']").attr("disabled",true);
            source.parent().parent().prev().parent().find("[name='rekv_document_2_y']").closest("tr").find(".field-requiredMarker").hide();			
		}	
		
		$(".datepicker").each(function () 
		{
		  if ($(this).attr("disabled")) {$(this).parent().find(".ui-datepicker-trigger").hide();}
		  else $(this).parent().find(".ui-datepicker-trigger").show();	  
		});		

	}
	
	
	function hideButtonPicker(id)
	{
	  var nameId="#"+id;	  
	  if ($(nameId).attr("disabled"))
	  {
	    $(nameId).parent().find(".ui-datepicker-trigger").hide();
	  }
	  else
	  {
	    $(nameId).parent().find(".ui-datepicker-trigger").show();
	  }
	}

function validObjectMembers(listArray, arrTest)
{
   
   
   
  
  if ( typeof(listArray) == 'undefined' ) 
  {
    listArray = {};	  
  }
  
  for (var i=0; i<arrTest.length; i++)
  {
    var arrOneTest = arrTest[i].split(".");		
	  j=0;
	  if (arrOneTest[j].indexOf("[]")>=0)
	  {
	    var elName = arrOneTest[j].replace("[]","");
		if (typeof(listArray[elName]) == 'undefined') 
		{			  
		  listArray[elName] = [];
		}
		else
		if (typeof(listArray[elName].length) == 'undefined')
		{
		  listArray[elName] = [listArray[elName]];
		}
	    if (j<arrOneTest.length-1)
        {    			  
		  arrOneTest.splice(0,j+1);
	      for (var k=0; k<listArray[elName].length; k++)
		  {		    							
			if (listArray[elName][k] == "") { listArray[elName][k]={}; }
		    listArray[elName][k] = validObjectMembers(listArray[elName][k], [arrOneTest.join(".")]);
	      }		
		}					   	
	  }
	  else
	  {
	    var elName = arrOneTest[j];
        if (typeof(listArray[elName]) == 'undefined') 
	    { 		
		  listArray[elName] = "";
	    }
		    if (j<arrOneTest.length-1)
        {    			  
		  arrOneTest.splice(0,j+1);
		  if (listArray[elName] == "") { listArray[elName]={}; }
	      listArray[elName] = validObjectMembers(listArray[elName],[arrOneTest.join(".")]);
		}  
	  }
  }	
  return listArray;
}

function changeStepNames()
{
  f_steps = $("#listSteps");
  lst_steps = f_steps.val().split("\n");
  
  var names=[
  "",  
  "", 																		 
  "Законный представитель",  												 
  "Законный представитель", 												 
  "Правообладатель", 														 
  "Правообладатель", 														 
  "Правообладатель", 														 
  "Сведения о лице, на основании данных которого оказывается услуга", 		 
  "Сведения о почтовых (банковских) реквизитах для получения выплат", 		 
  "Сведения о выплатных реквизитах индивидуального предпринимателя", 		 
  "Сведения о выплатных реквизитах юридического лица", 						 
  "Сведения об адресе предоставления услуги", 								 
  "Сведения о членах семьи для предоставления услуги", 						 
  "Сведения о членах семьи для расчета СДД (среднедушевой доход), зарегистрированных по адресу регистрации правообладающего лица", 		 
  "Сведения о членах семьи для расчета СДД (среднедушевой доход), не зарегистрированных по адресу регистрации правообладающего лица", 	 
  "Сведения о доходах всех членов семьи", 									 
  "Запрашиваемые сведения", 												 
  "Дополнительные сведения", 												 
  "Сведения о документах заявителя, необходимых для оказания услуги", 		 
  "Документы, которые необходимо принести лично" 							 
  ];
  
  var tmp = $("#appData_cond").val();	  
  
  if (typeof(tmp) != 'undefined') 	  
  {	  
  
    tmp = jsonParse("1 (listSteps-названия шагов)",tmp);
	if (tmp!='' && tmp!=null)
    {		  
	  var result = tmp;		
	  for (var i=0; i<result.length; i++)
	  {		  
	   
	   
	   
		if (typeof(result[i].number) != 'undefined')
		{
		    if (typeof(result[i].name) != 'undefined') 
		    {
			  names[result[i].number] = result[i].name;
		    }	
		}
	   
	  }
	}		
  }	
  
  /*
  if (names[i].substring(0,4) == "Шаг " || names[i].substring(0,4) == "шаг ")
  {
    var k=names[i].substring(4).indexOf("–");
	if (k>=0)
	{
	  names[i] = names[i].substring(k+1);
	}
  }	  	  
  */
   
   
   
   
   
  
  for (var j=4; j<19; j++)
  {	    
    $("#tab_"+j).find("span:first").text(names[j]);
	if ($("#print_"+j+"_title").text()!="") $("#print_"+j+"_title").text(names[j]);
  }
  
}

function compareConstraintRKS(elA, elB) 
{  
  var cmp=0;
  if (elA.keyConstraintRKS > elB.keyConstraintRKS) cmp=1;
  if (elA.keyConstraintRKS < elB.keyConstraintRKS) cmp=-1;
  return cmp;
}


function constraintArray(arrObj, aKey, aLife,fullListName)
{
   
   
   
   
  for (var i=0; i<arrObj.length; i++)
  {
    var key = "";
    for (var j=0; j<aKey.length; j++)
    { 
	  if (typeof(arrObj[i][aKey[j]]) != 'undefined') 
	  {
	    key +=  arrObj[i][aKey[j]];
	  }	
    }	
    arrObj[i].keyConstraintRKS = key;
  }	  
   
  arrObj.sort(compareConstraintRKS);
   
  for (var i = arrObj.length - 1; i > 0; i--) 
  {
   if (arrObj[i].keyConstraintRKS == arrObj[i - 1].keyConstraintRKS) 
   { 	     
     var needDelete=true;
	 for (var i_life=0; i_life<aLife.length; i_life++)
	 {
	   if (eval(aLife[i_life]))		   
	   {
	     for (var i_full=0; i_full<fullListName; i_full++)
		 {
		   if (arrObj[i-1][fullListName[i_full]] != arrObj[i][fullListName[i_full]])
		   {
				needDelete = false;
				break;			   
		   }
		 }
	     if (needDelete == false)
		 {
	 	   break;
		 }  
	   }
	 }
     if (needDelete)
	 {
       arrObj.splice( i-1, 1);
	 }  
   }	 
  }
}

function convertToTextarea(jqueryKey)
{  
$(jqueryKey).each(function(i)
{
  $(this).hide(); 
  $(this).parent().before('<textarea>'+ $(this).val() +'</textarea>');
});	
}

function getCurDate()
{ 
  var d = new Date();
  var off = 0;  
  d.setDate(d.getDate()+off);
  return (d.getDate()<10 ? "0":"") + d.getDate() + "." + ( d.getMonth()<9 ? "0":"")+(d.getMonth()+1)+"."+(d.getYear()+1900);
}

function saveMainJson() {
	$("#save").click();
	for (var name in jsonField) {
		var fieldVal = $("#"+name).val();
		if (typeof(fieldVal) == 'object'){
			jsonField[name] = JSON.parse(fieldVal);	
		}else
			jsonField[name] = fieldVal;
	}
	for (var i in newStepFields){
		var field = newStepFields[i];
		var val = $("#"+field).val();
		if (val == ''){
			alert("Заполните обязательные поля!");
			return null;
		}
		else
			jsonField[field] = $("#"+field).val(); 
	}
	return JSON.stringify(jsonField, null, 2); 
}
