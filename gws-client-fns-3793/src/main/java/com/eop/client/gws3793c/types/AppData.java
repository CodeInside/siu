package com.eop.client.gws3793c.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "AppData")
@XmlType(name = "AppData")
public class AppData {
	
	@XmlElement(name = "Документ", namespace="http://ws.unisoft/FNSZDL/Rq1", type=unisoft.ws.fnszdl.rq1.Документ.class, required=false)
	protected unisoft.ws.fnszdl.rq1.Документ документПервичныйЗапрос;
	
	@XmlElement(name = "Документ", namespace = "http://ws.unisoft/FNSZDL/Rs1", type=unisoft.ws.fnszdl.rs1.Документ.class, required=false)
	protected unisoft.ws.fnszdl.rs1.Документ документПервичныйОтвет;
	
	@XmlElement(name = "Документ", namespace="http://ws.unisoft/FNSZDL/Rq2", type=unisoft.ws.fnszdl.rq2.Документ.class, required=false)
	protected unisoft.ws.fnszdl.rq2.Документ документВторичныйЗапрос;
	
	@XmlElement(name = "Документ", namespace="http://ws.unisoft/FNSZDL/Rs2", type=unisoft.ws.fnszdl.rs2.Документ.class, required=false)
	protected unisoft.ws.fnszdl.rs2.Документ документВторичныйОтвет;

	public unisoft.ws.fnszdl.rq1.Документ getДокументПервичныйЗапрос() {
		return документПервичныйЗапрос;
	}

	public void setДокументПервичныйЗапрос(
			unisoft.ws.fnszdl.rq1.Документ документПервичныйЗапрос) {
		this.документПервичныйЗапрос = документПервичныйЗапрос;
	}

	public unisoft.ws.fnszdl.rs1.Документ getДокументПервичныйОтвет() {
		return документПервичныйОтвет;
	}

	public void setДокументПервичныйОтвет(
			unisoft.ws.fnszdl.rs1.Документ документПервичныйОтвет) {
		this.документПервичныйОтвет = документПервичныйОтвет;
	}

	public unisoft.ws.fnszdl.rq2.Документ getДокументВторичныйЗапрос() {
		return документВторичныйЗапрос;
	}

	public void setДокументВторичныйЗапрос(
			unisoft.ws.fnszdl.rq2.Документ документВторичныйЗапрос) {
		this.документВторичныйЗапрос = документВторичныйЗапрос;
	}

	public unisoft.ws.fnszdl.rs2.Документ getДокументВторичныйОтвет() {
		return документВторичныйОтвет;
	}

	public void setДокументВторичныйОтвет(
			unisoft.ws.fnszdl.rs2.Документ документВторичныйОтвет) {
		this.документВторичныйОтвет = документВторичныйОтвет;
	}


}
