package com.andreiolar.abms.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class PersonalUpkeepInformation implements IsSerializable {

	private String aptNumber;
	private String spatiuComun;
	private String suprafataApt;
	private String incalzire;
	private String apaCaldaMenajera;
	private String apaReceSiCanalizare;
	private String numarPersoane;
	private String gunoi;
	private String curent;
	private String gaz;
	private String servicii;
	private String gospodaresti;
	private String nume;
	private String costTotal;
	private String luna;

	public PersonalUpkeepInformation() {
	}

	public PersonalUpkeepInformation(String aptNumber, String spatiuComun, String suprafataApt, String incalzire, String apaCaldaMenajera,
			String apaReceSiCanalizare, String numarPersoane, String gunoi, String curent, String gaz, String servicii, String gospodaresti,
			String nume, String costTotal, String luna) {
		this.aptNumber = aptNumber;
		this.spatiuComun = spatiuComun;
		this.suprafataApt = suprafataApt;
		this.incalzire = incalzire;
		this.apaCaldaMenajera = apaCaldaMenajera;
		this.apaReceSiCanalizare = apaReceSiCanalizare;
		this.numarPersoane = numarPersoane;
		this.gunoi = gunoi;
		this.curent = curent;
		this.gaz = gaz;
		this.servicii = servicii;
		this.gospodaresti = gospodaresti;
		this.nume = nume;
		this.costTotal = costTotal;
		this.luna = luna;
	}

	public String getAptNumber() {
		return aptNumber;
	}

	public String getSpatiuComun() {
		return spatiuComun;
	}

	public String getSuprafataApt() {
		return suprafataApt;
	}

	public String getIncalzire() {
		return incalzire;
	}

	public String getApaCaldaMenajera() {
		return apaCaldaMenajera;
	}

	public String getApaReceSiCanalizare() {
		return apaReceSiCanalizare;
	}

	public String getNumarPersoane() {
		return numarPersoane;
	}

	public String getGunoi() {
		return gunoi;
	}

	public String getCurent() {
		return curent;
	}

	public String getGaz() {
		return gaz;
	}

	public String getServicii() {
		return servicii;
	}

	public String getGospodaresti() {
		return gospodaresti;
	}

	public String getNume() {
		return nume;
	}

	public String getCostTotal() {
		return costTotal;
	}

	public void setAptNumber(String aptNumber) {
		this.aptNumber = aptNumber;
	}

	public void setSpatiuComun(String spatiuComun) {
		this.spatiuComun = spatiuComun;
	}

	public void setSuprafataApt(String suprafataApt) {
		this.suprafataApt = suprafataApt;
	}

	public void setIncalzire(String incalzire) {
		this.incalzire = incalzire;
	}

	public void setApaCaldaMenajera(String apaCaldaMenajera) {
		this.apaCaldaMenajera = apaCaldaMenajera;
	}

	public void setApaReceSiCanalizare(String apaReceSiCanalizare) {
		this.apaReceSiCanalizare = apaReceSiCanalizare;
	}

	public void setNumarPersoane(String numarPersoane) {
		this.numarPersoane = numarPersoane;
	}

	public void setGunoi(String gunoi) {
		this.gunoi = gunoi;
	}

	public void setCurent(String curent) {
		this.curent = curent;
	}

	public void setGaz(String gaz) {
		this.gaz = gaz;
	}

	public void setServicii(String servicii) {
		this.servicii = servicii;
	}

	public void setGospodaresti(String gospodaresti) {
		this.gospodaresti = gospodaresti;
	}

	public void setNume(String nume) {
		this.nume = nume;
	}

	public void setCostTotal(String costTotal) {
		this.costTotal = costTotal;
	}

	public String getLuna() {
		return luna;
	}

	public void setLuna(String luna) {
		this.luna = luna;
	}

}
