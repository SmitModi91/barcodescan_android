package com.barcodescan.android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.barcodescan.android.BarcodeScanner.BarcodeScannerActivity;
import com.barcodescan.android.ContactsListing.ContactsListingAdapter;
import com.barcodescan.android.ListingSetup.CustomItemClickListener;
import com.barcodescan.android.LocalData.ContactDetail;
import com.barcodescan.android.LocalData.DBHandler;
import com.barcodescan.android.Util.AlertDialog.AlertDialogHelper;
import com.barcodescan.android.Util.AlertDialog.AlertDialogModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Smit Modi.
 */
public class MainActivity extends AppCompatActivity {

    String TAG = "HomeActivity";
    DBHandler dbHandler;
    @BindView(R.id.rvContactsList)
    RecyclerView rvContactsList;
    @BindView(R.id.fabAdd)
    FloatingActionButton fabAdd;

    ArrayList<ContactDetail> contactDetailArrayList;
    LinearLayoutManager layoutManager;

    ContactsListingAdapter contactsListingAdapter;
    AlertDialogHelper alertDialogHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        dbHandler = new DBHandler(this);
        alertDialogHelper=new AlertDialogHelper(this);
        listSetup();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(dbHandler!=null&&contactsListingAdapter!=null)
        {
            contactDetailArrayList=(ArrayList<ContactDetail>) dbHandler.getAccountsList();
            loadAdapter(contactDetailArrayList);
        }
    }

    public void listSetup()
    {
        contactDetailArrayList= (ArrayList<ContactDetail>) dbHandler.getAccountsList();
        Log.d(TAG, "listSetup: "+contactDetailArrayList.size());
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvContactsList.setLayoutManager(layoutManager);
        contactsListingAdapter=new ContactsListingAdapter(this,contactDetailArrayList,getItemCLickListener());
        rvContactsList.setAdapter(contactsListingAdapter);
        Log.d(TAG, "listSetup: item count"+contactsListingAdapter.getItemCount());
    }

    public void loadAdapter(final ArrayList<ContactDetail> itemsList) {

        rvContactsList.post(() -> {
            rvContactsList.getRecycledViewPool().clear();
            contactsListingAdapter.updateListing(itemsList);
            contactsListingAdapter.notifyDataSetChanged();
        });
    }

    @OnClick(R.id.fabAdd)
    public void onViewClicked() {
        Intent barcodeScanner=new Intent(this, BarcodeScannerActivity.class);
        startActivity(barcodeScanner);
    }

    public CustomItemClickListener getItemCLickListener()
    {
        CustomItemClickListener customItemClickListener=new CustomItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {

            }

            @Override
            public void onItemClick(View v, int position, ContactsListingAdapter.ActionItem actionItem) {
                switch(actionItem)
                {
                    case CALL:
                        AlertDialogModel callAlertDialog=new AlertDialogModel();
                        callAlertDialog.setTitle("Phone");
                        callAlertDialog.setDesc(contactDetailArrayList.get(position).getPhoneNumber());
                        callAlertDialog.setCancelable(true);
                        callAlertDialog.setPositiveText("Call");
                        callAlertDialog.setNegativeText("Cancel");
                        alertDialogHelper.showAlertDialog(callAlertDialog,null);
                        break;
                    case MAIL:
                        AlertDialogModel mailAlertDialog=new AlertDialogModel();
                        mailAlertDialog.setTitle("Mail to");
                        mailAlertDialog.setDesc(contactDetailArrayList.get(position).getEmailID());
                        mailAlertDialog.setCancelable(true);
                        mailAlertDialog.setPositiveText("Send");
                        mailAlertDialog.setNegativeText("Cancel");
                        alertDialogHelper.showAlertDialog(mailAlertDialog,null);
                        break;
                    case WEB:
                        // Redirect to web
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        String url=contactDetailArrayList.get(position).getWebLink();
                        if(url.contains("https://")||url.contains("http://"))
                            i.setData(Uri.parse(url));
                        else
                            i.setData(Uri.parse("https://"+url));
                        startActivity(i);
                        break;
                    case DELETE:
                        // Get confirmation and delete
                        AlertDialogModel deleteAlertDialog=new AlertDialogModel();
                        deleteAlertDialog.setTitle("Delete");
                        deleteAlertDialog.setDesc("Are you sure you want to delete this item?");
                        deleteAlertDialog.setCancelable(true);
                        deleteAlertDialog.setPositiveText("Delete");
                        deleteAlertDialog.setNegativeText("Cancel");
                        alertDialogHelper.showAlertDialog(deleteAlertDialog, new AlertDialogHelper.AlertDialogListener() {
                            @Override
                            public void onPositiveClick(int from) {
                                if(dbHandler!=null&&contactsListingAdapter!=null)
                                {
                                    dbHandler.deleteAccountDetails(contactDetailArrayList.get(position).getId());
                                    contactDetailArrayList=(ArrayList<ContactDetail>) dbHandler.getAccountsList();
                                    loadAdapter(contactDetailArrayList);
                                }

                            }

                            @Override
                            public void onNegativeClick(int from) {

                            }

                            @Override
                            public void onNeutralClick(int from) {

                            }
                        });

                        break;

                }
            }
        };

        return customItemClickListener;
    }
}