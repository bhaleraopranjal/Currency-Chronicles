
package com.pranjal.coins;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Year;
import java.util.*;

public class MainApp { 

	public static void main(String[] args) {
		
		System.out.println("\n+--------------------------------------------------------+"
						 + "\n|                  Currency Chronicles                   |"
						 + "\n+--------------------------------------------------------+");
		
		Scanner sc = new Scanner(System.in);
		
		List<Coin> coinList = new ArrayList<>();
		List<String> status = new ArrayList<>();
		int choice = 0;
		try 
		{
			// Step 1 : Load driver
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			// Step 2 : set connection with database
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/coin","root", "pranjal");
			
			String query = "select * from coin_details";
			
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next())
			{
				String country = rs.getString(2);
				String denomination = rs.getString(3);
				int yearValue = rs.getInt(4); // Column index for 'yearOfMining' as integer
	            Year yearOfMining = Year.of(yearValue);
	            double currentValue = rs.getDouble(5); // Column index for 'currentValue'
	            Date acquiredDate = rs.getDate(6); // Column index for 'acquiredDate'
	            
	            coinList.add(new Coin(country,denomination,yearOfMining,currentValue,acquiredDate));
	            status.add("Old");
			}
			
			while(choice != 7)
			{
				System.out.println("\n\t\t1.Add coin \n\t\t2.Display coin \n\t\t3.Update Coin "
								 + "\n\t\t4.Delete Coin \n\t\t5.Sort by current value \n\t\t6.Search coin \n\t\t7.Exit \nEnter your choice: ");
				choice = sc.nextInt();
				
				switch(choice)
				{
					case 1:						
						CoinOperations.addCoin(coinList, status);
						break;
						
					case 2:									
						CoinOperations.displayCoin(coinList);						
						break;
						
					case 3:												
						CoinOperations.displayCoin(coinList);
						CoinOperations.updateCoin(coinList,status);						
						break;
						
					case 4:						
						CoinOperations.deleteCoin(coinList, status);
						break;
						
					case 5:
						CoinOperations.sortByCurrentValue(coinList);
						break;
						
					case 6:
						CoinOperations.serchCoin(coinList);
						break;
						
					case 7:					
						CoinOperations.processDatabaseUpdates(coinList, status);					
						break;
						
					default:
						System.out.println("Invalid choice");
						break;
				}
			}								
		} 
		
		catch (ClassNotFoundException | SQLException e) 
		{
			e.printStackTrace();
		}	
		finally
		{  
			System.out.println("\n*----*----*----*----*----*----*----*----*Thank You*----*----*----*----*----*----*----*----*");
			sc.close();
		}
	}
}
