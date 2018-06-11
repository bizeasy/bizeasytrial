import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.io.File;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;





String xlsFileName = parameters.get("_productFile_fileName");
String productFile = parameters.get("productFile");
String xlsFilePath = parameters.get("productFilePath");

parameters.xlsFileName=xlsFileName ;
parameters.xlsFilePath=xlsFilePath ;
println ("xlsFilePath======="+xlsFilePath);
println ("parameters======="+parameters.keySet());
// Now we are getting sheets and names
session.setAttribute("uploadedXLSFile",xlsFileName);;
