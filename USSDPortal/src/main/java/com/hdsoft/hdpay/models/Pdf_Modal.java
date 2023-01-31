package com.hdsoft.hdpay.models;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.xhtmlrenderer.pdf.ITextRenderer;
import com.google.gson.JsonObject;
import com.hdsoft.common.File_handling;
import com.zaxxer.hikari.HikariDataSource;

@Component
public class Pdf_Modal 
{
	public JdbcTemplate Jdbctemplate;
	
	@Autowired
	public void setJdbctemplate(HikariDataSource Datasource) 
	{
		Jdbctemplate = new JdbcTemplate(Datasource);
	}
	
	public JsonObject Download_Invoice()
	{   	
		 JsonObject details = new JsonObject();
		 
		 try
		 { 
			// String sql = "select t1.Id, t1.Order_No, t1.User_Id, t1.User_Email, t1.Requester_Name, t1.Purpose_of_Wish, t1.To_Whom, t1.His_Her_Name, t1.Date_of_occasion, t1.Message, t1.Celebrity_Name, t1.Celebrity_Image, t1.Requested_at, t2.Order_Amount, t2.GST_Amount, t2.TXN_Amount, t2.TXN_Date, t2.PayuMoneyId, t2.Payment_Mode, t2.Gateway_Name, t2.TXN_Id, t2.Status, t3.Media from wishes t1, payment t2, profiles t3 where t1.Order_No = ? AND t1.Order_No= t2.Order_Id AND t3.Id = t1.Celebrity_Id";

			 //Invoice bill = JdbcTemplate.queryForObject(sql, new Object[] {Order_Id}, new InvoiceMapper());
			 
			 String content = getcontent();
			 
			 File_handling f = new File_handling();
			 
			 final String outputFile =  f.Get_temp_path() + "sample" +".pdf";
			 
			 htmlToPdf(content, outputFile);
			 
			 //boolean Pdf_Generated = htmlToPdf(content, outputFile);
				
			 /*if(Pdf_Generated) 
			 {
				 File_handling fi = new File_handling();
				 
				 String Invoice_path = ""; //fi.FTP_File_Upload_Using_Domain(new File(outputFile), "Invoice");
				 
				 details.addProperty("Invoice_link", Pdf_Generated ? Invoice_path : ""); 				 
			 }
				*/
			 
			 details.addProperty("Result",  "Success");
			 details.addProperty("Message", "Invoice Downloaded Successfully");
		 }
		 catch(Exception e)
		 {
			 details.addProperty("Result", "Failed");
			 details.addProperty("Message", e.getMessage());
		 }
			  
		 return details;
	}
	
	public String getcontent()
	{
		String Printed_on = "10-Dec-2021";
		String Printed_by = "10-Dec-2021";
		String Received_From = "RIM ZONE TRADING";			
		String Receipt = "LOCE007032021DAR";
		String Address = "-";
		String Payment_Date= "LOCE007032021DAR";
		String Tel_Fax	= "-";
		String Client_Id= "LOCE007032021DAR";
		String Account_No= "9943126789";
		String Invoice = "LOCE007032021DAR";
		String Bank = "EXIM BANK (T) Ltd";
		String Payment_Mode = "LOCE007032021DAR";
		String Invoice_date = "10-Dec-2021";
		String Exim_Receipt_No	= "10-Dec-2021";
		String Amount_Paid	= "10-Dec-2021";
		String Currency = "10-Dec-2021";
		String Invoice_Balance_Amount = "10-Dec-2021";
		String Narration= "10-Dec-2021";
		String Beneficary = "GEPG";
		
		//Company_Name = StringEscapeUtils.escapeHtml4(Company_Name);
		//UAM_No = StringEscapeUtils.escapeHtml4(UAM_No);
		//GSTIN = StringEscapeUtils.escapeHtml4(GSTIN);
		//Company_Address = StringEscapeUtils.escapeHtml4(Company_Address);

		return (
				
				 "<!DOCTYPE html [ "+
						 "    <!ENTITY nbsp \"&#160;\"> "+
						 "    ]>  "+
						"<html>"+
						"<head>"+
						    "<meta charset=\"UTF-8\"></meta>"+
						    "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"></meta>"+
						    "<title>EB INVOICE</title>"+
						    "<style>"+
						        "body{"+
						            "background-color: #F6F6F6; "+
									"margin: 30px;"+
									"padding-bottom: 50px;	"+
						        "}"+
						        "h1,h2,h3,h4,h5,h6{"+
						            "margin: 0;"+
						            "padding: 0;"+
						        "}"+
						        ".container{"+
						            "width: 80%;"+
						            "margin-right: auto;"+
						            "margin-left: auto;"+
									"border: 2px solid;"+
									"padding-bottom: 60px;"+
									"padding-top: 30px;"+
									"padding-left:50px;"+
									"padding-right: 50px;"+
						        "}"+
						        ".brand-section{"+					           
						          // "padding: 10px 40px;"+
								   "border-bottom: solid;"+								   
						        "}"+
						        ".logo{"+
						            "width: 50%;"+
						        "}"+

						        ".row{"+
						            "display: flex;"+
						            "flex-wrap: wrap;"+
						        "}"+
						        ".col-4{"+
						            "width: 30%;"+
						        "}"+
						         ".col-sm-6 {\r\n" + 
						         "    width: 50%;\r\n" + 
						         "}"+
						        ".text-white{"+
						            "color: #fff;"+
						        "}"+
						        ".company-details{"+
						            "float: right;"+
						            "text-align: left;"+
									"padding-left:20px;"+
						        "}"+
						        ".heading{"+
						            "font-size: 18px;"+
						            "margin-bottom: 08px;"+
									"padding-right:180px;"+
						        "}"+
						        ".sub-heading{"+
						            "color: #262626;"+
						            "margin-bottom: 05px;"+
						        "}"+
						        "table{"+
						            "background-color: #fff;"+
						            "width: 100%;"+
									"text-align: left;"+						            
						        "}"+
						        ".table-bordered{"+
						            "box-shadow: 0px 0px 5px 0.5px gray;"+
						        "}"+
						        ".text-right{"+
						            "text-align: end;"+
						        "}"+
						        ".w-20{"+
						            "width: 20%;"+
						        "}"+
								".body-section {"+
								    "margin-top: 18px;"+
								"}"+
								".logo {"+
									 "background-image:url('data:image/png;base64,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');"+
									 "width: 100%;"+
									 "height: 40px;"+
									 "background-repeat: no-repeat;"+
								"}"+	
								"@page { size: A4 landscape;}"+
						    "</style>"+
						"</head>"+

						"<body>"+
						    "<div class=\"container\">"+
						        "<div class=\"brand-section\">"+
						            "<div class=\"row\">"+
						                "<div class=\"col-4\">"+
						                    "<h1>"+"<div class=\"logo\">"+"</div>"+"</h1>"+
						                "</div>"+
										"<div class=\"col-4\">"+
						                    "<h2>Invoice Payment Receipt</h2>"+
						                "</div>"+
						               /* "<div class=\"col-4\">"+
						                    "<div class=\"company-details\">"+
						                        "<p >"+"<span style=\"font-weight: bold;\">Printed on:</span>"+Printed_on+"</p>"+
						                        "<p >"+"<span style=\"font-weight: bold;\">Printed by:</span>"+ Printed_by+"</p>"+
						                    "</div>"+
						                "</div>"+ */
						            "</div>"+
						        "</div>"+

						        "<div class=\"body-section\">"+
						            "<div class=\"row mb\">"+
						                "<div class=\"col-sm-6\">"+
											"<div class=\"row\">"+
												"<div class=\"col-sm-6\">"+
													"<label for=\"from\" style=\"font-size:18px; font-weight:bold; \">Received From</label>"+
												"</div>"+
												"<div class=\"col-sm-6\">"+
													"<label for=\"from\" class=\"heading\" style=\"padding-left:20px;\">"+Received_From+"</label>"+
												"</div>"+
											"</div>"+
										"</div>"+
						                "<div class=\"col-sm-6\">"+
						                    "<div class=\"row\">"+
												"<div class=\"col-sm-6\">"+
													"<label for=\"from\" style=\"font-size:18px; font-weight:bold; padding-left:100px;\">Receipt</label>"+
												"</div>"+
												"<div class=\"col-sm-6\">"+
													"<label for=\"from\" class=\"sub-heading\" style=\"padding-left:70px;\">"+Receipt+"</label>"+
												"</div>"+
											"</div>"+
						                "</div>"+
						            "</div>"+
									"<div class=\"row mb\">"+
						                "<div class=\"col-sm-6\">"+
											"<div class=\"row\">"+
												"<div class=\"col-sm-6\">"+
													"<label for=\"from\" style=\"font-size:18px; font-weight:bold; \">Address</label>"+
												"</div>"+
												"<div class=\"col-sm-6\">"+
													"<label for=\"from\" class=\"heading\" style=\"padding-left:20px;\">"+Address+"</label>"+
												"</div>"+
											"</div>"+
										"</div>"+
						                "<div class=\"col-sm-6\">"+
						                    "<div class=\"row\">"+
												"<div class=\"col-sm-6\">"+
													"<label for=\"from\" style=\"font-size:18px; font-weight:bold; padding-left:325px;\">Payment Date</label>"+
												"</div>"+
												"<div class=\"col-sm-6\">"+
													"<label for=\"from\" class=\"sub-heading\" style=\"padding-left:20px;\">"+Payment_Date+"</label>"+
												"</div>"+
											"</div>"+
						                "</div>"+
						            "</div>"+
									"<div class=\"row mb\">"+
						                "<div class=\"col-sm-6\">"+
											"<div class=\"row\">"+
												"<div class=\"col-sm-6\">"+
													"<label for=\"from\" style=\"font-size:18px; font-weight:bold; \">Tel/Fax</label>"+
												"</div>"+
												"<div class=\"col-sm-6\">"+
													"<label for=\"from\" class=\"heading\" style=\"padding-left:20px;\">"+Tel_Fax+"</label>"+
												"</div>"+
											"</div>"+
										"</div>"+
						                "<div class=\"col-sm-6\">"+
						                    "<div class=\"row\">"+
												"<div class=\"col-sm-6\">"+
													"<label for=\"from\" style=\"font-size:18px; font-weight:bold; padding-left:330px;\">Client Id</label>"+
												"</div>"+
												"<div class=\"col-sm-6\">"+
													"<label for=\"from\" class=\"sub-heading\" style=\"padding-left:57px;\">"+Client_Id+"</label>"+
												"</div>"+
											"</div>"+
						                "</div>"+
						            "</div>"+
									"<div class=\"row mb\">"+
						                "<div class=\"col-sm-6\">"+
											"<div class=\"row\">"+
												"<div class=\"col-sm-6\">"+
													"<label for=\"from\" style=\"font-size:18px; font-weight:bold; \">Account No</label>"+
												"</div>"+
												"<div class=\"col-sm-6\">"+
													"<label for=\"from\" class=\"heading\" style=\"padding-left:20px;\">"+Account_No+"</label>"+
												"</div>"+
											"</div>"+
										"</div>"+
						                "<div class=\"col-sm-6\">"+
						                    "<div class=\"row\">"+
												"<div class=\"col-sm-6\">"+
													"<label for=\"from\" style=\"font-size:18px; font-weight:bold; padding-left:300px;\">Invoice</label>"+
												"</div>"+
												"<div class=\"col-sm-6\">"+
													"<label for=\"from\" class=\"sub-heading\" style=\"padding-left:73px;\">"+Invoice+"</label>"+
												"</div>"+
											"</div>"+
						                "</div>"+
						            "</div>"+
									"<div class=\"row mb\">"+
						                "<div class=\"col-sm-6\">"+
											"<div class=\"row\">"+
												"<div class=\"col-sm-6\">"+
													"<label for=\"from\" style=\"font-size:18px; font-weight:bold; \">Bank</label>"+
												"</div>"+
												"<div class=\"col-sm-6\">"+
													"<label for=\"from\" class=\"heading\" style=\"padding-left:100px;\">"+Bank+"</label>"+
												"</div>"+
											"</div>"+
										"</div>"+
						                "<div class=\"col-sm-6\">"+
						                    "<div class=\"row\">"+
												"<div class=\"col-sm-6\">"+
													"<label for=\"from\" style=\"font-size:18px; font-weight:bold; padding-left:110px;\">Payment Mode</label>"+
												"</div>"+
												"<div class=\"col-sm-6\">"+
													"<label for=\"from\" class=\"sub-heading\" style=\"padding-left:18px;\">"+Payment_Mode+"</label>"+
												"</div>"+
											"</div>"+
						                "</div>"+
						            "</div>"+
						        "</div>"+
								
						        "<div class=\"body-section\">"+
						            "<h4>Beneficary : "+Beneficary+"</h4>"+
						            "<br></br>"+
						            "<table style=\"border: 2px solid; padding:30px;\">"+
						                "<th style=\"font-size: 20px;\">Invoice Summary</th>"+
						                    "<tr>"+
						                        "<th>Date</th>"+
						                        "<th>Exim Receipt No</th>"+
						                        "<th>Amount Paid</th>"+
						                        "<th>Currency</th>"+
												"<th>Invoice Balance Amount</th>"+
						                    "</tr>"+
						                    "<tr>"+
						                        "<td>"+Invoice_date+"</td>"+
						                        "<td>"+Exim_Receipt_No+"</td>"+
						                        "<td>"+Amount_Paid+"</td>"+
						                        "<td>"+Currency+"</td>"+
												"<td>"+Invoice_Balance_Amount+"</td>"+
						                    "</tr>"+
									"</table>"+

						        "<div class=\"body-section\">"+
						            "<p>Brief Narration: "+Narration+"</p>"+
									"<p style=\"float:left; padding-top: 10px;\">Customer Signature</p>"+
									"<p style=\"float:right; padding-top: 10px;\">Cashier/User Signature</p>"+
						        "</div>"+      
						    "</div>"+      
						  "</div>"+   
					  "</body>"+
				"</html>"			
		);
	}
	
	public boolean generatePDF(String Input_String, String outputPdfPath)
	{
		boolean Status = false;
		
		try 
		{	
			OutputStream out = new FileOutputStream(outputPdfPath);
	
			ITextRenderer renderer = new ITextRenderer();
	
			renderer.setDocumentFromString(Input_String); 
			renderer.layout();
			renderer.createPDF(out);
			out.close();	        
			
			Status = true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return Status;
	}
	
	public void htmlToPdf(String xhtml, String outFileName) throws IOException 
	{
		try 
		{
			File output = new File(outFileName);
			
		    ITextRenderer iTextRenderer = new ITextRenderer();
		    
		    iTextRenderer.setDocumentFromString(xhtml);
		    iTextRenderer.layout();
		    
		    OutputStream os = new FileOutputStream(output);
		    iTextRenderer.createPDF(os);
		    os.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
	
    }
}
