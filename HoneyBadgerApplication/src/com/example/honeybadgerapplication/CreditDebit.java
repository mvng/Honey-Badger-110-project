package com.example.honeybadgerapplication;

import java.util.ArrayList;
import java.util.List;

import com.example.honeybadgerapi.Teller;
import com.example.honeybadgerapi.User;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class CreditDebit extends ActionBarActivity {
	private Spinner accountSpinner;
	private Spinner actionSpinner;		// Select credit or debit
	private EditText amount_edit_text;
	//Strings of existing User accounts
	private List<String> accStrings = new ArrayList<String>();
	//String Array Adapter of User accounts
	private ArrayAdapter<String> accStringsAdapt;
	private String action;	
	private String accountSelected;
	private double amountRequested = 0.0;	// Amount to credit/debit
	private User customer;
	private Teller teller;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_credit_debit); // Load credit/debit view
		// Intent to enter view where Teller can access Customer Info
		final Intent intentCustomerInfo = new Intent(CreditDebit.this, TellerCustomerInfo.class);
		// Intent to reenter this view
		final Intent intentCreditDebit = new Intent(CreditDebit.this,
				CreditDebit.class);
		// Buttons to confirm or cancel debit/credit
		final Button confirmButton = (Button) findViewById(R.id.conf_cred);
		final Button cancelButton = (Button) findViewById(R.id.cancel_cred);
		
		// Grab bundle from previous view and attempt to get Teller object
		Bundle tellerBundle = this.getIntent().getExtras();
		if( tellerBundle != null)
			teller = tellerBundle.getParcelable("user");
		else{
			startActivity( intentCustomerInfo );
		}
		// Grab the Customer object being accessed by the Teller
		customer = teller.getCustomer();
		
		//Finds which user accounts exist (checking or saving)
		int aCombo = customer.getAccountCombo();
		switch(aCombo) {
		    case 1: //just checking account
		    	accStrings.add("Checking Account");
			    break;
		    case 2: //just saving account
		    	accStrings.add("Savings Account");
			    break;
		    case 3: //both accounts
		    	accStrings.add("Checking Account");
		    	accStrings.add("Savings Account");
			    break;
		}
		
		accountSpinner = (Spinner) findViewById(R.id.account_num);
		// Select whether to credit or debit
		actionSpinner = (Spinner) findViewById(R.id.action);
		// Amount to credit or debit
		amount_edit_text = (EditText) findViewById(R.id.amount_cred);
		
		//Creates Spinner
		accStringsAdapt = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, accStrings);
		accStringsAdapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		accountSpinner.setAdapter(accStringsAdapt);
	
		ArrayAdapter<CharSequence> actionAdapt = ArrayAdapter
				.createFromResource(this, R.array.cred_or_deb,
						android.R.layout.simple_spinner_item);
		actionAdapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		actionSpinner.setAdapter(actionAdapt);
		
		// Set click listener for confirm button
		confirmButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// Account to credit/debit
				accountSelected = accountSpinner.getSelectedItem().toString().trim();
				// Credit / Debit selection
				action = actionSpinner.getSelectedItem().toString().trim();
				// Amount wanted
				amountRequested = Double.parseDouble(amount_edit_text.getText()
						.toString().trim());
				
				if(action.equals("Debit")){
					if( accountSelected.equals("Checking Account")){
						// If amount to debit > account balance
						if( customer.debit( 1, amountRequested) == false ){
							// Rebundle Teller to send to next view
							Bundle nextBundle = new Bundle();
							nextBundle.putParcelable("user", teller);
							intentCreditDebit.putExtra("user", teller);
							
							// Error: Insufficient funds
							Toast.makeText(getApplicationContext(),
							"Insufficient funds to debit", Toast.LENGTH_SHORT)
							.show();
							// Reenter this credit/debit view
							startActivity( intentCreditDebit );	
						}
					}
					else if( accountSelected.equals("Savings Account")){
						// If amount to debit > account balance
						if( customer.debit( 2, amountRequested) == false ){
							// Rebundle Teller to send to next view
							Bundle nextBundle = new Bundle();
							nextBundle.putParcelable("user", teller);
							intentCreditDebit.putExtra("user", teller);
							
							// Error: Insufficient funds
							Toast.makeText(getApplicationContext(),
							"Insufficient funds to debit", Toast.LENGTH_SHORT)
							.show();
							// Reenter this credit/debit view
							startActivity( intentCreditDebit );	
						}
					}
				}
				else if( action.equals("Credit")){
					if(accountSelected.equals("Checking Account")) {
						customer.credit( 1, amountRequested );
					}
					else if( accountSelected.equals("Savings Account")){
						customer.credit( 2, amountRequested);
					}
				}
				// Rebundle Teller to send to next view
				Bundle nextBundle = new Bundle();
				nextBundle.putParcelable("user", teller);
				intentCustomerInfo.putExtra("user", teller);
				
				// Return to Teller's page viewing customer info
				startActivity( intentCustomerInfo );
			}
		});
		// Set click listener for cancel button
		cancelButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// Rebundle Teller to send to next view
				Bundle nextBundle = new Bundle();
				nextBundle.putParcelable("user", teller);
				intentCustomerInfo.putExtra("user", teller);
				
				// Return to Teller's page viewing customer info
				startActivity(intentCustomerInfo);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.credit_debit, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		return super.onOptionsItemSelected(item);
	}
}


