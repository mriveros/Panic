package com.panic.root.panic;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper
{
	//Table Customers
	public final static String TABLE_CUSTOMER = "customer";
	public final static String CUSTOMER_KEY_ID = "_id";
	public final static String CUSTOMER_COL_NAME = "name";
	public final static String CUSTOMER_COL_PHONE = "phone";
	public final static String CUSTOMER_COL_EMAIL = "email";

	//Table Companies
	public final static String TABLE_COMPANY = "company";
	public final static String COMPANY_KEY_ID = "_id";
	public final static String COMPANY_COL_NAME = "name";
	public final static String COMPANY_COL_ADDRESS = "address";
	public final static String COMPANY_COL_CITY = "city";
	public final static String COMPANY_COL_STATE = "state";
	public final static String COMPANY_COL_ZIP = "zip";
	public final static String COMPANY_COL_PHONE = "phone";
	public final static String COMPANY_COL_EMAIL = "email";
	public final static String COMPANY_COL_RFC = "rfc";
	
	//Table product
	public final static String TABLE_PRODUCT = "product";
	public final static String PRODUCT_KEY_ID = "_id";
	public final static String PRODUCT_COL_CODE = "code";
	public final static String PRODUCT_COL_NAME = "name";
	public final static String PRODUCT_COL_COMPANY_ID = "company_id";
	public final static String PRODUCT_COL_PRICE = "price";
	public final static String PRODUCT_COL_TAX = "tax";

	//Table Invoice
	public final static String TABLE_INVOICE = "invoice";
	public final static String INVOICE_KEY_ID = "_id";
	public final static String INVOICE_COL_FOLIO = "folio";
	public final static String INVOICE_COL_COMPANY_ID = "company_id";
	public final static String INVOICE_COL_CUSTOMER_ID = "customer_id";
	public final static String INVOICE_COL_DATE = "date";
	public final static String INVOICE_COL_COMMENTS = "comments";
	public final static String INVOICE_COL_SUBTOTAL = "subtotal";
	public final static String INVOICE_COL_TOTAL = "total";
	public final static String INVOICE_COL_DISCOUNT = "discount";
	public final static String INVOICE_COL_SIGNATURE_ID = "signature_id";
	
	//Table Invoice details
	public final static String TABLE_INVOICE_DETAIL = "invoice_detail";
	public final static String INVOICE_DETAIL_KEY_ID = "_id";
	public final static String INVOICE_DETAIL_COL_INVOICE_ID = "invoice_id";
	public final static String INVOICE_DETAIL_COL_PRODUCT_ID = "product_id";
	public final static String INVOICE_DETAIL_COL_QUANTITY = "quantity";
	public final static String INVOICE_DETAIL_COL_TOTAL = "total";
	
	private final static String DATABASE_NAME = "PedidosMovilDB";
	private final static int DATABASE_VERSION = 1;
	
	private final static String CREATE_TABLE_CUSTOMER =
			"CREATE TABLE customer(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
			"name nvarchar(50)," +
            "address nvarchar(50)," +
            "zip nvarchar(5)," +
            "city nvarchar(20)," +
            "state nvarchar(20)," +
            "phone nvarchar(20)," +
            "email nvarchar(20)," + 
            "rfc nvarchar(15))";
	private final static String CREATE_TABLE_COMPANY =
			"CREATE TABLE company(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
			"name nvarchar(50)," +
            "address nvarchar(50)," +
            "zip nvarchar(5)," +
            "city nvarchar(20)," +
            "state nvarchar(20)," +
            "phone nvarchar(20)," +
            "email nvarchar(20)," +
            "rfc nvarchar(15))";
	private final static String CREATE_TABLE_PRODUCT =
			"CREATE TABLE product(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
			"code nvarchar(10)," +
            "name nvarchar(50)," +
            "price real," +
            "company_id int REFERENCES company(_id) ON UPDATE CASCADE ON DELETE CASCADE," +
            "tax real)";
	private final static String CREATE_TABLE_INVOICE =
            "CREATE TABLE invoice(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "folio int," +
            "company_id int REFERENCES company(_id) ON UPDATE CASCADE ON DELETE CASCADE," +
            "customer_id int REFERENCES customer(_id) ON UPDATE CASCADE ON DELETE CASCADE," +
            "date datetime," +
            "comments ntext," +
            "subtotal real," +
            "discount real," +
            "total real," +
            "boxes int," +
            "signature_id)";		
	private final static String CREATE_TABLE_INVOICE_DETAIL =
            "CREATE TABLE invoice_detail(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "invoice_id int REFERENCES invoice(_id) ON UPDATE CASCADE ON DELETE CASCADE," +
            "product_id int REFERENCES product(_id) ON UPDATE CASCADE ON DELETE CASCADE," +
            "quantity int," +
            "total real)";			
	
	public DBOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase sqLiteDB)
	{
		sqLiteDB.execSQL(CREATE_TABLE_CUSTOMER);
		sqLiteDB.execSQL(CREATE_TABLE_COMPANY);
		sqLiteDB.execSQL(CREATE_TABLE_PRODUCT);
		sqLiteDB.execSQL(CREATE_TABLE_INVOICE);
		sqLiteDB.execSQL(CREATE_TABLE_INVOICE_DETAIL);
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDB, int oldVersion, int newVersion)
	{
		sqLiteDB.execSQL("DROP TABLE IF EXISTS " + TABLE_CUSTOMER);
		sqLiteDB.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPANY);
		sqLiteDB.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT);
		sqLiteDB.execSQL("DROP TABLE IF EXISTS " + TABLE_INVOICE);
		sqLiteDB.execSQL("DROP TABLE IF EXISTS " + TABLE_INVOICE_DETAIL);
		
		onCreate(sqLiteDB);
	}
}
