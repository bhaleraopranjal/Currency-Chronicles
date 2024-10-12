package com.pranjal.coins;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class CoinOperations {
	
	static Scanner co = new Scanner(System.in);
	
	
	public static void addCoin(List<Coin> coinList, List<String> status) {

		System.out.println("\nHow you want to add coins in collection "
						 + "\n\t\t1.Add single coin \n\t\t2.Add coin in bulk" 
						 +	"\nEnter your choice: ");
		
		int ans = co.nextInt();
		co.nextLine(); 
		
		if(ans == 1)
		{
			System.out.println("Enter country: ");
			String country = co.nextLine();
			
			System.out.println("Enter denomination: ");
			String denomination = co.nextLine();
			
			System.out.println("Enter year of mining: ");
			int yearValue =	co.nextInt(); 
	        Year yearOfMining = Year.of(yearValue);
	        
	        System.out.println("Enter current value: ");
	        double currentValue = co.nextDouble();
	        co.nextLine();
	        
	        System.out.println("Enter acquired date: ");
	        Date acquiredDate = Date.valueOf(co.nextLine());
	        
	        coinList.add(new Coin(country,denomination,yearOfMining,currentValue,acquiredDate));
	        status.add("New");
		}
		else if(ans == 2)
		{			
			System.out.println("Enter csv file you want to insert: ");
			String fileName = co.nextLine();
			
			try 
			{
				Scanner file = new Scanner(new File(fileName));
				while(file.hasNextLine())
				{
					String line = file.nextLine();
					String[] details = line.split(",");
					
					String country = details[0];
				    String denomination = details[1];
				    int yearValue = Integer.parseInt(details[2]);
				    Year yearOfMining = Year.of(yearValue);
				    double currentValue = Double.parseDouble(details[3]);
				    Date acquiredDate = Date.valueOf(details[4]);

				    coinList.add(new Coin(country, denomination, yearOfMining, currentValue, acquiredDate));
				    status.add("New");
				}
				file.close();
			} 
			catch (NumberFormatException | FileNotFoundException e) 
			{
				e.printStackTrace();
			}
		}
		else
		{
			System.out.println("Invalid choice");
		}
	}
	
	public static void displayCoin(List<Coin> coinList) {
		
		System.out.print("+----------------+-----------------+-------------------+------------------+-------------------+--------------------+\n");
        System.out.print("|       Id       |     Country     |     Denomination    |  Year of Mining  |   Current Value   |    Acquired Date   |\n");
        System.out.print("+----------------+-----------------+-------------------+------------------+-------------------+--------------------+\n");	    
		
		for(Coin c : coinList)
			System.out.println(c);	
	}
	
	public static void updateCoin(List<Coin> coinList, List<String> status) {
		
		System.out.println("Enter coin id whose data you want to update: ");
		int coin_id = co.nextInt();
		
		int index = CoinOperations.searchCoinId(coinList, coin_id);		
		if(index == -1)
		{
			System.out.println("Coin id not in list");
		}		
		else
		{
			if( status.get(index).equals("Old") )
			{
				int ch = 0;
				while(ch != 6)
				{
					System.out.println("\n\t\t1.Update country \n\t\t2.Update denomination" 
									 + "\n\t\t3.Update year of mining \n\t\t4.Update current value"
									 + "\n\t\t5.Update acquired date \n\t\t6.Exit \nEnter your choice: ");					
					ch = co.nextInt();	
					co.nextLine();
					switch(ch)
					{
						case 1:
							System.out.println("Enter country name you want to update: ");
							String upCountry = co.nextLine();
							
							coinList.get(index).setCountry(upCountry);
							status.set(index, "Update");
							System.out.println("Updated successfully");
							break;
							
						case 2:
							System.out.println("Enter denomination you want to update: ");
							String upDenomination = co.nextLine();
							
							coinList.get(index).setDenomination(upDenomination);
							status.set(index, "Update");
							System.out.println("Updated successfully");
							break;
							
						case 3:
							System.out.println("Enter year of mining you want to update: ");
							int yearValue =	co.nextInt(); 
							Year upYearOfMining = Year.of(yearValue);
							
							coinList.get(index).setYearOfMining(upYearOfMining);
							status.set(index, "Update");
							System.out.println("Updated successfully");
							break;
							
						case 4:
							System.out.println("Enter current value you want to update: ");
							double upCurrentValue =	co.nextDouble(); 
							
							coinList.get(index).setCurrentValue(upCurrentValue);
							status.set(index, "Update");
							System.out.println("Updated successfully");
							break;
							
						case 5:
							System.out.println("Enter acquired date you want to update: ");
							Date upAcquiredYear =	Date.valueOf(co.nextLine());											
							
							coinList.get(index).setAcquiredDate(upAcquiredYear);
							status.set(index, "Update");
							System.out.println("Updated successfully");
							break;
					
						case 6:
							System.out.println("------Exiting from update-------");
							break;
							
						default:
							System.out.println("Invalid choice");
							break;					
					}										
				}
			}								
		}
	}

	public static void processDatabaseUpdates(List<Coin> coinList, List<String> status) {
        String query;
        PreparedStatement ps;
        
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/coin", "root", "pranjal")) 
        {
            for (int ctr = 0; ctr < status.size(); ctr++) 
            {
                String ch = status.get(ctr);
                
                if (!ch.equals("Old") && !ch.equals("New") && !ch.equals("Update") && !ch.equals("Delete")) 
                {
                    System.out.println("Invalid Status\n*----*----*----*----*----*----*----*----*Thank You*----*----*----*----*----*----*----*----*");
                } 
                else 
                {
                    switch (ch) 
                    {
                        case "Old":
//                            System.out.println("Data already exists");
                            break;
                            
                        case "Delete":
                            query = "delete from coin_details where Id = ?";
                            ps = conn.prepareStatement(query);
                            ps.setInt(1, coinList.get(ctr).getId());
                            ps.executeUpdate();
                            break;
                            
                        case "Update":
                            query = "update coin_details set country = ?, denomination = ?, year_Of_Mining = ?, current_Value = ?, acquired_Date = ? where Id = ?";
                            ps = conn.prepareStatement(query);
                            
                            ps.setString(1, coinList.get(ctr).getCountry());
                            ps.setString(2, coinList.get(ctr).getDenomination());
                            ps.setInt(3, coinList.get(ctr).getYearOfMining().getValue());
                            ps.setDouble(4, coinList.get(ctr).getCurrentValue());
                            ps.setDate(5, coinList.get(ctr).getAcquiredDate());
                            ps.setInt(6, coinList.get(ctr).getId());
                            
                            ps.executeUpdate();
                            System.out.println("Coin updated successfully.");
                            break;
                            
                        case "New":
                            query = "insert into coin_details values (?,?,?,?,?,?)";
                            ps = conn.prepareStatement(query);
                            
                            ps.setInt(1, coinList.get(ctr).getId());
                            ps.setString(2, coinList.get(ctr).getCountry());
                            ps.setString(3, coinList.get(ctr).getDenomination());
                            ps.setInt(4, coinList.get(ctr).getYearOfMining().getValue());
                            ps.setDouble(5, coinList.get(ctr).getCurrentValue());
                            ps.setDate(6, coinList.get(ctr).getAcquiredDate());
                            
                            ps.executeUpdate();
                            System.out.println("New coin added successfully.");
                            break;
                    }
                }
            }
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }
    
    public static void deleteCoin(List<Coin> coinList, List<String> status) {
		
		CoinOperations.displayCoin(coinList);		
	
		System.out.println("Enter coin id you want to delete: ");
		int id = co.nextInt();
		
		int index = CoinOperations.searchCoinId(coinList, id);
		if(index == -1)
		{
			System.out.println("Coin id not in list");
		}
		else
		{
			if(status.get(index) == "New")
			{
				coinList.remove(index);
				status.remove(index);
			}
			status.set(index, "Delete");			
			System.out.println("Deleted successfully");
		}
	}
    
    public static int searchCoinId(List<Coin> coinList, int id) {

		for(int index = 0; index < coinList.size(); index++)
		{
			if(id == coinList.get(index).getId())
				return index;
		}
		return -1;
	}

	public static void sortByCurrentValue(List<Coin> coinList) {
		
		System.out.println("\n\t\t1.Sort Ascendingly \n\t\t2.Sort Descendingly \nEnter your choice: ");
		int ch = co.nextInt();
		
		List<Coin> newCoinList = new ArrayList<>(coinList);
	
		switch(ch)
		{
			case 1:
				newCoinList.sort(Comparator.comparingDouble(Coin::getCurrentValue));
				List<Coin> newCoinListSorted = coinList.stream()
								               .sorted(Comparator.comparingDouble(Coin::getCurrentValue))
								               .collect(Collectors.toList());
			    System.out.println("Coins sorted by current value:");
			    CoinOperations.displayCoin(newCoinListSorted);
				 
				break;
			
			case 2:
				newCoinList.sort(Comparator.comparingDouble(Coin::getCurrentValue));
				List<Coin> newCoinListSortedDesc = coinList.stream()
									               .sorted(Comparator.comparingDouble(Coin::getCurrentValue).reversed())
									               .collect(Collectors.toList());
				System.out.println("Coins sorted by current value:");
				CoinOperations.displayCoin(newCoinListSortedDesc);
				break;	
			
			default:
				System.out.println("Invalid choice");
				break;
		}		
	}

	public static void serchCoin(List<Coin> coinList) {
		
		System.out.println("\n\t\t1.Search by Country + Denomination \n\t\t2.Search by Country + Year of Mining"
						 + "\n\t\t3.Search by Country + Denomination + Year of Mining \n\t\t4.Search by Acquired Date + Country "
						 + "\nEnter your choice: ");
		
		int ans = co.nextInt();
		co.nextLine();
		
		int index;
		
		switch(ans) 
		{
			case 1:
				index = searchByCountryDenomination(coinList);
				System.out.println( (index == -1) ? "\nCoin not found" : "\nCoin found at index " + index );
				break;
				
			case 2:
				index = searchByCountryYearOfMining(coinList);
				System.out.println( (index == -1) ? "\nCoin not found" : "\nCoin found at index " + index );
				break;
				
			case 3:
				index = searchByCountryDenominationYearOfMining(coinList);
				System.out.println( (index == -1) ? "\nCoin not found" : "\nCoin found at index " + index );
				break;
				
			case 4:
				index = searchByAcquiredDateCountry(coinList);
				System.out.println( (index == -1) ? "\nCoin not found" : "\nCoin found at index " + index );
				break;
				
			default:
				System.out.println("Invalid choice");
				break;
		}
	}

	private static int searchByCountryDenomination(List<Coin> coinList) {

		System.out.println("Enter country you want to search: ");
		String searchCountry = co.nextLine();
		
		System.out.println("Enter denomination you want to search: ");
		String searchDenomination = co.nextLine();
		
		for(int index = 0; index < coinList.size(); index++)
		{
			if(coinList.get(index).country.equalsIgnoreCase(searchCountry) && 
					coinList.get(index).denomination.equalsIgnoreCase(searchDenomination))
			{
				return index;
			}
		}
		return -1;
		
	}
  
	private static int searchByCountryYearOfMining(List<Coin> coinList) {

		System.out.println("Enter country you want to search: ");
		String searchCountry = co.nextLine();
		
		System.out.println("Enter year of mining you want to search: ");
		int searchYear = co.nextInt();
		
		for(int index = 0; index < coinList.size(); index++)
		{
			if(coinList.get(index).country.equalsIgnoreCase(searchCountry) && 
					coinList.get(index).yearOfMining.getValue() == searchYear)
			{
				return index;
			}
		}
		return -1;
	}
	
	private static int searchByCountryDenominationYearOfMining(List<Coin> coinList) {
		
		System.out.println("Enter country you want to search: ");
		String searchCountry = co.nextLine();
		
		System.out.println("Enter denomination you want to search: ");
		String searchDenomination = co.nextLine();
		
		System.out.println("Enter year of mining you want to search: ");
		int searchYear = co.nextInt();
		
		for(int index = 0; index < coinList.size(); index++)
		{
			if(coinList.get(index).country.equalsIgnoreCase(searchCountry) && 
			   coinList.get(index).denomination.equalsIgnoreCase(searchDenomination) &&
			   coinList.get(index).yearOfMining.getValue() == searchYear)
			{
				return index;
			}
		}
		return -1;
	}
	
	private static int searchByAcquiredDateCountry(List<Coin> coinList) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		System.out.println("Enter country you want to search: ");
		String searchCountry = co.nextLine();
		
		System.out.println("Enter acquired date you want to search: ");
		String searchDateStr = co.nextLine();
		
		java.util.Date searchDateUtil = null;
	    try 
	    {
	        searchDateUtil = sdf.parse(searchDateStr);
	    } 
	    catch (ParseException e) 
	    {
	        System.out.println("Invalid date format. Please use yyyy-MM-dd.");
	        return -1;
	    }
	    
	    Date searchDateSql = new Date(searchDateUtil.getTime());
	    
		for(int index = 0; index < coinList.size(); index++)
		{
			if(coinList.get(index).country.equalsIgnoreCase(searchCountry) && coinList.get(index).acquiredDate.equals(searchDateSql))
			{
				return index;
			}
		}
		return -1;
	}
	
}
