package com.neosoft.task;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class POC_bank_version1 {
	public static void main(String[] args) {
		try {
			Scanner sc = new Scanner(System.in);
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
			String date;
			Map<String, Double> balanceSheet = new HashMap<String, Double>();
			Map<String, String> userPassList = new HashMap<String, String>();
			Map<String, String> accountList = new HashMap<String, String>();
			Map<String, String> userInfoList = new HashMap<String, String>();
			Iterator iterator;
			String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$";
			String regex_mobile = "[+]{1}[0-9]{2}[-]{1}\\d{10}";
			boolean condition = false, condition2 = false, doesUsernameExist = false, updateUser = false;
			int choice, choice2;

			double deposit, amount, payeeAmount, balance = 0, balancePayee;
			String name, address, username = "", payeeUsername, password = null, number = "";
			System.out.println("----------------");
			System.out.println("BANK OF  MYBANK");
			System.out.println("----------------");
			while (true) {
				updateUser = false;
				condition = false;
				condition2 = false;
				System.out.println("1. Register Account");
				System.out.println("2. Login");
				System.out.println("3. Update accounts");
				System.out.println("4. Exit");
				System.out.println("Enter your Choice: ");
				choice = sc.nextInt();
				System.out.println();
				switch (choice) {
				case 1:
					try {
						condition = false;
						sc.nextLine();
						System.out.println("Enter Name: ");
						name = sc.nextLine();
						System.out.println("Enter Address: ");
						address = sc.nextLine();
						System.out.println("Enter contact number");
						while (condition != true) {
							number = sc.next();
							if (number.equals("")) {
								System.out.println("Write a 10 digit number with country code Please: ");
							} else {
								if (Pattern.matches(regex_mobile, number)) {

									condition = true;
								} else {
									System.out.println(
											"PLEASE ENTER 10 DIGIT NUMBER WITH COUNTRY CODE- EXAMPLE: +91********** ");
								}
							}
						}
						condition = false;
						sc.nextLine();
						System.out.println("Set Username: ");
						username = sc.nextLine();
						System.out.println("Set a Password:( (minimum 8 chars; minimum 1 digit, 1 lowercase, 1 \r\n"
								+ "uppercase, 1 special character[!@#$%^&*_]) ) ");
						while (condition != true) {
							password = sc.nextLine();
							if (password.equals("")) {
								System.out.println("Write a Password Please: ");
							} else {
								if (Pattern.matches(regex, password)) {
									userPassList.put(username.toLowerCase(), password.toLowerCase());
									condition = true;
								} else {
									System.out.println("Invalid Password Condition.Set again: ");
								}
							}
						}
						System.out.println("Enter Initial Deposit: ");
						deposit = Double.parseDouble(sc.next());
						date = sdf.format(new Date());
						accountList.put(username,
								"Initial Deposit- Rs. " + deposit + " as on " + date + " " + java.time.LocalTime.now());
						balance = deposit;
						balanceSheet.put(username, balance);
						userInfoList.put(username, "Accountholder name: " + name + "\n" + "Accountholder Address: "
								+ address + "\n" + "Accountholder contact: " + number);
						System.out.println();
						break;
					} catch (Exception e) {
						System.out.println("Error Occured: " + e.getLocalizedMessage());
						break;
					}
				case 2:
					System.out.println("Enter UserName: ");
					username = sc.next();
					System.out.println("Enter Password: ");
					password = sc.next();
					if (userPassList.containsKey(username.toLowerCase())
							&& userPassList.containsValue(password.toLowerCase())) {
						while (condition2 != true) {
							doesUsernameExist = false;
							System.out.println("----------------------------");
							System.out.println("W E L C O M E");
							System.out.println("----------------------------");
							System.out.println("1. Deposit");
							System.out.println("2. Transfer");
							System.out.println("3. Last 5 Transactions");
							System.out.println("4. User Information");
							System.out.println("5. Log Out");
							System.out.println("Enter your Choice: ");
							choice2 = sc.nextInt();
							switch (choice2) {
							case 1:
								System.out.println("Enter Amount: ");
								amount = sc.nextDouble();
								date = sdf.format(new Date());
								balance = balance + amount;
								balanceSheet.put(username, balance);
								accountList.put(username,
										"Rs. " + amount + " credited to your account. Balance -Rs." + balance
												+ " as on " + date + " " + java.time.LocalTime.now() + "\n"
												+ accountList.get(username));
								break;
							case 2:
								System.out.println("Enter Payee Username: ");
								payeeUsername = sc.next();
								System.out.println("Enter Amount: ");
								payeeAmount = sc.nextDouble();
								balance = balanceSheet.get(username);
								iterator = userPassList.entrySet().iterator();

								while (iterator.hasNext()) {
									Map.Entry presentElement = (Map.Entry) iterator.next();
									if (presentElement.getKey().equals(payeeUsername)) {
										if (balance <= payeeAmount) {
											System.out.println("Sorry you have insufficent balance!!!");
											doesUsernameExist = true;
										} else {
											balance = balance - payeeAmount;
											date = sdf.format(new Date());
											System.out.println("Your Current Balance: " + balance + " as on " + date
													+ " " + java.time.LocalTime.now());
											balanceSheet.put(username, balance);
											accountList.put(username, "You have transferred: Rs. " + payeeAmount
													+ " to username: " + payeeUsername + " as on" + date + " "
													+ java.time.LocalTime.now() + "\n" + accountList.get(username));
											System.out.println("Money has been Transferred Successfully!!");
											balancePayee = balanceSheet.get(payeeUsername);
											balancePayee = balancePayee + payeeAmount;
											balanceSheet.put(payeeUsername, balancePayee);
											accountList.put(payeeUsername,
													"Your Current balance is: " + balancePayee + "\n"
															+ "You have received: Rs. " + payeeAmount
															+ " from username: " + username + " as on " + date + " "
															+ java.time.LocalTime.now() + "\n"
															+ accountList.get(payeeUsername));
											doesUsernameExist = true;
										}
									}
								}
								if (doesUsernameExist != true) {
									System.out.println("UserName doesn't exist");
								}
								break;
							case 3:
								date = sdf.format(new Date());
								balance = balanceSheet.get(username);
								System.out.println("Available-Balance: Rs. " + balance + "as on " + date + " "
										+ java.time.LocalTime.now() + "\n" + accountList.get(username));
								break;
							case 4:
								iterator = userInfoList.entrySet().iterator();
								while (iterator.hasNext()) {
									Map.Entry mapElement = (Map.Entry) iterator.next();
									if (mapElement.getKey().equals(username)) {
										System.out.println(mapElement.getValue());
									}
								}
								break;
							case 5:
								condition2 = true;
								break;
							default:
								System.out.println("Please enter a valid choice");
							}

						}
					} else {
						System.out.println("Sorry You dont have any Account.Please Register!!");
					}
					System.out.println();
					break;
				case 3:
					try {
						System.out.println("Enter your UserName: ");
						String username2 = sc.next();
						iterator = userInfoList.entrySet().iterator();
						while (iterator.hasNext()) {
							Map.Entry mapElement = (Map.Entry) iterator.next();
							if (mapElement.getKey().equals(username2)) {
								userInfoList.remove(username2);
								updateUser = true;
							}
						}
						if (updateUser == true) {
							condition = false;
							System.out.println("Enter New Name: ");
							name = sc.next();
							System.out.println("Enter New Address: ");
							address = sc.next();
							System.out.println("Enter New Contact: ");
							while (condition != true) {
								number = sc.next();
								if (number.equals("")) {
									System.out.println("Write a 10 digit number with country code Please: ");
								} else {
									if (Pattern.matches(regex_mobile, number)) {
										condition = true;
									} else {
										System.out.println(
												"PLEASE ENTER 10 DIGIT NUMBER WITH COUNTRY CODE- EXAMPLE: +91********** ");
									}
								}
							}
							userInfoList.put(username, "Accountholder name: " + name + "\n" + "Accountholder Address: "
									+ address + "\n" + "Accountholder contact: " + number);
						} else
							System.out.println("Username is not present");
						System.out.println();
						break;
					} catch (Exception e) {
						System.out.println("Some error occured while updating.");
					}
				case 4:
					System.exit(0);
					System.out.println();
					break;
				default:
					System.out.println("Please enter proper choice");
				}
			}
		} catch (Exception e) {
			System.out.println("Error Occured!!: " + e.getMessage());
		}
	}

}
