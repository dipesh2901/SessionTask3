package com.neosoft.programs;
import java.util.*;

import java.io.FileNotFoundException;
import java.io.FileReader;

import javax.script.Bindings;
import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class Program21 {

	public static void main(String[] args) throws FileNotFoundException, ScriptException, NoSuchMethodException {
		Scanner sc=new Scanner(System.in);
		int num1,num2;
		ScriptEngine engine=new ScriptEngineManager().getEngineByName("nashorn");
		engine.eval(new FileReader("resources/calci.js"));
		System.out.println("Enter first number:");
		num1=sc.nextInt();
		System.out.println("Enter second number: ");
		num2=sc.nextInt();
		Invocable invocable=(Invocable) engine;
		invocable.invokeFunction("add",num1, num2);
		invocable.invokeFunction("sub",num1, num2);
		invocable.invokeFunction("mul",num1, num2);
		invocable.invokeFunction("div",num1, num2);
	}

}
