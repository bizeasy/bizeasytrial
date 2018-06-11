package com.palredErp.util;

import java.util.List;
import java.util.Map;

import org.apache.ofbiz.base.util.StringUtil;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.util.EntityUtil;

import java.util.LinkedList;
import java.util.HashMap;

import jxl.Cell;
import jxl.Sheet;

public class ProductLoaderHelper {

    public static final String module = ProductLoaderHelper.class.getName();
    
    public static List getDataList(List dataRows) 
    {
        List dataList = new LinkedList();
        for (int i=0 ; i < dataRows.size() ; i++) 
        {
            Map mRow = (Map)dataRows.get(i);
            dataList.add(mRow);
        }
        return dataList;
    }
    
    public static List buildDataRows(List headerCols,Sheet s) 
    {
		List dataRows = new LinkedList();
		try 
		{
            for (int rowCount = 1 ; rowCount < s.getRows() ; rowCount++) 
            {
            	Cell[] row = s.getRow(rowCount);
                if (row.length > 0) 
                {
            	    Map mRows = new HashMap();
                    for (int colCount = 0; colCount < headerCols.size(); colCount++) 
                    {
                	    String colContent=null;
                        try 
                        {
                		    colContent=row[colCount].getContents();
                	    }
                	    catch (Exception e) 
                	    {
                		    colContent="";
                	    }
                        mRows.put(headerCols.get(colCount),StringUtil.replaceString(colContent,"\"","'"));
                    }
                    dataRows.add(mRows);
                }
            }
    	}
      	catch (Exception e) {}
      	return dataRows;
    }
    
    
    static Map featureTypeIdMap = new HashMap();
    public static Map buildFeatureMap(Map featureTypeMap,String parseFeatureType, Delegator delegator) 
    {
    	if (UtilValidate.isNotEmpty(parseFeatureType))
    	{
        	int iFeatIdx = parseFeatureType.indexOf(':');
        	if (iFeatIdx > -1)
        	{
            	String featureType = parseFeatureType.substring(0,iFeatIdx).trim();
            	String sFeatures = parseFeatureType.substring(iFeatIdx +1);
                String[] featureTokens = sFeatures.split(",");
            	Map mFeatureMap = new HashMap();
                for (int f=0;f < featureTokens.length;f++)
                {
                	String featureId = ""; 
                	try 
                	{
                		String featureTypeKey = StringUtil.removeSpaces(featureType).toUpperCase()+"~"+featureTokens[f].trim();
                		if(featureTypeIdMap.containsKey(featureTypeKey))
                		{
                			featureId = (String) featureTypeIdMap.get(featureTypeKey); 
                		}
                		else
                		{
                			List productFeatureList = delegator.findByAnd("ProductFeature", UtilMisc.toMap("productFeatureTypeId", StringUtil.removeSpaces(featureType).toUpperCase(), "productFeatureCategoryId", StringUtil.removeSpaces(featureType).toUpperCase(), "description", featureTokens[f].trim()),null,false);
                			if(UtilValidate.isNotEmpty(productFeatureList))
                			{
                				GenericValue productFeature = EntityUtil.getFirst(productFeatureList);
        						featureId = productFeature.getString("productFeatureId");
                			}
                			else
                			{
                				featureId = delegator.getNextSeqId("ProductFeature");
                			}
                		}
                		featureTypeIdMap.put(featureTypeKey, featureId);
					} catch (GenericEntityException e) 
					{
						e.printStackTrace();
					}
                	mFeatureMap.put(""+featureId,""+featureTokens[f].trim());
                }
        		featureTypeMap.put(featureType, mFeatureMap);
        	}
    		
    	}
    	return featureTypeMap;
    	    	
        }
    }

    



