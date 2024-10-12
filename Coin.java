
package com.pranjal.coins;

import java.sql.Date;
import java.time.Year;

public class Coin {

	static int nextId = 1;
	int id;
	String country;
	String denomination;
	Year yearOfMining;
	double currentValue;
	Date acquiredDate;
	
	public Coin() {
		
	}
	
	public Coin(String country, String denomination, Year yearOfMining, double currentValue, Date acquiredDate) {
		super();
		this.id = nextId++;
		this.country = country;
		this.denomination = denomination;
		this.yearOfMining = yearOfMining;
		this.currentValue = currentValue;
		this.acquiredDate = acquiredDate;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getDenomination() {
		return denomination;
	}

	public void setDenomination(String denomination) {
		this.denomination = denomination;
	}

	public Year getYearOfMining() {
		return yearOfMining;
	}

	public void setYearOfMining(Year yearOfMining) {
		this.yearOfMining = yearOfMining;
	}

	public double getCurrentValue() {
		return currentValue;
	}

	public void setCurrentValue(double currentValue) {
		this.currentValue = currentValue;
	}

	public Date getAcquiredDate() {
		return acquiredDate;
	}

	public void setAcquiredDate(Date acquiredDate) {
		this.acquiredDate = acquiredDate;
	}

	@Override
	public String toString() {
	    // Define the row content
	    String rowContent = String.format("| %-14d | %-15s | %-17s | %-16s | %-17.2f | %-18s |\n",
	        this.id, this.country, this.denomination, this.yearOfMining, this.currentValue, this.acquiredDate
	    );

	    return rowContent + "+----------------+-----------------+-------------------+------------------+-------------------+--------------------+";
	}
}

