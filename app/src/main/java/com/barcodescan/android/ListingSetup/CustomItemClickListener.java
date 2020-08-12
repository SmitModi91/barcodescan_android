package com.barcodescan.android.ListingSetup;

import android.view.View;

import com.barcodescan.android.ContactsListing.ContactsListingAdapter;


/**
 * Created by Smit Modi.
 */
public interface CustomItemClickListener {
     void onItemClick(View v, int position);
     void onItemClick(View v, int position, ContactsListingAdapter.ActionItem actionItem);
}
