package com.neosoft.task;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@SuppressWarnings("unused")

interface Regex_pattern {
	default String regex_value() {
		return "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$";
	}

	default String regex_number_value() {
		return "[+]{1}[0-9]{2}[-]{1}\\d{10}";
	}
}

public class POC_bank_version2 implements Regex_pattern {

	@SuppressWarnings({ "finally", "resource", "unused" })
	public static void main(String[] args) throws ClassNotFoundException, FileNotFoundException, IOException {
		try {
			POC_bank_version2 poc2 = new POC_bank_version2();
			List<String> account_list = new ArrayList<String>();
			Scanner sc = new Scanner(System.in);
			List<String> choice_list1 = new ArrayList<String>(
					Arrays.asList("1. Register Account", "2. Login", "3. Update Accounts", "4. Exit"));
			List<String> choice_list2 = new ArrayList<String>(Arrays.asList("----------------------------",
					"W E L C O M E", "----------------------------", "1. Deposit", "2. Transfer",
					"3. Last 5 Transactions", "4. User Information", "5. Log Out"));
			Base64.Encoder password_encoder = Base64.getEncoder();
			Base64.Decoder password_decoder = Base64.getDecoder();
			String decoded_password = "";
			String encoded_password = "";
			String username2 = "", verification_password;
			Map<String, UserInformation> updatedMap = new HashMap<String, UserInformation>();
			ObjectOutputStream os = null, os2 = null, os3 = null;
			ObjectInputStream is = null, is2 = null, is3 = null;
			String file = "resources/userInfoList.txt";
			String file_password = "resources/passwords.txt";
			String file_balance = "resources/balance.txt";
			PrintWriter writer;
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
			String date;
			Map<String, Balance> balanceSheet = new HashMap<String, Balance>();
			Map<String, Passwords> userPassList = new HashMap<String, Passwords>();
			Map<String, String> accountList = new HashMap<String, String>();
			Map<String, UserInformation> userInfoList = new HashMap<String, UserInformation>();
			@SuppressWarnings("rawtypes")
			Iterator iterator;
			String regex, regexNumber;
			boolean subCondition = false, condition = false, condition2 = false, doesUsernameExist = false,
					updateUser = false, mainCondition = false;
			int choice, choice2;
			double deposit, amount, payeeAmount, balance = 0, balancePayee = 0;
			String name, number, address, username = "", payeeUsername, password = null;
			System.out.println("----------------");
			System.out.println("BANK OF  MYBANK");
			System.out.println("----------------");

			while (true) {
				updateUser = false;
				condition = false;
				condition2 = false;
				choice_list1.stream().forEach(System.out::println);
				System.out.println("Enter your Choice: ");
				choice = sc.nextInt();
				System.out.println();
				switch (choice) {
				case 1:
					try {
						sc.nextLine();
						System.out.println("Enter Name: ");
						name = sc.next();
						System.out.println("Enter Address: ");
						address = sc.next();
						System.out.println("Enter contact number");
						number = sc.next();
						regexNumber = poc2.regex_number_value();
						if (Pattern.matches(regexNumber, number)) {
						} else {
							System.out.println("Please put country code and 10 digit number!! example- +91-**********");
							number = sc.next();
							if (Pattern.matches(regexNumber, number)) {
							} else {
								break;
							}
						}
						sc.nextLine();
						System.out.println("Set Username: ");
						username = sc.next();
						System.out.println("Set a Password:( (minimum 8 chars; minimum 1 digit, 1 lowercase, 1 \r\n"
								+ "uppercase, 1 special character[!@#$%^&*_]) ) ");
						while (condition != true) {
							System.out.println();
							password = sc.next();
							if (password.equals("")) {
								System.out.println("Write a Password Please: ");
							} else {
								regex = poc2.regex_value();
								if (Pattern.matches(regex, password)) {
									encoded_password = password_encoder.encodeToString(password.getBytes());
									userPassList.put(username.toLowerCase(), new Passwords(encoded_password));
									try {
										os2 = new ObjectOutputStream(new FileOutputStream(file_password));
										os2.writeObject(userPassList);
									} catch (Exception e) {
										e.printStackTrace();
									} finally {
										if (os2 != null)
											os2.close();
									}
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
						balanceSheet.put(username, new Balance(balance));
						os3 = new ObjectOutputStream(new FileOutputStream(file_balance));
						os3.writeObject(balanceSheet);
						userInfoList.put(username, new UserInformation(name, address, number));
						os = new ObjectOutputStream(new FileOutputStream(file));
						os.writeObject(userInfoList);
						System.out.println();
						break;
					} catch (Exception e) {
						System.out.println("Error Occured: " + e.getLocalizedMessage());
						break;
					} finally {
						try {
							if (os != null)
								os.close();
							if (os3 != null)
								os3.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				case 2:
					System.out.println("Enter UserName: ");
					username = sc.next();
					System.out.println("Enter Password: ");
					password = sc.next();

					is2 = new ObjectInputStream(new FileInputStream(file_password));
					try {
						while (true) {
							@SuppressWarnings("unchecked")
							Map<String, Passwords> pass_map = (Map<String, Passwords>) is2.readObject();
							for (Map.Entry<String, Passwords> entry : pass_map.entrySet()) {
								if (entry.getKey().equals(username)) {
									verification_password = entry.getValue().password;
									decoded_password = new String(password_decoder.decode(verification_password));
									System.out.println(decoded_password);
								}
							}
						}
					} catch (EOFException eof) {
						System.out.println("UserName and Passwords tested!!!");
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						if (is2 != null)
							is2.close();
					}

					if (decoded_password.equals(password)) {
						while (condition2 != true) {
							doesUsernameExist = false;
							choice_list2.stream().forEach(System.out::println);
							System.out.println("Enter your Choice: ");
							choice2 = sc.nextInt();
							switch (choice2) {
							case 1:
								System.out.println("Enter Amount: ");
								amount = sc.nextDouble();
								date = sdf.format(new Date());
								is3 = new ObjectInputStream(new FileInputStream(file_balance));

								@SuppressWarnings("unchecked")
								Map<String, Balance> map_balance = (Map<String, Balance>) is3.readObject();
								for (Map.Entry<String, Balance> entry : map_balance.entrySet()) {
									if (entry.getKey().equals(username)) {
										balance = entry.getValue().balance;
									}
								}
								balance = balance + amount;
								balanceSheet.put(username, new Balance(balance));
								writer = new PrintWriter(file_balance);
								writer.print("");
								writer.close();
								os3 = new ObjectOutputStream(new FileOutputStream(file_balance));
								os3.writeObject(balanceSheet);
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
								is3 = new ObjectInputStream(new FileInputStream(file_balance));
								try {
									@SuppressWarnings("unchecked")
									Map<String, Balance> balance_map = (Map<String, Balance>) is3.readObject();
									for (Map.Entry<String, Balance> entry : balance_map.entrySet()) {
										if (entry.getKey().equals(username)) {
											balance = entry.getValue().balance;
										}
									}
								} catch (EOFException eof) {
									System.out.println("");
								} catch (Exception e) {
									e.printStackTrace();
								} finally {
									if (is3 != null) {
										is3.close();
									}
								}
								iterator = userPassList.entrySet().iterator();
								while (iterator.hasNext()) {
									@SuppressWarnings("rawtypes")
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
											balanceSheet.put(username, new Balance(balance));
											writer = new PrintWriter(file_balance);
											writer.print("");
											writer.close();
											os3 = new ObjectOutputStream(new FileOutputStream(file_balance));
											os3.writeObject(balanceSheet);
											accountList.put(username, "You have transferred: Rs. " + payeeAmount
													+ " to username: " + payeeUsername + " as on" + date + " "
													+ java.time.LocalTime.now() + "\n" + accountList.get(username));
											System.out.println("Money has been Transferred Successfully!!");
											is3 = new ObjectInputStream(new FileInputStream(file_balance));
											try {
												@SuppressWarnings("unchecked")
												Map<String, Balance> balance_map = (Map<String, Balance>) is3
														.readObject();
												for (Map.Entry<String, Balance> entry : balance_map.entrySet()) {
													if (entry.getKey().equals(payeeUsername)) {
														balancePayee = entry.getValue().balance;
													}
												}
											} catch (EOFException eof) {
												System.out.println("");
											} catch (Exception e) {
												e.printStackTrace();
											} finally {
												if (is3 != null) {
													is3.close();
												}
											}
											balancePayee = balancePayee + payeeAmount;
											balanceSheet.put(payeeUsername, new Balance(balancePayee));
											writer = new PrintWriter(file_balance);
											writer.print("");
											writer.close();
											os3 = new ObjectOutputStream(new FileOutputStream(file_balance));
											os3.writeObject(balanceSheet);

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
								is3 = new ObjectInputStream(new FileInputStream(file_balance));
								try {
									while (true) {
										@SuppressWarnings("unchecked")
										Map<String, Balance> balance_map = (Map<String, Balance>) is3.readObject();
										for (Map.Entry<String, Balance> entry : balance_map.entrySet()) {
											if (entry.getKey().equals(username)) {
												balance = entry.getValue().balance;
											}
										}
									}
								} catch (EOFException eof) {
									System.out.println("");
								} catch (Exception e) {
									e.printStackTrace();
								} finally {
									if (is3 != null) {
										is3.close();
									}
								}
								System.out.println("Available-Balance: Rs. " + balance + "as on " + date + " "
										+ java.time.LocalTime.now() + "\n" + accountList.get(username));
								break;
							case 4:
								try {
									is = new ObjectInputStream(new FileInputStream(file));
									while (true) {
										@SuppressWarnings("unchecked")
										Map<String, UserInformation> map_list = (Map<String, UserInformation>) is
												.readObject();
										for (Map.Entry<String, UserInformation> map_userInfo : map_list.entrySet()) {
											if (map_userInfo.getKey().equals(username)) {
												System.out.println(map_userInfo.getValue().name + "\n"
														+ map_userInfo.getValue().address + "\n"
														+ map_userInfo.getValue().number + "\n");
											}
										}

									}

								} catch (EOFException eof) {
									System.out.println("Information Traced!!!");

								} catch (FileNotFoundException e) {
									e.printStackTrace();
								} catch (IOException e) {
									e.printStackTrace();
								} catch (Exception e) {
									System.out.println("Null reference!");
								} finally {
									if (is != null) {
										is.close();
									}
									break;
								}
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
						username2 = sc.next();
						is = new ObjectInputStream(new FileInputStream(file));
						try {
							while (true) {
								@SuppressWarnings("unchecked")
								Map<String, UserInformation> map = (Map<String, UserInformation>) is.readObject();

								for (Map.Entry<String, UserInformation> entry : map.entrySet()) {

									if (entry.getKey().equals(username2)) {
										updateUser = true;
									} else {
										updatedMap.put(entry.getKey(), entry.getValue());
									}
								}
							}
						} catch (EOFException eof) {
							System.out.println("Username is correct!!");
						} catch (Exception e) {
							e.printStackTrace();
						}

					} catch (Exception e) {
						System.out.println("Some error occured while updating.");
					} finally {
						writer = new PrintWriter(file);
						writer.print("");
						writer.close();
						if (updateUser == true) {
							System.out.println("Enter New Name: ");
							name = sc.next();
							System.out.println("Enter New Address: ");
							address = sc.next();
							System.out.println("Enter New Contact: ");
							number = sc.next();
							userInfoList.remove(username2);
							userInfoList.put(username2, new UserInformation(name, address, number));
							os = new ObjectOutputStream(new FileOutputStream(file));
							os.writeObject(userInfoList);
							os.writeObject(updatedMap);
						} else
							System.out.println("Username is not present");
						break;
					}
				case 4:
					mainCondition = true;
					System.exit(0);
					break;

				default:
					System.out.println("Please enter proper choice");
				}
			}
		} catch (Exception e) {
			System.out.println("Error Occured: " + e.getMessage());
		}
	}
}
