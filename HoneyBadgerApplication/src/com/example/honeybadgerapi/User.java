package com.example.honeybadgerapi;


import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public abstract class User implements Parcelable{
	
	protected int loginStatus = 0; // 0 fails, 1 success
	protected boolean signUpStatus = true;
 
	public User() {}
	
    public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(loginStatus);
		dest.writeValue(signUpStatus);;
    } 
 
    protected User(Parcel in) {
		this.loginStatus = in.readInt();
		this.signUpStatus = (Boolean) in.readValue(null);
    } 
	
	
	public int getLoginStatus() {
		return loginStatus;
	}

	public boolean getSignUpStatus() {
		return signUpStatus;
	}
	
	public abstract void login(String username, String password);

	public abstract void setBalance(int code, double amount);

	public abstract double getBalance(int code);

	public abstract void setAccountCombo(int code);
	
	public abstract int getAccountCombo();

	public abstract boolean transfer(int accFrom, double amount, int accTo);

	public abstract boolean transfer(int accFrom, double amount, String phone_email);

	public abstract void updateAccountList();
	
	public abstract Account[] getAccountList();

	public abstract boolean closeAccount(int code);

	public abstract void updateUserInfo();
	
	public abstract boolean credit(int code, double d);

	public abstract boolean debit(int code, double d);

}
