
	 
	
	var KEY_ENTER = 13;
	var KEY_F2 = 113;
	var KEY_ESC = 27;
	var KEY_TAB = 9;
	var KEY_F5 = 116;
	var KEY_F1 = 112;
	var KEY_F3 = 114;
	var KEY_F12=123;
	var KEY_F11 = 122;
	var KEY_F10 = 121;
	var KEY_LEFTARROW = 37;
	var KEY_RIGHTARROW = 39;
	var KEY_UPPERCASE_A = 65;
	var KEY_UPPERCASE_Z = 90;
	var KEY_LOWERCASE_A = 97;
	var KEY_LOWERCASE_Z = 122;
	var KEY_SPACE = 32;
	var KEY_NUMBER_0 = 48;
	var KEY_NUMBER_9 = 57;
	var KEY_COMMA = 188;
	var KEY_F9	= 9;
	var KEY_HYPHEN = 45;
	var KEY_FWDSLASH = 47;
	var KEY_DOT = 46;
	var KEY_BACKSPACE = 8;
	var KEY_DELETE = 46; 	
var returnvalue;
	 
	var BANK_BRN_DUPLICATE_CHECK = "Branch Combination Should not be Duplicated";
	var RECORD_PRESENT 		= "Already exists";
	var RECORD_NOT_PRESENT  = "Does not exist";
	var BLANK_CHECK 		= "Field required";
	var ZERO_CHECK 			= "Should not be zero";
	var NUMERIC_CHECK		= "Should be numeric";		
	var NEGATIVE_CHECK 		= "Should not be Negative";
	var OPTION_MESSAGE 		= "Select Option ";
	var HELP_LEGEND 		= "F5 - Help";
	var NOT_VALID_INPUT 	= "Special characters are not allowed";
	var FUNCTION_LEGEND 	= "F2 - Traverse Back , ESC - Cancel";

	var OPTION_LEGEND 		= "ESC - Exit";
	var DELETE_LEGEND 		= "F10- Delete From The Grid" ;
	var REPORT_FILEEXT		= ".zip";
	var RESULT_TRUE 	  	= "TRUE";
	var RESULT_FALSE 		= "FALSE";
	var NUMERIC_CHECK 		= "Should be numeric";
	var LESSER_CHECK		= "Should not be less than Starting No";
	var GREATER_CHECK		= "Should not be greater than Ending No";

	var HUB_SOD_NOT_DONE='HUB SOD Not Done,Can\'t Do Branch SignIn';

	var PREVIOUS_DATE_CHECK="should be greater than previous date";
var DATE_GREAT_CHECK = "Should Be Greater Than Sanction Date";
var LOG_CONN_BLANK_SPACE = "Logical Connector of the last partition defition should be blankspace";
	var ALL_LCC_UCC="Both LCC And UCC";

var NOT_BE_GREATER = "Cannot not be greater than no of of instruments...";
var AMT_NOT_BE_GREATER = "Cannot not be greater than deposit slip amount ";
var RETURN_ALREADY_MARKED = "Return has already been marked for this instrument";
var INVALID_USER = "Invalid User Option";
var EXPLIMLTEQMAX = "Exposure Limit Should be Less than or Equal to Maximum Exposure Limit";
var EXPLIMGTEQMIN = "Exposure Limit Should be Greater than or Equal to Minimum Exposure Limit";
var CANNTMTTWODECIMAL = "Can't have more than two decimals";
var SHGTHUND = "should not be greater  than 100";
var REC_DUPLICATED_SAMEARRTYPE = "Record Duplicated for the Same Arrangement Type";
	 var USERID_ROLECODE     ="Either UserId or RoleCode is Required";

var DEPSLIP_CASHPROD = "Deposit Slip Belonging To Cash Product Are Not Allowed";
var DATE_OF_APPPOINTMENT = "Effective date should not be less than DATE_OF_APPPOINTMENT";
var GREATER_EQUAL = "Date should be greater or equal to";
var COMMENSE_LESS_HUBCBD = "Commencement Date Is Less Than Current Business Date,Can't Modify";
	var MAX_CHRGS_GREATER="should be greater than Minimum Charges";
      
	var CMD_AUTHORISE	   = "Press Enter To Authorise";
	var CMD_CONFIRM	   = "Press Enter To Confirm";
	var CMD_REFRESH	   = "Press Enter To Refresh";
	
	var NO_PRO_PEND			= "No Process Pending";

	var ALL_LCC= "For All LCC";
	var ALL_UCC= "For All UCC";


   	var PARENT_HIER_FOUND="Grandparent Hierarchy already found";
   	var HIERCODE_NOT_SAME="and Hierarchy code should not be the same";
	
	 
	var CANNOT_ASSIGNED	   ="MICR Code Cannot be assigned for Non-Micr Locations";
	
	 	
	var FORMAT_CHECK = "should be of valid decimal format";

	 
	var DATE_FORMAT_CHECK=" Must be in DD-MM-YYYY format";
	 	

	 
	var LESS_CURRBUS_DATE= "Latest Effective Date Lesser than Current Business Date";
	var GRTOEQL_CURRBUS_DATE= "Latest Effective Date Greater or Equal to Current Business Date";
	 

	 
	var RECORD_NOT_SELECTED = "Atleast One Row in Grid's Required Column Should be Y ";
	var LESS_CHECK			="Should be less than ";
	var DUPLICATE_CHECK		="Should not be Duplicated";
	 
	var STATE_NOT_REQ = "State Code Not Required for this Country";

	 
	var LESS_CHECK			="Should be less than ";	
	 

	var PINCODE_CHECK	="should be of 6 digits";

	var OPTION_RESTRICT="Delete Option not allowed";

	 
	var REC_DUPLICATED      = "Record duplicated";
	 

	 
	var RECORD_NOT_SELECTED = "No Rows Selected from the Grid";
	var ROW_YM=		"Grid Row Should be Y/M";
	var LESS_CHECK			="Should be less than ";
	var DUPLICATE_CHECK		="Should not be Duplicated";
	var REQUIRED_CHECK=	"Required Should be Y / M";
	 	
	

	var SELECT_FLD_MSG = "Select ";
	var ENTER_FLD_MSG  = "Enter ";


	var CMD_SAVE	   = "Press Enter To Save"
	var CMD_CANCEL	   = "Press Enter To Cancel The Operations"
	var CMD_EXIT	   = "Press Enter To Exit From the Page"
	var CMD_PRINT	   = "Press Enter To Print"
	var CANCEL_PAGE = "Do you Want To Cancel?";
	var EXIT_PAGE = "Do you Want To Exit?";
	var SAVE_MESSAGE = "Press Enter Or Spacebar To Save ";
	var CANCEL_MESSAGE = "Press Enter Or Spacebar To Cancel ";
	var EXIT_MESSAGE = "Press Enter Or Spacebar To Exit ";
	var SESSION_NULL	  	  = "Session Expired";
	var SESSION_INVALID	  	  = "Invalid Session";
	var SESSION_EXPIRED	  	  = "Session Expired";	
	 
	var GREATER_THAN_CHECK			="Should not be less than"	
	 

	 
	var DELETE_NOT_ALLOWED			="Delete is Not Allowed ";
	var SHOULD_GREA_MINIMUM			="Should be Greater than Minimum Charges";	
	var	SHOULD_GREA_MINIMUM_DD		="Should be Greater than DD Minimum Charges";
	var	SHOULD_GREA_MINIMUM_PO		="Should be Greater than PO Minimum Charges";
	var	SHOULD_GREA_MINIMUM_RBI		="Should be Greater than RBI Minimum Charges";
	
	 

	 
	
	var CUSPROD_CHECK       = "Product Code is not a Customer Product";
	var DIV_CHECK           = "Division Code is not Mandatory.Do you want to proceed ?";
	var HIER_CHECK			="Hierarchy Code is not Mandatory.Do you want to proceed ?";

	 

      
    var TOTALAMOUNT_DEPOSIT    ="Total Amount Returned Does Not Tally With the Deposit Slip Amount";
    var GREATER_ZERO           ="Entred Should Be greater than  Zero" ;
    var INSTRUMENT_TOTALAMOUNT ="Total Instrumentwise Amount Does Not Tally With Total amount Returned";
    var LESS_THAN              ="Should Be Less Than Or Equal To";
    var BULK_RETURN            ="Is Not allowed for bulk return";
    var RETURN_CAN_NOT_MARKED  ="Record Already Exist Returns Can Not be Marked";  
    var INSTRUMENTS_EXIST      ="No Of Instruments should not exceed Total no of Instruments Returned"; 
     

	 
	var SCH_LESS_CHECK=" Should not be Less than Schedule Date ";
	var NOT_LESS_CHECK= " Should not be Less than ";
	var FSY_FIN_LESS_CHECK= " Forwarding Schedule Year Should Be Either Current or Previous Financial Year  ";
	 
	

	 
	var CANNOT_MODIFY_LATEST_EFDATE="Cannot Modify,Latest Available Effective date is Lesser than Current Business Date";
	var LATEST_EFDATE_GREOREQL= "Latest Available Effective date Greater or Equal to Current Business Date";
	var BANK_CODE_COMBINATION	="Product Code Combination is Invalid ";	
	var AGREEMENT_SERIAL ="is Invalid";
	var COURIER_CODE_BASIS1="should be 'D' or 'I' or 'L' or 'M'" ;
	var COURIER_CODE_BASIS2="should be 'S' or 'I' or 'L' or 'M'" ;
	var COURIER_CODE_BASIS3= "Basis should be  'I' or 'L' or 'M'" ;
	var CHARGE_BASIS_I_D="should be 'I' or 'D'";
	var CHARGE_BASIS_I_S="should be 'I' or 'S'" ;
	var CHARGE_BASIS_P="should be 'P'";
	 


	 
	var EXPOSURE_LIMIT="Invalid Format";
	var EFFDATE_GREATER="Should be greater than" ;
	 

	 
	var SHOULD_TEN_TO_TENTHOUSAND	="Should be 10 or 100 or 1000 or 10000";
	var SHOULD_GRE_ZERO_AND_LESS_SEVEN	="Value Should be >=0 And <=7 ";	
	var	FIRST_WEEK_HOLIDAY			="cannot be same as First Weekly Holiday ";
	
	 

	  
	EFF_GREAT_CBD		= "Should not be less than current business date";
	DATE_LESSCBD_CHECK  = "Should be less than or equal to current business date";
	DATE_GREATCBD_CHECK = "Should be greater than or equal to current business date";
	DATE_GREATEFF_CHECK = "Should be greater than or equal to effective date";
	DATE_GREATER_COMMENCE = "Should Be Greater Than Commencement Date";
	DATE_GREATER_EXP	  = "Should Be Greater Than Expiry Date";
	FOR_UCC_ONLY		  ="Can Be Defined Only For UCC Product";
	ONLY_FOR_COLLEC		  ="Can Be Defined Only For Collection Products";
	NOT_FOR_DISB		  ="Not Allowed For Disbursement Products";
	NOT_FOR_UCC			  ="Not Allowed For UCC Product";
	ONLY_FOR_DISB		  ="Day One Definition Can Be 'F' Only For Disb. Products";
	ONLY_FOR_COLLEC		  ="Can Be Defined Only For Collection Products";
	CHECK_PRDTYPE		  ="Not Allowed For This Prodcut Type";
	HIERY_CHECK			  ="Hierarchy Not Available For This Customer";
	UCC_CHECK			  ="This Option is Not For UCC Products";
	DIV_REQ_CHECK		  ="Division Required Flag is 'N' For This Customer Product";
	HIRAR_REQ_CHECK		  ="Hierarchy Required Flag is 'N' For This Customer Product";
	DAYS_EXCEED_CHECK	  ="Number Of Days Should Not Exceed 180";
	DAY_DEF_CHECK		  ="Day One Definition is Not Date Of Funding";
	DEDICATED_PRD_CHECK	  ="Pooling Serial is Dedicated For a some other Product";
	ADDL_INFO_EXCEED	  ="Additional Information Should Not Be > Than 100";
	ADDL_LESS_ZERO		  ="Additional Information Should Not Be < Than 0";
	DI_ONLY				  ="Enter 'D' or 'I' Only";
	SIZE_GRT			  ="Information Size Should Not Be > Than 35";
	SIZE_ERR			  ="Information Size Should Be 10 For This Data Type"; 
	TYPE_ERR			  ="Enter [ T / D / I / A ] Only";
	YES_NO                ="Enter 'Y' or 'N' Only";
	DATA_FORMAT			  ="Enter Only X or N or A or a or S or / ";
	MSG_YES="YES";
	MSG_NO="NO";
	
	 

	  
    	var EFFDATE_LESS_CHECK="Effective Date Should not be less than the Product Commencement Date";

	 
	
	  

	var NON_BLANK      = 'nonblank';
	var NON_BLANK_ZERO = 'nonblankorzereo';

	 

 
	var COLLN_ADJ_DATE = "should be equal to Current Business Date";
	var DATE_FORMAT = " should be in DD-MM-YYYY format ";
	var	NETW_PDT_TYPE="Network Location Type and Product Covering Network do not match"	;
	var POOLING		="Either Pooling has not been done or Due Date is yet to be calculated"	;
	var GREATER_DATE=" Should not be Greater than Current Business Date ";
	 var LESSER_DATE = " Should not be Lesser than From Date";
	var BANK_RELATION=" should be Our ";
	var CUS_PRD_EXP = "This Customer product combination has expired";
	var CHECK_COM_DATE="Check for Product Commencement Date";
	var DIV_HIER_NOT_REQD="Division and Hierarchy Code Not Required";
 

 
	var EMAIL_CHECK         ="One or More Email Addresses Entered is Invalid ";
 


 
	var ATLEAST_ONE_ROW_ENTERED = "Atleast One Row should be Entered";
	 
 
var EXIT_FRAME = "Do you want to exit from Details";
 

 
	var EQUAL_DATE="should be equal to Current Business Date";
	var LESSER_EQUAL_DATE="Should be Either Equal To Or Greater Than Current Business Date";
	var RATE_CHECK="cannot be less than Incremental Rate";
	var RECORD_COMB_NOT_FOUND="Combination of ";
	var LESSER_THAN = " Should Be Lesser Than ";
	var GREATER_THAN = " Should Be Greater Than ";
 
	
	
	  
    	var PRODUCT_TYPE_MATCH="Product Type of the given Product Code does not match with the selected Product Type";

	 
 
		var GREAT_CHECK			="Should not be less than ";
 
	

	
	var FIN_LESS_CHECK=	"Should Not be Less than Current Financial Year";
	var FWD_SCH_NOTSAME_BRN="Forwarding Schedule Doesn't Belong To This Branch";
	var CORR_SCH_NO_FUND="Corr. Schedule Cannot Be Realised Without Funding";
	var PART_ONLY_ALLOWED="Only Part Realisation Allowed";
	var AMT_REAL_CANNOT_EXCEED="Amount Realised Cannot Exceed Balance Amount Available For  Realisation Marking";
	
	var DATE_GREATER="Must Be Greater Than  ";
	var DATE_LESSER="Must Be Lesser Than Or Equal  to  ";
	var DATE_GREATER_EQUAL="Must Be Greater Than Or Equal to  ";
	var ALL_INS_REAL="Instrument(s) Already realised / returned";
	var CANNOT_PROC_WITH_PART_REAL=	"Cannot Proceed With Part Realisation Of Schedule  Sent To Our Branch";
	var REALAMT_CANNOT_SCH = "Realised Amount Cannot Be greater than Schedule Amount";
	var ENTER_VALID = " Enter a Valid  ";
	var STALE_INS ="  This Is A Stale Instrument  ";
	var INVALID_DATE_FORMAT="Date must be in DD-MM-YYYY format";
	var SHOULD_NOT_GRT="  Should Not be Greater Than ";
	var NOT_MORE_ROW="More Than 10 records is Not Accepted And Amount Should Be Equal To  Amount Realized";
	var AMT_CANNOT_EXCEED_REALAMT="Total Amount Cannot Exceed Amount Realized";
	var DUPLICATE_DATA_NOT_ALLW=" Row Should Not Be Duplicated";
	var AMT_NTLESS_HGVAL="Realised Amount Cannot Be Less Than High Value Amount";
	var CRDT_GT_CLGDT="Credit Date Must Not be Greater than 7 Days From Clearing Date";
	var AMT_EQUAL_REALAMT="Total Amount Should Be Equal To Amount Realized";
	var GIVEN_AMT_EXCEED_REAL_AMT=	"Given Amount Is Exceeding The Available Amount for  Realisation";
	var REALAMT_NOTEQUAL_SCHAMT=" Amount Realized Should Not Be Equal To Schedule Amount   ";
	var FIN_YEAR_CHECK= " should be either Current or Previous Financial Year  ";


var REC_DET_NOT_FOUND	= "Recovery Details Not Found";


var GREATER_EQUAL_DATE = "should be either equal to or greater than Current business Date";
var EOD_ALL_READY_DONE = " EOD Already Done   ";
var REC_AUTH_SUCCESSFULLY = "Record authorised successfully";
var REC_AUTH_PARTIALLY    ="Some records are pending for authorization";

var VALID_CHECK=" Enter a Valid Exposure Limit   ";

var ATLEAST_ONE_SELECT = "Atleast one of the Branch Type Should be Selected ";
var MIN_DAY_CHECK="Minimum days should be less than maximum days";

var GREATER_HUD =" cannot be greater than 100";
var GREATER_EQUAL_CHECK ="should be lesser or equal  ";



var GREATER_THAN_CHECK_COND= " Should be greater than ";
var EFF_DATE_CHECK="Should be Greater than Current Business Date";

var INST_AMT_ERR=" Instrument Amount Exceeding Slip Amount ";
var INST_DATE_ERR2 =" Cannot Be < 180 Days From Current Business Date	";
var INST_DATE_ERR1 ="	Cannot  Exceed Deposit Slip Date	";
var CLG_DATE_ERR2 ="	Cannot Be Lesser than Deposit Slip Date	";
var RBI_DATE_ERR2="	Should Not be Lesser Than Clearing Date	";
var RBI_DATE_ERR3="	Cannot Exceed	";
var ADD_DATA_SIZE_ERR="	Additional Info Cannot Exceed Max Length ";
var ALPHEBET_CHECK="	Only Alphabets Allowed	";


var CAL_DATE_CHECK= " Should be greater than ";
var CAL_FROM_DATE_CHECK="Should be lesser than Current Business Date";
var CAL_UPTO_DATE_CHECK="Should be Greater than or equal to From Date";


 var GREATER_ZERO           =	" Should Be greater than  Zero" ;
 var LESS_THAN_TOT_NO_INST     =	"Should Be Less Than Or Equal To Total No. Of Instruments";
 var LESS_THAN_INST_AMT	=" should be less than Instrument Amount";

var OUR_BANK_NOT_ALLW = "Our Code Allowed only For Home Clearing";
var HIGH_VAL_AMT_NOT_ALLW="   Should Be Greater Than  ";


var ATLEAST_ONE_ROW_SEL = "Atleast One Row should be Selected";



var INVALID_FWSCHYR		=	" Not a Valid Forwarding Schedule Year";
var FWSCHYR_NOTGRT_CBD	=   " Forwarding Schedule Year Should not be greater than CBD Year";
var FWSCHYR_NOTGRT_1999	=	" Forwarding Schedule Year Should not be less than 1999";
var INVALID_RTN_SLPNUM	=	" Not A Valid Return Slip Number";
var INVALID_INST_NO		=	" Not A Valid Instrument Number";
var DATE_FMT_CHECK		=	" Must be in DD-MM-YYYY format";



var ALL_BANKS = "For All ";
var ALL_BRANCHES = "For All Branches";
var DESPBANK_SENDBNK_NOT_EQUAL = "Despatched Code Should not be Same As Send Code";

var REC_NOT_AUTH = "Record not authorized ";



var UPTODATE_GT_FROMDATE="Should be Equal to or Greater than From Date"
var ALL_CUSTOMERS="For All Customers";
var ALL_PRODUCTS="For All Products";
var ALL_LOCATIONS="For All Locations";
var ALL_DIVISIONS="For All Divisions";
var ALL_HIERARCHY="For All Hierarchies";


var ALL_POOLSERIAL="For All PoolSerial";

var ALL_BRANCHES = "For All Branches";
var ALL_BANKS="For All ";



var INVALID_FWSCHYR		=	"Not a Valid Forwarding Schedule Year";
var FWSCHYR_NOTGRT_CBD	=   "Forwarding Schedule Year Should not be greater than CBD Year";
var FWSCHYR_NOTGRT_1999	=	"Forwarding Schedule Year Should not be less than 1999";
var INVALID_RTN_SLPNUM	=	"Not A Valid Return Slip Number";
var INVALID_INST_NO		=	"Not A Valid Instrument Number";



var ALL_ZONES = "For all Zones";

	var DATE_GREATDIS_CHECK ="Should be > Activation Date";


var DENOM_AMT_ERR =" Total Denomination Amount Should Be Equal To Deposit Slip Amount  ";

var DEPSLNO_INSREQ_MANDATORY = "Either DepslpNo or Instrument required is Mandatory";
var INSTR_RETURN="Instrument Has Been Already Returned";

var ALL_ZONECODE = "For All Zones";
var ETR_NOTVALIDUSR="Authorization Is Not Permitted For Entered User";

var FOR_ALL_CORR_BNK_PROD_CODES = "For all Correspondent Product Codes";

var FOR_ALL_CORR_BANKS = "For all Correspondent ";

var ALL_DATES = "For All Dates";

var REG_ADDRESS = "Atleast One Registered Address is Mandatory";
var MAIL_ADDRESS = "Atleast One Mail Address is Mandatory";
var EMAILID_CHECK ="should be of valid format"; 

var UPTO_GREATE_FROM = "Should be greater than from Date";
var ROW_NOT_PRT_WLE_QUERY_PROC="Row Not Present while Querying Procedure";

var HIERCODE_EQUAL =" And Hierarchy Code should not be the same";
var GRANDPARENT_HIER =" GrandParent Hierarchy Already found";

var DATE_LESSCUTOF_CHECK = "Should be Less than As On Date";
var DATE_LESSUPTO_CHECK="Should be Greater than From Due Date";


var RECORD_NOT_AUTH="Record Not authorised";
var CURRENCY_CODE_SAME="Currency Code Should Be Same For All The Instruments"


var CLEAR_DATE_ERR =" Cannot Be > 3 Days From Current Business Date	";

var DELETE_CONFIRM		= "Are You Sure Want To Delete?";
var PAYMOD_XSIZE_EXCD	= " Vertical Size Should Not Exceed Stationary's Vertical Size";  
var PAYMOD_YSIZE_EXCD	= " Horizontal Size Should Not Exceed Stationary's Horizontal Size";  	


var convSubUnit='1000';

var INST_AMT_ERR4=" Instrument Duplication Not Allowed ";
var EFF_DATE_EQUAL="Should not be equal to current Business Date";

var SCHE_WAS_SUCCESS_GENED=" SCHEDULE WAS SUCCESSFULLY GENERATED";

var INST_AMT_ERR3="	Instrument Not Matching With Total No., But Amount Is Equal ";

var CUST_CODE_SHOULD_NOT_BLANK = "Customer Code Should not be Blank";

var INST_DATE_ERR3  =" Cannot Be < 180 Days From Deposit Slip Date	";

var SELECT_MSG = "Option Should Not Be Blank";

var ALL_COMPANY = "For All Companies";

var NOT_EQUAL_TO = " should not be Equal to ";
var NOT_ALLOWED_FOR_UCC = " Not Allowed for UCC Products ";
var NOT_ALLOWED_FOR_LCC = " Not Allowed for LCC Products ";
var LESS_THAN_CBD_FOR_PRE = " should be less than Current Business Date for Preponement ";
var GREAT_THAN_CBD_FOR_POST=" should be less than Current Business Date for Preponement ";


var ALL_DEP_SLIP = "For All Deposit Slip Nos.";
var CMD_CLOSE = "Press Enter To Close the Grid";


var ALL_INSTRUMENTS="For All Instruments";
var ALL_LCC = "For All LCC Products";
var ALL_UCC ="For All UCC Products";

var INFOTYPE_LEGEND     = "[T / D / I / A]";


var SCHE_WAS_SUCCESS_GENED=" THE SCHEDULE GENERATION IS COMPLETED ";

var RESTRICT_LESS_DATE = "Credit Due Date Should be Greater than or equal to Current Business Date ";
var NO_REC_TO_POPULATE ="No Records to Populate";
var PART_NOT_ALLOWED_FOR_ONE_INST	= "  Part Realisation Not Allowed For Single Intrument";


var RESET_COL_CHK = "Atleast one Reset Column should Be Checked";
var USER_CANNOT_RESET="Same user cannot reset the value";
var CURR_CODE ="Currency Code";

var FORWARD_DETAILS_QUERY = "Forwarding Schedule Details";
var ALL_STATUS ="For All Status";

	var ALREADY_RETURNED= " Already Returned ";
	var ALREADY_REALISED= " Already Realised ";


var INST_LOC_NOT_MATCH ="Does Not Match with the PDC Ware House Branch Code for the Lodgement Type";

var CMD_PROCESS	   = "Press Enter To Process";

var CHECK_PROD_TYPE="UCC Not Allowed For Nature Of Prod. Of Type Cash Product";

var EQUAL_TO =" Should be Equals to ";	

var RELTYPE_NOT_ALLOWED = "With Relation Type 'S' Not Allowed ";

var POLL_NO_ERR = " Policy Number Duplication Not Allowed ";
var POLL_NO_ERR1 = "Instrument Amount Should Be Equal To Total Premium Amount";
var POLL_NO_ERR2 = "Enter Policy Details";
var POLL_NO_ERR3 = " Total Premium Amount Cannot be > Than Instrument Amount ";
var POLL_NO_ERR4 = " Total Premium Amount Cannot be < Than Instrument Amount ";
var ADDINFO_ERR5="Additional Info Cannot Be Blank";

var AMT_CANNOT_EXCEED_REALAMT="Total Amount should not be greater than Amount Realized";
var INST_AMT_ERR1="	No Of Instruments Matching But Instrument Amount  Exceeds ";
var INST_AMT_ERR2="	No Of Instruments Matching But Instrument Amount  Less ";
var INST_NO_ERR2="	Total Amount Matching But No Of Instruments Doesn't Matching ";
var DR_LOC_INSLOC_ERR=	"Drawn On Location Cannot Be Same As Deposit Location Code";

var INST_AMT_ERR1="	No Of Instruments Matching But Instrument Amount  Exceeds ";
var INST_AMT_ERR2="	No Of Instruments Matching But Instrument Amount  Less ";
var INST_NO_ERR2="	Total Amount Matching But No Of Instruments Doesn't Matching ";

var AMT_CANNOT_EXCEED_REALAMT="Total Amount should not be greater than Amount Realized";
var CUSPROD_CHECK = "Product Code is not a Customer Product";
var DIV_CHECK = "Division Code is not Mandatory.Do you want to proceed ?";
var HIER_CHECK = "Hierarchy Code is not Mandatory.Do you want to proceed ?";
var DIVMAN_CHECK = "Division Code is Required For This Customer code";
var HIERMAN_CHECK = "Hierarchy Code is Required For This Customer code";
var DIV_BLANK_CHECK = "Default Division Code Is mandatory";
    var HIER_BLANK_CHECK 	="Default Hierarchy Code Is Mandatory";

var CUST_CODE = " Customer Code ";
		var GROUP_CODE= " Group Code ";
var CUSPROD_CHECK = "Product Code is not a Customer Product";
var DIV_CHECK = "Division Code is not Mandatory.Do you want to proceed ?";
	var HIER_CHECK			="Hierarchy Code is not Mandatory.Do you want to proceed ?";
	
var HIER_BLANK_CHECK = "Default Hierarchy Code Is Mandatory";
var LOC_BLANK_CHECK = "Default Location Code Is Mandatory";
var GREART_AMOUNT = "To Amount Should Be Greater Than The From Amount ";
var AMOUNT_RANGE = "Amount Should Not Be In The Range Of Previous Slab(s)";
var DEFAULT_HIR_CODE = "Default Hierarchy Code Is Already Available";
var DEFAULT_DIV_CODE = "Default Division Code Is Already Available";
var DEFAULT_LOC_CODE = "Default Location Code Is Already Available";
var TOT_PER_EXCEED = "Total Percentage Should Not Be Exceed 100 ";
var TOT_PER_NOT_EXCEED = "Total Percentage Should Be Equal To 100";
var TO_AMOUNT_GREATER = "To Amount Can Only Be Greater Than Current Slab's From Amount";


var FIN_LESS_CHECK = "Should be Less than Current Financial Year";
var FWD_SCH_NOTSAME_BRN = "Forwarding Schedule Doesn't Belong To This Branch";
var CORR_SCH_NO_FUND = "Corr. Schedule Cannot Be Realised Without Funding";
var PART_ONLY_ALLOWED = "Only Part Realisation Allowed";
var AMT_REAL_CANNOT_EXCEED = "Amount Realised Cannot Exceed Balance Amount Available For  Realisation Marking";
var DATE_GREATER = "Must Be Greater Than  ";
var DATE_LESSER = "Must Be Lesser Than Or Equal  to  ";
var DATE_GREATER_EQUAL = "Must Be Greater Than Or Equal to  ";
var ALL_INS_REAL = "Instrument(s) Already realised / returned";
var CANNOT_PROC_WITH_PART_REAL = "Cannot Proceed With Part Realisation Of Schedule  Sent To Our Branch";
var REALAMT_CANNOT_SCH = "Realised Amount Cannot Be greater than Schedule Amount";
var ENTER_VALID = " Enter a Valid  ";
var STALE_INS = "  This Is A Stale Instrument  ";
var SHOULD_NOT_GRT = "  Should Not be Greater Than ";
var NOT_MORE_ROW = "More Than 10 records is Not Accepted And Amount Should Be Equal To  Amount Realized";
var AMT_CANNOT_EXCEED_REALAMT = "Total Amount Cannot Exceed Amount Realized";
var DUPLICATE_DATA_NOT_ALLW = " Row Should Not Be Duplicated";
var AMT_NTLESS_HGVAL = "Realised Amount Cannot Be Less Than High Value Amount";
var CRDT_GT_CLGDT = "Credit Date Must Not be Greater than 7 Days From Clearing Date";
var AMT_EQUAL_REALAMT = "Total Amount Should Be Equal To Amount Realized";
var GIVEN_AMT_EXCEED_REAL_AMT = "Given Amount Is Exceeding The Available Amount for  Realisation";
	var REALAMT_NOTEQUAL_SCHAMT=" Amount Realized Should Not Be Equal To Schedule Amount   ";
	var FIN_YEAR_CHECK= " should be either Current or Previous Financial Year  ";
	var AMT_LESS_SCHAMT="Amount realized should be equal to the schedule amount " ;
	var AMT_GRT_TOTREAL="Total Instrument Amount should not be greater than Amount Realized";
	var AMT_LESS_TOTREAL="Total Instrument Amount should not be less than Amount Realized";
	var MAND_FOR_REAL="Select Flag is mandatory for the Realisation type";
	var ROW_GREAT="Row greater than 10";
var EFF_AND_APPT_DATE = "should be greater than or equal Date of Appointment";
var CALL_UPTO_CHECK = "Should be Greater Than From Date"; 
var SLAB_NOT_DEFINE = "Default Slab Not Define";   	
var HOUR_GREATER = "Should not be Greater than 23";
var MIN_GREATER  = "Should not be Greater than 59";	

   	var DATE_LESSCBD_CHECK_MDRPBOX  = "Start Date Should Be <= Current Business Date";
   	var LESS_DATE_MDRPBOX = "Closed Date Should not be Lesser than Start Date";
   	var GREATER_DATE_MDRPBOX="Closed Date Should be Greater than Current Business Date ";
   	var DATE_FORMAT_CHECK_MDRPBOX="Start Date Must be in DD-MM-YYYY Format";
   	var DATE_FORMAT_CHECK_END_MDRPBOX="Closed Date Must be in DD-MM-YYYY Format";
    
     var SHLD_NOT_LESS="Should not be Lesser than";

	var ENTRD_VAL_SHLD_BE_NUMBER ="Entered Value Should Be a Number";
    var FOR_ALL_STD_INS = "For All Standing Instruction Sl";
var ALL_CURRENCY="For All Currency";
var DUP_NOT_ALLOW="Account Details Should Not Be Duplicated ";

var RECOVER_AMOUNT ="TotalRecoveredAmount Should Be Less Than Or Equal To PaidAmount";

LESS_THAN_OR_EQUAL_TO = "Should be Less than or Equal to Current Business Date";

var MOD_NOT_ALLOWED = "Modification not Allowed for Grid Row";
	var GREAT_THAN_CBD_FOR_PRE = " Should Be Greater Than Or Equal To Current Business Date For Preponement ";	
	var REC_BANK_CHK="Not Equal To Received";

var LESS_EQUAL = "Should be Less than or Equal to Current Business Date";
var NO_DATA = "No Data to Load in grid";

var AMT_RTND_EQUAL_TO_DEPSLIPAMT = " Should be Equals to Deposit Slip Amount";

var ALL_BILL_TYPES="For All Bill Types";

    var ALL_BIL_TYPE="For All Bill Type Codes";
var SOD_SUCCESSFUL = "SOD DONE SUCCESSFULLY";
var POOL_PROC_SUCCESS = "Pooling Process Generated Successfully";


var NO_USERS_LOGGED = "No Users Logged In";
var No_BRANCH_SIGNED_OUT="No Branches to be Signed Out";
var COLL_VOUCH_SUCCESS = "Collection Voucher Generated Successfully";

var REPORT_SUCESS="Forward Scheduling Printing Report Generated Successfully";
var NOT_A_FINANCIAL_YEAR = "This Forwarding Schedule Year Is Not A Financial Year"; 
var BANKCODECHECK = "Instrument Code and Deposit Code Should be Same For Home Clearing";
var RBISBI_CLEARINGDATE =" Credit Our A/c With RBI/SBI After No Of Days should be < No Of Days As Per Clearing Zone Master";

var NOT_VALID_TIME="  Not A Valid Time ";

var REPORT_SUCCESS = "Report Generated Successfully";
var REPORT_FAILURE = "No valid Records To Print";
var EMAIL_REPORT_SUCCESS="Email Report Generated successfully";

var DR_LOC_INSLOC_EQL_ERR=	" Drawn On Location Should Be Same As Deposit Location Code ";
var INSTNO_REGULARIZED = "Instrument No Already Regularized Can't Mark Return";
var SLIPNO_REGULARIZED = "Already Regularized Can't Mark Return";
var NOINST_UCCO_ENTRY = "Instrument Number Not Presented For UCCO/Clearing";

 	var ALL_INST_NO="For All Instrument Nos.";
 
var AMT_INST_SHOULD_BE_EQUAL = "And  Instrument should be equal";
var INST_RTND_EQUAL_TO_DEPSLIPINSTS = " Should be Equal to Or Less than Total No.Of Insts in Dep.Slip";
 

var NOT_VALID_IP="  IP Address Format Should Be Like (eg : 255.255.255.255) ";


var MINIMUM = " should have Minimum ";
var CHARACTERS = " Characters";
var NUMBERS = " Numbers";
var ALPHABETS = " Alphabets";
var PARAM_NOT_DEFINED = "System Parameters Are Not Specified, Cannot Proceed Further";
var NEGATIVE_CHECK = "Negative value not allowed";
var RETYPE_PW = "Please Retype The Password Correctly";
var SAMEUSR_NOT_MOD = "Same User Cannot be Modify ";
var SUBMNU_NOTSAME_MNU = "Menu should not be same as Sub Menu";
var NO_VALID_RECORDS = "No valid records";
var NOT_VALID_OPTION = "Not a Valid Option";
var ROLW_CODE_CHECK = "New Role Code Cannot Be Equal To Existing Role Code";
var SAME_USER_NOT_ALLOWED = " User Can't Transfer His/Her Own-Id";
var EQUAL_TO_CBD = " Should be Equals to Current Business Date";
var TRANSFER_FROM_BRANCH = " Transfer From Branch ";
var SHOULD_NOT_SAME = "Should not be Same";
var ADMUNI_PAR_UNI_SHOULD_NOT_BE_EQUAL = "Admin Unit Type And Parent Admin Unit Type Are Same, Cannot Proceed";
var SAME_USER_NOT_ALLOWED_TO_DISABLE = " User Can't Disable His/Her Own-Id";
var USER_CHECK = "Same User Can not block";
var NUMERIC_NOT_GREATER_THAN = " Numeric Digits  Should not be > Minimum Length of UserID ";
var EQUAL_PASS_CHECK = "Should Not Be Equal";
var NEW_PASS_CHECK = "There Should Be Minimum Of";
var ALPHA_PASS_CHECK = "Alphabets In The Password";
var MIN_NUM_PASS = "Numbers In The Password";
var CHAR_PASS_CHECK = "Characters In The Password";
var FINANCE_YEAR_CHECK = "Financial Year Should be Equal to";
var FROM_DATE_CHECK = "From Date Should be Greater than or Equal to Current Business Date";
var DATE_CHECK_FROM = "From Date Should be Greater than or Equal to ";
var DATE_CHECK_UPTO = "From Date Should be Lesser than or Equal to ";
var DATE_CHECK_UPTO_GTE = "Upto Date Should be Greater than or Equal to ";
var DATE_CHECK_UPTO_LTE = "Upto Date Should be Lesser than or Equal to ";
var NOT_VALID_IP = "InValid Ip Address";
var SAME_USER_CAN_NOT_MOD = "Same User Can Not Modify";
var NUMERIC_ALPHA ="Both Numeric Digits and Alpha characters Should Not Be Zero";
var SUMOFNUMERIC = "Sum of Numeric Digits and Alpha characters Should be <= Minimum Length of UserId";
var GR3LESS9     = "Should be >= 3 and <= 9";
var NUMERICPASS  = "Numeric Digits  Should not be > Minimum Length of password";
var SUMOFPASS = "Sum of Numeric Digits and Alpha characters Should not be <= Minimum Length of password";
var GTTHAN30   = "Should be >= 30";
var GTTHAN1200 = "Should be >= 1200";

   var UCC_REALISA_QUERY="UCC Realisation Query";
var DATE_VALID_CHECK = " Enter A Valid Date "; 
var DATa_VALID_CHECK = "Enter A Valid Data Format ";
var USER_ROLE_SUPERVI_ROLE_CODE = "User Role Code and Supervisory Role Code Should not be Same";

var NO_OF_ADDINFO = "No.of Rows should be equal to the No. Of Information Required";
var DISB_ONLY		="Should be Date Of Funding(Disbursement Only) for Payment Products" ;

var LEAFS_EQUL_BOOKS="Number of Leafs Should be Equally Divided into No. Of Books";

var BANKCODECHECKMICR = "Instrument Code(Micr) and Deposit Code(Micr) Should be Same For Home Clearing";

var DEPOSIT_REGULARISED = "Deposit Slip Has Already Been Regularised...Return Cannot Be Marked For This Instrument";
var REC_POOLED = "This Return Instrument Is Already Pooled.Cannot Be Modified Or Deleted.";
var REC_RETURNED = "Return Has Already Been Marked For This Instrument";
var AMOUNT_NOT_TALLY = "Returned Amount Should Match With Instrument Amount...";

var GREATER_CHECK_ADVICE = "Should Not Be Greater Than Current Business Date";
var LESSER_CHECK_ADVICE ="Should Not Be Lesser EntryDate ";



var BULK_DD_MIN_AMT = 999999.99;
var RTGS_MIN_AMT = 100000.00;
var BULK_DD_LESS_AMT = "DD Amount Should Be <= [" + BULK_DD_MIN_AMT + "]"
var RTGS_LESS_AMT = "RTGS Amount Should Be >= [" + RTGS_MIN_AMT + "]"


var LOGUSR_NOT_MOD_DEL="Login User Cannot Modify/Delete Supervisory Role User";

var OUR_BNK_CODE_ONLY_ALLOWED = " Should Be Our Code ";
 
 var  LESS_100 =" Should Be Less than or Equal to 100.00";

var SHD_LESS_VAL_DATE = " Should be Less Than or Equal to Value Date";
var SHD_GRT_FRM_DATE = " Should be Greater Than or Equal to From Date ";
var SHD_LESS_TENOR_DATE = " Should be Less Than or Equal to Tenor Due Date";
var SERIAL_SHD_NO = " Serial Number Should Be a Number";

var ALL_SUB_CUSTOMERS = " For All Sub Customers";
var ALL_CURR_CODE = " For All Currency Code";
var ALL_RECV_BRN =" For All Recv Branch";
var ALL_RECV_BNK =" For All Recv ";

var ALL_PAY_BNK = " For All Paying ";
var ALL_PAY_BRN = " For All Paying Branch";

var ASSIGN_TO_ALL_BRN = " Will be Assigned to HUB and All OB Branches,Do you Want to Continue ?";

var GR3LESS8     = "Should be >= 3 and <= 8"; 

var CUST_PROD_EXP = "Customer Product Combination has Already Expired";

var HIGH_VAL_CLEARING_NT_ALLOWED="High Value Clearing Not Allowed If Instrument Amt Lesser Than High Value Amount";
var TRANSFER_CLEARING_NT_ALLOWED ="Transfer Clearing Not Allowed If Drawn On and Received Code are Not Same";
var TRANSFER_CLEARING_REQUIRED="Transfer Clearing is Required If Drawn On and Session Code are Same";

var USER_SHOULD_NOT_BE_SAME = "Password Reset Cannot Be Done for the Same UserId";

var UPLOAD_WITH_ERR=" File Uploaded With Errors ";
var UPLOAD_WITHOUT_ERR=" File Uploaded ";

var UPLOADUSER_NOT_CONFIRM="Upload User Cannot Confirm";
var AEL_ACC_REJ = "Select Accepted/Rejected";  

var REQUIRED_CHECK1 = "Required Should be N"; 
var VIEW_NOT_ALLOWED = " View Not Allowed ";
var HEADER_REQUIRED = "Either Header required or Details Record Required or both Should be selected";

var FROM_SCH_YR_CHECK="From Schedule Year Should Not Be Blank";
var FROM_SCH_NO_CHECK="From Schedule Number Should Not Be Blank";
var TO_SCH_YR_CHECK="To Schedule Year Should Not Be Blank";
var TO_SCH_NO_CHECK="To Schedule Number Should Not Be Blank";

var SELECT_ONE_OPT ="Select Either Accepted or Rejected";

var ALL_GROUPS = " For All Groups";
var ALL_SUB_CUST = " For All Sub Customer";
var ALL_DIV = " For All Divisions";

var INST_DATE_ERR4 =" Should Not Be More Than 	";
var FROM ="Days From";

var RBI_SBI_CLGAC_DATE = "Credit Date with RBI/SBI should be <= Credit Date with RBI/SBI ";

var AMT_GREAT_TOTREAL = "Total Instrument Amount should not be greater than Amount Realized";

 var CMD_RESET    = "Press Enter To Reset";

var NO_OF_INFO_SHOULD_BE_EQUAL="No.Of Info Given in the Grid Should be Equals to ";

var ATLEAST_ONE_SHARE 	= "Enter Atleast One Of The Shares Mentioned";
var SHD_EQUAL_100 	  	= "Sum Of All Shares Should Be Equal to 100%";
var LSR_OR_EQL			= "Should Be Lesser Than Or Equal To 100%";

var LOCWISE_OR_VOLWISE 	= "Select Either Location Wise Charges Required Or Volume Wise Charges Required";
var ALPHANUMERIC_CHECK  = "Should be alphaNumeric";